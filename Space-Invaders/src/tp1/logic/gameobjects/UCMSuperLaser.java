package tp1.logic.gameobjects;

import tp1.logic.GameWorld;
import tp1.logic.Move;
import tp1.logic.Position;
import tp1.view.Messages;

public class UCMSuperLaser extends UCMWeapon {
	
	private final static int ARMOR = 1;
	private final static int DAMAGE = 2;

	protected UCMSuperLaser(GameWorld game, Position pos) {
		super(game, pos, ARMOR, Move.UP);
	}
	
	//OVERRIDES
	
	@Override
	public String getSymbol() {
		return Messages.SUPERLASER_SYMBOL;
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
