package nc.noumea.mairie.sirh.exception;

public class DaoException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1384896215540276578L;

	public DaoException() {
		super();
	}
	
	public DaoException(String message) {
		super(message);
	}
	
	public DaoException(String message, Throwable cause) {
		super(message, cause);
	}
}
