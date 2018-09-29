package main.java.editor.fx;

import java.io.File;
import javafx.stage.FileChooser;
import javafx.stage.Window;

public class Locator {
//-----------------DATA MEMBERS---------------------------
	
	private static final File FACTORY_DIR = new File(System.getProperty("user.home"));
	private static File defaultDir = new File("/Users/Quang Phan/git/farius.crossword");
	private static File lastKnownDir;
	
// -------------------CONSTRUCTORS--------------------------
	
	public Locator() {
		if (defaultDir.exists()) {
			lastKnownDir = defaultDir;
		} else {
			lastKnownDir = FACTORY_DIR;
		}
	}
	
//------------------INSTANCE METHODS-----------------------
	public File setTemplate(Window window) {
		FileChooser fileChooser = createFileChooser(FilterMode.TEMPLATE);
		fileChooser.setTitle("Save Template");
		fileChooser.setInitialFileName(".cwtpl");
		File file = fileChooser.showSaveDialog(window);

		if (file != null) 
			lastKnownDir = file.getParentFile();
		
		return file;
	}
	
	public File setGame(Window window) {
		FileChooser fileChooser = createFileChooser(FilterMode.GAME);
		fileChooser.setTitle("Export Game");
		fileChooser.setInitialFileName(".cw");
		File file = fileChooser.showSaveDialog(window);

		if (file != null) 
			lastKnownDir = file.getParentFile();
		
		return file;
	}
	
	public File getTemplate(Window window) {
		FileChooser fileChooser = createFileChooser(FilterMode.TEMPLATE);
		fileChooser.setTitle("Load Template");
		File file = fileChooser.showOpenDialog(window);

		if (file != null) 
			lastKnownDir = file.getParentFile();
		
		return file;
	}
	
	public File getGame(Window window) {
		FileChooser fileChooser = createFileChooser(FilterMode.GAME);
		fileChooser.setTitle("Import Game");
		File file = fileChooser.showOpenDialog(window);

		if (file != null) 
			lastKnownDir = file.getParentFile();
		
		return file;
	}
	
	
	private FileChooser createFileChooser(FilterMode filterMode) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(lastKnownDir);
		fileChooser.getExtensionFilters().add(filterMode.getExtensionFilter());
		return fileChooser;
	}
//---------------------------------------------------------
	
}
