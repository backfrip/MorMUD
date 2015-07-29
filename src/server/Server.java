package server;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

import main.Main;

public class Server extends Thread {
    private Listener listener;
    private final String id = "SERVER";
    private boolean go;
    private ArrayList<Connection> connections;
    private int counter;

    public Server(int port) {
	Main.printInfo(id, "Attempting to start a new server on port " + port
		+ "...");
	try {
	    listener = new Listener(this, port);
	} catch (IOException e) {
	    Main.printAlert(id, "Error starting server!");
	    e.printStackTrace();
	}
	if (listener.isBound()) {
	    Main.printInfo(id, "Server started successfully!");
	    connections = new ArrayList<Connection>();
	    counter = 0;
	    start();
	} else {
	    Main.printAlert(id,
		    "Unknown error verifying server startup, shutting down...");
	    listener.dispose();
	    clearConnections();
	}
    }

    @Override
    public void run() {
	for (go = true; go;) {
	}
	Main.printInfo(id, "Stopping listener...");
	listener.dispose();
	Main.printInfo(id, "Clearing active connections...");
	clearConnections();
	Main.printInfo(id, "Server shut down successfully!");
    }

    public boolean dispose() {
	go = false;
	interrupt();
	while (isAlive())
	    interrupted();
	return true;
    }

    public void newConnection(Socket client) {
	Main.printInfo(id, "New connection from ("
		+ client.getRemoteSocketAddress().toString().substring(1) + ")");
	Connection con = new Connection(client, counter);
	connections.add(con);
	counter++;
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
