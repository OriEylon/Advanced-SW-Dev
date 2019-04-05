package server_side;

import java.util.ArrayList;

public class Matrix implements Searchable {
	
	private State initialState;
	private State GoalState;
	private MatrixState[][] matrix;
	
	
	public Matrix(int[][] mat,State initial,State goal) {
		int i=mat.length+2;
		int j=mat[0].length+2;
		matrix=new MatrixState[i][j];

		for(int n=0;n<i;n++) {
			matrix[n][0]=new MatrixState("-1");
			matrix[0][n]=new MatrixState("-1");
			matrix[i-1][n]=new MatrixState("-1");
			matrix[n][i-1]=new MatrixState("-1");
		}
		
		for(int n=0;n<mat.length;n++) {
			for(int m=0;m<mat[0].length;m++) {
				//matrix[n+1][m+1]=mat[n][m];
				matrix[n+1][m+1]=new MatrixState((n+1)+","+(m+1));
				matrix[n+1][m+1].setCost(mat[n][m]);
			}
		}
		
		String[] arr1=initial.getState().toString().split(",");
		int a=Integer.parseInt(arr1[0]);
		a++;
		int b=Integer.parseInt(arr1[1]);
		b++;
		this.initialState=matrix[a][b];
		
		arr1=goal.getState().toString().split(",");
		a=Integer.parseInt(arr1[0])+1;
		b=Integer.parseInt(arr1[1])+1;
		this.GoalState=matrix[a][b];
				
	}
	
	
	public State getInitialState() {
		return initialState;
	}

	public void setInitialState(State initialState) {
		this.initialState = initialState;
	}

	public State getGoalState() {
		return GoalState;
	}

	public void setGoalState(State goalState) {
		GoalState = goalState;
	}

	public MatrixState[][] getMatrix() {
		return matrix;
	}

	public void setMatrix(MatrixState[][] matrix) {
		this.matrix = matrix;
	}

	@Override
	public ArrayList<State> getAllPossibleStates(State s) {
		ArrayList<State> list=new ArrayList<>();
		String[] point=((String)s.getState()).split(",");
		int x=Integer.parseInt(point[0]);
		int y=Integer.parseInt(point[1]);
		MatrixState dummy = new MatrixState("-1");
		if(s.getCameFrom()!=null)
		s.setCost(matrix[x][y].getCost()+s.getCameFrom().getCost());

		if(!matrix[x-1][y].equals(dummy)) { //up 
			if(matrix[x-1][y]!=s.getCameFrom())	//if its where we came from,dont add
			list.add(matrix[x-1][y]);
			
		}
		if(!matrix[x+1][y].equals(dummy)) { //down
			if(matrix[x+1][y]!=s.getCameFrom())
			list.add(matrix[x+1][y]);
		}
		if(!matrix[x][y-1].equals(dummy)) { //left
			if(matrix[x][y-1]!=s.getCameFrom())
			list.add(matrix[x][y-1]);
		}
		if(!matrix[x][y+1].equals(dummy)) { //right
			if(matrix[x][y+1]!=s.getCameFrom())
			list.add(matrix[x][y+1]);
		}
		return list;
		/*		ArrayList<State> list=new ArrayList<>();
		String[] point=((String)s.getState()).split(",");
		int x=Integer.parseInt(point[0]);
		int y=Integer.parseInt(point[1]);
		s.setCost(matrix[x][y]);
		
		if(matrix[x-1][y]!=-1)	//up
			list.add(new MatrixState((x-1)+","+(y)));
		
		if(matrix[x+1][y]!=-1)	//down
			list.add(new MatrixState((x+1)+","+(y)));
		
		if(matrix[x][y-1]!=-1)	//left
			list.add(new MatrixState((x)+","+(y-1)));
		
		if(matrix[x][y+1]!=-1)	//right
			list.add(new MatrixState((x)+","+(y+1)));
		
		return list;
	}
*/
	}
}
