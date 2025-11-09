package tp1.logic.gameobjects;

import tp1.logic.GameWorld;
import tp1.logic.Move;
import tp1.logic.Position;
import tp1.view.Messages;

public class Shockwave extends UCMWeapon {
	
	private final static int DAMAGE = 1;

	protected Shockwave(GameWorld game) {
		super(game, null, game.getRemainingAliens(), Move.NONE);
	}

	//OVERRIDES
	
	@Override
	public boolean isInPosition(Position pos) {
		return false;
	}
	
	@Override
	public String getSymbol() {
		return Messages.SHOCKWAVE_SYMBOL;
	}
	
	@Override
	protected int getArmour() {
		return game.getRemainingAliens();
	}
	
	@Override 
	protected int getDamage() {
		return DAMAGE;
	}
	
	@Override
	public void onDelete() {
		this.life--;
		if(!isAlive())
			game.removeObject(this);
	}
	
	@Override
	public void automaticMove() {
		
	}
	
	@Override
	public boolean performAttack(GameItem other) {
		if(isAlive() && other.isAlive()) {
			boolean attack = other.receiveAttack(this);
			if (attack) 
				onDelete();
			return attack;
		}
		return false;
	}
	
}
