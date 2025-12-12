import java.io.Serializable;
public class Player implements Serializable{
    int index;
    String username;
    int[] resources;
    MyDLList<DevCard> devCards;
    boolean[] unlockedTrades;
    int vp;
    public Player(int index, String username){
        this.index = index;
        this.username = username;
        resources = new int[5];
        resources[0] = 0;
        resources[1] = 0;
        resources[2] = 0;
        resources[3] = 0;
        resources[4] = 0;

        devCards = new MyDLList<DevCard>();
        vp = 0;
        unlockedTrades = new boolean[] {false, false, false, false, false, false};
    }
    public String getUsername(){
        return username;
    }
    public boolean[] getUnlockedTrades(){
        return unlockedTrades;
    }
    public void setUnlockedTrades(boolean[] unlockedTrades){
        this.unlockedTrades = unlockedTrades;
    }
    public void addVP(int change){
        vp += change;
    }
    public int getVP(){
        return vp;
    }
    public int monopoly(int type){
        int returnVal = resources[type];
        resources[type] = 0;
        return returnVal;
    }
    public void addCard(TileType type){
        switch (type) {
            case ORE:
                resources[0] = resources[0]+1;
                break;
            case BRICK:
                resources[1] = resources[1]+1;
                break;
            case WHEAT:
                resources[2] = resources[2]+1;
                break;
            case SHEEP:
                resources[3] = resources[3]+1;
                break;
            case WOOD:
                resources[4] = resources[4]+1;
                break;
        }
    }

    public void addDevCard(DevCard newCard){
        devCards.add(newCard);
    }
    public MyDLList<DevCard> getDevCards(){
        return devCards;
    }
    public DevCard removeCard(int selectedCard) {
        return devCards.remove(selectedCard);
    }
    public int cardCount(){
        return resources[0]+resources[1]+resources[2]+resources[3]+resources[4];
    }
    public void addResources(int[] newResources){
        for(int i = 0; i < 5; i++){
            resources[i] = resources[i] + newResources[i];
        }
    }
    public void send(Player other){
        int which = (int)(Math.random() * cardCount());
        int counter = 0;
        for(int i = 0; i < 5; i++){
            counter += resources[i];
            if(counter >= which && resources[i] != 0){
                int[] change = new int[]{0,0,0,0,0};
                change[i]++;
                removeResources(change);
                other.addResources(change);
                return;
            }
        }
    }

    public void removeResources(int[] newResources){
        for(int i = 0; i < 5; i++){
            resources[i] = resources[i] - newResources[i];
        }
    }


    public boolean road(){
        if(resources[4] >= 1 && resources[1] >= 1 ){
            resources[4] -= 1;
            resources[1] -= 1;
            return true;
        }
        return false;
    }
    public boolean settlement(){
        if(resources[4] >= 1 && resources[1] >= 1 && resources[3] >= 1 && resources[2] >= 1){
            resources[4] -= 1;
            resources[1] -= 1;
            resources[3] -= 1;
            resources[2] -= 1;
            return true;
        }
        return false;
    }
    public boolean city(){
        if(resources[2] >= 2 && resources[0] >= 3){
            resources[2] -= 2;
            resources[0] -= 3;
            return true;
        }
        return false;
    }
    public boolean devCard(){
        if(resources[3] >= 1 && resources[2] >= 1 && resources[0] >= 1){
            resources[3] -= 1;
            resources[2] -= 1;
            resources[0] -= 1;

            return true;
        }
        return false;
    }
    public boolean canRoad(){
        return resources[4] >= 1 && resources[1] >= 1;
    }
    public boolean canSettlement(){
        return resources[4] >= 1 && resources[1] >= 1 && resources[3] >= 1 && resources[2] >= 1;
    }
    public boolean canCity(){
        return resources[2] >= 2 && resources[0] >= 3;
    }
    public int[] getResources(){
        return resources;
    }
}