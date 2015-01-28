package br.game.entities.bh;

public abstract class BulletHellTask {
	
	private int mWait = 0;
	private BulletHellTaskAction action= null;
	
	public void setExecuteAction(BulletHellTaskAction action) {
		
	}
	
	/**
	 * Shold be called 1 time per frame at a rate of 60 fps. 
	 */
	public void update() {
		if(mWait > 0) return;
		else mWait--;
		
		action.doExecute();
	}
	
	public void wait(int frames) {
		mWait = frames;
	}

}