package nc.noumea.mairie.sirh.eae.dto.identification;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ValeurListeDtoTest {

	@Test
	public void testConstructor() {
		ValeurListeDto dto = new ValeurListeDto("pCode", "pValeur");
		
		assertEquals(dto.getCode(), "pCode");
		assertEquals(dto.getValeur(), "pValeur");
	}
}
