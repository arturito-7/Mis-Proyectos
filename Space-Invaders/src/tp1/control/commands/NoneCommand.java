package tp1.control.commands;

import tp1.logic.GameModel;
import tp1.view.Messages;

public class NoneCommand extends NoParamsCommand{
	  		
	@Override
	public boolean execute(GameModel game) {
		game.update();
		return true;
	}
	
	@Override
	protected boolean matchCommandName(String name) {
	    return super.matchCommandName(name) || name.equalsIgnoreCase("");
	}

	@Override
	protected String getName() {
		return Messages.COMMAND_NONE_NAME;
	}

	@Override
	protected String getShortcut() {
		return Messages.COMMAND_NONE_SHORTCUT;
	}

	@Override
	protected String getDetails() {
		return Messages.COMMAND_NONE_DETAILS;
	}

	@Override
	protected String getHelp() {
		return Messages.COMMAND_NONE_HELP;
	}
	
}