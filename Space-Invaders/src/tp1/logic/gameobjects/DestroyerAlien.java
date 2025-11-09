package tp1.logic.gameobjects;

import tp1.logic.AlienManager;
import tp1.logic.GameWorld;
import tp1.logic.Position;
import tp1.view.Messages;

public class DestroyerAlien extends AlienShip{

	private final static int ARMOR = 1;
	private final static int DAMAGE = 1;

	private boolean existeBomba;

	public DestroyerAlien(GameWorld game, Position pos, AlienManager manager) {
		super(game, pos, ARMOR, manager);
		this.existeBomba = false;
	}

	protected DestroyerAlien() {}
	
	//OVERRIDES
	
	@Override
	public String getSymbol() {
		return Messages.DESTROYER_ALIEN_SYMBOL;
	}
	
	@Override
	public void computerAction() {
		if(!existeBomba && canGenerateRandomBomb()) {
			game.addObject(new Bomb(game, new Position(pos), this));
			existeBomba = true;
		}
	}
	
	@Override
	protected int getPoints() {
		return 10;
	}
	
	@Override
	protected int getArmour() {
		return ARMOR;
	}
	
	@Override 
	protected int getDamage() {
		return DAMAGE;
	}
	
	//FUNCIONES STRINGS

	protected String getDescription() {
		return Messages.DESTROYER_ALIEN_DESCRIPTION;
	}
	
	//FUNCIONES DE LA BOMBA
	
	private boolean canGenerateRandomBomb(){
		return game.getRandom().nextDouble() < game.getLevel().getShootFrequency();
	}
			
	protected void cambiarExisteBomba(){
		existeBomba = false;
	}
	
	//Ship Factory
	
	@Override
	protected AlienShip copy(GameWorld game, Position pos, AlienManager am) {
		return new DestroyerAlien(game, pos, am);
	}
}