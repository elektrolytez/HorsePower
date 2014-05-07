package ui;
import utility.Move;

import java.util.Observable;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class MoveList extends Observable{
        
        private JList _list;
        private DefaultListModel _model;        
        
        /**
         * Creates a new MoveList displaying all moves the user can use.
         */
        public MoveList(){
                _model = new DefaultListModel();
                _list = new JList(_model);
                _list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                setSL();
        }

        
        /**
         * Sets all possible legal moves to use in order to select
         * and play this move within the list.
         * @param Moves An array of legal moves to use.
         */
        public void setMoves(Move[] Moves){
                _model.clear();
                for(int i = 0; i < Moves.length; i++){
                        _model.addElement(Moves[i]);
                }
        }
        
        /**
         * Clears the list of its contents.
         */
        public void clear(){
                _model.clear();
        }
        
        public JList getList(){
                return _list;
        }
        
        /**
         * Get the move that is currently selected.
         * @return Returns a Move.
         */
        public Move getCurrentMove(){
                return (Move)_list.getSelectedValue();
        }
        
        private void setSL(){
                _list.addListSelectionListener(
                                new ListSelectionListener(){
                                        @Override
                                        public void valueChanged(ListSelectionEvent lse) {
                                                selectionChange();
                                                notifyObservers();
                                        }
                                }
                );
        }
        
        private void selectionChange(){
                setChanged();
        }
        

}