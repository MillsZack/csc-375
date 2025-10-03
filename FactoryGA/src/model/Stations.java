package model;

public class Stations {
    public int id;
    public int type;
    
    public Stations(int id, int type){
        this.id = id;
        this.type = type;     
    }
    
    public int getId(){
        return id;
    }
    
    public int getType(){
        return type;
    }
    
    public String toString()
    {
        return "Station(ID = " + id + ", type = " + type;
    }        
    
}
