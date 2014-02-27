package edu.ucsb.cs56.projects.games.pacman;

import java.io.*;
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

public class Leaderboard extends ArrayList<GamePlayed> implements Serializable {
	private static String filename="pacmanLeaderbaord.ser";
	/**Add function that adds a GamePlayed object to the Leaderboard in the proper spot 
	* @param g - represents a GamePlayed object that is to be added to the leaderboard
	* @return boolean - true if added succesfully, false otherwise
	*/

	 public boolean add(GamePlayed g){
		 int i=0;
         if (this.isEmpty()) {
             this.add(0,g);
             return true;
         }
		 for(GamePlayed game: this){
			 if(game.getScore() < g.getScore()){
				this.add(i, g);
                 return true;
			 }
			i++;
		 }
         return false;
	 }

	public boolean add(String name, Date d, int score){
		GamePlayed g = new GamePlayed(name, d, score);
		return this.add(g);
		
	}
    
    public void save(){
        try {
            FileOutputStream fileOut = new FileOutputStream(this.filename);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this);
            out.close();
        }catch(IOException io) {
            //~ io.printStackTrace();
            System.out.println("COULD NOT SAVE LEADERBOARD");
        }
    }
    
    public void load(){
        Leaderboard temp = null;
        try{
            FileInputStream fileIn = new FileInputStream(this.filename);
            ObjectInputStream in  = new ObjectInputStream(fileIn);
            temp = (Leaderboard) in.readObject();
            in.close();
            fileIn.close();
        }catch (Exception e){
            //~ e.printStackTrace();
            System.out.println("NO LEADERBOARD FOUND");
            return;
        }
        this.clear();
        this.addAll(temp);
    }
	
	public String getTopThree(){
		int length = this.size();
		String result ="";
		if(length < 3){
			for(int i = 0; i < length; i++){
				result += this.get(i) + "\n";
			}
		}else{
			for(int i=0; i< 3; i++){
				result += this.get(i) + "\n";
			}
		}
		return result;
		
	}
	
	//~ public String getPlayerTopThree(){
		//~ 
	//~ }
	
	
    public static void main(String [] args){
        Leaderboard l = new Leaderboard();
        Date d = new Date();
        GamePlayed g = new GamePlayed("Bob", d, 10);
        l.add(g);
        l.save();
        l.clear();
        l.load();
        System.out.println(l.get(0).getName());
    }
	
	
}
