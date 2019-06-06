package interpeter;

public class interpeter {
	Lexer lexer;
	Parser parser;

	public interpeter(String[] script) {
		lexer = new Lexer(script);
		parser = new Parser();
	}

	public double interpet() {
		return parser.parse(lexer.Read());
	}
}
