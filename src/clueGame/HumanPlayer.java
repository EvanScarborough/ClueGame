package clueGame;

import java.util.Set;

public class HumanPlayer extends Player{

	public boolean isTurnDone = true;
	
	public HumanPlayer(String name) {
		super(name);
	}
	
	
	public Solution takeTurn(BoardCell cell){
		row = cell.getRow();
		column = cell.getColumn();
		return null;
	}
}
