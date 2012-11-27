package nc.noumea.mairie.sirh.eae.dto;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CanFinalizeEaeDtoTest {

	@Test
	public void testSerializeInJson_emptyObject() {
		// Given
		CanFinalizeEaeDto dto = new CanFinalizeEaeDto();
		String expectedJson = "{\"canFinalize\":false,\"message\":null}";
		
		// When
		String result = dto.serializeInJSON();
		
		// Then
		assertEquals(expectedJson, result);
	}
	
	@Test
	public void testSerializeInJson_filleInObject() {
		// Given
		CanFinalizeEaeDto dto = new CanFinalizeEaeDto();
		dto.setCanFinalize(true);
		dto.setMessage("This EAE can be finalized, too bad!!");
		
		String expectedJson = "{\"canFinalize\":true,\"message\":\"This EAE can be finalized, too bad!!\"}";
		
		// When
		String result = dto.serializeInJSON();
		
		// Then
		assertEquals(expectedJson, result);
	}
}
