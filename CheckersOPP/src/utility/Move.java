package utility;

public class Move {

        private int[] _positions; //Allows for multiple jumps.
        
        public Move(int[] positions){
                _positions = positions;
        }
        
        public Move(int s, int e){
                _positions = new int[2];
                _positions[0] = s;
                _positions[1] = e;
        }
        
        /**
         * Takes in moves in string format provided by the server,
         * converts the moves into Board format, and saves the moves
         * @param moves A string representing all the moves.
         */
        public Move(String moves) {
                
                char[] movechars = moves.toCharArray();

                int endlength = ((moves.length()-5)/6);
                int[] positions = new int[(endlength+1)*2];
                positions[0] = Character.getNumericValue(movechars[1]);
                positions[1] = Character.getNumericValue(movechars[3]);
                
                for(int i = 1; i < endlength+1; i++){
                        int position = 5+(i-1)*6+2;
                        positions[i*2] = Character.getNumericValue(movechars[position]);
                        positions[i*2+1] = Character.getNumericValue(movechars[position+2]);
                }
                
                _positions = toBoardCoordinates(positions);
        }
        
        
        /**
         * Get all positions the checker will move in board format.
         * @return An array of moves in board format.
         */
        public int[] getPositions(){
                return _positions;
        }
        
        /**
         * Takes an array of coordinates for rows and columns and converts them
         * into single coordinates for use with the board.
         * @param move Array of integers in the format row column.
         * @return Array of coordinates in Board format with a size half the input array.
         */
        private int[] toBoardCoordinates(int[] move){
                int[] conv = new int[move.length/2];
                
                for(int i = 0; i < conv.length; i++){
                        int rowoffset = (move[i*2]+1)*4;
                        rowoffset = rowoffset+rowoffset/9;
                        int coloffset = (move[i*2+1]/2);
                        
                        conv[i] = rowoffset-coloffset;
                }
                return conv;
        }

        /**
         * Converts a coordinate in the internal board format into a format
         * using a column/row system.
         * @param coord Coordinate in the board format skipping 0, 9, 18, 27.
         * @return An integer array containing the value of the column and row.
         */
        public int[] toColRow(int coord){
                int[] colrowcoord = new int[2];
                int format = (coord-coord/9-1);
                colrowcoord[0] = format/4;
                colrowcoord[1] = (((colrowcoord[0]+1)*4)-format-1)*2+colrowcoord[0]%2;
                return colrowcoord;
        }
        
        /**
         * Creates a message to be passed to the server to register a move.
         * @return A string representation of the message to be sent.
         */
        public String getMessage(){

                String message;
                int[] start = toColRow(_positions[0]);
                message = "("+start[0]+":"+start[1]+")";
                
                for(int i = 1; i < _positions.length; i++){
                        int[] endpos = toColRow(_positions[i]);
                        message = message+":("+endpos[0]+":"+endpos[1]+")";
                }
                
                return message;
        }
        
        /**
         * Determines whether or not a move is a jump.
         * 0 Means that the move is not a jump
         * Anything higher means that the move is a jump and this is how
         * many jumps are made.
         * @return Returns an integer greater than or equal to 0 equal
         * to the number of jumps made.
         */
        public int jumpCount(){
                int size = _positions.length;
                if(size > 2){
                        return size-1;
                }
                else{
                        int d = (_positions[0] - _positions[1]);
                        if(d%8 == 0 || d%10 == 0){
                                return 1;
                        }
                        else{
                                return 0;
                        }
                }
        }
        
        public String toString(){
                return getMessage();
        }
        
        
}