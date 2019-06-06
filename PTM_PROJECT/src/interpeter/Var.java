package interpeter;

public class Var {
	public double v;
	public String binded = null;

	public Var(Double v) {
		this.v = v;
	}

	public Var() {
	}

	public double getV() {
		return v;
	}

	public void setV(double v) {
		if (this.v != v) {
			this.v = v;
//			setChanged();
//			notifyObservers(v);
		}
	}

//	@Override
//	public void update(Observable o, Object arg) {
//		this.setV((double) arg);
//	}
}
