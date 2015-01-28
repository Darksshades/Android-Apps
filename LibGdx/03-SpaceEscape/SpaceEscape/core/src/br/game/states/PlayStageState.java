package br.game.states;

import static br.game.handlers.B2DVars.PPM;

import com.badlogic.gdx.files.FileHandle;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Scanner;

import br.game.GdxGame;
import br.game.entities.B2DSprite;
import br.game.entities.Meteor;
import br.game.entities.Player;
import br.game.entities.Shot;
import br.game.entities.SpaceShoter;
import br.game.entities.shot.MovementFactory;
import br.game.entities.shot.ShotPattern;
import br.game.handlers.B2DVars;
import br.game.handlers.GameInput;
import br.game.handlers.GameInputProcessor;
import br.game.handlers.GameStateManager;
import br.game.handlers.MyContactLisntenner;
import br.game.holders.GameInformation;
import br.game.states.stage.StageSequence;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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

public class PlayStageState extends GameState {

	private BitmapFont font = new BitmapFont();
	
	private World world;
	private Box2DDebugRenderer b2dr;
	private boolean isDrawDebugRender = false;

	ShapeRenderer shapeRenderer = new ShapeRenderer();
	private Texture stars;
	private Texture stars1;
	private Texture stars2;
	private Texture stars3;
	private float px1 = 0;
	private float px2 = 0;
	
	private OrthographicCamera b2dCam;
	
	private ArrayList<Shot> shots;
	private ArrayList<Meteor> meteors;
	
	public static ArrayList<B2DSprite> objects;
	private ArrayList<B2DSprite> enemies;
	
	public static Player player;
	
	private float lastY = 0;
	
	private float deadTimer = 0;
	private boolean isDead = false;
	private boolean isWin = false;
	
	private float attackDelay = 0;
	
	private int score = 0;
	
	private float sequenceTimer = 0;

	private ArrayList<StageSequence> stageSequence = new ArrayList<StageSequence>();
	
	private int maxRounds = 1;
	private int currentRound = 1;

	

	public PlayStageState(GameStateManager gsm) {
		super(gsm);
		
		score = 0;
		
		shots = new ArrayList<Shot>();
		meteors = new ArrayList<Meteor>();
		objects = new ArrayList<B2DSprite>();
		enemies = new ArrayList<B2DSprite>();
		
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
		
		//loadMap((String)gsm.message);
		
		loadStageSequence((String)gsm.message);
		
//		SpaceShoter s = createSpaceShoter((440)/ PPM, GdxGame.V_HEIGHT/ PPM, ShotPattern.Burst, MovementFactory.ShowingUpToCenter);
//		
//		s.shotPattern.startDelay = 2.5f;
		
//		s.moveData.add(new MovementData(2.5f, 2.f, 90, 0, false));
//		s.moveData.add(new MovementData(22.5f, 0.f, 0, 0, false));
//		s.moveData.add(new MovementData(23f, 5.f, 180, 0, false));
		
//		createSpaceShoter((440)/ PPM, GdxGame.V_HEIGHT/4.f/ PPM, ShotPattern.Circular);
//		createSpaceShoter((440)/ PPM, GdxGame.V_HEIGHT/1.3f/ PPM, ShotPattern.Swiff);
		
	}
	
	
	/**
	 * Loads stage file, example file:
	 * 
	 * 1.0 1 1 1 440 320 0
	 * 1.0 1 1 2 440 0   0
	 * 2.1 1 1 1 440 320 0
	 * 2.1 1 1 2 440 0   0
	 * 3.2 1 1 1 440 320 0
	 * 3.2 1 1 2 440 0   0
	 * wait
	 * 1.0 2 8 0 440 320 2.5
	 * 
	 * (delay, id, shot, move, px, py)
	 * On wait, resets timer
	 * 
	 * @param mapfile
	 */
	private void loadStageSequence(String mapfile) {
		Gdx.app.log(GdxGame.NAME, "loading: " + mapfile);
		
		FileHandle file = Gdx.files.internal(mapfile);
		if(file.exists() == false){
			Gdx.app.log(GdxGame.ERROR, "Error opening mapfile: " + mapfile);
			return;
		}
		
		Scanner scanner = new Scanner(new BufferedReader(file.reader()) );
		
		while (scanner.hasNextLine()) {
			String token = scanner.next();
			if(token.contains("wait") == false) {
				Gdx.app.log(GdxGame.NAME, "enemy");
				float delay = Float.valueOf(token);
				int id = Integer.valueOf(scanner.next());
				int shotPattern = Integer.valueOf(scanner.next());
				int movementPattern = Integer.valueOf(scanner.next());
				float px = Float.valueOf(scanner.next());
				float py = Float.valueOf(scanner.next());
				float delayShot = Float.valueOf(scanner.next());
				stageSequence.add( new StageSequence(delay, id, shotPattern, movementPattern, px, py, delayShot) );
			} else 
			{
				Gdx.app.log(GdxGame.NAME, "wait");
				stageSequence.add(new StageSequence() );
				maxRounds += 1;
			}			
		}
		scanner.close();
		//Last sequence
		stageSequence.add(new StageSequence() );
		
		Gdx.app.log(GdxGame.NAME, "loaded: " + stageSequence.size());
		
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
		//player.getBody().setLinearVelocity(1.f, 0.f);
	}
	
