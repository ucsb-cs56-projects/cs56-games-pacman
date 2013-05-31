package edu.ucsb.cs56.projects.games.pacman;

import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

public class Ghost extends Character{
	private Image ghost;

	public Ghost(int x, int y, int speed) {
		super(x, y);
		this.speed = speed;
		loadImages();
	}

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
			ghost = ImageIO.read(getClass().getResource("pacpix/ghost.png"));
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
	public void keyPressed(int key) { }
	
  	/**
     * Handles key releases for game controls
     * @param key Integer representing the key released
  	 */
	@Override
	public void keyReleased(int key) { }
	
	/**
	 * Moves character's current position with the board's collision
	 * @param blockSize The size of each block in pixels
	 * @param nrOfBlocks The number of blocks
	 * @param screenData The contents of the blocks
	 */
	@Override
	public void move(int blockSize, int nrOfBlocks, short[] screendata) { }

}
