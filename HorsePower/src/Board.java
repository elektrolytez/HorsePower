
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * TODO
 * Figure out how to allow ASCII characters on print out
 * 
 * @author Ken
 *
 */

public class Board {

	/**
	 * Array storing all positions.
	 */
	private int[] _board;
	private boolean _player;
	private ArrayList<Integer> _blackcheckers;
	private ArrayList<Integer> _blackkings;
	private ArrayList<Integer> _whitecheckers;
	private ArrayList<Integer> _whitekings;


	/**
	 * Creates a board with the initial board positioning;
	 * A board stores information into an integer array where each values is mapped
	 * in the following format:
	 * -3 - Not Used
	 * -1 - Empty
	 * 1 - Black Checker
	 * 2 - White Checker
	 * 3 - Black King
	 * 4 - White King
	 * 
	 * Odds are black. Evens are white.
	 */
	public Board(){
		_player = true;
		_blackcheckers = new ArrayList<Integer>();
		_blackkings = new ArrayList<Integer>();
		_whitecheckers = new ArrayList<Integer>();
		_whitekings = new ArrayList<Integer>();


		/* create a more efficient algorithm */
		_board = new int[36];
		for(int i = 1; i < 9; i++)
		{
			_board[i] = 2;
			_whitecheckers.add(i);
		}
		for(int i= 10; i<14;i++)
		{
			_board[i] = 2;
			_whitecheckers.add(i);
		}
		for(int i = 14; i<22;i++)
		{
			_board[i] = -1;

		}
		for(int i = 23; i<32; i++)
		{
			_board[i] = 1;
			if(i != 27)
				_blackcheckers.add(i);
		}
		for(int i = 32; i<36; i++)
		{
			_board[i] = 1;
			_blackcheckers.add(i);
		}
		/*
                _board[4] = -3;
                _board[13] = -3;
               _board[22] = -3;
               _board[31] = -3;
		 */
		 _board[0] = -3;
		 _board[9] = -3;
		 _board[18] = -3;
		 _board[27] = -3;

	}


	/**
	 * Creates a preset board given a byte array;
	 * @param board - An int array[36] with values of [1, 4] for checkers
	 * -1 for open spaces, and -3 for unusable spaces.
	 */
	public Board(int[] board, boolean player){
		_board = board;
		_player = player;
		_blackcheckers = new ArrayList<Integer>();
		_blackkings = new ArrayList<Integer>();
		_whitecheckers = new ArrayList<Integer>();
		_whitekings = new ArrayList<Integer>();
		for(int i = 0; i < board.length; i++){
			if(_board[i] == 1){
				_blackcheckers.add(i);
			}
			else if(_board[i] == 2){
				_whitecheckers.add(i);
			}
			else if(_board[i] == 3){
				_blackkings.add(i);
			}
			else if(_board[i] == 4){
				_whitekings.add(i);
			}
		}
	}

	/**
	 * TEST METHOD
	 * Get all the positions for every black checker on the board.
	 * @return Returns a ArrayList of all black checker locations.
	 */
	public ArrayList<Integer> getBlackCheckers(){
		return _blackcheckers;
	}

	/**
	 * TEST METHOD
	 * Get all the positions for every white checker on the board.
	 * @return Returns a ArrayList of all white checker locations.
	 */
	public ArrayList<Integer> getWhiteCheckers(){
		return _whitecheckers;
	}

	/**
	 * TEST METHOD
	 * Get all the positions for every black king on the board.
	 * @return Returns a ArrayList of all black king locations.
	 */
	public ArrayList<Integer> getBlackKings(){
		return _blackkings;
	}

	/**
	 * TEST METHOD
	 * Get all the positions for every white king on the board.
	 * @return Returns a ArrayList of all white king locations.
	 */
	public ArrayList<Integer> getWhiteKings(){
		return _whitekings;
	}

	/**
	 * Collects a list of moves based on current turn.
	 * 
	 * @return moves from the function getPossibleMoves(boolean)
	 */
	public List<Move> actions(){
		List<Move> moves = new ArrayList<Move>();

//		String player = player();
//		Boolean turn = false;
//		if(player == "Black") turn = true;
		moves = getPossibleMoves(_player);

		return moves;
	}
	
	
	/**
	 * Determines the current turn
	 * After returning the current turn	
	 * the player's turn is switched
	 * @return String turn Black or White
	 */
	//DON'T THINK WE SHOULD FLIP HERE, CHANGED TO FLIP IN RESULT
//	public String player(){
//		if(_player)
//		{
////			_player =!_player; // flip the boolean 
//			return "Black";
//		}
//		else
//		{
////			_player =!_player; // flip the boolean
//			return "White";
//		}
//	}

