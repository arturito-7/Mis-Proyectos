package tp1.logic.gameobjects;

import tp1.logic.GameWorld;
import tp1.logic.Move;
import tp1.logic.Position;
import tp1.view.Messages;

public class Bomb extends EnemyWeapon {

	private final static int ARMOR = 1;
	private final static int DAMAGE = 1;
	
	private DestroyerAlien alien;
	
	protected Bomb(GameWorld game, Position pos, DestroyerAlien alien) {
		super(game, pos, ARMOR, Move.DOWN);
		this.alien = alien;
	}
	
	//OVERRIDES
	
	@Override
	public String getSymbol() {
		return Messages.BOMB_SYMBOL;
	}
	
	@Override
	public void onDelete() {
		super.onDelete();
		alien.cambiarExisteBomba();
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
