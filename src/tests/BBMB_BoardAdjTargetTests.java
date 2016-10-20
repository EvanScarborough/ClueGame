package tests;

/*
 * This program tests that adjacencies and targets are calculated correctly.
 */

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import clueGame.Board;
import clueGame.BoardCell;

public class BBMB_BoardAdjTargetTests {
	// We make the Board static because we can load it one time and 
	// then do all the tests. 
	private static Board board;   
	@BeforeClass
	public static void setUp() {
		// Board is singleton, get the only instance and initialize it		
		board = Board.getInstance();
		board.setConfigFiles("data/BBMB_ClueLayout.csv", "data/BBMB_ClueLegend.txt");		
		board.initialize();
	}
	
	// Ensure that player can move freely on a walkway
	// These cells are LIGHT PURPLE on the planning spreadsheet
	@Test
	public void testSurroundingWalkway()
	{
		// Test a square surrounding
		Set<BoardCell> testList = board.getAdjList(5, 8);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCellAt(4, 8)));
		assertTrue(testList.contains(board.getCellAt(6, 8)));
		assertTrue(testList.contains(board.getCellAt(5, 7)));
		assertTrue(testList.contains(board.getCellAt(5, 9)));
	}
	
	// Ensure that player does not move around within room or off the board
	// These cells are LIGHT BLUE on the planning spreadsheet
	@Test
	public void testEdges()
	{
		// Test a corner
		Set<BoardCell> testList = board.getAdjList(21, 0);
		assertEquals(0, testList.size());
		
		testList = board.getAdjList(21,22);
		assertEquals(0, testList.size());
		
		testList = board.getAdjList(7, 22);
		assertEquals(0, testList.size());
		
		testList = board.getAdjList(0, 16);
		assertEquals(0, testList.size());
	}

	// Ensure that player does not move into a room without using a door
	// These cells are PURPLE on the planning spreadsheet
	@Test
	public void testRoomEdges()
	{
		Set<BoardCell> testList = board.getAdjList(13, 1);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCellAt(13, 0)));
		assertTrue(testList.contains(board.getCellAt(13, 2)));
		assertTrue(testList.contains(board.getCellAt(14, 1)));
		
		testList = board.getAdjList(5, 5);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCellAt(5, 4)));
		assertTrue(testList.contains(board.getCellAt(5, 6)));
		assertTrue(testList.contains(board.getCellAt(6, 5)));
		
	}
	
	// Ensure that player can move into a door when in the right location
	// These cells are ORANGE on the planning spreadsheet
	@Test
	public void testDoorEntrance()
	{
		Set<BoardCell> testList = board.getAdjList(4, 7);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCellAt(3, 7)));
		assertTrue(testList.contains(board.getCellAt(4, 6)));
		
		testList = board.getAdjList(3, 14);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCellAt(3, 13)));

		testList = board.getAdjList(9, 6);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCellAt(9, 5)));
		
		testList = board.getAdjList(10, 6);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCellAt(10, 5)));
	}
	
	// Ensure that player can move out of a door when in one
	// These cells are GREEN on the planning spreadsheet
	@Test
	public void testDoorExit()
	{
		Set<BoardCell> testList = board.getAdjList(9, 5);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCellAt(9, 6)));

		testList = board.getAdjList(10, 5);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCellAt(10, 6)));
	}
	
	// Ensure that player can move freely on a walkway
	// These cells are DARK GREEN on the planning spreadsheet
	@Test
	public void testSurroundingWalkwayTargets()
	{
		// Test a square surrounding
		board.calcTargets(0,0,3);
		Set<BoardCell> testList = board.getTargets();
		assertEquals(2, testList.size());
		assertTrue(testList.contains(board.getCellAt(3, 0)));
		assertTrue(testList.contains(board.getCellAt(0,3)));
		
		board.calcTargets(6,0,1);
		testList = board.getTargets();
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCellAt(5, 0)));
		
		board.calcTargets(0,8,1);
		testList = board.getTargets();
		assertEquals(2, testList.size());
		assertTrue(testList.contains(board.getCellAt(0, 7)));
		assertTrue(testList.contains(board.getCellAt(1, 8)));
		
		board.calcTargets(5,9,3);
		testList = board.getTargets();
		assertEquals(10, testList.size());
	}
	// Ensure that a player can target a door
	// These cells are DARK RED on the planning spreadsheet
	@Test
	public void testDoorTarget()
	{
		// Test a square surrounding
		board.calcTargets(2,8,4);
		Set<BoardCell> testList = board.getTargets();

		assertTrue(testList.contains(board.getCellAt(3, 7)));

		board.calcTargets(5,7,6);
		testList = board.getTargets();
		assertTrue(testList.contains(board.getCellAt(3, 7)));
	}
	
	// Ensure that a player can target outside a door
	// These cells are DARK ORANGE on the planning spreadsheet
	@Test
	public void testDoorExitTarget()
	{
		// Test a square surrounding
		board.calcTargets(1,9,1);
		Set<BoardCell> testList = board.getTargets();
		assertTrue(testList.contains(board.getCellAt(1, 8)));

		board.calcTargets(3,7,2);
		testList = board.getTargets();
		for (BoardCell bitches : testList) {
			System.out.println(bitches);
		}
		assertTrue(testList.contains(board.getCellAt(5, 7)));
	}
}