package nc.noumea.mairie.sirh.ws;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import flexjson.JSONDeserializer;
import nc.noumea.mairie.alfresco.ws.WSConsumerException;
import nc.noumea.mairie.sirh.tools.transformer.MSDateTransformer;

public abstract class BaseWsConsumer {

	private Logger logger = LoggerFactory.getLogger(BaseWsConsumer.class);

	public ClientResponse createAndFireGetRequest(Map<String, String> parameters, String url) {
		return createAndFireRequest(parameters, url, false, null);
	}

	public ClientResponse createAndFirePostRequest(Map<String, String> parameters, String url) {
		return createAndFireRequest(parameters, url, true, null);
	}

	public ClientResponse createAndFireRequest(Map<String, String> parameters, String url, boolean isPost, String postContent) {

		Client client = Client.create();
		WebResource webResource = client.resource(url);

		for (String key : parameters.keySet()) {
			webResource = webResource.queryParam(key, parameters.get(key));
		}

		ClientResponse response = null;

		try {
			if (isPost)
				if (postContent != null)
					response = webResource.accept(MediaType.APPLICATION_JSON_VALUE).post(ClientResponse.class);
				else
					response = webResource.accept(MediaType.APPLICATION_JSON_VALUE).post(ClientResponse.class, postContent);
			else
				response = webResource.accept(MediaType.APPLICATION_JSON_VALUE).get(ClientResponse.class);
		} catch (ClientHandlerException ex) {
			throw new WSConsumerException(String.format("An error occured when querying '%s'.", url), ex);
		}

		return response;
	}

	public void readResponse(ClientResponse response, String url) {

		if (response.getStatus() == HttpStatus.OK.value())
			return;

		throw new WSConsumerException(String.format("An error occured when querying '%s'. Return code is : %s, content is %s", url, response.getStatus(), response.getEntity(String.class)));
	}

	public <T> T readResponse(Class<T> targetClass, ClientResponse response, String url) {

		T result = null;

		try {

			result = targetClass.newInstance();

		} catch (Exception ex) {
			throw new WSConsumerException("An error occured when instantiating return type when deserializing JSON from WS request.", ex);
		}

		if (response.getStatus() == HttpStatus.NO_CONTENT.value()) {
			return null;
		}

		if (response.getStatus() != HttpStatus.OK.value()) {
			throw new WSConsumerException(String.format("An error occured when querying '%s'. Return code is : %s, content is %s", url, response.getStatus(), response.getEntity(String.class)));
		}

		String output = response.getEntity(String.class);

		result = new JSONDeserializer<T>().use(Date.class, new MSDateTransformer()).deserializeInto(output, result);

		return result;
	}

	public <T> List<T> readResponseAsList(Class<T> targetClass, ClientResponse response, String url) {
		List<T> result = null;
		result = new ArrayList<T>();

		if (response.getStatus() == HttpStatus.NO_CONTENT.value()) {
			return result;
		}

		if (response.getStatus() != HttpStatus.OK.value()) {
			throw new WSConsumerException(String.format("An error occured when querying '%s'. Return code is : %s, content is %s", url, response.getStatus(), response.getEntity(String.class)));
		}

		String output = response.getEntity(String.class);

		result = new JSONDeserializer<List<T>>().use(null, ArrayList.class).use("values", targetClass).use(Date.class, new MSDateTransformer()).deserialize(output);

		return result;
	}

	protected byte[] readResponseAsByteArray(ClientResponse response, String url) throws Exception {

		if (response.getStatus() != HttpStatus.OK.value()) {
			logger.error(String.format("An error occured when querying '%s'. Return code is : %s", url, response.getStatus()));
			throw new RuntimeException(String.format("An error occured when querying '%s'. Return code is : %s", url, response.getStatus()));
		}

		byte[] reponseData = null;
		File reportFile = null;

		try {
			reportFile = response.getEntity(File.class);
			reponseData = IOUtils.toByteArray(new FileInputStream(reportFile));
		} catch (Exception e) {
			logger.error("Erreur dans readResponseAsByteArray" + e.getMessage());
			throw new Exception("An error occured while reading the downloaded report.", e);
		} finally {
			if (reportFile != null && reportFile.exists())
				reportFile.delete();
		}

		return reponseData;
	}

	protected FileInputStream readResponseAsInputStream(ClientResponse response, String url) throws Exception {

		if (response.getStatus() != HttpStatus.OK.value()) {
			logger.error(String.format("An error occured when querying '%s'. Return code is : %s", url, response.getStatus()));
			throw new RuntimeException(String.format("An error occured when querying '%s'. Return code is : %s", url, response.getStatus()));
		}

		FileInputStream reponseData = null;
		File reportFile = null;

		try {
			reportFile = response.getEntity(File.class);
			reponseData = new FileInputStream(reportFile);
		} catch (Exception e) {
			logger.error("Erreur dans readResponseAsByteArray" + e.getMessage());
			throw new Exception("An error occured while reading the downloaded report.", e);
		} finally {
			if (reportFile != null && reportFile.exists())
				reportFile.delete();
		}

		return reponseData;
	}
}
