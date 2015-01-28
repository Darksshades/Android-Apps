package br.game.states.stage;

import br.game.handlers.B2DVars;

public class StageSequence {
	
	public float delay;
	public int id;
	public int shot;
	public int move;
	public float px;
	public float py;
	public float delayShot;
	
	public boolean waitDeadEnemies = false;
	
	public StageSequence(float delay, int id, int shot, int move, float px, float py, float delayShot) {
		this.delay = delay;
		this.id = id;
		this.shot = shot;
		this.move = move;
		this.px = px / B2DVars.PPM;
		this.py = py / B2DVars.PPM;
		this.delayShot = delayShot;
	}
	
	public StageSequence() {
		this.delay = 0;
		waitDeadEnemies = true;
	}
	
}
