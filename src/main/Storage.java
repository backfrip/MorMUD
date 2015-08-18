package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

import object.GameObject;

public class Storage {
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private ConcurrentHashMap<String, String> users;
    private ConcurrentHashMap<Integer, GameObject> objects;
    private GameObject startroom;
    private final String id = "STORAGE";

    public Storage() {
	loadUsers();
	objects = new ConcurrentHashMap<Integer, GameObject>();
	loadObjects();
	startroom = objects.get(0);
    }

    public static Path getDirpath(String dirpath) {
	return FileSystems.getDefault().getPath(dirpath);
    }

    public static Path getFilepath(String dirpath, String filename) {
	return FileSystems.getDefault().getPath(dirpath, filename);
    }

    public static File getFile(String dirpath, String filename) {
	return getFilepath(dirpath, filename).toFile();
    }

    @SuppressWarnings("unchecked")
    private void loadUsers() {
	try {
	    in = new ObjectInputStream(new FileInputStream(getFile("data", "users.mmd")));
	    users = (ConcurrentHashMap<String, String>) in.readObject();
	    in.close();
	} catch (IOException e) {
	    Main.printAlert(id, "Error reading from file 'data/users.mmd'!");
	    Main.printAlert(id, "Ordering shutdown!");
	    Main.shutdown();
	} catch (ClassNotFoundException e) {
	    Main.printAlert(id, "Invalid 'data/users.mmd' file!");
	    Main.printAlert(id, "Ordering shutdown!");
	    Main.shutdown();
	}
    }

    @SuppressWarnings("unchecked")
    private void loadObjects() {
	try {
	    for (Path path : Files.newDirectoryStream(getDirpath("data/objects"))) {
		in = new ObjectInputStream(new FileInputStream(path.toFile()));
		LinkedList<String[]> data = (LinkedList<String[]>) in.readObject();
		GameObject objectData = new GameObject(data);
		objects.put(objectData.getID(), objectData);
	    }
	} catch (IOException e) {
	    Main.printAlert(id, "Error reading from directory 'data/objects/'!");
	    Main.printAlert(id, "Ordering shutdown!");
	    e.printStackTrace();
	    Main.shutdown();
	} catch (ClassNotFoundException e) {
	    Main.printAlert(id, "Found invalid object file!");
	    Main.printAlert(id, "Ordering shutdown!");
	    Main.shutdown();
	}
    }

    private void writeUsers() {
	try {
	    out = new ObjectOutputStream(new FileOutputStream(getFile("data", "users.mmd")));
	    out.writeObject(users);
	    out.close();
	} catch (IOException e) {
	    Main.printAlert(id, "Error writing users to 'data/users.mmd'!");
	}
    }

    public boolean checkUser(String u, String p) {
	if (users.containsKey(u))
	    if (users.get(u).equals(p))
		return true;
	return false;
    }

    public void addUser(String u, String p) {
	if (!users.containsKey(u))
	    users.put(u, p);
    }

    public GameObject getObject(int id) {
	return objects.get(id);
    }

}
