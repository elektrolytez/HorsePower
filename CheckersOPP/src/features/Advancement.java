package features;

import java.util.Iterator;
import java.util.Vector;

import utility.Board;
import utility.Move;

public class Advancement implements Feature {

        @Override
        public int getValue(Board b, Move[] nextmoves, boolean color, int mobility, int deny) {
                
                final int[] toprow = {18, 27};
                final int[] botrow = {9, 18};
                
                int[] advrow;
                int[] backrow;
                int value = 0;
                Vector<Integer> checkers;
                
                if(color){
                        advrow = botrow;
                        backrow = toprow;
                        checkers = b.getBlackCheckers();

                }
                else{
                        advrow = toprow;
                        backrow = botrow;
                        checkers = b.getWhiteCheckers();
                }
                
                for(Iterator<Integer> it = checkers.iterator(); it.hasNext();){
                        int next = it.next();
                        
                        if(next > advrow[0] && next < advrow[1]){
                                value++;
                        }
                        else if(next > backrow[0] && next < backrow[1]){
                                value--;
                        }
                }
                
                
                return value;
        }

}