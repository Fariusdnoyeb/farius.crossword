package main.java.editor.exception;

import main.java.editor.fx.InfoMessageEng;

public class GridAddedElsewhereException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5461963422157495561L;

	public GridAddedElsewhereException() {
		super(InfoMessageEng.ADDING_GRID_CONFLICT);
	}
}
