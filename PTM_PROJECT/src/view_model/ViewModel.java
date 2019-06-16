package view_model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

import Model_Pack.Model;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ViewModel extends Observable implements Observer {
	Model m;
//	ClientSim client = new ClientSim();
	public StringProperty Script;
	public DoubleProperty VMaileron;
	public DoubleProperty VMelevator;
	public DoubleProperty VMrudder;
	public DoubleProperty VMthrottle;
	public DoubleProperty VMXcoordinate, VMYcoordinate;
	public DoubleProperty VMscale;
	public DoubleProperty VMlongitude, VMlatitude;
	String[][] smat;
	Double[][] dmat;

//	public Model getM() {
//		return m;
//	}

//	public ClientSim getClient() {
//		return client;
//	}

	public ViewModel(Model m) {
		this.m = m;
		Script = new SimpleStringProperty();
		VMrudder = new SimpleDoubleProperty();
		VMthrottle = new SimpleDoubleProperty();
		VMaileron = new SimpleDoubleProperty();
		VMelevator = new SimpleDoubleProperty();
		VMXcoordinate = new SimpleDoubleProperty();
		VMYcoordinate = new SimpleDoubleProperty();
		VMscale = new SimpleDoubleProperty();
		VMlongitude = new SimpleDoubleProperty();
		VMlatitude = new SimpleDoubleProperty();
		m.s.bind(Script);
		m.addObserver(this);
	}

	public void interpet(StringProperty s) {
		m.interpet(s.getValue());
	}

	public void connect(String ip, int port) {
		m.connect(ip, port);
	}

	public void readCSV(File file) {
		Double max = 0.0, min = 0.0;
		Double newmax = 255.0, newmin = 0.0;
		int size = 0;
		String firstline;
		try {
			Scanner scanner = new Scanner(file);
			ArrayList<String> arr = new ArrayList<>();
			firstline = scanner.nextLine();
			String[] line1 = firstline.split(",");
			VMYcoordinate.setValue(Double.parseDouble(line1[0]));
			VMXcoordinate.setValue(Double.parseDouble(line1[1]));
			line1 = scanner.nextLine().split(",");
			VMscale.setValue(Double.parseDouble(line1[0]));
			while (scanner.hasNextLine()) {
				arr.add(scanner.nextLine());
			}

			scanner.close();
			size = arr.size();
			smat = new String[size][];
			dmat = new Double[size][];
			for (int i = 0; i < size; i++) { // read all the file into a matrix
				smat[i] = arr.get(i).split(",");
			}

			for (int i = 0; i < size; i++) { // convert the strings into a Double matrix + get the max & min values
				dmat[i] = new Double[smat[i].length];
				for (int j = 0; j < smat[i].length; j++) {
					dmat[i][j] = Double.parseDouble(smat[i][j]);
					if (max < dmat[i][j])
						max = dmat[i][j];
					if (min > dmat[i][j])
						min = dmat[i][j];
				}
			}

			for (int i = 0; i < size; i++) { // normalize
				for (int j = 0; j < smat[i].length; j++) {
					dmat[i][j] = m.normalize(dmat[i][j], max, min, newmax, newmin);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		setChanged();
		notifyObservers(dmat);
	}

	public void send() {
		Double ailron = m.normalize(VMaileron.get(), 76.0, -76.0, 1.0, -1.0);
		Double elevator = (-1) * m.normalize(VMelevator.get(), 76.0, -76.0, 1.0, -1.0);
		// Double throttle = m.normalize(VMthrottle.get(), 76.0, -76.0, 1.0, -1.0);
		// Double rudder = m.normalize(VMrudder.get(), 76.0, -76.0, 1.0, -1.0);
//		System.out.println(ailron + " " + elevator + " " + VMthrottle.getValue() + " " + VMrudder.getValue());
		m.send(ailron, elevator, VMthrottle.getValue(), VMrudder.getValue());
	}

	public void calcPath(String ip, int port) {
		m.calcPath(ip, port, dmat, VMXcoordinate.get(), VMYcoordinate.get(), VMscale.get());
	}

//	public void getPlanePos() {
//
//		VMlatitude.setValue(m.getLat());
//		VMlongitude.setValue(m.getLongi());
//
//	}

	@Override
	public void update(Observable o, Object arg) {
		if (o.getClass() == Model.class) {
			VMlongitude.set(m.getLongi());
			VMlatitude.set(m.getLat());
			System.out.println("update:: " + "long: " + VMlongitude.getValue() + " lat:" + VMlatitude.getValue());
			setChanged();
			notifyObservers(VMlongitude);
		}
	}

}
