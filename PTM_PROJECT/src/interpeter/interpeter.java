package interpeter;

public class interpeter {
	Lexer lexer;
	Parser parser;

	public interpeter(String[] script) {
		lexer = new Lexer(script);
		parser = new Parser();
	}

	public void setLexer(Lexer lexer) {
		this.lexer = lexer;
	}

	public void setParser(Parser parser) {
		this.parser = parser;
	}

	public interpeter(String s) {
		lexer = new Lexer(s);
		parser = new Parser();
	}

	public double interpet() {
		return parser.parse(lexer.Read());
	}
}
