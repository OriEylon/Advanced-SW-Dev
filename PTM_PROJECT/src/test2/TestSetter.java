package test2;

import server_side.MySerialServer;
import server_side.MyTestClientHandler;
import server_side.Server;

public class TestSetter {
	
	
	static Server s; 
	
	public static void runServer(int port) {
		// put the code here that runs your server
		// for example:
		s=new MySerialServer(port); 
		s.open(port,new MyTestClientHandler());
		
	}

	public static void stopServer() {
		// put the code here that stops your server
		// for example:
		s.stop();
	}
	

}
