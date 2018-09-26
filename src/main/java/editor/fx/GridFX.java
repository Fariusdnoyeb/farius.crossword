package main.java.editor.fx;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Side;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;

import main.java.game.*;

public class GridFX extends StackPane implements Comparable<GridFX>{
//-----------------DATA MEMBERS---------------------------
	private static Stop[] stops = new Stop[] { new Stop(0, Color.WHITE), new Stop(1, Color.ORANGE) };
	private static LinearGradient lg = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops);
	
	private static final int GRID_SIZE = 35;
	private static final int LETTER_SIZE = 20;

	protected Grid grid;

	protected BoardFX boardFX;

	protected Rectangle rec;
	protected Label label;

	protected final BooleanProperty isSelected = new SimpleBooleanProperty(this, "isSelected", false);

//-------------------CONSTRUCTORS--------------------------	
	public GridFX(BoardFX boardFX, Grid grid) {
		super();
		this.boardFX = boardFX;
		this.grid = grid;
		
		label = new Label();
		label.setFont(new Font("Times New Roman", LETTER_SIZE));
		label.textProperty().bindBidirectional((this.grid.getContentProperty()));
		rec = new Rectangle(GRID_SIZE, GRID_SIZE, Color.TRANSPARENT);
		rec.setStrokeType(StrokeType.INSIDE);
		rec.setStroke(Color.BLACK);
		label.setText(grid.getContent());
		if (grid.isBlack()) {
			Editor.blacken(this);
		}
		if (grid.isAdded()) {
			rec.setFill(Color.LIGHTGREY);
		}
	
		this.setOnContextMenuRequested(event -> {
			if (!this.boardFX.isMultiSelected()) {
				Editor.focus(this);
			}
			this.boardFX.GRID_CONTEXT_MENU.relabel();
			this.boardFX.GRID_CONTEXT_MENU.show(this, Side.BOTTOM, 0, 0);
		});

		addMouseHandler();
		getChildren().addAll(rec, label);
		
		isSelected.addListener((observable, oldValue, newValue) -> {
			if (newValue == true) {
				rec.setFill(lg);
			} else {
				if (this.isAdded())
					rec.setFill(Color.LIGHTGREY);	
				 else if (this.isBlack())
					Editor.blacken(this);
				else
					rec.setFill(Color.TRANSPARENT);
			}
		});
		
		this.grid.addedProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue == true) {
				rec.setFill(Color.LIGHTGREY);
			} else {
				rec.setFill(Color.TRANSPARENT);
			}
		});
	}
//------------------INSTANCE METHODS-----------------------
	public String getText() {
		return this.label.getText();
	}
	
	public StringProperty getTextProperty() {
		return this.label.textProperty();
	}
	public int getGridRow() {
		return this.grid.getGridRow();
	}

	public int getGridCol() {
		return this.grid.getGridCol();
	}


	public BoardFX getBoardFX() {
		return this.boardFX;
	}
	
	public boolean isNumbered() {
		return this.getChildren().size() == 3;
	}
	
	public boolean isBlack() {
		return this.grid.isBlack();
	}

	public boolean isAdded() {
		return this.grid.isAdded();
	}
	
	public boolean isSelected() {
		return this.isSelected.get();
	}
//---------------------------------------------------------	

	public void setText(String letter) {
		this.label.setText(letter.toUpperCase());
	}

//---------------------------------------------------------
	private void addMouseHandler() {

		label.addEventHandler(MouseEvent.ANY, e -> rec.fireEvent(e));

		rec.setOnMouseEntered(e -> {
			if (this.boardFX.lastFocused[0] != this && !this.grid.isBlack() && !this.isSelected()) {
				rec.setStrokeWidth(3.0);
				rec.setStroke(Color.GRAY);
			}
		});

		rec.setOnMouseExited(e -> {
			if (this.boardFX.lastFocused[0] != this && !this.isSelected()) {
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
		if (e.isShiftDown()) {
			if (this.boardFX.isMultiSelected()) {
				Editor.deSelectAll(this.boardFX);
			}
			Editor.shiftSelect(this.boardFX.lastFocused[0], this);
		} else if (e.isControlDown()) {
			if (!this.isSelected()) 
				Editor.ctrlSelect(this);
			else 
				Editor.deSelect(this);
		} else {
			if (this.boardFX.isMultiSelected()) {
				Editor.deSelectAll(this.boardFX);
			}
			Editor.focus(this);
		}
	}
//---------------------------------------------------------	
	@Override
	public int compareTo(GridFX other) {
		if (this.getGridRow() < other.getGridRow()) 
			return -1;
		else if (this.getGridRow() > other.getGridRow()) 
			return 1;
		else {
			if (this.getGridCol() < other.getGridCol()) 
				return -1;
			else if (this.getGridCol() > other.getGridCol())
				return 1;
			else
				return 0;
		}
	}	
}
