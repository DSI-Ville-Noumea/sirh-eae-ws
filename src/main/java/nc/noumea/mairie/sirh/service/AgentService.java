package nc.noumea.mairie.sirh.service;

import nc.noumea.mairie.sirh.domain.Agent;
import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluateur;
import nc.noumea.mairie.sirh.eae.domain.EaeEvalue;
import nc.noumea.mairie.sirh.eae.domain.EaeFichePoste;

import org.springframework.stereotype.Service;

@Service
public class AgentService implements IAgentService {

	@Override
	public Eae fillEaeWithAgents(Eae eaeToFill) {

		if (eaeToFill.getEaeEvalue() != null)
			fillEaeEvalueWithAgent(eaeToFill.getEaeEvalue());
		
		if (eaeToFill.getEaeFichePostes() != null)
			for (EaeFichePoste fdp : eaeToFill.getEaeFichePostes())
				fillEaeFichePosteWithAgent(fdp);
		
		if (eaeToFill.getIdAgentDelegataire() != null)
			eaeToFill.setAgentDelegataire(getAgent(eaeToFill.getIdAgentDelegataire()));
		
		if (eaeToFill.getEaeEvaluateurs() != null)
			for (EaeEvaluateur eval : eaeToFill.getEaeEvaluateurs())
				fillEaeEvaluateurWithAgent(eval);
		
		return eaeToFill;
	}
	
	@Override
	public EaeEvalue fillEaeEvalueWithAgent(EaeEvalue eaeEvalueToFill) {

		Agent agent = getAgent(eaeEvalueToFill.getIdAgent());
		eaeEvalueToFill.setAgent(agent);
		
		return eaeEvalueToFill;
	}

	@Override
	public EaeEvaluateur fillEaeEvaluateurWithAgent(EaeEvaluateur eaeEvaluateurToFill) {
		
		Agent agent = getAgent(eaeEvaluateurToFill.getIdAgent());
		eaeEvaluateurToFill.setAgent(agent);
		
		return eaeEvaluateurToFill;
	}
	
	@Override
	public EaeFichePoste fillEaeFichePosteWithAgent(EaeFichePoste eaeFichePosteToFill) {
		
		Agent agent = getAgent(eaeFichePosteToFill.getIdAgentShd());
		eaeFichePosteToFill.setAgentShd(agent);
		
		return eaeFichePosteToFill;
	}

	@Override
	public Agent getAgent(Integer idAgent) {
		return Agent.findAgent(idAgent);
	}
}
