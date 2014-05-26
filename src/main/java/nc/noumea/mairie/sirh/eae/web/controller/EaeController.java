package nc.noumea.mairie.sirh.eae.web.controller;

import java.util.Date;
import java.util.List;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeReportFormatEnum;
import nc.noumea.mairie.sirh.eae.dto.CampagneEaeDto;
import nc.noumea.mairie.sirh.eae.dto.CanFinalizeEaeDto;
import nc.noumea.mairie.sirh.eae.dto.EaeDashboardItemDto;
import nc.noumea.mairie.sirh.eae.dto.EaeEvalueNameDto;
import nc.noumea.mairie.sirh.eae.dto.EaeFinalizationDto;
import nc.noumea.mairie.sirh.eae.dto.EaeListItemDto;
import nc.noumea.mairie.sirh.eae.dto.FinalizationInformationDto;
import nc.noumea.mairie.sirh.eae.dto.ReturnMessageDto;
import nc.noumea.mairie.sirh.eae.security.IEaeSecurityProvider;
import nc.noumea.mairie.sirh.eae.service.EaeReportingServiceException;
import nc.noumea.mairie.sirh.eae.service.EaeServiceException;
import nc.noumea.mairie.sirh.eae.service.IAgentMatriculeConverterService;
import nc.noumea.mairie.sirh.eae.service.IEaeReportingService;
import nc.noumea.mairie.sirh.eae.service.IEaeService;
import nc.noumea.mairie.sirh.ws.SirhWSConsumerException;
import nc.noumea.mairie.sirh.tools.transformer.MSDateTransformer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Controller
@RequestMapping("/eaes")
public class EaeController {

	private Logger logger = LoggerFactory.getLogger(EaeController.class);

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private IEaeService eaeService;

	@Autowired
	private IAgentMatriculeConverterService agentMatriculeConverterService;

	@Autowired
	private IEaeSecurityProvider eaeSecurityProvider;

	@Autowired
	private IEaeReportingService eaeReportingService;

