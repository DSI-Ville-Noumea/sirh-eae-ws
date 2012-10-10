package nc.noumea.mairie.sirh.eae.mock;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import nc.noumea.mairie.sirh.eae.service.ISirhWsConsumer;

import org.springframework.stereotype.Service;

@Service
public class MockSirhWsConsumer implements ISirhWsConsumer {

	@PersistenceContext(unitName = "eaePersistenceUnit")
	private EntityManager eaeEntityManager;

	@Override
	public List<Integer> getListOfEaesForAgentId(int agentId) {

		TypedQuery<Integer> eaeQuery = eaeEntityManager.createQuery(
				"select e.idEae from Eae e where e.idAgent = :idAgent",
				Integer.class);
		eaeQuery.setParameter("idAgent", agentId);
		List<Integer> result = eaeQuery.getResultList();

		return result;

		// return new HashSet<Integer>(Arrays.asList(25944, 25945, 25946, 25947,
		// 25948, 25949, 25950));
	}

}