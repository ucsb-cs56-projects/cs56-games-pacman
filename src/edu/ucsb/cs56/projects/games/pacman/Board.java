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
    public final static int COOPERATIVE = 2;
    public final static int VERSUS = 3;
    
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

    Character pacman, msPacman, ghost1, ghost2;
    Character[] pacmen, playerGhosts;
    final int maxghosts = 12;
    int nrofghosts = 6;

    int numBoardsCleared = 0;

    int[] dx, dy;
    
    Ghost[] ghosts;

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
        pacman = new PacPlayer(7 * blocksize, 11 * blocksize, PacPlayer.PACMAN);
        msPacman = new PacPlayer(7 * blocksize, 11 * blocksize, PacPlayer.MSPACMAN);
	ghost1 = new Ghost(4 * blocksize, 4 * blocksize, 4, Ghost.GHOST1);
	ghost2 = new Ghost(4 * blocksize, 4 * blocksize, 4, Ghost.GHOST2);
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
	if (!checkAlive(pacmen)){
	    gameOver();
	}	    
	else {
	    switch (gameType) {
	    case SINGLEPLAYER:
		if (pacman.alive){
		    pacman.move(grid);
		    pacman.draw(g2d, this);
		}
		for (int i=0; i<nrofghosts; i++){
		    ghosts[i].moveAI(grid, dx, dy);
		    ghosts[i].draw(g2d, this);
		}
		detectCollision(ghosts, pacman);
		break;
	    case COOPERATIVE:
		if (pacman.alive){
		    pacman.move(grid);
		    pacman.draw(g2d, this);
		}
		if (msPacman.alive){
		    msPacman.move(grid);
		    msPacman.draw(g2d, this);
		}
		for (int i=0; i<nrofghosts; i++){
		    ghosts[i].moveAI(grid, dx, dy);
		    ghosts[i].draw(g2d, this);
		}
		detectCollision(ghosts, pacman, msPacman);
		break;
	    case VERSUS:
		if (pacman.alive){
		    pacman.move(grid);
		    pacman.draw(g2d, this);
		}
		for (Character ghost: playerGhosts){
		    ghost.move(grid);
		    ghost.draw(g2d, this);
		}
		if (score >= 149){
		    score = 0;
		    numBoardsCleared++;
		    grid.levelInit(numBoardsCleared);
		    levelContinue();
		}
		detectCollision(playerGhosts, pacman);
		break;
	}
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
        g.fillRect(50, scrsize / 2 - 30, scrsize - 100, 65);
        g.setColor(Color.white);
        g.drawRect(50, scrsize / 2 - 30, scrsize - 100, 65);

        String s = "Press s for single player";
        String d = "Press d for Co-Op";
	String f = "Press f for Versus";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = this.getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(s, (scrsize - metr.stringWidth(s)) / 2, scrsize / 2 - metr.getHeight()/2);
        g.drawString(d, (scrsize - metr.stringWidth(d)) / 2, scrsize / 2 + metr.getHeight()/2);
	g.drawString(f, (scrsize - metr.stringWidth(f)) / 2, scrsize / 2 + metr.getHeight()*3/2);
        drawHighScores(g);
    }

    /**
     * Display the current score on the bottom right of the screen
     * @param g a Graphics object
     */
    public void drawScore(Graphics2D g) {
        int i;
	int pelletsLeft;
        String s;
	String p;

        g.setFont(smallfont);
        g.setColor(new Color(96, 128, 255));
	if (gameType == VERSUS) {
	    pelletsLeft = 149 - score;
	    p = "Pellets left: " + pelletsLeft;
	    g.drawString(p, scrsize / 2 + 56, scrsize + 16);
	}
	else {
	    s = "Score: " + score;
	    g.drawString(s, scrsize / 2 + 96, scrsize + 16);
	}        
	for (i = 0; i < pacman.lives; i++) {
            g.drawImage(pacman.getLifeImage(), i * 28 + 8, scrsize + 1, this);
        }
	if (gameType == COOPERATIVE){
	    for (i = 0; i < msPacman.lives; i++) {
		g.drawImage(msPacman.getLifeImage(), i * 28 + 108, scrsize + 1, this);
	    }
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
     * End the game if remaining lives reaches 0.
     */
    public void gameOver() {
	if(gameType != VERSUS) {
	    if (score  > 1)
		sl.writeScore(score);
	}
	ingame = false;
        numBoardsCleared = 0;
    }

    /**
     * Detects when ghosts and pacman collide
     * @param ghosts An array of Ghost
     * @param pacmen Characters controlled by player
     */
    public void detectCollision(Character[] ghosts, Character... pacmen) {
    	for (Character pacman: pacmen){
	    if (gameType == VERSUS){
		for (Character ghost: ghosts){
		    if (pacman.x > (ghost.x - 12) && pacman.x < (ghost.x + 12) &&
			pacman.y > (ghost.y - 12) && pacman.y < (ghost.y + 12) &&
			ingame) {

			pacman.death();
		    }
		}
	    }
	    else {
		for(int i = 0; i < nrofghosts; i++) {
		    if (pacman.x > (ghosts[i].x - 12) && pacman.x < (ghosts[i].x + 12) &&
			pacman.y > (ghosts[i].y - 12) && pacman.y < (ghosts[i].y + 12) &&
			ingame) {
			
			pacman.death();
		    }
		}
	    }
	}	
    }

    /**
     * Returns true if any pacman is alive, returns false if they
     * are all dead
     * @param pacmen Any number of characters to check
     * @return true if any surviving, false if all dead
     */
    public boolean checkAlive(Character... pacmen) {
	int nAlive = 0;
	for (Character pacman: pacmen) {
	    if (pacman.alive)
		nAlive++;
	}
	if (nAlive == 0)
	    return false;
	else
	    return true;
    }
    /**
     * Initialize game variables
     */
    public void gameInit() {
	switch (gameType) {
	case SINGLEPLAYER:
	    pacmen = new Character[1];
	    pacmen[0] = pacman;
	    pacman.reset();
	    break;
	case COOPERATIVE:
	    pacmen = new Character[2];
	    pacmen[0] = pacman;
	    pacmen[1] = msPacman;
	    pacman.reset();
	    msPacman.reset();
	    break;
	case VERSUS:
	    pacmen = new Character[1];
	    pacmen[0] = pacman;
	    pacman.reset();
	    playerGhosts = new Character[2];
	    playerGhosts[0] = ghost1;
	    playerGhosts[1] = ghost2;
	    ghost1.reset();
	    ghost2.reset();
	    break;
	}
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
	switch (gameType) {
	case SINGLEPLAYER:
	    pacman.resetPos();
	    break;
	case COOPERATIVE:
	    pacman.resetPos();
	    msPacman.resetPos();
	    break;
	case VERSUS:
	    pacman.resetPos();
	    for (Character ghost: playerGhosts){
		ghost.resetPos();
		if (numBoardsCleared == 3){
		    ghost.speed = 6;
		}
	    }
	    break;
	}
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
      		switch (gameType) {
			case SINGLEPLAYER:
			    pacman.keyPressed(key);
			    break;
			case COOPERATIVE:
			    pacman.keyPressed(key);
			    msPacman.keyPressed(key);
			    break;
			case VERSUS:
			    pacman.keyPressed(key);
			    ghost1.keyPressed(key);
			    ghost2.keyPressed(key);
			    break;
		}
        	
            if (key == KeyEvent.VK_ESCAPE && timer.isRunning()) {
              ingame=false;
	      numBoardsCleared = 0;
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
	      else if (key == 'd' || key == 'D') {
		  ingame=true;
		  gameType = COOPERATIVE;
		  gameInit();
	      }
	      else if (key == 'f' || key == 'F') {
		  ingame=true;
		  gameType = VERSUS;
		  gameInit();
	      }
          }
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
