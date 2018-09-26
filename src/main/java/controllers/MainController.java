package main.java.controllers;

import java.util.concurrent.ExecutionException;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;

import main.java.editor.ExtractedWord;
import main.java.editor.fx.AddWordPrompt;
import main.java.editor.fx.BoardFX;
import main.java.editor.fx.BoardTab;
import main.java.editor.fx.BoardTabContent;
import main.java.editor.fx.Editor;
import main.java.editor.fx.GridFX;
import main.java.editor.fx.InfoMessageEng;
import main.java.editor.fx.InputHandler;
import main.java.editor.fx.UserInput;
import main.java.editor.fx.UserInputMode;


public class MainController {
	
	@FXML TabPane tabPane;
	BoardTab selectedTab;
	
	@FXML MenuItem closeTabMI;
	@FXML MenuItem clearBoardMI;
	@FXML MenuItem setClueNumberMI;
	@FXML MenuItem removeClueNumberMI;
	@FXML MenuItem addWordFromSelectedMI;
	@FXML MenuItem addWordMI;
	
	@FXML HBox boardCursorStatus;
	@FXML Label currentRow, currentCol, currentWord;
	@FXML Label infoBar;
	
	@FXML UserInput userInput;
	
	@FXML Button printButton;
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
		
		closeTabMI.disableProperty().bind(noTab);
		clearBoardMI.disableProperty().bind(noTab);
		setClueNumberMI.disableProperty().bind(noTab);
		removeClueNumberMI.disableProperty().bind(noTab);
		addWordFromSelectedMI.disableProperty().bind(noTab);
		addWordMI.disableProperty().bind(noTab);
		
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
	
	@FXML private void print() {
		BoardFX boardFX = selectedTab.getTabContent().getBoardFX();
		System.out.println(boardFX.getEditableBoard().getWords().toString());
		System.out.println(boardFX.getEditableBoard().toString());
		System.out.println(boardFX.getEditableBoard().getWords().size());
	}
	
//	private void addMouseEventEnterAndExit(Node node, String infoMessage) {
//		node.setOnMouseEntered(e -> {
//			Editor.setInfo(infoMessage);
//		});
//		node.setOnMouseExited(e -> {
//			Editor.clearInfo();
//		});
//	}

	@FXML private void closeSelectedTab() {
		tabPane.getTabs().remove(selectedTab);
		selectedTab = (BoardTab) tabPane.getSelectionModel().getSelectedItem();
	}
	
	@FXML private void clearBoard() {
		Editor.clearBoard(selectedTab.getTabContent().getBoardFX());
	}
//-----------------------------------------------------------------	
	@FXML private void setClueNumber() throws InterruptedException, ExecutionException {

		GridFX gridFX = selectedTab.getTabContent().getBoardFX().getFocusedGrid();

		InputHandler<String> inputHandler = (input) -> {
			boolean isSuccessful = false;
			if (!input.isEmpty() && Editor.setClueNumber(gridFX, Integer.parseInt(input)))
				isSuccessful = true;
			gridFX.requestFocus();
			return isSuccessful;
		};
		userInput.performAction(UserInputMode.NUMBER, inputHandler);
		Editor.setInfo(InfoMessageEng.SET_CLUE_INS);

	}
	
	@FXML private void removeClueNumber() {
		BoardFX boardFX = selectedTab.getTabContent().getBoardFX();
		Editor.removeClueNumberIfAny(boardFX.getFocusedGrid());
	}
	
//-----------------------------------------------------------------	
	@FXML private void addWordFromSelectedGrids() {
		BoardFX boardFX = selectedTab.getTabContent().getBoardFX();
		Editor.addWordFromSelectedGrids(boardFX);
	}
	@FXML private void addWordFromPrompt() throws Exception {
		BoardFX boardFX = selectedTab.getTabContent().getBoardFX();
		
		AddWordPrompt prompt = new AddWordPrompt(boardFX.getFocusedGrid(), userInput.getScene().getWindow());
		ExtractedWord extractedWord = prompt.prompt();
		
		Editor.addWordFromPrompt(extractedWord, boardFX);
	}
//-----------------------------------------------------------------	
	@FXML private void new15() {
		newBoard();
		selectedTab.getTabContent().makeBoard();
	}
	@FXML private void newBoard() {

		BoardTab newTab = new BoardTab("Untitled " + tabPane.getTabs().size(), new BoardTabContent());
		tabPane.getTabs().add(newTab);
		tabPane.getSelectionModel().select(newTab);
		
		selectedTab = (BoardTab) tabPane.getSelectionModel().getSelectedItem();
	}
//-----------------------------------------------------------------
	@FXML private void loadTemplate() throws Exception{
		selectedTab = (BoardTab) tabPane.getSelectionModel().getSelectedItem();
		selectedTab.getTabContent().loadTempalte();
	}
	
	@FXML private void saveAsTemplate() {
		BoardFX boardFX = selectedTab.getTabContent().getBoardFX();
		Editor.saveAsTemplate(boardFX);
	}
	
	@FXML private void importGame() throws Exception	{
		selectedTab = (BoardTab) tabPane.getSelectionModel().getSelectedItem();
		selectedTab.getTabContent().importGame();
	}
	
	@FXML private void exportGame() {
		BoardFX boardFX = selectedTab.getTabContent().getBoardFX();
		Editor.exportGame(boardFX);
	}
}

//private  transient BooleanProperty isAdded = new SimpleBooleanProperty(this, "isAdded", false);
//private  transient StringProperty content = new SimpleStringProperty(this, "content", "");
////private char content;
//private boolean isBlack;
//
//private int gridRow; //row index on board
//private int gridCol; //col index on board
//private Board board;
//
//private Word vWord; //vertical
//private Word hWord; //horizontal
//private int vIndex; //index in vWord
//private int hIndex; //index in hWord