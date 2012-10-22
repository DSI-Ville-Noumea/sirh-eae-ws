package nc.noumea.mairie.sirh.eae.domain;

import static org.junit.Assert.assertEquals;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeTypeCompetenceEnum;

import org.junit.Test;

public class EaeFdpCompetenceTest {

	@Test
	public void testGetFullLabel_TypeSA() {
		// Given
		EaeFdpCompetence c = new EaeFdpCompetence();
		c.setType(EaeTypeCompetenceEnum.SA);
		c.setLibelle("un libellé");
		
		// Then
		assertEquals("Savoir - un libellé", c.getFullLabel());
	}
	
	@Test
	public void testGetFullLabel_TypeSF() {
		// Given
		EaeFdpCompetence c = new EaeFdpCompetence();
		c.setType(EaeTypeCompetenceEnum.SF);
		c.setLibelle("un libellé");
		
		// Then
		assertEquals("Savoir faire - un libellé", c.getFullLabel());
	}
	
	@Test
	public void testGetFullLabel_TypeCP() {
		// Given
		EaeFdpCompetence c = new EaeFdpCompetence();
		c.setType(EaeTypeCompetenceEnum.CP);
		c.setLibelle("un libellé");
		
		// Then
		assertEquals("Compétence professionnelle - un libellé", c.getFullLabel());
	}
}
