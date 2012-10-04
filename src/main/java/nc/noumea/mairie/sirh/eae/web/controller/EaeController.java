package nc.noumea.mairie.sirh.eae.web.controller;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import nc.noumea.mairie.sirh.domain.Agent;
import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.service.IEaeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import flexjson.JSONSerializer;

@RooWebJson(jsonObject = Eae.class)
@Controller
@RequestMapping("/eaes")
public class EaeController {
	
	@Autowired
	private IEaeService eaeService;
	
	@ResponseBody
	@RequestMapping("listEaesByAgent")
	public ResponseEntity<String> listEaesByAgent(@RequestParam("idAgent") int idAgent) {
		
		HttpHeaders headers = new HttpHeaders();
	    headers.add("Content-Type", "application/json; charset=utf-8");
	    
		List<Eae> result = eaeService.listEaesByAgentId(idAgent);
		
		if (result.isEmpty())
			return new ResponseEntity<String>(headers, HttpStatus.NO_CONTENT); 
		
		
		return new ResponseEntity<String>(Eae.getSerializerForEaeList().serialize(result), headers, HttpStatus.OK);
	}
	
	@ResponseBody
	@RequestMapping("findAgent")
	public ResponseEntity<String> findAgent(@RequestParam("id") int id) {
		
	    HttpHeaders headers = new HttpHeaders();
	    headers.add("Content-Type", "application/json; charset=utf-8");
	    Agent result = Agent.findAgent(id);
	    
	    if (result == null)
	    	return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
	    
	    JSONSerializer serializer = new JSONSerializer()
	    .exclude("class")
	    .exclude("version");
        
        return new ResponseEntity<String>(serializer.serialize(result), headers, HttpStatus.OK);
	}
	
}
