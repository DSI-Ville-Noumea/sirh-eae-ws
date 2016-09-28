package nc.noumea.mairie.sirh.eae.web.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.SystemException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.context.MessageSource;
import org.springframework.mock.staticmock.MockStaticEntityMethods;
import org.springframework.test.util.ReflectionTestUtils;

import nc.noumea.mairie.sirh.eae.dto.EaeAppreciationsDto;
import nc.noumea.mairie.sirh.eae.dto.EaeAutoEvaluationDto;
import nc.noumea.mairie.sirh.eae.dto.EaeEvaluationDto;
import nc.noumea.mairie.sirh.eae.dto.EaeEvolutionDto;
import nc.noumea.mairie.sirh.eae.dto.EaeResultatsDto;
import nc.noumea.mairie.sirh.eae.dto.ReturnMessageDto;
import nc.noumea.mairie.sirh.eae.dto.identification.EaeIdentificationDto;
import nc.noumea.mairie.sirh.eae.dto.planAction.EaePlanActionDto;
import nc.noumea.mairie.sirh.eae.dto.poste.EaeFichePosteDto;
import nc.noumea.mairie.sirh.eae.security.IEaeSecurityProvider;
import nc.noumea.mairie.sirh.eae.service.EaeServiceException;
import nc.noumea.mairie.sirh.eae.service.EvaluationServiceException;
import nc.noumea.mairie.sirh.eae.service.IEvaluationService;

@MockStaticEntityMethods
public class EvaluationControllerTest {

	IEaeSecurityProvider eaeSecurityProvider;
	MessageSource messageSourceMock;

	@Before
	public void SetUp() {
		eaeSecurityProvider = Mockito.mock(IEaeSecurityProvider.class);
		messageSourceMock = Mockito.mock(MessageSource.class);
	}

	@Test
	public void testGetEaeIdentifitcation_agentDoesNotHaveRight_ReturnCode403() {
		// Given
		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		
		Mockito.doAnswer(new Answer<Object>() {
			public Object answer(InvocationOnMock invocation) {
				throw new ForbiddenException("");
			}
		})
				.when(eaeSecurityProvider)
				.checkEaeAndReadRight(789, 900000);

		// When
		try {
			controller.getEaeIdentifitcation(789, 900000);
		} catch (ForbiddenException e) {
			return;
		}

		// Then
		fail("Shoud have thrown an exception");
	}

	@Test
	public void testGetEaeIdentifitcation_ExistingEae_ReturnJsonAndCode200() {
		// Given
		EaeIdentificationDto dto = new EaeIdentificationDto();
		dto.setIdEae(789);

		IEvaluationService evaluationServiceMock = Mockito.mock(IEvaluationService.class);
		when(evaluationServiceMock.getEaeIdentification(789)).thenReturn(dto);

		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "evaluationService", evaluationServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);

		// When
		EaeIdentificationDto result = null;
		try {
			result = controller.getEaeIdentifitcation(789, 900000);
		} catch (Exception e) {
			fail("Shoud have not thrown an exception");
		}

