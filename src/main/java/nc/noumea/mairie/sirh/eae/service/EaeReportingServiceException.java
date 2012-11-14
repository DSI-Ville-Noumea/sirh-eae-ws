package nc.noumea.mairie.sirh.eae.service;

public class EaeReportingServiceException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public EaeReportingServiceException() {
		super();
	}
	
	public EaeReportingServiceException(String message) {
		super(message);
	}
	
	public EaeReportingServiceException(String message, Exception innerException) {
		super(message, innerException);
	}
	
}
