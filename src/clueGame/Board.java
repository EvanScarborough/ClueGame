package clueGame;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Board extends JPanel{
	private static Board theInstance = new Board();
	private static int numRows, numColumns;
	private static BoardCell[][] board;
	static Map<Character, String> rooms;
	private static Map<BoardCell, HashSet<BoardCell>> adjMatrix;
	private static HashSet<BoardCell> targets = new HashSet<BoardCell>();
	private static HashSet<BoardCell> visited = new HashSet<BoardCell>();
	private static String boardConfigFile;
	private static String roomConfig;
	
	public Solution theAnswer;
	public ArrayList<Player> players;
	public HumanPlayer human;
	public ArrayList<Card> cards;
	
	public static ArrayList<Card> weaponCards;
	public static ArrayList<Card> peopleCards;
	public static ArrayList<Card> roomCards;
	
	public static int MAX_BOARD_SIZE = 0;
	
	public int activePlayer = 0;
	
	static boolean isRealGame = false;
	static boolean gameFinished = false;

	private Board() {
		addMouseListener(new TargetListener());
	}
	
	public static Board getInstance() { 
		return theInstance; 	
	}
	
	public void setConfigFiles(String map, String legend) {
		Board.boardConfigFile = map;
		Board.roomConfig = legend;
		Board.rooms = new HashMap<Character, String>();
	}
	
	public void initialize() {
		peopleCards = new ArrayList<Card>();
		roomCards = new ArrayList<Card>();
		weaponCards = new ArrayList<Card>();
		try {
			File fileinLayout = new File(boardConfigFile);
			File fileinLegend = new File(roomConfig);
			Scanner scLayout = new Scanner(fileinLayout);
			Scanner scLegend = new Scanner(fileinLegend);
			
			cards = new ArrayList<Card>();
			 
			while (scLegend.hasNextLine()) {
				String str = scLegend.nextLine();
				String[] lineArray = str.split(", ");
				Board.rooms.put(lineArray[0].charAt(0), lineArray[1]);
				if(lineArray[2].equals("Card")){
					roomCards.add(new Card(lineArray[1],CardType.ROOM));
					cards.add(roomCards.get(roomCards.size()-1));
				}
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
						}
						else if (cellTypes.get(cellNum).charAt(1) == 'R') {
							board[i][j].setDoorDirection(DoorDirection.RIGHT);
						}
						else if (cellTypes.get(cellNum).charAt(1) == 'D') {
							board[i][j].setDoorDirection(DoorDirection.DOWN);
						}
						else if (cellTypes.get(cellNum).charAt(1) == 'L') {
							board[i][j].setDoorDirection(DoorDirection.LEFT);
						}
						else if (cellTypes.get(cellNum).charAt(1) == 'N') {
							board[i][j].setType(BoardCell.CellType.ROOM);
							board[i][j].hasTitle = true;
						}
					}
					else if (cellTypes.get(cellNum).charAt(0) == 'W') {
						board[i][j].setType(BoardCell.CellType.WALKWAY);
					}
					else {
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
		adjMatrix = new HashMap<BoardCell,HashSet<BoardCell>>();
		for(int i = 0; i < numRows; i++){
			for(int j = 0; j < numColumns; j++){
				adjMatrix.put(getCellAt(i,j), new HashSet<BoardCell>());
				
				if(getCellAt(i,j).getDoorDirection() == DoorDirection.DOWN){
					adjMatrix.get(getCellAt(i,j)).add(getCellAt(i+1,j));
					continue;
				}
				if(getCellAt(i,j).getDoorDirection() == DoorDirection.UP){
					adjMatrix.get(getCellAt(i,j)).add(getCellAt(i-1,j));
					continue;
				}
				if(getCellAt(i,j).getDoorDirection() == DoorDirection.LEFT){
					adjMatrix.get(getCellAt(i,j)).add(getCellAt(i,j-1));
					continue;
				}
				if(getCellAt(i,j).getDoorDirection() == DoorDirection.RIGHT){
					adjMatrix.get(getCellAt(i,j)).add(getCellAt(i,j+1));
					continue;
				}
				if(getCellAt(i,j).getInitial() != 'W'){
					continue;
				}
				if(isValid(i-1,j)){ //Cell Above
					if((getCellAt(i-1,j).getInitial() == 'W' || getCellAt(i-1,j).getDoorDirection() == DoorDirection.DOWN)){
						adjMatrix.get(getCellAt(i,j)).add(getCellAt(i-1,j));
					}
				}
				if(isValid(i+1,j)){ //Cell Below
					if((getCellAt(i+1,j).getInitial() == 'W' || getCellAt(i+1,j).getDoorDirection() == DoorDirection.UP)){
						adjMatrix.get(getCellAt(i,j)).add(getCellAt(i+1,j));
					}
				}
				if(isValid(i,j-1)){ //To the Left
					if((getCellAt(i,j-1).getInitial() == 'W' || getCellAt(i,j-1).getDoorDirection() == DoorDirection.RIGHT)){
						adjMatrix.get(getCellAt(i,j)).add(getCellAt(i,j-1));
					}
				}
				if(isValid(i,j+1)){ //To the Right
					if((getCellAt(i,j+1).getInitial() == 'W' || getCellAt(i,j+1).getDoorDirection() == DoorDirection.LEFT)){
						adjMatrix.get(getCellAt(i,j)).add(getCellAt(i,j+1));
					}
				}
				
			}
		}
	}
	private boolean isValid(int y, int x){
		return x >= 0 && y >= 0 && y < numRows && x < numColumns;
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
	
	public void selectAnswer(){
		theAnswer = new Solution("","","");
		
		Collections.shuffle(peopleCards);
		Collections.shuffle(peopleCards);
		Collections.shuffle(roomCards);
		Collections.shuffle(roomCards);
		Collections.shuffle(weaponCards);
		Collections.shuffle(weaponCards);
		
		theAnswer.person = peopleCards.get(0).getName();
		theAnswer.room = roomCards.get(0).getName();
		theAnswer.weapon = weaponCards.get(0).getName();
		
		Collections.shuffle(peopleCards);
		Collections.shuffle(peopleCards);
		Collections.shuffle(roomCards);
		Collections.shuffle(roomCards);
		Collections.shuffle(weaponCards);
		Collections.shuffle(weaponCards);
	}
	
	public Card handleSuggestion(int playerIndex, Solution s){
		//System.out.println("Start at " + playerIndex);
		for(int i = playerIndex + 1; i < playerIndex + 6; i++) {
			int index = i % 6;
			//System.out.println(index);
			Card c = players.get(index).disproveSuggestion(s);
			if (c != null) {
				return c;
			}
		}
		mostRecentDisprove = "Nobody can disprove!";
		if(isRealGame) ClueGUI.response.setText("Nobody can disprove!");
		return null;
	}
	
	public boolean checkAccusation(Solution accusation){
		if(theAnswer.isSame(accusation)) return true;
		return false;
	}
	
	public void loadConfigFiles(String playerFile, String weaponFile){
		players = new ArrayList<Player>();
		
		try {
			File pFile = new File(playerFile);
			File wFile = new File(weaponFile);
			Scanner pfin = new Scanner(pFile);
			Scanner wfin = new Scanner(wFile);
			
			for(int i = 0; i < 6; i++){
				String n = pfin.nextLine();
				if(i!=0)players.add(new ComputerPlayer(n));
				else{
					human = new HumanPlayer(n);
					players.add(human);
				}
				peopleCards.add(new Card(n,CardType.PERSON));
				cards.add(peopleCards.get(peopleCards.size()-1));
			}
			for(int i = 0; i < 6; i++){
				String c = pfin.nextLine();
				String[] rgb = c.split(" ");
				players.get(i).setColor(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]));
			}
			for(int i = 0; i < 6; i++){
				String p = pfin.nextLine();
				String[] yx = p.split(" ");
				players.get(i).setPosition(Integer.parseInt(yx[0]), Integer.parseInt(yx[1]));
			}
			while(wfin.hasNextLine()) {
				String w = wfin.nextLine();
				weaponCards.add(new Card(w,CardType.WEAPON));
				cards.add(weaponCards.get(weaponCards.size()-1));
			}
			
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		
		activePlayer = players.size() - 1;
		
	}
	
	public void dealCards(){
		selectAnswer();
		Collections.shuffle(cards);
		Collections.shuffle(cards);
		
		int pgive = 0;
		while(cards.size() > 0){
			if (theAnswer.person == cards.get(0).getName() || theAnswer.room == cards.get(0).getName() || theAnswer.weapon == cards.get(0).getName()) {
				cards.remove(0);
				continue;
			}
			players.get(pgive % 6).giveCard(cards.get(0));
			cards.remove(0);
			pgive++;
		}
	}
	
	Random rand = new Random();
	public Solution mostRecentSolution;
	public String mostRecentDisprove = "";
	public int roll;
	
	public void nextPlayer(){
		activePlayer++;
		if(activePlayer >= players.size()){
			activePlayer = 0;
		}
		roll = rand.nextInt(6) + 1;
		calcTargets(players.get(activePlayer).row,players.get(activePlayer).column,roll);
		
		if(activePlayer != 0){
			if(players.get(activePlayer).accusation != null){
				handleAccusation();
				players.get(activePlayer).accusation = null;
				return;
				//String end = "GAME OVER!\n" + players.get(activePlayer).getName() + " won the game. The answer was\n" + theAnswer.toString();
				//JOptionPane.showMessageDialog(new JFrame(), end, "Game Finished", JOptionPane.INFORMATION_MESSAGE);
			}
			
			mostRecentSolution = players.get(activePlayer).takeTurn(targets);
			if(mostRecentSolution != null){
				teleportPlayer();
			}
		}
		else{
			human.turnState = 1;
		}
		
		
		repaint();
	}
	
	void handleAccusation(){
		String accus = players.get(activePlayer).getName() + " made an accusation:\n" + players.get(activePlayer).accusation.toString();
		JOptionPane.showMessageDialog(new JFrame(), accus, "Accusation", JOptionPane.INFORMATION_MESSAGE);
		if(checkAccusation(players.get(activePlayer).accusation)){
			String end = "GAME OVER!\n" + players.get(activePlayer).getName() + " won the game. The answer was\n" + theAnswer.toString();
			JOptionPane.showMessageDialog(new JFrame(), end, "Game Finished", JOptionPane.INFORMATION_MESSAGE);
			gameFinished = true;
			setVisible(false);
		}
	}
	
	void handleAccusation(Solution solution){
		if(checkAccusation(solution)){
			String end = "YOU WIN!\nYou guessed correctly! The answer was\n" + theAnswer.toString();
			JOptionPane.showMessageDialog(new JFrame(), end, "Game Finished", JOptionPane.INFORMATION_MESSAGE);
			gameFinished = true;
			setVisible(false);
		}
		else{
			String end = "That isn't correct.";
			JOptionPane.showMessageDialog(new JFrame(), end, "Nope", JOptionPane.INFORMATION_MESSAGE);
			repaint();
		}
	}
	
	public int mouseX = -1;
	public int mouseY = -1;
	public boolean targClick = false;
	
	public class TargetListener implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent e) {
			if(human.turnState == 0) return;
			
			int yp = e.getY() / BoardCell.TILE_SIZE;
			int xp = e.getX() / BoardCell.TILE_SIZE;
			
			//System.out.println(yp + " " + xp);
			if(!isValid(yp,xp)){
				return;
			}
			BoardCell clickedOn = getCell(yp, xp);
			if(!targets.contains(clickedOn)){
				JOptionPane.showMessageDialog(new JFrame(), "Invalit Target!!", "Select a target", JOptionPane.INFORMATION_MESSAGE);
			}
			else{
				if(human.takeTurn(clickedOn)) human.turnState = 2;
				else human.turnState = 0;
				repaint();
				
			}
		}
		public void mousePressed(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		
	}
	
	public void teleportPlayer(){
		Card c = handleSuggestion(activePlayer, mostRecentSolution);
		if(c != null){
			mostRecentDisprove = c.getName();
			for(Player p: players){
				p.giveCard(c);
				if(p.getName().equals(mostRecentSolution.person)){
					p.setPosition(players.get(activePlayer).row, players.get(activePlayer).column);
				}
			}
		}
		else{
			ClueGUI.response.setText("Nobody can disprove!");
			if(activePlayer != 0 && checkAccusation(mostRecentSolution)){
				players.get(activePlayer).accusation = mostRecentSolution;
				//String end = "GAME OVER!\n" + players.get(activePlayer).getName() + " won the game. The answer was\n" + theAnswer.toString();
				//JOptionPane.showMessageDialog(new JFrame(), end, "Game Finished", JOptionPane.INFORMATION_MESSAGE);
			}
		}
		ClueGUI.response.setText(mostRecentDisprove);
		
		
		repaint();
	}
	
	
	
	

	public void paintComponent(Graphics g){
		super.paintComponent(g);
		for(int i = 0; i < numRows; i++){
			for(int j = 0; j < numColumns; j++){
				//System.out.println("cat");
				if(human.turnState == 1 && targets.contains(getCell(i,j))){
					getCell(i,j).isTarget = true;
				}
				else{
					getCell(i,j).isTarget = false;
				}
				getCell(i,j).draw(g);
			}
		}
		for(int i = 0; i < players.size(); i++){
			players.get(i).draw(g);
		}
	}
	
}
