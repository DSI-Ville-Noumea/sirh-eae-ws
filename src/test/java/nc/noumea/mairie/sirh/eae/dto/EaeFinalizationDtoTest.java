package nc.noumea.mairie.sirh.eae.dto;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;

import nc.noumea.mairie.sirh.eae.domain.EaeFinalisation;

public class EaeFinalizationDtoTest {

	@Test
	public void testConstructor() {

		Date dateFinalisation = new Date();

		EaeFinalisation finalisation = new EaeFinalisation();
		finalisation.setNodeRefAlfresco("1");
		finalisation.setCommentaire("commentaire");
		finalisation.setDateFinalisation(dateFinalisation);

		EaeFinalizationDto dto = new EaeFinalizationDto(finalisation);

		assertEquals(dto.getCommentaire(), finalisation.getCommentaire());
		assertEquals(dto.getDateFinalisation(), dateFinalisation);
		assertEquals(dto.getVersionDocument(), "");
		assertEquals(dto.getIdDocument(), finalisation.getNodeRefAlfresco());
	}
}
