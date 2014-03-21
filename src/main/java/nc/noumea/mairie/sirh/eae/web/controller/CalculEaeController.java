package nc.noumea.mairie.sirh.eae.web.controller;

import java.text.ParseException;

import nc.noumea.mairie.sirh.eae.service.ICalculEaeService;
import nc.noumea.mairie.sirh.eae.service.SirhWSConsumerException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping("/calculEae")
public class CalculEaeController {

	private Logger logger = LoggerFactory.getLogger(CalculEaeController.class);
	
	@Autowired
	private ICalculEaeService calculEaeService;
	
	@ResponseBody
	@RequestMapping(value = "creerEAESansAffecte", produces = "application/json;charset=utf-8", method = RequestMethod.POST)
	@Transactional(value = "eaeTransactionManager")
	public ResponseEntity<String> creerEAESansAffecte(@RequestParam("idCampagneEae") int idCampagneEae, @RequestParam("idAgent") int idAgent) {
		
		logger.debug("entered POST [calculEae/creerEAESansAffecte] => creerEAESansAffecte with parameter idCampagneEAE = {} , idAgent = {}",
				idCampagneEae, idAgent);
		
		try {	
			calculEaeService.creerEaeSansAffecte(idCampagneEae, idAgent);
		} catch (SirhWSConsumerException e) {
			logger.error(e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (ParseException e) {
			logger.error(e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	@ResponseBody
	@RequestMapping(value = "creerEaeAffecte", produces = "application/json;charset=utf-8", method = RequestMethod.POST)
	@Transactional(value = "eaeTransactionManager")
	public ResponseEntity<String> creerEaeAffecte(@RequestParam("idCampagneEae") int idCampagneEae, @RequestParam("idAgent") int idAgent) {
		
		logger.debug("entered POST [calculEae/creerEaeAffecte] => creerEaeAffecte with parameter idCampagneEAE = {} , idAgent = {}",
				idCampagneEae, idAgent);
		
		try {	
			calculEaeService.creerEaeAffecte(idCampagneEae, idAgent);
		} catch (SirhWSConsumerException e) {
			logger.error(e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (ParseException e) {
			logger.error(e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<String>(HttpStatus.OK);
	}
}
