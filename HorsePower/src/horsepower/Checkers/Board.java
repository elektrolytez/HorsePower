package horsepower.Checkers;

import horsepower_main.HPClient;

import java.util.ArrayList;
import java.util.List;


public class Board {

	private HPClient _HPClient;
	private String[] _board; // samuels board representation
	private List<Move> _actions = new ArrayList<Move>();
	private List<Move> _jumps = new ArrayList<Move>();
	private List<Move> _possibleMoves = new ArrayList<Move>(); //stores _actions if&f _jumps is empty
	
	// ~~~~~~~~~~~~~~~~ Player Specific Information ~~~~~~~~~~~~~~
	private Boolean _playerToMove, _HPPlayer; //our client's player
	private static String _regPiece, _kingPiece;
	private List<Integer> _regMovements = new ArrayList<Integer>();
	private List<Integer> _kingMovements = new ArrayList<Integer>();
	private List<Integer> _regsList = new ArrayList<Integer>(); //stores locations of player's regular pieces
	private List<Integer> _kingsList = new ArrayList<Integer>(); //stores locations of player's king pieces
	private List<Integer> _kingRowIndices = new ArrayList<Integer>();
	private static String _oppSymbols;
	private String _oppKing;
	private int _oppPceCount;
	
	
	public Board(HPClient HPClient,String[] board, Boolean player) {

		_HPClient = HPClient;
		_playerToMove = player;
		
		if (_playerToMove) { // player is black
			_oppSymbols = "w Q";
			_oppKing = "Q";
			_regPiece = "b";
			_kingPiece = "K";
			_regMovements.add(4); //black moves "downward" or in increasing index order
			_regMovements.add(5);
			_kingRowIndices.add(32);
			_kingRowIndices.add(33);
			_kingRowIndices.add(34);
			_kingRowIndices.add(35);
		} else { // player is white
			_oppSymbols = "b K"; //opponent's pieces
			_oppKing = "K";
			_regPiece = "w";
			_kingPiece = "Q";
			_regMovements.add(-4); //white moves "upward" or in decending index order
			_regMovements.add(-5);
			_kingRowIndices.add(1);
			_kingRowIndices.add(2);
			_kingRowIndices.add(3);
			_kingRowIndices.add(4);
		}
		//kings have full mobility
		_kingMovements.add(-4);
		_kingMovements.add(-5);
		_kingMovements.add(4);
		_kingMovements.add(5);

		if (board == null) {
			this.initBoard();
		} else {
			_board = board;
		}
		
		_HPPlayer = _HPClient.getColor();
		this.findActions();
		_possibleMoves = this.getActions();
	}
	
	public void findActions() {
		// find actions
		this.findPieces();
		
		for (Integer regPos : _regsList) {
			this.findJumps(regPos, regPos, null, _regMovements); //passing with previous pos = start pos
		}
		for (Integer kingPos : _kingsList) {
			this.findJumps(kingPos, kingPos, null, _kingMovements);
		}
		if (_jumps.isEmpty()) { //if any jumps were found no need to look for regular moves
			for (Integer regPos : _regsList) {
				this.findMoves(regPos, _regMovements);
			}
			for (Integer kingPos : _kingsList) {
				this.findMoves(kingPos, _kingMovements);
			}
		}
	}

	/*
	 * non recursive method for finding all regular actions there are available
	 */
	public void findMoves(int startPos, List<Integer> validSteps) {
		for (Integer step : validSteps) {
			if (this.validTile(startPos + step) && this.validMove(startPos + step)) {
				Move act = new Move(startPos, startPos+step,_playerToMove);
				_actions.add(act);
			}
		}
	}
	
