package nc.noumea.mairie.sirh.eae.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluateur;
import nc.noumea.mairie.sirh.eae.domain.EaeFichePoste;
import nc.noumea.mairie.sirh.eae.security.EaeSecurityProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.staticmock.AnnotationDrivenStaticEntityMockingControl;
import org.springframework.mock.staticmock.MockStaticEntityMethods;
import org.springframework.test.util.ReflectionTestUtils;

@MockStaticEntityMethods
public class EaeSecurityProviderTest {

	IAgentMatriculeConverterService idConverterMock;
	ISirhWsConsumer sirhsConsumerMock;
	EaeSecurityProvider provider;
	
	@Before
	public void SetUp() {
		idConverterMock = Mockito.mock(IAgentMatriculeConverterService.class);
		sirhsConsumerMock = Mockito.mock(ISirhWsConsumer.class);
		provider = new EaeSecurityProvider();
		ReflectionTestUtils.setField(provider, "agentMatriculeConverterService", idConverterMock);
		ReflectionTestUtils.setField(provider, "sirhWsConsumer", sirhsConsumerMock);
	}
	
	@Test
	public void testIsAgentAuthorizedToViewEae_AgentNotRelated_ReturnFalse() throws SirhWSConsumerException{
		
		// Given
		Integer eaeId = 4;
		Eae eae = new Eae();
		eae.setIdEae(eaeId);
		Integer agentId = 9001223;
		List<Integer> sirhConsumerResult = Arrays.asList(1, 2, 3);
		
		when(idConverterMock.tryConvertFromADIdAgentToEAEIdAgent(agentId)).thenReturn(agentId);
		when(sirhsConsumerMock.getListOfEaesForAgentId(agentId)).thenReturn(sirhConsumerResult);
		
		// Then
		assertFalse(provider.isAgentAuthorizedToViewEae(agentId, eae));
	}
	
	@Test
	public void testIsAgentAuthorizedToViewEae_AgentRelated_ReturnTrue() throws SirhWSConsumerException{
		
		// Given
		Integer eaeId = 2;
		Eae eae = new Eae();
		eae.setIdEae(eaeId);
		Integer agentId = 901223;
		Integer convertedAgentId = 9001223;
		List<Integer> sirhConsumerResult = Arrays.asList(1, 2, 3);
		
		when(idConverterMock.tryConvertFromADIdAgentToEAEIdAgent(agentId)).thenReturn(convertedAgentId);
		when(sirhsConsumerMock.getListOfEaesForAgentId(convertedAgentId)).thenReturn(sirhConsumerResult);
		
		// Then
		assertTrue(provider.isAgentAuthorizedToViewEae(agentId, eae));
	}
	
	@Test
	public void testIsAgentAuthorizedToEditEae_AgentNotRelated_ReturnFalse(){
		
		// Given
		Eae eae = new Eae();
		eae.setIdEae(1234);
		eae.setIdAgentDelegataire(null);
		
		EaeFichePoste fp1 = new EaeFichePoste();
		fp1.setIdAgentShd(9002345);
		eae.getEaeFichePostes().add(fp1);
		
		EaeFichePoste fp2 = new EaeFichePoste();
		fp2.setIdAgentShd(9003456);
		fp2.setPrimary(true);
		eae.getEaeFichePostes().add(fp2);
		
		EaeEvaluateur ev1 = new EaeEvaluateur();
		ev1.setIdAgent(9004567);
		eae.getEaeEvaluateurs().add(ev1);
		
		EaeEvaluateur ev2 = new EaeEvaluateur();
		ev2.setIdAgent(9005678);
		eae.getEaeEvaluateurs().add(ev2);
		
		when(idConverterMock.tryConvertFromADIdAgentToEAEIdAgent(9008765)).thenReturn(9008765);
		
		// Then
		assertFalse(provider.isAgentAuthorizedToEditEae(9008765, eae));
	}
	
