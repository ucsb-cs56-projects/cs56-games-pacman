package edu.ucsb.cs56.projects.games.pacman;

import java.awt.Event;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

/**
 * Player controlled pacman character.
 * @author Dario Castellanos
 * @author Daniel Ly
 * @version CS56 S13
 */
public class PacPlayer extends Character{
	public final static int PACMAN = 1;
	public final static int MSPACMAN = 2;
	
    private final int pacanimdelay = 2;
    private final int pacmananimcount = 4;
    private final int pacmanspeed = 6;
    private Image pacman1, pacman2up, pacman2left, pacman2right, pacman2down,
		pacman3up, pacman3down, pacman3left, pacman3right, pacman4up,
		pacman4down, pacman4left, pacman4right;
    int pacanimcount = pacanimdelay;
    int pacanimdir = 1;
    int pacmananimpos = 0;
    
    /**
     * Constructor for PacPlayer class
     * @param x the starting x coordinate of pacman
     * @param y the starting y coordinate of pacman
     */
    public PacPlayer(int x, int y){
    	super(x,y);
    	speed = pacmanspeed;
    	loadImages();
    }
    
    /**
     * Constructor for PacPlayer class
     * @param x the starting x coordinate of pacman
     * @param y the starting y coordinate of pacman
     * @param playerNum int representing who the player is controlling
     */
    public PacPlayer(int x, int y, int playerNum){
    	super(x,y, playerNum);
    	speed = pacmanspeed;
    	loadImages();
    }
    
    /**
     * Moves character's current position with the board's collision
     * @param grid The Grid to be used for collision
     */
    public void move(Grid grid) {
        int pos;
        short ch;

        if (reqdx == -dx && reqdy == -dy) {
            dx = reqdx;
            dy = reqdy;
            viewdx = dx;
            viewdy = dy;
        }
        if (x % grid.blockSize == 0 && y % grid.blockSize == 0) {
            pos = x / grid.blockSize + grid.nrOfBlocks * (int)(y / grid.blockSize);
            ch = grid.screenData[pos];

            if ((ch & 16) != 0) {
            	grid.screenData[pos] = (short)(ch & 15);
                Board.score++;
            }

            if (reqdx != 0 || reqdy != 0) {
                if (!((reqdx == -1 && reqdy == 0 && (ch & 1) != 0) ||
                      (reqdx == 1 && reqdy == 0 && (ch & 4) != 0) ||
                      (reqdx == 0 && reqdy == -1 && (ch & 2) != 0) ||
                      (reqdx == 0 && reqdy == 1 && (ch & 8) != 0))) {
                    dx = reqdx;
                    dy = reqdy;
                    viewdx = dx;
                    viewdy = dy;
                }
            }

            // Check for standstill
            if ((dx == -1 && dy == 0 && (ch & 1) != 0) ||
                (dx == 1 && dy == 0 && (ch & 4) != 0) ||
                (dx == 0 && dy == -1 && (ch & 2) != 0) ||
                (dx == 0 && dy == 1 && (ch & 8) != 0)) {
                dx = 0;
                dy = 0;
            }
        }
        move();
    }
    
    /**
     * Calls the appropriate draw method for the direction Pacman is facing
     * @param g2d a Graphics2D object
     * @param canvas A Jcomponent object to be drawn on
     */
    public void draw(Graphics2D g2d, JComponent canvas) {
    	doAnim();
        if (viewdx == -1)
            drawPacManLeft(g2d, canvas);
        else if (viewdx == 1)
            drawPacManRight(g2d, canvas);
        else if (viewdy == -1)
            drawPacManUp(g2d, canvas);
        else
            drawPacManDown(g2d, canvas);
    }
    
    /**
     * Draws Pacman facing up
     * @param g2d a Graphics2D object
     * @param canvas A JComponent object to be drawn on
     */
    public void drawPacManUp(Graphics2D g2d, JComponent canvas) {
        switch (pacmananimpos) {
	        case 1:
	            g2d.drawImage(pacman2up, x + 1, y + 1, canvas);
	            break;
	        case 2:
	            g2d.drawImage(pacman3up, x + 1, y + 1, canvas);
	            break;
	        case 3:
	            g2d.drawImage(pacman4up, x + 1, y + 1, canvas);
	            break;
	        default:
	            g2d.drawImage(pacman1, x + 1, y + 1, canvas);
	            break;
        }
    }

