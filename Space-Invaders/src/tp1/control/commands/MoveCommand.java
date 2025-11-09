package tp1.control.commands;


import tp1.Exceptions.CommandExecuteException;
import tp1.Exceptions.CommandParseException;
import tp1.Exceptions.GameModelException;
import tp1.Exceptions.NotAllowedMoveException;
import tp1.logic.GameModel;
import tp1.logic.Move;
import tp1.view.Messages;

public class MoveCommand extends Command {

	private Move move;

	public MoveCommand() {}

	protected MoveCommand(Move move) {
		this.move = move;
	}
	
	@Override
	public boolean execute(GameModel game) throws CommandExecuteException {
		try {
			game.move(this.move);
			game.update();
		} catch (GameModelException e) {
			throw new CommandExecuteException(Messages.MOVEMENT_ERROR, e);
		}
		return true;
	}
		
	@Override
	public Command parse(String[] commandWords) throws CommandParseException {
		if (matchCommandName(commandWords[0])) {
	        if(commandWords.length == 2) {
	        	try {
	        		Move move = Move.parseMovement(commandWords[1]);
	        		return new MoveCommand(move);
	        	}catch(NotAllowedMoveException e) {
	        		throw new CommandParseException(Messages.DIRECTION_ERROR + commandWords[1] ,e);
	        	}catch(IllegalArgumentException e) {
	        		throw new CommandParseException(Messages.DIRECTION_ERROR + commandWords[1] ,new Throwable(Messages.ALLOWED_MOVES));
	        	}
	        }
	        else
	        	throw new CommandParseException(Messages.COMMAND_INCORRECT_PARAMETER_NUMBER);
		}
        else
        	return null;
	}

	@Override
	protected String getName() {
		return Messages.COMMAND_MOVE_NAME;
	}

	@Override
	protected String getShortcut() {
		return Messages.COMMAND_MOVE_SHORTCUT;
	}

	@Override
	protected String getDetails() {
		return Messages.COMMAND_MOVE_DETAILS;
	}

	@Override
	protected String getHelp() {
		return Messages.COMMAND_MOVE_HELP;
	}

}