package main;

import java.io.EOFException;
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
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import object.GameObject;

/**
 * Handles reading and writing of MorMUD data to disk.
 * 
 * @author E Stringham
 * 
 */
public abstract class Data {
    private static ObjectInputStream in;
    private static ObjectOutputStream out;
    private static Properties properties;
    private static ConcurrentHashMap<String, String> users;
    private static ConcurrentHashMap<Integer, GameObject> objects;
    private final static String id = "DATA";
    private final static File propertyFile = getFile(".", "main.properties");
    private final static File userFile = getFile("data", "users.mmd");


    //   __| __|  \ | __| _ \   \   |   
    //  (_ | _|  .  | _|    /  _ \  |   
    // \___|___|_|\_|___|_|_\_/  _\____|


    /**
     * Method for initializing MorMUD data.
     */
    public static void initialize() {
	File data = getDir("data/objects");
	if (!Files.exists(data.toPath()))
	    data.mkdirs();
	if (Main.isActive())
	    loadProperties();
	if (Main.isActive())
	    loadUsers();
	if (Main.isActive())
	    loadObjects();
    }

    /**
     * Attempts to save relevant MorMUD data.
     * 
     * @return True.
     */
    public static boolean save() {
	writeProperties();
	writeUsers();
	writeObjects();
	return true;
    }

