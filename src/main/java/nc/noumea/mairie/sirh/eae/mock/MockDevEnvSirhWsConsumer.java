package nc.noumea.mairie.sirh.eae.mock;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import nc.noumea.mairie.sirh.domain.Agent;
import nc.noumea.mairie.sirh.eae.dto.AvancementEaeDto;
import nc.noumea.mairie.sirh.eae.dto.CalculEaeInfosDto;
import nc.noumea.mairie.sirh.eae.dto.agent.AutreAdministrationAgentDto;
import nc.noumea.mairie.sirh.eae.dto.agent.DateAvctDto;
import nc.noumea.mairie.sirh.eae.dto.poste.SpbhorDto;
import nc.noumea.mairie.sirh.ws.ISirhWsConsumer;
import nc.noumea.mairie.sirh.ws.SirhWSConsumerException;

public class MockDevEnvSirhWsConsumer implements ISirhWsConsumer {

	@PersistenceContext(unitName = "eaePersistenceUnit")
	private EntityManager eaeEntityManager;

	@Override
	public List<Integer> getListOfSubAgentsForAgentId(int agentId) throws SirhWSConsumerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Integer> getListOfShdAgentsForAgentId(int agentId) throws SirhWSConsumerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AvancementEaeDto getAvancement(Integer idAgent, Integer anneeAvancement, boolean isFonctionnaire)
			throws SirhWSConsumerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AvancementEaeDto getAvancementDetache(Integer idAgent, Integer anneeAvancement)
			throws SirhWSConsumerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CalculEaeInfosDto getDetailAffectationActiveByAgent(Integer idAgent, Integer anneeFormation)
			throws SirhWSConsumerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CalculEaeInfosDto> getListeAffectationsAgentAvecService(Integer idAgent, String idService)
			throws SirhWSConsumerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CalculEaeInfosDto> getListeAffectationsAgentAvecFP(Integer idAgent, Integer idFichePoste)
			throws SirhWSConsumerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AutreAdministrationAgentDto chercherAutreAdministrationAgentAncienne(Integer idAgent, boolean isFonctionnaire)
			throws SirhWSConsumerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AutreAdministrationAgentDto> getListeAutreAdministrationAgent(Integer idAgent)
			throws SirhWSConsumerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SpbhorDto> getListSpbhor() throws SirhWSConsumerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SpbhorDto getSpbhorById(Integer idSpbhor)
			throws SirhWSConsumerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Agent getAgent(Integer idAgent) throws SirhWSConsumerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DateAvctDto getCalculDateAvct(Integer idAgent) throws SirhWSConsumerException {
		// TODO Auto-generated method stub
		return null;
	}

}
