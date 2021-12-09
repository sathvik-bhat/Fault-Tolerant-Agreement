import common.*;
import java.util.*;
import java.lang.Math;
public class Game_0009 extends Game{
    
    public Game_0009(){
        machines= new ArrayList<Machine>();
    }

    public void addMachines(ArrayList<Machine> machines1, int numFaulty){
        machines.addAll(0,machines1);
        t=numFaulty;
    }

    public void startPhase(){
        ArrayList<Boolean> correct=new ArrayList<Boolean>();
        for(int i=0; i<t; i++){
            correct.add(false);
        }
        for(int i=t; i<machines.size(); i++){
            correct.add(true);
        }
        Collections.shuffle(correct);
    
        for(int i=0; i<correct.size(); i++){
            machines.get(i).setState(correct.get(i));
        }
    
        int min=0; int max=machines.size()-1;
        int rand = (int)(Math.random()*(max-min+1)+min);  
        machines.get(rand).setLeader();        
    }
    private ArrayList<Machine> machines;
    private int t;
    
}
