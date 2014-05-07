package ui;

import networking.NetworkModule;
import networking.CheckersNetworking;
import utility.Move;

/**
 * UI  Module that connects with network.
 * @author Asa
 *
 */
public class UINetworkModule implements NetworkModule, Runnable{

        private String _user;
        private String _password;
        private String _opponent;
        private CheckersNetworking _network;
        private boolean _color;
        private CheckerUI _ui;
        private boolean _wait;
        
        public UINetworkModule(CheckerUI ui){
                _ui = ui;
                _network = new CheckersNetworking(this);
        }
        
        public void Start(){
                if(_user != null && _password != null && _opponent != null){
                        new Thread(this).start();
                }
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
                _ui.setBoard(move);
                _ui.setTime(time);
                _ui.getNewMove();
                blockAndGetMove();
                return _ui.getMove();
        }

        public void notifyResult(boolean result) {}

        public void setColor(String color){
                if(color.equalsIgnoreCase("Black")){
                        _color = true;
                }
                else{
                        _color = false;
                }
                _ui.setColor(_color);
        }
        
        /**
         * Set the user name.
         * @param user
         */
        public void setUser(String user){
                _user = user;
        }
        
        /**
         * Set the password.
         * @param password
         */
        public void setPassword(String password){
                _password = password;
        }
        
        /**
         * Set the opponent.
         * @param opponent
         */
        public void setOpponent(String opponent){
                _opponent = opponent;
        }

        @Override
        public Move nextMove(int time) {
                _ui.setTime(time);
                _ui.getNewMove();
                blockAndGetMove();
                return _ui.getMove();
        }
        
        private synchronized void blockAndGetMove(){
                _wait = true;
                while(_wait){
                        try {
                                wait();
                        } catch (InterruptedException e) {}
                }
        }

        public synchronized void stopWaiting(){
                _wait = false;
                notify();
        }
        
        @Override
        public void run() {
                _network.start();
        }

        @Override
        public void notifyMessageSent() {}

}