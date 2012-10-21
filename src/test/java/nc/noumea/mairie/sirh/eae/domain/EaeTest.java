package nc.noumea.mairie.sirh.eae.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;


public class EaeTest {

	@Test
	public void testIsEvaluateur_idAgentNotInEvaluateurs_returnFalse() {
		// Given
		Eae eae = new Eae();
		
		// Then
		assertFalse(eae.isEvaluateur(89));
	}
	
	@Test
	public void testIsDelegataire_idAgentNotDelegataire_returnFalse() {
		// Given
		Eae eae = new Eae();
		
		// Then
		assertFalse(eae.isDelegataire(89));
	}
	
	@Test
	public void testIsEvaluateur_idAgentInEvaluateurs_returnTrue() {
		// Given
		Eae eae = new Eae();
		EaeEvaluateur eval = new EaeEvaluateur();
		eval.setIdAgent(5678);
		eae.getEaeEvaluateurs().add(eval);
		
		// Then
		assertTrue(eae.isEvaluateur(5678));
	}
	
	@Test
	public void testIsDelegataire_idAgentAsDelegataire_returnTrue() {
		// Given
		Eae eae = new Eae();
		EaeEvaluateur eval = new EaeEvaluateur();
		eval.setIdAgent(5699);
		eae.getEaeEvaluateurs().add(eval);
		
		eae.setIdAgentDelegataire(5678);
		
		// Then
		assertTrue(eae.isDelegataire(5678));
	}
	
	@Test
	public void testGetPrimaryFichePoste_2FDP_returnSecondOne() {
		//Given
		EaeFichePoste f1 = new EaeFichePoste();
		f1.setPrimary(false);
		EaeFichePoste f2 = new EaeFichePoste();
		f2.setPrimary(true);
		Eae eae = new Eae();
		eae.getEaeFichePostes().add(f1);
		eae.getEaeFichePostes().add(f2);
		
		// When
		EaeFichePoste result = eae.getPrimaryFichePoste();
		
		// Then
		assertEquals(f2, result);
	}
	
	@Test
	public void testGetPrimaryFichePoste_2FDPNoneIsPrimary_returnNull() {
		//Given
		EaeFichePoste f1 = new EaeFichePoste();
		f1.setPrimary(false);
		EaeFichePoste f2 = new EaeFichePoste();
		f2.setPrimary(false);
		Eae eae = new Eae();
		eae.getEaeFichePostes().add(f1);
		eae.getEaeFichePostes().add(f2);
		
		// When
		EaeFichePoste result = eae.getPrimaryFichePoste();
		
		// Then
		assertNull(result);
	}
}
