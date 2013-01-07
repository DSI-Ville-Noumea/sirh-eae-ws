package nc.noumea.mairie.sirh.eae.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;

import nc.noumea.mairie.sirh.eae.domain.enums.EaeReportFormatEnum;

import org.apache.commons.io.IOUtils;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

@Service
public class EaeReportingService implements IEaeReportingService {

	@Autowired
	@Qualifier("reportingBaseUrl")
	private String reportingBaseUrl;
	
	@Autowired
	@Qualifier("targetReportPath")
	private String remoteVirtualFolderPath;
	
	private static final String REPORT_PAGE = "frameset";
	private static final String REPORT_SERVER_PATH = "sirh-eae-reports/";
	private static final String PARAM_REPORT = "__report";
	private static final String PARAM_FORMAT = "__format";
	
	public EaeReportingService() {
		
	}
	
	public EaeReportingService(String _reportingBaseUrl, String _remoteVirtualFolderPath) {
		this.reportingBaseUrl = _reportingBaseUrl;
		this.remoteVirtualFolderPath = _remoteVirtualFolderPath;
	}
	
	@Override
	public void saveEaeReportToRemoteFileSystem(int idEae) throws EaeReportingServiceException {
		
		byte[] fileAsBytes = getEaeReportAsByteArray(idEae, EaeReportFormatEnum.PDF);
		
		saveFileToRemoteFileSystem(fileAsBytes, "filename.pdf");
	}

	@Override
	public void saveFileToRemoteFileSystem(byte[] fileAsBytes, String filename) throws EaeReportingServiceException {

		BufferedOutputStream bos = null;
		FileObject pdfFile = null;
		
		try {
			
			FileSystemManager fsManager = VFS.getManager();
			pdfFile = fsManager.resolveFile(String.format("%s%s", remoteVirtualFolderPath, filename));
			bos = new BufferedOutputStream(pdfFile.getContent().getOutputStream());
			IOUtils.write(fileAsBytes, bos);
			
		} catch (Exception e) {
			throw new EaeReportingServiceException(
					String.format(
							"An error occured while writing the report file to the following path '%s'.",
							reportingBaseUrl));
		} finally {
			
			IOUtils.closeQuietly(bos);
			
			if (pdfFile != null) {
				try {
					pdfFile.close();
				} catch (FileSystemException e) {
					// ignore the exception
				}
			}
		}
	}
	
	@Override
	public byte[] getEaeReportAsByteArray(int idEae, EaeReportFormatEnum format) throws EaeReportingServiceException {
		
		ClientResponse response = createAndFireRequest(idEae, format);
		
		return readResponseAsByteArray(response, idEae, format);
	}

	@Override
	public EaeReportFormatEnum getFileFormatFromString(String format) throws EaeReportingServiceException {
		
		EaeReportFormatEnum formatValue;
		
		if (format == null || format.equals(""))
			formatValue = EaeReportFormatEnum.PDF;
		else {
			try {
				formatValue = EaeReportFormatEnum.valueOf(format.toUpperCase());
			} catch(IllegalArgumentException ex) {
				throw new EaeReportingServiceException(String.format("Report file format not supported '%s'.", format));
			}
		}
		return formatValue;
	}

	public ClientResponse createAndFireRequest(int idEae, EaeReportFormatEnum format) {
		
		Client client = Client.create();

		WebResource webResource = client
				.resource(reportingBaseUrl + REPORT_PAGE)
				.queryParam(PARAM_REPORT, REPORT_SERVER_PATH + "eae.rptdesign")
				.queryParam(PARAM_FORMAT, format.toString())
				.queryParam("idEae", String.valueOf(idEae));

		ClientResponse response = webResource.get(ClientResponse.class);
		
		return response;
	}

	public byte[] readResponseAsByteArray(ClientResponse response, int idEae, EaeReportFormatEnum format) throws EaeReportingServiceException {
		
		if (response.getStatus() != HttpStatus.OK.value()) {
			throw new EaeReportingServiceException(
					String.format(
							"An error occured while querying the reporting server '%s' with eaeId '%s' and format '%s'. HTTP Status code is : %s.",
							reportingBaseUrl, idEae, format, response.getStatus()));
		}
		
		byte[] reponseData = null;
		File reportFile = null;
		
		try {
			reportFile = response.getEntity(File.class);
			reponseData =  IOUtils.toByteArray(new FileInputStream(reportFile));
		} catch (Exception e) {
			throw new EaeReportingServiceException("An error occured while reading the downloaded report.", e);
		} finally {
			if (reportFile != null && reportFile.exists())
				reportFile.delete();
		}
		
		return reponseData;
	}

}
