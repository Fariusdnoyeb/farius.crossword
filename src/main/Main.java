package main;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import main.java.controllers.MainController;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Main extends Application {
	
	private Parent root;
	private static Stage activeStage;
	
	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void init() throws Exception {

	}
	
	@Override
	public void start(Stage primaryStage) throws Exception{

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/main/resources/view/Main.fxml"));
		root = loader.load();
		MainController controller = (MainController)loader.getController();
		controller.setStage(primaryStage);
		setActiveStage(primaryStage);
		getActiveStage().setTitle("CrosssWord");
		getActiveStage().setScene(new Scene(root));	
		getActiveStage().show();
		
	}

	public static Stage getActiveStage() {
		return activeStage;
	}

	public static void setActiveStage(Stage activeStage) {
		Main.activeStage = activeStage;
	}


}
