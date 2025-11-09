package tp1.logic.gameobjects;

import java.util.Arrays;
import java.util.List;

import tp1.Exceptions.InitializationException;
import tp1.logic.AlienManager;
import tp1.logic.GameWorld;
import tp1.logic.Position;
import tp1.view.Messages;

public class ShipFactory {
	
	private static final List<AlienShip> AVAILABLE_ALIEN_SHIPS = Arrays.asList(
			new RegularAlien(), 
			new DestroyerAlien(), 
			new ExplosiveAlien()
	);
	
	private static final List<Ship> AVAILABLE_SHIPS = Arrays.asList(
			new UCMShip(),
			new RegularAlien(), 
			new DestroyerAlien(), 
			new ExplosiveAlien(),
			new Ufo()
	);

	public static AlienShip spawnAlienShip(String input, GameWorld game, Position pos, AlienManager am) throws InitializationException {
		boolean alien = false;
		if(!pos.isOut()) {
			for (AlienShip ship: AVAILABLE_ALIEN_SHIPS) {
				if (input.equalsIgnoreCase(ship.getSymbol())) {
					alien = true;
					return ship.copy(game, pos, am);
				}
			}
			if (!alien)
				throw new InitializationException(Messages.UNKNOWN_SHIP + input);
		}else
			throw new InitializationException(pos.toString() + Messages.OFF_BOARD);
		return null;
	}

	public static String infoToString() {
		StringBuilder str = new StringBuilder();
		for (Ship ship: AVAILABLE_SHIPS) {
			str.append(ship.getInfo() + "\n");
		}
		return str.toString();
	}
	
}
