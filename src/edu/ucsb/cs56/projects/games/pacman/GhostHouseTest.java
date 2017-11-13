package edu.ucsb.cs56.projects.games.pacman;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test class for GhostHouse
 *
 * @author Nicholas Duncan
 * @author Wei Tung Chen
 * @version CS56 F17
 * @see GhostHouse
 */

public class GhostHouseTest {
	
	@Test
	public void test_constructor_simple() {
		GhostHouse ghostHouse = new GhostHouse(new Location(0, 1), 3, 20);
		assertEquals(0, ghostHouse.getTopLeft().getX());
		assertEquals(1, ghostHouse.getTopLeft().getY());
		assertEquals(3, ghostHouse.getWidth());
	}
	
	@Test
	public void test_addGhost() {
		GhostHouse ghostHouse = new GhostHouse(new Location(0, 1), 3, 20);
		ghostHouse.addGhost(new Ghost(0, 0, 3, Ghost.GHOST1));
		//assertEquals(1, ghostHouse.getGhosts().size());
	}

}
