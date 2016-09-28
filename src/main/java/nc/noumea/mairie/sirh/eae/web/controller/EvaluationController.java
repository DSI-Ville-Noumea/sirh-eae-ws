package nc.noumea.mairie.sirh.eae.web.controller;

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

import nc.noumea.mairie.sirh.eae.dto.EaeAppreciationsDto;
import nc.noumea.mairie.sirh.eae.dto.EaeAutoEvaluationDto;
import nc.noumea.mairie.sirh.eae.dto.EaeEvaluationDto;
import nc.noumea.mairie.sirh.eae.dto.EaeEvolutionDto;
import nc.noumea.mairie.sirh.eae.dto.EaeResultatsDto;
import nc.noumea.mairie.sirh.eae.dto.ReturnMessageDto;
import nc.noumea.mairie.sirh.eae.dto.identification.EaeIdentificationDto;
import nc.noumea.mairie.sirh.eae.dto.planAction.EaePlanActionDto;
import nc.noumea.mairie.sirh.eae.dto.poste.EaeFichePosteDto;
import nc.noumea.mairie.sirh.eae.security.IEaeSecurityProvider;
import nc.noumea.mairie.sirh.eae.service.EaeServiceException;
import nc.noumea.mairie.sirh.eae.service.EvaluationServiceException;
import nc.noumea.mairie.sirh.eae.service.IEvaluationService;

@Controller
@RequestMapping("/evaluation")
public class EvaluationController {

	private Logger					logger	= LoggerFactory.getLogger(EvaluationController.class);

	@Autowired
	private MessageSource			messageSource;

	@Autowired
	private IEvaluationService		evaluationService;

	@Autowired
	private IEaeSecurityProvider	eaeSecurityProvider;

