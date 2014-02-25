package edu.ucsb.cs56.projects.games.pacman;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import java.util.Date;

/**
* Stores instances of GamePlayed objects and sorts them by highest score--keeps track of Game History
* Loads and Stores the GamePlayed Objects in a file
* @author Kateryna Fomenko
* @author Deanna Hartsook
* @version CS56, Winter 2014
 */

public class Leaderboard<GamePlayed> extends ArrayList<GamePlayed>{
	
	/**Add function that adds a GamePlayed object to the Leaderboard in the proper spot 
	* @param g - represents a GamePlayed object that is to be added to the leaderboard
	* @return boolean - true if added succesfully, false otherwise
	*/
	 @Override
	 public boolean add(GamePlayed g){
		 int i;
		 for(GamePlayed game: this){	
			 if(game.getScore() < g.getScore()){
				this.add(i, g); 
			 }
			i++;
		 }
	 }
	
	
	
}
