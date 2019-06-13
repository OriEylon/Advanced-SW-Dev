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
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
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
import javafx.scene.input.MouseEvent;
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
	DoubleProperty aileron;
	DoubleProperty elevator;

	double orgSceneX, orgSceneY;
	double orgTranslateX, orgTranslateY;

	// @FXML
//	TextField VarBreaks, VarThrottle, VarHeading, VarAirspeed, VarRoll, VarPitch, VarRudder, VarAilron, VarElevetor,
//			VarAlt, VarRpm, VarH0;
	Stage stage = new Stage();

	public MainWindowController() {
		this.vm = new ViewModel(new Model());
		script = new SimpleStringProperty();
		aileron = new SimpleDoubleProperty();
		elevator = new SimpleDoubleProperty();
		vm.Script.bind(script);
		vm.VMaileron.bind(aileron);
		vm.VMelevator.bind(elevator);
		// vm.VMrudder.bind(Rudder.valueProperty());
		// vm.VMthrottle.bind(Throttle.valueProperty());
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
		fc.setInitialDirectory(new File("C:\\Users\\Ori\\GitHub\\Advanced-SW-Dev\\PTM_PROJECT"));
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
			vm.VMrudder.bind(Rudder.valueProperty());
			vm.VMthrottle.bind(Throttle.valueProperty());
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
				if (manual.isSelected())
					vm.send();
//				System.out.println("set throttle " + newValue.doubleValue());
			}
		});

		Rudder.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (manual.isSelected())
					vm.send();
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

	EventHandler<MouseEvent> circleOnMousePressedEventHandler = new EventHandler<MouseEvent>() {

		@Override
		public void handle(MouseEvent t) {

			orgSceneX = t.getSceneX();
			orgSceneY = t.getSceneY();
			orgTranslateX = ((Circle) (t.getSource())).getTranslateX();
			orgTranslateY = ((Circle) (t.getSource())).getTranslateY();
//			System.out.println("X : " + ((Circle) (t.getSource())).getTranslateX() + " " + " " + "Y :  " + ((Circle) (t.getSource())).getTranslateY());
//			((Circle) (t.getSource())).toFront();
		}
	};

	EventHandler<MouseEvent> circleOnMouseDraggedEventHandler = new EventHandler<MouseEvent>() {

		@Override
		public void handle(MouseEvent t) {

			double offsetX = t.getSceneX() - orgSceneX;
			double offsetY = t.getSceneY() - orgSceneY;
			double newTranslateX = orgTranslateX + offsetX;
			double newTranslateY = orgTranslateY + offsetY;
			double circleInCenterX = (((Circle) (t.getSource())).getCenterX());
			double circleInCenterY = (((Circle) (t.getSource())).getCenterY());
			double frameRadius = circleOut.getRadius();

			double slant = Math
					.sqrt(Math.pow(newTranslateX - circleInCenterX, 2) + Math.pow(newTranslateY - circleInCenterY, 2));

			if (slant > frameRadius) {

				double alpha = Math.atan((newTranslateY - circleInCenterY) / (newTranslateX - circleInCenterX));
				if ((newTranslateX - circleInCenterX) < 0) {
					alpha = alpha + Math.PI;

				}
				newTranslateX = Math.cos(alpha) * frameRadius + orgTranslateX;
				newTranslateY = Math.sin(alpha) * frameRadius + orgTranslateY;
				((Circle) (t.getSource())).setTranslateX(newTranslateX);
				((Circle) (t.getSource())).setTranslateY(newTranslateY);
//				System.out.println("x: " + newX + "  y: " + newY);
			} else {
				((Circle) (t.getSource())).setTranslateX(newTranslateX);
				((Circle) (t.getSource())).setTranslateY(newTranslateY);
//				System.out.println("x: " + newTranslateX + "  y: " + newTranslateY);
			}
			System.out.println("X : " + newTranslateX + " " + " " + "Y :  " + newTranslateY);
			if (manual.isSelected()) {
				aileron.set(newTranslateX);
				elevator.set(newTranslateY);
				vm.send();
//				vm.getClient().send("/controls/flight/aileron", newTranslateY);
//				vm.getClient().send("/controls/flight/elevator", newTranslateX);
			}
		}

	};

	EventHandler<MouseEvent> circleOnMouseReleaseEventHandler = new EventHandler<MouseEvent>() {

		@Override
		public void handle(MouseEvent t) {

			((Circle) (t.getSource())).setTranslateX(((Circle) (t.getSource())).getCenterX());
			((Circle) (t.getSource())).setTranslateY(((Circle) (t.getSource())).getCenterY());
//			((Circle) (t.getSource())).toFront();
//			vm.getClient().send("/controls/flight/aileron", 0.0);
//			vm.getClient().send("/controls/flight/elevator", 0.0);

		}
	};

	public void OnPress() {

		circleIn.setOnMousePressed(circleOnMousePressedEventHandler);

	}

	public void OnDragg() {

		circleIn.setOnMouseDragged(circleOnMouseDraggedEventHandler);

	}

	public void OnRelease() {

		circleIn.setOnMouseReleased(circleOnMouseReleaseEventHandler);

	}

	public void LoadData() {
		FileChooser fc = new FileChooser();
		fc.setTitle("Load map.csv");
		fc.setInitialDirectory(new File("C:\\Users\\Ori\\GitHub\\Advanced-SW-Dev\\PTM_PROJECT"));
		File selected = fc.showOpenDialog(null);
		if (selected != null) {
			vm.readCSV(selected);
		}
	}

	@Override
	public void update(Observable o, Object arg) {

	}

}
