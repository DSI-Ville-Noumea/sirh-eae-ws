package nc.noumea.mairie.sirh.eae.service;

import java.util.List;

import nc.noumea.mairie.sirh.eae.dto.CampagneEaeDto;

public interface ISirhWsConsumer {

	public List<Integer> getListOfSubAgentsForAgentId(int agentId)
			throws SirhWSConsumerException;

	public List<Integer> getListOfShdAgentsForAgentId(int agentId)
			throws SirhWSConsumerException;

	public CampagneEaeDto getCampagneEnCours() throws SirhWSConsumerException;
}
