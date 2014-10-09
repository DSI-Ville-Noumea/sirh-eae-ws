package nc.noumea.mairie.sirh.eae.web.controller;

import java.util.List;

import nc.noumea.mairie.sirh.eae.dto.EaeAppreciationsDto;
import nc.noumea.mairie.sirh.eae.dto.EaeAutoEvaluationDto;
import nc.noumea.mairie.sirh.eae.dto.EaeEvaluationDto;
import nc.noumea.mairie.sirh.eae.dto.EaeEvolutionDto;
import nc.noumea.mairie.sirh.eae.dto.EaeResultatsDto;
import nc.noumea.mairie.sirh.eae.dto.identification.EaeIdentificationDto;
import nc.noumea.mairie.sirh.eae.dto.planAction.EaePlanActionDto;
import nc.noumea.mairie.sirh.eae.dto.poste.EaeFichePosteDto;
import nc.noumea.mairie.sirh.eae.security.IEaeSecurityProvider;
import nc.noumea.mairie.sirh.eae.service.EaeServiceException;
import nc.noumea.mairie.sirh.eae.service.EvaluationServiceException;
import nc.noumea.mairie.sirh.eae.service.IEaeService;
import nc.noumea.mairie.sirh.eae.service.IEvaluationService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/evaluation")
public class EvaluationController {

	private Logger logger = LoggerFactory.getLogger(EvaluationController.class);

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private IEvaluationService evaluationService;

	@Autowired
	private IEaeService eaeService;

	@Autowired
	private IEaeSecurityProvider eaeSecurityProvider;

