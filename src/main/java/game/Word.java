package main.java.game;

public class Word implements Comparable<Word>{
	
//-----------------DATA MEMBERS----------------------------
	
	private static Sort sortType = Sort.BY_LENGTH_REVERSE;
	
	private String clue;
	
	private Grid[] grids;
	private Orientation orien;
	
	private int rowIndex;
	private int colIndex;

//--------------------CONSTRUCTORS--------------------------
	public Word(Grid[] grids, Orientation orien, int rowIndex, int colIndex) {
		this.grids = grids;
		this.orien = orien;
		this.rowIndex = rowIndex;
		this.colIndex = colIndex;
	}
	
	public Word(Grid[] grids, Orientation orien) {
		this(grids, orien, grids[0].getGridRow(), grids[0].getGridCol());
	}
	
	public Word(int length) {
		grids = new Grid[length];
	}
//	public Word(String word) {
//		this.word = word.toUpperCase();
//		this.length = this.word.length();
//		Word.sortType = Sort.BY_LENGTH_REVERSE;
//		this.grids = new Grid[this.word.length()];
//	}
	
//------------------INSTANCE METHODS-----------------------
	public String getClue() {
		return this.clue;
	}
	
	public int getRowIndex() {
		return this.rowIndex;
	}
	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}

	public int getColIndex() {
		return this.colIndex;
	}
	public void setColIndex(int colIndex) {
		this.colIndex = colIndex;
	}
	
	public Orientation getOrien() {
		return this.orien;
	}
	public String getStringOrien() {
		switch (orien) {
		case HORIZONTAL: return "Horizontally";
		case VERTICAL: return "Vertically"; 
		default: return "Invalid";
		}
	}
	
	public void setOrien(Orientation orien) {
		this.orien = orien;
	}

	public int getLength() {
		return this.grids.length;
	}
	
	public static void setSortType(Sort sortType) {
		Word.sortType = sortType;
	}
	
	public String getWord() {
		String word = "";
		for (Grid grid : this.grids) {
			word = word + grid.getContent();
		}
		return word;
	}
	
	public Grid getGrid(int index) {
		return this.grids[index];
	}
	
	public Grid[] getGridArray() {
		return this.grids;
	}
	
	public void setGrid(Grid grid, int index) {	
		grids[index] = grid;
		grid.setAdded(true);
	}
//-------------------------------------------------------------
	@Override
	public int compareTo(Word other) {
		if (Word.sortType == Sort.BY_LENGTH_REVERSE) {
			if (this.getLength() > other.getLength()) return -1;
			else if (this.getLength() < other.getLength()) return 1;
			else return 0;
		} else {
			if (this.rowIndex > other.rowIndex) return 1;
			else if (this.rowIndex < other.rowIndex) return -1;
			else {
				if (this.colIndex > other.colIndex) return 1;
				else if (this.colIndex < other.colIndex) return -1;
				else return 0;
			}
		}
	}	
	
	@Override
	public String toString() {
		return "Word: " + this.getWord()
			+	". Length: " + this.getLength()
			+	"\n\tClue: " + this.clue
			+	"\n\tPlaced " + this.orien 
			+ " at (" + rowIndex + ", " + colIndex + ").\n";
	}
//---------------------------------------------------------

}
