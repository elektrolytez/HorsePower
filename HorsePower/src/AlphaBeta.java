import java.util.List;

/**
 * 
 * This class takes a board
 * Uses minimax algorithm with alpha beta pruning
 * 
 * References:
 * https://github.com/shymek/Ai-Chess/blob/master/src/ai/AlphaBeta.java <-- similiar but with no ply
 * http://stackoverflow.com/questions/15447580/java-minimax-alpha-beta-pruning-recursion-return
 * http://codereview.stackexchange.com/questions/27113/minimax-alpha-beta-code-for-java
 * http://en.wikipedia.org/wiki/Alpha%E2%80%93beta_pruning
 * @author kenzl_000
 *
 */
public class AlphaBeta {
	
	private Board _board;
	private boolean _color; //player
	private Evaluation _eval;
	
	public AlphaBeta(){
		_board = new Board();
		
		
		
	}
	
	
	private Move alphaBetaSearch(Board board){
        List<Move> moves = board.getPossibleMoves(_color); //get moves 

        int max = Integer.MIN_VALUE; //blah blah default max
        int min = Integer.MIN_VALUE; //default min
        Move selectedMove = moves.get(0);
        for(int i = 0; i < moves.size(); i++){
                int eval = alphaBetaMin(board.result(moves.get(i)), min, Integer.MAX_VALUE, 1, 10);
                
                if(eval > max){
                        max = eval;
                        selectedMove = moves.get(i);
                }
                
                if(max > min){
                        min = max;
                }
        }
        
        
        
        return selectedMove;
}
	/**
	 * 
	 * 
	 * @param board - takes a board
	 * @param alpha - define an alpha
	 * @param beta - define a beta
	 * @param ply - define the ply
	 * @param maxply - set maximum ply
	 * @return
	 */
	private int alphaBetaMax(Board board, int alpha, int beta, int ply, int maxply){
        List<Move> moves = board.getPossibleMoves(_color); //get moves
        
        if(ply > 15 || ply > maxply || moves.size() == 0){ //if the ply is greater than 15/maxPly or no moves
                return _eval.eval(board, _color); //return the evaluation 
        }
        else{
                int a = alpha;
                
                int newply = maxply;
                if(moves.get(0).jumpCount() == 0){
                        newply++; //next no jumps available (i.e don't search)
                }
                
                int max = Integer.MIN_VALUE;
                for(int i = 0; i < moves.size(); i++){
                        int eval = alphaBetaMin(board.result(moves.get(i)), a, beta, ply+1, newply); //recursively iterate through
                        
                        if(eval > max){
                                max = eval;
                        }
                        
                        if(max >= beta){
                                return max;
                        }
                        
                        if(max > a){
                                a = max;
                        }
                }
                
                return max;
        }
}

private int alphaBetaMin(Board board, int alpha, int beta, int ply, int maxply){
        List<Move> moves = board.getPossibleMoves(!_color);
        
        if(ply > 8 || ply > maxply || moves.size() == 0){
                return -_eval.eval(board, !_color);
        }
        else{
                int b = beta;
                
                int newply = maxply;
                if(moves.get(0).jumpCount() == 0){
                        newply++;
                }
                
                int min = Integer.MAX_VALUE;
                for(int i = 0; i < moves.size(); i++){
                        int eval = alphaBetaMax(board.result(moves.get(i)), alpha, b, ply+1, newply);
                        
                        if(eval < min){
                                min = eval;
                        }
                        
                        if(min <= alpha){
                                return min;
                        }
                        
                        if(min < b){
                                b = min;
                        }
                }
                
                return min;
                
        }
}

}
