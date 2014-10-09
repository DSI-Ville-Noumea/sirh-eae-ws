package nc.noumea.mairie.sirh.eae.web.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.SystemException;

import nc.noumea.mairie.sirh.eae.dto.EaeAppreciationsDto;
import nc.noumea.mairie.sirh.eae.dto.EaeAutoEvaluationDto;
import nc.noumea.mairie.sirh.eae.dto.EaeEvaluationDto;
import nc.noumea.mairie.sirh.eae.dto.EaeEvolutionDto;
import nc.noumea.mairie.sirh.eae.dto.EaeResultatsDto;
import nc.noumea.mairie.sirh.eae.dto.identification.EaeIdentificationDto;
import nc.noumea.mairie.sirh.eae.dto.planAction.EaePlanActionDto;
import nc.noumea.mairie.sirh.eae.dto.poste.EaeFichePosteDto;
import nc.noumea.mairie.sirh.eae.security.IEaeSecurityProvider;
import nc.noumea.mairie.sirh.eae.service.EaeServiceException;
import nc.noumea.mairie.sirh.eae.service.EvaluationServiceException;
import nc.noumea.mairie.sirh.eae.service.IEaeService;
import nc.noumea.mairie.sirh.eae.service.IEvaluationService;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.staticmock.MockStaticEntityMethods;
import org.springframework.test.util.ReflectionTestUtils;

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
		when(eaeSecurityProvider.checkEaeAndReadRight(789, 900000)).thenReturn(
				new ResponseEntity<String>(HttpStatus.FORBIDDEN));

		// When
		ResponseEntity<String> result = controller.getEaeIdentifitcation(789, 900000);

		// Then
		assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
		assertFalse(result.hasBody());
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
		ResponseEntity<String> result = controller.getEaeIdentifitcation(789, 900000);

		// Then
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertTrue(result.hasBody());

		verify(evaluationServiceMock, times(1)).getEaeIdentification(789);
	}

	@Test
	public void testSetEaeIdentifitcation_evaluateurDoesNotHaveRight_ReturnCode403() {
		// Given
		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		when(eaeSecurityProvider.checkEaeAndWriteRight(789, 9000000)).thenReturn(
				new ResponseEntity<String>(HttpStatus.FORBIDDEN));

		// When
		ResponseEntity<String> result = controller.setEaeIdentifitcation(789, 9000000, null);

		// Then
		assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
		assertFalse(result.hasBody());
	}

	@Test
	public void testSetEaeIdentifitcation_updateExistingEae_ReturnCode200() throws EvaluationServiceException,
			EaeServiceException {
		// Given
		String json = "{}";

		IEvaluationService evaluationServiceMock = Mockito.mock(IEvaluationService.class);

		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "evaluationService", evaluationServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		ReflectionTestUtils.setField(controller, "messageSource", messageSourceMock);

		// When
		ResponseEntity<String> result = controller.setEaeIdentifitcation(789, 0, json);

		// Then
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertFalse(result.hasBody());

		verify(evaluationServiceMock, times(1)).setEaeIdentification(Mockito.anyInt(),
				Mockito.any(EaeIdentificationDto.class));
	}

	@Test
	public void testSetEaeIdentifitcation_EaeCannotBeStarted_ReturnCode409() throws EvaluationServiceException,
			EaeServiceException {
		// Given
		String json = "{}";

		IEvaluationService evaluationServiceMock = Mockito.mock(IEvaluationService.class);
		Mockito.doThrow(new EaeServiceException("message")).when(evaluationServiceMock).setEaeIdentification(Mockito.anyInt(), Mockito.isA(EaeIdentificationDto.class));
		
		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "evaluationService", evaluationServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		ReflectionTestUtils.setField(controller, "messageSource", messageSourceMock);

		// When
		ResponseEntity<String> result = controller.setEaeIdentifitcation(789, 0, json);

		// Then
		assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
		assertEquals("message", result.getBody());
		
		verify(evaluationServiceMock, times(1)).setEaeIdentification(Mockito.anyInt(),
				Mockito.isA(EaeIdentificationDto.class));
	}

	@Test
	public void testGetEaeFichePoste_agentDoesNotHaveright_ReturnCode403() {
		// Given
		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		when(eaeSecurityProvider.checkEaeAndReadRight(789, 900000)).thenReturn(
				new ResponseEntity<String>(HttpStatus.FORBIDDEN));

		// When
		ResponseEntity<String> result = controller.getEaeFichePoste(789, 900000);

		// Then
		assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
		assertFalse(result.hasBody());
	}

	@Test
	public void testGetEaeFichePoste_ExistingEae_ReturnJsonAndCode200() {
		// Given
		List<EaeFichePosteDto> dtos = new ArrayList<EaeFichePosteDto>();

		IEvaluationService evaluationServiceMock = Mockito.mock(IEvaluationService.class);
		when(evaluationServiceMock.getEaeFichePoste(789)).thenReturn(dtos);

		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "evaluationService", evaluationServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);

		// When
		ResponseEntity<String> result = controller.getEaeFichePoste(789, 900000);

		// Then
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertTrue(result.hasBody());

		verify(evaluationServiceMock, times(1)).getEaeFichePoste(789);
	}

	@Test
	public void testGetEaeResultats_agentDoesNotHaveRight_ReturnCode403() {
		// Given
		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		when(eaeSecurityProvider.checkEaeAndReadRight(789, 900000)).thenReturn(
				new ResponseEntity<String>(HttpStatus.FORBIDDEN));

		// When
		ResponseEntity<String> result = controller.getEaeResultats(789, 900000);

		// Then
		assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
		assertFalse(result.hasBody());
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
		ResponseEntity<String> result = controller.getEaeResultats(789, 900000);

		// Then
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertTrue(result.hasBody());

		verify(evaluationServiceMock, times(1)).getEaeResultats(789);
	}

	@Test
	public void testSetEaeResultats_evaluateurDoesNotHaveRight_ReturnCode403() {
		// Given
		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		when(eaeSecurityProvider.checkEaeAndWriteRight(789, 900000)).thenReturn(
				new ResponseEntity<String>(HttpStatus.FORBIDDEN));

		// When
		ResponseEntity<String> result = controller.setEaeResultats(789, 900000, null);

		// Then
		assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
		assertFalse(result.hasBody());
	}

	@Test
	public void testSetEaeResultats_updateExistingEae_ReturnCode200() throws EvaluationServiceException,
			EaeServiceException {
		// Given
		String json = "{}";

		IEvaluationService evaluationServiceMock = Mockito.mock(IEvaluationService.class);

		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "evaluationService", evaluationServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		ReflectionTestUtils.setField(controller, "messageSource", messageSourceMock);

		// When
		ResponseEntity<String> result = controller.setEaeResultats(789, 0, json);

		// Then
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertFalse(result.hasBody());

		verify(evaluationServiceMock, times(1)).setEaeResultats(Mockito.anyInt(),
				Mockito.any(EaeResultatsDto.class));
	}

	@Test
	public void testSetEaeResultats_EaeCannotBeStarted_ReturnCode409() throws EvaluationServiceException,
			EaeServiceException {
		// Given
		String json = "{}";

		IEvaluationService evaluationServiceMock = Mockito.mock(IEvaluationService.class);
		Mockito.doThrow(new EaeServiceException("message")).when(evaluationServiceMock).setEaeResultats(Mockito.anyInt(), Mockito.isA(EaeResultatsDto.class));
		
		IEaeService eaeServiceMock = Mockito.mock(IEaeService.class);
		Mockito.doThrow(new EaeServiceException("message")).when(eaeServiceMock).startEae(789);

		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "evaluationService", evaluationServiceMock);
		ReflectionTestUtils.setField(controller, "eaeService", eaeServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		ReflectionTestUtils.setField(controller, "messageSource", messageSourceMock);

		// When
		ResponseEntity<String> result = controller.setEaeResultats(789, 0, json);

		// Then
		Mockito.verify(eaeServiceMock, Mockito.times(1)).clear();
		assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
		assertEquals("message", result.getBody());

		verify(evaluationServiceMock, times(1)).setEaeResultats(Mockito.eq(789),
				Mockito.any(EaeResultatsDto.class));
	}

	@Test
	public void testGetEaeAppreciations_agentDoesNotHaveRight_ReturnCode403() {
		// Given
		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		when(eaeSecurityProvider.checkEaeAndReadRight(789, 900000)).thenReturn(
				new ResponseEntity<String>(HttpStatus.FORBIDDEN));

		// When
		ResponseEntity<String> result = controller.getEaeAppreciations(789, 900000, null);

		// Then
		assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
		assertFalse(result.hasBody());
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
		ResponseEntity<String> result = controller.getEaeAppreciations(789, 900000, null);

		// Then
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertTrue(result.hasBody());

		verify(evaluationServiceMock, times(1)).getEaeAppreciations(789, null);
	}

	@Test
	public void testSetEaeAppreciations_evaluateurDoesNotHaveRight_ReturnCode403() {
		// Given
		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		when(eaeSecurityProvider.checkEaeAndWriteRight(789, 900000)).thenReturn(
				new ResponseEntity<String>(HttpStatus.FORBIDDEN));

		// When
		ResponseEntity<String> result = controller.setEaeAppreciations(789, 900000, null);

		// Then
		assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
		assertFalse(result.hasBody());
	}

	@Test
	public void testSetEaeAppreciations_updateExistingEae_ReturnCode200() throws EvaluationServiceException,
			EaeServiceException {
		// Given
		String json = "{}";

		IEvaluationService evaluationServiceMock = Mockito.mock(IEvaluationService.class);

		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "evaluationService", evaluationServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		ReflectionTestUtils.setField(controller, "messageSource", messageSourceMock);

		// When
		ResponseEntity<String> result = controller.setEaeAppreciations(789, 0, json);

		// Then
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertFalse(result.hasBody());

		verify(evaluationServiceMock, times(1)).setEaeAppreciations(Mockito.eq(789),
				Mockito.any(EaeAppreciationsDto.class));
	}

	@Test
	public void testSetEaeAppreciations_EaeCannotBeStarted_ReturnCode409() throws EvaluationServiceException,
			EaeServiceException {
		// Given
		String json = "{}";

		IEvaluationService evaluationServiceMock = Mockito.mock(IEvaluationService.class);
		Mockito.doThrow(new EaeServiceException("message")).when(evaluationServiceMock).setEaeAppreciations(Mockito.anyInt(), Mockito.isA(EaeAppreciationsDto.class));

		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "evaluationService", evaluationServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		ReflectionTestUtils.setField(controller, "messageSource", messageSourceMock);

		// When
		ResponseEntity<String> result = controller.setEaeAppreciations(789, 0, json);

		// Then
		assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
		assertEquals("message", result.getBody());

		verify(evaluationServiceMock, times(1)).setEaeAppreciations(Mockito.eq(789),
				Mockito.any(EaeAppreciationsDto.class));
	}

	@Test
	public void testGetEaeEvaluation_agentDoesNotHaveRight_ReturnCode403() {
		// Given
		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		when(eaeSecurityProvider.checkEaeAndReadRight(789, 900000)).thenReturn(
				new ResponseEntity<String>(HttpStatus.FORBIDDEN));

		// When
		ResponseEntity<String> result = controller.getEaeEvaluation(789, 900000);

		// Then
		assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
		assertFalse(result.hasBody());
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
		ResponseEntity<String> result = controller.getEaeEvaluation(789, 900000);

		// Then
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertTrue(result.hasBody());

		verify(evaluationServiceMock, times(1)).getEaeEvaluation(789);
	}

	@Test
	public void testSetEaeEvaluation_evaluateurDoesNotHaveRight_ReturnCode403() throws IllegalStateException,
			SecurityException, SystemException {
		// Given
		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		when(eaeSecurityProvider.checkEaeAndWriteRight(789, 900000)).thenReturn(
				new ResponseEntity<String>(HttpStatus.FORBIDDEN));

		// When
		ResponseEntity<String> result = controller.setEaeEvaluation(789, 900000, null);

		// Then
		assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
		assertFalse(result.hasBody());
	}

	@Test
	public void testSetEaeEvaluation_updateExistingEae_ReturnCode200() throws EvaluationServiceException,
			EaeServiceException, IllegalStateException, SecurityException, SystemException {
		// Given
		String json = "{}";

		IEvaluationService evaluationServiceMock = Mockito.mock(IEvaluationService.class);
		
		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "evaluationService", evaluationServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		ReflectionTestUtils.setField(controller, "messageSource", messageSourceMock);

		// When
		ResponseEntity<String> result = controller.setEaeEvaluation(789, 0, json);

		// Then
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertFalse(result.hasBody());

		verify(evaluationServiceMock, times(1)).setEaeEvaluation(Mockito.eq(789),
				Mockito.any(EaeEvaluationDto.class));
	}

	@Test
	public void testSetEaeEvaluation_EaeCannotBeStarted_ReturnCode409() throws EvaluationServiceException,
			EaeServiceException, IllegalStateException, SecurityException, SystemException {
		// Given
		String json = "{}";

		IEvaluationService evaluationServiceMock = Mockito.mock(IEvaluationService.class);
		Mockito.doThrow(new EaeServiceException("message")).when(evaluationServiceMock).setEaeEvaluation(Mockito.anyInt(), Mockito.isA(EaeEvaluationDto.class));

		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "evaluationService", evaluationServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		ReflectionTestUtils.setField(controller, "messageSource", messageSourceMock);

		// When
		ResponseEntity<String> result = controller.setEaeEvaluation(789, 0, json);

		// Then
		assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
		assertEquals("message", result.getBody());

		verify(evaluationServiceMock, times(1)).setEaeEvaluation(Mockito.eq(789),
				Mockito.any(EaeEvaluationDto.class));
	}

	@Test
	public void testSetEaeEvaluation_EvaluationDataIsInvalid_ReturnCode409() throws EvaluationServiceException,
			EaeServiceException, IllegalStateException, SecurityException, SystemException {
		// Given
		String json = "{}";

		IEvaluationService evaluationServiceMock = Mockito.mock(IEvaluationService.class);
		Mockito.doThrow(new EvaluationServiceException("message")).when(evaluationServiceMock)
				.setEaeEvaluation(Mockito.eq(789), Mockito.any(EaeEvaluationDto.class));

		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "evaluationService", evaluationServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);

		// When
		ResponseEntity<String> result = controller.setEaeEvaluation(789, 0, json);

		// Then
		assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
		assertEquals("message", result.getBody());

		verify(evaluationServiceMock, times(1)).setEaeEvaluation(Mockito.eq(789),
				Mockito.any(EaeEvaluationDto.class));
	}

	@Test
	public void testGetEaeAutoEvaluation_agentDoesNotHaveRight_ReturnCode403() {
		// Given
		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		when(eaeSecurityProvider.checkEaeAndReadRight(789, 900000)).thenReturn(
				new ResponseEntity<String>(HttpStatus.FORBIDDEN));

		// When
		ResponseEntity<String> result = controller.getEaeAutoEvaluation(789, 900000);

		// Then
		assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
		assertFalse(result.hasBody());
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
		ResponseEntity<String> result = controller.getEaeAutoEvaluation(789, 900000);

		// Then
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertTrue(result.hasBody());

		verify(evaluationServiceMock, times(1)).getEaeAutoEvaluation(789);
	}

	@Test
	public void testSetEaeAutoEvaluation_evaluateurDoesNotHaveRight_ReturnCode403() throws IllegalStateException,
			SecurityException, SystemException {
		// Given
		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		when(eaeSecurityProvider.checkEaeAndWriteRight(789, 900000)).thenReturn(
				new ResponseEntity<String>(HttpStatus.FORBIDDEN));

		// When
		ResponseEntity<String> result = controller.setEaeAutoEvaluation(789, 900000, null);

		// Then
		assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
		assertFalse(result.hasBody());
	}

	@Test
	public void testSetEaeAutoEvaluation_updateExistingEae_ReturnCode200() throws EvaluationServiceException,
			EaeServiceException, IllegalStateException, SecurityException, SystemException {
		// Given
		String json = "{}";

		IEvaluationService evaluationServiceMock = Mockito.mock(IEvaluationService.class);

		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "evaluationService", evaluationServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		ReflectionTestUtils.setField(controller, "messageSource", messageSourceMock);

		// When
		ResponseEntity<String> result = controller.setEaeAutoEvaluation(789, 0, json);

		// Then
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertFalse(result.hasBody());

		verify(evaluationServiceMock, times(1)).setEaeAutoEvaluation(Mockito.eq(789),
				Mockito.any(EaeAutoEvaluationDto.class));
	}

	@Test
	public void testSetEaeAutoEvaluation_EaeCannotBeStarted_ReturnCode409() throws EvaluationServiceException,
			EaeServiceException, IllegalStateException, SecurityException, SystemException {
		// Given
		String json = "{}";

		IEvaluationService evaluationServiceMock = Mockito.mock(IEvaluationService.class);
		Mockito.doThrow(new EaeServiceException("message")).when(evaluationServiceMock).setEaeAutoEvaluation(Mockito.anyInt(), Mockito.isA(EaeAutoEvaluationDto.class));

		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "evaluationService", evaluationServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		ReflectionTestUtils.setField(controller, "messageSource", messageSourceMock);

		// When
		ResponseEntity<String> result = controller.setEaeAutoEvaluation(789, 0, json);

		// Then
		assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
		assertEquals("message", result.getBody());

		verify(evaluationServiceMock, times(1)).setEaeAutoEvaluation(Mockito.eq(789),
				Mockito.any(EaeAutoEvaluationDto.class));
	}

	@Test
	public void testGetEaePlanAction_agentDoesNotHaveRight_ReturnCode403() {
		// Given
		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		when(eaeSecurityProvider.checkEaeAndReadRight(789, 900000)).thenReturn(
				new ResponseEntity<String>(HttpStatus.FORBIDDEN));

		// When
		ResponseEntity<String> result = controller.getEaePlanAction(789, 900000);

		// Then
		assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
		assertFalse(result.hasBody());
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
		ResponseEntity<String> result = controller.getEaePlanAction(789, 900000);

		// Then
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertTrue(result.hasBody());

		verify(evaluationServiceMock, times(1)).getEaePlanAction(789);
	}

	@Test
	public void testSetEaePlanAction_evaluateurDoesNotHaveRight_ReturnCode403() throws IllegalStateException,
			SecurityException, SystemException {
		// Given
		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		when(eaeSecurityProvider.checkEaeAndWriteRight(789, 900000)).thenReturn(
				new ResponseEntity<String>(HttpStatus.FORBIDDEN));

		// When
		ResponseEntity<String> result = controller.setEaePlanAction(789, 900000, null);

		// Then
		assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
		assertFalse(result.hasBody());
	}

	@Test
	public void testSetEaePlanAction_updateExistingEae_ReturnCode200() throws EvaluationServiceException,
			EaeServiceException, IllegalStateException, SecurityException, SystemException {
		// Given
		String json = "{}";

		IEvaluationService evaluationServiceMock = Mockito.mock(IEvaluationService.class);

		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "evaluationService", evaluationServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		ReflectionTestUtils.setField(controller, "messageSource", messageSourceMock);

		// When
		ResponseEntity<String> result = controller.setEaePlanAction(789, 0, json);

		// Then
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertFalse(result.hasBody());

		verify(evaluationServiceMock, times(1)).setEaePlanAction(Mockito.eq(789),
				Mockito.any(EaePlanActionDto.class));
	}

	@Test
	public void testSetEaePlanAction_EaeCannotBeStarted_ReturnCode409() throws EvaluationServiceException,
			EaeServiceException, IllegalStateException, SecurityException, SystemException {
		// Given
		String json = "{}";

		IEvaluationService evaluationServiceMock = Mockito.mock(IEvaluationService.class);
		Mockito.doThrow(new EaeServiceException("message")).when(evaluationServiceMock).setEaePlanAction(Mockito.anyInt(), Mockito.isA(EaePlanActionDto.class));

		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "evaluationService", evaluationServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		ReflectionTestUtils.setField(controller, "messageSource", messageSourceMock);

		// When
		ResponseEntity<String> result = controller.setEaePlanAction(789, 0, json);

		// Then
		assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
		assertEquals("message", result.getBody());

		verify(evaluationServiceMock, times(1)).setEaePlanAction(Mockito.eq(789),
				Mockito.any(EaePlanActionDto.class));
	}

	@Test
	public void testGetEaeEvolution_agentDoesNotHaveRight_ReturnCode403() {
		// Given
		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		when(eaeSecurityProvider.checkEaeAndReadRight(789, 900000)).thenReturn(
				new ResponseEntity<String>(HttpStatus.FORBIDDEN));

		// When
		ResponseEntity<String> result = controller.getEaeEvolution(789, 900000);

		// Then
		assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
		assertFalse(result.hasBody());
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
		ResponseEntity<String> result = controller.getEaeEvolution(789, 900000);

		// Then
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertTrue(result.hasBody());

		verify(evaluationServiceMock, times(1)).getEaeEvolution(789);
	}

	@Test
	public void testSetEaeEvolution_evaluateurDoesNotHaveRight_ReturnCode403() throws IllegalStateException,
			SecurityException, SystemException {
		// Given
		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		when(eaeSecurityProvider.checkEaeAndWriteRight(789, 900000)).thenReturn(
				new ResponseEntity<String>(HttpStatus.FORBIDDEN));

		// When
		ResponseEntity<String> result = controller.setEaeEvolution(789, 900000, null);

		// Then
		assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
		assertFalse(result.hasBody());
	}

	@Test
	public void testSetEaeEvolution_updateExistingEae_ReturnCode200() throws EvaluationServiceException,
			EaeServiceException, IllegalStateException, SecurityException, SystemException {
		// Given
		String json = "{}";

		IEvaluationService evaluationServiceMock = Mockito.mock(IEvaluationService.class);

		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "evaluationService", evaluationServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		ReflectionTestUtils.setField(controller, "messageSource", messageSourceMock);

		// When
		ResponseEntity<String> result = controller.setEaeEvolution(789, 0, json);

		// Then
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertFalse(result.hasBody());

		verify(evaluationServiceMock, times(1)).setEaeEvolution(Mockito.eq(789),
				Mockito.any(EaeEvolutionDto.class));
	}

	@Test
	public void testSetEaeEvolution_EaeCannotBeStarted_ReturnCode409() throws EvaluationServiceException,
			EaeServiceException, IllegalStateException, SecurityException, SystemException {
		// Given
		String json = "{}";

		IEvaluationService evaluationServiceMock = Mockito.mock(IEvaluationService.class);
		Mockito.doThrow(new EaeServiceException("message")).when(evaluationServiceMock).setEaeEvolution(Mockito.anyInt(), Mockito.isA(EaeEvolutionDto.class));

		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "evaluationService", evaluationServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		ReflectionTestUtils.setField(controller, "messageSource", messageSourceMock);

		// When
		ResponseEntity<String> result = controller.setEaeEvolution(789, 0, json);

		// Then
		assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
		assertEquals("message", result.getBody());

		verify(evaluationServiceMock, times(1)).setEaeEvolution(Mockito.eq(789),
				Mockito.any(EaeEvolutionDto.class));
	}

	@Test
	public void testSetEaeEvolution_EaeEvolutionCannotBeSaved_ReturnCode409() throws EvaluationServiceException,
			EaeServiceException, IllegalStateException, SecurityException, SystemException {
		// Given
		String json = "{}";

		IEvaluationService evaluationServiceMock = Mockito.mock(IEvaluationService.class);
		Mockito.doThrow(new EvaluationServiceException("message")).when(evaluationServiceMock)
				.setEaeEvolution(Mockito.eq(789), Mockito.any(EaeEvolutionDto.class));

		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "evaluationService", evaluationServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);

		// When
		ResponseEntity<String> result = controller.setEaeEvolution(789, 0, json);

		// Then
		assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
		assertEquals("message", result.getBody());
	}
}
