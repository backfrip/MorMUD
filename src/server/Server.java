package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Iterator;

import main.Main;

public class Server extends Thread {
    private ServerSocket socket;
    private final String id = "SERVER";
    private boolean go;
    private ArrayList<Connection> connections;
    private int counter;

    public Server(int port) {
	Main.printInfo(id, "Starting a new server on port " + port + "...");
	try {
	    socket = new ServerSocket(port);
	} catch (IOException e) {
	    Main.printAlert(id, "Error starting server!");
	    e.printStackTrace();
	}
	if (socket.isBound()) {
	    Main.printInfo(id, "Server started successfully!");
	    connections = new ArrayList<Connection>();
	    counter = 0;
	    start();
	}
    }

    @Override
    public void run() {
	for (go = true; go;) {
	    Main.printInfo(id, "Listening for new connections...");
	    Connection con;
	    try {
		con = new Connection(socket.accept(), counter);
		connections.add(con);
		counter++;
	    } catch (IOException e) {
		Main.printAlert(id,
			"Server socket connection error. (This may be intentional.)");
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}
	Main.printInfo(id, "Clearing active connections...");
	clearConnections();
	Main.printInfo(id, "Server shut down successfully!");
    }

    public boolean dispose() {
	go = false;
	interrupt();
	try {
	    socket.close();
	} catch (IOException e) {
	    Main.printInfo(id, "Server socket forcibly closed.");
	}
	while (isAlive())
	    interrupted();
	return true;
    }

    public void clearConnections() {
	Iterator<Connection> clearer = connections.iterator();
	Connection c;
	while (clearer.hasNext()) {
	    c = clearer.next();
	    c.dispose();
	    clearer.remove();
	}
    }
}
