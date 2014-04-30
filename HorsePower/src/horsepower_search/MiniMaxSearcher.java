package horsepower_search;


import java.util.Collections;
import java.util.List;
import java.util.Random;

import horsepower_checkers.*;


/*
 * MiniMax adversarial search algorithm with alpha-beta pruning
 */
public class MiniMaxSearcher {

	private int _recurCount;
	private int _totalRecursion = 0;
	
	public MiniMaxSearcher() {	}
	
	
	public Move minimaxDecision(Board board, int depth){
		double vBest = Double.NEGATIVE_INFINITY;
		double v;
		Move bestAction = null;
		
		int tempCount = 0;
		for (Move a : board.getActions()) {
			_recurCount = 0;
			v = maxValue(board, board.result(a), depth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
			if (v > vBest) {
				vBest = v;
				bestAction = a;
			}
			tempCount = tempCount+_recurCount;
		}
		System.out.println("Total recursive calls : "+tempCount);
		return bestAction;
	}

	/*
	 * ALPHA = the value of the best alternative for MAX along the path to 'board'
	 * BETA = the value of the best alternative for MIN along the path to 'board'
	 */
	public double maxValue(Board prevBoard, Board board, int depth, double ALPHA, double BETA){
		if (board.isTerminal(depth)) {
			return board.evaluateFor(prevBoard, board.getPlayer());
		} else {
			_recurCount++;
			_totalRecursion++;
			double v = Double.NEGATIVE_INFINITY;
			
			List<Move> moveList = board.getActions();
			long seed = System.nanoTime();
			Collections.shuffle(moveList, new Random(seed));
			
			for (Move s : moveList) { //board.getActions()
				v = Math.max(v, minValue(board, board.result(s), depth - 1, ALPHA, BETA));
				if (v >= BETA) {
					return v;
				}
				ALPHA = Math.max(ALPHA, v);
			}
			return v;	
		}
	}
	
	public double minValue(Board prevBoard, Board board, int depth, double ALPHA, double BETA){
		if (board.isTerminal(depth)) {
			return board.evaluateFor(prevBoard, board.getPlayer());
		} else {
			_recurCount++;
			_totalRecursion++;
			double v = Double.POSITIVE_INFINITY;
			
			List<Move> moveList = board.getActions();
			long seed = System.nanoTime();
			Collections.shuffle(moveList, new Random(seed));
			
			for (Move s : moveList ) { //board.getActions()
				v = Math.min(v, maxValue(board, board.result(s), depth - 1, ALPHA, BETA));
				if (v <= ALPHA) {
					return v;
				}
				BETA = Math.min(BETA, v);
			}
			return v;	
		}
	}
	
	public int getFinalRecursion() {
		return _totalRecursion;
	}
}
