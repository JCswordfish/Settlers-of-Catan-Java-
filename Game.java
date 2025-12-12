import java.io.Serializable;

public class Game implements Serializable{
    Tile[][] grid;
    int selected;
    Player[] players;
    boolean reversed, phaseChange, shouldRob;
    int phase = 0; 
    int size;
    int original, last;
    MyArrayList<Settlement> settlements;
    MyArrayList<Integer[]> roads;
    MyArrayList<Trade> trades;
    MyArrayList<String> cardList;
    int currentRoll;
    int winner;
    Tile robberTile;
    int[] ports, knights;
    int[] roadLengths;
    int currentLongestRoads, currentLongestRoadsCount;
    int currentMostKnights, currentMostKnightsCount;


    private static final int[][] port1 = new int[][] { new int[] {2, 1}, new int[] {1, 1} };
    private static final int[][] port2 = new int[][] { new int[] {0, 2}, new int[] {0, 3} };
    private static final int[][] port3 = new int[][] { new int[] {0, 5}, new int[] {0, 6} };
    private static final int[][] port4 = new int[][] { new int[] {1, 8}, new int[] {1, 9} };
    private static final int[][] port5 = new int[][] { new int[] {2, 10}, new int[] {3, 10} };
    private static final int[][] port6 = new int[][] { new int[] {4, 9}, new int[] {4, 8} };
    private static final int[][] port7 = new int[][] { new int[] {5, 6}, new int[] {5, 5} };
    private static final int[][] port8 = new int[][] { new int[] {5, 3}, new int[] {5, 2} };
    private static final int[][] port9 = new int[][] { new int[] {4, 1}, new int[] {3, 1} };

    //0: placing roads/house
    //1: gameplay