	@ResponseBody
	@RequestMapping(value = "eaeIdentification", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
	public ResponseEntity<String> getEaeIdentifitcation(@RequestParam("idEae") int idEae,
			@RequestParam("idAgent") int idAgent) {

		logger.debug(
				"entered GET [evaluation/eaeIdentification] => getEaeIdentifitcation with parameter idAgent = {} , idEae = {}",
				idAgent, idEae);

		ResponseEntity<String> response = eaeSecurityProvider.checkEaeAndReadRight(idEae, idAgent);

		if (response != null)
			return response;

		EaeIdentificationDto dto = evaluationService.getEaeIdentification(idEae);

		String result = dto.serializeInJSON();

		return new ResponseEntity<String>(result, HttpStatus.OK);
	}

	@ResponseBody
	@RequestMapping(value = "eaeIdentification", produces = "application/json;charset=utf-8", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<String> setEaeIdentifitcation(@RequestParam("idEae") int idEae,
			@RequestParam("idAgent") int idAgent, @RequestBody String eaeIdentificationDtoJson) {

		logger.debug(
				"entered POST [evaluation/eaeIdentification] => setEaeIdentifitcation with parameter idAgent = {} , idEae = {}, json = {}",
				idAgent, idEae, eaeIdentificationDtoJson);

		ResponseEntity<String> response = eaeSecurityProvider.checkEaeAndWriteRight(idEae, idAgent);

		if (response != null)
			return response;

		try {
			EaeIdentificationDto dto = new EaeIdentificationDto().deserializeFromJSON(eaeIdentificationDtoJson);
			evaluationService.setEaeIdentification(idEae, dto);
		} catch (EaeServiceException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		} catch (EvaluationServiceException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		}

		return new ResponseEntity<String>(messageSource.getMessage("EAE_IDENTIFICATION_OK", null, null), HttpStatus.OK);
	}

	@ResponseBody
	@RequestMapping(value = "eaeFichePoste", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
	public ResponseEntity<String> getEaeFichePoste(@RequestParam("idEae") int idEae,
			@RequestParam("idAgent") int idAgent) {

		logger.debug(
				"entered GET [evaluation/eaeFichePoste] => getEaeFichePoste with parameter idAgent = {} , idEae = {}",
				idAgent, idEae);

		ResponseEntity<String> response = eaeSecurityProvider.checkEaeAndReadRight(idEae, idAgent);

		if (response != null)
			return response;

		List<EaeFichePosteDto> dtos = evaluationService.getEaeFichePoste(idEae);

		String result = EaeFichePosteDto.getSerializerForEaeFichePosteDto().serialize(dtos);

		return new ResponseEntity<String>(result, HttpStatus.OK);
	}

	@ResponseBody
	@RequestMapping(value = "eaeResultats", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
	public ResponseEntity<String> getEaeResultats(@RequestParam("idEae") int idEae, @RequestParam("idAgent") int idAgent) {

		logger.debug(
				"entered GET [evaluation/eaeResultats] => getEaeResultats with parameter idAgent = {} , idEae = {}",
				idAgent, idEae);

		ResponseEntity<String> response = eaeSecurityProvider.checkEaeAndReadRight(idEae, idAgent);

		if (response != null)
			return response;

		EaeResultatsDto dto = evaluationService.getEaeResultats(idEae);

		String result = dto.serializeInJSON();

		return new ResponseEntity<String>(result, HttpStatus.OK);
	}

	@ResponseBody
	@RequestMapping(value = "eaeResultats", produces = "application/json;charset=utf-8", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<String> setEaeResultats(@RequestParam("idEae") int idEae,
			@RequestParam("idAgent") int idAgent, @RequestBody String eaeResultatsDtoJson) {

		logger.debug(
				"entered POST [evaluation/eaeResultats] => setEaeResultats with parameter idAgent = {} , idEae = {}, json = {}",
				idAgent, idEae, eaeResultatsDtoJson);

		ResponseEntity<String> response = eaeSecurityProvider.checkEaeAndWriteRight(idEae, idAgent);

		if (response != null)
			return response;

		try {
			EaeResultatsDto dto = new EaeResultatsDto().deserializeFromJSON(eaeResultatsDtoJson);
			evaluationService.setEaeResultats(idEae, dto);
		} catch (EaeServiceException e) {
			eaeService.clear();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		} catch (EvaluationServiceException e) {
			eaeService.clear();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		}

		return new ResponseEntity<String>(messageSource.getMessage("EAE_RESULTATS_OK", null, null), HttpStatus.OK);
	}

	@ResponseBody
	@RequestMapping(value = "eaeAppreciations", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
	public ResponseEntity<String> getEaeAppreciations(@RequestParam("idEae") int idEae,
			@RequestParam("idAgent") int idAgent, @RequestParam(value = "annee", required = false) String annee) {

		logger.debug(
				"entered GET [evaluation/eaeAppreciations] => getEaeAppreciations with parameter idAgent = {} , idEae = {}",
				idAgent, idEae);

		if(null == annee) {
			ResponseEntity<String> response = eaeSecurityProvider.checkEaeAndReadRight(idEae, idAgent);
			if (response != null)
				return response;
		}
		// si l'année est renseignée, alors il faut trouver l'EAE de l'annee
		// precedente de la personne
		EaeAppreciationsDto dto = evaluationService.getEaeAppreciations(idEae, annee);

		if(null == dto) {
			return new ResponseEntity<String>("AUCUN EAE TROUVE", HttpStatus.CONFLICT);
		}
		
		String result = dto.serializeInJSON();

		return new ResponseEntity<String>(result, HttpStatus.OK);
	}

	@ResponseBody
	@RequestMapping(value = "eaeAppreciations", produces = "application/json;charset=utf-8", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<String> setEaeAppreciations(@RequestParam("idEae") int idEae,
			@RequestParam("idAgent") int idAgent, @RequestBody String eaeAppreciationsDtoJson) {

		logger.debug(
				"entered POST [evaluation/eaeAppreciations] => setEaeAppreciations with parameter idAgent = {} , idEae = {}, json = {}",
				idAgent, idEae, eaeAppreciationsDtoJson);

		ResponseEntity<String> response = eaeSecurityProvider.checkEaeAndWriteRight(idEae, idAgent);

		if (response != null)
			return response;

		try {
			EaeAppreciationsDto dto = new EaeAppreciationsDto().deserializeFromJSON(eaeAppreciationsDtoJson);
			evaluationService.setEaeAppreciations(idEae, dto);
		} catch (EaeServiceException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		}

		return new ResponseEntity<String>(messageSource.getMessage("EAE_APPRECIATIONS_OK", null, null), HttpStatus.OK);
	}

	@ResponseBody
	@RequestMapping(value = "eaeEvaluation", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
	public ResponseEntity<String> getEaeEvaluation(@RequestParam("idEae") int idEae,
			@RequestParam("idAgent") int idAgent) {

		logger.debug(
				"entered GET [evaluation/eaeEvaluation] => getEaeEvaluation with parameter idAgent = {} , idEae = {}",
				idAgent, idEae);

		ResponseEntity<String> response = eaeSecurityProvider.checkEaeAndReadRight(idEae, idAgent);

		if (response != null)
			return response;

		EaeEvaluationDto dto = evaluationService.getEaeEvaluation(idEae);

		String result = dto.serializeInJSON();

		return new ResponseEntity<String>(result, HttpStatus.OK);
	}

	@ResponseBody
	@RequestMapping(value = "eaeEvaluation", produces = "application/json;charset=utf-8", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<String> setEaeEvaluation(@RequestParam("idEae") int idEae,
			@RequestParam("idAgent") int idAgent, @RequestBody String eaeEvaluationDtoJson) {

		logger.debug(
				"entered POST [evaluation/eaeEvaluation] => setEaeEvaluation with parameter idAgent = {} , idEae = {}, json = {}",
				idAgent, idEae, eaeEvaluationDtoJson);

		ResponseEntity<String> response = eaeSecurityProvider.checkEaeAndWriteRight(idEae, idAgent);

		if (response != null)
			return response;

		try {
			EaeEvaluationDto dto = new EaeEvaluationDto().deserializeFromJSON(eaeEvaluationDtoJson);
			evaluationService.setEaeEvaluation(idEae, dto);
		} catch (EaeServiceException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		} catch (EvaluationServiceException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		}

		return new ResponseEntity<String>(messageSource.getMessage("EAE_EVALUATION_OK", null, null), HttpStatus.OK);
	}

	@ResponseBody
	@RequestMapping(value = "eaeAutoEvaluation", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
	public ResponseEntity<String> getEaeAutoEvaluation(@RequestParam("idEae") int idEae,
			@RequestParam("idAgent") int idAgent) {

		logger.debug(
				"entered GET [evaluation/eaeAutoEvaluation] => getEaeAutoEvaluation with parameter idAgent = {} , idEae = {}",
				idAgent, idEae);

		ResponseEntity<String> response = eaeSecurityProvider.checkEaeAndReadRight(idEae, idAgent);

		if (response != null)
			return response;

		EaeAutoEvaluationDto dto = evaluationService.getEaeAutoEvaluation(idEae);

		String result = dto.serializeInJSON();

		return new ResponseEntity<String>(result, HttpStatus.OK);
	}

	@ResponseBody
	@RequestMapping(value = "eaeAutoEvaluation", produces = "application/json;charset=utf-8", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<String> setEaeAutoEvaluation(@RequestParam("idEae") int idEae,
			@RequestParam("idAgent") int idAgent, @RequestBody String eaeAutoEvaluationDtoJson) {

		logger.debug(
				"entered POST [evaluation/eaeAutoEvaluation] => setEaeAutoEvaluation with parameter idAgent = {} , idEae = {}, json = {}",
				idAgent, idEae, eaeAutoEvaluationDtoJson);

		ResponseEntity<String> response = eaeSecurityProvider.checkEaeAndWriteRight(idEae, idAgent);

		if (response != null)
			return response;

		try {
			EaeAutoEvaluationDto dto = new EaeAutoEvaluationDto().deserializeFromJSON(eaeAutoEvaluationDtoJson);
			evaluationService.setEaeAutoEvaluation(idEae, dto);
		} catch (EaeServiceException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		}

		return new ResponseEntity<String>(messageSource.getMessage("EAE_AUTO_EVALUATION_OK", null, null), HttpStatus.OK);
	}

	@ResponseBody
	@RequestMapping(value = "eaePlanAction", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
	public ResponseEntity<String> getEaePlanAction(@RequestParam("idEae") int idEae,
			@RequestParam("idAgent") int idAgent) {

		logger.debug(
				"entered GET [evaluation/eaePlanAction] => getEaePlanAction with parameter idAgent = {} , idEae = {}",
				idAgent, idEae);

		ResponseEntity<String> response = eaeSecurityProvider.checkEaeAndReadRight(idEae, idAgent);

		if (response != null)
			return response;

		EaePlanActionDto dto = evaluationService.getEaePlanAction(idEae);

		String result = dto.serializeInJSON();

		return new ResponseEntity<String>(result, HttpStatus.OK);
	}

	@ResponseBody
	@RequestMapping(value = "eaePlanAction", produces = "application/json;charset=utf-8", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<String> setEaePlanAction(@RequestParam("idEae") int idEae,
			@RequestParam("idAgent") int idAgent, @RequestBody String eaePlanActionDtoJson) {

		logger.debug(
				"entered POST [evaluation/eaePlanAction] => setEaePlanAction with parameter idAgent = {} , idEae = {}, json = {}",
				idAgent, idEae, eaePlanActionDtoJson);

		ResponseEntity<String> response = eaeSecurityProvider.checkEaeAndWriteRight(idEae, idAgent);

		if (response != null)
			return response;

		try {
			EaePlanActionDto dto = new EaePlanActionDto().deserializeFromJSON(eaePlanActionDtoJson);
			evaluationService.setEaePlanAction(idEae, dto);
		} catch (EaeServiceException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		}

		return new ResponseEntity<String>(messageSource.getMessage("EAE_PLAN_ACTION_OK", null, null), HttpStatus.OK);
	}

	@ResponseBody
	@RequestMapping(value = "eaeEvolution", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
	public ResponseEntity<String> getEaeEvolution(@RequestParam("idEae") int idEae, @RequestParam("idAgent") int idAgent) {

		logger.debug(
				"entered GET [evaluation/eaeEvolution] => getEaeEvolution with parameter idAgent = {} , idEae = {}",
				idAgent, idEae);

		ResponseEntity<String> response = eaeSecurityProvider.checkEaeAndReadRight(idEae, idAgent);

		if (response != null)
			return response;

		EaeEvolutionDto dto = evaluationService.getEaeEvolution(idEae);

		String result = dto.serializeInJSON();

		return new ResponseEntity<String>(result, HttpStatus.OK);
	}

	@ResponseBody
	@RequestMapping(value = "eaeEvolution", produces = "application/json;charset=utf-8", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<String> setEaeEvolution(@RequestParam("idEae") int idEae,
			@RequestParam("idAgent") int idAgent, @RequestBody String eaeEvolutionDtoJson) {

		logger.debug(
				"entered POST [evaluation/eaeEvolution] => setEaeEvolution with parameter idAgent = {} , idEae = {}, json = {}",
				idAgent, idEae, eaeEvolutionDtoJson);

		ResponseEntity<String> response = eaeSecurityProvider.checkEaeAndWriteRight(idEae, idAgent);

		if (response != null)
			return response;

		try {
			EaeEvolutionDto dto = new EaeEvolutionDto().deserializeFromJSON(eaeEvolutionDtoJson);
			evaluationService.setEaeEvolution(idEae, dto);
		} catch (EaeServiceException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		} catch (EvaluationServiceException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		}

		return new ResponseEntity<String>(messageSource.getMessage("EAE_EVOLUTION_OK", null, null), HttpStatus.OK);
	}
}
