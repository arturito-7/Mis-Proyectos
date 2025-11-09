package tp1.Exceptions;

public class GameModelException extends Exception{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GameModelException(String message) {
        super(message);
    }
    
    public GameModelException(Throwable cause) {
        super(cause);
    }
	
	public GameModelException(String message, Throwable cause) {
        super(message, cause);
    }
	
}
