package object;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

public class GameObject {
    private ConcurrentHashMap<String, Integer> parts, contents, adornments,
	    properties;
    private ConcurrentHashMap<String, String> messages;
    private String[] keywords;


    //   __| __|__ __| |  | _ \
    // \__ \ _|    |   |  | __/
    // ____/___|  _|  \__/ _|  


    public GameObject(LinkedList<String[]> save) {
	ConcurrentHashMap<String, Integer> tparts = new ConcurrentHashMap<String, Integer>();
	ConcurrentHashMap<String, Integer> tcontents = new ConcurrentHashMap<String, Integer>();
	ConcurrentHashMap<String, Integer> tadornments = new ConcurrentHashMap<String, Integer>();
	properties = new ConcurrentHashMap<String, Integer>();
	messages = new ConcurrentHashMap<String, String>();
	keywords = new String[0];
	for (String[] parcel : save) {
	    if (parcel[0].equals("parts"))
		tparts.put(parcel[1], Integer.parseInt(parcel[2]));
	    else if (parcel[0].equals("contents"))
		tcontents.put(parcel[1], Integer.parseInt(parcel[2]));
	    else if (parcel[0].equals("adornments"))
		tadornments.put(parcel[1], Integer.parseInt(parcel[2]));
	    else if (parcel[0].equals("properties"))
		properties.put(parcel[1], Integer.parseInt(parcel[2]));
	    else if (parcel[0].equals("messages"))
		messages.put(parcel[1], parcel[2]);
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

    public void addKeyword(String s) {
	String[] tk = new String[keywords.length + 1];
	for (int i = 0; i < keywords.length; i++)
	    tk[i] = keywords[i];
	tk[keywords.length] = s;
	keywords = tk;
    }

    //    \   __|  __| __|  __|  __|
    //   _ \ (    (    _| \__ \\__ \
    // _/  _\___|\___|___|____/____/


    public int getID() {
	return getProperty("id");
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


    //   __|  _ \   \  |  \  |   \    \ | _ \ 
    //  (    (   | |\/ | |\/ |  _ \  .  | |  |
    // \___|\___/ _|  _|_|  _|_/  _\_|\_|___/ 


    public String lookAt() {
	return "`s"
		+ getMessage("title")
		+ "`r "
		+ ((keywords.length > 0) ? Arrays.toString(keywords) + " " : "")
		+ "`3(#" + getProperty("id") + ")`r\n" + getMessage("desc");
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


    //  _ \   \__ __| \   __|_ _|  __|   \__ __|_ _|  _ \   \ |
    //  |  | _ \  |  _ \  _|   |  (     _ \  |    |  (   | .  |
    // ___/_/  _\_|_/  _\_|  ___|\___|_/  _\_|  ___|\___/ _|\_|


    public LinkedList<String[]> getData() {
	LinkedList<String[]> data = new LinkedList<String[]>();
	if (hasParts())
	    datifyIntegerMap(data, "parts", parts);
	if (isContainer())
	    datifyIntegerMap(data, "contents", contents);
	if (canAdorn())
	    datifyIntegerMap(data, "adornments", adornments);
	datifyIntegerMap(data, "properties", properties);
	datifyMessages(data);
	datifyKeywords(data);
	return data;
    }

    private void datifyIntegerMap(LinkedList<String[]> data, String name,
	    ConcurrentHashMap<String, Integer> map) {
	for (String key : map.keySet())
	    data.add(new String[] { name, key, String.valueOf(map.get(key)) });
    }

    private void datifyMessages(LinkedList<String[]> data) {
	for (String key : messages.keySet())
	    data.add(new String[] { "messages", key, messages.get(key) });
    }

    private void datifyKeywords(LinkedList<String[]> data) {
	for (String keyword : keywords)
	    data.add(new String[] { "keywords", keyword });
    }

}
