package nc.noumea.mairie.sirh.eae.web.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.dto.CanFinalizeEaeDto;
import nc.noumea.mairie.sirh.eae.dto.EaeDashboardItemDto;
import nc.noumea.mairie.sirh.eae.dto.EaeEvalueNameDto;
import nc.noumea.mairie.sirh.eae.dto.EaeFinalizationDto;
import nc.noumea.mairie.sirh.eae.dto.EaeListItemDto;
import nc.noumea.mairie.sirh.eae.dto.FinalizationInformationDto;
import nc.noumea.mairie.sirh.eae.dto.ReturnMessageDto;
import nc.noumea.mairie.sirh.eae.security.IEaeSecurityProvider;
import nc.noumea.mairie.sirh.eae.service.AgentMatriculeConverterServiceException;
import nc.noumea.mairie.sirh.eae.service.EaeServiceException;
import nc.noumea.mairie.sirh.eae.service.IAgentMatriculeConverterService;
import nc.noumea.mairie.sirh.eae.service.IEaeService;
import nc.noumea.mairie.sirh.ws.SirhWSConsumerException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.context.MessageSource;
import org.springframework.test.util.ReflectionTestUtils;

public class EaeControllerTest {

	private static IAgentMatriculeConverterService	agentMatriculeMock;

	IEaeSecurityProvider							eaeSecurityProvider;
	MessageSource									messageSourceMock;

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
		try {
			controller.listEaesByAgent(0);
		} catch (NoContentException e) {
			return;
		}

