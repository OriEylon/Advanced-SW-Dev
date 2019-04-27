package test;

import interpeter.interpeter;

public class MyInterpreter {

	public static  int interpret(String[] lines){
		interpeter i=new interpeter(lines);
		return (int) i.interpet();
		
	}
}
