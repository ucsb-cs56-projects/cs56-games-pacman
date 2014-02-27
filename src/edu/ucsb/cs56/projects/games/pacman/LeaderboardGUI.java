
package edu.ucsb.cs56.projects.games.pacman;

import javax.swing.*;
import java.util.Date;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;


/**
* Represents the GUI elements of the Leaderboard class
* @author Kateryna Fomenko
* @author Deanna Hartsook
* @version CS56, Winter 2014
 */

public class LeaderboardGUI{

	private JPanel panel = new JPanel();
	private JTextField field = new JTextField();
	private	JButton submitBtn = new JButton("Submit");
	private JLabel topThree = new JLabel();
	private JLabel playersTopThree = new JLabel();
	
	private Leaderboard leaderBoard = new Leaderboard();

    /**
     * Draw a box with the Game Over text, that prompts user for his/her name
     * @param b a Board object
     */
	public void showEndGameScreen(Board b, Date d){
		this.leaderBoard.load();
		b.add(this.panel);
		this.panel.setBackground(new Color(102,0,0));
		this.panel.add(this.field);
		//note: add Game Over Label
		this.field.setText("Enter Your Name Here");
		this.panel.add(this.submitBtn);
		this.submitBtn.addActionListener(new submitBtnListener(b.score, d));
		//~ return this.panel;
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
	
	public void showLeaderboard(String userName, Date d, int score){
		//add and save new GamePlayed object
		this.leaderBoard.add(userName, d, score);
		this.leaderBoard.save();
		//removes old textField and submitBtn
		this.panel.remove(this.field);	
		this.panel.remove(this.submitBtn); 
		//get the values from Leaderboard
		String top3 = this.leaderBoard.getTopThree();
		//~ String playerTop3 = this.l.getPlayerTopThree();
		//add values to JLabels
		this.topThree.setText(top3);
		//~ this.playersTopThree.setText(playerTop3);
		//add JLabels to panel
		this.panel.add(this.topThree);	
		this.panel.add(this.playersTopThree);	
		
		
	}
	
	
	
}
