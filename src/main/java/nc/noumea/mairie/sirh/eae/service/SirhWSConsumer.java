package nc.noumea.mairie.sirh.eae.service;

import java.util.ArrayList;
import java.util.List;

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

	public ClientResponse createAndFireRequest(int agentId, int maxDepth, String url) throws SirhWSConsumerException {

		Client client = Client.create();

		WebResource webResource = client.resource(url).queryParam("idAgent", String.valueOf(agentId));

		if (maxDepth != 0)
			webResource.queryParam("maxDepth", String.valueOf(maxDepth));

		ClientResponse response = null;

		try {
			response = webResource.accept(MediaType.APPLICATION_JSON_VALUE).get(ClientResponse.class);
		} catch (ClientHandlerException ex) {
			throw new SirhWSConsumerException(String.format("An error occured when querying '%s' with agentId '%d'.", url, agentId), ex);
		}

		return response;
	}

	public List<Integer> readResponse(ClientResponse response, int agentId, String url) throws SirhWSConsumerException {

		List<Integer> result = new ArrayList<Integer>();

		if (response.getStatus() == HttpStatus.NO_CONTENT.value()) {
			return result;
		}

		if (response.getStatus() != HttpStatus.OK.value()) {
			throw new SirhWSConsumerException(String.format("An error occured when querying '%s' with agentId '%d'. Return code is : %s", url,
					agentId, response.getStatus()));
		}

		String output = response.getEntity(String.class);

		result = new JSONDeserializer<List<Integer>>().deserialize(output);

		return result;
	}

	@Override
	public List<Integer> getListOfSubAgentsForAgentId(int agentId) throws SirhWSConsumerException {

		ClientResponse response = createAndFireRequest(agentId, 3, getSirhWsAgensUrl());

		return readResponse(response, agentId, getSirhWsAgensUrl());
	}

	@Override
	public List<Integer> getListOfShdAgentsForAgentId(int agentId)
			throws SirhWSConsumerException {
		
		ClientResponse response = createAndFireRequest(agentId, 3, getSirhWsShdAgensUrl());

		return readResponse(response, agentId, getSirhWsShdAgensUrl());
	}

}
