package nc.noumea.mairie.sirh.eae.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class EaeFinalizationDtoTest {

	@Test
	public void testDeserializeFromJson_EmptyJson_ReturnNewObject() {
		// Given
		String json = "{}";
		
		// When
		EaeFinalizationDto dto = new EaeFinalizationDto().deserializeFromJSON(json);
		
		// Then
		assertNotNull(dto);
	}
	
	@Test
	public void testDeserializeFromJson_FilledInJson_ReturnNewObject() {
		// Given
		String json = "{\"idDocument\": \"theSharepointId\", \"versionDocument\" : \"sharepointVersion\", \"commentaire\" : \"The most important comment.\" }";
		
		// When
		EaeFinalizationDto dto = new EaeFinalizationDto().deserializeFromJSON(json);
		
		// Then
		assertNotNull(dto);
		assertEquals("theSharepointId", dto.getIdDocument());
		assertEquals("sharepointVersion", dto.getVersionDocument());
		assertEquals("The most important comment.", dto.getCommentaire());
	}
}
