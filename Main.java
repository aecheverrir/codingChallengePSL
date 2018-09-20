import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
	
	public enum GameState{
		WON,
		LOST,
		INGAME;	
	}

	public static void main(String[] args) throws IOException {
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		while (true) {
		
			//data needed to start a gameboard
			int numColumns = -1;
			int numRows = -1;
			int numMines = -1;
			
			//obtains number of columns
			System.out.println("Enter the amount of columns you want to play with");
			boolean validInput = false;
			while (!validInput) {
				String input = br.readLine();
				validInput = validateColumnsInput(input);
				if(validInput) {
					numColumns = Integer.parseInt(input);
				}
			}
			
			//obtains number of rows
			System.out.println("Enter the amount of rows you want to play with");
			validInput = false;
			while (!validInput) {
				String input = br.readLine();
				validInput = validateRowsInput(input);
				if(validInput) {
					numRows = Integer.parseInt(input);
				}
			}
			
			//obtains number of mines
			System.out.println("Enter the amount of mines you want to play with");
			validInput = false;
			while (!validInput) {
				String input = br.readLine();
				validInput = validateMinesInput(input, numColumns*numRows );
				if(validInput) {
					numMines = Integer.parseInt(input);
				}
			}
			
			// --- starts game ----
			boolean inGame = true;
			Minefield minefield = new Minefield(numColumns, numRows, numMines);
			
			System.out.println("");
			System.out.println(" GAME READY ");
			System.out.println("");
			minefield.printboard();
					
			while (inGame) {
				
				System.out.println("");
				System.out.println("Make a move or type 'help' for list of possible commands and instructions" );
				
				
				validInput = false;
				while (!validInput) {
					
					String input = br.readLine();
					//checks if command is correctly typed
					validInput = validatePlayInput(input, numRows, numColumns);
					
					//checks if input is possible in the gameboard
					if(validInput) {
						
						String[] inputValues = input.split(",");
						int inputCol = Integer.parseInt(inputValues[0])-1;
						int inputRow = Integer.parseInt(inputValues[1])-1;
						String command = inputValues[2];
						
						validInput = minefield.makeMove(inputCol, inputRow, command);
						if(!validInput) {
							System.out.println("Please select a valid cell. They are represented with a '.' ");					
						}
						else {
							minefield.printboard();
							if ( minefield.getGamestate() == GameState.WON) {
								System.out.println("YOU WON!!! ");
								System.out.println("Enter anything to start a new game");
								String any = br.readLine();
								inGame = false;
							}
							else if( minefield.getGamestate() == GameState.LOST) {
								System.out.println("YOU LOST");
								System.out.println("Enter anything to start a new game");
								String any = br.readLine();
								inGame = false;
							}
						}
					}
				}			
			}
		}
	}

	public static boolean validatePlayInput(String pInput, int pRows, int pColumns) {
		
		try{
			
			if(pInput.equals("help")) {
				
				//help command
				System.out.println("");
				System.out.println(" - In order to uncover a cell type its row, column and it's command in the following format: <rowNumber>,<columnNumber>,U");
				System.out.println("   Example for uncovering cell on row #10 column #5: '10,5,U' ");
				System.out.println("");
				System.out.println(" - In order to flag a cell type its row, column and it's command in the following format: <rowNumber>,<columnNumber>,P");
				System.out.println("   Example for placing a flag on a cell at row #10 column #5: '10,5,P' ");
				
				return false;
			}
			else {
				
				String[] inputValues = pInput.split(",");
				int rowPlayed = Integer.parseInt(inputValues[0]);
				int colPlayed = Integer.parseInt(inputValues[1]);
				String moveType = inputValues[2];

				boolean wrongInput = false;
			    if(rowPlayed < 1 || rowPlayed > pRows ){
			    	wrongInput = true;
			    	System.out.println(rowPlayed + " is out of bounds, choose a number between 1 and " + pRows );    
			    }
				if(colPlayed < 1 || colPlayed > pColumns ) {
					wrongInput = true;
					System.out.println(colPlayed + " is out of bounds, choose a number between 1 and " + pColumns );
				}
				if( !(moveType.equals("P") || moveType.equals("U")) ) {
					wrongInput = true;
	    			System.out.println("The type of command used is invalid, possible commands are 'U' for uncovering cells and 'P' for flagging cells" );
			    }
				return !wrongInput;
				
			}
		    	
		}
		catch (Exception ex) {
			System.out.println("Enter a valid move, type 'help' for possible moves and instructions");
			return false;
		}
		
	}
	
	public static boolean validateRowsInput(String pInput) {
		
		try{
			
		    int numRows = Integer.parseInt(pInput);
		    if(numRows > 0 && numRows < 101 )
		    {
		    	System.out.println("--- Number of rows: " + numRows + " --- ");
		    	return true;
		    }
		    else {
		    	System.out.println(numRows+" is not between 1 and 100");
		    	return false;
		    }	
		    	
		}
		catch (NumberFormatException ex) {
		    //handle exception here
			System.out.println("Enter a numeric value between 1 and 100");
	    	return false;

		}
		
	}
	
	public static boolean validateColumnsInput(String pInput) {
		
		try{
			
			int numColumns = Integer.parseInt(pInput);
			
		    if(numColumns > 0 && numColumns < 101 )
		    {
		    	
		    	System.out.println("--- Number of columns: " + numColumns + " --- ");
		    	return true;
		    }
		    else {
		    	System.out.println(numColumns+" is not between 1 and 100");
		    	return false;
		    }	 	
			    	
		}
		catch (NumberFormatException ex) {
		    //handle exception here
			System.out.println("Enter a numeric value between 1 and 100");
			return false;
		}
		
	}
	
	public static boolean validateMinesInput(String pInput, int maxNumberMines) {
		
		
		try{
			int numMines = Integer.parseInt(pInput);
		    if(numMines > 0 && numMines <= (maxNumberMines) )
		    {
		    	System.out.println("--- Number of mines: " + numMines + " --- ");
		    	return true;
		    }
		    else {
		    	System.out.println("Your minefield has " + maxNumberMines +  " cells, "+ numMines +" mines will not fit in the gameboard. Type a value between 1 and "+ maxNumberMines);
		    	return false;
		    }	
		    	
		}
		catch (NumberFormatException ex) {
		    //handle exception here
			System.out.println("Enter a numeric value between 1 and " + maxNumberMines);
			return false;
		}
		
	}
}
