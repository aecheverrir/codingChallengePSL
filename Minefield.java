import java.util.ArrayList;
import java.util.List;

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
			uncoverDisabledCell(currentCell.getRow(), currentCell.getColumn());
		}
				
		if(minesFlagged == mines) {
			gamestate = GameState.WON;
		}
		return true;
		
	}
	
	private void uncoverDisabledCell(int pRow, int pCol) {
        List<Cell> res = new ArrayList<Cell>();
        Cell startCell = gameboard[pRow][pCol]; 
        res.add(startCell);

        while(res.size() > 0) {
          int count = res.size();
          Cell currentCell = res.get(count-1);
          int row = currentCell.getRow();
          int col = currentCell.getColumn();
          res.remove(count-1);

          if(!currentCell.isHidden()) {
            continue;
          }
          currentCell.setHidden(false);

          if(currentCell.getState().equals("-") && !currentCell.isFlagged()) {
            if(check(row-1, col-1) && gameboard[row-1][col-1].isHidden()) {
              res.add(gameboard[row-1][col-1]);
            }

            if(check(row, col-1) && gameboard[row][col-1].isHidden()) {
              res.add(gameboard[row][col-1]);;
            }

            if(check(row+1, col-1) && gameboard[row+1][col-1].isHidden()) {
              res.add(gameboard[row+1][col-1]);
            }

            if(check(row-1, col+1) && gameboard[row-1][col+1].isHidden()) {
              res.add(gameboard[row-1][col+1]);
            }

            if(check(row, col+1) && gameboard[row][col+1].isHidden()) {
              res.add(gameboard[row][col+1]);
            }

            if(check(row+1, col+1) && gameboard[row+1][col+1].isHidden()) {
              res.add(gameboard[row+1][col+1]);
            }

            if(check(row+1, col) && gameboard[row+1][col].isHidden()) {
              res.add(gameboard[row+1][col]);
            }

            if(check(row-1, col) && gameboard[row-1][col].isHidden()) {
              res.add(gameboard[row-1][col]);
            }
          }
        }
    }
	
	public boolean check(int pRow, int pCol) {
		return (pRow >= 0 && pRow < rows && pCol >= 0 && pCol < columns);
	}
	
	public void printN(int pRow,int pCol) {
		System.out.println("Buenas\n\n\n");
		System.out.print(gameboard[pRow-1][pCol-1].getState()); // Arriba izq
		System.out.print(gameboard[pRow-1][pCol].getState()); // Arriba
		System.out.print(gameboard[pRow-1][pCol+1].getState() + "\n"); // Arriba Der

		System.out.print(gameboard[pRow][pCol-1].getState()); // Izq
		System.out.print(gameboard[pRow][pCol].getState()); // YO
		System.out.print(gameboard[pRow][pCol+1].getState() +"\n"); // Der
		
		System.out.print(gameboard[pRow+1][pCol-1].getState()); // Abajo Izq
		System.out.print(gameboard[pRow+1][pCol].getState()); // Abajo		
		System.out.print(gameboard[pRow+1][pCol+1].getState() + "\n"); // Abajo Der

		System.out.println("Buenas\n\n\n");
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
