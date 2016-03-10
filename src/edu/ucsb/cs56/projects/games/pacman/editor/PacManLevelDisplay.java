package edu.ucsb.cs56.projects.games.pacman.editor;

import java.awt.*;
import javax.swing.*;
import edu.ucsb.cs56.projects.games.pacman.GridData;

public class PacManLevelDisplay extends JPanel {
	private short[][] gridData;
	private Color mazeColor, dotColor, fruitColor;
	private int block_size;

	public PacManLevelDisplay() {
		this.mazeColor = new Color(5, 100, 5);
        this.dotColor = new Color(192, 192, 0);
        this.fruitColor = new Color(255, 0, 0);
	}

	public PacManLevelDisplay(short[][] gridData) {
		this.gridData = gridData;
		this.mazeColor = new Color(5, 100, 5);
        this.dotColor = new Color(192, 192, 0);
        this.fruitColor = new Color(255, 0, 0);
	}

	public void updateGrid(short[][] gridData) {
		this.gridData = gridData;
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

                g2.setColor(this.fruitColor);
                if ((this.gridData[i][j] & GridData.GRID_CELL_FRUIT) != 0)
                    g2.fillRect(x + block_size/2 - fruit_size/2, y + block_size/2 - fruit_size/2, fruit_size, fruit_size);
            }
        }
	}
}