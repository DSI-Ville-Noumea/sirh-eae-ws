package nc.noumea.mairie.sirh.eae.dto.identification;

import static org.junit.Assert.assertEquals;
import nc.noumea.mairie.sirh.eae.domain.EaeEvalue;

import org.joda.time.DateTime;
import org.junit.Test;

public class EaeIdentificationStatutDtoTest {

	@Test
	public void testEaeIdentificationStatutDto() {
		// Given
		EaeEvalue eval = new EaeEvalue();
		eval.setCadre("cadre");
		eval.setGrade("grade");
		eval.setEchelon("echelon");
		eval.setCategorie("categorie");
		eval.setClassification("classification");
		eval.setAncienneteEchelonJours(18);
		eval.setNouvEchelon("nouvEchelon");
		eval.setNouvGrade("nouvGrade");
		eval.setDateEffetAvancement(new DateTime(2012, 12, 21, 0, 0, 0, 0).toDate());

		// When
		EaeIdentificationStatutDto dto = new EaeIdentificationStatutDto(eval);

		// Then
		assertEquals(eval.getCadre(), dto.getCadre());
		assertEquals(eval.getGrade(), dto.getGrade());
		assertEquals(eval.getEchelon(), dto.getEchelon());
		assertEquals(eval.getCategorie(), dto.getCategorie());
		assertEquals(eval.getClassification(), dto.getClassification());
		assertEquals(eval.getAncienneteEchelonJours(), dto.getAncienneteEchelonJours());
		assertEquals(eval.getNouvEchelon(), dto.getNouvEchelon());
		assertEquals(eval.getNouvGrade(), dto.getNouvGrade());
		assertEquals(eval.getDateEffetAvancement(), dto.getDateEffet());
	}

}