	private SpaceShoter createSpaceShoter(float px, float py, int pattern, int movement) {
		BodyDef bdef = new BodyDef();
		Body body;
		PolygonShape shape = new PolygonShape();
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_ENEMY;
		fdef.filter.maskBits     = B2DVars.BIT_PLAYER | B2DVars.BIT_SHOT;
		fdef.isSensor = true;
		
		// create shooter
		bdef.position.set(px, py);
		bdef.type = BodyType.DynamicBody;
		body = world.createBody(bdef);
		
		shape.setAsBox(15/PPM, 5/PPM);
		fdef.shape = shape;
		body.createFixture(fdef);
		
		int id = 0;
		if(pattern == ShotPattern.Boss1) {
			id = 1;
		}
		//body.setLinearVelocity(0.6f, 0f);
		SpaceShoter s = new SpaceShoter(body, world, id);
		
		
		if(pattern != 0)
			s.shotPattern = new ShotPattern(body, world, pattern);		
		
		if(movement >= 0) {
			s.moveData = MovementFactory.getMovement(movement);
		}
		
		
		enemies.add(s);
		return s;
		
	}
		
	@Override
	public void handleInput() {
		if(Gdx.app.getType() == ApplicationType.Desktop) {
			if(GameInput.isDown(Input.Keys.ENTER) ) {
				isDrawDebugRender = !isDrawDebugRender;
			}
		}
		
		//Handle player movement
		
		if(GameInput.isDown()) {
			lastY = GameInput.y[0];
		}
		if(GameInput.isPressed() && GameInput.x[0] < GdxGame.V_WIDTH/2.f) {
			float diff = -(lastY - GameInput.y[0]);
			player.getBody().setTransform(player.getPosition().x, player.getPosition().y+diff/PPM, 0);
			lastY = GameInput.y[0];
		}
		if(GameInput.isUp()) {
			player.getBody().setLinearVelocity(player.getBody().getLinearVelocity().x, 0);
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
		
		updateSequence(dt);
		
		if(isDead || isWin) {
			deadTimer += dt;
			if(deadTimer >= 0.5f) {
				FileHandle	scoreFile = Gdx.files.local("data/highscore");
				int highscore = 0;
				if(scoreFile.exists())
					highscore = Integer.valueOf(scoreFile.readString());
				
				if(score > highscore) {
					scoreFile.writeString(String.valueOf(score), false);
				}
				
				GameInformation g = new GameInformation(isDead == false, score, (String)gsm.message);
				gsm.message = g;
				gsm.setState(GameStateManager.END_RESULTS);
			}
			
			return;
		}
		
		attackDelay -= dt;
		if(attackDelay <= 0) {
			//processAttack();
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
				m.getBody().setLinearVelocity(MathUtils.random(0.0f, -0.8f), MathUtils.random(0.5f, 1.0f) );
				meteors.get(i).getBody().setLinearVelocity(MathUtils.random(-0.8f, -1.8f), MathUtils.random(-0.5f, -1.0f) );
				GdxGame.res.getSound("explosion").play();
				
				score += 5-m.getType()+1;
			}
		}
		
		//Shots and objects in general
		for(int i = 0; i < objects.size(); i++) {
			objects.get(i).update(dt);
			if(objects.get(i).shouldRemove() ) {
				world.destroyBody(objects.get(i).getBody());
				objects.remove(i);
				i--;
				continue;
			}
		}
		
		//Enemies
		for(int i = 0; i < enemies.size(); i++) {
			enemies.get(i).update(dt);
			if(enemies.get(i).shouldRemove() ) {
				world.destroyBody(enemies.get(i).getBody());
				
				if(enemies.get(i).getState() == B2DSprite.DIE) {
					score += 1;
				}
				
				enemies.remove(i);
				i--;
				
				continue;
			}
		}
		
		player.update(dt);	
		
		if(player.shouldRemove()) {
			isDead = true;
		}
		
		updateBackground(dt);
	}

	private void updateSequence(float dt) {
		
		if(stageSequence.isEmpty()) {
			if(enemies.isEmpty()) {
				isWin  = true;
			}
			return;
		}
		
		if(stageSequence.get(0).waitDeadEnemies) {
			checkDeadEnemies();
			return;
		}
		
		sequenceTimer += dt;
		
		while(checkSequence());
		
		
		
	}

