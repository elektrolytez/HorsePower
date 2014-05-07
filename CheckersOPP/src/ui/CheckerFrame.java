package ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;


@SuppressWarnings("serial")
public class CheckerFrame extends JFrame{
        
        private CheckerBoard _checkerboard;
        private JList _movelist;
        private JButton _playmove;
        private JLabel _timelabel;
        private JLabel _movelabel;
        private CheckerUI _ui;
        
        public CheckerFrame(CheckerUI ui, CheckerBoard cb, JList list){         
                super("Checkers");
                _ui = ui;
                _checkerboard = cb;
                _movelist = list;
                
                setSize(725,575);
                setResizable(false);
                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                
                _checkerboard.setSize(500,500);
                _checkerboard.setPreferredSize(new Dimension(500,500));
                
                _movelist.addKeyListener(
                                new KeyListener(){

                                        @Override
                                        public void keyPressed(KeyEvent ke) {
                                                if(ke.getKeyCode() == KeyEvent.VK_ENTER && _playmove.isEnabled()){
                                                        _ui.makeMove();
                                                }
                                        }

                                        @Override
                                        public void keyReleased(KeyEvent arg0) {}

                                        @Override
                                        public void keyTyped(KeyEvent arg0) {}
                                        
                                }
                );
                
                _playmove = new JButton("Play Move");
                _playmove.setEnabled(false);
                _playmove.addActionListener(
                                new ActionListener(){
                                        @Override
                                        public void actionPerformed(ActionEvent ae) {
                                                _ui.makeMove();
                                        }
                                }
                );
                
                _timelabel = new JLabel("Time: "+0);
                _timelabel.setFont(new Font(null, Font.BOLD, 20));
                
                _movelabel = new JLabel("Moves:");
                
                setMenu();
                setLayout();
                
                setVisible(true);
        }
        
        private void setMenu(){
                JMenuBar menu = new JMenuBar();
                JMenu game = new JMenu("Game");
                
                JMenuItem newgame = new JMenuItem("New Game");
                newgame.addActionListener(
                                new ActionListener(){
                                        @Override
                                        public void actionPerformed(ActionEvent ae) {
                                                startGame();
                                        }
                                }
                );
                
                
                JMenuItem exit = new JMenuItem("Exit");
                exit.addActionListener(
                                new ActionListener(){
                                        @Override
                                        public void actionPerformed(ActionEvent arg0) {
                                                System.exit(0);
                                        }
                                }
                );
                
                game.add(newgame);
                game.addSeparator();
                game.add(exit);
                
                menu.add(game);
                setJMenuBar(menu);
        }
        
        private void setLayout(){
                JScrollPane listpane = new JScrollPane(_movelist);

                JPanel contents = new JPanel();
                GroupLayout layout = new GroupLayout(contents);
                contents.setLayout(layout);
                
                layout.setAutoCreateGaps(true);
                layout.setAutoCreateContainerGaps(true);
                
                layout.setHorizontalGroup(layout.createSequentialGroup()
                                .addComponent(_checkerboard, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createParallelGroup()
                                                .addComponent(_timelabel)
                                                .addComponent(_movelabel)
                                                .addComponent(listpane)
                                                .addComponent(_playmove)
                                )
                );
                
                layout.setVerticalGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup()
                                        .addComponent(_checkerboard, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createSequentialGroup()
                                                        .addComponent(_timelabel)
                                                        .addComponent(_movelabel)
                                                        .addComponent(listpane)
                                                        .addComponent(_playmove)
                                        )
                                )
                );
                
                getContentPane().add(contents);
        }

        public void enableMove(boolean b) {
                _playmove.setEnabled(b);
        }

        public void setTime(int t) {
                _timelabel.setText("Time: "+t);
        }
        
        private void startGame(){
                String username = (String)JOptionPane.showInputDialog(this,
                                "Username:",
                                "Username",
                                JOptionPane.QUESTION_MESSAGE);
                String password = (String)JOptionPane.showInputDialog(this,
                                "Password:",
                                "Password",
                                JOptionPane.QUESTION_MESSAGE);
                String opponent = (String)JOptionPane.showInputDialog(this,
                                "Opponent:",
                                "Opponent",
                                JOptionPane.QUESTION_MESSAGE);
                if(username != null && password != null && opponent != null){
                        _ui.startGame(username, password, opponent);
                }
        }
        
}