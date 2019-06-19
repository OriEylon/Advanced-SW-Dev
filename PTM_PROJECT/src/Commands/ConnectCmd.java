package Commands;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ConnectCmd implements Command {
	public static volatile boolean stop = false;
	public static Object wait = new Object();
	public static PrintWriter out;

	@Override
	public void DoCommand(ArrayList<String> arr) {
		stop = false;
		String ip = arr.get(0);
		int port = Integer.parseInt(arr.get(1));
		new Thread(() -> {
			try {
//				synchronized (wait) {
//					wait.wait();
//				}
				Thread.sleep(3000);
				Thread.currentThread().setName("My client Thread");
				Socket socket = new Socket(ip, port);
				out = new PrintWriter(socket.getOutputStream());
				System.out.println("connected to simulator");
				while (!stop) {
//					for (String string : Parser.simVars) {
//						if (Parser.symbolTable.get(string).hasChanged()) {
//							Double var = Parser.symbolTable.get(string).getV();
//							out.println("set " + string + " " + var);
//							out.flush();
//							System.out.println("set " + string + " " + var);
//						}
//					}
				}
				out.println("bye");
				out.close();
				socket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
	}

	@Override
	public int getparameters() {
		return 2;
	}

}
