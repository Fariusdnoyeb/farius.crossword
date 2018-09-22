package main.java.editor.fx;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyCodeCombination;

public class GridContextMenu extends ContextMenu{
	private MenuItem blacken;
	private MenuItem setClueNumber;
	private MenuItem restore;
	
	BoardFX boardFX;
	
	public GridContextMenu(BoardFX boardFX) {
		super();
		this.boardFX = boardFX;
		
		blacken = new MenuItem();
		blacken.setOnAction(e -> blacken());
		
		setClueNumber = new MenuItem();
		setClueNumber.setOnAction(e -> setClueNumber());
		
		restore = new MenuItem("Restore");
		restore.setOnAction(e -> restore());
		
		this.getItems().addAll(blacken, setClueNumber, restore);
	}

	protected void relabel() {
		if (boardFX.lastFocused[0].isBlack()) {
			blacken.setText("Unblacken");
		} else {
			blacken.setText("Blacken");
		}
		
		if (boardFX.lastFocused[0].getChildren().size() == 3) {
			setClueNumber.setText("Remove Clue Number");
		} else {
			setClueNumber.setText("SetClueNumber...");
		}
	}
	private void blacken() {
		GridFX focus = boardFX.lastFocused[0];
		if (!focus.isBlack()) {
			Editor.blackenSelectedGrids(boardFX);
		} else {
			Editor.unblackenSelectedGrids(boardFX);
		}
	}
	
	private void setClueNumber() {
		
	}
	
	private void restore() {
		Editor.restoreSelectedGrids(boardFX);
	}
}
