package view_model;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientSim {
	public volatile boolean stop;
	public static PrintWriter out;

	public void connect(String ip, int port) {
		new Thread(() -> {
			try {
				Socket socket = new Socket(ip, port);
				out = new PrintWriter(socket.getOutputStream());
				System.out.println("client connected to simulator");
				while (!stop) {

				}
				out.println("bye");
				out.close();
				socket.close();
			} catch (IOException e) {
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
}
