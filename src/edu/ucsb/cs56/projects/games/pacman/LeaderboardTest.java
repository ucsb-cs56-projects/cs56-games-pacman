package edu.ucsb.cs56.projects.games.pacman;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Test class for Leaderboard
 *
 * @author Kateryna Fomenko
 * @author Deanna Hartsook
 * @author Kelvin Yang
 * @version CS56, Winter 2015
 * @see Leaderboard
 */

public class LeaderboardTest {
	/**
	 * Test case for add(GamePlayed g) method of Leaderboard
	 *
	 * @see Leaderboard#add
	 */
	@Test
	public void test_add() {
		Leaderboard l = new Leaderboard();
		Date d = new Date();
		GamePlayed g = new GamePlayed("Barbara", d, 2000);
		l.add(g);
		assertEquals(g, l.first());
	}

	/**
	 * Test case for add(String name, Date d, int score) method of Leaderboard
	 *
	 * @see Leaderboard#add
	 */
	@Test
	public void test_add2() {
		Leaderboard l = new Leaderboard();
		Date d = new Date();
		l.add("Terry", d, 50);
		assertEquals(new GamePlayed("Terry", d, 50), l.first());
	}

	/**
	 * Test case for getTopThree() method of Leaderboard
	 *
	 * @see Leaderboard#getTopThree
	 */
	@Test
	public void getTopThree() {
		Leaderboard l = new Leaderboard();
		Date d = new Date();
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		String date = df.format(d);
		GamePlayed g1 = new GamePlayed("Barbara", d, 200);
		GamePlayed g2 = new GamePlayed("Nick", d, 100);
		GamePlayed g3 = new GamePlayed("John", d, 75);
		GamePlayed g4 = new GamePlayed("Felicity", d, 250);
		l.add(g1);
		l.add(g2);
		l.add(g3);
		l.add(g4);
		String result = String.format("%15s %5s (%10s)\n", "Felicity", 250, date);
		result += String.format("%15s %5s (%10s)\n", "Barbara", 200, date);
		result += String.format("%15s %5s (%10s)\n", "Nick", 100, date);
		assertEquals(result, l.getTopThree());
	}

	/**
	 * Test case for getPlayerTopThree() method of Leaderboard
	 *
	 * @see Leaderboard#getPlayerTopThree
	 */
	@Test
	public void getPlayerTopThree() {
		Leaderboard l = new Leaderboard();
		Date d = new Date();
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		String date = df.format(d);
		GamePlayed g1 = new GamePlayed("Barbara", d, 200);
		GamePlayed g2 = new GamePlayed("Barbara", d, 100);
		GamePlayed g3 = new GamePlayed("Barbara", d, 250);
		GamePlayed g4 = new GamePlayed("NotBarbara", d, 9000);
		GamePlayed g5 = new GamePlayed("NotBarbara", d, 1);
		l.add(g1);
		l.add(g2);
		l.add(g3);
		l.add(g4);
		l.add(g5);
		String result = String.format("%15s %5s (%10s)\n", "Barbara", 250, date);
		result += String.format("%15s %5s (%10s)\n", "Barbara", 200, date);
		result += String.format("%15s %5s (%10s)\n", "Barbara", 100, date);
		assertEquals(result, l.getPlayerTopThree("Barbara"));
	}
}
