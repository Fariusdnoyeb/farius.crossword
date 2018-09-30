package main.java.editor.fx;

import java.io.Serializable;
import java.util.ArrayList;

public class Template implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -602344054877657595L;

	static class Coordinate implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = -7301415089026872670L;
		private int row;
		private int col;
		
		public Coordinate() { }
		
		public Coordinate(int row, int col) {
			this.row = row;
			this.col = col;
		}
		
		public int getRow() {
			return row;
		}
		public void setRow(int row) {
			this.row = row;
		}
		public int getCol() {
			return col;
		}
		public void setCol(int col) {
			this.col = col;
		}
		
		public void setCoor(int row, int col) {
			this.row = row;
			this.col = col;
		}
		
	}
	
	private int rowSize;
	private int colSize;
	
	private ArrayList<Coordinate> numberedGrids;
	private ArrayList<Coordinate> blackGrids;
	
	public Template(int rowSize, int colSize) {
		this.rowSize = rowSize;
		this.colSize = colSize;
		numberedGrids = new ArrayList<Coordinate>();
		blackGrids = new ArrayList<Coordinate>();
	}
	
	public int getRowSize() {
		return rowSize;
	}

	public int getColSize() {
		return colSize;
	}

	public void addBlackCoor(int row, int col) {
		blackGrids.add(new Coordinate(row, col));
	}

	public void addNumberCoor(int row, int col) {
		numberedGrids.add(new Coordinate(row, col));
	}
	
	public void loadBlackAndNumberedGrids(BoardFX boardFX) {
		
		Coordinate coor;
		
		//sets black grids:
		for (int i = 0; i < blackGrids.size(); i++) {
			coor = blackGrids.get(i);
			Editor.blacken(boardFX.getGridFX(coor.getRow(), coor.getCol()));
		}
		
		//sets numbered grids:
		for (int i = 0; i < numberedGrids.size(); i++) {
			coor = numberedGrids.get(i);
			Editor.setClueNumber(boardFX.getGridFX(coor.getRow(), coor.getCol()),i + 1);
		}
		
	}
	
	
}
