package nc.noumea.mairie.sirh.eae.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import nc.noumea.mairie.sirh.ws.SirhWSConsumer;
import nc.noumea.mairie.sirh.ws.SirhWSConsumerException;

import org.junit.Test;

import com.sun.jersey.api.client.ClientResponse;

public class SirhWSConsumerTest {

	@Test
	public void testReadResponse_WSReturn204() throws SirhWSConsumerException {

		// Given
		SirhWSConsumer ws = new SirhWSConsumer();
		ClientResponse cr = mock(ClientResponse.class);
		when(cr.getStatus()).thenReturn(204);
		
		// When
		List<Integer> result = ws.readResponse(cr, 0, "");

		// Then
		assertTrue(result.isEmpty());
	}

	@Test
	public void testGetListOfEaesForAgentId_ExceptionOccursOrWSReturn500Error() {

		// Given
		SirhWSConsumer ws = new SirhWSConsumer();
		ClientResponse cr = mock(ClientResponse.class);
		when(cr.getStatus()).thenReturn(500);

		try {
			// When
			ws.readResponse(cr, 1789, "http://www.test.com/");
		} catch (SirhWSConsumerException ex) {
			// Then
			assertEquals("An error occured when querying 'http://www.test.com/' with agentId '1789'. Return code is : 500", ex.getMessage());
		}
	}
	
	@Test
	public void testGetListOfEaesForAgentId_ReturnAListof2EaeIds() throws SirhWSConsumerException {

		// Given
		SirhWSConsumer ws = new SirhWSConsumer();
		ClientResponse cr = mock(ClientResponse.class);
		when(cr.getStatus()).thenReturn(200);
		when(cr.getEntity(String.class)).thenReturn("[1,2]");
		
		// When
		List<Integer> result = ws.readResponse(cr, 1789, null);
		
		// Then
		assertEquals(2, result.size());
		assertEquals(new Integer(1), result.get(0));
		assertEquals(new Integer(2), result.get(1));
	}
}
