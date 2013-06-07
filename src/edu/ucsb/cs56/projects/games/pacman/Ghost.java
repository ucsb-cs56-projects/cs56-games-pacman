package edu.ucsb.cs56.projects.games.pacman;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;

import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

/**
 * Class representing enemy ghosts in single player mode
 * and player ghosts in multiplayer mode
 * @author Dario Castellanos Anaya
 * @author Daniel Ly
 * @version CS56, S13
 */
public class Ghost extends Character{
    public static final int GHOST1 = 1;
    public static final int GHOST2 = 2;
	
    private Image ghost;
    
    public Ghost(int x, int y, int speed) {
	super(x, y);
	this.speed = speed;
	loadImages();
    }
    
    public Ghost(int x, int y, int speed, int playerNum){
    	super(x, y, playerNum);
    	this.speed = speed;
    	loadImages();
    }
    
    public void death() { }

    /**
     * Draws the ghost
     * @param g2d a Graphics2D object
     * @param canvas A Jcomponent object to be drawn on
     */
    @Override
    public void draw(Graphics2D g, JComponent canvas) {
        g.drawImage(ghost, x + 1, y + 1, canvas);
    }
    
    /**
     * Load game sprites from images folder
     */
    @Override
    public void loadImages() {
	try {
	    ghost = ImageIO.read(getClass().getResource("assets/ghost.png"));
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
    
    /**
     * Returns the image used for displaying remaining lives
     * @return Image of character
     */
    @Override
    public Image getLifeImage() {
	return ghost;
    }
    
    /**
     * Handles key presses for game controls
     * @param key Integer representing the key pressed
     */
    @Override
    public void keyPressed(int key) {
    	if (playerNum == GHOST1){
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
    	else if (playerNum == GHOST2){
	    switch (key){
	    case KeyEvent.VK_NUMPAD4:
		reqdx=-1;
		reqdy=0;
		break;
	    case KeyEvent.VK_NUMPAD6:
		reqdx=1;
		reqdy=0;
		break;
	    case KeyEvent.VK_NUMPAD8:
		reqdx=0;
		reqdy=-1;
		break;
	    case KeyEvent.VK_NUMPAD5:
		reqdx=0;
		reqdy=1;
		break;
	    default: break;
	    }
        }
    }
    
    /**
     * Handles key releases for game controls
     * @param key Integer representing the key released
     */
    @Override
    public void keyReleased(int key) { }
    
    /**
     * Moves character's current position with the board's collision
     * @param grid The Grid to be used for collision
     */
    @Override
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
     * Moves character's current position with the board's collision
     * @param grid The Grid to be used for collision
     * @param dx An array of integers used for randomized movement
     * @param dy An array of integers used for randomized movement
     */
    @Override
    public void moveAI(Grid grid, int[] dx, int[] dy) {	
        int pos;
        int count;
	
        if (this.x % grid.blockSize == 0 && this.y % grid.blockSize == 0) {
	    pos = x / grid.blockSize + grid.nrOfBlocks * (int)(this.y / grid.blockSize);
	    
	    count = 0;
	    if ((grid.screenData[pos] & 1) == 0 && this.dx != 1) {
		dx[count] = -1;
		dy[count] = 0;
		count++;
	    }
	    if ((grid.screenData[pos] & 2) == 0 && this.dy != 1) {
		dx[count] = 0;
		dy[count] = -1;
		count++;
	    }
	    if ((grid.screenData[pos] & 4) == 0 && this.dx != -1) {
		dx[count] = 1;
		dy[count] = 0;
		count++;
	    }
	    if ((grid.screenData[pos] & 8) == 0 && this.dy != -1) {
		dx[count] = 0;
		dy[count] = 1;
		count++;
	    }
	    
	    if (count == 0) {
		if ((grid.screenData[pos] & 15) == 15) {
		    this.dx = 0;
		    this.dy = 0;
		} else {
		    this.dx = -this.dx;
		    this.dy = -this.dy;
		}
	    } else {
		count = (int)(Math.random() * count);
		if (count > 3)
		    count = 3;
		this.dx = dx[count];
		this.dy = dy[count];
	    }
        }
	move();
    }
}
