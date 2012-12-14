package nc.noumea.mairie.sirh.eae.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class EaeEvalueTest {

	@Test
	public void testGetAvctDureeDisplay_dureeNull_returnNR() {
		// Given
		EaeEvalue evalue = new EaeEvalue();
		
		// Then
		assertEquals("(NR)", evalue.getAvctDureeDisplay(null));
	}
	
	@Test
	public void testGetAvctDureeDisplay_duree0_returnNR() {
		// Given
		EaeEvalue evalue = new EaeEvalue();
		
		// Then
		assertEquals("(NR)", evalue.getAvctDureeDisplay(0));
	}
	
	@Test
	public void testGetAvctDureeDisplay_dureeValued_returnString() {
		// Given
		EaeEvalue evalue = new EaeEvalue();
		
		// Then
		assertEquals("(36 mois)", evalue.getAvctDureeDisplay(36));
	}
	
	@Test
	public void testGetAvctDureeMinDisplay_dureeValues_returnString() {
		// Given
		EaeEvalue evalue = new EaeEvalue();
		evalue.setAvctDureeMin(12);
		
		// Then
		assertEquals("Durée minimale (12 mois)", evalue.getAvctDureeMinDisplay());
	}
	
	@Test
	public void testGetAvctDureeMoyDisplay_dureeValues_returnString() {
		// Given
		EaeEvalue evalue = new EaeEvalue();
		evalue.setAvctDureeMoy(24);
		
		// Then
		assertEquals("Durée moyenne (24 mois)", evalue.getAvctDureeMoyDisplay());
	}
	
	@Test
	public void testGetAvctDureeMaxDisplay_dureeValues_returnString() {
		// Given
		EaeEvalue evalue = new EaeEvalue();
		evalue.setAvctDureeMax(36);
		
		// Then
		assertEquals("Durée maximale (36 mois)", evalue.getAvctDureeMaxDisplay());
	}
}
