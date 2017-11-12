package edu.ucsb.cs56.projects.games.pacman;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.PriorityQueue;

/**
 * Class representing enemy ghosts in single player mode
 * and player ghosts in multiplayer mode
 *
 * @author Dario Castellanos Anaya
 * @author Daniel Ly
 * @author Kelvin Yang
 * @author Joseph Kompella
 * @author Kekoa Sato
 * @version CS56 F16
 */
public class Ghost extends Character {

	public static final String IMAGE_PATH = "assets/";
	public static final int TYPE_RED = 0;
	public static final int TYPE_PINK = 1;
	public static final int GHOST1 = 1;
	public static final int GHOST2 = 2;
        public static int defaultSpeed = 3;

        private Image ghost;
	private Image scared_ghost;
	private Grid grid;
	public boolean edible;
	public int prev_speed;
	public int edibleTimer;
	public int type;

	public Ghost(int x, int y, int speed, int type) {
		this(x, y, speed, type, 1, null);
	}

	public Ghost(int x, int y, int speed, int playerNum, Grid grid) {
		this(x, y, speed, 0, playerNum, grid);
	}

	public Ghost(int x, int y, int speed, int type, int playerNum, Grid grid) {
                super(x, y, playerNum);
                this.speed = speed;
		this.type = type;
                this.grid = grid;
                assetImagePath = IMAGE_PATH;
                loadImages();
                edible = false;
                prev_speed = speed;
                edibleTimer = 1;
        }

	/**
	 * Handles character's death
	 */
	public void death() {
		x = startX;
		y = startY;
		edible = false;
		edibleTimer = 0;
	}

	/**
	 * Handles character's death with set coordinates
	 */
	public void death(int newX, int newY) {
		x = newX;
		y = newY;
		edible = false;
		edibleTimer = 0;
	}

	/**
	 * Draws the ghost
	 *
	 * @param g a Graphics2D object
	 * @param canvas A Jcomponent object to be drawn on
	 */
	@Override
	public void draw(Graphics2D g, JComponent canvas) {
		int offset = 4;
		if(edible)
			g.drawImage(scared_ghost, x + offset, y + offset, canvas);
		else
			g.drawImage(ghost, x + offset, y + offset, canvas);
	}

