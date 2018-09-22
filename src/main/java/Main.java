package main.java;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
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

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/main/resources/view/Main.fxml"));
		root = loader.load();

	}
	@Override
	public void start(Stage primaryStage) throws Exception{
//		try {
//			BorderPane root = new BorderPane();
//			Scene scene = new Scene(root,400,400);
//			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
//			primaryStage.setScene(scene);
//			primaryStage.show();
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
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
