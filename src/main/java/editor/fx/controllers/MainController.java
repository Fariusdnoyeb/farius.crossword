package main.java.editor.fx.controllers;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;

import main.java.editor.fx.BoardFX;
import main.java.editor.fx.BoardTab;
import main.java.editor.fx.BoardTabContent;
import main.java.editor.fx.Editor;
import main.java.editor.fx.GridFX;

public class MainController {
	
	@FXML TabPane tabPane;
	BoardTab selectedTab;
	
	@FXML MenuItem clearMenuItem;
	@FXML MenuItem addWordMenuItem;
	
	@FXML HBox boardCursorStatus;
	@FXML Label currentRow, currentCol, currentWord;
	@FXML Label infoBar;
	@FXML Button testButton;
	
	private final BooleanProperty noTab = new SimpleBooleanProperty(true);

	
	@FXML private void initialize() {
		
		ObservableList<Tab> tabs = tabPane.getTabs();
		tabs.addListener(new InvalidationListener() {
			@Override
			public void invalidated(Observable arg0) {
				if (tabs.isEmpty())
					noTab.set(true);
				else
					noTab.set(false);
			}
			
		});

		boardCursorStatus.disableProperty().bind(noTab);
		clearMenuItem.disableProperty().bind(noTab);
		addWordMenuItem.disableProperty().bind(noTab);
		
		tabPane.getSelectionModel().selectedItemProperty().addListener((c, oldTab, newTab) -> {
			if (newTab != null) {
				BoardFX board = ((BoardTab) newTab).getTabContent().getBoardFX();
				GridFX focused = board.getFocusedGrid();
				if (focused != null) {
					Editor.updateFocusedGrid(focused);
				}
				Platform.runLater(new Runnable() {
	                @Override
	                public void run() {
	                    board.requestFocus();
	                }
	            });
			}
		});
		
		currentRow.textProperty().bind(Editor.getFocusedRowIndexProperty().add(1).asString());
		currentCol.textProperty().bind(Editor.getFocusedColIndexProperty().add(1).asString());
		
//		addMouseEventEnterAndExit(testButton, "test button: mouse happens");
		
		infoBar.textProperty().bind(Editor.getInfoProperty());
	}
	
	private void addMouseEventEnterAndExit(Node node, String infoMessage) {
		node.setOnMouseEntered(e -> {
			Editor.setInfo(infoMessage);
		});
		node.setOnMouseExited(e -> {
			Editor.clearInfo();
		});
	}
	
	
	@FXML private void loadTemplate() throws Exception{
		selectedTab = (BoardTab) tabPane.getSelectionModel().getSelectedItem();
		selectedTab.getTabContent().loadTempalte();
	}

	@FXML private void newBoard() {

		BoardTab newTab = new BoardTab("Untitled " + tabPane.getTabs().size(), new BoardTabContent());
		tabPane.getTabs().add(newTab);
		tabPane.getSelectionModel().select(newTab);
		
		selectedTab = (BoardTab) tabPane.getSelectionModel().getSelectedItem();
	}

	@FXML private void closeSelectedTab() {
		tabPane.getTabs().remove(selectedTab);
		selectedTab = (BoardTab) tabPane.getSelectionModel().getSelectedItem();
	}
	
	@FXML private void clearBoard() {
		Editor.clearBoard(selectedTab.getTabContent().getBoardFX());
	}
	
	@FXML private void new15() {
		newBoard();
		selectedTab.getTabContent().makeBoard();
	}
	
	@FXML private void saveAsTemplate() {
		selectedTab.getTabContent().saveAsTemplate();
	}
}
