package tp1.control.commands;

import java.util.Arrays;
import java.util.List;

import tp1.Exceptions.CommandParseException;
import tp1.view.Messages;

public class CommandGenerator {

	private static final List<Command> availableCommands = Arrays.asList(
		new HelpCommand(),
		new MoveCommand(),
		new ExitCommand(),
		new ListCommand(),
		new ResetCommand(),
		new NoneCommand(),
		new ShootCommand(),
		new ShockWaveCommand(),
		new ShootSuperLaserCommand()
	);

	public static Command parse(String[] commandWords) throws CommandParseException{		
		Command command = null;
		for (Command c: availableCommands) {
			if(c.matchCommandName(commandWords[0]))
				command = c.parse(commandWords);
		}
		if (command == null)
			throw new CommandParseException(Messages.UNKNOWN_COMMAND);
		return command;
	}
		
	public static String commandHelp() {
		StringBuilder commands = new StringBuilder();	
		for (Command c: availableCommands) {
			commands.append(c.helpText());
		}
		return commands.toString();
	}

}