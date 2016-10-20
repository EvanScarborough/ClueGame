package clueGame;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Board {
	private static Board theInstance = new Board();
	private static int numRows, numColumns;
	private static BoardCell[][] board;
	private static Map<Character, String> rooms;
	private static Map<BoardCell, HashSet<BoardCell>> adjMatrix;
	private static HashSet<BoardCell> targets = new HashSet<BoardCell>();
	private static HashSet<BoardCell> visited = new HashSet<BoardCell>();
	private static String boardConfigFile;
	private static String roomConfig;
	
	public static int MAX_BOARD_SIZE = 0;

	private Board() {}
	
	public static Board getInstance() { 
		return theInstance; 	
	}
	
	public void setConfigFiles(String map, String legend) {
		Board.boardConfigFile = map;
		Board.roomConfig = legend;
		Board.rooms = new HashMap<Character, String>();
	}
	
	public void initialize() {
		try {
			File fileinLayout = new File(boardConfigFile);
			File fileinLegend = new File(roomConfig);
			Scanner scLayout = new Scanner(fileinLayout);
			Scanner scLegend = new Scanner(fileinLegend);
			
			while (scLegend.hasNextLine()) {
				String str = scLegend.nextLine();
				String[] lineArray = str.split(", ");
				Board.rooms.put(lineArray[0].charAt(0), lineArray[1]);
			}
			scLegend.close();
			
			int lineCount = 0;
			ArrayList<String> cellTypes = new ArrayList<String>();
			while (scLayout.hasNextLine()) {
				String str = scLayout.nextLine();
				String[] strArray = str.split(",");
				for (int i = 0; i < strArray.length; i++) {
					cellTypes.add(strArray[i]);
				}
				lineCount++;
			}
			scLayout.close();
			
			int cellNum = 0;
			Board.numRows = lineCount;
			Board.numColumns = cellTypes.size() / Board.numRows;
			board = new BoardCell[numRows][numColumns];
			for (int i = 0; i < Board.numRows; i++) {
				for (int j = 0; j < Board.numColumns; j++) {
					board[i][j] = new BoardCell(i, j);
					if (cellTypes.get(cellNum).length() > 1) {
						board[i][j].setType(BoardCell.CellType.DOORWAY);
						if (cellTypes.get(cellNum).charAt(1) == 'U') {
							board[i][j].setDoorDirection(DoorDirection.UP);
						} else if (cellTypes.get(cellNum).charAt(1) == 'R') {
							board[i][j].setDoorDirection(DoorDirection.RIGHT);
						} else if (cellTypes.get(cellNum).charAt(1) == 'D') {
							board[i][j].setDoorDirection(DoorDirection.DOWN);
						} else if (cellTypes.get(cellNum).charAt(1) == 'L') {
							board[i][j].setDoorDirection(DoorDirection.LEFT);
						} else if (cellTypes.get(cellNum).charAt(1) == 'N'){
							board[i][j].setType(BoardCell.CellType.ROOM);
						}
					} else if (cellTypes.get(cellNum).charAt(0) == 'W') {
						board[i][j].setType(BoardCell.CellType.WALKWAY);
					} else {
						board[i][j].setType(BoardCell.CellType.ROOM);
					}
					
					board[i][j].setInitial(cellTypes.get(cellNum).charAt(0));
					
					cellNum++;
				}
			}
			calcAdjacencies();
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public Map<Character, String> getLegend() {
		return Board.rooms;
	}
	
	public int getNumRows() {
		return Board.numRows;
	}
	
	public int getNumColumns() {
		return Board.numColumns;
	}
	
	public BoardCell getCellAt(int row, int column) {
		return board[row][column];
	}
	
	public void calcAdjacencies() {
		adjMatrix = new HashMap<BoardCell, HashSet<BoardCell>>();
		for(int i=0;i<board.length;i++){
			for(int j=0;j<board[0].length;j++){
				BoardCell cell = board[i][j];
				adjMatrix.put(cell, new HashSet<BoardCell>());
				if(cell.isRoom()){
					continue;
				}
				if (cell.isDoorway()) {
					if (cell.getDoorDirection()==DoorDirection.UP) {
						adjMatrix.get(cell).add(board[cell.getRow()-1][cell.getColumn()]);
					} else if (cell.getDoorDirection()==DoorDirection.DOWN) {
						adjMatrix.get(cell).add(board[cell.getRow()+1][cell.getColumn()]);
					} else if (cell.getDoorDirection()==DoorDirection.RIGHT) {
						adjMatrix.get(cell).add(board[cell.getRow()][cell.getColumn()+1]);
					} else if (cell.getDoorDirection()==DoorDirection.LEFT) {
						adjMatrix.get(cell).add(board[cell.getRow()][cell.getColumn()-1]);
					}
					continue;
				}
				if(cell.getRow()>0){
					if(!board[cell.getRow()-1][cell.getColumn()].isRoom()){
						if(board[cell.getRow()-1][cell.getColumn()].isDoorway()&&board[cell.getRow()-1][cell.getColumn()].getDoorDirection()!=DoorDirection.DOWN){
						}
						else adjMatrix.get(cell).add(board[cell.getRow()-1][cell.getColumn()]);
					}
				}
				if(cell.getRow()<board.length-1){
					if(!board[cell.getRow()+1][cell.getColumn()].isRoom()){
						if(board[cell.getRow()+1][cell.getColumn()].isDoorway()&&board[cell.getRow()+1][cell.getColumn()].getDoorDirection()!=DoorDirection.UP){
						}
						else adjMatrix.get(cell).add(board[cell.getRow()+1][cell.getColumn()]);
					}
				}
				if(cell.getColumn()>0){
					if(!board[cell.getRow()][cell.getColumn()-1].isRoom()){
						if(board[cell.getRow()][cell.getColumn()-1].isDoorway()&&board[cell.getRow()][cell.getColumn()-1].getDoorDirection()!=DoorDirection.RIGHT){
						}
						else adjMatrix.get(cell).add(board[cell.getRow()][cell.getColumn()-1]);
					}
				}
				if(cell.getColumn()<board[0].length-1){
					if(!board[cell.getRow()][cell.getColumn()+1].isRoom()){
						if(board[cell.getRow()][cell.getColumn()+1].isDoorway()&&board[cell.getRow()][cell.getColumn()+1].getDoorDirection()!=DoorDirection.LEFT){
						}
						else adjMatrix.get(cell).add(board[cell.getRow()][cell.getColumn()+1]);
					}
				}
			}
		}
	}
	
	public void calcTargets(BoardCell cell, int pathLength){
		targets = new HashSet<BoardCell>();
		visited = new HashSet<BoardCell>();
		Board.visited.add(cell);
		findAllTargets(cell, pathLength);
	}
	
	public void findAllTargets(BoardCell cell, int pathLength){
		HashSet<BoardCell> adjCells = Board.adjMatrix.get(cell);
		for(BoardCell bc: adjCells){
			if(!Board.visited.contains(bc)){
				if(bc.isDoorway()){
					if(bc.getDoorDirection()==DoorDirection.DOWN&&cell.getRow()==bc.getRow()+1){
						Board.targets.add(bc);
						continue;
					}
					else if(bc.getDoorDirection()==DoorDirection.UP&&cell.getRow()==bc.getRow()-1){
						Board.targets.add(bc);
						continue;
					}
					else if(bc.getDoorDirection()==DoorDirection.RIGHT&&cell.getColumn()==bc.getColumn()+1){
						Board.targets.add(bc);
						continue;
					}
					else if(bc.getDoorDirection()==DoorDirection.LEFT&&cell.getColumn()==bc.getColumn()-1){
						Board.targets.add(bc);
						continue;
					}
				}
				Board.visited.add(bc);
				if(pathLength==1){
					Board.targets.add(bc);
				}
				else{
					
					findAllTargets(bc,pathLength-1);
				}
				Board.visited.remove(bc);
			}
			
		}
	}
	
	public Set<BoardCell> getTargets() {
		return targets;
	}
	
	
	public BoardCell getCell(int row, int column) {
		return board[row][column];
	}

	public Set<BoardCell> getAdjList(int i, int j) {
		return adjMatrix.get(board[i][j]);
	}

	public void calcTargets(int i, int j, int k) {
		calcTargets(board[i][j],k);
	}
}
