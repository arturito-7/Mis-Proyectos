package tp1.Exceptions;

public class NotEnoughPointsException extends GameModelException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NotEnoughPointsException(String message) {
        super(message);
    }
    
    public NotEnoughPointsException(Throwable cause) {
        super(cause);
    }
	
	public NotEnoughPointsException(String message, Throwable cause) {
        super(message, cause);
    }
	
}
