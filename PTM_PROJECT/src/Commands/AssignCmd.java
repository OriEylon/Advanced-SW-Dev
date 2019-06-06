package Commands;

import java.util.ArrayList;

import expression.ShuntingYard;
import interpeter.Parser;
import interpeter.Var;

public class AssignCmd implements Command {

	@Override
	public void DoCommand(ArrayList<String> arr) {
		String exp = "";
		Var var1 = Parser.symbolTable.get(arr.get(0));
		for (int i = 2; i < arr.size(); i++) { // get the entire expression
			if (!arr.get(i).equals("bind"))
				exp += arr.get(i);
		}

		if (arr.get(2).equals("bind")) {
			Var var2 = Parser.symbolTable.get(exp);
			var2.binded = exp;
			Parser.symbolTable.put(arr.get(0), var2);

		} else if (var1.binded != null) {
			Double v = ShuntingYard.calc(exp);
			ConnectCmd.out.println("set " + var1.binded + " " + v);
			ConnectCmd.out.flush();
			System.out.println("set " + var1.binded + " " + v);
		} else {
			Var newvar = Parser.symbolTable.get(exp);
			if (newvar == null) {
				newvar = new Var();
				newvar.setV(ShuntingYard.calc(exp));
			}
			var1.setV(newvar.getV());

		}

	}

	@Override
	public int getparameters() {
		return 0;
	}

}
