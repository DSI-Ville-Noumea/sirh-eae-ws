package nc.noumea.mairie.sirh.eae.service.dataConsistency;

public class EaeDataConsistencyServiceException extends Exception {

	private static final long serialVersionUID = 1L;

	public EaeDataConsistencyServiceException() {
		super();
	}
	
	public EaeDataConsistencyServiceException(String message) {
		super(message);
	}
	
	public EaeDataConsistencyServiceException(String message, Exception innerException) {
		super(message, innerException);
	}
}
