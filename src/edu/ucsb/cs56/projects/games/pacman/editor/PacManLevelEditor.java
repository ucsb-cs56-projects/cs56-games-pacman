package edu.ucsb.cs56.projects.games.pacman.editor;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import edu.ucsb.cs56.projects.games.pacman.GridData;

public class PacManLevelEditor extends JFrame {
	private JMenuBar menu_bar;
	private JMenu menu_file;
	private JMenuItem menu_file_new;
	private JMenuItem menu_file_load;
	private JMenuItem menu_file_save;
	private JMenuItem menu_file_save_as;
	private PacManLevelDisplay panel_grid_display;
	private JPanel panel_editor;
	private JLabel label_grid_properties;
	private JPanel panel_grid_properties;
	private JLabel label_grid_size;
	private JTextField grid_size_width;
	private JTextField grid_size_height;
	private JLabel label_cell_properties;
	private JPanel panel_cell_properties;
	private JToggleButton toggle_cell_border_top;
	private JToggleButton toggle_cell_border_left;
	private JToggleButton toggle_cell_pellet;
	private JToggleButton toggle_cell_border_right;
	private JToggleButton toggle_cell_border_bottom;
	private JPanel panel_special;
	private JLabel label_special;
	private JToggleButton toggle_cell_pacman_spawn;
	private short[][] gridData;

	public PacManLevelEditor() {
		initComponents();
	}

	public static void main(String[] args) {
		System.setProperty("apple.laf.useScreenMenuBar", "true");

		PacManLevelEditor editor = new PacManLevelEditor();
		editor.setVisible(true);
	}

	public void loadLevel() {
		JFileChooser fileChooser = new JFileChooser();
		int result = fileChooser.showOpenDialog(this);
		if(result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			System.out.println(selectedFile.getAbsolutePath());
			try {	
				FileInputStream input_file_stream = new FileInputStream(selectedFile.getAbsolutePath());
	      		ObjectInputStream object_input_stream = new ObjectInputStream(input_file_stream);
	      		GridData data = (GridData)object_input_stream.readObject();
	      		this.gridData = data.get2DGridData();

	      		this.panel_grid_display.updateGrid(this.gridData);
	      		this.panel_grid_display.repaint();

				for(int i = 0; i < this.gridData.length; i++) {
					for(int j = 0; j < this.gridData[i].length; j++) {
						System.out.print(" " + this.gridData[i][j]);
					}
					System.out.print("\n");
				}
	      	} catch (Exception e) {
	      		e.printStackTrace();
	      		System.out.println(e);
	      		System.out.println("Unable to deserialize the level data.");
	      	}
		}
	}

