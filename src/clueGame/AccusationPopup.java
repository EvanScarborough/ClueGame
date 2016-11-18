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

public class AccusationPopup extends JDialog{
	Board board;
	JComboBox<String> names;
	JComboBox<String> rooms;
	JComboBox<String> weapons;
	String nameon = "";
	String roomon = "";
	String weaponon = "";
	
	
	public AccusationPopup(){
		board = Board.getInstance();
		setTitle("Make a suggestion");
		setSize(300,430);
		setLayout(new GridLayout(5,1));
		add(makePersonGuessPanel());
		add(makeRoomGuessPanel());
		add(makeWeaponGuessPanel());
		JButton done = new JButton("Accuse");
		JButton cancel = new JButton("Cancel");
		done.addActionListener(new SubmitButtonListener());
		cancel.addActionListener(new CancelButtonListener());
		add(done);
		add(cancel);
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
	
	public JPanel makeRoomGuessPanel(){
		JPanel panel = new JPanel();
		rooms = new JComboBox<String>();
		for(Card c: board.roomCards){
			rooms.addItem(c.getName());
		}
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
	
	
	private class SubmitButtonListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(board.human.turnState != 1){
				JOptionPane.showMessageDialog(new JFrame(), "Wait until the beginning of your turn to accuse.", "Can't Accuse Now", JOptionPane.INFORMATION_MESSAGE);
			}
			nameon = names.getSelectedItem().toString();
			roomon = rooms.getSelectedItem().toString();
			weaponon = weapons.getSelectedItem().toString();
			Solution solution = new Solution(nameon,roomon,weaponon);
			board.handleAccusation(solution);
			board.human.turnState = 0;
			setVisible(false);
		}
	}
	private class CancelButtonListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			setVisible(false);
		}
	}
	
	private class ComboListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == names) {
				
			}
		}
	}
}
