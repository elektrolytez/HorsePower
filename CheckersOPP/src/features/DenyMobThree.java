package features;

import utility.Board;
import utility.Move;

public class DenyMobThree implements Feature{

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
                
                if(4*mobility >= 3*piececount){
                        a = true;
                }
                
                if(3*deny > mobility){
                        b = true;
                }
                
                if(!a && b){
                        value = 1;
                }
                
                return value;
        }
}