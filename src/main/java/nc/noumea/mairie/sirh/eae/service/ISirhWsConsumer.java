package nc.noumea.mairie.sirh.eae.service;

import java.util.List;

public interface ISirhWsConsumer {

	public List<Integer> getListOfSubAgentsForAgentId(int agentId) throws SirhWSConsumerException;
}
