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
	static PrintWriter out, Pout, Sout;
	Double[][] mat;
	Double initx, inity, scale;
	public volatile boolean stop;
//	public static PrintWriter out;
	public static Scanner in, Pin, Sin;
	private Double lat;
	private Double longi;
	static Socket socket, Psocket, ServerSocket;
	Double offSet = 0.00165;
	Double InitX = -158.020959;
	Double initY = 21.443738;
	Integer markX, markY;
	String[] solution;
	// Server server;

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

//	public void reCalc() {
//		int i = 0, j = 0;
//		for (i = 0; i < mat.length; i++) {
//			for (j = 0; j < mat[i].length - 1; j++) {
//				out.print(mat[i][j] + ",");
////				out.flush();
//			}
//			out.println(mat[i][j]);
//		}
//		out.println("end");
//		out.println(initx + "," + inity);
////		goal
//		out.flush();
//	}

	public void calcPath(String ip, int port, Double[][] mat, Double markX, Double markY) {
		connect2server(ip, port);
		this.markX = (int) Math.abs(Math.floor(markX));
		this.markY = (int) Math.abs(Math.floor(markY));
		this.mat = mat;
		int longitude = (int) Math.abs(Math.floor(longi));
		int latitude = (int) Math.abs(Math.floor(lat));
		new Thread(() -> {

			int j, i;
			System.out.println("\tsending problem...");
			for (i = 0; i < mat.length; i++) {
				System.out.print("\t");
				for (j = 0; j < mat[i].length - 1; j++) {
					Sout.print(mat[i][j] + ",");
				}
				Sout.println(mat[i][j]);
			}
			Sout.println("end");
			Sout.println( latitude + "," + longitude );
			Sout.println(this.markY + "," + this.markX);
			Sout.flush();
			String usol = null;
			usol = Sin.nextLine();
			System.out.println("\tsolution received");
			System.out.println(usol);
			String[] temp = usol.split(",");
			this.solution = temp;
			String[] notfiy = new String[temp.length + 1];
			notfiy[0] = "path";
			for (i = 0; i < temp.length; i++)
				notfiy[i + 1] = temp[i];
			this.setChanged();
			this.notifyObservers(notfiy);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();
	}

	public void connect2server(String ip, int port) {
		try {
			ServerSocket = new Socket(ip, port);
			Sout = new PrintWriter(ServerSocket.getOutputStream());
			Sin = new Scanner(ServerSocket.getInputStream());
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
		try {
			socket = new Socket(ip, port);
			out = new PrintWriter(socket.getOutputStream());
			in = new Scanner(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void send(Double ailron, Double elevator, Double throttle, Double rudder) {
		out.println("set /controls/flight/aileron " + ailron + " \n");
		out.println("set /controls/flight/elevator " + elevator + " \n");
		out.println("set /controls/engines/current-engine/throttle " + throttle + " \n");
		out.println("set /controls/flight/rudder " + rudder + " \n");
		out.flush();
//		System.out.println("set " + path + " " + value);
	}

	public void LoadData() {
		new Thread(() -> {
			try {
				Psocket = new Socket("127.0.0.1", 5402);
				Pout = new PrintWriter(Psocket.getOutputStream());
				Pin = new Scanner(Psocket.getInputStream());

				while (!stop) {
					getPlanePos();
					Thread.sleep(250);
				}

			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		}).start();
	}

	public void getPlanePos() {

		if (out != null) {

			Pout.println("get /position/latitude-deg");
			Pout.flush();
			String s = Pin.nextLine();
			Mlatitude = Double.parseDouble(s.split("'")[1]);
//			Pin.nextLine();
			Pout.println("get /position/longitude-deg");
			Pout.flush();
			String s1 = Pin.nextLine();
			Mlongitude = Double.parseDouble(s1.split("'")[1]);
//			System.out.println("lat: " + lat + "\n");
//			System.out.println("long: " + longi + "\n");
			coordsToPos(Mlongitude, Mlatitude);
		}

	}

	public void coordsToPos(Double x, Double y) {
		lat = -1 * ((y - initY) / offSet) + 1;
		longi = ((x - InitX) / offSet) + 1;
		setChanged();
		notifyObservers();
	}

}