	/** 
	 * Get all legal moves given a turn from the current board.
	 * @param turn TRUE if Black, FALSE is White.
	 * @return Returns an array of legal moves. Jumps over no _jumps if they exist.
	 */
	public List<Move> getPossibleMoves(boolean turn){ 

		ArrayList<Integer> _turncheckers; 
		ArrayList<Integer> _turnkings; 
		int _sign = 1; //default white
		if(turn){
			_turncheckers = _blackcheckers;
			_turnkings = _blackkings;
			_sign = -1;
		}
		else{
			_turncheckers = _whitecheckers;
			_turnkings = _whitekings;
		}

		List<Move> _jumpMoves = getJumps(_turnkings, _turncheckers, _sign); //get the possible jump moves

		if(_jumpMoves.size() == 0){ //if there are no jump moves available then
			//return the _nonJumps
			List<Move> _nonJumps = getNonJumps(_turnkings, _turncheckers, _sign);

			return _nonJumps;
		}
		else{
			return _jumpMoves;
		}
	}

	private List<Move> getNonJumps(ArrayList<Integer> kings, ArrayList<Integer> checkers, int _sign) {

		ArrayList<Move> _nonJumps = new ArrayList<Move>();//list of all _jumps

		/**
		 * check in four directions for a legal non-jump move
		 */
		for(Iterator<Integer> i = kings.iterator(); i.hasNext();){ 
			int _next = i.next();
			int _move;
			_move = _next+4;
			if(_move < 36){
				if(_board[_move] == -1){
					_nonJumps.add(new Move(_next, _move));
				}
			}
			_move = _next-4;
			if(_move > 0){
				if(_board[_move] == -1){
					_nonJumps.add(new Move(_next, _move));
				}
			}

			_move = _next+5;
			if(_move < 36){
				if(_board[_move] == -1){
					_nonJumps.add(new Move(_next, _move));
				}
			}

			_move = _next-5;
			if(_move > 0){
				if(_board[_move] == -1){
					_nonJumps.add(new Move(_next, _move));
				}
			}
		}

		for(Iterator<Integer> i = checkers.iterator(); i.hasNext();){//iterate through the list of checkers
			int _next = i.next();
			int _move;
			// System.out.print(_next);

			_move = _next+(_sign*4); //_sign is 1 or -1, get diagon in array is to check +/- 4

			if(_move > 0 && _move < 36){//check legal position

				if(_board[_move] == -1){//if is empty
					_nonJumps.add(new Move(_next, _move));
				}/*
                        	else{
                        		System.out.println("|4:"+_board[_move]);	

                        	}*/
			}

			_move = _next+(_sign*5); //_sign is 1 or -1, and get diagonal in an array is to check +/- 5
			if(_move > 0 && _move < 36){ //check for legal positions
				if(_board[_move] == -1){
					_nonJumps.add(new Move(_next, _move));
				}/*
                                else{
                            		System.out.println("|5:"+_board[_move]);	

                            	}*/
			}
		}
		/* test the _move list
                for(int i = 0; i<_nonJumps.size(); i++)
                {
                	System.out.println(_nonJumps.get(i));
                }
		 */

		return _nonJumps;
	}


	/**
	 * Gets all moves that jump
	 * if jump exists it must be taken
	 */
	private List<Move> getJumps(ArrayList<Integer> kings, ArrayList<Integer> checkers, int sign){
		Iterator<Integer> kingiterator = kings.iterator();
		Iterator<Integer> checkeriterator = checkers.iterator();
		ArrayList<Move> all_jumps = new ArrayList<Move>();
		int take;
		if(sign == -1){
			take = 0; //Black takes even pieces.
		}
		else{
			take = 1; //White takes odd pieces.
		}


		while(kingiterator.hasNext()){ //while there still exists a king
			int _cPosition = kingiterator.next(); 

			LinkedList<Integer> prev_jumps = new LinkedList<Integer>(); //list to hold a previous jump
			prev_jumps.add(_cPosition);

			LinkedList<Move> _jumps_cPosition = kingJumps(prev_jumps, new LinkedList<Integer>(), take);//store the jump

			Iterator<Move> j_cPositionit = _jumps_cPosition.iterator();
			while(j_cPositionit.hasNext()){
				all_jumps.add(j_cPositionit.next()); //while a jump exists add it
			}
		}

		while(checkeriterator.hasNext()){ //same as king except in 1 direction
			int _cPosition = checkeriterator.next();

			LinkedList<Integer> prev_jumps = new LinkedList<Integer>();
			prev_jumps.add(_cPosition);

			LinkedList<Move> _jumps_cPosition = checkerJumps(prev_jumps, sign, take);

			Iterator<Move> j_cPositionit = _jumps_cPosition.iterator();
			while(j_cPositionit.hasNext()){
				all_jumps.add(j_cPositionit.next());
			}

		}


		return all_jumps;
	}


