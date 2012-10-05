package nc.noumea.mairie.sirh.eae.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import nc.noumea.mairie.sirh.domain.Agent;
import nc.noumea.mairie.sirh.eae.domain.Eae;

import org.springframework.stereotype.Service;

@Service
public class EaeService implements IEaeService {

	@PersistenceContext(unitName = "eaePersistenceUnit")
	private EntityManager eaeEntityManager;
	
	@Override
	public List<Eae> listEaesByAgentId(int agentId) {

		TypedQuery<Eae> eaeQuery = eaeEntityManager.createQuery("select e from Eae e where e.idAgent = :idAgent", Eae.class);
		eaeQuery.setParameter("idAgent", agentId);
		
		List<Eae> result = eaeQuery.getResultList();
		
		// For each result, retrieve the Agent, SHD and Delegataire informations from the Agent (other persistenceUnit)
		for(Eae eae : result) {
			eae.setAgentEvalue(Agent.findAgent(eae.getIdAgent()));
			
			if (eae.getIdAgentShd() != null)
				eae.setAgentShd(Agent.findAgent(eae.getIdAgentShd()));
			
			if (eae.getIdAgentDelegataire() != null)
				eae.setAgentDelegataire(Agent.findAgent(eae.getIdAgentDelegataire()));
		}
		
		return result;
	}
}
