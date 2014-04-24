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
         * Get all positions the checker will move in board format.
         * @return An array of moves in board format.
         */
        public int[] getPositions(){
                return _positions;
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
         * Creates a string representation of the move
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
         * Anything higher means that the move is a jump
         * @return Returns an integer greater than or equal to 0, equal
         * to the number of jumps made.
         */
        public int jumpCount(){
                int size = _positions.length;
                if(size > 2){
                        return size-1;
                }
                else{
                        int pos = (_positions[0] - _positions[1]);
                        if(pos%8 == 0 || pos%10 == 0){
                                return 1;
                        }
                        else{
                                return 0;
                        }
                }
        }
        
}