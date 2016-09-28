package nc.noumea.mairie.sirh.eae.web.controller;

import java.text.ParseException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import nc.noumea.mairie.sirh.eae.dto.CampagneEaeDto;
import nc.noumea.mairie.sirh.eae.dto.EaeCampagneTaskDto;
import nc.noumea.mairie.sirh.eae.dto.EaeDashboardItemDto;
import nc.noumea.mairie.sirh.eae.dto.EaeDocumentDto;
import nc.noumea.mairie.sirh.eae.dto.EaeDto;
import nc.noumea.mairie.sirh.eae.dto.EaeEvaluationDto;
import nc.noumea.mairie.sirh.eae.dto.EaeEvolutionDto;
import nc.noumea.mairie.sirh.eae.dto.FormRehercheGestionEae;
import nc.noumea.mairie.sirh.eae.dto.ReturnMessageDto;
import nc.noumea.mairie.sirh.eae.dto.agent.BirtDto;
import nc.noumea.mairie.sirh.eae.dto.identification.ValeurListeDto;
import nc.noumea.mairie.sirh.eae.dto.planAction.EaePlanActionDto;
import nc.noumea.mairie.sirh.eae.service.EaeServiceException;
import nc.noumea.mairie.sirh.eae.service.EvaluationServiceException;
import nc.noumea.mairie.sirh.eae.service.IAgentMatriculeConverterService;
import nc.noumea.mairie.sirh.eae.service.ICalculEaeService;
import nc.noumea.mairie.sirh.eae.service.ICampagneEaeService;
import nc.noumea.mairie.sirh.eae.service.IEaeService;
import nc.noumea.mairie.sirh.eae.service.IEvaluationService;
import nc.noumea.mairie.sirh.ws.ISirhWsConsumer;
import nc.noumea.mairie.sirh.ws.SirhWSConsumerException;

@Controller
@RequestMapping("/sirhEaes")
public class SirhEaeController {

	private Logger							logger	= LoggerFactory.getLogger(SirhEaeController.class);

	@Autowired
	private IEaeService						eaeService;

	@Autowired
	private IAgentMatriculeConverterService	agentMatriculeConverterService;

	@Autowired
	private IEvaluationService				evaluationService;

	@Autowired
	private ICampagneEaeService				campagneService;

	@Autowired
	private ICalculEaeService				calculService;

	@Autowired
	private MessageSource					messageSource;

	@Autowired
	private ISirhWsConsumer					sirhWSConsumer;

