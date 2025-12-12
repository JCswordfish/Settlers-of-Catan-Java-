
import java.io.Serializable;

public class Tile implements Serializable{
    private TileType type;
    private int r,c, activationRoll;
    boolean robber;
    public Tile(int r, int c, TileType type, int activationRoll){
        this.r = r;
        this.c = c;
        this.type = type;
        this.activationRoll = activationRoll;
        if(type.equals(TileType.DESERT)){
            robber = true;
        }
    }
    public int getR(){
        return r;
    }
    public int getC(){
        return c;
    }
    public TileType getType(){
        return type;
    }
    public int getAR(){
        return activationRoll;
    }
    public void toggleRobber(){
        robber = !robber;
    }
    public boolean isRobber(){
        return robber;
    }
    public boolean equals(Object other){
        if(((Tile)other).getR() == r && ((Tile)other).getC() == c){
            return true;
        }
        return false;
    }
}