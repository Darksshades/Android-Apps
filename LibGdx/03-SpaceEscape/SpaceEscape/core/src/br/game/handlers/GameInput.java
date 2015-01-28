package br.game.handlers;

public class GameInput {
	public static int[] x;
	public static int[] y;
	
	public static boolean[] down;
	public static boolean[] pdown;
	public static boolean[] keys;
	public static boolean[] pkeys;
	private static final int NUM_KEYS = 255;
	private static final int NUM_CLICKS = 5;
	
	static {
		keys = new boolean[NUM_KEYS];
		pkeys = new boolean[NUM_KEYS];
		down = new boolean[NUM_CLICKS];
		pdown = new boolean[NUM_CLICKS];
		x = new int[NUM_CLICKS];
		y = new int[NUM_CLICKS];
	}
	
	public static void update() {
		for(int i = 0; i < NUM_CLICKS; i++) {
			pdown[i] = down[i];
		}
		for(int i = 0; i < NUM_KEYS; i++) {
			pkeys[i] = keys[i];
		}
	}
	
	public static boolean isPressed() { return down[0]; }
	public static boolean isDown() { return down[0] && !pdown[0]; }
	public static boolean isUp() { return !down[0] && pdown[0]; }

	public static boolean isPressedTouch(int i) { return down[i]; }
	public static boolean isDownTouch(int i) { return down[i] && !pdown[i]; }
	public static boolean isUpTouch(int i) { return !down[i] && pdown[i]; }
	
	public static void setKey(int i, boolean b) { keys[i] = b; }
	public static boolean isPressed(int i) { return keys[i]; }
	public static boolean isDown(int i) { return keys[i] && !pkeys[i]; }
	public static boolean isUp(int i) { return !keys[i] && pkeys[i]; }
}
