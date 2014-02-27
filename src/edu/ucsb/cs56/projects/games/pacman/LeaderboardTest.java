package edu.ucsb.cs56.projects.games.pacman;

import static org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import java.util.Date;

/** Test class for Leaderboard
 @author Kateryna Fomenko
 @author Deanna Hartsook
 @version CS56, Winter 2014
 @see Leaderboard
 */

public class LeaderboardTest{
	/** Test case for add(GamePlayed g) method of Leaderboard
	 * @see Leaderboard#add
	 */
    @Test
    public void test_add(){
		Leaderboard l = new Leaderboard();
        Date d = new Date();
        GamePlayed g = new GamePlayed("Barbara", d, 2000);
        l.add(g);
		assertEquals(g, l.get(0));
    }

    /** Test case for add(String name, Date d, int score) method of Leaderboard
	 * @see Leaderboard#add
	 */
    @Test
    public void test_add2(){
		Leaderboard l = new Leaderboard();
        Date d = new Date();
        l.add("Terry", d, 50);
		assertEquals(new GamePlayed("Terry", d, 50), l.get(0));
    }

    
    /** Test case for getTopThree() method of Leaderboard
	 * @see Leaderboard#getTopThree
	 */
    @Test
    public void getTopThree(){
		Leaderboard l = new Leaderboard();
        Date d = new Date();
        GamePlayed g1 = new GamePlayed("Barbara", d, 200);
        GamePlayed g2 = new GamePlayed("Nick", d, 100);
        GamePlayed g3 = new GamePlayed("Felicity", d, 250);
        l.add(g1); l.add(g2); l.add(g3);
		assertEquals("        Barbara   50 09/18/2013\n           Nick   30 12/01/2013\n          Katie   29 10/15/2013", l.getTopThree());
    }

    /** Test case for getPlayerTopThree() method of Leaderboard
	 * @see Leaderboard#getPlayerTopThree
	 */
    @Test
    public void getPlayerTopThree(){
		Leaderboard l = new Leaderboard();
        Date d = new Date();
        GamePlayed g1 = new GamePlayed("Barbara", d, 200);
        GamePlayed g2 = new GamePlayed("Barbara", d, 100);
        GamePlayed g3 = new GamePlayed("Barbara", d, 250);
        l.add(g1); l.add(g2); l.add(g3);
		assertEquals("        Barbara  250 09/18/2013\n       Barbara  200 12/01/2013\n        Barbara  100 10/15/2013", l.getPlayerTopThree("Barbara"));
    }

    
}