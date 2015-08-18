package main;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

import object.GameObject;

public abstract class DefaultWriter {
    private static final String id = "DEFAULTWRITER";

    public static void check() {
	if (Files.notExists(Storage.getFilepath("data", "users.mmd"))) {
	    Main.printInfo(id, "Creating users file...");
	    writeDefaultUsers();
	}
	try {
	    Files.createDirectories(Storage.getDirpath("data/objects"));
	} catch (IOException e) {
	    Main.printAlert(id, "Error creating directory 'data/objects/'!");
	}
	if (Files.notExists(Storage.getFilepath("data/objects", "0.mmd"))) {
	    Main.printInfo(id, "Creating '0.mmd'");
	    writeDefaultStartroom();
	}
    }

    public static void writeDefaultUsers() {
	try {
	    ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(Storage.getFile("data", "users.mmd")));
	    out.writeObject(new ConcurrentHashMap<String, String>());
	    out.close();
	} catch (IOException e) {
	    Main.printAlert(id, "Error writing to file 'users.mmd'!");
	}
    }

    public static void writeDefaultStartroom() {
	try {
	    LinkedList<String[]> data = new LinkedList<String[]>();
	    data.add(new String[] { "properties", "id", "0" });
	    data.add(new String[] { "properties", "isRoom", "1" });
	    data.add(new String[] { "properties", "isContainer", "1" });
	    data.add(new String[] { "messages", "name", "Start Room" });
	    data.add(new String[] { "messages", "desc", "This is the starting room! Take a gooooood look." });
	    ObjectOutputStream out = new ObjectOutputStream(
		    new FileOutputStream(Storage.getFile("data/objects", "0.mmd")));
	    out.writeObject(data);
	    out.close();
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	    Main.shutdown();
	} catch (IOException e) {
	    e.printStackTrace();
	    Main.shutdown();
	}
    }
}
