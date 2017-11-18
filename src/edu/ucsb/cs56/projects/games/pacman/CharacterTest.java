package edu.ucsb.cs56.projects.games.pacman;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test class for Character
 *
 * @author Nicholas Duncan
 * @author Wei Tung Chen
 * @version CS56, Fall 2017
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
		Character character = new PacPlayer(5, 1);
		character.x = 3;
		character.y = 4;
		character.dx = 5;
		character.dy = 6;
		character.reset();
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
                assertEquals(5, character.x);
                assertEquals(1, character.y);
	}
}
