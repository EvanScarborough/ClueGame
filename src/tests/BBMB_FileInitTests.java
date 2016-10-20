package tests;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import clueGame.*;

public class BBMB_FileInitTests {
	// known constants of the files being uploaded
	public static final int LEGEND_SIZE = 11;
	public static final int NUM_ROWS = 22;
	public static final int NUM_COLUMNS = 23;
	
	private static Board board;
	
	@BeforeClass
	public static void setUp() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("data/BBMB_ClueLayout.csv", "data/BBMB_ClueLegend.txt");		
		// Initialize will load BOTH config files 
		board.initialize();
	}
	
	@Test
	public void testRooms() {
		// Get the map of initial => room 
		Map<Character, String> legend = board.getLegend();
		// Ensure we read the correct number of rooms
		assertEquals(LEGEND_SIZE, legend.size());
		// To ensure data is correctly loaded, test retrieving a few rooms 
		// from the hash, including the first and last in the file and a few others
		assertEquals("Bedroom", legend.get('B'));
		assertEquals("Movie room", legend.get('M'));
		assertEquals("Lounge", legend.get('L'));
		assertEquals("Office", legend.get('O'));
		assertEquals("Server room", legend.get('S'));
		assertEquals("Kitchen", legend.get('K'));
		assertEquals("Sun room", legend.get('R'));
		assertEquals("Pool", legend.get('P'));
		assertEquals("Garage", legend.get('G'));
		assertEquals("Closet", legend.get('X'));
		assertEquals("Walkway", legend.get('W'));
	}
	
	@Test
	public void testBoardDimensions() {
		// Ensure we have the proper number of rows and columns
		assertEquals(NUM_ROWS, board.getNumRows());
		assertEquals(NUM_COLUMNS, board.getNumColumns());		
	}
	
	// Test two doorways in each direction (RIGHT/LEFT/UP/DOWN), plus 
	// two cells that are room squares and two that are walkways.
	// These cells are grey on the planning spreadsheet
	@Test
	public void FourDoorDirections() {
		BoardCell room = board.getCellAt(1, 9);
		assertTrue(room.isDoorway());
		assertEquals(DoorDirection.LEFT, room.getDoorDirection());
		room = board.getCellAt(1, 21);
		assertTrue(room.isDoorway());
		assertEquals(DoorDirection.DOWN, room.getDoorDirection());
		room = board.getCellAt(3, 13);
		assertTrue(room.isDoorway());
		assertEquals(DoorDirection.RIGHT, room.getDoorDirection());
		room = board.getCellAt(5, 18);
		assertTrue(room.isDoorway());
		assertEquals(DoorDirection.LEFT, room.getDoorDirection());
		room = board.getCellAt(9, 19);
		assertTrue(room.isDoorway());
		assertEquals(DoorDirection.DOWN, room.getDoorDirection());
		room = board.getCellAt(10, 5);
		assertTrue(room.isDoorway());
		assertEquals(DoorDirection.RIGHT, room.getDoorDirection());
		room = board.getCellAt(16, 9);
		assertTrue(room.isDoorway());
		assertEquals(DoorDirection.UP, room.getDoorDirection());
		room = board.getCellAt(18, 18);
		assertTrue(room.isDoorway());
		assertEquals(DoorDirection.UP, room.getDoorDirection());
		// Test that room pieces that aren't doors know it
		room = board.getCellAt(3, 16);
		assertFalse(room.isDoorway());
		room = board.getCellAt(20, 5);
		assertFalse(room.isDoorway());
		// Test that walkways are not doors
		BoardCell cell = board.getCellAt(7, 6);
		assertFalse(cell.isDoorway());
		cell = board.getCellAt(14, 15);
		assertFalse(cell.isDoorway());
	}
	
	// Test that we have the correct number of doors
	@Test
	public void testNumberOfDoorways() 
	{
		int numDoors = 0;
		for (int row=0; row<board.getNumRows(); row++)
			for (int col=0; col<board.getNumColumns(); col++) {
				BoardCell cell = board.getCellAt(row, col);
				if (cell.isDoorway())
					numDoors++;
			}
		Assert.assertEquals(13, numDoors);
	}
	
	// Test a few room cells to ensure the room initial is correct.
	@Test
	public void testRoomInitials() {
		// Test first cell in room
		assertEquals('O', board.getCellAt(0, 15).getInitial());
		assertEquals('P', board.getCellAt(1, 1).getInitial());
		assertEquals('B', board.getCellAt(0, 9).getInitial());
		// Test last cell in room
		assertEquals('L', board.getCellAt(9, 22).getInitial());
		assertEquals('G', board.getCellAt(12, 4).getInitial());
		// Test a walkway
		assertEquals('W', board.getCellAt(14, 9).getInitial());
		// Test the closet
		assertEquals('X', board.getCellAt(7, 10).getInitial());
	}
}
