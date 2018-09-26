package main.java.editor.exception;

import main.java.editor.fx.InfoMessageEng;

public class EmptyGridException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5171486177607632287L;

	public EmptyGridException() {
		super(InfoMessageEng.EMPTY_GRID);
	}
}
