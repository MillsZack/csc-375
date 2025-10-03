package geneticAlgorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;
import static javax.swing.Spring.height;
import model.Layout;
import model.Spot;
import model.Stations;

/**
 *
 * @author ZMill
 */
public class ga {
    public int populationSize;
    public double mutationRate;
    public int generations;
    
    public int numTypes;
    int numThreads;
    private List<Stations> stations;
    private List<Spot> spots;
    int selectionPoolSize;
    private Fitness calculator;
    
    int selectionSize = 4;
    
    public ga(int populationSize, double mutationRate, Fitness calculator, int numThreads, List<Stations> stations){
        this.populationSize = populationSize;
        this.mutationRate = mutationRate;
        this.generations = generations;
        this.numThreads = numThreads;
        this.calculator = calculator;
    }
    
    public List<Stations> createStations(int numStations){
        Random random = new Random();
        List<Stations> stations = new ArrayList<>();
        for(int i = 0; i < numStations; i++){
            int type = random.nextInt(numTypes);
            stations.add(new Stations(i, type));
        }
        return stations;
    }
    
    public List<Layout> generateRandomList(List<Stations> stations, int width, int height){
      Random random = new Random();
      List<Layout> population = new ArrayList<>();
      for(int i = 0; i < populationSize; i++){
          Layout layout =  new Layout();
          for(Stations s : stations){
              Spot randomS = new Spot(random.nextInt(width), random.nextInt(height));
              layout.assign(s, randomS);
      }
          population.add(layout);
      }
      return population;  
    }
    
    private void fitnessPhaser(List<Layout> population, List<Stations> stations) throws InterruptedException {
        Phaser phaser = new Phaser(1); //Main Thread
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        
        for(Layout layout : population){
            phaser.register();
            executor.submit(() -> {
                try {
                    calculator.calculateTotal(layout, stations);
                } finally {
                    phaser.arriveAndDeregister();
                }
        });
        }
        phaser.arriveAndAwaitAdvance();
        executor.shutdown();
        try {
        executor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Interrupted in Fitness.");
            
        }
    }
    
    private Layout crossover(Layout p1, Layout p2, List<Stations> stations){  
        Random random = new Random();
        int pointOfCross = random.nextInt(stations.size());
        
        Layout c = new Layout();
        
        for(int i = 0; i < stations.size(); i++){
            Stations s = stations.get(i);
            Spot spot;
            
            if(i < pointOfCross){
                spot = p1.getSpot(s);
            } else {
                spot = p2.getSpot(s);
            }
            
            if(spot != null){
                c.assign(s, new Spot(spot.getX(), spot.getY()));
            }
        }
        return c;
    }
    
    private Layout selection(List<Layout> population){
        Random random = new Random();
        Layout best = null;
        double highscore = 0.00;
        
        for(int i = 0; i < selectionSize; i++){
            Layout temp = population.get(random.nextInt(population.size()));
            double score = temp.getFitness();
            if(score > highscore){
                best = temp;
                highscore = score;
            }
        }
        return best;
    }
    
    private void mutate(Layout layout){
        Random random = new Random();
        if(random.nextDouble() < mutationRate){
            int i = random.nextInt(stations.size());
            int k = random.nextInt(stations.size());
            
            Stations tempStation1 = stations.get(i);
            Stations tempStation2 = stations.get(k);
            
            Spot tempSpot1 = layout.getSpot(tempStation1);
            Spot tempSpot2 = layout.getSpot(tempStation2);
            
            if(tempSpot1 != null && tempSpot2 != null){
                layout.assign(tempStation1, new Spot(tempSpot2.getX(), tempSpot2.getY()));
                layout.assign(tempStation2, new Spot(tempSpot1.getX(), tempSpot1.getY()));
            }
        }
    
    
    } 
    
    public Layout run(int numStations, int width, int height) throws InterruptedException {
        this.stations = createStations(numStations);
        
        List<Layout> population = generateRandomList(stations, width, height);
        
        fitnessPhaser(population, stations);
        
        for(int i = 0; i  < generations; i++){
            List<Layout> newList = new ArrayList<>();
            
            for (int j = 0; j < populationSize; j++){
                Layout p1 = selection(population);
                Layout p2 = selection(population);
                
                Layout c = crossover(p1, p2, stations);
                mutate(c);
                newList.add(c);
                
            }
            
            population = newList;
            fitnessPhaser(population, stations);
            
        }
        
        Layout best = null;
        double score = Double.NEGATIVE_INFINITY;
        for(Layout layout: population){
            if(layout.getFitness() > score){
                best = layout;
                score = layout.getFitness();
            }
           
        }
        return best;
    }
}