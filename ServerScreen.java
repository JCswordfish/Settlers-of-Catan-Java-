import javax.swing.*;
import java.awt.*;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;



import java.io.*;
import java.net.*;
import java.util.*;


public class ServerScreen extends JPanel{


    private Manager manager;



	public ServerScreen(){
        manager = new Manager();
		this.setLayout(null);
		this.setFocusable(true);

		
    }
	
	


	public Dimension getPreferredSize() {


		return new Dimension(1024,1024);
	}


	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		try {
            g.drawString("IP: " + InetAddress.getLocalHost().getHostAddress(),100,100);
			g.drawString(manager.getConnections() + " Users ", 100, 200);
			repaint();
        } catch (UnknownHostException ex) {
            System.out.println("Could not find IP address for this host");
        }




		


	} 


	public void startServer() throws IOException {


		// try {
		int portNumber = 1024;
		ServerSocket server = new ServerSocket(portNumber);
		
		while(true){
			System.out.println("Waiting for a connection");

			//Wait for a connection.
			Socket socket = server.accept();
			

			//Once a connection is made, run the socket in a ServerThread.
			ServerThread sThread = new ServerThread(socket, manager);
			Thread thread = new Thread(sThread);
			manager.add(sThread);
			thread.start();
		}
		// }
		// } catch (UnknownHostException e) {
		// 	System.err.println("Host unkown: " + hostName);
		// 	System.exit(1);
		// } catch (IOException e) {
		// 	System.err.println("Couldn't get I/O for the connection to " + hostName);
		// 	System.exit(1);
		// }


		//This loop will run and wait for one connection at a time.
		


	}






}
