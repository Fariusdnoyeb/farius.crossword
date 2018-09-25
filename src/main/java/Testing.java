package main.java;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import main.java.editor.EditableBoard;
import main.java.editor.fx.BoardFX;
import main.java.editor.fx.Editor;
import main.java.editor.fx.Template;


public class Testing extends Application {
	protected static final int	HORIZONTAL	= 0;
	protected static final int	VERTICAL	= 1;
	
	Stage window;
	boolean focused;
	
	public static void main(String[] args) {

		launch(args);

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		window = primaryStage;

		BoardFX boardFX = new BoardFX();;
		
//Making a prototype template
	

//		Template template = new Template(15, 15);
//		template.addBlackCoor(0, 4);
//		template.addBlackCoor(1, 4);
//		template.addBlackCoor(0, 10);
//		template.addBlackCoor(1, 10);
//		template.addBlackCoor(14, 4);
//		template.addBlackCoor(13, 4);
//		template.addBlackCoor(14, 10);
//		template.addBlackCoor(13,10);
//		template.addBlackCoor(3, 3);
//		template.addBlackCoor(3, 7);
//		template.addBlackCoor(4, 6);
//		template.addBlackCoor(5, 5);
//		template.addBlackCoor(6, 4);
//		template.addBlackCoor(7, 3);
//		template.addBlackCoor(5, 0);
//		template.addBlackCoor(5, 1);
//		template.addBlackCoor(10, 0);
//		template.addBlackCoor(10, 1);
//		template.addBlackCoor(10, 2);
//		template.addBlackCoor(5, 9);
//		template.addBlackCoor(4, 12);
//		template.addBlackCoor(4, 13);
//		template.addBlackCoor(4, 14);
//		template.addBlackCoor(7, 11);
//		template.addBlackCoor(8, 10);
//		template.addBlackCoor(9, 9);
//		template.addBlackCoor(10, 8);
//		template.addBlackCoor(11, 7);
//		template.addBlackCoor(11, 11);
//		template.addBlackCoor(9, 14);
//		template.addBlackCoor(9, 13);
//		template.addBlackCoor(9, 5);
//
//		
//		template.addClueCoor(0, 0);
//		template.addClueCoor(0, 1);
//		template.addClueCoor(0, 2);
//		template.addClueCoor(0, 3);
//		template.addClueCoor(0, 5);
//		template.addClueCoor(0, 6);
//		template.addClueCoor(0, 7);
//		template.addClueCoor(0, 8);
//		template.addClueCoor(0, 9);
//		template.addClueCoor(0, 11);
//		template.addClueCoor(0, 12);
//		template.addClueCoor(0, 13);
//		template.addClueCoor(0, 14);
//		template.addClueCoor(1, 0);
//		template.addClueCoor(1, 5);
//		template.addClueCoor(1, 11);
//		template.addClueCoor(2, 0);
//		template.addClueCoor(2, 4);
//		template.addClueCoor(2, 10);
//		template.addClueCoor(3, 0);
//		template.addClueCoor(3, 4);
//		template.addClueCoor(3, 8);
//		template.addClueCoor(4, 0);
//		template.addClueCoor(4, 3);
//		template.addClueCoor(4, 7);
//		template.addClueCoor(5, 2);
//		template.addClueCoor(5, 6);
//		template.addClueCoor(5, 10);
//		template.addClueCoor(5, 12);
//		template.addClueCoor(5, 13);
//		template.addClueCoor(5, 14);
//		template.addClueCoor(6, 0);
//		template.addClueCoor(6, 1);
//		template.addClueCoor(6, 5);
//		template.addClueCoor(6, 9);
//		template.addClueCoor(7, 0);
//		template.addClueCoor(7, 4);
//		template.addClueCoor(7, 12);
//		template.addClueCoor(8, 0);
//		template.addClueCoor(8, 3);
//		template.addClueCoor(8, 11);
//		template.addClueCoor(9, 0);
//		template.addClueCoor(9, 6);
//		template.addClueCoor(9, 10);
//		template.addClueCoor(10, 3);
//		template.addClueCoor(10, 5);
//		template.addClueCoor(10, 9);
//		template.addClueCoor(10, 13);
//		template.addClueCoor(10, 14);
//		template.addClueCoor(11, 0);
//		template.addClueCoor(11, 1);
//		template.addClueCoor(11, 2);
//		template.addClueCoor(11, 8);
//		template.addClueCoor(11, 12);
//		template.addClueCoor(12, 0);
//		template.addClueCoor(12, 7);
//		template.addClueCoor(12, 11);
//		template.addClueCoor(13, 0);
//		template.addClueCoor(13, 5);
//		template.addClueCoor(13, 11);
//		template.addClueCoor(14, 0);
//		template.addClueCoor(14, 5);
//		template.addClueCoor(14, 11);
//		
//		try (ObjectOutputStream outStream = 
//				new ObjectOutputStream(new FileOutputStream("template_prototype.cwtpl"))) {
//
//			outStream.writeObject(template);
//
//		} catch (IOException e) {
//			System.out.println("Exception during serialization: " + e);
//		}
		
		
//Loading template		
//		
//		Template template;
//		try (ObjectInputStream inStream 
//				= new ObjectInputStream(new FileInputStream("template_prototype.cwtpl"))) {
//			
//			template = (Template)inStream.readObject();
//			Editor.loadTemplate(template, boardFX);
//			
//		} catch (IOException e)	{
//			System.out.println("Exception during deserialization: " + e);
//		}
		
		
//Testing AI
		
//		board.getGrid(0, 4).blacken();
//		board.getGrid(1, 4).blacken();
//		board.getGrid(0, 10).blacken();
//		board.getGrid(1, 10).blacken();
//		board.getGrid(14, 4).blacken();
//		board.getGrid(13, 4).blacken();
//		board.getGrid(14, 10).blacken();
//		board.getGrid(13, 10).blacken();
//		board.getGrid(3, 3).blacken();
//		board.getGrid(3, 7).blacken();
//		board.getGrid(4, 6).blacken();
//		board.getGrid(5, 5).blacken();
//		board.getGrid(6, 4).blacken();
//		board.getGrid(7, 3).blacken();
//		board.getGrid(5, 0).blacken();
//		board.getGrid(5, 1).blacken();
//		board.getGrid(10, 0).blacken();
//		board.getGrid(10, 1).blacken();
//		board.getGrid(10, 2).blacken();
//		board.getGrid(5, 9).blacken();
//		board.getGrid(4, 12).blacken();
//		board.getGrid(4, 13).blacken();
//		board.getGrid(4, 14).blacken();
//		board.getGrid(7, 11).blacken();
//		board.getGrid(8, 10).blacken();
//		board.getGrid(9, 9).blacken();
//		board.getGrid(10, 8).blacken();
//		board.getGrid(11, 7).blacken();
//		board.getGrid(11, 11).blacken();
//		board.getGrid(9, 14).blacken();
//		board.getGrid(9, 13).blacken();
//		board.getGrid(9, 5).blacken();

//		ArrayList<Word> wordBank = new ArrayList<Word>();
//		String[] wordList = {"mist", "lime", "snicker", "paladin", "caramel",
//				"leaven", "pumpernickel", "coral", "fjord", "plague", "piston", 
//				"lip", "dawn", "saffron", "coda"};
//		for (int i = 0; i < wordList.length; i++) {
//			wordBank.add(new Word(wordList[i]));
//		}
//		
//		board.placeList(wordBank);
//		System.out.println(board.toString());
//		System.out.println(board.getWordsPlaced());
//		board.reveal();
//		board.setAlignment(Pos.CENTER);
		
		
		
		
		BorderPane bp = new BorderPane();
		
		
//		TextField tf = new TextField("Testing");
//		bp.setTop(tf);
		
		bp.setCenter(boardFX);
		
		Scene scene = new Scene(bp, 600, 600);
		
		window.setScene(scene);
		window.show();
	}

}