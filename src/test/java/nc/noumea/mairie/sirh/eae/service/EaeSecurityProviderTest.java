package nc.noumea.mairie.sirh.eae.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluateur;
import nc.noumea.mairie.sirh.eae.domain.EaeEvalue;
import nc.noumea.mairie.sirh.eae.domain.EaeFichePoste;
import nc.noumea.mairie.sirh.eae.security.EaeSecurityProvider;
import nc.noumea.mairie.sirh.ws.ISirhWsConsumer;
import nc.noumea.mairie.sirh.ws.SirhWSConsumerException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.staticmock.MockStaticEntityMethods;
import org.springframework.test.util.ReflectionTestUtils;

@MockStaticEntityMethods
public class EaeSecurityProviderTest {

	/*
	 * IAgentMatriculeConverterService idConverterMock; ISirhWsConsumer
	 * sirhsConsumerMock; EaeSecurityProvider provider; MessageSource
	 * messageSource;
	 */

	@Before
	public void SetUp() {
		IAgentMatriculeConverterService idConverterMock = Mockito.mock(IAgentMatriculeConverterService.class);
		ISirhWsConsumer sirhsConsumerMock = Mockito.mock(ISirhWsConsumer.class);
		MessageSource messageSource = Mockito.mock(MessageSource.class);

		EaeSecurityProvider provider = new EaeSecurityProvider();
		ReflectionTestUtils.setField(provider, "agentMatriculeConverterService", idConverterMock);
		ReflectionTestUtils.setField(provider, "sirhWsConsumer", sirhsConsumerMock);
		ReflectionTestUtils.setField(provider, "messageSource", messageSource);
	}

	@Test
	public void testIsAgentAuthorizedToViewEae_AgentNotRelated_ReturnFalse() throws SirhWSConsumerException {

		// Given
		Integer eaeId = 4;
		Eae eae = new Eae();
		eae.setEaeEvalue(new EaeEvalue());
		eae.setIdEae(eaeId);
		Integer agentId = 9001223;
		List<Integer> sirhConsumerResult = Arrays.asList(1, 2, 3);

		IAgentMatriculeConverterService idConverterMock = Mockito.mock(IAgentMatriculeConverterService.class);
		Mockito.when(idConverterMock.tryConvertFromADIdAgentToEAEIdAgent(agentId)).thenReturn(agentId);

		ISirhWsConsumer sirhsConsumerMock = Mockito.mock(ISirhWsConsumer.class);
		Mockito.when(sirhsConsumerMock.getListOfSubAgentsForAgentId(agentId)).thenReturn(sirhConsumerResult);

		EaeSecurityProvider provider = new EaeSecurityProvider();
		ReflectionTestUtils.setField(provider, "agentMatriculeConverterService", idConverterMock);
		ReflectionTestUtils.setField(provider, "sirhWsConsumer", sirhsConsumerMock);

		// Then
		assertFalse(provider.isAgentAuthorizedToViewEae(agentId, eae));
	}

	@Test
	public void testIsAgentAuthorizedToViewEae_AgentRelated_ReturnTrue() throws SirhWSConsumerException {

		// Given
		Eae eae = new Eae();
		eae.setEaeEvalue(new EaeEvalue());
		eae.getEaeEvalue().setIdAgent(2);
		Integer agentId = 901223;
		Integer convertedAgentId = 9001223;
		List<Integer> sirhConsumerResult = Arrays.asList(1, 2, 3);

		IAgentMatriculeConverterService idConverterMock = Mockito.mock(IAgentMatriculeConverterService.class);
		Mockito.when(idConverterMock.tryConvertFromADIdAgentToEAEIdAgent(agentId)).thenReturn(convertedAgentId);

		ISirhWsConsumer sirhsConsumerMock = Mockito.mock(ISirhWsConsumer.class);
		Mockito.when(sirhsConsumerMock.getListOfSubAgentsForAgentId(convertedAgentId)).thenReturn(sirhConsumerResult);

		EaeSecurityProvider provider = new EaeSecurityProvider();
		ReflectionTestUtils.setField(provider, "agentMatriculeConverterService", idConverterMock);
		ReflectionTestUtils.setField(provider, "sirhWsConsumer", sirhsConsumerMock);

		// Then
		assertTrue(provider.isAgentAuthorizedToViewEae(agentId, eae));
	}

