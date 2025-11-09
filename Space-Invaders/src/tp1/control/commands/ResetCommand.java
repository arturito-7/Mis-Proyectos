package tp1.control.commands;

import java.io.FileNotFoundException;
import java.io.IOException;

import tp1.Exceptions.CommandExecuteException;
import tp1.Exceptions.CommandParseException;
import tp1.Exceptions.GameModelException;
import tp1.control.InitialConfiguration;
import tp1.logic.GameModel;
import tp1.view.Messages;

public class ResetCommand extends Command{
		  		
	private InitialConfiguration conf;

	public ResetCommand() {}

	protected ResetCommand(InitialConfiguration conf) {
		this.conf = conf;
	}
	
	@Override
	public boolean execute(GameModel game) throws CommandExecuteException {
		try {
			game.reset(conf);
		}catch (GameModelException e) {
			throw new CommandExecuteException(Messages.INITIAL_CONFIGURATION_ERROR,e);
		}
		return true;
	}

	@Override
	public Command parse(String[] commandWords) throws CommandParseException {
		if (matchCommandName(commandWords[0])) {
	        if(commandWords.length == 2) {
	        	if(commandWords[1].equalsIgnoreCase("NONE") || commandWords[1].equalsIgnoreCase("N"))
	        		return new ResetCommand(InitialConfiguration.NONE);
	        	else {
		        	try {    
		        		InitialConfiguration conf = InitialConfiguration.readFromFile(commandWords[1]);
		        		return new ResetCommand(conf);
		        	}catch (FileNotFoundException e) {
		        		throw new CommandParseException(Messages.FILE_NOT_FOUND_ERROR + commandWords[1]);
	        		} catch (IOException e) {
		        		throw new CommandParseException(Messages.FILE_ERROR + commandWords[1]);
		        	}
	        	}
	        }
	        else
	        	if(commandWords.length == 1)
	        		return new ResetCommand(InitialConfiguration.NONE);
	        	else
	        		throw new CommandParseException(Messages.COMMAND_INCORRECT_PARAMETER_NUMBER);	
		}
        else
        	return null;
	}
	
	@Override
	protected String getName() {
		return Messages.COMMAND_RESET_NAME;
	}

	@Override
	protected String getShortcut() {
		return Messages.COMMAND_RESET_SHORTCUT;
	}

	@Override
	protected String getDetails() {
		return Messages.COMMAND_RESET_DETAILS;
	}

	@Override
	protected String getHelp() {
		return Messages.COMMAND_RESET_HELP;
	}
	
}