		// Then
		fail("Shoud have thrown an exception");
	}

	@Test
	public void testlistEaesByAgent_1EaeForIdAgent_ReturnListWith1ItemAndHttpOK()
			throws AgentMatriculeConverterServiceException, SirhWSConsumerException {

		// Given
		List<EaeListItemDto> resultOfService = new ArrayList<EaeListItemDto>(Arrays.asList(new EaeListItemDto()));
		EaeController controller = new EaeController();

		IEaeService eaeServiceMock = Mockito.mock(IEaeService.class);
		when(eaeServiceMock.listEaesByAgentId(1)).thenReturn(resultOfService);

		ReflectionTestUtils.setField(controller, "agentMatriculeConverterService", agentMatriculeMock);
		ReflectionTestUtils.setField(controller, "eaeService", eaeServiceMock);

		// When
		List<EaeListItemDto> result = controller.listEaesByAgent(1);

		// Then
		assertNotNull(result);
		assertEquals(1, result.size());
	}

	@Test
	public void testInitializeEae_initialisationOk_ReturnHttp200() throws EaeServiceException, SirhWSConsumerException {

		// Given
		int agentId = 12;
		int agentEvalueId = 13;
		Eae lastEae = new Eae();
		lastEae.setIdEae(120);
		List<Eae> eaeToCreateList = new ArrayList<Eae>(Arrays.asList(lastEae));

		IEaeService eaeServiceMock = Mockito.mock(IEaeService.class);
		when(eaeServiceMock.findCurrentAndPreviousEaesByAgentId(agentEvalueId)).thenReturn(eaeToCreateList);

		when(messageSourceMock.getMessage("EAE_INITIALISE_OK", null, null)).thenReturn("L'eae a �t� initialis� avec succ�s");

		EaeController controller = new EaeController();
		ReflectionTestUtils.setField(controller, "agentMatriculeConverterService", agentMatriculeMock);
		ReflectionTestUtils.setField(controller, "eaeService", eaeServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		ReflectionTestUtils.setField(controller, "messageSource", messageSourceMock);

		// When
		ReturnMessageDto result = null;
		try {
			result = controller.initializeEae(agentId, agentEvalueId);
		} catch (Exception e) {
			fail("Shoud have not thrown an exception");
		}

		// Then
		assertEquals(result.getInfos().get(0), "L'eae a �t� initialis� avec succ�s");

		verify(eaeSecurityProvider, times(1)).checkEaeAndWriteRight(120, agentId);
		verify(eaeServiceMock, times(1)).findCurrentAndPreviousEaesByAgentId(agentEvalueId);
		verify(eaeServiceMock, times(1)).initializeEae(lastEae, null);
	}

	@Test
	public void testInitializeEae_noEaeForAgent_ReturnHttp404() throws EaeServiceException, SirhWSConsumerException {

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
		try {
			controller.initializeEae(agentId, agentEvalueId);
		} catch (NotFoundException e) {
			return;
		}

		// Then
		fail("Shoud have thrown an exception");
	}

	@Test
	public void testInitializeEae_EaeForAgentIsAlreadyInitialized_ReturnHttp409() throws EaeServiceException, SirhWSConsumerException {

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
		ReturnMessageDto result = new ReturnMessageDto();
		try {
			result = controller.initializeEae(agentId, agentEvalueId);
		} catch (ConflictException e) {
			return;
		}

		// Then
		assertEquals(result.getErrors().get(0), "message");
	}

	@Test
	public void testInitializeEae_AgentDoesNotHaveRight_ReturnHttp403() throws EaeServiceException, SirhWSConsumerException {

		// Given
		int agentId = 12;
		int agentEvalueId = 13;
		Eae lastEae = new Eae();
		lastEae.setIdEae(120);
		List<Eae> eaeToCreateList = new ArrayList<Eae>(Arrays.asList(lastEae));

		IEaeService eaeServiceMock = Mockito.mock(IEaeService.class);
		when(eaeServiceMock.findCurrentAndPreviousEaesByAgentId(agentEvalueId)).thenReturn(eaeToCreateList);

		Mockito.doAnswer(new Answer<Object>() {
			public Object answer(InvocationOnMock invocation) {
				throw new ForbiddenException("");
			}
		}).when(eaeSecurityProvider).checkEaeAndWriteRight(lastEae.getIdEae(), agentId);

		EaeController controller = new EaeController();
		ReflectionTestUtils.setField(controller, "agentMatriculeConverterService", agentMatriculeMock);
		ReflectionTestUtils.setField(controller, "eaeService", eaeServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);

		// When
		try {
			controller.initializeEae(agentId, agentEvalueId);
		} catch (ForbiddenException e) {
			return;
		}

		// Then
		fail("Shoud have thrown an exception");
	}

	@Test
	public void testSetDelegataire_DelegataireCannotBeSet_ReturnHttp409() throws EaeServiceException {

		// Given
		int agentId = 12;
		int eaeId = 13;
		int agentDelegataireId = 14;
		EaeServiceException ex = new EaeServiceException("message");

		IEaeService eaeServiceMock = Mockito.mock(IEaeService.class);
		org.mockito.Mockito.doThrow(ex).when(eaeServiceMock).setDelegataire(eaeId, agentDelegataireId);

		EaeController controller = new EaeController();
		ReflectionTestUtils.setField(controller, "agentMatriculeConverterService", agentMatriculeMock);
		ReflectionTestUtils.setField(controller, "eaeService", eaeServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);

		// When
		ReturnMessageDto result = null;
		try {
			result = controller.setDelegataire(eaeId, agentId, agentDelegataireId);
		} catch (ConflictException e) {
			assertEquals(e.getMessage(), "message");
			return;
		}

		// Then
		assertEquals(result.getErrors().get(0), "message");
	}

	@Test
	public void testSetDelegataire_EaeAndDelegataireExist_ReturnHttp200() throws EaeServiceException {

		// Given
		int agentId = 12;
		int eaeId = 13;
		int agentDelegataireId = 14;

		IEaeService eaeServiceMock = Mockito.mock(IEaeService.class);

		when(messageSourceMock.getMessage("EAE_DELEGATAIRE_OK", null, null)).thenReturn("Le d�l�gataire �t� affect� avec succ�s");

		EaeController controller = new EaeController();
		ReflectionTestUtils.setField(controller, "agentMatriculeConverterService", agentMatriculeMock);
		ReflectionTestUtils.setField(controller, "eaeService", eaeServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		ReflectionTestUtils.setField(controller, "messageSource", messageSourceMock);

		// When
		ReturnMessageDto result = null;
		try {
			result = controller.setDelegataire(eaeId, agentId, agentDelegataireId);
		} catch (Exception e) {
			fail("Shoud have not thrown an exception");
		}

		// Then
		assertEquals("Le d�l�gataire �t� affect� avec succ�s", result.getInfos().get(0));
		verify(eaeServiceMock, times(1)).setDelegataire(eaeId, agentDelegataireId);
	}

	@Test
	public void testSetDelegataire_AgentDoesNotHaveRight_ReturnHttp403() throws EaeServiceException {

		// Given
		int agentId = 12;
		int eaeId = 13;
		int agentDelegataireId = 14;

		Mockito.doAnswer(new Answer<Object>() {
			public Object answer(InvocationOnMock invocation) {
				throw new ForbiddenException("");
			}
		}).when(eaeSecurityProvider).checkEaeAndWriteRight(eaeId, agentId);

		EaeController controller = new EaeController();
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);

		// When
		try {
			controller.setDelegataire(eaeId, agentId, agentDelegataireId);
		} catch (Exception e) {
			return;
		}

		fail("Shoud have thrown an exception");
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
		try {
			controller.getEaesDashboard(0);
		} catch (NoContentException e) {
			return;
		}

		// Then
		fail("Shoud have thrown an exception");
	}

	@Test
	public void testgetEaesDashboard_1EaeForIdAgent_ReturnListWith1ItemAndHttpOK()
			throws AgentMatriculeConverterServiceException, SirhWSConsumerException {

		// Given
		List<EaeDashboardItemDto> resultOfService = new ArrayList<EaeDashboardItemDto>(Arrays.asList(new EaeDashboardItemDto()));
		EaeController controller = new EaeController();

		IEaeService eaeServiceMock = Mockito.mock(IEaeService.class);
		when(eaeServiceMock.getEaesDashboard(1)).thenReturn(resultOfService);

		ReflectionTestUtils.setField(controller, "agentMatriculeConverterService", agentMatriculeMock);
		ReflectionTestUtils.setField(controller, "eaeService", eaeServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);

		// When
		List<EaeDashboardItemDto> result = null;
		try {
			result = controller.getEaesDashboard(1);
		} catch (NoContentException e) {
			fail("Shoud have not thrown an exception");
		}

		// Then
		assertEquals(1, result.size());
	}

	@Test
	public void testGetFinalizationInformation_EaeExistsAndServiceReturnsData_Return200() throws SirhWSConsumerException {
		// Given
		FinalizationInformationDto resultOfService = new FinalizationInformationDto();

		IEaeService eaeServiceMock = Mockito.mock(IEaeService.class);
		when(eaeServiceMock.getFinalizationInformation(1)).thenReturn(resultOfService);

		EaeController controller = new EaeController();
		ReflectionTestUtils.setField(controller, "eaeService", eaeServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);

		// When
		// When
		FinalizationInformationDto result = null;
		try {
			result = controller.getFinalizationInformation(1, 0);
		} catch (NoContentException e) {
			fail("Shoud have not thrown an exception");
		}

		// Then
		assertNotNull(result);
	}

	@Test
	public void testGetFinalizationInformation_AgentDoesNotHaveRight_Return403() {
		// Given
		Mockito.doAnswer(new Answer<Object>() {
			public Object answer(InvocationOnMock invocation) {
				throw new ForbiddenException("");
			}
		}).when(eaeSecurityProvider).checkEaeAndWriteRight(1, 0);

		EaeController controller = new EaeController();
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);

		// When
		try {
			controller.getFinalizationInformation(1, 0);
		} catch (ForbiddenException e) {
			return;
		}

		// Then
		fail("Shoud have thrown an exception");
	}

	@Test
	public void testFinalizeEae_AgentDoesNotHaveRight_Return403() {
		// Given
		Mockito.doAnswer(new Answer<Object>() {
			public Object answer(InvocationOnMock invocation) {
				throw new ForbiddenException("");
			}
		}).when(eaeSecurityProvider).checkEaeAndWriteRight(1, 900);

		EaeController controller = new EaeController();
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);

		// When
		try {
			controller.finalizeEae(1, 900, new EaeFinalizationDto());
		} catch (ForbiddenException e) {
			return;
		}

		// Then
		fail("Shoud have thrown an exception");
	}

	@Test
	public void testFinalizeEae_EaeIsFinalized_Return200() throws EaeServiceException {
		// Given
		IEaeService eaeServiceMock = Mockito.mock(IEaeService.class);

		ReturnMessageDto rmd = new ReturnMessageDto();
		rmd.getInfos().add("L'eae a été finalisé avec succès");

		when(eaeServiceMock.finalizeEae(Mockito.anyInt(), Mockito.anyInt(), Mockito.any(EaeFinalizationDto.class))).thenReturn(rmd);
		
		EaeController controller = new EaeController();
		ReflectionTestUtils.setField(controller, "agentMatriculeConverterService", agentMatriculeMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		ReflectionTestUtils.setField(controller, "eaeService", eaeServiceMock);
		
		// When
		ReturnMessageDto result = null;
		try {
			result = controller.finalizeEae(1, 1, new EaeFinalizationDto());
		} catch (Exception e) {
			fail("Shoud have not thrown an exception");
		}

		// Then
		assertEquals("L'eae a été finalisé avec succès", result.getInfos().get(0));
	}

	@Test
	public void testCanFinalizeEae_AgentDoesNotHaveRight_Return403() {
		// Given
		Mockito.doAnswer(new Answer<Object>() {
			public Object answer(InvocationOnMock invocation) {
				throw new ForbiddenException("message conflict");
			}
		}).when(eaeSecurityProvider).checkEaeAndWriteRight(1, 1);

		EaeController controller = new EaeController();
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);

		// When*
		try {
			controller.canFinalizeEae(1, 1);
		} catch (ForbiddenException e) {
			assertEquals(e.getMessage(), "message conflict");
			return;
		}

		// Then
		fail("Shoud have thrown an exception");
	}

	@Test
	public void testCanFinalizeEae_CannotFinalizeEae_Return409AndMessage() {
		// Given
		CanFinalizeEaeDto dto = new CanFinalizeEaeDto();
		dto.setCanFinalize(false);
		dto.setMessage("message");

		IEaeService eaeServiceMock = Mockito.mock(IEaeService.class);
		when(eaeServiceMock.canFinalizEae(1)).thenReturn(dto);

		EaeController controller = new EaeController();
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		ReflectionTestUtils.setField(controller, "eaeService", eaeServiceMock);

		// When
		try {
			controller.canFinalizeEae(1, 1);
		} catch (ConflictException e) {
			assertEquals(e.getMessage(), "message");
			return;
		}

		// Then
		fail("Shoud have thrown an exception");
	}

	@Test
	public void testCanFinalizeEae_CanFinalizeEae_Return200AndNoMessage() {
		// Given
		CanFinalizeEaeDto dto = new CanFinalizeEaeDto();
		dto.setCanFinalize(true);
		dto.setMessage("message");

		IEaeService eaeServiceMock = Mockito.mock(IEaeService.class);
		when(eaeServiceMock.canFinalizEae(1)).thenReturn(dto);

		EaeController controller = new EaeController();
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		ReflectionTestUtils.setField(controller, "eaeService", eaeServiceMock);

		// When
		try {
			controller.canFinalizeEae(1, 1);
		} catch (Exception e) {
			fail("Shoud have not thrown an exception");
		}
	}

	@Test
	public void getEvalueFullname_EaeDoesNotExists_ReturnHttp404() {
		// Given
		Mockito.doAnswer(new Answer<Object>() {
			public Object answer(InvocationOnMock invocation) {
				throw new NotFoundException();
			}
		}).when(eaeSecurityProvider).checkEaeAndReadRight(1, 1);

		EaeController controller = new EaeController();
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);

		// When
		try {
			controller.getEvalueFullname(1, 1);
		} catch (NotFoundException e) {
			return;
		}

		// Then
		fail("Shoud have thrown an exception");
	}

	@Test
	public void getEvalueFullname_AgentDoesNotHaveRight_Return403() {
		// Given
		Mockito.doAnswer(new Answer<Object>() {
			public Object answer(InvocationOnMock invocation) {
				throw new ConflictException("message");
			}
		}).when(eaeSecurityProvider).checkEaeAndReadRight(1, 1);

		EaeController controller = new EaeController();
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);

		// When
		try {
			controller.getEvalueFullname(1, 1);
		} catch (ConflictException e) {
			assertEquals("message", e.getMessage());
			return;
		}

		// Then
		fail("Shoud have thrown an exception");
	}

	@Test
	public void getEvalueFullname_EaeExists_ReturnJsonResult() {
		// Given
		EaeEvalueNameDto dto = new EaeEvalueNameDto();
		dto.setPrenom("NICOLAS");
		dto.setNom("RAYNAUD");

		IEaeService service = mock(IEaeService.class);
		when(service.getEvalueName(1)).thenReturn(dto);

		EaeController controller = new EaeController();
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		ReflectionTestUtils.setField(controller, "eaeService", service);

		// When
		EaeEvalueNameDto result = null;
		try {
			result = controller.getEvalueFullname(1, 1);
		} catch (ConflictException e) {
			fail("Shoud have not thrown an exception");
		}

		// Then
		assertNotNull(result);
		assertEquals("NICOLAS", result.getPrenom());
		assertEquals("RAYNAUD", result.getNom());
	}

}
