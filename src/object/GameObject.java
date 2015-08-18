package object;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

import main.Main;

public class GameObject {
    private ConcurrentHashMap<String, GameObject> parts, contents, adornments;
    private ConcurrentHashMap<String, String> messages;
    private ConcurrentHashMap<String, Integer> properties;
    private String[] keywords;
    private int id;

    public GameObject(LinkedList<String[]> save) {
	ConcurrentHashMap<String, GameObject> tparts = new ConcurrentHashMap<String, GameObject>();
	ConcurrentHashMap<String, GameObject> tcontents = new ConcurrentHashMap<String, GameObject>();
	ConcurrentHashMap<String, GameObject> tadornments = new ConcurrentHashMap<String, GameObject>();
	messages = new ConcurrentHashMap<String, String>();
	properties = new ConcurrentHashMap<String, Integer>();
	keywords = new String[0];
	for (String[] parcel : save) {
	    if (parcel[0].equals("parts"))
		tparts.put(parcel[1], Main.storage.getObject(Integer.parseInt(parcel[2])));
	    else if (parcel[0].equals("contents"))
		tcontents.put(parcel[1], Main.storage.getObject(Integer.parseInt(parcel[2])));
	    else if (parcel[0].equals("adornments"))
		tadornments.put(parcel[1], Main.storage.getObject(Integer.parseInt(parcel[2])));
	    else if (parcel[0].equals("messages"))
		messages.put(parcel[1], parcel[2]);
	    else if (parcel[0].equals("properties"))
		properties.put(parcel[1], Integer.parseInt(parcel[2]));
	    else if (parcel[0].equals("keywords"))
		addKeyword(parcel[1]);
	}
	if (hasParts())
	    parts = tparts;
	if (isContainer())
	    contents = tcontents;
	if (canAdorn())
	    adornments = tadornments;
    }

    public int getID() {
	return id;
    }

    public void addKeyword(String s) {
	String[] tk = new String[keywords.length + 1];
	for (int i = 0; i < keywords.length; i++)
	    tk[i] = keywords[i];
	tk[keywords.length] = s;
	keywords = tk;
    }

    private int getProperty(String key) {
	if (properties.containsKey(key))
	    return properties.get(key);
	return 0;
    }

    private String getMessage(String key) {
	if (messages.containsKey(key))
	    return messages.get(key);
	return "";
    }

    public String lookAt() {
	return getMessage("desc");
    }

    public String lookIn() {
	if (isContainer())
	    return "Default GameObject contents...";
	return "This isn't a container!";
    }

    public String lookOn() {
	if (canAdorn())
	    return "Default GameObject adornments...";
	return "Nothing special about that...";
    }

    public boolean isContainer() {
	return getProperty("isContainer") == 1;
    }

    public boolean canAdorn() {
	return getProperty("canAdorn") == 1;
    }

    public boolean hasParts() {
	return getProperty("hasParts") == 1;
    }

    public boolean isRoom() {
	return getProperty("isRoom") == 1;
    }
}
