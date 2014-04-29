package horsepower_search;

import horsepower_checkers.*;

public class AlphaBeta {

	private int _depth = 0;
	private int _recurCount;
	private int _totalRecursion = 0;
	private EvalCheckers _eval;

	public AlphaBeta() {
	}

	/**
	 * Very similar to minimax just with more variables. I changed NEGATIVE
	 * INFINITY to MIN_VALUE etc. Only thing greater than MIN_VALUE is NEG INF,
	 * where as nothing is greater than NEG INF. A case may arise where we want
	 * to set the evaluation value to NEG INF indicating best possible move?
	 * 
	 * 
	 * @param board
	 *            takes a board
	 * @return best Move
	 */
	public Move alphaBeta(Board board) {
		double vBest = Double.MIN_VALUE;
		double alpha = Double.MIN_VALUE;
		double v;
		Move bestAction = null;

		int tempCount = 0;
		for (Move a : board.getActions()) {

			_recurCount = 0;
			v = alphaBetaMin(board.result(a), alpha, Double.POSITIVE_INFINITY,
					1, 9);
			if (v > vBest) {
				vBest = v;
				bestAction = a;
			}
			tempCount = tempCount + _recurCount;

			if (vBest > alpha)
				alpha = vBest;
		}
		System.out.println("Total recursive calls : " + tempCount);
		return bestAction;
	}

	public double alphaBetaMax(Board board, double alpha, double beta, int ply,int maxply) {
		/*
		 * if (board.isTerminal(_depth)) { return
		 * board.evaluateFor(board.getPlayer()); } else { _depth--;
		 * _recurCount++; _totalRecursion++; double v =
		 * Double.NEGATIVE_INFINITY; for (Move s : board.getActions()) { v =
		 * Math.max(v, minValue(board.result(s))); } //decrement depth by 1
		 * return v;
		 * 
		 * 
		 * }
		 */
		// 12 is a upper limit
		if (ply >= 12 || ply >= maxply || board.getActions().size() == 0) {
			if (board.getActions().size() == 0)
				return Double.MIN_VALUE;

			return _eval.evaluate(board, board.getPlayer());
		} else {
			double a = alpha;
			double max = Double.MIN_VALUE;
			for (Move m : board.getActions()) {
				// pass it a new board that has the following result move,
				// increase ply by 1
				double eval = alphaBetaMin(board.result(m), a, beta, ply + 1,
						maxply);

				if (eval > max)
					max = eval;

				if (max >= beta)
					return max;
				if (max > a)
					a = max;

			}// end for
			
			return max;

		}//end else

	}

	public double alphaBetaMin(Board board, double alpha, double beta, int ply, int maxply) {
		/*if (board.isTerminal(_depth)) {
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
		}*/
		
		//possible issue with board.getActions().size resulting in lowered performance
		if (ply >= 12 || ply >= maxply || board.getActions().size() == 0) {
			if (board.getActions().size() == 0)
				return Double.MAX_VALUE;

			return _eval.evaluate(board, board.getPlayer());
		} else {
			double b = beta;
			double min = Double.MAX_VALUE;
			for (Move m : board.getActions()) {
				// pass it a new board that has the following result move,
				// increase ply by 1
				double eval = alphaBetaMax(board.result(m), alpha, b, ply + 1,
						maxply);

				if (eval < min)
					min = eval;

				if (min <= alpha)
					return min;
				if (min > b)
					b = min;

			}// end for
			
			return min;

		}//end else
	}

	public int getFinalRecursion() {
		return _totalRecursion;
	}
}