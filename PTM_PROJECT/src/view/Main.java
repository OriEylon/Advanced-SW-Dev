package view;

import java.io.IOException;

import Model_Pack.Model;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import server_side.MyClientHandler;
import server_side.MySerialServer;
import view_model.ViewModel;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("MainWindow.fxml"));
//		Parent root = null;
		try {
			Parent root = loader.load();
			MainWindowController view = loader.getController();
			ViewModel viewModel = new ViewModel();
			Model model = new Model();
			MySerialServer server=new MySerialServer();
			server.open(3141, new MyClientHandler());
			viewModel.addObserver(view);
			model.addObserver(viewModel);
			viewModel.setM(model);
			view.setVm(viewModel);
			primaryStage.setTitle("Flight Gear Simulator");
			primaryStage.setScene(new Scene(root));
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		launch(args);
	}
}
