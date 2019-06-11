package view;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

import Model_Pack.Model;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import view_model.ViewModel;

public class MainWindowController implements Observer {
	ViewModel vm;
	@FXML
	Label AltLabel;
	@FXML
	TextArea textarea;
	@FXML
	RadioButton AutoPilot;
	@FXML
	RadioButton manual;
	@FXML
	Circle circleOut;
	@FXML
	Circle circleIn;
	@FXML
	Slider Throttle;
	@FXML
	Slider Rudder;
	@FXML
	TextField IP;
	@FXML
	TextField Port;
	@FXML
	Button open;
	Scanner scanner;
	StringProperty script;
//	@FXML
//	TextField VarBreaks, VarThrottle, VarHeading, VarAirspeed, VarRoll, VarPitch, VarRudder, VarAilron, VarElevetor,
//			VarAlt, VarRpm, VarH0;
	Stage stage = new Stage();

	public MainWindowController() {
		this.vm = new ViewModel(new Model());
		script = new SimpleStringProperty();
		vm.Script.bind(script);
	}

	public void autopilot() {
		select("autopilot");
	}

	public void exeAutopilot() {
		script = textarea.textProperty();
		vm.interpet(script);
	}

	public void setVm(ViewModel vm) {
		this.vm = vm;

	}

	public void load() {
		FileChooser fc = new FileChooser();
		File selected = fc.showOpenDialog(null);
		if (selected != null) {
			try {
				scanner = new Scanner(new BufferedReader(new FileReader(selected)));
				while (scanner.hasNextLine()) {
					textarea.appendText(scanner.nextLine() + "\n");
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void manualFlight() {
		select("manual");
	}

	public void select(String s) {
		if (s.equals("manual")) {
			if (AutoPilot.isSelected()) {
				AutoPilot.setSelected(false);
				manual.setSelected(true);
			}
			sliders();
		} else if (s.equals("autopilot")) {
			if (manual.isSelected()) {
				AutoPilot.setSelected(true);
				manual.setSelected(false);
			}
			exeAutopilot();
		}
	}

	public void sliders() {
		Throttle.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				vm.getClient().send("/controls/engines/current-engine/throttle", newValue.doubleValue());
//				System.out.println("set throttle " + newValue.doubleValue());
			}
		});

		Rudder.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				vm.getClient().send("/controls/flight/rudder", newValue.doubleValue());
//				System.out.println("set rudder " + newValue.doubleValue());
			}
		});
	}

	public void connectButton() {
		Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("popup.fxml"));
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.setTitle("Connect");
			stage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void openButton() {
		vm.connect(IP.getText(), Integer.parseInt(Port.getText()));
		Stage stage = (Stage) open.getScene().getWindow();
		IP.clear();
		Port.clear();
		stage.close();
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub

	}

}
