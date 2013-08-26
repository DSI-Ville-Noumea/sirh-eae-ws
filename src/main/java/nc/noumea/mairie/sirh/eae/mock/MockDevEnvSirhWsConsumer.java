package nc.noumea.mairie.sirh.eae.mock;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import nc.noumea.mairie.sirh.eae.dto.CampagneEaeDto;
import nc.noumea.mairie.sirh.eae.service.ISirhWsConsumer;
import nc.noumea.mairie.sirh.eae.service.SirhWSConsumerException;

public class MockDevEnvSirhWsConsumer implements ISirhWsConsumer {

	@PersistenceContext(unitName = "eaePersistenceUnit")
	private EntityManager eaeEntityManager;
	
	@Override
	public List<Integer> getListOfSubAgentsForAgentId(int agentId)
			throws SirhWSConsumerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Integer> getListOfShdAgentsForAgentId(int agentId)
			throws SirhWSConsumerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CampagneEaeDto getCampagneEnCours() throws SirhWSConsumerException {
		// TODO Auto-generated method stub
		return null;
	}

}
