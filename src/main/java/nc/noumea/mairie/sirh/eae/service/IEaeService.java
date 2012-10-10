package nc.noumea.mairie.sirh.eae.service;

import java.util.List;

import nc.noumea.mairie.sirh.eae.domain.Eae;

public interface IEaeService {

	List<Eae> listEaesByAgentId(int agentId);
	void initializeEae(Eae eaeToInitialize, List<Eae> previousEaes) throws EaeServiceException;
	void startEae(Eae eaeToStart) throws EaeServiceException;
	Eae findLastEaeByAgentId(int agentId);
	List<Eae> findFourPreviousEaesByAgentId(int agentId);
}
