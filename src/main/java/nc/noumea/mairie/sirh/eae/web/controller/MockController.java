package nc.noumea.mairie.sirh.eae.web.controller;

import java.util.Collection;
import java.util.List;

import nc.noumea.mairie.sirh.domain.Agent;

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
@RequestMapping("/mock")
public class MockController {

	@ResponseBody
	@RequestMapping("listAgents")
	@Transactional
	public ResponseEntity<String> listAgents(@RequestParam("param") String param) {

		if (!param.equalsIgnoreCase("please"))
			return new ResponseEntity<String>(HttpStatus.METHOD_NOT_ALLOWED);

		List<Agent> agents = Agent.findAgentEntries(0, 50);

		return new ResponseEntity<String>(Agent.toJsonArray(agents), HttpStatus.OK);
	}

	@ResponseBody
	@RequestMapping(value = "uploadAgents", method = RequestMethod.POST, headers = "Accept=application/json")
	@Transactional(value = "sirhTransactionManager")
	public ResponseEntity<String> uploadAgents(@RequestParam("param") String param, @RequestBody String listOfAgentsJson) {

		if (!param.equalsIgnoreCase("please"))
			return new ResponseEntity<String>(HttpStatus.METHOD_NOT_ALLOWED);

		Collection<Agent> list = Agent.fromJsonArrayToAgents(listOfAgentsJson);

		for (Agent a : list) {
			a.persist();
			a.flush();
		}

		return new ResponseEntity<String>(HttpStatus.OK);
	}
}
