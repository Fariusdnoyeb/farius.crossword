package main.java.game;

public class Grid {
//-----------------DATA MEMBERS---------------------------
	public static final int INVALID = -1;
	
	private char content;
	private boolean isBlack;
	
	private int gridRow; //row index on board
	private int gridCol; //col index on board
	private Board board;
	
	private Word vWord; //vertical
	private Word hWord; //horizontal
	private int vIndex; //index in vWord
	private int hIndex; //index in hWord

//-------------------CONSTRUCTORS--------------------------
	public Grid(Board board, int row, int col) {
		vWord = hWord = null;
		vIndex = hIndex = INVALID;
		content = '\0';

		this.board = board;
		gridRow = row;
		gridCol = col;
	}
//------------------INSTANCE METHODS-----------------------
	public int getGridRow() {
		return this.gridRow;
	}
	
	public int getGridCol() {
		return this.gridCol;
	}
	
	public boolean isBlack() {
		return this.isBlack;
	}
	
	public void setBlack(boolean isBlack) {
		this.isBlack = isBlack;
	}
	
	public char getContent() {
		return this.content;
	}
	
	public void setContent(char character) {
		this.content = Character.toUpperCase(character);
	}
	
	public Word getHWord() {
		return this.hWord;
	}
	public Word getVWord() {
		return this.vWord;
	}
	
	public int getHIndex() {
		return this.hIndex;
	}
	public int getVIndex() {
		return this.vIndex;
	}
	
	public void setWord(Word word, int index, Orientation orien) {
		switch (orien) {
		case HORIZONTAL:
			hWord = word;
			hIndex = index;
			break;
		case VERTICAL:
			vWord = word;
			vIndex = index;
			break;
		default:
			break;
		}
	}

//---------------------------------------------------------
	
}
