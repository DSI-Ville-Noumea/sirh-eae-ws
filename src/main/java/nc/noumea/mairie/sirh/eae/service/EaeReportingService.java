package nc.noumea.mairie.sirh.eae.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;

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
	@Qualifier("remoteVirtualFolderPath")
	private String remoteVirtualFolderPath;
	
	private static final String REPORTING_PAGE = "frameset";
	private static final String REPORT_URL_REPORT_PARAM = "__report";
	private static final String REPORT_URL_FORMAT_PARAM = "__format";
	
	public EaeReportingService() {
		
	}
	
	public EaeReportingService(String _reportingBaseUrl, String _remoteVirtualFolderPath) {
		this.reportingBaseUrl = _reportingBaseUrl;
		this.remoteVirtualFolderPath = _remoteVirtualFolderPath;
	}
	
	@Override
	public void saveEaeReportToRemoteFileSystem(int idEae) throws EaeReportingServiceException {
		
		byte[] fileAsBytes = getEaeReportAsByteArray(idEae, "PDF");
		
		BufferedOutputStream bos = null;
		FileObject pdfFile = null;
		
		try {
			
			FileSystemManager fsManager = VFS.getManager();
			pdfFile = fsManager.resolveFile(String.format("%s%s", remoteVirtualFolderPath, "fileName.pdf"));
			bos = new BufferedOutputStream(pdfFile.getContent().getOutputStream());
			IOUtils.write(fileAsBytes, bos);
			
		} catch (Exception e) {
			throw new EaeReportingServiceException(
					String.format(
							"An error occured while writing the report file to the following path '%s' (eaeId '%s' and format 'PDF').",
							reportingBaseUrl, idEae));
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
	public byte[] getEaeReportAsByteArray(int idEae, String format) throws EaeReportingServiceException {
		
		ClientResponse response = createAndFireRequest(idEae, format);
		
		return readResponseAsByteArray(response, idEae, format);
	}

	public ClientResponse createAndFireRequest(int idEae, String format) {
		
		Client client = Client.create();

		WebResource webResource = client
				.resource(reportingBaseUrl + REPORTING_PAGE)
				.queryParam(REPORT_URL_REPORT_PARAM, "eae.rptdesign")
				.queryParam(REPORT_URL_FORMAT_PARAM, format)
				.queryParam("idEae", String.valueOf(idEae));

		ClientResponse response = webResource.get(ClientResponse.class);
		
		return response;
	}

	public byte[] readResponseAsByteArray(ClientResponse response, int idEae, String format) throws EaeReportingServiceException {
		
		if (response.getStatus() != HttpStatus.OK.value() || 
			!response.getHeaders().get("Content-Type").contains("application/pdf")) {
			throw new EaeReportingServiceException(
					String.format(
							"An error occured while querying the reporting server '%s' with eaeId '%s' and format '%s'. HTTP Status code is : %s.",
							reportingBaseUrl, idEae, format, response.getStatus()));
		}
		
		byte[] reponseData = null;
		File pdfFile = null;
		
		try {
			pdfFile = response.getEntity(File.class);
			reponseData =  IOUtils.toByteArray(new FileInputStream(pdfFile));
		} catch (Exception e) {
			throw new EaeReportingServiceException("An error occured while reading the downloaded report.", e);
		} finally {
			if (pdfFile != null && pdfFile.exists())
				pdfFile.delete();
		}
		
		return reponseData;
	}
}
