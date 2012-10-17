package nc.noumea.mairie.sirh.eae.web.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.dto.EaeIdentificationDto;
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
}
