package ui;

import java.util.concurrent.Semaphore;

public class GameTimer implements Runnable{
        private int _time = 0;
        private Semaphore _sem;
        private CheckerFrame _frame;
        private boolean _enabled = false;
        
        public GameTimer(CheckerFrame frame){
                _sem = new Semaphore(1);
                _frame = frame;
        }
        
        @Override
        public void run() {
                while(true){                    
                        try {
                                if(_enabled){
                                        Thread.sleep(1000);
                                        _sem.acquire();
                                        _frame.setTime(_time--);
                                        _sem.release();
                                }
                        } catch (InterruptedException e) {
                                e.printStackTrace();
                        }
                }
        }
        
        public void setTime(int t){
                try {
                        _sem.acquire();
                        _time = t;
                        _frame.setTime(_time);
                        _sem.release();
                } catch (InterruptedException e) {
                        e.printStackTrace();
                }
        }
        
        public void setEnabled(boolean b){
                try {
                        _sem.acquire();
                        _enabled = b;
                        _sem.release();
                } catch (InterruptedException e) {
                        e.printStackTrace();
                }
        }
}