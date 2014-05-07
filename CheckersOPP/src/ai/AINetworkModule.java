package ai;

import networking.CheckersNetworking;
import networking.NetworkModule;
import utility.Move;

public class AINetworkModule implements NetworkModule{
        
        private static final String _OPPONENT = "16";
        private static final String _USERNAME = "15";
        private static final String _PASSWORD = "544075";
        private CheckersNetworking _network;
        private AIProgram _ai;
        
        
        
        public AINetworkModule(AIProgram ai){
                _network = new CheckersNetworking(this);
                _ai = ai;
                _network.start();
        }
        
        @Override
        public String getOpponent() {
                return _OPPONENT;
        }

        @Override
        public String getPassword() {
                return _PASSWORD;
        }

        @Override
        public String getUser() {
                return _USERNAME;
        }

        @Override
        public Move nextMove(Move move, int time) {
                _ai.setBoard(move);
                _ai.setUI();
                _ai.setTime(time);
                return _ai.getMove();
        }

        @Override
        public Move nextMove(int time) {
                _ai.setTime(time);
                return _ai.getMove();
        }

        @Override
        public void notifyResult(boolean result) {
                _ai.setResult(result);
        }

        @Override
        public void setColor(String color) {
                        _ai.setColor(color.equals("Black"));
        }

        @Override
        public void notifyMessageSent() {
                _ai.setUI();
        }
        
}