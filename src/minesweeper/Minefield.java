package minesweeper;

import java.util.UnknownFormatConversionException;

public class Minefield {


	public enum GameState{
		WON,
		LOST,
		INGAME;	
	}
	
	private GameState gamestate;
	
	private Cell[][] gameboard;
	
	private int rows;
	
	private int columns;
	
	private int mines;
	
	private int minesFlagged;
	
	
	public Minefield(int pAmountOfColumns, int pAmountOfRows, int pAmountOfMines) {
		
		rows = pAmountOfRows;
		columns = pAmountOfColumns;
		mines = pAmountOfMines;
		gameboard = new Cell[rows][columns];
		gamestate = GameState.INGAME;
		minesFlagged = 0;
		
		setField();
		placeMinesInField();
		setStates();
		
	}

	public void setStates() {
		
		for( int i = 0; i < rows; i++ )
        {
            for( int j = 0; j < columns; j++ )
            {
                Cell currentCell = gameboard[ i ][ j ] ;
                currentCell.setState(getCellState(i,j)); 
            }
        }
		
	}

	public String getCellState(int pRow,int pCol) {
		
		int amountMinesAround = 0;
		Cell currentCell = gameboard[ pRow ][ pCol ] ;
		
		//searches
		if(!currentCell.isMined()) {
			
			//top left
			if(pRow > 0 && pCol > 0) {
				if(gameboard[ pRow-1 ][ pCol-1 ].isMined()) {
					amountMinesAround ++;
				}
			}
			
			//top
			if(pCol > 0) {
				if(gameboard[ pRow ][ pCol-1 ].isMined()) {
					amountMinesAround ++;
				}
			}
			
			//top right
			if(pCol > 0 && pRow < rows-1) {
				if(gameboard[ pRow+1 ][ pCol-1 ].isMined()) {
					amountMinesAround ++;
				}
			}
			
			//left
			if(pRow > 0) {
				if(gameboard[ pRow-1 ][ pCol ].isMined()) {
					amountMinesAround ++;
				}
			}
			
			//right
			if(pRow < rows-1) {
				if(gameboard[ pRow+1 ][ pCol ].isMined()) {
					amountMinesAround ++;
				}
			}
			
			
			//bottom left
			if(pRow > 0 && pCol < columns-1) {
				if(gameboard[ pRow-1 ][ pCol+1 ].isMined()) {
					amountMinesAround ++;
				}
			}
			
			//bottom
			if(pCol < columns-1) {
				if(gameboard[ pRow ][ pCol+1 ].isMined()) {
					amountMinesAround ++;
				}
			}
			
			//bottom right
			if(pRow < rows-1 && pCol < columns-1) {
				if(gameboard[ pRow+1 ][ pCol+1 ].isMined()) {
					amountMinesAround ++;
				}
			}
			
			if (amountMinesAround > 0) {
				return ""+amountMinesAround;
			}
			else{
				return "-";
			}	
			
		}
		else {
			return "*";
		}
		
	}

	public void setField() {
		
		for( int i = 0; i < rows; i++ )
        {
            for( int j = 0; j < columns; j++ )
            {
                gameboard[ i ][ j ] = new Cell(i, j);
            }
        }
		
	}

	public void placeMinesInField() {
		
		for (int i = 0; i < mines; i++) 
		{
			boolean placed = false;
			while(!placed) {
				int colAtt = (int) (Math.random() * ( columns ));
				int rowAtt = (int) (Math.random() * ( rows ));
				
				Cell cellAttempt = gameboard[rowAtt][colAtt] ;
				if(!cellAttempt.isMined()) {
					cellAttempt.setMined(true);
					cellAttempt.setState("*");
					placed = true;
				}
			}
			
		}
		
	}
	
	public boolean makeMove(int pColumn, int pRow, String pAction) {
			
		Cell currentCell = gameboard[pRow][pColumn];
		if(!currentCell.isHidden()) {
			return false;
		}
		//uncovers mine, looses
		if(pAction.equals("U") && currentCell.isMined()) {
			gamestate = GameState.LOST;
			uncoverAllMines();
		}
		//Flags
		//removes flag from a mined cell
		else if(pAction.equals("P") && currentCell.isMined() && currentCell.isFlagged()) {
			currentCell.setFlagged(false);
			minesFlagged --;
		}
		//removes flag from a unmined cell
		else if(pAction.equals("P") && !currentCell.isMined() && currentCell.isFlagged()) {
			currentCell.setFlagged(false);
			minesFlagged ++;
		}
		//places flag on an mined cell
		else if(pAction.equals("P") && currentCell.isMined() && !currentCell.isFlagged()) {
			currentCell.setFlagged(true);
			minesFlagged ++;
		}
		//places flag on an unmined cell
		else if(pAction.equals("P") && !currentCell.isMined() && !currentCell.isFlagged()) {
			currentCell.setFlagged(true);
			minesFlagged --;
		}
		//Uncovers number
		else if(pAction.equals("U") && !currentCell.isMined() && !currentCell.isFlagged() && !currentCell.getState().equals("-")) {
			currentCell.setHidden(false);
		}
		//Uncovers disabled/empty cell
		else if(pAction.equals("U") && !currentCell.isMined() && !currentCell.isFlagged() && currentCell.getState().equals("-")) {
			uncoverDisabledCell(currentCell);
		}
				
		if(minesFlagged == mines) {
			gamestate = GameState.WON;
		}
		return true;
		
	}
	
