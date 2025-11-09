package tp1.logic;

import java.util.Objects;

import tp1.view.Messages;

public class Position {

	private final int col;
	private final int row;


	public Position (int col, int row) {
		this.col = col;
		this.row = row;
	}
	
	public Position(Position pos) {
		this.col = pos.col;
		this.row = pos.row;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(col, row);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Position other = (Position) obj;
		return col == other.col && row == other.row;
	}
	
	/*
	public void actualizar(Move dir) {
		this.col+= dir.getX();
		this.row += dir.getY();
	}
	*/
	
	public Position actualizar(Move dir) {
		return new Position(this.col + dir.getX(),this.row + dir.getY());
	}
	
	
	public boolean isOut() {
		return (col < 0) || (col >= Game.DIM_X || (row < 0) || (row >= Game.DIM_Y));
	}

	public boolean isInFinalRow() {
		return this.row == Game.DIM_Y - 1;
	}

	public boolean inBorder() {
		return this.col == 0 || this.col == Game.DIM_X - 1;
	}

	public String toString() {
		return String.format(Messages.SHOW_POSITION, col, row);
	}
	
	public boolean isAround(Position pos) {
		return (this.col - pos.col <= 1 && this.col - pos.col >= -1) && 
				(this.row - pos.row <= 1 && this.row - pos.row >= -1);
	}
	
}
