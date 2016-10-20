package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.*;

import clueGame.Board;
import clueGame.Card;
import clueGame.CardType;

public class gameSetupTests {
	private static Board board;
	@BeforeClass
	public static void setup(){
		board = Board.getInstance();
		board.setConfigFiles("data/BBMB_ClueLayout.csv", "data/BBMB_ClueLegend.txt");		
		board.initialize();
		board.loadConfigFiles("data/CluePlayers.txt", "data/ClueWeapons.txt");
	}
	
	@Test
	public void testLoadPeople(){
		assertTrue(board.players.size() == 6);
		//Checks a few player names
		assertTrue(board.players.get(0).getName().equals("Blaster the Burro"));
		assertTrue(board.players.get(3).getName().equals("Dr. Rader"));
		//Checks a player's colors
		assertTrue(board.players.get(5).getColor().getBlue() == 230);
		assertTrue(board.players.get(5).getColor().getGreen() == 203);
		assertTrue(board.players.get(5).getColor().getRed() == 42);
		//Checks a few player positions
		assertTrue(board.players.get(1).getPosition()[0] == 21);
		assertTrue(board.players.get(1).getPosition()[1] == 7);
		assertTrue(board.players.get(2).getPosition()[0] == 21);
		assertTrue(board.players.get(2).getPosition()[1] == 15);
	}
	///*
	@Test
	public void testLoadDeck(){
		//Tests if there are the correct number of cards
		assertTrue(board.cards.size() == 21);
		int[] numCardType = new int[3];
		boolean hasRader = false;
		boolean hasPencil = false;
		boolean hasKitchen = false;
		for(int i = 0; i < board.cards.size(); i++) {
			CardType c = board.cards.get(i).getType();
			switch (c){
				case ROOM:
					numCardType[0]++;
					break;
				case PERSON:
					numCardType[1]++;
					break;
				case WEAPON:
					numCardType[2]++;
					break;
			}
			if(board.cards.get(i).getName().equals("Dr. Rader")) hasRader = true;
			if(board.cards.get(i).getName().equals("Pencil")) hasPencil = true;
			if(board.cards.get(i).getName().equals("Kitchen")) hasKitchen = true;
		}
		//Tests if there is the correct number of each type of cards
		assertTrue(numCardType[0] == 9);
		assertTrue(numCardType[1] == 6);
		assertTrue(numCardType[2] == 6);
		//Checks that specific cards exists in the deck
		assertTrue(hasRader);
		assertTrue(hasPencil);
		assertTrue(hasKitchen);
	}
	
	@Test
	public void testDealCards(){
		//Tests if all cards have been dealt
		assertTrue(board.cards.size() == 0);
		ArrayList<Integer> numCards = new ArrayList<Integer>();
		Set<String> found = new HashSet<String>();
		found.add(board.theAnswer.person);
		found.add(board.theAnswer.weapon);
		found.add(board.theAnswer.room);
		for (int i = 0; i < 6; i++){
			numCards.add(board.players.get(i).getHandSize());
			//Checks that no cards are repeated
			for(Card c: board.players.get(i).getMyCards()){
				if(found.contains(c.getName())){
					assertTrue(false);
				}
				found.add(c.getName());
			}
		}
		//Checks that no player has more than one card than any other player
		assertTrue(Collections.max(numCards) - Collections.min(numCards) <= 1);
		
		
		
	}
	//*/
}
