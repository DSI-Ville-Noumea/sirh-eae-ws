package nc.noumea.mairie.sirh.eae.dto;

import static org.junit.Assert.*;

import java.util.Date;

import nc.noumea.mairie.sirh.eae.domain.EaeDeveloppement;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeTypeDeveloppementEnum;

import org.junit.Test;

public class EaeDeveloppementDtoTest {

	@Test
	public void testConstructor() {
		
		EaeDeveloppement eaeDev = new EaeDeveloppement();
			eaeDev.setIdEaeDeveloppement(1);
			eaeDev.setEcheance(new Date());
			eaeDev.setLibelle("libelle");
			eaeDev.setPriorisation(0);
			eaeDev.setTypeDeveloppement(EaeTypeDeveloppementEnum.COMPETENCE);
		
		EaeDeveloppementDto result = new EaeDeveloppementDto(eaeDev);
		
		assertEquals(eaeDev.getIdEaeDeveloppement(), result.getIdEaeDeveloppement());
		assertEquals(eaeDev.getEcheance(), result.getEcheance());
		assertEquals(eaeDev.getLibelle(), result.getLibelle());
		assertEquals(eaeDev.getPriorisation(), result.getPriorisation());
		assertEquals(eaeDev.getTypeDeveloppement().name(), result.getTypeDeveloppement());
	}
}
