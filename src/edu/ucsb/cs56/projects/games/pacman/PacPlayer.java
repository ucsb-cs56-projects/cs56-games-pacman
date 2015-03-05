package edu.ucsb.cs56.projects.games.pacman;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;

/**
 * Player controlled pacman character.
 *
 * @author Dario Castellanos
 * @author Daniel Ly
 * @author Kelvin Yang
 * @version CS56 W15
 */
public class PacPlayer extends Character {
    public final static int PACMAN = 1;
    public final static int MSPACMAN = 2;

    private final int pacanimdelay = 2;
    private final int pacmananimcount = 4;
    private final int pacmanspeed = 6;
    int pacanimcount = pacanimdelay;
    int pacanimdir = 1;
    int pacmananimpos = 0;

    // need these so that when pacman collides with wall and stops moving
    // he keeps facing wall instead of facing default position
    public int viewdx, viewdy;

    private Image pacman1up, pacman1left, pacman1right, pacman1down,
            pacman2up, pacman2left, pacman2right, pacman2down,
            pacman3up, pacman3down, pacman3left, pacman3right, pacman4up,
            pacman4down, pacman4left, pacman4right;

    /**
     * Constructor for PacPlayer class
     *
     * @param x the starting x coordinate of pacman
     * @param y the starting y coordinate of pacman
     */
    public PacPlayer(int x, int y) {
        super(x, y);
        speed = pacmanspeed;
        lives = 3;
        assetPath = "assets/pacman/";
        loadImages();
    }

    /**
     * Constructor for PacPlayer class
     *
     * @param x         the starting x coordinate of pacman
     * @param y         the starting y coordinate of pacman
     * @param playerNum int representing who the player is controlling
     */
    public PacPlayer(int x, int y, int playerNum) {
        super(x, y, playerNum);
        speed = pacmanspeed;
        lives = 3;
        if (playerNum == PACMAN) assetPath = "assets/pacman/";
        else if (playerNum == MSPACMAN) assetPath = "assets/mspacman/";
        loadImages();
    }

    public void resetPos()
    {
        super.resetPos();
        viewdx = -1;
        viewdy = 0;
    }

    public void death() {
        if (deathTimer <= 0) {
            lives--;
            deathTimer = 40;
            resetPos();
        }
        if (lives <= 0) {
            alive = false;
        }
    }

