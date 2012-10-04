package nc.noumea.mairie.sirh.eae.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Service;

import nc.noumea.mairie.sirh.eae.domain.Eae;

@Service
public class EaeService implements IEaeService {

	@PersistenceContext(unitName = "eaePersistenceUnit")
	private EntityManager eaeEntityManager;

	@Override
	public List<Eae> listEaesByAgentId(int agentId) {

		Query eaeQuery = eaeEntityManager.createQuery("select e from Eae e where e.idAgent = :idAgent", Eae.class);
		eaeQuery.setParameter("idAgent", agentId);
		
		List<Eae> result = eaeQuery.getResultList();
		
		return result;
	}
}
