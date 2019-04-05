package server_side;

public class MatrixState extends State<String> {
	
	public MatrixState(String state) {
		super(state);
		setCameFrom(null);
	}
	
}
