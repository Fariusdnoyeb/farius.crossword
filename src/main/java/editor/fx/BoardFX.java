package main.java.editor.fx;

import java.util.ArrayList;

import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import main.java.editor.EditableBoard;
import main.java.game.*;

public class BoardFX extends GridPane {
//-----------------DATA MEMBERS---------------------------
	protected final GridContextMenu GRID_CONTEXT_MENU = new GridContextMenu(this);

	protected GridFX[] lastFocused;
	protected ArrayList<GridFX> multiselectedGrids;

	protected static Word lastHighlighted;
	protected static Orientation lastHighlightOrien;

	protected EditableBoard editableBoard;
	protected GridFX[][] gridFXs;

//-------------------CONSTRUCTORS--------------------------
	public BoardFX() {
		super();
	}
//------------------INSTANCE METHODS-----------------------
	public void make(EditableBoard editableBoard) {
		this.editableBoard = editableBoard;

		lastFocused = new GridFX[2];
		multiselectedGrids = new ArrayList<GridFX>();

		int rowSize = editableBoard.getRowSize();
		int colSize = editableBoard.getColSize();
		gridFXs = new GridFX[rowSize][colSize];
		
		GridFX gridFX;
		for (int row = 0; row < rowSize; row++) {
			for (int col = 0; col < colSize; col++) {
				gridFX = new GridFX(this, editableBoard.getGrid(row, col));
				this.gridFXs[row][col] = gridFX;
				this.add(gridFX, col, row);
			}
		}
		Editor.focus(gridFXs[0][0]);
		
		this.addKeyHandler();
		this.requestFocus();
	}

//----------------------------------------------------------
	public EditableBoard getEditableBoard() {
		return this.editableBoard;
	}
	
	public GridFX getGridFX(Grid grid) {
		return this.gridFXs[grid.getGridRow()][grid.getGridCol()];
	}

	public GridFX getGridFX(int row, int col) {
		return this.gridFXs[row][col];
	}

	public GridFX getFocusedGrid() {
		if (lastFocused != null) {
			return lastFocused[0];
		} else {
			return null;
		}
	}

	public int getColSize() {
		return this.editableBoard.getColSize();
	}

	public int getRowSize() {
		return this.editableBoard.getRowSize();
	}
	
	public boolean isMultiSelected() {
		return !this.multiselectedGrids.isEmpty();
	}
// ----------------------------Key Event----------------------------
	private void addKeyHandler() {
		this.setOnKeyPressed(e -> {
			if (e.isControlDown() || e.isShiftDown()) {
				return; //need implementation
			}
			String c = e.getText();
			KeyCode code = e.getCode();

			if (!this.lastFocused[0].isBlack() && (code.isLetterKey() || code.isDigitKey())) {
				if (!this.multiselectedGrids.isEmpty())
					Editor.deSelectAll(this);
				this.lastFocused[0].setText(c); // EDIT vs PREVIEW mode???
				moveToNextGrid();
			} else if (code.isWhitespaceKey()) {
				if (!this.multiselectedGrids.isEmpty())
					Editor.deSelectAll(this);
				moveToNextGrid();
			} else if (code.isArrowKey()) {
				if (!this.multiselectedGrids.isEmpty())
					Editor.deSelectAll(this);
				navigate(code);
			} else if (code == KeyCode.DELETE || code == KeyCode.BACK_SPACE) {
				if (!this.multiselectedGrids.isEmpty()) {
					for (GridFX g : this.multiselectedGrids) {
						g.setText("");
					}
				} else
					this.lastFocused[0].setText("");
			}

		});
		
	}

	private void navigate(KeyCode code) {
		GridFX nowFocused = this.lastFocused[0];
		switch (code) {
		case RIGHT:
			if (nowFocused.getGridCol() + 1 < this.editableBoard.getColSize()) {
				GridFX gridFX = this.gridFXs[nowFocused.getGridRow()][nowFocused.getGridCol() + 1];
				Editor.focus(gridFX);
			}
			break;

		case LEFT:
			if (nowFocused.getGridCol() - 1 >= 0) {
				GridFX gridFX = this.gridFXs[nowFocused.getGridRow()][nowFocused.getGridCol() - 1];
				Editor.focus(gridFX);
			}
			break;

		case UP:
			if (nowFocused.getGridRow() - 1 >= 0) {
				GridFX gridFX = this.gridFXs[nowFocused.getGridRow() - 1][nowFocused.getGridCol()];
				Editor.focus(gridFX);
			}
			break;

		case DOWN:
			if (nowFocused.getGridRow() + 1 < this.editableBoard.getRowSize()) {
				GridFX gridFX = this.gridFXs[nowFocused.getGridRow() + 1][nowFocused.getGridCol()];
				Editor.focus(gridFX);
			}
			break;
		default:
			break;

		}
	}

