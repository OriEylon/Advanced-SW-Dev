package server_side;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class MySerialServer implements Server {

	private int port;
	private ClientHandler ch;
	private volatile boolean stop;

	public MySerialServer() {
		stop = false;
	}

	public MySerialServer(int port) {
		this.port = port;
		stop = false;
	}

	@Override
	public void open(int port, ClientHandler ch) {
		this.port = port;
		this.ch = ch;
		new Thread(() -> {
			try {
				RunServer();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}).start();

	}

	@Override
	public void stop() {
		stop = true;
	}

	public void RunServer() throws IOException {
		ServerSocket server = new ServerSocket(port);
		server.setSoTimeout(3000);
//		int counter=0;
		while (!stop) { // counter<5..
			try {
				Socket aClient = server.accept(); // blocking call
				try {
					ch.handleClient(aClient.getInputStream(), aClient.getOutputStream());
					aClient.close();
//					counter++;
				} catch (IOException e) {
					System.out.println("invalid input/output");
				}
			} catch (SocketTimeoutException e) {
				System.out.println("time out,Goodbye");
			}
		}
		server.close();

	}

}
