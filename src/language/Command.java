package language;

import object.Room;

public abstract class Command {
    public static String who() {
	return "We don't know! :D\nGood luck figuring it out!";
    }
    
    public static String lookAt(Room r) {
	return "This is supposed to print the description of the room that you're in!";
    }

    public static String lookAt(String desc, Room r) {
	return "This is supposed to analyze the description given and look at whatever it is that's in the room!";
    }

    public static String lookIn(Room r) {
	return "This is supposed to give a quick list of a room's contents!";
    }

    public static String lookIn(String string, Room r) {
	return "This is supposed to analyze the description given and give a quick list of what it describes' contents!";
    }

    public static String lookOn(String string, Room r) {
	return "This is supposed to analyze the description given and give a quick list of whatever is on what it describes' surface!";
    }

    public static String attack(String string, Room r) {
	return "This is supposed to analyze the description given and have the player begin attacking the described entitiy.";
    }

    public static String exit(String string, Room r) {
	return "This last ditch effort at analysis attempts to exit the room based on the given criteria.";
    }
}