    public Game(int size, String[] usernames){
        grid = new Tile[5][5];
        settlements = new MyArrayList<Settlement>(); //6 row, 11 col in the grid
        roads = new MyArrayList<Integer[]>();
        trades = new MyArrayList<Trade>();
        cardList = new MyArrayList<String>();
        
        for(int i = 0; i < 14; i++) cardList.add("0");
        for(int i = 0; i < 2; i++) cardList.add("1");
        for(int i = 0; i < 2; i++) cardList.add("2");
        for(int i = 0; i < 2; i++) cardList.add("3");
        for(int i = 0; i < 5; i++) cardList.add("4");

        selected = (int)(Math.random() * size);
        this.size = size;
        original = selected;
        if(original == 0){
            last = size-1;
        } else {
            last = original-1;
        }
        players = new Player[size];
        for(int i = 0; i < size; i++){
            players[i] = new Player(i, usernames[i]);
        }
        reversed = false;
        generateGrid();
        phaseChange = false;
        winner = -1;
        shouldRob = false;
        roadLengths = new int[size];
        knights = new int[size];

        currentMostKnights = -1;
        currentLongestRoads = -1;

        currentMostKnightsCount = 2;
        currentLongestRoadsCount = 4;
        
        
    }
    public int getPhase(){
        return phase;
    }
    public int getSize(){
        return size;
    }
    public Player getPlayer(int index){
        return players[index];
    }
    public int getSelected(){
        return selected;
    }
    public int[] getRoadLengths(){
        return roadLengths;
    }
    public int[] getKnights(){
        return knights;
    }
    public void addKnight(int index){
        knights[index]++;
        int maxKnights = currentMostKnightsCount;
        int maxPlayer = currentMostKnights;
        for (int i = 0; i < players.length; i++) {
            if (knights[i] > maxKnights) {
                maxKnights = knights[i];
                maxPlayer = i;
            }
        }
        currentMostKnightsCount = maxKnights;
        currentMostKnights = maxPlayer;

    }
    public boolean addCity(int r, int c){
        Settlement tempSettlement;
        int tempR,tempC;
        for(int i = 0; i < settlements.size(); i++){
            tempSettlement = settlements.get(i);
            tempR = tempSettlement.getR();
            tempC = tempSettlement.getC();
            if(tempSettlement.getIndex() == selected && tempR == r && tempC == c && players[selected].city()){
                tempSettlement.setCity();
                players[selected].addVP(1);
                return true;
            }
        }
        return true;
    
    }
    public int mostKnights(){
        return currentMostKnights;
    }
    public int mostRoads(){
        return currentLongestRoads;
    }
    public boolean addSettlement(int r, int c){
        Settlement tempSettlement;
        int tempR,tempC;
        if ((r+c) % 2 == 0) { // one above, ID rightmost, blue
            for(int i = 0; i < settlements.size(); i++){
                tempSettlement = settlements.get(i);
                tempR = tempSettlement.getR();
                tempC = tempSettlement.getC();
                if((tempR == r && tempC == c) || (tempR == r && tempC == c-1) || (tempR == r && tempC == c+1) || (tempR == r+1 && tempC == c)){
                    return false;
                }
            }
        } else { // one below ID, orange
            for(int i = 0; i < settlements.size(); i++){
                tempSettlement = settlements.get(i);
                tempR = tempSettlement.getR();
                tempC = tempSettlement.getC();
                if((tempR == r && tempC == c) || (tempR == r && tempC == c-1) || (tempR == r && tempC == c+1) || (tempR == r-1 && tempC == c)){
                    return false;
                }
            }
        }

        if(phase == 0){
            MyArrayList<Tile> adjacentTiles = getAdjacentTiles(r,c);
            if(reversed){
                for(int i = 0; i < adjacentTiles.size(); i++){
                    players[selected].addCard(adjacentTiles.get(i).getType());
                }
            }
            settlements.add( new Settlement(r, c, selected, adjacentTiles));
            players[selected].addVP(1);
            if(portCheck(r,c) != -1){ //portcheck returns -1 if not in port, or the value corresponding to the port, not port index
                boolean[] tempUnlocked = players[selected].getUnlockedTrades();
                tempUnlocked[portCheck(r,c)] = true;
                players[selected].setUnlockedTrades(tempUnlocked);
            }
            return true;
        } else {
            for(int i = 0; i < roads.size(); i++){
                Integer[] tempRoad = roads.get(i);
                if(tempRoad[2] == r && tempRoad[3] == c && tempRoad[4] == selected && players[selected].settlement()){
                    settlements.add( new Settlement(r, c, selected, getAdjacentTiles(r,c)) ); // continuation of road
                    players[selected].addVP(1);
                    if(portCheck(r,c) != -1){ //portcheck returns -1 if not in port, or the value corresponding to the port, not port index
                        boolean[] tempUnlocked = players[selected].getUnlockedTrades();
                        tempUnlocked[portCheck(r,c)] = true;
                        players[selected].setUnlockedTrades(tempUnlocked);
                    }
                    return true;
                }
            }
        }
        
        return false;
    }
    public void addDevCard(){
        int typeCard = Integer.parseInt(cardList.remove((int)(cardList.size()*Math.random())));
        players[selected].addDevCard(new DevCard(typeCard));
        if(typeCard == 4){
            players[selected].addVP(1);
        }
    }
    public boolean addRoad(int r1, int c1, int r2, int c2){
        if ((r1 == r2 && c1 == c2-1) || (r1 == r2 && c1 == c2+1) || (((r1+c1) % 2 == 1 && r1 == r2+1 && c1 == c2) || ((r1+c1) % 2 == 0 && r1 == r2-1 && c1 == c2))) {
            if(phase == 0){
                boolean firstSettlement = true;
                for(int i = 0; i < settlements.size(); i++){
                    Settlement tempSettlement = settlements.get(i);
                    if(tempSettlement.getR() == r1 && tempSettlement.getC() == c1 && tempSettlement.getIndex() == selected){
                        if(reversed){
                            if(!firstSettlement){
                                roads.add(new Integer[]{r1, c1, r2, c2, selected}); // added on to settlement
                                roadLengths();
                                return true;
                            }
                        } else {
                            roads.add(new Integer[]{r1, c1, r2, c2, selected}); // added on to settlement
                            roadLengths();
                            return true;
                        }
                        
                    } else if(tempSettlement.getIndex() == selected){
                        firstSettlement = false;
                    }
                }
            } else {
                for(int i = 0; i < roads.size(); i++){
                    Integer[] tempRoad = roads.get(i);
                    if(tempRoad[2] == r1 && tempRoad[3] == c1 && tempRoad[4] == selected && players[selected].road()){
                        roads.add(new Integer[]{r1, c1, r2, c2, selected}); // continuation of road
                        roadLengths();
                        return true;
                    }
                }
                for(int i = 0; i < settlements.size(); i++){
                    Settlement tempSettlement = settlements.get(i);
                    if(tempSettlement.getR() == r1 && tempSettlement.getC() == c1 && tempSettlement.getIndex() == selected && players[selected].road()){
                        roads.add(new Integer[]{r1, c1, r2, c2, selected}); // added on to settlement
                        roadLengths();
                        return true;
                    }
                }
            }
        }
        
        return false;
    }
    public boolean addFreeRoad(int r1, int c1, int r2, int c2){
        if ((r1 == r2 && c1 == c2-1) || (r1 == r2 && c1 == c2+1) || (((r1+c1) % 2 == 1 && r1 == r2+1 && c1 == c2) || ((r1+c1) % 2 == 0 && r1 == r2-1 && c1 == c2))) {
            for(int i = 0; i < roads.size(); i++){
                Integer[] tempRoad = roads.get(i);
                if(tempRoad[2] == r1 && tempRoad[3] == c1 && tempRoad[4] == selected){
                    roads.add(new Integer[]{r1, c1, r2, c2, selected}); // continuation of road
                    roadLengths();
                    return true;
                }
            }
            for(int i = 0; i < settlements.size(); i++){
                Settlement tempSettlement = settlements.get(i);
                if(tempSettlement.getR() == r1 && tempSettlement.getC() == c1 && tempSettlement.getIndex() == selected){
                    roads.add(new Integer[]{r1, c1, r2, c2, selected}); // added on to settlement
                    roadLengths();
                    return true;
                }
            }
        }
        
        return false;
    }
    public int getWinner(){
        return winner;
    }
    public int getCurrentRoll(){
        return currentRoll;
    }
    public boolean phaseChange(){
        if(phaseChange){
            phaseChange = false;
            return true;
        } else {
            return false;
        }
        
        
    }
    public void next(){
        if(phase == 0){
            if(!reversed){
                selected++;
                if(selected == size){
                    selected = 0;
                }
                if (selected == original){
                    selected = last;
                    reversed = true;
                }
            } else {
                selected--;
                if(selected == -1){
                    selected = size-1;
                }
                if(selected == last){
                    selected = original;
                    reversed = false;
                    phaseChange = true;
                    phase = 1;
                    currentRoll = (int)(Math.random() * 6 + 1) + (int)(Math.random() * 6 + 1);
                    for(int i = 0; i < settlements.size(); i++){
                        int tempIndex = settlements.get(i).getIndex();
                        MyArrayList<Tile> tempTiles = settlements.get(i).getTiles();
                        for(int j = 0; j < tempTiles.size(); j++){
                            if(tempTiles.get(j).getAR() == currentRoll){
                                players[tempIndex].addCard(tempTiles.get(j).getType());
                            }
                        }
                    }
                }
            }
            
        } else {

            trades = new MyArrayList<Trade>();
            MyDLList<DevCard> tempcards;
            for(int i = 0; i < players.length; i++){
                tempcards = players[i].getDevCards();
                for(int j = 0; j < tempcards.size(); j++){
                    tempcards.get(j).age();
                }
            }
            selected++;
            if(selected == size){
                selected = 0;
            }
            currentRoll = (int)(Math.random() * 6 + 1) + (int)(Math.random() * 6 + 1);
            if(currentRoll == 7){
                shouldRob = true;
            }
            for(int i = 0; i < settlements.size(); i++){
                int tempIndex = settlements.get(i).getIndex();
                MyArrayList<Tile> tempTiles = settlements.get(i).getTiles();
                for(int j = 0; j < tempTiles.size(); j++){
                    if(tempTiles.get(j).getAR() == currentRoll && !tempTiles.get(j).isRobber()){
                        players[tempIndex].addCard(tempTiles.get(j).getType());
                        if(settlements.get(i).isCity()){
                            players[tempIndex].addCard(tempTiles.get(j).getType());
                        }
                    }
                }
            }
        }
        
        
    }
    public boolean shouldRob(){
        if(shouldRob){
            shouldRob = false;
            return true;
        }
        return false;
    }


