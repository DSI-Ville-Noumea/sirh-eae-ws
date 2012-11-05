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
import nc.noumea.mairie.sirh.eae.service.EaeServiceException;
import nc.noumea.mairie.sirh.eae.service.EvaluationServiceException;
import nc.noumea.mairie.sirh.eae.service.IEaeService;
import nc.noumea.mairie.sirh.eae.service.IEvaluationService;

import org.springframework.beans.factory.annotation.Autowired;
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
	private IEvaluationService evaluationService;
	
	@Autowired
	private IEaeService eaeService;
	
	@ResponseBody
	@RequestMapping(value = "eaeIdentification", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public ResponseEntity<String> getEaeIdentifitcation(@RequestParam("idEae") int idEae) {

		Eae eae = Eae.findEae(idEae);
		
		if (eae == null)
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		
		EaeIdentificationDto dto = evaluationService.getEaeIdentification(eae);
		
		String result = dto.serializeInJSON();
		
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}
	
	@ResponseBody
	@RequestMapping(value = "eaeIdentification", produces = "application/json;charset=utf-8", method = RequestMethod.POST, consumes = "application/json")
	@Transactional(value = "eaeTransactionManager")
	public ResponseEntity<String> setEaeIdentifitcation(@RequestParam("idEae") int idEae, @RequestParam("idEvaluateur") int idEvaluateur, @RequestBody String eaeIdentificationDtoJson) {

		Eae eae = Eae.findEae(idEae);
		
		if (eae == null)
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		
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
		
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	@ResponseBody
	@RequestMapping(value = "eaeFichePoste", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public ResponseEntity<String> getEaeFichePoste(@RequestParam("idEae") int idEae) {

		Eae eae = Eae.findEae(idEae);
		
		if (eae == null)
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		
		List<EaeFichePosteDto> dtos = evaluationService.getEaeFichePoste(eae);
		
		String result = EaeFichePosteDto.getSerializerForEaeFichePosteDto().serialize(dtos);
		
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}
	
	@ResponseBody
	@RequestMapping(value = "eaeResultats", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public ResponseEntity<String> getEaeResultats(@RequestParam("idEae") int idEae) {

		Eae eae = Eae.findEae(idEae);
		
		if (eae == null)
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		
		EaeResultatsDto dto = evaluationService.getEaeResultats(eae);
		
		String result = dto.serializeInJSON();
		
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}
	
	@ResponseBody
	@RequestMapping(value = "eaeResultats", produces = "application/json;charset=utf-8", method = RequestMethod.POST, consumes = "application/json")
	@Transactional(value = "eaeTransactionManager")
	public ResponseEntity<String> setEaeResultats(@RequestParam("idEae") int idEae, @RequestParam("idEvaluateur") int idEvaluateur, @RequestBody String eaeResultatsDtoJson) {

		Eae eae = Eae.findEae(idEae);
		
		if (eae == null)
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		
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
		
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	@ResponseBody
	@RequestMapping(value = "eaeAppreciations", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public ResponseEntity<String> getEaeAppreciations(@RequestParam("idEae") int idEae) {

		Eae eae = Eae.findEae(idEae);
		
		if (eae == null)
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		
		EaeAppreciationsDto dto = evaluationService.getEaeAppreciations(eae);
		
		String result = dto.serializeInJSON();
		
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}
	
	@ResponseBody
	@RequestMapping(value = "eaeAppreciations", produces = "application/json;charset=utf-8", method = RequestMethod.POST, consumes = "application/json")
	@Transactional(value = "eaeTransactionManager")
	public ResponseEntity<String> setEaeAppreciations(@RequestParam("idEae") int idEae, @RequestParam("idEvaluateur") int idEvaluateur, @RequestBody String eaeAppreciationsDtoJson) {

		Eae eae = Eae.findEae(idEae);
		
		if (eae == null)
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		
		try {
			eaeService.startEae(eae);
			EaeAppreciationsDto dto = new EaeAppreciationsDto().deserializeFromJSON(eaeAppreciationsDtoJson);
			evaluationService.setEaeAppreciations(eae, dto);
		} catch (EaeServiceException e) {
			eae.clear();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		}
		
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	@ResponseBody
	@RequestMapping(value = "eaeEvaluation", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public ResponseEntity<String> getEaeEvaluation(@RequestParam("idEae") int idEae) {

		Eae eae = Eae.findEae(idEae);
		
		if (eae == null)
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		
		EaeEvaluationDto dto = evaluationService.getEaeEvaluation(eae);
		
		String result = dto.serializeInJSON();
		
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}
	
	@ResponseBody
	@RequestMapping(value = "eaeEvaluation", produces = "application/json;charset=utf-8", method = RequestMethod.POST, consumes = "application/json")
	@Transactional(value = "eaeTransactionManager")
	public ResponseEntity<String> setEaeEvaluation(@RequestParam("idEae") int idEae, @RequestParam("idEvaluateur") int idEvaluateur, @RequestBody String eaeEvaluationDtoJson) {

		Eae eae = Eae.findEae(idEae);
		
		if (eae == null)
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		
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
		
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	@ResponseBody
	@RequestMapping(value = "eaeAutoEvaluation", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public ResponseEntity<String> getEaeAutoEvaluation(@RequestParam("idEae") int idEae) {

		Eae eae = Eae.findEae(idEae);
		
		if (eae == null)
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		
		EaeAutoEvaluationDto dto = evaluationService.getEaeAutoEvaluation(eae);
		
		String result = dto.serializeInJSON();
		
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}
	
	@ResponseBody
	@RequestMapping(value = "eaeAutoEvaluation", produces = "application/json;charset=utf-8", method = RequestMethod.POST, consumes = "application/json")
	@Transactional(value = "eaeTransactionManager")
	public ResponseEntity<String> setEaeAutoEvaluation(@RequestParam("idEae") int idEae, @RequestParam("idEvaluateur") int idEvaluateur, @RequestBody String eaeAutoEvaluationDtoJson) {

		Eae eae = Eae.findEae(idEae);
		
		if (eae == null)
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		
		try {
			eaeService.startEae(eae);
			EaeAutoEvaluationDto dto = new EaeAutoEvaluationDto().deserializeFromJSON(eaeAutoEvaluationDtoJson);
			evaluationService.setEaeAutoEvaluation(eae, dto);
			eae.flush();
		} catch (EaeServiceException e) {
			eae.clear();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		}
		
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	@ResponseBody
	@RequestMapping(value = "eaePlanAction", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public ResponseEntity<String> getEaePlanAction(@RequestParam("idEae") int idEae) {

		Eae eae = Eae.findEae(idEae);
		
		if (eae == null)
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		
		EaePlanActionDto dto = evaluationService.getEaePlanAction(eae);
		
		String result = dto.serializeInJSON();
		
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}
	
	@ResponseBody
	@RequestMapping(value = "eaePlanAction", produces = "application/json;charset=utf-8", method = RequestMethod.POST, consumes = "application/json")
	@Transactional(value = "eaeTransactionManager")
	public ResponseEntity<String> setEaePlanAction(@RequestParam("idEae") int idEae, @RequestParam("idEvaluateur") int idEvaluateur, @RequestBody String eaePlanActionDtoJson) {

		Eae eae = Eae.findEae(idEae);
		
		if (eae == null)
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		
		try {
			eaeService.startEae(eae);
			EaePlanActionDto dto = new EaePlanActionDto().deserializeFromJSON(eaePlanActionDtoJson);
			evaluationService.setEaePlanAction(eae, dto);
			eae.flush();
		} catch (EaeServiceException e) {
			eae.clear();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		}
		
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	@ResponseBody
	@RequestMapping(value = "eaeEvolution", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public ResponseEntity<String> getEaeEvolution(@RequestParam("idEae") int idEae) {

		Eae eae = Eae.findEae(idEae);
		
		if (eae == null)
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		
		EaeEvolutionDto dto = evaluationService.getEaeEvolution(eae);
		
		String result = dto.serializeInJSON();
		
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}
	
	@ResponseBody
	@RequestMapping(value = "eaeEvolution", produces = "application/json;charset=utf-8", method = RequestMethod.POST, consumes = "application/json")
	@Transactional(value = "eaeTransactionManager")
	public ResponseEntity<String> setEaeEvolution(@RequestParam("idEae") int idEae, @RequestParam("idEvaluateur") int idEvaluateur, @RequestBody String eaeEvolutionDtoJson) {

		Eae eae = Eae.findEae(idEae);
		
		if (eae == null)
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		
		try {
			eaeService.startEae(eae);
			EaeEvolutionDto dto = new EaeEvolutionDto().deserializeFromJSON(eaeEvolutionDtoJson);
			evaluationService.setEaeEvolution(eae, dto);
			eae.flush();
		} catch (EaeServiceException e) {
			eae.clear();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		}
		
		return new ResponseEntity<String>(HttpStatus.OK);
	}
}
