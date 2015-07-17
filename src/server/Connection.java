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

    public Connection(Socket client, int i) throws Exception {
	socket = client;
	num = i;
	id = "CON" + i;

	Main.printInfo(id, "New connection from ("
		+ client.getRemoteSocketAddress().toString().substring(1) + ")");

	in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	out = new DataOutputStream(socket.getOutputStream());
	go = true;

	start();
    }

    @Override
    public void run() {
	while (go) {
	    try {
		String read = in.readLine();
		if (read != null)
		    System.out.println("[MESSAGE] " + read);
		else
		    go = false;
	    } catch (IOException e) {
		go = false;
		e.printStackTrace();
	    }
	}
	System.out.println("[INFO] Remote host ended connection.");
	try {
	    socket.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	status = false;
    }

    public void dispose() {
	go = false;
	interrupt();
    }

    public boolean isActive() {
	return status;
    }
}
