
package edu.ucsb.cs56.projects.games.pacman;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

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
	private JTextField field = new JTextField(20);
	private	JButton submitBtn = new JButton("Submit");
	private JLabel heading = new JLabel();
	private JLabel playerScoresHeading = new JLabel();
	private JLabel topThree = new JLabel();
	private JLabel playersTopThree = new JLabel();
	private JLabel scoreLabel = new JLabel();
	private BufferedImage pacmanImage;
	private JLabel picLabel;
	
	private Leaderboard leaderBoard = new Leaderboard();
	private static submitBtnListener submitListener;

	/**Constructor for LeaderboardGui--initializes the JComponents of leaderboardgui
	 * 
	 */
	public LeaderboardGUI(){
		try{
			pacmanImage = ImageIO.read(getClass().getResource("assets/pacman/right2.png"));
			int width = 5*pacmanImage.getWidth();
			int height = 5*pacmanImage.getHeight();
			Image pac = pacmanImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
			picLabel = new JLabel(new ImageIcon(pac));
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		this.frame = new JFrame("Leadboard");
		this.frame.setSize(380, 420);
		this.frame.setLocationRelativeTo(null);
		this.frame.setVisible(true);

		//add GameOver label
		this.gameOver.setText("Game Over!");
		this.gameOver.setFont(new Font("Serif", Font.PLAIN, 45));
		this.gameOver.setForeground(Color.black);
		this.gameOver.setAlignmentX(this.gameOver.CENTER_ALIGNMENT);
		
	
		this.panel.setBackground(new Color(224,224,224));
		this.panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
		this.frame.setVisible(false);


	}

    /**
     * Draw a box with the Game Over text, that prompts user for his/her name
     * @param b a Board object
     */
	public void showEndGameScreen(int score, Date d){
		//clear the panel in between games	
		this.panel.removeAll();

		this.frame.getContentPane().add(this.panel);
		this.scoreLabel.setText("Your Score: "+ score);
		this.scoreLabel.setFont(new Font("Serif", Font.PLAIN, 24));
		this.scoreLabel.setForeground(Color.black);
		this.scoreLabel.setAlignmentX(this.scoreLabel.CENTER_ALIGNMENT);
		
		//add spacer
		this.panel.add(Box.createRigidArea(new Dimension(0,15)));
		this.panel.add(this.gameOver);
		//add spacer
		this.panel.add(Box.createRigidArea(new Dimension(0,10)));
		this.panel.add(this.scoreLabel);
		//add spacer
		this.panel.add(Box.createRigidArea(new Dimension(0,10)));
		this.field.setText("Enter Your Name Here");
		this.field.setMaximumSize( this.field.getPreferredSize() );	
		this.panel.add(this.field);
		//add spacer
		this.panel.add(Box.createRigidArea(new Dimension(0,10)));
		this.panel.add(this.submitBtn);
		//add spacer
		this.panel.add(Box.createRigidArea(new Dimension(0,30)));
		
		picLabel.setAlignmentX(picLabel.CENTER_ALIGNMENT);		
		this.panel.add(picLabel);

		this.leaderBoard.load();
		this.submitListener = new submitBtnListener(score, d);
		this.submitBtn.addActionListener(this.submitListener);		
		this.submitBtn.setAlignmentX(this.submitBtn.CENTER_ALIGNMENT);		
		this.frame.setVisible(true);
		this.frame.revalidate();
		this.frame.repaint();
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
	
    /**
     * Add GamePlayed to leaderboard and display the highest scores
     * @param username the player's name
     * @param d the date of the game
     * @param score the player's score
    */
    
	private void showLeaderboard(String userName, Date d, int score){
		submitBtn.removeActionListener(this.submitListener);
		//add and save new GamePlayed object
		this.leaderBoard.add(userName, d, score);
		this.leaderBoard.save();

		//removes old textField and submitBtn
		this.panel.removeAll();

		//get the values from Leaderboard
		String top3 = this.leaderBoard.getTopThree();
		//System.out.println(top3);
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
	/** setLeaderBoardFileName method- sets the fileName in the instance of the leaderboard class
	* @param args - a string version of the command line arguments
	*/
	public void setLeaderBoardFileName(String args){
		leaderBoard.setFileName(args);
	}
	
}
