package main.java.editor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Stack;

import main.java.editor.exception.BlackGridException;
import main.java.editor.exception.GridAddedElsewhereException;
import main.java.game.*;

public class EditableBoard extends Board{
//-----------------DATA MEMBERS---------------------------	
	private HashMap<Character, ArrayList<Grid>> gridToCharMap;

// -------------------CONSTRUCTORS--------------------------
	public EditableBoard(int rowSize, int colSize) {
		this.rowSize = rowSize;
		this.colSize = colSize;
		
		this.words = new ArrayList<Word>();
		
		this.gridToCharMap = new HashMap<Character, ArrayList<Grid>>();
		
		this.board = new Grid[rowSize][colSize];
		for (int row = 0; row < rowSize; row++) {
			for (int col = 0; col < colSize; col++) {
				this.board[row][col] = new Grid(this, row, col);
			}
		}
		
		
		}
//------------------INSTANCE METHODS-----------------------
	public boolean placeWord(ExtractedWord extractedWord) throws Exception {
		Word word = new Word(extractedWord.getLength());
		return placeWord(word, extractedWord.getWord(), extractedWord.getRow(), extractedWord.getCol(), extractedWord.getOrien());
	}
	private boolean placeWord(Word word, String wordStr, int row, int col, Orientation orien) throws Exception {
		if (word == null || orien == Orientation.INVALID) {
			return false;
		} else if ( row < 0 || row > rowSize ||
				 col < 0 || col > colSize) {
			throw new IndexOutOfBoundsException();
		}
		
		int length = word.getLength();
		int cursor;
		int index = 0;
		char charPlaced;
		
		Grid[] grids = new Grid[length];
		String[] contents = new String[length]; 
		Grid grid;		
		switch(orien) {
		case HORIZONTAL:
			cursor = col;
			while (index < length) {
				charPlaced = wordStr.charAt(index);
				grid = this.board[row][cursor];
				
				if (grid.getHWord() != null)
					throw new GridAddedElsewhereException();
				if (grid.isBlack())
					throw new BlackGridException();
					
				if (gridToCharMap.get(charPlaced) == null) {
					ArrayList<Grid> gridList = new ArrayList<Grid>();
					gridList.add(grid);
					gridToCharMap.put(charPlaced, gridList);
				} else {
					gridToCharMap.get(charPlaced).add(grid);
				}
				contents[index] = Character.toString(charPlaced);
				grids[index] = grid;
				index++;
				cursor++;
			}
		case VERTICAL:
			cursor = row;
			while (index < length) {
				charPlaced = wordStr.charAt(index);
				grid = this.board[cursor][col];
				if (grid.getVWord() != null)
					throw new GridAddedElsewhereException();
				if (grid.isBlack())
					throw new BlackGridException();
				if (gridToCharMap.get(charPlaced) == null) {
					ArrayList<Grid> gridList = new ArrayList<Grid>();
					gridList.add(grid);
					gridToCharMap.put(charPlaced, gridList);
				} else {
					gridToCharMap.get(charPlaced).add(grid);
				}
				contents[index] = Character.toString(charPlaced);
				grids[index] = grid;
				index++;
				cursor++;
			}
		default:
			break;
		}
		
		word.setRowIndex(row);
		word.setColIndex(col);
		word.setOrien(orien);
		index = 0;
		for (; index < length; index++) {
			grids[index].setContent(contents[index]);
			grids[index].setWord(word, index, orien);
			word.setGrid(grids[index], index);
		}
		

		words.add(word);
		
		return true;
	}
	

	public ArrayList<Grid> getNumberedGrids() {
		Word.setSortType(Sort.BY_POS);
		ArrayList<Word> wordList = new ArrayList<Word>();
		wordList.addAll(this.words);
		Collections.sort(wordList);

		ArrayList<Grid> gridList = new ArrayList<Grid>();
		int[] currentPos = new int[2];
		currentPos[0] = currentPos[1] = -1;
		Word word;
		
		for (int i = 0; i < wordList.size(); i++) {
			int[] newPos = new int[2];
			word = wordList.get(i);
			newPos[0] = word.getRowIndex();
			newPos[1] = word.getColIndex();
			if (newPos[0] != currentPos[0] || newPos[1] != currentPos[1]) {
				gridList.add(word.getGrid(0));
				currentPos = newPos;
			}
		}

		Word.setSortType(Sort.BY_LENGTH_REVERSE);
		return gridList;
	}
	
	
//------ALGORITHM: AUTOMATICALLY PLACE A WORD LIST ONTO BOARD------
		/**
		 * places 
		 * @param word : (of type Word)
		 * @return true if placed successfully,
		 *		   false if failed to place.
		 * @throws GridAddedElsewhereException 
		 */
//		protected boolean placeAtCenter(Word word) throws GridAddedElsewhereException {
//			int length = word.getLength();
//			int col = (this.colSize - length) / 2 + 1;
//			int row = this.rowSize / 2;
//			return (placeWord(word, row, col - 1, Orientation.HORIZONTAL));
//		}
//		
		/**
		 * Pseudo:
		 * <ul>
		 * <li>longer words receive higher priority.</li>
		 * <li>places first word at (0,0).</li>
		 * <li>adds the rest on stack A. Make one additional empty stack B.</li>
		 * <li>tries adding each Word from the top of stack A to the best 
		 * possible position (see findBestPos()). If fails to place, adds to 
		 * stack B and continue until another Word is placed successfully, at 
		 * which point stack B is pushed reversely onto stack A 
		 * (to preserve length priority).</li>
		 * </ul> 
		 * @param wordBank list of words to place onto board.
		 * @throws GridAddedElsewhereException 
		 */
//		public void placeList(ArrayList<Word> wordBank) throws GridAddedElsewhereException {
//			Collections.sort(wordBank); //sorts by length. Longest first.
//			
//			Word currentW;
//			currentW = wordBank.get(0);
//			this.placeWord(currentW, 2, 0, Orientation.HORIZONTAL);
//			
//			Stack<Word> workingS = new Stack<Word>();
//			for (int i = wordBank.size() - 1; i > 0; i--) {
//				workingS.push(wordBank.get(i));			
//			}
//			
//			Stack<Word> remainS = new Stack<Word>();
//			while (!workingS.isEmpty()) {
//				currentW = workingS.pop();
//				if (tryPlaceWordToBestPos(currentW)) {
//					if (!remainS.isEmpty()) {
//						while (!remainS.isEmpty()) {
//							workingS.push(remainS.pop());
//						}
//						continue;
//					}
//				} else {
//					remainS.push(currentW);
//				}
//			}
//		}
		
