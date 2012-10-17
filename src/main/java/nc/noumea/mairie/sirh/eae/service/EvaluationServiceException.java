package nc.noumea.mairie.sirh.eae.service;

public class EvaluationServiceException extends Exception {

	private static final long serialVersionUID = 1L;

	public EvaluationServiceException() {
		super();
	}
	
	public EvaluationServiceException(String message) {
		super(message);
	}
	
	public EvaluationServiceException(String message, Exception innerException) {
		super(message, innerException);
	}
	
}
