package tp1.Exceptions;

public class InitializationException extends GameModelException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InitializationException(String message) {
        super(message);
    }
    
    public InitializationException(Throwable cause) {
        super(cause);
    }
	
	public InitializationException(String message, Throwable cause) {
        super(message, cause);
    }
	
}
