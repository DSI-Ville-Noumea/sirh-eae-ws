package nc.noumea.mairie.sirh.eae.service;

public class EaeServiceException extends Exception  {

	private static final long serialVersionUID = 1L;

	public EaeServiceException() {
		super();
	}
	
	public EaeServiceException(String message) {
		super(message);
	}
	
	public EaeServiceException(String message, Exception innerException) {
		super(message, innerException);
	}
}
