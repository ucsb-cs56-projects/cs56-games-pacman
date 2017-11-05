package edu.ucsb.cs56.projects.games.pacman;

import java.util.Queue;
import java.util.LinkedList;


/**
   This is a class to represent a Ghost House which holds ghost
   starting coordinates and tracks when ghosts are released onto
   the map to chase Pacman

   @author Wei Tung Chen
   @author Nicholas Duncan
*/

public class GhostHouse{
    private Queue<Ghost> ghosts;
    private Location topLeft; //location of the top left block of the GhostHouse
    private int width; //amount of blocks horizontally contained in the ghostHouse
    private final static double releaseTime = 3;
    private long lastReleaseTime = 0;
    private boolean timerRunning = false;
    private int blockSize;

    
    public GhostHouse(Location topLeft, int width, int blockSize){
	ghosts = new LinkedList<Ghost>();
	this.topLeft = topLeft;
	this.width = width;
	this.blockSize = blockSize;

	System.out.println("Intialize ---------");
	System.out.println("    TopLeft: " + topLeft.getX() + ", " + topLeft.getY());
    }

    public void addGhost(Ghost ghost){
	ghosts.add(ghost);
	ghost.setSpeed(0);
    }

    public Location getTopLeft(){
	return topLeft;
    }

    public int getWidth(){
	return width;
    }

    public void update(){
	long currentTime = System.currentTimeMillis() / 1000; //in seconds
	if(ghosts.size() == 0){
	    lastReleaseTime = currentTime;
	    return;
	}
	if(currentTime - lastReleaseTime > releaseTime){
	    releaseGhost();
	    lastReleaseTime = currentTime;
	}
    }

    private void releaseGhost(){
	if(ghosts.size() <= 0){
	    return;
	}
	Ghost ghost = ghosts.remove();
	Location releaseLocation = new Location(topLeft.getX() + ((this.width / 2) + 1), topLeft.getY() - 1);
	ghost.setX(releaseLocation.getX() * blockSize); //setting ghost x in pixels
	ghost.setY(releaseLocation.getY() * blockSize); //setting ghost y in pixels

	ghost.setSpeed((int) (Math.random() * Ghost.defaultSpeed) + 1);
	
	System.out.println("Spawning Point: " + releaseLocation.getX() + ", " + releaseLocation.getY());
    }
    
    public void resetTimer(){
	lastReleaseTime = 0;
    }
}
