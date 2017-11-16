package edu.ucsb.cs56.projects.games.pacman;

import java.util.Queue;
import java.util.List;
import java.util.LinkedList;


/**
 * This is a class to represent a Ghost House which holds ghost
 * starting coordinates and tracks when ghosts are released onto
 * the map to chase Pacman
 *
 * @author Wei Tung Chen
 * @author Nicholas Duncan
 * @version CS56 F17
 */
public class GhostHouse{
    private Queue<Ghost> ghosts; //ghosts held in the ghosthouse
    private Location topLeft; //location of the top left block of the GhostHouse
    private int width; //amount of blocks horizontally contained in the ghostHouse
    private final static double releaseTime = 3; //time between releases of ghosts
    private long lastReleaseTime = 0; //time that a ghost was last released
    private boolean timerRunning = false; //not currently used but could be implemented to turn timer on/off
    private int blockSize; //size in pixels of a block on the map (from board class)

    /**
     *Constructor for a ghosthouse object
     *note that location must matched with the level map data that is created
     *@param topLeft Location of the top left corner of the ghost house
     *@param width how many "blocks" the house encompasses (typically 1 block per ghost)
     *@param blockSize the size of each block as defined by the board class (must pass this in)
     */
    public GhostHouse(Location topLeft, int width, int blockSize){
        ghosts = new LinkedList<Ghost>();
        this.topLeft = topLeft;
        this.width = width;
        this.blockSize = blockSize;

        //System.out.println("Intialize ---------");
        //System.out.println("    TopLeft: " + topLeft.getX() + ", " + topLeft.getY());
    }

    /**
     *Method to add a ghost to a ghost house object
     *Ghosts in ghost house will properly spawn and respawn
     *@param ghost the ghost desired to be put into the ghost house
     */
    public void addGhost(Ghost ghost){
        ghosts.add(ghost);
        ghost.setSpeed(0); //ghost speed is now set to 0 so that ghosts won't run through ghost house walls
    }

    /**
     *Method to add ghosts to a ghost house object
     *Ghosts in ghost house will properly spawn and respawn
     *@param ghosts the ghosts desired to be put into the ghost house
     */
    public void addGhosts(List<Ghost> ghosts){
        //Empty ghost house
        while (this.ghosts.size() != 0)
        this.ghosts.remove();

        //Add ghosts to ghost house
        for (Ghost ghost1 : ghosts) {
            ghost1.death();
            addGhost(ghost1);
        }
    }

    /**
     *returns the top left location of the ghost house
     */
    public Location getTopLeft(){
        return topLeft;
    }

    /**
     *returns the width of the ghost house
     */
    public int getWidth(){
        return width;
    }

    /**
     *returns the ghosts list
     */
    public Queue<Ghost> getGhosts(){
        return ghosts;
    }

    /**
     *This method updates the timer on the ghost house so that ghosts
     *are released at proper intervals - call this in the logic updating loops
     */
    public void update(){
        long currentTime = System.currentTimeMillis() / 1000; //in seconds
        if(ghosts.size() == 0){
            lastReleaseTime = currentTime;
            return;
        }
        if(currentTime - lastReleaseTime > releaseTime){ //if its been over (releaseTime) second(s) since a ghost has last been released
            releaseGhost(); 
            lastReleaseTime = currentTime; //another ghost will be released in (releaseTime) second(s)
        }
    }

    /**
     *Takes a ghost out of the ghost house and spawns it at the location 
     *above the ghost house
     */
    private void releaseGhost(){
        if(ghosts.size() <= 0){
            return;
        }
        Ghost ghost = ghosts.remove();
        Location releaseLocation = new Location(topLeft.getX() + ((this.width / 2) + 1), topLeft.getY() - 1);
        ghost.setX(releaseLocation.getX() * blockSize); //setting ghost x in pixels
        ghost.setY(releaseLocation.getY() * blockSize); //setting ghost y in pixels

        ghost.setSpeed((int) (Math.random() * Ghost.defaultSpeed) + 1); //ghost speed is randomly set upon release

        //System.out.println("Spawning Point: " + releaseLocation.getX() + ", " + releaseLocation.getY());
    }
    /**
     *Resets the timer on the ghost house
     *must wait a full time interval before the next ghost is released
     */
    public void resetTimer(){
        lastReleaseTime = 0;
    }
}
