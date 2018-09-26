package main.java.game;

import java.io.Serializable;
import java.util.ArrayList;

public class Board implements Serializable{
//-----------------DATA MEMBERS---------------------------
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2607096484326456804L;
	protected int rowSize;
	protected int colSize;
	protected ArrayList<Word> words;
	protected Grid[][] board;

//------------------INSTANCE METHODS-----------------------

	public int getRowSize() {
		return this.rowSize;
	}
	public int getColSize() {
		return this.colSize;
	}
	
	public Grid getGrid(int row, int col) {
		return this.board[row][col];
	}
	
	public ArrayList<Word> getWords() {
		return this.words;
	}	
	
	@Override
	public String toString() {
		String retVal = "";
		String gridChar;
		for (int row = 0; row <rowSize; row++) {
			for (int col = 0; col < colSize; col++) {
				gridChar = board[row][col].getContent();
				if (gridChar == "") {
					retVal = retVal + "-";
				}else {
					retVal = retVal + gridChar;
				}
			}
			retVal = retVal + '\n';
		}
		return retVal;
	}
//-----------------------------------------------------------
	
}
