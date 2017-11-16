package edu.ucsb.cs56.projects.games.pacman;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test class for PacPlayer
 *
 * @author Nicholas Duncan
 * @author Wei Tung Chen
 * @version CS56 F17
 * @see PacPlayer
 */

public class PacPlayerTest {
	
	@Test
	public void test_constructor_simple() {
		PacPlayer player = new PacPlayer(0, 9);
		assertEquals(0, player.x);
		assertEquals(9, player.y);
		assertEquals(3, player.lives);
		assertEquals(3, player.direction);
		assertEquals(4, player.speed);
	}
	
	@Test
	public void test_constructor_complex() {
		PacPlayer player = new PacPlayer(3, 0, PacPlayer.MSPACMAN, new Grid());
		assertEquals(3, player.x);
		assertEquals(0, player.y);
		assertEquals(3, player.lives);
		assertEquals(3, player.direction);
		assertEquals(4, player.speed);
		assertEquals(PacPlayer.MSPACMAN, player.playerNum);
	}

	@Test
	public void test_resetPos() {
		PacPlayer player = new PacPlayer(1, 2);
		player.x ++;
		player.y ++;
		player.resetPos();
		assertEquals(player.startX, player.x);
		assertEquals(player.startY, player.y);
	}

	@Test
	public void test_death() {
		PacPlayer player = new PacPlayer(3, 4);
		player.death();
		assertEquals(true, player.alive);
		assertEquals(2, player.lives);
	}
}
