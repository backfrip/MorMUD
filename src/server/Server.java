package server;

import java.net.ServerSocket;

public class Server extends Thread {
    ServerSocket socket;

    public Server() throws Exception {
	System.out.println("[INFO] Starting a new server on port 5000...");
	socket = new ServerSocket(5000);
	System.out.println("[INFO] Server started successfully.");

	start();
    }

    @Override
    public void run() {
	while (true) {
	    System.out.println("[INFO] Listening for new sockets...");
	    try {
		@SuppressWarnings("unused")
		Connection con = new Connection(socket.accept());
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}
    }

}
