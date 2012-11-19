package nc.noumea.mairie.sirh.eae.service.dataConsistency;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeDeveloppement;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluation;
import nc.noumea.mairie.sirh.eae.domain.EaeEvolution;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeAvancementEnum;

import org.springframework.stereotype.Service;

@Service
public class EaeDataConsistencyService implements IEaeDataConsistencyService {

	@Override
	public void checkDataConsistencyForEaeEvaluation(Eae eae) throws EaeDataConsistencyServiceException {
		
		EaeEvaluation evaluation = eae.getEaeEvaluation();
		
		if (evaluation.getAvisRevalorisation() == null && evaluation.getAvisChangementClasse() == null && evaluation.getPropositionAvancement() == null)
			throw new EaeDataConsistencyServiceException("L'une des trois options suivantes doit être renseignée: 'Avis de revalorisation/reclassification (contractuels)', 'Avis de changement de classe' ou 'Avancement différencié'.");
		
		if (evaluation.getPropositionAvancement() != null && evaluation.getPropositionAvancement() != EaeAvancementEnum.MOY
				&& (evaluation.getCommentaireAvctEvaluateur() == null || evaluation.getCommentaireAvctEvaluateur().getText() == null || evaluation.getCommentaireAvctEvaluateur().getText() == ""))
			throw new EaeDataConsistencyServiceException("Le commentaire de l'évaluateur sur la proposition d'avancement est obligatoire dans le cas ou celui-ci est différent de 'Durée Moyenne'.");

	}

	@Override
	public void checkDataConsistencyForEaeEvolution(Eae eae) throws EaeDataConsistencyServiceException {

		EaeEvolution evolution = eae.getEaeEvolution();
		
		int[] verifier = new int[evolution.getEaeDeveloppements().size()];
		
		try {
			for (EaeDeveloppement developpement : evolution.getEaeDeveloppements()) {
				if (++verifier[developpement.getPriorisation() - 1] > 1)
					throw new EaeDataConsistencyServiceException("La priorisation des développements n'est pas valide.");
			}
		} catch(IndexOutOfBoundsException ex) {
			throw new EaeDataConsistencyServiceException("La priorisation des développements n'est pas valide.");
		}
	}

	@Override
	public void checkDataConsistencyForEaeIdentification(Eae eae) throws EaeDataConsistencyServiceException {
	
		if (eae.getDateEntretien() == null)
			throw new EaeDataConsistencyServiceException("La date d'entretien est obligatoire.");
		
	}

}
