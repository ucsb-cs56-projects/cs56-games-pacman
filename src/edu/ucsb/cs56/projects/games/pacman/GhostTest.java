package edu.ucsb.cs56.projects.games.pacman;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * A class for testing the functionality of
 * the ghost class
 *
 * @author Nicholas Duncan
 * @author Wei Tung Chen
 * @version CS56 F17
 * @see Ghost
 */
public class GhostTest {
        /**
         * Test to determine that the Ghost constructor works properly
	 */
	@Test
	public void test_constructor_1() {
		Ghost ghost = new Ghost(0, 0, 5, Ghost.TYPE_PINK);
		assertEquals(0, ghost.x);
		assertEquals(0, ghost.y);
		assertEquals(5, ghost.speed);
		assertEquals(Ghost.TYPE_PINK, ghost.type);
	}
        /**
	 * A more rigorous test to determine that the Ghost constructor works properly
	 */
	@Test
	public void test_constructor_2() {
		Ghost ghost = new Ghost(1, 2, 8, Ghost.GHOST2, new Grid());
		assertEquals(1, ghost.x);
		assertEquals(2, ghost.y);
		assertEquals(8, ghost.speed);
		assertEquals(Ghost.GHOST2, ghost.playerNum);
	}
        /**
	 * A test of the death() method where ghosts
	 * return to their original locations after death
	 */
	@Test
	public void test_constructor_super() {
		Ghost ghost = new Ghost(9, 8, 7, Ghost.TYPE_PINK, Ghost.GHOST2, new Grid());
		assertEquals(9, ghost.x);
		assertEquals(8, ghost.y);
		assertEquals(7, ghost.speed);
		assertEquals(Ghost.TYPE_PINK, ghost.type);
		assertEquals(Ghost.GHOST2, ghost.playerNum);
	}

	@Test
	public void test_death_1() {
		Ghost ghost = new Ghost(3, 4, 5, Ghost.GHOST2);
		ghost.death();
		assertEquals(false, ghost.edible);
	}
        /**
	 * A test of the death() method where ghosts
	 * return to a given location upon death
	 */
	@Test
	public void test_death_2() {
		Ghost ghost = new Ghost(3, 4, 5, Ghost.GHOST2);
		ghost.death(5, 8);
		assertEquals(false, ghost.edible);
		assertEquals(5, ghost.x);
		assertEquals(8, ghost.y);
	}
}
