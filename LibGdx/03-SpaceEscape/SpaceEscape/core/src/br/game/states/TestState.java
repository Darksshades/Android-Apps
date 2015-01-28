package br.game.states;

import static br.game.handlers.B2DVars.PPM;

import java.util.ArrayList;

import br.game.GdxGame;
import br.game.entities.B2DSprite;
import br.game.entities.bh.BulletHellShot;
import br.game.handlers.GameStateManager;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class TestState extends GameState{

	World world = new World(new Vector2(0.f, 0.f), true);
	Box2DDebugRenderer b2dr = new Box2DDebugRenderer();
	
	ArrayList<B2DSprite> objects;
	private OrthographicCamera b2dCam;
	private boolean isDrawDebugRender = true;
	
	public TestState(GameStateManager gsm) {
		super(gsm);
		
		objects = new ArrayList<B2DSprite>();
		b2dCam = new OrthographicCamera();
		b2dCam.setToOrtho(false, GdxGame.V_WIDTH / PPM, GdxGame.V_HEIGHT / PPM);
		
		createShots(240/PPM, 120/PPM);
	}

	private void createShots(float px, float py) {
		BodyDef bdef = new BodyDef();
		Body body;
		CircleShape shape = new CircleShape();
		shape.setRadius(5/PPM);
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.isSensor = true;
		bdef.fixedRotation = false;
		
	
		bdef.position.set(px, py);
		bdef.type = BodyType.DynamicBody;
		body = world.createBody(bdef);
		fdef.shape = shape;
		body.createFixture(fdef);
		body.setActive(true);
		
		BulletHellShot shot = new BulletHellShot(body);
		objects.add(shot);
	}

	@Override
	public void handleInput() {
				
	}
	
	private float timer = 0;
	@Override
	public void update(float dt) {
		timer+= dt;
		if(timer > 0.6f) {
			timer -= 0.6f;

			createShots(240/PPM, 120/PPM);
		}
		
		world.step(dt, 1, 1);
		
		for(int i = 0; i < objects.size(); i++) {
			objects.get(i).update(dt);
			if(objects.get(i).shouldRemove() ) {
				world.destroyBody(objects.get(i).getBody());
				objects.remove(i);
				i--;
				continue;
			}
		}
		
	}

	@Override
	public void render() {
		sb.setProjectionMatrix(cam.combined);
		sb.begin();
		for(B2DSprite s : objects) { s.render(sb); }
		sb.end();
		
		if(isDrawDebugRender )
			b2dr.render(world, b2dCam.combined);
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
