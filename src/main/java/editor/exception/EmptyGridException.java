package main.java.editor.exception;

import main.java.editor.fx.InfoMessageEng;

public class EmptyGridException extends Exception{
	public EmptyGridException() {
		super(InfoMessageEng.EMPTY_GRID);
	}
}
