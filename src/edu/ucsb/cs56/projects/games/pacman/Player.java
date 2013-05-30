package edu.ucsb.cs56.projects.games.pacman;

import java.awt.Event;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

/**
 * Class representing the player's character.
 * @author Daniel Ly
 */
public class Player extends JComponent{
    private final int pacanimdelay = 2;
    private final int pacmananimcount = 4;
    private final int pacmanspeed = 6;
    private Image pacman1, pacman2up, pacman2left, pacman2right, pacman2down,
    	pacman3up, pacman3down, pacman3left, pacman3right, pacman4up,
    	pacman4down, pacman4left, pacman4right;
    int pacanimcount = pacanimdelay;
    int pacanimdir = 1;
    int pacmananimpos = 0;
    int pacmanx, pacmany, pacmandx, pacmandy;
    int reqdx, reqdy, viewdx, viewdy;
    
    int blocksize, nrofblocks;
    short[] screendata;
    Board board;
    
    public Player(Board board){
    	super();
    	this.board = board;
    }
    
    /**
	 * Load game sprites from images folder
     */
    public void loadImages()
    {
	    try {
	        pacman1 = ImageIO.read(getClass().getResource("pacpix/pacman.png"));
	        pacman2up = ImageIO.read(getClass().getResource("pacpix/up1.png"));
	        pacman3up = ImageIO.read(getClass().getResource("pacpix/up2.png"));
	        pacman4up = ImageIO.read(getClass().getResource("pacpix/up3.png"));
	        pacman2down = ImageIO.read(getClass().getResource("pacpix/down1.png"));
	        pacman3down = ImageIO.read(getClass().getResource("pacpix/down2.png")); 
	        pacman4down = ImageIO.read(getClass().getResource("pacpix/down3.png"));
	        pacman2left = ImageIO.read(getClass().getResource("pacpix/left1.png"));
	        pacman3left = ImageIO.read(getClass().getResource("pacpix/left2.png"));
	        pacman4left = ImageIO.read(getClass().getResource("pacpix/left3.png"));
	        pacman2right = ImageIO.read(getClass().getResource("pacpix/right1.png"));
	        pacman3right = ImageIO.read(getClass().getResource("pacpix/right2.png"));
	        pacman4right = ImageIO.read(getClass().getResource("pacpix/right3.png"));
	    } 
        catch (IOException e) {
	        e.printStackTrace();
	    }
    }
    
    /**
     * 
     * @param blocksize
     * @param nrofblocks
     * @param screendata
     */
    public void setGrid(int blocksize, int nrofblocks, short[] screendata){
    	this.blocksize = blocksize;
    	this.nrofblocks = nrofblocks;
    	this.screendata = screendata;
    }
    
    /**
	 * Handles movement for the PacMan
     */
    public void movePacMan() {
        int pos;
        short ch;

        if (reqdx == -pacmandx && reqdy == -pacmandy) {
            pacmandx = reqdx;
            pacmandy = reqdy;
            viewdx = pacmandx;
            viewdy = pacmandy;
        }
        if (pacmanx % blocksize == 0 && pacmany % blocksize == 0) {
            pos = pacmanx / blocksize + nrofblocks * (int)(pacmany / blocksize);
            ch = screendata[pos];

            if ((ch & 16) != 0) {
                screendata[pos] = (short)(ch & 15);
                //score++;
            }

            if (reqdx != 0 || reqdy != 0) {
                if (!((reqdx == -1 && reqdy == 0 && (ch & 1) != 0) ||
                      (reqdx == 1 && reqdy == 0 && (ch & 4) != 0) ||
                      (reqdx == 0 && reqdy == -1 && (ch & 2) != 0) ||
                      (reqdx == 0 && reqdy == 1 && (ch & 8) != 0))) {
                    pacmandx = reqdx;
                    pacmandy = reqdy;
                    viewdx = pacmandx;
                    viewdy = pacmandy;
                }
            }

            // Check for standstill
            if ((pacmandx == -1 && pacmandy == 0 && (ch & 1) != 0) ||
                (pacmandx == 1 && pacmandy == 0 && (ch & 4) != 0) ||
                (pacmandx == 0 && pacmandy == -1 && (ch & 2) != 0) ||
                (pacmandx == 0 && pacmandy == 1 && (ch & 8) != 0)) {
                pacmandx = 0;
                pacmandy = 0;
            }
        }
        pacmanx = pacmanx + pacmanspeed * pacmandx;
        pacmany = pacmany + pacmanspeed * pacmandy;
    }

    /**
	 * Calls the appropriate draw method for the direction Pacman is facing
	 * @param g a Graphics object
	 */
    public void paint(Graphics g){
    	super.paint(g);
        Graphics2D g2d = (Graphics2D) g;

        doAnim();
        if (viewdx == -1)
            drawPacManLeft(g2d);
        else if (viewdx == 1)
            drawPacManRight(g2d);
        else if (viewdy == -1)
            drawPacManUp(g2d);
        else
            drawPacManDown(g2d);
    }
    
