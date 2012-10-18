package nc.noumea.mairie.sirh.eae.web.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.dto.EaeDashboardItemDto;
import nc.noumea.mairie.sirh.eae.dto.EaeListItemDto;
import nc.noumea.mairie.sirh.eae.service.AgentMatriculeConverterServiceException;
import nc.noumea.mairie.sirh.eae.service.EaeServiceException;
import nc.noumea.mairie.sirh.eae.service.IAgentMatriculeConverterService;
import nc.noumea.mairie.sirh.eae.service.IEaeService;
import nc.noumea.mairie.sirh.eae.service.SirhWSConsumerException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import flexjson.JSONDeserializer;

public class EaeControllerTest {

	private static IAgentMatriculeConverterService agentMatriculeMock;

	@BeforeClass
	public static void setUp() {
		agentMatriculeMock = mock(IAgentMatriculeConverterService.class);
		when(agentMatriculeMock.tryConvertFromADIdAgentToEAEIdAgent(1)).thenReturn(1);
		when(agentMatriculeMock.tryConvertFromADIdAgentToEAEIdAgent(12)).thenReturn(12);
		when(agentMatriculeMock.tryConvertFromADIdAgentToEAEIdAgent(13)).thenReturn(13);
		when(agentMatriculeMock.tryConvertFromADIdAgentToEAEIdAgent(14)).thenReturn(14);
	}
	
	@Test
	public void testNoEaeForIdAgent_ReturnNoContentHttpCode() throws AgentMatriculeConverterServiceException, SirhWSConsumerException {
		
		// Given
		EaeController controller = new EaeController();
		
		IEaeService eaeServiceMock = Mockito.mock(IEaeService.class);
		when(eaeServiceMock.listEaesByAgentId(0)).thenReturn(new ArrayList<EaeListItemDto>());
		
		ReflectionTestUtils.setField(controller, "agentMatriculeConverterService", agentMatriculeMock);
		ReflectionTestUtils.setField(controller, "eaeService", eaeServiceMock);
		
		// When
		ResponseEntity<String> result = controller.listEaesByAgent(0);
		
		// Then
		assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
		assertFalse(result.hasBody());
	}
	
