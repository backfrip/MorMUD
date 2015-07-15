package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Connection extends Thread {
    private Socket socket;
    private BufferedReader in;
    @SuppressWarnings("unused")
    private DataOutputStream out;
    private boolean go;

    public Connection(Socket client) throws Exception {
	System.out.println("[INFO] New connection from "
		+ client.getRemoteSocketAddress().toString());

	socket = client;
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
    }
}
