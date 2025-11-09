package tp1.Exceptions;

public class NotAllowedMoveException extends GameModelException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NotAllowedMoveException(String message) {
        super(message);
    }
    
    public NotAllowedMoveException(Throwable cause) {
        super(cause);
    }
	
	public NotAllowedMoveException(String message, Throwable cause) {
        super(message, cause);
    }
	
}
