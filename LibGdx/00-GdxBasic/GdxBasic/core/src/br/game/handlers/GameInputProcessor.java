package br.game.handlers;

import com.badlogic.gdx.InputAdapter;

public class GameInputProcessor extends InputAdapter {
	
	
	
	public boolean mouseMoved(int x, int y) {
		GameInput.x = x;
		GameInput.y = y;
		return true;
	}
	
	public boolean touchDragged(int x, int y, int pointer) {
		GameInput.x = x;
		GameInput.y = y;
		GameInput.down = true;
		return true;
	}
	
	public boolean touchDown(int x, int y, int pointer, int button) {
		GameInput.x = x;
		GameInput.y = y;
		GameInput.down = true;
		return true;
	}
	
	public boolean touchUp(int x, int y, int pointer, int button) {
		GameInput.x = x;
		GameInput.y = y;
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
