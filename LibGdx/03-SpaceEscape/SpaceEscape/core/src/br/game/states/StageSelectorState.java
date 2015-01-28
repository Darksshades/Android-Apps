package br.game.states;

import static br.game.handlers.B2DVars.PPM;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;

import br.game.GdxGame;
import br.game.entities.B2DSprite;
import br.game.handlers.B2DVars;
import br.game.handlers.GameInput;
import br.game.handlers.GameStateManager;
import br.game.handlers.MyContactLisntenner;
import br.game.states.ui.PlanetInfo;

public class StageSelectorState extends GameState {

	private Texture stars;
	private Texture stars1;
	private Texture stars2;
	private Texture stars3;
	private OrthographicCamera b2dCam;
	private float px1;
	private float px2;
	private B2DSprite player;
	private World world;
	private Box2DDebugRenderer b2dr = new Box2DDebugRenderer();
	private BitmapFont font = new BitmapFont();
	private boolean isDrawDebugRender = true;
	
	private ArrayList<PlanetInfo> planetsinfo;
	
	boolean selectedPlanet = false;

	public StageSelectorState(GameStateManager gsm) {
		super(gsm);

		b2dCam = new OrthographicCamera();
		b2dCam.setToOrtho(false, GdxGame.V_WIDTH / PPM, GdxGame.V_HEIGHT / PPM);
		
		stars = GdxGame.res.getTexture("stars");
		stars1 = GdxGame.res.getTexture("stars");
		stars2 = GdxGame.res.getTexture("stars2");
		stars3 = GdxGame.res.getTexture("stars2");
		
		world = new World(new Vector2(0,0), true);
		world.setContactListener(new MyContactLisntenner());
		
		planetsinfo = new ArrayList<PlanetInfo>();
		
		createPlayer();
		
		Body body = createPlanetBody(new Vector2(100/B2DVars.PPM, 100/B2DVars.PPM));
		PlanetInfo i = new PlanetInfo(body, 0.5f, "Stage 1", "Let's Battle", "map1.stg");
		planetsinfo.add(i);
		
		body = createPlanetBody(new Vector2(40/B2DVars.PPM, 190/B2DVars.PPM));
		i = new PlanetInfo(body, 0.2f, "Stage 2", "Oh!", "map2.stg");
		//i.setCanPlay(false);
		planetsinfo.add(i);
		
		body = createPlanetBody(new Vector2(300/B2DVars.PPM, 140/B2DVars.PPM));
		i = new PlanetInfo(body, 0.8f, "Stage 3", "Oh no!", "map3.stg");
		i.setCanPlay(false);
		planetsinfo.add(i);
		
		createWalls();
		
	}
	
	private void createPlayer() {
		BodyDef bdef = new BodyDef();
		Body body;
		CircleShape shape = new CircleShape();
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.friction = 0;
		// create player
		bdef.position.set((20+65)/ PPM, GdxGame.V_HEIGHT/2.f/ PPM);
		bdef.type = BodyType.DynamicBody;
		body = world.createBody(bdef);
		
		shape.setRadius(13/PPM);
		//shape.setAsBox(18.f / PPM, 13 / PPM);
		fdef.shape = shape;
		body.createFixture(fdef);
		
		player = new B2DSprite(body);
		
		Texture tex = GdxGame.res.getTexture("space_ship");
		TextureRegion[][] reg = TextureRegion.split(tex, 36, 25);
		
		player.setAnimation(reg, 0.47f);
		
	}
	
	private void createWalls() {
		BodyDef bdef = new BodyDef();
		Body wall;
		ChainShape shape = new ChainShape();
		Vector2[] v = new Vector2[5];
		v[0] = new Vector2( 0, 0 );
		v[1] = new Vector2( GdxGame.V_WIDTH/PPM, 0 );
		v[2] = new Vector2( GdxGame.V_WIDTH/PPM, GdxGame.V_HEIGHT/PPM );
		v[3] = new Vector2( 0, GdxGame.V_HEIGHT/PPM );
		v[4] = new Vector2( 0, 0 );
		
		shape.createChain(v);
		
		FixtureDef fdef = new FixtureDef();
		
		bdef.type = BodyType.StaticBody;
		fdef.shape = shape;
		
		wall = world.createBody(bdef);
		wall.createFixture(fdef);
	}

	private Body createPlanetBody(Vector2 position) {
		BodyDef bdef = new BodyDef();
		Body body;
		CircleShape shape = new CircleShape();
		shape.setRadius(2);
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.friction = 0.f;
		bdef.position.set(position);
		bdef.type = BodyType.KinematicBody;
		bdef.fixedRotation = false;
		bdef.position.set(position);
		body = world.createBody(bdef);
		body.createFixture(fdef);
		fdef.isSensor = true;
		shape.setRadius(4);
		body.createFixture(fdef);
		
		return body;
	}

	@Override
	public void handleInput() {
		if(Gdx.app.getType() == ApplicationType.Desktop) {
			if(GameInput.isDown(Input.Keys.ENTER) ) {
				isDrawDebugRender = !isDrawDebugRender;
			}
		}
		
		if(GameInput.isDown() ) {
			QueryCallback callback = new QueryCallback() {
				@Override
				public boolean reportFixture(Fixture fixture) {
					if(fixture.getUserData() == "center")
						checkPlay((PlanetInfo)fixture.getBody().getUserData());
					
					return true;
				}
			};
			world.QueryAABB(callback, GameInput.x[0]/PPM, GameInput.y[0]/PPM, 
					(GameInput.x[0]+1)/PPM, (GameInput.y[0]+1)/PPM);
			
			Vector2 force = new Vector2(GameInput.x[0] / PPM - player.getPosition().x, 
					GameInput.y[0] / PPM - player.getPosition().y);
			force.nor();
			
			player.getBody().setLinearVelocity(force.x*1.5f, force.y*1.5f);
		}
	}

	protected void checkPlay(PlanetInfo p) {
		if(p.isActive()) {
			gsm.message = p.loadstage;
			gsm.setState(GameStateManager.PLAY_STAGE);
		}
	}

	@Override
	public void update(float dt) {
		handleInput();
		
		world.step(dt, 8, 6);
		
		px1 -= 10*dt;
		px2 -= 1*dt;
		
		if(px1 <= -stars.getWidth()) {
			px1 = 0;
		}
		if(px2 <= -stars2.getWidth()) {
			px2 = 0;
		}
		
		player.update(dt);
		for( B2DSprite s : planetsinfo ) { s.update(dt); }
		
		
	}

	@Override
	public void render() {
		sb.setProjectionMatrix(cam.combined);
		sb.begin();
		sb.draw(stars, px1+stars.getWidth(), 0);
		sb.draw(stars1, px1, 0);
		sb.draw(stars2, px2, 0);
		sb.draw(stars3, px2+stars3.getWidth(), 0);		
		
		for( PlanetInfo s : planetsinfo ) { 
			s.render(sb, font);
		}
		player.render(sb);
		
		sb.end();
		
		if(isDrawDebugRender)
			b2dr.render(world, b2dCam.combined);
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
