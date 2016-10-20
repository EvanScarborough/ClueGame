package experiment;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import clueGame.BoardCell;
import clueGame.DoorDirection;

public class IntBoard {
	BoardCell[][] board = new BoardCell[4][4];
	HashMap<BoardCell, HashSet<BoardCell>> adjacencies;
	HashSet<BoardCell> targetCells;
	HashSet<BoardCell> visitedCells;
	
	public IntBoard() {
		for(int i=0;i<board.length;i++){
			for(int j=0;j<board[0].length;j++){
				board[i][j]=new BoardCell(i,j);
			}
		}
		calcAdjacencies();
	}
	
	public void calcAdjacencies() {
		adjacencies = new HashMap<BoardCell, HashSet<BoardCell>>();
		for(int i=0;i<board.length;i++){
			for(int j=0;j<board[0].length;j++){
				BoardCell cell = board[i][j];
				adjacencies.put(cell, new HashSet<BoardCell>());
				if(cell.getRow()>0){
					adjacencies.get(cell).add(board[cell.getRow()-1][cell.getColumn()]);
				}
				if(cell.getRow()<board.length-1){
					adjacencies.get(cell).add(board[cell.getRow()+1][cell.getColumn()]);
				}
				if(cell.getColumn()>0){
					adjacencies.get(cell).add(board[cell.getRow()][cell.getColumn()-1]);
				}
				if(cell.getColumn()<board[0].length-1){
					adjacencies.get(cell).add(board[cell.getRow()][cell.getColumn()+1]);
				}
			}
		}
	}
	
	public void calcTargets(BoardCell cell, int pathLength){
		targetCells = new HashSet<BoardCell>();
		visitedCells = new HashSet<BoardCell>();
		this.visitedCells.add(cell);
		findAllTargets(cell, pathLength);
	}
	
	public void findAllTargets(BoardCell cell, int pathLength){
		HashSet<BoardCell> adjCells = this.adjacencies.get(cell);
		for(BoardCell bc: adjCells){
			if(!this.visitedCells.contains(bc)&&!bc.isRoom()){
				if(bc.isDoorway()){
					if(bc.getDoorDirection()==DoorDirection.DOWN&&cell.getRow()==bc.getRow()+1){
						this.targetCells.add(bc);
						continue;
					}
					else if(bc.getDoorDirection()==DoorDirection.UP&&cell.getRow()==bc.getRow()-1){
						this.targetCells.add(bc);
						continue;
					}
					else if(bc.getDoorDirection()==DoorDirection.RIGHT&&cell.getColumn()==bc.getColumn()-1){
						this.targetCells.add(bc);
						continue;
					}
					else if(bc.getDoorDirection()==DoorDirection.LEFT&&cell.getColumn()==bc.getColumn()+1){
						this.targetCells.add(bc);
						continue;
					}
				}
				this.visitedCells.add(bc);
				if(pathLength==1){
					this.targetCells.add(bc);
				}
				else{
					
					findAllTargets(bc,pathLength-1);
				}
				this.visitedCells.remove(bc);
			}
			
		}
	}
	
	public Set<BoardCell> getTargets() {
		return targetCells;
	}
	
	
	public Set<BoardCell> getAdjList(BoardCell cell) {
		return adjacencies.get(cell);
	}

	public BoardCell getCell(int i, int j) {
		return board[i][j];
	}
}
