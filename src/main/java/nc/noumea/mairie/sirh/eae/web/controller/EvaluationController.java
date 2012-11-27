package nc.noumea.mairie.sirh.eae.web.controller;

import java.util.List;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.dto.EaeAppreciationsDto;
import nc.noumea.mairie.sirh.eae.dto.EaeAutoEvaluationDto;
import nc.noumea.mairie.sirh.eae.dto.EaeEvaluationDto;
import nc.noumea.mairie.sirh.eae.dto.EaeEvolutionDto;
import nc.noumea.mairie.sirh.eae.dto.EaeFichePosteDto;
import nc.noumea.mairie.sirh.eae.dto.EaeResultatsDto;
import nc.noumea.mairie.sirh.eae.dto.identification.EaeIdentificationDto;
import nc.noumea.mairie.sirh.eae.dto.planAction.EaePlanActionDto;
import nc.noumea.mairie.sirh.eae.security.IEaeSecurityProvider;
import nc.noumea.mairie.sirh.eae.service.EaeServiceException;
import nc.noumea.mairie.sirh.eae.service.EvaluationServiceException;
import nc.noumea.mairie.sirh.eae.service.IEaeService;
import nc.noumea.mairie.sirh.eae.service.IEvaluationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/evaluation")
public class EvaluationController {

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
	@Transactional(readOnly = true)
	public ResponseEntity<String> getEaeIdentifitcation(@RequestParam("idEae") int idEae, @RequestParam("idAgent") int idAgent) {

		ResponseEntity<String> response = eaeSecurityProvider.checkEaeAndReadRight(idEae, idAgent);
		
		if (response != null)
			return response;
		
		Eae eae = Eae.findEae(idEae);
		
		EaeIdentificationDto dto = evaluationService.getEaeIdentification(eae);
		
		String result = dto.serializeInJSON();
		
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}

	@ResponseBody
	@RequestMapping(value = "eaeIdentification", produces = "application/json;charset=utf-8", method = RequestMethod.POST, consumes = "application/json")
	@Transactional(value = "eaeTransactionManager")
	public ResponseEntity<String> setEaeIdentifitcation(@RequestParam("idEae") int idEae, @RequestParam("idAgent") int idAgent, @RequestBody String eaeIdentificationDtoJson) {
		
		ResponseEntity<String> response = eaeSecurityProvider.checkEaeAndWriteRight(idEae, idAgent);
		
		if (response != null)
			return response;
		
		Eae eae = Eae.findEae(idEae);
		
		try {
			eaeService.startEae(eae);
			EaeIdentificationDto dto = new EaeIdentificationDto().deserializeFromJSON(eaeIdentificationDtoJson);
			evaluationService.setEaeIdentification(eae, dto);
		} catch (EaeServiceException e) {
			eae.clear();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		} catch (EvaluationServiceException e) {
			eae.clear();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		}
		
		return new ResponseEntity<String>(messageSource.getMessage("EAE_IDENTIFICATION_OK", null, null), HttpStatus.OK);
	}
	
