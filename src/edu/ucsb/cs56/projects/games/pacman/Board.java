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

    Character pacman = new PacPlayer(7 * blocksize, 11 * blocksize);
    final int maxghosts = 12;
    int nrofghosts = 6;

    int numBoardsCleared = 0;

    int pacsleft, deathcounter;
    int[] dx, dy;
    
    Ghost[] ghosts;
    Image ghost;

    //Real level data
    final short leveldata1[] =
    { 19, 26, 26, 26, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 22,
      21, 0,  0,  0,  17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
      21, 0,  0,  0,  17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20, 
      21, 0,  0,  0,  17, 16, 16, 24, 16, 16, 16, 16, 16, 16, 20, 
      17, 18, 18, 18, 16, 16, 20, 0,  17, 16, 16, 16, 16, 16, 20,
      17, 16, 16, 16, 16, 16, 20, 0,  17, 16, 16, 16, 16, 24, 20, 
      25, 16, 16, 16, 24, 24, 28, 0,  25, 24, 24, 16, 20, 0,  21, 
      1,  17, 16, 20, 0,  0,  0,  0,  0,  0,  0,  17, 20, 0,  21,
      1,  17, 16, 16, 18, 18, 22, 0,  19, 18, 18, 16, 20, 0,  21,
      1,  17, 16, 16, 16, 16, 20, 0,  17, 16, 16, 16, 20, 0,  21, 
      1,  17, 16, 16, 16, 16, 20, 0,  17, 16, 16, 16, 20, 0,  21,
      1,  17, 16, 16, 16, 16, 16, 18, 16, 16, 16, 16, 20, 0,  21,
      1,  17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20, 0,  21,
      1,  25, 24, 24, 24, 24, 24, 24, 24, 24, 16, 16, 16, 18, 20,
      9,  8,  8,  8,  8,  8,  8,  8,  8,  8,  25, 24, 24, 24, 28 };

    final short leveldata2[] = 
    {  0,  0, 19, 18, 18, 18, 18, 18, 18, 18, 18, 18, 22,  0,  0,
       0, 19, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 22,  0,
      19, 16, 16, 16, 16, 16, 24, 24, 24, 16, 16, 16, 16, 16, 22,
      17, 16, 16, 16, 16, 20,  0,  0,  0, 17, 16, 16, 16, 16, 20,
      17, 16, 16, 16, 16, 20,  0,  0,  0, 17, 16, 16, 16, 16, 20,
      17, 16, 16, 24, 24, 28,  0,  0,  0, 25, 24, 24, 16, 16, 20,
      17, 16, 20,  0,  0,  0,  0,  0,  0,  0,  0,  0, 17, 16, 20,
      17, 16, 20,  0,  0,  0,  0,  0,  0,  0,  0,  0, 17, 16, 20,
      17, 16, 20,  0,  0,  0,  0,  0,  0,  0,  0,  0, 17, 16, 20,
      17, 16, 16, 18, 18, 22,  0,  0,  0, 19, 18, 18, 16, 16, 20,
      17, 16, 16, 16, 16, 20,  0, 23,  0, 17, 16, 16, 16, 16, 20,
      17, 16, 16, 16, 16, 20,  0, 21,  0, 17, 16, 16, 16, 16, 20,
      25, 16, 16, 16, 16, 16, 18, 16, 18, 16, 16, 16, 16, 16, 28,
       0, 25, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 28,  0,
       0,  0, 25, 24, 24, 24, 24, 24, 24, 24, 24, 24, 28,  0,  0 };


    final short leveldata3[] = 
    { 19, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 22,
      17, 16, 24, 24, 16, 16, 16, 16, 16, 24, 24, 24, 24, 16, 20,
      17, 20,  0,  0, 17, 16, 16, 16, 20,  0,  0,  0,  0, 17, 20,
      17, 20,  0,  0, 17, 16, 16, 16, 20,  0,  0,  0,  0, 17, 20,
      17, 16, 18, 18, 16, 16, 16, 16, 16, 18, 22,  0,  0, 17, 20,
      17, 16, 16, 16, 16, 16, 24, 24, 24, 16, 20,  0,  0, 17, 20,
      17, 16, 16, 16, 16, 20,  0,  0,  0, 17, 16, 18, 18, 16, 20,
      17, 16, 16, 16, 16, 20,  0,  0,  0, 17, 16, 16, 16, 16, 20,
      17, 16, 24, 24, 16, 20,  0,  0,  0, 17, 16, 16, 16, 16, 20,
      17, 20,  0,  0, 17, 16, 18, 18, 18, 16, 16, 16, 16, 16, 20,
      17, 20,  0,  0, 25, 24, 16, 16, 16, 16, 16, 24, 24, 16, 20,
      17, 20,  0,  0,  0,  0, 17,  0, 16, 16, 20,  0,  0, 17, 20,
      17, 20,  0,  0,  0,  0, 17, 16, 16, 16, 20,  0,  0, 17, 20,
      17, 16, 18, 18, 18, 18, 16, 16, 16, 16, 16, 18, 18, 16, 20,
      25, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 28 };


    final short leveldata4[] =
    { 19, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 22,
      17, 16, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 16, 20,
      17, 20,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 17, 20,
      17, 16, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 16, 20,
      17, 16, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 16, 20,
      17, 20,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 17, 20,
      17, 16, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 16, 20,
      17, 16, 24, 24, 24, 24, 24, 16, 24, 24, 24, 24, 24, 16, 20,
      17, 20,  0,  0,  0,  0,  0, 21,  0,  0,  0,  0,  0, 17, 20,
      17, 20,  0, 19, 18, 18, 18, 16, 18, 18, 18, 22,  0, 17, 20,
      17, 20,  0, 17, 16, 16, 16, 16, 16, 16, 16, 20,  0, 17, 20,
      17, 20,  0, 25, 24, 24, 24, 24, 24, 24, 24, 28,  0, 17, 20,
      17, 20,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 17, 20,
      17, 16, 18, 18, 18, 18, 22,  0, 19, 18, 18, 18, 18, 16, 20,
      25, 24, 24, 24, 24, 24, 28,  0, 25, 24, 24, 24, 24, 24, 28};

      final short leveldata5[] =
	{  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
	   0,  0, 19, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 22,  0,
	   0,  0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,  0,
	   0,  0, 17, 16, 16, 16, 24, 24, 24, 24, 24, 24, 16, 20,  0,
	   0,  0, 17, 16, 16, 20,  0,  0,  0,  0,  0,  0, 17, 20,  0,
	   0,  0, 17, 16, 16, 20,  0,  0,  0,  0,  0,  0, 25, 28,  0,
	   0,  0, 17, 16, 16, 16, 18, 18, 18, 18, 22,  0,  0,  0,  0,
	   0,  0, 17, 16, 16, 16, 16, 16, 16, 16, 20,  0,  0,  0,  0,
	   0,  0, 25, 24, 16, 16, 16, 16, 16, 16, 16, 18, 18, 18, 22,
	   0,  0,  0,  0, 17, 16, 16, 16, 16, 16, 24, 16, 16, 16, 20,
	   0,  0,  0,  0, 17, 16, 16, 16, 16, 20,  0, 17, 16, 16, 20,
	  19, 18, 18, 26, 16, 24, 24,  0, 24, 28,  0, 17, 16, 16, 20,
	  17, 16, 20,  0, 21,  0,  0, 21,  0,  0,  0, 17, 16, 16, 20,
	  17, 16, 20,  0, 21,  0, 19, 16, 22,  0,  0, 25, 24, 24, 28,
	  25, 24, 28,  0, 29,  0, 25, 24, 28,  0,  0,  0,  0,  0,  0};

    final int validspeeds[] = { 1, 2, 3, 4, 6, 8 };
    final int maxspeed = 6;

    int currentspeed = 3;
    short[] screendata;
    Timer timer;

    /**
     * Constructor for Board object
     */
    public Board() {
        addKeyListener(new TAdapter());

        screendata = new short[nrofblocks * nrofblocks];
        mazecolor = new Color(5, 100, 5);
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
	    pacman.move(blocksize, nrofblocks, screendata);
	    pacman.draw(g2d, this);
            moveGhosts(g2d);
	    detectCollision(ghosts, pacman);
            checkMaze();
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
    			g.drawString((i + 1) + ": " + scores.get(i), (int) scrsize / 4 + blocksize, (int) (scrsize - (scrsize / 3) + (i * fm.getHeight())));
    		else if (i < 10)
    			g.drawString((i + 1) + ": " + scores.get(i), (int) scrsize / 2 + blocksize, (int) (scrsize - (scrsize / 3) + ((i - 5) * fm.getHeight())));
    	}
    }
    
    /**
     * Checks if there are any pellets left for Pacman to eat, and restarts the game on the next board in a  higher difficulty if finished
     */
    public void checkMaze() {
        short i = 0;
        boolean finished = true;

        while (i < nrofblocks * nrofblocks && finished) {
            if ((screendata[i] & 48) != 0)
                finished = false;
            i++;
        }

        if (finished) {
            score += 50;
            this.numBoardsCleared++;

            if (nrofghosts < maxghosts)
                nrofghosts++;
            if (currentspeed < maxspeed)
                currentspeed++;
            levelInit();
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
     * Movement logic for ghost enemies. Ghosts will move one square and then decide whether to change directions or not
     * @param g2d a Graphics2D object
     */
    public void moveGhosts(Graphics2D g2d) {
        int pos;
        int count;

        for (short i = 0; i < nrofghosts; i++) {
            if (ghosts[i].x % blocksize == 0 && ghosts[i].y % blocksize == 0) {
                pos = ghosts[i].x / blocksize + nrofblocks * (int)(ghosts[i].y / blocksize);

                count = 0;
                if ((screendata[pos] & 1) == 0 && ghosts[i].dx != 1) {
                    dx[count] = -1;
                    dy[count] = 0;
                    count++;
                }
                if ((screendata[pos] & 2) == 0 && ghosts[i].dy != 1) {
                    dx[count] = 0;
                    dy[count] = -1;
                    count++;
                }
                if ((screendata[pos] & 4) == 0 && ghosts[i].dx != -1) {
                    dx[count] = 1;
                    dy[count] = 0;
                    count++;
                }
                if ((screendata[pos] & 8) == 0 && ghosts[i].dy != -1) {
                    dx[count] = 0;
                    dy[count] = 1;
                    count++;
                }
 
                if (count == 0) {
                    if ((screendata[pos] & 15) == 15) {
                        ghosts[i].dx = 0;
                        ghosts[i].dy = 0;
                    } else {
                        ghosts[i].dx = -ghosts[i].dx;
                        ghosts[i].dy = -ghosts[i].dy;
                    }
                } else {
                    count = (int)(Math.random() * count);
                    if (count > 3)
                        count = 3;
                    ghosts[i].dx = dx[count];
                    ghosts[i].dy = dy[count];
                }
            }
	    ghosts[i].move();
            ghosts[i].draw(g2d, this);
        }
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
     * Draws the maze that serves as a playing field.
     * @param g2d a Graphics2D object
     */
    public void drawMaze(Graphics2D g2d) {
        short i = 0;
        int x, y;

        for (y = 0; y < scrsize; y += blocksize) {
            for (x = 0; x < scrsize; x += blocksize) {
                g2d.setColor(mazecolor);
                g2d.setStroke(new BasicStroke(2));

                if ((screendata[i] & 1) != 0) // draws left
                {
                    g2d.drawLine(x, y, x, y + blocksize - 1);
                }
                if ((screendata[i] & 2) != 0) // draws top
                {
                    g2d.drawLine(x, y, x + blocksize - 1, y);
                }
                if ((screendata[i] & 4) != 0) // draws right
                {
                    g2d.drawLine(x + blocksize - 1, y, x + blocksize - 1,
                                 y + blocksize - 1);
                }
                if ((screendata[i] & 8) != 0) // draws bottom
                {
                    g2d.drawLine(x, y + blocksize - 1, x + blocksize - 1,
                                 y + blocksize - 1);
                }
                if ((screendata[i] & 16) != 0) // draws point
                {
                    g2d.setColor(dotcolor);
                    g2d.fillRect(x + 11, y + 11, 2, 2);
                }
                i++;
            }
        }
    }

    /**
     * Initialize game variables
     */
    public void gameInit() {
        pacsleft = 3;
        levelInit();
        score = 0;
        nrofghosts = 6;
        currentspeed = 3;
    }

    /**
     * Initialize level
     */
    public void levelInit() {
        for (int i = 0; i < nrofblocks * nrofblocks; i++) {
            if (numBoardsCleared%3 == 0)
                screendata[i] = leveldata1[i];
            else if (numBoardsCleared%3 == 1)
                screendata[i] = leveldata2[i];
            else if (numBoardsCleared%3 == 2)
                screendata[i] = leveldata3[i];
            else if (numBoardsCleared%5 == 3)
                screendata[i] = leveldata4[i];
            else if (numBoardsCleared%3 == 4)
                screendata[i] = leveldata5[i];
        }
        levelContinue();
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

      drawMaze(g2d);
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
