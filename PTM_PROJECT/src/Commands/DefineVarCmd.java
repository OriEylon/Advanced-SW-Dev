package Commands;

import java.util.ArrayList;

import interpeter.Parser;
import interpeter.Var;

public class DefineVarCmd implements Command {

	@Override
	public void DoCommand(ArrayList<String> arr) {
		Parser.symbolTable.put(arr.get(0), new Var());
	}

	@Override
	public int getparameters() {
		return 1;
	}

}
