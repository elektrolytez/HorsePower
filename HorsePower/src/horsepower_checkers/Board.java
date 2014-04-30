package horsepower_checkers;

import horsepower_main.HPClient;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Board {

	private HPClient _HPClient;
	private String[] _board; // samuels' board representation
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
	private List<Integer> _oppKingRowIndices = new ArrayList<Integer>();
	private static String _oppSymbols;
	private String _oppKing, _oppRegPce;
	private int _oppRegCount, _oppKingCount;
	
	
	public Board(HPClient HPClient,String[] board, Boolean player) {

		_HPClient = HPClient;
		_playerToMove = player;
		
		if (_playerToMove) { // player is black
			this.initBlackValues();
		} else { // player is white
			this.initWhiteValues();
		}
		
		//kings have full mobility
		_kingMovements.add(-4);
		_kingMovements.add(-5);
		_kingMovements.add(5);
		_kingMovements.add(4);

		if (board == null) {
			this.initBoard();
		} else {
			_board = board;
		}
		
		_HPPlayer = _HPClient.getColor();
		this.findActions();
		_possibleMoves = this.getActions();
		//System.out.println(_possibleMoves.size());
	}
	
	public void initBlackValues() {
		_oppSymbols = "w Q";
		_oppRegPce = "w";
		_oppKing = "Q";
		_regPiece = "b";
		_kingPiece = "K";
		_regMovements.add(4); //black moves "downward" or in increasing index order
		_regMovements.add(5);
		_kingRowIndices.add(32);
		_kingRowIndices.add(33);
		_kingRowIndices.add(34);
		_kingRowIndices.add(35);
		_oppKingRowIndices.add(1);
		_oppKingRowIndices.add(2);
		_oppKingRowIndices.add(3);
		_oppKingRowIndices.add(4);
	}
	public void initWhiteValues() {
		_oppSymbols = "b K"; //opponent's pieces
		_oppRegPce = "b";
		_oppKing = "K";
		_regPiece = "w";
		_kingPiece = "Q";
		_regMovements.add(-4); //white moves "upward" or in decending index order
		_regMovements.add(-5);
		_kingRowIndices.add(1);
		_kingRowIndices.add(2);
		_kingRowIndices.add(3);
		_kingRowIndices.add(4);
		_oppKingRowIndices.add(32);
		_oppKingRowIndices.add(33);
		_oppKingRowIndices.add(34);
		_oppKingRowIndices.add(35);
	}
	
	
	public void findActions() {
		// find actions
		this.findPieces();
		for (Integer regPos : _regsList) {
			this.findJumpMoves(regPos, regPos, null, _regMovements); //passing with previous pos = start pos
		}
		for (Integer kingPos : _kingsList) {
			this.findJumpMoves(kingPos, kingPos, null, _kingMovements);
		}
		if (_jumps.isEmpty()) { //if any jumps were found no need to look for regular moves
			for (Integer regPos : _regsList) {
				this.findRegMoves(regPos, _regMovements);
			}
			for (Integer kingPos : _kingsList) {
				this.findRegMoves(kingPos, _kingMovements);
			}
		}
	}

	/*
	 * non recursive method for finding all regular actions there are available
	 */
	public void findRegMoves(int startPos, List<Integer> validSteps) {
		for (Integer step : validSteps) {
			if (this.validTile(startPos + step) && this.validMove(startPos + step)) {
				Move act = new Move(_playerToMove); 
				act.addAction(startPos, startPos+step, false);
				_actions.add(act);
			}
		}
	}
	
	public void findJumpMoves(int prevPos, int curPos, Move lastMove, List<Integer> degOfFreedom) {
		List<Integer> nextPosList;// = findValidNextJumpPos(prevPos, curPos, degOfFreedom);
		if (lastMove == null) {
			nextPosList = findValidNextJumpPos(prevPos, curPos, degOfFreedom);
		} else {
			int[] preLoc = lastMove.getLastAct();
			nextPosList = findValidNextJumpPos(preLoc[0], preLoc[1], degOfFreedom);
			nextPosList = this.removeLoops(curPos, lastMove, nextPosList);			// takes care of infinite jump loop problem
		}
		int size = nextPosList.size();
		if (size == 0) { //no more jumps - base case
			if (lastMove != null) {
				//System.out.println("ADDING TO JUMP LIST :\n"+ lastMove.getMessage());
				_jumps.add(lastMove);
			}
			return;
		} else if (size > 1) {
			for (Integer nextPos : nextPosList) {
				Move m;
				if (lastMove == null) {
					m = new Move(this._playerToMove);
					m.addAction(curPos, nextPos, true);
				} else {
					List<int[]> lastMoveHistory = lastMove.getJumpListCopy();
					m = new Move(lastMove.player());
					m.setJumpList(lastMoveHistory);
					m.addAction(curPos, nextPos, true);
				}
				if (!this.isKingUpAction(curPos, nextPos, lastMove)  ) {
					this.findJumpMoves(curPos, nextPos, m, degOfFreedom);
				} else {
					_jumps.add(m);
				}
			}
		} else { //one move to analyze
			Move regJump;
			if (lastMove == null) {
				regJump = new Move(_playerToMove);
				_jumps.add(regJump);
			} else {
				List<int[]> lastMoveHistory = lastMove.getJumpListCopy();
				regJump = new Move(_playerToMove);
				regJump.setJumpList(lastMoveHistory);
			}
			int nextPos = nextPosList.get(0);
			if (this.isKingUpAction(curPos, nextPos, lastMove)) {
				regJump.addAction(curPos, nextPos, true);
				_jumps.add(regJump);
			} else {
				regJump.addAction(curPos, nextPos, true);
				this.findJumpMoves(curPos, nextPos, regJump, degOfFreedom);
			}
		}
	}
	public List<Integer> removeLoops(int curPos, Move lastMove, List<Integer> nextPosList) {
		List<int[]> actionList = lastMove.getJumpList();
		if (actionList.size() >= 4) {
			
			for (int[] act : actionList) {
				if (act[0] == curPos) {
					int i = nextPosList.indexOf(act[1]);
					if (i!=-1) {
						nextPosList.remove(i);
					}
				}
				if (act[1] == curPos) {
					int i = nextPosList.indexOf(act[0]);
					if (i!=-1) {
						nextPosList.remove(i);
					}
				}
			}
			return nextPosList;
		} else {
			return nextPosList;
		}
	}
	public boolean isKingUpAction(int curPos, int nextPos, Move lastMove) {
		if (this.isKingRow(nextPos)) {
			if (lastMove == null) {
				if (_board[curPos].equals(_regPiece)) {
					return true;
				} else {
					return false;
				}
			} else {
				if (_board[lastMove.getFirstAct()[0]].equals(_regPiece)) {
					return true;
				} else {
					return false;
				}
			}
		} else {
			return false;
		}
	}
	
	public List<Integer> findValidNextJumpPos(int prevPos, int curPos, List<Integer> degOfFreedom) {
		List<Integer> possibleSteps = new ArrayList<Integer>();
		if (!degOfFreedom.isEmpty()) {
			for (Integer step : degOfFreedom) {
				if ((curPos + step != prevPos) && (curPos + step*2 != prevPos)) {
					if (this.validTile(curPos+step)) {
						if (_oppSymbols.contains(_board[curPos+step])) {
							possibleSteps.add(curPos + step*2);
						}
					}
				}
			}
			if (possibleSteps.isEmpty()) {
				//skip loop and return
			} else {
				Iterator<Integer> stepIter = possibleSteps.iterator();
				while (stepIter.hasNext()) {
					int s = stepIter.next();
					if (!this.validMove(s)) {
						stepIter.remove();
					}
				}
			}
			return possibleSteps;
		} else {
			return degOfFreedom;
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
				_oppRegCount++;
				if (val.equals(_oppKing)) { //kings worth 2 pieces
					_oppKingCount++;
				}
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
		String[] newBoard = new String[36];
		for (int i=0 ; i<36 ; i++) {
			newBoard[i] = _board[i];
		}
		if (move.isMoveAJump()) { //move is a jump move
			String piece = _board[move.getFirstAct()[0]];
			for (int[] jump : move.getJumpList()) { //combine all jump moves if move is multi-jump
				int fromIndex = jump[0];
				int toIndex = jump[1];
				int jumpedIndex = fromIndex - (fromIndex-toIndex)/2; // calculate the index that is being jumped
				newBoard[fromIndex] = "_";
				newBoard[jumpedIndex] = "_";
				//test if destination is king row and jumping piece is regular piece
				if (this.isKingUpAction(fromIndex, toIndex, move)) { //this.isKingRow(toIndex) && piece.equals(_regPiece)
					newBoard[toIndex] = _kingPiece; //if so, make king and end move
					//break;
				} else { //else continue on your merry way
					newBoard[toIndex] = piece;
				}	
			}
		} else { //move is not a jump move
			int fromIndex = move.getFirstAct()[0];
			int toIndex = move.getFirstAct()[1];
			String piece = _board[fromIndex];
			newBoard[fromIndex] = "_";
			if (this.isKingUpAction(fromIndex, toIndex, move)) {
				newBoard[toIndex] = _kingPiece;
			} else {
				newBoard[toIndex] = piece;
			}
		}
		Board resultBoard = new Board(_HPClient, newBoard, !move.player());
		return resultBoard;
	}
	
	public void updateKingPositions() {
		//only do these checks if there are still reg pieces on board
		if ((_regsList.size() + _oppRegCount) > 0) {
			for (Integer p : _kingRowIndices) {
				if (_board[p].equals(_regPiece)) {
					_board[p] = _kingPiece;
				}
			}
			for (Integer p : _oppKingRowIndices) {
				if (_board[p].equals(_oppRegPce)) {
					_board[p] = _oppKing;
				}
			}
		}
	}
	
	/*
	 * Creates and returns a string representation of board
	 */
	public String toString() {
		String[] b = _board;
		String row7 = "7| # | "+b[1]+" | # | "+b[2]+" | # | "+b[3]+" | # | "+b[4]+" |\n";
		String row6 = "6| "+b[5]+" | # | "+b[6]+" | # | "+b[7]+" | # | "+b[8]+" | # |\n";
		String row5 = "5| # | "+b[10]+" | # | "+b[11]+" | # | "+b[12]+" | # | "+b[13]+" |\n";
		String row4 = "4| "+b[14]+" | # | "+b[15]+" | # | "+b[16]+" | # | "+b[17]+" | # |\n";
		String row3 = "3| # | "+b[19]+" | # | "+b[20]+" | # | "+b[21]+" | # | "+b[22]+" |\n";
		String row2 = "2| "+b[23]+" | # | "+b[24]+" | # | "+b[25]+" | # | "+b[26]+" | # |\n";
		String row1 = "1| # | "+b[28]+" | # | "+b[29]+" | # | "+b[30]+" | # | "+b[31]+" |\n";
		String row0 = "0| "+b[32]+" | # | "+b[33]+" | # | "+b[34]+" | # | "+b[35]+" | # |\n";
		String rowLabel = "   0   1   2   3   4   5   6   7";
		String boardAsString = row7+row6+row5+row4+row3+row2+row1+row0+rowLabel;
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
		//double perPceCount = byPieceCount(player);
		
		return Math.random();
		
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
