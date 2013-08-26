package nc.noumea.mairie.sirh.eae.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.noumea.mairie.sirh.eae.dto.CampagneEaeDto;
import nc.noumea.mairie.sirh.tools.transformer.MSDateTransformer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import flexjson.JSONDeserializer;

@Service
public class SirhWSConsumer implements ISirhWsConsumer {

	@Autowired
	@Qualifier("sirhWsBaseUrl")
	private String sirhWsBaseUrl;

	private static final String sirhAgentsUrl = "agents/sousAgents";
	private static final String sirhShdAgentsUrl = "agents/agentsShd";
	private static final String sirhCampagneEnCours = "eaes/getCampagneEnCours";

	public String getSirhWsBaseUrl() {
		return sirhWsBaseUrl;
	}

	public void setSirhWsBaseUrl(String sirhWsBaseUrl) {
		this.sirhWsBaseUrl = sirhWsBaseUrl;
	}

	private String getSirhWsAgensUrl() {
		return sirhWsBaseUrl + sirhAgentsUrl;
	}

	private String getSirhWsShdAgensUrl() {
		return sirhWsBaseUrl + sirhShdAgentsUrl;
	}

	private String getSirhWsCampagneEnCoursUrl() {
		return sirhWsBaseUrl + sirhCampagneEnCours;
	}

	public ClientResponse createAndFireRequest(int agentId, int maxDepth,
			String url) throws SirhWSConsumerException {

		Client client = Client.create();

		WebResource webResource = client.resource(url).queryParam("idAgent",
				String.valueOf(agentId));

		if (maxDepth != 0)
			webResource.queryParam("maxDepth", String.valueOf(maxDepth));

		ClientResponse response = null;

		try {
			response = webResource.accept(MediaType.APPLICATION_JSON_VALUE)
					.get(ClientResponse.class);
		} catch (ClientHandlerException ex) {
			throw new SirhWSConsumerException(String.format(
					"An error occured when querying '%s' with agentId '%d'.",
					url, agentId), ex);
		}

		return response;
	}

	public List<Integer> readResponse(ClientResponse response, int agentId,
			String url) throws SirhWSConsumerException {

		List<Integer> result = new ArrayList<Integer>();

		if (response.getStatus() == HttpStatus.NO_CONTENT.value()) {
			return result;
		}

		if (response.getStatus() != HttpStatus.OK.value()) {
			throw new SirhWSConsumerException(
					String.format(
							"An error occured when querying '%s' with agentId '%d'. Return code is : %s",
							url, agentId, response.getStatus()));
		}

		String output = response.getEntity(String.class);

		result = new JSONDeserializer<List<Integer>>().deserialize(output);

		return result;
	}

	@Override
	public List<Integer> getListOfSubAgentsForAgentId(int agentId)
			throws SirhWSConsumerException {

		ClientResponse response = createAndFireRequest(agentId, 3,
				getSirhWsAgensUrl());

		return readResponse(response, agentId, getSirhWsAgensUrl());
	}

	@Override
	public List<Integer> getListOfShdAgentsForAgentId(int agentId)
			throws SirhWSConsumerException {

		ClientResponse response = createAndFireRequest(agentId, 3,
				getSirhWsShdAgensUrl());

		return readResponse(response, agentId, getSirhWsShdAgensUrl());
	}

	@Override
	public CampagneEaeDto getCampagneEnCours() throws SirhWSConsumerException {

		Map<String, String> parameters = new HashMap<String, String>();

		ClientResponse res = createAndFireRequestWithParameter(parameters,
				getSirhWsCampagneEnCoursUrl());

		return readResponseDto(CampagneEaeDto.class, res,
				getSirhWsCampagneEnCoursUrl());
	}

	public <T> T readResponseDto(Class<T> targetClass, ClientResponse response,
			String url) throws SirhWSConsumerException {

		T result = null;

		try {

			result = targetClass.newInstance();

		} catch (Exception ex) {
			throw new SirhWSConsumerException(
					"An error occured when instantiating return type when deserializing JSON from SIRH WS request.",
					ex);
		}

		if (response.getStatus() == HttpStatus.NO_CONTENT.value()) {
			return null;
		}

		if (response.getStatus() != HttpStatus.OK.value()) {
			throw new SirhWSConsumerException(String.format(
					"An error occured when querying '%s'. Return code is : %s",
					url, response.getStatus()));
		}

		String output = response.getEntity(String.class);
        result = new JSONDeserializer<T>().use(Date.class, new MSDateTransformer()).deserializeInto(output, result);
        return result;
	}

	private ClientResponse createAndFireRequestWithParameter(
			Map<String, String> parameters, String url)
			throws SirhWSConsumerException {

		Client client = Client.create();
		WebResource webResource = client.resource(url);

		for (String key : parameters.keySet()) {
			webResource = webResource.queryParam(key, parameters.get(key));
		}

		ClientResponse response = null;

		try {
			response = webResource.accept(MediaType.APPLICATION_JSON_VALUE)
					.get(ClientResponse.class);
		} catch (ClientHandlerException ex) {
			throw new SirhWSConsumerException(String.format(
					"An error occured when querying '%s'.", url), ex);
		}

		return response;
	}

}
