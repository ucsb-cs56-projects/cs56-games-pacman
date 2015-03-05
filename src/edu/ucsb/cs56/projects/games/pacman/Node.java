package edu.ucsb.cs56.projects.games.pacman;

import java.awt.*;

/**
 * Class representing a node (tile) in map used for AI pathfinding
 * Algorithm used: A*
 * Read more: http://en.wikipedia.org/wiki/A*_search_algorithm
 *
 * @author Kelvin Yang
 * @version CS56, W15
 */
public class Node extends Point implements Comparable<Node>
{
    public static int tx, ty;
    public int gCost, hCost, fCost, dir;
    public Node parent;

    /**
     * Constructor for node
     *
     * @param x x grid coordinate of initial node
     * @param y y grid coordinate of initial node
     * @param gCost path cost to reach this node
     */
    public Node(int x, int y, int gCost)
    {
        //should help to prevent some crashes
        this.x = x % Board.NUMBLOCKS;
        this.y = y % Board.NUMBLOCKS;
        this.gCost = gCost;
        hCost = (int)Math.sqrt(Math.pow(x - tx, 2.0) + Math.pow(y - ty, 2.0));
        fCost = gCost + hCost;
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

        return n;
    }

    /**
     * Sets the direction of movement into node
     * Prevents abrupt movements (left <-> right and up <-> down)
     *
     * @param dx change in x
     * @param dy change in y
     */
    public void setDir(int dx, int dy)
    {
        if(Math.abs(dx + dy) != 1)
            throw new IllegalArgumentException("Cannot set direction " + dx + ", " + dy + ".");

        if(dx == -1) dir = 1;       //left
        else if(dy == -1) dir = 2;  //up
        else if(dx == 1) dir = 3;   //right
        else if(dy == 1) dir = 4;   //down
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
}
