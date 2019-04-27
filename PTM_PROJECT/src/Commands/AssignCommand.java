package Commands;

import java.util.ArrayList;

import expression.ShuntingYard;
import interpeter.Parser;
import interpeter.Var;

public class AssignCommand implements Command {

	@Override
	public void DoCommand(ArrayList<String> arr) {
		String exp = "";
		Var var1 = Parser.symbolTable.get(arr.get(0));
		for (int i = 2; i < arr.size(); i++) { // get the entire expression
			if (!arr.get(i).equals("bind"))
				exp += arr.get(i);
		}

		Var newvar = Parser.symbolTable.get(exp);
//		newvar.setV(ShuntingYard.calc(exp));
		if (newvar == null) {
			newvar = new Var();
			newvar.setV(ShuntingYard.calc(exp));
		}

		if (arr.get(2).equals("bind")) {
			var1.addObserver(newvar);
			newvar.addObserver(var1);
		}
		var1.setV(newvar.getV());
//		return 1;
	}

	@Override
	public int getparameters() {
		// TODO Auto-generated method stub
		return 0;
	}

}
