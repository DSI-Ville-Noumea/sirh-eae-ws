package nc.noumea.mairie.sirh.eae.web.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.SystemException;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.dto.EaeAppreciationsDto;
import nc.noumea.mairie.sirh.eae.dto.EaeAutoEvaluationDto;
import nc.noumea.mairie.sirh.eae.dto.EaeEvaluationDto;
import nc.noumea.mairie.sirh.eae.dto.EaeEvolutionDto;
import nc.noumea.mairie.sirh.eae.dto.EaeFichePosteDto;
import nc.noumea.mairie.sirh.eae.dto.EaeResultatsDto;
import nc.noumea.mairie.sirh.eae.dto.identification.EaeIdentificationDto;
import nc.noumea.mairie.sirh.eae.dto.planAction.EaePlanActionDto;
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
import org.springframework.mock.staticmock.AnnotationDrivenStaticEntityMockingControl;
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
		when(eaeSecurityProvider.checkEaeAndReadRight(789, 900000)).thenReturn(new ResponseEntity<String>(HttpStatus.FORBIDDEN));

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

		// Mock the Eae find static method to return our null eae
		Eae eaeToReturn = new Eae();

		Eae.findEae(789);
		AnnotationDrivenStaticEntityMockingControl.expectReturn(eaeToReturn);
		AnnotationDrivenStaticEntityMockingControl.playback();

		IEvaluationService evaluationServiceMock = Mockito.mock(IEvaluationService.class);
		when(evaluationServiceMock.getEaeIdentification(eaeToReturn)).thenReturn(dto);

		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "evaluationService", evaluationServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);

		// When
		ResponseEntity<String> result = controller.getEaeIdentifitcation(789, 900000);

		// Then
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertTrue(result.hasBody());

		verify(evaluationServiceMock, times(1)).getEaeIdentification(eaeToReturn);
	}

	@Test
	public void testSetEaeIdentifitcation_evaluateurDoesNotHaveRight_ReturnCode403() {
		// Given
		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		when(eaeSecurityProvider.checkEaeAndWriteRight(789, 9000000)).thenReturn(new ResponseEntity<String>(HttpStatus.FORBIDDEN));

		// When
		ResponseEntity<String> result = controller.setEaeIdentifitcation(789, 9000000, null);

		// Then
		assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
		assertFalse(result.hasBody());
	}

	@Test
	public void testSetEaeIdentifitcation_updateExistingEae_ReturnCode200() throws EvaluationServiceException, EaeServiceException {
		// Given
		// Mock the Eae find static method to return our null eae
		Eae eaeToReturn = new Eae();

		Eae.findEae(789);
		AnnotationDrivenStaticEntityMockingControl.expectReturn(eaeToReturn);
		AnnotationDrivenStaticEntityMockingControl.playback();

		String json = "{}";

		IEvaluationService evaluationServiceMock = Mockito.mock(IEvaluationService.class);
		IEaeService eaeServiceMock = Mockito.mock(IEaeService.class);

		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "evaluationService", evaluationServiceMock);
		ReflectionTestUtils.setField(controller, "eaeService", eaeServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		ReflectionTestUtils.setField(controller, "messageSource", messageSourceMock);

		// When
		ResponseEntity<String> result = controller.setEaeIdentifitcation(789, 0, json);

		// Then
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertFalse(result.hasBody());

		verify(eaeServiceMock, times(1)).startEae(eaeToReturn);
		verify(evaluationServiceMock, times(1)).setEaeIdentification(Mockito.eq(eaeToReturn), Mockito.any(EaeIdentificationDto.class));
	}

	@Test
	public void testSetEaeIdentifitcation_EaeCannotBeStarted_ReturnCode409() throws EvaluationServiceException, EaeServiceException {
		// Given
		// Mock the Eae find static method to return our null eae
		Eae eaeToReturn = spy(new Eae());
		Mockito.doNothing().when(eaeToReturn).clear();

		Eae.findEae(789);
		AnnotationDrivenStaticEntityMockingControl.expectReturn(eaeToReturn);
		AnnotationDrivenStaticEntityMockingControl.playback();

		String json = "{}";

		IEvaluationService evaluationServiceMock = Mockito.mock(IEvaluationService.class);
		IEaeService eaeServiceMock = Mockito.mock(IEaeService.class);
		Mockito.doThrow(new EaeServiceException("message")).when(eaeServiceMock).startEae(eaeToReturn);

		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "evaluationService", evaluationServiceMock);
		ReflectionTestUtils.setField(controller, "eaeService", eaeServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);

		// When
		ResponseEntity<String> result = controller.setEaeIdentifitcation(789, 0, json);

		// Then
		assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
		assertEquals("message", result.getBody());

		verify(eaeServiceMock, times(1)).startEae(eaeToReturn);
		verify(evaluationServiceMock, times(0)).setEaeIdentification(Mockito.eq(eaeToReturn), Mockito.any(EaeIdentificationDto.class));
	}

	@Test
	public void testGetEaeFichePoste_agentDoesNotHaveright_ReturnCode403() {
		// Given
		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		when(eaeSecurityProvider.checkEaeAndReadRight(789, 900000)).thenReturn(new ResponseEntity<String>(HttpStatus.FORBIDDEN));

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

		// Mock the Eae find static method to return our null eae
		Eae eaeToReturn = new Eae();

		Eae.findEae(789);
		AnnotationDrivenStaticEntityMockingControl.expectReturn(eaeToReturn);
		AnnotationDrivenStaticEntityMockingControl.playback();

		IEvaluationService evaluationServiceMock = Mockito.mock(IEvaluationService.class);
		when(evaluationServiceMock.getEaeFichePoste(eaeToReturn)).thenReturn(dtos);

		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "evaluationService", evaluationServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);

		// When
		ResponseEntity<String> result = controller.getEaeFichePoste(789, 900000);

		// Then
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertTrue(result.hasBody());

		verify(evaluationServiceMock, times(1)).getEaeFichePoste(eaeToReturn);
	}

	@Test
	public void testGetEaeResultats_agentDoesNotHaveRight_ReturnCode403() {
		// Given
		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		when(eaeSecurityProvider.checkEaeAndReadRight(789, 900000)).thenReturn(new ResponseEntity<String>(HttpStatus.FORBIDDEN));

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

		// Mock the Eae find static method to return our null eae
		Eae eaeToReturn = new Eae();

		Eae.findEae(789);
		AnnotationDrivenStaticEntityMockingControl.expectReturn(eaeToReturn);
		AnnotationDrivenStaticEntityMockingControl.playback();

		IEvaluationService evaluationServiceMock = Mockito.mock(IEvaluationService.class);
		when(evaluationServiceMock.getEaeResultats(eaeToReturn)).thenReturn(dto);

		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "evaluationService", evaluationServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);

		// When
		ResponseEntity<String> result = controller.getEaeResultats(789, 900000);

		// Then
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertTrue(result.hasBody());

		verify(evaluationServiceMock, times(1)).getEaeResultats(eaeToReturn);
	}

	@Test
	public void testSetEaeResultats_evaluateurDoesNotHaveRight_ReturnCode403() {
		// Given
		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		when(eaeSecurityProvider.checkEaeAndWriteRight(789, 900000)).thenReturn(new ResponseEntity<String>(HttpStatus.FORBIDDEN));

		// When
		ResponseEntity<String> result = controller.setEaeResultats(789, 900000, null);

		// Then
		assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
		assertFalse(result.hasBody());
	}

	@Test
	public void testSetEaeResultats_updateExistingEae_ReturnCode200() throws EvaluationServiceException, EaeServiceException {
		// Given
		// Mock the Eae find static method to return our null eae
		Eae eaeToReturn = new Eae();

		Eae.findEae(789);
		AnnotationDrivenStaticEntityMockingControl.expectReturn(eaeToReturn);
		AnnotationDrivenStaticEntityMockingControl.playback();

		String json = "{}";

		IEvaluationService evaluationServiceMock = Mockito.mock(IEvaluationService.class);
		IEaeService eaeServiceMock = Mockito.mock(IEaeService.class);

		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "evaluationService", evaluationServiceMock);
		ReflectionTestUtils.setField(controller, "eaeService", eaeServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		ReflectionTestUtils.setField(controller, "messageSource", messageSourceMock);

		// When
		ResponseEntity<String> result = controller.setEaeResultats(789, 0, json);

		// Then
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertFalse(result.hasBody());

		verify(eaeServiceMock, times(1)).startEae(eaeToReturn);
		verify(evaluationServiceMock, times(1)).setEaeResultats(Mockito.eq(eaeToReturn), Mockito.any(EaeResultatsDto.class));
	}

	@Test
	public void testSetEaeResultats_EaeCannotBeStarted_ReturnCode409() throws EvaluationServiceException, EaeServiceException {
		// Given
		// Mock the Eae find static method to return our null eae
		Eae eaeToReturn = spy(new Eae());
		Mockito.doNothing().when(eaeToReturn).clear();

		Eae.findEae(789);
		AnnotationDrivenStaticEntityMockingControl.expectReturn(eaeToReturn);
		AnnotationDrivenStaticEntityMockingControl.playback();

		String json = "{}";

		IEvaluationService evaluationServiceMock = Mockito.mock(IEvaluationService.class);
		IEaeService eaeServiceMock = Mockito.mock(IEaeService.class);
		Mockito.doThrow(new EaeServiceException("message")).when(eaeServiceMock).startEae(eaeToReturn);

		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "evaluationService", evaluationServiceMock);
		ReflectionTestUtils.setField(controller, "eaeService", eaeServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);

		// When
		ResponseEntity<String> result = controller.setEaeResultats(789, 0, json);

		// Then
		assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
		assertEquals("message", result.getBody());

		verify(eaeServiceMock, times(1)).startEae(eaeToReturn);
		verify(evaluationServiceMock, times(0)).setEaeResultats(Mockito.eq(eaeToReturn), Mockito.any(EaeResultatsDto.class));
	}

	@Test
	public void testGetEaeAppreciations_agentDoesNotHaveRight_ReturnCode403() {
		// Given
		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		when(eaeSecurityProvider.checkEaeAndReadRight(789, 900000)).thenReturn(new ResponseEntity<String>(HttpStatus.FORBIDDEN));

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

		// Mock the Eae find static method to return our null eae
		Eae eaeToReturn = new Eae();

		Eae.findEae(789);
		AnnotationDrivenStaticEntityMockingControl.expectReturn(eaeToReturn);
		AnnotationDrivenStaticEntityMockingControl.playback();

		IEvaluationService evaluationServiceMock = Mockito.mock(IEvaluationService.class);
		when(evaluationServiceMock.getEaeAppreciations(eaeToReturn)).thenReturn(dto);

		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "evaluationService", evaluationServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);

		// When
		ResponseEntity<String> result = controller.getEaeAppreciations(789, 900000, null);

		// Then
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertTrue(result.hasBody());

		verify(evaluationServiceMock, times(1)).getEaeAppreciations(eaeToReturn);
	}

	@Test
	public void testSetEaeAppreciations_evaluateurDoesNotHaveRight_ReturnCode403() {
		// Given
		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		when(eaeSecurityProvider.checkEaeAndWriteRight(789, 900000)).thenReturn(new ResponseEntity<String>(HttpStatus.FORBIDDEN));

		// When
		ResponseEntity<String> result = controller.setEaeAppreciations(789, 900000, null);

		// Then
		assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
		assertFalse(result.hasBody());
	}

	@Test
	public void testSetEaeAppreciations_updateExistingEae_ReturnCode200() throws EvaluationServiceException, EaeServiceException {
		// Given
		// Mock the Eae find static method to return our null eae
		Eae eaeToReturn = new Eae();

		Eae.findEae(789);
		AnnotationDrivenStaticEntityMockingControl.expectReturn(eaeToReturn);
		AnnotationDrivenStaticEntityMockingControl.playback();

		String json = "{}";

		IEvaluationService evaluationServiceMock = Mockito.mock(IEvaluationService.class);
		IEaeService eaeServiceMock = Mockito.mock(IEaeService.class);

		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "evaluationService", evaluationServiceMock);
		ReflectionTestUtils.setField(controller, "eaeService", eaeServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		ReflectionTestUtils.setField(controller, "messageSource", messageSourceMock);

		// When
		ResponseEntity<String> result = controller.setEaeAppreciations(789, 0, json);

		// Then
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertFalse(result.hasBody());

		verify(eaeServiceMock, times(1)).startEae(eaeToReturn);
		verify(evaluationServiceMock, times(1)).setEaeAppreciations(Mockito.eq(eaeToReturn), Mockito.any(EaeAppreciationsDto.class));
	}

	@Test
	public void testSetEaeAppreciations_EaeCannotBeStarted_ReturnCode409() throws EvaluationServiceException, EaeServiceException {
		// Given
		// Mock the Eae find static method to return our null eae
		Eae eaeToReturn = spy(new Eae());
		Mockito.doNothing().when(eaeToReturn).clear();

		Eae.findEae(789);
		AnnotationDrivenStaticEntityMockingControl.expectReturn(eaeToReturn);
		AnnotationDrivenStaticEntityMockingControl.playback();

		String json = "{}";

		IEvaluationService evaluationServiceMock = Mockito.mock(IEvaluationService.class);
		IEaeService eaeServiceMock = Mockito.mock(IEaeService.class);
		Mockito.doThrow(new EaeServiceException("message")).when(eaeServiceMock).startEae(eaeToReturn);

		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "evaluationService", evaluationServiceMock);
		ReflectionTestUtils.setField(controller, "eaeService", eaeServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);

		// When
		ResponseEntity<String> result = controller.setEaeAppreciations(789, 0, json);

		// Then
		assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
		assertEquals("message", result.getBody());

		verify(eaeServiceMock, times(1)).startEae(eaeToReturn);
		verify(evaluationServiceMock, times(0)).setEaeAppreciations(Mockito.eq(eaeToReturn), Mockito.any(EaeAppreciationsDto.class));
	}

	@Test
	public void testGetEaeEvaluation_agentDoesNotHaveRight_ReturnCode403() {
		// Given
		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		when(eaeSecurityProvider.checkEaeAndReadRight(789, 900000)).thenReturn(new ResponseEntity<String>(HttpStatus.FORBIDDEN));

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

		// Mock the Eae find static method to return our null eae
		Eae eaeToReturn = new Eae();

		Eae.findEae(789);
		AnnotationDrivenStaticEntityMockingControl.expectReturn(eaeToReturn);
		AnnotationDrivenStaticEntityMockingControl.playback();

		IEvaluationService evaluationServiceMock = Mockito.mock(IEvaluationService.class);
		when(evaluationServiceMock.getEaeEvaluation(eaeToReturn)).thenReturn(dto);

		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "evaluationService", evaluationServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);

		// When
		ResponseEntity<String> result = controller.getEaeEvaluation(789, 900000);

		// Then
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertTrue(result.hasBody());

		verify(evaluationServiceMock, times(1)).getEaeEvaluation(eaeToReturn);
	}

	@Test
	public void testSetEaeEvaluation_evaluateurDoesNotHaveRight_ReturnCode403() throws IllegalStateException, SecurityException, SystemException {
		// Given
		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		when(eaeSecurityProvider.checkEaeAndWriteRight(789, 900000)).thenReturn(new ResponseEntity<String>(HttpStatus.FORBIDDEN));

		// When
		ResponseEntity<String> result = controller.setEaeEvaluation(789, 900000, null);

		// Then
		assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
		assertFalse(result.hasBody());
	}

	@Test
	public void testSetEaeEvaluation_updateExistingEae_ReturnCode200() throws EvaluationServiceException, EaeServiceException, IllegalStateException,
			SecurityException, SystemException {
		// Given
		// Mock the Eae find static method to return our null eae
		Eae eaeToReturn = spy(new Eae());
		Mockito.doNothing().when(eaeToReturn).flush();

		Eae.findEae(789);
		AnnotationDrivenStaticEntityMockingControl.expectReturn(eaeToReturn);
		AnnotationDrivenStaticEntityMockingControl.playback();

		String json = "{}";

		IEvaluationService evaluationServiceMock = Mockito.mock(IEvaluationService.class);
		IEaeService eaeServiceMock = Mockito.mock(IEaeService.class);

		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "evaluationService", evaluationServiceMock);
		ReflectionTestUtils.setField(controller, "eaeService", eaeServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		ReflectionTestUtils.setField(controller, "messageSource", messageSourceMock);

		// When
		ResponseEntity<String> result = controller.setEaeEvaluation(789, 0, json);

		// Then
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertFalse(result.hasBody());

		verify(eaeServiceMock, times(1)).startEae(eaeToReturn);
		verify(evaluationServiceMock, times(1)).setEaeEvaluation(Mockito.eq(eaeToReturn), Mockito.any(EaeEvaluationDto.class));
	}

	@Test
	public void testSetEaeEvaluation_EaeCannotBeStarted_ReturnCode409() throws EvaluationServiceException, EaeServiceException,
			IllegalStateException, SecurityException, SystemException {
		// Given
		// Mock the Eae find static method to return our null eae
		Eae eaeToReturn = spy(new Eae());
		Mockito.doNothing().when(eaeToReturn).clear();

		Eae.findEae(789);
		AnnotationDrivenStaticEntityMockingControl.expectReturn(eaeToReturn);
		AnnotationDrivenStaticEntityMockingControl.playback();

		String json = "{}";

		IEvaluationService evaluationServiceMock = Mockito.mock(IEvaluationService.class);
		IEaeService eaeServiceMock = Mockito.mock(IEaeService.class);
		Mockito.doThrow(new EaeServiceException("message")).when(eaeServiceMock).startEae(eaeToReturn);

		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "evaluationService", evaluationServiceMock);
		ReflectionTestUtils.setField(controller, "eaeService", eaeServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);

		// When
		ResponseEntity<String> result = controller.setEaeEvaluation(789, 0, json);

		// Then
		assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
		assertEquals("message", result.getBody());

		verify(eaeServiceMock, times(1)).startEae(eaeToReturn);
		verify(evaluationServiceMock, times(0)).setEaeEvaluation(Mockito.eq(eaeToReturn), Mockito.any(EaeEvaluationDto.class));
	}

	@Test
	public void testSetEaeEvaluation_EvaluationDataIsInvalid_ReturnCode409() throws EvaluationServiceException, EaeServiceException,
			IllegalStateException, SecurityException, SystemException {
		// Given

		// Mock the Eae find static method to return our null eae
		Eae eaeToReturn = spy(new Eae());
		Mockito.doNothing().when(eaeToReturn).clear();

		Eae.findEae(789);
		AnnotationDrivenStaticEntityMockingControl.expectReturn(eaeToReturn);
		AnnotationDrivenStaticEntityMockingControl.playback();

		String json = "{}";

		IEvaluationService evaluationServiceMock = Mockito.mock(IEvaluationService.class);
		Mockito.doThrow(new EvaluationServiceException("message")).when(evaluationServiceMock)
				.setEaeEvaluation(Mockito.eq(eaeToReturn), Mockito.any(EaeEvaluationDto.class));
		IEaeService eaeServiceMock = Mockito.mock(IEaeService.class);

		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "evaluationService", evaluationServiceMock);
		ReflectionTestUtils.setField(controller, "eaeService", eaeServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);

		// When
		ResponseEntity<String> result = controller.setEaeEvaluation(789, 0, json);

		// Then
		assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
		assertEquals("message", result.getBody());

		verify(eaeServiceMock, times(1)).startEae(eaeToReturn);
		verify(evaluationServiceMock, times(1)).setEaeEvaluation(Mockito.eq(eaeToReturn), Mockito.any(EaeEvaluationDto.class));
	}

	@Test
	public void testGetEaeAutoEvaluation_agentDoesNotHaveRight_ReturnCode403() {
		// Given
		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		when(eaeSecurityProvider.checkEaeAndReadRight(789, 900000)).thenReturn(new ResponseEntity<String>(HttpStatus.FORBIDDEN));

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

		// Mock the Eae find static method to return our null eae
		Eae eaeToReturn = new Eae();

		Eae.findEae(789);
		AnnotationDrivenStaticEntityMockingControl.expectReturn(eaeToReturn);
		AnnotationDrivenStaticEntityMockingControl.playback();

		IEvaluationService evaluationServiceMock = Mockito.mock(IEvaluationService.class);
		when(evaluationServiceMock.getEaeAutoEvaluation(eaeToReturn)).thenReturn(dto);

		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "evaluationService", evaluationServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);

		// When
		ResponseEntity<String> result = controller.getEaeAutoEvaluation(789, 900000);

		// Then
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertTrue(result.hasBody());

		verify(evaluationServiceMock, times(1)).getEaeAutoEvaluation(eaeToReturn);
	}

	@Test
	public void testSetEaeAutoEvaluation_evaluateurDoesNotHaveRight_ReturnCode403() throws IllegalStateException, SecurityException, SystemException {
		// Given
		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		when(eaeSecurityProvider.checkEaeAndWriteRight(789, 900000)).thenReturn(new ResponseEntity<String>(HttpStatus.FORBIDDEN));

		// When
		ResponseEntity<String> result = controller.setEaeAutoEvaluation(789, 900000, null);

		// Then
		assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
		assertFalse(result.hasBody());
	}

	@Test
	public void testSetEaeAutoEvaluation_updateExistingEae_ReturnCode200() throws EvaluationServiceException, EaeServiceException,
			IllegalStateException, SecurityException, SystemException {
		// Given
		// Mock the Eae find static method to return our null eae
		Eae eaeToReturn = spy(new Eae());
		Mockito.doNothing().when(eaeToReturn).flush();

		Eae.findEae(789);
		AnnotationDrivenStaticEntityMockingControl.expectReturn(eaeToReturn);
		AnnotationDrivenStaticEntityMockingControl.playback();

		String json = "{}";

		IEvaluationService evaluationServiceMock = Mockito.mock(IEvaluationService.class);
		IEaeService eaeServiceMock = Mockito.mock(IEaeService.class);

		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "evaluationService", evaluationServiceMock);
		ReflectionTestUtils.setField(controller, "eaeService", eaeServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		ReflectionTestUtils.setField(controller, "messageSource", messageSourceMock);

		// When
		ResponseEntity<String> result = controller.setEaeAutoEvaluation(789, 0, json);

		// Then
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertFalse(result.hasBody());

		verify(eaeServiceMock, times(1)).startEae(eaeToReturn);
		verify(evaluationServiceMock, times(1)).setEaeAutoEvaluation(Mockito.eq(eaeToReturn), Mockito.any(EaeAutoEvaluationDto.class));
	}

	@Test
	public void testSetEaeAutoEvaluation_EaeCannotBeStarted_ReturnCode409() throws EvaluationServiceException, EaeServiceException,
			IllegalStateException, SecurityException, SystemException {
		// Given
		// Mock the Eae find static method to return our null eae
		Eae eaeToReturn = spy(new Eae());
		Mockito.doNothing().when(eaeToReturn).clear();

		Eae.findEae(789);
		AnnotationDrivenStaticEntityMockingControl.expectReturn(eaeToReturn);
		AnnotationDrivenStaticEntityMockingControl.playback();

		String json = "{}";

		IEvaluationService evaluationServiceMock = Mockito.mock(IEvaluationService.class);
		IEaeService eaeServiceMock = Mockito.mock(IEaeService.class);
		Mockito.doThrow(new EaeServiceException("message")).when(eaeServiceMock).startEae(eaeToReturn);

		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "evaluationService", evaluationServiceMock);
		ReflectionTestUtils.setField(controller, "eaeService", eaeServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);

		// When
		ResponseEntity<String> result = controller.setEaeAutoEvaluation(789, 0, json);

		// Then
		assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
		assertEquals("message", result.getBody());

		verify(eaeServiceMock, times(1)).startEae(eaeToReturn);
		verify(evaluationServiceMock, times(0)).setEaeAutoEvaluation(Mockito.eq(eaeToReturn), Mockito.any(EaeAutoEvaluationDto.class));
	}

	@Test
	public void testGetEaePlanAction_agentDoesNotHaveRight_ReturnCode403() {
		// Given
		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		when(eaeSecurityProvider.checkEaeAndReadRight(789, 900000)).thenReturn(new ResponseEntity<String>(HttpStatus.FORBIDDEN));

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

		// Mock the Eae find static method to return our null eae
		Eae eaeToReturn = new Eae();

		Eae.findEae(789);
		AnnotationDrivenStaticEntityMockingControl.expectReturn(eaeToReturn);
		AnnotationDrivenStaticEntityMockingControl.playback();

		IEvaluationService evaluationServiceMock = Mockito.mock(IEvaluationService.class);
		when(evaluationServiceMock.getEaePlanAction(eaeToReturn)).thenReturn(dto);

		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "evaluationService", evaluationServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);

		// When
		ResponseEntity<String> result = controller.getEaePlanAction(789, 900000);

		// Then
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertTrue(result.hasBody());

		verify(evaluationServiceMock, times(1)).getEaePlanAction(eaeToReturn);
	}

	@Test
	public void testSetEaePlanAction_evaluateurDoesNotHaveRight_ReturnCode403() throws IllegalStateException, SecurityException, SystemException {
		// Given
		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		when(eaeSecurityProvider.checkEaeAndWriteRight(789, 900000)).thenReturn(new ResponseEntity<String>(HttpStatus.FORBIDDEN));

		// When
		ResponseEntity<String> result = controller.setEaePlanAction(789, 900000, null);

		// Then
		assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
		assertFalse(result.hasBody());
	}

	@Test
	public void testSetEaePlanAction_updateExistingEae_ReturnCode200() throws EvaluationServiceException, EaeServiceException, IllegalStateException,
			SecurityException, SystemException {
		// Given
		// Mock the Eae find static method to return our null eae
		Eae eaeToReturn = spy(new Eae());
		Mockito.doNothing().when(eaeToReturn).flush();

		Eae.findEae(789);
		AnnotationDrivenStaticEntityMockingControl.expectReturn(eaeToReturn);
		AnnotationDrivenStaticEntityMockingControl.playback();

		String json = "{}";

		IEvaluationService evaluationServiceMock = Mockito.mock(IEvaluationService.class);
		IEaeService eaeServiceMock = Mockito.mock(IEaeService.class);

		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "evaluationService", evaluationServiceMock);
		ReflectionTestUtils.setField(controller, "eaeService", eaeServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		ReflectionTestUtils.setField(controller, "messageSource", messageSourceMock);

		// When
		ResponseEntity<String> result = controller.setEaePlanAction(789, 0, json);

		// Then
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertFalse(result.hasBody());

		verify(eaeServiceMock, times(1)).startEae(eaeToReturn);
		verify(evaluationServiceMock, times(1)).setEaePlanAction(Mockito.eq(eaeToReturn), Mockito.any(EaePlanActionDto.class));
	}

	@Test
	public void testSetEaePlanAction_EaeCannotBeStarted_ReturnCode409() throws EvaluationServiceException, EaeServiceException,
			IllegalStateException, SecurityException, SystemException {
		// Given
		// Mock the Eae find static method to return our null eae
		Eae eaeToReturn = spy(new Eae());
		Mockito.doNothing().when(eaeToReturn).clear();

		Eae.findEae(789);
		AnnotationDrivenStaticEntityMockingControl.expectReturn(eaeToReturn);
		AnnotationDrivenStaticEntityMockingControl.playback();

		String json = "{}";

		IEvaluationService evaluationServiceMock = Mockito.mock(IEvaluationService.class);
		IEaeService eaeServiceMock = Mockito.mock(IEaeService.class);
		Mockito.doThrow(new EaeServiceException("message")).when(eaeServiceMock).startEae(eaeToReturn);

		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "evaluationService", evaluationServiceMock);
		ReflectionTestUtils.setField(controller, "eaeService", eaeServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);

		// When
		ResponseEntity<String> result = controller.setEaePlanAction(789, 0, json);

		// Then
		assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
		assertEquals("message", result.getBody());

		verify(eaeServiceMock, times(1)).startEae(eaeToReturn);
		verify(evaluationServiceMock, times(0)).setEaePlanAction(Mockito.eq(eaeToReturn), Mockito.any(EaePlanActionDto.class));
	}

	@Test
	public void testGetEaeEvolution_agentDoesNotHaveRight_ReturnCode403() {
		// Given
		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		when(eaeSecurityProvider.checkEaeAndReadRight(789, 900000)).thenReturn(new ResponseEntity<String>(HttpStatus.FORBIDDEN));

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

		// Mock the Eae find static method to return our null eae
		Eae eaeToReturn = new Eae();

		Eae.findEae(789);
		AnnotationDrivenStaticEntityMockingControl.expectReturn(eaeToReturn);
		AnnotationDrivenStaticEntityMockingControl.playback();

		IEvaluationService evaluationServiceMock = Mockito.mock(IEvaluationService.class);
		when(evaluationServiceMock.getEaeEvolution(eaeToReturn)).thenReturn(dto);

		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "evaluationService", evaluationServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);

		// When
		ResponseEntity<String> result = controller.getEaeEvolution(789, 900000);

		// Then
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertTrue(result.hasBody());

		verify(evaluationServiceMock, times(1)).getEaeEvolution(eaeToReturn);
	}

	@Test
	public void testSetEaeEvolution_evaluateurDoesNotHaveRight_ReturnCode403() throws IllegalStateException, SecurityException, SystemException {
		// Given
		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		when(eaeSecurityProvider.checkEaeAndWriteRight(789, 900000)).thenReturn(new ResponseEntity<String>(HttpStatus.FORBIDDEN));

		// When
		ResponseEntity<String> result = controller.setEaeEvolution(789, 900000, null);

		// Then
		assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
		assertFalse(result.hasBody());
	}

	@Test
	public void testSetEaeEvolution_updateExistingEae_ReturnCode200() throws EvaluationServiceException, EaeServiceException, IllegalStateException,
			SecurityException, SystemException {
		// Given
		// Mock the Eae find static method to return our null eae
		Eae eaeToReturn = spy(new Eae());
		Mockito.doNothing().when(eaeToReturn).flush();

		Eae.findEae(789);
		AnnotationDrivenStaticEntityMockingControl.expectReturn(eaeToReturn);
		AnnotationDrivenStaticEntityMockingControl.playback();

		String json = "{}";

		IEvaluationService evaluationServiceMock = Mockito.mock(IEvaluationService.class);
		IEaeService eaeServiceMock = Mockito.mock(IEaeService.class);

		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "evaluationService", evaluationServiceMock);
		ReflectionTestUtils.setField(controller, "eaeService", eaeServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);
		ReflectionTestUtils.setField(controller, "messageSource", messageSourceMock);

		// When
		ResponseEntity<String> result = controller.setEaeEvolution(789, 0, json);

		// Then
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertFalse(result.hasBody());

		verify(eaeServiceMock, times(1)).startEae(eaeToReturn);
		verify(evaluationServiceMock, times(1)).setEaeEvolution(Mockito.eq(eaeToReturn), Mockito.any(EaeEvolutionDto.class));
	}

	@Test
	public void testSetEaeEvolution_EaeCannotBeStarted_ReturnCode409() throws EvaluationServiceException, EaeServiceException, IllegalStateException,
			SecurityException, SystemException {
		// Given
		// Mock the Eae find static method to return our null eae
		Eae eaeToReturn = spy(new Eae());
		Mockito.doNothing().when(eaeToReturn).clear();

		Eae.findEae(789);
		AnnotationDrivenStaticEntityMockingControl.expectReturn(eaeToReturn);
		AnnotationDrivenStaticEntityMockingControl.playback();

		String json = "{}";

		IEvaluationService evaluationServiceMock = Mockito.mock(IEvaluationService.class);
		IEaeService eaeServiceMock = Mockito.mock(IEaeService.class);
		Mockito.doThrow(new EaeServiceException("message")).when(eaeServiceMock).startEae(eaeToReturn);

		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "evaluationService", evaluationServiceMock);
		ReflectionTestUtils.setField(controller, "eaeService", eaeServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);

		// When
		ResponseEntity<String> result = controller.setEaeEvolution(789, 0, json);

		// Then
		assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
		assertEquals("message", result.getBody());

		verify(eaeServiceMock, times(1)).startEae(eaeToReturn);
		verify(evaluationServiceMock, times(0)).setEaeEvolution(Mockito.eq(eaeToReturn), Mockito.any(EaeEvolutionDto.class));
	}

	@Test
	public void testSetEaeEvolution_EaeEvolutionCannotBeSaved_ReturnCode409() throws EvaluationServiceException, EaeServiceException,
			IllegalStateException, SecurityException, SystemException {
		// Given
		// Mock the Eae find static method to return our null eae
		Eae eaeToReturn = spy(new Eae());
		Mockito.doNothing().when(eaeToReturn).clear();

		Eae.findEae(789);
		AnnotationDrivenStaticEntityMockingControl.expectReturn(eaeToReturn);
		AnnotationDrivenStaticEntityMockingControl.playback();

		String json = "{}";

		IEvaluationService evaluationServiceMock = Mockito.mock(IEvaluationService.class);
		Mockito.doThrow(new EvaluationServiceException("message")).when(evaluationServiceMock)
				.setEaeEvolution(Mockito.eq(eaeToReturn), Mockito.any(EaeEvolutionDto.class));
		IEaeService eaeServiceMock = Mockito.mock(IEaeService.class);

		EvaluationController controller = new EvaluationController();
		ReflectionTestUtils.setField(controller, "evaluationService", evaluationServiceMock);
		ReflectionTestUtils.setField(controller, "eaeService", eaeServiceMock);
		ReflectionTestUtils.setField(controller, "eaeSecurityProvider", eaeSecurityProvider);

		// When
		ResponseEntity<String> result = controller.setEaeEvolution(789, 0, json);

		// Then
		assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
		assertEquals("message", result.getBody());

		verify(eaeServiceMock, times(1)).startEae(eaeToReturn);
	}
}
