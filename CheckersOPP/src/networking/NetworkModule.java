package networking;
import utility.Move;

/**
 * An interface used for the engine such that many different checkers programs
 * can be built very easily without needing to worry about the server message
 * structure.
 * 
 * @author Asa
 *
 */
public interface NetworkModule {
        /**
         * Designates the color to the module.
         * @param color The color assigned by the server.
         */
        public void setColor(String color);
        
        /**
         * Notifies the module that the game has ended and provides the outcome.
         * @param result True if the result is a win, false if the result is a loss.
         */
        public void notifyResult(boolean result);
        
        /**Notifies the module that the message has been sent.  Use this time
         * to update the User Interface or do any upkeep while the opponent makes
         * his or her move.
         */
        public void notifyMessageSent();
        
        /**
         * Called when a move needs to be made by the module.
         * @param move  The last move made by the opponent.
         * @param time The amount of time left.
         * @return Returns a move to send to the server.
         */
        public Move nextMove(Move move, int time);
        
        /**
         * First move requester.  Removes computations checking
         * for null pointers.
         * @param time Time left.
         * @return Returns the next move.
         */
        public Move nextMove(int time);

        /**
         * The modules will store the user name to log into the server.
         * @return Returns the user name.
         */
        public String getUser();
        
        /**
         * The modules will store the password to log into the server.
         * @return Returns the password.
         */
        public String getPassword();
        
        /**
         * The modules will store which opponents they will play.
         * @return Returns the opponent.
         */
        public String getOpponent();
        
}