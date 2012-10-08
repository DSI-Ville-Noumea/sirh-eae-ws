package nc.noumea.mairie.sirh.service;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluateur;

public interface IAgentService {
	Eae fillEaeWithAgents(Eae eaeToFill);
	EaeEvaluateur fillEaeEvaluateurWithAgent(EaeEvaluateur eaeEvaluateurToFill);
}
