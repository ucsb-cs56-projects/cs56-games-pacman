package edu.ucsb.cs56.projects.games.pacman;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
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
import java.io.*;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.imageio.ImageIO;

/**
   Playing field for a Pacman arcade game remake that keeps track of all relevant data and handles game logic.<p>
   The version of the code by Jan Bodnar may be found at http://zetcode.com/tutorials/javagamestutorial/pacman/
   @author Brian Postma
   @author Jan Bodnar
   @author Dario Castellanos
   @author Brandon Newman
   @version CS56 S13
 */

public class Board extends JPanel implements ActionListener {

    Dimension d;
    Font smallfont = new Font("Helvetica", Font.BOLD, 14);

    FontMetrics fmsmall, fmlarge;
    Image ii;
    Color dotcolor = new Color(192, 192, 0);
    Color mazecolor;

    boolean ingame = false;
    boolean dying = false;

    final int blocksize = 24;
    final int nrofblocks = 15;
    final int scrsize = nrofblocks * blocksize;
    final int pacanimdelay = 2;
    final int pacmananimcount = 4;
    final int maxghosts = 12;
    final int pacmanspeed = 6;
    int numBoardsCleared = 0;

    int pacanimcount = pacanimdelay;
    int pacanimdir = 1;
    int pacmananimpos = 0;
    int nrofghosts = 6;
    int pacsleft, score;
    int deathcounter;
    int[] dx, dy;
    int[] ghostx, ghosty, ghostdx, ghostdy, ghostspeed;

    Image ghost, ghoster;
    Image pacman1, pacman2up, pacman2left, pacman2right, pacman2down;
    Image pacman3up, pacman3down, pacman3left, pacman3right;
    Image pacman4up, pacman4down, pacman4left, pacman4right;

    int pacmanx, pacmany, pacmandx, pacmandy;
    int reqdx, reqdy, viewdx, viewdy;

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
      {   0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
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
         25, 24, 28,  0, 29,  0, 25, 24, 28,  0,  0,  0,  0,  0,  0 };



    final int validspeeds[] = { 1, 2, 3, 4, 6, 8 };
    final int maxspeed = 6;

    int currentspeed = 3;
    short[] screendata;
    Timer timer;

    /**
       Constructor for Board object
     */
    public Board() {

        GetImages();

        addKeyListener(new TAdapter());

        screendata = new short[nrofblocks * nrofblocks];
        mazecolor = new Color(5, 100, 5);
        setFocusable(true);

        d = new Dimension(400, 400);

        setBackground(Color.black);
        setDoubleBuffered(true);

        ghostx = new int[maxghosts];
        ghostdx = new int[maxghosts];
        ghosty = new int[maxghosts];
        ghostdy = new int[maxghosts];
        ghostspeed = new int[maxghosts];
        dx = new int[4];
        dy = new int[4];
        timer = new Timer(40, this);
        timer.start();
    }

    /**
       Called by the system
     */
    public void addNotify() {
        super.addNotify();
        GameInit();
    }

    /**
       Animates the Pacman sprite's direction as well as mouth opening and closing
     */
    public void DoAnim() {
        pacanimcount--;
        if (pacanimcount <= 0) {
            pacanimcount = pacanimdelay;
            pacmananimpos = pacmananimpos + pacanimdir;
            if (pacmananimpos == (pacmananimcount - 1) || pacmananimpos == 0)
                pacanimdir = -pacanimdir;
        }
    }

    /**
       Main game logic loop
       @param g2d a Graphics 2D object 
     */
    public void PlayGame(Graphics2D g2d) {
        if (dying) {
            Death();
        } else {
            MovePacMan();
            DrawPacMan(g2d);
            moveGhosts(g2d);
            CheckMaze();
        }
    }

    /**
       Draw a message box with the text "Press s to start." in the center of the screen
       @param g2d a Graphics2D object
     */
    public void ShowIntroScreen(Graphics2D g2d) {

        g2d.setColor(new Color(0, 32, 48));
        g2d.fillRect(50, scrsize / 2 - 30, scrsize - 100, 50);
        g2d.setColor(Color.white);
        g2d.drawRect(50, scrsize / 2 - 30, scrsize - 100, 50);

        String s = "Press s to start.";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = this.getFontMetrics(small);

        g2d.setColor(Color.white);
        g2d.setFont(small);
        g2d.drawString(s, (scrsize - metr.stringWidth(s)) / 2, scrsize / 2);
    }

