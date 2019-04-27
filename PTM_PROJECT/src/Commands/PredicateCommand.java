package Commands;

import java.util.ArrayList;

import expression.SuntYard_Predicate;

public class PredicateCommand implements Command {
	Double bool;

	public Double getBool() {
		return bool;
	}

	public void setBool(Double bool) {
		this.bool = bool;
	}

	@Override
	public void DoCommand(ArrayList<String> arr) {
		String exp = "";
		for (String s : arr) {
			if (!s.equals("while"))
				exp += s;
		}
		bool = SuntYard_Predicate.calc(exp);
	}

	@Override
	public int getparameters() {
		// TODO Auto-generated method stub
		return 0;
	}
}
