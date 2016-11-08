package clueGame;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class DetectiveNotes extends JDialog{
	Board board;
	
	public DetectiveNotes(){
		board = Board.getInstance();
		
		setTitle("Detective Notes");
		setSize(800,400);
		setLayout(new GridLayout(3,2));
		add(makePeopleBox());
		add(makePersonGuessPanel());
		add(makeRoomBox());
		add(makeRoomGuessPanel());
		add(makeWeaponBox());
		add(makeWeaponGuessPanel());
	}
	
	public JPanel makePeopleBox(){
		JPanel peoplePanel = new JPanel();
		peoplePanel.setLayout(new GridLayout(3,2));
		
		ArrayList<JCheckBox> peopleCheckBoxes = new ArrayList<JCheckBox>();
		
		for(Player p: board.players){
			peopleCheckBoxes.add(new JCheckBox(p.getName()));
			peoplePanel.add(peopleCheckBoxes.get(peopleCheckBoxes.size()-1));
		}
		peoplePanel.setBorder(new TitledBorder(new EtchedBorder(),"People"));
		return peoplePanel;
	}
	
	public JPanel makeWeaponBox(){
		JPanel peoplePanel = new JPanel();
		peoplePanel.setLayout(new GridLayout((int) Math.ceil(board.weaponCards.size() / 2),2));
		
		ArrayList<JCheckBox> weaponCheckBoxes = new ArrayList<JCheckBox>();
		
		for(Card c: board.weaponCards){
			weaponCheckBoxes.add(new JCheckBox(c.getName()));
			peoplePanel.add(weaponCheckBoxes.get(weaponCheckBoxes.size()-1));
		}
		peoplePanel.setBorder(new TitledBorder(new EtchedBorder(),"Weapons"));
		return peoplePanel;
	}
	
	public JPanel makeRoomBox(){
		JPanel peoplePanel = new JPanel();
		peoplePanel.setLayout(new GridLayout((int) Math.ceil(board.roomCards.size() / 2),2));
		
		ArrayList<JCheckBox> roomCheckBoxes = new ArrayList<JCheckBox>();
		
		for(Card c: board.roomCards){
			roomCheckBoxes.add(new JCheckBox(c.getName()));
			peoplePanel.add(roomCheckBoxes.get(roomCheckBoxes.size()-1));
		}
		peoplePanel.setBorder(new TitledBorder(new EtchedBorder(),"Rooms"));
		return peoplePanel;
	}
	
	
	public JPanel makePersonGuessPanel(){
		JPanel panel = new JPanel();
		JComboBox<String> box = new JComboBox<String>();
		for(Player p: board.players){
			box.addItem(p.getName());
		}
		panel.add(box);
		panel.setBorder(new TitledBorder(new EtchedBorder(),"Person Guess"));
		return panel;
	}
	
	public JPanel makeRoomGuessPanel(){
		JPanel panel = new JPanel();
		JComboBox<String> box = new JComboBox<String>();
		for(Card c: board.roomCards){
			box.addItem(c.getName());
		}
		panel.add(box);
		panel.setBorder(new TitledBorder(new EtchedBorder(),"Room Guess"));
		return panel;
	}
	
	public JPanel makeWeaponGuessPanel(){
		JPanel panel = new JPanel();
		JComboBox<String> box = new JComboBox<String>();
		for(Card c: board.weaponCards){
			box.addItem(c.getName());
		}
		panel.add(box);
		panel.setBorder(new TitledBorder(new EtchedBorder(),"Weapon Guess"));
		return panel;
	}
	
}
