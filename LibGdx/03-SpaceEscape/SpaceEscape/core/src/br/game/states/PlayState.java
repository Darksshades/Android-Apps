package br.game.states;

import java.util.ArrayList;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
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
import br.game.entities.Meteor;
import br.game.entities.Player;
import br.game.entities.Shot;
import br.game.handlers.B2DVars;
import br.game.handlers.GameInput;
import br.game.handlers.GameStateManager;
import br.game.handlers.MyContactLisntenner;
import br.game.holders.GameInformation;

public class PlayState extends GameState {
	
	private BitmapFont font = new BitmapFont();
	
	private World world;
	private Box2DDebugRenderer b2dr;
	private boolean isDrawDebugRender = false;
	
	private Texture stars;
	private Texture stars1;
	private Texture stars2;
	private Texture stars3;
	private float px1 = 0;
	private float px2 = 0;
	
	private OrthographicCamera b2dCam;
	
	private ArrayList<Shot> shots;
	private ArrayList<Meteor> meteors;
	private Player player;
	
	private float lastY = 0;
	
	private float deadTimer = 0;
	private boolean isDead = false;
	
	private float attackDelay = 0;
	
	private int score = 0;
	
	public PlayState(GameStateManager gsm) {
		super(gsm);
		
		score = 0;
		
		shots = new ArrayList<Shot>();
		meteors = new ArrayList<Meteor>();
		stars = GdxGame.res.getTexture("stars");
		stars1 = GdxGame.res.getTexture("stars");
		stars2 = GdxGame.res.getTexture("stars2");
		stars3 = GdxGame.res.getTexture("stars2");
		
		world = new World( new Vector2(0.0f, 0.0f) , true);
		world.setContactListener(new MyContactLisntenner());
		b2dr  = new Box2DDebugRenderer();
		b2dr.setDrawContacts(true);
		
		createPlayer();
		b2dCam = new OrthographicCamera();
		b2dCam.setToOrtho(false, GdxGame.V_WIDTH / PPM, GdxGame.V_HEIGHT / PPM);
	}
	
	private Meteor createMeteor(float px, float py, short mtype) {
		BodyDef bdef = new BodyDef();
		Body body;
		CircleShape shape = new CircleShape();
		shape.setRadius(25/PPM);
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_ENEMY;
		fdef.filter.maskBits     = B2DVars.BIT_PLAYER | B2DVars.BIT_SHOT;
		fdef.isSensor = true;
		bdef.fixedRotation = false;
		
	
		bdef.position.set(px, py);
		bdef.type = BodyType.DynamicBody;
		body = world.createBody(bdef);
		fdef.shape = shape;
		body.createFixture(fdef);
		
		body.setAngularVelocity(MathUtils.random(0.2f, 2.f) );
		
		Meteor shot = new Meteor(body, mtype);
		
		meteors.add(shot);
		
		return shot;
		
	}
	
	private Shot createShot(float px, float py) {
		BodyDef bdef = new BodyDef();
		Body body;
		PolygonShape shape = new PolygonShape();
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_SHOT;
		fdef.filter.maskBits     = B2DVars.BIT_ENEMY;
		fdef.isSensor = true;
	
		bdef.position.set(px, py);
		bdef.type = BodyType.DynamicBody;
		body = world.createBody(bdef);
		
		shape.setAsBox(6.f / PPM, 3 / PPM);
		fdef.shape = shape;
		body.createFixture(fdef);
		
		Shot shot = new Shot(body);
		
		shots.add(shot);
		
		return shot;
	}

	private void createPlayer() {
		BodyDef bdef = new BodyDef();
		Body body;
		CircleShape shape = new CircleShape();
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
		fdef.filter.maskBits     = B2DVars.BIT_ENEMY;
		fdef.isSensor = true;
		
		// create player
		bdef.position.set((20+65)/ PPM, GdxGame.V_HEIGHT/2.f/ PPM);
		bdef.type = BodyType.DynamicBody;
		body = world.createBody(bdef);
		
		shape.setRadius(13/PPM);
		//shape.setAsBox(18.f / PPM, 13 / PPM);
		fdef.shape = shape;
		body.createFixture(fdef);
		
		player = new Player(body);
	}
		
	@Override
	public void handleInput() {
		if(Gdx.app.getType() == ApplicationType.Desktop) {
			if(GameInput.isDown(Input.Keys.ENTER) ) {
				isDrawDebugRender = !isDrawDebugRender;
			}
		}
		
		//Handle player movement
		
		//Old movement
//		if(GameInput.isDown()) {
//			if(GameInput.y > GdxGame.V_HEIGHT/2.f) {
//				player.getBody().setLinearVelocity(0, 1f);
//			} else {
//				player.getBody().setLinearVelocity(0, -1.f);
//			}
//		} else {
//			if(GameInput.isDown(Input.Keys.UP)) {
//				player.getBody().setLinearVelocity(0, 1f);
//			} else if(GameInput.isDown(Input.Keys.DOWN)) {
//				player.getBody().setLinearVelocity(0, -1.f);
//			}
//		}
		
		if(GameInput.isDown()) {
			lastY = GameInput.y[0];
		}
		if(GameInput.isPressed() && GameInput.x[0] < GdxGame.V_WIDTH/2.f) {
			float diff = -(lastY - GameInput.y[0]);
			player.getBody().setTransform(player.getPosition().x, player.getPosition().y+diff/PPM, 0);
			lastY = GameInput.y[0];
		}
		if(GameInput.isUp()) {
			player.getBody().setLinearVelocity(0, 0);
		}
		
		// Shot
		if(GameInput.isDown() && GameInput.x[0] > GdxGame.V_WIDTH/2.f) {
			Shot shot = createShot(player.getPosition().x+0.4f, player.getPosition().y);
			shot.getBody().setLinearVelocity(16.f, 0);
		} else if(GameInput.isDownTouch(1) && GameInput.x[1] > GdxGame.V_WIDTH/2.f) {
			Shot shot = createShot(player.getPosition().x+0.4f, player.getPosition().y);
			shot.getBody().setLinearVelocity(16.f, 0);
		}
		
		// Max player velocities
		if(player.getBody().getLinearVelocity().y > 2.f) {
			player.getBody().setLinearVelocity(0, 2.f);
		} else if(player.getBody().getLinearVelocity().y < -2.f) {
			player.getBody().setLinearVelocity(0, -2.f);
		} 
				
	}