	@Test
	public void testIsAgentAuthorizedToEditEae_AgentIsEvaluateur_ReturnTrue(){
		
		// Given
		Eae eae = new Eae();
		eae.setIdEae(1234);
		eae.setIdAgentDelegataire(null);
		
		EaeFichePoste fp1 = new EaeFichePoste();
		fp1.setIdAgentShd(9002345);
		eae.getEaeFichePostes().add(fp1);
		
		EaeFichePoste fp2 = new EaeFichePoste();
		fp2.setIdAgentShd(9003456);
		fp2.setPrimary(true);
		eae.getEaeFichePostes().add(fp2);
		
		EaeEvaluateur ev1 = new EaeEvaluateur();
		ev1.setIdAgent(9004567);
		eae.getEaeEvaluateurs().add(ev1);
		
		EaeEvaluateur ev2 = new EaeEvaluateur();
		ev2.setIdAgent(9005678);
		eae.getEaeEvaluateurs().add(ev2);
		
		when(idConverterMock.tryConvertFromADIdAgentToEAEIdAgent(905678)).thenReturn(9005678);
		
		// Then
		assertTrue(provider.isAgentAuthorizedToEditEae(905678, eae));
	}
	
	@Test
	public void testIsAgentAuthorizedToEditEae_AgentIsShd_ReturnFalse(){
		
		// Given
		Eae eae = new Eae();
		eae.setIdEae(1234);
		eae.setIdAgentDelegataire(901234);
		
		EaeFichePoste fp1 = new EaeFichePoste();
		fp1.setIdAgentShd(9002345);
		eae.getEaeFichePostes().add(fp1);
		
		EaeFichePoste fp2 = new EaeFichePoste();
		fp2.setIdAgentShd(9003456);
		fp2.setPrimary(true);
		eae.getEaeFichePostes().add(fp2);
		
		EaeEvaluateur ev1 = new EaeEvaluateur();
		ev1.setIdAgent(9004567);
		eae.getEaeEvaluateurs().add(ev1);
		
		EaeEvaluateur ev2 = new EaeEvaluateur();
		ev2.setIdAgent(9005678);
		eae.getEaeEvaluateurs().add(ev2);
		
		when(idConverterMock.tryConvertFromADIdAgentToEAEIdAgent(9002345)).thenReturn(9002345);
		
		// Then
		assertFalse(provider.isAgentAuthorizedToEditEae(9002345, eae));
	}
	
	@Test
	public void testIsAgentAuthorizedToEditEae_AgentIsDelegataire_ReturnTrue(){
		
		// Given
		Eae eae = new Eae();
		eae.setIdEae(1234);
		eae.setIdAgentDelegataire(9001234);
		
		EaeFichePoste fp1 = new EaeFichePoste();
		fp1.setIdAgentShd(9002345);
		eae.getEaeFichePostes().add(fp1);
		
		EaeFichePoste fp2 = new EaeFichePoste();
		fp2.setIdAgentShd(9003456);
		fp2.setPrimary(true);
		eae.getEaeFichePostes().add(fp2);
		
		EaeEvaluateur ev1 = new EaeEvaluateur();
		ev1.setIdAgent(9004567);
		eae.getEaeEvaluateurs().add(ev1);
		
		EaeEvaluateur ev2 = new EaeEvaluateur();
		ev2.setIdAgent(9005678);
		eae.getEaeEvaluateurs().add(ev2);
		
		when(idConverterMock.tryConvertFromADIdAgentToEAEIdAgent(9001234)).thenReturn(9001234);
		
		// Then
		assertTrue(provider.isAgentAuthorizedToEditEae(9001234, eae));
	}
	
	@Test
	public void testCheckEaeReadRight_CantfindEae_Return404() {
		// Given
		int idEae = 1234;
		Eae eae = null;
		int idAgent = 1890;
		
		Eae.findEae(1234);
		AnnotationDrivenStaticEntityMockingControl.expectReturn(eae);
		AnnotationDrivenStaticEntityMockingControl.playback();
		
		// When
		ResponseEntity<String> response = provider.checkEaeAndReadRight(idEae, idAgent);
		
		// Then
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}
	
	@Test
	public void testCheckEaeReadRight_AgentDoesNotHaveRight_Return403() throws SirhWSConsumerException {
		// Given
		int idEae = 1234;
		Eae eae = new Eae();
		eae.setIdEae(1234);
		int idAgent = 1890;
		
		Eae.findEae(1234);
		AnnotationDrivenStaticEntityMockingControl.expectReturn(eae);
		AnnotationDrivenStaticEntityMockingControl.playback();
		
		when(idConverterMock.tryConvertFromADIdAgentToEAEIdAgent(idAgent)).thenReturn(idAgent);
		when(sirhsConsumerMock.getListOfEaesForAgentId(idAgent)).thenReturn(new ArrayList<Integer>());
		
		// When
		ResponseEntity<String> response = provider.checkEaeAndReadRight(idEae, idAgent);
		
		// Then
		assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
		assertEquals("L'agent '1890' n'est pas autorisé à consulter cet Eae.", response.getBody());
	}
	
