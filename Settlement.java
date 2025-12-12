
import java.io.Serializable;

public class Settlement implements Serializable{
    int r,c,index;
    MyArrayList<Tile> tiles;
    boolean city;
    public Settlement(int r, int c, int index, MyArrayList<Tile> tiles){
        this.r = r;
        this.c = c;
        this.index = index;
        this.tiles = tiles;
        
    }
    public int getR(){
        return r;
    }
    public void setCity(){
        city = true;
    }
    public int getC(){
        return c;
    }
    public int getIndex(){
        return index;
    }
    public MyArrayList<Tile> getTiles(){
        return tiles;
    }
    public boolean isCity(){
        return city;
    }
}