	protected void moveToNextGrid() {
		if (this.lastFocused[1] == null) {
			return;
		}
		GridFX lastFocused = this.lastFocused[1];
		GridFX nowFocused = this.lastFocused[0];
		boolean isNext;

		int lastCol = lastFocused.grid.getGridCol();
		int lastRow = lastFocused.grid.getGridRow();
		int nowCol = nowFocused.grid.getGridCol();
		int nowRow = nowFocused.grid.getGridRow();

		isNext = nowCol == lastCol + 1 && nowCol + 1 < this.editableBoard.getColSize() && nowRow == lastRow
				&& !this.getGridFX(nowRow, nowCol + 1).isBlack();
		if (isNext) {
			Editor.focus(this.getGridFX(nowRow, nowCol + 1));
			return;
		}

		isNext = nowRow == lastRow + 1 && nowRow + 1 < this.editableBoard.getRowSize() && nowCol == lastCol
				&& !this.getGridFX(nowRow + 1, nowCol).isBlack();
		if (isNext) {
			Editor.focus(this.getGridFX(nowRow + 1, nowCol));
			;
			return;
		}

	}
//-------------------------PREVIEW-----------------------------
//	protected void changeHighlightOrien() {
//		Word word = null;
//
//		switch (BoardFX.lastHighlightOrien) {
//		case HORIZONTAL:
//			word = this.lastFocused[0].grid.getVWord();
//			break;
//		case VERTICAL:
//			word = this.lastFocused[0].grid.getHWord();
//			break;
//		default:
//			break;
//		}
//
//		if (word != null) {
//			Editor.highlight(this, word);
//
//		}
//	}
//	
//	private void moveGrid(KeyCode code) {
//		GridFX nowFocused = this.lastFocused[0];
//		Word word = null;
//		int index = Grid.INVALID;
//		switch (code) {
//		case RIGHT:
//			word = nowFocused.grid.getHWord();
//			if (word != null) {
//				index = nowFocused.grid.getHIndex();
//				index++;
//				if (index == word.getLength())
//					index = 0;
//
//				Editor.focus(this.getGridFX(word.getGrid(index)));
//
//				if (BoardFX.lastHighlightOrien == Orientation.VERTICAL) {
//					changeHighlightOrien();
//				}
//			}
//			break;
//
//		case LEFT:
//			word = nowFocused.grid.getHWord();
//			if (word != null) {
//				index = nowFocused.grid.getHIndex();
//				index--;
//				if (index == Grid.INVALID)
//					index = word.getLength() - 1;
//
//				Editor.focus(this.getGridFX(word.getGrid(index)));
//
//				if (BoardFX.lastHighlightOrien == Orientation.VERTICAL) {
//					changeHighlightOrien();
//				}
//			}
//
//			break;
//
//		case UP:
//			word = nowFocused.grid.getVWord();
//			if (word != null) {
//				index = nowFocused.grid.getVIndex();
//				index--;
//				if (index == Grid.INVALID)
//					index = word.getLength() - 1;
//
//				Editor.focus(this.getGridFX(word.getGrid(index)));
//
//				if (BoardFX.lastHighlightOrien == Orientation.HORIZONTAL) {
//					changeHighlightOrien();
//				}
//			}
//			break;
//
//		case DOWN:
//			word = nowFocused.grid.getVWord();
//			if (word != null) {
//				index = nowFocused.grid.getVIndex();
//				index++;
//				if (index == word.getLength())
//					index = 0;
//
//				Editor.focus(this.getGridFX(word.getGrid(index)));
//
//				if (BoardFX.lastHighlightOrien == Orientation.HORIZONTAL) {
//					changeHighlightOrien();
//				}
//			}
//			break;
//		default:
//			break;
//
//		}
//
//	}
//	
//	private void traverseWord() {
//		GridFX lastFocused = this.lastFocused[0];
//		Word word = null;
//		int index = Grid.INVALID;
//
//		switch (BoardFX.lastHighlightOrien) {
//		case HORIZONTAL:
//			word = lastFocused.grid.getHWord();
//			index = lastFocused.grid.getHIndex();
//			break;
//		case VERTICAL:
//			word = lastFocused.grid.getVWord();
//			index = lastFocused.grid.getVIndex();
//			break;
//		default:
//			break;
//		}
//		if (word != null) {
//			index++;
//			if (index == word.getLength())
//				index = 0;
//
//			Editor.focus(this.getGridFX(word.getGrid(index)));
//
//		} else {
//			moveToNextGrid();
//		}
//	}
	
	
}
