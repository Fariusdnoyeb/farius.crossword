package main.java.game;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Grid implements Externalizable{
/**
	 * 
	 */
	private static final long serialVersionUID = -4435296803480407561L;

//-----------------DATA MEMBERS---------------------------
	public static final int INVALID = -1;
	
	private  transient BooleanProperty isAdded = new SimpleBooleanProperty(this, "isAdded", false);
	private  transient StringProperty content = new SimpleStringProperty(this, "content", "");
	private boolean isBlack;
	
	private int gridRow; //row index on board
	private int gridCol; //col index on board
	private Board board;
	
	private Word vWord; //vertical
	private Word hWord; //horizontal
	private int vIndex; //index in vWord
	private int hIndex; //index in hWord

//-------------------CONSTRUCTORS--------------------------
	public Grid(Board board, int row, int col) {
		vWord = hWord = null;
		vIndex = hIndex = INVALID;

		this.board = board;
		gridRow = row;
		gridCol = col;
		isBlack = false;
	}
	
	public Grid() {};
//------------------INSTANCE METHODS-----------------------
	public boolean isAdded() {
		return this.isAdded.get();
	}
	
	public BooleanProperty addedProperty() {
		return this.isAdded;
	}
	
	public void setAdded(boolean isAdded) {
		this.isAdded.set(isAdded);
	}
	
	public int getGridRow() {
		return this.gridRow;
	}
	
	public int getGridCol() {
		return this.gridCol;
	}
	
	public boolean isBlack() {
		return this.isBlack;
	}
	
	public void setBlack(boolean isBlack) {
		this.isBlack = isBlack;
	}
	
	public String getContent() {
		return this.content.get();
	}
	
	public StringProperty getContentProperty() {
		return this.content;
	}
	public void setContent(String character) {
		this.content.set(character.toUpperCase());
	}
	
	public Word getHWord() {
		return this.hWord;
	}
	public Word getVWord() {
		return this.vWord;
	}
	
	public int getHIndex() {
		return this.hIndex;
	}
	public int getVIndex() {
		return this.vIndex;
	}
	
	public void setWord(Word word, int index, Orientation orien) {
		switch (orien) {
		case HORIZONTAL:
			hWord = word;
			hIndex = index;
			break;
		case VERTICAL:
			vWord = word;
			vIndex = index;
			break;
		default:
			break;
		}
	}

//---------------------------------------------------------
//	private void writeObject(ObjectOutputStream outStream) throws IOException {
//		outStream.defaultWriteObject();
////		outStream.writeUTF(content.get());
//		outStream.writeBoolean(isAdded.get());
//	}
//	private void readObject(ObjectInputStream inStream) throws IOException, ClassNotFoundException {
//		inStream.defaultReadObject();
////		content.set(inStream.readUTF());
//		isAdded.set(inStream.readBoolean());
//	}
	
	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(board);
		out.writeObject(vWord);
		out.writeObject(hWord);
		
		out.writeInt(gridRow);
		out.writeInt(gridCol);
		out.writeInt(vIndex);
		out.writeInt(hIndex);
		
		out.writeBoolean(isBlack);
		
		out.writeUTF(content.get());
		out.writeBoolean(isAdded.get());
	}
	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		board = (Board)in.readObject();
		vWord = (Word)in.readObject();
		hWord = (Word)in.readObject();
		
		gridRow = in.readInt();
		gridCol = in.readInt();
		vIndex = in.readInt();
		hIndex = in.readInt();
		
		isBlack = in.readBoolean();
		
		content.set(in.readUTF());
		isAdded.set(in.readBoolean());
		
	}
}
