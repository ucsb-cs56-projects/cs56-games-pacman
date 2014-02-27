package edu.ucsb.cs56.projects.games.pacman;

import javax.swing.JFrame;

import edu.ucsb.cs56.projects.games.pacman.Board;


/**
   A Pacman arcade game remake<p>
   The version of the code by Jan Bodnar may be found at http://zetcode.com/tutorials/javagamestutorial/pacman/
   @author Brian Postma
   @author Jan Bodnar
   @author Dario Castellanos
   @version CS56 S13
 */

public class PacMan extends JFrame
{

    /**
       Constructor for PacMan object       
     */
    public PacMan()
    {
		add(new Board());
		setTitle("Pacman");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(380, 420);
		setLocationRelativeTo(null);
		setVisible(true);
    }

    public static void main(String[] args){
		if(args.length == 1){
			//right now leaderBoard is not accessible to PacMan, but it should be:
			//~ leaderBoard.setFileName(args[0]);
		}else if(args.length > 1){
			System.out.println("Invalid Command Line Arguments--Please enter a single file name, or leave blank for default.");
		}

		new PacMan();
    }
}
