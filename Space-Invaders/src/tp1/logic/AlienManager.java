package tp1.logic;

import tp1.Exceptions.InitializationException;
import tp1.control.InitialConfiguration;
import tp1.logic.gameobjects.*;
import tp1.view.Messages;

public class AlienManager {
	
	private Level level;
	private Game game;
	private int remainingAliens;
	
	private boolean squadInFinalRow;
	private boolean onBorder;

	public AlienManager(Game game, Level level) {
		this.level = level;
		this.game = game;
		this.remainingAliens = 0;
		this.onBorder = false;
		this.squadInFinalRow = false;
	}
		
	// INITIALIZER METHODS
	
	public  GameObjectContainer initialize(InitialConfiguration conf) throws InitializationException {
		this.remainingAliens = 0;
		GameObjectContainer container = new GameObjectContainer();
		
		initializeOvni(container);
		
		if (conf == InitialConfiguration.NONE) {
			initializeRegularAliens(container);
			initializeDestroyerAliens(container);
		}else {
			costumedInitialization(container, conf);
		}
		
		return container;
	}
	
	private void initializeOvni(GameObjectContainer container) {
		container.add(new Ufo(game));
	}
	
	private void initializeRegularAliens (GameObjectContainer container) {

		int numRows = level.getNumRowsRegularAliens();
        int numCols = level.getNumRegularAliens() / numRows;
		
        for(int i = 0; i < numRows; i++) {
			for(int j = 0; j < numCols; j++) {
				container.add(new RegularAlien(game, new Position(2 + j,1 + i), this));
				this.remainingAliens++;
			}
        }
	}
	
	private void initializeDestroyerAliens(GameObjectContainer container) {

		int numRows = level.getNumRowsRegularAliens();
        int numCols = level.getNumDestroyerAliens();
				
		for(int j = 0; j < numCols; j++) {
			if(numCols == 2) {
				container.add(new DestroyerAlien(game, new Position(3 + j,numRows + 1), this)); 
				this.remainingAliens++;
			}else{
				container.add(new DestroyerAlien(game, new Position(2 + j,numRows + 1), this));
				this.remainingAliens++;
			}
		}
	}
	
	private void costumedInitialization(GameObjectContainer container, InitialConfiguration conf) throws InitializationException {
		for (String shipSymbol : conf.getShipDescription()) {
			String[] words = shipSymbol.toLowerCase().trim().split("\\s+");
			if(words.length == 3)
				try {
					container.add(ShipFactory.spawnAlienShip(words[0], game, new Position(Integer.valueOf(words[1]), Integer.valueOf(words[2])), this));
				}catch(NumberFormatException e) {
					throw new InitializationException(String.format(Messages.INVALID_POSITION, words[1], words[2]));
				}
			else 
				throw new InitializationException(Messages.INCORRECT_ENTRY + "'" + shipSymbol + "'. " + Messages.COMMAND_INCORRECT_PARAMETER_NUMBER);
			this.remainingAliens++;
		}
	}

	// CONTROL METHODS
		
	public void shipOnBorder() {
		if(!onBorder) {
			onBorder = true;
		}
	}

	public void decreaseOnBorder() {
		this.onBorder = false;
	}

	public boolean onBorder() {
		return onBorder;
	}
	
	public int getRemainingAliens() {
		return this.remainingAliens;
	}
	
	public void alienDead(AlienShip alienShip) {
		remainingAliens--;
	}
	
	public boolean allAliensDead() {
		return remainingAliens == 0;
	}
	
	public void finalRowReached() {
		this.squadInFinalRow = true;
	}
	
	public boolean haveLanded() {
		return this.squadInFinalRow;
	}

}
