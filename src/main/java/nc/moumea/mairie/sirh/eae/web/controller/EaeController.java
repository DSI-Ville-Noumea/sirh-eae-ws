package nc.moumea.mairie.sirh.eae.web.controller;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import nc.moumea.mairie.sirh.domain.Agent;
import nc.moumea.mairie.sirh.eae.domain.Eae;

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
	
	
	@ResponseBody
	@RequestMapping("eaelist")
	public ResponseEntity<String> listEaes() {
		//@RequestParam("idAgent") int idAgent
	    HttpHeaders headers = new HttpHeaders();
	    headers.add("Content-Type", "application/json; charset=utf-8");
//	    Query query = eaeEntityManager.createQuery("from e in Eae where e.idAgent = :idAgent");
//	    query.setParameter("idAgent", idAgent);
//	    List<Eae> result = query.getResultList();
	    List<Eae> result = Eae.findAllEaes();
	    
	    if (result.size() == 0)
	    	return new ResponseEntity<String>(headers, HttpStatus.NO_CONTENT);
	    
	    JSONSerializer serializer = new JSONSerializer()
	    .exclude("class")
	    .exclude("version");
        
        return new ResponseEntity<String>(serializer.serialize(result), headers, HttpStatus.OK);
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
