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
	ClientSim client = new ClientSim();
	public StringProperty Script;
	int Xcoordinate, Ycoordinate;
	int scale;
	public DoubleProperty VMaileron;
	public DoubleProperty VMelevator;
	public DoubleProperty VMrudder;
	public DoubleProperty VMthrottle;

	public Model getM() {
		return m;
	}

	public ClientSim getClient() {
		return client;
	}

	public ViewModel(Model m) {
		this.m = m;
		Script = new SimpleStringProperty();
		VMrudder = new SimpleDoubleProperty();
		VMthrottle = new SimpleDoubleProperty();
		VMaileron = new SimpleDoubleProperty();
		VMelevator = new SimpleDoubleProperty();
		m.s.bind(Script);
	}

	public void interpet(StringProperty s) {
		m.interpet(s.getValue());
	}

	public void connect(String ip, int port) {
		client.connect(ip, port);
	}

	public Double[][] readCSV(File file) {
		Double max = 0.0, min = 0.0;
		Double newmax = 255.0, newmin = 0.0;
		try {
			Scanner scanner = new Scanner(file);
			ArrayList<String> arr = new ArrayList<>();
			Ycoordinate = scanner.nextInt();
			Xcoordinate = scanner.nextInt();
			scale = scanner.nextInt();
			while (scanner.hasNextLine()) {
				arr.add(scanner.nextLine());
			}

			int size = arr.size();
			String[][] smat = new String[size][];
			Double[][] dmat = new Double[size][];
			for (int i = 0; i < size; i++) { // read all the file into a matrix
				smat[i] = arr.get(i).split(",");
			}

			for (int i = 0; i < size; i++) { // convert the strings into an int matrix + get the max & min values
				for (int j = 0; j < smat[j].length; j++) {
					dmat[i][j] = Double.parseDouble(smat[i][j]);
					if (max < dmat[i][j])
						max = dmat[i][j];
					if (min > dmat[i][j])
						min = dmat[i][j];
				}
			}

			for (int i = 0; i < size; i++) { // normalize
				for (int j = 0; j < smat[j].length; j++) {
					dmat[i][j] = m.normalize(dmat[i][j], max, min, newmax, newmin);
				}
			}
			scanner.close();
			return dmat;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void send() {
		Double ailron = m.normalize(VMaileron.get(), 76.0, -76.0, 1.0, -1.0);
		Double elevator =(-1)*m.normalize(VMelevator.get(), 76.0, -76.0, 1.0, -1.0);
		//Double throttle = m.normalize(VMthrottle.get(), 76.0, -76.0, 1.0, -1.0);
		//Double rudder = m.normalize(VMrudder.get(), 76.0, -76.0, 1.0, -1.0);
		System.out.println(ailron + " " +VMthrottle.getValue()+ " " +elevator+" "+VMrudder.getValue() );
		client.send(ailron, elevator, VMthrottle.getValue(), VMrudder.getValue());
	}

	@Override
	public void update(Observable o, Object arg) {

	}

}
