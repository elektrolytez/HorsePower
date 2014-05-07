package ai;

public class Main {

        public static void main(String args[]){
                if(args.length > 0){
                        if(args[0].equals("learn")){
                                boolean cycle = true;
                                int count = Integer.parseInt(args[1]);
                                
                                for(int i = 0; i < count; i++){
                                        new AILearningModule(cycle);
                                        cycle = !cycle;
                                }
                        }
                }
                else{
                        new AINetworkModule(new AIProgram("alpha.ai", true));
                }
        }
}