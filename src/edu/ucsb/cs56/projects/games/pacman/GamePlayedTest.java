package edu.ucsb.cs56.projects.games.pacman;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Test class for GamePlayed
 *
 * @author Kateryna Fomenko
 * @author Deanna Hartsook
 * @author Wei Tung Chen
 * @author Nicholas Duncan
 * @version CS56, F17
 * @see GamePlayed
 */

public class GamePlayedTest {

	/**
	 * Test case for GamePlayed constructor
	 */
	@Test
	public void test_constructor() {
		Date d = new Date();
		GamePlayed g1 = new GamePlayed("Game", d, 20);
		assertEquals("Game", g1.getName());
		assertEquals(d, g1.getDate());
		assertEquals(20, g1.getScore());
	}

	/**
	 * Test case for getName() method of GamePlayed
	 *
	 * @see GamePlayed#getName
	 */
	@Test
	public void test_getName() {
		Date d = new Date();
		GamePlayed g1 = new GamePlayed("Barbara", d, 2000);
		assertEquals("Barbara", g1.getName());
	}

	/**
	 * Test case for getDate() method of GamePlayed
	 *
	 * @see GamePlayed#getDate
	 */
	@Test
	public void test_getDate() {
		Date d = new Date();
		GamePlayed g1 = new GamePlayed("Barbara", d, 2000);
		assertEquals(d, g1.getDate());
	}

	/**
	 * Test case for getScore() method of GamePlayed
	 *
	 * @see GamePlayed#getScore
	 */
	@Test
	public void test_getScore() {
		Date d = new Date();
		GamePlayed g1 = new GamePlayed("Barbara", d, 2000);
		assertEquals(2000, g1.getScore());
	}
}
