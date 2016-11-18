package clueGame;

import java.util.Set;

public class HumanPlayer extends Player{

	public int turnState = 0; //0=not your turn, 1=choose tile, 2=make suggestion
	
	public HumanPlayer(String name) {
		super(name);
	}
	
	
	public boolean takeTurn(BoardCell cell){
		row = cell.getRow();
		column = cell.getColumn();
		if(cell.isDoorway()){
			SuggestionPopup popup = new SuggestionPopup(Board.rooms.get(cell.getInitial()));
			popup.setVisible(true);
		}
		if(cell.isDoorway()) return true;
		return false;
	}
}
