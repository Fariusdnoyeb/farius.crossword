package main.java.editor.fx;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import main.java.editor.EditableBoard;

public class BoardTabContent extends AnchorPane{
	
	protected static final int INVALID = -1;
	private static final int GRID_SIZE = 35;
	private static final int LETTER_SIZE = 20;
	
	@FXML VBox boardRow;
	@FXML HBox boardCol;
	@FXML BoardFX boardFX;
	@FXML ScrollPane boardScrollPane;
	@FXML VBox boardVBox;
	
	public BoardTabContent() {
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(getClass().getResource("/main/resources/view/BoardTabContent.fxml"));
		
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		
		try {
			fxmlLoader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	public BoardFX getBoardFX() {
		return this.boardFX;
	}
	
	public void loadTempalte() throws Exception{

		Editor.loadTemplate(boardFX);
		prepareUI();
		Editor.clearInfo();
		
	}
	
	public void importGame() throws Exception {
		
		Editor.importGame(boardFX);
		prepareUI();
		Editor.clearInfo();
		
	}
	
	public void makeBoard() {
		boardFX.make(new EditableBoard(15,15));
		prepareUI();
		Editor.clearInfo();
	}
	
//-----------------------------------------------------------------------------------------			
	private void prepareUI() {
		makeIndices();
		
		this.center(boardScrollPane.getViewportBounds(), boardVBox);
		boardScrollPane.viewportBoundsProperty().addListener((observable, oldValue, newValue) -> {
			this.center(newValue, boardVBox);
		});
		
	}
//-----------------------------------------------------------------------------------------	
	private StackPane generateIndex(int i) {
		Label label= new Label();
		label.setFont(new Font("Times New Roman", LETTER_SIZE));
		label.setText(Integer.toString(i+1));
		Rectangle rec = new Rectangle(GRID_SIZE, GRID_SIZE, Color.TRANSPARENT);
		return new StackPane(rec, label);
	}

	private void makeIndices() {
		int colSize = boardFX.getColSize();
		boardCol.getChildren().add(generateIndex(-1));
		for (int i = 0; i < colSize; i++) {
			boardCol.getChildren().add(generateIndex(i));
		}

		int rowSize = boardFX.getRowSize();
		for (int i = 0; i < rowSize; i++) {
			boardRow.getChildren().add(generateIndex(i));
		}
	}
//-----------------------------------------------------------------------------------------		
    private void center(Bounds viewPortBounds, Region centeredRegion) {
        double spWidth = viewPortBounds.getWidth();
        double spHeight = viewPortBounds.getHeight();
		double regionWidth = centeredRegion.getWidth();
		double regionHeight = centeredRegion.getHeight();

        if (spWidth > regionWidth) {
            centeredRegion.setTranslateX((spWidth - regionWidth) / 2);
        } else {
            centeredRegion.setTranslateX(0);
        }
        if (spHeight > regionHeight) {
            centeredRegion.setTranslateY((spHeight - regionHeight) / 2);
        } else {
            centeredRegion.setTranslateY(0);
        }

    }
    
}
