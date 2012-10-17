package nc.noumea.mairie.sirh.eae.web.controller;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.dto.EaeIdentificationDto;
import nc.noumea.mairie.sirh.eae.service.IEvaluationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/evaluation")
public class EvaluationController {

	@Autowired
	private IEvaluationService evaluationService;
	
	@ResponseBody
	@RequestMapping(value = "eaeIdentification", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public ResponseEntity<String> getEaeIdentifitcation(@RequestParam("idEae") int idEae) {

		Eae eae = Eae.findEae(idEae);
		
		if (eae == null) {
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		}
		
		EaeIdentificationDto dto = evaluationService.getEaeIdentification(eae);
		
		String result = EaeIdentificationDto.getSerializerForEaeIdentificationDto().serialize(dto);
		
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}
	
	@ResponseBody
	@RequestMapping(value = "eaeIdentification", produces = "application/json;charset=utf-8", method = RequestMethod.POST, consumes = "application/json")
	@Transactional(readOnly = true)
	public ResponseEntity<String> setEaeIdentifitcation(@RequestParam("idEae") int idEae) {

		
		
		return new ResponseEntity<String>("posted", HttpStatus.OK);
	}
}
