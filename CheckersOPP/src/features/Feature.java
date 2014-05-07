package features;

import utility.Board;
import utility.Move;

public interface Feature {

        public int getValue(Board b, Move[] nextmoves, boolean color, int mobility, int deny);
}