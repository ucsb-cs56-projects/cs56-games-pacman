package edu.ucsb.cs56.projects.games.pacman;

/**
 *  This class represents location in x-y coordinates
 *  of an object on the game map
 *
 *  @author Wei Tung Chen
 *  @author Nicholas Duncan
 *  @version CS56 F17
*/

public class Location{
    private int x; //x coordinate of location
    private int y; //y coordinate of location

    /**
     * Location constructor takes an x and y
     * integer to track location of object
     * @constructor
     * @param x x-coordinate
     * @param y y-coordinate
     *  Location constructor takes an x and y
     *  integer to track location of object
     *  @constructor
     *  @param x x-coordinate
     *  @param y y-coordinate
     */
    public Location(int x, int y){
	this.x = x;
	this.y = y;
    }

    /**
     * Gets the x-coordinate of the location
     *
     * @return x-coordinate
     *  @return x-coordinate
     */
    public int getX(){
	return this.x;
    }

    /**
     * Get the y-coordinate of the location
     *
     *  @return y-coordinate
     */
    public int getY(){
	return this.y;
    }

    /**
     * Changes the x-coordinate to the specified coordinate
     *
     * @param x new x-coordinate
     *  @param x new x-coordinate
     */
    public void setX(int x){
	this.x = x;
    }

    /**
     * Changes the y-coordinate to the specified coordinate
     *
     * @param y new y-coordinate
     *  @param y new y-coordinate
     */
    public void setY(int y){
	this.y = y;
    }
}
