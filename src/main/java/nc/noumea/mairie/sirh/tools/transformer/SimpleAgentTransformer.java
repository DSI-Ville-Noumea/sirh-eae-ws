package nc.noumea.mairie.sirh.tools.transformer;

import nc.noumea.mairie.sirh.domain.Agent;
import flexjson.transformer.AbstractTransformer;

public class SimpleAgentTransformer extends AbstractTransformer {

	@Override
	public void transform(Object arg0) {
		Agent agent = (Agent) arg0;

		getContext().writeOpenObject();
		
		getContext().writeName("idAgent");
	    getContext().transform(agent.getIdAgent());

	    getContext().writeComma();
	    getContext().writeName("nom");
	    getContext().transform(agent.getDisplayNom());

	    getContext().writeComma();
	    getContext().writeName("prenom");
	    getContext().transform(agent.getDisplayPrenom());
	    
	    getContext().writeCloseObject();
	    
	}

}
