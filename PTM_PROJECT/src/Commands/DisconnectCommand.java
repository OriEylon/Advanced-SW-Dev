package Commands;

import java.util.ArrayList;

public class DisconnectCommand implements Command {

	@Override
	public void DoCommand(ArrayList<String> arr) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		OpenServerCmd.stop=true;
		ConnectCmd.stop=true;
	}

	@Override
	public int getparameters() {
		// TODO Auto-generated method stub
		return 0;
	}

}
