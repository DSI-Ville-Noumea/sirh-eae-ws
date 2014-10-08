package nc.noumea.mairie.sirh.ws;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.noumea.mairie.sirh.domain.Agent;
import nc.noumea.mairie.sirh.eae.dto.AvancementEaeDto;
import nc.noumea.mairie.sirh.eae.dto.CalculEaeInfosDto;
import nc.noumea.mairie.sirh.eae.dto.agent.AutreAdministrationAgentDto;
import nc.noumea.mairie.sirh.eae.dto.agent.DateAvctDto;
import nc.noumea.mairie.sirh.eae.dto.poste.SpbhorDto;
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
	private static final String sirhAvancementUrl = "calculEae/avancement";
	private static final String sirhAvancementDetacheUrl = "calculEae/avancementDetache";
	private static final String sirhAffectationActiveByAgentUrl = "calculEae/affectationActiveByAgent";
	private static final String sirhListeAffectationsAgentAvecServiceUrl = "calculEae/listeAffectationsAgentAvecService";
	private static final String sirhListeAffectationsAgentAvecFPUrl = "calculEae/listeAffectationsAgentAvecFP";
	private static final String sirhAutreAdministrationAgentAncienneUrl = "calculEae/autreAdministrationAgentAncienne";
	private static final String sirhListeAutreAdministrationAgentUrl = "calculEae/listeAutreAdministrationAgent";
	private static final String sirhListSpbhorUrl = "fichePostes/listeSpbhor";
	private static final String sirhSpbhorByIdUrl = "fichePostes/spbhorById";
	private static final String sirhAgentUrl = "agents/agent";
	private static final String sirhDateAvctUrl = "calculEae/calculDateAvancement";

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

	private String getSirhAvancementUrl() {
		return sirhWsBaseUrl + sirhAvancementUrl;
	}

	private String getSirhAvancementDetacheUrl() {
		return sirhWsBaseUrl + sirhAvancementDetacheUrl;
	}

	private String getSirhAffectationActiveByAgentUrl() {
		return sirhWsBaseUrl + sirhAffectationActiveByAgentUrl;
	}

	private String getSirhListeAffectationsAgentAvecServiceUrl() {
		return sirhWsBaseUrl + sirhListeAffectationsAgentAvecServiceUrl;
	}

	private String getSirhListeAffectationsAgentAvecFPUrl() {
		return sirhWsBaseUrl + sirhListeAffectationsAgentAvecFPUrl;
	}

	private String getSirhAutreAdministrationAgentAncienneUrl() {
		return sirhWsBaseUrl + sirhAutreAdministrationAgentAncienneUrl;
	}

	private String getSirhListeAutreAdministrationAgentUrl() {
		return sirhWsBaseUrl + sirhListeAutreAdministrationAgentUrl;
	}

	private String getSirhListSpbhorUrl() {
		return sirhWsBaseUrl + sirhListSpbhorUrl;
	}

	private String getSirhSpbhorByIdUrl() {
		return sirhWsBaseUrl + sirhSpbhorByIdUrl;
	}

	private String getSirhAgentUrl() {
		return sirhWsBaseUrl + sirhAgentUrl;
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
			throw new SirhWSConsumerException(String.format("An error occured when querying '%s' with agentId '%d'.",
					url, agentId), ex);
		}

		return response;
	}

	public List<Integer> readResponse(ClientResponse response, int agentId, String url) throws SirhWSConsumerException {

		List<Integer> result = new ArrayList<Integer>();

		if (response.getStatus() == HttpStatus.NO_CONTENT.value()) {
			return result;
		}

		if (response.getStatus() != HttpStatus.OK.value()) {
			throw new SirhWSConsumerException(String.format(
					"An error occured when querying '%s' with agentId '%d'. Return code is : %s", url, agentId,
					response.getStatus()));
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
	public List<Integer> getListOfShdAgentsForAgentId(int agentId) throws SirhWSConsumerException {

		ClientResponse response = createAndFireRequest(agentId, 3, getSirhWsShdAgensUrl());

		return readResponse(response, agentId, getSirhWsShdAgensUrl());
	}

	public <T> T readResponseDto(Class<T> targetClass, ClientResponse response, String url)
			throws SirhWSConsumerException {

		T result = null;

		try {

			result = targetClass.newInstance();

		} catch (Exception ex) {
			throw new SirhWSConsumerException(
					"An error occured when instantiating return type when deserializing JSON from SIRH WS request.", ex);
		}

		if (response.getStatus() == HttpStatus.NO_CONTENT.value()) {
			return null;
		}

		if (response.getStatus() != HttpStatus.OK.value()) {
			throw new SirhWSConsumerException(String.format("An error occured when querying '%s'. Return code is : %s",
					url, response.getStatus()));
		}

		String output = response.getEntity(String.class);
		result = new JSONDeserializer<T>().use(Date.class, new MSDateTransformer()).deserializeInto(output, result);
		return result;
	}

	public <T> List<T> readResponseAsList(Class<T> targetClass, ClientResponse response, String url)
			throws SirhWSConsumerException {
		List<T> result = null;
		result = new ArrayList<T>();

		if (response.getStatus() == HttpStatus.NO_CONTENT.value()) {
			return result;
		}

		if (response.getStatus() != HttpStatus.OK.value()) {
			throw new SirhWSConsumerException(String.format(
					"An error occured when querying '%s'. Return code is : %s, content is %s", url,
					response.getStatus(), response.getEntity(String.class)));
		}

		String output = response.getEntity(String.class);

		result = new JSONDeserializer<List<T>>().use(null, ArrayList.class).use("values", targetClass)
				.use(Date.class, new MSDateTransformer()).deserialize(output);

		return result;
	}

	private ClientResponse createAndFireRequestWithParameter(Map<String, String> parameters, String url)
			throws SirhWSConsumerException {

		Client client = Client.create();
		WebResource webResource = client.resource(url);

		for (String key : parameters.keySet()) {
			webResource = webResource.queryParam(key, parameters.get(key));
		}

		ClientResponse response = null;

		try {
			response = webResource.accept(MediaType.APPLICATION_JSON_VALUE).get(ClientResponse.class);
		} catch (ClientHandlerException ex) {
			throw new SirhWSConsumerException(String.format("An error occured when querying '%s'.", url), ex);
		}

		return response;
	}

	@Override
	public AvancementEaeDto getAvancement(Integer idAgent, Integer anneeAvancement, boolean isFonctionnaire)
			throws SirhWSConsumerException {

		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("idAgent", String.valueOf(idAgent));
		parameters.put("anneeAvancement", String.valueOf(anneeAvancement));
		parameters.put("isFonctionnaire", String.valueOf(isFonctionnaire));

		ClientResponse res = createAndFireRequestWithParameter(parameters, getSirhAvancementUrl());

		return readResponseDto(AvancementEaeDto.class, res, getSirhAvancementUrl());
	}

	@Override
	public AvancementEaeDto getAvancementDetache(Integer idAgent, Integer anneeAvancement)
			throws SirhWSConsumerException {

		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("idAgent", String.valueOf(idAgent));
		parameters.put("anneeAvancement", String.valueOf(anneeAvancement));

		ClientResponse res = createAndFireRequestWithParameter(parameters, getSirhAvancementDetacheUrl());

		return readResponseDto(AvancementEaeDto.class, res, getSirhAvancementDetacheUrl());
	}

	@Override
	public CalculEaeInfosDto getDetailAffectationActiveByAgent(Integer idAgent, Integer anneeFormation)
			throws SirhWSConsumerException {

		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("idAgent", String.valueOf(idAgent));
		parameters.put("anneeFormation", String.valueOf(anneeFormation));

		ClientResponse res = createAndFireRequestWithParameter(parameters, getSirhAffectationActiveByAgentUrl());

		return readResponseDto(CalculEaeInfosDto.class, res, getSirhAffectationActiveByAgentUrl());
	}

	@Override
	public List<CalculEaeInfosDto> getListeAffectationsAgentAvecService(Integer idAgent, String idService)
			throws SirhWSConsumerException {

		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("idAgent", String.valueOf(idAgent));
		parameters.put("idService", idService);

		ClientResponse res = createAndFireRequestWithParameter(parameters,
				getSirhListeAffectationsAgentAvecServiceUrl());

		return readResponseAsList(CalculEaeInfosDto.class, res, getSirhListeAffectationsAgentAvecServiceUrl());
	}

	@Override
	public List<CalculEaeInfosDto> getListeAffectationsAgentAvecFP(Integer idAgent, Integer idFichePoste)
			throws SirhWSConsumerException {

		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("idAgent", String.valueOf(idAgent));
		parameters.put("idFichePoste", String.valueOf(idFichePoste));

		ClientResponse res = createAndFireRequestWithParameter(parameters, getSirhListeAffectationsAgentAvecFPUrl());

		return readResponseAsList(CalculEaeInfosDto.class, res, getSirhListeAffectationsAgentAvecFPUrl());
	}

	@Override
	public AutreAdministrationAgentDto chercherAutreAdministrationAgentAncienne(Integer idAgent, boolean isFonctionnaire)
			throws SirhWSConsumerException {

		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("idAgent", String.valueOf(idAgent));
		parameters.put("isFonctionnaire", String.valueOf(isFonctionnaire));

		ClientResponse res = createAndFireRequestWithParameter(parameters, getSirhAutreAdministrationAgentAncienneUrl());

		return readResponseDto(AutreAdministrationAgentDto.class, res, getSirhAutreAdministrationAgentAncienneUrl());
	}

	@Override
	public List<AutreAdministrationAgentDto> getListeAutreAdministrationAgent(Integer idAgent)
			throws SirhWSConsumerException {

		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("idAgent", String.valueOf(idAgent));

		ClientResponse res = createAndFireRequestWithParameter(parameters, getSirhListeAutreAdministrationAgentUrl());

		return readResponseAsList(AutreAdministrationAgentDto.class, res, getSirhListeAutreAdministrationAgentUrl());
	}

	@Override
	public List<SpbhorDto> getListSpbhor() throws SirhWSConsumerException {

		Map<String, String> parameters = new HashMap<String, String>();

		ClientResponse res = createAndFireRequestWithParameter(parameters, getSirhListSpbhorUrl());

		return readResponseAsList(SpbhorDto.class, res, getSirhListSpbhorUrl());
	}

	@Override
	public SpbhorDto getSpbhorById(Integer idSpbhor) throws SirhWSConsumerException {

		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("idSpbhor", String.valueOf(idSpbhor));

		ClientResponse res = createAndFireRequestWithParameter(parameters, getSirhSpbhorByIdUrl());

		return readResponseDto(SpbhorDto.class, res, getSirhSpbhorByIdUrl());
	}

	@Override
	public Agent getAgent(Integer idAgent) throws SirhWSConsumerException {

		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("idAgent", String.valueOf(idAgent));

		ClientResponse res = createAndFireRequestWithParameter(parameters, getSirhAgentUrl());

		return readResponseDto(Agent.class, res, getSirhAgentUrl());
	}

	@Override
	public DateAvctDto getCalculDateAvct(Integer idAgent) throws SirhWSConsumerException {

		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("idAgent", String.valueOf(idAgent));

		ClientResponse res = createAndFireRequestWithParameter(parameters, getSirhdateavcturl());

		return readResponseDto(DateAvctDto.class, res, getSirhdateavcturl());
	}

	private String getSirhdateavcturl() {
		return sirhWsBaseUrl + sirhDateAvctUrl;
	}
}
