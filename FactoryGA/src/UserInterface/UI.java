 package UserInterface;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Map;
import javax.swing.JPanel;
import model.Layout;
import model.Spot;
import model.Stations;

/**
 *
 * @author ZMill
 */
public class UI extends JPanel {
    private Layout layout;
    private int cellSize = 15;
    
    public UI(Layout layout){
        this.layout = layout;
        setPreferredSize(new Dimension(500, 500));
    }
    
    private final Color[] colors = {
       Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN
    };
    
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        int width = getWidth() / cellSize;
        int height = getHeight() / cellSize;
        
        g.setColor(Color.BLACK);
        for(int i =0; i < width; i++){
            g.drawLine(i * cellSize, 0, i * cellSize, getHeight());
        }
        for(int j =0; j < width; j++){
            g.drawLine(0, j * cellSize, getWidth(), j * cellSize);
        }
        
        for(Map.Entry<Stations, Spot> entry : layout.getPlacements().entrySet()){
            Stations s = entry.getKey();
            Spot spot = entry.getValue();
            
            if(spot != null){
                int pX = spot.getX() * cellSize;
                int pY = spot.getY() * cellSize;
                
                int type = s.getType();
                Color color = colors[type % colors.length];
                g.setColor(color);
                g.fillOval(pX, pY, cellSize, cellSize);
                
                g.setColor(Color.WHITE);
                g.drawString(String.valueOf(s.getId()), pX + 5, pY + 5);
            }
            
        }
    }
    public void updateLayout(Layout updated){
        this.layout = updated;
        repaint();
    }
    
      
    
    
    
}
 