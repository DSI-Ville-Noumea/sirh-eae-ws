package nc.noumea.mairie.sirh.eae.domain;

import static org.junit.Assert.assertEquals;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeAgentStatutEnum;

import org.junit.Test;

public class EaeEvalueTest {

	@Test
	public void testGetAvctDureeDisplay_dureeNull_returnNR() {
		// Given
		EaeEvalue evalue = new EaeEvalue();
		evalue.setStatut(EaeAgentStatutEnum.F);
		
		// Then
		assertEquals("(NR)", evalue.getAvctDureeDisplay(null));
	}
	
	@Test
	public void testGetAvctDureeDisplay_duree0_returnNR() {
		// Given
		EaeEvalue evalue = new EaeEvalue();
		evalue.setStatut(EaeAgentStatutEnum.F);
		
		// Then
		assertEquals("(NR)", evalue.getAvctDureeDisplay(0));
	}
	
	@Test
	public void testGetAvctDureeDisplay_dureeValued_returnString() {
		// Given
		EaeEvalue evalue = new EaeEvalue();
		evalue.setStatut(EaeAgentStatutEnum.F);
		
		// Then
		assertEquals("(36 mois)", evalue.getAvctDureeDisplay(36));
	}
	
	@Test
	public void testGetAvctDureeDisplayEvalueIsNotStatutF_returnEmptyString() {
		// Given
		EaeEvalue evalue = new EaeEvalue();
		evalue.setStatut(EaeAgentStatutEnum.A);
		
		// Then
		assertEquals("", evalue.getAvctDureeDisplay(36));
	}
	
	@Test
	public void testGetAvctDureeMinDisplay_dureeValues_returnString() {
		// Given
		EaeEvalue evalue = new EaeEvalue();
		evalue.setAvctDureeMin(12);
		evalue.setStatut(EaeAgentStatutEnum.F);
		
		// Then
		assertEquals("Durée minimale (12 mois)", evalue.getAvctDureeMinDisplay());
	}
	
	@Test
	public void testGetAvctDureeMoyDisplay_dureeValues_returnString() {
		// Given
		EaeEvalue evalue = new EaeEvalue();
		evalue.setAvctDureeMoy(24);
		evalue.setStatut(EaeAgentStatutEnum.F);
		
		// Then
		assertEquals("Durée moyenne (24 mois)", evalue.getAvctDureeMoyDisplay());
	}
	
	@Test
	public void testGetAvctDureeMaxDisplay_dureeValues_returnString() {
		// Given
		EaeEvalue evalue = new EaeEvalue();
		evalue.setAvctDureeMax(36);
		evalue.setStatut(EaeAgentStatutEnum.F);
		
		// Then
		assertEquals("Durée maximale (36 mois)", evalue.getAvctDureeMaxDisplay());
	}
}
