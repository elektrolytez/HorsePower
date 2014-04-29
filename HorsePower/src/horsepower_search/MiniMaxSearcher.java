package horsepower_search;

import horsepower_checkers.*;

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
			v = maxValue(board.result(a), depth);
			if (v > vBest) {
				vBest = v;
				bestAction = a;
			}
			tempCount = tempCount+_recurCount;
		}
		System.out.println("Total recursive calls : "+tempCount);
		return bestAction;
	}
	
	
	public double maxValue(Board board, int depth){
		if (board.isTerminal(depth)) {
			return board.evaluateFor(board.getPlayer());
		} else {
			_recurCount++;
			_totalRecursion++;
			double v = Double.NEGATIVE_INFINITY;
			for (Move s : board.getActions()) {
				v = Math.max(v, minValue(board.result(s), depth - 1));
			}
			return v;	
		}
	}
	
	public double minValue(Board board, int depth){
		if (board.isTerminal(depth)) {
			return board.evaluateFor(board.getPlayer());
		} else {
			_recurCount++;
			_totalRecursion++;
			double v = Double.POSITIVE_INFINITY;
			for (Move s : board.getActions()) {
				v = Math.min(v, maxValue(board.result(s), depth - 1));
			}
			return v;	
		}
	}
	
	
	public int getFinalRecursion() {
		return _totalRecursion;
	}
}
