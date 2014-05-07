import networking.NetworkModule;
import networking.CheckersNetworking;
import utility.Board;
import utility.Move;


/**
 * Dummy Module for now that makes use of the engine.
 * @author Asa
 *
 */
public class CheckersUserModule implements NetworkModule{

        private final String _user = "15";
        private final String _password = "544075";
        private final String _opponent = "16";
        private CheckersNetworking _engine;
        private boolean _color;
        private Board _Board;
        
        public CheckersUserModule(){
                _engine = new CheckersNetworking(this);
                _engine.start();
        }
        
        public String getOpponent() {
                return _opponent;
        }

        public String getPassword() {
                return _password;
        }

        public String getUser() {
                return _user;
        }

        public Move nextMove(Move move, int time) {
                int[] m = {30, 2};
                return new Move(m);
        }

        public void notifyResult(boolean result) {
                // TODO Auto-generated method stub
                
        }

        public void setColor(String color) {
                if(color.equalsIgnoreCase("Black")){
                        _color = true;
                }
                else{
                        _color = false;
                }
        }

        @Override
        public Move nextMove(int time) {
                // TODO Auto-generated method stub
                int[] m = { 30, 2};
                return new Move(m);
        }

        @Override
        public void notifyMessageSent() {}
        

}