	@ResponseBody
	@RequestMapping(value = "eaeFichePoste", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public ResponseEntity<String> getEaeFichePoste(@RequestParam("idEae") int idEae, @RequestParam("idAgent") int idAgent) {

		ResponseEntity<String> response = eaeSecurityProvider.checkEaeAndReadRight(idEae, idAgent);
		
		if (response != null)
			return response;
		
		Eae eae = Eae.findEae(idEae);
		
		List<EaeFichePosteDto> dtos = evaluationService.getEaeFichePoste(eae);
		
		String result = EaeFichePosteDto.getSerializerForEaeFichePosteDto().serialize(dtos);
		
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}
	
	@ResponseBody
	@RequestMapping(value = "eaeResultats", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public ResponseEntity<String> getEaeResultats(@RequestParam("idEae") int idEae, @RequestParam("idAgent") int idAgent) {

		ResponseEntity<String> response = eaeSecurityProvider.checkEaeAndReadRight(idEae, idAgent);
		
		if (response != null)
			return response;
		
		Eae eae = Eae.findEae(idEae);
		
		EaeResultatsDto dto = evaluationService.getEaeResultats(eae);
		
		String result = dto.serializeInJSON();
		
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}
	
	@ResponseBody
	@RequestMapping(value = "eaeResultats", produces = "application/json;charset=utf-8", method = RequestMethod.POST, consumes = "application/json")
	@Transactional(value = "eaeTransactionManager")
	public ResponseEntity<String> setEaeResultats(@RequestParam("idEae") int idEae, @RequestParam("idAgent") int idAgent, @RequestBody String eaeResultatsDtoJson) {

		ResponseEntity<String> response = eaeSecurityProvider.checkEaeAndWriteRight(idEae, idAgent);
		
		if (response != null)
			return response;
		
		Eae eae = Eae.findEae(idEae);
		
		try {
			eaeService.startEae(eae);
			EaeResultatsDto dto = new EaeResultatsDto().deserializeFromJSON(eaeResultatsDtoJson);
			evaluationService.setEaeResultats(eae, dto);
		} catch (EaeServiceException e) {
			eae.clear();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		} catch (EvaluationServiceException e) {
			eae.clear();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		}
		
		return new ResponseEntity<String>(messageSource.getMessage("EAE_RESULTATS_OK", null, null), HttpStatus.OK);
	}
	
	@ResponseBody
	@RequestMapping(value = "eaeAppreciations", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public ResponseEntity<String> getEaeAppreciations(@RequestParam("idEae") int idEae, @RequestParam("idAgent") int idAgent) {

		ResponseEntity<String> response = eaeSecurityProvider.checkEaeAndReadRight(idEae, idAgent);
		
		if (response != null)
			return response;
		
		Eae eae = Eae.findEae(idEae);
		
		EaeAppreciationsDto dto = evaluationService.getEaeAppreciations(eae);
		
		String result = dto.serializeInJSON();
		
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}
	
	@ResponseBody
	@RequestMapping(value = "eaeAppreciations", produces = "application/json;charset=utf-8", method = RequestMethod.POST, consumes = "application/json")
	@Transactional(value = "eaeTransactionManager")
	public ResponseEntity<String> setEaeAppreciations(@RequestParam("idEae") int idEae, @RequestParam("idAgent") int idAgent, @RequestBody String eaeAppreciationsDtoJson) {

		ResponseEntity<String> response = eaeSecurityProvider.checkEaeAndWriteRight(idEae, idAgent);
		
		if (response != null)
			return response;
		
		Eae eae = Eae.findEae(idEae);
		
		try {
			eaeService.startEae(eae);
			EaeAppreciationsDto dto = new EaeAppreciationsDto().deserializeFromJSON(eaeAppreciationsDtoJson);
			evaluationService.setEaeAppreciations(eae, dto);
		} catch (EaeServiceException e) {
			eae.clear();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		}
		
		return new ResponseEntity<String>(messageSource.getMessage("EAE_APPRECIATIONS_OK", null, null), HttpStatus.OK);
	}
	
	@ResponseBody
	@RequestMapping(value = "eaeEvaluation", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public ResponseEntity<String> getEaeEvaluation(@RequestParam("idEae") int idEae, @RequestParam("idAgent") int idAgent) {

		ResponseEntity<String> response = eaeSecurityProvider.checkEaeAndReadRight(idEae, idAgent);
		
		if (response != null)
			return response;
		
		Eae eae = Eae.findEae(idEae);
		
		EaeEvaluationDto dto = evaluationService.getEaeEvaluation(eae);
		
		String result = dto.serializeInJSON();
		
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}
	
	@ResponseBody
	@RequestMapping(value = "eaeEvaluation", produces = "application/json;charset=utf-8", method = RequestMethod.POST, consumes = "application/json")
	@Transactional(value = "eaeTransactionManager")
	public ResponseEntity<String> setEaeEvaluation(@RequestParam("idEae") int idEae, @RequestParam("idAgent") int idAgent, @RequestBody String eaeEvaluationDtoJson) {

		ResponseEntity<String> response = eaeSecurityProvider.checkEaeAndWriteRight(idEae, idAgent);
		
		if (response != null)
			return response;
		
		Eae eae = Eae.findEae(idEae);
		
		try {
			eaeService.startEae(eae);
			EaeEvaluationDto dto = new EaeEvaluationDto().deserializeFromJSON(eaeEvaluationDtoJson);
			evaluationService.setEaeEvaluation(eae, dto);
			eae.flush();
		} catch (EaeServiceException e) {
			eae.clear();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		} catch (EvaluationServiceException e) {
			eae.clear();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		}
		
		return new ResponseEntity<String>(messageSource.getMessage("EAE_EVALUATION_OK", null, null), HttpStatus.OK);
	}
	
	@ResponseBody
	@RequestMapping(value = "eaeAutoEvaluation", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public ResponseEntity<String> getEaeAutoEvaluation(@RequestParam("idEae") int idEae, @RequestParam("idAgent") int idAgent) {

		ResponseEntity<String> response = eaeSecurityProvider.checkEaeAndReadRight(idEae, idAgent);
		
		if (response != null)
			return response;
		
		Eae eae = Eae.findEae(idEae);
		
		EaeAutoEvaluationDto dto = evaluationService.getEaeAutoEvaluation(eae);
		
		String result = dto.serializeInJSON();
		
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}
	
	@ResponseBody
	@RequestMapping(value = "eaeAutoEvaluation", produces = "application/json;charset=utf-8", method = RequestMethod.POST, consumes = "application/json")
	@Transactional(value = "eaeTransactionManager")
	public ResponseEntity<String> setEaeAutoEvaluation(@RequestParam("idEae") int idEae, @RequestParam("idAgent") int idAgent, @RequestBody String eaeAutoEvaluationDtoJson) {

		ResponseEntity<String> response = eaeSecurityProvider.checkEaeAndWriteRight(idEae, idAgent);
		
		if (response != null)
			return response;
		
		Eae eae = Eae.findEae(idEae);
		
		try {
			eaeService.startEae(eae);
			EaeAutoEvaluationDto dto = new EaeAutoEvaluationDto().deserializeFromJSON(eaeAutoEvaluationDtoJson);
			evaluationService.setEaeAutoEvaluation(eae, dto);
			eae.flush();
		} catch (EaeServiceException e) {
			eae.clear();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		}
		
		return new ResponseEntity<String>(messageSource.getMessage("EAE_AUTO_EVALUATION_OK", null, null), HttpStatus.OK);
	}
	
	@ResponseBody
	@RequestMapping(value = "eaePlanAction", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public ResponseEntity<String> getEaePlanAction(@RequestParam("idEae") int idEae, @RequestParam("idAgent") int idAgent) {

		ResponseEntity<String> response = eaeSecurityProvider.checkEaeAndReadRight(idEae, idAgent);
		
		if (response != null)
			return response;
		
		Eae eae = Eae.findEae(idEae);
		
		EaePlanActionDto dto = evaluationService.getEaePlanAction(eae);
		
		String result = dto.serializeInJSON();
		
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}
	
	@ResponseBody
	@RequestMapping(value = "eaePlanAction", produces = "application/json;charset=utf-8", method = RequestMethod.POST, consumes = "application/json")
	@Transactional(value = "eaeTransactionManager")
	public ResponseEntity<String> setEaePlanAction(@RequestParam("idEae") int idEae, @RequestParam("idAgent") int idAgent, @RequestBody String eaePlanActionDtoJson) {

		ResponseEntity<String> response = eaeSecurityProvider.checkEaeAndWriteRight(idEae, idAgent);
		
		if (response != null)
			return response;
		
		Eae eae = Eae.findEae(idEae);
		
		try {
			eaeService.startEae(eae);
			EaePlanActionDto dto = new EaePlanActionDto().deserializeFromJSON(eaePlanActionDtoJson);
			evaluationService.setEaePlanAction(eae, dto);
			eae.flush();
		} catch (EaeServiceException e) {
			eae.clear();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		}
		
		return new ResponseEntity<String>(messageSource.getMessage("EAE_PLAN_ACTION_OK", null, null), HttpStatus.OK);
	}
	
	@ResponseBody
	@RequestMapping(value = "eaeEvolution", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public ResponseEntity<String> getEaeEvolution(@RequestParam("idEae") int idEae, @RequestParam("idAgent") int idAgent) {

		ResponseEntity<String> response = eaeSecurityProvider.checkEaeAndReadRight(idEae, idAgent);
		
		if (response != null)
			return response;
		
		Eae eae = Eae.findEae(idEae);
		
		EaeEvolutionDto dto = evaluationService.getEaeEvolution(eae);
		
		String result = dto.serializeInJSON();
		
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}
	
	@ResponseBody
	@RequestMapping(value = "eaeEvolution", produces = "application/json;charset=utf-8", method = RequestMethod.POST, consumes = "application/json")
	@Transactional(value = "eaeTransactionManager")
	public ResponseEntity<String> setEaeEvolution(@RequestParam("idEae") int idEae, @RequestParam("idAgent") int idAgent, @RequestBody String eaeEvolutionDtoJson) {

		ResponseEntity<String> response = eaeSecurityProvider.checkEaeAndWriteRight(idEae, idAgent);
		
		if (response != null)
			return response;
		
		Eae eae = Eae.findEae(idEae);
		
		try {
			eaeService.startEae(eae);
			EaeEvolutionDto dto = new EaeEvolutionDto().deserializeFromJSON(eaeEvolutionDtoJson);
			evaluationService.setEaeEvolution(eae, dto);
			eae.flush();
		} catch (EaeServiceException e) {
			eae.clear();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		} catch (EvaluationServiceException e) {
			eae.clear();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		}
		
		return new ResponseEntity<String>(messageSource.getMessage("EAE_EVOLUTION_OK", null, null), HttpStatus.OK);
	}
}
