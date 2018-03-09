package edu.ucsb.cs56.projects.games.pacman;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


import java.awt.Color;


/**
 * Represents the GUI elements of the Leaderboard class that holds high scores
 *
 * @author Kateryna Fomenko
 * @author Deanna Hartsook
 * @version CS56, Winter 2014
 */

public class LeaderboardGUI {
	private static submitBtnListener submitListener;
	private JLabel gameOver = new JLabel();
	private JFrame frame;
	private JPanel panel = new JPanel();
	private JTextField field = new JTextField(20);

	private JButton submitBtn = new JButton("Submit");
	private JLabel heading = new JLabel();
	private JLabel playerScoresHeading = new JLabel();
	private JLabel topThree = new JLabel();
	private JLabel playersTopThree = new JLabel();
	private JLabel scoreLabel = new JLabel();
	private BufferedImage pacmanImage;
	private JLabel picLabel;
	private JButton single = new JButton("Single Player");
	private JButton coop = new JButton("Cooperative");
	private JButton versus = new JButton("Versus");
	private JButton playAgain = new JButton("Play Again");
	private Leaderboard leaderBoardSingle = new Leaderboard();
	private Leaderboard leaderBoardCoop = new Leaderboard();
	private Leaderboard leaderBoardVersus = new Leaderboard();
	private playAgainBtnListener playAgainListener;
	private singBtnListener SingleListener;
	private coopBtnListener CoopListener;
	private versBtnListener VersusListener;

