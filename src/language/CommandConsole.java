package language;

import main.Main;

public abstract class CommandConsole {
    public static void stop() {
	Main.shutdown();
    }
}
