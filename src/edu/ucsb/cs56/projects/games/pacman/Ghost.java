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

	@Override
	public void loadImages() {
	    try {
			ghost = ImageIO.read(getClass().getResource("pacpix/ghost.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Image getLifeImage() {
		return ghost;
	}

}
