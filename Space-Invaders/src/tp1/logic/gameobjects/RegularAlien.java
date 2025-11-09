package tp1.logic.gameobjects;


import tp1.logic.Position;
import tp1.view.Messages;
import tp1.logic.AlienManager;
import tp1.logic.GameWorld;

public class RegularAlien extends AlienShip{

	private final static int ARMOR = 2;
	private final static int DAMAGE = 0;

	public RegularAlien(GameWorld game, Position pos, AlienManager manager) {
		super(game, pos, ARMOR, manager);
	}
	
	protected RegularAlien() {}

	//OVERRIDES
	
	@Override
	public String getSymbol() {
		return Messages.REGULAR_ALIEN_SYMBOL;
	}
	
	@Override
	protected int getDamage() {
		return DAMAGE;
	}
	
	@Override
	protected int getArmour() {
		return ARMOR;
	}
	
	@Override
	public int getPoints() {
		return 5;
	}
	
	//FUNCIONES STRINGS
	
	protected String getDescription() {
		return Messages.REGULAR_ALIEN_DESCRIPTION;
	}
	
	//Ship Factory
	
	@Override
	protected AlienShip copy(GameWorld game, Position pos, AlienManager am) {
		return new RegularAlien(game, pos, am);
	}
	
}