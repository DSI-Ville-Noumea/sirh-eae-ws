package nc.noumea.mairie.sirh.tools.transformer;

import nc.noumea.mairie.sirh.eae.domain.EaeEvaluateur;
import flexjson.transformer.AbstractTransformer;

public class EaeEvaluateurToAgentTransformer extends AbstractTransformer {

	@Override
	public void transform(Object object) {
		EaeEvaluateur evaluateur = (EaeEvaluateur) object;
		
		getContext().transform(evaluateur.getAgent());

	}

}
