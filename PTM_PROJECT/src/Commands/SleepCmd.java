package Commands;

import java.util.ArrayList;

public class SleepCmd implements Command {

	@Override
	public void DoCommand(ArrayList<String> arr) {
		Long msec = Long.parseLong(arr.get(0));
		try {
			Thread.sleep(msec);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int getparameters() {
		return 1;
	}

}
