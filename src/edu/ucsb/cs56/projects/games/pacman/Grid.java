package edu.ucsb.cs56.projects.games.pacman;

import java.awt.*;
import java.util.Arrays;

/**
 * Class representing the map layout
 *
 * @author Yuxiang Zhu
 * @version CS56, W15
 */

public class Grid
{
    public int fruitCounter = 0;
    public int x;
    public int y;

    /*
     check this link to implement the ghost AI movement at intersection.
     Revise the level 1 data to classic pacman for intersection detection
     http://gameinternals.com/post/2072558330/understanding-pac-man-ghost-behavior
     */
    final short leveldata1[][] = new short[][]{
            {19, 26, 26, 18, 26, 26, 26, 22,  0, 19, 26, 26, 26, 18, 26, 26, 22},
            {21,  0,  0, 21,  0,  0,  0, 21,  0, 21,  0,  0,  0, 21,  0,  0, 21},
            {17, 26, 26, 16, 26, 18, 26, 24, 26, 24, 26, 18, 26, 16, 26, 26, 20},
            {25, 26, 26, 20,  0, 25, 26, 22,  0, 19, 26, 28,  0, 17, 26, 26, 28},
            { 0,  0,  0, 21,  0,  0,  0, 21,  0, 21,  0,  0,  0, 21,  0,  0,  0},
            { 0,  0,  0, 21,  0, 19, 26, 24, 26, 24, 26, 22,  0, 21,  0,  0,  0},
            {26, 26, 26, 16, 26, 20,  0,  0,  0,  0,  0, 17, 26, 16, 26, 26, 26},
            { 0,  0,  0, 21,  0, 17, 26, 26, 26, 26, 26, 20,  0, 21,  0,  0,  0},
            { 0,  0,  0, 21,  0, 21,  0,  0,  0,  0,  0, 21,  0, 21,  0,  0,  0},
            {19, 26, 26, 16, 26, 24, 26, 22,  0, 19, 26, 24, 26, 16, 26, 26, 22},
            {21,  0,  0, 21,  0,  0,  0, 21,  0, 21,  0,  0,  0, 21,  0,  0, 21},
            {25, 22,  0, 21,  0,  0,  0, 17,  2, 20,  0,  0,  0, 21,  0, 19, 28}, // "2" in this line stands for where the pacman spawn
            { 0, 21,  0, 17, 26, 26, 18, 24, 24, 24, 18, 26, 26, 20,  0, 21,  0},
            {19, 24, 26, 28,  0,  0, 25, 18, 26, 18, 28,  0,  0, 25, 26, 24, 22},
            {21,  0,  0,  0,  0,  0,  0, 21,  0, 21,  0,  0,  0,  0,  0,  0, 21},
            {25, 26, 26, 26, 26, 26, 26, 24, 26, 24, 26, 26, 26, 26, 26, 26, 28},
            { 0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0},
    };

    final short leveldata2[][] = new short[][]{
            {19, 26, 26, 18, 26, 18, 26, 18, 26, 18, 26, 18, 26, 18, 26, 26, 22}, //1
            {25, 26, 18, 28,  0, 25, 22, 21,  0, 21, 19, 28,  0, 25, 18, 26, 28}, //2
            { 0,  0, 17, 22,  0,  0, 21, 21,  0, 21, 21,  0,  0, 19, 20,  0,  0}, //3
            { 0,  0, 21, 25, 26, 26, 24, 24, 26, 24, 24, 26, 26, 28, 21,  0,  0}, //4
            {26, 26, 20,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 17, 26, 26}, //5
            { 0,  0, 21,  0,  0, 19, 26, 26, 26, 26, 26, 22,  0,  0, 21,  0,  0}, //6
            { 0,  0, 25, 18, 26, 20,  0,  0,  0,  0,  0, 17, 26, 18, 28,  0,  0}, //7
            { 0,  0,  0, 21,  0, 21,  0,  0,  0,  0,  0, 21,  0, 21,  0,  0,  0}, //8
            { 0,  0,  0, 21,  0, 25, 26, 18, 26, 18, 26, 28,  0, 21,  0,  0,  0}, //9
            {26, 22,  0, 21,  0,  0,  0, 21,  0, 21,  0,  0,  0, 21,  0, 19, 26}, //10
            { 0, 17, 26, 20,  0,  0, 19, 20,  0, 17, 22,  0,  0, 17, 26, 20,  0}, //11
            { 0, 21,  0, 21,  0,  0, 21, 25, 26, 28, 21,  0,  0, 21,  0, 21,  0}, //12
            { 0, 21,  0, 17, 26, 26, 20,  0,  0,  0, 17, 26, 26, 20,  0, 21,  0}, //13
            {19, 28,  0, 21,  0,  0, 17, 22,  0, 19, 20,  0,  0, 21,  0, 25, 22}, //14
            {21,  0,  0, 21,  0,  0, 21, 21,  0, 21, 21,  0,  0, 21,  0,  0, 21}, //15
            {17, 26, 26, 20,  0,  0, 21, 21,  0, 21, 21,  0,  0, 17, 26, 26, 20}, //16
            {25, 26, 26, 24, 26, 26, 28, 25, 26, 28, 25, 26, 26, 24, 26, 26, 28}  //17

    };

