package tp1.logic.gameobjects;

import tp1.logic.GameWorld;
import tp1.logic.Move;
import tp1.logic.Position;

public abstract class GameObject implements GameItem {

	protected GameWorld game;
	protected Position pos;
	protected int life;
	
	protected GameObject(GameWorld game, Position pos, int life) {	
		this.pos = pos;
		this.game = game;
		this.life = life;
	}
	
	protected GameObject() {
	}
	
	//FUNCIONES GENERALES
	
	protected void receiveDamage(int damage) {
		life -= damage;
	}

	protected void performMovement(Move dir) {
		//this.pos.actualizar(dir);
		this.pos = this.pos.actualizar(dir);
	}
	
	public boolean isAlive() {
		return this.life > 0;
	}

	public int getLife() {
		return this.life;
	}
	
	public boolean isInPosition(Position position) {
		return pos.equals(position);
	}

	//TODO fill with your code
	
	public abstract String posToString();
	public abstract String getSymbol();
	protected abstract int getDamage();
	protected abstract int getArmour();
			
	public abstract void onDelete();
	public abstract void automaticMove();
	public void computerAction() {};
	
	//FUNCIONES DE ATAQUES
	
	@Override
	public boolean performAttack(GameItem other) {return false;}
	
	@Override
	public boolean receiveAttack (EnemyWeapon weapon) {return false;}
	
	@Override
	public boolean receiveAttack (UCMWeapon weapon) {return false;}
	
	@Override
	public boolean receiveAttack(Shockwave weapon) {return false;}
	
	@Override
	public boolean receiveAttack(ExplosiveAlien alien) {return false;}
	
	//FUNCION DE MIRAR SI SE ENCUENTRA UN OBJETO ALREDEDOR DE OTRO
	
	@Override
	public boolean isAround(Position position) {return false;}
	
}