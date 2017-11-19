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
	
	@Test
	public void test_constructor_simple() {
		Character character = new PacPlayer(5, 1);
		assertEquals(5, character.x);
		assertEquals(1, character.y);
		assertEquals(1, character.playerNum);
		assertEquals(true, character.alive);
	}

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
}
