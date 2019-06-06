package test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

import interpeter.interpeter;

public class MyMainTest {

	public static void main(String[] args) {
		try {
			Scanner scanner = new Scanner(new BufferedReader(new FileReader("flight_script.txt")));
			ArrayList<String> arr = new ArrayList<>();
			while (scanner.hasNextLine()) {
				arr.add(scanner.nextLine());
			}
			String[] script = new String[arr.size()];
			for (int i = 0; i < script.length; i++) {
				script[i] = arr.get(i);
			}
			interpeter myInt = new interpeter(script);
			myInt.interpet();
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
