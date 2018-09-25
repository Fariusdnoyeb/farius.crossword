package main.java.editor.fx;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import main.java.editor.*;
import main.java.editor.exception.BlackGridException;
import main.java.editor.exception.EmptyGridException;
import main.java.editor.exception.EmptyWordException;
import main.java.editor.exception.GridAddedElsewhereException;
import main.java.game.*;

public class Editor {
//-------------------DATA MEMBERS-------------------------
	
	protected static final String DEFAULT_INFO = "...";
	protected static final StringProperty info = new SimpleStringProperty("");

	protected static final IntegerProperty focusedRowIndex = new SimpleIntegerProperty(0);// grid row index
	protected static final IntegerProperty focusedColIndex = new SimpleIntegerProperty(0);// grid col index
	
//---------------------------------------------------------
	public static StringProperty getInfoProperty() {
		return info;
	}
	
	public static void setInfo(String message) {
		info.set(message);
	}
	
	public static void clearInfo() {
		info.set(DEFAULT_INFO);
	}
	
	
	public static IntegerProperty getFocusedRowIndexProperty() {
		return focusedRowIndex;
	}

	public static IntegerProperty getFocusedColIndexProperty() {
		return focusedColIndex;
	}
	
	public static void updateFocusedGrid(GridFX gridFX) {
		focusedRowIndex.set(gridFX.grid.getGridRow());
		focusedColIndex.set(gridFX.grid.getGridCol());
	}
//---------------------------------------------------------
	public static void saveAsTemplate(BoardFX boardFX) {
		Template template = new Template(boardFX.getRowSize(), boardFX.getColSize());
		
		GridFX gridFX;
		int rowSize = boardFX.getRowSize();
		int colSize = boardFX.getColSize();
		for (int row = 0; row < rowSize; row++) {
			for (int col = 0; col < colSize; col++) {
				gridFX = boardFX.getGridFX(row, col);
				if(gridFX.isBlack())
					template.addBlackCoor(row, col);
				if (gridFX.isNumbered())
					template.addClueCoor(row, col);
			}
		}
		
		try (ObjectOutputStream outStream = 
				new ObjectOutputStream(new FileOutputStream("template_prototype.cwtpl"))) {

			outStream.writeObject(template);

		} catch (IOException e) {
			System.out.println("Exception during serialization: " + e);
		}
	}
	public static void loadTemplate(BoardFX boardFX) throws ClassNotFoundException {
		
		Template template;
		try (ObjectInputStream inStream 
				= new ObjectInputStream(new FileInputStream("template_prototype.cwtpl"))) {
			
			template = (Template)inStream.readObject();
			boardFX.make((new EditableBoard(template.getRowSize(), template.getColSize())));
			template.loadBlackAndClue(boardFX);
			
		} catch (IOException e)	{
			System.out.println("Exception during deserialization: " + e);
		}
		
	}
//---------------------------------------------------------
	public static void exportGame(BoardFX boardFX) {
		try (ObjectOutputStream outStream = 
				new ObjectOutputStream(new FileOutputStream("demo.cw"))) {

			outStream.writeObject((Board)boardFX.editableBoard);

		} catch (IOException e) {
			System.out.println("Exception during serialization: " + e);
		}
	}
	
	public static void importGame(BoardFX boardFX) throws ClassNotFoundException {
		Board board;
		try (ObjectInputStream inStream 
				= new ObjectInputStream(new FileInputStream("demo.cw"))) {
			
			board = (Board)inStream.readObject();

			EditableBoard editableBoard = (EditableBoard)board;
			int rowSize = editableBoard.getRowSize();
			int colSize = editableBoard.getColSize();
			Grid grid;
			for (int row = 0; row < rowSize; row++) {
				for (int col = 0; col < colSize; col++) {
					grid = editableBoard.getGrid(row, col);
					boardFX.add(new GridFX(boardFX, grid), col, row);
				}
			}
			
			ArrayList<Grid> numberedGrids = editableBoard.getNumberedGrids();
			for (int index = 0; index < numberedGrids.size(); index++) {
				grid = numberedGrids.get(index);
				Editor.setClueNumber(boardFX.getGridFX(grid), index + 1);
			}
			
		} catch (IOException e)	{
			System.out.println("Exception during deserialization: " + e);
		}
		
	}
//---------------------------------------------------------
	public static void clearBoard(BoardFX boardFX) {
		int rowSize = boardFX.getRowSize();
		int colSize = boardFX.getColSize();
		for (int row = 0; row < rowSize; row++) {
			for (int col = 0; col < colSize; col++) {
				boardFX.gridFXs[row][col].setText("");
			}
		}
	}
	
