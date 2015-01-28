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
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import br.game.GdxGame;
import static br.game.handlers.B2DVars.PPM;
import br.game.entities.Cave;
import br.game.entities.City;
import br.game.entities.Player;
import br.game.entities.Shot;
import br.game.entities.Slimy;
import br.game.handlers.B2DVars;
import br.game.handlers.GameInput;
import br.game.handlers.GameStateManager;
import br.game.handlers.MyContactLisntenner;

public class PlayState extends GameState {
	
	private BitmapFont font = new BitmapFont();
	
	private World world;
	private Box2DDebugRenderer b2dr;
	private boolean isDrawDebugRender = false;
	
	private Texture background;
	
	private OrthographicCamera b2dCam;
	
	private ArrayList<Shot> shots;
	private ArrayList<Slimy> slimys;
	private ArrayList<City> citys;
	private ArrayList<Cave> caves;
	private Player player;
	
	
	private float deadTimer = 0;
	private float deadTimerLimit = 2.5f;
	private boolean isDeadTimer = false;
	
	private float attackDelay = 0;
	private float changeDelay = 0; 	
	
	private float difficulty = 1.f;
	private int   displayDifficulty = 1;
	
	private int caveIndex = 0;
	private int score = 0;	
	
	public PlayState(GameStateManager gsm) {
		super(gsm);
		
		shots = new ArrayList<Shot>();
		slimys= new ArrayList<Slimy>();
		citys = new ArrayList<City>();
		caves = new ArrayList<Cave>();
		
		background = GdxGame.res.getTexture("background");
		
		world = new World( new Vector2(0.0f, 0.0f) , true);
		world.setContactListener(new MyContactLisntenner());
		b2dr  = new Box2DDebugRenderer();
		b2dr.setDrawContacts(true);
		
		createCitys();
		createCaves();
		createPlayer();
		
		b2dCam = new OrthographicCamera();
		b2dCam.setToOrtho(false, GdxGame.V_WIDTH / PPM, GdxGame.V_HEIGHT / PPM);
	}
	
	private void createCaves() {
		BodyDef bdef = new BodyDef();
		Body body;
		PolygonShape shape = new PolygonShape();
		FixtureDef fdef = new FixtureDef();
		Cave c;
		shape.setAsBox( (16*1.5f) / PPM, 16 / PPM );
		fdef.shape = shape;
		bdef.type = BodyType.StaticBody;
		
		//Cave 1
		bdef.position.set( (16*1)/ PPM, (16*6) / PPM);
		body = world.createBody(bdef);
		body.createFixture(fdef);		
		c = new Cave(body);
		caves.add(c);
		//Cave 2
		bdef.position.set( (16*15) / PPM, (16*6) / PPM);
		body = world.createBody(bdef);
		body.createFixture(fdef);		
		c = new Cave(body);
		caves.add(c);
		//Cave 3
		bdef.position.set( (16*8) / PPM, (16*11) / PPM);
		body = world.createBody(bdef);
		body.createFixture(fdef);		
		c = new Cave(body);
		caves.add(c);
		//Cave 4
		bdef.position.set( (16*8)/ PPM, (16*1) / PPM);
		body = world.createBody(bdef);
		body.createFixture(fdef);		
		c = new Cave(body);
		caves.add(c);
	}
	
	private void createCitys() {
		BodyDef bdef = new BodyDef();
		Body body;
		PolygonShape shape = new PolygonShape();
		FixtureDef fdef = new FixtureDef();
		City c;
		fdef.isSensor = true;
		
		shape.setAsBox( 24 / PPM, 12 / PPM );
		fdef.shape = shape;
		bdef.type = BodyType.StaticBody;
		fdef.filter.categoryBits = B2DVars.BIT_HOUSE;
		fdef.filter.maskBits 	 = B2DVars.BIT_ENEMY;
		
		//City 1
		bdef.position.set(  (GdxGame.V_WIDTH/2.0f) / PPM, 
						    (GdxGame.V_HEIGHT/2.0f) / PPM);
		body = world.createBody(bdef);
		body.createFixture(fdef);		
		c = new City(body);
		citys.add(c);
	}

