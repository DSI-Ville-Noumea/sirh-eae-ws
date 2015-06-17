package nc.noumea.mairie.sirh.eae.service.dataConsistency;

import java.util.ArrayList;
import java.util.List;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeDeveloppement;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluation;
import nc.noumea.mairie.sirh.eae.domain.EaeEvolution;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeAvancementEnum;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeTypeDeveloppementEnum;

import org.springframework.stereotype.Service;

@Service
public class EaeDataConsistencyService implements IEaeDataConsistencyService {

	@Override
	public void checkDataConsistencyForEaeEvaluation(Eae eae) throws EaeDataConsistencyServiceException {

		EaeEvaluation evaluation = eae.getEaeEvaluation();

		if (evaluation.getAvisRevalorisation() == null && evaluation.getAvisChangementClasse() == null
				&& evaluation.getPropositionAvancement() == null)
			throw new EaeDataConsistencyServiceException(
					"L'une des trois options suivantes doit être renseignée: 'Avis de revalorisation/reclassification (contractuels)', 'Avis de changement de classe' ou 'Avancement différencié'.");

		if (evaluation.getPropositionAvancement() != null
				&& evaluation.getPropositionAvancement() != EaeAvancementEnum.MOY
				&& (evaluation.getCommentaireAvctEvaluateur() == null
						|| evaluation.getCommentaireAvctEvaluateur().getText() == null || evaluation
						.getCommentaireAvctEvaluateur().getText() == ""))
			throw new EaeDataConsistencyServiceException(
					"Le commentaire de l'évaluateur sur la proposition d'avancement est obligatoire dans le cas ou celui-ci est différent de 'Durée Moyenne'.");

	}

	@Override
	public void checkDataConsistencyForEaeEvolution(Eae eae) throws EaeDataConsistencyServiceException {

		EaeEvolution evolution = eae.getEaeEvolution();

		if (evolution.isTempsPartiel() && evolution.getTempsPartielIdSpbhor() == null)
			throw new EaeDataConsistencyServiceException(
					"Le taux horaire ne peut pas être égal à 0 en cas de temps partiel.");

		int[] verifier = new int[evolution.getEaeDeveloppements().size()];

		try {
			// #16157 : on eneleve la priorisation et l'echeance pour les
			// formateurs
			List<EaeDeveloppement> listeSansFormation = new ArrayList<EaeDeveloppement>();
			for (EaeDeveloppement developpement : evolution.getEaeDeveloppements()) {
				if (!developpement.getTypeDeveloppement().equals(EaeTypeDeveloppementEnum.FORMATEUR)) {
					listeSansFormation.add(developpement);
				}
			}
			for (EaeDeveloppement developpement : listeSansFormation) {
				if (++verifier[developpement.getPriorisation() - 1] > 1)
					throw new EaeDataConsistencyServiceException("La priorisation des développements n'est pas valide.");
			}
		} catch (IndexOutOfBoundsException ex) {
			throw new EaeDataConsistencyServiceException("La priorisation des développements n'est pas valide.");
		}
	}

	@Override
	public void checkDataConsistencyForEaeIdentification(Eae eae) throws EaeDataConsistencyServiceException {

		if (eae.getDateEntretien() == null)
			throw new EaeDataConsistencyServiceException("La date d'entretien est obligatoire.");

	}

}
