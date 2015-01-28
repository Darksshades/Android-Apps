package br.game.handlers;

import java.util.Stack;

import br.game.GdxGame;
import br.game.states.GameState;
import br.game.states.MenuState;
import br.game.states.PlayStageState;
import br.game.states.PlayState;
import br.game.states.ResultsStageState;
import br.game.states.StageSelectorState;
import br.game.states.TestState;

public class GameStateManager {
	
	private GdxGame game;
	
	private Stack<GameState> gameStates;
	
	public static final int PLAY = 1;
	public static final int MENU = 2;
	public static final int STAGE = 3;
	public static final int PLAY_STAGE = 4;
	public static final int END_RESULTS = 5;
	public static final int TEST = 99;
	
	public Object message = null;
	
	public GameStateManager(GdxGame game)
	{
		this.game = game;
		gameStates = new Stack<GameState>();
		pushState(MENU);
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
		if(state == MENU) return new MenuState(this);
		if(state == STAGE) return new StageSelectorState(this);
		if(state == PLAY_STAGE) return new PlayStageState(this);
		if(state == TEST) return new TestState(this);
		if(state == END_RESULTS) return new ResultsStageState(this);
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
