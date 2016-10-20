package clueGame;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

public class Player {
	private String playerName;
	private int row;
	private int column;
	private Color color;
	
	private Set<Card> myCards;
	
	public Card disproveSuggestion(Solution suggestion){
		return null;
	}
	
	public Player(String name){
		myCards = new HashSet<Card>();
		playerName = name;
	}
	public void setColor(int r, int g, int b){
		color = new Color(r,g,b);
	}
	public void setPosition(int y, int x){
		row = y;
		column = x;
	}
	
	public String getName(){
		return playerName;
	}
	public Color getColor(){
		return color;
	}
	public int[] getPosition(){
		int[] ret = new int[2];
		ret[0] = row;
		ret[1] = column;
		return ret;
	}
	
	public int getHandSize(){
		return myCards.size();
	}
	public Set<Card> getMyCards(){
		return myCards;
	}
	public void giveCard(Card c){
		myCards.add(c);
	}
}