	private void initComponents() {
		menu_bar = new JMenuBar();
		menu_file = new JMenu();
		menu_file_new = new JMenuItem();
		menu_file_load = new JMenuItem();
		menu_file_save = new JMenuItem();
		menu_file_save_as = new JMenuItem();
		panel_grid_display = new PacManLevelDisplay();
		panel_editor = new JPanel();
		label_grid_properties = new JLabel();
		panel_grid_properties = new JPanel();
		label_grid_size = new JLabel();
		grid_size_width = new JTextField();
		grid_size_height = new JTextField();
		JPanel panel_vspacer_top = new JPanel(null);
		label_cell_properties = new JLabel();
		panel_cell_properties = new JPanel();
		toggle_cell_border_top = new JToggleButton();
		toggle_cell_border_left = new JToggleButton();
		toggle_cell_pellet = new JToggleButton();
		toggle_cell_border_right = new JToggleButton();
		toggle_cell_border_bottom = new JToggleButton();
		JPanel panel_vspacer_bottom = new JPanel(null);
		panel_special = new JPanel();
		label_special = new JLabel();
		toggle_cell_pacman_spawn = new JToggleButton();

		setTitle("PacMan Level Editor");
		Container contentPane = getContentPane();
		contentPane.setLayout(new GridBagLayout());
		((GridBagLayout)contentPane.getLayout()).columnWidths = new int[] {0, 0, 0};
		((GridBagLayout)contentPane.getLayout()).rowHeights = new int[] {0, 0};
		((GridBagLayout)contentPane.getLayout()).columnWeights = new double[] {1.0, 0.0, 1.0E-4};
		((GridBagLayout)contentPane.getLayout()).rowWeights = new double[] {1.0, 1.0E-4};

		// File Menu

		MenuActionListener menu_listener = new MenuActionListener();

		menu_file.setText("File");

		menu_file_new.setText("New Level");
		menu_file_new.addActionListener(menu_listener);
		menu_file.add(menu_file_new);

		menu_file_load.setText("Load Level");
		menu_file_load.addActionListener(menu_listener);
		menu_file.add(menu_file_load);

		menu_file.addSeparator();

		menu_file_save.setText("Save");
		menu_file_save.addActionListener(menu_listener);
		menu_file.add(menu_file_save);

		menu_file_save_as.setText("Save As...");
		menu_file_save_as.addActionListener(menu_listener);
		menu_file.add(menu_file_save_as);

		
		menu_bar.add(menu_file);
		setJMenuBar(menu_bar);

		panel_grid_display.setBackground(new Color(0, 204, 102));
		panel_grid_display.setLayout(new FlowLayout());
		contentPane.add(panel_grid_display, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 0, 10), 0, 0));

		panel_editor.setLayout(new GridBagLayout());
		((GridBagLayout)panel_editor.getLayout()).columnWidths = new int[] {0, 0};
		((GridBagLayout)panel_editor.getLayout()).rowHeights = new int[] {0, 5, 0, 5, 10, 5, 0, 5, 0, 5, 10, 5, 0, 0};
		((GridBagLayout)panel_editor.getLayout()).columnWeights = new double[] {0.0, 1.0E-4};
		((GridBagLayout)panel_editor.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};
		// Grid Properties
		label_grid_properties.setText("Grid Properties");
		label_grid_properties.setFont(label_grid_properties.getFont().deriveFont(label_grid_properties.getFont().getStyle() | Font.BOLD));
		panel_editor.add(label_grid_properties, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
			new Insets(0, 0, 0, 0), 0, 0));

		panel_grid_properties.setLayout(new GridBagLayout());
		((GridBagLayout)panel_grid_properties.getLayout()).columnWidths = new int[] {0, 2, 0, 2, 0, 2, 0, 0};
		((GridBagLayout)panel_grid_properties.getLayout()).rowHeights = new int[] {0, 0};
		((GridBagLayout)panel_grid_properties.getLayout()).columnWeights = new double[] {0.0, 0.0, 1.0, 0.0, 1.0, 0.0, 0.0, 1.0E-4};
		((GridBagLayout)panel_grid_properties.getLayout()).rowWeights = new double[] {0.0, 1.0E-4};

		label_grid_size.setText("Grid Size");
		label_grid_size.setLabelFor(grid_size_width);
		panel_grid_properties.add(label_grid_size, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
			new Insets(0, 0, 0, 0), 0, 0));

		grid_size_width.setEditable(false);
		grid_size_width.setEnabled(false);
		/*
		 * TODO: In future revisions, if the game is changed to support
		 * additional board sizes, it would make sense to enable this
		 * option and allow editors to specify the size of the board.
		 */
		grid_size_width.setText("17");
		grid_size_width.setToolTipText("Currently, grid resizing is not supported by the game.");
		panel_grid_properties.add(grid_size_width, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
			new Insets(0, 0, 0, 0), 0, 0));

		grid_size_height.setEditable(false);
		grid_size_height.setEnabled(false);
		grid_size_height.setText("17");
		// It is currently presumed within the code that games will always be
		// square in size. The logic of other components in this editor
		// will need to be revised should there be non-square boards.
		grid_size_height.setToolTipText("Grids must be square.");
		panel_grid_properties.add(grid_size_height, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
			new Insets(0, 0, 0, 0), 0, 0));

		panel_editor.add(panel_grid_properties, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
			new Insets(0, 0, 0, 0), 0, 0));

		// Spacer
		panel_editor.add(panel_vspacer_top, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
			new Insets(0, 0, 0, 0), 0, 0));

		// Cell Properties
		label_cell_properties.setText("Cell Properties");
		label_cell_properties.setFont(label_cell_properties.getFont().deriveFont(label_cell_properties.getFont().getStyle() | Font.BOLD));
		panel_editor.add(label_cell_properties, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
			new Insets(0, 0, 0, 0), 0, 0));

		panel_cell_properties.setLayout(new GridBagLayout());
		((GridBagLayout)panel_cell_properties.getLayout()).columnWidths = new int[] {0, 2, 0, 2, 0, 0};
		((GridBagLayout)panel_cell_properties.getLayout()).rowHeights = new int[] {0, 5, 0, 5, 0, 0};
		((GridBagLayout)panel_cell_properties.getLayout()).columnWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};
		((GridBagLayout)panel_cell_properties.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};

		// Cell Top Border Toggle
		toggle_cell_border_top.setText("Top");
		toggle_cell_border_top.setEnabled(false);
		panel_cell_properties.add(toggle_cell_border_top, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
			new Insets(0, 0, 0, 0), 0, 0));

		// Cell Left Border Toggle
		toggle_cell_border_left.setText("Left");
		toggle_cell_border_left.setEnabled(false);
		panel_cell_properties.add(toggle_cell_border_left, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
			new Insets(0, 0, 0, 0), 0, 0));

		// Cell Pellet Border Toggle
		toggle_cell_pellet.setText("Pellet");
		toggle_cell_pellet.setEnabled(false);
		panel_cell_properties.add(toggle_cell_pellet, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
			new Insets(0, 0, 0, 0), 0, 0));

		// Cell Right Border Toggle
		toggle_cell_border_right.setText("Right");
		toggle_cell_border_right.setEnabled(false);
		panel_cell_properties.add(toggle_cell_border_right, new GridBagConstraints(4, 2, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
			new Insets(0, 0, 0, 0), 0, 0));

		// Cell Buttom Border Toggle
		toggle_cell_border_bottom.setText("Bottom");
		toggle_cell_border_bottom.setEnabled(false);
		panel_cell_properties.add(toggle_cell_border_bottom, new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
			new Insets(0, 0, 0, 0), 0, 0));

		panel_editor.add(panel_cell_properties, new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
			new Insets(0, 0, 0, 0), 0, 0));

		// Spacer
		/*panel_editor.add(panel_vspacer_bottom, new GridBagConstraints(0, 10, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
			new Insets(0, 0, 0, 0), 0, 0));*/

