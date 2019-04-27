package Commands;

import java.util.ArrayList;

import expression.Expression;

public class CommandExpression implements Expression {
	Command c;
	ArrayList<String> arr;

	public CommandExpression(Command c) {
		super();
		this.c = c;
	}

	@Override
	public double calculate() {
		c.DoCommand(arr);
		return 0;
	}

	public Command getC() {
		return c;
	}

	public void setC(Command c) {
		this.c = c;
	}

	public ArrayList<String> getArr() {
		return arr;
	}

	public void setArr(ArrayList<String> arr) {
		this.arr = arr;
	}

}
