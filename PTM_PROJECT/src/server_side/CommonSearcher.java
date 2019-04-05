package server_side;

import java.util.Comparator;
import java.util.PriorityQueue;

public abstract class CommonSearcher<Solution> implements Searcher<Solution> {
	
	protected PriorityQueue<State> openList;
	private int evaluatedNodes;
	private Comparator<State> mycomp=new StateComperator();
	
	public CommonSearcher() {
	openList = new PriorityQueue<>(mycomp);
	evaluatedNodes=0;
	}
	
	protected State popOpenList() {
	evaluatedNodes++;
	return openList.poll();
	}
	
	public int getNumberOfNodesEvaluated() {
		return evaluatedNodes;
	}
	
	protected Solution backTrace(State goal,State start) {
		if(goal.equals(start))
			return (Solution)start.getState();
		return (Solution) (backTrace(goal.getCameFrom(), start)+"->"+goal.getState());
	}
	
	
}
