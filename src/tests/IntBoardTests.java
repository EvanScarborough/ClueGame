package tests;

import experiment.*;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import clueGame.BoardCell;
/**
 * 
 * @author Michael Balmes and Bixler Baker
 *
 */
/**
 * @author michael
 *
 */
public class IntBoardTests {
	IntBoard board;
	
	@Before
	public void initialize() { 
		board = new IntBoard();
	}
	
	/*
	 * Test adjacencies for top left corner
	 */
	@Test
	public void testAdjacency0(){
		BoardCell cell = board.getCell(0, 0);
		Set<BoardCell> testList = board.getAdjList(cell);
		assertTrue(testList.contains(new BoardCell(1, 0)));
		assertTrue(testList.contains(new BoardCell(0, 1)));
		assertEquals(2, testList.size());
	}
	
	/*
	 * Test adjacencies for bottom right corner
	 */
	@Test
	public void testAdjacency1(){
		BoardCell cell = board.getCell(3, 3);
		Set<BoardCell> testList = board.getAdjList(cell);
		assertTrue(testList.contains(new BoardCell(3, 2)));
		assertTrue(testList.contains(new BoardCell(2, 3)));
		assertEquals(2, testList.size());
	}
	
	/*
	 * Test adjacencies for a right edge
	 */
	@Test
	public void testAdjacency2()
	{
		BoardCell cell = board.getCell(1, 3);
		Set<BoardCell> testList = board.getAdjList(cell);
		assertTrue(testList.contains(new BoardCell(0, 3)));
		assertTrue(testList.contains(new BoardCell(1, 2)));
		assertTrue(testList.contains(new BoardCell(2, 3)));
		assertEquals(3, testList.size());
	}
	
	/*
	 * Test adjacencies for a left edge
	 */
	public void testAdjacency3()
	{
		BoardCell cell = new BoardCell(2, 0);
		Set<BoardCell> testList = board.getAdjList(cell);
		assertTrue(testList.contains(new BoardCell(1, 0)));
		assertTrue(testList.contains(new BoardCell(2, 1)));
		assertTrue(testList.contains(new BoardCell(3, 0)));
		assertEquals(3, testList.size());
	}
	
	/*
	 * Test adjacencies for second column middle of grid
	 */
	public void testAdjacency4()
	{
		BoardCell cell = board.getCell(1, 1);
		Set<BoardCell> testList = board.getAdjList(cell);
		assertTrue(testList.contains(new BoardCell(0, 1)));
		assertTrue(testList.contains(new BoardCell(1, 0)));
		assertTrue(testList.contains(new BoardCell(2, 1)));
		assertTrue(testList.contains(new BoardCell(1, 2)));
		assertEquals(4, testList.size());
	}
	
	/*
	 * Test adjacencies for second from last column, middle of grid
	 */
	public void testAdjacency5()
	{
		BoardCell cell = board.getCell(2, 2);
		Set<BoardCell> testList = board.getAdjList(cell);
		assertTrue(testList.contains(new BoardCell(2, 1)));
		assertTrue(testList.contains(new BoardCell(1, 2)));
		assertTrue(testList.contains(new BoardCell(2, 3)));
		assertTrue(testList.contains(new BoardCell(3, 2)));
		assertEquals(4, testList.size());
	}
	
	/*
	 * Test targets at 0,0 3 tiles away
	 */
	public void testTargets0_3()
	{
		BoardCell cell = board.getCell(0, 0);
		board.calcTargets(cell, 3);
		Set targets = board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(3, 0)));
		assertTrue(targets.contains(board.getCell(2, 1)));
		assertTrue(targets.contains(board.getCell(1, 2)));
		assertTrue(targets.contains(board.getCell(0, 3)));
	}
	
	/*
	 * Test targets at 3,0 3 tiles away
	 */
	public void testTargets1_3()
	{
		BoardCell cell = board.getCell(3, 0);
		board.calcTargets(cell, 3);
		Set targets = board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(0, 0)));
		assertTrue(targets.contains(board.getCell(1, 1)));
		assertTrue(targets.contains(board.getCell(3, 3)));
		assertTrue(targets.contains(board.getCell(2, 2)));
	}
	
	/*
	 * Test targets at 1,1 2 tiles away
	 */
	public void testTargets0_2()
	{
		BoardCell cell = board.getCell(1,1);
		board.calcTargets(cell, 2);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(6, targets.size());
		assertTrue(targets.contains(board.getCell(2, 0)));
		assertTrue(targets.contains(board.getCell(3, 1)));
		assertTrue(targets.contains(board.getCell(2, 2)));
		assertTrue(targets.contains(board.getCell(1, 3)));
		assertTrue(targets.contains(board.getCell(0, 2)));
		assertTrue(targets.contains(board.getCell(0, 0)));
	}
	
	@Test
	public void testTargets1_2()
	{
		BoardCell cell = board.getCell(3,1);
		board.calcTargets(cell, 3);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(7, targets.size());
		assertTrue(targets.contains(board.getCell(0, 1)));
		assertTrue(targets.contains(board.getCell(1, 2)));
		assertTrue(targets.contains(board.getCell(2, 3)));
		assertTrue(targets.contains(board.getCell(1, 0)));
	}
	
	/*
	 * Test targets at 1,1 1 tiles away
	 */
	public void testTargets0_1()
	{
		BoardCell cell = board.getCell(1,1);
		board.calcTargets(cell, 1);
		Set targets = board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(1,0)));
	}
	
	/*
	 * Test targets at 3,1 1 tiles away
	 */
	public void testTargets1_1()
	{
		BoardCell cell = board.getCell(3,1);
		board.calcTargets(cell, 1);
		Set targets = board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCell(2, 1)));
		assertTrue(targets.contains(board.getCell(3, 2)));
		assertTrue(targets.contains(board.getCell(3, 0)));
	}
}
