package tp1.logic;

import tp1.Exceptions.NotAllowedMoveException;
import tp1.view.Messages;

/**
 * Represents the allowed movements in the game
 *
 */
public enum Move {
	LEFT(-1,0),
	LLEFT(-2,0),
	RIGHT(1,0),
	RRIGHT(2,0), 
	DOWN(0,1), 
	UP(0,-1), 
	NONE(0,0);
	
	private int x;
	private int y;
	
	private Move(int x, int y) {
		this.x=x;
		this.y=y;
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public static Move parseMovement (String str) throws NotAllowedMoveException{
		Move move = Move.valueOf(str.toUpperCase());
		switch (move) {
		case LEFT:
		case LLEFT:
		case RIGHT:
		case RRIGHT:
			return move;
		default:
			throw new NotAllowedMoveException(Messages.ALLOWED_MOVES);
		}
	}
	
	public Move flip() {
		Move dirAux;
		if(this == LEFT) 
			dirAux = RIGHT;
		else 
			dirAux = LEFT;
	    return dirAux;
	}
	
}
