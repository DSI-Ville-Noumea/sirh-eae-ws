package nc.noumea.mairie.sirh.eae.web.controller;

import java.util.List;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.dto.EaeDashboardItemDto;
import nc.noumea.mairie.sirh.eae.dto.EaeFinalizationDto;
import nc.noumea.mairie.sirh.eae.dto.EaeListItemDto;
import nc.noumea.mairie.sirh.eae.dto.FinalizationInformationDto;
import nc.noumea.mairie.sirh.eae.security.IEaeSecurityProvider;
import nc.noumea.mairie.sirh.eae.service.EaeServiceException;
import nc.noumea.mairie.sirh.eae.service.IAgentMatriculeConverterService;
import nc.noumea.mairie.sirh.eae.service.IEaeService;
import nc.noumea.mairie.sirh.eae.service.SirhWSConsumerException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping("/eaes")
public class EaeController {
	
	private Logger logger = LoggerFactory.getLogger(EaeController.class);
	
	@Autowired
	private IEaeService eaeService;
	
	@Autowired
	private IAgentMatriculeConverterService agentMatriculeConverterService;
	
	@Autowired
	private IEaeSecurityProvider eaeSecurityProvider;
		
	@ResponseBody
	@RequestMapping(value = "listEaesByAgent", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public ResponseEntity<String> listEaesByAgent(@RequestParam("idAgent") int idAgent) {
		
		Integer convertedId = agentMatriculeConverterService.tryConvertFromADIdAgentToEAEIdAgent(idAgent);
    	
    	List<EaeListItemDto> result;
		try {
			result = eaeService.listEaesByAgentId(convertedId);
		} catch (SirhWSConsumerException e) {
			logger.error(e.getMessage(), e);
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if (result.isEmpty())
			return new ResponseEntity<String>(HttpStatus.NO_CONTENT); 
		
		String jsonResult = EaeListItemDto.getSerializerForEaeListItemDto().serialize(result);
		
		return new ResponseEntity<String>(jsonResult, HttpStatus.OK);
	}
	
	@ResponseBody
	@RequestMapping(value = "initialiserEae", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
	@Transactional(value = "eaeTransactionManager")
	public ResponseEntity<String> initializeEae(@RequestParam("idAgent") int idAgent, @RequestParam("idEvalue") int idEvalue) {
		
		Integer convertedIdAgentEvalue = agentMatriculeConverterService.tryConvertFromADIdAgentToEAEIdAgent(idEvalue);
    	
		List<Eae> agentEaes = eaeService.findCurrentAndPreviousEaesByAgentId(convertedIdAgentEvalue);
		
		if (agentEaes.isEmpty())
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		
		Eae lastEae = agentEaes.get(0);
		Eae previousEae = null;
		if (agentEaes.size() > 1)
			previousEae = agentEaes.get(1);
		
		ResponseEntity<String> response = eaeSecurityProvider.checkEaeWriteRight(lastEae.getIdEae(), idAgent);
		
		if (response != null)
			return response;
		
		try {
			eaeService.initializeEae(lastEae, previousEae);
		} catch (EaeServiceException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		}
		
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	@ResponseBody
	@RequestMapping(value = "resetEae", method = RequestMethod.GET)
	@Transactional(value = "eaeTransactionManager")
	public ResponseEntity<String> resetEaeEvaluateur(@RequestParam("idEae") int idEae, @RequestParam("idAgent") int idAgent) {
			
		ResponseEntity<String> response = eaeSecurityProvider.checkEaeWriteRight(idEae, idAgent);
		
		if (response != null)
			return response;
		
		Eae eaeToReset = eaeService.getEae(idEae);
		
		try {
			eaeService.resetEaeEvaluateur(eaeToReset);
		} catch (EaeServiceException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		}
		
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	@ResponseBody
	@RequestMapping(value = "affecterDelegataire", method = RequestMethod.GET)
	@Transactional(value = "eaeTransactionManager")
	public ResponseEntity<String> setDelegataire(@RequestParam("idEae") int idEae, @RequestParam("idAgent") int idAgent, @RequestParam("idDelegataire") int idDelegataire) {
		
		ResponseEntity<String> response = eaeSecurityProvider.checkEaeWriteRight(idEae, idAgent);
		
		if (response != null)
			return response;
		
		Integer convertedIdAgentDelegataire = agentMatriculeConverterService.tryConvertFromADIdAgentToEAEIdAgent(idDelegataire);
    	
		Eae eae = eaeService.getEae(idEae);
		
		try {
			eaeService.setDelegataire(eae, convertedIdAgentDelegataire);
		} catch (EaeServiceException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		}
		
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	@ResponseBody
	@RequestMapping(value = "tableauDeBord", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public ResponseEntity<String> getEaesDashboard(@RequestParam("idAgent") int idAgent) {
		
		Integer convertedId = agentMatriculeConverterService.tryConvertFromADIdAgentToEAEIdAgent(idAgent);
    	
    	List<EaeDashboardItemDto> result;
		try {
			result = eaeService.getEaesDashboard(convertedId);
		} catch (SirhWSConsumerException e) {
			logger.error(e.getMessage(), e);
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if (result.isEmpty())
			return new ResponseEntity<String>(HttpStatus.NO_CONTENT); 
		
		String jsonResult = EaeDashboardItemDto.getSerializerForEaeDashboardItemDto().serialize(result);
		
		return new ResponseEntity<String>(jsonResult, HttpStatus.OK);
	}

	@ResponseBody
	@RequestMapping(value = "getFinalizationInformation", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public ResponseEntity<String> getFinalizationInformation(@RequestParam("idEae") int idEae, @RequestParam("idAgent") int idAgent) {
		
		ResponseEntity<String> response = eaeSecurityProvider.checkEaeWriteRight(idEae, idAgent);
		
		if (response != null)
			return response;
		
		Eae eae = eaeService.getEae(idEae);
		
		FinalizationInformationDto dto = eaeService.getFinalizationInformation(eae);		
		
		String jsonResult = dto.serializeInJSON();
		
		return new ResponseEntity<String>(jsonResult, HttpStatus.OK);
	}
	
	@ResponseBody
	@RequestMapping(value = "finalizeEae", produces = "application/json;charset=utf-8", method = RequestMethod.POST)
	@Transactional(value = "eaeTransactionManager")
	public ResponseEntity<String> finalizeEae(@RequestParam("idEae") int idEae, @RequestParam("idAgent") int idAgent, @RequestBody String eaeFinalizationDtoJson) {
		
		ResponseEntity<String> response = eaeSecurityProvider.checkEaeWriteRight(idEae, idAgent);
		
		if (response != null)
			return response;
		
		Eae eae = eaeService.getEae(idEae);
		
		try {
			EaeFinalizationDto dto = new EaeFinalizationDto().deserializeFromJSON(eaeFinalizationDtoJson);
			eaeService.finalizEae(eae, agentMatriculeConverterService.tryConvertFromADIdAgentToEAEIdAgent(idAgent), dto);
			eae.flush();
		} catch (EaeServiceException e) {
			eae.clear();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		}
		
		return new ResponseEntity<String>(HttpStatus.OK);
	}
}
	
	
