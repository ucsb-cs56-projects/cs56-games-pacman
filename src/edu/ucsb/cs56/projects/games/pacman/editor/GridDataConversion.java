package edu.ucsb.cs56.projects.games.pacman.editor;

import java.io.*;
import java.util.*;
import edu.ucsb.cs56.projects.games.pacman.GridData;

/*
 This file implements a method of converting the old 2D array of level grid data
 into a serialized file of GridData. This file is left here for historical reasons
 as this system is no longer used and is succeeded by the level editor.
 */

public class GridDataConversion {
	public static void main(String[] args) {
		short convertData[][] = new short[][]{
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

		// Determine the size of the grid.
		int grid_width = convertData[0].length;
		int grid_height = convertData.length;

		// Currently, the code presumes that the width and length
		// of the board are the same.
		if(grid_height != grid_width) {
			System.out.println("Abort. Unsupported grid dimensions.");
			System.exit(1);
		}

		int grid_data_size = grid_width*grid_height;

		// Flatten the data into a 1D array.
		short[] grid_data = new short[grid_data_size];
		for(int i = 0; i < grid_data_size; i++) {
			grid_data[i] = convertData[i / grid_width][i % grid_width];
		}

		// Store the data into a serializable object.
		GridData grid_data_out = new GridData(grid_width, grid_data);

		// Dump the grid data object into file.
		try {
			FileOutputStream grid_data_out_file = new FileOutputStream("level_out.data");
			ObjectOutputStream grid_data_object_out = new ObjectOutputStream(grid_data_out_file);
			grid_data_object_out.writeObject(grid_data_out);
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}