	@Override
	public void update(float dt) {
		
		if(isDead) {
			deadTimer += dt;
			if(deadTimer >= 1.f) {
				FileHandle	scoreFile = Gdx.files.local("data/highscore");
				int highscore = 0;
				if(scoreFile.exists())
					highscore = Integer.valueOf(scoreFile.readString());
				
				if(score > highscore) {
					scoreFile.writeString(String.valueOf(score), false);
				}
				
				GameInformation g = new GameInformation(isDead == false, score);
				gsm.message = g;
				gsm.setState(GameStateManager.END_RESULTS);
			}
			
			return;
		}
		
		attackDelay -= dt;
		if(attackDelay <= 0) {
			processAttack();
		}
		// check input
		handleInput();
		
		world.step(dt, 4, 2);
		
		//Shots
		for(int i = 0; i < shots.size(); i++) {
			shots.get(i).update(dt);
			if(shots.get(i).shouldRemove() ) {
				world.destroyBody(shots.get(i).getBody());
				shots.remove(i);
				i--;
				continue;
			}
		}
		//Meteors
		for(int i = 0; i < meteors.size(); i++) {
			meteors.get(i).update(dt);
			if(meteors.get(i).shouldRemove() ) {
				if(meteors.get(i).getHP() <= 0 ) {
					score+= 6;
					GdxGame.res.getSound("explosion").play();
				}
				
				world.destroyBody(meteors.get(i).getBody());
				meteors.remove(i);
				i--;
				continue;
			}
			if(meteors.get(i).isExploding()) {
				meteors.get(i).setExploding(false);
				Meteor m = createMeteor(meteors.get(i).getPosition().x, meteors.get(i).getPosition().y, meteors.get(i).getType());
				m.getBody().setLinearVelocity(MathUtils.random(-0.8f, -1.8f), MathUtils.random(0.5f, 1.0f) );
				meteors.get(i).getBody().setLinearVelocity(MathUtils.random(-0.8f, -1.8f), MathUtils.random(-0.5f, -1.0f) );
				GdxGame.res.getSound("explosion").play();
				
				score += 5-m.getType()+1;
			}
		}
		player.update(dt);	
		
		if(player.shouldRemove()) {
			isDead = true;
		}
		
		//Update background
		px1 -= 10*dt;
		px2 -= 1*dt;
		
		if(px1 <= -stars.getWidth()) {
			px1 = 0;
		}
		if(px2 <= -stars2.getWidth()) {
			px2 = 0;
		}
	}

	private void processAttack() {
		int mtype = MathUtils.random(2, 4);
		int quantity = MathUtils.random(1, 2);
		
		attackDelay += MathUtils.random(2.f, 5.f);
		
		for(int i = 0; i < quantity; i++) {
			Meteor m = createMeteor(
					GdxGame.V_WIDTH/PPM + MathUtils.random(0.2f, 2.f), 
					0, (short)mtype);
			float py = MathUtils.random(m.getHeight()/2.f/PPM, (GdxGame.V_HEIGHT-m.getHeight()/2.f)/PPM);
			m.getBody().setTransform(m.getPosition().x, py, 0);
			m.getBody().setLinearVelocity(-1.f, MathUtils.random(-0.8f, 0.8f));
		}
	}

	@Override
	public void render() {
		// background draw
		sb.setProjectionMatrix(cam.combined);
		sb.begin();
		sb.draw(stars, px1+stars.getWidth(), 0);
		sb.draw(stars1, px1, 0);
		sb.draw(stars2, px2, 0);
		sb.draw(stars3, px2+stars3.getWidth(), 0);
		sb.end();
		
		// draw objects
		sb.setProjectionMatrix(cam.combined);
		
		sb.begin();
		
		player.render(sb);
		for(Shot s 		: shots) 	s.render(sb);
		for(Meteor m	: meteors) 	m.render(sb);
		
		sb.end();
		
		// HUD draw
		sb.setProjectionMatrix(hudCam.combined);
		sb.begin();
		font.setColor(1, 0, 0, 1);
		String fontText = "Score: " + score;
		font.draw(sb, fontText, GdxGame.V_WIDTH - font.getBounds(fontText).width, GdxGame.V_HEIGHT );
		sb.end();		
		
		// Debug draw
		if(isDrawDebugRender)
			b2dr.render(world, b2dCam.combined);
		
	}

	@Override
	public void dispose() {
		
	}

}

