package features;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import ai.AIModule;
import ai.Coefficient;

public class Main {

        /**
         * @param args
         */
        public static void main(String[] args) {
                AIModule aim;
                FileInputStream fis;
                try {
                        fis = new FileInputStream(args[0]);
                        try {
                                ObjectInputStream ois = new ObjectInputStream(fis);
                                aim = (AIModule) ois.readObject();
                                ois.close();
                                fis.close();
                                
                                Coefficient[] coeff = aim.getSTDCoefficients();

                                System.out.println("Coefficients Used:");
                                System.out.println("Advancement: Sign = "+coeff[0].sign+" Power = "+coeff[0].power);
                                System.out.println("Back Row Bridge: Sign = "+coeff[1].sign+" Power = "+coeff[1].power);
                                System.out.println("Center Control: Sign = "+coeff[2].sign+" Power = "+coeff[2].power);
                                System.out.println("Denial of Occupancy: Sign = "+coeff[3].sign+"Power = "+coeff[3].power);
                                System.out.println("Denial+Mobility 1: Sign = "+coeff[4].sign+" Power = "+coeff[4].power);
                                System.out.println("Denial+Mobility 2: Sign = "+coeff[5].sign+" Power = "+coeff[5].power);
                                System.out.println("Denial+Mobility 3: Sign = "+coeff[6].sign+" Power = "+coeff[6].power);
                                System.out.println("Denial+Mobility 4: Sign = "+coeff[7].sign+" Power = "+coeff[7].power);
                                System.out.println("Denial+Undenied Mobility 1: Sign = "+coeff[8].sign+" Power = "+coeff[8].power);
                                System.out.println("Denial+Undenied Mobility 2: Sign = "+coeff[9].sign+" Power = "+coeff[9].power);
                                System.out.println("Denial+Undenied Mobility 3: Sign = "+coeff[10].sign+" Power = "+coeff[10].power);
                                System.out.println("Denial+Undenied Mobility 4: Sign = "+coeff[11].sign+" Power = "+coeff[11].power);
                                System.out.println("Double Corner: Sign = "+coeff[12].sign+" Power = "+coeff[12].power);
                                System.out.println("King Center Control: Sign = "+coeff[13].sign+" Power = "+coeff[13].power);
                                System.out.println("Mobility: Sign = "+coeff[14].sign+" Power = "+coeff[14].power);
                                System.out.println("Threat: Sign = "+coeff[15].sign+" Power = "+coeff[15].power);
                                System.out.println("Undenied Mobility: Sign = "+coeff[16].sign+" Power = "+coeff[16].power);
                                System.out.println("Undenied Mobility+Center Control 1: Sign = "+coeff[17].sign+" Power = "+coeff[17].power);
                                System.out.println("Undenied Mobility+Center Control 2: Sign = "+coeff[18].sign+" Power = "+coeff[18].power);
                                System.out.println("Undenied Mobility+Center Control 3: Sign = "+coeff[19].sign+" Power = "+coeff[19].power);
                                System.out.println("Undenied Mobility+Center Control 4: Sign = "+coeff[20].sign+" Power = "+coeff[20].power);

                                coeff = aim.getAverageCoefficients();
                                System.out.println("\n\nAverage Coefficients:");
                                System.out.println("Advancement: Sign = "+coeff[0].sign+" Power = "+coeff[0].power);
                                System.out.println("Back Row Bridge: Sign = "+coeff[1].sign+" Power = "+coeff[1].power);
                                System.out.println("Center Control: Sign = "+coeff[2].sign+" Power = "+coeff[2].power);
                                System.out.println("Denial of Occupancy: Sign = "+coeff[3].sign+" Power = "+coeff[3].power);
                                System.out.println("Denial+Mobility 1: Sign = "+coeff[4].sign+" Power = "+coeff[4].power);
                                System.out.println("Denial+Mobility 2: Sign = "+coeff[5].sign+" Power = "+coeff[5].power);
                                System.out.println("Denial+Mobility 3: Sign = "+coeff[6].sign+" Power = "+coeff[6].power);
                                System.out.println("Denial+Mobility 4: Sign = "+coeff[7].sign+" Power = "+coeff[7].power);
                                System.out.println("Denial+Undenied Mobility 1: Sign = "+coeff[8].sign+" Power = "+coeff[8].power);
                                System.out.println("Denial+Undenied Mobility 2: Sign = "+coeff[9].sign+" Power = "+coeff[9].power);
                                System.out.println("Denial+Undenied Mobility 3: Sign = "+coeff[10].sign+" Power = "+coeff[10].power);
                                System.out.println("Denial+Undenied Mobility 4: Sign = "+coeff[11].sign+" Power = "+coeff[11].power);
                                System.out.println("Double Corner: Sign = "+coeff[12].sign+" Power = "+coeff[12].power);
                                System.out.println("King Center Control: Sign = "+coeff[13].sign+" Power = "+coeff[13].power);
                                System.out.println("Mobility: Sign = "+coeff[14].sign+" Power = "+coeff[14].power);
                                System.out.println("Threat: Sign = "+coeff[15].sign+" Power = "+coeff[15].power);
                                System.out.println("Undenied Mobility: Sign = "+coeff[16].sign+"Power = "+coeff[16].power);
                                System.out.println("Undenied Mobility+Center Control 1: Sign = "+coeff[17].sign+" Power = "+coeff[17].power);
                                System.out.println("Undenied Mobility+Center Control 2: Sign = "+coeff[18].sign+" Power = "+coeff[18].power);
                                System.out.println("Undenied Mobility+Center Control 3: Sign = "+coeff[19].sign+" Power = "+coeff[19].power);
                                System.out.println("Undenied Mobility+Center Control 4: Sign = "+coeff[20].sign+" Power = "+coeff[20].power);

                                
                        } catch (IOException e) {
                                e.printStackTrace();
                                System.exit(1);
                        } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                                System.exit(1);
                        } 
                } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                        System.exit(1);
                }
                
                
        }

}