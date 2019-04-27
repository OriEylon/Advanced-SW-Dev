package interpeter;

import java.util.Observable;
import java.util.Observer;

import expression.ShuntingYard;

public class Var extends Observable implements Observer {
	public double v;

	public Var(Double v) {
		this.v = v;
	}

	public Var() {
//		this.v = ShuntingYard.calc(exp);
	}

	public double getV() {
		return v;
	}

	public void setV(double v) {
		if (this.v != v) {
			this.v = v;
			setChanged();
			notifyObservers(v);
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		this.setV((double) arg);
	}
}
