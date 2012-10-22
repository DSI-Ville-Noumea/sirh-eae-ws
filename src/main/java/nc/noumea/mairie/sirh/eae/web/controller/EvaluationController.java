package nc.noumea.mairie.sirh.eae.web.controller;

import java.util.List;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.dto.EaeFichePosteDto;
import nc.noumea.mairie.sirh.eae.dto.EaeResultatsDto;
import nc.noumea.mairie.sirh.eae.dto.identification.EaeIdentificationDto;
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
	public ResponseEntity<String> setEaeIdentifitcation(@RequestParam("idEae") int idEae, @RequestBody String eaeIdentificationDtoJson) {

		Eae eae = Eae.findEae(idEae);
		
		if (eae == null)
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		
		try {
			eaeService.startEae(eae);
			EaeIdentificationDto dto = new EaeIdentificationDto().deserializeFromJSON(eaeIdentificationDtoJson);
			evaluationService.setEaeIdentification(eae, dto);
		} catch (EaeServiceException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		} catch (EvaluationServiceException e) {
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
}
