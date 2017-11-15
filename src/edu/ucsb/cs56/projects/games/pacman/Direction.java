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
		int result = 0;
		if (goingLeft(dx, dy)) result = LEFT;
		if (goingUp(dx, dy)) result = UP;
		if (goingRight(dx, dy)) result = RIGHT;
		if (goingDown(dx, dy)) result = DOWN;
		return result;
	}
}
