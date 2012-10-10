package nc.noumea.mairie.sirh.eae.web.controller;

import java.util.List;

import nc.noumea.mairie.sirh.eae.domain.Eae;
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
    	
    	List<Eae> result = eaeService.listEaesByAgentId(convertedId);
		
		if (result.isEmpty())
			return new ResponseEntity<String>(HttpStatus.NO_CONTENT); 
		
		String jsonResult = Eae.getSerializerForEaeList().serialize(result);
		
		return new ResponseEntity<String>(jsonResult, getJsonHeaders(), HttpStatus.OK);
	}
	
	@ResponseBody
	@RequestMapping("creationEae")
	@Transactional
	public ResponseEntity<String> createEae(@RequestParam("idAgent") int idAgent, @RequestParam("idEvalue") int idEvalue) {
		
		List<Eae> agentEaes = eaeService.findFourPreviousEaesByAgentId(idEvalue);
		
		if (agentEaes.isEmpty())
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		
		Eae lastEae = agentEaes.get(0);
		agentEaes.remove(0);
		
		try {
			eaeService.initializeEae(lastEae, agentEaes);
		} catch (EaeServiceException e) {
			return new ResponseEntity<String>(e.getMessage(), getJsonHeaders(), HttpStatus.CONFLICT);
		}
		
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	@ResponseBody
	@RequestMapping("suppressionEae")
	@Transactional
	public ResponseEntity<String> deleteEae(@RequestParam("idAgent") int idAgent, @RequestParam("idEvalue") int idEvalue) {
		
		return new ResponseEntity<String>(HttpStatus.NOT_IMPLEMENTED);
	}
	
	@ResponseBody
	@RequestMapping("affecterDelegataire")
	@Transactional
	public ResponseEntity<String> affectDelegataire(@RequestParam("idAgent") int idAgent, @RequestParam("idEvalue") int idEvalue, @RequestParam("idDelegataire") int idDelegataire) {
		
		return new ResponseEntity<String>(HttpStatus.NOT_IMPLEMENTED);
	}
}
	
	
