package tp1.logic.gameobjects;

import tp1.logic.Position;

public interface GameItem {
	
	public boolean performAttack(GameItem other);
	
	public boolean receiveAttack(EnemyWeapon weapon);
	public boolean receiveAttack(UCMWeapon weapon);
	public boolean receiveAttack(Shockwave weapon);
	public boolean receiveAttack(ExplosiveAlien alien);

	public boolean isAlive();
	public boolean isInPosition(Position pos);

	public boolean isAround(Position position);

}