	/*
	 * recursive find jump method that works for any type of piece
	 */
	public void findJumps(int prevPos, int startPos, Move prevMove, List<Integer> validSteps) {
		for (Integer step : validSteps) {
			int dubStep = step*2; //because we're jumping
			int nextPos = startPos + dubStep; //calculate jump destination
			if (nextPos != prevPos) { //excludes looking at where it came from - for recursion
				if (this.validTile(startPos + step)) { //checks if valid index of board
					if (_oppSymbols.contains(_board[startPos + step])) { //does pos+step contain enemy piece
						if (this.validMove(nextPos)) { // found enemy, check if jump dest is free
							if (prevMove == null) { // if prevmove is not set, make new move and add it to jumps
								Move j = new Move(startPos, nextPos, _playerToMove);
								j.makeJump(true);
								j.updateStep(dubStep);
								_jumps.add(j);
								
								//testing if non king piece is reaching valid king row
								if (this.isKingRow(nextPos) && _board[startPos].equals(_regPiece)) {
									break;
								} else { //if not, continue recursing
									findJumps(startPos, nextPos, j, validSteps);									
								}
							} else { //else add jump step to prev move (making it a multi-jump)
								prevMove.addJump(startPos, nextPos);
								prevMove.updateStep(dubStep);

								//testing if non king piece is reaching valid king row - if so, stop recursing
								if (this.isKingRow(nextPos) && _board[prevMove.from()].equals(_regPiece)) {
									break;
								} else { //if not, continue recursing
									findJumps(startPos, nextPos, prevMove, validSteps);									
								}
							}
						}
					}
				}
			}
		}
	}
	
	/*
	 * Iterates through the board and populates the lists of pieces with their indices
	 */
	public void findPieces() {
		for (int i=1 ; i<36 ; i++ ) {
			String val = _board[i];
			if (val == _regPiece) {
				_regsList.add(i);
			} else if (val == _kingPiece) {
				_kingsList.add(i);
			} else if (_oppSymbols.contains(val)) {
				_oppPceCount++;
				if (val.equals(_oppKing)) { //kings worth 2 pieces
					_oppPceCount++;
				}
			} else {
				//do nothing - tile content is not relevant
			}
		}
	}
	
	/*
	 * validMove returns true if the board at that position is a valid tile and is unoccupied
	 * 
	 * validMove and validTile are a bit redundent and can be condensed probably
	 * but not worth it right now
	 */
	public Boolean validMove(int pos) {
		if (pos <= 0 || pos > 35) {
			return false;
		} else if ((pos % 9) == 0) {
			return false;
		} else if (_board[pos].equals("_")) {
			return true;
		} else {
			return false;
		}
	}
	public Boolean validTile(int pos) {
		if (pos < 0 || pos > 35) {
			return false;
		} else if ((pos % 9) == 0) {
			return false;
		} else {
			return true;
		}
	}
	public Boolean isKingRow(int pos) {
		if (_kingRowIndices.contains(pos)) {
			return true;
		} else {
			return false;
		}
	}
	
	/*
	 * Returns TRUE if current game state is terminal or if max depth of minimax has been reached
	 */
	 public Boolean isTerminal(int count) {
		 if (_possibleMoves.isEmpty()) {
			 return true;
		 } else {
			 if (count <= 0) {
				 return true;
			 } else {
				 return false;
			 }
		 }
	 }
	

	/*
	 * Takes in a Move and returns a new board that resembles game state after evaluating the move
	 */
	public Board result(Move move) {
		//make copy of cur board
		String[] newBoard = new String[36];
		for (int i=0 ; i<36 ; i++) {
			newBoard[i] = _board[i];
		}
		if (move.isJump()) { //move is a jump move
			String piece = _board[move.from()];
			for (int[] jump : move.getJumpList()) { //combine all jump moves if move is multi-jump
				int fromIndex = jump[0];
				int toIndex = jump[1];
				int jumpedIndex = fromIndex - (fromIndex-toIndex)/2; // calculate the index that is being jumped
				newBoard[fromIndex] = "_";
				newBoard[jumpedIndex] = "_";
				
				//test if destination is king row and jumping piece is regular piece
				if (this.isKingRow(toIndex) && piece.equals(_regPiece)) {
					newBoard[toIndex] = _kingPiece; //if so, make king and end move
					break;
				} else { //else continue on your merry way
					newBoard[toIndex] = piece;
				}	
			}
		} else { //move is not a jump move
			int fromIndex = move.from();
			int toIndex = move.to();
			String piece = _board[fromIndex];
			newBoard[fromIndex] = "_";
			if (this.isKingRow(toIndex) && piece.equals(_regPiece)) {
				newBoard[toIndex] = _kingPiece;
			} else {
				newBoard[toIndex] = piece;
			}
		}
		Board resultBoard = new Board(_HPClient, newBoard, !move.player());
		return resultBoard;
	}
	
