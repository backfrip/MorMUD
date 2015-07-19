package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import main.Main;

public class Connection extends Thread {
    private Socket socket;
    private BufferedReader in;
    private DataOutputStream out;
    private boolean go, status = true;
    private int num;
    private String id;

    public Connection(Socket client, int i) {
	socket = client;
	num = i;
	id = "CON" + i;

	Main.printInfo(id, "New connection from ("
		+ socket.getRemoteSocketAddress().toString().substring(1) + ")");

	try {
	    in = new BufferedReader(new InputStreamReader(
		    socket.getInputStream()));
	    out = new DataOutputStream(socket.getOutputStream());
	} catch (IOException e) {
	    Main.printAlert(id, "Error getting client input/output streams!");
	    e.printStackTrace();
	}

	start();
    }

    @Override
    public void run() {
	for (go = true; go;) {
	    try {
		String read = in.readLine();
		if (read != null)
		    Main.printInfo(id, "New input.");
		else
		    go = false;
	    } catch (IOException e) {
		Main.printAlert(id,
			"Client socket connection error. (This may be intentional.)");
		go = false;
	    }
	}
	try {
	    socket.close();
	} catch (IOException e) {
	    Main.printAlert(id, "Error closing client socket!");
	    e.printStackTrace();
	}
	status = false;
	Main.printInfo(id, "Connection closed successfully.");
    }

    public boolean dispose() {
	go = false;
	interrupt();
	try {
	    socket.close();
	} catch (IOException e) {
	    Main.printInfo(id, "Client socket forcibly closed.");
	}
	while (isAlive())
	    interrupted();
	return true;
    }

    public boolean isActive() {
	return status;
    }
}
