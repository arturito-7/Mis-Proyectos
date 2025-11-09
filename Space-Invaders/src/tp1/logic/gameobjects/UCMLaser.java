package tp1.logic.gameobjects;

import tp1.logic.GameWorld;
import tp1.logic.Move;
import tp1.logic.Position;
import tp1.view.Messages;

public class UCMLaser extends UCMWeapon {
	
	private final static int ARMOR = 1;
	private final static int DAMAGE = 1;

	protected UCMLaser(GameWorld game, Position pos) {
		super(game, pos, ARMOR, Move.UP);
	}
	
	//OVERRIDES
	
	@Override
	public String getSymbol() {
		return Messages.LASER_SYMBOL;
	}
	
	@Override
	protected int getArmour() {
		return ARMOR;
	}
	
	@Override 
	protected int getDamage() {
		return DAMAGE;
	}
	
}
