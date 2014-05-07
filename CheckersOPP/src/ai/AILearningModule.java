package ai;

import utility.Board;
import utility.Move;

public class AILearningModule {

        private AIProgram _ai1, _ai2;
        
        public AILearningModule(boolean cycle){
                _ai1 = new AIProgram("alpha.ai", true);
                _ai2 = new AIProgram("beta.ai", true);
                _ai1.setColor(cycle);
                _ai2.setColor(!cycle);
                
                AIProgram first = _ai2;
                AIProgram second = _ai1;
                
                if(cycle){
                        first = _ai1;
                        second = _ai2;
                }

                
                Board board = new Board();
                
                boolean turn = true;
                String result = "undecided";
                String airesult = "undecided";
                int piececount = 0;
                int turncount = 0;
                
                while(board.getPossibleMoves(turn).length > 0){
                        
                        int cpiececount = board.getBCheckerCount()+board.getBKingCount()+board.getBCheckerCount()+board.getBKingCount();
                        
                        if(piececount == cpiececount){
                                turncount++;
                        }
                        else{
                                piececount = cpiececount;
                                turncount = 0;
                        }
                        
                        if(turncount == 50){
                                result = "Draw!";
                                break;
                        }
                        
                        Move nextmove;
                        if(turn){
                                nextmove = first.getMove();
                                board = board.getNewBoard(nextmove);
                                second.setBoard(nextmove);
                                turn = !turn;
                        }
                        else{
                                nextmove = second.getMove();
                                board = board.getNewBoard(nextmove);
                                first.setBoard(nextmove);
                                turn = !turn;
                        }
                        first.setUI();
                        second.setUI();
                }
                
                if(result.equals("undecided")){
                        if(turn){
                                result = "White wins";
                                
                                first.setResult(second.getCoefficients(), false);
                                second.setResult(true);
                                if(cycle){
                                        airesult = "Beta Wins.";
                                }
                                else{
                                        airesult = "Alpha Wins.";
                                }
                        }
                        else{
                                result = "Black wins";
                                first.setResult(true);
                                second.setResult(second.getCoefficients(), false);
                                if(!cycle){
                                        airesult = "Beta Wins.";
                                }
                                else{
                                        airesult = "Alpha Wins.";
                                }
                        }
                }
                else{
                        first.setResult(false);
                        second.setResult(false);
                }

                
                System.out.println(result);
                System.out.println(airesult);
                
        }
        
}