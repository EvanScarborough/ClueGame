package clueGame;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class ComputerPlayer extends Player{
	public char lastVisited = '-';
	
	public ComputerPlayer(String name) {
		super(name);
	}

	public BoardCell pickLocation(Set<BoardCell> targets){
		Set<BoardCell> rooms = new HashSet<BoardCell>();
		Random rand = new Random();
		for(BoardCell c: targets){
			//System.out.println(c.Row() + "  " + c.Column());
			if(c.isDoorway() && c.getInitial() != lastVisited){
				boolean haveThatCard = false;
				for(Card r: myCards){
					if(r.getName().equals(Board.rooms.get(c.getInitial()))) haveThatCard = true;
				}
				if(!haveThatCard) rooms.add(c);
				else if(rand.nextBoolean() || !Board.isRealGame) rooms.add(c);
			}
		}
		if(rooms.size() == 0){
			//System.out.println(targets.size());
			int randomIndex = rand.nextInt(targets.size());
			for(BoardCell c: targets){
				if(randomIndex == 0) return c;
				randomIndex--;
			}
		}
		int randomIndex = rand.nextInt(rooms.size());
		for(BoardCell c: rooms){
			if(randomIndex == 0) return c;
			randomIndex--;
		}
		return null;
	}
	
	public Solution makeAccusation(){
		return null;
	}
	
	public Solution createSuggestion(){
		Board board = Board.getInstance();
		ArrayList<Card> people = new ArrayList<Card>();
		for(Card c: Board.peopleCards){
			if(!myCards.contains(c)) people.add(c);
		}
		ArrayList<Card> weapons = new ArrayList<Card>();
		for(Card c: Board.weaponCards){
			if(!myCards.contains(c)) weapons.add(c);
		}
		Random rand = new Random();
		String roomsug = board.rooms.get(board.getCell(row, column).getInitial());
		//System.out.println(roomsug);
		String person = people.get(rand.nextInt(people.size())).getName();
		String weapon = weapons.get(rand.nextInt(weapons.size())).getName();
		return new Solution(person,roomsug,weapon);
	}
	
	
	
	
	@Override
	public Solution takeTurn(Set<BoardCell> targets){
		BoardCell newCell = pickLocation(targets);
		row = newCell.getRow();
		column = newCell.getColumn();
		if(newCell.isDoorway()){
			lastVisited = newCell.getInitial();
			return createSuggestion();
		}
		return null;
	}
}
