package interpeter;

import java.util.ArrayList;
import java.util.Scanner;

public class Lexer {
	Scanner scanner;

	public <V> Lexer(V script) {
		scanner = new Scanner((Readable) script);
	}

	public Lexer(String s) {
		scanner=new Scanner(s);
	}
	
	public Lexer(String[] script) {
		String stream = "";
		for (String s : script) {
			if (s.contains("="))
				s = s.replaceAll("=", " = ");

			stream += s + " ";
		}
		scanner = new Scanner(stream);
	}

	public ArrayList<String> Read() {
		ArrayList<String> array = new ArrayList<>();
		while (scanner.hasNext()) {
			array.add(scanner.next());
		}
		scanner.close();
		return array;
	}

}
