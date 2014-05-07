package features;

import utility.Board;
import utility.Move;

public class Mobility implements Feature {

        @Override
        public int getValue(Board b, Move[] nextmoves, boolean color, int mobility, int deny) {
                return mobility;
        }

}