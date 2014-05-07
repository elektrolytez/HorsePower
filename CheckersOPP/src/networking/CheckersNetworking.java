
package networking;
import java.io.IOException;

import utility.Move;


public class CheckersNetworking {


    private final Connect _connection;
    private NetworkModule _module;
    private String _color;
    private String _gameID;
    private String _opponentc;
    
        public CheckersNetworking(NetworkModule m){
                _connection = new Connect();
                _module = m;
        }
        
        public String getGameID(){
                return _gameID;
        }
        
        /**
         * This method will start the communication with the server, enter
         * the necessary information and then go into a loop where it will act
         * as a sentinel program continuously reading messages from the server
         * and signaling the module whenever a move needs to be made.
         * 
         * The loop exits when either the game ends or an error appears.
         */
        public void start(){
                String readMessage;
            boolean continueGame = true;
            Move opponentLastMove = null;
            Move lastMove;

                _connection.openSocket();
                try {
                        
                    readMessage = _connection.readAndEcho(); // start message
                    
                    if(!readMessage.substring(0,8).equals("SAM v1.0")){
                        _connection.close();
                        System.out.println("Log in error: Version");
                        System.exit(1);
                    }
                    
                    readMessage = _connection.readAndEcho(); // ID query
                    if(readMessage.substring(0,10).equals("?Username:")){
                        _connection.writeMessageAndEcho(_module.getUser()); // user ID
                    }
                    else{
                        _connection.close();
                        System.out.println("Log in error: Expected a username query.");
                        System.exit(1);
                    }
                    
                    readMessage = _connection.readAndEcho(); // password query 
                    if(readMessage.substring(0,10).equals("?Password:")){
                        _connection.writeMessage(_module.getPassword());  // password
                    }
                    else{
                        _connection.close();
                        System.out.println("Log in error: Expected a password query");
                        System.exit(1);
                    }
                    
                    readMessage = _connection.readAndEcho(); // opponent query
                    if(readMessage.substring(0,10).equals("?Opponent:")){
                        _connection.writeMessageAndEcho(_module.getOpponent());  // opponent
                    }
                    else {
                        _connection.close();
                        System.out.println("Log in error: Expected an opponent query.");
                        System.exit(1);
                    }
                    
                    
                    readMessage = _connection.readAndEcho();
                    if(readMessage.substring(0,5).equals("Game:")){
                        _gameID = readMessage.substring(5,9); // game 
                    }
                    else{
                        _connection.close();
                        System.out.println("Log in error: Expected Game ID.");
                        System.exit(1);
                    }
                    
                    readMessage = _connection.readAndEcho();
                    if(readMessage.substring(0,6).equals("Color:")){
                        _color = readMessage.substring(6,11);  // color
                        _module.setColor(_color);
                        if(_color.equals("White")){
                                _opponentc = "Black";
                        }
                        else{
                                _opponentc = "White";
                        }
                    }
                    else{
                        _connection.close();
                        System.out.println("Log in error: Expected color of player.");
                        System.exit(1);
                    }
                    
                    if(_color.equals("Black")){
                            readMessage = _connection.readAndEcho();
                            lastMove = _module.nextMove(Integer.valueOf(readMessage.substring(6, readMessage.lastIndexOf(')'))));
                        _connection.writeMessageAndEcho(lastMove.getMessage());
                    }
                    
                    while(continueGame){
                            readMessage = _connection.readAndEcho();
                            if(readMessage.substring(0,5).equals("?Move")){
                                lastMove = _module.nextMove(opponentLastMove, Integer.valueOf(readMessage.substring(6, readMessage.lastIndexOf(')'))));
                                _connection.writeMessageAndEcho(lastMove.getMessage());
                                _module.notifyMessageSent();
                            }
                            else if(readMessage.substring(0,11).equals(("Move:"+_opponentc+":"))){
                                opponentLastMove = new Move(readMessage.substring(11, readMessage.lastIndexOf(')')+1));
                            }
                            else if(readMessage.substring(0,7).equals("Result:")){
                                _module.notifyResult(readMessage.substring(7, 12).equals(_color));
                                continueGame = false;
                            }
                            else if(readMessage.substring(0,6).equals("Error:")){
                                continueGame = false;
                            }
                    }
                    
                    _connection.close();

                } catch (IOException e) {
                        e.printStackTrace();
                        System.exit(1);
                }
        }
}