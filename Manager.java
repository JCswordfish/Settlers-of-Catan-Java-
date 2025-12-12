public class Manager{
    MyArrayList<ServerThread> connections;
    String[] usernames;
    int readyint;
    boolean ready;
    Game game;

    public Manager(){
        connections = new MyArrayList<ServerThread>();
        readyint = 0;
        ready = false;

    }
    public void add(ServerThread newThread){
        connections.add(newThread);
        newThread.setIndex(connections.size()-1);
        usernames = new String[connections.size()];
    }
    public void broadcast(Object o){
        for(int i = 0; i < connections.size(); i++){
            connections.get(i).send(o);
        }
    }
    public boolean ready(){
        return ready;

    }
    public int getConnections(){
        return connections.size();
    }
    public void addReady(String username, int index){
        readyint ++;
        usernames[index] = username;
        if(readyint == connections.size()){
            ready = true;
            game = new Game(connections.size(), usernames);
            broadcast(game);
        }
    }
    public void reset(){
        game = new Game(connections.size(), usernames);
        broadcast(game);
    }
}
