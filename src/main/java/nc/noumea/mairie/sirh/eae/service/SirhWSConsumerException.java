package nc.noumea.mairie.sirh.eae.service;

public class SirhWSConsumerException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SirhWSConsumerException() {
		super();
	}
	
	public SirhWSConsumerException(String message) {
		super(message);
	}
	
	public SirhWSConsumerException(String message, Exception innerException) {
		super(message, innerException);
	}
	
}
