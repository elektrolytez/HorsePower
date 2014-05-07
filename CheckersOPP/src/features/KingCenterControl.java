package features;

import utility.Board;
import utility.Move;

public class KingCenterControl implements Feature{

        @Override
        public int getValue(Board b, Move[] moves, boolean color, int mobility, int deny) {
                final int[] positions = { 11, 12, 15, 16, 20, 21, 24, 25 };
                int king = 4;
                int value = 0;
                
                if(color){
                        king = 3;
                }
                
                for(int i = 0; i < positions.length; i++){
                        if(b.getValue(positions[i]) == king){
                                value++;
                        }
                }
                return value;
        }
}