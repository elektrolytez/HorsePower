import java.io.*;
import java.net.*;
import java.util.List;
import java.util.Scanner;

/** EDITS
 * added server connection, input id and password into console when prompted
 * deleted redundant toString method in Move.java
 * added terminal() in Main for while loop in main method
 * added getMove and getPosition in Main that convert from the server's notation to our Samuels' notation
 * computer makes random legal moves
 **/

public class Main {

	private static String _user = null;  // need legit id here
	private static String _password = null;  // need password here
	private final static String _opponent = "0";
	private final String _machine  = "icarus2.engr.uconn.edu"; 
	private int _port = 3499;
	private Socket _socket = null;
	private PrintWriter _out = null;
	private BufferedReader _in = null;

	private String _gameID;
	private String _myColor;

	public Main(){	
		_socket = openSocket();
	}

	public Socket getSocket(){
		return _socket;
	}

	public PrintWriter getOut(){
		return _out;
	}

	public BufferedReader getIn(){
		return _in;
	}

	public void setGameID(String id){
		_gameID = id;
	}

	public String getGameID() {
		return _gameID;
	}

	public void setColor(String color){
		_myColor = color;
	}

	public String getColor() {
		return _myColor;
	}

	public static void main(String[] argv){
		String readMessage;
		Main myClient = new Main();

		try{
			Scanner reader = new Scanner(System.in);
			
			myClient.readAndEcho(); // start message
			myClient.readAndEcho(); // ID query
			_user = reader.next();
			myClient.writeMessageAndEcho(_user); // user ID

			myClient.readAndEcho(); // password query 
			_password = reader.next();
			myClient.writeMessage(_password);  // password

			myClient.readAndEcho(); // opponent query
			myClient.writeMessageAndEcho(_opponent);  // opponent

			myClient.setGameID(myClient.readAndEcho().substring(5,9)); // game 
			myClient.setColor(myClient.readAndEcho().substring(6,11));  // color
			System.out.println("I am playing as "+myClient.getColor()+ " in game number "+ myClient.getGameID());

			Board _board = new Board();
			readMessage = myClient.readAndEcho();  //either returns time if black or black's move if white
			
			if (myClient.getColor().equals("White")) {
					//black already made a move
					Move move = getMove(readMessage);
					_board = _board.result(move);// write the move to the board
					System.out.println(_board.toString()); // show the move
					readMessage = myClient.readAndEcho();  // time left
				}
			while(!terminal(readMessage)) {
					//make a random legal move until we implement minimax
					List<Move> actions = _board.actions();
					Move action = actions.get((int)(Math.random() * actions.size()));
					myClient.writeMessageAndEcho(action.getMessage());// send the move to the server
					readMessage = myClient.readAndEcho();  //show the move received by the server
					_board = _board.result(action); //write the move to the board
					System.out.println(_board.toString()); //print the board
					readMessage = myClient.readAndEcho();  // opponent's move
					if (terminal(readMessage))
						break;
					Move move = getMove(readMessage);
					_board = _board.result(move);// write the move to the board
					System.out.println(_board.toString()); //show the move
					readMessage = myClient.readAndEcho();  // move query
				}
			if (readMessage.contains(myClient.getColor()))
				System.out.println("YOU WIN!!!");
			else if (!readMessage.contains("Draw"))
				System.out.println("You lose");
			myClient.getSocket().close();
		} catch  (IOException e) {
			System.out.println("Failed in read/close");
			System.exit(1);
		}
	}

	public String readAndEcho() throws IOException
	{
		String readMessage = _in.readLine();
		System.out.println("read: "+readMessage);
		return readMessage;
	}

	public void writeMessage(String message) throws IOException
	{
		_out.print(message+"\r\n");  
		_out.flush();
	}

	public void writeMessageAndEcho(String message) throws IOException
	{
		_out.print(message+"\r\n");  
		_out.flush();
		System.out.println("sent: "+ message);
	}

	public  Socket openSocket(){
		//Create socket connection, adapted from Sun example
		try{
			_socket = new Socket(_machine, _port);
			_out = new PrintWriter(_socket.getOutputStream(), true);
			_in = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
		} catch (UnknownHostException e) {
			System.out.println("Unknown host: " + _machine);
			System.exit(1);
		} catch  (IOException e) {
			System.out.println("No I/O");
			System.exit(1);
		}
		return _socket;
	}
	
	//rely on the server to say when the game is over
	public static boolean terminal(String readMessage) {
		if (readMessage.contains("Result"))
			return true;
		return false;
	}
	
	public static Move getMove(String move) {//Ex: move = read: Move:Black:(5:7):(4:6)
	Move newMove = null;
	String delims = "[()]+";
	String[] tokens = move.split(delims);
	delims = ":";
	String[][] gridPositions = new String[tokens.length/2][2];
	int[][] gridPositionInts = new int[gridPositions.length][2]; //[{5,7},{4,6}]
	for (int n = 1; n < tokens.length; n+=2) {    //supports multiple jumps
		gridPositions[n/2] = tokens[n].split(delims);
		int[] i = {Integer.parseInt(gridPositions[n/2][0]), Integer.parseInt(gridPositions[n/2][1])};
		gridPositionInts[n/2] = i;
	}
	int[] positions = new int[gridPositionInts.length];
	for (int i = 0; i < positions.length; i++) { 
		positions[i] = getPosition(gridPositionInts[i]); //{23,19}
	}
	newMove = new Move(positions);
	return newMove;
	}

	//converts from grid notation to Samuels' notation
	public static int getPosition(int[] array) {
		int location = 4*array[0] - array[1]/2 + 4 + array[0]/2;
		return location;
	}

}
