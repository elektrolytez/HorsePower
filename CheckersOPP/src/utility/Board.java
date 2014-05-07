package utility;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;




public class Board {

        /**
         * Array storing all positions.
         */
        private int[] _board;

        private Vector<Integer> _blackcheckers;
        private Vector<Integer> _blackkings;
        private Vector<Integer> _whitecheckers;
        private Vector<Integer> _whitekings;
        
        
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
         * Odds are black. Evens are white. Neither is negative under modulo 2.
         */
        public Board(){
                _blackcheckers = new Vector<Integer>();
                _blackkings = new Vector<Integer>();
                _whitecheckers = new Vector<Integer>();
                _whitekings = new Vector<Integer>();
                
                _board = new int[36];
                for(int i = 35; i > 27; i--){
                        _board[i] = 1;
                        _blackcheckers.add(i);
                }
                for(int i = 26; i > 22; i--){
                        _board[i] = 1;
                        _blackcheckers.add(i);
                }
                
                for(int i = 22; i > 18; i--){
                        _board[i] = -1;
                }
                for(int i = 17; i > 13; i--){
                        _board[i] = -1;
                }
                
                for(int i = 13; i > 9; i--){
                        _board[i] = 2;
                        _whitecheckers.add(i);
                }
                for(int i = 8; i > 0; i--){
                        _board[i] = 2;
                        _whitecheckers.add(i);
                }
                
                _board[0] = -3;
                _board[9] = -3;
                _board[18] = -3;
                _board[27] = -3;
        }
        
        
        /**
         * Creates a preset board given a byte array;
         * @param board An integer array of size 36 containing the positions of
         * each piece who's values should have a range of [1, 4] for checker
         * pieces, -1 for open spaces, and -3 for unusable spaces.
         */
        public Board(int[] board){
                _board = board;
                _blackcheckers = new Vector<Integer>();
                _blackkings = new Vector<Integer>();
                _whitecheckers = new Vector<Integer>();
                _whitekings = new Vector<Integer>();
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
         * Get all the positions for every black checker on the board.
         * @return Returns a vector of all black checker locations.
         */
        public Vector<Integer> getBlackCheckers(){
                return _blackcheckers;
        }
        
        /**
         * Get all the positions for every white checker on the board.
         * @return Returns a vector of all white checker locations.
         */
        public Vector<Integer> getWhiteCheckers(){
                return _whitecheckers;
        }
        
        /**
         * Get all the positions for every black king on the board.
         * @return Returns a vector of all black king locations.
         */
        public Vector<Integer> getBlackKings(){
                return _blackkings;
        }
        
        /**
         * Get all the positions for every white king on the board.
         * @return Returns a vector of all white king locations.
         */
        public Vector<Integer> getWhiteKings(){
                return _whitekings;
        }
        
        /** 
         * Get all legal moves given a turn from the current board.
         * @param turn TRUE if Black, FALSE is White.
         * @return Returns an array of legal moves.
         */
        public Move[] getPossibleMoves(boolean turn){

                Vector<Integer> turncheckers;
                Vector<Integer> turnkings;
                int sign = 1;
                if(turn){
                        turncheckers = _blackcheckers;
                        turnkings = _blackkings;
                        sign = -1;
                }
                else{
                        turncheckers = _whitecheckers;
                        turnkings = _whitekings;
                }
                
                Move[] jumpmoves = getJumps(turnkings, turncheckers, sign);
                
                if(jumpmoves.length == 0){
                        Move[] nonjumps = getNonJumps(turnkings, turncheckers, sign);
                        return nonjumps;
                }
                else{
                        return jumpmoves;
                }
        }
        
        private Move[] getNonJumps(Vector<Integer> kings, Vector<Integer> checkers, int sign) {
                Vector<Move> allnonjumps = new Vector<Move>();
                for(Iterator<Integer> i = kings.iterator(); i.hasNext();){
                        int next = i.next();
                        
                        int move;
                        
                        move = next+4;
                        if(move < 36){
                                if(_board[move] == -1){
                                        allnonjumps.add(new Move(next, move));
                                }
                        }
                        
                        move = next-4;
                        if(move > 0){
                                if(_board[move] == -1){
                                        allnonjumps.add(new Move(next, move));
                                }
                        }
                        
                        move = next+5;
                        if(move < 36){
                                if(_board[move] == -1){
                                        allnonjumps.add(new Move(next, move));
                                }
                        }
                        
                        move = next-5;
                        if(move > 0){
                                if(_board[move] == -1){
                                        allnonjumps.add(new Move(next, move));
                                }
                        }
                }
                
                for(Iterator<Integer> i = checkers.iterator(); i.hasNext();){
                        int next = i.next();
                        int move;
                        
                        move = next+sign*4;
                        if(move > 0 && move < 36){
                                if(_board[move] == -1){
                                        allnonjumps.add(new Move(next, move));
                                }
                        }
                        
                        move = next+sign*5;
                        if(move > 0 && move < 36){
                                if(_board[move] == -1){
                                        allnonjumps.add(new Move(next, move));
                                }
                        }
                }
                
                return allnonjumps.toArray(new Move[allnonjumps.size()]);
        }


        /*
         * Gets all moves that jump; Beneficial because if there is a jumping move,
         * it MUST be taken.
         */
        private Move[] getJumps(Vector<Integer> kings, Vector<Integer> checkers, int sign){
                Iterator<Integer> kingiterator = kings.iterator();
                Iterator<Integer> checkeriterator = checkers.iterator();
                Vector<Move> alljumps = new Vector<Move>();
                int atest;
                if(sign == -1){
                        atest = 0; //Black jumps even pieces.
                }
                else{
                        atest = 1; //White jumps odd pieces.
                }
                
                while(kingiterator.hasNext()){
                        int cpos = kingiterator.next();
                        LinkedList<Integer> prevjumps = new LinkedList<Integer>();
                        prevjumps.add(cpos);
                        
                        LinkedList<Move> jumpscpos = kingJumps(prevjumps, new LinkedList<Integer>(), atest);
                        
                        Iterator<Move> jcposit = jumpscpos.iterator();
                        while(jcposit.hasNext()){
                                alljumps.add(jcposit.next());
                        }
                }
                
                while(checkeriterator.hasNext()){
                        int cpos = checkeriterator.next();
                
                        LinkedList<Integer> prevjumps = new LinkedList<Integer>();
                        prevjumps.add(cpos);
                        
                        LinkedList<Move> jumpscpos = checkerJumps(prevjumps, sign, atest);
                        
                        Iterator<Move> jcposit = jumpscpos.iterator();
                        while(jcposit.hasNext()){
                                alljumps.add(jcposit.next());
                        }
                        
                }
                
                return alljumps.toArray(new Move[alljumps.size()]);
        }
        
        
        
        private LinkedList<Integer> nextCheckerJumps(int position, int sign, int atest){
                LinkedList<Integer> nextjumps = new LinkedList<Integer>();
                for(int i = 0; i < 2; i++){
                        int adjacent = position+sign*(4+i);
                        if(adjacent < 0 || adjacent > 35){
                                adjacent = 0;
                        }
                        
                        if(_board[adjacent]%2 == atest){
                                int landposition = adjacent+sign*(4+i);
                                if(landposition < 36 && landposition > 0){
                                        if(_board[landposition] == -1){
                                                nextjumps.add(landposition);
                                        }
                                }
                        }
                }
                return nextjumps;
        }
        
        //Recursively searches for all jumping moves.
        private LinkedList<Move> checkerJumps(LinkedList<Integer> prevJumps, int sign, int atest){
                LinkedList<Move> jumps = new LinkedList<Move>();        
                
                LinkedList<Integer> nextjumps = nextCheckerJumps(prevJumps.getLast(), sign, atest);
                
                if(nextjumps.size() == 0){
                        if(prevJumps.size() > 1){
                                int[] newmove = new int[prevJumps.size()];
                                Iterator<Integer> previt = prevJumps.iterator();
                                int count = 0;
                                while(previt.hasNext()){
                                        newmove[count] = previt.next();
                                        count++;
                                }
                                jumps.add(new Move(newmove));
                        }
                        return jumps;
                }
                else{
                        Iterator<Integer> njit = nextjumps.iterator();
                        while(njit.hasNext()){
                                prevJumps.addLast(njit.next());
                                LinkedList<Move> moreJumps = checkerJumps(prevJumps, sign, atest);
                                Iterator<Move> mjit = moreJumps.iterator();
                                while(mjit.hasNext()){
                                        jumps.add(mjit.next());
                                }
                                prevJumps.removeLast();
                        }
                        return jumps;
                }
        }
        
                
        
        private LinkedList<Jump> nextKingJumps(int first, int position, LinkedList<Integer> jumped, int atest){
                LinkedList<Jump> kingjumps = new LinkedList<Jump>();

                int sign = -1;
                for(int i = 0; i < 4; i++){
                        if(i > 1){
                                sign = 1;
                        }
                        int adjacent = position+sign*(4+i%2);
                        if(adjacent < 0 || adjacent > 35){
                                adjacent = 0;
                        }
                        
                        if(_board[adjacent]%2 == atest){
                                if(!jumped.contains(adjacent)){
                                        int landposition = adjacent+sign*(4+i%2);
                                        if(landposition < 36 && landposition > 0){
                                                if(_board[landposition] == -1 || landposition == first){
                                                        kingjumps.add(new Jump(landposition, adjacent));
                                                }
                                        }
                                }
                        }
                }
                
                return kingjumps;
        }
        
        //Very similar to the checker jump method, only it accounts for four directions.
        private LinkedList<Move> kingJumps(LinkedList<Integer> prevJumps, LinkedList<Integer> jumped, int atest){
                LinkedList<Move> jumps = new LinkedList<Move>();
                LinkedList<Jump> nextjumps = nextKingJumps(prevJumps.getFirst(), prevJumps.getLast(), jumped, atest);
                
                if(nextjumps.size() == 0){
                        if(prevJumps.size() > 1){
                                int[] newmove = new int[prevJumps.size()];
                                Iterator<Integer> previt = prevJumps.iterator();
                                int count = 0;
                                while(previt.hasNext()){
                                        newmove[count] = previt.next();
                                        count++;
                                }
                                jumps.add(new Move(newmove));
                        }
                        return jumps;
                }
                else{
                        Iterator<Jump> njit = nextjumps.iterator();
                        while(njit.hasNext()){
                                Jump next = njit.next();
                                prevJumps.addLast(next.getLandPosition());
                                jumped.add(next.getJumpedPosition());
                                LinkedList<Move> moreJumps = kingJumps(prevJumps, jumped, atest);
                                Iterator<Move> mjit = moreJumps.iterator();
                                while(mjit.hasNext()){
                                        jumps.add(mjit.next());
                                }
                                jumped.removeLast();
                                prevJumps.removeLast();
                        }

                        return jumps;
                }
        }
        
        
        
        /**
         * Get a board after making a move.
         * @param m Move used to generate the new board.
         * @return Returns a new board after making the move.
         */
        public Board getNewBoard(Move m){
                int[] positions = m.getPositions();
                int psize = positions.length-1;
                int last = positions[psize];
                int[] newboard = new int[36];           
                for(int i = 0; i < 36; i++){
                        newboard[i] = _board[i];
                }
                
                int startpos = positions[0];
                int start = newboard[startpos];
                newboard[startpos] = -1;

                //Determine whether or not to king
                if(start%2 == 1){
                        if(last < 5){
                                start = 3;
                        }
                }
                else {
                        if(last > 31){
                                start = 4;
                        }
                }
                
                newboard[last] = start;
                
                //Non-jumping move.
                if(m.jumpCount() == 0){
                        return new Board(newboard);
                }
                else{
                        //calculate jumps;
                        int lastpos = positions[0];
                        for(int i = 1; i <= psize; i++){
                                int curpos = positions[i];
                                newboard[curpos+((lastpos - curpos)/2)] = -1;
                                lastpos = curpos;
                        }
                        return new Board(newboard);
                }
        }
        
        /**
         * Returns what piece is located at the specified index.
         * @param index Index value within the range (0,35).
         * @return
         */
        public int getValue(int index){
                return _board[index];
        }
        
        /**
         * Get the number of black kings on this board.
         * @return An integer representing the number of black kings.
         */
        public int getBKingCount(){
                return _blackkings.size();
        }
        
        /**
         * Get the number of white kings on this board.
         * @return An integer representing the number of white kings.
         */
        public int getWKingCount(){
                return _whitekings.size();
        }

        /**
         * Get the number of black checkers on this board.
         * @return An integer representing the number of black checkers.
         */
        public int getBCheckerCount(){
                return _blackcheckers.size();
        }
        
        /**
         * Get the number of white checkers on this board.
         * @return An integer representing the number of white checkers.
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
                

                
                for(int i = 35; i > 0; i--){
                        int boardlabel = _board[i];
                        
                        if(boardlabel < 0){
                                boardlabel = 0;
                        }
                        
                        if(i%9 == 0){
                                i--;
                        }
                        
                        if(row%2 == 0){
                                s = s+"| |"+Conversion.CONVERT[boardlabel];
                                count++;
                        }
                        else {
                                s = s+"|"+Conversion.CONVERT[boardlabel]+"| ";
                                count++;
                        }
                        
                        if(count == 4){
                                s = s+"|\n";
                                row++;
                                count = 0;
                        }
                }
                return s;
        }
        
}