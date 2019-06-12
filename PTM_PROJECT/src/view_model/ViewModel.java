package view_model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

import Model_Pack.Model;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ViewModel extends Observable implements Observer {
	Model m;
	public StringProperty Script;
	ClientSim client = new ClientSim();
	int Xcoordinate, Ycoordinate;
	int scale;

	public Model getM() {
		return m;
	}

	public ClientSim getClient() {
		return client;
	}

	public ViewModel(Model m) {
		this.m = m;
		Script = new SimpleStringProperty();
		m.s.bind(Script);
	}

	public void interpet(StringProperty s) {
		m.interpet(s.getValue());
	}

	public void connect(String ip, int port) {
		client.connect(ip, port);
	}

	public int[][] readCSV(File file) {
		int max = 0, min = 0, newmax = 255, newmin = 0;
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
			int[][] imat = new int[size][];
			for (int i = 0; i < size; i++) { // read all the file into a matrix
				smat[i] = arr.get(i).split(",");
			}

			for (int i = 0; i < size; i++) { // convert the strings into an int matrix + get the max & min values
				for (int j = 0; j < smat[j].length; j++) {
					imat[i][j] = Integer.parseInt(smat[i][j]);
					if (max < imat[i][j])
						max = imat[i][j];
					if (min > imat[i][j])
						min = imat[i][j];
				}
			}

			for (int i = 0; i < size; i++) { // normalize
				for (int j = 0; j < smat[j].length; j++) {
					imat[i][j] = m.normalize(imat[i][j], max, min, newmax, newmin);
				}
			}
			scanner.close();
			return imat;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void update(Observable o, Object arg) {

	}

}