    final short leveldata3[][] = new short[][]{
            {19, 26, 26, 18, 26, 22,  0, 19, 26, 22,  0, 19, 26, 18, 26, 26, 22}, //1
            {21,  0,  0, 21,  0, 17, 26, 20,  0, 17, 26, 20,  0, 21,  0,  0, 21}, //2
            {21,  0,  0, 21,  0, 21,  0, 17, 26, 20,  0, 21,  0, 21,  0,  0, 21}, //3
            {25, 26, 26, 20,  0, 21,  0, 21,  0, 21,  0, 21,  0, 17, 26, 26, 28}, //4
            { 0,  0,  0, 17, 26, 24, 26, 20,  0, 17, 26, 24, 26, 20,  0,  0,  0}, //5
            { 0, 19, 18, 20,  0,  0,  0, 21,  0, 21,  0,  0,  0, 17, 18, 22,  0}, //6
            { 0, 21, 21, 21,  0, 19, 26, 24, 26, 24, 26, 22,  0, 21, 21, 21,  0}, //7
            {26, 28, 21, 21,  0, 21,  0,  0,  0,  0,  0, 21,  0, 21, 21, 25, 26}, //8
            { 0,  0, 21, 25, 26, 24, 26, 18, 26, 18, 26, 24, 26, 28, 21,  0,  0}, //9
            {26, 26, 20, 19, 26, 26, 26, 20,  0, 17, 26, 26, 26, 22, 17, 26, 26}, //10
            { 0,  0, 21, 21,  0,  0,  0, 21,  0, 21,  0,  0,  0, 21, 21,  0,  0}, //11
            {19, 22, 21, 21,  0, 19, 18, 24, 26, 24, 18, 22,  0, 21, 21, 19, 22}, //12
            {21, 21, 17, 16, 26, 20, 25, 22,  0, 19, 28, 17, 26, 16, 20, 21, 21}, //13
            {21, 21, 21, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21, 21, 21, 21}, //14
            {21, 25, 20, 21,  0, 17, 22, 21,  0, 21, 19, 20,  0, 21, 17, 28, 21}, //15
            {21,  0, 21, 25, 22, 21, 17, 28,  0, 25, 20, 21, 19, 28, 21,  0, 21}, //16
            {25, 26, 28,  0, 25, 28, 25, 26, 26, 26, 28, 25, 28,  0, 25, 26, 28}  //17
    };

    final short leveldata4[][] = new short[][]{
            {19, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 16},
            {17, 16, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 16, 16, 16, 16},
            {17, 20,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 17, 16, 16, 16},
            {17, 16, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 16, 16, 16, 16},
            {17, 16, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 16, 16, 16, 16},
            {17, 20,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 17, 16, 16, 16},
            {17, 16, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 16, 16, 16, 16},
            {17, 16, 24, 24, 24, 24, 24, 16, 24, 24, 24, 24, 24, 16, 16, 16, 16},
            {17, 20,  0,  0,  0,  0,  0, 21,  0,  0,  0,  0,  0, 17, 16, 16, 16},
            {17, 20,  0, 19, 18, 18, 18, 16, 18, 18, 18, 22,  0, 17, 16, 16, 16},
            {17, 20,  0, 17, 16, 16, 16, 16, 16, 16, 16, 20,  0, 17, 16, 16, 16},
            {17, 20,  0, 25, 24, 24, 24, 24, 24, 24, 24, 28,  0, 17, 16, 16, 16},
            {17, 20,  0,  0,  0,  0,  0, 16,  0,  0,  0,  0,  0, 17, 16, 16, 16},
            {17, 16, 18, 18, 18, 18, 22, 16, 19, 18, 18, 18, 18, 16, 16, 16, 16},
            {25, 24, 24, 24, 24, 24, 28, 16, 25, 24, 24, 24, 24, 24, 24, 24, 16},
            {18, 18, 18, 18, 18, 18, 18, 16, 18, 18, 18, 18, 18, 18, 18, 18, 16},
            {16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16}
    };

