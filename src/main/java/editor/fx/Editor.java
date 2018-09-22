package main.java.editor.fx;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
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
import main.java.game.*;

public class Editor {
//-------------------DATA MEMBERS-------------------------
	
	private static Stop[] stops = new Stop[] { new Stop(0, Color.WHITE), new Stop(1, Color.ORANGE) };
	private static LinearGradient lg = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops);
	
	protected static Mode mode = Mode.EDIT;

	protected static final String DEFAULT_INFO = "";
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
	public static void loadTemplate(Template template, BoardFX boardFX) {
		
		boardFX.make((new EditableBoard(template.getRowSize(), template.getColSize())));
		template.loadBlackAndClue(boardFX);
		
	}
	
	public static void saveAsTemplate(BoardFX boardFX) {
		Template template = new Template(boardFX.getRowSize(), boardFX.getColSize());
		EditableBoard board = boardFX.board;
		int rowSize = board.getRowSize();
		int colSize = board.getColSize();
		for (int row = 0; row < rowSize; row++) {
			for (int col = 0; col < colSize; col++) {
				if(board.getGrid(row, col).isBlack())
					template.addBlackCoor(row, col);;
			}
		}
		for (Grid grid : board.getNumberedGrids()) {
			template.addClueCoor(grid.getGridRow(), grid.getGridCol());
		}
		
		try (ObjectOutputStream outStream = 
				new ObjectOutputStream(new FileOutputStream("template_prototype.cwtpl"))) {

			outStream.writeObject(template);

		} catch (IOException e) {
			System.out.println("Exception during serialization: " + e);
		}
	}
//---------------------------------------------------------
	public static void clearBoard(BoardFX boardFX) {
		int rowSize = boardFX.getRowSize();
		int colSize = boardFX.getColSize();
		for (int row = 0; row < rowSize; row++) {
			for (int col = 0; col < colSize; col++) {
				boardFX.gridFXs[row][col].setText('\0');
			}
		}
	}
	
	public static void restore(GridFX gridFX) {
		gridFX.setText('\0');
		removeClueNumberIfAny(gridFX);
		if (gridFX.isBlack()) {
			Editor.unblacken(gridFX);
		}
	}
	
	public static void restoreSelectedGrids(BoardFX boardFX) {
		if (!boardFX.multiselectedGrids.isEmpty()) {
			for (GridFX g : boardFX.multiselectedGrids) {
				Editor.restore(g);
			}
			Editor.deSelect(boardFX);
		} else {
			Editor.restore(boardFX.lastFocused[0]);
		}
	}
//---------------------------------------------------------
	public static void blacken(GridFX gridFX) {
		gridFX.rec.setFill(Color.BLACK);
		gridFX.setText('\0');
		Editor.removeClueNumberIfAny(gridFX);
		gridFX.grid.setBlack(true);
	}
	
	public static void unblacken(GridFX gridFX) {
		gridFX.rec.setFill(Color.TRANSPARENT);
		gridFX.grid.setBlack(false);
	}
	
	public static void blackenSelectedGrids(BoardFX boardFX) {
		if (!boardFX.multiselectedGrids.isEmpty()) {
			for (GridFX g : boardFX.multiselectedGrids) {
				Editor.blacken(g);
			}
			Editor.deSelect(boardFX);
		} else {
			Editor.blacken(boardFX.lastFocused[0]);
		}
	}

	public static void unblackenSelectedGrids(BoardFX boardFX) {
		if (!boardFX.multiselectedGrids.isEmpty()) {
			for (GridFX g : boardFX.multiselectedGrids) {
				Editor.unblacken(g);
			}
			Editor.deSelect(boardFX);
		} else {
			Editor.unblacken(boardFX.lastFocused[0]);
		}
	}
//---------------------------------------------------------		
	public static void setClueNumber(GridFX gridFX, int number) {
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
	}
	
	public static void removeClueNumberIfAny(GridFX gridFX) {
		ObservableList childrens = gridFX.getChildren();
		int size = childrens.size();
		if (size == 3) {
			childrens.remove(size - 1);
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
		protected static void highlight(GridFX gridFX) {
			gridFX.rec.setFill(lg);
		}

		protected static void unhighlight(GridFX gridFX) {
			gridFX.rec.setFill(Color.TRANSPARENT);
		}
//--------------------------------------------------------	
	protected static void highlight(BoardFX boardFX, Word word) {
		
		if (BoardFX.lastHighlighted != word) {
			if (BoardFX.lastHighlighted != null) {
				for (Grid grid : BoardFX.lastHighlighted.getGridArray()) {
					unhighlight(boardFX.gridFXs[grid.getGridRow()][grid.getGridCol()]);
				}
			}
	
			BoardFX.lastHighlighted = word;
			BoardFX.lastHighlightOrien = word.getOrien();
			for (Grid grid : BoardFX.lastHighlighted.getGridArray()) {
				highlight(boardFX.gridFXs[grid.getGridRow()][grid.getGridCol()]);
			}
		}
		
	}
	
	protected static void unHighlight(BoardFX boardFX) {
		
		if (BoardFX.lastHighlighted != null) {
			for (Grid grid : BoardFX.lastHighlighted.getGridArray()) {
				unhighlight(boardFX.gridFXs[grid.getGridRow()][grid.getGridCol()]);
			}
		}
		BoardFX.lastHighlighted = null;
		BoardFX.lastHighlightOrien = Orientation.INVALID;

	}
	
	protected static void changeHighlightOrien(BoardFX boardFX) {
		Word word = null;

		switch (BoardFX.lastHighlightOrien) {
		case HORIZONTAL:
			word = boardFX.lastFocused[0].grid.getVWord();
			break;
		case VERTICAL:
			word = boardFX.lastFocused[0].grid.getHWord();
			break;
		default:
			break;
		}

		if (word != null) {
			highlight(boardFX, word);

		}
	}
//----------------------------MULTIPLE GRID SELECTION-------------------------------
	protected static boolean shiftSelect(GridFX start, GridFX end) {
		if (start.getBoardFX() != end.getBoardFX()) {
			System.out.println("gridFXs are not of same boardFX");
			return false;
		} else {			
			BoardFX boardFX = start.getBoardFX();
			
			if (!boardFX.multiselectedGrids.isEmpty()) {
				deSelect(boardFX);
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
		Editor.highlight(gridFX);
		gridFX.boardFX.multiselectedGrids.add(gridFX);
		gridFX.isSelected = true;
	}
	
	protected static void ctrlSelect(GridFX gridFX) {
		GridFX focus = gridFX.boardFX.lastFocused[0];
		if (!focus.isSelected) {
			Editor.select(focus);
		}
		Editor.select(gridFX);
		Editor.unfocus(gridFX);
		gridFX.boardFX.requestFocus();
	}
	
	protected static void deSelect(GridFX gridFX) {
		if (gridFX.isBlack()) 
			gridFX.rec.setFill(Color.BLACK);
		else 
			Editor.unhighlight(gridFX);
	
		gridFX.boardFX.multiselectedGrids.remove(gridFX);
		gridFX.isSelected = false;
	}
	
	protected static void deSelect(BoardFX boardFX) {
		for (GridFX g : boardFX.multiselectedGrids) {
			if (g.isBlack()) 
				g.rec.setFill(Color.BLACK);
			else 
				Editor.unhighlight(g);
			g.isSelected = false;
		}
		boardFX.multiselectedGrids = new ArrayList<GridFX>();
	}
//----------------------------------------------------------------------

}
