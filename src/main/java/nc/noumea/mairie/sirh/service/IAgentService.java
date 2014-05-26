package nc.noumea.mairie.sirh.service;

import nc.noumea.mairie.sirh.domain.Agent;
import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluateur;
import nc.noumea.mairie.sirh.eae.domain.EaeEvalue;
import nc.noumea.mairie.sirh.eae.domain.EaeFichePoste;

public interface IAgentService {

	Eae fillEaeWithAgents(Eae eaeToFill);

	EaeEvalue fillEaeEvalueWithAgent(EaeEvalue eaeEvalueToFill);

	EaeEvaluateur fillEaeEvaluateurWithAgent(EaeEvaluateur eaeEvaluateurToFill);

	EaeFichePoste fillEaeFichePosteWithAgent(EaeFichePoste eaeFichePosteToFill);

	Agent getAgent(Integer idAgent);
}
