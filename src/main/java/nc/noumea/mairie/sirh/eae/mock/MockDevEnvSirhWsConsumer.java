package nc.noumea.mairie.sirh.eae.mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import nc.noumea.mairie.sirh.eae.service.ISirhWsConsumer;

public class MockDevEnvSirhWsConsumer implements ISirhWsConsumer {

	@PersistenceContext(unitName = "eaePersistenceUnit")
	private EntityManager eaeEntityManager;
	
	@Override
	public List<Integer> getListOfEaesForAgentId(int agentId) {
		
		List<Integer> agentIds = new ArrayList<Integer>();
		
		if(agentId == 9007000) {
			agentIds = Arrays.asList(9007001, 9007002, 9007003, 9007004, 9007005, 9007006, 9007007, 9007008, 9007009, 9007010);
		}
		
		if(agentId == 9007001) {
			agentIds = Arrays.asList(9007001);
		}
		
		if(agentId == 9007002) {
			agentIds = Arrays.asList(9007003, 9007004, 9007005, 9007006, 9007007, 9007008, 9007009, 9007010);
		}
		
		if(agentId == 9007003) {
			agentIds = Arrays.asList(9007004, 9007005);
		}
		
		if(agentId == 9007006) {
			agentIds = Arrays.asList(9007007);
		}
		
		if(agentId == 9007008) {
			agentIds = Arrays.asList(9007009, 9007010);
		}
		
		if (agentIds.isEmpty())
			return agentIds;
		
		TypedQuery<Integer> eaeQuery = eaeEntityManager.createQuery(
				"select e.idEae from Eae e where e.idAgent in (:idAgents)",
				Integer.class);
		eaeQuery.setParameter("idAgents", agentIds);
		List<Integer> result = eaeQuery.getResultList();

		return result;
	}

}