    public void generateGrid(){
        int a;
        MyArrayList<TileType> tilesAvailable = new MyArrayList<TileType>();
        for(int i = 0; i < 3; i++){
            tilesAvailable.add(TileType.ORE);
            tilesAvailable.add(TileType.WHEAT);
            tilesAvailable.add(TileType.SHEEP);
            tilesAvailable.add(TileType.BRICK);
            tilesAvailable.add(TileType.WOOD);
        }
        tilesAvailable.add(TileType.WHEAT);
        tilesAvailable.add(TileType.SHEEP);
        tilesAvailable.add(TileType.WOOD);
        tilesAvailable.add(TileType.DESERT);
        MyArrayList<Integer> ARavailable = new MyArrayList<Integer>();
        ARavailable.add(2);
        ARavailable.add(3);
        ARavailable.add(3);
        ARavailable.add(4);
        ARavailable.add(4);
        ARavailable.add(5);
        ARavailable.add(5);
        ARavailable.add(6);
        ARavailable.add(6);
        ARavailable.add(8);
        ARavailable.add(8);
        ARavailable.add(9);
        ARavailable.add(9);
        ARavailable.add(10);
        ARavailable.add(10);
        ARavailable.add(11);
        ARavailable.add(11);
        ARavailable.add(12);
        for(int i = 0; i < 5; i++){
            if(i <= 2){
                a = 0;
            } else {
                a = i-2;
            }
            for(int k = a; k < a + 5-(Math.abs(2-i)); k++){
                TileType tempType = tilesAvailable.remove((int)(Math.random() * tilesAvailable.size()));
                if(tempType.equals(TileType.DESERT)){
                    robberTile = new Tile(i,k,tempType,0);
                    grid[i][k] = robberTile;
                } else {
                    grid[i][k] = new Tile(i,k,tempType,ARavailable.remove((int)(Math.random() * ARavailable.size())));
                }

                
            }
        }

        ports = new int[9];
        MyArrayList<Integer> portsAvailable = new MyArrayList<Integer>();
        portsAvailable.add(5);
        portsAvailable.add(5);
        portsAvailable.add(5);
        portsAvailable.add(5);
        portsAvailable.add(5);
        portsAvailable.add(0);
        portsAvailable.add(1);
        portsAvailable.add(2);
        portsAvailable.add(3);
        portsAvailable.add(4);
        for(int i = 0; i < 9; i++){
            ports[i] = portsAvailable.remove((int)(Math.random() * portsAvailable.size()));
        }
    }

