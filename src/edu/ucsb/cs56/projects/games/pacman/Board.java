package edu.ucsb.cs56.projects.games.pacman;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
        INTRO, SINGLEPLAYER, COOPERATIVE, VERSUS, LEADERBOARD
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
    private Character pacman, msPacman, ghost1, ghost2;
    private Character[] pacmen, playerGhosts;
    private Ghost[] ghosts;
    private int numGhosts = 6, numBoardsCleared = 0;
    private int curSpeed = 3;
    private int numPellet;
    private Timer timer;

    /**
     * Constructor for Board object
     */
    public Board() {
        addKeyListener(new TAdapter());
        grid = new Grid();
        pacman = new PacPlayer(8 * BLOCKSIZE, 11 * BLOCKSIZE, PacPlayer.PACMAN);
        msPacman = new PacPlayer(7 * BLOCKSIZE, 11 * BLOCKSIZE, PacPlayer.MSPACMAN);
        ghost1 = new Ghost(8 * BLOCKSIZE, 7 * BLOCKSIZE, 4, Ghost.GHOST1);
        ghost2 = new Ghost(8 * BLOCKSIZE, 7 * BLOCKSIZE, 4, Ghost.GHOST2);
        setFocusable(true);

        setBackground(Color.black);
        setDoubleBuffered(true);
        ghosts = new Ghost[MAX_GHOSTS];
        timer = new Timer(40, this);
        timer.start();
    }

    /**
     * Called by the system
     */
    public void addNotify() {
        super.addNotify();
        gt = GameType.INTRO;
        gameInit();
        numPellet = grid.getPelletNum();
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
                grid.incrementFruit(numBoardsCleared);
                pacman.move(grid);
                pacman.draw(g2d, this);
            }
            switch (gt)
            {
                case SINGLEPLAYER:
                    for (int i = 0; i < numGhosts; i++)
                    {
                        ghosts[i].moveAI(grid, pacmen);
                        ghosts[i].draw(g2d, this);
                    }
                    detectCollision(ghosts);
                    break;
                case COOPERATIVE:
                    if (msPacman.alive)
                    {
                        msPacman.move(grid);
                        msPacman.draw(g2d, this);
                    }
                    for (int i = 0; i < numGhosts; i++)
                    {
                        ghosts[i].moveAI(grid, pacmen);
                        ghosts[i].draw(g2d, this);
                    }
                    detectCollision(ghosts);
                    break;
                case VERSUS:
                    for (Character ghost : playerGhosts)
                    {
                        ghost.move(grid);
                        ghost.draw(g2d, this);
                    }

                    if (score >= numPellet)
                    {
                        score = 0;
                        numBoardsCleared++;
                        grid.levelInit(numBoardsCleared);
                        numPellet = grid.getPelletNum();
                        levelContinue();

                    }
                    detectCollision(playerGhosts);
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
     * @param g a Graphics2D object
     */
    public void showIntroScreen(Graphics2D g) {
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
     * Display the current score on the bottom right of the screen
     *
     * @param g a Graphics object
     */
    public void drawScore(Graphics2D g) {
        int i;
        int pelletsLeft;
        String s;
        String p;

        g.setFont(smallFont);
        g.setColor(new Color(96, 128, 255));
        if (gt == GameType.VERSUS) {
            pelletsLeft = numPellet - score;
            p = "Pellets left: " + pelletsLeft;
            g.drawString(p, SCRSIZE / 2 + 96, SCRSIZE + 16);
        } else {
            s = "Score: " + score;
            g.drawString(s, SCRSIZE / 2 + 136, SCRSIZE + 16);
        }
        for (i = 0; i < pacman.lives; i++) {
            g.drawImage(pacman.getLifeImage(), i * 28 + 8, SCRSIZE + 1, this);
        }
        if (gt == GameType.COOPERATIVE) {
            for (i = 0; i < msPacman.lives; i++) {
                g.drawImage(msPacman.getLifeImage(), i * 28 + 108, SCRSIZE + 1, this);
            }
        }
    }

    /**
     * Displays a list of scores on the bottom of the screen
     *
     * @param g a Graphics2D object
     */
    public void drawHighScores(Graphics2D g)
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
        gameInit();
    }

    /**
     * Detects when ghosts and pacman collide
     *
     * @param ghosts An array of Ghost
     */
    public void detectCollision(Character[] ghosts)
    {
        for (Character pacman : pacmen) {
            if (gt == GameType.VERSUS) {
                for (Character ghost : ghosts) {
                    if (pacman.x > (ghost.x - 12) && pacman.x < (ghost.x + 12) &&
                            pacman.y > (ghost.y - 12) && pacman.y < (ghost.y + 12) && gt != GameType.INTRO)
                        pacman.death();
                }
            } else {
                for (int i = 0; i < numGhosts; i++) {
                    if (pacman.x > (ghosts[i].x - 12) && pacman.x < (ghosts[i].x + 12) &&
                            pacman.y > (ghosts[i].y - 12) && pacman.y < (ghosts[i].y + 12) && gt != GameType.INTRO)
                        pacman.death();
                }
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
        {
            if (pacman.alive)
                return true;
        }
        return false;
    }

    /**
     * Initialize game variables
     */
    public void gameInit() {
        switch (gt) {
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
                playerGhosts = new Character[2];
                playerGhosts[0] = ghost1;
                playerGhosts[1] = ghost2;
                ghost1.reset();
                ghost2.reset();
                break;
        }
        grid.levelInit(numBoardsCleared);
        levelContinue();
        score = 0;
        numGhosts = 6;
        curSpeed = 3;
    }

    /**
     * Initialize Pacman and ghost position/direction
     */
    public void levelContinue() {
        int dx = 1;
        int random;

        for (short i = 0; i < numGhosts; i++)
        {
            random = (int) (Math.random() * curSpeed) + 1;
            ghosts[i] = new Ghost(4 * BLOCKSIZE, 4 * BLOCKSIZE, random);
            ghosts[i].dx = dx;
            dx = -dx;
        }
        switch (gt) {
            case SINGLEPLAYER:
                pacman.resetPos();
                break;
            case COOPERATIVE:
                pacman.resetPos();
                msPacman.resetPos();
                break;
            case VERSUS:
                pacman.resetPos();
                for (Character ghost : playerGhosts)
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
        drawScore(g2d);
        if (gt == GameType.INTRO)
            showIntroScreen(g2d);
        else //Won't be true if leaderboard is integrted
            playGame(g2d);

        if(!timer.isRunning())
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
        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }

    public String loadHelpFile(String filename) {
        String input = "";
        try {
            FileReader reader = new FileReader(filename);
            BufferedReader bufferedreader = new BufferedReader(reader);

            String textReader = bufferedreader.readLine();
            while (textReader != null) {
                input += "\n" + textReader;
                textReader = bufferedreader.readLine();
            }
        } catch (IOException ex) {
            System.out.println("Error: Could not load help file");
        }
        return input;
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
                        System.out.println("Help Selected");
                        JFrame helpFrame = new JFrame();
                        JPanel helpPanel = new JPanel();
                        JLabel helpLabel = new JLabel("Help");

                        helpLabel.setForeground(Color.white);
                        helpLabel.setPreferredSize(new Dimension(200, 20));
                        helpLabel.setHorizontalAlignment(SwingConstants.CENTER);

                        JTextArea text = new JTextArea(37, 40);
                        String instructions = loadHelpFile("instructions.txt");
                        text.setText(instructions);
                        text.setLineWrap(true);
                        text.setWrapStyleWord(true);
                        text.setCaretPosition(0);
                        text.setEditable(false);

                        JScrollPane scroller = new JScrollPane(text);
                        scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                        scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                        helpPanel.add(helpLabel);
                        helpPanel.add(scroller);
                        helpPanel.setBackground(new Color(0, 32, 48));
                        helpPanel.setForeground(Color.white);
                        helpFrame.getContentPane().add(helpPanel);
                        helpFrame.setSize(550, 700);
                        helpFrame.setVisible(true);
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
                        }
                        break;
                }
            }
        }
    }

}
