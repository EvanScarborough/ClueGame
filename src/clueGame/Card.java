package clueGame;

public class Card {
	private String cardName;
	private CardType type;
	
	public boolean equals(){
		return false;
	}
	
	public Card(String name, CardType t){
		cardName = name;
		type = t;
	}
	
	public CardType getType(){
		return type;
	}
	public String getName(){
		return cardName;
	}
}
