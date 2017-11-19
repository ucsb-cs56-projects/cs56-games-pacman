package edu.ucsb.cs56.projects.games.pacman;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test class for Character
 *
 * @author Nicholas Duncan
 * @author Wei Tung Chen
 * @version CS56 F17
 * @see Character
 */
public class CharacterTest {
        /**
	 * Runs a test on the character constructor
	 * for debugging purposes
	 */
	@Test
	public void test_constructor_simple() {
		Character character = new PacPlayer(5, 1);
		assertEquals(5, character.x);
		assertEquals(1, character.y);
		assertEquals(1, character.playerNum);
		assertEquals(true, character.alive);
	}
        /**
	 * A test to debug the reset method of the PacPlayer class
	 */
	@Test
	public void test_reset() {
		Character character = new PacPlayer(0, 0);
		character.lives = 1;
		character.alive = false;
		character.reset();
		assertEquals(3, character.lives);
		assertEquals(true, character.alive);
	}

	@Test
	public void test_resetPos() {
		Character character = new PacPlayer(5, 1);
		character.x = 3;
		character.y = 4;
		character.dx = 5;
		character.dy = 6;
		character.resetPos();
		assertEquals(5, character.x);
		assertEquals(1, character.y);
		assertEquals(0, character.dx);
		assertEquals(0, character.dy);
	}
        /**
	 * A test to debug the character class's 'move' method
	 */
	@Test
	public void test_move() {
		Character character = new PacPlayer(5, 1);
		character.dx = 5;
		character.dy = 5;
		character.speed = 5;
		character.move();
		assertEquals(30, character.x);
		assertEquals(26, character.y);
	}

	@Test
	public void test_moveable() {
		//Test Left
		short grid = GridData.GRID_CELL_BORDER_LEFT;
		boolean result = Character.moveable(-1, 0, grid);
		assertEquals(false, result);

		//Test Right
		grid = GridData.GRID_CELL_BORDER_RIGHT;
		result = Character.moveable(-1, 0, grid);
		assertEquals(true, result);

		//Test Up
		grid = GridData.GRID_CELL_BORDER_TOP;
		result = Character.moveable(0, -1, grid);
		assertEquals(false, result);

		//Test Down
		grid = GridData.GRID_CELL_BORDER_BOTTOM;
		result = Character.moveable(0, -1, grid);
		assertEquals(true, result);
	}
}
