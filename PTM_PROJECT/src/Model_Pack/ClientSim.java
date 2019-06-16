package Model_Pack;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientSim {
	public volatile boolean stop;
	public static PrintWriter out;
	public static Scanner in;
	private Double lat;
	private Double longi;

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
			out.println("get /position/latitude-deg");
			String s = in.nextLine();
			lat = Double.parseDouble(s.split("'")[1]);
			out.println("get /position/longitude-deg");
			String s1 = in.nextLine();
			longi = Double.parseDouble(s1.split("'")[1]);

		}

	}
}
