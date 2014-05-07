package utility;

public class Jump {

        private int _land;
        private int _jumped;
        
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