package features;

import utility.Board;
import utility.Move;

public class Deny implements Feature {

        @Override
        public int getValue(Board b, Move[] nextmoves, boolean color, int mobility, int deny) {
                return deny;
        }

}