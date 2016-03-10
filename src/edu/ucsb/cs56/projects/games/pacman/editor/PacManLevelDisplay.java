package edu.ucsb.cs56.projects.games.pacman.editor;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import edu.ucsb.cs56.projects.games.pacman.GridData;

public class PacManLevelDisplay extends JPanel {
	private short[][] gridData;
	private Color mazeColor, dotColor, selectColor;
	private int block_size;
	private Point current_selection;
	private PacManLevelEditor parent;

	public PacManLevelDisplay(PacManLevelEditor parent) {
		this.parent = parent;
		this.mazeColor = new Color(5, 100, 5);
		this.dotColor = new Color(192, 192, 0);
		this.selectColor = new Color(250, 44, 125);
		this.addMouseListener(new MouseListener());
	}

	public PacManLevelDisplay(PacManLevelEditor parent, short[][] gridData) {
		this.parent = parent;
		this.gridData = gridData;
		this.mazeColor = new Color(5, 100, 5);
		this.dotColor = new Color(192, 192, 0);
		this.selectColor = new Color(250, 44, 125);
		this.addMouseListener(new MouseListener());
	}

	public void updateGrid(short[][] gridData) {
		this.gridData = gridData;
		this.current_selection = null;
		this.parent.setGridSelection(new Point(-1, -1));
	}

	@Override
	public void paintComponent(Graphics g) {
		if(this.gridData == null || this.gridData.length == 0) {
			return;
		}

		Graphics2D g2 = (Graphics2D)g;

		int screen_width = this.getWidth();
		int screen_height = this.getHeight();

		int minimum_dimension = Math.min(screen_width, screen_height);
		this.block_size = minimum_dimension/gridData.length;

		int x, y;
		g2.setStroke(new BasicStroke(2));

		int pellet_size = (int)(this.block_size * 0.2);
		int fruit_size = (int)(this.block_size * 0.3);

		for (int i = 0; i < gridData.length; i++) {
			for (int j = 0; j < gridData[i].length; j++) {
				y = i * block_size + 3;
				x = j * block_size + 3;


				g2.setColor(this.selectColor);

				if(this.current_selection != null) {
					if(i == this.current_selection.y && j == this.current_selection.x) {
						g2.fillRect(x, y, block_size, block_size);
					}
				}

				g2.setColor(this.mazeColor);

				if ((this.gridData[i][j] & GridData.GRID_CELL_BORDER_LEFT) != 0)
					g2.drawLine(x, y, x, y + block_size - 1);
				if ((this.gridData[i][j] & GridData.GRID_CELL_BORDER_TOP) != 0)
					g2.drawLine(x, y, x + block_size - 1, y);
				if ((this.gridData[i][j] & GridData.GRID_CELL_BORDER_RIGHT) != 0)
					g2.drawLine(x + block_size - 1, y, x + block_size - 1, y + block_size - 1);
				if ((this.gridData[i][j] & GridData.GRID_CELL_BORDER_BOTTOM) != 0)
					g2.drawLine(x, y + block_size - 1, x + block_size - 1, y + block_size - 1);

				g2.setColor(this.dotColor);

				if ((this.gridData[i][j] & GridData.GRID_CELL_PELLET) != 0)
					g2.fillRect(x + block_size/2 - pellet_size/2, y + block_size/2 - pellet_size/2, pellet_size, pellet_size);

			}
		}
	}

	private class MouseListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			if(PacManLevelDisplay.this.gridData == null || PacManLevelDisplay.this.gridData.length == 0) {
				return;
			}

			Point original_point = e.getPoint();
			Point new_grid_location = new Point((original_point.x-3)/PacManLevelDisplay.this.block_size, (original_point.y-3)/PacManLevelDisplay.this.block_size);

			if(new_grid_location.y >= PacManLevelDisplay.this.gridData.length || new_grid_location.x >= PacManLevelDisplay.this.gridData[new_grid_location.y].length) {
				return;
			}

			PacManLevelDisplay.this.current_selection = new_grid_location;
			PacManLevelDisplay.this.parent.setGridSelection(new_grid_location);
			PacManLevelDisplay.this.repaint();
			parent.repaint();
		}
	}
}
