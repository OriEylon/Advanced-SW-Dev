package Commands;

import java.util.ArrayList;

import expression.ShuntingYard;
import interpeter.Parser;

public class ReturnCmd implements Command {

	@Override
	public void DoCommand(ArrayList<String> arr) {
		String exp = "";
		for (int i = 1; i < arr.size(); i++) {
			exp += arr.get(i);
		}
		Parser.retVal = ShuntingYard.calc(exp);
	}

	@Override
	public int getparameters() {
		// TODO Auto-generated method stub
		return 0;
	}

}