    final short leveldata5[][] = new short[][]{
            { 0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0},
            { 0,  0, 19, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 22,  0,  0,  0},
            { 0,  0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,  0,  0,  0},
            { 0,  0, 17, 16, 16, 16, 24, 24, 24, 24, 24, 24, 16, 20,  0,  0,  0},
            { 0,  0, 17, 16, 16, 20,  0,  0,  0,  0,  0,  0, 17, 20,  0,  0,  0},
            { 0,  0, 17, 16, 16, 20,  0,  0,  0,  0,  0,  0, 25, 28,  0,  0,  0},
            { 0,  0, 17, 16, 16, 16, 18, 18, 18, 18, 22,  0,  0,  0,  0,  0,  0},
            { 0,  0, 17, 16, 16, 16, 16, 16, 16, 16, 20,  0,  0,  0,  0,  0,  0},
            { 0,  0, 25, 24, 16, 16, 16, 16, 16, 16, 16, 18, 18, 18, 22,  0,  0},
            { 0,  0,  0,  0, 17, 16, 16, 16, 16, 16, 24, 16, 16, 16, 20,  0,  0},
            { 0,  0,  0,  0, 17, 16, 16, 16, 16, 20,  0, 17, 16, 16, 20,  0,  0},
            {19, 18, 18, 26, 16, 24, 24,  0, 24, 28,  0, 17, 16, 16, 20,  0,  0},
            {17, 16, 20,  0, 21,  0,  0, 21,  0,  0,  0, 17, 16, 16, 20,  0,  0},
            {17, 16, 20,  0, 21,  0, 19, 16, 22,  0,  0, 25, 24, 24, 28,  0,  0},
            {25, 24, 28,  0, 29,  0, 25, 24, 28,  0,  0,  0,  0,  0,  0,  0,  0},
            { 0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0},
            { 0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}
    };

    short[][] screenData;
    Color mazeColor, dotColor, fruitColor;

    /**
     * Constructor for Board object
     */
    public Grid() {
        screenData = new short[Board.NUMBLOCKS][Board.NUMBLOCKS];
        mazeColor = new Color(5, 100, 5);
        dotColor = new Color(192, 192, 0);
        fruitColor = new Color(255,0,0);
    }

    /**
     * Checks if there are any pellets left for Pacman to eat, and restarts the game on the next board in a
     * higher difficulty if finished
     *
     * @return A boolean indicating whether or not the maze is finished
     */
    public boolean checkMaze() {
        for (int i = 0; i < Board.NUMBLOCKS; i++) {
            for (int j = 0; j < Board.NUMBLOCKS; j++) {
                if ((screenData[i][j] & 16) != 0)
                    return false;
            }
        }
        return true;
    }

    /**
     * Count the number of pellets left for Pacman to eat
     *
     * @return An int indicating how many pellets are left
     */
    public int getPelletNum() {
        int numOfPellet = 0;
        for (int i = 0; i < Board.NUMBLOCKS; i++) {
            for (int j = 0; j < Board.NUMBLOCKS; j++) {
                if ((screenData[i][j] & 16) != 0)
                    numOfPellet++;
            }
        }
        return numOfPellet;
    }


    /**
     * Initialize level
     */
    public void levelInit(int numBoardsCleared) {
        for (int i = 0; i < Board.NUMBLOCKS; i++) {
            if (numBoardsCleared % 3 == 0)
                screenData[i] = Arrays.copyOf(leveldata1[i], Board.NUMBLOCKS);
            else if (numBoardsCleared % 3 == 1)
                screenData[i] = Arrays.copyOf(leveldata2[i], Board.NUMBLOCKS);
            else if (numBoardsCleared % 3 == 2)
                screenData[i] = Arrays.copyOf(leveldata3[i], Board.NUMBLOCKS);
            else if (numBoardsCleared % 3 == 3)
                screenData[i] = Arrays.copyOf(leveldata4[i], Board.NUMBLOCKS);
            else if (numBoardsCleared % 3 == 4)
                screenData[i] = Arrays.copyOf(leveldata5[i], Board.NUMBLOCKS);
        }
    }


    /* Better Implementation Idea
    You could have an ArrayList that holds Point Objects of
    locations that the Pacman has eaten pellets.
    - Create ArrayList<Point> in Grid.java & initialize
    - Add new Point every time Pacman eats pellet/fruit in PacPlayer.java

    When it is time to spawn a fruit, choose a random Point from this
    ArrayList (int) (Math.random() * list.size()), then remove the point
    where you spawned the fruit.

    When a level is complete, make sure you clear this ArrayList using
    list.clear()

    **If you want to fix this too, you can try:
    To prevent the fruit from spawning on Pacman, just cross-check
    that the spawning location != any of the Pacman location
    (use a loop on pacmen array for this so you handle multi-player mode)

    I think without having a defined search space, and just searching
    randomly, you cannot easily fix the bug where the program is looking
    forever to find an open space.  That's why having this list of possible
    spaces is the best way I can think of.
     */

