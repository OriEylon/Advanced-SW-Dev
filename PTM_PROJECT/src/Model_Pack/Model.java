package Model_Pack;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Observable;
import java.util.Scanner;

import interpeter.Lexer;
import interpeter.interpeter;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Model extends Observable {
	interpeter i;
//	public ClientSim client=new ClientSim();
	public StringProperty s;
	Double Mlongitude, Mlatitude;
	static PrintWriter out;
	Double[][] mat;
	Double initx, inity, scale;
	public volatile boolean stop;
//	public static PrintWriter out;
	public static Scanner in;
	private Double lat;
	private Double longi;
//	Server server;

	public Model() {
		s = new SimpleStringProperty();
		i = new interpeter(s.toString());

	}

	public void interpet(String s) {
		i.setLexer(new Lexer(s));
		i.interpet();
	}

	public Double normalize(Double x, Double max, Double min, Double newmax, Double newmin) {
		return (x - min) / (max - min) * (newmax - newmin) + newmin;
	}

	public void coordsToPos(Double x, Double y) {
		lat = ((x - 21.443738) / 1.65) + 1;
		longi = ((x + 158.020959) / 1.65) + 1;
		setChanged();
		notifyObservers();
	}

	public void reCalc() {
		int i = 0, j = 0;
		for (i = 0; i < mat.length; i++) {
			for (j = 0; j < mat[i].length - 1; j++) {
				out.print(mat[i][j] + ",");
//				out.flush();
			}
			out.println(mat[i][j]);
		}
		out.println("end");
		out.println(initx + "," + inity);
//		goal
		out.flush();
	}

	public void calcPath(String ip, int port, Double[][] mat, Double initX, Double initY, Double scale) {
		this.initx = initX;
		this.inity = initY;
		this.scale = scale;
		try {
			Socket socket = new Socket(ip, port);
			out = new PrintWriter(socket.getOutputStream());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Double getLat() {
		return lat;
	}

	public Double getLongi() {
		return longi;
	}

	public void connect(String ip, int port) {
		new Thread(() -> {
			try {
				Socket socket = new Socket(ip, port);
				out = new PrintWriter(socket.getOutputStream());
				in = new Scanner(socket.getInputStream());
				System.out.println("client connected to simulator");
				while (!stop) {
					getPlanePos();
					Thread.sleep(250);
				}
				out.println("bye");
				out.close();
				socket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
	}

	public void send(Double ailron, Double elevator, Double throttle, Double rudder) {
		out.println("set /controls/flight/aileron " + ailron + " \n");
		out.println("set /controls/flight/elevator " + elevator + " \n");
		out.println("set /controls/engines/current-engine/throttle " + throttle + " \n");
		out.println("set /controls/flight/rudder " + rudder + " \n");
		out.flush();
//		System.out.println("set " + path + " " + value);
	}

	public void getPlanePos() {

		if (out != null) {
			Double lat, longi;
			out.println("get /position/latitude-deg");
			String s = in.nextLine();
			lat = Double.parseDouble(s.split("'")[1]);
			out.println("get /position/longitude-deg");
			String s1 = in.nextLine();
			longi = Double.parseDouble(s1.split("'")[1]);
//			System.out.println("lat: " + lat + "\n");
//			System.out.println("long: " + longi + "\n");
			coordsToPos(lat, longi);
		}

	}
}
