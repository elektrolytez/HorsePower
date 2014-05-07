package ai;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JFrame;

import ui.CheckerBoard;
import utility.Board;
import utility.Move;

public class AIProgram {
        private Board _board;
        private boolean _color;
        private int _time;
        private Evaluation _eval;
        private CheckerBoard _uiboard;
        private AIModule _aim;
        private String _file;
        private boolean _learning;
        private JFrame _frame;

        
        public AIProgram(String file, boolean learn){
                _board = new Board();
                _file = file;
                
                FileInputStream fis;
                try {
                        fis = new FileInputStream(_file);
                        try {
                                ObjectInputStream ois = new ObjectInputStream(fis);
                                _aim = (AIModule) ois.readObject();
                                ois.close();
                                fis.close();
                        } catch (IOException e) {
                                e.printStackTrace();
                                System.exit(1);
                        } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                                System.exit(1);
                        } 
                        
                }
                catch (FileNotFoundException e) {
                        _aim = new AIModule();
                }
                
                
                _learning = learn;
                
                _eval = new Evaluation(_aim.getSTDCoefficients());
                
                _frame = new JFrame("Checkers AI");
                _frame.setSize(500,500);
                _frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                _uiboard = new CheckerBoard();
                _frame.getContentPane().add(_uiboard);
                _frame.setVisible(true);
                        
        }
        
        public void setTime(int t){
                _time = t;
        }
        
        public void setBoard(Move m){
                _board = _board.getNewBoard(m);
        }
        
        public void setColor(boolean c){
                _color = c;
        }
        
        public Move getMove(){
                Move m = alphaBetaSearch(_board);
                _board = _board.getNewBoard(m);
                return m;
        }
        
        public void setUI(){
                _uiboard.update(_board);
                _uiboard.repaint();
        }
        
        public void setResult(boolean r){
                //TODO get result, modify coefficients here
                if(_learning){
                        if(r){
                                _aim.confirm();
                        }
                        else{
                                _aim.learn();
                        }
                        
                        FileOutputStream fos;
                        try {
                                fos = new FileOutputStream(_file);
                                try {
                                        ObjectOutputStream oos = new ObjectOutputStream(fos);
                                        oos.writeObject(_aim);
                                        oos.close();
                                        fos.close();
                                } catch (IOException e) {
                                        e.printStackTrace();
                                }
                        } catch (FileNotFoundException e1) {
                                e1.printStackTrace();
                        }
                        _frame.dispose();
                }
        }
        
        public void setResult(Coefficient[] coeff, boolean r){
                if(_learning){
                        if(r){
                                _aim.confirm();
                        }
                        else{
                                _aim.learnFromSelf(coeff);
                        }
                        
                        FileOutputStream fos;
                        try {
                                fos = new FileOutputStream(_file);
                                try {
                                        ObjectOutputStream oos = new ObjectOutputStream(fos);
                                        oos.writeObject(_aim);
                                        oos.close();
                                        fos.close();
                                } catch (IOException e) {
                                        e.printStackTrace();
                                }
                        } catch (FileNotFoundException e1) {
                                e1.printStackTrace();
                        }
                        _frame.dispose();
                }
        }
        
        private Move alphaBetaSearch(Board board){
                Move[] moves = board.getPossibleMoves(_color);

                int max = Integer.MIN_VALUE;
                int a = Integer.MIN_VALUE;
                Move selectedMove = moves[0];
                for(int i = 0; i < moves.length; i++){
                        int eval = alphaBetaMin(board.getNewBoard(moves[i]), a, Integer.MAX_VALUE, 1, 9);
                        
                        if(eval > max){
                                max = eval;
                                selectedMove = moves[i];
                        }
                        
                        if(max > a){
                                a = max;
                        }
                }
                
                return selectedMove;
        }
        
        private int alphaBetaMax(Board board, int alpha, int beta, int ply, int maxply){
                Move[] moves = board.getPossibleMoves(_color);
                if(ply >= 12 || ply >= maxply || moves.length == 0){
                        if(moves.length == 0){
                                return Integer.MIN_VALUE;
                        }
                        return _eval.evaluate(board, _color);
                }
                else{
                        int a = alpha;
                        int max = Integer.MIN_VALUE;
                        
                        for(int i = 0; i < moves.length; i++){
                                
                                int eval = alphaBetaMin(board.getNewBoard(moves[i]), a, beta, ply+1, maxply);
                                
                                if(eval > max){
                                        max = eval;
                                }
                                
                                if(max >= beta){
                                        return max;
                                }
                                
                                if(max > a){
                                        a = max;
                                }
                        }
                        
                        return max;
                }
        }
        
        private int alphaBetaMin(Board board, int alpha, int beta, int ply, int maxply){
                Move[] moves = board.getPossibleMoves(!_color);
                
                if(ply >= 12 || ply >= maxply || moves.length == 0){
                        if(moves.length == 0){
                                return Integer.MAX_VALUE;
                        }
                        return -_eval.evaluate(board, !_color);
                }
                else{
                        int b = beta;
                        
                        int min = Integer.MAX_VALUE;
                        for(int i = 0; i < moves.length; i++){
                                
                                int eval = alphaBetaMax(board.getNewBoard(moves[i]), alpha, b, ply+1, maxply);
                                
                                if(eval < min){
                                        min = eval;
                                }
                                
                                if(min <= alpha){
                                        return min;
                                }
                                
                                if(min < b){
                                        b = min;
                                }
                        }
                        
                        return min;
                        
                }
        }
        
        public Coefficient[] getCoefficients(){
                return _aim.getSTDCoefficients();
        }
        
}