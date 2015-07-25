package language;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.CharBuffer;

import object.Room;
import main.Main;

public abstract class Parser {
    private static final String id = "PARSE";

    public static void parseConsole(String s) {
	s = s.trim();
	int i;
	for (i = 0; i < s.length() && s.charAt(i) != ' '; i++)
	    ;
	String[] c = { s.substring(0, i), s.substring(i) };

	if (c[0].toLowerCase().equals("quit")
		|| c[0].toLowerCase().equals("stop"))
	    CommandConsole.stop();
	else
	    Main.printAlert(id, "Command not recognized!");
    }

    public static String parse(String s, Room r) {
	s = s.trim();
	int i;
	for (i = 0; i < s.length() && s.charAt(i) != ' '; i++)
	    ;
	String[] c = { s.substring(0, i), s.substring(i).trim() };

	if (c[0].equals("who")) {
	    s = Command.who();
	} else if (c[0].toLowerCase().equals("l")
		|| c[0].toLowerCase().equals("look")) {
	    if (c[1].equals("")) {
		s = Command.lookAt(r);
	    } else {
		c = grabWord(c);
		if (c[1].equals("at")) {
		    if (c[2].equals("")) {
			s = "Look at what?";
		    } else if (c[2].toLowerCase().equals("here")) {
			s = Command.lookAt(r);
		    } else {
			s = Command.lookAt(c[2], r);
		    }
		} else if (c[1].equals("in")) {
		    if (c[2].equals("")) {
			s = "Look in what?";
		    } else if (c[2].toLowerCase().equals("here")) {
			s = Command.lookIn(r);
		    } else {
			s = Command.lookIn(c[2], r);
		    }
		} else if (c[1].equals("on")) {
		    if (c[2].equals("")) {
			s = "Look on what?";
		    } else {
			s = Command.lookOn(c[2], r);
		    }
		} else {
		    s = Command.lookAt(c[1] + " " + c[2], r);
		}
	    }
	} else if (c[0].toLowerCase().equals("a")
		|| c[0].toLowerCase().equals("attack")) {
	    s = Command.attack(c[1], r);
	} else {
	    s = Command.exit(c[1], r);
	}
	return s;
    }

    public static String welcome() {
	String s;
	try {
	    BufferedReader in = new BufferedReader(new FileReader(
		    "./welcome.txt"));
	    CharBuffer target = CharBuffer.allocate(1400);
	    in.read(target);
	    target.rewind();
	    s = target.toString();
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

    private static void writeDefaultWelcome() {
	InputStream in = Main.class
		.getResourceAsStream("/resource/welcome.txt");
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
