package model;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author ZMill
 */
public class Layout {
    Map<Stations, Spot> placement;
    public double fitness;
    
    public Layout(){
        placement = new HashMap<>();
    }   
    
    public void assign(Stations s, Spot p){
        placement.put(s, p);
    }
    
    public Spot getSpot(Stations s){
        return placement.get(s);
    }
    
    public void setFitness(double score){
        this.fitness = score;
    }
    
    public double getFitness(){
        return fitness;
    }
    
    public Map<Stations, Spot> getPlacements(){
        return placement;
    }
}
