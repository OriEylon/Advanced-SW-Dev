package client_side;

import java.util.ArrayList;
import java.util.HashMap;

public class Parser {
	HashMap<String, Command> map = new HashMap<>();

	public void parse(ArrayList<String> arr) {
		for (int i = 0; i < arr.size(); i++) {
			Command c = map.get(arr.get(i));
			if (c != null) {
				i += c.DoCommand((ArrayList<String>) arr.subList(i, arr.size()));
			}
		}
	}
}
