package br.game.handlers;

import br.game.GdxGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;

public class GameInputProcessor extends InputAdapter {
	
	
	public static float scale = (float)( GdxGame.V_WIDTH ) / (float) (Gdx.graphics.getWidth() );
	public static float scaleUp = (float)( Gdx.graphics.getWidth() ) / (float) (GdxGame.V_WIDTH  );
	
	public boolean mouseMoved(int x, int y) {
		GameInput.x[0] = (int)(x*scale);
		GameInput.y[0] = (GdxGame.V_HEIGHT - (int)(y*scale) );
		return true;
	}
	
	public boolean touchDragged(int x, int y, int pointer) {
		if(pointer < 5) {
			GameInput.x[pointer] = (int)(x*scale);
			GameInput.y[pointer] = (GdxGame.V_HEIGHT - (int)(y*scale) );
			GameInput.down[pointer] = true;
		}		
		return true;
	}
	
	public boolean touchDown(int x, int y, int pointer, int button) {
		if(pointer < 5) {
			GameInput.x[pointer] = (int)(x*scale);
			GameInput.y[pointer] = (GdxGame.V_HEIGHT - (int)(y*scale) );		
			GameInput.down[pointer] = true;
		}
		return true;
	}
	
	public boolean touchUp(int x, int y, int pointer, int button) {
		if(pointer < 5) {
			GameInput.x[pointer] = (int)(x*scale);
			GameInput.y[pointer] = (GdxGame.V_HEIGHT - (int)(y*scale) );
			GameInput.down[pointer] = false;
		}
		return true;
	}
	
	public boolean keyDown(int k) {
		GameInput.setKey(k, true);
		return true;
	}
	
	public boolean keyUp(int k) {
		GameInput.setKey(k, false);
		return true;
	}
	
	

}
