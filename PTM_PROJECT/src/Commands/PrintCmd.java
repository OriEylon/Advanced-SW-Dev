package Commands;

import java.util.ArrayList;

import interpeter.Parser;

public class PrintCmd implements Command {

	@Override
	public void DoCommand(ArrayList<String> arr) {
		if (Parser.symbolTable.containsKey(arr.get(0))) {
			System.out.println(Parser.symbolTable.get(arr.get(0)).getV());
		}
		else
			System.out.println(arr.get(0));
	}

	@Override
	public int getparameters() {
		return 1;
	}

}
