package clueGame;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

public class Player {
	protected String playerName;
	protected int row;
	protected int column;
	protected Color color;
	
	protected Set<Card> myCards;
	
	public Card disproveSuggestion(Solution suggestion){
		ArrayList<Card> randoReturn = new ArrayList<Card>();
		
		for(Card c: myCards){ //Add cards that can disprove into an arraylist
			if(c.getName().equals(suggestion.person)||c.getName().equals(suggestion.room)||c.getName().equals(suggestion.weapon)) randoReturn.add(c);
		}
		
		if (randoReturn.size() == 0) return null; //Can't disprove anything
		
		Random rand = new Random();
		
		return randoReturn.get(rand.nextInt(randoReturn.size())); //Return a random card that can disprove.
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
	
	
	
	
	
	public void draw(Graphics g){
		g.setColor(color);
		g.fillOval(column * BoardCell.TILE_SIZE, row * BoardCell.TILE_SIZE, BoardCell.TILE_SIZE, BoardCell.TILE_SIZE);
		g.setColor(Color.BLACK);
		g.drawOval(column * BoardCell.TILE_SIZE, row * BoardCell.TILE_SIZE, BoardCell.TILE_SIZE, BoardCell.TILE_SIZE);
	}
}
