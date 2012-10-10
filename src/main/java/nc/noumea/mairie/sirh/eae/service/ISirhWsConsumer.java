package nc.noumea.mairie.sirh.eae.service;

import java.util.List;

public interface ISirhWsConsumer {

	public List<Integer> getListOfEaesForAgentId(int agentId);
}
