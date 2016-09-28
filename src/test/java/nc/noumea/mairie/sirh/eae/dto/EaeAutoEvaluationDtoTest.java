package nc.noumea.mairie.sirh.eae.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeAutoEvaluation;

import org.junit.Test;

public class EaeAutoEvaluationDtoTest {

	@Test
	public void testConstructor() {
		// Given
		Eae eae = new Eae();
		eae.setIdEae(18);
		EaeAutoEvaluation autoEval = new EaeAutoEvaluation();
		autoEval.setEae(eae);
		autoEval.setParticularites("particularités");
		autoEval.setAcquis("acquis");
		autoEval.setSuccesDifficultes(("succès & difficultés"));
		eae.setEaeAutoEvaluation(autoEval);
		
		// When
		EaeAutoEvaluationDto dto = new EaeAutoEvaluationDto(eae);
		
		// Then
		assertEquals(18, dto.getIdEae());
		assertEquals("particularités", dto.getParticularites());
		assertEquals("acquis", dto.getAcquis());
		assertEquals("succès & difficultés", dto.getSuccesDifficultes());
	}
	
	@Test
	public void testConstructor_AutoEvalIsNull() {
		// Given
		Eae eae = new Eae();
		eae.setIdEae(18);
		
		// When
		EaeAutoEvaluationDto dto = new EaeAutoEvaluationDto(eae);
		
		// Then
		assertEquals(18, dto.getIdEae());
		assertNull(dto.getParticularites());
		assertNull( dto.getAcquis());
		assertNull(dto.getSuccesDifficultes());
	}
}
