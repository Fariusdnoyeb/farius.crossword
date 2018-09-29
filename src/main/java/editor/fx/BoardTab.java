package main.java.editor.fx;

import javafx.scene.control.Tab;

public class BoardTab extends Tab{
	private BoardTabContent content;
	
	public BoardTab(String label, BoardTabContent content) {

		this.setText(label);
		this.content = content;
		this.setContent(content);
		
	}

	public BoardTabContent getTabContent() {
		return this.content;
	}
	
}
