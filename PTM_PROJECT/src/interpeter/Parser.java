package interpeter;

import java.util.ArrayList;
import java.util.HashMap;

import Commands.AssignCommand;
import Commands.Command;
import Commands.CommandExpression;
import Commands.ConditionCommand;
import Commands.ConnectCommand;
import Commands.DisconnectCommand;
import Commands.OpenServerCommand;
import Commands.PredicateCommand;
import Commands.DefineVarCommand;
import Commands.WhileCommand;
import Commands.ReturnCommand;

public class Parser {
	HashMap<String, CommandExpression> cmdTbl;
	public static HashMap<String, Var> symbolTable = new HashMap<>();
	public static double retVal;
	private GenericFactory<Command> cmdFac;
	ArrayList<CommandExpression> cmdList = new ArrayList<>();

	public Parser() {
		cmdTbl = new HashMap<>();
		cmdFac = new GenericFactory<Command>();

		cmdFac.insertProduct("var", DefineVarCommand.class);
		cmdFac.insertProduct("while", WhileCommand.class);
		cmdFac.insertProduct("openDataServer", OpenServerCommand.class);
		cmdFac.insertProduct("return", ReturnCommand.class);
		cmdFac.insertProduct("disconnect", DisconnectCommand.class);
		cmdFac.insertProduct("connect", ConnectCommand.class);
		cmdFac.insertProduct("=", AssignCommand.class);

		cmdTbl.put("var", new CommandExpression(new DefineVarCommand()));
		cmdTbl.put("while", new CommandExpression(new WhileCommand()));
		cmdTbl.put("openDataServer", new CommandExpression(new OpenServerCommand()));
		cmdTbl.put("return", new CommandExpression(new ReturnCommand()));
		cmdTbl.put("disconnect", new CommandExpression(new DisconnectCommand()));
		cmdTbl.put("connect", new CommandExpression(new ConnectCommand()));
		cmdTbl.put("=", new CommandExpression(new AssignCommand()));
		symbolTable.put("simX", new Var());
		symbolTable.put("simY", new Var());
		symbolTable.put("simZ", new Var());

	}

	public ArrayList<CommandExpression> parseScript(ArrayList<String> script) {
		ArrayList<CommandExpression> commands = new ArrayList<>();
		for (int index = 0; index < script.size(); index++) {
			String token = script.get(index);
//			CommandExpression c = cmdTbl.get(token);
			CommandExpression c = new CommandExpression((Command) cmdFac.getNewProduct(token));
			if (c.getC() != null) {
				if (token.equals("while")) {
					int i = index;
					while (!script.get(index).equals("}"))
						index++;
					c.setC(ConditionParser(new ArrayList<>(script.subList(i, index + 1))));

				} else if (token.equals("return")) {
					c.setArr(new ArrayList<>(script.subList(index, script.size())));
//					return c.calculate();
				} else if (token.equals("=")) {
					int i = index + 1;
					while (!cmdTbl.containsKey((script.get(i))))
						if (i + 1 == script.size() || script.get(i + 1).equals("="))
							break;

						else
							i++;

					c.setArr(new ArrayList<>(script.subList(index - 1, i)));
//					index += c.calculate();
				} else {
					int parameters = c.getC().getparameters() + 1;
					c.setArr(new ArrayList<>(script.subList(index + 1, index + parameters)));
//					index += c.calculate();
				}
				commands.add(c);
			}
		}
		return commands;
	}

	public double parse(ArrayList<String> script) {
		cmdList = parseScript(script);
		for (CommandExpression ce : cmdList) {
			ce.calculate();
		}
		return retVal;
	}

	private Command ConditionParser(ArrayList<String> script) {
		ConditionCommand c = (ConditionCommand) cmdTbl.get(script.get(0)).getC();
		ArrayList<CommandExpression> temp = new ArrayList<>();
		CommandExpression cmdtmp = new CommandExpression(new PredicateCommand());
		cmdtmp.setArr(new ArrayList<>(script.subList(0, script.indexOf("{"))));
		temp.add(cmdtmp);
		c.setCmdList(temp);
		c.getCmdList().addAll(1, parseScript(new ArrayList<>(script.subList(1, script.size()))));
		return c;
	}

}