/*		// Special Properties
		panel_special.setLayout(new GridBagLayout());
		((GridBagLayout)panel_special.getLayout()).columnWidths = new int[] {0, 2, 0, 0};
		((GridBagLayout)panel_special.getLayout()).rowHeights = new int[] {0, 0};
		((GridBagLayout)panel_special.getLayout()).columnWeights = new double[] {1.0, 0.0, 0.0, 1.0E-4};
		((GridBagLayout)panel_special.getLayout()).rowWeights = new double[] {0.0, 1.0E-4};

		label_special.setText("Special");
		label_special.setFont(label_special.getFont().deriveFont(label_special.getFont().getStyle() | Font.BOLD));
		panel_special.add(label_special, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
			new Insets(0, 0, 0, 0), 0, 0));

		// Toggle Button for PacMan Spawn Point.
		toggle_cell_pacman_spawn.setText("PacMan Spawn Point");
		toggle_cell_pacman_spawn.setToolTipText("Sets the current cell as the spawn point for PacMan.");
		panel_special.add(toggle_cell_pacman_spawn, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
			new Insets(0, 0, 0, 0), 0, 0));

		panel_editor.add(panel_special, new GridBagConstraints(0, 12, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
			new Insets(0, 0, 0, 0), 0, 0));*/

		contentPane.add(panel_editor, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
			new Insets(0, 0, 0, 0), 0, 0));

		pack();
		setLocationRelativeTo(getOwner());
	}

	class MenuActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.out.println("Received action performed.");
			if(e.getSource() == PacManLevelEditor.this.menu_file_load) {
				PacManLevelEditor.this.loadLevel();
			}
		}
	}
}
