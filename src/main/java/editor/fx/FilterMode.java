package main.java.editor.fx;

import javafx.stage.FileChooser.ExtensionFilter;

public enum FilterMode {
//-----------------DATA MEMBERS---------------------------
	
	TEMPLATE ("crossword template files (*.cwtpl)", "*.cwtpl"),
	GAME ("crossword files (*.cw)", "*.cw");
	
	private ExtensionFilter extensionFilter;
	
// -------------------CONSTRUCTORS--------------------------
	
	private FilterMode(String extensionDisplayName, String extension) {
		extensionFilter = new ExtensionFilter(extensionDisplayName, extension);
	}
	
//------------------INSTANCE METHODS-----------------------
	public ExtensionFilter getExtensionFilter() {
		return this.extensionFilter;
	}
}