	@Test
	public void testlistEaesByAgent_1EaeForIdAgent_ReturnListWith1ItemAndHttpOK() throws AgentMatriculeConverterServiceException, SirhWSConsumerException {
		
		// Given
		List<EaeListItemDto> resultOfService = new ArrayList<EaeListItemDto>(Arrays.asList(new EaeListItemDto()));
		EaeController controller = new EaeController();
		
		IEaeService eaeServiceMock = Mockito.mock(IEaeService.class);
		when(eaeServiceMock.listEaesByAgentId(1)).thenReturn(resultOfService);

		ReflectionTestUtils.setField(controller, "agentMatriculeConverterService", agentMatriculeMock);
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
	
	@Test
	public void testInitializeEae_initialisationOk_ReturnHttp200() throws EaeServiceException {
		
		// Given
		int agentId = 12;
		int agentEvalueId = 13;
		Eae lastEae = new Eae();
		List<Eae> eaeToCreateList = new ArrayList<Eae>(Arrays.asList(lastEae));
		
		IEaeService eaeServiceMock = Mockito.mock(IEaeService.class);
		when(eaeServiceMock.findCurrentAndPreviousEaesByAgentId(agentEvalueId)).thenReturn(eaeToCreateList);
		
		EaeController controller = new EaeController();
		ReflectionTestUtils.setField(controller, "agentMatriculeConverterService", agentMatriculeMock);
		ReflectionTestUtils.setField(controller, "eaeService", eaeServiceMock);
		
		// When
		ResponseEntity<String> result = controller.initializeEae(agentId, agentEvalueId);
		
		// Then
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertFalse(result.hasBody());
		
		verify(eaeServiceMock, times(1)).findCurrentAndPreviousEaesByAgentId(agentEvalueId);
		verify(eaeServiceMock, times(1)).initializeEae(lastEae, null);
	}
	
	@Test
	public void testInitializeEae_noEaeForAgent_ReturnHttp404() throws EaeServiceException {
		
		// Given
		int agentId = 12;
		int agentEvalueId = 13;
		List<Eae> eaeToCreateList = new ArrayList<Eae>();
		
		IEaeService eaeServiceMock = Mockito.mock(IEaeService.class);
		when(eaeServiceMock.findCurrentAndPreviousEaesByAgentId(agentEvalueId)).thenReturn(eaeToCreateList);
		
		EaeController controller = new EaeController();
		ReflectionTestUtils.setField(controller, "agentMatriculeConverterService", agentMatriculeMock);
		ReflectionTestUtils.setField(controller, "eaeService", eaeServiceMock);
		
		// When
		ResponseEntity<String> result = controller.initializeEae(agentId, agentEvalueId);
		
		// Then
		assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
		assertFalse(result.hasBody());
		
		verify(eaeServiceMock, times(1)).findCurrentAndPreviousEaesByAgentId(agentEvalueId);
	}
	
	@Test
	public void testInitializeEae_EaeForAgentIsAlreadyInitialized_ReturnHttp409() throws EaeServiceException {
		
		// Given
		int agentId = 12;
		int agentEvalueId = 13;
		Eae lastEae = new Eae();
		List<Eae> eaeToCreateList = new ArrayList<Eae>(Arrays.asList(lastEae));
		EaeServiceException ex = new EaeServiceException("message");
		
		IEaeService eaeServiceMock = Mockito.mock(IEaeService.class);
		when(eaeServiceMock.findCurrentAndPreviousEaesByAgentId(agentEvalueId)).thenReturn(eaeToCreateList);
		org.mockito.Mockito.doThrow(ex).when(eaeServiceMock).initializeEae(lastEae, null);
		
		EaeController controller = new EaeController();
		ReflectionTestUtils.setField(controller, "agentMatriculeConverterService", agentMatriculeMock);
		ReflectionTestUtils.setField(controller, "eaeService", eaeServiceMock);
		
		// When
		ResponseEntity<String> result = controller.initializeEae(agentId, agentEvalueId);

		// Then
		assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
		assertTrue(result.hasBody());
		assertEquals(ex.getMessage(), result.getBody());
	}

	@Test
	public void testResetEaeEvaluateur_EaeForAgentCannotBeReset_ReturnHttp409() throws EaeServiceException {
		
		// Given
		int agentId = 12;
		int agentEvalueId = 13;
		Eae lastEae = new Eae();
		EaeServiceException ex = new EaeServiceException("message");
		
		IEaeService eaeServiceMock = Mockito.mock(IEaeService.class);
		when(eaeServiceMock.findLastEaeByAgentId(agentEvalueId)).thenReturn(lastEae);
		org.mockito.Mockito.doThrow(ex).when(eaeServiceMock).resetEaeEvaluateur(lastEae);
		
		EaeController controller = new EaeController();
		ReflectionTestUtils.setField(controller, "agentMatriculeConverterService", agentMatriculeMock);
		ReflectionTestUtils.setField(controller, "eaeService", eaeServiceMock);
		
		// When
		ResponseEntity<String> result = controller.resetEaeEvaluateur(agentId, agentEvalueId);

		// Then
		assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
		assertTrue(result.hasBody());
		assertEquals(ex.getMessage(), result.getBody());
	}
	
	@Test
	public void testResetEaeEvaluateur_EaeForAgentCanBeReset_ReturnHttp200() throws EaeServiceException {
		
		// Given
		int agentId = 12;
		int agentEvalueId = 13;
		Eae lastEae = new Eae();
		
		IEaeService eaeServiceMock = Mockito.mock(IEaeService.class);
		when(eaeServiceMock.findLastEaeByAgentId(agentEvalueId)).thenReturn(lastEae);
		
		EaeController controller = new EaeController();
		ReflectionTestUtils.setField(controller, "agentMatriculeConverterService", agentMatriculeMock);
		ReflectionTestUtils.setField(controller, "eaeService", eaeServiceMock);
		
		// When
		ResponseEntity<String> result = controller.resetEaeEvaluateur(agentId, agentEvalueId);

		// Then
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertFalse(result.hasBody());
		
		verify(eaeServiceMock, times(1)).resetEaeEvaluateur(lastEae);
	}
	
	@Test
	public void testResetEaeEvaluateur_EaeForAgentCantBeFound_ReturnHttp404() throws EaeServiceException {
		
		// Given
		int agentId = 12;
		int agentEvalueId = 13;
		Eae lastEae = null;
		
		IEaeService eaeServiceMock = Mockito.mock(IEaeService.class);
		when(eaeServiceMock.findLastEaeByAgentId(agentEvalueId)).thenReturn(lastEae);
		
		EaeController controller = new EaeController();
		ReflectionTestUtils.setField(controller, "agentMatriculeConverterService", agentMatriculeMock);
		ReflectionTestUtils.setField(controller, "eaeService", eaeServiceMock);
		
		// When
		ResponseEntity<String> result = controller.resetEaeEvaluateur(agentId, agentEvalueId);

		// Then
		assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
		assertFalse(result.hasBody());
		
		verify(eaeServiceMock, times(0)).resetEaeEvaluateur(lastEae);
	}
	
	@Test
	public void testSetDelegataire_DelegataireCannotBeSet_ReturnHttp409() throws EaeServiceException {
		
		// Given
		int agentId = 12;
		int agentEvalueId = 13;
		int agentDelegataireId = 14;
		Eae lastEae = new Eae();
		EaeServiceException ex = new EaeServiceException("message");
		
		IEaeService eaeServiceMock = Mockito.mock(IEaeService.class);
		when(eaeServiceMock.findLastEaeByAgentId(agentEvalueId)).thenReturn(lastEae);
		org.mockito.Mockito.doThrow(ex).when(eaeServiceMock).setDelegataire(lastEae, agentDelegataireId);
		
		EaeController controller = new EaeController();
		ReflectionTestUtils.setField(controller, "agentMatriculeConverterService", agentMatriculeMock);
		ReflectionTestUtils.setField(controller, "eaeService", eaeServiceMock);
		
		// When
		ResponseEntity<String> result = controller.setDelegataire(agentId, agentEvalueId, agentDelegataireId);

		// Then
		assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
		assertTrue(result.hasBody());
		assertEquals(ex.getMessage(), result.getBody());
	}
	
	@Test
	public void testSetDelegataire_EaeAndDelegataireExist_ReturnHttp200() throws EaeServiceException {
		
		// Given
		int agentId = 12;
		int agentEvalueId = 13;
		int agentDelegataireId = 14;
		Eae lastEae = new Eae();
		
		IEaeService eaeServiceMock = Mockito.mock(IEaeService.class);
		when(eaeServiceMock.findLastEaeByAgentId(agentEvalueId)).thenReturn(lastEae);
		
		EaeController controller = new EaeController();
		ReflectionTestUtils.setField(controller, "agentMatriculeConverterService", agentMatriculeMock);
		ReflectionTestUtils.setField(controller, "eaeService", eaeServiceMock);
		
		// When
		ResponseEntity<String> result = controller.setDelegataire(agentId, agentEvalueId, agentDelegataireId);

		// Then
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertFalse(result.hasBody());
		
		verify(eaeServiceMock, times(1)).setDelegataire(lastEae, agentDelegataireId);
	}
	
	@Test
	public void testSetDelegataire_EaeForAgentCantBeFound_ReturnHttp404() throws EaeServiceException {
		
		// Given
		int agentId = 12;
		int agentEvalueId = 13;
		int agentDelegataireId = 14;
		Eae lastEae = null;
		
		IEaeService eaeServiceMock = Mockito.mock(IEaeService.class);
		when(eaeServiceMock.findLastEaeByAgentId(agentEvalueId)).thenReturn(lastEae);
		
		EaeController controller = new EaeController();
		ReflectionTestUtils.setField(controller, "agentMatriculeConverterService", agentMatriculeMock);
		ReflectionTestUtils.setField(controller, "eaeService", eaeServiceMock);
		
		// When
		ResponseEntity<String> result = controller.setDelegataire(agentId, agentEvalueId, agentDelegataireId);

		// Then
		assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
		assertFalse(result.hasBody());
		
		verify(eaeServiceMock, times(0)).setDelegataire(lastEae, agentDelegataireId);
	}
	
	@Test
	public void testgetEaesDashboard_NoEaes_ReturnNoContentHttpCode() throws AgentMatriculeConverterServiceException, SirhWSConsumerException {
		
		// Given
		EaeController controller = new EaeController();
		
		IEaeService eaeServiceMock = Mockito.mock(IEaeService.class);
		when(eaeServiceMock.getEaesDashboard(0)).thenReturn(new ArrayList<EaeDashboardItemDto>());
		
		ReflectionTestUtils.setField(controller, "agentMatriculeConverterService", agentMatriculeMock);
		ReflectionTestUtils.setField(controller, "eaeService", eaeServiceMock);
		
		// When
		ResponseEntity<String> result = controller.getEaesDashboard(0);
		
		// Then
		assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
		assertFalse(result.hasBody());
	}
	
	@Test
	public void testgetEaesDashboard_1EaeForIdAgent_ReturnListWith1ItemAndHttpOK() throws AgentMatriculeConverterServiceException, SirhWSConsumerException {
		
		// Given
		List<EaeDashboardItemDto> resultOfService = new ArrayList<EaeDashboardItemDto>(Arrays.asList(new EaeDashboardItemDto()));
		EaeController controller = new EaeController();
		
		IEaeService eaeServiceMock = Mockito.mock(IEaeService.class);
		when(eaeServiceMock.getEaesDashboard(1)).thenReturn(resultOfService);

		ReflectionTestUtils.setField(controller, "agentMatriculeConverterService", agentMatriculeMock);
		ReflectionTestUtils.setField(controller, "eaeService", eaeServiceMock);
		
		// When
		ResponseEntity<String> result = controller.getEaesDashboard(1);
		
		// Then
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertTrue(result.hasBody());
		
		JSONDeserializer<List<EaeDashboardItemDto>> deserializer = new JSONDeserializer<List<EaeDashboardItemDto>>();
		List<EaeDashboardItemDto> returnedResult = deserializer.deserialize(result.getBody().toString());
		assertEquals(1, returnedResult.size());
	}
}
