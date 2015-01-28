package br.game.states;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;

import br.game.GdxGame;
import br.game.handlers.GameInput;
import br.game.handlers.GameStateManager;

public class MenuState extends GameState {

	private Texture play;
	private Texture background;
	private Texture stars;
	private Texture stars1;
	private Texture stars2;
	private Texture stars3;
	private Music music;
	private Rectangle rect;
	private Rectangle rectStart;
	private BitmapFont font = new BitmapFont();
	
	private float px1 = 0;
	private float px2 = 0;
	private int highscore = 0;
	
	FileHandle scoreFile;
	
	public MenuState(GameStateManager gsm) {
		super(gsm);
		
		play = GdxGame.res.getTexture("button_play");
		background = GdxGame.res.getTexture("space_escape");
		stars = GdxGame.res.getTexture("stars");
		stars1 = GdxGame.res.getTexture("stars");
		stars2 = GdxGame.res.getTexture("stars2");
		stars3 = GdxGame.res.getTexture("stars2");
		music = GdxGame.res.getMusic("myday");
		music.setLooping(true);
		music.play();
	
		rect = new Rectangle(50 , 40, 150, 150);
		rectStart = new Rectangle(GdxGame.V_WIDTH-play.getWidth()-50 , 40, 150, 150);
		
		font.setScale(1.8f);
		
		scoreFile = Gdx.files.local("data/highscore");
		if(scoreFile.exists())
			highscore = Integer.valueOf(scoreFile.readString());
		
		font.setColor(1, 0, 0, 1);
	}

	@Override
	public void handleInput() {
		if(GameInput.isDown(Input.Keys.ENTER) ) {
			gsm.setState(GameStateManager.END_RESULTS);
		}
		else 
		if(GameInput.isDown() && rectStart.contains(GameInput.x[0], GameInput.y[0]) ) {
			gsm.setState(GameStateManager.STAGE);			
		}
		else 
		if(GameInput.isDown() && rect.contains(GameInput.x[0], GameInput.y[0]) ) {
			gsm.setState(GameStateManager.PLAY);			
		}
	}

	@Override
	public void update(float dt) {
		handleInput();
		
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
		
		sb.setProjectionMatrix(cam.combined);
		sb.begin();
		sb.draw(stars, px1+stars.getWidth(), 0);
		sb.draw(stars1, px1, 0);
		sb.draw(stars2, px2, 0);
		sb.draw(stars3, px2+stars3.getWidth(), 0);
		sb.draw(background, 0, GdxGame.V_HEIGHT-background.getHeight());		
		sb.draw(play, rectStart.x, rectStart.y);
		sb.draw(play,rect.x, rect.y, play.getWidth(), play.getHeight(), 0, 0, play.getWidth(), play.getHeight(), true, false);
		sb.end();
		
		// HUD draw
		sb.setProjectionMatrix(hudCam.combined);
		sb.begin();
		String fontText = "Highscore: " + highscore;
		font.draw(sb, fontText, 0,30 );
		sb.end();		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
