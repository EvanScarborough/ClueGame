package clueGame;

import java.awt.Color;
import java.awt.Graphics;

/**
 * 
 * @author Michael Balmes and Bixler Baker
 *
 */
public class BoardCell{
	
	public static final int TILE_SIZE = 24;
	public boolean hasTitle = false;
	
	
	
	
	public enum CellType{
		WALKWAY,ROOM,DOORWAY
	}
	private int row, column;
	private char initial;
	private CellType type;
	private DoorDirection doorDirection;
	public BoardCell(int row, int column) {
		this.row = row;
		this.column = column;
		this.type=CellType.WALKWAY;
		hasTitle = false;
	}
	
	public BoardCell(int row, int column, CellType type) {
		this.row = row;
		this.column = column;
		this.type = type;
		hasTitle = false;
	}
	
	public BoardCell(int row, int column, DoorDirection doorDirection) {
		this.row = row;
		this.column = column;
		this.type = CellType.DOORWAY;
		this.doorDirection=doorDirection;
		hasTitle = false;
	}
	
	public int Row() { return row; }
	
	public int Column() { return column; }
	
	public void setRow(int row) { this.row = row; }
	
	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) { this.row = column; }
	
	
	@Override
	public boolean equals(Object o) {
		return ((BoardCell) o).getRow()==this.getRow()&&((BoardCell) o).getColumn()==this.getColumn();
	}
	
	public String toString(){
		return "["+this.getRow()+","+this.getColumn()+"]";
	}
	
	@Override
	public int hashCode(){
		return (this.getRow()+1)*7+(this.getColumn()+1)*14;
	}
	
	public boolean isWalkway(){
		return type==CellType.WALKWAY;
	}
	
	public boolean isRoom(){
		return type==CellType.ROOM;
	}
	
	public boolean isDoorway(){
		return type==CellType.DOORWAY;
	}

	public DoorDirection getDoorDirection() {
		return this.doorDirection;
	}

	public char getInitial() {
		return this.initial;
	}
	
	public void setType(CellType type){
		this.type = type;
	}
	
	public void setInitial(char a){
		this.initial = a;
	}
	
	public void setDoorDirection(DoorDirection d) {
		this.doorDirection = d;
	}
	
	public boolean isTarget = false;
	public void draw(Graphics g){
		if(isWalkway()){
			g.setColor(new Color(16,226,164));
			if(isTarget) g.setColor(new Color(0,150,164));
			g.fillRect(column * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
			g.setColor(Color.BLACK);
			g.drawRect(column * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
			//System.out.println("cat");
		}
		if(hasTitle){
			g.setColor(Color.WHITE);
			g.drawString(Board.rooms.get(initial), column * TILE_SIZE, row * TILE_SIZE);
		}
		if(isDoorway()){
			if(isTarget){
				g.setColor(new Color(0,150,164));
				g.fillRect(column * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
				g.setColor(Color.BLACK);
				g.drawRect(column * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
			}
			g.setColor(Color.WHITE);
			switch(doorDirection){
			case DOWN:
				g.fillRect(column*TILE_SIZE, row * TILE_SIZE + 5*TILE_SIZE/6, TILE_SIZE, TILE_SIZE/6);
				
				break;
			case UP:
				g.fillRect(column*TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE/6);

				break;
			case LEFT:
				g.fillRect(column*TILE_SIZE, row * TILE_SIZE, TILE_SIZE/6, TILE_SIZE);

				break;
			case RIGHT:
				g.fillRect(column*TILE_SIZE + 5*TILE_SIZE/6, row * TILE_SIZE, TILE_SIZE/6, TILE_SIZE);

				break;
			}
		}
		
	}
}
