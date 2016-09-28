package nc.noumea.mairie.sirh.eae.dto;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import nc.noumea.mairie.sirh.eae.domain.EaeEvolutionSouhait;

public class EaeEvolutionSouhaitDtoTest {

	@Test
	public void testConstructor() {
		
		EaeEvolutionSouhait evolSouhait = new EaeEvolutionSouhait();
			evolSouhait.setIdEaeEvolutionSouhait(1);
			evolSouhait.setSouhait("souhait");
			evolSouhait.setSuggestion("suggestion");
		
		EaeEvolutionSouhaitDto result = new EaeEvolutionSouhaitDto(evolSouhait);
		
		assertEquals(result.getIdEaeEvolutionSouhait(), evolSouhait.getIdEaeEvolutionSouhait());
		assertEquals(result.getSouhait(), evolSouhait.getSouhait());
		assertEquals(result.getSuggestion(), evolSouhait.getSuggestion());
	}
}
