package tp1.logic.gameobjects;

import tp1.logic.GameWorld;
import tp1.logic.Move;
import tp1.logic.Position;

public abstract class Weapon extends GameObject {

	private Move dir;

	protected Weapon(GameWorld game, Position pos, int life, Move dir) {
		super(game, pos, life);
		this.dir = dir;
	}
	
	//OVERRIDES
	
	@Override
	public void onDelete() {
		this.life = 0;
		game.removeObject(this);
	}
	
	@Override
	public void automaticMove () {
		if(isAlive())
			performMovement(dir);
		if(pos.isOut()) 
			onDelete();
	}
	
	public String posToString() {
		return getSymbol();
	}
		
}
