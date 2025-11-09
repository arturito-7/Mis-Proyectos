package tp1.logic;

import java.util.Random;

import tp1.Exceptions.GameModelException;
import tp1.Exceptions.NotEnoughPointsException;
import tp1.control.InitialConfiguration;
import tp1.logic.gameobjects.*;
import tp1.view.Messages;


public class Game implements GameModel, GameStatus, GameWorld{

	private GameObjectContainer container;
	private UCMShip player;
	private AlienManager alienManager;
	private int currentCycle;
	
	private Random rand;
	private Level level;
	private long seed;
	private int points;
	private boolean doExit;
	
	//INICIAR JUEGO
	
	public Game(Level level, long seed) {
		this.level = level;
		this.seed = seed;
		try {initGame(InitialConfiguration.NONE);}catch (GameModelException e) {}
	}
	
	private void initGame(InitialConfiguration conf) throws GameModelException{
		this.rand = new Random(seed);
		this.doExit =  false;
		this.currentCycle = 0;
		
		this.alienManager = new AlienManager(this, level);
		this.container = alienManager.initialize(conf);
		this.player = new UCMShip(this, new Position(DIM_X / 2, DIM_Y - 1));
		container.add(player);
		
		this.points = 0;
	}
	
	public void reset(InitialConfiguration conf) throws GameModelException{
		initGame(conf);
	}
	
	//CAMBIAR EL CONTAINER
	
	public void addObject(GameObject object) {
		this.container.add(object);
	}
	
	public void removeObject(GameObject object) {
		this.container.remove(object);
	}

	//FUNCIONES TERMINAR JUEGO
	
	public boolean isFinished() {
		return (doExit) || (aliensWin()) || (playerWin());
	}
	
	public boolean playerWin() {
		return (this.player.isAlive() && alienManager.allAliensDead());
	}

	public boolean aliensWin() {
		return ((!this.player.isAlive() && !alienManager.allAliensDead()) || haveLanded());
	}

	public void exit() {
		this.doExit = true;
	}

	//FUNCIONES VARIAS
	
	public int getCycle() {
		return this.currentCycle;
	}

	public int getRemainingAliens() {
		return alienManager.getRemainingAliens();
	}

	private boolean haveLanded() {
		return alienManager.haveLanded();
	}

	public Random getRandom() {
		return rand;
	}

	public Level getLevel() {
		return this.level;
	}
	
	public int getNumCyclesToMoveOneCell() {
		return level.getNumCyclesToMoveOneCell();
	}
	
	public void recievePoints(int puntos) {
		this.points += puntos;
	}
	
	//FUNCION DE LA NAVE
	
	public void move(Move dir) throws GameModelException {
		player.move(dir);
	}
	
	//FUNCION DEL LASER
	
	public void shootLaser() throws GameModelException {
		player.shootLaser();
	}
	
	//FUNCION DEL SUPERLASER
	
	public void shootSuperLaser() throws GameModelException {
		if (points >= 5) {
			if(player.shootSuperLaser(points))
				points -=5;
		}else
			throw new NotEnoughPointsException(String.format(Messages.NOT_ENOUGH_POINTS, points));
	}
	
	//FUNCION DE AMBOS LASERS
	
	public void cambiarLaser() {
		player.cambiarLaser();
	}
	
	//FUNCIONES DEL SHOCKWAVE
	
	public void shootShockwave() throws GameModelException {
		player.shootShockWave();
	}

	public void cambiarShockWave() {
		player.cambiarShockWave();
	}
	
	//FUNCION EXPLOSIVE ALIEN
	
	public void performExplosiveAttack(GameObject alien) {
		container.performAttacks(alien);
	}
	
	///////////////////////////////////////////
	//FUNCIONES DE LA ACTUALIZACION DEL JUEGO//
	///////////////////////////////////////////
	
	//FUNCIONES STRINGS
	
	public String positionToString(int col, int row) {
		return container.toString(new Position(col, row));
	}
	
	public String infoToString() {
		return ShipFactory.infoToString();
	}
	
	public String stateToString() {
		StringBuilder str = new StringBuilder();
		str.append(Messages.LIFE + player.getLife() + "\n");
		str.append(Messages.SCORE + points + "\n");
		str.append(player.stringShockWave());
		return str.toString();
	}
	
	//FUNCIONES DEL UPDATE
	
	public void update() {
		container.computerActions();
		container.automaticMoves();
		this.currentCycle++;
	}
	
}
