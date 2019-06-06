package Commands;

import java.util.ArrayList;

public class WhileCmd extends ConditionCommand {

	@Override
	public void DoCommand(ArrayList<String> arr) {
		PredicateCommand pc = (PredicateCommand) cmdList.get(0).getC();
		cmdList.get(0).calculate();
		while (pc.bool != 0) {
			for (CommandExpression c : cmdList) {
				c.calculate();
			}
			cmdList.get(0).calculate();
		}
	}

	@Override
	public int getparameters() {
		return 1;
	}

}
