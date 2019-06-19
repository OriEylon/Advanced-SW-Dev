package view;

import server_side.MyClientHandler;
import server_side.MySerialServer;

public class MainServer {

	public static void main(String[] args) {
		MySerialServer server = new MySerialServer();
		server.open(3141, new MyClientHandler());
		
	}

}