		// Then
		assertNotNull(result);
		verify(evaluationServiceMock, times(1)).getEaeIdentification(789);
	}

	@Test
	public void testSetEaeIdentifitcation_evaluateurDoesNotHaveRight_ReturnCode403() {
		// Given
		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		Mockito.doAnswer(new Answer<Object>() {
			public Object answer(InvocationOnMock invocation) {
				throw new ForbiddenException("");
			}
		})
				.when(eaeSecurityProvider)
				.checkEaeAndWriteRight(789, 900000);

		// When
		try {
			controller.setEaeIdentifitcation(789, 900000, null);
		} catch (ForbiddenException e) {
			return;
		}

		// Then
		fail("Shoud have thrown an exception");
	}

	@Test
	public void testSetEaeIdentifitcation_updateExistingEae_ReturnCode200() throws EvaluationServiceException,
			EaeServiceException {
		// Given
		IEvaluationService evaluationServiceMock = Mockito.mock(IEvaluationService.class);

		when(messageSourceMock.getMessage("EAE_IDENTIFICATION_OK", null, null)).thenReturn("L'onglet Identification a été sauvé avec succès");

		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "evaluationService", evaluationServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		ReflectionTestUtils.setField(controller, "messageSource", messageSourceMock);

		// When
		ReturnMessageDto result = null;
		try {
			result = controller.setEaeIdentifitcation(789, 0, new EaeIdentificationDto());
		} catch (Exception e) {
			fail("Shoud have not thrown an exception");
		}

		// Then
		assertEquals(result.getInfos().get(0).toString(), "L'onglet Identification a été sauvé avec succès");
		assertEquals(result.getErrors().size(), 0);
		verify(evaluationServiceMock, times(1)).setEaeIdentification(Mockito.anyInt(),
				Mockito.any(EaeIdentificationDto.class), Mockito.anyBoolean());
	}

	@Test
	public void testSetEaeIdentifitcation_EaeCannotBeStarted_ReturnCode409() throws EvaluationServiceException,
			EaeServiceException {
		// Given
		IEvaluationService evaluationServiceMock = Mockito.mock(IEvaluationService.class);
		Mockito.doThrow(new EaeServiceException("message")).when(evaluationServiceMock).setEaeIdentification(
				Mockito.anyInt(), Mockito.isA(EaeIdentificationDto.class), Mockito.anyBoolean());
		
		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "evaluationService", evaluationServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		ReflectionTestUtils.setField(controller, "messageSource", messageSourceMock);

		// When
		ReturnMessageDto result = controller.setEaeIdentifitcation(789, 0, new EaeIdentificationDto());
		assertEquals(result.getErrors().size(), 1);
	}

	@Test
	public void testGetEaeFichePoste_agentDoesNotHaveright_ReturnCode403() {
		// Given
		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		
		Mockito.doAnswer(new Answer<Object>() {
			public Object answer(InvocationOnMock invocation) {
				throw new ForbiddenException("");
			}
		})
				.when(eaeSecurityProvider)
				.checkEaeAndReadRight(789, 900000);

		// When
		try {
			controller.getEaeFichePoste(789, 900000);
		} catch (ForbiddenException e) {
			return;
		} 

		// Then
		fail("Shoud have thrown an exception");
	}

	@Test
	public void testGetEaeFichePoste_ExistingEae_ReturnJsonAndCode200() {
		// Given
		List<EaeFichePosteDto> dtos = new ArrayList<EaeFichePosteDto>();
		dtos.add(new EaeFichePosteDto());

		IEvaluationService evaluationServiceMock = Mockito.mock(IEvaluationService.class);
		when(evaluationServiceMock.getEaeFichePoste(789)).thenReturn(dtos);

		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "evaluationService", evaluationServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);

		// When
		List<EaeFichePosteDto> result = null;
		try {
			result = controller.getEaeFichePoste(789, 900000);
		} catch (Exception e) {
			fail("Shoud have not thrown an exception");
		} 

		// Then
		assertEquals(1, result.size());
	}

	@Test
	public void testGetEaeResultats_agentDoesNotHaveRight_ReturnCode403() {
		// Given
		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		
		Mockito.doAnswer(new Answer<Object>() {
			public Object answer(InvocationOnMock invocation) {
				throw new ForbiddenException("");
			}
		})
				.when(eaeSecurityProvider)
				.checkEaeAndReadRight(789, 900000);

		// When
		try {
			controller.getEaeResultats(789, 900000);
		} catch (ForbiddenException e) {
			return;
		}

		fail("Shoud have thrown an exception");
	}

	@Test
	public void testGetEaeResultats_ExistingEae_ReturnJsonAndCode200() {
		// Given
		EaeResultatsDto dto = new EaeResultatsDto();

		IEvaluationService evaluationServiceMock = Mockito.mock(IEvaluationService.class);
		when(evaluationServiceMock.getEaeResultats(789)).thenReturn(dto);

		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "evaluationService", evaluationServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);

		// When
		EaeResultatsDto result = null;
		try {
			result = controller.getEaeResultats(789, 900000);
		} catch (Exception e) {
			fail("Shoud have not thrown an exception");
		}

		assertNotNull(result);
		verify(evaluationServiceMock, times(1)).getEaeResultats(789);
	}

	@Test
	public void testSetEaeResultats_evaluateurDoesNotHaveRight_ReturnCode403() {
		// Given
		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		
		Mockito.doAnswer(new Answer<Object>() {
			public Object answer(InvocationOnMock invocation) {
				throw new ForbiddenException("");
			}
		})
				.when(eaeSecurityProvider)
				.checkEaeAndWriteRight(789, 900000);

		// When
		try {
			controller.setEaeResultats(789, 900000, new EaeResultatsDto());
		} catch (ForbiddenException e) {
			return;
		}

		// Then
		fail("Shoud have thrown an exception");
	}

	@Test
	public void testSetEaeResultats_updateExistingEae_ReturnCode200() throws EvaluationServiceException,
			EaeServiceException {
		// Given
		IEvaluationService evaluationServiceMock = Mockito.mock(IEvaluationService.class);

		when(messageSourceMock.getMessage("EAE_RESULTATS_OK", null, null)).thenReturn("L'onglet Résultat a été sauvé avec succès");
		
		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "evaluationService", evaluationServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		ReflectionTestUtils.setField(controller, "messageSource", messageSourceMock);

		// When
		ReturnMessageDto result = null;
		try {
			result = controller.setEaeResultats(789, 900000, new EaeResultatsDto());
		} catch (Exception e) {
			fail("Shoud have thrown an exception");
		}

		// Then
		assertEquals(result.getInfos().get(0).toString(), "L'onglet Résultat a été sauvé avec succès");
		assertEquals(result.getErrors().size(), 0);
		verify(evaluationServiceMock, times(1)).setEaeResultats(Mockito.anyInt(),
				Mockito.any(EaeResultatsDto.class), Mockito.anyBoolean());
	}

	@Test
	public void testSetEaeResultats_EaeCannotBeStarted_ReturnCode409() throws EvaluationServiceException,
			EaeServiceException {
		// Given
		IEvaluationService evaluationServiceMock = Mockito.mock(IEvaluationService.class);
		Mockito.doThrow(new EaeServiceException("message")).when(evaluationServiceMock).setEaeResultats(
				Mockito.anyInt(), Mockito.isA(EaeResultatsDto.class), Mockito.anyBoolean());

		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "evaluationService", evaluationServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		ReflectionTestUtils.setField(controller, "messageSource", messageSourceMock);

		// When
		ReturnMessageDto result = controller.setEaeResultats(789, 0, new EaeResultatsDto());
		assertEquals(result.getErrors().size(), 1);
	}

	@Test
	public void testGetEaeAppreciations_agentDoesNotHaveRight_ReturnCode403() {
		// Given
		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		
		Mockito.doAnswer(new Answer<Object>() {
			public Object answer(InvocationOnMock invocation) {
				throw new ForbiddenException("");
			}
		})
				.when(eaeSecurityProvider)
				.checkEaeAndReadRight(789, 900000);

		// When
		try {
			controller.getEaeAppreciations(789, 900000, null);
		} catch(ForbiddenException e) {
			return;
		}

		// Then
		fail("Shoud have thrown an exception");
	}

	@Test
	public void testGetEaeAppreciations_ExistingEae_ReturnJsonAndCode200() {
		// Given
		EaeAppreciationsDto dto = new EaeAppreciationsDto();

		IEvaluationService evaluationServiceMock = Mockito.mock(IEvaluationService.class);
		when(evaluationServiceMock.getEaeAppreciations(789, null)).thenReturn(dto);

		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "evaluationService", evaluationServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);

		// When
		EaeAppreciationsDto result = null;
		try {
			result = controller.getEaeAppreciations(789, 900000, null);
		} catch(Exception e) {
			fail("Shoud have not thrown an exception");
		}

		// Then
		assertNotNull(result);
		verify(evaluationServiceMock, times(1)).getEaeAppreciations(789, null);
	}

	@Test
	public void testSetEaeAppreciations_evaluateurDoesNotHaveRight_ReturnCode403() {
		// Given
		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		
		Mockito.doAnswer(new Answer<Object>() {
			public Object answer(InvocationOnMock invocation) {
				throw new ForbiddenException("");
			}
		})
				.when(eaeSecurityProvider)
				.checkEaeAndWriteRight(789, 900000);

		// When
		try {
			controller.setEaeAppreciations(789, 900000, new EaeAppreciationsDto());
		} catch(ForbiddenException e) {
			return;
		}

		// Then
		fail("Shoud have thrown an exception");
	}

	@Test
	public void testSetEaeAppreciations_updateExistingEae_ReturnCode200() throws EvaluationServiceException,
			EaeServiceException {
		// Given
		IEvaluationService evaluationServiceMock = Mockito.mock(IEvaluationService.class);

		when(messageSourceMock.getMessage("EAE_APPRECIATIONS_OK", null, null)).thenReturn("L'onglet Appréciation a été sauvé avec succès");
		
		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "evaluationService", evaluationServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		ReflectionTestUtils.setField(controller, "messageSource", messageSourceMock);
		
		// When
		ReturnMessageDto result = null;
		try {
			result = controller.setEaeAppreciations(789, 0, new EaeAppreciationsDto());
		} catch (Exception e) {
			fail("Shoud have not thrown an exception");
		}

		// Then
		assertEquals(result.getInfos().get(0).toString(), "L'onglet Appréciation a été sauvé avec succès");
		assertEquals(result.getErrors().size(), 0);
		verify(evaluationServiceMock, times(1)).setEaeAppreciations(Mockito.eq(789),
				Mockito.any(EaeAppreciationsDto.class), Mockito.anyBoolean());
	}

	@Test
	public void testSetEaeAppreciations_EaeCannotBeStarted_ReturnCode409() throws EvaluationServiceException,
			EaeServiceException {
		// Given
		IEvaluationService evaluationServiceMock = Mockito.mock(IEvaluationService.class);
		Mockito.doThrow(new EaeServiceException("message")).when(evaluationServiceMock).setEaeAppreciations(
				Mockito.anyInt(), Mockito.isA(EaeAppreciationsDto.class), Mockito.anyBoolean());

		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "evaluationService", evaluationServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		ReflectionTestUtils.setField(controller, "messageSource", messageSourceMock);

		// When
		ReturnMessageDto result = controller.setEaeAppreciations(789, 0, new EaeAppreciationsDto());
		assertEquals(result.getErrors().size(), 1);
	}

	@Test
	public void testGetEaeEvaluation_agentDoesNotHaveRight_ReturnCode403() {
		// Given
		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		
		Mockito.doAnswer(new Answer<Object>() {
			public Object answer(InvocationOnMock invocation) {
				throw new ForbiddenException("");
			}
		})
				.when(eaeSecurityProvider)
				.checkEaeAndReadRight(789, 900000);

		// When
		try {
			controller.getEaeEvaluation(789, 900000);
		} catch (ForbiddenException e) {
			return;
		}

		// Then
		fail("Shoud have thrown an exception");
	}

	@Test
	public void testGetEaeEvaluation_ExistingEae_ReturnJsonAndCode200() {
		// Given
		EaeEvaluationDto dto = new EaeEvaluationDto();

		IEvaluationService evaluationServiceMock = Mockito.mock(IEvaluationService.class);
		when(evaluationServiceMock.getEaeEvaluation(789)).thenReturn(dto);

		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "evaluationService", evaluationServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);

		// When
		EaeEvaluationDto result = null;
		try {
			result = controller.getEaeEvaluation(789, 900000);
		} catch (Exception e) {
			fail("Shoud have not thrown an exception");
		}

		// Then
		assertNotNull(result);
		verify(evaluationServiceMock, times(1)).getEaeEvaluation(789);
	}

	@Test
	public void testSetEaeEvaluation_evaluateurDoesNotHaveRight_ReturnCode403() throws IllegalStateException,
			SecurityException, SystemException {
		// Given
		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		
		Mockito.doAnswer(new Answer<Object>() {
			public Object answer(InvocationOnMock invocation) {
				throw new ForbiddenException("");
			}
		})
				.when(eaeSecurityProvider)
				.checkEaeAndWriteRight(789, 900000);

		// When
		try {
			controller.setEaeEvaluation(789, 900000, new EaeEvaluationDto());
		} catch (ForbiddenException e) {
			return;
		}

		// Then
		fail("Shoud have thrown an exception");
	}

	@Test
	public void testSetEaeEvaluation_updateExistingEae_ReturnCode200() throws EvaluationServiceException,
			EaeServiceException, IllegalStateException, SecurityException, SystemException {
		// Given
		IEvaluationService evaluationServiceMock = Mockito.mock(IEvaluationService.class);

		when(messageSourceMock.getMessage("EAE_EVALUATION_OK", null, null)).thenReturn("L'onglet Evaluation a été sauvé avec succès");
		
		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "evaluationService", evaluationServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		ReflectionTestUtils.setField(controller, "messageSource", messageSourceMock);

		// When
		ReturnMessageDto result = null;
		try {
			result = controller.setEaeEvaluation(789, 0, new EaeEvaluationDto());
		} catch (Exception e) {
			fail("Shoud have not thrown an exception");
		}

		// Then
		assertEquals(result.getInfos().get(0).toString(), "L'onglet Evaluation a été sauvé avec succès");
		assertEquals(result.getErrors().size(), 0);
		verify(evaluationServiceMock, times(1)).setEaeEvaluation(Mockito.eq(789),
				Mockito.any(EaeEvaluationDto.class), Mockito.anyBoolean());
	}

	@Test
	public void testSetEaeEvaluation_EaeCannotBeStarted_ReturnCode409() throws EvaluationServiceException,
			EaeServiceException, IllegalStateException, SecurityException, SystemException {
		// Given
		IEvaluationService evaluationServiceMock = Mockito.mock(IEvaluationService.class);
		Mockito.doThrow(new EaeServiceException("message")).when(evaluationServiceMock).setEaeEvaluation(
				Mockito.anyInt(), Mockito.isA(EaeEvaluationDto.class), Mockito.anyBoolean());

		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "evaluationService", evaluationServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		ReflectionTestUtils.setField(controller, "messageSource", messageSourceMock);

		// When
		ReturnMessageDto result = controller.setEaeEvaluation(789, 0, new EaeEvaluationDto());
		assertEquals(result.getErrors().size(), 1);
	}

	@Test
	public void testSetEaeEvaluation_EvaluationDataIsInvalid_ReturnCode409() throws EvaluationServiceException,
			EaeServiceException, IllegalStateException, SecurityException, SystemException {
		// Given
		IEvaluationService evaluationServiceMock = Mockito.mock(IEvaluationService.class);
		Mockito.doThrow(new EvaluationServiceException("message")).when(evaluationServiceMock)
				.setEaeEvaluation(Mockito.eq(789), Mockito.any(EaeEvaluationDto.class), Mockito.anyBoolean());

		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "evaluationService", evaluationServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);

		// When
		ReturnMessageDto result = controller.setEaeEvaluation(789, 0, new EaeEvaluationDto());
		assertEquals(result.getErrors().size(), 1);
	}

	@Test
	public void testGetEaeAutoEvaluation_agentDoesNotHaveRight_ReturnCode403() {
		// Given
		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		
		Mockito.doAnswer(new Answer<Object>() {
			public Object answer(InvocationOnMock invocation) {
				throw new ForbiddenException("");
			}
		})
				.when(eaeSecurityProvider)
				.checkEaeAndReadRight(789, 900000);

		// When
		try {
			controller.getEaeAutoEvaluation(789, 900000);
		} catch(ForbiddenException e) {
			return;
		}

		// Then
		fail("Shoud have thrown an exception");
	}

	@Test
	public void testGetEaeAutoEvaluation_ExistingEae_ReturnJsonAndCode200() {
		// Given
		EaeAutoEvaluationDto dto = new EaeAutoEvaluationDto();

		IEvaluationService evaluationServiceMock = Mockito.mock(IEvaluationService.class);
		when(evaluationServiceMock.getEaeAutoEvaluation(789)).thenReturn(dto);

		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "evaluationService", evaluationServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);

		// When
		EaeAutoEvaluationDto result = null;
		try {
			result = controller.getEaeAutoEvaluation(789, 900000);
		} catch(Exception e) {
			fail("Shoud have not thrown an exception");
		}

		// Then
		assertNotNull(result);
		verify(evaluationServiceMock, times(1)).getEaeAutoEvaluation(789);
	}

	@Test
	public void testSetEaeAutoEvaluation_evaluateurDoesNotHaveRight_ReturnCode403() throws IllegalStateException,
			SecurityException, SystemException {
		// Given
		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		
		Mockito.doAnswer(new Answer<Object>() {
			public Object answer(InvocationOnMock invocation) {
				throw new ForbiddenException("");
			}
		})
				.when(eaeSecurityProvider)
				.checkEaeAndWriteRight(789, 900000);

		// When
		try {
			controller.setEaeAutoEvaluation(789, 900000, null);
		} catch(ForbiddenException e) {
			return;
		}

		// Then
		fail("Shoud have thrown an exception");
	}

	@Test
	public void testSetEaeAutoEvaluation_updateExistingEae_ReturnCode200() throws EvaluationServiceException,
			EaeServiceException, IllegalStateException, SecurityException, SystemException {
		// Given
		IEvaluationService evaluationServiceMock = Mockito.mock(IEvaluationService.class);

		when(messageSourceMock.getMessage("EAE_AUTO_EVALUATION_OK", null, null)).thenReturn("L'onglet Auto-évaluation a été sauvé avec succès");

		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "evaluationService", evaluationServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		ReflectionTestUtils.setField(controller, "messageSource", messageSourceMock);

		// When
		ReturnMessageDto result = null;
		try {
			result = controller.setEaeAutoEvaluation(789, 0, new EaeAutoEvaluationDto());
		} catch(Exception e) {
			fail("Shoud have not thrown an exception");
		}

		// Then
		assertEquals(result.getInfos().get(0).toString(), "L'onglet Auto-évaluation a été sauvé avec succès");
		assertEquals(result.getErrors().size(), 0);
		verify(evaluationServiceMock, times(1)).setEaeAutoEvaluation(Mockito.eq(789),
				Mockito.any(EaeAutoEvaluationDto.class), Mockito.anyBoolean());
	}

	@Test
	public void testSetEaeAutoEvaluation_EaeCannotBeStarted_ReturnCode409() throws EvaluationServiceException,
			EaeServiceException, IllegalStateException, SecurityException, SystemException {
		// Given
		IEvaluationService evaluationServiceMock = Mockito.mock(IEvaluationService.class);
		Mockito.doThrow(new EaeServiceException("message")).when(evaluationServiceMock).setEaeAutoEvaluation(
				Mockito.anyInt(), Mockito.isA(EaeAutoEvaluationDto.class), Mockito.anyBoolean());

		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "evaluationService", evaluationServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		ReflectionTestUtils.setField(controller, "messageSource", messageSourceMock);

		// When
		ReturnMessageDto result = controller.setEaeAutoEvaluation(789, 0, new EaeAutoEvaluationDto());
		assertEquals(result.getErrors().size(), 1);
	}

	@Test
	public void testGetEaePlanAction_agentDoesNotHaveRight_ReturnCode403() {
		// Given
		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		
		Mockito.doAnswer(new Answer<Object>() {
			public Object answer(InvocationOnMock invocation) {
				throw new ForbiddenException("");
			}
		})
				.when(eaeSecurityProvider)
				.checkEaeAndReadRight(789, 900000);

		// When
		try {
			controller.getEaePlanAction(789, 900000);
		} catch(ForbiddenException e) {
			return;
		}

		// Then
		fail("Shoud have thrown an exception");
	}

	@Test
	public void testGetEaePlanAction_ExistingEae_ReturnJsonAndCode200() {
		// Given
		EaePlanActionDto dto = new EaePlanActionDto();

		IEvaluationService evaluationServiceMock = Mockito.mock(IEvaluationService.class);
		when(evaluationServiceMock.getEaePlanAction(789)).thenReturn(dto);

		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "evaluationService", evaluationServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);

		// When
		EaePlanActionDto result = null;
		try {
			result = controller.getEaePlanAction(789, 900000);
		} catch(Exception e) {
			fail("Shoud have not thrown an exception");
		}

		// Then
		assertNotNull(result);
		verify(evaluationServiceMock, times(1)).getEaePlanAction(789);
	}

	@Test
	public void testSetEaePlanAction_evaluateurDoesNotHaveRight_ReturnCode403() throws IllegalStateException,
			SecurityException, SystemException {
		// Given
		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		
		Mockito.doAnswer(new Answer<Object>() {
			public Object answer(InvocationOnMock invocation) {
				throw new ForbiddenException("");
			}
		})
				.when(eaeSecurityProvider)
				.checkEaeAndWriteRight(789, 900000);

		// When
		try {
			controller.setEaePlanAction(789, 900000, null);
		} catch(ForbiddenException e) {
			return;
		}

		// Then
		fail("Shoud have thrown an exception");
	}

	@Test
	public void testSetEaePlanAction_updateExistingEae_ReturnCode200() throws EvaluationServiceException,
			EaeServiceException, IllegalStateException, SecurityException, SystemException {
		// Given
		IEvaluationService evaluationServiceMock = Mockito.mock(IEvaluationService.class);

		when(messageSourceMock.getMessage("EAE_PLAN_ACTION_OK", null, null)).thenReturn("L'onglet Plan d'action a été sauvé avec succès");

		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "evaluationService", evaluationServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		ReflectionTestUtils.setField(controller, "messageSource", messageSourceMock);

		// When
		ReturnMessageDto result = null;
		try {
			result = controller.setEaePlanAction(789, 0, new EaePlanActionDto());
		} catch(Exception e) {
			fail("Shoud have not thrown an exception");
		}

		// Then
		assertEquals(result.getInfos().get(0).toString(), "L'onglet Plan d'action a été sauvé avec succès");
		assertEquals(result.getErrors().size(), 0);
		verify(evaluationServiceMock, times(1)).setEaePlanAction(Mockito.eq(789),
				Mockito.any(EaePlanActionDto.class), Mockito.anyBoolean());
	}

	@Test
	public void testSetEaePlanAction_EaeCannotBeStarted_ReturnCode409() throws EvaluationServiceException,
			EaeServiceException, IllegalStateException, SecurityException, SystemException {
		// Given
		IEvaluationService evaluationServiceMock = Mockito.mock(IEvaluationService.class);
		Mockito.doThrow(new EaeServiceException("message")).when(evaluationServiceMock).setEaePlanAction(
				Mockito.anyInt(), Mockito.isA(EaePlanActionDto.class), Mockito.anyBoolean());

		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "evaluationService", evaluationServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		ReflectionTestUtils.setField(controller, "messageSource", messageSourceMock);

		// When
		ReturnMessageDto result = controller.setEaePlanAction(789, 0, new EaePlanActionDto());
		assertEquals(result.getErrors().size(), 1);
	}

	@Test
	public void testGetEaeEvolution_agentDoesNotHaveRight_ReturnCode403() {
		// Given
		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		
		Mockito.doAnswer(new Answer<Object>() {
			public Object answer(InvocationOnMock invocation) {
				throw new ForbiddenException("");
			}
		})
				.when(eaeSecurityProvider)
				.checkEaeAndReadRight(789, 900000);

		// When
		try {
			controller.getEaeEvolution(789, 900000);
		} catch (ForbiddenException e) {
			return;
		}

		// Then
		fail("Shoud have thrown an exception");
	}

	@Test
	public void testGetEaeEvolution_ExistingEae_ReturnJsonAndCode200() {
		// Given
		EaeEvolutionDto dto = new EaeEvolutionDto();

		IEvaluationService evaluationServiceMock = Mockito.mock(IEvaluationService.class);
		when(evaluationServiceMock.getEaeEvolution(789)).thenReturn(dto);

		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "evaluationService", evaluationServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);

		// When
		EaeEvolutionDto result = null;
		try {
			result = controller.getEaeEvolution(789, 900000);
		} catch (Exception e) {
			fail("Shoud have not thrown an exception");
		}

		// Then
		assertNotNull(result);
		verify(evaluationServiceMock, times(1)).getEaeEvolution(789);
	}

	@Test
	public void testSetEaeEvolution_evaluateurDoesNotHaveRight_ReturnCode403() throws IllegalStateException,
			SecurityException, SystemException {
		// Given
		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		
		Mockito.doAnswer(new Answer<Object>() {
			public Object answer(InvocationOnMock invocation) {
				throw new ForbiddenException("");
			}
		})
				.when(eaeSecurityProvider)
				.checkEaeAndWriteRight(789, 900000);

		// When
		try {
			controller.setEaeEvolution(789, 900000, null);
		} catch (ForbiddenException e) {
			return;
		}

		// Then
		fail("Shoud have thrown an exception");
	}

	@Test
	public void testSetEaeEvolution_updateExistingEae_ReturnCode200() throws EvaluationServiceException,
			EaeServiceException, IllegalStateException, SecurityException, SystemException {
		// Given
		IEvaluationService evaluationServiceMock = Mockito.mock(IEvaluationService.class);

		when(messageSourceMock.getMessage("EAE_EVOLUTION_OK", null, null)).thenReturn("L'onglet Evolution a été sauvé avec succès");

		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "evaluationService", evaluationServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		ReflectionTestUtils.setField(controller, "messageSource", messageSourceMock);

		// When
		ReturnMessageDto result = null;
		try {
			result = controller.setEaeEvolution(789, 0, new EaeEvolutionDto());
		} catch(Exception e){
			fail("Shoud have not thrown an exception");
		}

		// Then
		assertEquals(result.getInfos().get(0).toString(), "L'onglet Evolution a été sauvé avec succès");
		verify(evaluationServiceMock, times(1)).setEaeEvolution(Mockito.eq(789),
				Mockito.any(EaeEvolutionDto.class), Mockito.anyBoolean());
	}

	@Test
	public void testSetEaeEvolution_EaeCannotBeStarted_ReturnCode409() throws EvaluationServiceException,
			EaeServiceException, IllegalStateException, SecurityException, SystemException {
		// Given
		IEvaluationService evaluationServiceMock = Mockito.mock(IEvaluationService.class);
		Mockito.doThrow(new EaeServiceException("message")).when(evaluationServiceMock).setEaeEvolution(
				Mockito.anyInt(), Mockito.isA(EaeEvolutionDto.class), Mockito.anyBoolean());

		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "evaluationService", evaluationServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		ReflectionTestUtils.setField(controller, "messageSource", messageSourceMock);

		// When
		ReturnMessageDto result = controller.setEaeEvolution(789, 0, new EaeEvolutionDto());
		assertEquals(result.getErrors().size(), 1);
	}

	@Test
	public void testSetEaeEvolution_EaeEvolutionCannotBeSaved_ReturnCode409() throws EvaluationServiceException,
			EaeServiceException, IllegalStateException, SecurityException, SystemException {
		// Given
		IEvaluationService evaluationServiceMock = Mockito.mock(IEvaluationService.class);
		Mockito.doThrow(new EvaluationServiceException("message")).when(evaluationServiceMock)
				.setEaeEvolution(Mockito.eq(789), Mockito.any(EaeEvolutionDto.class), Mockito.anyBoolean());

		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "evaluationService", evaluationServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);

		// When
		ReturnMessageDto result = controller.setEaeEvolution(789, 0, new EaeEvolutionDto());
		assertEquals(result.getErrors().size(), 1);
	}
}
