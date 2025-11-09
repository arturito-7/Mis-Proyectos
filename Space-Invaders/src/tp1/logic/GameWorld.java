package tp1.logic;

import java.util.Random;

import tp1.logic.gameobjects.GameObject;

public interface GameWorld {
	
	public static final int DIM_X = 9;
	public static final int DIM_Y = 8;

	public void addObject(GameObject object);
	public void removeObject(GameObject object);
	
	public void cambiarLaser();
	public void cambiarShockWave();
	
	public Random getRandom();
	public Level getLevel();
	public int getNumCyclesToMoveOneCell();
	public void recievePoints(int puntos);
	
	public int getRemainingAliens();
	public void performExplosiveAttack(GameObject alien);
		
}

