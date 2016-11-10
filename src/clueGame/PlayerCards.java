package clueGame;

import java.awt.GridLayout;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class PlayerCards extends JPanel{
	public PlayerCards(Player player){
		setLayout(new GridLayout(3,1));
		add(makePanel(player, CardType.PERSON));
		add(makePanel(player, CardType.ROOM));
		add(makePanel(player, CardType.WEAPON));

		setBorder(new TitledBorder (new EtchedBorder(), "My Cards"));

	}
	
	private JPanel makePanel(Player player, CardType type) {
		JTextArea textField = new JTextArea(20, 20);
		textField.setEditable(false);
		JPanel pan = new JPanel();
		
		String string = "";
		for(Card c: player.getMyCards()){
			if(c.getType() == type) string += c.getName() + "\n";
		}
		
		textField.setText(string);
		pan.add(textField);
		
		pan.setBorder(new TitledBorder (new EtchedBorder(), type.toString()));
		return pan;
	}
}
