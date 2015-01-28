package br.game.holders;

public class GameInformation {

	private boolean isWin = false;
	private int score;
	private String mapfile = null;
	
	
	public GameInformation(boolean isWin, int score) {
		this(isWin, score, null);
	}
	public GameInformation(boolean isWin, int score, String mapfile) {
		this.isWin = isWin;
		this.score = score;
		this.mapfile = mapfile;
	}

	public boolean isWin() {
		return isWin;
	}
	
	public String getMapFile() {
		return mapfile;
	}

	public int getScore() {
		return score;
	}	
	
}

