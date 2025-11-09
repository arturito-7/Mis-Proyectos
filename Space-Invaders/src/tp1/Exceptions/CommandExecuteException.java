package tp1.Exceptions;

public class CommandExecuteException extends Exception {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CommandExecuteException(String message) {
        super(message);
    }
    
    public CommandExecuteException(Throwable cause) {
        super(cause);
    }
	
	public CommandExecuteException(String message, Throwable cause) {
        super(message, cause);
    }
	
}
