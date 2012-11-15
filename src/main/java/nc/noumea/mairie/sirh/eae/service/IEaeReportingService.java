package nc.noumea.mairie.sirh.eae.service;


public interface IEaeReportingService {
	
	void saveEaeReportToRemoteFileSystem(int idEae) throws EaeReportingServiceException;
	void saveFileToRemoteFileSystem(byte[] fileAsBytes, String filename) throws EaeReportingServiceException;
	byte[] getEaeReportAsByteArray(int idEae, String format) throws EaeReportingServiceException;

}
