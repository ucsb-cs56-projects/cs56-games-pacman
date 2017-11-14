package edu.ucsb.cs56.projects.games.pacman;


/**
 * Direction class holds 4 directions.
 * @author Wei Tung Chen
 * @author Nicholas Duncan
 * @version CS56 F17
 */
public class Direction {
	public static final int NONE = 0;
	public static final int LEFT = 1;
	public static final int UP = 2;
	public static final int RIGHT = 3;
	public static final int DOWN = 4;

	public static boolean goingLeft(int dx, int dy) {
		return dx == -1 && dy == 0;
	}

	public static boolean goingUp(int dx, int dy) {
		return dx == 0 && dy == -1;
	}

	public static boolean goingRight(int dx, int dy) {
		return dx == 1 && dy == 0;
	}

	public static boolean goingDown(int dx, int dy) {
		return dx == 0 && dy == 1;
	}

	public static int getDirection(int dx, int dy) {
		if (goingLeft(dx, dy)) return LEFT;
		if (goingUp(dx, dy)) return UP;
		if (goingRight(dx, dy)) return RIGHT;
		if (goingDown(dx, dy)) return DOWN;
		return NONE;
	}
}
