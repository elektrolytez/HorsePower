package features;

import java.util.Vector;

import utility.Board;
import utility.Move;

public class Threat implements Feature {

        @Override
        public int getValue(Board b, Move[] nextmoves, boolean color, int mobility, int deny) {

                int size = nextmoves.length;
                Vector<Integer> squares = new Vector<Integer>();
                
                int sign = 1;
                int opp = 1;
                
                if(color){
                        sign = -1;
                        opp = 2;
                }
                
                if(size > 0){
                        if(nextmoves[size-1].jumpCount() == 0){
                                for(int i = 0; i < size; i++){
                                        int endpos = nextmoves[i].getPositions()[1];
                                        
                                        if(!squares.contains(endpos)){
                                                if(checkPosition(endpos+sign*8, opp, b) ||
                                                                checkPosition(endpos+sign*9, opp, b) ||
                                                                checkPosition(endpos+sign*10, opp, b))
                                                squares.add(endpos);
                                        }
                                        
                                }
                        }
                }
                
                return squares.size();
        }
        
        private boolean checkPosition(int position, int opp, Board b){
                if(position < 36 && position > 0){
                        if(b.getValue(position) == opp){
                                return true;
                        }
                }
                return false;
        }

}