package nc.noumea.mairie.sirh.eae.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import flexjson.JSONDeserializer;

@Service
public class SirhWSConsumer implements ISirhWsConsumer {

	@Autowired
	@Qualifier(value = "sirhWsEaeUrl")
	private String sirhWsEaeUrl;
	
	public String getSirhWsEaeUrl() {
		return sirhWsEaeUrl;
	}

	public void setSirhWsEaeUrl(String sirhWsEaeUrl) {
		this.sirhWsEaeUrl = sirhWsEaeUrl;
	}

	@Override
	public List<Integer> getListOfEaesForAgentId(int agentId) throws SirhWSConsumerException {

		ClientResponse response = createAndFireRequest(agentId);

		return readResponse(response, agentId);
	}

	public ClientResponse createAndFireRequest(int agentId) {
		
		Client client = Client.create();

		WebResource webResource = client
				.resource(getSirhWsEaeUrl())
				.queryParam("idAgent", String.valueOf(agentId));

		ClientResponse response = webResource.accept(
				MediaType.APPLICATION_JSON_VALUE).get(ClientResponse.class);
		
		return response;
	}
	
	public List<Integer> readResponse(ClientResponse response, int agentId) throws SirhWSConsumerException {
		
		List<Integer> result = new ArrayList<Integer>();

		if (response.getStatus() == HttpStatus.NO_CONTENT.value()) {
			return result;
		}

		if (response.getStatus() != HttpStatus.OK.value()) {
			throw new SirhWSConsumerException(
					String.format(
							"An error occured when querying '%s' with agentId '%d'. Return code is : %s",
							getSirhWsEaeUrl(), agentId, response.getStatus()));
		}

		String output = response.getEntity(String.class);
		
		result = new JSONDeserializer<List<Integer>>().deserialize(output);
		
		return result;
	}

}
