package edu.ucsb.cs56.projects.games.pacman;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Date;


/**
 * Playing field for a Pacman arcade game remake that keeps track of all relevant data and handles game logic.<p>
 * The version of the code by Jan Bodnar may be found at http://zetcode.com/tutorials/javagamestutorial/pacman/
 *
 * @author Brian Postma
 * @author Jan Bodnar
 * @author Dario Castellanos
 * @author Brandon Newman
 * @author Daniel Ly
 * @author Deanna Hartsook
 * @author Kateryna Fomenko
 * @author Yuxiang Zhu
 * @author Kelvin Yang
 * @version CS56 W15
 */
public class Board extends JPanel implements ActionListener
{
    enum GameType {
        INTRO, HELP, SINGLEPLAYER, COOPERATIVE, VERSUS, LEADERBOARD
    }

    public static final int BLOCKSIZE = 24;
    public static final int NUMBLOCKS = 17;
    public static final int SCRSIZE = NUMBLOCKS * BLOCKSIZE;

    private final int MAX_GHOSTS = 12;
    private final int MAX_SPEED = 6;


    public static int score;
    private ScoreLoader sl = new ScoreLoader("highScores.txt");
    private LeaderboardGUI leaderBoardGui = new LeaderboardGUI();
    private Grid grid;
    private Font smallFont = new Font("Helvetica", Font.BOLD, 14);
    private GameType gt;
    private PacPlayer pacman, msPacman;
    private Ghost ghost1, ghost2;
    private Character[] pacmen;
    private ArrayList<Ghost> ghosts;
    private int numGhosts = 6, numBoardsCleared = 0;
    private int curSpeed = 3;
    private int numPellet;
    private Timer timer;
    private Audio beginningAudio;

