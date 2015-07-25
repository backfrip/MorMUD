package main;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import language.Parser;
import server.Server;

public class Main {
    private static Server server;
    private static Properties properties;
    private static boolean go;

    /**
     * Handler method for MorMUD.
     * 
     * @param args
     *            Arguments given by command line.
     */
    public static void main(String[] args) {
	properties = new Properties();
	try {
	    printInfo("Attempting to load properties from 'main.properties'...");
	    loadProperties();
	} catch (FileNotFoundException e) {
	    printAlert("File 'main.properties' not found!");
	    printInfo("Creating new default properties file...");
	    try {
		writeDefaultProperties();
	    } catch (IOException ioe) {
		printAlert("Error creating file 'main.properties'!");
		printAlert("Ending execution...");
		ioe.printStackTrace();
		System.exit(1);
	    }
	} catch (IOException e) {
	    printAlert("Error reading file 'main.properties'!");
	    printAlert("Ending execution...");
	    e.printStackTrace();
	    System.exit(1);
	}
	try {
	    printInfo("Attempting to start the server...");
	    server = new Server(
		    Integer.parseInt(properties.getProperty("port")));
	} catch (Exception e) {
	    printAlert("Error starting server!");
	    printAlert("Ending execution...");
	    e.printStackTrace();
	    System.exit(1);
	}
	BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	String read;
	printInfo("Now listening for console input...");
	for (go = true; go;) {
	    try {
		read = in.readLine().trim();
		Parser.parseConsole(read);
	    } catch (IOException e) {
		printAlert("Error reading System input!");
		e.printStackTrace();
	    }
	}
	printInfo("Shutting down server...");
	server.dispose();
	printInfo("Ending execution...");
    }

    /**
     * Prints an info update to the system output.
     * 
     * @param s
     *            The String to be printed.
     */
    private static void printInfo(String s) {
	System.out.println("[INFO] " + s);
    }

    /**
     * Prints an alert update to the system output.
     * 
     * @param s
     *            The String to be printed.
     */
    private static void printAlert(String s) {
	System.out.println("[ALERT] " + s);
    }

    /**
     * Attempts to load application properties from the file 'main.properties'.
     */
    private static void loadProperties() throws IOException {
	FileInputStream file = new FileInputStream("./main.properties");
	properties.load(file);
	file.close();
    }

    /**
     * Writes a new default 'main.properties' file.
     */
    private static void writeDefaultProperties() throws IOException {
	properties.setProperty("port", "5000");
	FileOutputStream file = new FileOutputStream("./main.properties");
	properties.store(file, null);
	file.close();
	printInfo("Default properties file created successfully!");
    }

    /**
     * Allows other objects to print info updates to the system output.
     * 
     * @param id
     *            An identifier for the object in question.
     * @param s
     *            The String to be printed.
     */
    public static void printInfo(String id, String s) {
	printInfo("<" + id + "> " + s);
    }

    /**
     * Allows other objects to print alert updates to the system output.
     * 
     * @param id
     *            An identifier for the object in question.
     * @param s
     *            The String to be printed.
     */
    public static void printAlert(String id, String s) {
	printAlert("<" + id + "> " + s);
    }

    public static void shutdown() {
	go = false;
    }
}
