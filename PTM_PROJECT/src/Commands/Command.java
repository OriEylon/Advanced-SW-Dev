package Commands;

import java.util.ArrayList;

public interface Command {
	void DoCommand(ArrayList<String> arr);

	int getparameters();
}
