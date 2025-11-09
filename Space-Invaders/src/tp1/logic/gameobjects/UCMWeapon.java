package tp1.logic.gameobjects;

import tp1.logic.GameWorld;
import tp1.logic.Move;
import tp1.logic.Position;

public abstract class UCMWeapon extends Weapon{

	protected UCMWeapon(GameWorld game, Position pos, int life, Move dir) {
		super(game, pos, life, dir);
	}
	
	//OVERRIDES
	
	@Override
	public boolean performAttack(GameItem other) {
		if(isAlive() && other.isAlive() && other.isInPosition(pos)) {
			boolean attack = other.receiveAttack(this);
			if (attack) 
				onDelete();
			return attack;
		}
		return false;
	}
	
	@Override
	public boolean receiveAttack (EnemyWeapon weapon) {
		receiveDamage(weapon.getDamage());
		return true;
	}
	
	@Override 
	protected void receiveDamage(int damage) {
		super.receiveDamage(damage);
		onDelete();
	}
	
	@Override
	public void onDelete() {
		super.onDelete();
		game.cambiarLaser();
	}
	
}
