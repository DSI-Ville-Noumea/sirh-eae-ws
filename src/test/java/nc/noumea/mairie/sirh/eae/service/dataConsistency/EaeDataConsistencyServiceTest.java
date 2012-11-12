package nc.noumea.mairie.sirh.eae.service.dataConsistency;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeCommentaire;
import nc.noumea.mairie.sirh.eae.domain.EaeDeveloppement;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluation;
import nc.noumea.mairie.sirh.eae.domain.EaeEvolution;
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
	
	@Test
	public void testCheckDataConsistencyForEaeEvaluationEverythingEmpty_throwException() {
		
		// Given
		Eae eae = new Eae();
		EaeEvaluation eval = new EaeEvaluation();
		eae.setEaeEvaluation(eval);
		
		EaeDataConsistencyService service = new EaeDataConsistencyService();
		
		try {
			// When
			service.checkDataConsistencyForEaeEvaluation(eae);
		}
		catch (EaeDataConsistencyServiceException ex) {
			assertEquals("L'une des trois options suivantes doit être renseignée: 'Avis de revalorisation/reclassification (contractuels)', 'Avis de changement de classe' ou 'Avancement différencié'.", ex.getMessage());
			return;
		}
		
		fail("Shoud have thrown an exception");
	}
	
	@Test
	public void testCheckDataConsistencyForEaeEvolution_validDeveloppementsPriorisation_doNothing() throws EaeDataConsistencyServiceException {
		
		// Given
		Eae eae = new Eae();
		EaeEvolution evolution = new EaeEvolution();
		EaeDeveloppement dev1 = new EaeDeveloppement();
		dev1.setPriorisation(1);
		evolution.getEaeDeveloppements().add(dev1);
		
		EaeDeveloppement dev2 = new EaeDeveloppement();
		dev2.setPriorisation(2);
		evolution.getEaeDeveloppements().add(dev2);

		eae.setEaeEvolution(evolution);
		
		EaeDataConsistencyService service = new EaeDataConsistencyService();
		
		// When
		service.checkDataConsistencyForEaeEvolution(eae);
		
		// Then
		// nothing happens
	}
	
	@Test
	public void testCheckDataConsistencyForEaeEvolution_validDeveloppementsPriorisationStartAt0_throwException() throws EaeDataConsistencyServiceException {
		
		// Given
		Eae eae = new Eae();
		EaeEvolution evolution = new EaeEvolution();
		EaeDeveloppement dev1 = new EaeDeveloppement();
		dev1.setPriorisation(1);
		evolution.getEaeDeveloppements().add(dev1);
		
		EaeDeveloppement dev2 = new EaeDeveloppement();
		dev2.setPriorisation(0);
		evolution.getEaeDeveloppements().add(dev2);

		eae.setEaeEvolution(evolution);
		
		EaeDataConsistencyService service = new EaeDataConsistencyService();
		
		try {
			// When
			service.checkDataConsistencyForEaeEvolution(eae);
		} catch (EaeDataConsistencyServiceException e) {
			// Then
			assertEquals("La priorisation des développements n'est pas valide.", e.getMessage());
			return;
		}
		
		fail("Should have thrown exception");
	}
	
	@Test
	public void testCheckDataConsistencyForEaeEvolution_invalidDeveloppementsPriorisationSameNumberTwice_throwException() {
		
		// Given
		Eae eae = new Eae();
		EaeEvolution evolution = new EaeEvolution();
		EaeDeveloppement dev1 = new EaeDeveloppement();
		dev1.setPriorisation(1);
		evolution.getEaeDeveloppements().add(dev1);
		
		EaeDeveloppement dev2 = new EaeDeveloppement();
		dev2.setPriorisation(1);
		evolution.getEaeDeveloppements().add(dev2);
		
		eae.setEaeEvolution(evolution);
		
		EaeDataConsistencyService service = new EaeDataConsistencyService();
		
		
		try {
			// When
			service.checkDataConsistencyForEaeEvolution(eae);
		} catch (EaeDataConsistencyServiceException e) {
			// Then
			assertEquals("La priorisation des développements n'est pas valide.", e.getMessage());
			return;
		}
		
		fail("Should have thrown exception");
	}
	
	@Test
	public void testCheckDataConsistencyForEaeEvolution_invalidDeveloppementsPriorisationMissingNumber_throwException() {
		
		// Given
		Eae eae = new Eae();
		EaeEvolution evolution = new EaeEvolution();
		EaeDeveloppement dev1 = new EaeDeveloppement();
		dev1.setPriorisation(3);
		evolution.getEaeDeveloppements().add(dev1);
		
		EaeDeveloppement dev2 = new EaeDeveloppement();
		dev2.setPriorisation(1);
		evolution.getEaeDeveloppements().add(dev2);

		eae.setEaeEvolution(evolution);
		
		EaeDataConsistencyService service = new EaeDataConsistencyService();
		
		
		try {
			// When
			service.checkDataConsistencyForEaeEvolution(eae);
		} catch (EaeDataConsistencyServiceException e) {
			// Then
			assertEquals("La priorisation des développements n'est pas valide.", e.getMessage());
			return;
		}
		
		fail("Should have thrown exception");
	}
}