    /**
     * Deletes any relevant storage data.
     */
    public static boolean clean() {
	Main.printInfo(id, "Attempting to delete relevant storage data...");
	try {
	    for (Path object : Files
		    .newDirectoryStream(getDirpath("data/objects"))) {
		Files.deleteIfExists(object);
		Main.printInfo(id, "Deleting object " + object.toString()
			+ "...");
	    }
	    Main.printInfo(id, "Deleting 'main.properties'...");
	    Files.deleteIfExists(propertyFile.toPath());
	    Main.printInfo(id, "Deleting 'users.mmd'...");
	    Files.deleteIfExists(userFile.toPath());
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return true;
    }

    //  _ \ _ \  _ \  _ \ __| _ \__ __|_ _| __|  __|
    //  __/   / (   | __/ _|    /   |    |  _| \__ \
    // _|  _|_\\___/ _|  ___|_|_\  _|  ___|___|____/


    /**
     * Attempts to load the properties from 'main.properties'. Creates default
     * properties if 'main.properties' does not exist.
     */
    public static void loadProperties() {
	Main.printInfo(id,
		"Attempting to load properties from 'main.properties'...");
	properties = new Properties();
	if (!propertyFile.exists()) {
	    Main.printInfo(id, "File 'main.properties' not found!");
	    Main.printInfo(id, "Attempting to create 'main.properties'...");
	    properties.setProperty("port", "5000");
	    writeProperties();
	}
	try {
	    FileInputStream i = new FileInputStream(propertyFile);
	    properties.load(i);
	    i.close();
	} catch (IOException e) {
	    Main.printFatalAlert(id,
		    "Error reading properties from 'main.properties'!");
	    Main.shutdown();
	}
	if (Main.isActive())
	    Main.printInfo(id, "Properties loaded successfully!");
    }

    /**
     * Attempts to write the properties to 'main.properties'.
     */
    public static void writeProperties() {
	Main.printInfo(id,
		"Attempting to write properties to 'main.properties'...");
	try {
	    FileOutputStream o = new FileOutputStream(propertyFile);
	    properties.store(o, null);
	    o.close();
	} catch (IOException e) {
	    Main.printFatalAlert(id,
		    "Error writing properties to 'main.properties'!");
	    Main.shutdown();
	}
	if (Main.isActive())
	    Main.printInfo(id, "Properties written sucessfully!");
    }

    /**
     * Provides a MorMUD property.
     * 
     * @param s
     *            The name of the property.
     * @return The value of the property.
     */
    public static String getProperty(String s) {
	return properties.getProperty(s);
    }


    //  |  |  __| __| _ \  __|
    //  |  |\__ \ _|    /\__ \
    // \__/ ____/___|_|_\____/


    /**
     * Attempts to load the users from 'data/users.mmd'.
     */
    @SuppressWarnings("unchecked")
    public static void loadUsers() {
	Main.printInfo(id, "Attempting to load users from 'data/users.mmd'...");
	if (!userFile.exists()) {
	    Main.printInfo(id, "File 'data/users.mmd' not found!");
	    Main.printInfo(id, "Attempting to create 'data/users.mmd'...");
	    users = new ConcurrentHashMap<String, String>();
	    writeUsers();
	}
	try {
	    in = new ObjectInputStream(new FileInputStream(userFile));
	    users = (ConcurrentHashMap<String, String>) in.readObject();
	    in.close();
	} catch (ClassNotFoundException | EOFException e) {
	    Main.printAlert(id, "Invalid users file!");
	    Main.printInfo(id, "Creating new clean users file.");
	    users = new ConcurrentHashMap<String, String>();
	} catch (IOException e) {
	    Main.printFatalAlert(id,
		    "Error reading users from 'data/users.mmd'!");
	    Main.shutdown();
	}
	if (Main.isActive())
	    Main.printInfo(id, "Users loaded successfully!");
    }

    /**
     * Attempts to write the users to 'data/users.mmd'. Creates a new
     * 'data/users.mmd' if one is not found.
     */
    public static void writeUsers() {
	Main.printInfo(id, "Attempting to write users to 'data/users.mmd'...");
	try {
	    out = new ObjectOutputStream(new FileOutputStream(userFile));
	    out.writeObject(users);
	    out.close();
	} catch (IOException e) {
	    Main.printFatalAlert(id, "Error writing users to 'data/users.mmd'!");
	    Main.shutdown();
	}
	if (Main.isActive())
	    Main.printInfo(id, "Users written successfully!");
    }

    public static void addUser(String u, String p) {
	users.put(u, p);
	Main.printInfo(id, "Added new user '" + u + "'.");
    }

    public static boolean checkUser(String u, String p) {
	return users.containsKey(u) && users.get(u).equals(p);
    }


    //   _ \  _ )    | __|  __|__ __| __|
    //  (   | _ \ \  | _|  (      | \__ \
    // \___/ ___/\__/ ___|\___|  _| ____/


    /**
     * Attempts to load the objects from the 'data/objects' directory. Creates a
     * new default room if one is not found.
     */
    public static void loadObjects() {
	Main.printInfo(id, "Attempting to load objects from 'data/objects'...");
	objects = new ConcurrentHashMap<Integer, GameObject>();
	try {
	    for (Path path : Files
		    .newDirectoryStream(getDirpath("data/objects"))) {
		in = new ObjectInputStream(new FileInputStream(path.toFile()));
		try {
		    @SuppressWarnings("unchecked")
		    LinkedList<String[]> data = (LinkedList<String[]>) in
			    .readObject();
		    GameObject object = new GameObject(data);
		    addObject(object);
		} catch (ClassNotFoundException | EOFException e) {
		    Main.printAlert(id, "'" + path.toString()
			    + "' is an invalid object file!");
		}
		in.close();
	    }
	} catch (IOException e) {
	    Main.printFatalAlert(id,
		    "Error reading from directory 'data/objects'!");
	    e.printStackTrace();
	    Main.shutdown();
	}
	if (!objects.containsKey(0) && Main.isActive()) {
	    Main.printInfo(id, "Creating new default room.");
	    LinkedList<String[]> defaultData = new LinkedList<String[]>();
	    defaultData.add(new String[] { "properties", "id", "0" });
	    defaultData.add(new String[] { "messages", "desc",
		    "This is the default starting room. Take a good look!" });
	    defaultData.add(new String[] { "messages", "title",
		    "`2Starting Room`c" });
	    defaultData.add(new String[] { "properties", "isContainer", "1" });
	    defaultData.add(new String[] { "properties", "canAdorn", "1" });
	    objects.put(0, new GameObject(defaultData));
	}
	if (Main.isActive())
	    Main.printInfo(id, "Objects loaded successfully!");
    }

    /**
     * Attempts to write the objects to the 'data/objects' directory.
     */
    public static void writeObjects() {
	Main.printInfo(id, "Attempting to write objects to 'data/objects'...");
	try {
	    for (GameObject object : objects.values()) {
		out = new ObjectOutputStream(new FileOutputStream(getFile(
			"data/objects", object.getID() + ".mmd")));
		out.writeObject(object.getData());
		out.close();
	    }
	} catch (IOException e) {
	    Main.printFatalAlert(id,
		    "Error writing to directory 'data/objects'!);");
	    Main.shutdown();
	}
	if (Main.isActive())
	    Main.printInfo(id, "Objects written successfully!");
    }

    public static void addObject(GameObject o) {
	objects.put(o.getID(), o);
    }

    public static GameObject getObject(int id) {
	return objects.get(id);
    }


    //  |  | __| |    _ \ __| _ \
    //  __ | _|  |    __/ _|    /
    // _| _|___|____|_|  ___|_|_\


    private static File getFile(String dirpath, String filename) {
	return getFilepath(dirpath, filename).toFile();
    }

    private static File getDir(String dirpath) {
	return getDirpath(dirpath).toFile();
    }

    private static Path getFilepath(String dirpath, String filename) {
	return FileSystems.getDefault().getPath(dirpath, filename);
    }

    private static Path getDirpath(String dirpath) {
	return FileSystems.getDefault().getPath(dirpath);
    }

}
