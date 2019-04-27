package Commands;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import interpeter.Parser;
import interpeter.Var;

public class ConnectCommand implements Command {
	public static volatile boolean stop = false;

	@Override
	public void DoCommand(ArrayList<String> arr) {
		stop = false;
		String ip = arr.get(0);
		int port = Integer.parseInt(arr.get(1));
		new Thread(() -> {
			try {
				Thread.currentThread().setName("My client Thread");
				Socket socket = new Socket(ip, port);
				PrintWriter out = new PrintWriter(socket.getOutputStream());
				while (!stop) {
					if (Parser.symbolTable.get("simX").hasChanged()) {
						out.println("set simX " + Parser.symbolTable.get("simX").getV());
						out.flush();
					}

					if (Parser.symbolTable.get("simY").hasChanged()) {
						out.println("set simY " + Parser.symbolTable.get("simY").getV());
						out.flush();
					}

					if (Parser.symbolTable.get("simZ").hasChanged()) {
						out.println("set simZ " + Parser.symbolTable.get("simZ").getV());
						out.flush();
					}
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

	@Override
	public int getparameters() {
		return 2;
	}

}
