package br.game.handlers;

import java.util.Stack;

import br.game.GdxGame;
import br.game.states.GameState;
import br.game.states.PlayState;

public class GameStateManager {
	
	private GdxGame game;
	
	private Stack<GameState> gameStates;
	
	public static final int PLAY = 1;
	
	public GameStateManager(GdxGame game)
	{
		this.game = game;
		gameStates = new Stack<GameState>();
		pushState(PLAY);
	}

	public GdxGame getGame() { return game; }
	
	public void update(float dt)
	{
		gameStates.peek().update(dt);
	}
	
	public void render()
	{
		gameStates.peek().render();
	}
	
	private GameState getState(int state)
	{
		if(state == PLAY) return new PlayState(this);
		return null;
	}
	
	public void setState(int state)
	{
		popState();
		pushState(state);
	}
	
	public void pushState(int state)
	{
		gameStates.push(getState(state) );
		
	}
	
	public void popState() 
	{
		GameState g = gameStates.pop();
		g.dispose();
	}

}
