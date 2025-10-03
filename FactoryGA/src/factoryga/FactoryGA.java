package factoryga;

import UserInterface.UI;
import geneticAlgorithm.Fitness;
import geneticAlgorithm.ga;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import model.Layout;

/**
 *
 * @author ZMill
 */
public class FactoryGA {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        int populationSize = 32;
        double mutationRate = 0.1;
        int generations = 10;
        int numThreads = 32;
        int numStations = 32;
        int numTypes = 4;
        int width = 32;
        int height = 32;
        
        Fitness fitnessCalculator = new Fitness(numTypes);
        
        fitnessCalculator.affinity[0][1] = 5;
        fitnessCalculator.affinity[1][0] = 5;
        fitnessCalculator.affinity[1][2] = 3;
        fitnessCalculator.affinity[2][1] = 3;
        fitnessCalculator.affinity[0][2] = 1;
        fitnessCalculator.affinity[2][0] = 1;
        
        ga geneticAlgorithm = new ga(populationSize, mutationRate, fitnessCalculator, numThreads, null);
        geneticAlgorithm.generations = generations;
        geneticAlgorithm.numTypes = numTypes;
        
        Layout best = geneticAlgorithm.run(numStations, width, height);
        
        System.out.println("Best fitness score: " + best.getFitness());
        
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Visualize Layout");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            UI panel = new UI(best);
            frame.add(panel);
            
            frame.setSize(600, 600);
            frame.setVisible(true);
            
         
    });
    }
        
        
               
        
        
         
}