	@ResponseBody
	@RequestMapping(value = "listEaesByAgent", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public ResponseEntity<String> listEaesByAgent(@RequestParam("idAgent") int idAgent) {

		logger.debug("entered GET [eaes/listEaesByAgent] => listEaesByAgent with parameter idAgent = {}", idAgent);

		Integer convertedId = agentMatriculeConverterService.tryConvertFromADIdAgentToEAEIdAgent(idAgent);

		List<EaeListItemDto> result;
		try {
			result = eaeService.listEaesByAgentId(convertedId);
		} catch (SirhWSConsumerException e) {
			logger.error(e.getMessage(), e);
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (result.isEmpty())
			return new ResponseEntity<String>(HttpStatus.NO_CONTENT);

		String jsonResult = EaeListItemDto.getSerializerForEaeListItemDto().serialize(result);

		return new ResponseEntity<String>(jsonResult, HttpStatus.OK);
	}

	@ResponseBody
	@RequestMapping(value = "initialiserEae", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
	@Transactional(value = "eaeTransactionManager")
	public ResponseEntity<String> initializeEae(@RequestParam("idAgent") int idAgent,
			@RequestParam("idEvalue") int idEvalue) {

		logger.debug("entered GET [eaes/initialiserEae] => initializeEae with parameter idAgent = {} , idEvalue = {}",
				idAgent, idEvalue);

		Integer convertedIdAgentEvalue = agentMatriculeConverterService.tryConvertFromADIdAgentToEAEIdAgent(idEvalue);

		Integer convertedIdAgentEvaluateur = agentMatriculeConverterService
				.tryConvertFromADIdAgentToEAEIdAgent(idAgent);

		List<Eae> agentEaes = eaeService.findCurrentAndPreviousEaesByAgentId(convertedIdAgentEvalue);

		if (agentEaes.isEmpty())
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);

		Eae lastEae = agentEaes.get(0);
		Eae previousEae = null;
		if (agentEaes.size() > 1)
			previousEae = agentEaes.get(1);

		ResponseEntity<String> response = eaeSecurityProvider.checkEaeAndWriteRight(lastEae.getIdEae(),
				convertedIdAgentEvaluateur);

		if (response != null)
			return response;

		try {
			eaeService.initializeEae(lastEae, previousEae);
		} catch (EaeServiceException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		}

		return new ResponseEntity<String>(messageSource.getMessage("EAE_INITIALISE_OK", null, null), HttpStatus.OK);
	}

	@ResponseBody
	@RequestMapping(value = "affecterDelegataire", method = RequestMethod.GET)
	@Transactional(value = "eaeTransactionManager")
	public ResponseEntity<String> setDelegataire(@RequestParam("idEae") int idEae,
			@RequestParam("idAgent") int idAgent, @RequestParam("idDelegataire") int idDelegataire) {

		logger.debug(
				"entered GET [eaes/affecterDelegataire] => setDelegataire with parameter idAgent = {} , idDelegataire = {}",
				idAgent, idDelegataire);

		ResponseEntity<String> response = eaeSecurityProvider.checkEaeAndWriteRight(idEae, idAgent);

		if (response != null)
			return response;

		Integer convertedIdAgentDelegataire = agentMatriculeConverterService
				.tryConvertFromADIdAgentToEAEIdAgent(idDelegataire);

		Eae eae = eaeService.getEae(idEae);

		try {
			eaeService.setDelegataire(eae, convertedIdAgentDelegataire);
		} catch (EaeServiceException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		}

		return new ResponseEntity<String>(messageSource.getMessage("EAE_DELEGATAIRE_OK", null, null), HttpStatus.OK);
	}

	@ResponseBody
	@RequestMapping(value = "tableauDeBord", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public ResponseEntity<String> getEaesDashboard(@RequestParam("idAgent") int idAgent) {

		logger.debug("entered GET [eaes/tableauDeBord] => getEaesDashboard with parameter idAgent = {} ", idAgent);

		Integer convertedId = agentMatriculeConverterService.tryConvertFromADIdAgentToEAEIdAgent(idAgent);

		List<EaeDashboardItemDto> result;
		try {
			result = eaeService.getEaesDashboard(convertedId);
		} catch (SirhWSConsumerException e) {
			logger.error(e.getMessage(), e);
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (result.isEmpty())
			return new ResponseEntity<String>(HttpStatus.NO_CONTENT);

		String jsonResult = EaeDashboardItemDto.getSerializerForEaeDashboardItemDto().serialize(result);

		return new ResponseEntity<String>(jsonResult, HttpStatus.OK);
	}

	@ResponseBody
	@RequestMapping(value = "canFinalizeEae", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
	@Transactional(value = "eaeTransactionManager")
	public ResponseEntity<String> canFinalizeEae(@RequestParam("idEae") int idEae, @RequestParam("idAgent") int idAgent) {

		logger.debug("entered GET [eaes/canFinalizeEae] => canFinalizeEae with parameter idAgent = {} , idEae = {}",
				idAgent, idEae);

		ResponseEntity<String> response = eaeSecurityProvider.checkEaeAndWriteRight(idEae, idAgent);

		if (response != null)
			return response;

		Eae eae = eaeService.getEae(idEae);

		CanFinalizeEaeDto dto = eaeService.canFinalizEae(eae);

		if (!dto.isCanFinalize())
			return new ResponseEntity<String>(dto.getMessage(), HttpStatus.CONFLICT);
		else
			return new ResponseEntity<String>(HttpStatus.OK);
	}

	@ResponseBody
	@RequestMapping(value = "getFinalizationInformation", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public ResponseEntity<String> getFinalizationInformation(@RequestParam("idEae") int idEae,
			@RequestParam("idAgent") int idAgent) {

		logger.debug(
				"entered GET [eaes/getFinalizationInformation] => getFinalizationInformation with parameter idAgent = {} , idEae = {}",
				idAgent, idEae);

		ResponseEntity<String> response = eaeSecurityProvider.checkEaeAndWriteRight(idEae, idAgent);

		if (response != null)
			return response;

		Eae eae = eaeService.getEae(idEae);
		FinalizationInformationDto dto = null;

		try {
			dto = eaeService.getFinalizationInformation(eae);
		} catch (SirhWSConsumerException e) {
			logger.error(e.getMessage(), e);
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		String jsonResult = dto.serializeInJSON();

		return new ResponseEntity<String>(jsonResult, HttpStatus.OK);
	}

	@ResponseBody
	@RequestMapping(value = "finalizeEae", produces = "application/json;charset=utf-8", method = RequestMethod.POST)
	@Transactional(value = "eaeTransactionManager")
	public ResponseEntity<String> finalizeEae(@RequestParam("idEae") int idEae, @RequestParam("idAgent") int idAgent,
			@RequestBody String eaeFinalizationDtoJson) {

		logger.debug("entered POST [eaes/finalizeEae] => finalizeEae with parameter idAgent = {} , idEae = {}",
				idAgent, idEae);

		ResponseEntity<String> response = eaeSecurityProvider.checkEaeAndWriteRight(idEae, idAgent);

		if (response != null)
			return response;

		Eae eae = eaeService.getEae(idEae);

		try {
			EaeFinalizationDto dto = new EaeFinalizationDto().deserializeFromJSON(eaeFinalizationDtoJson);
			eaeService
					.finalizEae(eae, agentMatriculeConverterService.tryConvertFromADIdAgentToEAEIdAgent(idAgent), dto);
			eaeService.flush();
		} catch (EaeServiceException e) {
			eaeService.clear();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		}

		return new ResponseEntity<String>(messageSource.getMessage("EAE_FINALISE_OK", null, null), HttpStatus.OK);
	}

	@SuppressWarnings("rawtypes")
	@ResponseBody
	@RequestMapping(value = "downloadEae", method = RequestMethod.GET)
	@Transactional(value = "eaeTransactionManager", readOnly = true)
	public ResponseEntity downloadEae(@RequestParam("idEae") int idEae, @RequestParam("idAgent") int idAgent,
			@RequestParam(value = "format", required = false) String format) {

		logger.debug("entered GET [eaes/downloadEae] => downloadEae with parameter idAgent = {} , idEae = {}", idAgent,
				idEae);

		ResponseEntity<String> response = eaeSecurityProvider.checkEaeAndReadRight(idEae, idAgent);

		if (response != null)
			return response;

		byte[] responseData = null;
		EaeReportFormatEnum formatValue = null;

		try {
			formatValue = eaeReportingService.getFileFormatFromString(format);
			responseData = eaeReportingService.getEaeReportAsByteArray(idEae, formatValue);
		} catch (EaeReportingServiceException e) {
			logger.error(e.getMessage(), e);
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		}

		return new ResponseEntity<byte[]>(responseData, getHeadersForFileFormat(formatValue, idEae), HttpStatus.OK);
	}

	private HttpHeaders getHeadersForFileFormat(EaeReportFormatEnum format, int idEae) {

		String contentType;

		switch (format) {
			default:
			case PDF:
				contentType = "application/pdf";
				break;
			case DOC:
				contentType = "application/msword";
		}

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", contentType);
		headers.add("Content-Disposition",
				String.format("attachment; filename=\"%s.%s\"", idEae, format.toString().toLowerCase()));

		return headers;
	}

	@ResponseBody
	@RequestMapping(value = "getEaeEvalueFullname", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
	@Transactional(value = "eaeTransactionManager", readOnly = true)
	public ResponseEntity<String> getEvalueFullname(@RequestParam("idEae") int idEae,
			@RequestParam("idAgent") int idAgent) {

		logger.debug(
				"entered GET [eaes/getEaeEvalueFullname] => getEvalueFullname with parameter idAgent = {} , idEae = {}",
				idAgent, idEae);

		ResponseEntity<String> response = eaeSecurityProvider.checkEaeAndReadRight(idEae, idAgent);

		if (response != null)
			return response;

		Eae eae = eaeService.getEae(idEae);
		EaeEvalueNameDto fullName = eaeService.getEvalueName(eae);

		return new ResponseEntity<String>(fullName.serializeInJSON(), HttpStatus.OK);
	}

	/*
	 * POUR SIRH-WS
	 */
	@ResponseBody
	@RequestMapping(value = "getEaeCampagneOuverte", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
	@Transactional(value = "eaeTransactionManager", readOnly = true)
	public ResponseEntity<String> getEaeCampagneOuverte() {

		logger.debug("entered GET [eaes/getEaeCampagneOuverte] => getEaeCampagneOuverte ");

		CampagneEaeDto campagneEnCours = eaeService.getEaeCampagneOuverte();

		String json = new JSONSerializer().exclude("*.class").transform(new MSDateTransformer(), Date.class)
				.serialize(campagneEnCours);

		return new ResponseEntity<String>(json, HttpStatus.OK);
	}

	/*
	 * POUR SIRH-WS
	 */
	@ResponseBody
	@RequestMapping(value = "getAvisSHD", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
	@Transactional(value = "eaeTransactionManager", readOnly = true)
	public ResponseEntity<String> getAvisSHD(@RequestParam("idEae") int idEae) {

		logger.debug("entered GET [eaes/getAvisSHD] => getAvisSHD with parameter idEae = {}", idEae);

		String avis = eaeService.getAvisSHD(idEae);

		ReturnMessageDto rmDto = new ReturnMessageDto();
		rmDto.getInfos().add(avis);

		String jsonResult = new JSONSerializer().exclude("*.class").deepSerialize(rmDto);

		return new ResponseEntity<String>(jsonResult, HttpStatus.OK);
	}

	/*
	 * POUR SIRH-WS
	 */
	@ResponseBody
	@RequestMapping(value = "findEaeByAgentAndYear", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
	@Transactional(value = "eaeTransactionManager", readOnly = true)
	public ResponseEntity<String> findEaeByAgentAndYear(@RequestParam("idAgent") int idAgent,
			@RequestParam("annee") int annee) {

		logger.debug(
				"entered GET [eaes/findEaeByAgentAndYear] => findEaeByAgentAndYear with parameter idAgent = {} and annee = {}",
				idAgent, annee);
		Eae eae = eaeService.findEaeByAgentAndYear(idAgent, annee);
		ReturnMessageDto rmDto = new ReturnMessageDto();

		if (eae != null) {
			rmDto.getInfos().add(eae.getIdEae().toString());

		}

		String response = new JSONSerializer().exclude("*.class").deepSerialize(rmDto);
		return new ResponseEntity<String>(response, HttpStatus.OK);
	}

	/*
	 * POUR SIRH-WS
	 */
	@ResponseBody
	@RequestMapping(value = "compterlistIdEaeByCampagneAndAgent", produces = "application/json;charset=utf-8", method = RequestMethod.POST)
	@Transactional(value = "eaeTransactionManager", readOnly = true)
	public ResponseEntity<String> compterlistIdEaeByCampagneAndAgent(@RequestParam("idCampagneEae") int idCampagneEae,
			@RequestParam("idAgent") int idAgent, @RequestBody String idAgents) {

		logger.debug(
				"entered POST [eaes/compterlistIdEaeByCampagneAndAgent] => compterlistIdEaeByCampagneAndAgent with parameter idAgent = {} and idCampagneEae = {} and idAgents = {}",
				idAgent, idCampagneEae, idAgents);

		List<Integer> list = new JSONDeserializer<List<Integer>>().use("values", Integer.class).deserialize(idAgents);

		Integer nbEae = eaeService.compterlistIdEaeByCampagneAndAgent(idCampagneEae, list, idAgent);
		ReturnMessageDto rmDto = new ReturnMessageDto();

		if (nbEae != null) {
			rmDto.getInfos().add(nbEae.toString());

		}

		String response = new JSONSerializer().exclude("*.class").deepSerialize(rmDto);
		return new ResponseEntity<String>(response, HttpStatus.OK);
	}

	/*
	 * POUR SIRH-WS
	 */
	@ResponseBody
	@RequestMapping(value = "getEaesGedIdsForAgents", produces = "application/json;charset=utf-8", method = RequestMethod.POST)
	@Transactional(value = "eaeTransactionManager", readOnly = true)
	public ResponseEntity<String> getEaesGedIdsForAgents(@RequestParam("annee") int annee, @RequestBody String idAgents) {

		logger.debug(
				"entered POST [eaes/getEaesGedIdsForAgents] => getEaesGedIdsForAgents with parameter annee = {}  and idAgents = {}",
				annee, idAgents);

		List<Integer> list = new JSONDeserializer<List<Integer>>().use("values", Integer.class).deserialize(idAgents);

		List<String> listGEDDocument = eaeService.getEaesGedIdsForAgents(list, annee);
		ReturnMessageDto rmDto = new ReturnMessageDto();

		if (listGEDDocument != null) {
			for (String ged : listGEDDocument) {
				rmDto.getInfos().add(ged);
			}
		}

		String response = new JSONSerializer().exclude("*.class").deepSerialize(rmDto);
		return new ResponseEntity<String>(response, HttpStatus.OK);
	}
}
