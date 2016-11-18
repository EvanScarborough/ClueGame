package clueGame;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class ClueGUI extends JPanel{
	
	private JTextField name;
    private JTextField roll;
    static JTextField guesstext;
    static JTextField response;
	Board board = Board.getInstance();
	
	public ClueGUI(){
		// Create a layout with 2 rows
		setLayout(new GridLayout(2,1));
		JPanel panel = createFirstRow();
		add(panel);
		panel = createSecondRow();
		add(panel);
		
	}
	
	 private JPanel createFirstRow() {
		JButton next = new JButton("Next Player");
		next.addActionListener(new ButtonListener());
		JButton accusation = new JButton("Make an accusation");
		accusation.addActionListener(new AccusationListener());
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1,3));
		JLabel nameLabel = new JLabel("Whose Turn?");
		
		JPanel whoseturn = new JPanel();
		//panel.setLayout(new GridLayout(2,1));
		
		name = new JTextField(20);
		name.setEditable(false);
		name.setText(board.players.get(board.activePlayer).getName());
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
		roll.setText(Integer.toString(board.roll));
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
		//guessresJPanel.setLayout(new GridLayout(1,2));
		response = new JTextField(15);
		response.setEditable(false);
		guessresJPanel.add(guessresLabel);
		guessresJPanel.add(response);
		guessresJPanel.setBorder(new TitledBorder (new EtchedBorder(), "Guess Result"));
		
		panel.add(guessresJPanel);
		return panel;
	}
	
	
	class ButtonListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(Board.gameFinished){
				JOptionPane.showMessageDialog(new JFrame(), "The game is finished!!\nClose the game and re-open to play again.", "GAME OVER", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			if((board.activePlayer == 0 && board.human.turnState == 0) || board.activePlayer != 0) {
				board.nextPlayer();
				roll.setText(Integer.toString(board.roll));
				name.setText(board.players.get(board.activePlayer).getName());
				if(board.mostRecentSolution != null){
					guesstext.setText(board.mostRecentSolution.toString());
					response.setText(board.mostRecentDisprove);
				}
			}
			else{
				if(board.human.turnState == 1) JOptionPane.showMessageDialog(new JFrame(), "Finish your turn!!", "Select a target", JOptionPane.INFORMATION_MESSAGE);
				else JOptionPane.showMessageDialog(new JFrame(), "Finish your turn!!", "Make a suggestion", JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}
	
	class AccusationListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(board.human.turnState != 1){
				JOptionPane.showMessageDialog(new JFrame(), "Wait until the beginning of your turn to accuse.", "Can't Accuse Now", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			AccusationPopup accuse = new AccusationPopup();
			accuse.setVisible(true);
		}
	}
	
	
}
