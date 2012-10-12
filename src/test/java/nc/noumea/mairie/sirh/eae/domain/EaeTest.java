package nc.noumea.mairie.sirh.eae.domain;

import static org.junit.Assert.assertFalse;
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
}
