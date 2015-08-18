package server;

import main.Main;
import object.GameObject;

public class User {
    private String username;
    private GameObject room, character;

    public User(String u) {
	username = u;
	room = Main.storage.getObject(0);
	character = null;
    }

    public GameObject getRoom() {
	return room;
    }

    public GameObject getCharacter() {
	return character;
    }

}