	/**
	 * 
	 * @param position, current position
	 * @param _sign, whether black or white
	 * @param take, to take
	 * @return a list of _next available _jumps
	 */
	private LinkedList<Integer> _nextCheckerJumps(int position, int _sign, int take){
		LinkedList<Integer> _nxtJumps = new LinkedList<Integer>();
		for(int i = 0; i < 2; i++){
			int _adj = position+_sign*(4+i);
			if(_adj < 0 || _adj > 35){
				_adj = 0;
			}

			if(_board[_adj]%2 == take){
				int _landPos = _adj+_sign*(4+i);
				if(_landPos < 36 && _landPos > 0){
					if(_board[_landPos] == -1){
						_nxtJumps.add(_landPos);
					}
				}
			}
		}
		return _nxtJumps;
	}

	/**
	 * Recusively searches for all available jumps
	 * 
	 * @param prevJumps
	 * @param _sign
	 * @param take
	 * @return
	 */
	private LinkedList<Move> checkerJumps(LinkedList<Integer> prevJumps, int sign, int take){
		LinkedList<Move> _jumps = new LinkedList<Move>();        

		LinkedList<Integer> _nxtJumps = _nextCheckerJumps(prevJumps.getLast(), sign, take);

		if(_nxtJumps.size() == 0){
			if(prevJumps.size() > 1){
				int[] newmove = new int[prevJumps.size()];
				Iterator<Integer> previt = prevJumps.iterator();
				int count = 0;
				while(previt.hasNext()){
					newmove[count] = previt.next();
					count++;
				}
				_jumps.add(new Move(newmove));
			}
			return _jumps;
		}
		else{
			Iterator<Integer> _nextJumpIterator = _nxtJumps.iterator();
			while(_nextJumpIterator.hasNext()){
				prevJumps.addLast(_nextJumpIterator.next());
				LinkedList<Move> moreJumps = checkerJumps(prevJumps, sign, take);
				Iterator<Move> mjit = moreJumps.iterator();
				while(mjit.hasNext()){
					_jumps.add(mjit.next());
				}
				prevJumps.removeLast();
			}
			return _jumps;
		}
	}




	private LinkedList<Move> kingJumps(LinkedList<Integer> prevJumps, LinkedList<Integer> jumped, int take){
		LinkedList<Move> _jumps = new LinkedList<Move>();
		LinkedList<Jump> _nxtJumps = _nextKingJumps(prevJumps.getFirst(), prevJumps.getLast(), jumped, take);

		if(_nxtJumps.size() == 0){
			if(prevJumps.size() > 1){
				int[] newmove = new int[prevJumps.size()];
				Iterator<Integer> previt = prevJumps.iterator();
				int count = 0;
				while(previt.hasNext()){
					newmove[count] = previt.next();
					count++;
				}
				_jumps.add(new Move(newmove));
			}
			return _jumps;
		}
		else{
			Iterator<Jump> _nextJumpIterator = _nxtJumps.iterator();
			while(_nextJumpIterator.hasNext()){
				Jump _next = _nextJumpIterator.next();
				prevJumps.addLast(_next.getLandPosition());
				jumped.add(_next.getJumpedPosition());
				LinkedList<Move> moreJumps = kingJumps(prevJumps, jumped, take);
				Iterator<Move> mjit = moreJumps.iterator();
				while(mjit.hasNext()){
					_jumps.add(mjit.next());
				}
				jumped.removeLast();
				prevJumps.removeLast();
			}

			return _jumps;
		}
	}