	@Test
	public void testIsAgentAuthorizedToViewEae_AgentIsDelegataire_ReturnTrue() throws SirhWSConsumerException {

		// Given
		Integer agentId = 901223;
		Integer convertedAgentId = 9001223;
		Eae eae = new Eae();
		eae.setIdAgentDelegataire(convertedAgentId);
		eae.setEaeEvalue(new EaeEvalue());
		eae.getEaeEvalue().setIdAgent(2);
		List<Integer> sirhConsumerResult = new ArrayList<Integer>();

		IAgentMatriculeConverterService idConverterMock = Mockito.mock(IAgentMatriculeConverterService.class);
		Mockito.when(idConverterMock.tryConvertFromADIdAgentToEAEIdAgent(agentId)).thenReturn(convertedAgentId);

		ISirhWsConsumer sirhsConsumerMock = Mockito.mock(ISirhWsConsumer.class);
		Mockito.when(sirhsConsumerMock.getListOfSubAgentsForAgentId(convertedAgentId)).thenReturn(sirhConsumerResult);

		EaeSecurityProvider provider = new EaeSecurityProvider();
		ReflectionTestUtils.setField(provider, "agentMatriculeConverterService", idConverterMock);
		ReflectionTestUtils.setField(provider, "sirhWsConsumer", sirhsConsumerMock);

		// Then
		assertTrue(provider.isAgentAuthorizedToViewEae(agentId, eae));
	}

	@Test
	public void testIsAgentAuthorizedToViewEae_AgentIsEvaluateur_ReturnTrue() throws SirhWSConsumerException {

		// Given
		Integer agentId = 901223;
		Integer convertedAgentId = 9001223;
		Eae eae = new Eae();
		EaeEvaluateur eval = new EaeEvaluateur();
		eval.setIdAgent(convertedAgentId);
		eae.getEaeEvaluateurs().add(eval);
		eae.setEaeEvalue(new EaeEvalue());
		eae.getEaeEvalue().setIdAgent(2);
		List<Integer> sirhConsumerResult = new ArrayList<Integer>();

		IAgentMatriculeConverterService idConverterMock = Mockito.mock(IAgentMatriculeConverterService.class);
		Mockito.when(idConverterMock.tryConvertFromADIdAgentToEAEIdAgent(agentId)).thenReturn(convertedAgentId);

		ISirhWsConsumer sirhsConsumerMock = Mockito.mock(ISirhWsConsumer.class);
		Mockito.when(sirhsConsumerMock.getListOfSubAgentsForAgentId(convertedAgentId)).thenReturn(sirhConsumerResult);

		EaeSecurityProvider provider = new EaeSecurityProvider();
		ReflectionTestUtils.setField(provider, "agentMatriculeConverterService", idConverterMock);
		ReflectionTestUtils.setField(provider, "sirhWsConsumer", sirhsConsumerMock);

		// Then
		assertTrue(provider.isAgentAuthorizedToViewEae(agentId, eae));
	}

	@Test
	public void testIsAgentAuthorizedToEditEae_AgentNotRelated_ReturnFalse() {

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

		IAgentMatriculeConverterService idConverterMock = Mockito.mock(IAgentMatriculeConverterService.class);
		Mockito.when(idConverterMock.tryConvertFromADIdAgentToEAEIdAgent(9008765)).thenReturn(9008765);

		EaeSecurityProvider provider = new EaeSecurityProvider();
		ReflectionTestUtils.setField(provider, "agentMatriculeConverterService", idConverterMock);

		// Then
		assertFalse(provider.isAgentAuthorizedToEditEae(9008765, eae));
	}

	@Test
	public void testIsAgentAuthorizedToEditEae_AgentIsEvaluateur_ReturnTrue() {

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

		IAgentMatriculeConverterService idConverterMock = Mockito.mock(IAgentMatriculeConverterService.class);
		Mockito.when(idConverterMock.tryConvertFromADIdAgentToEAEIdAgent(905678)).thenReturn(9005678);

		EaeSecurityProvider provider = new EaeSecurityProvider();
		ReflectionTestUtils.setField(provider, "agentMatriculeConverterService", idConverterMock);

		// Then
		assertTrue(provider.isAgentAuthorizedToEditEae(905678, eae));
	}

