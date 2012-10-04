package nc.noumea.mairie.sirh.eae.service;

import java.util.List;

import nc.noumea.mairie.sirh.eae.domain.Eae;

public interface IEaeService {

	List<Eae> listEaesByAgentId(int agentId);
}
