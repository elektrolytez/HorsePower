package features;

import utility.Board;
import utility.Move;

public class UnMobCenterTwo implements Feature{

        @Override
        public int getValue(Board board, Move[] nextmoves, boolean color, int mobility, int deny) {
                boolean a = false;;
                boolean b = false;
                
                int value = 0;
                
                int piececount;
                if(color){
                        piececount = board.getBCheckerCount()+board.getBKingCount();
                }
                else{
                        piececount = board.getWCheckerCount()+board.getWKingCount();
                }
                
                if(4*mobility >= 3*piececount && mobility < 2*(mobility-deny)){
                        a = true;
                }
                
                CenterControl cc = new CenterControl();
                
                if(cc.getValue(board, nextmoves, color, mobility, deny) > 3){
                        b = true;
                }
                
                if(a && !b){
                        value = 1;
                }
                
                return value;
        }

}