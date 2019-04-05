package server_side;

public class State<T> {
	private T state; //the state represented by a string
	private double cost; //cost to reach this state
	private State<T> cameFrom; //the state we came from to this state
	
	public State(T state) {
		this.state=state;
	}
	
	
	public T getState() {
		return state;
	}


	public void setState(T state) {
		this.state = state;
	}


	public double getCost() {
		if(cameFrom==null)
			return cost;
		return cameFrom.cost+cost;
	}


	public void setCost(double cost) {
		this.cost = cost;
	}


	public State<T> getCameFrom() {
		return cameFrom;
	}


	public void setCameFrom(State<T> cameFrom) {
		this.cameFrom = cameFrom;
	}


	public boolean equals(State s) {
		return state.equals(s.state);
	}
	
}
