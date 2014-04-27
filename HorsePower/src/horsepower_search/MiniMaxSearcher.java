package horsepower_search;

import horsepower_checkers.*;

public class MiniMaxSearcher {

	private int _depth = 0;
	private int _recurCount;
	private int _totalRecursion = 0;
	
	public MiniMaxSearcher() {	}
	
	
	public Move minimaxDecision(Board board, int depth){
		double vBest = Double.NEGATIVE_INFINITY;
		double v;
		Move bestAction = null;
		
		int tempCount = 0;
		for (Move a : board.getActions()) {
			_depth = depth;
			_recurCount = 0;
			v = minValue(board.result(a));
			if (v > vBest) {
				vBest = v;
				bestAction = a;
			}
			tempCount = tempCount+_recurCount;
		}
		System.out.println("Total recursive calls : "+tempCount);
		return bestAction;
	}
	
	public double maxValue(Board board){
		if (board.isTerminal(_depth)) {
			return board.evaluateFor(board.getPlayer());
		} else {
			_depth--;
			_recurCount++;
			_totalRecursion++;
			double v = Double.NEGATIVE_INFINITY;
			for (Move s : board.getActions()) {
				v = Math.max(v, minValue(board.result(s)));
			}
			//decrement depth by 1
			return v;	
		}
	}
	
	public double minValue(Board board){
		if (board.isTerminal(_depth)) {
			return board.evaluateFor(board.getPlayer());
		} else {
			_depth--;
			_recurCount++;
			_totalRecursion++;
			double v = Double.POSITIVE_INFINITY;
			for (Move s : board.getActions()) {
				v = Math.min(v, maxValue(board.result(s)));
			}
			return v;	
		}
	}
	
	
	public int getFinalRecursion() {
		return _totalRecursion;
	}
}