	/*
	 * Creates and returns a string representation of board
	 */
	public String toString() {
		String[] b = _board;
		String row7 = "| # | "+b[1]+" | # | "+b[2]+" | # | "+b[3]+" | # | "+b[4]+" |\n";
		String row6 = "| "+b[5]+" | # | "+b[6]+" | # | "+b[7]+" | # | "+b[8]+" | # |\n";
		String row5 = "| # | "+b[10]+" | # | "+b[11]+" | # | "+b[12]+" | # | "+b[13]+" |\n";
		String row4 = "| "+b[14]+" | # | "+b[15]+" | # | "+b[16]+" | # | "+b[17]+" | # |\n";
		String row3 = "| # | "+b[19]+" | # | "+b[20]+" | # | "+b[21]+" | # | "+b[22]+" |\n";
		String row2 = "| "+b[23]+" | # | "+b[24]+" | # | "+b[25]+" | # | "+b[26]+" | # |\n";
		String row1 = "| # | "+b[28]+" | # | "+b[29]+" | # | "+b[30]+" | # | "+b[31]+" |\n";
		String row0 = "| "+b[32]+" | # | "+b[33]+" | # | "+b[34]+" | # | "+b[35]+" | # |\n";
		String boardAsString = row7+row6+row5+row4+row3+row2+row1+row0;
		return boardAsString;
	}
	
	// create initial board
	public void initBoard() {
		_board = new String[36];
		for (int i=1 ; i<=13 ; i++ ) {
			if (i != 9) {
				_board[i] = "b";
			}
		}
		for (int i=14 ; i<=22 ; i++ ) {
			if (i != 18) {
				_board[i] = "_";
			}
		}
		for (int i=23 ; i<=35 ; i++ ) {
			if (i != 27) {
				_board[i] = "w";
			}
		}
		//invalid places
		_board[0] = "#";
		_board[9] = "#";
		_board[18] = "#";
		_board[27] = "#";
	}

	/*
	 * @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@~ EVALUATING BOARD ~@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
	 * 								..needs modifications
	 */
	
	public Double evaluateFor(Boolean player) {
		// eventually the return value will be the result of some function that takes all of the following variables
		double perPceCount = byPieceCount(player);
		
		//testing...
		//double perPceCount = Math.random() * 1000;
		
		return perPceCount;
	}

	
	public Double byPieceCount(Boolean player) {
		int _plyrPceCount = _regsList.size() + _kingsList.size()*2;
		
		//this is sloppy logic and really confusing to see what is correct - I can explain better in person
		if (_HPPlayer.equals(player)) {
			if (_plyrPceCount > _oppPceCount) {
				return 2.0 + Math.random();
			} else if (_plyrPceCount == _oppPceCount) { // player is white
				return 0.0;
			} else {
				return -2.0 + Math.random();
			}
		} else {
			if (_plyrPceCount > _oppPceCount) {
				return -2.0  + Math.random();
			} else if (_plyrPceCount == _oppPceCount) { // player is white
				return 0.0;
			} else {
				return 2.0  + Math.random();
			}
		}
	}
	
	// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@~ GETTERS / SETTERS ~@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
	
	public Boolean getPlayer() {
		return _playerToMove;
	}
	public List<Move> getActions() {
		if (_jumps.isEmpty()) {
			return _actions;
		} else {
			return _jumps;
		}
	}
	public String[] getBoard() {
		return _board;
	}
	
	
}
