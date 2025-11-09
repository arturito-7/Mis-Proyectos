package tp1.logic;

import java.util.ArrayList;
import java.util.List;

import tp1.logic.gameobjects.GameObject;

public class GameObjectContainer {

	private List<GameObject> objects;

	public GameObjectContainer() {
		objects = new ArrayList<>();
	}

	public void add(GameObject object) {
		objects.add(object);
	}

	public void remove(GameObject object) {
		objects.remove(object);
	}

	public void computerActions() {
		List<GameObject> objectsCopy = new ArrayList<>(objects);
	    for (GameObject object : objectsCopy) {
	        object.computerAction();
	    }
	}
	
	public void automaticMoves() {
		List<GameObject> objectsCopy = new ArrayList<>(objects);
		for (GameObject object : objectsCopy) {
			performAttacks(object);
			object.automaticMove();
			if(object.isAlive())
				performAttacks(object);
		}
	}

	public void performAttacks(GameObject object) {
		List<GameObject> objectsCopy = new ArrayList<>(objects);
	    for (GameObject obj : objectsCopy) {
	    	object.performAttack(obj);
		}
	}

	public String toString(Position position) {
		for(GameObject obj: objects) {
			if(obj.isInPosition(position))
				return obj.posToString();
		}
		return "";
	}
	
}