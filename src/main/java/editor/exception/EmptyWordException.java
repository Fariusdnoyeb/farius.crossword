package main.java.editor.exception;

import main.java.editor.fx.InfoMessageEng;

public class EmptyWordException extends Exception{
	public EmptyWordException() {
		super(InfoMessageEng.EMPTY_WORD);
	}
}
