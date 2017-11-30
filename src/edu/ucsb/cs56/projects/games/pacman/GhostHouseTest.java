package edu.ucsb.cs56.projects.games.pacman;

import org.junit.Test;
import java.util.List;
import java.util.LinkedList;

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
	public void test_constructor() {
		GhostHouse ghostHouse = new GhostHouse(new Location(0, 1), 3, 20);
		assertEquals(0, ghostHouse.getTopLeft().getX());
		assertEquals(1, ghostHouse.getTopLeft().getY());
		assertEquals(3, ghostHouse.getWidth());
	}
	
	@Test
	public void test_addGhost() {
		GhostHouse ghostHouse = new GhostHouse(new Location(0, 1), 3, 20);
		ghostHouse.addGhost(new Ghost(0, 0, 3, Ghost.GHOST1));
		assertEquals(1, ghostHouse.getGhosts().size());
	}

	@Test
	public void test_addGhosts() {
		List<Ghost> ghosts = new LinkedList<Ghost>();
		ghosts.add(new Ghost(0, 0, 3, Ghost.GHOST1));
		ghosts.add(new Ghost(0, 0, 3, Ghost.GHOST1));
		ghosts.add(new Ghost(0, 0, 3, Ghost.GHOST1));

		GhostHouse ghostHouse = new GhostHouse(new Location(0, 1), 3, 20);
		ghostHouse.addGhosts(ghosts);
		assertEquals(3, ghostHouse.getGhosts().size());
	}

	@Test
	public void test_update() {
		List<Ghost> ghosts = new LinkedList<Ghost>();
		ghosts.add(new Ghost(0, 0, 3, Ghost.GHOST1));
		ghosts.add(new Ghost(0, 0, 3, Ghost.GHOST1));
		ghosts.add(new Ghost(0, 0, 3, Ghost.GHOST1));

		GhostHouse ghostHouse = new GhostHouse(new Location(0, 1), 3, 20);
		ghostHouse.addGhosts(ghosts);
		ghostHouse.update(); //should release one ghost
		assertEquals(2, ghostHouse.getGhosts().size());
	}
}
