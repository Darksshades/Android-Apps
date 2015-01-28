package br.game;

import br.game.handlers.Content;
import br.game.handlers.GameInput;
import br.game.handlers.GameInputProcessor;
import br.game.handlers.GameStateManager;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	
	public static final String NAME = "GdxGame";
	public static final String ERROR = "GameError";
	public static final String DEBUG = "DEBUG";
	
	public static final int V_WIDTH = 480; // normally 320x240 
	public static final int V_HEIGHT = 320;
	public static final float SCALE = 2;
	
	public static final float STEP = 1 / 60.f;
	//private float accum;
	
	private SpriteBatch sb;
	public static OrthographicCamera cam;
	private OrthographicCamera hud;
	
	private GameStateManager gsm;
	
	public static Content res;
	
	@Override
	public void create () {
		Gdx.app.log(NAME, "create()");
		batch = new SpriteBatch();
		
		sb  = new SpriteBatch();
		cam = new OrthographicCamera();
		cam.setToOrtho(false, V_WIDTH, V_HEIGHT);
		hud = new OrthographicCamera();
		hud.setToOrtho(false, V_WIDTH, V_HEIGHT);
		
		Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
		// clear loading time
		Gdx.graphics.getDeltaTime();
		
		res = new Content();
		res.loadTexture("alien.png");
		res.loadTexture("ship.png");
		res.loadTexture("game_over.png");
		res.loadTexture("planet.png");
		res.loadTexture("meteor.png");
		res.loadTexture("button_play.png");
		res.loadTexture("space_escape.png");
		res.loadTexture("stars.png");
		res.loadTexture("stars2.png");
		res.loadTexture("meteor_1.png");
		res.loadTexture("meteor_2.png");
		res.loadTexture("meteor_3.png");
		res.loadTexture("meteor_4.png");
		res.loadTexture("meteor_5.png");
		res.loadTexture("meteor_6.png");
		res.loadTexture("shot_01.png");
		res.loadTexture("shot.png");
		res.loadTexture("space_ship.png");
		res.loadTexture("space_ship2.png");
		res.loadTexture("background.png");
		res.loadMusic("myday.wav");
		res.loadSound("explosion.wav");
		res.loadSound("laser_shot.wav");
		
		Gdx.input.setInputProcessor(new GameInputProcessor());
		
		gsm = new GameStateManager(this);
		
	}

	@Override
	public void render () {
		//accum += Gdx.graphics.getDeltaTime();
		//while(accum >= STEP) {
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			//accum -= STEP;
			gsm.update(Gdx.graphics.getDeltaTime());
			gsm.render();
			GameInput.update();

	//	}
		
	}
	
	public SpriteBatch getSpriteBatch()	{ return sb; }
	public OrthographicCamera getCamera() { return cam; }
	public OrthographicCamera getHUDCamera() { return hud; }
}