	public static void restore(GridFX gridFX) {
		gridFX.setText("");
		removeClueNumberIfAny(gridFX);
		if (gridFX.isBlack()) {
			Editor.unblacken(gridFX);
		}
	}
	
	public static void restoreSelectedGrids(BoardFX boardFX) {
		if (boardFX.isMultiSelected()) {
			for (GridFX g : boardFX.multiselectedGrids) {
				Editor.restore(g);
			}
			Editor.deSelectAll(boardFX);
		} else {
			Editor.restore(boardFX.getFocusedGrid());
		}
	}
//---------------------------------------------------------
	public static void blacken(GridFX gridFX) {
		gridFX.rec.setFill(Color.BLACK);
		gridFX.setText("");
		Editor.removeClueNumberIfAny(gridFX);
		gridFX.grid.setBlack(true);
	}
	
	public static void unblacken(GridFX gridFX) {
		gridFX.rec.setFill(Color.TRANSPARENT);
		gridFX.grid.setBlack(false);
	}
	
	public static void blackenSelectedGrids(BoardFX boardFX) {
		if (boardFX.isMultiSelected()) {
			for (GridFX g : boardFX.multiselectedGrids) {
				Editor.blacken(g);
			}
			Editor.deSelectAll(boardFX);
		} else {
			Editor.blacken(boardFX.getFocusedGrid());
		}
	}

