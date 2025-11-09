package tp1.logic.gameobjects;

import tp1.logic.GameWorld;
import tp1.logic.Position;
import tp1.view.Messages;

public abstract class Ship extends GameObject {

	protected Ship(GameWorld game, Position pos, int life) {
		super(game, pos, life);
	}

	protected Ship() {
	}
	
	@Override
	public String posToString() {
		return Messages.status(getSymbol(), life);
	}

	protected abstract String getInfo();
	protected abstract String getDescription();
	
}