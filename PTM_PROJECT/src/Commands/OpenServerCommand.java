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

public class OpenServerCommand implements Command {
	public static volatile boolean stop = false;

	@Override
	public void DoCommand(ArrayList<String> arr) {
		int port = Integer.parseInt(arr.get(0));
		int Hz = (int) (1000 / ShuntingYard.calc(arr.get(1)));

		Server s = new MySerialServer();
		s.open(port, new ClientHandler() {

			@Override
			public void handleClient(InputStream in, OutputStream out) {
				stop = false;
				Scanner scanner = new Scanner(new BufferedReader(new InputStreamReader(in)));
				String[] line;
				while (!stop) {
					line = scanner.nextLine().split(",");
					if (Parser.symbolTable.get("simX").getV() != Double.parseDouble(line[0]))
						Parser.symbolTable.get("simX").setV(Double.parseDouble(line[0]));
					if (Parser.symbolTable.get("simY").getV() != Double.parseDouble(line[1]))
						Parser.symbolTable.get("simY").setV(Double.parseDouble(line[1]));
					if (Parser.symbolTable.get("simZ").getV() != Double.parseDouble(line[2]))
						Parser.symbolTable.get("simZ").setV(Double.parseDouble(line[2]));

					try {
						Thread.sleep(Hz);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				s.stop();
			}
		});

	}

	@Override
	public int getparameters() {
		return 2;
	}

}
