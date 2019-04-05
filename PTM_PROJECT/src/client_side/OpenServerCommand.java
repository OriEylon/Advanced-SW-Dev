package client_side;

import java.util.ArrayList;

public class OpenServerCommand implements Command {

	@Override
	public int DoCommand(ArrayList<String> arr) {

		int port = Integer.parseInt(arr.get(1));
		int Hz = Integer.parseInt(arr.get(2));

		return 2;
	}

}
