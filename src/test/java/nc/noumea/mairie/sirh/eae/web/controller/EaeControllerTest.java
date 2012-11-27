package nc.noumea.mairie.sirh.eae.web.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.dto.CanFinalizeEaeDto;
import nc.noumea.mairie.sirh.eae.dto.EaeDashboardItemDto;
import nc.noumea.mairie.sirh.eae.dto.EaeFinalizationDto;
import nc.noumea.mairie.sirh.eae.dto.EaeListItemDto;
import nc.noumea.mairie.sirh.eae.dto.FinalizationInformationDto;
import nc.noumea.mairie.sirh.eae.security.IEaeSecurityProvider;
import nc.noumea.mairie.sirh.eae.service.AgentMatriculeConverterServiceException;
import nc.noumea.mairie.sirh.eae.service.EaeServiceException;
import nc.noumea.mairie.sirh.eae.service.IAgentMatriculeConverterService;
import nc.noumea.mairie.sirh.eae.service.IEaeService;
import nc.noumea.mairie.sirh.eae.service.SirhWSConsumerException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import flexjson.JSONDeserializer;

public class EaeControllerTest {

	private static IAgentMatriculeConverterService agentMatriculeMock;

	IEaeSecurityProvider eaeSecurityProvider;
	MessageSource messageSourceMock;
	
	@Before
	public void SetUp() {
		eaeSecurityProvider = Mockito.mock(IEaeSecurityProvider.class);
		messageSourceMock = Mockito.mock(MessageSource.class);
	}
	
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
		lastEae.setIdEae(120);
		List<Eae> eaeToCreateList = new ArrayList<Eae>(Arrays.asList(lastEae));
		
		IEaeService eaeServiceMock = Mockito.mock(IEaeService.class);
		when(eaeServiceMock.findCurrentAndPreviousEaesByAgentId(agentEvalueId)).thenReturn(eaeToCreateList);
		
