package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import language.Parser;
import server.Server;

/**
 * The MorMUD Main class.
 * 
 * @author E Stringham
 *
 */
public class Main {
    private static int state;
    private static Server server;


    //   \  |   \  _ _|  \ |
    //  |\/ |  _ \   |  .  |
    // _|  _|_/  _\___|_|\_|


    /**
     * Main handler method for MorMUD.
     * 
     * @param args
     *            Arguments given by command line.
     */
    public static void main(String[] args) {
	printInfo("MorMUD Version 0.2.2");
	state = 0;
	Data.initialize();
	if (state != 0)
	    System.exit(1);

	printInfo("Attempting to start the server...");
	server = new Server(Integer.parseInt(Data.getProperty("port")));

	BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	String read;

	printInfo("Now listening for console input...");
	while (state == 0) {
	    try {
		read = in.readLine().trim();

		Parser.parseConsole(read);
	    } catch (IOException e) {
		printAlert("Error reading System input!");
	    }
	}

	printInfo("Shutting down server...");
	server.shutdown();
	if (state == -1) {
	    Data.clean();
	    printInfo("Restarting execution...");
	    main(args);
	} else {
	    Data.save();
	    printInfo("Ending execution...");
	}
    }

    /**
     * Ends MorMUD execution.
     */
    public static void shutdown() {
	state = 1;
    }

    public static void wipe() {
	state = -1;
    }

    public static boolean isActive() {
	return state == 0;
    }


    //  |     _ \   __|  __|_ _|  \ |  __|
    //  |    (   | (_ | (_ |  |  .  | (_ |
    // ____|\___/ \___|\___|___|_|\_|\___|


    /**
     * Prints an information update to the system output.
     * 
     * @param s
     *            The String to be printed.
     */
    private static void printInfo(String s) {
	System.out.println("[INFO] " + s);
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
     * Prints an alert update to the system output.
     * 
     * @param s
     *            The String to be printed.
     */
    private static void printAlert(String s) {
	System.out.println("[ALERT] " + s);
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

    private static void printFatalAlert(String s) {
	System.err.println("[ALERT] " + s);
    }

    public static void printFatalAlert(String id, String s) {
	printFatalAlert("<" + id + "> " + s);
    }

}