    public int[] getPorts(){
        return ports;
    }
    public Tile[][] getGrid(){
        return grid;
    }
    public MyArrayList<Settlement> getSettlements(){
        return settlements;
    }
    public MyArrayList<Integer[]> getRoads(){
        return roads;
    }
    public void checkForWin(){
        for(int i = 0; i < players.length; i++){
            int requiredVP = 10;
            if(i == currentMostKnights) requiredVP -= 2;
            if(i == currentLongestRoads) requiredVP -= 2;
            if(players[i].getVP() >= requiredVP){
                winner = i;
            }
        }
    }
    public boolean rob(int r, int c){
        if(!grid[r][c].isRobber()){
            robberTile.toggleRobber();
            grid[r][c].toggleRobber();
            robberTile = grid[r][c];
            return true;
        }
        return false;
    }
    public boolean steal(int r, int c){
        for(int i = 0; i < settlements.size(); i++){
            if(settlements.get(i).getR() == r && settlements.get(i).getC() == c && settlements.get(i).getTiles().contains(robberTile)){
                players[settlements.get(i).getIndex()].send(players[selected]);
                return true;
            }
        }
        return false;
    }
    public MyArrayList<Tile> getAdjacentTiles(int r, int c) {
        MyArrayList<Tile> tiles = new MyArrayList<Tile>();
        int oneRow, twoRow;
        int stagger = 2-r;
        int coordC;
        if ((r+c) % 2 == 0) { // one above, ID rightmost, blue
            oneRow = r-1;
            twoRow = r;
            coordC = (c-stagger)/2;
            addCheck(tiles, new int[]{twoRow,coordC});
            addCheck(tiles, new int[]{twoRow,coordC-1});
            addCheck(tiles, new int[]{oneRow,coordC-1});

        } else { // one below ID, orange
            oneRow = r;
            twoRow = r-1;
            coordC = (c-stagger-1)/2;
            addCheck(tiles, new int[]{oneRow,coordC});
            addCheck(tiles, new int[]{twoRow,coordC});
            addCheck(tiles, new int[]{twoRow,coordC-1});
            
        }


        return tiles;
    }
    public void addCheck(MyArrayList<Tile> tiles, int[] coords){
        int row = coords[0];
        int col = coords[1];
        if(validTile(row,col)){
            tiles.add(grid[row][col]);
        }
    }
    public boolean validTile(int row, int col){
        return !((col + Math.abs(row - 4)) <= 1) && !((row + Math.abs(col - 4)) <= 1) && row >= 0 && row < 5 && col >= 0 && col < 5;
    }
    public boolean validSettlement(int row, int col){
        return ((row + col) < 14 && (row + col) > 1 && row >= 0 && row < 6 && col >= 0 && col < 11 && !(row+Math.abs(col-10) < 2) && !(col+Math.abs(row-5) < 2));
    }
    public MyArrayList<Trade> getTrades(){
        return trades;
    }
    public void recheckTrades(){
        int i = 0;
        while(i < trades.size()){
            if(trades.get(i).checkTrade()){
                trades.remove(i);
                i--;
            }
            i++;
        }
    }
    public boolean makeTrade(int index2, int[] resources1, int[] resources2, boolean agree1, boolean agree2){
        Player play1 = players[selected];
        Player play2 = players[index2];
        int[] play1res = play1.getResources();
        for(int i = 0; i < 5; i++){
            if(play1res[i]-resources1[i] < 0){
                return false;
            }
        }
        trades.add(new Trade(selected, index2, resources1, resources2, players[selected], players[index2], agree1, agree2));
        return true;
    }
    public int portCheck(int r, int c) {
        int[][][] allPorts = {port1, port2, port3, port4, port5, port6, port7, port8, port9};
        for (int i = 0; i < allPorts.length; i++) {
            int[][] port = allPorts[i];
            for (int j = 0; j < port.length; j++) {
                if (port[j][0] == r && port[j][1] == c) {
                    return ports[i];
                }
            }
        }
        return -1;
    }
    public boolean canStealFrom(int r, int c){
        for(int i = 0; i < settlements.size(); i++){
            MyArrayList<Tile> tempTiles = settlements.get(i).getTiles();
            for(int j = 0; j < tempTiles.size(); j++){
                if(tempTiles.get(j).getR() == r && tempTiles.get(j).getC() == c){
                    return true;
                }
            }
        }
        return false;
    }
    public void roadLengths() {
        int maxLength = 0;
        int maxPlayer = -1;


        int[] roadCounts = new int[players.length];
        for (int i = 0; i < roads.size(); i++) {
            Integer[] road = roads.get(i);
            int player = road[4];
            roadCounts[player]++;
        }
        roadLengths = roadCounts;

        for (int player = 0; player < players.length; player++) {
            if (roadCounts[player] > maxLength) {
                maxLength = roadCounts[player];
                maxPlayer = player;
            }
        }


        if (maxLength > currentLongestRoadsCount) {
            currentLongestRoadsCount = maxLength;
            currentLongestRoads = maxPlayer;
        }
    }