	public static void unblackenSelectedGrids(BoardFX boardFX) {
		if (boardFX.isMultiSelected()) {
			for (GridFX g : boardFX.multiselectedGrids) {
				Editor.unblacken(g);
			}
			Editor.deSelectAll(boardFX);
		} else {
			Editor.unblacken(boardFX.getFocusedGrid());
		}
	}
//---------------------------------------------------------		
	public static boolean setClueNumber(GridFX gridFX, int number) {
		try {
			
			if (gridFX.isBlack()) {
				Editor.setInfo(InfoMessageEng.BLACK_GRID + " " + InfoMessageEng.NO_ACTION);
				return false;
			} else {
				if (gridFX.getChildren().size() == 3) {
					((Label)gridFX.getChildren().get(gridFX.getChildren().size() - 1)).
													setText(Integer.toString(number));
				}
				Label numL = new Label();
				numL.setText(Integer.toString(number));
				numL.setFont(new Font("Times New Roman", 10));
				gridFX.getChildren().add(numL);
				StackPane.setAlignment(numL, Pos.TOP_LEFT);
				StackPane.setMargin(numL, new Insets(3, 3, 3, 3));
				numL.addEventHandler(MouseEvent.ANY, e -> gridFX.rec.fireEvent(e));
				
				Editor.setInfo(InfoMessageEng.CLUE_NUMBER_ADDITION + " " + InfoMessageEng.SUCCEEDED);
				return true;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			Editor.setInfo(InfoMessageEng.CLUE_NUMBER_ADDITION + " " + InfoMessageEng.FAILED);
			return false;
		}
	}
	
	public static boolean removeClueNumberIfAny(GridFX gridFX) {
		try {
			ObservableList childrens = gridFX.getChildren();
			int size = childrens.size();
			if (size == 3) {
				childrens.remove(size - 1);
			}
			Editor.setInfo(InfoMessageEng.CLUE_NUMBER_REMOVAL + " " + InfoMessageEng.SUCCEEDED);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			Editor.setInfo(InfoMessageEng.CLUE_NUMBER_REMOVAL + " " + InfoMessageEng.FAILED);
			return false;
		}
	}
//---------------------------------------------------------	
	protected static void focus(GridFX gridFX) {
		if (gridFX.boardFX.lastFocused[0] != null) {
			unfocus(gridFX.boardFX.lastFocused[0]);
			gridFX.boardFX.lastFocused[1] = gridFX.boardFX.lastFocused[0];
		}
		gridFX.rec.setStrokeWidth(2.5);
		gridFX.rec.setStroke(Color.RED);
		
		gridFX.boardFX.lastFocused[0] = gridFX;
		gridFX.requestFocus();
		updateFocusedGrid(gridFX);
	}

	protected static void unfocus(GridFX gridFX) {
		gridFX.rec.setStrokeWidth(1.0);
		gridFX.rec.setStroke(Color.BLACK);
	}

//---------------------------------------------------------	
//	protected static void highlight(GridFX gridFX) {
//		gridFX.rec.setFill(lg);
//	}
//
//	protected static void unhighlight(GridFX gridFX) {
//		gridFX.rec.setFill(Color.TRANSPARENT);
//	}

//--------------------------------------------------------	
	
//	protected static void highlight(BoardFX boardFX, Word word) {
//
//		if (BoardFX.lastHighlighted != word) {
//			if (BoardFX.lastHighlighted != null) {
//				for (Grid grid : BoardFX.lastHighlighted.getGridArray()) {
//					unhighlight(boardFX.gridFXs[grid.getGridRow()][grid.getGridCol()]);
//				}
//			}
//
//			BoardFX.lastHighlighted = word;
//			BoardFX.lastHighlightOrien = word.getOrien();
//			for (Grid grid : BoardFX.lastHighlighted.getGridArray()) {
//				highlight(boardFX.gridFXs[grid.getGridRow()][grid.getGridCol()]);
//			}
//		}
//
//	}
//	
//	protected static void unHighlight(BoardFX boardFX) {
//		
//		if (BoardFX.lastHighlighted != null) {
//			for (Grid grid : BoardFX.lastHighlighted.getGridArray()) {
//				unhighlight(boardFX.gridFXs[grid.getGridRow()][grid.getGridCol()]);
//			}
//		}
//		BoardFX.lastHighlighted = null;
//		BoardFX.lastHighlightOrien = Orientation.INVALID;
//
//	}
//	
//	protected static void changeHighlightOrien(BoardFX boardFX) {
//		Word word = null;
//
//		switch (BoardFX.lastHighlightOrien) {
//		case HORIZONTAL:
//			word = boardFX.getFocusedGrid().grid.getVWord();
//			break;
//		case VERTICAL:
//			word = boardFX.getFocusedGrid().grid.getHWord();
//			break;
//		default:
//			break;
//		}
//
//		if (word != null) {
//			highlight(boardFX, word);
//
//		}
//	}
	
//----------------------------MULTIPLE GRID SELECTION-------------------------------
	protected static boolean shiftSelect(GridFX start, GridFX end) {
		if (start.getBoardFX() != end.getBoardFX()) {
			System.out.println("gridFXs are not of same boardFX");
			return false;
		} else {			
			BoardFX boardFX = start.getBoardFX();
			
			if (!boardFX.multiselectedGrids.isEmpty()) {
				deSelectAll(boardFX);
			}
			int startX, startY, endX, endY;

			if (start.getGridRow()< end.getGridRow()) {
				startX = start.getGridRow();
				endX = end.getGridRow();
			} else {
				startX = end.getGridRow();
				endX = start.getGridRow();
			}

			if (start.getGridCol() < end.getGridCol()) {
				startY = start.getGridCol();
				endY = end.getGridCol();
			} else {
				startY = end.getGridCol();
				endY = start.getGridCol();
			}

			GridFX toSelect;
			for (int row = startX; row <= endX; row++) {
				for (int col = startY; col <= endY; col++) {
					toSelect = boardFX.gridFXs[row][col];
					if (toSelect.isBlack())
						continue;
					else {
						Editor.select(toSelect);
					}
				}
			}
			// end Grid must be unfocused;
			Editor.unfocus(end);
			boardFX.requestFocus();
			return true;
		}
	
	}
	
	protected static void select(GridFX gridFX) {
		gridFX.boardFX.multiselectedGrids.add(gridFX);
		gridFX.isSelected.set(true);;
	}
	
	protected static void ctrlSelect(GridFX gridFX) {
		GridFX focus = gridFX.boardFX.getFocusedGrid();
		if (!focus.isSelected()) {
			Editor.select(focus);
		}
		Editor.select(gridFX);
		Editor.unfocus(gridFX);
		gridFX.boardFX.requestFocus();
	}

	protected static void deSelect(GridFX gridFX) {
		gridFX.boardFX.multiselectedGrids.remove(gridFX);
		gridFX.isSelected.set(false);
	}

	protected static void deSelectAll(BoardFX boardFX) {
		for (GridFX g : boardFX.multiselectedGrids) {
			g.isSelected.set(false);
		}
		boardFX.multiselectedGrids = new ArrayList<GridFX>();
	}
	
//----------------------------------------------------------------------
	private static ExtractedWord extractWordFromSelectedGrids(BoardFX boardFX) throws EmptyGridException {
		ArrayList<GridFX> grids = boardFX.multiselectedGrids;
		Collections.sort(grids);
		
		String word = grids.get(0).getText();
		
		int row = grids.get(0).getGridRow();
		boolean isConsecutivelyHorizontal = true;
		for (int i = 1; i < grids.size(); i++) {
			if ( grids.get(i).getGridRow() != row 
					|| grids.get(i).getGridCol() != grids.get(i - 1).getGridCol() + 1
					|| grids.get(i).isBlack() ) {
				isConsecutivelyHorizontal = false;
				word = grids.get(0).getText();
				break;
			} else {
				if (grids.get(i).getText() == "")
					throw new EmptyGridException();
				word = word + grids.get(i).getText();
			}
		}
		
		int col = grids.get(0).getGridCol();
		boolean isConsecutivelyVertical = true;
		if (!isConsecutivelyHorizontal) {
			for (int i = 1; i < grids.size(); i++) {
				if ( grids.get(i).getGridCol() != col 
						|| grids.get(i).getGridRow() != grids.get(i - 1).getGridRow() + 1
						|| grids.get(i).isBlack() ) {
					isConsecutivelyVertical = false;
					break;
				} else {
					if (grids.get(i).getText() == "")
						throw new EmptyGridException();
					word = word + grids.get(i).getText();
				}
			}
		}
		Orientation orien;
		if (isConsecutivelyHorizontal) {
			orien = Orientation.HORIZONTAL;
		} else if (isConsecutivelyVertical)
			orien = Orientation.VERTICAL;
		else 
			return null;
		
		return new ExtractedWord(word, row, col, orien);
	
	}
	
	public static boolean addWordFromSelectedGrids(BoardFX boardFX) {
		if (!boardFX.isMultiSelected()) {
			Editor.setInfo(InfoMessageEng.NO_ACTION + " " + InfoMessageEng.ADD_WORD_FROM_SELECTED_GRIDS_INS);
			return false;
		} else {

			try {
				ExtractedWord extractedWord = Editor.extractWordFromSelectedGrids(boardFX);
				if (extractedWord == null ) {
					Editor.setInfo(InfoMessageEng.NO_ACTION + " " + InfoMessageEng.MISSING_PARAMETERS);
				} else if(extractedWord.getWord().isEmpty()) {
					throw new EmptyWordException();
				} else {
					boardFX.editableBoard.placeWord(extractedWord);
					Editor.setInfo(extractedWord.getWord() + " " + InfoMessageEng.WORD_ADDED + " "
							+ InfoMessageEng.CLUE_NUMBER_REMINDER);
					return true;
				}
			} catch (GridAddedElsewhereException e) {
				Editor.setInfo(InfoMessageEng.FAILED + " " + e.getMessage());
			} catch (EmptyGridException e) {
				Editor.setInfo(InfoMessageEng.FAILED + " " + e.getMessage());
			} catch (EmptyWordException e) {
				Editor.setInfo(InfoMessageEng.FAILED + " " + e.getMessage());
			} catch (IndexOutOfBoundsException e) {
				Editor.setInfo(InfoMessageEng.FAILED + " " + InfoMessageEng.INDEX_OUT_OF_BOUNDS);
			} catch (BlackGridException e) {
				Editor.setInfo(InfoMessageEng.FAILED + " " + e.getMessage());
			} catch (Exception e) {
				Editor.setInfo(InfoMessageEng.FAILED + " " + e.getMessage());
			}
			return false;
		}
	}
	
	public static boolean addWordFromPrompt(ExtractedWord extractedWord, BoardFX boardFX) throws Exception {
		try {
			if (extractedWord == null) {
				Editor.setInfo(InfoMessageEng.NO_ACTION + " " + InfoMessageEng.MISSING_PARAMETERS);
			} else if (extractedWord.getWord().isEmpty()) {
				throw new EmptyWordException();
			} else {
				boardFX.editableBoard.placeWord(extractedWord);
				Editor.setInfo(extractedWord.getWord() + " " + InfoMessageEng.WORD_ADDED + " "
						+ InfoMessageEng.CLUE_NUMBER_REMINDER);
				return true;
			}
		} catch (GridAddedElsewhereException e) {
			Editor.setInfo(InfoMessageEng.FAILED + " " + e.getMessage());
		} catch (EmptyWordException e) {
			Editor.setInfo(InfoMessageEng.FAILED + " " + e.getMessage());
		} catch (IndexOutOfBoundsException e) {
			Editor.setInfo(InfoMessageEng.FAILED + " " + InfoMessageEng.INDEX_OUT_OF_BOUNDS);
		} catch (BlackGridException e) {
			Editor.setInfo(InfoMessageEng.FAILED + " " + e.getMessage());
		} catch (Exception e) {
			Editor.setInfo(InfoMessageEng.FAILED + " " + e.getMessage());
		}
		return false;
	}


//----------------------------------------------------------------------
	
//----------------------------------------------------------------------
}
