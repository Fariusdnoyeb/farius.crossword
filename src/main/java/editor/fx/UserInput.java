package main.java.editor.fx;

import java.io.IOException;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class UserInput extends HBox{
	TextField textField;
	Button button;
	
	private	ChangeListener<String> listener;
	private InputHandler<String> inputHandler;
	
	public UserInput() {
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(getClass().getResource("/main/resources/view/UserInput.fxml"));
		
		fxmlLoader.setRoot(this);		
		try {
			fxmlLoader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		this.textField = (TextField)this.getChildren().get(0);
		this.button = (Button)this.getChildren().get(1);
		
		addEventListener();
	}
	
	public String getInput() {
		return this.textField.getText();
	}
	
	public void performAction(UserInputMode userInputMode, InputHandler<String> inputHandler) {
		this.inputHandler = inputHandler;
		switch (userInputMode) {
		case NUMBER:
			addNumericValidationListener();
		case TEXT:
			
		}
		textField.requestFocus();
	}
	
	public void addEventListener( ) {
		textField.setOnAction(e -> button.fire());
		button.setOnAction(e -> { 
			if (!(this.inputHandler == null)) {
				inputHandler.handle(textField.getText());
				textField.setText("");
				removeListenter();
				inputHandler = null;
			}
		});
	}
	
	private void addNumericValidationListener() {
		listener = (observable, oldVlue, newValue) -> {
			if (!( newValue).matches("\\d*")) {
				textField.setText(newValue.replaceAll("[^\\d]", ""));
			}
		};
		textField.textProperty().addListener(listener);
	}
	
	private void removeListenter() {
		textField.textProperty().removeListener(listener);
	}
	
}
