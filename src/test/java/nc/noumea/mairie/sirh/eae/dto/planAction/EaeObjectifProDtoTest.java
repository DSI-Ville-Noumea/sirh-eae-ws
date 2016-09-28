package nc.noumea.mairie.sirh.eae.dto.planAction;

import static org.junit.Assert.assertEquals;
import nc.noumea.mairie.sirh.eae.domain.EaePlanAction;

import org.junit.Test;

public class EaeObjectifProDtoTest {

	@Test
	public void testConstructor() {
		// Given
		EaePlanAction pa = new EaePlanAction();
		pa.setIdEaePlanAction(1);
		pa.setObjectif("objo");
		pa.setMesure("mesuuuure");
		
		// When
		EaeObjectifProDto dto = new EaeObjectifProDto(pa);
		
		// Then
		assertEquals(pa.getObjectif(), dto.getObjectif());
		assertEquals(pa.getMesure(), dto.getIndicateur());
		assertEquals(pa.getIdEaePlanAction(), dto.getIdObjectifPro());
	}
}