    public void randomBlock(){
        this.x = (int) (Math.random() * Board.NUMBLOCKS);
        this.y = (int) (Math.random() * Board.NUMBLOCKS);
    }
    /**
     * Increment fruit as the pacman is alive
     *
     * @param numBoardsCleared number of levels cleared
     */
    public void incrementFruit(int numBoardsCleared) {
        if (fruitCounter > 20) {
            fruitCounter = 0;
            this.randomBlock();
            if (numBoardsCleared % 3 == 0) {
                while (true) {
                    {
                        if (((screenData[this.x][this.y] & 16) == 0) && (leveldata1[this.x][this.y] & 16) != 0) {
                            screenData[this.x][this.y] = (short) (screenData[this.x][this.y] | 32);
                            break;
                        }
                        this.randomBlock();
                    }
                }
            } else if (numBoardsCleared % 3 == 1)
                while (true) {
                    {
                        if (((screenData[this.x][this.y] & 16) == 0) && (leveldata2[this.x][this.y] & 16) != 0) {
                            screenData[this.x][this.y] = (short) (screenData[this.x][this.y] | 32);
                            break;
                        }
                        this.randomBlock();
                    }
                }
            else if (numBoardsCleared % 3 == 2)
                while (true) {
                    {
                        if (((screenData[this.x][this.y] & 16) == 0) && (leveldata3[this.x][this.y] & 16) != 0) {
                            screenData[this.x][this.y] = (short) (screenData[this.x][this.y] | 32);
                            break;
                        }
                        this.randomBlock();
                    }
                }
            else if (numBoardsCleared % 3 == 3)
                while (true) {
                    {
                        if (((screenData[this.x][this.y] & 16) == 0) && (leveldata4[this.x][this.y] & 16) != 0) {
                            screenData[this.x][this.y] = (short) (screenData[this.x][this.y] | 32);
                            break;
                        }
                        this.randomBlock();
                    }
                }
            else if (numBoardsCleared % 3 == 4)
                while (true) {
                    {
                        if (((screenData[this.x][this.y] & 16) == 0) && (leveldata5[this.x][this.y] & 16) != 0) {
                                screenData[this.x][this.y] = (short) (screenData[this.x][this.y] | 32);
                                break;
                            }
                        this.randomBlock();
                    }
                }
        }
        else
            fruitCounter++;
    }


    /**
     * Draws the maze that serves as a playing field.
     *
     * @param g2d a Graphics2D object
     */
    public void drawMaze(Graphics2D g2d)
    {
        int x, y;
        g2d.setStroke(new BasicStroke(2));
        for (int i = 0; i < Board.NUMBLOCKS; i++) {
            for (int j = 0; j < Board.NUMBLOCKS; j++) {
                y = i * Board.BLOCKSIZE + 3;
                x = j * Board.BLOCKSIZE + 3;

                g2d.setColor(mazeColor);
                
                /*
                screen draw explaination
                bit 0 not 0 -> draw left
                bit 1 not 0 -> draw top
                bit 2 not 0 -> draw right
                bit 3 not 0 -> draw bottom
                bit 4 not 0 -> draw pellet
                bit 5 not 0 -> draw fruit
                 */

                if ((screenData[i][j] & 1) != 0) // draws left
                    g2d.drawLine(x, y, x, y + Board.BLOCKSIZE - 1);
                if ((screenData[i][j] & 2) != 0) // draws top
                    g2d.drawLine(x, y, x + Board.BLOCKSIZE - 1, y);
                if ((screenData[i][j] & 4) != 0) // draws right
                    g2d.drawLine(x + Board.BLOCKSIZE - 1, y, x + Board.BLOCKSIZE - 1, y + Board.BLOCKSIZE - 1);
                if ((screenData[i][j] & 8) != 0) // draws bottom
                    g2d.drawLine(x, y + Board.BLOCKSIZE - 1, x + Board.BLOCKSIZE - 1, y + Board.BLOCKSIZE - 1);

                g2d.setColor(dotColor);
                if ((screenData[i][j] & 16) != 0) // draws pellet
                    g2d.fillRect(x + 11, y + 11, 2, 2);

                g2d.setColor(fruitColor);
                if ((screenData[i][j] & 32) != 0) // draws fruit
                    g2d.fillRect(x + 10, y + 10, 4, 4);
            }
        }
    }
}