	@ResponseBody
	@RequestMapping(value = "eaeIdentification", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
	public EaeIdentificationDto getEaeIdentifitcation(@RequestParam("idEae") int idEae, @RequestParam("idAgent") int idAgent) {

		logger.debug("entered GET [evaluation/eaeIdentification] => getEaeIdentifitcation with parameter idAgent = {} , idEae = {}", idAgent, idEae);

		eaeSecurityProvider.checkEaeAndReadRight(idEae, idAgent);

		return evaluationService.getEaeIdentification(idEae);
	}

	@ResponseBody
	@RequestMapping(value = "eaeIdentification", produces = "application/json;charset=utf-8", method = RequestMethod.POST, consumes = "application/json")
	public ReturnMessageDto setEaeIdentifitcation(@RequestParam("idEae") int idEae, @RequestParam("idAgent") int idAgent,
			@RequestBody EaeIdentificationDto eaeIdentificationDto) {

		logger.debug("entered POST [evaluation/eaeIdentification] => setEaeIdentifitcation with parameter idAgent = {} , idEae = {}, json = {}",
				idAgent, idEae, eaeIdentificationDto == null ? "" : eaeIdentificationDto.toString());

		eaeSecurityProvider.checkEaeAndWriteRight(idEae, idAgent);

		ReturnMessageDto result = new ReturnMessageDto();
		try {
			evaluationService.setEaeIdentification(idEae, eaeIdentificationDto, false);
		} catch (EaeServiceException e) {
			logger.debug(e.getMessage(), e);
			result.getErrors().add(e.getMessage());
			return result;
		} catch (EvaluationServiceException e) {
			logger.debug(e.getMessage(), e);
			result.getErrors().add(e.getMessage());
			return result;
		}

		result.getInfos().add(messageSource.getMessage("EAE_IDENTIFICATION_OK", null, null));
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "eaeFichePoste", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
	public List<EaeFichePosteDto> getEaeFichePoste(@RequestParam("idEae") int idEae, @RequestParam("idAgent") int idAgent) {

		logger.debug("entered GET [evaluation/eaeFichePoste] => getEaeFichePoste with parameter idAgent = {} , idEae = {}", idAgent, idEae);

		eaeSecurityProvider.checkEaeAndReadRight(idEae, idAgent);

		return evaluationService.getEaeFichePoste(idEae);
	}

	@ResponseBody
	@RequestMapping(value = "eaeResultats", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
	public EaeResultatsDto getEaeResultats(@RequestParam("idEae") int idEae, @RequestParam("idAgent") int idAgent) {

		logger.debug("entered GET [evaluation/eaeResultats] => getEaeResultats with parameter idAgent = {} , idEae = {}", idAgent, idEae);

		eaeSecurityProvider.checkEaeAndReadRight(idEae, idAgent);

		return evaluationService.getEaeResultats(idEae);
	}

	@ResponseBody
	@RequestMapping(value = "eaeResultats", produces = "application/json;charset=utf-8", method = RequestMethod.POST, consumes = "application/json")
	public ReturnMessageDto setEaeResultats(@RequestParam("idEae") int idEae, @RequestParam("idAgent") int idAgent,
			@RequestBody EaeResultatsDto eaeResultatsDto) {

		logger.debug("entered POST [evaluation/eaeResultats] => setEaeResultats with parameter idAgent = {} , idEae = {}, json = {}", idAgent, idEae,
				eaeResultatsDto.toString());

		eaeSecurityProvider.checkEaeAndWriteRight(idEae, idAgent);

		ReturnMessageDto result = new ReturnMessageDto();
		try {
			evaluationService.setEaeResultats(idEae, eaeResultatsDto, false);
		} catch (EaeServiceException e) {
			logger.debug(e.getMessage(), e);
			result.getErrors().add(e.getMessage());
			return result;
		} catch (EvaluationServiceException e) {
			logger.debug(e.getMessage(), e);
			result.getErrors().add(e.getMessage());
			return result;
		}

		result.getInfos().add(messageSource.getMessage("EAE_RESULTATS_OK", null, null));
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "eaeAppreciations", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
	public EaeAppreciationsDto getEaeAppreciations(@RequestParam("idEae") int idEae, @RequestParam("idAgent") int idAgent,
			@RequestParam(value = "annee", required = false) String annee) {

		logger.debug("entered GET [evaluation/eaeAppreciations] => getEaeAppreciations with parameter idAgent = {} , idEae = {}", idAgent, idEae);

		if (null == annee) {
			eaeSecurityProvider.checkEaeAndReadRight(idEae, idAgent);
		}
		// si l'année est renseignée, alors il faut trouver l'EAE de l'annee
		// precedente de la personne
		EaeAppreciationsDto dto = evaluationService.getEaeAppreciations(idEae, annee);

		if (null == dto) {
			throw new ConflictException("AUCUN EAE TROUVE");
		}

		return dto;
	}

	@ResponseBody
	@RequestMapping(value = "eaeAppreciations", produces = "application/json;charset=utf-8", method = RequestMethod.POST, consumes = "application/json")
	public ReturnMessageDto setEaeAppreciations(@RequestParam("idEae") int idEae, @RequestParam("idAgent") int idAgent,
			@RequestBody EaeAppreciationsDto eaeAppreciationsDto) {

		logger.debug("entered POST [evaluation/eaeAppreciations] => setEaeAppreciations with parameter idAgent = {} , idEae = {}", idAgent, idEae);

		eaeSecurityProvider.checkEaeAndWriteRight(idEae, idAgent);

		ReturnMessageDto result = new ReturnMessageDto();
		try {
			evaluationService.setEaeAppreciations(idEae, eaeAppreciationsDto, false);
		} catch (EaeServiceException e) {
			logger.debug(e.getMessage(), e);
			result.getErrors().add(e.getMessage());
			return result;
		}

		result.getInfos().add(messageSource.getMessage("EAE_APPRECIATIONS_OK", null, null));
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "eaeEvaluation", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
	public EaeEvaluationDto getEaeEvaluation(@RequestParam("idEae") int idEae, @RequestParam("idAgent") int idAgent) {

		logger.debug("entered GET [evaluation/eaeEvaluation] => getEaeEvaluation with parameter idAgent = {} , idEae = {}", idAgent, idEae);

		eaeSecurityProvider.checkEaeAndReadRight(idEae, idAgent);

		return evaluationService.getEaeEvaluation(idEae);
	}

	@ResponseBody
	@RequestMapping(value = "eaeEvaluation", produces = "application/json;charset=utf-8", method = RequestMethod.POST)
	public ReturnMessageDto setEaeEvaluation(@RequestParam("idEae") int idEae, @RequestParam("idAgent") int idAgent,
			@RequestBody EaeEvaluationDto eaeEvaluationDto) {

		logger.debug("entered POST [evaluation/eaeEvaluation] => setEaeEvaluation with parameter idAgent = {} , idEae = {}, json = {}", idAgent,
				idEae, eaeEvaluationDto.toString());

		eaeSecurityProvider.checkEaeAndWriteRight(idEae, idAgent);

		ReturnMessageDto result = new ReturnMessageDto();
		try {
			evaluationService.setEaeEvaluation(idEae, eaeEvaluationDto, false);
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
	@RequestMapping(value = "eaeAutoEvaluation", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
	public EaeAutoEvaluationDto getEaeAutoEvaluation(@RequestParam("idEae") int idEae, @RequestParam("idAgent") int idAgent) {

		logger.debug("entered GET [evaluation/eaeAutoEvaluation] => getEaeAutoEvaluation with parameter idAgent = {} , idEae = {}", idAgent, idEae);

		eaeSecurityProvider.checkEaeAndReadRight(idEae, idAgent);

		return evaluationService.getEaeAutoEvaluation(idEae);
	}

	@ResponseBody
	@RequestMapping(value = "eaeAutoEvaluation", produces = "application/json;charset=utf-8", method = RequestMethod.POST, consumes = "application/json")
	public ReturnMessageDto setEaeAutoEvaluation(@RequestParam("idEae") int idEae, @RequestParam("idAgent") int idAgent,
			@RequestBody EaeAutoEvaluationDto eaeAutoEvaluationDto) {
		logger.debug("entered POST [evaluation/eaeAutoEvaluation] => setEaeAutoEvaluation with parameter idAgent = {} , idEae = {}", idAgent, idEae,
				eaeAutoEvaluationDto == null ? "" : eaeAutoEvaluationDto.toString());

		eaeSecurityProvider.checkEaeAndWriteRight(idEae, idAgent);

		ReturnMessageDto result = new ReturnMessageDto();
		try {
			evaluationService.setEaeAutoEvaluation(idEae, eaeAutoEvaluationDto, false);
		} catch (EaeServiceException e) {
			logger.debug(e.getMessage(), e);
			result.getErrors().add(e.getMessage());
			return result;
		}

		result.getInfos().add(messageSource.getMessage("EAE_AUTO_EVALUATION_OK", null, null));
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "eaePlanAction", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
	public EaePlanActionDto getEaePlanAction(@RequestParam("idEae") int idEae, @RequestParam("idAgent") int idAgent) {

		logger.debug("entered GET [evaluation/eaePlanAction] => getEaePlanAction with parameter idAgent = {} , idEae = {}", idAgent, idEae);

		eaeSecurityProvider.checkEaeAndReadRight(idEae, idAgent);

		return evaluationService.getEaePlanAction(idEae);
	}

	@ResponseBody
	@RequestMapping(value = "eaePlanAction", produces = "application/json;charset=utf-8", method = RequestMethod.POST, consumes = "application/json")
	public ReturnMessageDto setEaePlanAction(@RequestParam("idEae") int idEae, @RequestParam("idAgent") int idAgent,
			@RequestBody EaePlanActionDto eaePlanActionDto) {

		logger.debug("entered POST [evaluation/eaePlanAction] => setEaePlanAction with parameter idAgent = {} , idEae = {}, json = {}", idAgent,
				idEae, eaePlanActionDto == null ? "" : eaePlanActionDto.toString());

		eaeSecurityProvider.checkEaeAndWriteRight(idEae, idAgent);

		ReturnMessageDto result = new ReturnMessageDto();
		try {
			evaluationService.setEaePlanAction(idEae, eaePlanActionDto, false);
		} catch (EaeServiceException e) {
			logger.debug(e.getMessage(), e);
			result.getErrors().add(e.getMessage());
			return result;
		}

		result.getInfos().add(messageSource.getMessage("EAE_PLAN_ACTION_OK", null, null));
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "eaeEvolution", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
	public EaeEvolutionDto getEaeEvolution(@RequestParam("idEae") int idEae, @RequestParam("idAgent") int idAgent) {

		logger.debug("entered GET [evaluation/eaeEvolution] => getEaeEvolution with parameter idAgent = {} , idEae = {}", idAgent, idEae);

		eaeSecurityProvider.checkEaeAndReadRight(idEae, idAgent);

		return evaluationService.getEaeEvolution(idEae);
	}

	@ResponseBody
	@RequestMapping(value = "eaeEvolution", produces = "application/json;charset=utf-8", method = RequestMethod.POST, consumes = "application/json")
	public ReturnMessageDto setEaeEvolution(@RequestParam("idEae") int idEae, @RequestParam("idAgent") int idAgent,
			@RequestBody EaeEvolutionDto eaeEvolutioDto) {

		logger.debug("entered POST [evaluation/eaeEvolution] => setEaeEvolution with parameter idAgent = {} , idEae = {}, json = {}", idAgent, idEae,
				eaeEvolutioDto == null ? "" : eaeEvolutioDto.toString());

		eaeSecurityProvider.checkEaeAndWriteRight(idEae, idAgent);

		ReturnMessageDto result = new ReturnMessageDto();
		try {
			evaluationService.setEaeEvolution(idEae, eaeEvolutioDto, false);
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
}
