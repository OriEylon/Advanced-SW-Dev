package server_side;

public interface Searcher<Solution> {
	public abstract Solution search(Searchable s);
	public int getNumberOfNodesEvaluated();
}
