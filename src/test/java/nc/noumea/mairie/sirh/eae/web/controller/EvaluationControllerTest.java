package nc.noumea.mairie.sirh.eae.web.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.dto.EaeAppreciationsDto;
import nc.noumea.mairie.sirh.eae.dto.EaeFichePosteDto;
import nc.noumea.mairie.sirh.eae.dto.EaeResultatsDto;
import nc.noumea.mairie.sirh.eae.dto.identification.EaeIdentificationDto;
import nc.noumea.mairie.sirh.eae.service.EaeServiceException;
import nc.noumea.mairie.sirh.eae.service.EvaluationServiceException;
import nc.noumea.mairie.sirh.eae.service.IEaeService;
import nc.noumea.mairie.sirh.eae.service.IEvaluationService;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.staticmock.AnnotationDrivenStaticEntityMockingControl;
import org.springframework.mock.staticmock.MockStaticEntityMethods;
import org.springframework.test.util.ReflectionTestUtils;

@MockStaticEntityMethods
public class EvaluationControllerTest {

	@Test
	public void testGetEaeIdentifitcation_nonExistingEae_ReturnCode404() {
		// Given
		EvaluationController controller = new EvaluationController();
		
		// Mock the Eae find static method to return our null eae
		Eae eaeToReturn = null;

		Eae.findEae(789);
		AnnotationDrivenStaticEntityMockingControl.expectReturn(eaeToReturn);
		AnnotationDrivenStaticEntityMockingControl.playback();
		
		// When
		ResponseEntity<String> result = controller.getEaeIdentifitcation(789);
		
		// Then
		assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
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
		
		// When
		ResponseEntity<String> result = controller.getEaeIdentifitcation(789);
		
		// Then
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertTrue(result.hasBody());
		
		verify(evaluationServiceMock, times(1)).getEaeIdentification(eaeToReturn);
	}
	
	@Test
	public void testSetEaeIdentifitcation_nonExistingEae_ReturnCode404() {
		// Given
		EvaluationController controller = new EvaluationController();
		
		// Mock the Eae find static method to return our null eae
		Eae eaeToReturn = null;

		Eae.findEae(789);
		AnnotationDrivenStaticEntityMockingControl.expectReturn(eaeToReturn);
		AnnotationDrivenStaticEntityMockingControl.playback();
		
		// When
		ResponseEntity<String> result = controller.setEaeIdentifitcation(789, 0, null);
		
		// Then
		assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
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
		Eae eaeToReturn = new Eae();

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
		
		// When
		ResponseEntity<String> result = controller.setEaeIdentifitcation(789, 0, json);
			
		// Then
		assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
		assertEquals("message", result.getBody());
		
		verify(eaeServiceMock, times(1)).startEae(eaeToReturn);
		verify(evaluationServiceMock, times(0)).setEaeIdentification(Mockito.eq(eaeToReturn), Mockito.any(EaeIdentificationDto.class));
	}

	@Test
	public void testGetEaeFichePoste_nonExistingEae_ReturnCode404() {
		// Given
		EvaluationController controller = new EvaluationController();
		
		// Mock the Eae find static method to return our null eae
		Eae eaeToReturn = null;

		Eae.findEae(789);
		AnnotationDrivenStaticEntityMockingControl.expectReturn(eaeToReturn);
		AnnotationDrivenStaticEntityMockingControl.playback();
		
		// When
		ResponseEntity<String> result = controller.getEaeFichePoste(789);
		
		// Then
		assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
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
		
		// When
		ResponseEntity<String> result = controller.getEaeFichePoste(789);
		
		// Then
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertTrue(result.hasBody());
		
		verify(evaluationServiceMock, times(1)).getEaeFichePoste(eaeToReturn);
	}
	
	@Test
	public void testGetEaeResultats_nonExistingEae_ReturnCode404() {
		// Given
		EvaluationController controller = new EvaluationController();
		
		// Mock the Eae find static method to return our null eae
		Eae eaeToReturn = null;

		Eae.findEae(789);
		AnnotationDrivenStaticEntityMockingControl.expectReturn(eaeToReturn);
		AnnotationDrivenStaticEntityMockingControl.playback();
		
		// When
		ResponseEntity<String> result = controller.getEaeResultats(789);
		
		// Then
		assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
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
		
		// When
		ResponseEntity<String> result = controller.getEaeResultats(789);
		
		// Then
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertTrue(result.hasBody());
		
		verify(evaluationServiceMock, times(1)).getEaeResultats(eaeToReturn);
	}
	
	@Test
	public void testSetEaeResultats_nonExistingEae_ReturnCode404() {
		// Given
		EvaluationController controller = new EvaluationController();
		
		// Mock the Eae find static method to return our null eae
		Eae eaeToReturn = null;

		Eae.findEae(789);
		AnnotationDrivenStaticEntityMockingControl.expectReturn(eaeToReturn);
		AnnotationDrivenStaticEntityMockingControl.playback();
		
		// When
		ResponseEntity<String> result = controller.setEaeResultats(789, 0, null);
		
		// Then
		assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
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
		Eae eaeToReturn = new Eae();

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
		
		// When
		ResponseEntity<String> result = controller.setEaeResultats(789, 0, json);
			
		// Then
		assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
		assertEquals("message", result.getBody());
		
		verify(eaeServiceMock, times(1)).startEae(eaeToReturn);
		verify(evaluationServiceMock, times(0)).setEaeResultats(Mockito.eq(eaeToReturn), Mockito.any(EaeResultatsDto.class));
	}
	
	@Test
	public void testGetEaeAppreciations_nonExistingEae_ReturnCode404() {
		// Given
		EvaluationController controller = new EvaluationController();
		
		// Mock the Eae find static method to return our null eae
		Eae eaeToReturn = null;

		Eae.findEae(789);
		AnnotationDrivenStaticEntityMockingControl.expectReturn(eaeToReturn);
		AnnotationDrivenStaticEntityMockingControl.playback();
		
		// When
		ResponseEntity<String> result = controller.getEaeAppreciations(789);
		
		// Then
		assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
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
		
		// When
		ResponseEntity<String> result = controller.getEaeAppreciations(789);
		
		// Then
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertTrue(result.hasBody());
		
		verify(evaluationServiceMock, times(1)).getEaeAppreciations(eaeToReturn);
	}
	
	@Test
	public void testSetEaeAppreciations_nonExistingEae_ReturnCode404() {
		// Given
		EvaluationController controller = new EvaluationController();
		
		// Mock the Eae find static method to return our null eae
		Eae eaeToReturn = null;

		Eae.findEae(789);
		AnnotationDrivenStaticEntityMockingControl.expectReturn(eaeToReturn);
		AnnotationDrivenStaticEntityMockingControl.playback();
		
		// When
		ResponseEntity<String> result = controller.setEaeAppreciations(789, 0, null);
		
		// Then
		assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
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
		Eae eaeToReturn = new Eae();

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
		
		// When
		ResponseEntity<String> result = controller.setEaeAppreciations(789, 0, json);
			
		// Then
		assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
		assertEquals("message", result.getBody());
		
		verify(eaeServiceMock, times(1)).startEae(eaeToReturn);
		verify(evaluationServiceMock, times(0)).setEaeAppreciations(Mockito.eq(eaeToReturn), Mockito.any(EaeAppreciationsDto.class));
	}
}