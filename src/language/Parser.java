package language;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.CharBuffer;

import main.Main;
import object.GameObject;
import server.User;

public abstract class Parser {
    private static final String id = "PARSE";


    //   __| __|  \ | __| _ \   \   |   
    //  (_ | _|  .  | _|    /  _ \  |   
    // \___|___|_|\_|___|_|_\_/  _\____|


    public static void parseConsole(String s) {
	s = s.trim();
	int i;
	for (i = 0; i < s.length() && s.charAt(i) != ' '; i++)
	    ;
	String[] c = { s.substring(0, i), s.substring(i) };

	if (c[0].equalsIgnoreCase("quit") || c[0].equalsIgnoreCase("stop"))
	    CommandConsole.stop();
	else if (c[0].equalsIgnoreCase("wipe"))
	    CommandConsole.wipe();
	else
	    Main.printAlert(id, "Command not recognized!");
    }

    public static String parse(String s, User u) {
	int i;
	s = s.trim();
	for (i = 0; i < s.length() && s.charAt(i) != ' '; i++)
	    ;
	String[] c = { s.substring(0, i), s.substring(i).trim() };

	if (c[0].equalsIgnoreCase("who")) {
	    s = Command.who();
	} else if (c[0].equalsIgnoreCase("l") || c[0].equalsIgnoreCase("look")) {
	    s = parseLook(c, u);
	} else if (c[0].equalsIgnoreCase("a")
		|| c[0].equalsIgnoreCase("attack")) {
	    s = "`1Attacking not yet implemented!";
	} else {
	    s = "`1I'm sorry, I have absolutely no idea what you said.";
	}
	return s;
    }

    // TODO: Everything welcome related should be pushed over to Data.
    public static String welcome() {
	String s;
	try {
	    BufferedReader in = new BufferedReader(new FileReader(
		    "./welcome.txt"));
	    CharBuffer target = CharBuffer.allocate(1400);
	    in.read(target);
	    target.rewind();
	    s = target.toString().trim();
	    in.close();
	} catch (FileNotFoundException e) {
	    Main.printAlert(id,
		    "'welcome.txt' not found! Creating default welcome...");
	    writeDefaultWelcome();
	    s = welcome();
	} catch (IOException e) {
	    Main.printAlert(id,
		    "Error reading 'welcome.txt'! Creating default welcome...");
	    writeDefaultWelcome();
	    s = welcome();
	}
	return s;
    }


    //   __| _ \ __|  __|_ _| __|_ _|  __|
    // \__ \ __/ _|  (     |  _|   |  (   
    // ____/_|  ___|\___|___|_|  ___|\___|


    public static String parseLook(String[] c, User u) {
	if (c[1].equals("")) {
	    return Command.lookAt(u, u.getRoom());
	} else {
	    c = grabWord(c);
	    if (c[1].equalsIgnoreCase("at")) {
		if (c[2].isEmpty())
		    return "Look at what?";
		if (getByKeys(u, c[2]) != null)
		    return Command.lookAt(u, getByKeys(u, c[2]));
	    } else if (c[1].equalsIgnoreCase("in")
		    || c[1].equalsIgnoreCase("inside")
		    || c[1].equalsIgnoreCase("into")) {
		if (c[2].isEmpty())
		    return "Look in what?";
		if (getByKeys(u, c[2]) != null)
		    return Command.lookIn(u, getByKeys(u, c[2]));
	    } else if (c[1].equalsIgnoreCase("on")) {
		if (c[2].equals(""))
		    return "Look on what?";
		if (getByKeys(u, c[2]) != null)
		    return Command.lookOn(u, getByKeys(u, c[2]));
	    } else {
		c = new String[] { c[0],
			c[1] + ((!c[2].isEmpty()) ? " " : "") + c[2] };
		if (getByKeys(u, c[1]) != null)
		    return Command.lookAt(u, getByKeys(u, c[1]));
	    }
	}
	return "There doesn't appear to be any \"" + c[c.length - 1]
		+ "\" here...";
    }


    //  |  | __| |    _ \ __| _ \
    //  __ | _|  |    __/ _|    /
    // _| _|___|____|_|  ___|_|_\


    private static String[] grabWord(String[] c) {
	int i;
	for (i = 0; i < c[c.length - 1].length()
		&& c[c.length - 1].charAt(i) != ' '; i++)
	    ;
	String[] r = new String[c.length + 1];
	for (int j = 0; j < c.length - 1; j++)
	    r[j] = c[j];
	r[c.length - 1] = c[c.length - 1].substring(0, i);
	r[c.length] = c[c.length - 1].substring(i).trim();
	return r;
    }

    private static GameObject getByKeys(User u, String s) {
	if (s.equalsIgnoreCase("me"))
	    return u.getCharacter();
	if (s.equalsIgnoreCase("here"))
	    return u.getRoom();
	return null;
    }

    private static void writeDefaultWelcome() {
	InputStream in = Main.class
		.getResourceAsStream("/resource/defaultwelcome");
	FileOutputStream out = null;
	try {
	    out = new FileOutputStream("./welcome.txt");
	} catch (FileNotFoundException e) {
	    Main.printAlert(id, "Could create file 'welcome.txt'!");
	}
	int read = 0;
	byte[] bytes = new byte[1024];

	try {
	    while ((read = in.read(bytes)) != -1) {
		out.write(bytes, 0, read);
	    }
	} catch (IOException e) {
	    Main.printAlert(id, "Error writing to file 'welcome.txt'!");
	}
	Main.printInfo(id, "'welcome.txt' created successfully!");
	try {
	    in.close();
	    out.close();
	} catch (IOException e) {
	    Main.printInfo(id, "Error closing welcome writer streams!");
	}
    }
}
