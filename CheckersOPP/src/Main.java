import utility.Board;
import utility.Move;


public class Main {

        public static void main(String args[]){
                //CheckersUserModule b = new CheckersUserModule();
                //Testing conversions, seem to work correctly!
                int[] board = new int[36];
                for(int i = 0; i < 36; i++){
                        board[i] = -1;
                }
                board[25] = 1;
                board[30] = 2;
                board[21] = 2;
                board[12] = 4;
                board[11] = 1;
                board[20] = 2;
                board[29] = 2;
                board[28] = 4;
                board[19] = 2;
                board[10] = 2;
                
                Board b = new Board(board);
                //Move m = new Move("(1:1):(2:4):(3:5)");
                //System.out.println(m.getPositions()[0]+":"+m.getPositions()[1]+":"+m.getPositions()[2]);
                
                Move[] ms = b.getPossibleMoves(true);
                
                for(int i = 0; i < ms.length; i++){
                        /*int[] temp = ms[i].getPositions();
                        for(int j = 0; j < temp.length; j++){
                                System.out.print(temp[j]+":");
                        }
                        */
                        //System.out.print("\n");
                        System.out.println(ms[i].getMessage());
                        System.out.println(b.getNewBoard(ms[i]));

                }
                System.out.println(b);



        }
        
}