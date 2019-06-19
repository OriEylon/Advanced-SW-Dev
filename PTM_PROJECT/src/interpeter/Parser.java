package interpeter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import Commands.AssignCmd;
import Commands.Command;
import Commands.CommandExpression;
import Commands.ConditionCommand;
import Commands.ConnectCmd;
import Commands.DefineVarCmd;
import Commands.DisconnectCommand;
import Commands.OpenServerCmd;
import Commands.PredicateCommand;
import Commands.PrintCmd;
import Commands.ReturnCmd;
import Commands.SleepCmd;
import Commands.WhileCmd;

public class Parser {
	HashMap<String, CommandExpression> cmdTbl = new HashMap<>();
	public static HashMap<String, Var> symbolTable = new HashMap<>();
	public static double retVal;
	private GenericFactory<Command> cmdFac = new GenericFactory<Command>();
	ArrayList<CommandExpression> cmdList = new ArrayList<>();
	public static ArrayList<String> simVars = new ArrayList<>();
	public static boolean readvars;

	public Parser() {
//		cmdTbl = new HashMap<>();
//		cmdFac = new GenericFactory<Command>();

		cmdFac.insertProduct("var", DefineVarCmd.class);
		cmdFac.insertProduct("while", WhileCmd.class);
		cmdFac.insertProduct("openDataServer", OpenServerCmd.class);
		cmdFac.insertProduct("return", ReturnCmd.class);
		cmdFac.insertProduct("disconnect", DisconnectCommand.class);
		cmdFac.insertProduct("connect", ConnectCmd.class);
		cmdFac.insertProduct("=", AssignCmd.class);
		cmdFac.insertProduct("sleep", SleepCmd.class);
		cmdFac.insertProduct("print", PrintCmd.class);

		cmdTbl.put("var", new CommandExpression(new DefineVarCmd()));
		cmdTbl.put("while", new CommandExpression(new WhileCmd()));
		cmdTbl.put("openDataServer", new CommandExpression(new OpenServerCmd()));
		cmdTbl.put("return", new CommandExpression(new ReturnCmd()));
		cmdTbl.put("disconnect", new CommandExpression(new DisconnectCommand()));
		cmdTbl.put("connect", new CommandExpression(new ConnectCmd()));
		cmdTbl.put("=", new CommandExpression(new AssignCmd()));
		cmdTbl.put("sleep", new CommandExpression(new SleepCmd()));
		cmdTbl.put("print", new CommandExpression(new PrintCmd()));
//		symbolTable.put("simX", new Var());
//		symbolTable.put("simY", new Var());
//		symbolTable.put("simZ", new Var());
	}

	public ArrayList<CommandExpression> parseScript(ArrayList<String> script) {
		ArrayList<CommandExpression> commands = new ArrayList<>();
		for (int index = 0; index < script.size(); index++) {
			String token = script.get(index);
			CommandExpression c = new CommandExpression((Command) cmdFac.getNewProduct(token));
			if (c.getC() != null) {
				if (token.equals("while")) {
					int i = index;
					while (!script.get(index).equals("}"))
						index++;
					c.setC(ConditionParser(new ArrayList<>(script.subList(i, index + 1))));

				} else if (token.equals("return")) {
					c.setArr(new ArrayList<>(script.subList(index, script.size())));
				} else if (token.equals("=")) {
					int i = index + 1;
					while (!cmdTbl.containsKey((script.get(i))))
						if (i + 1 == script.size() || script.get(i + 1).equals("="))
							break;

						else
							i++;

					c.setArr(new ArrayList<>(script.subList(index - 1, i)));
				} else {
					int parameters = c.getC().getparameters() + 1;
					c.setArr(new ArrayList<>(script.subList(index + 1, index + parameters)));
				}
				commands.add(c);
			}
		}
		return commands;
	}

	public double parse(ArrayList<String> script) {
		if (readvars == false) {
			readvars = true;
			readVars();
		}
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

	private void readVars() {
		try {
			Scanner scanner = new Scanner(new BufferedReader(new FileReader("simulator_vars.txt")));
			while (scanner.hasNextLine()) {
				String var = scanner.nextLine();
				simVars.add(var);
				symbolTable.put(var, new Var());
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}