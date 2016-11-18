package clueGame;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class SuggestionPopup extends JDialog{
	Board board;
	JComboBox<String> names;
	JComboBox<String> rooms;
	JComboBox<String> weapons;
	String nameon = "";
	String roomon = "";
	String weaponon = "";
	
	
	public SuggestionPopup(String roomIn){
		board = Board.getInstance();
		setTitle("Make a suggestion");
		setSize(300,400);
		setLayout(new GridLayout(4,1));
		add(makePersonGuessPanel());
		add(makeRoomGuessPanel(roomIn));
		add(makeWeaponGuessPanel());
		JButton done = new JButton("Done");
		done.addActionListener(new DoneButtonListener());
		add(done);
	}
	
	
	public JPanel makePersonGuessPanel(){
		JPanel panel = new JPanel();
		names = new JComboBox<String>();
		for(Player p: board.players){
			names.addItem(p.getName());
		}
		panel.add(names);
		panel.setBorder(new TitledBorder(new EtchedBorder(),"Person Guess"));
		return panel;
	}
	
	public JPanel makeRoomGuessPanel(String roomIn){
		JPanel panel = new JPanel();
		rooms = new JComboBox<String>();
		rooms.addItem(roomIn);
		panel.add(rooms);
		panel.setBorder(new TitledBorder(new EtchedBorder(),"Room Guess"));
		return panel;
	}
	
	public JPanel makeWeaponGuessPanel(){
		JPanel panel = new JPanel();
		weapons = new JComboBox<String>();
		for(Card c: board.weaponCards){
			weapons.addItem(c.getName());
		}
		panel.add(weapons);
		panel.setBorder(new TitledBorder(new EtchedBorder(),"Weapon Guess"));
		return panel;
	}
	
	
	private class DoneButtonListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			nameon = names.getSelectedItem().toString();
			roomon = rooms.getSelectedItem().toString();
			weaponon = weapons.getSelectedItem().toString();
			Solution solution = new Solution(nameon,roomon,weaponon);
			board.mostRecentSolution = solution;
			ClueGUI.guesstext.setText(solution.toString());
			board.human.turnState = 0;
			setVisible(false);
			board.teleportPlayer();
		}
	}
	
	private class ComboListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == names) {
				
			}
		}
	}
}
