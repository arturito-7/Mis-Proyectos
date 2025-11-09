package tp1.logic.gameobjects;

import tp1.logic.GameWorld;
import tp1.logic.Move;
import tp1.logic.Position;
import tp1.view.Messages;

public abstract class EnemyShip extends Ship {
	
	protected Move dir;

	protected EnemyShip(GameWorld game, Position pos, int life) {
		super(game, pos, life);
		this.dir = Move.LEFT; 
	}
	
	protected EnemyShip() {
	}

	//RECIBIR PUNTOS
	
	public int recievePoints(EnemyShip ship) {
		return getPoints();
	}
	
	protected abstract int getPoints();
	
	@Override
	public boolean receiveAttack (UCMWeapon weapon) {
		receiveDamage(weapon.getDamage());
		return true;
	}
	
	@Override
	protected String getInfo() {
		return Messages.alienDescription(getDescription(), getPoints(), getDamage(), getArmour());
	}
	
	@Override
	public void automaticMove() {
		performMovement(dir);
	}
	
}
