package ui;
import utility.Board;

import java.awt.GridLayout;
import javax.swing.JPanel;


@SuppressWarnings("serial")
public class CheckerBoard extends JPanel{
        
        private CheckerSquare[] _squares;
        
        public CheckerBoard(){
        setLayout(new GridLayout(8,8));
                _squares = new CheckerSquare[36];
                
                Board b = new Board();
                int count = 0;
                int row = 0;
                for(int i = 35; i > 0; i--){
                        if(i%9 == 0){
                                i--;
                        }
                        
                        boolean rowcheck = (row%2 == 0);
                        
                        if(rowcheck && count == 0){
                                add(new CheckerSquare(false));
                                count++;
                        }
                        
                        if(rowcheck && count == 7){
                                _squares[i] = new CheckerSquare(true);
                                _squares[i].setOccupant(b.getValue(i));
                                add(_squares[i]);
                                count++;
                        }
                        else{
                                _squares[i] = new CheckerSquare(true);
                                _squares[i].setOccupant(b.getValue(i));
                                add(_squares[i]);
                                add(new CheckerSquare(false));
                                count += 2;
                        }
                        
                        if(count == 8){
                                row++;
                                count = 0;
                        }
                }
        }
        
        public void update(Board b){
                for(int i = 35; i > 0; i--){
                        if(i%9 == 0){
                                i--;
                        }
                        _squares[i].setOccupant(b.getValue(i));
                }
        }
        
}