package tp1.logic.gameobjects;

import tp1.logic.AlienManager;
import tp1.logic.GameWorld;
import tp1.logic.Move;
import tp1.logic.Position;

public abstract class AlienShip extends EnemyShip{
	
	private AlienManager alienManager;
	private int speed;
	private int cyclesToMove;
	private boolean hasDescendido;

	protected AlienShip(GameWorld game, Position pos, int life, AlienManager manager) {
		super(game, pos, life);
		this.alienManager = manager;
		this.speed = game.getNumCyclesToMoveOneCell();
		this.cyclesToMove = this.speed;
		this.hasDescendido = true;
	}
	
	protected AlienShip() {
	}
	
	//OVERRIDES

	@Override
	public void onDelete() {
		alienManager.alienDead(this);
		game.recievePoints(getPoints());
		game.removeObject(this);
	}
	
	@Override
	public void automaticMove() {
		if (cyclesToMove == 0){
			super.automaticMove();
			cyclesToMove = speed;
			if(pos.inBorder()) {
				alienManager.shipOnBorder();
			}
		}else if(alienManager.onBorder() && !hasDescendido) {
			descent();
			dir = dir.flip();
		}else{
			cyclesToMove--;
			alienManager.decreaseOnBorder();
		}
	}
	
	@Override
	protected void performMovement(Move dir) {
		super.performMovement(dir);
		hasDescendido = false;
	}

	@Override
	protected void receiveDamage(int damage) {
		super.receiveDamage(damage);
		if(!isAlive())
			onDelete();
	}
	
	@Override
	public boolean receiveAttack(Shockwave weapon) {
		receiveDamage(weapon.getDamage());
		return true;
	}
	
	@Override
	public boolean receiveAttack(ExplosiveAlien alien) {
		receiveDamage(alien.getDamage());
		return true;
	}
	
	@Override
	public boolean isAround(Position position) {
		return this.pos.isAround(position);
	}
	
	//OTRAS FUNCIONES
	
	private void descent() {
		performMovement(Move.DOWN);
		hasDescendido = true;
		if (this.pos.isInFinalRow())
			alienManager.finalRowReached();
	}
	
	//Ship Factory
	
	protected abstract AlienShip copy(GameWorld game, Position pos, AlienManager am);
	
}