    /**
     * Moves character's current position with the board's collision
     *
     * @param grid The Grid to be used for collision
     */
    public void move(Grid grid) {
        if (deathTimer > 0) deathTimer--;
        short ch;

        //allows you to switch directions even when you are not in a grid
        if (reqdx == -dx && reqdy == -dy) {
            viewdx = dx = reqdx;
            viewdy = dy = reqdy;
        }

        if (x % Board.BLOCKSIZE == 0 && y % Board.BLOCKSIZE == 0) {

            //Tunnel effect
            x = ((x / Board.BLOCKSIZE) % Board.NUMBLOCKS) * Board.BLOCKSIZE;
            y = ((y / Board.BLOCKSIZE) % Board.NUMBLOCKS) * Board.BLOCKSIZE;

            ch = grid.screenData[y / Board.BLOCKSIZE][x / Board.BLOCKSIZE];

            //if pellet, eat and increase score
            if ((ch & 16) != 0) {
                //THIS WILL NOT WORK IF OTHER BITS ARE IMPORTANT TOO
                //IT REMOVES ALL OTHER BITS BESIDES 4 LSB
                grid.screenData[y / Board.BLOCKSIZE][x / Board.BLOCKSIZE] = (short) (ch & 15);
                Board.score++;
            }

            //TODO: implement fruits here.  Very easy just like pellet

            //passes key commands to movement
            if (!((reqdx == -1 && reqdy == 0 && (ch & 1) != 0) ||
                    (reqdx == 1 && reqdy == 0 && (ch & 4) != 0) ||
                    (reqdx == 0 && reqdy == -1 && (ch & 2) != 0) ||
                    (reqdx == 0 && reqdy == 1 && (ch & 8) != 0))) {
                viewdx = dx = reqdx;
                viewdy = dy = reqdy;
            }

            // Check for standstill, stop movement if hit wall
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
     *
     * @param g2d    a Graphics2D object
     * @param canvas A Jcomponent object to be drawn on
     */
    public void draw(Graphics2D g2d, JComponent canvas) {
        if (deathTimer % 5 > 3) return; // Flicker while invincibile
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
     *
     * @param g2d    a Graphics2D object
     * @param canvas A JComponent object to be drawn on
     */
    public void drawPacManUp(Graphics2D g2d, JComponent canvas) {
        switch (pacmananimpos) {
            case 1:
                g2d.drawImage(pacman2up, x + 4, y + 4, canvas);
                break;
            case 2:
                g2d.drawImage(pacman3up, x + 4, y + 4, canvas);
                break;
            case 3:
                g2d.drawImage(pacman4up, x + 4, y + 4, canvas);
                break;
            default:
                g2d.drawImage(pacman1up, x + 4, y + 4, canvas);
                break;
        }
    }

    /**
     * Draws Pacman facing down
     *
     * @param g2d    a Graphics2D object
     * @param canvas A JComponent object to be drawn on
     */
    public void drawPacManDown(Graphics2D g2d, JComponent canvas) {
        switch (pacmananimpos) {
            case 1:
                g2d.drawImage(pacman2down, x + 4, y + 4, canvas);
                break;
            case 2:
                g2d.drawImage(pacman3down, x + 4, y + 4, canvas);
                break;
            case 3:
                g2d.drawImage(pacman4down, x + 4, y + 4, canvas);
                break;
            default:
                g2d.drawImage(pacman1down, x + 4, y + 4, canvas);
                break;
        }
    }

    /**
     * Draws Pacman facing left
     *
     * @param g2d    a Graphics2D object
     * @param canvas A JComponent object to be drawn on
     */
    public void drawPacManLeft(Graphics2D g2d, JComponent canvas) {
        switch (pacmananimpos) {
            case 1:
                g2d.drawImage(pacman2left, x + 4, y + 4, canvas);
                break;
            case 2:
                g2d.drawImage(pacman3left, x + 4, y + 4, canvas);
                break;
            case 3:
                g2d.drawImage(pacman4left, x + 4, y + 4, canvas);
                break;
            default:
                g2d.drawImage(pacman1left, x + 4, y + 4, canvas);
                break;
        }
    }

    /**
     * Draws Pacman facing right
     *
     * @param g2d    a Graphics2D object
     * @param canvas A JComponent object to be drawn on
     */
    public void drawPacManRight(Graphics2D g2d, JComponent canvas) {
        switch (pacmananimpos) {
            case 1:
                g2d.drawImage(pacman2right, x + 4, y + 4, canvas);
                break;
            case 2:
                g2d.drawImage(pacman3right, x + 4, y + 4, canvas);
                break;
            case 3:
                g2d.drawImage(pacman4right, x + 4, y + 4, canvas);
                break;
            default:
                g2d.drawImage(pacman1right, x + 4, y + 4, canvas);
                break;
        }
    }

    /**
     * Moves character's current position with the board's collision
     *
     * @param grid The Grid to be used for collision
     */
    @Override
    public void moveAI(Grid grid, int tx, int ty) {
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
     * Handles key presses for game controls
     *
     * @param key Integer representing the key pressed
     */
    public void keyPressed(int key) {
        if (playerNum == PACMAN) {
            switch (key) {
                case KeyEvent.VK_LEFT:
                    reqdx = -1;
                    reqdy = 0;
                    break;
                case KeyEvent.VK_RIGHT:
                    reqdx = 1;
                    reqdy = 0;
                    break;
                case KeyEvent.VK_UP:
                    reqdx = 0;
                    reqdy = -1;
                    break;
                case KeyEvent.VK_DOWN:
                    reqdx = 0;
                    reqdy = 1;
                    break;
                default:
                    break;
            }
        } else if (playerNum == MSPACMAN) {
            switch (key) {
                case KeyEvent.VK_A:
                    reqdx = -1;
                    reqdy = 0;
                    break;
                case KeyEvent.VK_D:
                    reqdx = 1;
                    reqdy = 0;
                    break;
                case KeyEvent.VK_W:
                    reqdx = 0;
                    reqdy = -1;
                    break;
                case KeyEvent.VK_S:
                    reqdx = 0;
                    reqdy = 1;
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Load game sprites from images folder
     */
    @Override
    public void loadImages() {
        try {
            pacman1up = ImageIO.read(getClass().getResource(assetPath + "pacmanup.png"));
            pacman2up = ImageIO.read(getClass().getResource(assetPath + "up1.png"));
            pacman3up = ImageIO.read(getClass().getResource(assetPath + "up2.png"));
            pacman4up = ImageIO.read(getClass().getResource(assetPath + "up3.png"));
            pacman1down = ImageIO.read(getClass().getResource(assetPath + "pacmandown.png"));
            pacman2down = ImageIO.read(getClass().getResource(assetPath + "down1.png"));
            pacman3down = ImageIO.read(getClass().getResource(assetPath + "down2.png"));
            pacman4down = ImageIO.read(getClass().getResource(assetPath + "down3.png"));
            pacman1left = ImageIO.read(getClass().getResource(assetPath + "pacmanleft.png"));
            pacman2left = ImageIO.read(getClass().getResource(assetPath + "left1.png"));
            pacman3left = ImageIO.read(getClass().getResource(assetPath + "left2.png"));
            pacman4left = ImageIO.read(getClass().getResource(assetPath + "left3.png"));
            pacman1right = ImageIO.read(getClass().getResource(assetPath + "pacmanright.png"));
            pacman2right = ImageIO.read(getClass().getResource(assetPath + "right1.png"));
            pacman3right = ImageIO.read(getClass().getResource(assetPath + "right2.png"));
            pacman4right = ImageIO.read(getClass().getResource(assetPath + "right3.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the image used for displaying remaining lives
     *
     * @return image of pacman facing left
     */
    @Override
    public Image getLifeImage() {
        return pacman3left;
    }
}
