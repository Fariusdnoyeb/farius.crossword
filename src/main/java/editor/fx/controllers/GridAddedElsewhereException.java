package main.java.editor.fx.controllers;

import main.java.editor.fx.InfoMessageEng;

public class GridAddedElsewhereException extends Exception{
	public GridAddedElsewhereException() {
		super(InfoMessageEng.ADDING_GRID_CONFLICT);
	}
}
