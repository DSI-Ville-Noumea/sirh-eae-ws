package nc.noumea.mairie.sirh.eae.web.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.service.IEaeService;
import nc.noumea.mairie.sirh.eae.web.controller.EaeController;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import flexjson.JSONDeserializer;

public class EaeControllerTest {

	@Test
	public void testNoEaeForIdAgent_ReturnNoContentHttpCode() {
		
		// Given
		EaeController controller = new EaeController();
		
		IEaeService eaeServiceMock = Mockito.mock(IEaeService.class);
		when(eaeServiceMock.listEaesByAgentId(0)).thenReturn(new ArrayList<Eae>());
		ReflectionTestUtils.setField(controller, "eaeService", eaeServiceMock);
		
		// When
		ResponseEntity<String> result = controller.listEaesByAgent(0);
		
		// Then
		assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
		assertFalse(result.hasBody());
	}
	
	@Test
	public void test1EaeForIdAgent_ReturnListWith1ItemAndHttpOK() {
		
		// Given
		List<Eae> resultOfService = new ArrayList<Eae>(Arrays.asList(new Eae()));
		EaeController controller = new EaeController();
		
		IEaeService eaeServiceMock = Mockito.mock(IEaeService.class);
		when(eaeServiceMock.listEaesByAgentId(1)).thenReturn(resultOfService);
		ReflectionTestUtils.setField(controller, "eaeService", eaeServiceMock);
		
		// When
		ResponseEntity<String> result = controller.listEaesByAgent(1);
		
		// Then
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertTrue(result.hasBody());
		
		JSONDeserializer<List<Eae>> deserializer = new JSONDeserializer<List<Eae>>();
		List<Eae> returnedResult = deserializer.deserialize(result.getBody().toString());
		assertEquals(1, returnedResult.size());
	}
}
