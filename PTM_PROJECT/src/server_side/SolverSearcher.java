package server_side;

public class SolverSearcher<Problem, Solution> implements Solver<Problem, Solution> {
	private Searcher searcher;
	
	public SolverSearcher(Searcher s) {
		searcher=s;
	}
	
	@Override
	public Solution Solve(Problem p) {
		// TODO Auto-generated method stub
		return (Solution) searcher.search((Searchable) p);
	}
	
}