    /**
       Display the current score on the bottom right of the screen
       @param g a Graphics object
     */
    public void DrawScore(Graphics2D g) {
        int i;
        String s;

        g.setFont(smallfont);
        g.setColor(new Color(96, 128, 255));
        s = "Score: " + score;
        g.drawString(s, scrsize / 2 + 96, scrsize + 16);
        for (i = 0; i < pacsleft; i++) {
            g.drawImage(pacman3left, i * 28 + 8, scrsize + 1, this);
        }
    }

    /**
       Checks if there are any pellets left for Pacman to eat, and restarts the game on the next board in a  higher difficulty if finished
     */
    public void CheckMaze() {
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
            LevelInit();
        }
    }

    /**
       Decrements number of lives left when player touches a ghost and reinitializes player location.
       End the game if remaining lives reaches 0.
     */
    public void Death() {

        pacsleft--;
        if (pacsleft == 0)
        {
            ingame = false;
            numBoardsCleared = 0;
        }
        LevelContinue();
    }

    /**
       Movement logic for ghost enemies. Ghosts will move one square and then decide whether to change directions or not
       @param g2d a Graphics2D object
     */
    public void moveGhosts(Graphics2D g2d) {
        short i;
        int pos;
        int count;

        for (i = 0; i < nrofghosts; i++) {
            if (ghostx[i] % blocksize == 0 && ghosty[i] % blocksize == 0) {
                pos = ghostx[i] / blocksize + nrofblocks * (int)(ghosty[i] / blocksize);

                count = 0;
                if ((screendata[pos] & 1) == 0 && ghostdx[i] != 1) {
                    dx[count] = -1;
                    dy[count] = 0;
                    count++;
                }
                if ((screendata[pos] & 2) == 0 && ghostdy[i] != 1) {
                    dx[count] = 0;
                    dy[count] = -1;
                    count++;
                }
                if ((screendata[pos] & 4) == 0 && ghostdx[i] != -1) {
                    dx[count] = 1;
                    dy[count] = 0;
                    count++;
                }
                if ((screendata[pos] & 8) == 0 && ghostdy[i] != -1) {
                    dx[count] = 0;
                    dy[count] = 1;
                    count++;
                }
 
                if (count == 0) {
                    if ((screendata[pos] & 15) == 15) {
                        ghostdx[i] = 0;
                        ghostdy[i] = 0;
                    } else {
                        ghostdx[i] = -ghostdx[i];
                        ghostdy[i] = -ghostdy[i];
                    }
                } else {
                    count = (int)(Math.random() * count);
                    if (count > 3)
                        count = 3;
                    ghostdx[i] = dx[count];
                    ghostdy[i] = dy[count];
                }

            }
            ghostx[i] = ghostx[i] + (ghostdx[i] * ghostspeed[i]);
            ghosty[i] = ghosty[i] + (ghostdy[i] * ghostspeed[i]);
            DrawGhost(g2d, ghostx[i] + 1, ghosty[i] + 1);

            if (pacmanx > (ghostx[i] - 12) && pacmanx < (ghostx[i] + 12) &&
                pacmany > (ghosty[i] - 12) && pacmany < (ghosty[i] + 12) &&
                ingame) {

                dying = true;
                deathcounter = 64;

            }
        }
    }

    /**
       Draws the ghost sprite
       @param g2d a Graphics2D object
       @param x the x position of the ghost
       @param y the y position of the ghost
     */
    public void DrawGhost(Graphics2D g2d, int x, int y) {
        g2d.drawImage(ghost, x, y, this);
    }

    /**
       Handles movement for Pacman
     */
    public void MovePacMan() {
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
                score++;
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
       Calls the appropriate draw method for the direction Pacman is facing
       @param g2d a Graphics2D object
     */
    public void DrawPacMan(Graphics2D g2d) {
        if (viewdx == -1)
            DrawPacManLeft(g2d);
        else if (viewdx == 1)
            DrawPacManRight(g2d);
        else if (viewdy == -1)
            DrawPacManUp(g2d);
        else
            DrawPacManDown(g2d);
    }

    /**
       Draws Pacman facing up
       @param g2d a Graphics2D object
     */
    public void DrawPacManUp(Graphics2D g2d) {
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
       Draws Pacman facing down
       @param g2d a Graphics2D object
     */
    public void DrawPacManDown(Graphics2D g2d) {
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
       Draws Pacman facing left
       @param g2d a Graphics2D object
     */
    public void DrawPacManLeft(Graphics2D g2d) {
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
       Draws Pacman facing right
       @param g2d a Graphics2D object
     */
    public void DrawPacManRight(Graphics2D g2d) {
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
       Draws the maze that serves as a playing field.
       @param g2d a Graphics2D object
     */
    public void DrawMaze(Graphics2D g2d) {
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
       Initialize game variables
     */
    public void GameInit() {
        pacsleft = 3;
        score = 0;
        LevelInit();
        nrofghosts = 6;
        currentspeed = 3;
    }

    /**
       Initialize level
     */
    public void LevelInit() {
        int i;
        for (i = 0; i < nrofblocks * nrofblocks; i++)
        {
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

        LevelContinue();
    }

    /**
       Initialize Pacman and ghost position/direction
     */
    public void LevelContinue() {
        short i;
        int dx = 1;
        int random;

        for (i = 0; i < nrofghosts; i++) {
            ghosty[i] = 4 * blocksize;
            ghostx[i] = 4 * blocksize;
            ghostdy[i] = 0;
            ghostdx[i] = dx;
            dx = -dx;
            random = (int)(Math.random() * (currentspeed + 1));
            if (random > currentspeed)
                random = currentspeed;
            ghostspeed[i] = validspeeds[random];
        }

        pacmanx = 7 * blocksize;
        pacmany = 11 * blocksize;
        pacmandx = 0;
        pacmandy = 0;
        reqdx = 0;
        reqdy = 0;
        viewdx = -1;
        viewdy = 0;
        dying = false;
    }

    /**
       Load game sprites from pacpix directory
     */
    public void GetImages()
    {
	    try 
        {
	        ghost = ImageIO.read(getClass().getResource("pacpix/ghost.png"));
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
        catch (IOException e) 
        {
	        e.printStackTrace();
	    }
    }

    /**
       Paint graphics onto screen
       @param g a Graphics object
     */
    public void paint(Graphics g)
    {
      super.paint(g);

      Graphics2D g2d = (Graphics2D) g;

      g2d.setColor(Color.black);
      g2d.fillRect(0, 0, d.width, d.height);

      DrawMaze(g2d);
      DrawScore(g2d);
      DoAnim();
      if (ingame)
        PlayGame(g2d);
      else
        ShowIntroScreen(g2d);

      g.drawImage(ii, 5, 5, this);
      Toolkit.getDefaultToolkit().sync();
      g.dispose();
    }

    /**
       Class that handles key presses for game controls
     */
    class TAdapter extends KeyAdapter {

	/**
	   Detects when a key is pressed.<p>
	   In-game: Changes Pacman's direction of movement with the arrow keys. Quit game by pressing the escape key.<p> 
	   Not in-game: Press the 'S' key to begin the game.
	   @param e a KeyEvent
	 */
        public void keyPressed(KeyEvent e) {

          int key = e.getKeyCode();

          if (ingame)
          {
            if (key == KeyEvent.VK_LEFT)
            {
              reqdx=-1;
              reqdy=0;
            }
            else if (key == KeyEvent.VK_RIGHT)
            {
              reqdx=1;
              reqdy=0;
            }
            else if (key == KeyEvent.VK_UP)
            {
              reqdx=0;
              reqdy=-1;
            }
            else if (key == KeyEvent.VK_DOWN)
            {
              reqdx=0;
              reqdy=1;
            }
            else if (key == KeyEvent.VK_ESCAPE && timer.isRunning())
            {
              ingame=false;
            }
            else if (key == KeyEvent.VK_PAUSE) {
                if (timer.isRunning())
                    timer.stop();
                else timer.start();
            }
          }
          else
          {
            if (key == 's' || key == 'S')
          {
              ingame=true;
              GameInit();
            }
          }
      }

	/**
	   Detects when a key is released
	   @param e a KeyEvent
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

    /**
       Repaint the graphics each frame
       @param e an ActionEvent
     */
    public void actionPerformed(ActionEvent e) {
        repaint();  
    }
}
