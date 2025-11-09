package tp1.Exceptions;

public class LaserInFlightException extends GameModelException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LaserInFlightException(String message) {
        super(message);
    }
    
    public LaserInFlightException(Throwable cause) {
        super(cause);
    }
	
	public LaserInFlightException(String message, Throwable cause) {
        super(message, cause);
    }
	
}
