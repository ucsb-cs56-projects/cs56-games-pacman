
package edu.ucsb.cs56.projects.games.pacman;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//~ import java.awt.Color;


/**
* Represents the GUI elements of the Leaderboard class
* @author Kateryna Fomenko
* @author Deanna Hartsook
* @version CS56, Winter 2014
 */

public class LeaderboardGUI{
	private JLabel gameOver = new JLabel();
    private JFrame frame;
	private JPanel panel = new JPanel();
	private JTextField field = new JTextField();
	private	JButton submitBtn = new JButton("Submit");
    private JLabel heading = new JLabel();
    private JLabel playerScoresHeading = new JLabel();
	private JLabel topThree = new JLabel();
	private JLabel playersTopThree = new JLabel();

	private Leaderboard leaderBoard = new Leaderboard();

    /**
     * Draw a box with the Game Over text, that prompts user for his/her name
     * @param b a Board object
     */
	public void showEndGameScreen(Board b, Date d){
		
		this.leaderBoard.load();		
		
		//clear the panel in between games	
		this.panel.removeAll();
		
        this.frame = new JFrame("Leadboard");
        this.frame.setSize(380, 420);
		this.frame.setLocationRelativeTo(null);
        this.frame.setSize(250,250);
        this.frame.setVisible(true);
		
		//add GameOver label
		this.gameOver.setText("Game Over!");
        this.gameOver.setForeground(Color.white);
		this.gameOver.setPreferredSize(new Dimension(200,20));
		this.gameOver.setHorizontalAlignment(SwingConstants.CENTER);
				
		this.field.setText("Enter Your Name Here");
		this.submitBtn.addActionListener(new submitBtnListener(b.score, d));
   		this.panel.setBackground(new Color(102,0,0));


		this.frame.getContentPane().add(this.panel);
        this.panel.add(this.gameOver);
		this.panel.add(this.field);
		this.panel.add(this.submitBtn);
	 }

	/** submitBtnListener inner class - listens for the submit button to get pressed
     */
	
	private class submitBtnListener implements ActionListener{
		private int score;
		private Date d;
		public submitBtnListener(int score, Date d){
			this.score = score;
			this.d = d;
		}
		
		public void actionPerformed(ActionEvent ev){
			String userName = LeaderboardGUI.this.field.getText();
			LeaderboardGUI.this.showLeaderboard(userName, this.d, this.score);
		}
	}
	
    /*
     * Add GamePlayed to leaderboard and display the highest scores
     * @param username the player's name
     * @param d the date of the game
     * @param score the player's score
    */
    
	private void showLeaderboard(String userName, Date d, int score){
		//add and save new GamePlayed object
		this.leaderBoard.add(userName, d, score);
		this.leaderBoard.save();
        
		//removes old textField and submitBtn
        this.panel.removeAll();
        
		//get the values from Leaderboard
		String top3 = this.leaderBoard.getTopThree();
        System.out.println(top3);
		String playerTop3 = this.leaderBoard.getPlayerTopThree(userName);
        
        top3 = top3.replace("\n", " <br> ");
        playerTop3 = playerTop3.replace("\n", " <br> ");
        
		//~ System.out.println("top3"+ top3);
        //~ System.out.println("playerTop3"+ playerTop3);
        

        this.heading.setText("High Scores!");
		this.topThree.setText("<html> " + top3 + "</html>");
        this.heading.setForeground(Color.white);
		this.heading.setPreferredSize(new Dimension(200,20));
        this.heading.setHorizontalAlignment(SwingConstants.CENTER);
        this.topThree.setForeground(Color.white);
        
     
		
		this.playerScoresHeading.setText("Your Top Scores:");
		this.playersTopThree.setText("<html> " + playerTop3 + "</html>");		
        this.playerScoresHeading.setForeground(Color.white);
		this.playerScoresHeading.setPreferredSize(new Dimension(200,20));
		this.playerScoresHeading.setHorizontalAlignment(SwingConstants.CENTER);
        this.playersTopThree.setForeground(Color.white);
        
		//add JLabels to panel
        this.panel.add(this.heading);
		this.panel.add(this.topThree);
		
        this.panel.add(this.playerScoresHeading);
   		this.panel.add(this.playersTopThree);

		//this.panel.add(this.playersTopThree);
        this.frame.revalidate();
		this.frame.repaint();
		
	}
	
	public void setLeaderBoardFileName(String [] args){
		if(args.length > 1){
			System.out.println("Invalid Command Line Arguments--Please enter a single file name, or leave blank for default.");
			return;
		}
		if(args.length == 1){
			leaderBoard.setFileName(args[0]);			
		}else{
			leaderBoard.setFileName("");
		}

	}
	
}
