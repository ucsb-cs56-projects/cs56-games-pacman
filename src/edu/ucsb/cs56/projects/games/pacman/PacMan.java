package edu.ucsb.cs56.projects.games.pacman;

import javax.swing.*;


/**
 * A Pacman arcade game remake<p>
 * The version of the code by Jan Bodnar may be found at http://zetcode.com/tutorials/javagamestutorial/pacman/
 *
 * @author Brian Postma
 * @author Jan Bodnar
 * @author Dario Castellanos
 * @author Deanna Hartsook
 * @author Kateryna Fomenko
 * @version CS56 W14
 */

public class PacMan extends JFrame {
    /**
     * Constructor for PacMan object
     *
     * @param args - string version of the command line arguments inputed by the user
     */
    public PacMan(String args) {
        Board b = new Board();
        add(b);
        b.callLeaderboardMain(args);
        setTitle("Pacman");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(380, 420);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Main function for PacMan Class that tests to see if there are command line arguments
     *
     * @param args[] -- the command line arguments
     */
    public static void main(String[] args) {

        if (args.length == 0) {
            new PacMan("");
            return;
        } else if (args[1].equals("${arg1}") || args.length == 1) {
            new PacMan(args[0]);
            return;
        }
        //Note: prints out all args, for debugging purposes
        //~ for(int i=0; i< args.length; i++){System.out.println("args["+i+"] :"+ args[i]);}
        System.out.println("Invalid Command Line Arguments--Please enter a single file name, or leave blank for default.");
        return;

    }
}
