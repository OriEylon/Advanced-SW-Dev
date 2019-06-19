package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import Model_Pack.Model;
import interpeter.Parser;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
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
			viewModel.addObserver(view);
			model.addObserver(viewModel);
			viewModel.setM(model);
			view.setVm(viewModel);
			primaryStage.setTitle("Flight Gear Simulator");
			primaryStage.setScene(new Scene(root));
			primaryStage.show();
			MySerialServer server1 = new MySerialServer();
			server1.open(5400, (in, out) -> {
				boolean stop = false;
//				synchronized (ConnectCmd.wait) {
//					ConnectCmd.wait.notifyAll();
//				}
//				System.out.println("connected to simulator");
//				stop = false;
				Scanner scanner = new Scanner(new BufferedReader(new InputStreamReader(in)));
				String[] line;
				int i = 0;
				while (!stop) {
					i = 0;
					String a = scanner.nextLine();
//						System.out.println(a);
					line = a.split(",");
					for (String string : Parser.simVars) {
						Parser.symbolTable.get(string).setV(Double.parseDouble(line[i]));
//							System.out.println("server is reading " + string + "= " + line[i]);
						i++;
					}
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				scanner.close();
				server1.stop();
			});
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		launch(args);
	}
}