     /**
	 * Animates the Pacman sprite's direction as well as mouth opening and closing
     */
    public void doAnim() {
        pacanimcount--;
        if (pacanimcount <= 0) {
            pacanimcount = pacanimdelay;
            pacmananimpos = pacmananimpos + pacanimdir;
            if (pacmananimpos == (pacmananimcount - 1) || pacmananimpos == 0)
                pacanimdir = -pacanimdir;
        }
    }   
    /**
	 * Calls the appropriate draw method for the direction Pacman is facing
	 * @param g2d a Graphics2D object
	 */
    public void drawPacMan(Graphics2D g2d) {
        if (viewdx == -1)
            drawPacManLeft(g2d);
        else if (viewdx == 1)
            drawPacManRight(g2d);
        else if (viewdy == -1)
            drawPacManUp(g2d);
        else
            drawPacManDown(g2d);
    }
    
    /**
	 * Draws Pacman facing up
	 * @param g2d a Graphics2D object
	 * @param canvas A JComponent object to be drawn on
	 */
    public void drawPacManUp(Graphics2D g2d) {
        switch (pacmananimpos) {
	        case 1:
	            g2d.drawImage(pacman2up, pacmanx + 1, pacmany + 1, this);
	            break;
	        case 2:
	            g2d.drawImage(pacman3up, pacmanx + 1, pacmany + 1, this);
	            break;
	        case 3:
	            g2d.drawImage(pacman4up, pacmanx + 1, pacmany + 1, this);
	            break;
	        default:
	            g2d.drawImage(pacman1, pacmanx + 1, pacmany + 1, this);
	            break;
        }
    }


    /**
	 * Draws Pacman facing down
	 * @param g2d a Graphics2D object
	 * @param canvas A JComponent object to be drawn on
	 */
    public void drawPacManDown(Graphics2D g2d) {
        switch (pacmananimpos) {
	        case 1:
	            g2d.drawImage(pacman2down, pacmanx + 1, pacmany + 1, this);
	            break;
	        case 2:
	            g2d.drawImage(pacman3down, pacmanx + 1, pacmany + 1, this);
	            break;
	        case 3:
	            g2d.drawImage(pacman4down, pacmanx + 1, pacmany + 1, this);
	            break;
	        default:
	            g2d.drawImage(pacman1, pacmanx + 1, pacmany + 1, this);
	            break;
        }
    }

    /**
	 * Draws Pacman facing left
	 * @param g2d a Graphics2D object
	 * @param canvas A JComponent object to be drawn on
	 */
    public void drawPacManLeft(Graphics2D g2d) {
        switch (pacmananimpos) {
        case 1:
            g2d.drawImage(pacman2left, pacmanx + 1, pacmany + 1, this);
            break;
        case 2:
            g2d.drawImage(pacman3left, pacmanx + 1, pacmany + 1, this);
            break;
        case 3:
            g2d.drawImage(pacman4left, pacmanx + 1, pacmany + 1, this);
            break;
        default:
            g2d.drawImage(pacman1, pacmanx + 1, pacmany + 1, this);
            break;
        }
    }

    /**
	 * Draws Pacman facing right
	 * @param g2d a Graphics2D object
	 * @param canvas A JComponent object to be drawn on
	 */
    public void drawPacManRight(Graphics2D g2d) {
        switch (pacmananimpos) {
	        case 1:
	            g2d.drawImage(pacman2right, pacmanx + 1, pacmany + 1, this);
	            break;
	        case 2:
	            g2d.drawImage(pacman3right, pacmanx + 1, pacmany + 1, this);
	            break;
	        case 3:
	            g2d.drawImage(pacman4right, pacmanx + 1, pacmany + 1, this);
	            break;
	        default:
	            g2d.drawImage(pacman1, pacmanx + 1, pacmany + 1, this);
	            break;
        }
    }
    
    /**
	 * Class that handles key presses for game controls
     */
    class PlayerAdapter extends KeyAdapter {
        public void keyPressed(KeyEvent e) {

          int key = e.getKeyCode();

          switch (key){
	          case KeyEvent.VK_LEFT:
	            reqdx=-1;
	            reqdy=0;
	            break;
	          case KeyEvent.VK_RIGHT:
	            reqdx=1;
	            reqdy=0;
	          	break;
	          case KeyEvent.VK_UP:
	            reqdx=0;
	            reqdy=-1;
	            break;
	          case KeyEvent.VK_DOWN:
	            reqdx=0;
	            reqdy=1;
	            break;
	          default: break;
          }
        }
        
    	/**
         * Detects when a key is released
         * @param e a KeyEvent
    	 */
          public void keyReleased(KeyEvent e) {
              int key = e.getKeyCode();

              if (key == Event.LEFT || key == Event.RIGHT || 
                 key == Event.UP ||  key == Event.DOWN)
              {
                reqdx=0;
                reqdy=0;
              }
          }
      }
}
