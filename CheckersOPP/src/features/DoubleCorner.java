package features;

import utility.Board;
import utility.Move;

public class DoubleCorner implements Feature {

        @Override
        public int getValue(Board b, Move[] nextmoves, boolean color, int mobility, int deny) {

                int king = 3;
                if(color){
                        king = 2;
                }
                
                if(((b.getValue(35) + b.getValue(31)) == king) ||
                                ((b.getValue(5) + b.getValue(1)) == king)){
                        return 1;
                }
                
                return 0;
        }

}