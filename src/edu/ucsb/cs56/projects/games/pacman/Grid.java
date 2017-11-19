package edu.ucsb.cs56.projects.games.pacman;

import java.awt.*;
import java.util.Arrays;
import java.io.*;

/**
 * Class representing the map layout
 *
 * @author Yuxiang Zhu
 * @author Joseph Kompella
 * @author Kekoa Sato
 * @version CS56 F16
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

	short[][] screenData;
	short[][][] levelsData;
	Color mazeColor, dotColor, fruitColor;

	/**
	 * Constructor for Board object
	 */
	public Grid() {
		screenData = new short[Board.NUMBLOCKS][Board.NUMBLOCKS];
		mazeColor = new Color(5, 100, 5);
		dotColor = new Color(192, 192, 0);
		fruitColor = new Color(255,0,0);

		String[] loadableLevels = {"level1.data", "level2.data", "level3.data", "level4.data", "level5.data"};
		this.levelsData = new short[loadableLevels.length][1][1];
		for(int i = 0; i < loadableLevels.length; i++) {
			GridData level = loadLevel("assets/levels/"+loadableLevels[i]);
			levelsData[i] = level.get2DGridData();
		}
	}

	public GridData loadLevel(String asset_path) {
		try {
			InputStream input_stream = getClass().getResourceAsStream(asset_path);
			//System.out.println(input_stream);
			ObjectInputStream object_input_stream = new ObjectInputStream(input_stream);
			GridData data = (GridData)object_input_stream.readObject();
			return data;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
			System.out.println("Failed to load level data assets.");
			System.exit(2);
		}
		return null;
	}

	/**
	 * Checks if there are any pellets left for Pacman to eat, and restarts the game on the next board in a
	 * higher difficulty if finished
	 *
	 * @return A boolean indicating whether or not the maze is finished
	 */
	public boolean checkMaze() {
		int pellets = getItemNum(this.screenData, GridData.GRID_CELL_PELLET);
		int pills = getItemNum(this.screenData, GridData.GRID_CELL_POWER_PILL);
		return (pellets + pills == 0);
	}

	/**
	 * Count the number of pellets left for Pacman to eat
	 *
	 * @return An int indicating how many pellets are left
	 */
	public int getPelletNum() {
		return getItemNum(this.screenData, GridData.GRID_CELL_PELLET);
	}

	/**
	 * Count the number of power pills left for Pacman to eat
	 *
	 * @return An int indicating how many pills are left
	 */
	public int getPillNum() {
		return getItemNum(this.screenData, GridData.GRID_CELL_POWER_PILL);
	}

	/**
	 * Count the number of pellets in each map
	 *
	 * @param numBoardsCleared the number of levels that have been cleared
	 * @return An int indicating how many pellets are left
	 */
	public int getPelletNumForMap(int numBoardsCleared) {
		return getItemNum(this.levelsData[numBoardsCleared % this.levelsData.length], GridData.GRID_CELL_PELLET);
	}

	/**
	 * Count the number of items on the map
	 * @param grid_type type of item (GridData constant)
	 * @return the number of items on the map
	 */
	private int getItemNum(short[][] map, byte grid_type) {
		int count = 0;
		for (int i = 0; i < Board.NUMBLOCKS; i++) {
			for (int j = 0; j < Board.NUMBLOCKS; j++) {
				if ((map[i][j] & grid_type) != 0)
					count++;
			}
		}
		return count;
	}

	/**
	 * Initialize level with data containing game object information
	 * @param numBoardsCleared the number of levels that have been cleared
	 */
	public void levelInit(int numBoardsCleared) {
		for (int i = 0; i < Board.NUMBLOCKS; i++) {
			screenData[i] = Arrays.copyOf(this.levelsData[numBoardsCleared % this.levelsData.length][i], Board.NUMBLOCKS);
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
        /**
	 * A method to set the x and y coordinates to a random number on the board
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
		if(this.getPelletNum()< this.getPelletNumForMap(numBoardsCleared)) {
			if (fruitCounter > 100) {
				fruitCounter = 0;
				this.randomBlock();
				while(true) {
					if (((screenData[this.x][this.y] & GridData.GRID_CELL_PELLET) == 0) && (this.levelsData[numBoardsCleared % this.levelsData.length][this.x][this.y] & GridData.GRID_CELL_PELLET) != 0) {
						screenData[this.x][this.y] = (short) (screenData[this.x][this.y] | GridData.GRID_CELL_FRUIT);
						break;
					}
					this.randomBlock();
				}
			} else {
				fruitCounter++;
			}
		}else {
			return;
		}
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

				if ((screenData[i][j] & GridData.GRID_CELL_BORDER_LEFT) != 0) // draws left
					g2d.drawLine(x, y, x, y + Board.BLOCKSIZE - 1);
				if ((screenData[i][j] & GridData.GRID_CELL_BORDER_TOP) != 0) // draws top
					g2d.drawLine(x, y, x + Board.BLOCKSIZE - 1, y);
				if ((screenData[i][j] & GridData.GRID_CELL_BORDER_RIGHT) != 0) // draws right
					g2d.drawLine(x + Board.BLOCKSIZE - 1, y, x + Board.BLOCKSIZE - 1, y + Board.BLOCKSIZE - 1);
				if ((screenData[i][j] & GridData.GRID_CELL_BORDER_BOTTOM) != 0) // draws bottom
					g2d.drawLine(x, y + Board.BLOCKSIZE - 1, x + Board.BLOCKSIZE - 1, y + Board.BLOCKSIZE - 1);

				g2d.setColor(dotColor);
				if ((screenData[i][j] & GridData.GRID_CELL_PELLET) != 0) // draws pellet
					g2d.fillRect(x + 11, y + 11, 2, 2);
				
				if ((screenData[i][j] & GridData.GRID_CELL_POWER_PILL) != 0) // draws power pill
					g2d.fillOval(x + 6, y + 6, 12, 12);
				g2d.setColor(fruitColor);
				if ((screenData[i][j] & GridData.GRID_CELL_FRUIT) != 0) // draws fruit
					g2d.fillRect(x + 10, y + 10, 4, 4);
			}
		}
	}
}
