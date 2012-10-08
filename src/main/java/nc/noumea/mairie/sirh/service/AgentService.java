package nc.noumea.mairie.sirh.service;

import nc.noumea.mairie.sirh.domain.Agent;
import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluateur;

import org.springframework.stereotype.Service;

@Service
public class AgentService implements IAgentService{

	@Override
	public Eae fillEaeWithAgents(Eae eaeToFill) {

		eaeToFill.setAgentEvalue(Agent.findAgent(eaeToFill.getIdAgent()));
		
		if (eaeToFill.getIdAgentShd() != null)
			eaeToFill.setAgentShd(Agent.findAgent(eaeToFill.getIdAgentShd()));
		
		if (eaeToFill.getIdAgentDelegataire() != null)
			eaeToFill.setAgentDelegataire(Agent.findAgent(eaeToFill.getIdAgentDelegataire()));
		
		if (eaeToFill.getEaeEvaluateurs() != null)
			for (EaeEvaluateur eval : eaeToFill.getEaeEvaluateurs())
				fillEaeEvaluateurWithAgent(eval);
		
		return eaeToFill;
		
	}

	@Override
	public EaeEvaluateur fillEaeEvaluateurWithAgent(EaeEvaluateur eaeEvaluateurToFill) {
		
		Agent agent = Agent.findAgent(eaeEvaluateurToFill.getIdAgent());
		eaeEvaluateurToFill.setAgent(agent);
		
		return eaeEvaluateurToFill;
	}

}
