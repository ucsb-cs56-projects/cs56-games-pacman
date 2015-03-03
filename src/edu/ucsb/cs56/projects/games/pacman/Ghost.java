package edu.ucsb.cs56.projects.games.pacman;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;

/**
 * Class representing enemy ghosts in single player mode
 * and player ghosts in multiplayer mode
 *
 * @author Dario Castellanos Anaya
 * @author Daniel Ly
 * @author Kelvin Yang
 * @version CS56, W15
 */
public class Ghost extends Character {
    public static final int GHOST1 = 1;
    public static final int GHOST2 = 2;

    private Image ghost;

    public Ghost(int x, int y, int speed) {
        super(x, y);
        this.speed = speed;
        assetPath = "assets/";
        loadImages();
    }

    public Ghost(int x, int y, int speed, int playerNum) {
        super(x, y, playerNum);
        this.speed = speed;
        assetPath = "assets/";
        loadImages();
    }

    /**
     * Handles character's death
     */
    public void death() {
    }

    /**
     * Draws the ghost
     *
     * @param g2d    a Graphics2D object
     * @param canvas A Jcomponent object to be drawn on
     */
    @Override
    public void draw(Graphics2D g, JComponent canvas) {
        g.drawImage(ghost, x + 4, y + 4, canvas);
    }

    /**
     * Load game sprites from images folder
     */
    @Override
    public void loadImages() {
        try {
            if (playerNum == GHOST1) ghost = ImageIO.read(getClass().getResource(assetPath + "ghostred.png"));
            else if (playerNum == GHOST2) ghost = ImageIO.read(getClass().getResource(assetPath + "ghostblue.png"));
            else ghost = ImageIO.read(getClass().getResource(assetPath + "ghostred.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the image used for displaying remaining lives
     *
     * @return Image of character
     */
    @Override
    public Image getLifeImage() {
        return ghost;
    }

    /**
     * Handles key presses for game controls
     *
     * @param key Integer representing the key pressed
     */
    @Override
    public void keyPressed(int key) {
        if (playerNum == GHOST1) {
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
        } else if (playerNum == GHOST2) {
            switch (key) {
                case KeyEvent.VK_NUMPAD4:
                    reqdx = -1;
                    reqdy = 0;
                    break;
                case KeyEvent.VK_NUMPAD6:
                    reqdx = 1;
                    reqdy = 0;
                    break;
                case KeyEvent.VK_NUMPAD8:
                    reqdx = 0;
                    reqdy = -1;
                    break;
                case KeyEvent.VK_NUMPAD5:
                    reqdx = 0;
                    reqdy = 1;
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Moves character's current position with the board's collision
     *
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
            ch = grid.screenData[y / grid.blockSize][x / grid.blockSize];

            if (reqdx != 0 || reqdy != 0) {
                if (!((reqdx == -1 && reqdy == 0 && (ch & 1) != 0) || (reqdx == 1 && reqdy == 0 && (ch & 4) != 0) ||
                        (reqdx == 0 && reqdy == -1 && (ch & 2) != 0) || (reqdx == 0 && reqdy == 1 && (ch & 8) != 0))) {
                    dx = reqdx;
                    dy = reqdy;
                    viewdx = dx;
                    viewdy = dy;
                }
            }

            // Check for standstill
            if ((dx == -1 && dy == 0 && (ch & 1) != 0) || (dx == 1 && dy == 0 && (ch & 4) != 0) ||
                    (dx == 0 && dy == -1 && (ch & 2) != 0) || (dx == 0 && dy == 1 && (ch & 8) != 0)) {
                dx = 0;
                dy = 0;
            }
        }
        move();
    }

    /**
     * For ghosts that are close to pacman, have them follow pacman
     * with a specified probability
     *
     * @param grid The Grid to be used for the collision
     * @param px   pacman's x position in integer form
     * @param py   pacman's y position in integer form
     */
    public void moveAI(Grid grid, int px, int py) {
        double distance = Math.sqrt(Math.pow(this.x - px, 2.0) + Math.pow(this.y - py, 2.0));

        if(distance < 100.0 && Math.random() < 0.6)
        {
            //Makes sure ghost is in a grid and not in movement
            if (this.x % grid.blockSize == 0 && this.y % grid.blockSize == 0)
            {
                int[][] d = moveList(grid);
                int count = d[0][0];
                int[] best = new int[3];

                for(int i = 1; i <= count; i++)
                {
                    distance = Math.sqrt(Math.pow(this.x - px - d[i][0], 2.0) + Math.pow(this.y - py - d[i][1], 2.0));
                    if(distance > best[0]) //I honestly don't know why it's > but it works...
                    {
                        best[0] = (int)distance;
                        best[1] = d[i][0];
                        best[2] = d[i][1];
                    }
                }
                dx = best[1];
                dy = best[2];
            }
            move();
        }
        else
        {
            moveAI(grid);
        }
    }

    /**
     * Moves character's current position with the board's collision
     * More of a move random rather than "AI"
     *
     * @param grid The Grid to be used for collision
     */
    @Override
    public void moveAI(Grid grid) {
        int[][] d;
        int count;

        //Makes sure ghost is in a grid and not in movement
        if (this.x % grid.blockSize == 0 && this.y % grid.blockSize == 0) {
            d = moveList(grid);
            count = d[0][0];

            // What does this even do?
            if (count == 0) {
                if ((grid.screenData[y / grid.blockSize][x / grid.blockSize] & 15) == 15) {
                    this.dx = 0;
                    this.dy = 0;
                }
                else {
                    this.dx = -this.dx;
                    this.dy = -this.dy;
                }
            }

            //randomly pick an available move
            count = (int) (Math.random() * count) + 1;
            this.dx = d[count][0];
            this.dy = d[count][1];
        }
        move();
    }

    /**
     * Returns a list of possible movements
     * moves[0][0] is the number of available moves
     *
     * @param grid
     */
    private int[][] moveList(Grid grid)
    {
        int[][] moves = new int[5][2];
        int count = 1;
        int block = grid.screenData[y / grid.blockSize][x / grid.blockSize];

        // following block of code randomizes movement (randomizes dx[] and dy[])
        // First condition prevents checks collision with wall
        // Second condition prevents switching direction abruptly (left -> right) (up -> down)
        if ((block & 1) == 0 && this.dx != 1) {
            moves[count][0] = -1;
            count++;
        }
        if ((block & 2) == 0 && this.dy != 1) {
            moves[count][1] = -1;
            count++;
        }
        if ((block & 4) == 0 && this.dx != -1) {
            moves[count][0] = 1;
            count++;
        }
        if ((block & 8) == 0 && this.dy != -1) {
            moves[count][1] = 1;
            count++;
        }

        moves[0][0] = count - 1;

        return moves;
    }
}