	private void uncoverDisabledCell(Cell currentCell) {
		
		currentCell.setHidden(false);
		int pRow = currentCell.getRow();
		int pCol = currentCell.getColumn();

		//top left
		if(pRow > 0 && pCol > 0 ) {
			Cell checkCell = gameboard[ pRow-1 ][ pCol-1 ];
			if(checkCell.getState().equals("-") && checkCell.isHidden() && !checkCell.isFlagged()) {
				uncoverDisabledCell(checkCell);
			}
			else if(!checkCell.isFlagged()){
				checkCell.setHidden(false);
			}
		}
		
		//top
		if(pCol > 0) {
			Cell checkCell = gameboard[ pRow ][ pCol-1 ];
			if(checkCell.getState().equals("-") && checkCell.isHidden() && !checkCell.isFlagged()) {
				uncoverDisabledCell(checkCell);
			}
			else if(!checkCell.isFlagged()){
				checkCell.setHidden(false);
			}
		}
		
		//top right
		if(pCol > 0 && pRow < rows-1 ) {
			Cell checkCell = gameboard[ pRow+1 ][ pCol-1 ];
			if(checkCell.getState().equals("-") && checkCell.isHidden() && !checkCell.isFlagged()) {
				uncoverDisabledCell(checkCell);
			}
			else if(!checkCell.isFlagged()){
				checkCell.setHidden(false);
			}
		}
		
		//left
		if(pRow > 0) {
			Cell checkCell = gameboard[ pRow-1 ][ pCol ];
			if(checkCell.getState().equals("-") && checkCell.isHidden() && !checkCell.isFlagged()) {
				uncoverDisabledCell(checkCell);
			}
			else if(!checkCell.isFlagged()){
				checkCell.setHidden(false);
			}
		}
		
		//right
		if(pRow < rows-1) {
			Cell checkCell = gameboard[ pRow+1 ][ pCol ];
			if(checkCell.getState().equals("-") && checkCell.isHidden() && !checkCell.isFlagged()) {
				uncoverDisabledCell(checkCell);
			}
			else if(!checkCell.isFlagged()){
				checkCell.setHidden(false);
			}
		}
		
		
		//bottom left
		if(pRow > 0 && pCol < columns-1) {
			Cell checkCell = gameboard[ pRow-1 ][ pCol+1 ];
			if(checkCell.getState().equals("-") && checkCell.isHidden() && !checkCell.isFlagged()) {
				uncoverDisabledCell(checkCell);
			}
			else if(!checkCell.isFlagged()){
				checkCell.setHidden(false);
			}
		}
		
		//bottom
		if(pCol < columns-1) {
			Cell checkCell = gameboard[ pRow ][ pCol+1 ];
			if(checkCell.getState().equals("-") && checkCell.isHidden() && !checkCell.isFlagged()) {
				uncoverDisabledCell(checkCell);
			}
			else if(!checkCell.isFlagged()){
				checkCell.setHidden(false);
			}
		}
		
		//bottom right
		if(pRow < rows-1 && pCol < columns-1) {
			Cell checkCell = gameboard[ pRow+1 ][ pCol+1 ];
			if(checkCell.getState().equals("-") && checkCell.isHidden() && !checkCell.isFlagged()) {
				uncoverDisabledCell(checkCell);
			}
			else if(!checkCell.isFlagged()){
				checkCell.setHidden(false);
			}
		}
		
	}

	public void uncoverAllMines() {
		
		for( int i = 0; i < rows; i++ )
        {
            for( int j = 0; j < columns; j++ )
            {
            	if(gameboard[i][j].isMined()) {
            		gameboard[ i ][ j ].setHidden(false);
            	}
                
            }
        }
		
	}

	public void printboard() {
		
		for (int i = 0; i < rows; i++) {
			
			String lineToPrint = "";
					
			for (int j = 0; j < columns; j++) {
				Cell currentCell = gameboard[i][j];
				if(currentCell.isHidden() && !currentCell.isFlagged()){
					lineToPrint += " .";
				}
				else if(currentCell.isFlagged()){
					lineToPrint += " P";
				}
				else {
					lineToPrint += (" " + currentCell.getState());
				}
			}
			
			System.out.println(lineToPrint);
		}
		
	}
	
	
	public GameState getGamestate() {
		return gamestate;
	}


	public void setGamestate(GameState gamestate) {
		this.gamestate = gamestate;
	}


	public Cell[][] getGameboard() {
		return gameboard;
	}


	public void setGameboard(Cell[][] gameboard) {
		this.gameboard = gameboard;
	}


	public int getRows() {
		return rows;
	}


	public void setRows(int rows) {
		this.rows = rows;
	}


	public int getColumns() {
		return columns;
	}


	public void setColumns(int columns) {
		this.columns = columns;
	}


	public int getMines() {
		return mines;
	}


	public void setMines(int mines) {
		this.mines = mines;
	}
	
	
}