	@ResponseBody
	@RequestMapping(value = "listEaesByAgent", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
	public List<EaeDto> listEaesByAgent(@RequestParam("idAgentSirh") int idAgentSirh, @RequestParam("idAgent") int idAgent)
			throws SirhWSConsumerException {

		logger.debug("entered GET [sirhEaes/listEaesByAgent] => listEaesByAgent with parameter idAgentSirh = {}, idAgent = {}", idAgentSirh, idAgent);

		isUtilisateurSirh(idAgentSirh);

		Integer convertedId = agentMatriculeConverterService.tryConvertFromADIdAgentToEAEIdAgent(idAgent);

		List<EaeDto> result = eaeService.findEaesByIdAgentOnly(convertedId);

		if (result.isEmpty())
			throw new NoContentException();

		return result;
	}

	@ResponseBody
	@RequestMapping(value = "detailsEae", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
	public EaeDto getDetailsEae(@RequestParam("idAgentSirh") int idAgentSirh, @RequestParam("idEae") int idEae) throws SirhWSConsumerException {

		logger.debug("entered GET [sirhEaes/detailsEae] => getDetailsEae with parameter idAgentSirh = {}, idEae = {}", idAgentSirh, idEae);

		isUtilisateurSirh(idAgentSirh);

		EaeDto result = eaeService.findEaeDto(idEae);

		if (null == result)
			throw new NoContentException();

		return result;
	}

	@ResponseBody
	@RequestMapping(value = "eaeEvaluation", produces = "application/json;charset=utf-8", method = RequestMethod.POST)
	public ReturnMessageDto setEaeEvaluation(@RequestParam("idEae") int idEae, @RequestParam("idAgent") int idAgent,
			@RequestBody EaeEvaluationDto eaeEvaluationDto) throws SirhWSConsumerException {

		logger.debug("entered POST [sirhEaes/eaeEvaluation] => setEaeEvaluation with parameter idAgent = {} , idEae = {}, eaeEvaluationDto ", idAgent,
				idEae, eaeEvaluationDto.toString());

		isUtilisateurSirh(idAgent);

		ReturnMessageDto result = new ReturnMessageDto();
		try {
			evaluationService.setEaeEvaluation(idEae, eaeEvaluationDto, true);
		} catch (EaeServiceException e) {
			logger.debug(e.getMessage(), e);
			result.getErrors().add(e.getMessage());
			return result;
		} catch (EvaluationServiceException e) {
			logger.debug(e.getMessage(), e);
			result.getErrors().add(e.getMessage());
			return result;
		}

		result.getInfos().add(messageSource.getMessage("EAE_EVALUATION_OK", null, null));
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "eaePlanAction", produces = "application/json;charset=utf-8", method = RequestMethod.POST)
	public ReturnMessageDto setEaePlanAction(@RequestParam("idEae") int idEae, @RequestParam("idAgent") int idAgent,
			@RequestBody EaePlanActionDto eaePlanActionDto) throws SirhWSConsumerException {

		logger.debug("entered POST [sirhEaes/eaePlanAction] => setEaePlanAction with parameter idAgent = {} , idEae = {}, eaePlanActionDto ", idAgent,
				idEae, eaePlanActionDto.toString());

		isUtilisateurSirh(idAgent);

		ReturnMessageDto result = new ReturnMessageDto();
		try {
			evaluationService.setEaePlanAction(idEae, eaePlanActionDto, true);
		} catch (EaeServiceException e) {
			logger.debug(e.getMessage(), e);
			result.getErrors().add(e.getMessage());
			return result;
		}

		result.getInfos().add(messageSource.getMessage("EAE_PLAN_ACTION_OK", null, null));
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "eaeEvolution", produces = "application/json;charset=utf-8", method = RequestMethod.POST)
	public ReturnMessageDto setEaeEvolution(@RequestParam("idEae") int idEae, @RequestParam("idAgent") int idAgent,
			@RequestBody EaeEvolutionDto eaeEvolutioDto) throws SirhWSConsumerException {

		logger.debug("entered POST [sirhEaes/eaeEvolution] => setEaeEvolution with parameter idAgent = {} , idEae = {} , eaeEvolutioDto ", idAgent,
				idEae, eaeEvolutioDto.toString());

		isUtilisateurSirh(idAgent);

		ReturnMessageDto result = new ReturnMessageDto();
		try {
			evaluationService.setEaeEvolution(idEae, eaeEvolutioDto, true);
		} catch (EaeServiceException e) {
			logger.debug(e.getMessage(), e);
			result.getErrors().add(e.getMessage());
			return result;
		} catch (EvaluationServiceException e) {
			logger.debug(e.getMessage(), e);
			result.getErrors().add(e.getMessage());
			return result;
		}

		result.getInfos().add(messageSource.getMessage("EAE_EVOLUTION_OK", null, null));
		return result;
	}

	private void isUtilisateurSirh(Integer idAgent) throws SirhWSConsumerException {
		if (!sirhWSConsumer.isUtilisateurSirh(agentMatriculeConverterService.tryConvertFromADIdAgentToEAEIdAgent(idAgent)))
			throw new ForbiddenException("Utilisateur SIRH non habilité");
	}

	@ResponseBody
	@RequestMapping(value = "eaeNumIncrement", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
	public Integer chercherEaeNumIncrement(@RequestParam("idAgentSirh") int idAgentSirh) throws SirhWSConsumerException {

		logger.debug("entered GET [sirhEaes/eaeNumIncrement] => chercherEaeNumIncrement");

		isUtilisateurSirh(idAgentSirh);

		return eaeService.chercherEaeNumIncrement();
	}

	@ResponseBody
	@RequestMapping(value = "listeTypeDeveloppement", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
	public List<ValeurListeDto> getListeTypeDeveloppement() throws SirhWSConsumerException {

		logger.debug("entered GET [sirhEaes/listeTypeDeveloppement] => getListeTypeDeveloppement");

		return eaeService.getListeTypeDeveloppement();
	}

	@ResponseBody
	@RequestMapping(value = "listCampagnesEae", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
	public List<CampagneEaeDto> listCampagnesEae(@RequestParam("idAgentSirh") int idAgentSirh) throws SirhWSConsumerException {

		logger.debug("entered GET [sirhEaes/listCampagnesEae] => listCampagnesEae with parameter idAgentSirh = {}", idAgentSirh);

		isUtilisateurSirh(idAgentSirh);

		List<CampagneEaeDto> result = campagneService.getListeCampagneEae();

		if (result.isEmpty())
			throw new NoContentException();

		return result;
	}

	@ResponseBody
	@RequestMapping(value = "setCampagneEae", produces = "application/json;charset=utf-8", method = RequestMethod.POST)
	public ReturnMessageDto setCampagneEae(@RequestParam("idAgentSirh") int idAgentSirh, @RequestBody CampagneEaeDto campagneEaeDto)
			throws SirhWSConsumerException {

		logger.debug("entered POST [sirhEaes/setCampagneEae] => setCampagneEae with parameter idAgentSirh = {}", idAgentSirh);

		isUtilisateurSirh(idAgentSirh);

		return campagneService.createOrModifyCampagneEae(campagneEaeDto);
	}

	@ResponseBody
	@RequestMapping(value = "campagneAnneePrecedente", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
	public CampagneEaeDto getCampagneAnneePrecedente(@RequestParam("idAgentSirh") int idAgentSirh,
			@RequestParam("anneePrecedente") Integer anneePrecedente) throws SirhWSConsumerException {

		logger.debug(
				"entered GET [sirhEaes/campagneAnneePrecedente] => getCampagneAnneePrecedente with parameter idAgentSirh = {}, anneePrecedente = {}",
				idAgentSirh, anneePrecedente);

		isUtilisateurSirh(idAgentSirh);

		return campagneService.getCampagneEaeAnnePrecedente(anneePrecedente);
	}

	@ResponseBody
	@RequestMapping(value = "documentEae", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
	public EaeDocumentDto getDocumentEae(@RequestParam("idAgentSirh") int idAgentSirh, @RequestParam("idDocument") Integer idDocument)
			throws SirhWSConsumerException {

		logger.debug("entered GET [sirhEaes/documentEae] => getDocumentEae with parameter idAgentSirh = {}, idDocument = {}", idAgentSirh,
				idDocument);

		isUtilisateurSirh(idAgentSirh);

		return campagneService.getEaeDocumentByIdDocument(idDocument);
	}

	@ResponseBody
	@RequestMapping(value = "deleteDocumentEae", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
	public ReturnMessageDto deleteDocumentEae(@RequestParam("idAgentSirh") int idAgentSirh, @RequestParam("idEaeDocument") Integer idEaeDocument)
			throws SirhWSConsumerException {

		logger.debug("entered GET [sirhEaes/deleteDocumentEae] => deleteDocumentEae with parameter idAgentSirh = {}, idEaeDocument = {}", idAgentSirh,
				idEaeDocument);

		isUtilisateurSirh(idAgentSirh);

		return campagneService.deleteEaeDocument(idEaeDocument);
	}

	@ResponseBody
	@RequestMapping(value = "eae", produces = "application/json;charset=utf-8", method = RequestMethod.POST)
	public ReturnMessageDto setEae(@RequestParam("idAgent") int idAgent, @RequestBody EaeDto eaeDto) throws SirhWSConsumerException {

		logger.debug("entered POST [sirhEaes/eae] => setEae with parameter idAgent = {} and eaeDto ", idAgent, eaeDto.toString());

		isUtilisateurSirh(idAgent);

		ReturnMessageDto result = new ReturnMessageDto();
		try {
			eaeService.setEae(eaeDto);
		} catch (EaeServiceException e) {
			logger.debug(e.getMessage(), e);
			result.getErrors().add(e.getMessage());
			return result;
		}

		result.getInfos().add(messageSource.getMessage("EAE_OK", null, null));
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "listeEae", produces = "application/json;charset=utf-8", method = RequestMethod.POST)
	public List<EaeDto> getListeEae(@RequestParam("idAgent") int idAgent, @RequestBody FormRehercheGestionEae form) throws SirhWSConsumerException {

		logger.debug("entered POST [sirhEaes/listeEae] => getListeEae with parameter idAgent = {}", idAgent);

		isUtilisateurSirh(idAgent);

		List<EaeDto> result = null;

		try {
			result = eaeService.getListeEaeDto(form);
		} catch (EaeServiceException e) {
			logger.debug(e.getMessage(), e);
			throw new ConflictException(e.getMessage());
		}

		return result;
	}

	@ResponseBody
	@RequestMapping(value = "eaeCampagneTask", method = RequestMethod.GET)
	public EaeCampagneTaskDto findEaeCampagneTask(@RequestParam("idAgent") int idAgent, @RequestParam("idEaeCampagne") int idEaeCampagne)
			throws SirhWSConsumerException {

		logger.debug("entered GET [sirhEaes/eaeCampagneTask] => findEaeCampagneTask with parameter idAgent = {}, idEaeCampagne = {}", idAgent,
				idEaeCampagne);

		isUtilisateurSirh(idAgent);

		return campagneService.findEaeCampagneTaskByIdCampagne(idEaeCampagne);
	}

	@ResponseBody
	@RequestMapping(value = "eaeCampagneTask", method = RequestMethod.POST)
	public ReturnMessageDto setEaeCampagneTask(@RequestParam("idAgent") int idAgent, @RequestBody EaeCampagneTaskDto campagneTaskDto)
			throws SirhWSConsumerException {

		logger.debug("entered POST [sirhEaes/eaeCampagneTask] => setEaeCampagneTask with parameter idAgent = {}", idAgent);

		isUtilisateurSirh(idAgent);

		return campagneService.createOrModifyEaeCampagneTask(campagneTaskDto);
	}

	@ResponseBody
	@RequestMapping(value = "updateEae", method = RequestMethod.GET)
	public ReturnMessageDto updateEae(@RequestParam("idAgent") int idAgent, @RequestParam("idEae") int idEae) throws SirhWSConsumerException {

		logger.debug("entered GET [sirhEaes/updateEae] => updateEae with parameter idAgent = {}, idEae = {}", idAgent, idEae);

		isUtilisateurSirh(idAgent);

		ReturnMessageDto result = new ReturnMessageDto();
		try {
			calculService.updateEae(idEae);
		} catch (SirhWSConsumerException e) {
			logger.debug(e.getMessage(), e);
			result.getErrors().add(e.getMessage());
		} catch (ParseException e) {
			logger.debug(e.getMessage(), e);
			result.getErrors().add(e.getMessage());
		}

		result.getInfos().add("L'EAE a bien été mis à jour.");
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "updateCapEae", method = RequestMethod.GET)
	public ReturnMessageDto updateCapEae(@RequestParam("idAgentSirh") int idAgent, @RequestParam("idEae") int idEae, @RequestParam("cap") boolean cap)
			throws SirhWSConsumerException {

		logger.debug("entered GET [sirhEaes/updateCapEae] => updateCapEae with parameter idAgentSirh = {}, idEae = {}, cap = {}", idAgent, idEae,
				cap);

		isUtilisateurSirh(idAgent);

		return eaeService.updateCapEae(idEae, cap);
	}

	@ResponseBody
	@RequestMapping(value = "lastDocumentEaeFinalise", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
	public String getLastDocumentEaeFinalise(@RequestParam("idAgentSirh") int idAgentSirh, @RequestParam("idEae") Integer idEae)
			throws SirhWSConsumerException {

		logger.debug("entered GET [sirhEaes/lastDocumentEaeFinalise] => getLastDocumentEaeFinalise with parameter idAgentSirh = {}, idEae = {}",
				idAgentSirh, idEae);

		isUtilisateurSirh(idAgentSirh);

		return eaeService.getLastDocumentEaeFinalise(idEae);
	}

	@ResponseBody
	@RequestMapping(value = "tableauDeBord", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
	public List<EaeDashboardItemDto> getEaesDashboard(@RequestParam("idAgentSirh") int idAgentSirh, @RequestParam("annee") int annee)
			throws SirhWSConsumerException {

		logger.debug("entered GET [sirhEaes/tableauDeBord] => getEaesDashboard with parameter idAgentSirh = {} and annee = {}", idAgentSirh, annee);

		isUtilisateurSirh(idAgentSirh);

		List<EaeDashboardItemDto> result;
		try {
			result = eaeService.getEaesDashboardForSIRH(annee);
		} catch (SirhWSConsumerException e) {
			logger.error(e.getMessage(), e);
			throw new ConflictException(e.getMessage());
		}

		if (result.isEmpty())
			throw new NoContentException();

		return result;
	}

	@ResponseBody
	@RequestMapping(value = "saveDateEvaluateurFromSirh", produces = "application/json;charset=utf-8", method = RequestMethod.POST)
	public ReturnMessageDto saveDateEvaluateurFromSirh(@RequestParam("idEae") int idEae, @RequestParam("idAgent") int idAgent,
			@RequestBody BirtDto evaluateur) throws SirhWSConsumerException {

		logger.debug(
				"entered POST [sirhEaes/saveDateEvaluateurFromSirh] => saveDateEvaluateurFromSirh with parameter idAgent = {} , idEae = {}, eaeEvaluationDto ",
				idAgent, idEae, evaluateur.toString());

		isUtilisateurSirh(idAgent);

		ReturnMessageDto result = new ReturnMessageDto();
		try {
			evaluationService.saveDateEvaluateurFromSirh(idEae, evaluateur);
		} catch (Exception e) {
			logger.debug(e.getMessage(), e);
			result.getErrors().add(e.getMessage());
			return result;
		}

		result.getInfos().add(messageSource.getMessage("EAE_OK", null, null));
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "saveDateEvalueFromSirh", produces = "application/json;charset=utf-8", method = RequestMethod.POST)
	public ReturnMessageDto saveDateEvalueFromSirh(@RequestParam("idEae") int idEae, @RequestParam("idAgent") int idAgent,
			@RequestBody BirtDto evalue) throws SirhWSConsumerException {

		logger.debug(
				"entered POST [sirhEaes/saveDateEvalueFromSirh] => saveDateEvalueFromSirh with parameter idAgent = {} , idEae = {}, eaeEvaluationDto ",
				idAgent, idEae, evalue.toString());

		isUtilisateurSirh(idAgent);

		ReturnMessageDto result = new ReturnMessageDto();
		try {
			evaluationService.saveDateEvalueFromSirh(idEae, evalue);
		} catch (Exception e) {
			logger.debug(e.getMessage(), e);
			result.getErrors().add(e.getMessage());
			return result;
		}

		result.getInfos().add(messageSource.getMessage("EAE_OK", null, null));
		return result;
	}
}
