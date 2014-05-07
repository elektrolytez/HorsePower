package ai;

import java.io.Serializable;

public class Coefficient implements Serializable {

        /**
         * Version 1.0
         */
        private static final long serialVersionUID = 1L;
        public int sign;
        public int power;

        public Coefficient(int s, int p){
                sign = s;
                power = p;
        }
        
}