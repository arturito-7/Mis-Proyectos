package tp1.Exceptions;

public class OffWorldException extends GameModelException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OffWorldException(String message) {
        super(message);
    }
    
    public OffWorldException(Throwable cause) {
        super(cause);
    }
	
	public OffWorldException(String message, Throwable cause) {
        super(message, cause);
    }
	
}
