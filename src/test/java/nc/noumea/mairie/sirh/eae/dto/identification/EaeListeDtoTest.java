package nc.noumea.mairie.sirh.eae.dto.identification;

import static org.junit.Assert.assertEquals;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeNiveauEnum;

import org.junit.Test;

public class EaeListeDtoTest {

	@Test
	public void testConstructor() {
		
		EaeListeDto dto = new EaeListeDto(EaeNiveauEnum.SATISFAISANT, EaeNiveauEnum.class);
		
		assertEquals(dto.getCourant(), EaeNiveauEnum.SATISFAISANT.name());
		assertEquals(dto.getListe().size(), EaeNiveauEnum.class.getEnumConstants().length);
	}
}
