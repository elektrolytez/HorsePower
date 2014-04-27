
public class Jump {
	

        private int _land;
        private int _jumped;
        /**
         * basic constructor to hold where each jump landed and what was jumped
         */
        public Jump(int land, int jumped){
                _land = land;
                _jumped = jumped;
        }
        
        public int getLandPosition(){
                return _land;
        }
        
        public int getJumpedPosition(){
                return _jumped;
        }
        
}
