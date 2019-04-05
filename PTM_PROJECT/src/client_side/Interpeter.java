package client_side;

import java.io.BufferedReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Interpeter<V> {
	HashMap<String, Command> CommandMap = new HashMap<>();
	HashMap<String, Double> symbolTable = new HashMap<>();

	public void interpet(V v) {
		ArrayList<String> file = lexer(v);
		parse(file);
	}

	public ArrayList<String> lexer(V v) {
		Scanner scanner = new Scanner(new BufferedReader((Reader) v));
		ArrayList<String> array = new ArrayList<>();
		while (scanner.hasNext()) {
			array.add(scanner.next());
		}
		scanner.close();
		return array;
	}

	public void parse(ArrayList<String> arr) {
		for (int i = 0; i < arr.size(); i++) {
			Command c = CommandMap.get(arr.get(i));
			if (c != null) {
				i += c.DoCommand((ArrayList<String>) arr.subList(i, arr.size()));
			}
		}
	}
}
