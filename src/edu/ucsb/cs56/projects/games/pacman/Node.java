package edu.ucsb.cs56.projects.games.pacman;

import java.awt.*;

/**
 * Class representing a node (tile) in map used for AI pathfinding
 * Algorithm used: A*
 * Read more: http://en.wikipedia.org/wiki/A*_search_algorithm
 *
 * Currently, the hCost is NOT admissable for going through tunnels
 * since it will evaluate the cartesian distance and overestimate.
 * In order for ghosts to consider tunnels, you need to modify hCost
 * heuristic to be admissable.
 *
 * @author Kelvin Yang
 * @author Nicholas Duncan
 * @author Wei Tung Chen
 * @version CS56, F17
 */
public class Node extends Point implements Comparable<Node>
{
	public static int tx, ty;
	public int gCost, hCost, fCost, dir;
	public Node parent;
	public MutableInt distance;

	/**
	 * Constructor for node
	 *
	 * @param x x grid coordinate of initial node
	 * @param y y grid coordinate of initial node
	 * @param gCost path cost to reach this node
	 */
	public Node(int x, int y, int gCost)
	{
		//should help to prevent crashes when tunneling
		this.x = (x + Board.NUMBLOCKS) % Board.NUMBLOCKS;
		this.y = (y + Board.NUMBLOCKS) % Board.NUMBLOCKS;
		this.gCost = gCost;
		hCost = (int)Math.sqrt(Math.pow(x - tx, 2.0) + Math.pow(y - ty, 2.0));
		fCost = gCost + hCost;
	}

	/**
	 * Initializes distance for a path. Only do it once
	 */
	public void init()
	{
		distance = new MutableInt();
	}

	/**
	 * Creates a child node which represents movement from parent node
	 *
	 * @param dx change in x
	 * @param dy change in y
	 * @return new child node with set values
	 */
	public Node getChild(int dx, int dy)
	{
		if(Math.abs(dx + dy) != 1)
			throw new IllegalArgumentException("Cannot get child " + dx + ", " + dy + ".");

		Node n = new Node(x + dx, y + dy, gCost + 1);
		n.parent = this;
		n.setDir(dx, dy);
		n.distance = distance;
		distance.plus();
		return n;
	}

	/**
	 * Sets the direction of movement into node
	 * Prevents abrupt movements (left to right and up to down)
	 *
	 * @param dx change in x
	 * @param dy change in y
	 */
	public void setDir(int dx, int dy)
	{
	    try{
		if(Math.abs(dx + dy) != 1)
		    throw new IllegalArgumentException("Cannot set direction " + dx + ", " + dy + ".");

		if(dx == -1) dir = Direction.LEFT;       //left
		else if(dy == -1) dir = Direction.UP;  //up
		else if(dx == 1) dir = Direction.RIGHT;   //right
		else if(dy == 1) dir = Direction.DOWN;   //down
	    }catch(IllegalArgumentException ex){
		System.out.println("Cannot set direction error");
	    }
	}

	/**
	 * Compares two nodes and determines which is better
	 * Used for sorting in priority queue
	 *
	 * @param n node being compared to
	 * @return int signifying order
	 */
	public int compareTo(Node n) //make sure this works?
	{
		if(fCost - n.fCost != 0)
			return fCost - n.fCost;
		else
			return hCost - n.hCost;
	}

	/**
	 * Need for distance so it can be used as a reference
	 * but is also mutable
	 */
	class MutableInt
	{
		int value = 0;
		public void plus() { ++value; }
	}
}
