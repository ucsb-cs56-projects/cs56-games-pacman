package edu.ucsb.cs56.projects.games.pacman;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.Timer;

/**
   Playing field for a Pacman arcade game remake that keeps track of all relevant data and handles game logic.<p>
   The version of the code by Jan Bodnar may be found at http://zetcode.com/tutorials/javagamestutorial/pacman/
   @author Brian Postma
   @author Jan Bodnar
   @author Dario Castellanos
   @author Brandon Newman
   @author Daniel Ly
   @version CS56 S13
 */

public class Board extends JPanel implements ActionListener {
    public final static int SINGLEPLAYER = 1;
    
    public static int score;
    ScoreLoader sl = new ScoreLoader("highScores.txt");
    Grid grid;
    Dimension d;
    Font smallfont = new Font("Helvetica", Font.BOLD, 14);

    FontMetrics fmsmall, fmlarge;
    Image ii;
    Color dotcolor = new Color(192, 192, 0);
    Color mazecolor;

    int gameType;
    boolean ingame = false;
    boolean dying = false;

    final int blocksize = 24;
    final int nrofblocks = 15;
    final int scrsize = nrofblocks * blocksize;

    Character pacman;
    final int maxghosts = 12;
    int nrofghosts = 6;

    int numBoardsCleared = 0;

    int pacsleft, deathcounter;
    int[] dx, dy;
    
    Ghost[] ghosts;
    Image ghost;

    final int validspeeds[] = { 1, 2, 3, 4, 6, 8 };
    final int maxspeed = 6;

    int currentspeed = 3;
    Timer timer;

    /**
     * Constructor for Board object
     */
    public Board() {
        addKeyListener(new TAdapter());
        grid = new Grid();
        pacman = new PacPlayer(7 * blocksize, 11 * blocksize);
        setFocusable(true);

        d = new Dimension(400, 400);

        setBackground(Color.black);
        setDoubleBuffered(true);

        ghosts = new Ghost[maxghosts];
        dx = new int[4];
        dy = new int[4];
        timer = new Timer(40, this);
        timer.start();
    }
    
    /**
     * Called by the system
     */
    public void addNotify() {
        super.addNotify();
        gameInit();
    }

    /**
     * Main game logic loop
     * @param g2d a Graphics 2D object 
     */
    public void playGame(Graphics2D g2d) {
        if (dying) {
            death();
        } 
	else {
	    pacman.move(grid);
	    pacman.draw(g2d, this);
	    for (int i=0; i<nrofghosts; i++){
	    	ghosts[i].moveAI(grid, dx, dy);
	    	ghosts[i].draw(g2d, this);
	    }
	    detectCollision(ghosts, pacman);
            if (grid.checkMaze()){
                score += 50;
                numBoardsCleared++;

                if (nrofghosts < maxghosts)
                    nrofghosts++;
                if (currentspeed < maxspeed)
                    currentspeed++;
                grid.levelInit(numBoardsCleared);
                levelContinue();
            }
        }
    }

    /**
     * Draw a message box with the text "Press s to start." in the center of the screen
     * @param g a Graphics2D object
     */
    public void showIntroScreen(Graphics2D g) {
        g.setColor(new Color(0, 32, 48));
        g.fillRect(50, scrsize / 2 - 30, scrsize - 100, 50);
        g.setColor(Color.white);
        g.drawRect(50, scrsize / 2 - 30, scrsize - 100, 50);

        String s = "Press s to start.";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = this.getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(s, (scrsize - metr.stringWidth(s)) / 2, scrsize / 2);
        drawHighScores(g);
    }

    /**
     * Display the current score on the bottom right of the screen
     * @param g a Graphics object
     */
    public void drawScore(Graphics2D g) {
        int i;
        String s;

        g.setFont(smallfont);
        g.setColor(new Color(96, 128, 255));
        s = "Score: " + score;
        g.drawString(s, scrsize / 2 + 96, scrsize + 16);
        for (i = 0; i < pacsleft; i++) {
            g.drawImage(pacman.getLifeImage(), i * 28 + 8, scrsize + 1, this);
        }
    }

