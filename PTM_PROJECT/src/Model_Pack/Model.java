package Model_Pack;

import java.util.Observable;

import interpeter.Lexer;
import interpeter.interpeter;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Model extends Observable {
	interpeter i;
	public StringProperty s;

	public Model() {
		s = new SimpleStringProperty();
		i = new interpeter(s.toString());

	}

	public void interpet(String s) {
		i.setLexer(new Lexer(s));
		i.interpet();
	}

	public Double normalize(Double x, Double max, Double min, Double newmax, Double newmin) {
		return (x - min) / (max - min) * (newmax - newmin) + newmin;
	}

}