		EaeController controller = new EaeController();
		ReflectionTestUtils.setField(controller, "agentMatriculeConverterService", agentMatriculeMock);
		ReflectionTestUtils.setField(controller, "eaeService", eaeServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		ReflectionTestUtils.setField(controller, "messageSource", messageSourceMock);
		
		// When
		ResponseEntity<String> result = controller.initializeEae(agentId, agentEvalueId);
		
		// Then
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertFalse(result.hasBody());
		
		verify(eaeSecurityProvider, times(1)).checkEaeAndWriteRight(120, agentId);
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
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		
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
		lastEae.setIdEae(120);
		List<Eae> eaeToCreateList = new ArrayList<Eae>(Arrays.asList(lastEae));
		EaeServiceException ex = new EaeServiceException("message");
		
		IEaeService eaeServiceMock = Mockito.mock(IEaeService.class);
		when(eaeServiceMock.findCurrentAndPreviousEaesByAgentId(agentEvalueId)).thenReturn(eaeToCreateList);
		org.mockito.Mockito.doThrow(ex).when(eaeServiceMock).initializeEae(lastEae, null);
		
		EaeController controller = new EaeController();
		ReflectionTestUtils.setField(controller, "agentMatriculeConverterService", agentMatriculeMock);
		ReflectionTestUtils.setField(controller, "eaeService", eaeServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		
		// When
		ResponseEntity<String> result = controller.initializeEae(agentId, agentEvalueId);

		// Then
		assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
		assertTrue(result.hasBody());
		assertEquals(ex.getMessage(), result.getBody());
	}
	
	@Test
	public void testInitializeEae_AgentDoesNotHaveRight_ReturnHttp403() throws EaeServiceException {
		
		// Given
		int agentId = 12;
		int agentEvalueId = 13;
		Eae lastEae = new Eae();
		lastEae.setIdEae(120);
		List<Eae> eaeToCreateList = new ArrayList<Eae>(Arrays.asList(lastEae));
		
		IEaeService eaeServiceMock = Mockito.mock(IEaeService.class);
		when(eaeServiceMock.findCurrentAndPreviousEaesByAgentId(agentEvalueId)).thenReturn(eaeToCreateList);
		
		when(eaeSecurityProvider.checkEaeAndWriteRight(lastEae.getIdEae(), agentId)).thenReturn(new ResponseEntity<String>(HttpStatus.FORBIDDEN));
		
		EaeController controller = new EaeController();
		ReflectionTestUtils.setField(controller, "agentMatriculeConverterService", agentMatriculeMock);
		ReflectionTestUtils.setField(controller, "eaeService", eaeServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		
		// When
		ResponseEntity<String> result = controller.initializeEae(agentId, agentEvalueId);

		// Then
		assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
	}
	
	@Test
	public void testSetDelegataire_DelegataireCannotBeSet_ReturnHttp409() throws EaeServiceException {
		
		// Given
		int agentId = 12;
		int eaeId = 13;
		int agentDelegataireId = 14;
		Eae lastEae = new Eae();
		EaeServiceException ex = new EaeServiceException("message");
		
		IEaeService eaeServiceMock = Mockito.mock(IEaeService.class);
		when(eaeServiceMock.getEae(eaeId)).thenReturn(lastEae);
		org.mockito.Mockito.doThrow(ex).when(eaeServiceMock).setDelegataire(lastEae, agentDelegataireId);
		
		EaeController controller = new EaeController();
		ReflectionTestUtils.setField(controller, "agentMatriculeConverterService", agentMatriculeMock);
		ReflectionTestUtils.setField(controller, "eaeService", eaeServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		
		// When
		ResponseEntity<String> result = controller.setDelegataire(eaeId, agentId, agentDelegataireId);

		// Then
		assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
		assertTrue(result.hasBody());
		assertEquals(ex.getMessage(), result.getBody());
	}
	
	@Test
	public void testSetDelegataire_EaeAndDelegataireExist_ReturnHttp200() throws EaeServiceException {
		
		// Given
		int agentId = 12;
		int eaeId = 13;
		int agentDelegataireId = 14;
		Eae lastEae = new Eae();
		
		IEaeService eaeServiceMock = Mockito.mock(IEaeService.class);
		when(eaeServiceMock.getEae(eaeId)).thenReturn(lastEae);
		
		EaeController controller = new EaeController();
		ReflectionTestUtils.setField(controller, "agentMatriculeConverterService", agentMatriculeMock);
		ReflectionTestUtils.setField(controller, "eaeService", eaeServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		ReflectionTestUtils.setField(controller, "messageSource", messageSourceMock);
		
		// When
		ResponseEntity<String> result = controller.setDelegataire(eaeId, agentId, agentDelegataireId);

		// Then
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertFalse(result.hasBody());
		
		verify(eaeServiceMock, times(1)).setDelegataire(lastEae, agentDelegataireId);
	}
	
	@Test
	public void testSetDelegataire_AgentDoesNotHaveRight_ReturnHttp403() throws EaeServiceException {
		
		// Given
		int agentId = 12;
		int eaeId = 13;
		int agentDelegataireId = 14;
		
		when(eaeSecurityProvider.checkEaeAndWriteRight(eaeId, agentId)).thenReturn(new ResponseEntity<String>(HttpStatus.FORBIDDEN));
	
		EaeController controller = new EaeController();
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		
		// When
		ResponseEntity<String> result = controller.setDelegataire(eaeId, agentId, agentDelegataireId);

		// Then
		assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
	}
	
	@Test
	public void testgetEaesDashboard_NoEaes_ReturnNoContentHttpCode() throws AgentMatriculeConverterServiceException, SirhWSConsumerException {
		
		// Given
		EaeController controller = new EaeController();
		
		IEaeService eaeServiceMock = Mockito.mock(IEaeService.class);
		when(eaeServiceMock.getEaesDashboard(0)).thenReturn(new ArrayList<EaeDashboardItemDto>());
		
		ReflectionTestUtils.setField(controller, "agentMatriculeConverterService", agentMatriculeMock);
		ReflectionTestUtils.setField(controller, "eaeService", eaeServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		
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
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		
		// When
		ResponseEntity<String> result = controller.getEaesDashboard(1);
		
		// Then
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertTrue(result.hasBody());
		
		JSONDeserializer<List<EaeDashboardItemDto>> deserializer = new JSONDeserializer<List<EaeDashboardItemDto>>();
		List<EaeDashboardItemDto> returnedResult = deserializer.deserialize(result.getBody().toString());
		assertEquals(1, returnedResult.size());
	}
	
	@Test
	public void testGetFinalizationInformation_EaeExistsAndServiceReturnsData_Return200() {
		// Given
		Eae eae = new Eae();
		FinalizationInformationDto resultOfService = new FinalizationInformationDto();
		
		IEaeService eaeServiceMock = Mockito.mock(IEaeService.class);
		when(eaeServiceMock.getEae(1)).thenReturn(eae);
		when(eaeServiceMock.getFinalizationInformation(eae)).thenReturn(resultOfService);

		EaeController controller = new EaeController();
		ReflectionTestUtils.setField(controller, "eaeService", eaeServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		
		// When
		ResponseEntity<String> result = controller.getFinalizationInformation(1, 0);
		
		// Then
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertTrue(result.hasBody());
	}
	
	@Test
	public void testGetFinalizationInformation_AgentDoesNotHaveRight_Return403() {
		// Given
		when(eaeSecurityProvider.checkEaeAndWriteRight(1, 900)).thenReturn(new ResponseEntity<String>("message", HttpStatus.FORBIDDEN));
		
		EaeController controller = new EaeController();
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		
		// When
		ResponseEntity<String> result = controller.getFinalizationInformation(1, 900);
		
		// Then
		assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
		assertEquals("message", result.getBody());
	}
	
	@Test
	public void testFinalizeEae_AgentDoesNotHaveRight_Return403() {
		// Given
		when(eaeSecurityProvider.checkEaeAndWriteRight(1, 900)).thenReturn(new ResponseEntity<String>("message", HttpStatus.FORBIDDEN));
		
		EaeController controller = new EaeController();
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		
		// When
		ResponseEntity<String> result = controller.finalizeEae(1, 900, null);
		
		// Then
		assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
		assertEquals("message", result.getBody());
	}
	
	@Test
	public void testFinalizeEae_EaeCantBeFinalized_Return409() throws EaeServiceException {
		// Given
		Eae eae = spy(new Eae());
		Mockito.doNothing().when(eae).clear();
		
		EaeServiceException ex = new EaeServiceException("message");
		
		IEaeService eaeServiceMock = Mockito.mock(IEaeService.class);
		when(eaeServiceMock.getEae(1)).thenReturn(eae);
		org.mockito.Mockito.doThrow(ex).when(eaeServiceMock).finalizEae(Mockito.eq(eae), Mockito.eq(1), Mockito.any(EaeFinalizationDto.class));

		EaeController controller = new EaeController();
		ReflectionTestUtils.setField(controller, "agentMatriculeConverterService", agentMatriculeMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		ReflectionTestUtils.setField(controller, "eaeService", eaeServiceMock);
		
		// When
		ResponseEntity<String> result = controller.finalizeEae(1, 1, "{}");
		
		// Then
		assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
		assertEquals("message", result.getBody());
	}
	
	@Test
	public void testFinalizeEae_EaeIsFinalized_Return200() throws EaeServiceException {
		// Given
		Eae eae = spy(new Eae());
		Mockito.doNothing().when(eae).flush();
		
		IEaeService eaeServiceMock = Mockito.mock(IEaeService.class);
		when(eaeServiceMock.getEae(1)).thenReturn(eae);

		EaeController controller = new EaeController();
		ReflectionTestUtils.setField(controller, "agentMatriculeConverterService", agentMatriculeMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		ReflectionTestUtils.setField(controller, "eaeService", eaeServiceMock);
		ReflectionTestUtils.setField(controller, "messageSource", messageSourceMock);
		
		// When
		ResponseEntity<String> result = controller.finalizeEae(1, 1, "{}");
		
		// Then
		assertEquals(HttpStatus.OK, result.getStatusCode());
	}

	@Test
	public void testCanFinalizeEae_AgentDoesNotHaveRight_Return403() {
		// Given
		when(eaeSecurityProvider.checkEaeAndWriteRight(1, 1)).thenReturn(new ResponseEntity<String>("message", HttpStatus.CONFLICT));
		
		EaeController controller = new EaeController();
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		
		// When
		ResponseEntity<String> result = controller.canFinalizeEae(1, 1);
		
		// Then
		assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
		assertEquals("message", result.getBody());
	}
	
	@Test
	public void testCanFinalizeEae_CannotFinalizeEae_Return409AndMessage() {
		// Given
		Eae eae = new Eae();
		
		CanFinalizeEaeDto dto = new CanFinalizeEaeDto();
		dto.setCanFinalize(false);
		dto.setMessage("message");
		
		IEaeService eaeServiceMock = Mockito.mock(IEaeService.class);
		when(eaeServiceMock.getEae(1)).thenReturn(eae);
		when(eaeServiceMock.canFinalizEae(eae)).thenReturn(dto);

		EaeController controller = new EaeController();
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		ReflectionTestUtils.setField(controller, "eaeService", eaeServiceMock);
		
		// When
		ResponseEntity<String> result = controller.canFinalizeEae(1, 1);
		
		// Then
		assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
		assertEquals("message", result.getBody());
	}
	
	@Test
	public void testCanFinalizeEae_CanFinalizeEae_Return200AndNoMessage() {
		// Given
		Eae eae = new Eae();
		
		CanFinalizeEaeDto dto = new CanFinalizeEaeDto();
		dto.setCanFinalize(true);
		dto.setMessage("message");
		
		IEaeService eaeServiceMock = Mockito.mock(IEaeService.class);
		when(eaeServiceMock.getEae(1)).thenReturn(eae);
		when(eaeServiceMock.canFinalizEae(eae)).thenReturn(dto);

		EaeController controller = new EaeController();
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		ReflectionTestUtils.setField(controller, "eaeService", eaeServiceMock);
		
		// When
		ResponseEntity<String> result = controller.canFinalizeEae(1, 1);
		
		// Then
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertFalse(result.hasBody());
	}
	
}
