package tests;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import clueGame.Board;
import clueGame.BoardCell;
import clueGame.Card;
import clueGame.CardType;
import clueGame.ComputerPlayer;
import clueGame.Solution;

public class gameActionTests {
	private static Board board;
	@BeforeClass
	public static void setup(){
		board = Board.getInstance();
		board.setConfigFiles("data/BBMB_ClueLayout.csv", "data/BBMB_ClueLegend.txt");		
		board.initialize();
		board.loadConfigFiles("data/CluePlayers.txt", "data/ClueWeapons.txt");
		board.dealCards();
	}
	
	@Test
	public void testSelectTargetLocation(){
		ComputerPlayer npc = new ComputerPlayer("The Atomic Squat");
		npc.setPosition(13,11);
		int p[] = npc.getPosition(); //Position of Test Computer Player
		board.calcTargets(board.getCell(p[0], p[1]), 5); //Calc Targets from this position with 5 movement
		Set<BoardCell> targets = board.getTargets();
		BoardCell b = npc.pickLocation(targets);
		//Makes sure this cell is one of npc's options
		assertEquals(b,board.getCell(16, 9));
		
		npc.lastVisited = 'S';
		Map<BoardCell,Integer> chosen = new HashMap<BoardCell,Integer>();
		for(int i = 0; i < 100; i++){
			b = npc.pickLocation(targets);
			if(!chosen.containsKey(b)) chosen.put(b, 1);
		}
		//Tests size of possible movements
		assertEquals(chosen.size(),16);
		
		board.calcTargets(board.getCell(p[0], p[1]), 4);
		targets = board.getTargets();
		chosen.clear();
		for(int i = 0; i < 100; i++){
			b = npc.pickLocation(targets);
			if(!chosen.containsKey(b)) chosen.put(b, 1);
		}
		//Tests random cell from possible targets
		assertEquals(chosen.size(),11);
	}
	
	
	@Test
	public void testAccusation() {
		//Tests if s is the correct answer
		Solution s = new Solution(board.theAnswer.person,board.theAnswer.room,board.theAnswer.weapon);
		assertTrue(board.checkAccusation(s));
		s.person = "wrong";
		//tests if s guesses only the wrong person
		assertFalse(board.checkAccusation(s));
		s = new Solution(board.theAnswer.person,board.theAnswer.room,board.theAnswer.weapon);
		s.room = "wrong";
		//tests if s guesses only the wrong room
		assertFalse(board.checkAccusation(s));
		s = new Solution(board.theAnswer.person,board.theAnswer.room,board.theAnswer.weapon);
		s.weapon = "wrong";
		//tests if s guesses only the wrong weapon
		assertFalse(board.checkAccusation(s));
	}
	
	@Test
	public void testCreateSuggestion() {
		ComputerPlayer npc = new ComputerPlayer("The Atomic Squat");
		npc.setPosition(16,9); //He's in the S
		npc.giveCard(Board.weaponCards.get(0));
		
		Set<String> guessedWeapons = new HashSet<String>();
		Set<String> guessedPeople = new HashSet<String>();
		for(int i = 0; i < 100; i++){
			Solution s = npc.createSuggestion();
			assertTrue(s.room.equals("Server room")); // Tests that the Server room is the only room option
			if(!guessedWeapons.contains(s.weapon)) guessedWeapons.add(s.weapon);
			if(!guessedPeople.contains(s.person)) guessedPeople.add(s.person);
		}
		assertTrue(guessedWeapons.size() == 5); //Tests that all the weapons are choices for the guess.
		assertFalse(guessedWeapons.contains(Board.weaponCards.get(0))); //Makes sure that the player has one of the weapon cards.
		assertTrue(guessedPeople.size() == 6); // Tests that all the player cards are possible choices.
		
		npc.giveCard(Board.peopleCards.get(0));
		for(int i = 1; i < 5; i++){
			npc.giveCard(Board.weaponCards.get(i));
			npc.giveCard(Board.peopleCards.get(i));
		}
		
		Solution s = npc.createSuggestion();
		assertTrue(s.person.equals(Board.peopleCards.get(5).getName())); //Tests that if multiple people cards exist one is chosen at random.
		assertTrue(s.weapon.equals(Board.weaponCards.get(5).getName())); // Tests that if multiple weapon cards exist one is chosen at random.
	}
	
