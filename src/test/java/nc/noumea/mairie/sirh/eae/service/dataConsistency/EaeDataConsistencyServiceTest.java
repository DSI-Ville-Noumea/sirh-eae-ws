package nc.noumea.mairie.sirh.eae.service.dataConsistency;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeCommentaire;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluation;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeAvancementEnum;

import org.junit.Test;

public class EaeDataConsistencyServiceTest {

	@Test
	public void testCheckDataConsistencyForEaeEvaluationNoCommentForPropositionDifferentFroMOYEN() {
		
		// Given
		Eae eae = new Eae();
		EaeEvaluation eval = new EaeEvaluation();
		eval.setPropositionAvancement(EaeAvancementEnum.MAXI);
		eae.setEaeEvaluation(eval);
		
		EaeDataConsistencyService service = new EaeDataConsistencyService();
		
		try {
			// When
			service.checkDataConsistencyForEaeEvaluation(eae);
		}
		catch (EaeDataConsistencyServiceException ex) {
			assertEquals("Le commentaire de l'évaluateur sur la proposition d'avancement est obligatoire dans le cas ou celui-ci est différent de MOYEN.", ex.getMessage());
			return;
		}
		
		fail("Shoud have thrown an exception");
	}
	
	@Test
	public void testCheckDataConsistencyForEaeEvaluationNullCommentForPropositionDifferentFroMOYEN() {
		
		// Given
		Eae eae = new Eae();
		EaeEvaluation eval = new EaeEvaluation();
		eval.setPropositionAvancement(EaeAvancementEnum.MAXI);
		eval.setCommentaireAvctEvaluateur(new EaeCommentaire());
		eae.setEaeEvaluation(eval);
		
		EaeDataConsistencyService service = new EaeDataConsistencyService();
		
		try {
			// When
			service.checkDataConsistencyForEaeEvaluation(eae);
		}
		catch (EaeDataConsistencyServiceException ex) {
			assertEquals("Le commentaire de l'évaluateur sur la proposition d'avancement est obligatoire dans le cas ou celui-ci est différent de MOYEN.", ex.getMessage());
			return;
		}
		
		fail("Shoud have thrown an exception");
	}
	
	@Test
	public void testCheckDataConsistencyForEaeEvaluationEmptyCommentForPropositionDifferentFroMOYEN() {
		
		// Given
		Eae eae = new Eae();
		EaeEvaluation eval = new EaeEvaluation();
		eval.setPropositionAvancement(EaeAvancementEnum.MAXI);
		eval.setCommentaireAvctEvaluateur(new EaeCommentaire());
		eval.getCommentaireAvctEvaluateur().setText("");
		eae.setEaeEvaluation(eval);
		
		EaeDataConsistencyService service = new EaeDataConsistencyService();
		
		try {
			// When
			service.checkDataConsistencyForEaeEvaluation(eae);
		}
		catch (EaeDataConsistencyServiceException ex) {
			assertEquals("Le commentaire de l'évaluateur sur la proposition d'avancement est obligatoire dans le cas ou celui-ci est différent de MOYEN.", ex.getMessage());
			return;
		}
		
		fail("Shoud have thrown an exception");
	}
}
