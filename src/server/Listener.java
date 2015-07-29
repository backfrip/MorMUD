package server;

import java.io.IOException;
import java.net.ServerSocket;

import main.Main;

public class Listener extends Thread {
    private ServerSocket socket;
    private Server server;
    private final String id = "SERVER";
    private boolean go;

    public Listener(Server s, int port) throws IOException {
	server = s;
	socket = new ServerSocket(port);
	start();
    }

    public boolean isBound() {
	return socket.isBound();
    }

    @Override
    public void run() {
	for (go = true; go;) {
	    Main.printInfo(id, "Listening for new connections...");
	    try {
		server.newConnection(socket.accept());
	    } catch (IOException e) {
		Main.printAlert(id,
			"Server socket connection error. (This may be intentional.)");
	    }
	}
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

}
