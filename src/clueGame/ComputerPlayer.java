package clueGame;

import java.util.Set;

public class ComputerPlayer extends Player{
	public char lastVisited = '-';
	
	public ComputerPlayer(String name) {
		super(name);
	}

	public BoardCell pickLocation(Set<BoardCell> targets){
		return null;
	}
	
	public Solution makeAccusation(){
		return null;
	}
	
	public Solution createSuggestion(){
		return null;
	}
}