	private LinkedList<Jump> _nextKingJumps(int first, int position, LinkedList<Integer> jumped, int take){
		LinkedList<Jump> king_jumps = new LinkedList<Jump>();

		int _sign = -1;
		for(int i = 0; i < 4; i++){
			if(i > 1){
				_sign = 1;
			}
			int _adj = position+_sign*(4+i%2);
			if(_adj < 0 || _adj > 35){
				_adj = 0;
			}

			if(_board[_adj]%2 == take){
				if(!jumped.contains(_adj)){
					int _landPos = _adj+_sign*(4+i%2);
					if(_landPos < 36 && _landPos > 0){
						if(_board[_landPos] == -1 || _landPos == first){
							king_jumps.add(new Jump(_landPos, _adj));
						}
					}
				}
			}
		}

		return king_jumps;
	}


	/**
	 * Get a board after making a move.
	 * @param m Move used to generate the new board.
	 * @return Returns a new board after making the move.
	 */
	 public Board result(Move m){
		int[] positions = m.getPositions();
		int psize = positions.length-1;
		int _last = positions[psize];
		int[] _newBoard = new int[36];           
		for(int i = 0; i < 36; i++){
			_newBoard[i] = _board[i];
		}

		int _startPos = positions[0];
		int _start = _newBoard[_startPos];
		_newBoard[_startPos] = -1;

		//Determine whether or not to king
		if(_start%2 == 1){
			if(_last < 5){
				_start = 3;
			}
		}
		else {
			if(_last > 31){
				_start = 4;
			}
		}

		_newBoard[_last] = _start;

		//Non-jumping _move.
		if(m.jumpCount() == 0){
			return new Board(_newBoard, !_player);
		}
		else{
			//calculate _jumps;
			int _lastPos = positions[0];
			for(int i = 1; i <= psize; i++){
				int _currentPos = positions[i];
				_newBoard[_currentPos+((_lastPos - _currentPos)/2)] = -1;
				_lastPos = _currentPos;
			}
			return new Board(_newBoard, !_player);
		}
	}

	/**
	 * TEST FUNCTION
	 * Returns what piece is located at the specified index.
	 * @param index Index value within the range (0,35).
	 * @return value in _board at index [1,4] for checkers -1 for empty, -3 for unused
	 */
	 public int getValue(int index){
		 return _board[index];
	 }

	 /**
	  * Get the number of black kings on this board.
	  * @return An int = # of black kings.
	  */
	 public int getBKingCount(){
		 return _blackkings.size();
	 }

	 /**
	  * Get the number of white kings on this board.
	  * @return an int = # of white kings.
	  */
	 public int getWKingCount(){
		 return _whitekings.size();
	 }

	 /**
	  * Get the number of black checkers on this board.
	  * @return An int = # of black checkers.
	  */
	 public int getBCheckerCount(){
		 return _blackcheckers.size();
	 }

	 /**
	  * Get the number of white checkers on this board.
	  * @return An int = # of white checkers.
	  */
	 public int getWCheckerCount(){
		 return _whitecheckers.size();
	 }

	 /**
	  * Returns a string representation of the board.
	  */
	 public String toString(){
		 String s = "";
		 int row = 0;
		 int count = 0;
		 //  boolean test = false;



		 for(int i = 35; i >0; i--){
			 int boardlabel = _board[i];



			 if(boardlabel == -3){
				 //do nothing
				 //test = true;
				 // System.out.print(Converter.CONVERT[boardlabel]+ " ");
			 }

			 else{

				 if(row%2 == 0){
					 if(boardlabel < 0) boardlabel = 0;
					 s = s+"|#|"+Converter.CONVERT[boardlabel];
					 // s += " . ";
					 //System.out.print(boardlabel + " ");
					 count++;
				 }
				 else {
					 if(boardlabel < 0) boardlabel = 0;
					 s = s+"|"+Converter.CONVERT[boardlabel]+"|#";
					 // s += " . ";
					 //System.out.print(boardlabel + " ");
					 count++;
				 }
				 if(count == 4){
					 //if(!test){
					 s = s+"|\n";
					 row++;
					 count = 0;
					 //}
					 /*else
                        	{
                        		s +="|-3\n";
                        		row++;
                        		count = 0;
                        		test = false;
                        	}*/
				 }

			 }
		 }

		 return s;
	 }

	 public void printMoveArray (List<Move> m)
	 {
		 Move _move;
		 for(int i = 0; i<m.size(); i++)
		 {
			 _move = m.get(i); 
			 System.out.println(_move.toString());
		 }

	 }
}
