package main.java.editor.exception;

import main.java.editor.fx.InfoMessageEng;

public class BlackGridException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7056879847495128517L;

	public BlackGridException() {
		super(InfoMessageEng.BLACK_GRID);
	}
}
