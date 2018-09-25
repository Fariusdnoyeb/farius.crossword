package main.java.editor.exception;

import main.java.editor.fx.InfoMessageEng;

public class BlackGridException extends Exception{
	public BlackGridException() {
		super(InfoMessageEng.BLACK_GRID);
	}
}
