package nc.noumea.mairie.sirh.eae.web.controller;

import java.util.List;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.dto.EaeDashboardItemDto;
import nc.noumea.mairie.sirh.eae.dto.EaeListItemDto;
import nc.noumea.mairie.sirh.eae.service.EaeServiceException;
import nc.noumea.mairie.sirh.eae.service.IAgentMatriculeConverterService;
import nc.noumea.mairie.sirh.eae.service.IEaeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RooWebJson(jsonObject = Eae.class)
@Controller
@RequestMapping("/eaes")
public class EaeController {
	
	@Autowired
	private IEaeService eaeService;
	
	@Autowired
	private IAgentMatriculeConverterService agentMatriculeConverterService;
	
	private HttpHeaders getJsonHeaders() {
		HttpHeaders headers = new HttpHeaders();
	    headers.add("Content-Type", "application/json; charset=utf-8");
	    return headers;
	}
	
	@ResponseBody
	@RequestMapping("listEaesByAgent")
	@Transactional(readOnly = true)
	public ResponseEntity<String> listEaesByAgent(@RequestParam("idAgent") int idAgent) {
		
		Integer convertedId = agentMatriculeConverterService.tryConvertFromADIdAgentToEAEIdAgent(idAgent);
    	
    	List<EaeListItemDto> result = eaeService.listEaesByAgentId(convertedId);
		
		if (result.isEmpty())
			return new ResponseEntity<String>(HttpStatus.NO_CONTENT); 
		
		String jsonResult = EaeListItemDto.getSerializerForEaeListItemDto().serialize(result);
		
		return new ResponseEntity<String>(jsonResult, getJsonHeaders(), HttpStatus.OK);
	}
	
	@ResponseBody
	@RequestMapping("initialiserEae")
	@Transactional
	public ResponseEntity<String> initializeEae(@RequestParam("idAgent") int idAgent, @RequestParam("idEvalue") int idEvalue) {
		
		Integer convertedIdAgentEvalue = agentMatriculeConverterService.tryConvertFromADIdAgentToEAEIdAgent(idEvalue);
    	
		List<Eae> agentEaes = eaeService.findCurrentAndPreviousEaesByAgentId(convertedIdAgentEvalue);
		
		if (agentEaes.isEmpty())
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		
		Eae lastEae = agentEaes.get(0);
		Eae previousEae = null;
		if (agentEaes.size() > 1)
			previousEae = agentEaes.get(1);
		
		try {
			eaeService.initializeEae(lastEae, previousEae);
		} catch (EaeServiceException e) {
			return new ResponseEntity<String>(e.getMessage(), getJsonHeaders(), HttpStatus.CONFLICT);
		}
		
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	@ResponseBody
	@RequestMapping("demarrerEae")
	@Transactional
	public ResponseEntity<String> startEae(@RequestParam("idAgent") int idAgent, @RequestParam("idEvalue") int idEvalue) {
		
		Integer convertedIdAgentEvalue = agentMatriculeConverterService.tryConvertFromADIdAgentToEAEIdAgent(idEvalue);
    	
		Eae eaeToStart = eaeService.findLastEaeByAgentId(convertedIdAgentEvalue);
		
		if (eaeToStart == null)
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		
		try {
			eaeService.startEae(eaeToStart);
		} catch (EaeServiceException e) {
			return new ResponseEntity<String>(e.getMessage(), getJsonHeaders(), HttpStatus.CONFLICT);
		}
		
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	@ResponseBody
	@RequestMapping("resetEae")
	@Transactional
	public ResponseEntity<String> resetEaeEvaluateur(@RequestParam("idAgent") int idAgent, @RequestParam("idEvalue") int idEvalue) {
		
		Integer convertedIdAgentEvalue = agentMatriculeConverterService.tryConvertFromADIdAgentToEAEIdAgent(idEvalue);
    	
		Eae eaeToReset = eaeService.findLastEaeByAgentId(convertedIdAgentEvalue);
		
		if (eaeToReset == null)
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		
		try {
			eaeService.resetEaeEvaluateur(eaeToReset);
		} catch (EaeServiceException e) {
			return new ResponseEntity<String>(e.getMessage(), getJsonHeaders(), HttpStatus.CONFLICT);
		}
		
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	@ResponseBody
	@RequestMapping("affecterDelegataire")
	@Transactional
	public ResponseEntity<String> setDelegataire(@RequestParam("idAgent") int idAgent, @RequestParam("idEvalue") int idEvalue, @RequestParam("idDelegataire") int idDelegataire) {
		
		Integer convertedIdAgentEvalue = agentMatriculeConverterService.tryConvertFromADIdAgentToEAEIdAgent(idEvalue);
		Integer convertedIdAgentDelegataire = agentMatriculeConverterService.tryConvertFromADIdAgentToEAEIdAgent(idDelegataire);
    	
		Eae eae = eaeService.findLastEaeByAgentId(convertedIdAgentEvalue);
		
		if (eae == null)
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		
		try {
			eaeService.setDelegataire(eae, convertedIdAgentDelegataire);
		} catch (EaeServiceException e) {
			return new ResponseEntity<String>(e.getMessage(), getJsonHeaders(), HttpStatus.CONFLICT);
		}
		
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	@ResponseBody
	@RequestMapping("tableauDeBord")
	@Transactional
	public ResponseEntity<String> getEaesDashboard(@RequestParam("idAgent") int idAgent) {
		
		Integer convertedId = agentMatriculeConverterService.tryConvertFromADIdAgentToEAEIdAgent(idAgent);
    	
    	List<EaeDashboardItemDto> result = eaeService.getEaesDashboard(convertedId);
		
		if (result.isEmpty())
			return new ResponseEntity<String>(HttpStatus.NO_CONTENT); 
		
		String jsonResult = EaeDashboardItemDto.getSerializerForEaeDashboardItemDto().serialize(result);
		
		return new ResponseEntity<String>(jsonResult, getJsonHeaders(), HttpStatus.OK);
	}
}
	
	