	private void createPlayer() {
		BodyDef bdef = new BodyDef();
		Body body;
		PolygonShape shape = new PolygonShape();
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
		fdef.filter.maskBits     = B2DVars.BIT_ENEMY;
		fdef.isSensor = true;
		
		// create player
		bdef.position.set(70 / PPM, 50 / PPM);
		bdef.type = BodyType.DynamicBody;
		body = world.createBody(bdef);
		
		shape.setAsBox(3.5f / PPM, 6 / PPM, new Vector2(0, -2 / PPM), 0);
		fdef.shape = shape;
		body.createFixture(fdef);
		
		player = new Player(body);
		player.getCurrentAnimation().setFrameDuration(0.08f);
		player.getBody().setTransform(
				citys.get(0).getPosition().x + 1,
				citys.get(0).getPosition().y,
				0);
	}

	private Shot createShot(float x, float y) {
		BodyDef bdef = new BodyDef();
		bdef.position.set(x, y);
		bdef.type = BodyType.DynamicBody;
		Body body = world.createBody(bdef);
		FixtureDef fdef = new FixtureDef();
		fdef.isSensor = true;
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(6 / PPM, 6 / PPM);
		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_SHOT;
		fdef.filter.maskBits 	 = B2DVars.BIT_ENEMY;
		body.createFixture(fdef);
		
		Shot s = new Shot(body);
		
		shots.add(s);
		
		return s;
	}
	
	private Slimy createSlimy(float x, float y) {
		BodyDef bdef = new BodyDef();
		bdef.position.set(x, y);
		bdef.type = BodyType.DynamicBody;

		Body body = world.createBody(bdef);
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(7 / PPM, 4 / PPM, new Vector2(0, -3.f/PPM), 0);
		FixtureDef fdef = new FixtureDef();
		fdef.isSensor = true;
		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_ENEMY;
		fdef.filter.maskBits 	 = B2DVars.BIT_HOUSE | B2DVars.BIT_SHOT | B2DVars.BIT_PLAYER;
		body.createFixture(fdef);
		
		Slimy s = new Slimy(body);
		
		slimys.add(s);
		
		return s;
	}

	@Override
	public void handleInput() {
		if(Gdx.app.getType() == ApplicationType.Desktop) {
			if(GameInput.isDown(Input.Keys.ENTER) ) {
				isDrawDebugRender = !isDrawDebugRender;
			}
		}
		
		//Handle player position
		
		float acelX = Gdx.input.getAccelerometerX();
		float acelY = Gdx.input.getAccelerometerY();
		
		// Handle desktop
		if(GameInput.isPressed(Input.Keys.RIGHT) ) {
			acelY = 5;
		} else
		if(GameInput.isPressed(Input.Keys.LEFT) ) {
			acelY = -5;
		} else
		if(GameInput.isPressed(Input.Keys.DOWN) ){ 
			acelX = 5;
		} else 
		if(GameInput.isPressed(Input.Keys.UP) ) {
			acelX = -5;
		}
		
		//Prioritize X
		if(Math.abs(acelY) > Math.abs(acelX) )
		{
			if(acelY > 2.f) {
				player.getBody().setTransform(
						citys.get(0).getPosition().x + 1,
						citys.get(0).getPosition().y,
						0);
				
			} else if (acelY < -2.f) {
				player.getBody().setTransform(
						citys.get(0).getPosition().x - 1,
						citys.get(0).getPosition().y,
						0);
			}
		} else {
			if(acelX > 2.f) {
				player.getBody().setTransform(
						citys.get(0).getPosition().x,
						citys.get(0).getPosition().y - 0.5f,
						0);				
			} else if (acelX < -2.f) {
				player.getBody().setTransform(
						citys.get(0).getPosition().x,
						citys.get(0).getPosition().y + 0.5f,
						0);
			}
		}
		
		
		
	}

