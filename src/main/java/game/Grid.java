package main.java.game;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Grid {
//-----------------DATA MEMBERS---------------------------
	public static final int INVALID = -1;
	
	private final BooleanProperty isAdded = new SimpleBooleanProperty(this, "isAdded", false);
	private final StringProperty content = new SimpleStringProperty(this, "content", "");
//	private char content;
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

		this.board = board;
		gridRow = row;
		gridCol = col;
		isBlack = false;
	}
//------------------INSTANCE METHODS-----------------------
	public boolean isAdded() {
		return this.isAdded.get();
	}
	
	public BooleanProperty addedProperty() {
		return this.isAdded;
	}
	
	public void setAdded(boolean isAdded) {
		this.isAdded.set(isAdded);
	}
	
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
	
	public String getContent() {
		return this.content.get();
	}
	
	public StringProperty getContentProperty() {
		return this.content;
	}
	public void setContent(String character) {
		this.content.set(character.toUpperCase());
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
