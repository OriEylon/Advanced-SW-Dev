package server_side;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class MyTestClientHandler implements ClientHandler {
	
	private Solver solver;
	private CacheManager cm;
	
	@Override
	public void handleClient(InputStream in, OutputStream out) {
		BufferedReader reader=new BufferedReader(new InputStreamReader(in));
		PrintWriter writer= new PrintWriter(new OutputStreamWriter(out));
		cm=new FileCacheManager();
		String line;
		String solved;
		try {
			while(!(line=reader.readLine()).equals("end")) {
			if(cm.Check(line)) {
				solved=(String) cm.Extract(line);
			}
			else {
				//solver=String->new StringBuilder().reverse().toString();
				solver=new Solver<String,String>(){
					public String Solve(String s) {
						return new StringBuilder(s).reverse().toString();
					}
				};
				solved=(String) solver.Solve(line);
				cm.Save(line, solved);
				
			}
			writer.println(solved);
			writer.flush();
			}
		} catch (IOException e) { e.printStackTrace();}
			try {
				reader.close();
				writer.close();
			} catch (IOException e) {e.printStackTrace();}
	}

}
