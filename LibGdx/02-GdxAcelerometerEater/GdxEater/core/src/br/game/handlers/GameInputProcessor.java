package br.game.handlers;

import br.game.GdxGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;

public class GameInputProcessor extends InputAdapter {
	
	
	private float scale = (float)( GdxGame.V_WIDTH ) / (float) (Gdx.graphics.getWidth() );
	
	public boolean mouseMoved(int x, int y) {
		GameInput.x = (int)(x*scale);
		GameInput.y = (int)(y*scale);
		return true;
	}
	
	public boolean touchDragged(int x, int y, int pointer) {
		GameInput.x = (int)(x*scale);
		GameInput.y = (int)(y*scale);
		GameInput.down = true;
		return true;
	}
	
	public boolean touchDown(int x, int y, int pointer, int button) {
		GameInput.x = (int)(x*scale);
		GameInput.y = (int)(y*scale);
		GameInput.down = true;
		return true;
	}
	
	public boolean touchUp(int x, int y, int pointer, int button) {
		GameInput.x = (int)(x*scale);
		GameInput.y = (int)(y*scale);
		GameInput.down = false;
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
