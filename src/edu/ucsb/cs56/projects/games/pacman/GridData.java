package edu.ucsb.cs56.projects.games.pacman;

import java.io.Serializable;


/**
 * A class to hold the information that is associated with a level
 * such as walls, fruit, pellets, and power pills
 *
 * @version cs56 f17
 */
public class GridData implements Serializable {
	private int grid_width;
	private short[] grid_data;

	static final long serialVersionUID = 56565656565656L;

	final public static byte GRID_CELL_BORDER_LEFT = 1;
	final public static byte GRID_CELL_BORDER_TOP = 2;
    	final public static byte GRID_CELL_BORDER_RIGHT = 4;
    	final public static byte GRID_CELL_BORDER_BOTTOM = 8;
	final public static byte GRID_CELL_PELLET = 16;
	final public static byte GRID_CELL_FRUIT = 32;
	final public static byte GRID_CELL_POWER_PILL = 64;	

        /**
	 * Constructor for a Grid object 
	 *
	 * @param grid_width the horizontal size of the grid object
	 * @param grid_data an array that holds the information about items in the level
	 */
	public GridData(int grid_width, short[] grid_data) {
		this.grid_width = grid_width;
		this.grid_data = grid_data;
	}

        /**
	 * A method to specify the grid's width
	 *
	 * @param width an integer that represents the desired width
	 */
	public void setGridWidth(int grid_width) {
		this.grid_width = grid_width;
	}

        /**
	 * Method to set the grid's data array
	 *
	 * @param grid_data an array of type short that holds the desired grid data
	 */
	public void setGridData(short[] grid_data) {
		this.grid_data = grid_data;
	}

	public short[][] get2DGridData() {
		short[][] gridData = new short[this.grid_data.length / this.grid_width][this.grid_width];
		for(int i = 0; i < this.grid_data.length; i++) {
			gridData[i / this.grid_width][i % this.grid_width] = this.grid_data[i];
		}
		return gridData;
	}

	public int getGridWidth() {
		return this.grid_width;
	}

	public short[] getGridData() {
		return this.grid_data;
	}
}
