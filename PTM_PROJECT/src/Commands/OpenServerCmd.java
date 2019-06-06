package Commands;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Scanner;

import expression.ShuntingYard;
import interpeter.Parser;
import server_side.ClientHandler;
import server_side.MySerialServer;
import server_side.Server;

public class OpenServerCmd implements Command {
	public static volatile boolean stop = false;

	@Override
	public void DoCommand(ArrayList<String> arr) {
		int port = Integer.parseInt(arr.get(0));
		int Hz = (int) (1000 / ShuntingYard.calc(arr.get(1)));

		Server s = new MySerialServer();
		s.open(port, new ClientHandler() {

			@Override
			public void handleClient(InputStream in, OutputStream out) {
				synchronized (ConnectCmd.wait) {
					ConnectCmd.wait.notifyAll();
				}
				System.out.println("connected to client");
				stop = false;
				Scanner scanner = new Scanner(new BufferedReader(new InputStreamReader(in)));
				String[] line;
				int i = 0;
				while (!stop) {
					i = 0;
					String a = scanner.nextLine();
//					System.out.println(a);
					line = a.split(",");
					for (String string : Parser.simVars) {
						Parser.symbolTable.get(string).setV(Double.parseDouble(line[i]));
						i++;
					}
					try {
						Thread.sleep(Hz);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				scanner.close();
				s.stop();
			}
		});

	}

	@Override
	public int getparameters() {
		return 2;
	}

}
