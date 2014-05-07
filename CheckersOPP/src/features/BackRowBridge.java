package features;

import utility.Board;
import utility.Move;

public class BackRowBridge implements Feature {

        @Override
        public int getValue(Board b, Move[] nextmoves, boolean color, int mobility, int deny) {
                
                final int[] toprow = {1, 3};
                final int[] botrow = {30, 32};
                
                int[] row;
                int kings;
                int turnpiece;
                int value = 0;
                
                if(color){
                        row = toprow;
                        kings = b.getWKingCount();
                        turnpiece  = 1;
                }
                else{
                        row = botrow;
                        kings = b.getBKingCount();
                        turnpiece = 2;
                }
                
                if(b.getValue(row[0]) == turnpiece && b.getValue(row[1]) == turnpiece && kings == 0){
                        value = 1;
                }
                
                return value;
        }

}