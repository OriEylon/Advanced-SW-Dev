package client_side;

import java.io.BufferedReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Scanner;

public class Lexer<V> {

	public ArrayList<String> Read(V v) {
		Scanner scanner = new Scanner(new BufferedReader((Reader) v));
		ArrayList<String> array = new ArrayList<>();
		while (scanner.hasNext()) {
			array.add(scanner.next());
		}
		scanner.close();
		return array;
	}

}
