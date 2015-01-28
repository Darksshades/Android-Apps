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
	
	public static final int V_WIDTH = 320;
	public static final int V_HEIGHT = 240;
	public static final float SCALE = 2;
	
	public static final float STEP = 1 / 60f;
	private float accum;
	
	private SpriteBatch sb;
	private OrthographicCamera cam;
	private OrthographicCamera hud;
	
	private GameStateManager gsm;
	
	public static Content res;
	
	@Override
	public void create () {
		Gdx.app.log(NAME, "create()");
		batch = new SpriteBatch();
		img = new Texture("mage.png");
		
		sb  = new SpriteBatch();
		cam = new OrthographicCamera();
		cam.setToOrtho(false, V_WIDTH, V_HEIGHT);
		hud = new OrthographicCamera();
		hud.setToOrtho(false, V_WIDTH, V_HEIGHT);
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		// clear loading time
		Gdx.graphics.getDeltaTime();
		
		res = new Content();
		res.loadTexture("mage.png");
		res.loadTexture("slimy.png");
		res.loadTexture("house.png");
		res.loadTexture("background.png");
		res.loadTexture("cave.png");
		res.loadTexture("shot.png");
		
		Gdx.input.setInputProcessor(new GameInputProcessor());
		
		gsm = new GameStateManager(this);
	}

	@Override
	public void render () {
		
		accum += Gdx.graphics.getDeltaTime();
		while(accum >= STEP) {
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			accum -= STEP;
			gsm.update(STEP);
			gsm.render();

		}
		
		GameInput.update();
		
	}
	
	public SpriteBatch getSpriteBatch()	{ return sb; }
	public OrthographicCamera getCamera() { return cam; }
	public OrthographicCamera getHUDCamera() { return hud; }
}
