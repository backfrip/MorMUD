package language;

import main.Main;

public abstract class Parser {
    private static final String id = "PARSE";

    public static void parseConsole(String s) {
	s = s.trim();
	int i = findBreak(s);
	String[] a = { s.substring(0, i), s.substring(i) };

	if (a[0].equals("quit") || a[0].equals("stop"))
	    CommandConsole.stop();
	else
	    Main.printAlert(id, "Command not recognized!");
    }

    private static int findBreak(String s) {
	int i;
	for (i = 0; i < s.length() && s.charAt(i) != ' '; i++)
	    ;
	return i;
    }
}
