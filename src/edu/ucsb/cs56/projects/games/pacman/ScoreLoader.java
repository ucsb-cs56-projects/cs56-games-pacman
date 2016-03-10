package edu.ucsb.cs56.projects.games.pacman;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Writes and loads a list of int from a textfile
 *
 * @author Daniel Ly
 * @author Dario Castellanos Anaya
 * @version CS56, Spring 2013
 */
public class ScoreLoader {
	private String filePath;

	/**
	 * Constructor to create an object of class ScoreLoader
	 *
	 * @param filePath File path of the file the ScoreLoader will read and load from
	 */
	public ScoreLoader(String filePath) {
		this.filePath = filePath;
	}

	/**
	 * Scans the save file and assigns its values into an ArrayList
	 *
	 * @return ArrayList of values obtained from the save file
	 */
	public ArrayList<Integer> loadScores() {
		File file = new File(filePath);
		ArrayList<Integer> scoreList = new ArrayList<Integer>();
		try {
			file.createNewFile(); // make file if it does not exist

			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				String value = line;
				Integer scoreRec = new Integer(value);
				scoreList.add(scoreRec);
			}
			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return scoreList;
	}

	/**
	 * Rewrites the save file using the list of scores given
	 *
	 * @param scores An ArrayList containing all the scores to be written
	 */
	public void saveScore(ArrayList<Integer> scores) {
		FileWriter fileWriter;
		try {
			fileWriter = new FileWriter(filePath);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			if (scores != null) {
				Collections.sort(scores); // Sorts scores into ascending order
				Collections.reverse(scores); // reverses scores into descending order
				for (Integer score : scores) {
					bufferedWriter.write(score + "\n");
				}
			}
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Saves multiple scores into the save file
	 *
	 * @param scores Scores to be saved
	 */
	public void writeScore(int... scores) {
		ArrayList<Integer> scoreList = loadScores();
		for (int score : scores) scoreList.add(score);
		saveScore(scoreList);
	}

	/**
	 * Deletes the save file
	 */
	public void resetScores() {
		File file = new File(filePath);
		file.delete();
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
