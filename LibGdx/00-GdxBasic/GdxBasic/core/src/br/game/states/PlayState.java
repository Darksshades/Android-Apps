package br.game.states;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import br.game.GdxGame;
import static br.game.handlers.B2DVars.PPM;
import br.game.entities.B2DSprite;
import br.game.handlers.GameInput;
import br.game.handlers.GameStateManager;
import br.game.handlers.MyContactLisntenner;

public class PlayState extends GameState {
	
	private BitmapFont font = new BitmapFont();
	
	private World world;
	private Box2DDebugRenderer b2dr;
	private boolean isDrawDebugRender = true;
	
	private Texture background;
	
	private OrthographicCamera b2dCam;
	
	private B2DSprite player;

	public PlayState(GameStateManager gsm) {
		super(gsm);
		
		background = GdxGame.res.getTexture("background");
		
		world = new World( new Vector2(0.0f, 0.0f) , true);
		world.setContactListener(new MyContactLisntenner());
		b2dr  = new Box2DDebugRenderer();
		b2dr.setDrawContacts(true);
		
		// create platform
		BodyDef bdef = new BodyDef();
		bdef.position.set(160 / PPM, 120 / PPM);
		bdef.type = BodyType.StaticBody;
		
		Body body = world.createBody(bdef);
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(50 / PPM, 5 / PPM);
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		body.createFixture(fdef);
		
		// create player
		bdef.position.set(160 / PPM, 200 / PPM);
		bdef.type = BodyType.DynamicBody;
		body = world.createBody(bdef);
		
		shape.setAsBox(3.5f / PPM, 6 / PPM, new Vector2(0, -2 / PPM), 0);
		fdef.shape = shape;
		body.createFixture(fdef);
		TextureRegion[] frames = TextureRegion.split(GdxGame.res.getTexture("mage"), 16*1, 16*1)[0];
		player = new B2DSprite(body);
		player.setAnimation(frames, 0.2f);
		
		// create ball
		bdef.position.set(153 / PPM, 220 / PPM);
		body = world.createBody(bdef);
		CircleShape cshape = new CircleShape();
		cshape.setRadius(5 / PPM);
		fdef.shape = cshape;
		body.createFixture(fdef);
		
		b2dCam = new OrthographicCamera();
		b2dCam.setToOrtho(false, GdxGame.V_WIDTH / PPM, GdxGame.V_HEIGHT / PPM);
	}

	@Override
	public void handleInput() {
		if(Gdx.app.getType() == ApplicationType.Desktop) {
			if(GameInput.isDown(Input.Keys.ENTER) ) {
				isDrawDebugRender = !isDrawDebugRender;
			}
		}
		
	}

	@Override
	public void update(float dt) {
		// check input
		handleInput();
		
		world.step(dt, 6, 2);
	
//		Gdx.app.log(GdxGame.NAME, "Acelerometer X: " + Gdx.input.getAccelerometerX());
//		Gdx.app.log(GdxGame.NAME, "Acelerometer Y: " + Gdx.input.getAccelerometerY());
//		Gdx.app.log(GdxGame.NAME, "Acelerometer Z: " + Gdx.input.getAccelerometerZ());
		
		Vector2 gravity = new Vector2(Gdx.input.getAccelerometerY()*10.f / PPM, -Gdx.input.getAccelerometerX()*10.f / PPM);
		world.setGravity(gravity);
		world.setGravity(new Vector2(0,-9.8f));
		
		player.update(dt);
	}

	@Override
	public void render() {
		// background draw
		sb.setProjectionMatrix(cam.combined);
		sb.begin();
		sb.draw(background, 0, 0);
		sb.end();
		
		// HUD draw
		sb.setProjectionMatrix(hudCam.combined);
		sb.begin();
		String fontText = "Play State";
		font.draw(sb, fontText, GdxGame.V_WIDTH/2.f - font.getBounds(fontText).width/2, GdxGame.V_HEIGHT );
		sb.end();
		
		// Box2D draw
		sb.setProjectionMatrix(b2dCam.combined);
		player.render(sb);
		
		// Debug draw
		if(isDrawDebugRender)
			b2dr.render(world, b2dCam.combined);
		
	}

	@Override
	public void dispose() {
		
	}

}
