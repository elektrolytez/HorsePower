package ui;

import java.util.Observable;
import java.util.Observer;

import utility.Board;
import utility.Move;

public class CheckerUI implements Observer{

        private CheckerFrame _frame;
        private UINetworkModule _module;
        private CheckerBoard _boardui;
        private Board _board;
        private MoveList _movelist;
        private GameTimer _gametimer;
        private boolean _turn;
        private Move _newmove;
        private Move _updatemove;

        
        public CheckerUI(){
                _boardui = new CheckerBoard();
                _movelist = new MoveList();
                _movelist.addObserver(this);
                _module = new UINetworkModule(this);
                _board = new Board();

                _frame = new CheckerFrame(this, _boardui, _movelist.getList());
                
                _gametimer = new GameTimer(_frame);
                new Thread(_gametimer).start();
        }
        
        public void setBoard(Move move){
                _board = _board.getNewBoard(move);
                _boardui.update(_board);
                _frame.getContentPane().repaint();
        }
        
        public void getNewMove(){
                _movelist.setMoves(_board.getPossibleMoves(_turn));
                _frame.getContentPane().repaint();
        }
        
        public void setTime(int time) {
                _gametimer.setTime(time);
                _gametimer.setEnabled(true);
        }
        
        @Override
        public void update(Observable o, Object arg) {
                _updatemove = _movelist.getCurrentMove();
                if(_updatemove == null){
                        _boardui.update(_board);
                        _frame.enableMove(false);
                }
                else{
                        _newmove = new Move(_updatemove.getPositions());
                        _boardui.update(_board.getNewBoard(_newmove));
                        _frame.enableMove(true);
                }
                _frame.getContentPane().repaint();
        }

        public void setColor(boolean c) {
                _turn = c;
                _boardui.update((_board = new Board()));
        }

        public synchronized void makeMove() {
                if(_newmove != null){
                        _board = _board.getNewBoard(_newmove);
                        _gametimer.setEnabled(false);
                        _frame.enableMove(false);
                        _module.stopWaiting();
                        _movelist.clear();
                }
        }

        public Move getMove(){
                return _newmove;
        }

        public void startGame(String username, String password, String opponent) {
                _module.setUser(username);
                _module.setPassword(password);
                _module.setOpponent(opponent);
                _module.Start();
        }

        
        

        
}