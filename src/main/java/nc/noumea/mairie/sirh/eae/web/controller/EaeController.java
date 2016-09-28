package nc.noumea.mairie.sirh.eae.web.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import flexjson.JSONSerializer;
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

@Controller
@RequestMapping("/eaes")
public class EaeController {

	private Logger							logger	= LoggerFactory.getLogger(EaeController.class);

	@Autowired
	private MessageSource					messageSource;

	@Autowired
	private IEaeService						eaeService;

	@Autowired
	private IAgentMatriculeConverterService	agentMatriculeConverterService;

	@Autowired
	private IEaeSecurityProvider			eaeSecurityProvider;

	@Autowired
	private IEaeReportingService			eaeReportingService;

	@ResponseBody
	@RequestMapping(value = "countListEaesByAgent", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
	public ResponseEntity<String> countListEaesByAgent(@RequestParam("idAgent") int idAgent) {

		logger.debug("entered GET [eaes/listEaesByAgent] => listEaesByAgent with parameter idAgent = {}", idAgent);

		Integer convertedId = agentMatriculeConverterService.tryConvertFromADIdAgentToEAEIdAgent(idAgent);

		Integer result;
		try {
			result = eaeService.countListEaesByAgentId(convertedId);
		} catch (SirhWSConsumerException e) {
			logger.error(e.getMessage(), e);
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		String jsonResult = new JSONSerializer().exclude("*.class").deepSerialize(result);

		return new ResponseEntity<String>(jsonResult, HttpStatus.OK);
	}

	@ResponseBody
	@RequestMapping(value = "listEaesByAgent", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
	public List<EaeListItemDto> listEaesByAgent(@RequestParam("idAgent") int idAgent) {

		logger.debug("entered GET [eaes/listEaesByAgent] => listEaesByAgent with parameter idAgent = {}", idAgent);

		Integer convertedId = agentMatriculeConverterService.tryConvertFromADIdAgentToEAEIdAgent(idAgent);

		List<EaeListItemDto> result;
		try {
			result = eaeService.listEaesByAgentId(convertedId);
		} catch (SirhWSConsumerException e) {
			logger.error(e.getMessage(), e);
			throw new ConflictException(e.getMessage());
		}

		if (result.isEmpty())
			throw new NoContentException();

		return result;
	}

	@ResponseBody
	@RequestMapping(value = "initialiserEae", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
	public ReturnMessageDto initializeEae(@RequestParam("idAgent") int idAgent, @RequestParam("idEvalue") int idEvalue) {

		logger.debug("entered GET [eaes/initialiserEae] => initializeEae with parameter idAgent = {} , idEvalue = {}", idAgent, idEvalue);

		Integer convertedIdAgentEvalue = agentMatriculeConverterService.tryConvertFromADIdAgentToEAEIdAgent(idEvalue);

		Integer convertedIdAgentEvaluateur = agentMatriculeConverterService.tryConvertFromADIdAgentToEAEIdAgent(idAgent);

		List<Eae> agentEaes = eaeService.findCurrentAndPreviousEaesByAgentId(convertedIdAgentEvalue);

		if (agentEaes.isEmpty())
			throw new NotFoundException();

		Eae lastEae = agentEaes.get(0);
		Eae previousEae = null;
		if (agentEaes.size() > 1)
			previousEae = agentEaes.get(1);

		eaeSecurityProvider.checkEaeAndWriteRight(lastEae.getIdEae(), convertedIdAgentEvaluateur);

		ReturnMessageDto result = new ReturnMessageDto();
		try {
			eaeService.initializeEae(lastEae, previousEae);
		} catch (EaeServiceException e) {
			logger.debug(e.getMessage(), e);
			result.getErrors().add(e.getMessage());
			return result;
		}

		result.getInfos().add(messageSource.getMessage("EAE_INITIALISE_OK", null, null));
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "affecterDelegataire", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
	public ReturnMessageDto setDelegataire(@RequestParam("idEae") int idEae, @RequestParam("idAgent") int idAgent,
			@RequestParam("idDelegataire") int idDelegataire) {

		logger.debug("entered GET [eaes/affecterDelegataire] => setDelegataire with parameter idAgent = {} , idDelegataire = {}", idAgent,
				idDelegataire);

		eaeSecurityProvider.checkEaeAndWriteRight(idEae, idAgent);

		Integer convertedIdAgentDelegataire = agentMatriculeConverterService.tryConvertFromADIdAgentToEAEIdAgent(idDelegataire);

		ReturnMessageDto result = new ReturnMessageDto();
		try {
			eaeService.setDelegataire(idEae, convertedIdAgentDelegataire);
		} catch (EaeServiceException e) {
			logger.debug(e.getMessage(), e);
			result.getErrors().add(e.getMessage());
			return result;
		}

		result.getInfos().add(messageSource.getMessage("EAE_DELEGATAIRE_OK", null, null));
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "tableauDeBord", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
	public List<EaeDashboardItemDto> getEaesDashboard(@RequestParam("idAgent") int idAgent) {

		logger.debug("entered GET [eaes/tableauDeBord] => getEaesDashboard with parameter idAgent = {} ", idAgent);

		Integer convertedId = agentMatriculeConverterService.tryConvertFromADIdAgentToEAEIdAgent(idAgent);

		List<EaeDashboardItemDto> result;
		try {
			result = eaeService.getEaesDashboard(convertedId);
		} catch (SirhWSConsumerException e) {
			logger.error(e.getMessage(), e);
			throw new ConflictException(e.getMessage());
		}

		if (result.isEmpty())
			throw new NoContentException();

		return result;
	}

	@ResponseBody
	@RequestMapping(value = "canFinalizeEae", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
	public void canFinalizeEae(@RequestParam("idEae") int idEae, @RequestParam("idAgent") int idAgent) {

		logger.debug("entered GET [eaes/canFinalizeEae] => canFinalizeEae with parameter idAgent = {} , idEae = {}", idAgent, idEae);

		eaeSecurityProvider.checkEaeAndWriteRight(idEae, idAgent);

		CanFinalizeEaeDto dto = eaeService.canFinalizEae(idEae);

		if (!dto.isCanFinalize())
			throw new ConflictException(dto.getMessage());
	}

	@ResponseBody
	@RequestMapping(value = "getFinalizationInformation", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
	public FinalizationInformationDto getFinalizationInformation(@RequestParam("idEae") int idEae, @RequestParam("idAgent") int idAgent) {

		logger.debug("entered GET [eaes/getFinalizationInformation] => getFinalizationInformation with parameter idAgent = {} , idEae = {}", idAgent,
				idEae);

		eaeSecurityProvider.checkEaeAndWriteRight(idEae, idAgent);

		FinalizationInformationDto dto = null;

		try {
			dto = eaeService.getFinalizationInformation(idEae);
		} catch (SirhWSConsumerException e) {
			logger.error(e.getMessage(), e);
			throw new ConflictException(e.getMessage());
		}

		return dto;
	}

	@ResponseBody
	@RequestMapping(value = "finalizeEae", produces = "application/json;charset=utf-8", method = RequestMethod.POST)
	public ReturnMessageDto finalizeEae(@RequestParam("idEae") int idEae, @RequestParam("idAgent") int idAgent,
			@RequestBody EaeFinalizationDto eaeFinalizationDto) {

		logger.debug("entered POST [eaes/finalizeEae] => finalizeEae with parameter idAgent = {} , idEae = {}, eaeFinalizationDto ", idAgent, idEae,
				eaeFinalizationDto.toString());

		eaeSecurityProvider.checkEaeAndWriteRight(idEae, idAgent);

		return eaeService.finalizeEae(idEae, agentMatriculeConverterService.tryConvertFromADIdAgentToEAEIdAgent(idAgent), eaeFinalizationDto);
	}

	@SuppressWarnings("rawtypes")
	@ResponseBody
	@RequestMapping(value = "downloadEae", method = RequestMethod.GET)
	public ResponseEntity downloadEae(@RequestParam("idEae") int idEae, @RequestParam("idAgent") int idAgent,
			@RequestParam(value = "format", required = false) String format) {

		logger.debug("entered GET [eaes/downloadEae] => downloadEae with parameter idAgent = {} , idEae = {}", idAgent, idEae);

		eaeSecurityProvider.checkEaeAndReadRight(idEae, idAgent);

		byte[] responseData = null;
		EaeReportFormatEnum formatValue = null;

		try {
			formatValue = eaeReportingService.getFileFormatFromString(format);
			responseData = eaeReportingService.getEaeReportAsByteArray(idEae, formatValue);
		} catch (EaeReportingServiceException e) {
			logger.error(e.getMessage(), e);
			throw new ConflictException(e.getMessage());
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
			case DOCX:
				contentType = "application/msword";
		}

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", contentType);
		headers.add("Content-Disposition", String.format("attachment; filename=\"%s.%s\"", idEae, format.toString().toLowerCase()));

		return headers;
	}

	@ResponseBody
	@RequestMapping(value = "getEaeEvalueFullname", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
	public EaeEvalueNameDto getEvalueFullname(@RequestParam("idEae") int idEae, @RequestParam("idAgent") int idAgent) {

		logger.debug("entered GET [eaes/getEaeEvalueFullname] => getEvalueFullname with parameter idAgent = {} , idEae = {}", idAgent, idEae);

		eaeSecurityProvider.checkEaeAndReadRight(idEae, idAgent);

		EaeEvalueNameDto fullName = eaeService.getEvalueName(idEae);

		return fullName;
	}

	/*
	 * POUR SIRH-WS et KIOSQUE-J2EE
	 */
	@ResponseBody
	@RequestMapping(value = "getEaeCampagneOuverte", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
	public CampagneEaeDto getEaeCampagneOuverte() {

		logger.debug("entered GET [eaes/getEaeCampagneOuverte] => getEaeCampagneOuverte ");

		CampagneEaeDto campagneEnCours = eaeService.getEaeCampagneOuverte();

		return campagneEnCours;
	}

	/*
	 * POUR SIRH-WS
	 */
	@ResponseBody
	@RequestMapping(value = "getAvisSHD", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
	public ReturnMessageDto getAvisSHD(@RequestParam("idEae") int idEae) {

		logger.debug("entered GET [eaes/getAvisSHD] => getAvisSHD with parameter idEae = {}", idEae);

		String avis = eaeService.getAvisSHD(idEae);

		ReturnMessageDto rmDto = new ReturnMessageDto();
		rmDto.getInfos().add(avis);

		return rmDto;
	}

	/*
	 * POUR SIRH-WS
	 */
	@ResponseBody
	@RequestMapping(value = "findEaeByAgentAndYear", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
	public ReturnMessageDto findEaeByAgentAndYear(@RequestParam("idAgent") int idAgent, @RequestParam("annee") int annee) {

		logger.debug("entered GET [eaes/findEaeByAgentAndYear] => findEaeByAgentAndYear with parameter idAgent = {} and annee = {}", idAgent, annee);

		Eae eae = eaeService.findEaeByAgentAndYear(idAgent, annee);
		ReturnMessageDto rmDto = new ReturnMessageDto();

		if (eae != null) {
			rmDto.getInfos().add(eae.getIdEae().toString());
		}

		return rmDto;
	}

	/*
	 * POUR SIRH-WS
	 */
	@ResponseBody
	@RequestMapping(value = "compterlistIdEaeByCampagneAndAgent", produces = "application/json;charset=utf-8", method = RequestMethod.POST)
	public ReturnMessageDto compterlistIdEaeByCampagneAndAgent(@RequestParam("idCampagneEae") int idCampagneEae, @RequestParam("idAgent") int idAgent,
			@RequestBody List<Integer> idAgents) {

		logger.debug(
				"entered POST [eaes/compterlistIdEaeByCampagneAndAgent] => compterlistIdEaeByCampagneAndAgent with parameter idAgent = {} and idCampagneEae = {} and idAgents = {}",
				idAgent, idCampagneEae, idAgents);

		Integer nbEae = eaeService.compterlistIdEaeByCampagneAndAgent(idCampagneEae, idAgents, idAgent);
		ReturnMessageDto rmDto = new ReturnMessageDto();

		if (nbEae != null) {
			rmDto.getInfos().add(nbEae.toString());
		}

		return rmDto;
	}

	/*
	 * POUR SIRH-WS
	 */
	@ResponseBody
	@RequestMapping(value = "getEaesGedIdsForAgents", produces = "application/json;charset=utf-8", method = RequestMethod.POST)
	public ReturnMessageDto getEaesGedIdsForAgents(@RequestParam("annee") int annee, @RequestBody List<Integer> idAgents) {

		logger.debug("entered POST [eaes/getEaesGedIdsForAgents] => getEaesGedIdsForAgents with parameter annee = {}  and idAgents = {}", annee,
				idAgents);

		List<String> listGEDDocument = eaeService.getEaesGedIdsForAgents(idAgents, annee);
		ReturnMessageDto rmDto = new ReturnMessageDto();

		if (listGEDDocument != null) {
			for (String ged : listGEDDocument) {
				rmDto.getInfos().add(ged);
			}
		}

		return rmDto;
	}

	@ResponseBody
	@RequestMapping(value = "getEeaControle", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
	public List<EaeFinalizationDto> getEeaControleByAgent(@RequestParam("idAgent") int idAgent) {

		logger.debug("entered GET [eaes/getEeaControle] => getEeaControleByAgent with parameter idAgent = {}", idAgent);

		Integer convertedId = agentMatriculeConverterService.tryConvertFromADIdAgentToEAEIdAgent(idAgent);

		List<EaeFinalizationDto> result;
		try {
			result = eaeService.listEeaControleByAgent(convertedId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ConflictException(e.getMessage());
		}

		if (result.isEmpty())
			throw new NoContentException();

		return result;
	}
}
