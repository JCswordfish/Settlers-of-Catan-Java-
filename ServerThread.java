import java.net.*;
import java.io.*;

public class ServerThread implements Runnable{	
	private Socket socket;
    private Object o;
    private Manager manager;
    ObjectOutputStream out;
    ObjectInputStream in;
    PushbackInputStream pin;
	private String username;
	boolean ready;
	int index = -2;
	public ServerThread(Socket socket, Manager manager){
		this.socket = socket;
        this.manager = manager; 
	}

	public void run(){
		System.out.println(Thread.currentThread().getName() + ": connection opened.");
		try{

			out = new ObjectOutputStream(socket.getOutputStream());
			pin = new PushbackInputStream(socket.getInputStream());
			in = new ObjectInputStream(socket.getInputStream());
			ready = false;
			
			username = (String)in.readObject();
			ready = true;
			manager.addReady(username, index);
			out.reset();
			out.writeObject(index);
			System.out.println(index);
			while(true){
				
				System.out.println("index " + index + " waiting");

				if(manager.ready()){

					System.out.println("waiting for object" + index);
					try{

						Object obj = in.readObject();
						System.out.println("object recieved");
						if(obj instanceof String){
    						manager.reset();
						} else {
							manager.broadcast(obj);
							System.out.println("broadcast");

						}

					} catch (UnknownHostException e) {
						System.err.println("Host unkown: ");
						System.exit(1);
					} catch (IOException e) {
						System.err.println("Couldn't get I/O for the connection to ");
						System.exit(1);
					} catch (ClassNotFoundException e){
						System.err.println("no class");
						System.exit(1);
					}
					
				}
			}
			
			
			
			//Clears and close the output stream.
			// out.flush();
			// out.close();
			// System.out.println(Thread.currentThread().getName() + ": connection closed.");
		} catch (IOException ex){
			System.out.println("Error listening for a connection");
			System.out.println(ex.getMessage());
		} catch (ClassNotFoundException ex){
			System.err.println("no class");
			System.exit(1);
		}
	}
    public void send(Object o){
        try{
			System.out.println("send");
			out.reset();
            out.writeObject(o);
            

        } catch (UnknownHostException e) {
			System.err.println("Host unkown: ");
			System.exit(1);
		} catch (IOException e) {
			System.err.println(e);
			System.exit(1);
		}
        
    }
	public boolean ready(){
		return ready;
	}
	public void setIndex(int index){
		this.index = index;

	}
}