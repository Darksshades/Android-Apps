package br.game.handlers;

public class GameInput {
	public static int x;
	public static int y;
	public static boolean down;
	public static boolean pdown;
	public static boolean[] keys;
	public static boolean[] pkeys;
	private static final int NUM_KEYS = 255;
	
	static {
		keys = new boolean[NUM_KEYS];
		pkeys = new boolean[NUM_KEYS];
	}
	
	public static void update() {
		pdown = down;
		for(int i = 0; i < NUM_KEYS; i++) {
			pkeys[i] = keys[i];
		}
	}
	
	public static boolean isPressed() { return down; }
	public static boolean isDown() { return down && !pdown; }
	public static boolean isUp() { return !down && pdown; }
	
	public static void setKey(int i, boolean b) { keys[i] = b; }
	public static boolean isPressed(int i) { return keys[i]; }
	public static boolean isDown(int i) { return keys[i] && !pkeys[i]; }
	public static boolean isUp(int i) { return !keys[i] && pkeys[i]; }
}