	/**
	 * Load game sprites from images folder
	 */
	@Override
	public void loadImages() {
		try {
			if (type == TYPE_RED)
				ghost = ImageIO.read(getClass().getResource(assetImagePath + "ghostred.png"));
			else if (type == TYPE_PINK)
				ghost = ImageIO.read(getClass().getResource(assetImagePath + "ghostpink.png"));
			else {
				if (playerNum == GHOST1)
					ghost = ImageIO.read(getClass().getResource(assetImagePath + "ghostred.png"));
				else if (playerNum == GHOST2) 
					ghost = ImageIO.read(getClass().getResource(assetImagePath + "ghostpink.png"));
			} 
			scared_ghost = ImageIO.read(getClass().getResource(assetImagePath + "ghostblue.png"));
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns the image used for displaying remaining lives
	 *
	 * @return Image of character
	 */
	@Override
	public Image getLifeImage() {
		return ghost;
	}

	/**
	 * Handles key presses for game controls
	 *
	 * @param key Integer representing the key pressed
	 */
	@Override
	public void keyPressed(int key) {
		if (playerNum == GHOST1) {
			switch (key) {
				case KeyEvent.VK_A:
					reqdx = -1;
					reqdy = 0;
					break;
				case KeyEvent.VK_D:
					reqdx = 1;
					reqdy = 0;
					break;
				case KeyEvent.VK_W:
					reqdx = 0;
					reqdy = -1;
					break;
				case KeyEvent.VK_S:
					reqdx = 0;
					reqdy = 1;
					break;
				default:
					break;
			}
		} else if (playerNum == GHOST2) {
			switch (key) {
				case KeyEvent.VK_J:
					reqdx = -1;
					reqdy = 0;
					break;
				case KeyEvent.VK_L:
					reqdx = 1;
					reqdy = 0;
					break;
				case KeyEvent.VK_I:
					reqdx = 0;
					reqdy = -1;
					break;
				case KeyEvent.VK_K:
					reqdx = 0;
					reqdy = 1;
					break;
				default:
					break;
			}
		}
	}

	@Override
	public void keyReleased(int key) {
		move(this.grid);
		if (playerNum == GHOST1) {
			switch (key) {
				case KeyEvent.VK_A:
					reqdx = 0;
					break;
				case KeyEvent.VK_D:
					reqdx = 0;
					break;
				case KeyEvent.VK_W:
					reqdy = 0;
					break;
				case KeyEvent.VK_S:
					reqdy = 0;
					break;
				default:
					break;
			}
		} else if (playerNum == GHOST2) {
			switch (key) {
				case KeyEvent.VK_J:
					reqdx = 0;
					break;
				case KeyEvent.VK_L:
					reqdx = 0;
					break;
				case KeyEvent.VK_I:
					reqdy = 0;
					break;
				case KeyEvent.VK_K:
					reqdy = 0;
					break;
				default:
					break;
			}
		}
	}

	/**
	 * Moves character's current position with the board's collision
	 *
	 * see PacPlayer.java for code explanation
	 *
	 * @param grid The Grid to be used for collision
	 */
	@Override
	public void move(Grid grid) {
		short ch;
		if(edible) {
			edibleTimer--;	
			if(edibleTimer <= 0)
				death(x,y);	
		}
		if (reqdx == -dx && reqdy == -dy) {
			dx = reqdx;
			dy = reqdy;
		}
		if (x % Board.BLOCKSIZE == 0 && y % Board.BLOCKSIZE == 0) {
			//Tunnel effect
			x = ((x / Board.BLOCKSIZE + Board.NUMBLOCKS) % Board.NUMBLOCKS) * Board.BLOCKSIZE;
			y = ((y / Board.BLOCKSIZE + Board.NUMBLOCKS) % Board.NUMBLOCKS) * Board.BLOCKSIZE;

			ch = grid.screenData[y / Board.BLOCKSIZE][x / Board.BLOCKSIZE];

			if((ch & GridData.GRID_CELL_FRUIT) != 0) {
				grid.screenData[y / Board.BLOCKSIZE][x / Board.BLOCKSIZE] = (short) (ch ^ GridData.GRID_CELL_FRUIT);
				Board.score -= 5;
			}

			if (reqdx != 0 || reqdy != 0) {
				if ( !(reqdx == -1 && reqdy == 0 && (ch & GridData.GRID_CELL_BORDER_LEFT) != 0) && 
				     !(reqdx == 1 && reqdy == 0 && (ch & GridData.GRID_CELL_BORDER_RIGHT) != 0) &&
				     !(reqdx == 0 && reqdy == -1 && (ch & GridData.GRID_CELL_BORDER_TOP) != 0) && 
				     !(reqdx == 0 && reqdy == 1 && (ch & GridData.GRID_CELL_BORDER_BOTTOM) != 0) ) {
					dx = reqdx;
					dy = reqdy;
				}
			}

			// Check for standstill
			if ( (dx == -1 && dy == 0 && (ch & GridData.GRID_CELL_BORDER_LEFT) != 0) || 
			     (dx == 1 && dy == 0 && (ch & GridData.GRID_CELL_BORDER_RIGHT) != 0) ||
			     (dx == 0 && dy == -1 && (ch & GridData.GRID_CELL_BORDER_TOP) != 0) || 
			     (dx == 0 && dy == 1 && (ch & GridData.GRID_CELL_BORDER_BOTTOM) != 0) ) {
				dx = 0;
				dy = 0;
			}
		}
		move();
	}

	/**
	 * For ghosts that are close to pacman, have them follow pacman
	 * with a specified probability
	 *
	 * @param grid The Grid to be used for the collision
	 * @param c    Array of pacmen to chase
	 */
	@Override
	public void moveAI(Grid grid, Character[] c)
	{
		move();
		if(c.length == 0) //Nothing to chase.  Should never happen
			return;
		if(edible) {
			edibleTimer--;	
			if(edibleTimer <= 0)
				death(x,y);	
			moveRandom(grid);
		}
		else {
			int[][] coord = new int[c.length][2];
			int count = 0;
	
			for(Character p : c) {
				double distance = 0;
				if (type == TYPE_RED) {
					distance = Math.sqrt(Math.pow(this.x - p.x, 2.0) + Math.pow(this.y - p.y, 2.0));
				} else if (type == TYPE_PINK) {
					int aheadX = p.x;
					int aheadY = p.y;
					PacPlayer pacman = (PacPlayer)p;
					if (pacman.direction == Direction.LEFT)
						aheadX = p.x - 4;
					else if (pacman.direction == Direction.UP)
						aheadX = p.y - 4;
					else if (pacman.direction == Direction.RIGHT)
						aheadX = p.x + 4;
					else 
						aheadY= p.y + 4;

					if(aheadX > 16)
						aheadX = 16;
					else if(aheadX < 0)
						aheadX = 0;
					if(aheadY > 16)
						aheadY = 16;
					else if (aheadY < 0)
						aheadY = 0;
					distance = Math.sqrt(Math.pow(this.x - aheadX, 2.0) + Math.pow(this.y - aheadY, 2.0));
				}
				if(p.alive && distance < 150.0) { // && Math.random() < 0.6)
					coord[count][0] = p.x;
					coord[count][1] = p.y;
					count++;
				}
			}
	
			if(count > 0 && hasChoice(grid)) {

				Node bestDir = pathFind(grid, coord[0][0] / Board.BLOCKSIZE, coord[0][1] / Board.BLOCKSIZE);
				Node tempDir;

				//Loop through each pacman
				for (int i = 1; i < count; i++) {
					tempDir = pathFind(grid, coord[i][0] / Board.BLOCKSIZE, coord[i][1] / Board.BLOCKSIZE);
					if (tempDir.distance.value < bestDir.distance.value) //If new path is shorter
						bestDir = tempDir;
				}
	
				if (bestDir.x - this.x / Board.BLOCKSIZE == 0 && bestDir.y - this.y / Board.BLOCKSIZE == 0) //ghost on pacman
					moveRandom(grid);
				else {
					dx = bestDir.x - this.x / Board.BLOCKSIZE;
					dy = bestDir.y - this.y / Board.BLOCKSIZE;
				}

			} else {
				moveRandom(grid);
			}
		}
	}

	/**
	 * A* pathfinding algorithm
	 *
	 * @param grid the grid to be used for pathfinding
	 * @param x target x coordinate in grid form
	 * @param y target y coordinate in grid form
	 * @return The next move to make for the ghost
	 */
	public Node pathFind(Grid grid, int x, int y) {
		//Set target x, y
		Node.tx = x;
		Node.ty = y;

		Node current = null, temp;
		int block;

		PriorityQueue<Node> opened = new PriorityQueue<Node>();
		HashSet<Node> closed = new HashSet<Node>();

		temp = new Node(this.x / Board.BLOCKSIZE, this.y / Board.BLOCKSIZE, 0); //current location of ghost
		temp.init();
		temp.setDir(dx, dy);
		opened.offer(temp);

		while(!opened.isEmpty()) {

			current = opened.poll(); //get best node
			closed.add(current); //add node to closed set (visited)

			if(current.hCost == 0) //if future cost is 0, then it is target node
				break;

			block = grid.screenData[current.y][current.x];

			//If can move, not abrupt, and unvisited, add to opened
			if((block & GridData.GRID_CELL_BORDER_LEFT) == 0 && current.dir != Direction.RIGHT) {
				temp = current.getChild(-1, 0); //get child node
				if(!closed.contains(temp)) //Unvisited
					opened.add(temp);
			}
			if((block & GridData.GRID_CELL_BORDER_TOP) == 0 && current.dir != Direction.DOWN) {
				temp = current.getChild(0, -1);
				if(!closed.contains(temp))
					opened.add(temp);
			}
			if((block & GridData.GRID_CELL_BORDER_RIGHT) == 0 && current.dir != Direction.LEFT) {
				temp = current.getChild(1, 0);
				if(!closed.contains(temp))
					opened.add(temp);
			}
			if((block & GridData.GRID_CELL_BORDER_BOTTOM) == 0 && current.dir != Direction.UP) {
				temp = current.getChild(0, 1);
				if(!closed.contains(temp))
					opened.add(temp);
			}
		}

		//if current.parent == null, then ghost is on pacman.  Handle it by moving randomly
		//current.parent.parent == null, then current is best next move
		while(current.parent != null && current.parent.parent != null)
			current = current.parent;

		return current;
	}

	/**
	 * Moves character's current position with the board's collision
	 *
	 * @param grid The Grid to be used for collision
	 */
	public void moveRandom(Grid grid) {
		//Makes sure ghost is in a grid and not in movement
		if (this.x % Board.BLOCKSIZE == 0 && this.y % Board.BLOCKSIZE == 0) {
			ArrayList<Point> list = moveList(grid);

			//randomly pick an available move
			if (list.size() > 0) {
			    int rand = (int) (Math.random() * list.size());
			    this.dx = list.get(rand).x;
			    this.dy = list.get(rand).y;
			}
		}
	}

	/**
	 * Returns an ArrayList of possible movements
	 *
	 * @param grid
	 */
	private ArrayList<Point> moveList(Grid grid) {
		ArrayList<Point> moves = new ArrayList<Point>();
		int block = grid.screenData[y / Board.BLOCKSIZE][x / Board.BLOCKSIZE];

		// First condition prevents checks collision with wall
		// Second condition prevents switching direction abruptly (left -> right) (up -> down)
		if ((block & GridData.GRID_CELL_BORDER_LEFT) == 0 && this.dx != 1)
			moves.add(new Point(-1, 0));
		if ((block & GridData.GRID_CELL_BORDER_TOP) == 0 && this.dy != 1)
			moves.add(new Point(0, -1));
		if ((block & GridData.GRID_CELL_BORDER_RIGHT) == 0 && this.dx != -1)
			moves.add(new Point(1, 0));
		if ((block & GridData.GRID_CELL_BORDER_BOTTOM) == 0 && this.dy != -1)
			moves.add(new Point(0, 1));

		return moves;
	}

	private boolean hasChoice(Grid grid) {
		if (this.x % Board.BLOCKSIZE == 0 && this.y % Board.BLOCKSIZE == 0) {
			int block = grid.screenData[y / Board.BLOCKSIZE][x / Board.BLOCKSIZE];
			int count = (block & GridData.GRID_CELL_BORDER_LEFT) == 0 && this.dx != 1 ? 1 : 0;
			count += (block & GridData.GRID_CELL_BORDER_TOP) == 0 && this.dy != 1 ? 1 : 0;
			count += (block & GridData.GRID_CELL_BORDER_RIGHT) == 0 && this.dx != -1 ? 1 : 0;
			count += (block & GridData.GRID_CELL_BORDER_BOTTOM) == 0 && this.dy != -1 ? 1 : 0;
			if(count > 1)
				return true;
		}
		return false;
	}
}
