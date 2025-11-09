package tp1.Exceptions;

public class NoShockWaveException extends GameModelException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoShockWaveException(String message) {
        super(message);
    }
    
    public NoShockWaveException(Throwable cause) {
        super(cause);
    }
	
	public NoShockWaveException(String message, Throwable cause) {
        super(message, cause);
    }
	
}
