package horsepower_main;

import horsepower_checkers.*;
import horsepower_search.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
//import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class HPClient {
	
	// ~~~~~~~~~~~~~~~ Icarus2 Server Connection Info ~~~~~~~~~~~~~~~
	private static String _user="15";
	private static String _pw="544075";
	private static String _opponent = "0"; // 0 for server bot
	private final String _icarusAddress = "icarus2.engr.uconn.edu";
	private int _icarusPort = 3499;
	private Socket _socket = null;
	private PrintWriter _out = null;
	private BufferedReader _in = null;
	
	// ~~~~~~~~~~~~~~~ Game Info ~~~~~~~~~~~~~~~
	private static String _gameID;
	private static Boolean _myColor;
	
	// ~~~~~~~~~~~~~~~ Search Info ~~~~~~~~~~~~~~~
	private static MiniMaxSearcher _sherlock;
	
	// ~~~~~~~~~~~~~~~ Other SHIT ~~~~~~~~~~~~~~~
	private static Map<String,Integer> _IcarusMap = new HashMap<String,Integer>();
	private static long _timeStart, _timeStop, _makeConverterStart, _makeConverterStop;
	
	
	public HPClient() {
		_sherlock = new MiniMaxSearcher();
		_socket = this.openSocket(_icarusAddress, _icarusPort);
		
		//time creation of converter
		_makeConverterStart = System.currentTimeMillis();
		this.makeIcarusConverter();
		_makeConverterStop = System.currentTimeMillis();
	}

	
	public static void main(String [] args) {
		
		HPClient HPClient = new HPClient();
		String readMessage;
		
		try {
			Scanner reader = new Scanner(System.in);
			HPClient.readAndEcho(); // start message
			HPClient.readAndEcho(); // ID query
			//_user = reader.next();
			HPClient.writeMessageAndEcho(_user); // user ID
			HPClient.readAndEcho(); // password query
			//_pw = reader.next();
			HPClient.writeMessage(_pw); // password
			HPClient.readAndEcho(); // opponent query
			//_opponent = reader.next();
			HPClient.writeMessageAndEcho(_opponent); // opponent
			reader.close();
			
			HPClient.setGameID(HPClient.readAndEcho().substring(5, 6)); // game
			HPClient.setColor(HPClient.readAndEcho().substring(6, 11)); // color
			System.out.println("I am playing as " + getColorAsString()
					+ " in game number (approximately)" + getGameID()+"00");
			
			//get start time in milliseconds
			_timeStart = System.nanoTime();
			
			//create init board
			Board _board = new Board(HPClient, null, _myColor); //passing null is OK here, handled in Board constructor
			readMessage = HPClient.readAndEcho(); // either returns time if black or black's move if white
			
			if (getColorAsString().equals("White")) { //playing as white
				// black already made a move
				Move move = convertMove(readMessage);
				_board = _board.result(move);// write the move to the board
				System.out.println(_board.toString()); // show the move
				readMessage = HPClient.readAndEcho(); // time left
			}
			while (!gameOver(readMessage)) {
				
				//random valid move - no minimax
//				List<Move> actions = _board.getActions();
//				Move nextMove = actions.get((int)(Math.random() * actions.size()));
				
				//define minimax depth
				int depth = 7;
				Move nextMove = _sherlock.minimaxDecision(_board, depth);
				
				HPClient.writeMessageAndEcho(nextMove.getMessage());// send the move to the server
				readMessage = HPClient.readAndEcho(); // show the move received by the server
				_board = _board.result(nextMove); // write the move to the board
				_board.updateKingPositions();
				
				System.out.println(_board.toString()); // print the board
				readMessage = HPClient.readAndEcho(); // opponent's move
				
				if (gameOver(readMessage)) {break;}
				
				Move serverMove = convertMove(readMessage);
				_board = _board.result(serverMove);// write the move to the board
				System.out.println(_board.toString()); // show the move
				readMessage = HPClient.readAndEcho(); // move query
			}
			
			if (readMessage.contains(getColorAsString())) {
				System.out.println("YOU WIN!!!");
			} else if (!readMessage.contains("Draw")) {
				System.out.println("You lose");
			}
			HPClient.getSocket().close();
		} catch (IOException e) {
			System.out.println("Failed in read/close");
			System.exit(1);
		}
		
		_timeStop = System.nanoTime();
		long time = (_timeStop - _timeStart) + (_makeConverterStop - _makeConverterStart);
		System.out.println("\n\nTotal Time (ns) : " + time);
		System.out.println("Total Time (ms) : " + time/1000000.0);
		
		System.out.println("Total Recursion : " + _sherlock.getFinalRecursion());
		
		
	}
	
	
	/*
	 * Test if server response has "Result" in it, meaning game is over
	 */
	public static boolean gameOver(String readMessage) {
		if (readMessage.contains("Result")) {
			return true;
		} else return false;
	}
	/*
	 * Converts from server's (X:X):(X:X).. notation to a new Move object with valid samuels indices
	 */
	public static Move convertMove(String serverMove) {
		Move newMove = null;
		String delims = "[()]+";
		String[] tokens = serverMove.split(delims);
		int fromPos = _IcarusMap.get(tokens[1]);
		int toPos = _IcarusMap.get(tokens[3]);
		if (Math.abs(fromPos-toPos) > 5) { //it's a jump move - indices differ than more than 5
			newMove = new Move(!_myColor); 
			newMove.addAction(fromPos, toPos, true);
			if (tokens.length > 4) { //it's a multi-jump!
				for (int i=3 ; i < tokens.length-2 ; i += 2 ) {
					fromPos = _IcarusMap.get(tokens[i]);
					toPos = _IcarusMap.get(tokens[i+2]);
					newMove.addAction(fromPos, toPos, true);
				}
			}
		} else { //it's a regular move
			newMove = new Move(!_myColor);
			newMove.addAction(fromPos, toPos, false);
		}
		return newMove;
	}
	
	/*
	 * ~~~~~~~~~~~~~~~ Server Communication Methods ~~~~~~~~~~~~~~~
	 */
	public String readAndEcho() throws IOException {
		String readMessage = _in.readLine();
		System.out.println("IN: " + readMessage);
		return readMessage;
	}
	public void writeMessage(String message) throws IOException {
		_out.print(message + "\r\n");
		_out.flush();
	}
	public void writeMessageAndEcho(String message) throws IOException {
		_out.print(message + "\r\n");
		_out.flush();
		System.out.println("OUT: " + message);
	}
	private Socket openSocket(String address,int port) {
		try {
			_socket = new Socket(address, port);
			_out = new PrintWriter(_socket.getOutputStream(), true);
			_in = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
		} catch (UnknownHostException e) {
			System.out.println("Unknown host: " + address);
			System.exit(1);
		} catch (IOException e) {
			System.out.println("No I/O");
			System.exit(1);
		}
		return _socket;
	}
	
	public Socket getSocket() {
		return _socket;
	}
	public PrintWriter getOut() {
		return _out;
	}
	public BufferedReader getIn() {
		return _in;
	}
	
	
	// ############################# RANDOM SHIT
	public void setGameID(String id) {
		_gameID = id;
	}
	public static String getGameID() {
		return _gameID;
	}
	public void setColor(String color) {
		if (color.equals("White")) {
			_myColor = false;
		} else {
			_myColor = true;
		}
	}
	public static String getColorAsString() {
		if (_myColor) {
			return "Black";
		} else {
			return "White";
		}
	}
	public Boolean getColor() {
		return _myColor;
	}

	
	
	/*
	 * adding map key,values for converting between server moves and our Move objects with
	 * samuel's indices
	 * 
	 */
	public void makeIcarusConverter() {
		_IcarusMap.put("7:1", 1);
		_IcarusMap.put("7:3", 2);
		_IcarusMap.put("7:5", 3);
		_IcarusMap.put("7:7", 4);
		
		_IcarusMap.put("6:0", 5);
		_IcarusMap.put("6:2", 6);
		_IcarusMap.put("6:4", 7);
		_IcarusMap.put("6:6", 8);
		
		_IcarusMap.put("5:1", 10);
		_IcarusMap.put("5:3", 11);
		_IcarusMap.put("5:5", 12);
		_IcarusMap.put("5:7", 13);
		
		_IcarusMap.put("4:0", 14);
		_IcarusMap.put("4:2", 15);
		_IcarusMap.put("4:4", 16);
		_IcarusMap.put("4:6", 17);
		
		_IcarusMap.put("3:1", 19);
		_IcarusMap.put("3:3", 20);
		_IcarusMap.put("3:5", 21);
		_IcarusMap.put("3:7", 22);
		
		_IcarusMap.put("2:0", 23);
		_IcarusMap.put("2:2", 24);
		_IcarusMap.put("2:4", 25);
		_IcarusMap.put("2:6", 26);
		
		_IcarusMap.put("1:1", 28);
		_IcarusMap.put("1:3", 29);
		_IcarusMap.put("1:5", 30);
		_IcarusMap.put("1:7", 31);
		
		_IcarusMap.put("0:0", 32);
		_IcarusMap.put("0:2", 33);
		_IcarusMap.put("0:4", 34);
		_IcarusMap.put("0:6", 35);
		
	}
	
}