    // public void roadLengths() {
    //     int maxLength = 0;
    //     int maxPlayer = -1;

    //     for (int player = 0; player < players.length; player++) {

    //         MyArrayList<Integer[]> playerRoads = new MyArrayList<>();
    //         for (int i = 0; i < roads.size(); i++) {
    //             Integer[] road = roads.get(i);
    //             if (road[4] == player) {
    //                 playerRoads.add(road);
    //             }
    //         }


    //         for (int i = 0; i < playerRoads.size(); i++) {
    //             Integer[] road = playerRoads.get(i);

    //             int len1 = dfsRoad(playerRoads, road[0], road[1], i, new boolean[playerRoads.size()]);
    //             int len2 = dfsRoad(playerRoads, road[2], road[3], i, new boolean[playerRoads.size()]);
    //             int longest = Math.max(len1, len2);
    //             roadLengths[i] = longest;
    //             if (longest > maxLength) {
    //                 maxLength = longest;
    //                 maxPlayer = player;
    //             }
    //         }
    //     }


    //     if (maxLength > currentLongestRoadsCount) {
    //         currentLongestRoadsCount = maxLength;
    //         currentLongestRoads = maxPlayer;
    //     }
    // }


    // private int dfsRoad(MyArrayList<Integer[]> roads, int r, int c, int usedIdx, boolean[] used) {
    //     used[usedIdx] = true;
    //     int max = 1;
    //     for (int i = 0; i < roads.size(); i++) {
    //         if (!used[i]) {
    //             Integer[] road = roads.get(i);

    //             if ((road[0] == r && road[1] == c)) {
    //                 max = Math.max(max, 1 + dfsRoad(roads, road[2], road[3], i, copyUsed(used)));
    //             } else if ((road[2] == r && road[3] == c)) {
    //                 max = Math.max(max, 1 + dfsRoad(roads, road[0], road[1], i, copyUsed(used)));
    //             }
    //         }
    //     }
    //     return max;
    // }


    // private boolean[] copyUsed(boolean[] used) {
    //     boolean[] copy = new boolean[used.length];
    //     for (int i = 0; i < used.length; i++) {
    //         copy[i] = used[i];
    //     }
    //     return copy;
    // }



}