	private void checkDeadEnemies() {
		if(enemies.isEmpty()) {
			stageSequence.remove(0);
			sequenceTimer = 0;
			currentRound += 1;
		}
	}

	private boolean checkSequence() {
		if(stageSequence.get(0).waitDeadEnemies) return false;
		
		if(stageSequence.get(0).delay <= sequenceTimer) {
			StageSequence s = stageSequence.get(0);
			System.out.println("new enemy");			
			SpaceShoter en = createSpaceShoter(s.px, s.py, s.shot, s.move);
			en.shotPattern.startDelay = s.delayShot;
			stageSequence.remove(0);
			
			return (stageSequence.isEmpty() == false);
		}
		return false;
	}


	private void updateBackground(float dt) {
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

	@Override
	public void render() {
		
		// Player position - startOffset + centerScreen
		cam.position.x = (player.getPosition().x*PPM) - (20+65) + GdxGame.V_WIDTH/2.f;
		b2dCam.position.x = player.getPosition().x  -(20+65)/PPM +  GdxGame.V_WIDTH/2.f/PPM;
		cam.update();
		b2dCam.update();
		// background draw
		sb.setProjectionMatrix(hudCam.combined);
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
		for(B2DSprite sm	: objects) 	sm.render(sb);
		for(B2DSprite sm	: enemies) 	sm.render(sb);
		
		sb.end();
		
		// HUD draw
		sb.setProjectionMatrix(hudCam.combined);
		sb.begin();
		font.setColor(1, 0, 0, 1);
		String fontText = "Score: " + score;
		font.draw(sb, fontText, GdxGame.V_WIDTH - font.getBounds(fontText).width, GdxGame.V_HEIGHT );
		sb.end();	
		
		//Rounds draw
		float ballsize = 10;
		//Gdx.gl.glEnable(GL20.GL_BLEND);
	    //Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		//Concluded waves
		shapeRenderer.setColor(0.0f, 1.f, 0.0f, 1.f);
		shapeRenderer.identity();
		
		//System.out.println(currentRound + " | " + maxRounds);
		for(int i = 1; i <= maxRounds; i++) {

			//In progress
			if(i == currentRound) shapeRenderer.setColor(0.0f, 0.0f, 1.0f, 0.8f);
			//Not concluded
			if(i > currentRound) shapeRenderer.setColor(1.f, 0.0f, 0.0f, 0.8f);
			
			if(i < maxRounds) {
				shapeRenderer.rect(
						(i*15+GdxGame.V_WIDTH/2.f-ballsize-(15*maxRounds/2.f) )*GameInputProcessor.scaleUp, 
						(2.5f-(ballsize)/2.f+GdxGame.V_HEIGHT-ballsize)*GameInputProcessor.scaleUp, 20*GameInputProcessor.scaleUp, 5*GameInputProcessor.scaleUp);
			}
			
			shapeRenderer.circle(
					(i*15+GdxGame.V_WIDTH/2.f-ballsize-(15*maxRounds/2.f) )*GameInputProcessor.scaleUp, 
					(GdxGame.V_HEIGHT-ballsize)*GameInputProcessor.scaleUp, 10.f);
		}
	
		shapeRenderer.end();
		//Gdx.gl.glDisable(GL20.GL_BLEND);
		
		
		// Debug draw
		if(isDrawDebugRender)
			b2dr.render(world, b2dCam.combined);
		
		
		if(isWin) {
			sb.setProjectionMatrix(hudCam.combined);
			sb.begin();
			float sx = font.getScaleX();
			float sy = font.getScaleY();
			font.setScale(2.f);
			font.setColor(0, 1, 0.2f, 1);
			String winText = "You Win! score: " + score;
			font.draw(sb, winText, GdxGame.V_WIDTH/2.f - font.getBounds(winText).width/2.f, GdxGame.V_HEIGHT/2.f );
			sb.end();
			font.setScale(sx, sy);
		} else
		if(isDead) {
			sb.setProjectionMatrix(hudCam.combined);
			sb.begin();
			float sx = font.getScaleX();
			float sy = font.getScaleY();
			font.setScale(2.f);
			font.setColor(1, 0, 0, 1);
			String winText = "You Lose! score: " + score;
			font.draw(sb, winText, GdxGame.V_WIDTH/2.f - font.getBounds(winText).width/2.f, GdxGame.V_HEIGHT/2.f );
			sb.end();
			font.setScale(sx, sy);
		}
		
	}

	@Override
	public void dispose() {
		cam.position.x = GdxGame.V_WIDTH/2.f;
		cam.update();
		objects.clear();
	}

}
