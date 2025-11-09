package tp1.control.commands;

import tp1.Exceptions.CommandExecuteException;
import tp1.Exceptions.GameModelException;
import tp1.logic.GameModel;
import tp1.view.Messages;

public class ShootSuperLaserCommand extends NoParamsCommand{
		  		
	@Override
	public boolean execute(GameModel game) throws CommandExecuteException{
		try {
			game.shootSuperLaser();
			game.update();
		} catch (GameModelException e) {
			throw new CommandExecuteException(Messages.SUPERLASER_ERROR, e);
		}
		return true;
	}

	@Override
	protected String getName() {
		return Messages.COMMAND_SHOOT_SUPERLASER_NAME;
	}

	@Override
	protected String getShortcut() {
		return Messages.COMMAND_SHOOT_SUPERLASER_SHORTCUT;
	}

	@Override
	protected String getDetails() {
		return Messages.COMMAND_SHOOT_SUPERLASER_DETAILS;
	}

	@Override
	protected String getHelp() {
		return Messages.COMMAND_SHOOT_SUPERLASER_HELP;
	}

}