package ai;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Random;
import java.util.Stack;
import java.util.Vector;

/**
 * This class is used to store all evaluation variables that will change as the
 * program learns to play checkers.  In a sense, this is just making use of Java's
 * Serialization in order to make saving much easier. 
 * @author Asa
 *
 */
public class AIModule implements Serializable{

        /**
         * Version 1.0
         */
        private static final long serialVersionUID = 1L;
        
        /**
         * Controls how far deep to search the tree.
         */
        private Coefficient[] _stdcoefficients;
        private Vector<Coefficient[]> _oldcoefficients;
        private int _lossesinrow = 0;
        private int _wincount = 0;
        
        public AIModule(){
                _oldcoefficients = new Stack<Coefficient[]>();
                _stdcoefficients = new Coefficient[21];
                for(int i = 0; i < 21; i++){
                        _stdcoefficients[i] = new Coefficient(1, 0);
                }
        }
        
        public Coefficient[] getSTDCoefficients(){
                return _stdcoefficients;
        }
        
        public void learnFromSelf(Coefficient[] oppcoeff){
                _oldcoefficients.add(0, oppcoeff);

                if(_oldcoefficients.size() > 10){
                        _oldcoefficients.remove(10);
                }
                
                learn();
                
        }
        
        public void learn(){
                _lossesinrow++;
                Random gen = new Random(System.currentTimeMillis());

                
                if(_oldcoefficients.size() > 9 && _lossesinrow < 7){
                        if(_lossesinrow%2 == 1){
                                _stdcoefficients = getAverageCoefficients();
                        }
                }

                if(_lossesinrow < 7){
                        int termschanged = 3*(_lossesinrow);
                        
                        for(int i = 0; i < termschanged; i++){
                                int partition = gen.nextInt(21);
                                int addsub = gen.nextInt(2);
                                int value = gen.nextInt(5)+1;
                                _stdcoefficients[partition].power = _stdcoefficients[partition].power+(2*addsub-1)*value;
                                if(_stdcoefficients[partition].power > 27){
                                        _stdcoefficients[partition].power = 27;
                                }
                                
                                if(_stdcoefficients[partition].power < 0){
                                        _stdcoefficients[partition].power = 0;
                                }

                        }
                }
                else{
                        for(int i = 0; i < 21; i++){
                                int sign = gen.nextInt(2);
                                int addsub = gen.nextInt(2);
                                int value = gen.nextInt(10)+1;
                                _stdcoefficients[i].power = _stdcoefficients[i].power+(2*addsub-1)*value;
                                _stdcoefficients[i].sign = 2*sign-1;
                                if(_stdcoefficients[i].power > 27){
                                        _stdcoefficients[i].power = 27;
                                }
                                
                                if(_stdcoefficients[i].power < 0){
                                        _stdcoefficients[i].power = 0;
                                }
                        }
                }
                
        }
        
        public void confirm(){
                if(_lossesinrow > 0){
                        if(_wincount == 1){
                                _oldcoefficients.add(0, _stdcoefficients);
                                _lossesinrow = 0;
                                
                                if(_oldcoefficients.size() > 10){
                                        _oldcoefficients.remove(10);
                                }
                                _wincount = 0;
                        }
                        else{
                                _wincount++;
                        }
                }
                
        }
        
        public Coefficient[] getAverageCoefficients(){
                
                Coefficient[] avgcoefficients = new Coefficient[21];
                
                for(int i = 0; i < 21; i++){
                        avgcoefficients[i] = new Coefficient(0,0);
                }
                
                for(Iterator<Coefficient[]> it = _oldcoefficients.iterator(); it.hasNext();){
                        Coefficient[] next = it.next();
                        for(int i = 0; i < 21; i++){
                                avgcoefficients[i].power += next[i].power;
                                if(next[i].sign == -1){
                                        avgcoefficients[i].sign = 0;
                                }
                                else{
                                        avgcoefficients[i].sign = 1;
                                }
                        }
                }
                
                for(int i = 0; i < 21; i++){
                        avgcoefficients[i].power = avgcoefficients[i].power/_oldcoefficients.size();
                        avgcoefficients[i].sign = Math.round(((float)avgcoefficients[i].sign/_oldcoefficients.size()))*2-1;
                }
                
                return avgcoefficients;
                
        }
        
        
        
}
