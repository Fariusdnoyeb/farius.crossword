package main.java.editor.fx;

import java.io.IOException;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import main.java.editor.ExtractedWord;
import main.java.game.Orientation;
import main.java.game.Word;

public class AddWordPrompt extends VBox{
	
	private TextField wordTF;
	private TextField colTF;
	private TextField rowTF;
	private RadioButton horizontal;
	private CheckBox currentCoor;
	private Button submitButton;
	private Button cancelButton;
	
	private Window owner;
	
	private boolean isCanceled;
	
	public AddWordPrompt(GridFX focused, Window owner) {
		this.owner = owner;
		this.isCanceled = false;
		
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(getClass().getResource("/main/resources/view/AddWordPrompt.fxml"));
		
		fxmlLoader.setRoot(this);		
		try {
			fxmlLoader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		HBox hBox;
		hBox = ((HBox)this.getChildren().get(0));
		wordTF = (TextField)hBox.getChildren().get(1);
		
		hBox = ((HBox)this.getChildren().get(1));
		currentCoor = (CheckBox)hBox.getChildren().get(0);
		rowTF = (TextField)hBox.getChildren().get(3);
		colTF = (TextField)hBox.getChildren().get(5);
		
		hBox = ((HBox)this.getChildren().get(2));
		horizontal = (RadioButton)hBox.getChildren().get(1);
		
		hBox = ((HBox)this.getChildren().get(3));
		submitButton = (Button)hBox.getChildren().get(0);
		cancelButton = (Button)hBox.getChildren().get(1);
		
		rowTF.setText(Integer.toString(focused.getGridRow() + 1));
		colTF.setText(Integer.toString(focused.getGridCol() + 1));
		currentCoor.selectedProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue == true) {
				rowTF.setText(Integer.toString(focused.getGridRow() + 1));
				colTF.setText(Integer.toString(focused.getGridCol() + 1));
				rowTF.setDisable(true);
				colTF.setDisable(true);
			} else {
				rowTF.setDisable(false);
				colTF.setDisable(false);
			}
		});
		
	}
	
	public ExtractedWord prompt() {
		final Stage dialog = new Stage();
		
		dialog.setTitle("Word Addition Parameters");
		dialog.initOwner(this.owner);
		dialog.initModality(Modality.WINDOW_MODAL);
//		dialog.setX(owner.getX());
//		dialog.setY(owner.getY());
		
//		submitButton.setDefaultButton(true);
		submitButton.setOnAction(e -> {
			isCanceled = false;
			dialog.close();
		});
		
//		cancelButton.setDefaultButton(true);
		cancelButton.setOnAction(e -> {
			isCanceled = true;
			dialog.close();
		});
		
		addTextFieldListener();
		
		dialog.setScene(new Scene(this));
		dialog.showAndWait();
		
		String word = wordTF.getText();
		
		if (isCanceled || word.isEmpty() || colTF.getText().isEmpty() || rowTF.getText().isEmpty()) {
			return null;
		} else {
			
			Orientation orien; 
			if (horizontal.isSelected()) {
				orien = Orientation.HORIZONTAL;
			} else {
				orien = Orientation.VERTICAL;
			}
			
			int col = Integer.parseInt(colTF.getText());
			int row = Integer.parseInt(rowTF.getText());
			
			return new ExtractedWord(word.toUpperCase(), row - 1, col - 1, orien);
		}
		
	}
	
	private void addTextFieldListener() {
		colTF.textProperty().addListener((observable, oldVlue, newValue) -> {
			if (!( newValue).matches("\\d*")) {
				colTF.setText(newValue.replaceAll("[^\\d]", ""));
			}
		});
		
		rowTF.textProperty().addListener((observable, oldVlue, newValue) -> {
			if (!( newValue).matches("\\d*")) {
				rowTF.setText(newValue.replaceAll("[^\\d]", ""));
			}
		});
	}
}
