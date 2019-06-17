package server_side;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class MyClientHandler implements ClientHandler {
	
	private Solver solver;
	private CacheManager cm;
	int[][] mat;
	
	public void setMat(int[][] mat) {
		this.mat = mat;
	}
	
	@Override
	public void handleClient(InputStream in, OutputStream out) {
		BufferedReader reader=new BufferedReader(new InputStreamReader(in));
		PrintWriter writer= new PrintWriter(new OutputStreamWriter(out));
		cm=new FileCacheManager();
		String line;
		String solved;
		int rows=0;
		int cols=0;
//		int[][] mat;
		StringBuilder sb=new StringBuilder();
		String[] stringArr=null;
		String[] stringArr2 = null;
		int counter=0;
		State initial;
		State goal;
		ArrayList<String[]> array=new ArrayList<>();
		
		try {
			while(!(line=reader.readLine()).equals("end")) {
				array.add(line.split(","));
				
/*				
				stringArr2=line.split(",");
				stringArr=sb.append(line).toString().split(",");
				sb.append(",");
				rows++;
	*/	
			}
			
//			cols=stringArr2.length;
			line=reader.readLine();
			initial=new State<String>(line);
			line=reader.readLine();
			goal=new State<String>(line);
//			counter=0;
			mat=new int[array.size()][array.get(0).length];
			for(int i=0;i<mat.length;i++) {
				for(int j=0;j<mat[i].length;j++) {
					if(array.get(i)[j].contains(".")) {
						int temp=(int) Double.parseDouble(array.get(i)[j]);
						mat[i][j]=temp;
					}
					else
					mat[i][j]=Integer.parseInt(array.get(i)[j]);
//					mat[i][j]=Integer.parseInt(stringArr[counter]);
//				 	counter++;
		
				}
			}
			solver=new SolverSearcher<>(new BFS<>());
			Matrix matrix=new Matrix(mat, initial, goal);
			solved=(String) solver.Solve(matrix);
			stringArr=solved.split("->");
			String[] arrow1;
			String[] arrow2;
			solved="";
			for (int i=0;i<stringArr.length-1;i++) {
				arrow1=stringArr[i].split(",");
				arrow2=stringArr[i+1].split(",");
				int x=Integer.parseInt(arrow2[0])-Integer.parseInt(arrow1[0]);
				int y=Integer.parseInt(arrow2[1])-Integer.parseInt(arrow1[1]);
				
				if(x>0) 
				solved+="Down"+",";
				else if(x<0) 
				solved+="Up"+",";
				else
					if(y>0)
						solved+="Right"+",";
					else
						solved+="Left"+",";
			}
			solved=solved.substring(0, solved.length()-1); // remove the last ","
			writer.println(solved);
			writer.flush();
		} catch (IOException e) { e.printStackTrace();}
			try {
				reader.close();
				writer.close();
			} catch (IOException e) {e.printStackTrace();}
			
			
	}
		
}