	@Test
	public void testIsAgentAuthorizedToEditEae_AgentIsShd_ReturnFalse() {

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

		IAgentMatriculeConverterService idConverterMock = Mockito.mock(IAgentMatriculeConverterService.class);
		Mockito.when(idConverterMock.tryConvertFromADIdAgentToEAEIdAgent(9002345)).thenReturn(9002345);

		EaeSecurityProvider provider = new EaeSecurityProvider();
		ReflectionTestUtils.setField(provider, "agentMatriculeConverterService", idConverterMock);

		// Then
		assertFalse(provider.isAgentAuthorizedToEditEae(9002345, eae));
	}

	@Test
	public void testIsAgentAuthorizedToEditEae_AgentIsDelegataire_ReturnTrue() {

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

		IAgentMatriculeConverterService idConverterMock = Mockito.mock(IAgentMatriculeConverterService.class);
		Mockito.when(idConverterMock.tryConvertFromADIdAgentToEAEIdAgent(9001234)).thenReturn(9001234);

		EaeSecurityProvider provider = new EaeSecurityProvider();
		ReflectionTestUtils.setField(provider, "agentMatriculeConverterService", idConverterMock);

		// Then
		assertTrue(provider.isAgentAuthorizedToEditEae(9001234, eae));
	}

	@Test
	public void testCheckEaeReadRight_CantfindEae_Return404() {
		// Given
		int idEae = 1234;
		Eae eae = null;
		int idAgent = 1890;

		IEaeService eaeService = Mockito.mock(IEaeService.class);
		Mockito.when(eaeService.findEae(1234)).thenReturn(eae);

		EaeSecurityProvider provider = new EaeSecurityProvider();
		ReflectionTestUtils.setField(provider, "eaeService", eaeService);
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
		eae.setEaeEvalue(new EaeEvalue());
		int idAgent = 1890;

		IAgentMatriculeConverterService idConverterMock = Mockito.mock(IAgentMatriculeConverterService.class);
		Mockito.when(idConverterMock.tryConvertFromADIdAgentToEAEIdAgent(idAgent)).thenReturn(idAgent);

		ISirhWsConsumer sirhsConsumerMock = Mockito.mock(ISirhWsConsumer.class);
		Mockito.when(sirhsConsumerMock.getListOfSubAgentsForAgentId(idAgent)).thenReturn(new ArrayList<Integer>());

		IEaeService eaeService = Mockito.mock(IEaeService.class);
		Mockito.when(eaeService.findEae(1234)).thenReturn(eae);

		MessageSource messageSource = Mockito.mock(MessageSource.class);
		Mockito.when(
				messageSource.getMessage(Mockito.eq("EAE_CANNOT_READ"), Mockito.any(Object[].class),
						Mockito.any(Locale.class))).thenReturn("L'agent '1890' n'est pas autorisé à consulter cet Eae");

		EaeSecurityProvider provider = new EaeSecurityProvider();
		ReflectionTestUtils.setField(provider, "eaeService", eaeService);
		ReflectionTestUtils.setField(provider, "agentMatriculeConverterService", idConverterMock);
		ReflectionTestUtils.setField(provider, "sirhWsConsumer", sirhsConsumerMock);
		ReflectionTestUtils.setField(provider, "messageSource", messageSource);

		// When
		ResponseEntity<String> response = provider.checkEaeAndReadRight(idEae, idAgent);

		// Then
		assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
		assertEquals("L'agent '1890' n'est pas autorisé à consulter cet Eae", response.getBody());
	}

