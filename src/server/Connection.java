package server;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import language.Formatter;
import language.Parser;
import main.Data;
import main.Main;

public class Connection extends Thread {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private boolean go;
    private int num;
    private String id, read;
    private User user;

    public Connection(Socket client, int i) {
	socket = client;
	num = i;
	id = "CON" + i;
	user = null;

	try {
	    in = new BufferedReader(new InputStreamReader(
		    socket.getInputStream()));
	    out = new PrintWriter(new BufferedOutputStream(
		    socket.getOutputStream()), true);
	} catch (IOException e) {
	    Main.printAlert(id, "Error getting client input/output streams!");
	    e.printStackTrace();
	}

	start();
    }

    @Override
    public void run() {
	send(Parser.welcome());
	initializeUser();
	if (user != null) {
	    send(Parser.parse("l", user));
	    for (go = true; go;) {
		send("`sprompt!>");
		try {
		    read = in.readLine();
		    if (read != null && !read.equalsIgnoreCase("quit")
			    && !read.equalsIgnoreCase("exit")) {
			send(Parser.parse(read, user));
		    } else {
			send("`5Logging you out...");
			go = false;
		    }
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
	}
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

    public void send(String s) {
	out.println("\n" + Formatter.format(s, user));
	if (out.checkError())
	    Main.printAlert(id, "Error sending message to client!");
    }

    public boolean isActive() {
	try {
	    return socket.getInetAddress().isReachable(0);
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return false;
    }

    public boolean isConnected() {
	return socket.isInputShutdown();
    }

    public boolean initialized() {
	return user != null;
    }

    public int getNum() {
	return num;
    }

    public User getUser() {
	return user;
    }

    private void initializeUser() {
	loop: while (user == null) {
	    send("`sUsername:");
	    String u;
	    try {
		u = in.readLine();
	    } catch (IOException e) {
		return;
	    }
	    send("`sPassword:");
	    String p;
	    try {
		p = in.readLine();
	    } catch (IOException e) {
		return;
	    }
	    if (u == null || p == null)
		break loop;
	    if (Data.checkUser(u, p)) {
		send("`5Logging you in...");
		user = new User(u);
	    } else {
		send("`5Username and password not recognized! Creating new user...");
		Data.addUser(u, p);
	    }
	}
    }
}
