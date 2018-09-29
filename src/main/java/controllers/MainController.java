package main.java.controllers;

import java.io.File;
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
import javafx.stage.Stage;
import main.java.editor.ExtractedWord;
import main.java.editor.fx.AddWordPrompt;
import main.java.editor.fx.BoardFX;
import main.java.editor.fx.BoardTab;
import main.java.editor.fx.BoardTabContent;
import main.java.editor.fx.Editor;
import main.java.editor.fx.GridFX;
import main.java.editor.fx.InfoMessageEng;
import main.java.editor.fx.InputHandler;
import main.java.editor.fx.Locator;
import main.java.editor.fx.UserInput;
import main.java.editor.fx.UserInputMode;


public class MainController {
	
	@FXML TabPane tabPane;
	BoardTab selectedTab;
	
	@FXML MenuItem saveAsTemplateMI;
	@FXML MenuItem exportGameMI;
	
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
	private Stage stage;
	private final Locator locator = new Locator();
	
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
		
		saveAsTemplateMI.disableProperty().bind(noTab);
		exportGameMI.disableProperty().bind(noTab);
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
		
		infoBar.textProperty().bind(Editor.getInfoProperty());
		
	}
	
	public void setStage(Stage stage) {
		this.stage = stage;
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
		
		AddWordPrompt prompt = new AddWordPrompt(boardFX.getFocusedGrid(), this.userInput.getScene().getWindow());
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
	@FXML private void loadTemplate() throws Exception {
		final File file = locator.getTemplate(stage.getScene().getWindow());
		
		if (file != null) {
			newBoard();
			selectedTab = (BoardTab) tabPane.getSelectionModel().getSelectedItem();
			selectedTab.getTabContent().loadTempalte(file);
		}
	}
	
	@FXML private void saveAsTemplate() {
		final File file = locator.setTemplate(stage.getScene().getWindow());
		
		if (file != null) {
			BoardFX boardFX = selectedTab.getTabContent().getBoardFX();
			Editor.saveAsTemplate(boardFX, file);
		}
	}
	
	@FXML private void importGame() throws Exception {
		final File file = locator.getGame(stage.getScene().getWindow());
		
		if (file != null) {
			newBoard();
			selectedTab = (BoardTab) tabPane.getSelectionModel().getSelectedItem();
			selectedTab.getTabContent().importGame(file);
		}
	}
	
	@FXML private void exportGame() {
		final File file = locator.setGame(stage.getScene().getWindow());
		
		if (file != null) {
			BoardFX boardFX = selectedTab.getTabContent().getBoardFX();
			Editor.exportGame(boardFX, file);
		}
	}
}