    /**
     * Displays a list of scores on the bottom of the screen
     * @param g a Graphics2D object
     */
    public void drawHighScores(Graphics2D g) {
    	ArrayList<Integer> scores = sl.loadScores();
    	g.setFont(smallfont);
    	FontMetrics fm = this.getFontMetrics(smallfont);
    	
    	g.setColor(new Color(0, 32, 48));
        g.fillRect((int) scrsize / 4 , (int) scrsize - (scrsize / 3) - fm.getAscent(), (int) (scrsize / 2), blocksize * 4);
        g.setColor(Color.white);
        g.drawRect((int) scrsize / 4, (int) scrsize - (scrsize / 3) - fm.getAscent(), (int) (scrsize / 2), blocksize * 4);
        
        g.setColor(new Color(96, 128, 255));
    	for (int i = 0; i < scores.size(); i++) {
    		if (i < 5)
    			g.drawString((i + 1) + ": " + scores.get(i), (int) scrsize / 4 + blocksize, 
				     (int) (scrsize - (scrsize / 3) + (i * fm.getHeight())));
    		else if (i < 10)
    			g.drawString((i + 1) + ": " + scores.get(i), (int) scrsize / 2 + blocksize, 
				     (int) (scrsize - (scrsize / 3) + ((i - 5) * fm.getHeight())));
    	}
    }

    /**
     * Decrements number of lives left when player touches a ghost and reinitializes player location.
     * End the game if remaining lives reaches 0.
     */
    public void death() {
        pacsleft--;
        if (pacsleft == 0) {
        	if (score  > 1) sl.writeScore(score);
            ingame = false;
            numBoardsCleared = 0;
        }
        levelContinue();
    }

    /**
     * Detects when ghosts and pacman collide
     * @param ghosts An array of Ghost
     * @param pacman Character controlled by player
     */
    public void detectCollision(Ghost[] ghosts, Character pacman) {
	for(int i = 0; i < nrofghosts; i++) {
	    if (pacman.x > (ghosts[i].x - 12) && pacman.x < (ghosts[i].x + 12) &&
		pacman.y > (ghosts[i].y - 12) && pacman.y < (ghosts[i].y + 12) &&
		ingame) {
	    
		dying = true;
		deathcounter = 64;
	    }
	}	
    }

    /**
     * Initialize game variables
     */
    public void gameInit() {
        pacsleft = 3;
        grid.levelInit(numBoardsCleared);
        levelContinue();
        score = 0;
        nrofghosts = 6;
        currentspeed = 3;
    }
    
    /**
     * Initialize Pacman and ghost position/direction
     */
    public void levelContinue() {
    	int dx = 1;
        int random;
        
        for (short i = 0; i < nrofghosts; i++) {
        	random = (int)(Math.random() * (currentspeed + 1));
            if (random > currentspeed)
            	random = currentspeed;
        	ghosts[i] = new Ghost(4 * blocksize, 4 * blocksize, random);
        	ghosts[i].dx = dx;
            dx = -dx;
            ghosts[i].speed = validspeeds[random];
        }

        pacman.reset();
        dying = false;
    }

    /**
     * Paint graphics onto screen
     * @param g a Graphics object
     */
    public void paint(Graphics g) {
      super.paint(g);

      Graphics2D g2d = (Graphics2D) g;

      g2d.setColor(Color.black);
      g2d.fillRect(0, 0, d.width, d.height);

      grid.drawMaze(g2d);
      drawScore(g2d);
      if (ingame)
        playGame(g2d);
      else
        showIntroScreen(g2d);

      g.drawImage(ii, 5, 5, this);
      Toolkit.getDefaultToolkit().sync();
      g.dispose();
    }

    /**
     * Class that handles key presses for game controls
     */
    class TAdapter extends KeyAdapter {

	/**
	 * Detects when a key is pressed.<p>
	 * In-game: Changes Pacman's direction of movement with the arrow keys. Quit game by pressing the escape key.<p> 
	 * Not in-game: Press the 'S' key to begin the game.
	 * @param e a KeyEvent
	 */
        public void keyPressed(KeyEvent e) {

          int key = e.getKeyCode();

          if (ingame)
          {
        	if (gameType == SINGLEPLAYER)
        		pacman.keyPressed(key);
        	
            if (key == KeyEvent.VK_ESCAPE && timer.isRunning()) {
              ingame=false;
            }
            else if (key == KeyEvent.VK_PAUSE) {
                if (timer.isRunning())
                    timer.stop();
                else timer.start();
            }
          }
          else {
            if (key == 's' || key == 'S') {
              ingame=true;
              gameType = SINGLEPLAYER;
              gameInit();
            }
          }
      }

	/**
	 * Detects when a key is released
	 * @param e a KeyEvent
	 */
	    public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		if (gameType == SINGLEPLAYER)
		    pacman.keyReleased(key);
	    }
    }
    
    /**
     * Repaint the graphics each frame
     * @param e an ActionEvent
     */
    public void actionPerformed(ActionEvent e) {
        repaint();  
    }
}
