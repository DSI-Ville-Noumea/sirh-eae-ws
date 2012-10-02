package nc.moumea.mairie.sirh.eae.web.controller;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import nc.moumea.mairie.sirh.eae.domain.Eae;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import flexjson.JSONSerializer;

@RooWebJson(jsonObject = Eae.class)
@Controller
@RequestMapping("/eaes")
public class EaeController {
	
	@PersistenceContext
	transient EntityManager entityManager;
	
	@ResponseBody
	@RequestMapping("eaelist")
	public ResponseEntity<String> listEaes() {
		
	    HttpHeaders headers = new HttpHeaders();
	    headers.add("Content-Type", "application/json; charset=utf-8");
	    Eae result = Eae.findEae(1);
	    
//	    if (result == null)
//	    	return 
	    
	    JSONSerializer serializer = new JSONSerializer()
	    .exclude("class")
	    .exclude("version");
        
        return new ResponseEntity<String>(serializer.serialize(result), headers, HttpStatus.OK);
	}
	
}
