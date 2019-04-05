package server_side;

import java.util.Comparator;

public class StateComperator implements Comparator<State> {

	@Override
	public int compare(State s1, State s2) {
		if(s1.getCost()>s2.getCost())
			return 1;
		else if(s1.getCost()<s2.getCost())
			return -1;
		else
			return 0;
	}

	

}