	@Test
	public void testCheckEaeReadRight_SirhWSIsUnavailable_Return503() throws SirhWSConsumerException {
		// Given
		int idEae = 1234;
		Eae eae = new Eae();
		eae.setIdEae(1234);
		int idAgent = 1890;

		IAgentMatriculeConverterService idConverterMock = Mockito.mock(IAgentMatriculeConverterService.class);
		Mockito.when(idConverterMock.tryConvertFromADIdAgentToEAEIdAgent(idAgent)).thenReturn(idAgent);

		ISirhWsConsumer sirhsConsumerMock = Mockito.mock(ISirhWsConsumer.class);
		Mockito.when(sirhsConsumerMock.getListOfSubAgentsForAgentId(idAgent)).thenThrow(
				new SirhWSConsumerException("message"));

		IEaeService eaeService = Mockito.mock(IEaeService.class);
		Mockito.when(eaeService.findEae(1234)).thenReturn(eae);

		EaeSecurityProvider provider = new EaeSecurityProvider();
		ReflectionTestUtils.setField(provider, "eaeService", eaeService);
		ReflectionTestUtils.setField(provider, "agentMatriculeConverterService", idConverterMock);
		ReflectionTestUtils.setField(provider, "sirhWsConsumer", sirhsConsumerMock);

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
		eae.setEaeEvalue(new EaeEvalue());
		eae.getEaeEvalue().setIdAgent(11);
		int idAgent = 1890;

		IAgentMatriculeConverterService idConverterMock = Mockito.mock(IAgentMatriculeConverterService.class);
		Mockito.when(idConverterMock.tryConvertFromADIdAgentToEAEIdAgent(idAgent)).thenReturn(idAgent);

		IEaeService eaeService = Mockito.mock(IEaeService.class);
		Mockito.when(eaeService.findEae(1234)).thenReturn(eae);

		ISirhWsConsumer sirhsConsumerMock = Mockito.mock(ISirhWsConsumer.class);
		Mockito.when(sirhsConsumerMock.getListOfSubAgentsForAgentId(idAgent)).thenReturn(Arrays.asList(11));

		EaeSecurityProvider provider = new EaeSecurityProvider();
		ReflectionTestUtils.setField(provider, "eaeService", eaeService);
		ReflectionTestUtils.setField(provider, "agentMatriculeConverterService", idConverterMock);
		ReflectionTestUtils.setField(provider, "sirhWsConsumer", sirhsConsumerMock);

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

		IEaeService eaeService = Mockito.mock(IEaeService.class);
		Mockito.when(eaeService.findEae(1234)).thenReturn(eae);

		EaeSecurityProvider provider = new EaeSecurityProvider();
		ReflectionTestUtils.setField(provider, "eaeService", eaeService);

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

		IAgentMatriculeConverterService idConverterMock = Mockito.mock(IAgentMatriculeConverterService.class);
		Mockito.when(idConverterMock.tryConvertFromADIdAgentToEAEIdAgent(idAgent)).thenReturn(idAgent);

		IEaeService eaeService = Mockito.mock(IEaeService.class);
		Mockito.when(eaeService.findEae(1234)).thenReturn(eae);

		MessageSource messageSource = Mockito.mock(MessageSource.class);
		Mockito.when(
				messageSource.getMessage(Mockito.eq("EAE_CANNOT_WRITE"), Mockito.any(Object[].class),
						Mockito.any(Locale.class))).thenReturn("L'agent '1890' n'est pas autorisé à modifier cet Eae");

		EaeSecurityProvider provider = new EaeSecurityProvider();
		ReflectionTestUtils.setField(provider, "eaeService", eaeService);
		ReflectionTestUtils.setField(provider, "agentMatriculeConverterService", idConverterMock);
		ReflectionTestUtils.setField(provider, "messageSource", messageSource);

		// When
		ResponseEntity<String> response = provider.checkEaeAndWriteRight(idEae, idAgent);

		// Then
		assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
		assertEquals("L'agent '1890' n'est pas autorisé à modifier cet Eae", response.getBody());
	}

	@Test
	public void testCheckEaeWriteRight_UserHasRight_ReturnNull() throws SirhWSConsumerException {
		// Given
		int idEae = 1234;
		Eae eae = new Eae();
		eae.setIdEae(1234);
		int idAgent = 1890;
		eae.setIdAgentDelegataire(idAgent);

		IAgentMatriculeConverterService idConverterMock = Mockito.mock(IAgentMatriculeConverterService.class);
		Mockito.when(idConverterMock.tryConvertFromADIdAgentToEAEIdAgent(idAgent)).thenReturn(idAgent);

		IEaeService eaeService = Mockito.mock(IEaeService.class);
		Mockito.when(eaeService.findEae(1234)).thenReturn(eae);

		EaeSecurityProvider provider = new EaeSecurityProvider();
		ReflectionTestUtils.setField(provider, "eaeService", eaeService);
		ReflectionTestUtils.setField(provider, "agentMatriculeConverterService", idConverterMock);

		// When
		ResponseEntity<String> response = provider.checkEaeAndWriteRight(idEae, idAgent);

		// Then
		assertNull(response);
	}
}
