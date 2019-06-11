package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = (BorderPane) FXMLLoader.load(getClass().getResource("MainWindow.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Flight_Sim GUI");
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
//		MainWindowController mwc=new MainWindowController();

	}

	public static void main(String[] args) {
		launch(args);
	}
}
