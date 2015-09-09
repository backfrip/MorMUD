package language;

import object.GameObject;
import server.User;

public abstract class Command {

    public static String lookAt(User u, GameObject o) {
	return o.lookAt();
    }

    public static String lookIn(User u, GameObject o) {
	return o.lookIn();
    }

    public static String lookOn(User u, GameObject o) {
	return o.lookOn();
    }

    public static String who() {
	return "Should give a list of all the online players in some way, shape, or form.";
    }

}
