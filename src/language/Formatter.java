package language;

import java.util.regex.Pattern;

import server.User;

public abstract class Formatter {
    public static final String ANSI_RESET = "\u001B[0m"; // r

    // Color
    public static final String ANSI_RESET_COLOR = "\u001B[39;49m"; // c
    public static final String ANSI_BLACK = "\u001B[30m"; // 0
    public static final String ANSI_RED = "\u001B[31m"; // 1
    public static final String ANSI_GREEN = "\u001B[32m"; // 2
    public static final String ANSI_YELLOW = "\u001B[33m"; // 3
    public static final String ANSI_BLUE = "\u001B[34m"; // 4
    public static final String ANSI_PURPLE = "\u001B[35m"; // 5
    public static final String ANSI_CYAN = "\u001B[36m"; // 6
    public static final String ANSI_WHITE = "\u001B[37m"; // 7

    // Intensity
    public static final String ANSI_RESET_INTENSITY = "\u001B[22m"; // f
    public static final String ANSI_INTENSE = "\u001B[1m"; // s

    private static final Pattern p = Pattern.compile("`");

    public static String format(String s, User u) {
	String[] r = p.split(s);
	for (int i = 1; i < r.length; i++) {
	    if (!r[i].isEmpty())
		r[i] = parseTag(r[i].charAt(0)) + r[i].substring(1);
	}
	return String.join("", r) + ANSI_RESET;
    }

    private static String parseTag(char s) {
	if (s == 'r')
	    return ANSI_RESET;
	if (s == 'c')
	    return ANSI_RESET_COLOR;
	if (s == 'f')
	    return ANSI_RESET_INTENSITY;
	if (s == '0')
	    return ANSI_BLACK;
	if (s == '1')
	    return ANSI_RED;
	if (s == '2')
	    return ANSI_GREEN;
	if (s == '3')
	    return ANSI_YELLOW;
	if (s == '4')
	    return ANSI_BLUE;
	if (s == '5')
	    return ANSI_PURPLE;
	if (s == '6')
	    return ANSI_CYAN;
	if (s == '7')
	    return ANSI_WHITE;
	if (s == 's')
	    return ANSI_INTENSE;
	if (s == '\\')
	    return "\\";
	return "";
    }
}
