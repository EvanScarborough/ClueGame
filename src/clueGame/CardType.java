package clueGame;

public enum CardType {
	PERSON, WEAPON, ROOM;
	
	public String toString() {
		String type = "Error";
		switch (this) {
		case PERSON:
			type = "Person";
			break;
		
		case WEAPON:
			type = "Weapon";
			break;
			
		case ROOM:
			type = "Room";
			break;
		}
		
		return type;
	}
}
