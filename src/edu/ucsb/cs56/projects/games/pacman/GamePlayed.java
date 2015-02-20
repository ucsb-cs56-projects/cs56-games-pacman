package edu.ucsb.cs56.projects.games.pacman;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Stores information about the game that has just been played
 *
 * @author Kateryna Fomenko
 * @author Deanna Hartsook
 * @version CS56, Winter 2014
 */

public class GamePlayed implements Serializable {
    private String name;
    private Date d;
    private int score;

    /**
     * Constructor to create a GamePlayed object
     *
     * @param name  Player's name
     * @param score Player's score
     */
    public GamePlayed(String name, Date date, int score) {
        this.name = name;
        this.d = date;
        this.score = score;
    }

    /**
     * Overriden version of toString() method for GamePlayed Class
     *
     * @return String returns string print out a the GamePlayed object
     */
    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String date = dateFormat.format(this.d);

        String result = String.format("%15s %5d (%10s)", this.name, this.score, date);
        return result;
    }

    /**
     * Overriden version of equals() method for GamePlayed Class
     *
     * @return boolean - represents whether the two GamePlayed Objects are equal or false
     */
    @Override
    public boolean equals(Object o) {

        GamePlayed g = (GamePlayed) o;

        if (!this.name.equals(g.name)) {
            return false;
        }
        if (this.score != g.score) {
            return false;
        }
        if (!this.d.equals(g.d)) {
            return false;
        }
        return true;
    }

    /**
     * Getter that returns the name for this GamePlayed instance
     *
     * @return String name of player
     */
    public String getName() {
        return this.name;
        //~ return "stub";
    }

    /**
     * Getter that returns the date for this GamePlayed instance
     *
     * @return Date when this GamePlayed instance was created
     */
    public Date getDate() {
        return this.d;
        //~ Date d = new Date();
        //~ return d;
    }

    /**
     * Getter that returns the score for this GamePlayed instance
     *
     * @return int score
     */
    public int getScore() {
        return this.score;
        //~ return -128349;
    }

}
