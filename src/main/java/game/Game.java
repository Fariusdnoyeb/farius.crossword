package main.java.game;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class Game {
	
	public static Board loadGame() throws ClassNotFoundException {
		try (ObjectInputStream inStream 
				= new ObjectInputStream(new FileInputStream("demo.cw"))) {
			Board board = (Board)inStream.readObject();
			return board;
		} catch (IOException e)	{
			System.out.println("Exception during deserialization: " + e);
		}
		return null;
	}
	
	public static void main(String[] args) {
		
		try {
						
			Board board = Game.loadGame();
			
			System.out.println(board.toString());
			System.out.println(board.getWords());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
