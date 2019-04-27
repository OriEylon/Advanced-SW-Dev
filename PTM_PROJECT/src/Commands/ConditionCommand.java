package Commands;

import java.util.ArrayList;
import java.util.function.Predicate;

public abstract class ConditionCommand implements Command {
	public Predicate<Double> condition;
	public ArrayList<CommandExpression> cmdList;

	public Predicate<Double> getCondition() {
		return condition;
	}

	public void setCondition(Predicate<Double> condition) {
		this.condition = condition;
	}

	public ArrayList<CommandExpression> getCmdList() {
		return cmdList;
	}

	public void setCmdList(ArrayList<CommandExpression> cmdList) {
		this.cmdList = cmdList;
	}

}
