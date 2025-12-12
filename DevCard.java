import java.io.Serializable;
public class DevCard implements Serializable{
    int type;
    boolean playable;
    public DevCard(int type){// 0. knight 1. 2 roads 2. 2 free resources 3. monopoly 4. +1 point
        this.type = type;
        playable = false;
    }
    public int getType(){
        return type;
    }
    public void age(){
        playable = true;
    }
    public boolean usable(){
        return playable;
    }
}