		/**
		 * tries placing a Word instance to the best
		 * possible place on the board.
		 * @param word
		 * @return true if placed successfully,
		 * 			false if fails to place.
		 * @throws GridAddedElsewhereException 
		 */
//		protected boolean tryPlaceWordToBestPos(Word word) throws GridAddedElsewhereException {
//			final int	HORIZONTAL	= 0;
////			final int	VERTICAL		= 1;	
//			int[] sugCoor = findBestPos(word);
//			if (sugCoor == null) {
//				return false;
//			} else {
//				Orientation orien = (sugCoor[3] == HORIZONTAL) ? (Orientation.HORIZONTAL) : (Orientation.VERTICAL);
//				placeWord(word, sugCoor[1], sugCoor[2], orien);
//				return true;
//			}
//		}
		
		/**
		 * 
		 * loops through all the possible coordinates in posMap 
		 * and all characters in the word and return the
		 * best starting coordinates for the word to be placed.
		 * <p>See numIntersect() for ranking procedure.</p>
		 * @param word
		 * @return
		 */
//		protected int[] findBestPos(Word word) {
//			String currentStr = word.getWord();
////			ArrayList<Integer[]> sugPosList;
//			ArrayList<Grid> sugGridList;
//			int[] sugCoor = null;
//			int rank = -1;
//			for (int i = 0; i < currentStr.length(); i++) {
////				sugPosList = posMap.get(currentStr.charAt(i));
//				sugGridList = gridToCharMap.get(currentStr.charAt(i));
//				if (sugGridList == null) continue;
//				int[] temp;
//				for (int j = 0; j < sugGridList.size(); j++) {
//					temp = numIntersect(word, i, sugGridList.get(j));
//					if (temp[0] > rank) {
//						rank = temp[0];
//						sugCoor = temp;
//					}
//					
//				}
//			}
//			return (rank < 0) ? (null) : (sugCoor);
//		}
		
		/**
		 * finds the number of intersections with other words at suggested
		 * placing coordinates.
		 * <ul>Ranking = number of intersections with other words:
		 * <li>the higher the ranking, the better the coordinate.</li>
		 * <li>if ranking = -1, the word cannot be placed at
		 * the suggested coordinates.</li>
		 * </ul>
		 * @param word a Word instance to be placed onto Board
		 * @param letterIndex the index of the character where 
		 * the word is placed based on.
		 * @param letterCoor board coordinates of the above letter.
		 * @return int[4] = {ranking, row, col, orientation}
		 */
		protected int[] numIntersect(Word word, int letterIndex, Grid sugGrid) {
			final int	HORIZONTAL	= 0;
			final int	VERTICAL		= 1;
			
			int[] hor = {sugGrid.getGridRow(), sugGrid.getGridCol() - letterIndex};
			int[] ver = {sugGrid.getGridRow() - letterIndex, sugGrid.getGridCol()};
			int length = word.getLength();
			int cursor, stop;
			String boardChar, wordChar;
			int row, col;
			//Horizontal Tests
			int numH = 0;
			row = hor[0];
			col = hor[1];
			if (col + length - 1 < this.colSize && col >= 0) {
				cursor = col;
				stop = cursor + length;
				for (int i = 0; cursor < stop; cursor++, i++) {
					if (this.board[row][cursor].isBlack()) {
						numH = -1;
						break;
					}
					boardChar = this.board[row][cursor].getContent(); 
					wordChar = Character.toString( word.getWord().charAt(i) );
					if (boardChar != "") {
						if (boardChar == wordChar) {
							numH++;
						} else {
							numH = -1;
							break;
						}
					}
				}
			} else {
				numH = -1;
			}
			//Vertical Tests
			
			int numV = 0;
			row = ver[0];
			col = ver[1];
			if (row + length - 1 < this.rowSize && row >= 0) {
				cursor = row;
				stop = cursor + length;
				for (int i = 0; cursor < stop; cursor++, i++) {
					if (this.board[cursor][col].isBlack()) {
						numV = -1;
						break;
					}
					boardChar = this.board[cursor][col].getContent(); 
					wordChar = Character.toString( word.getWord().charAt(i) );
					if (boardChar != "") {
						if (boardChar == wordChar) {
							numV++;
						} else {
							numV = -1;
							break;
						}
					}
				}
			} else {
				numV = -1;
			}
			
			if (numV > numH) {
				int[] sugCoor = {numV, ver[0], ver[1], VERTICAL};
				return sugCoor;
			} else {
				int[] sugCoor = {numH, hor[0], hor[1], HORIZONTAL};
				return sugCoor;
			}
		}
//--------------------------------------------------------------------		
	
	
}