    /**
     * Draws Pacman facing down
     * @param g2d a Graphics2D object
     * @param canvas A JComponent object to be drawn on
     */
    public void drawPacManDown(Graphics2D g2d, JComponent canvas) {
        switch (pacmananimpos) {
	        case 1:
	            g2d.drawImage(pacman2down, x + 1, y + 1, canvas);
	            break;
	        case 2:
	            g2d.drawImage(pacman3down, x + 1, y + 1, canvas);
	            break;
	        case 3:
	            g2d.drawImage(pacman4down, x + 1, y + 1, canvas);
	            break;
	        default:
	            g2d.drawImage(pacman1, x + 1, y + 1, canvas);
	            break;
        }
    }

    /**
     * Draws Pacman facing left
     * @param g2d a Graphics2D object
     * @param canvas A JComponent object to be drawn on
     */
    public void drawPacManLeft(Graphics2D g2d, JComponent canvas) {
        switch (pacmananimpos) {
        case 1:
            g2d.drawImage(pacman2left, x + 1, y + 1, canvas);
            break;
        case 2:
            g2d.drawImage(pacman3left, x + 1, y + 1, canvas);
            break;
        case 3:
            g2d.drawImage(pacman4left, x + 1, y + 1, canvas);
            break;
        default:
            g2d.drawImage(pacman1, x + 1, y + 1, canvas);
            break;
        }
    }

    /**
     * Draws Pacman facing right
     * @param g2d a Graphics2D object
     * @param canvas A JComponent object to be drawn on
     */
    public void drawPacManRight(Graphics2D g2d, JComponent canvas) {
        switch (pacmananimpos) {
	        case 1:
	            g2d.drawImage(pacman2right, x + 1, y + 1, canvas);
	            break;
	        case 2:
	            g2d.drawImage(pacman3right, x + 1, y + 1, canvas);
	            break;
	        case 3:
	            g2d.drawImage(pacman4right, x + 1, y + 1, canvas);
	            break;
	        default:
	            g2d.drawImage(pacman1, x + 1, y + 1, canvas);
	            break;
        }
    }
    
    /**
     * Moves character's current position with the board's collision
     * @param grid The Grid to be used for collision
     * @param dx An array of integers used for randomized movement
     * @param dy An array of integers used for randomized movement
     */
    @Override
    public void moveAI(Grid grid, int[] dx, int[] dy) {	}   
    
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
     * Handles key presses for game controls
     * @param key Integer representing the key pressed
     */
    public void keyPressed(int key) {
    	if (playerNum == PACMAN){
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
    	else if (playerNum == MSPACMAN){
	        switch (key){
		          case KeyEvent.VK_A:
		            reqdx=-1;
		            reqdy=0;
		            break;
		          case KeyEvent.VK_D:
		            reqdx=1;
		            reqdy=0;
		          	break;
		          case KeyEvent.VK_W:
		            reqdx=0;
		            reqdy=-1;
		            break;
		          case KeyEvent.VK_S:
		            reqdx=0;
		            reqdy=1;
		            break;
		          default: break;
	        }
        }
    }
      
    /**
     * Detects when a key is released
     * @param key Integer representing the key released
     */
    public void keyReleased(int key) {
    	if ((key == Event.LEFT || key == Event.RIGHT || 
    			key == Event.UP ||  key == Event.DOWN) &&
    			playerNum == PACMAN) {
    		reqdx=0;
    		reqdy=0;
    	}
    	else if ((key == KeyEvent.VK_A || key == KeyEvent.VK_D || 
    			key == KeyEvent.VK_W ||  key == KeyEvent.VK_S) &&
    			playerNum == MSPACMAN) {
    		reqdx=0;
    		reqdy=0;
    	}
    }
    
    /**
     * Load game sprites from images folder
     */
	@Override
	public void loadImages() {
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
     * Returns the image used for displaying remaining lives
     * @return image of pacman facing left
     */
    @Override
    public Image getLifeImage() {
    	return pacman3left;
    }
}
