package main.java.editor;

import main.java.game.Orientation;

public class ExtractedWord {

	private String word;
	private int row;
	private int col;
	private Orientation orien;
	
	public ExtractedWord(String word, int row, int col, Orientation orien) {
		this.word = word;
		this.row = row;
		this.col = col;
		this.orien = orien;
	}
	
	public String getWord() {
		return word;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public Orientation getOrien() {
		return orien;
	}
	
	public int getLength() {
		return this.word.length();
	}
	
}
