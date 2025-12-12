import java.io.Serializable;
public class Trade implements Serializable{
    int index1, index2;
    int[] resources1, resources2;
    Player p1,p2;
    boolean agree1, agree2;
    public Trade(int index1, int index2, int[] resources1, int[] resources2, Player p1, Player p2, boolean agree1, boolean agree2){
        this.index1 = index1;
        this.index2 = index2;
        this.resources1 = resources1;
        this.resources2 = resources2;
        this.p1 = p1;
        this.p2 = p2;
        this.agree1 = agree1;
        this.agree2 = agree2;

    }
    public int getIndex1(){
        return index1;
    }
    public int getIndex2(){
        return index2;
    }
    public int[] getResources1(){
        return resources1;
    }
    public int[] getResources2(){
        return resources2;
    }
    public boolean getAgree1(){
        return agree1;
    }
    public boolean getAgree2(){
        return agree2;
    }
    public void setAgree1(boolean newAgree){
        agree1 = newAgree;
    }
    public void setAgree2(boolean newAgree){
        agree2 = newAgree;
    }
    public boolean checkTrade(){
        if(agree1 == agree2){
            int[] totalR1 = p1.getResources();
            int[] totalR2 = p2.getResources();
            
            if(agree1 == true){
                if(!check1(totalR1) || !check2(totalR2)){
                    return true;
                }
                p1.addResources(resources2);
                p1.removeResources(resources1);
                p2.addResources(resources1);
                p2.removeResources(resources2);
            }
            return true;
        }
        return false;

    }
    public boolean check1(int[] totalR1){
        for(int i = 0; i < 5; i++){
            if(totalR1[i]-resources1[i] < 0){
                return false;
            }
        }
        return true;
    }
    public boolean check2(int[] totalR2){
        for(int i = 0; i < 5; i++){
            if(totalR2[i]-resources2[i] < 0){
                return false;
            }
        }
        return true;
    }


}

