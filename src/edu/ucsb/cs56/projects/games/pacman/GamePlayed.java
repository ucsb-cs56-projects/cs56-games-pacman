package edu.ucsb.cs56.projects.games.pacman;

import java.util.Date
/**
* Stores information about the game that has just been played
* @author Kateryna Fomenko
* @author Deanna Hartsook
* @version CS56, Winter 2014
 */

public class GamePlayed {
    private String name;
    private Date d;
    private int score;
    
    /**
    * Constructor to create a GamePlayed object
    * @param name Player's name
    * @param score Player's score
     */
    public GamePlayed(String name, int score){
        this.name = name;
        this.d = new Date();
        this.score = score;
    }
    
    /**
     * Getter that returns the name for this GamePlayed instance
     * @return String name of player
     */
    public int getName() {
        return this.name;
    }
    
    /**
     * Getter that returns the date for this GamePlayed instance
     * @return Date when this GamePlayed instance was created
     */
    public int getDate() {
        return this.date;
    }
    
    /**
    * Getter that returns the score for this GamePlayed instance
    * @return int score
    */
    public int getScore() {
        return this.score;
    }
    

}