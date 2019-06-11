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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}).start();
	}

	public void send(String path, Double value) {
		out.println("set " + path + " " + value);
		out.flush();
//		System.out.println("set " + path + " " + value);
	}
}