	private int WIDTH = 380;
	private int HEIGHT = 420;
	private int displayNum = 1;
	private String name = "";
	/**
	 * Constructor for LeaderboardGui--initializes the JComponents of leaderboardgui
	 */
	public LeaderboardGUI() {
		try {
			pacmanImage = ImageIO.read(getClass().getResource("assets/pacman/right2.png"));
			int width = 5 * pacmanImage.getWidth();
			int height = 5 * pacmanImage.getHeight();
			Image pac = pacmanImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
			picLabel = new JLabel(new ImageIcon(pac));
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.frame = new JFrame("Leadboard");
		this.frame.setSize(WIDTH, HEIGHT);
		this.frame.setLocationRelativeTo(null);
		this.frame.setVisible(true);

		//add GameOver label
		this.gameOver.setText("Game Over!");
		this.gameOver.setFont(new Font("Serif", Font.PLAIN, 45));
		this.gameOver.setForeground(Color.black);
		this.gameOver.setAlignmentX(this.gameOver.CENTER_ALIGNMENT);

		this.panel.setBackground(new Color(224, 224, 224));
		this.panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		this.frame.setVisible(false);

	}

	/**
	 * Draw a box with the Game Over text, that prompts user for his/her name
	 *
	 * @param score the player's score
	 * @param d the date of the game
	 */
	public void showEndGameScreen(int score, Date d, int type) {
		//clear the panel in between games
		this.panel.removeAll();

		this.frame.getContentPane().add(this.panel);
		this.scoreLabel.setText("Your Score: " + score);
		this.scoreLabel.setFont(new Font("Serif", Font.PLAIN, 24));
		this.scoreLabel.setForeground(Color.black);
		this.scoreLabel.setAlignmentX(this.scoreLabel.CENTER_ALIGNMENT);

		//add spacer
		this.panel.add(Box.createRigidArea(new Dimension(0, 15)));
		this.panel.add(this.gameOver);
		//add spacer
		this.panel.add(Box.createRigidArea(new Dimension(0, 10)));
		this.panel.add(this.scoreLabel);
		//add spacer
		this.panel.add(Box.createRigidArea(new Dimension(0, 10)));
		this.field.setText("Your Name Here");
        this.field.setMaximumSize(this.field.getPreferredSize());
		this.panel.add(this.field);
		this.field.requestFocus();
		field.selectAll();
        field.setSelectionColor(Color.YELLOW);

		//add spacer
		this.panel.add(Box.createRigidArea(new Dimension(0, 10)));
		this.panel.add(this.submitBtn);
		//add spacer
		this.panel.add(Box.createRigidArea(new Dimension(0, 30)));

		picLabel.setAlignmentX(picLabel.CENTER_ALIGNMENT);
		this.panel.add(picLabel);

		this.leaderBoardSingle.load();
		this.leaderBoardCoop.load();
		this.leaderBoardVersus.load();
		this.submitListener = new submitBtnListener(score, d, type);
		this.submitBtn.addActionListener(this.submitListener);
		frame.getRootPane().setDefaultButton(submitBtn);
		this.submitBtn.setAlignmentX(this.submitBtn.CENTER_ALIGNMENT);
		this.frame.setVisible(true);
		this.frame.revalidate();
		this.frame.repaint();

	}


	/**
	 * Add GamePlayed to leaderboard and display the highest scores
	 *
	 * @param userName the player's name
	 * @param d        the date of the game
	 * @param score    the player's score
	 */

	private void showLeaderboard(String userName, Date d, int score, int type) {
		name = userName;
		submitBtn.removeActionListener(this.submitListener);
		//add and save new GamePlayed object
		if(type == 1) {
			this.leaderBoardSingle.add(userName, d, score);
			this.leaderBoardSingle.save();
		}
		else if(type == 2) {
			this.leaderBoardCoop.add(userName, d, score);
			this.leaderBoardCoop.save();
		}
		else {
			this.leaderBoardVersus.add(userName, d, score);
			this.leaderBoardVersus.save();
		}
		//removes old textField and submitBtn
		this.panel.removeAll();

		//get the values from Leaderboard
		showDifferentLeaderboard(displayNum);
		
		playerScoresHeading.setText("Your Top Scores:");

		heading.setFont(new Font("Serif", Font.PLAIN, 32));
		topThree.setFont(new Font("Serif", Font.PLAIN, 18));
		playerScoresHeading.setFont(new Font("Serif", Font.PLAIN, 32));
		playersTopThree.setFont(new Font("Serif", Font.PLAIN, 18));

		heading.setForeground(new Color(0, 51, 0));
		playerScoresHeading.setForeground(new Color(0, 51, 0));
		topThree.setForeground(new Color(0, 51, 0));
		playersTopThree.setForeground(new Color(0, 51, 0));

		playAgainListener = new playAgainBtnListener();
		playAgain.addActionListener(playAgainListener);
		
		SingleListener = new singBtnListener(score, d, type);
		single.addActionListener(SingleListener);

		CoopListener = new coopBtnListener(score, d, type);
		coop.addActionListener(CoopListener);

		VersusListener = new versBtnListener(score, d, type);
		versus.addActionListener(VersusListener);

		//add JLabels to panel
		JPanel tabPanel = new JPanel(new FlowLayout());
		tabPanel.add(single);
		tabPanel.add(coop);
		tabPanel.add(versus);
		panel.add(tabPanel);

		JPanel headingPanel = new JPanel();
		headingPanel.add(heading);
		panel.add(headingPanel);

		JPanel topThreePanel = new JPanel();
		topThreePanel.add(topThree);
		panel.add(topThreePanel);

		JPanel PlayerHeadingPanel = new JPanel();
		PlayerHeadingPanel.add(playerScoresHeading);
		panel.add(PlayerHeadingPanel);

		JPanel playerThreePanel = new JPanel();
		playerThreePanel.add(playersTopThree);
		panel.add(playerThreePanel);

		JPanel buttonPanel = new JPanel();
		buttonPanel.add(playAgain);
		panel.add(buttonPanel);

		frame.revalidate();
		frame.repaint();

	}

	/**
	 * setLeaderBoardFileName method- sets the fileName in the instance of the leaderboard class
	 *
	 * @param files - a string version of the command line arguments
	 */
	public void setLeaderBoardFileName(String [] files) {
		leaderBoardSingle.setFileName(files[0]);
		leaderBoardCoop.setFileName(files[1]);
		leaderBoardVersus.setFileName(files[2]);
	}

	/**
	 * submitBtnListener inner class - listens for the submit button to get pressed
	 */
	private class submitBtnListener implements ActionListener {
		private int score;
		private Date d;
		private int type;
		
		public submitBtnListener(int score, Date d, int type) {
			this.score = score;
			this.d = d;
			this.type = type;
		}

		public void actionPerformed(ActionEvent ev) {
			String userName = LeaderboardGUI.this.field.getText();
			LeaderboardGUI.this.showLeaderboard(userName, this.d, this.score, this.type);
		}
	}
	private class playAgainBtnListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			frame.dispose();
		}
	}

	private class singBtnListener implements ActionListener {
		private int score;
		private Date d;
		private int type;

		public singBtnListener(int score, Date d, int type) {
			this.score = score;
			this.d = d;
			this.type = type;
		}

		public void actionPerformed(ActionEvent e) {
			displayNum = 1;
			String userName = LeaderboardGUI.this.field.getText();
			showDifferentLeaderboard(1);
			//showLeaderboard(userName, this.d, this.score, this.type);
		}
	}
	
	 private class coopBtnListener implements ActionListener {
                private int score;
		private Date d;
		private int type;

		public coopBtnListener(int score, Date d, int type) {
			this.score = score;
			this.d = d;
			this.type = type;
		}

		public void actionPerformed(ActionEvent e) {
                        displayNum = 2;
			String userName = LeaderboardGUI.this.field.getText();
			showDifferentLeaderboard(2);
			//showLeaderboard(userName, this.d, this.score, this.type);
                }
        }

	 private class versBtnListener implements ActionListener {
                private int score;
		private Date d;
		private int type;

		public versBtnListener(int score, Date d, int type) {
			this.score = score;
			this.d = d;
			this.type = type;
		}

		public void actionPerformed(ActionEvent e) {
                        displayNum = 3;
			String userName = LeaderboardGUI.this.field.getText();
			showDifferentLeaderboard(3);
			//showLeaderboard(userName, this.d, this.score, this.type);
                }
        }
        /**
	 * A method to choose which leaderboard (single player, coop, or versus)
	 * gets displayed on the leaderboard
	 * 
	 * @param displayNum parameter that indicates which gamemode's leaderboard is displayed
	 */
	private void showDifferentLeaderboard(int displayNum) {
		String top3S = this.leaderBoardSingle.getTopThree();
		String playerTop3S = this.leaderBoardSingle.getPlayerTopThree(name);
		
		String top3C = this.leaderBoardCoop.getTopThree();
		String playerTop3C = this.leaderBoardCoop.getPlayerTopThree(name);
		
		String top3V = this.leaderBoardVersus.getTopThree();
		String playerTop3V = this.leaderBoardVersus.getPlayerTopThree(name);
		
		top3S = top3S.replace("\n", " <br> ");
		playerTop3S = playerTop3S.replace("\n", " <br> ");
		
		top3C = top3C.replace("\n", " <br> ");
		playerTop3C = playerTop3C.replace("\n", " <br> ");
		
		top3V = top3V.replace("\n", " <br> ");
		playerTop3V = playerTop3V.replace("\n", " <br> ");

		heading.setText("High Scores!");
		switch(displayNum)
		{	
			case 1:	
				topThree.setText("<html>" + top3S + "</html>");
				playersTopThree.setText("<html> " + playerTop3S + "</html>");
				break;
			case 2:
				topThree.setText("<html>" + top3C + "</html>");
				playersTopThree.setText("<html> " + playerTop3C + "</html>");
				break;
			case 3:
				topThree.setText("<html>" + top3V + "</html>");
				playersTopThree.setText("<html> " + playerTop3V + "</html>");
				break;
			default: break;

		}
	}
}
