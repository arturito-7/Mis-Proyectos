package tp1.logic.gameobjects;


import tp1.logic.Position;
import tp1.view.Messages;
import tp1.logic.AlienManager;
import tp1.logic.GameWorld;

public class ExplosiveAlien extends AlienShip{

	private final static int ARMOR = 2;
	private final static int DAMAGE = 1;

	protected ExplosiveAlien(GameWorld game, Position pos, AlienManager manager) {
		super(game, pos, ARMOR, manager);
	}
	
	protected ExplosiveAlien() {}
	
	//OVERRIDES
	
	@Override
	public String getSymbol() {
		return Messages.EXPLOSIVE_ALIEN_SYMBOL;
	}
	
	@Override
	protected int getArmour() {
		return ARMOR;
	}
	
	@Override 
	protected int getDamage() {
		return DAMAGE;
	}
	
	@Override
	public void onDelete() {
        game.performExplosiveAttack(this);
		super.onDelete();
	}
	
	@Override
	public boolean performAttack(GameItem other) {
		if(!isAlive() && other.isAlive() && other.isAround(pos)) {
			return other.receiveAttack(this);
		}
		return false;
	}
	
	@Override
	public boolean receiveAttack(ExplosiveAlien alien) {
		return false;
	}
	
	@Override
	public int getPoints() {
		return 12;
	}
	
	//FUNCIONES STRINGS
	
	protected String getDescription() {
		return Messages.EXPLOSIVE_ALIEN_DESCRIPTION;
	}

	//Ship Factory
	
	@Override
	protected AlienShip copy(GameWorld game, Position pos, AlienManager am) {
		return new ExplosiveAlien(game, pos, am);
	}
	
}