	@Test
	public void testDisproveSuggestion() {
		Solution s = new Solution(Board.peopleCards.get(0).getName(),Board.roomCards.get(0).getName(),Board.weaponCards.get(0).getName());
		ComputerPlayer npc = new ComputerPlayer("The Atomic Squat");
		assertTrue(npc.disproveSuggestion(s) == null); // Tests that if there is nothing to disprove the statement.
		npc.giveCard(Board.weaponCards.get(0));
		assertEquals(npc.disproveSuggestion(s),Board.weaponCards.get(0)); // Tests that the weapon card exists in the npc and returns it to the user.
		npc.giveCard(Board.peopleCards.get(0));
		Set<Card> returnedCards = new HashSet<Card>();
		for(int i = 0; i < 50; i++){
			Card c = npc.disproveSuggestion(s);
			if(!returnedCards.contains(c)) returnedCards.add(c);
		}
		assertTrue(returnedCards.size() == 2); // Tests that it is randomly choosing of among the two cards that it can return.
	}
	
	
	@Test
	public void testHandleSuggestion(){
		//suggestion no player can disprove
		assertTrue(board.handleSuggestion(0,board.theAnswer) == null);
		assertTrue(board.handleSuggestion(1,board.theAnswer) == null);
		//suggestion only suggesting player can disprove
		for(int i = 1; i < 6; i++){
			Set<Card> cards = board.players.get(i).getMyCards();
			boolean hasWeapon = false;
			for(Card c: cards){
				if(c.getType() == CardType.WEAPON){//find a computer player with a weapon and use that weapon as part of the suggestion
					Solution p = new Solution(board.theAnswer.person, board.theAnswer.room, c.getName());
					assertTrue(board.handleSuggestion(i,p) == null);
					break;
				}
			}
			if(hasWeapon) break;
		}
		//System.out.println(board.theAnswer.person);
		Solution playersol = new Solution(board.theAnswer.person,board.theAnswer.room,board.theAnswer.weapon);
		Set<Card> playercards = board.players.get(0).getMyCards();
		boolean humanfound[] = new boolean[3];
		for(Card c: playercards){ //Assemble solution from player's cards or the answers
			switch(c.getType()){
			case PERSON:
				playersol.person = c.getName();
				humanfound[0] = true;
				break;
			case ROOM:
				playersol.room = c.getName();
				humanfound[1] = true;
				break;
			case WEAPON:
				playersol.weapon = c.getName();
				humanfound[2] = true;
				break;
			}
		}
		assertNull(board.handleSuggestion(0,playersol)); //Suggestion made by human only human can disprove is null
		
		String cardString = board.handleSuggestion(1,playersol).getName(); //Suggestion made by computer only human can disprove
		boolean playerCardFound = false;
		for(Card c: playercards){
			if(c.getName().equals(cardString)) playerCardFound = true;
		}
		assertTrue(playerCardFound); //should contain a card that the player has
		
		Solution p5sol = new Solution(board.theAnswer.person,board.theAnswer.room,board.theAnswer.weapon);
		Set<Card> p5cards = board.players.get(5).getMyCards();
		boolean found[] = new boolean[3];
		for(Card c: p5cards){ //Assemble solution from player 5's cards or the answers
			switch(c.getType()){
			case PERSON:
				p5sol.person = c.getName();
				found[0] = true;
				break;
			case ROOM:
				p5sol.room = c.getName();
				found[1] = true;
				break;
			case WEAPON:
				p5sol.weapon = c.getName();
				found[2] = true;
				break;
			}
		}//p5sol can only be disproved by player at index 5 at this point
		if(found[0] && found[1] && found[2]) found[0] = false; //make sure there's at least one that can be replaced
		for(int i = 0; i < 4; i++){
			boolean ff = false;
			Set<Card> cards = board.players.get(i).getMyCards();
			for(Card c: cards){
				switch(c.getType()){
				case PERSON:
					if(found[0])break;
					p5sol.person = c.getName();
					ff = true;
					break;
				case ROOM:
					if(found[1])break;
					p5sol.room = c.getName();
					ff = true;
					break;
				case WEAPON:
					if(found[2])break;
					p5sol.weapon = c.getName();
					ff = true;
					break;
				}
				if(ff)break;
			}
			if(ff) break;
		}//this adds a card from a player past p5 that will not overwrite a card that p5 has (unless p5 has every card in the solution)
		//It's times like this that I really start to not enjoy TDD
		cardString = board.handleSuggestion(4,p5sol).getName(); //Suggestion made by p4 that p5 and another non-human can disprove
		playerCardFound = false;
		for(Card c: p5cards){
			if(c.getName().equals(cardString)) playerCardFound = true;
		}
		assertTrue(playerCardFound); //should contain a card that player 5 has
		
		//Note: Our code treats human and computer players the same with regard to disproving a solution,
		//this last test will ensure that two players have cards to disprove a suggestion, but these could
		//be human or computer. It works for both.
		
	}
}






