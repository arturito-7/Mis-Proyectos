package tp1.logic;

import tp1.Exceptions.*;

import tp1.control.InitialConfiguration;

public interface GameModel {
	
	public boolean isFinished();
	public void update();
	public String infoToString();
	
	//PLAYER ACTIONS
	public void move(Move move) throws GameModelException;
	public void shootLaser() throws GameModelException ;
	public void shootShockwave() throws GameModelException;
	public void shootSuperLaser() throws GameModelException ;
	public void reset(InitialConfiguration conf) throws GameModelException;
	public void exit();

}
