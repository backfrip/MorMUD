package language;

import object.GameObject;
import object.Room;
import server.User;

public abstract class Command {
    public static String who() {
	return "Should give a list of all the online players in some way, shape, or form.";
    }

    public static String lookAt(User u, GameObject o) {
	return o.lookAt();
    }

    public static String lookIn(User u, GameObject o) {
	if (o.isContainer())
	    return o.lookIn();
	return o + " is not a container!";
    }
}
