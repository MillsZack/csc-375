package geneticAlgorithm;

import java.util.List;
import model.Layout;
import model.Spot;
import model.Stations;

/**
 *
 * @author ZMill
 */
public class Fitness {
    public double[][] affinity;
    
    public Fitness(int num){
        affinity = new double[num][num];
    }
    
    public double affinity(Stations A, Stations B, Layout layout){
        Spot spotA = layout.getSpot(A);
        Spot spotB = layout.getSpot(B);
        
        if(spotA == null || spotB == null)
            return 0;
        
        double distance = Math.sqrt(Math.pow(spotA.getX() - spotB.getX(), 2))
                + (Math.pow(spotA.getY() - spotB.getY(), 2));
        
        double typeOf = affinity[A.getType()][B.getType()];
        
        
        return typeOf / (1 + distance);
        
    }
    
    public double calculateTotal(Layout layout, List<Stations> stations){
        double total = 0;
        
        for(int i = 0; i < stations.size(); i++){
            for(int k = i + 1; k < stations.size(); k++){
                total += affinity(stations.get(i), stations.get(k), layout);
            }
        }
        
        layout.setFitness(total);
        
        return total;
    }
    
    
}
