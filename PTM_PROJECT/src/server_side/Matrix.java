package server_side;

import java.util.ArrayList;

public class Matrix implements Searchable {

	private State InitialState;
	private State GoalState;
	private int[][] state;
	private MatrixState[][] mats;
	int f = 0;
	int h = 0;

	public Matrix(int[][] mat, State initial, State goal) {

		setInitialState(initial);
		setGoalState(goal);
		int i = mat.length + 2;
		int j = mat[0].length + 2;
		state = new int[i][j];
		mats = new MatrixState[i - 2][j - 2];
		for (int k = 0; k < i; k++) {

			for (int l = 0; l < j; l++) {

				state[k][l] = -1;

			}
		}

		for (int k = 1, f = 0; k < i - 1; k++, f++) {

			for (int l = 1, h = 0; l < j - 1; l++, h++) {

				state[k][l] = mat[k - 1][l - 1];
				mats[f][h] = ((new MatrixState(k + "," + l)));
				mats[f][h].setCost(mat[k - 1][l - 1]);
				// if(mats[f][h].getCameFrom()!=null)
				// mats[f][h].setFatherCost(mats[k][l].getCost()+mats[h][l].getCameFrom().getCost());
			}
		}
	}

	@Override
	public State getInitialState() {

		return this.InitialState;
	}

	public void setInitialState(State s) {
		this.InitialState = s;
	}

	public void setGoalState(State s) {
		this.GoalState = s;
	}

	@Override
	public State getGoalState() {
		return this.GoalState;
	}

	@Override
	public ArrayList<State> getAllPossibleStates(State s) {
		String[] ms = ((String) s.getState()).split(",");
		String part1 = ms[0];
		String part2 = ms[1];
		int col = Integer.parseInt(part2);
		int row = Integer.parseInt(part1);
		// s.setCost(state[row][col]);
		if (s.getCameFrom() != null) {
			s.setCost(state[row][col] + s.getCameFrom().getCost());
		}

		ArrayList<State> arr = new ArrayList();

		if ((state[row][col + 1] != -1))
			arr.add(mats[row - 1][col]);

		if (state[row + 1][col] != -1)
			arr.add(mats[row][col - 1]);

		if (state[row][col - 1] != -1)
			arr.add(mats[row - 1][col - 2]);

		if (state[row - 1][col] != -1)
			arr.add(mats[row - 2][col - 1]);

		return arr;
	}

}
