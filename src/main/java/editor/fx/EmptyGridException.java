package main.java.editor.fx;

public class EmptyGridException extends Exception{
	public EmptyGridException() {
		super(InfoMessageEng.ADDING_GRID_EMPTY);
	}
}
