package main.java.editor.fx;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;

import main.java.game.*;

public class GridFX extends StackPane {
//-----------------DATA MEMBERS---------------------------
	private static final int GRID_SIZE = 35;
	private static final int LETTER_SIZE = 20;

	protected Grid grid;

	protected BoardFX boardFX;

	protected Rectangle rec;
	protected Label label;

	protected boolean isSelected;

//-------------------CONSTRUCTORS--------------------------	
	public GridFX(BoardFX boardFX, int row, int col) {
		super();
		this.boardFX = boardFX;
		isSelected = false;

		this.grid = new Grid(boardFX.board, row, col);

		label = new Label();
		label.setFont(new Font("Times New Roman", LETTER_SIZE));
		rec = new Rectangle(GRID_SIZE, GRID_SIZE, Color.TRANSPARENT);
		rec.setStrokeType(StrokeType.INSIDE);
		rec.setStroke(Color.BLACK);

		this.setOnContextMenuRequested(event -> {
			if (this.boardFX.multiselectedGrids.isEmpty()) {
				Editor.focus(this);
			}
			this.boardFX.GRID_CONTEXT_MENU.relabel();
			this.boardFX.GRID_CONTEXT_MENU.show(this, Side.BOTTOM, 0, 0);
		});

		addMouseHandler();
		getChildren().addAll(rec, label);

	}

//------------------INSTANCE METHODS-----------------------
	public int getGridRow() {
		return this.grid.getGridRow();
	}

	public int getGridCol() {
		return this.grid.getGridCol();
	}

	public boolean isBlack() {
		return this.grid.isBlack();
	}

	public BoardFX getBoardFX() {
		return this.boardFX;
	}
	
	public boolean isNumbered() {
		return this.getChildren().size() == 3;
	}
//---------------------------------------------------------	
	public void setText(char letter) {
		this.label.setText(Character.toString(letter).toUpperCase());
	}

	public void setText(String letter) {
		this.label.setText(letter.toUpperCase());
	}

//---------------------------------------------------------
	private void addMouseHandler() {

		label.addEventHandler(MouseEvent.ANY, e -> rec.fireEvent(e));

		rec.setOnMouseEntered(e -> {
			if (this.boardFX.lastFocused[0] != this && !this.grid.isBlack() && !this.isSelected) {
				rec.setStrokeWidth(3.0);
				rec.setStroke(Color.GRAY);
			}
		});

		rec.setOnMouseExited(e -> {
			if (this.boardFX.lastFocused[0] != this && !this.isSelected) {
				Editor.unfocus(this);
			}
		});

		rec.setOnMouseClicked(e -> {
			if (e.getButton() == MouseButton.PRIMARY) {
				primaryMouseClickHandler(e);
			}
		});

	}

//---------------------------------------------------------
	private void primaryMouseClickHandler(MouseEvent e) {
		if (e.isShiftDown() && Editor.mode == Mode.EDIT) {
			Editor.shiftSelect(this.boardFX.lastFocused[0], this);
		} else if (e.isControlDown() && Editor.mode == Mode.EDIT) {
			Editor.ctrlSelect(this);
		} else {
			if (!this.boardFX.multiselectedGrids.isEmpty()) {
				Editor.deSelect(this.boardFX);
			}

			switch (Editor.mode) {
			case EDIT:
				Editor.focus(this);
				break;
			case PREVIEW:
				if (!this.isBlack()) {
					if (this.boardFX.lastFocused[0] == this) {
						Editor.changeHighlightOrien(this.boardFX);
						return;
					}

					Editor.focus(this);

					if (this.grid.getHWord() != null) {
						Editor.highlight(this.boardFX, this.grid.getHWord());
					} else if (grid.getVWord() != null) {
						Editor.highlight(this.boardFX, this.grid.getVWord());
					} else {
						Editor.unHighlight(this.boardFX);
					}
					break;
				}
			}
		}
	}
//---------------------------------------------------------	

}
