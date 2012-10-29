package nc.noumea.mairie.sirh.eae.service.dataConsistency;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluation;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeAvancementEnum;

public class EaeDataConsistencyService implements IEaeDataConsistencyService {

	@Override
	public void checkDataConsistencyForEaeEvaluation(Eae eae) throws EaeDataConsistencyServiceException {
		
		EaeEvaluation evaluation = eae.getEaeEvaluation();
		
		if (evaluation.getPropositionAvancement() != null && evaluation.getPropositionAvancement() != EaeAvancementEnum.MOY
				&& (evaluation.getCommentaireAvctEvaluateur() == null || evaluation.getCommentaireAvctEvaluateur().getText() == null || evaluation.getCommentaireAvctEvaluateur().getText() == ""))
			throw new EaeDataConsistencyServiceException("Le commentaire de l'évaluateur sur la proposition d'avancement est obligatoire dans le cas ou celui-ci est différent de MOYEN.");

	}

}
