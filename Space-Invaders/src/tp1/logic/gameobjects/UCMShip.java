package tp1.logic.gameobjects;

import tp1.Exceptions.LaserInFlightException;
import tp1.Exceptions.NoShockWaveException;
import tp1.Exceptions.OffWorldException;
import tp1.logic.GameWorld;
import tp1.logic.Move;
import tp1.logic.Position;
import tp1.view.Messages;

public class UCMShip extends Ship {
	
	protected final static int ARMOR = 3;
	protected final static int DAMAGE = 1;
	private boolean sePuedeLanzarLaser;
	private boolean sePuedeLanzarShockWave;
	
	public UCMShip (GameWorld game, Position pos) {
		super(game, pos, ARMOR);
		sePuedeLanzarLaser = true;
		sePuedeLanzarShockWave = false;
	}
	
	protected UCMShip() {}
	
	//OVERRIDES
	
	@Override
	public String posToString() {
		return getSymbol();
	}
	
	@Override
	public String getSymbol() {
		if (isAlive())
			return Messages.UCMSHIP_SYMBOL;
		else
			return Messages.UCMSHIP_DEAD_SYMBOL;
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
	public void onDelete() {}
	
	@Override
	public void automaticMove() {}
	
	@Override
	public boolean receiveAttack (EnemyWeapon weapon) {
		receiveDamage(weapon.getDamage());
		return true;
	}
	
	//FUNCIONES STRINGS
	
	@Override
	public String getInfo(){
		return Messages.ucmShipDescription(getDescription(), getDamage(), getArmour());
	}
	
	protected String getDescription() {
		return Messages.UCMSHIP_DESCRIPTION;
	}

	//OTRAS FUNCIONES
	
	public boolean move(Move dir) throws OffWorldException {
		Position position = new Position (this.pos);
		performMovement(dir);
		boolean dentro = !pos.isOut();
		if(!dentro) {
			this.pos = position;
			throw new OffWorldException(Messages.OFF_WORLD_1 + dir + Messages.OFF_WORLD_2 + pos.toString());
		}
		return dentro;
	}
	
	//FUNCIONES DEL LASER

	public void enableLaser() {
		game.addObject(new UCMLaser(game, new Position (this.pos)));
		sePuedeLanzarLaser = false;
	}
	
	public void shootLaser() throws LaserInFlightException {
		if(sePuedeLanzarLaser) {
			game.addObject(new UCMLaser(game, new Position (this.pos)));
			sePuedeLanzarLaser = false;
		}else
			throw new LaserInFlightException(Messages.LASER_EN_TABLERO);
	}
	
	public void cambiarLaser() {
		sePuedeLanzarLaser = true;
	}
	
	//FUNCIONES DEL SUPER LASER


	public boolean shootSuperLaser(int points) throws LaserInFlightException {
		if(sePuedeLanzarLaser) {
				game.addObject(new UCMSuperLaser(game, new Position (this.pos)));
				sePuedeLanzarLaser = false;
				return true;
		}else
			throw new LaserInFlightException(Messages.LASER_EN_TABLERO);
	}
	
	
	//FUNCIONES DEL SHOCKWAVE
	
	public void shootShockWave() throws NoShockWaveException {
		if(sePuedeLanzarShockWave) {
			game.addObject(new Shockwave (game));
			sePuedeLanzarShockWave = false;
		}else
			throw new NoShockWaveException(Messages.NO_SHOCKWAVE_DISPONIBLE);
	}
	
	public void cambiarShockWave() {
		sePuedeLanzarShockWave = true;
	}

	public String stringShockWave() {
		if(sePuedeLanzarShockWave)
			return Messages.COMMAND_SHOCKWAVE_NAME + Messages.ON + "\n";
		else
			return Messages.COMMAND_SHOCKWAVE_NAME + Messages.OFF + "\n";
	}
	
}
