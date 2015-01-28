package br.game.entities.shot;

import java.util.ArrayList;

public class MovementFactory {

	public static final int ShowingUpToCenter = 0;
	public static final int UpLoopAndGo = 1;
	public static final int DownLoopAndGo = 2;
	
	public static ArrayList<MovementData> getMovement(int id) {
		return getMovement(id, 25);
	}
	public static ArrayList<MovementData> getMovement(int id, float exitTime) {
		ArrayList<MovementData> moveData = new ArrayList<MovementData>();
		
		if(id == ShowingUpToCenter) {
			moveData.add(new MovementData(2.5f, 2.f, 90, 0, false));
			moveData.add(new MovementData(exitTime+2.5f, 0.f, 0, 0, false));
			moveData.add(new MovementData(exitTime+10.f, 5.f, 180, 0, false));
		} else 
		if(id == UpLoopAndGo) {
			moveData.add(new MovementData(2.5f, 2.f, 90, 0, false));
			moveData.add(new MovementData(4.5f, 2.f, 90, -1, false));
			moveData.add(new MovementData(99.f, 2.f, 0, -0.1f, false));
		}else 
		if(id == DownLoopAndGo) {
			moveData.add(new MovementData(2.5f, -2.f, 90, 0, false));
			moveData.add(new MovementData(4.5f, -2.f, 90, 1, false));
			moveData.add(new MovementData(99.f, 2.f, 0, 0.1f, false));
		}
		
		return moveData;		
	}
}
