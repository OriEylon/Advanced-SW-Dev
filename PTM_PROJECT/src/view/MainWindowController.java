package view;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
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
	@FXML
	MapDisplayer mapDisplayer;
	@FXML
	PlaneDisplayer planeDisplayer;
	@FXML
	MarkDisplayer markDisplayer;
	Scanner scanner;
	StringProperty script, ip, port;
	DoubleProperty aileron;
	DoubleProperty elevator;
	Stage stage = new Stage();
	double orgSceneX, orgSceneY;
	double orgTranslateX, orgTranslateY;
	DoubleProperty Xcoordinate, Ycoordinate, scale, longitude, latitude, markX, markY;
	static String who = "";
	public static boolean clicked = false;
	Double[][] mat;
	DoubleProperty h, w;
	String[] solution;

	public MainWindowController() {
//		vm = new ViewModel(new Model());
//		vm.addObserver(this);
		vm = new ViewModel();
		script = new SimpleStringProperty();
		aileron = new SimpleDoubleProperty();
		elevator = new SimpleDoubleProperty();
		Xcoordinate = new SimpleDoubleProperty();
		Ycoordinate = new SimpleDoubleProperty();
		scale = new SimpleDoubleProperty();
		longitude = new SimpleDoubleProperty();
		latitude = new SimpleDoubleProperty();
		markX = new SimpleDoubleProperty();
		markY = new SimpleDoubleProperty();
		h = new SimpleDoubleProperty();
		w = new SimpleDoubleProperty();
		ip = new SimpleStringProperty();
		port = new SimpleStringProperty();
	}

	public void setVm(ViewModel viewmodel) {
		this.vm = viewmodel;
		vm.Script.bind(script);
		vm.VMaileron.bind(aileron);
		vm.VMelevator.bind(elevator);
		Xcoordinate.bind(vm.VMXcoordinate);
		Ycoordinate.bind(vm.VMYcoordinate);
		scale.bind(vm.VMscale);
		longitude.bind(vm.VMlongitude);
		latitude.bind(vm.VMlatitude);
		this.markX.bindBidirectional(vm.VMmarkX);
		this.markY.bindBidirectional(vm.VMmarkY);
		h.bindBidirectional(vm.VMh);
		w.bindBidirectional(vm.VMw);
		ip.bindBidirectional(vm.ip);
		port.bindBidirectional(vm.port);
	}

	public void autopilot() {
		select("autopilot");
	}

	public void exeAutopilot() {
		script = textarea.textProperty();
		vm.interpet(script);
	}

	public void load() {
		FileChooser fc = new FileChooser();
//		fc.setInitialDirectory(new File("C:\\Users\\Ori\\GitHub\\Advanced-SW-Dev\\PTM_PROJECT"));
		File selected = fc.showOpenDialog(null);
		if (selected != null) {
			try {
				scanner = new Scanner(new BufferedReader(new FileReader(selected)));
				while (scanner.hasNextLine()) {
					textarea.appendText(scanner.nextLine() + "\n");
				}
			} catch (FileNotFoundException e) {
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
		Throttle.valueProperty().addListener((ChangeListener<Number>) (observable, oldValue, newValue) -> {
			if (manual.isSelected())
				vm.send();
//				System.out.println("set throttle " + newValue.doubleValue());
		});

		Rudder.valueProperty().addListener((ChangeListener<Number>) (observable, oldValue, newValue) -> {
			if (manual.isSelected())
				vm.send();
//				System.out.println("set rudder " + newValue.doubleValue());
		});
	}

	public void connectButton() {
		who = "connect";
		Popup();
	}

	public void Popup() {
		Parent root;
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("popup.fxml"));
			root = fxmlLoader.load();
			MainWindowController fc = fxmlLoader.getController();
			fc.vm = this.vm;
			fc.mat = this.mat;
			fc.markX = this.markX;
			fc.markY = this.markY;
			fc.mapDisplayer = this.mapDisplayer;
			fc.h = this.h;
			fc.w = this.w;
			fc.ip = this.ip;
			fc.port = this.port;

			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.setTitle("Connect");
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void openButton() {
		ip.set(IP.getText());
		port.set(Port.getText());
		if (who.equals("connect")) {
			vm.connect(IP.getText(), Integer.parseInt(Port.getText()));
		} else {
			clicked = true;
			vm.calcPath(ip.get(), Integer.parseInt(port.get()), markX.get(), markY.get());
		}

		Stage stage = (Stage) open.getScene().getWindow();

		IP.clear();
		Port.clear();
		stage.close();
	}

	EventHandler<MouseEvent> circleOnMousePressedEventHandler = t -> {

		orgSceneX = t.getSceneX();
		orgSceneY = t.getSceneY();
		orgTranslateX = ((Circle) (t.getSource())).getTranslateX();
		orgTranslateY = ((Circle) (t.getSource())).getTranslateY();
//			System.out.println("X : " + ((Circle) (t.getSource())).getTranslateX() + " " + " " + "Y :  " + ((Circle) (t.getSource())).getTranslateY());
//			((Circle) (t.getSource())).toFront();
	};

	EventHandler<MouseEvent> circleOnMouseDraggedEventHandler = t -> {

		double offsetX = t.getSceneX() - orgSceneX;
		double offsetY = t.getSceneY() - orgSceneY;
		double newTranslateX = orgTranslateX + offsetX;
		double newTranslateY = orgTranslateY + offsetY;
		double circleInCenterX = (((Circle) (t.getSource())).getCenterX());
		double circleInCenterY = (((Circle) (t.getSource())).getCenterY());
		double circleOutRadius = circleOut.getRadius();
		double pi = Math.PI;
		double slant = Math
				.sqrt(Math.pow(newTranslateX - circleInCenterX, 2) + Math.pow(newTranslateY - circleInCenterY, 2));

		if (slant > circleOutRadius) {

			double alpha = Math.atan((newTranslateY - circleInCenterY) / (newTranslateX - circleInCenterX));
			if ((newTranslateX - circleInCenterX) < 0) {
				alpha += pi;

			}
			newTranslateX = Math.cos(alpha) * circleOutRadius + orgTranslateX;
			newTranslateY = Math.sin(alpha) * circleOutRadius + orgTranslateY;
			((Circle) (t.getSource())).setTranslateX(newTranslateX);
			((Circle) (t.getSource())).setTranslateY(newTranslateY);
//				System.out.println("x: " + newX + "  y: " + newY);
		} else {
			((Circle) (t.getSource())).setTranslateX(newTranslateX);
			((Circle) (t.getSource())).setTranslateY(newTranslateY);
//				System.out.println("x: " + newTranslateX + "  y: " + newTranslateY);
		}
//		System.out.println("X : " + newTranslateX + " " + " " + "Y :  " + newTranslateY);
		if (manual.isSelected()) {
			aileron.set(newTranslateX);
			elevator.set(newTranslateY);
			vm.send();
//				vm.getClient().send("/controls/flight/aileron", newTranslateY);
//				vm.getClient().send("/controls/flight/elevator", newTranslateX);
		}
	};

	EventHandler<MouseEvent> circleOnMouseReleaseEventHandler = t -> {

		((Circle) (t.getSource())).setTranslateX(((Circle) (t.getSource())).getCenterX());
		((Circle) (t.getSource())).setTranslateY(((Circle) (t.getSource())).getCenterY());
//			((Circle) (t.getSource())).toFront();
//			vm.getClient().send("/controls/flight/aileron", 0.0);
//			vm.getClient().send("/controls/flight/elevator", 0.0);

	};

	EventHandler<MouseEvent> MapPressed = event -> {
		markX.setValue(event.getX());
		markY.setValue(event.getY());
		markDisplayer.drawMark(markX.getValue(), markY.getValue());
//		draw path
		if (clicked) {
			vm.recalc(markX.get(), markY.get());
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

	public void OnMark() {
		markDisplayer.setOnMouseClicked(MapPressed);
	}

	public void LoadData() {
		FileChooser fc = new FileChooser();
		fc.setTitle("Load map");
		fc.getExtensionFilters().add(new ExtensionFilter("CSV files", "*.csv"));
//		fc.setInitialDirectory(new File("C:\\Users\\Ori\\GitHub\\Advanced-SW-Dev\\PTM_PROJECT"));
		File selected = fc.showOpenDialog(null);
		if (selected != null) {
			vm.readCSV(selected);
			vm.LoadData();

		}

	}

	public void calcPath() {
		who = "calcPath";
//		clicked = true;
		Popup();
	}

	public void drawLine() {
		double H = markDisplayer.getHeight();
		double W = markDisplayer.getWidth();
		double h = H / mat.length;
		double w = W / mat[0].length;
		double x = longitude.getValue() / w + 5;
		double y = latitude.getValue() * h - 10;
		GraphicsContext gc = markDisplayer.getGraphicsContext2D();
		String move = solution[1];

		for (int i = 2; i < solution.length; i++) {
			move = solution[i];
			switch (move) {
			case "Right":

				gc.strokeLine(x, y, x + w, y);
				x += w;
				break;
			case "Left":

				gc.strokeLine(x, y, x - w, y);
				x -= w;
				break;
			case "Up":

				gc.strokeLine(x, y, x, y - h);
				y -= h;
				break;
			case "Down":

				gc.strokeLine(x, y, x, y + h);
				y += h;
			}
		}

	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg.getClass() == Double[][].class) {
			this.mat = (Double[][]) arg;
			mapDisplayer.setMapData((Double[][]) arg);
			planeDisplayer.setMapdata((Double[][]) arg);
			markDisplayer.setMapData((Double[][]) arg);
			h.set(mapDisplayer.h);
			w.set(mapDisplayer.w);
//			planeDisplayer.drawPlane(21.0, 158.0);
		}

		else if (arg.getClass() == String[].class) {
			this.solution = (String[]) arg;
			this.drawLine();
		} else {
			planeDisplayer.drawPlane(longitude.getValue(), latitude.getValue());
//			System.out.println("view update:: longi: " + longitude.getValue() + " alt: " + latitude.getValue());
//			planeDisplayer.toFront();
		}

	}

}
