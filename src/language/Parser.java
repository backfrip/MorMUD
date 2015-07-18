package language;

import main.Main;

public abstract class Parser {
    public static void parseConsole(String s) {
	s.trim();
	String[] c = s.split(" ");
	boolean go = true;
	int i = 0;
	while (go) {
	    if (c[i].equals("")) {
		i++;
	    } else if (c[i].toLowerCase().equals("quit") || c[i].toLowerCase().equals("stop")) {
		Main.shutdown();
		go = false;
	    } else {
		Main.printAlert("CONSOLE", "Command not recognized!");
		go = false;
	    }
	}
    }
}
