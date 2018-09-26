package main.java.editor.exception;

import main.java.editor.fx.InfoMessageEng;

public class EmptyWordException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4877132622799040015L;

	public EmptyWordException() {
		super(InfoMessageEng.EMPTY_WORD);
	}
}