	@Test
	public void testCheckEaeReadRight_SirhWSIsUnavailable_Return503() throws SirhWSConsumerException {
		// Given
		int idEae = 1234;
		Eae eae = new Eae();
		eae.setIdEae(1234);
		int idAgent = 1890;
		
		Eae.findEae(1234);
		AnnotationDrivenStaticEntityMockingControl.expectReturn(eae);
		AnnotationDrivenStaticEntityMockingControl.playback();
		
		when(idConverterMock.tryConvertFromADIdAgentToEAEIdAgent(idAgent)).thenReturn(idAgent);
		when(sirhsConsumerMock.getListOfEaesForAgentId(idAgent)).thenThrow(new SirhWSConsumerException("message"));
		
		// When
		ResponseEntity<String> response = provider.checkEaeAndReadRight(idEae, idAgent);
		
		// Then
		assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
		assertEquals("message", response.getBody());
	}
	
	@Test
	public void testCheckEaeReadRight_UserHasRight_ReturnNull() throws SirhWSConsumerException {
		// Given
		int idEae = 1234;
		Eae eae = new Eae();
		eae.setIdEae(1234);
		int idAgent = 1890;
		
		Eae.findEae(1234);
		AnnotationDrivenStaticEntityMockingControl.expectReturn(eae);
		AnnotationDrivenStaticEntityMockingControl.playback();
		
		when(idConverterMock.tryConvertFromADIdAgentToEAEIdAgent(idAgent)).thenReturn(idAgent);
		when(sirhsConsumerMock.getListOfEaesForAgentId(idAgent)).thenReturn(Arrays.asList(idEae));
		
		// When
		ResponseEntity<String> response = provider.checkEaeAndReadRight(idEae, idAgent);
		
		// Then
		assertNull(response);
	}
	
	@Test
	public void testCheckEaeWriteRight_CantfindEae_Return404() {
		// Given
		int idEae = 1234;
		Eae eae = null;
		int idAgent = 1890;
		
		Eae.findEae(1234);
		AnnotationDrivenStaticEntityMockingControl.expectReturn(eae);
		AnnotationDrivenStaticEntityMockingControl.playback();
		
		// When
		ResponseEntity<String> response = provider.checkEaeAndWriteRight(idEae, idAgent);
		
		// Then
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}
	
	@Test
	public void testCheckEaeWriteRight_AgentDoesNotHaveRight_Return403() throws SirhWSConsumerException {
		// Given
		int idEae = 1234;
		Eae eae = new Eae();
		eae.setIdEae(1234);
		int idAgent = 1890;
		
		Eae.findEae(1234);
		AnnotationDrivenStaticEntityMockingControl.expectReturn(eae);
		AnnotationDrivenStaticEntityMockingControl.playback();
		
		when(idConverterMock.tryConvertFromADIdAgentToEAEIdAgent(idAgent)).thenReturn(idAgent);
		
		// When
		ResponseEntity<String> response = provider.checkEaeAndWriteRight(idEae, idAgent);
		
		// Then
		assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
		assertEquals("L'agent '1890' n'est pas autorisé à modifier cet Eae.", response.getBody());
	}
	
	@Test
	public void testCheckEaeWriteRight_UserHasRight_ReturnNull() throws SirhWSConsumerException {
		// Given
		int idEae = 1234;
		Eae eae = new Eae();
		eae.setIdEae(1234);
		int idAgent = 1890;
		eae.setIdAgentDelegataire(idAgent);
		
		Eae.findEae(1234);
		AnnotationDrivenStaticEntityMockingControl.expectReturn(eae);
		AnnotationDrivenStaticEntityMockingControl.playback();
		
		when(idConverterMock.tryConvertFromADIdAgentToEAEIdAgent(idAgent)).thenReturn(idAgent);
		
		// When
		ResponseEntity<String> response = provider.checkEaeAndWriteRight(idEae, idAgent);
		
		// Then
		assertNull(response);
	}
}
