package main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import server.Server;

public class Main {
    private static Server server;
    private static Properties properties;

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
	    server = new Server();
	} catch (Exception e) {
	    printAlert("Error starting server!");
	    printAlert("Ending execution...");
	    e.printStackTrace();
	    System.exit(1);
	}
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
     * Attempts to load application properties from the file main.properties.
     */
    private static void loadProperties() throws IOException {
	FileInputStream file = new FileInputStream("./main.properties");
	properties.load(file);
	file.close();
    }

    /**
     * 
     */
    private static void writeDefaultProperties() throws IOException {
	properties.setProperty("port", "5000");
	FileOutputStream file = new FileOutputStream("./main.properties");
	properties.store(file, null);
	file.close();
    }

}
