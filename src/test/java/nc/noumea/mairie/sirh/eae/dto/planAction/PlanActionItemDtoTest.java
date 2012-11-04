package nc.noumea.mairie.sirh.eae.dto.planAction;

import static org.junit.Assert.assertEquals;
import nc.noumea.mairie.sirh.eae.domain.EaePlanAction;

import org.junit.Test;

public class PlanActionItemDtoTest {

	@Test
	public void testConstructor() {
		// Given
		EaePlanAction pa = new EaePlanAction();
		pa.setObjectif("objo");
		pa.setMesure("mesuuuure");
		
		// When
		PlanActionItemDto dto = new PlanActionItemDto(pa);
		
		// Then
		assertEquals(pa.getObjectif(), dto.getObjectif());
		assertEquals(pa.getMesure(), dto.getIndicateur());
	}
}
