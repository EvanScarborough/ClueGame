package clueGame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class ClueGame extends JFrame{
	Board board;
	int width;
	int height;

	public ClueGame(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Clue Game");
		//setSize(800, 250);
		
		board = Board.getInstance();
		board.setConfigFiles("data/BBMB_ClueLayout.csv", "data/BBMB_ClueLegend.txt");
		board.initialize();
		board.loadConfigFiles("data/CluePlayers.txt", "data/ClueWeapons.txt");
		board.setBackground(Color.BLACK);
		add(board, BorderLayout.CENTER);
		
		width = BoardCell.TILE_SIZE * board.getNumColumns();
		height = BoardCell.TILE_SIZE * board.getNumRows();
		
		setPreferredSize(new Dimension(width,height + BoardCell.TILE_SIZE * 8));
		
		ClueGUI gui = new ClueGUI();
		add(gui, BorderLayout.SOUTH);
		
		pack();
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		menuBar.add(createFileMenu());
	}
	
	public JMenu createFileMenu(){
		JMenu menu = new JMenu("File");
		menu.add(createFileShowNotesItem());
		menu.add(createFileExitItem());
		return menu;
	}
	
	public JMenuItem createFileExitItem(){
		JMenuItem item = new JMenuItem("Exit");
		
		class MenuItemListener implements ActionListener{
			@Override
			public void actionPerformed(ActionEvent e){
				System.exit(0);
			} 
		}
		item.addActionListener(new MenuItemListener());
		return item;
	}
	
	public JMenuItem createFileShowNotesItem(){
		JMenuItem item = new JMenuItem("Show Notes");
		
		class MenuItemListener implements ActionListener{
			@Override
			public void actionPerformed(ActionEvent e){
				DetectiveNotes notes = new DetectiveNotes();
				notes.setVisible(true);
			} 
		}
		item.addActionListener(new MenuItemListener());
		return item;
	}
	
	
	public static void main(String[] args) {
		// Create a JFrame with all the normal functionality
		//JFrame frame = new JFrame();
		// Create the JPanel and add it to the JFrame
		//ClueGUI gui = new ClueGUI();
		//frame.add(gui, BorderLayout.CENTER);
		// Now let's view it
		//frame.setVisible(true);
		
		ClueGame clueGame = new ClueGame();
		
		//clueGame.pack();
		clueGame.setVisible(true);
		
	}
}
