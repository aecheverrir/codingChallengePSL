package minesweeper;

public class Cell {
	
	
	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	private boolean mined;
	
	private boolean hidden; 
	
	private boolean flagged;
	
	// should be either: "1", "2", "3", "4", "5", "6", "7", "8", "9", "-", "*"
	private String state;
	
	private int row;
	
	private int column;
	
	public Cell(int pRow, int pColumn) {	
		state = "-";
		mined = false;
		hidden = true;
		flagged = false;
		row = pRow;
		column = pColumn;
	}

	public boolean isMined() {
		return mined;
	}

	public void setMined(boolean mined) {
		this.mined = mined;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public boolean isFlagged() {
		return flagged;
	}

	public void setFlagged(boolean flagged) {
		this.flagged = flagged;
	}
	
	

}
