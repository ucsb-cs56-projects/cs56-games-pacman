package edu.ucsb.cs56.projects.games.pacman.editor;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import edu.ucsb.cs56.projects.games.pacman.GridData;

/**
 * Renders the level grid data for the custom Pac-Man level editor.
 *
 * @author Ryan Tse
 * @author Chris Beser
 * @author Joseph Kompella
 * @author Kekoa Sato
 * @version CS56 F16
 */
public class PacManLevelDisplay extends JPanel {
	private short[][] grid_data;
	private Color color_maze, color_dot, color_select;
	private int block_size;
	private Point current_selection;
	private PacManLevelEditor parent;

	public PacManLevelDisplay(PacManLevelEditor parent) {
		this.parent = parent;
		this.color_maze = new Color(5, 100, 5);
		this.color_dot = new Color(192, 192, 0);
		this.color_select = new Color(250, 44, 125);
		this.addMouseListener(new MouseListener());
	}

	public PacManLevelDisplay(PacManLevelEditor parent, short[][] grid_data) {
		this.parent = parent;
		this.grid_data = grid_data;
		this.color_maze = new Color(5, 100, 5);
		this.color_dot = new Color(192, 192, 0);
		this.color_select = new Color(250, 44, 125);
		this.addMouseListener(new MouseListener());
	}

	public void updateGrid(short[][] grid_data) {
		this.grid_data = grid_data;
		this.current_selection = null;
		this.parent.setGridSelection(new Point(-1, -1));
	}

	@Override
	public void paintComponent(Graphics g) {
		if(this.grid_data == null || this.grid_data.length == 0) {
			return;
		}

		Graphics2D g2 = (Graphics2D)g;

		int screen_width = this.getWidth();
		int screen_height = this.getHeight();

		int minimum_dimension = Math.min(screen_width, screen_height);
		this.block_size = minimum_dimension/this.grid_data.length;

		int x, y;
		g2.setStroke(new BasicStroke(2));

		int pellet_size = (int)(this.block_size * 0.2);
		int fruit_size = (int)(this.block_size * 0.3);
		int pill_size = (int)(this.block_size * 0.5);
		
		for (int i = 0; i < this.grid_data.length; i++) {
			for (int j = 0; j < this.grid_data[i].length; j++) {
				y = i * block_size + 3;
				x = j * block_size + 3;


				g2.setColor(this.color_select);

				if(this.current_selection != null) {
					if(i == this.current_selection.y && j == this.current_selection.x) {
						g2.fillRect(x, y, block_size, block_size);
					}
				}

				g2.setColor(this.color_maze);

				if ((this.grid_data[i][j] & GridData.GRID_CELL_BORDER_LEFT) != 0)
					g2.drawLine(x, y, x, y + block_size - 1);
				if ((this.grid_data[i][j] & GridData.GRID_CELL_BORDER_TOP) != 0)
					g2.drawLine(x, y, x + block_size - 1, y);
				if ((this.grid_data[i][j] & GridData.GRID_CELL_BORDER_RIGHT) != 0)
					g2.drawLine(x + block_size - 1, y, x + block_size - 1, y + block_size - 1);
				if ((this.grid_data[i][j] & GridData.GRID_CELL_BORDER_BOTTOM) != 0)
					g2.drawLine(x, y + block_size - 1, x + block_size - 1, y + block_size - 1);

				g2.setColor(this.color_dot);

				if ((this.grid_data[i][j] & GridData.GRID_CELL_PELLET) != 0)
					g2.fillRect(x + block_size/2 - pellet_size/2, y + block_size/2 - pellet_size/2, pellet_size, pellet_size);
				
				if ((this.grid_data[i][j] & GridData.GRID_CELL_POWER_PILL) != 0)
					g2.fillOval(x + block_size/2 - pill_size/2, y + block_size/2 - pill_size/2, pill_size, pill_size);
			}
		}
	}

	private class MouseListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			if(PacManLevelDisplay.this.grid_data == null || PacManLevelDisplay.this.grid_data.length == 0) {
				return;
			}

			// This converts the coordinates from a screen size x and y coordinate to a grid data x and y value.
			Point original_point = e.getPoint();
			Point new_grid_location = new Point((original_point.x-3)/PacManLevelDisplay.this.block_size, (original_point.y-3)/PacManLevelDisplay.this.block_size);

			if(new_grid_location.y >= PacManLevelDisplay.this.grid_data.length || new_grid_location.x >= PacManLevelDisplay.this.grid_data[new_grid_location.y].length) {
				// Ignore clicks outside of the PacMan level grid.
				return;
			}

			PacManLevelDisplay.this.current_selection = new_grid_location;
			PacManLevelDisplay.this.parent.setGridSelection(new_grid_location);
			PacManLevelDisplay.this.repaint();
			PacManLevelDisplay.this.parent.repaint();
		}
	}
}
