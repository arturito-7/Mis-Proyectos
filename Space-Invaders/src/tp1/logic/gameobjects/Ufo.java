package tp1.logic.gameobjects;

import tp1.logic.GameWorld;
import tp1.logic.Position;
import tp1.view.Messages;


public class Ufo extends EnemyShip {
	
	private final static int DAMAGE = 0;
	private final static int ARMOR = 1;
	
	public Ufo(GameWorld game) {
		super(game, new Position (GameWorld.DIM_X,0), 0);
	}

	protected Ufo() {}
	
	//OVERRRRIDES
	
	@Override
	public String getSymbol() {
		return Messages.UFO_SYMBOL;
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
		this.life = 0;
		this.pos = new Position (GameWorld.DIM_X,0);
	}

	@Override
	public void automaticMove() {
		if(isAlive()) {
			super.automaticMove();
		}
		if(pos.isOut()) {
			onDelete();
		}
	}
	
	@Override
	public void computerAction() {
		if(!isAlive() && canGenerateRandomUfo()) {
			this.life = ARMOR;
		}
	}
	
	@Override
	protected void receiveDamage(int damage) {
		super.receiveDamage(damage);
		if(!isAlive()) {
			onDelete();
			game.recievePoints(getPoints());
			game.cambiarShockWave();
		}
	}
	
	@Override
	public int getPoints() {
		return 25;
	}
	
	//FUNCIONES STRINGS
	
	protected String getDescription() {
		return Messages.UFO_DESCRIPTION;
	}

	//OTRAS FUNCIONES
	
	private boolean canGenerateRandomUfo(){
		return game.getRandom().nextDouble() < game.getLevel().getUfoFrequency();
	}
	
}
