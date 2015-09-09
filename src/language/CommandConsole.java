package language;

import main.Main;

public abstract class CommandConsole {

    public static void stop() {
	Main.shutdown();
    }

    public static void wipe() {
	Main.wipe();
    }

}
