package clueGame;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class ClueGUI extends JPanel{
	
	private JTextField name;
    private JTextField roll;
    private JTextField guesstext;
    private JTextField response;
	
	
	public ClueGUI(){
		// Create a layout with 2 rows
		setLayout(new GridLayout(2,0));
		JPanel panel = createFirstRow();
		add(panel);
		panel = createSecondRow();
		add(panel);
		
	}
	
	 private JPanel createFirstRow() {
		JButton next = new JButton("Next Player");
		JButton accusation = new JButton("Make an accusation");
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1,3));
		JLabel nameLabel = new JLabel("Whose Turn?");
		
		JPanel whoseturn = new JPanel();
		//panel.setLayout(new GridLayout(2,1));
		
		name = new JTextField(20);
		name.setEditable(false);
		whoseturn.add(nameLabel);
		whoseturn.add(name);
		
		panel.add(whoseturn);
		panel.add(next);
		panel.add(accusation);
		return panel;
	}
	 
	private JPanel createSecondRow() {
		JPanel panel = new JPanel();
		//Die Roll
	 	JLabel nameLabel = new JLabel("Roll");
		
		JPanel dieJPanel = new JPanel();
		dieJPanel.setLayout(new GridLayout(1,2));
		roll = new JTextField(2);
		roll.setEditable(false);
		dieJPanel.add(nameLabel);
		dieJPanel.add(roll);
		dieJPanel.setBorder(new TitledBorder (new EtchedBorder(), "Die"));
		
		panel.add(dieJPanel);
		
		//Guess
		JPanel guessJPanel = new JPanel();
		guesstext = new JTextField(30);
		guesstext.setEditable(false);
		guessJPanel.add(guesstext);
		guessJPanel.setBorder(new TitledBorder (new EtchedBorder(), "Guess"));
		panel.add(guessJPanel);

		//Guess Result
		JLabel guessresLabel = new JLabel("Response");
		
		JPanel guessresJPanel = new JPanel();
		guessresJPanel.setLayout(new GridLayout(1,2));
		response = new JTextField(15);
		response.setEditable(false);
		guessresJPanel.add(guessresLabel);
		guessresJPanel.add(response);
		guessresJPanel.setBorder(new TitledBorder (new EtchedBorder(), "Guess Result"));
		
		panel.add(guessresJPanel);
		return panel;
	}
	
	public static void main(String[] args) {
		// Create a JFrame with all the normal functionality
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Clue Game");
		frame.setSize(1000, 250);
		// Create the JPanel and add it to the JFrame
		ClueGUI gui = new ClueGUI();
		frame.add(gui, BorderLayout.CENTER);
		// Now let's view it
		frame.setVisible(true);
	}
	
}