    /**
     * Constructor for Board object
     */
    public Board() {
        addKeyListener(new TAdapter());
        grid = new Grid();
        pacman = new PacPlayer(8 * BLOCKSIZE, 11 * BLOCKSIZE, PacPlayer.PACMAN, grid);
        msPacman = new PacPlayer(7 * BLOCKSIZE, 11 * BLOCKSIZE, PacPlayer.MSPACMAN, grid);
        ghost1 = new Ghost(8 * BLOCKSIZE, 7 * BLOCKSIZE, 2, Ghost.GHOST1, grid);
        ghost2 = new Ghost(8 * BLOCKSIZE, 7 * BLOCKSIZE, 2, Ghost.GHOST2, grid);
        setFocusable(true);

        setBackground(Color.black);
        setDoubleBuffered(true);
        ghosts = new ArrayList<Ghost>();
        timer = new Timer(40, this);
        timer.start();

        try {
            this.beginningAudio = new Audio(getClass().getResourceAsStream("assets/audio/beginning.wav"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Called by the system
     */
    public void addNotify()
    {
        super.addNotify();
        gt = GameType.INTRO;
        grid.levelInit(0);
    }

    /**
     * Main game logic loop
     *
     * @param g2d a Graphics 2D object
     */
    public void playGame(Graphics2D g2d) {
        if (!checkAlive())
        {
            gameOver();
        }
        else
        {
            if (pacman.alive)
            {
                pacman.move(grid);
                pacman.draw(g2d, this);
            }
            switch (gt)
            {
                case SINGLEPLAYER:
                    for(Ghost g : ghosts)
                    {
                        g.moveAI(grid, pacmen);
                        g.draw(g2d, this);
                    }
                    //increment in here so you don't in versus mode
                    grid.incrementFruit(numBoardsCleared);
                    detectCollision(ghosts);
                    break;
                case COOPERATIVE:
                    if (msPacman.alive)
                    {
                        msPacman.move(grid);
                        msPacman.draw(g2d, this);
                    }
                    for(Ghost g : ghosts)
                    {
                        g.moveAI(grid, pacmen);
                        g.draw(g2d, this);
                    }
                    //increment in here so you don't in versus mode
                    grid.incrementFruit(numBoardsCleared);
                    detectCollision(ghosts);
                    break;
                case VERSUS:
                    for (Character ghost : ghosts)
                    {
                        ghost.move(grid);
                        ghost.draw(g2d, this);
                    }

                    if (score >= numPellet)
                    {
                        score = 0;
                        numBoardsCleared++;
                        grid.levelInit(numBoardsCleared);
                        levelContinue();

                    }
                    detectCollision(ghosts);
                    break;
            }
            if (grid.checkMaze())
            {
                score += 50;
                numBoardsCleared++;

                numGhosts = (numGhosts + 1) % MAX_GHOSTS;
                curSpeed = (curSpeed + 1) % MAX_SPEED;
                grid.levelInit(numBoardsCleared);
                levelContinue();
            }
        }
    }

    /**
     * Draw a message box with the text "Press s to start." in the center of the screen
     *
     * @param g a Graphics object
     */
    public void showIntroScreen(Graphics g) {
        g.setColor(new Color(0, 32, 48));
        g.fillRect(50, SCRSIZE / 2 - 50, SCRSIZE - 100, 90);
        g.setColor(Color.white);
        g.drawRect(50, SCRSIZE / 2 - 50, SCRSIZE - 100, 90);

        String s = "Press s for single player";
        String d = "Press d for Co-Op";
        String f = "Press f for Versus";
        String h = "Press h for help";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = this.getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(s, (SCRSIZE - metr.stringWidth(s)) / 2, SCRSIZE / 2 - metr.getHeight() * 3 / 2);
        g.drawString(d, (SCRSIZE - metr.stringWidth(d)) / 2, SCRSIZE / 2 - metr.getHeight() / 2);
        g.drawString(f, (SCRSIZE - metr.stringWidth(f)) / 2, SCRSIZE / 2 + metr.getHeight() / 2);
        g.drawString(h, (SCRSIZE - metr.stringWidth(h)) / 2, SCRSIZE / 2 + metr.getHeight() * 3 / 2);
        drawHighScores(g);
    }

    /**
     * Draw a message box telling the player the game is paused
     * Also tells player to press 'p' to continue the game
     *
     * @param g a Graphics object
     */
    public void showPauseScreen(Graphics g)
    {
        g.setColor(new Color(0, 32, 48));
        g.fillRect(50, SCRSIZE / 2 - 50, SCRSIZE - 100, 90);
        g.setColor(Color.white);
        g.drawRect(50, SCRSIZE / 2 - 50, SCRSIZE - 100, 90);

        String a = "Game Paused...";
        String b = "Press 'p' or 'Pause' to continue";
        Font big = new Font("Helvetica", Font.BOLD, 20);
        Font small = new Font("Helvetica", Font.BOLD, 12);
        FontMetrics metr1 = this.getFontMetrics(big);
        FontMetrics metr2 = this.getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(big);
        g.drawString(a, (SCRSIZE - metr1.stringWidth(a)) / 2, SCRSIZE / 2 - metr1.getHeight() / 2);
        g.setFont(small);
        g.drawString(b, (SCRSIZE - metr2.stringWidth(b)) / 2, SCRSIZE / 2 + metr2.getHeight() / 2);
    }

    /**
     * Shows help
     *
     * @param g a Graphics object
     */
    public void showHelpScreen(Graphics g)
    {
        g.setColor(new Color(0, 32, 48));
        g.fillRect(10, 10, SCRSIZE - 15, SCRSIZE - 15);
        g.setColor(Color.white);
        g.drawRect(10, 10, SCRSIZE - 15, SCRSIZE - 15);

        int bx = 25, by = 60;

        String a = "Help";

        Font big = new Font("Helvetica", Font.BOLD, 18);
        Font medium = new Font("Helvetica", Font.BOLD, 14);
        Font small = new Font("Helvetica", Font.PLAIN, 12);
        FontMetrics metr1 = this.getFontMetrics(big);

        g.setColor(Color.white);
        g.setFont(big);
        g.drawString(a, (SCRSIZE - metr1.stringWidth(a)) / 2, 40);
        g.setFont(medium);
        g.drawString("Title Screen", bx, by);
        g.drawString("In Game", bx + 200, by);
        g.drawString("Controls", bx, by + 90);
        g.setFont(small);
        g.drawString("S - Start Single Player", bx + 10, by + 20);
        g.drawString("D - Start Co-op", bx + 10, by + 40);
        g.drawString("F - Start Versus", bx + 10, by + 60);
        g.drawString("Esc - Quit Game", bx + 210, by + 20);
        g.drawString("P - Pause Game", bx + 210, by + 40);

        g.drawString("Pacman:", bx + 10, by + 110);
        g.drawString("Up Arrow - Move Up", bx + 30, by + 130);
        g.drawString("Left Arrow - Move Left", bx + 30, by + 150);
        g.drawString("Down Arrow - Move Down", bx + 30, by + 170);
        g.drawString("Right Arrow - Move Right", bx + 30, by + 190);

        g.drawString("Mrs. Pacman:", bx + 220, by + 110);
        g.drawString("W - Move Up", bx + 240, by + 130);
        g.drawString("A - Move Left", bx + 240, by + 150);
        g.drawString("S - Move Down", bx + 240, by + 170);
        g.drawString("D - Move Right", bx + 240, by + 190);

        g.drawString("Ghost 1", bx + 10, by + 220);
        g.drawString("W - Move Up", bx + 30, by + 240);
        g.drawString("A - Move Left", bx + 30, by + 260);
        g.drawString("S - Move Down", bx + 30, by + 280);
        g.drawString("D - Move Right", bx + 30, by + 300);

        g.drawString("Ghost 2", bx + 170, by + 220);
        g.drawString("Numpad 8 - Move Up", bx + 190, by + 240);
        g.drawString("Numpad 4 - Move Left", bx + 190, by + 260);
        g.drawString("Numpad 5 - Move Down", bx + 190, by + 280);
        g.drawString("Numpad 6 - Move Right", bx + 190, by + 300);

        g.drawString("Press 'h' to return...", bx + 245, by + 330);
    }

    /**
     * Display the current score on the bottom right of the screen
     *
     * @param g a Graphics object
     */
    public void drawScore(Graphics g)
    {
        g.setFont(smallFont);
        g.setColor(new Color(96, 128, 255));
        if (gt == GameType.VERSUS) {
            String p = "Pellets left: " + (numPellet - score);
            g.drawString(p, SCRSIZE / 2 + 96, SCRSIZE + 16);
        } else {
            String s = "Score: " + score;
            g.drawString(s, SCRSIZE / 2 + 136, SCRSIZE + 16);
        }

        for (int i = 0; i < pacman.lives; i++) {
            g.drawImage(pacman.getLifeImage(), i * 28 + 8, SCRSIZE + 1, this);
        }
        if (gt == GameType.COOPERATIVE) {
            for (int i = 0; i < msPacman.lives; i++) {
                g.drawImage(msPacman.getLifeImage(), i * 28 + 108, SCRSIZE + 1, this);
            }
        }
    }

    /**
     * Displays a list of scores on the bottom of the screen
     *
     * @param g a Graphics object
     */
    public void drawHighScores(Graphics g)
    {
        ArrayList<Integer> scores = sl.loadScores();
        g.setFont(smallFont);
        FontMetrics fm = this.getFontMetrics(smallFont);

        g.setColor(new Color(0, 32, 48));
        g.fillRect(SCRSIZE / 4, SCRSIZE - (SCRSIZE / 3) - fm.getAscent(), SCRSIZE / 2, BLOCKSIZE * 4);
        g.setColor(Color.white);
        g.drawRect(SCRSIZE / 4, SCRSIZE - (SCRSIZE / 3) - fm.getAscent(), SCRSIZE / 2, BLOCKSIZE * 4);

        g.setColor(new Color(96, 128, 255));
        for (int i = 0; i < scores.size(); i++) {
            if (i < 5)
                g.drawString((i + 1) + ": " + scores.get(i), SCRSIZE / 4 + BLOCKSIZE,
                             SCRSIZE - (SCRSIZE / 3) + (i * fm.getHeight()));
            else if (i < 10)
                g.drawString((i + 1) + ": " + scores.get(i), SCRSIZE / 2 + BLOCKSIZE,
                             SCRSIZE - (SCRSIZE / 3) + ((i - 5) * fm.getHeight()));
        }
    }

    /**
     * End the game if remaining lives reaches 0.
     */
    public void gameOver() {
        if (gt != GameType.VERSUS) {
            if (score > 1)
                sl.writeScore(score);
        }
        gt = GameType.INTRO;
        numBoardsCleared = 0;
        Date d = new Date();
        leaderBoardGui.showEndGameScreen(this.score, d);
        grid.levelInit(0);
    }

    /**
     * Detects when ghosts and pacman collide
     *
     * @param ghosts An array of Ghost
     */
    public void detectCollision(ArrayList<Ghost> ghosts)
    {
        for (Character pacman : pacmen)
        {
            for (Character ghost : ghosts)
            {
                if (Math.abs(pacman.x - ghost.x) < 20 &&
                    Math.abs(pacman.y - ghost.y) < 20 )
                    pacman.death();
            }
        }
    }

    /**
     * Returns true if any pacman is alive, returns false if they
     * are all dead
     *
     * @return true if any surviving, false if all dead
     */
    public boolean checkAlive()
    {
        for (Character pacman : pacmen)
            if (pacman.alive) return true;
        return false;
    }

    /**
     * Initialize game variables
     */
    public void gameInit()
    {
        grid.levelInit(numBoardsCleared);
        levelContinue();
        score = 0;
        numGhosts = 6;
        curSpeed = 3;

        try {
            this.beginningAudio.play();
        } catch (Exception e) {
            e.printStackTrace();
        }

        switch (gt)
        {
            case SINGLEPLAYER:
                pacmen = new Character[1];
                pacmen[0] = pacman;
                pacman.reset();
                break;
            case COOPERATIVE:
                pacmen = new Character[2];
                pacmen[0] = pacman;
                pacmen[1] = msPacman;
                pacman.reset();
                msPacman.reset();
                break;
            case VERSUS:
                pacmen = new Character[1];
                pacmen[0] = pacman;
                pacman.reset();
                break;
        }
    }

    /**
     * Initialize Pacman and ghost position/direction
     */
    public void levelContinue()
    {
        numPellet = grid.getPelletNum();
        ghosts.clear();
        if(gt == GameType.VERSUS)
        {
            ghosts.add(ghost1);
            ghosts.add(ghost2);
        }
        else
        {
            for (int i = 0; i < numGhosts; i++)
            {
                int random = (int) (Math.random() * curSpeed) + 1;
                ghosts.add(new Ghost(4 * BLOCKSIZE, 4 * BLOCKSIZE, random));
            }
        }
        switch (gt)
        {
            case SINGLEPLAYER:
                pacman.resetPos();
                break;
            case COOPERATIVE:
                pacman.resetPos();
                msPacman.resetPos();
                break;
            case VERSUS:
                pacman.resetPos();
                for (Character ghost : ghosts)
                {
                    ghost.resetPos();
                    if (numBoardsCleared == 3)
                        ghost.speed = 6;
                }
                break;
        }
    }

    /**
     * Paint graphics onto screen
     *
     * @param g a Graphics object
     */
    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D g2d = (Graphics2D) g;

        grid.drawMaze(g2d);
        drawScore(g);
        switch(gt)
        {
            case INTRO:
                showIntroScreen(g);
                break;
            case HELP:
                showHelpScreen(g);
                break;
            default:
                playGame(g2d);
        }

        if(!timer.isRunning())
            showPauseScreen(g);

        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }

    /**
     * Repaint the graphics each frame
     *
     * @param e an ActionEvent
     */
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    /**
     * Calls the leaderboards main method with the command line arguments
     *
     * @param args - represents the command line arguments
     */
    public void callLeaderboardMain(String args) {
        leaderBoardGui.setLeaderBoardFileName(args);
    }

    /**
     * Class that handles key presses for game controls
     */
    class TAdapter extends KeyAdapter {

        /**
         * Detects when a key is pressed.<p>
         * In-game: Changes Pacman's direction of movement with the arrow keys. Quit game by pressing the escape key.<p>
         * Not in-game: Press the 'S' key to begin the game.
         *
         * @param e a KeyEvent
         */
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if (gt == GameType.INTRO)
            {
                switch(key)
                {
                    case KeyEvent.VK_S:
                        gt = GameType.SINGLEPLAYER;
                        gameInit();
                        break;
                    case KeyEvent.VK_D:
                        gt = GameType.COOPERATIVE;
                        gameInit();
                        break;
                    case KeyEvent.VK_F:
                        gt = GameType.VERSUS;
                        gameInit();
                        break;
                    case KeyEvent.VK_H:
                        gt = GameType.HELP;
                        break;
                }
            }
            else if(gt == GameType.HELP)
            {
                switch(key)
                {
                    case KeyEvent.VK_H:
                        gt = GameType.INTRO;
                        break;
                }
            }
            else
            {
                switch (gt)
                {
                    case SINGLEPLAYER:
                        pacman.keyPressed(key);
                        break;
                    case COOPERATIVE:
                        pacman.keyPressed(key);
                        msPacman.keyPressed(key);
                        break;
                    case VERSUS:
                        pacman.keyPressed(key);
                        ghost1.keyPressed(key);
                        ghost2.keyPressed(key);
                        break;
                }

                switch(key)
                {
                    case KeyEvent.VK_PAUSE:case KeyEvent.VK_P:
                        if (timer.isRunning())
                        {
                            timer.stop();
                            repaint();
                        }
                        else
                            timer.start();
                        break;
                    case KeyEvent.VK_ESCAPE:
                        if(timer.isRunning())
                        {
                            gt = GameType.INTRO;
                            numBoardsCleared = 0;
                            grid.levelInit(0);
                        }
                        break;
                }
            }
        }

        public void keyReleased(KeyEvent e) {
            int key = e.getKeyCode();

            switch (gt)
                {
                    case SINGLEPLAYER:
                        pacman.keyReleased(key);
                        break;
                    case COOPERATIVE:
                        pacman.keyReleased(key);
                        msPacman.keyReleased(key);
                        break;
                    case VERSUS:
                        pacman.keyReleased(key);
                        ghost1.keyReleased(key);
                        ghost2.keyReleased(key);
                        break;
                }
        }
    }
}
