package main.java.editor.fx;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyCodeCombination;

public class GridContextMenu extends ContextMenu{
	private MenuItem blacken;
	private MenuItem restore;
	
	BoardFX boardFX;
	
	public GridContextMenu(BoardFX boardFX) {
		super();
		this.boardFX = boardFX;
		
		blacken = new MenuItem();
		blacken.setOnAction(e -> blacken());
		
		restore = new MenuItem("Restore");
		restore.setOnAction(e -> restore());
		
		this.getItems().addAll(blacken, restore);
	}

	protected void relabel() {
		if (boardFX.lastFocused[0].isBlack()) {
			blacken.setText("Unblacken");
		} else {
			blacken.setText("Blacken");
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
	
	private void restore() {
		Editor.restoreSelectedGrids(boardFX);
	}
}