	@Override
	public void update(float dt) {
		
		if(isDeadTimer) {
			deadTimer+=dt;
			if(deadTimer > deadTimerLimit) {
				FileHandle	scoreFile = Gdx.files.local("data/highscore");
				int highscore = 0;
				if(scoreFile.exists())
					highscore = Integer.valueOf(scoreFile.readString());
				
				if(score > highscore) {
					scoreFile.writeString(String.valueOf(score), false);
				}
				gsm.setState(GameStateManager.MENU);
			}
			
			return;
		}
		
		changeDelay -= dt;
		attackDelay -= dt;
		
		// check spawn
		createSpawns();
		
		// check input
		handleInput();
		
		world.step(dt, 6, 2);
		
		if(player.isAnimationFinished()) {
			player.setState(Player.STAND);
			if(GdxGame.easyMode) {
				Shot s = createShot(player.getPosition().x, player.getPosition().y);
				Vector2 vel = new Vector2(MathUtils.random(-1.f, 1.f), MathUtils.random(-1.f, 1.f));
				vel.nor();
				s.getBody().setLinearVelocity(vel);
			}
		}
		
		for(Cave c 		: caves) {
			c.update(dt);
			if(c.isAnimationFinished()) {
				Slimy s = createSlimy(c.getPosition().x, c.getPosition().y-8/PPM);
				
				Vector2 target = citys.get(0).getPosition();
				target.x -= s.getPosition().x;
				target.y -= s.getPosition().y;
				
				target.nor();
				
				s.getBody().setLinearVelocity(target.x*1, target.y*1);
				c.setState(Cave.STAND);
			}
		}
		int destroyed = 0;
		for(City city 	: citys) {
			city.update(dt);
			if(city.getState() == City.DEAD)
				destroyed++;
		}
		
		if(destroyed >= citys.size())
			isDeadTimer = true;
		
		for(int i = 0; i < slimys.size(); i++) {
			slimys.get(i).update(dt);
			if(slimys.get(i).shouldRemove()) {
				// Only points if death animation played
				if(slimys.get(i).isAnimationFinished()) {
					score++;
				}
				world.destroyBody(slimys.get(i).getBody());
				slimys.remove(i);
				i--;
				continue;
			}
		}
		for(int i = 0; i < shots.size(); i++) {
			shots.get(i).update(dt);
			if(shots.get(i).shouldRemove()) {
				world.destroyBody(shots.get(i).getBody());
				shots.remove(i);
				i--;
				continue;
			}
		}
		
		player.update(dt);
		
		
	}

	private void createSpawns() {
		if(changeDelay <= 0) {
			caveIndex = MathUtils.random(3);
			changeDelay += MathUtils.random(1.f*difficulty, 2.f*difficulty);
		}
		if(attackDelay <= 0) {	
			System.out.println("Attack: " + caveIndex);
			caves.get(caveIndex).setState(Cave.ATTACK);
			caves.get(caveIndex).getCurrentAnimation().setFrameDuration(1.f*difficulty);
			attackDelay += MathUtils.random(1.f*difficulty, 2.f*difficulty);
		}
		
		
		if(score > 120) {
			displayDifficulty = 10;
			difficulty = 0.1f;
		} else if(score > 80) {
			displayDifficulty = 9;
			difficulty = 0.2f;
		} else if(score > 50) {
			displayDifficulty = 8;
			difficulty = 0.3f;
		} else if(score > 40) {
			displayDifficulty = 7;
			difficulty = 0.4f;
		} else if(score > 30) {
			displayDifficulty = 6;
			difficulty = 0.5f;
		} else if(score > 20) {
			displayDifficulty = 5;
			difficulty = 0.6f;
		} else if(score > 15) {
			displayDifficulty = 4;
			difficulty = 0.7f;
		} else if(score > 10) {
			difficulty = 0.8f;
			displayDifficulty = 3;
		} else if(score > 5) {
			difficulty = 0.9f;
			displayDifficulty = 2;
		} 
	}

	@Override
	public void render() {
		// background draw
		sb.setProjectionMatrix(cam.combined);
		sb.begin();
		sb.draw(background, 0, 0);
		sb.end();
		
		// draw objects
		sb.setProjectionMatrix(cam.combined);
		for(Cave c 		: caves) 	c.render(sb);
		for(City city 	: citys) 	city.render(sb);
		for(Slimy s 	: slimys) 	s.render(sb);
		
		player.render(sb);
		
		for(Shot s 		: shots) 	s.render(sb);
		
		// HUD draw
		sb.setProjectionMatrix(hudCam.combined);
		sb.begin();
		font.setColor(1, 0, 0, 1);
		String fontText = "City Life: " + citys.get(0).getHP();
		String font2Text = "Score: " + score;
		String font3Text = "Difficulty: " + displayDifficulty;
		font.draw(sb, font3Text, 0, GdxGame.V_HEIGHT );
		font.draw(sb, font2Text, GdxGame.V_WIDTH/2.f - font.getBounds(fontText).width/2.f, GdxGame.V_HEIGHT );
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

