package test;

import server_side.BFS;
import server_side.Matrix;
import server_side.MatrixState;
import server_side.Searchable;
import server_side.Searcher;
import server_side.State;

public class mainMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[][] m= { {197,130,139,188},{20,24,167,182},{108,78,169,193},{184,65,83,41} };
		int[][] m2= {{149,77,42,136},
				{145,155,45,0},
				{136,102,182,87},
				{178,183,143,60}};
		for (int[] is : m2) {
			for (int is2 : is) {
				System.out.print(is2+",");
				
			}
			System.out.println("\n");
		}
		Searchable matrix=new Matrix(m2,new MatrixState("0,0"),new MatrixState("3,3"));
		Searcher<String> sol=new BFS<>();
		System.out.println(sol.search(matrix));
	}

}
