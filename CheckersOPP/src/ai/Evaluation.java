package ai;

import java.util.Vector;

import features.*;
import utility.Board;
import utility.Move;

public class Evaluation {

        private Feature[] _standard;
        private FeatureUtility _util;
        private Coefficient[] _stdcoefficients;
        
        public Evaluation(Coefficient[] coef){
                _stdcoefficients = coef;                
                _standard = new Feature[21];
                _util = new FeatureUtility();
                
                _standard[0] = new Advancement();
                _standard[1] = new BackRowBridge();
                _standard[2] = new CenterControl();
                _standard[3] = new Deny();
                _standard[4] = new DenyMobOne();
                _standard[5] = new DenyMobTwo();
                _standard[6] = new DenyMobThree();
                _standard[7] = new DenyMobFour();
                _standard[8] = new DenyUnMobOne();
                _standard[9] = new DenyUnMobTwo();
                _standard[10] = new DenyUnMobThree();
                _standard[11] = new DenyUnMobFour();
                _standard[12] = new DoubleCorner();
                _standard[13] = new KingCenterControl();
                _standard[14] = new Mobility();
                _standard[15] = new Threat();
                _standard[16] = new UndeniedMobility();
                _standard[17] = new UnMobCenterOne();
                _standard[18] = new UnMobCenterTwo();
                _standard[19] = new UnMobCenterThree();
                _standard[20] = new UnMobCenterFour();

        }
        
        public int evaluate(Board b, boolean color){
                
                Vector<Integer> mobil = _util.getMobility(b, color);
                int mobilv = mobil.size();
                int denyv = _util.getDenialOfOccupancy(b, color, mobil);
                
                Vector<Integer> oppmobil = _util.getMobility(b, color);
                int oppmobilv = oppmobil.size();
                int oppdenyv = _util.getDenialOfOccupancy(b, color, oppmobil);
                
                Move[] nextmoves = b.getPossibleMoves(color);
                Move[] oppnextmoves = b.getPossibleMoves(!color);
                
                int value = 0;
                for(int i = 0; i < _standard.length; i++){
                        int sign = _stdcoefficients[i].sign;
                        int power = 1;
                        
                        power = power(_stdcoefficients[i].power);
                        
                        value += sign*_standard[i].getValue(b, nextmoves, color, mobilv, denyv)*power;
                        value -= sign*_standard[i].getValue(b, oppnextmoves, !color, oppmobilv, oppdenyv)*power;
                
                }
                
                int piececount;
                int opppiececount;
                
                if(color){
                        piececount = b.getBCheckerCount()+3*b.getBKingCount()/2;
                        opppiececount = b.getWCheckerCount()+3*b.getWKingCount()/2;
                }
                else{
                        piececount = b.getWCheckerCount()+3*b.getWKingCount()/2;
                        opppiececount = b.getBCheckerCount()+3*b.getBKingCount()/2;
                }
                
                value += (piececount-opppiececount)*power(27);
                
                return value;
        }
        
        private int power(int pow){
                int power = 1;
                for(int j = 0; j < pow; j++){
                        power = power*2;
                } 
                return power;
        }
}