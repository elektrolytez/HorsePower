package features;

import java.util.Iterator;
import java.util.Vector;

import utility.Board;

public class FeatureUtility {

        private Vector<Integer> _mobility;
        
        public Vector<Integer> getMobility(Board b, boolean color){
                Vector<Integer> checkers;
                Vector<Integer> kings;
                int sign;
                _mobility = new Vector<Integer>();
                
                if(color){
                        checkers = b.getBlackCheckers();
                        kings = b.getBlackKings();
                        sign = -1;
                }
                else{
                        checkers = b.getWhiteCheckers();
                        kings = b.getWhiteCheckers();
                        sign = 1;
                }
                
                for(Iterator<Integer> it = checkers.iterator(); it.hasNext();){
                        int next = it.next();
                        determineMob((next+sign*4), b);
                        determineMob((next+sign*5), b);                 
                }
                
                for(Iterator<Integer> it = kings.iterator(); it.hasNext();){
                        int next = it.next();
                        determineMob((next+4), b);
                        determineMob((next-4), b);
                        determineMob((next+5), b);
                        determineMob((next-5), b);
                }
                        
                return _mobility;
        }
        
        private void determineMob(int position, Board b){
                if(position < 36 && position > 0){
                        if(b.getValue(position) == -1){
                                if(!_mobility.contains(position)){
                                        _mobility.add(position);
                                }
                        }
                }
        }

        public int getDenialOfOccupancy(Board b, boolean color, Vector<Integer> mobility){
                
                int value = 0;
                int sign = 1;
                int opp;
                int oppking;
                
                if(color){
                        sign = -1;
                        opp = 0;
                        oppking = 4;
                }
                else{
                        opp = 1;
                        oppking = 3;
                }
                
                for(Iterator<Integer> it = mobility.iterator(); it.hasNext();){
                        int next = it.next();
                        
                        if(checkPosition((next+sign*4), opp, b)
                                        || checkPosition((next+sign*5), opp, b)
                                        || checkKingPosition((next+-1*sign*4), oppking, b)
                                        || checkKingPosition((next+-1*sign*5), oppking, b)){
                                value++;
                        }

                }
                
                return value;
        }
        
        private boolean checkPosition(int position, int opp, Board b){
                if(position < 36 && position > 0){
                        if(b.getValue(position)%2 == opp){
                                return true;
                        }
                }
                return false;
        }
        
        private boolean checkKingPosition(int position, int opp, Board b){
                if(position < 36 && position > 0){
                        if(b.getValue(position) == opp){
                                return true;
                        }
                }
                return false;
        }
        
}