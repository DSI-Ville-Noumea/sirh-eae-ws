package nc.noumea.mairie.sirh.eae.service;

import nc.noumea.mairie.sirh.eae.domain.enums.EaeReportFormatEnum;


public interface IEaeReportingService {
	
	void saveEaeReportToRemoteFileSystem(int idEae) throws EaeReportingServiceException;
	void saveFileToRemoteFileSystem(byte[] fileAsBytes, String filename) throws EaeReportingServiceException;
	byte[] getEaeReportAsByteArray(int idEae, EaeReportFormatEnum format) throws EaeReportingServiceException;
	EaeReportFormatEnum getFileFormatFromString(String format) throws EaeReportingServiceException;
}
