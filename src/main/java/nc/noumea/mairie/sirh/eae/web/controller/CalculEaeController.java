package nc.noumea.mairie.sirh.eae.web.controller;

import java.text.ParseException;

import javax.servlet.http.HttpServletResponse;

import nc.noumea.mairie.sirh.eae.domain.EaeCampagneTask;
import nc.noumea.mairie.sirh.eae.dto.ReturnMessageDto;
import nc.noumea.mairie.sirh.eae.repository.IEaeRepository;
import nc.noumea.mairie.sirh.eae.service.ICalculEaeService;
import nc.noumea.mairie.sirh.ws.SirhWSConsumerException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
	
	@Autowired
	private IEaeRepository eaeRepository;
	
	@ResponseBody
	@RequestMapping(value = "creerEAESansAffecte", produces = "application/json;charset=utf-8", method = RequestMethod.POST)
	public ReturnMessageDto creerEAESansAffecte(@RequestParam("idCampagneEae") int idCampagneEae, @RequestParam("idAgent") int idAgent,
			HttpServletResponse response) {
		
		logger.debug("entered POST [calculEae/creerEAESansAffecte] => creerEAESansAffecte with parameter idCampagneEAE = {} , idAgent = {}",
				idCampagneEae, idAgent);
		
		ReturnMessageDto rmDto = new ReturnMessageDto();
		
		try {	
			calculEaeService.creerEaeSansAffecte(idCampagneEae, idAgent);
		} catch (SirhWSConsumerException e) {
			logger.error(e.getMessage(), e);
			rmDto.getErrors().add(e.getMessage());
		} catch (ParseException e) {
			logger.error(e.getMessage(), e);
			rmDto.getErrors().add(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			rmDto.getErrors().add(e.getMessage());
		}

		if (!rmDto.getErrors().isEmpty()) {
			response.setStatus(HttpServletResponse.SC_CONFLICT);
		}
		
		return rmDto;
	}
	
	@ResponseBody
	@RequestMapping(value = "creerEaeAffecte", produces = "application/json;charset=utf-8", method = RequestMethod.POST)
	public ReturnMessageDto creerEaeAffecte(@RequestParam("idCampagneEae") int idCampagneEae, @RequestParam("idAgent") int idAgent,
			HttpServletResponse response) {
		
		logger.debug("entered POST [calculEae/creerEaeAffecte] => creerEaeAffecte with parameter idCampagneEAE = {} , idAgent = {}",
				idCampagneEae, idAgent);
		
		ReturnMessageDto rmDto = new ReturnMessageDto();
		
		try {	
			calculEaeService.creerEaeAffecte(idCampagneEae, idAgent);
		} catch (SirhWSConsumerException e) {
			logger.error(e.getMessage(), e);
			rmDto.getErrors().add(e.getMessage());
		} catch (ParseException e) {
			logger.error(e.getMessage(), e);
			rmDto.getErrors().add(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			rmDto.getErrors().add(e.getMessage());
		}

		if (!rmDto.getErrors().isEmpty()) {
			response.setStatus(HttpServletResponse.SC_CONFLICT);
		}
		
		return rmDto;
	}
	
	@ResponseBody
	@RequestMapping(value = "getEaeCampagneTask", method = RequestMethod.GET)
	public EaeCampagneTask findEaeCampagneTask(@RequestParam("idEaeCampagneTask") int idEaeCampagneTask) {

		EaeCampagneTask result = eaeRepository.findEaeCampagneTask(idEaeCampagneTask);
		
		return result;
	}
}
