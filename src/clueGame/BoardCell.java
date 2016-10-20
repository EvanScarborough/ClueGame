package clueGame;
/**
 * 
 * @author Michael Balmes and Bixler Baker
 *
 */
public class BoardCell{
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
	}
	
	public BoardCell(int row, int column, CellType type) {
		this.row = row;
		this.column = column;
		this.type = type;
	}
	
	public BoardCell(int row, int column, DoorDirection doorDirection) {
		this.row = row;
		this.column = column;
		this.type = CellType.DOORWAY;
		this.doorDirection=doorDirection;
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

	public Object getInitial() {
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
}
