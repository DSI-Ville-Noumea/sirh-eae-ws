package nc.noumea.mairie.sirh.eae.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AgentMatriculeConverterServiceTest {

	@Test
	public void testfromADIdAgentToEAEIdAgent_withIdNot5digits_throwException() {
		
		// Given
		int theIdToConvert = 89;
		AgentMatriculeConverterService service = new AgentMatriculeConverterService();
				
		try {
			// When
			service.fromADIdAgentToEAEIdAgent(theIdToConvert);
		}
		catch(AgentMatriculeConverterServiceException ex) {
			// Then
			assertEquals("Impossible de convertir le matricule '89' en matricule SIRH-EAE.", ex.getMessage());
		}
		
	}
	
	@Test
	public void testfromADIdAgentToEAEIdAgent_withIdIs5digits_convertItTo6Digits() throws AgentMatriculeConverterServiceException {
		
		// Given
		int theIdToConvert = 906898;
		AgentMatriculeConverterService service = new AgentMatriculeConverterService();
		
		// When
		int result = service.fromADIdAgentToEAEIdAgent(theIdToConvert);
				
		// Then
		assertEquals(9006898, result);
	}
}
