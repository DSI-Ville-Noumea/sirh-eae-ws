package nc.noumea.mairie.sirh.eae.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import nc.noumea.mairie.alfresco.cmis.IAlfrescoCMISService;
import nc.noumea.mairie.sirh.domain.Agent;
import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeCampagne;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluateur;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluation;
import nc.noumea.mairie.sirh.eae.domain.EaeEvalue;
import nc.noumea.mairie.sirh.eae.domain.EaeFichePoste;
import nc.noumea.mairie.sirh.eae.domain.EaeFinalisation;
import nc.noumea.mairie.sirh.eae.domain.EaePlanAction;
import nc.noumea.mairie.sirh.eae.domain.EaeResultat;
import nc.noumea.mairie.sirh.eae.domain.EaeTypeDeveloppement;
import nc.noumea.mairie.sirh.eae.domain.EaeTypeObjectif;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeEtatEnum;
import nc.noumea.mairie.sirh.eae.dto.CanFinalizeEaeDto;
import nc.noumea.mairie.sirh.eae.dto.EaeDashboardItemDto;
import nc.noumea.mairie.sirh.eae.dto.EaeDto;
import nc.noumea.mairie.sirh.eae.dto.EaeEvalueNameDto;
import nc.noumea.mairie.sirh.eae.dto.EaeFinalizationDto;
import nc.noumea.mairie.sirh.eae.dto.EaeListItemDto;
import nc.noumea.mairie.sirh.eae.dto.FinalizationInformationDto;
import nc.noumea.mairie.sirh.eae.dto.ReturnMessageDto;
import nc.noumea.mairie.sirh.eae.dto.identification.ValeurListeDto;
import nc.noumea.mairie.sirh.eae.repository.IEaeRepository;
import nc.noumea.mairie.sirh.eae.web.controller.NoContentException;
import nc.noumea.mairie.sirh.service.IAgentService;
import nc.noumea.mairie.sirh.tools.IHelper;
import nc.noumea.mairie.sirh.ws.ISirhWsConsumer;
import nc.noumea.mairie.sirh.ws.SirhWSConsumerException;

import org.joda.time.DateTime;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;
import org.springframework.mock.staticmock.MockStaticEntityMethods;
import org.springframework.test.util.ReflectionTestUtils;

@SuppressWarnings("unchecked")
@MockStaticEntityMethods
public class EaeServiceTest {

	private static IHelper			helperMock;
	private static EaeTypeObjectif	t1, t2;

	@BeforeClass
	public static void SetUp() {
		Calendar c = new GregorianCalendar();
		c.set(2007, 04, 19);

		helperMock = mock(IHelper.class);
		when(helperMock.getCurrentDate()).thenReturn(c.getTime());

		t1 = new EaeTypeObjectif();
		t1.setLibelle("INDIVIDUEL");
		t2 = new EaeTypeObjectif();
		t2.setLibelle("EQUIPE");
	}

	@Test
	public void testlistEaesByAgentId_WhenNoEaeForAgent_returnEmptyList() throws SirhWSConsumerException {

		// Given
		List<Integer> eaeIds = new ArrayList<Integer>();
		ISirhWsConsumer consumerMock = mock(ISirhWsConsumer.class);
		when(consumerMock.getListOfSubAgentsForAgentId(9)).thenReturn(eaeIds);

		// Mock the query to return a specific result
		TypedQuery<Eae> queryMock = mock(TypedQuery.class);
		when(queryMock.setParameter("eaeIds", eaeIds)).thenReturn(queryMock);
		when(queryMock.getResultList()).thenReturn(new ArrayList<Eae>());

		EntityManager entManagerMock = mock(EntityManager.class);
		when(entManagerMock.createQuery(any(String.class), any(Class.class))).thenReturn(queryMock);

		EaeService service = new EaeService();
		ReflectionTestUtils.setField(service, "sirhWsConsumer", consumerMock);
		ReflectionTestUtils.setField(service, "eaeEntityManager", entManagerMock);
		ReflectionTestUtils.setField(service, "helper", helperMock);

		// When
		List<EaeListItemDto> result = service.listEaesByAgentId(9);

		// Then
		assertNotNull(result);
		assertEquals(0, result.size());

		verify(consumerMock, times(1)).getListOfSubAgentsForAgentId(9);
	}

	@Test
	public void testlistEaesByAgentId_When1EaeForAgent_returnListOf1EaeWithAgentsFilledIn() throws SirhWSConsumerException {

		// Given
		List<Integer> eaeIds = new ArrayList<Integer>(Arrays.asList(98));
		ISirhWsConsumer consumerMock = mock(ISirhWsConsumer.class);
		when(consumerMock.getListOfSubAgentsForAgentId(9)).thenReturn(eaeIds);

		Eae eaeToReturn = new Eae();
		eaeToReturn.setEtat(EaeEtatEnum.EC);
		eaeToReturn.setEaeEvalue(new EaeEvalue());
		List<Eae> resultOfQuery = new ArrayList<Eae>(Arrays.asList(eaeToReturn));

		// Mock the query to return a specific result
		TypedQuery<Eae> queryMock = mock(TypedQuery.class);
		when(queryMock.setParameter("eaeIds", eaeIds)).thenReturn(queryMock);
		when(queryMock.getResultList()).thenReturn(resultOfQuery);

		EntityManager entManagerMock = mock(EntityManager.class);
		when(entManagerMock.createQuery(any(String.class), any(Class.class))).thenReturn(queryMock);

		// Mock the AgentService
		IAgentService agentServiceMock = mock(IAgentService.class);

		// Set the mock as the entityManager of the service class
		EaeService service = new EaeService();
		ReflectionTestUtils.setField(service, "eaeEntityManager", entManagerMock);
		ReflectionTestUtils.setField(service, "agentService", agentServiceMock);
		ReflectionTestUtils.setField(service, "sirhWsConsumer", consumerMock);
		ReflectionTestUtils.setField(service, "helper", helperMock);

		// When
		List<EaeListItemDto> result = service.listEaesByAgentId(9);

		// Then
		assertNotNull(result);
		assertEquals(1, result.size());

		verify(consumerMock, times(1)).getListOfSubAgentsForAgentId(9);
		verify(queryMock, times(1)).getResultList();
		verify(agentServiceMock, times(1)).fillEaeWithAgents(eaeToReturn);
	}

	@Test
	public void testlistEaesByAgentId_When2EaesForAgent_returnListOf1EaeWithAgentsFilledIn_FiltreEtat() throws SirhWSConsumerException {

		// Given
		List<Integer> agentIds = Arrays.asList(1, 2);
		ISirhWsConsumer consumerMock = mock(ISirhWsConsumer.class);
		when(consumerMock.getListOfSubAgentsForAgentId(2)).thenReturn(agentIds);

		Eae eaeToReturn = new Eae();
		eaeToReturn.setEtat(EaeEtatEnum.EC);
		eaeToReturn.setEaeEvalue(new EaeEvalue());
		Eae eaeToReturn2 = new Eae();
		eaeToReturn2.setEtat(EaeEtatEnum.C);
		eaeToReturn2.setEaeEvalue(new EaeEvalue());
		List<Eae> resultOfQuery = new ArrayList<Eae>(Arrays.asList(eaeToReturn));

		// Mock the query to return a specific result
		TypedQuery<Eae> queryMock = mock(TypedQuery.class);
		when(queryMock.setParameter("agentIds", agentIds)).thenReturn(queryMock);
		when(queryMock.setParameter("date", helperMock.getCurrentDate())).thenReturn(queryMock);
		when(queryMock.setParameter("etat", EaeEtatEnum.EC)).thenReturn(queryMock);
		when(queryMock.getResultList()).thenReturn(resultOfQuery);

		EntityManager entManagerMock = mock(EntityManager.class);
		when(entManagerMock.createQuery(any(String.class), any(Class.class))).thenReturn(queryMock);

		// Mock the AgentService
		IAgentService agentServiceMock = mock(IAgentService.class);

		// Set the mock as the entityManager of the service class
		EaeService service = new EaeService();
		ReflectionTestUtils.setField(service, "eaeEntityManager", entManagerMock);
		ReflectionTestUtils.setField(service, "agentService", agentServiceMock);
		ReflectionTestUtils.setField(service, "sirhWsConsumer", consumerMock);
		ReflectionTestUtils.setField(service, "helper", helperMock);

		// When
		List<EaeListItemDto> result = service.listEaesByAgentId(2);

		// Then
		assertNotNull(result);
		assertEquals(1, result.size());

		verify(consumerMock, times(1)).getListOfSubAgentsForAgentId(2);
		verify(queryMock, times(1)).getResultList();
		verify(agentServiceMock, times(1)).fillEaeWithAgents(eaeToReturn);
	}

	@Test
	public void testlistEaesByAgentId_When2EaesForAgent_returnListOf2EaeWithAgentsFilledIn() throws SirhWSConsumerException {

		// Given
		List<Integer> agentIds = Arrays.asList(1, 2);
		ISirhWsConsumer consumerMock = mock(ISirhWsConsumer.class);
		when(consumerMock.getListOfSubAgentsForAgentId(2)).thenReturn(agentIds);

		Eae eaeToReturn = new Eae();
		eaeToReturn.setEtat(EaeEtatEnum.EC);
		eaeToReturn.setEaeEvalue(new EaeEvalue());
		Eae eaeToReturn2 = new Eae();
		eaeToReturn2.setEtat(EaeEtatEnum.EC);
		eaeToReturn2.setEaeEvalue(new EaeEvalue());
		List<Eae> resultOfQuery = new ArrayList<Eae>(Arrays.asList(eaeToReturn, eaeToReturn2));

		// Mock the query to return a specific result
		TypedQuery<Eae> queryMock = mock(TypedQuery.class);
		when(queryMock.setParameter("agentIds", agentIds)).thenReturn(queryMock);
		when(queryMock.setParameter("date", helperMock.getCurrentDate())).thenReturn(queryMock);
		when(queryMock.getResultList()).thenReturn(resultOfQuery);

		EntityManager entManagerMock = mock(EntityManager.class);
		when(entManagerMock.createQuery(any(String.class), any(Class.class))).thenReturn(queryMock);

		// Mock the AgentService
		IAgentService agentServiceMock = mock(IAgentService.class);

		// Set the mock as the entityManager of the service class
		EaeService service = new EaeService();
		ReflectionTestUtils.setField(service, "eaeEntityManager", entManagerMock);
		ReflectionTestUtils.setField(service, "agentService", agentServiceMock);
		ReflectionTestUtils.setField(service, "sirhWsConsumer", consumerMock);
		ReflectionTestUtils.setField(service, "helper", helperMock);

		// When
		List<EaeListItemDto> result = service.listEaesByAgentId(2);

		// Then
		assertNotNull(result);
		assertEquals(2, result.size());

		verify(consumerMock, times(1)).getListOfSubAgentsForAgentId(2);
		verify(queryMock, times(1)).getResultList();
		verify(agentServiceMock, times(1)).fillEaeWithAgents(eaeToReturn);
		verify(agentServiceMock, times(1)).fillEaeWithAgents(eaeToReturn2);
	}

	@Test
	public void testlistEaesByAgentId_When2DuplicatedEaesForAgent_returnListOf1EaeWithAgentsFilledIn() throws SirhWSConsumerException {

		// Given
		List<Integer> agentIds = Arrays.asList(1, 2);
		ISirhWsConsumer consumerMock = mock(ISirhWsConsumer.class);
		when(consumerMock.getListOfSubAgentsForAgentId(2)).thenReturn(agentIds);

		Eae eaeToReturn = new Eae();
		eaeToReturn.setEtat(EaeEtatEnum.EC);
		eaeToReturn.setEaeEvalue(new EaeEvalue());
		List<Eae> resultOfQuery = new ArrayList<Eae>(Arrays.asList(eaeToReturn, eaeToReturn));

		// Mock the query to return a specific result
		TypedQuery<Eae> queryMock = mock(TypedQuery.class);
		when(queryMock.setParameter("agentIds", agentIds)).thenReturn(queryMock);
		when(queryMock.setParameter("date", helperMock.getCurrentDate())).thenReturn(queryMock);
		when(queryMock.getResultList()).thenReturn(resultOfQuery);

		EntityManager entManagerMock = mock(EntityManager.class);
		when(entManagerMock.createQuery(any(String.class), any(Class.class))).thenReturn(queryMock);

		// Mock the AgentService
		IAgentService agentServiceMock = mock(IAgentService.class);

		// Set the mock as the entityManager of the service class
		EaeService service = new EaeService();
		ReflectionTestUtils.setField(service, "eaeEntityManager", entManagerMock);
		ReflectionTestUtils.setField(service, "agentService", agentServiceMock);
		ReflectionTestUtils.setField(service, "sirhWsConsumer", consumerMock);
		ReflectionTestUtils.setField(service, "helper", helperMock);

		// When
		List<EaeListItemDto> result = service.listEaesByAgentId(2);

		// Then
		assertNotNull(result);
		assertEquals(1, result.size());

		verify(consumerMock, times(1)).getListOfSubAgentsForAgentId(2);
		verify(queryMock, times(1)).getResultList();
		verify(agentServiceMock, times(1)).fillEaeWithAgents(eaeToReturn);
	}

	@Test
	public void testInitilizeEae_setCreationDateAndStatus() throws EaeServiceException {
		// Given
		Date dateJour = new Date();
		// Mock the EAE
		Eae eaeToInit = new Eae();
		eaeToInit.setIdEae(987);
		eaeToInit.setEtat(EaeEtatEnum.ND);

		EntityManager entManagerMock = mock(EntityManager.class);
		when(entManagerMock.find(Eae.class, 987)).thenReturn(eaeToInit);

		IHelper helperMockInterne = mock(IHelper.class);
		when(helperMockInterne.getCurrentDate()).thenReturn(dateJour);

		EaeService service = new EaeService();
		ReflectionTestUtils.setField(service, "helper", helperMockInterne);
		ReflectionTestUtils.setField(service, "eaeEntityManager", entManagerMock);

		// When
		service.initializeEae(eaeToInit, null);

		// Then
		assertEquals(dateJour, eaeToInit.getDateCreation());
		verify(helperMockInterne, times(1)).getCurrentDate();
	}

	@Test
	public void testInitilizeEae_setCreationDateAndStatus_OldDateCreation() throws EaeServiceException {
		// Given
		// #19139
		// Mock the EAE
		Eae eaeToInit = new Eae();
		eaeToInit.setIdEae(987);
		eaeToInit.setEtat(EaeEtatEnum.ND);
		eaeToInit.setDateCreation(new DateTime(2010, 01, 01, 12, 00, 00).toDate());

		EntityManager entManagerMock = mock(EntityManager.class);
		when(entManagerMock.find(Eae.class, 987)).thenReturn(eaeToInit);

		IHelper helperMockInterne = mock(IHelper.class);

		EaeService service = new EaeService();
		ReflectionTestUtils.setField(service, "helper", helperMockInterne);
		ReflectionTestUtils.setField(service, "eaeEntityManager", entManagerMock);

		// When
		service.initializeEae(eaeToInit, null);

		// Then
		assertEquals(new DateTime(2010, 01, 01, 12, 00, 00).toDate(), eaeToInit.getDateCreation());
		verify(helperMockInterne, times(0)).getCurrentDate();
	}

	@Test
	public void testInitilizeEae_noPreviousEaes_createNoEaeResultat() throws EaeServiceException {
		// Given
		Eae eaeToInit = new Eae();
		eaeToInit.setIdEae(987);
		eaeToInit.setEtat(EaeEtatEnum.ND);

		EntityManager eaeEntityManager = mock(EntityManager.class);
		when(eaeEntityManager.find(Eae.class, 987)).thenReturn(eaeToInit);

		EaeService service = new EaeService();
		ReflectionTestUtils.setField(service, "helper", helperMock);
		ReflectionTestUtils.setField(service, "eaeEntityManager", eaeEntityManager);

		// When
		service.initializeEae(eaeToInit, null);

		// Then
		assertTrue(eaeToInit.getEaeResultats().isEmpty());
	}

	@Test
	public void testInitilizeEae_1PreviousEae_createEaeResultatFromPreviousPlanActionAndCopyNotes() throws EaeServiceException {
		// Given
		Eae eaeToInit = new Eae();
		eaeToInit.setIdEae(987);
		eaeToInit.setEtat(EaeEtatEnum.ND);

		// Mock EAEs list (current, previous)
		Eae previousEae = new Eae();
		EaePlanAction p1 = new EaePlanAction();
		p1.setObjectif("obj1");
		p1.setTypeObjectif(t1);
		previousEae.getEaePlanActions().add(p1);

		EaePlanAction p2 = new EaePlanAction();
		p2.setObjectif("obj2");
		p2.setTypeObjectif(t2);
		previousEae.getEaePlanActions().add(p2);

		EaeEvaluation eval = new EaeEvaluation();
		eval.setNoteAnnee(13.03f);
		eval.setNoteAnneeN1(14.04f);
		eval.setNoteAnneeN2(15.05f);
		eval.setNoteAnneeN3(16.06f);

		previousEae.setEaeEvaluation(eval);
		previousEae.setIdEae(789);

		EntityManager eaeEntityManager = mock(EntityManager.class);
		when(eaeEntityManager.find(Eae.class, 987)).thenReturn(eaeToInit);
		when(eaeEntityManager.find(Eae.class, previousEae.getIdEae())).thenReturn(previousEae);

		EaeService service = new EaeService();
		ReflectionTestUtils.setField(service, "helper", helperMock);
		ReflectionTestUtils.setField(service, "eaeEntityManager", eaeEntityManager);

		// When
		service.initializeEae(eaeToInit, previousEae);

		// Then
		assertFalse(eaeToInit.getEaeResultats().isEmpty());
		assertNull(eaeToInit.getEaeEvaluation().getNoteAnnee());
		assertEquals(new Float(13.03), eaeToInit.getEaeEvaluation().getNoteAnneeN1());
		assertEquals(new Float(14.04), eaeToInit.getEaeEvaluation().getNoteAnneeN2());
		assertEquals(new Float(15.05), eaeToInit.getEaeEvaluation().getNoteAnneeN3());
		assertEquals(2, eaeToInit.getEaeResultats().size());

		List<EaeResultat> resultats = new ArrayList<EaeResultat>();
		resultats.addAll(eaeToInit.getEaeResultats());

		assertTrue(resultats.get(0).getObjectif() == p1.getObjectif() || resultats.get(0).getObjectif() == p2.getObjectif());
		assertTrue(resultats.get(0).getTypeObjectif() == p1.getTypeObjectif() || resultats.get(0).getTypeObjectif() == p2.getTypeObjectif());

		assertTrue(resultats.get(1).getObjectif() == p1.getObjectif() || resultats.get(1).getObjectif() == p2.getObjectif());
		assertTrue(resultats.get(1).getTypeObjectif() == p1.getTypeObjectif() || resultats.get(1).getTypeObjectif() == p2.getTypeObjectif());
	}

	@Test
	public void testInitilizeEae_eaeNotInEtatND_throwException() {
		// Given
		Eae eaeToInit = new Eae();
		eaeToInit.setIdEae(987);
		eaeToInit.setEtat(EaeEtatEnum.EC);

		EntityManager eaeEntityManager = mock(EntityManager.class);
		when(eaeEntityManager.find(Eae.class, 987)).thenReturn(eaeToInit);

		EaeService service = new EaeService();
		ReflectionTestUtils.setField(service, "helper", helperMock);
		ReflectionTestUtils.setField(service, "eaeEntityManager", eaeEntityManager);

		// When
		String exMessage = "";

		try {
			service.initializeEae(eaeToInit, null);
		} catch (EaeServiceException ex) {
			exMessage = ex.getMessage();
		}

		// Then
		assertEquals("Impossible d'initialiser l'EAE id '987': le statut de cet Eae est 'En cours'.", exMessage);
		assertEquals(EaeEtatEnum.EC, eaeToInit.getEtat());
	}

	@Test
	public void testFindLastEaeByAgentId_noEae_returnNull() {
		// Given
		int agentId = 9987;
		List<Eae> eaeFound = new ArrayList<Eae>();

		// Mock the query to return a specific result
		TypedQuery<Eae> queryMock = mock(TypedQuery.class);
		when(queryMock.setParameter("idAgent", agentId)).thenReturn(queryMock);
		when(queryMock.getResultList()).thenReturn(eaeFound);

		EntityManager entManagerMock = mock(EntityManager.class);
		when(entManagerMock.createQuery(
				"select e from Eae e where e.eaeEvalue.idAgent = :idAgent and e.etat != 'S' order by e.eaeCampagne.annee desc", Eae.class))
						.thenReturn(queryMock);

		EaeService service = new EaeService();
		ReflectionTestUtils.setField(service, "eaeEntityManager", entManagerMock);

		// When
		Eae result = service.findLastEaeByAgentId(agentId);

		// Then
		assertNull(result);
		verify(queryMock, times(1)).getResultList();
	}

	@Test
	public void testFindLastEaeByAgentId_3Eaes_returnFirsOfTheList() {
		// Given
		int agentId = 9987;
		List<Eae> eaeFound = new ArrayList<Eae>();
		Eae firstEae = new Eae();
		eaeFound.add(firstEae);
		eaeFound.add(new Eae());
		eaeFound.add(new Eae());

		// Mock the query to return a specific result
		TypedQuery<Eae> queryMock = mock(TypedQuery.class);
		when(queryMock.setParameter("idAgent", agentId)).thenReturn(queryMock);
		when(queryMock.getResultList()).thenReturn(eaeFound);

		EntityManager entManagerMock = mock(EntityManager.class);
		when(entManagerMock.createQuery(
				"select e from Eae e where e.eaeEvalue.idAgent = :idAgent and e.etat != 'S' order by e.eaeCampagne.annee desc", Eae.class))
						.thenReturn(queryMock);

		EaeService service = new EaeService();
		ReflectionTestUtils.setField(service, "eaeEntityManager", entManagerMock);

		// When
		Eae result = service.findLastEaeByAgentId(agentId);

		// Then
		assertEquals(firstEae, result);
		verify(queryMock, times(1)).getResultList();
	}

	@Test
	public void testStartEae_eaeIsNotCree_throwException() {
		// Given
		Eae eaeToStart = new Eae();
		eaeToStart.setIdEae(987);
		eaeToStart.setEtat(EaeEtatEnum.ND);

		EntityManager entManagerMock = mock(EntityManager.class);
		when(entManagerMock.find(Eae.class, 987)).thenReturn(eaeToStart);

		EaeService service = new EaeService();
		ReflectionTestUtils.setField(service, "eaeEntityManager", entManagerMock);

		try {
			// When
			service.startEae(987, false);
		} catch (EaeServiceException ex) {
			// Then
			assertEquals("Impossible de démarrer l'EAE id '987': le statut de cet Eae est 'Non débuté'.", ex.getMessage());
		}
	}

	@Test
	public void testStartEae_eaeIsCree_setEtatToEC() throws EaeServiceException {

		// Given
		Eae eaeToStart = new Eae();
		eaeToStart.setIdEae(987);
		eaeToStart.setEtat(EaeEtatEnum.C);

		EntityManager entManagerMock = mock(EntityManager.class);
		when(entManagerMock.find(Eae.class, 987)).thenReturn(eaeToStart);

		EaeService service = new EaeService();
		ReflectionTestUtils.setField(service, "eaeEntityManager", entManagerMock);

		// When
		service.startEae(987, false);

		// Then
		assertEquals(EaeEtatEnum.EC, eaeToStart.getEtat());
	}
	
	@Test
	public void testStartEae_eaeCO_isSirh() throws EaeServiceException {

		// Given
		Eae eaeToStart = new Eae();
		eaeToStart.setIdEae(987);
		eaeToStart.setEtat(EaeEtatEnum.CO);

		EntityManager entManagerMock = mock(EntityManager.class);
		when(entManagerMock.find(Eae.class, 987)).thenReturn(eaeToStart);

		EaeService service = new EaeService();
		ReflectionTestUtils.setField(service, "eaeEntityManager", entManagerMock);

		// When
		Eae result = service.startEae(987, true);

		// Then
		assertEquals(EaeEtatEnum.CO, result.getEtat());
	}

	@Test
	public void testStartEae_eaeIsEC_doNothing() throws EaeServiceException {

		// Given
		Eae eaeToStart = new Eae();
		eaeToStart.setIdEae(987);
		eaeToStart.setEtat(EaeEtatEnum.EC);

		EntityManager entManagerMock = mock(EntityManager.class);
		when(entManagerMock.find(Eae.class, 987)).thenReturn(eaeToStart);

		EaeService service = new EaeService();
		ReflectionTestUtils.setField(service, "eaeEntityManager", entManagerMock);

		// When
		service.startEae(987, false);

		// Then
		assertEquals(EaeEtatEnum.EC, eaeToStart.getEtat());
	}

	@Test
	public void testResetEaeEvaluateur_eaeIsNotND_C_EC_throwException() {
		// Given
		Eae eaeToDelete = new Eae();
		eaeToDelete.setIdEae(987);
		eaeToDelete.setEtat(EaeEtatEnum.F);

		EaeEvaluateur eval = new EaeEvaluateur();
		eval.setIdAgent(9009);
		eaeToDelete.getEaeEvaluateurs().add(eval);

		EaeService service = new EaeService();

		try {
			// When
			service.resetEaeEvaluateur(eaeToDelete);
		} catch (EaeServiceException ex) {
			// Then
			assertEquals("Impossible de réinitialiser l'EAE id '987': le statut de cet Eae est 'Finalisé'.", ex.getMessage());
		}

		assertEquals(eval, eaeToDelete.getEaeEvaluateurs().iterator().next());
	}

	@Test
	public void testResetEaeEvaluateur_eaeIsCree_setEtatToNDAndResetEvaluateurs() throws EaeServiceException {

		// Given
		Eae eaeToDelete = spy(new Eae());
		// org.mockito.Mockito.doNothing().when(eaeToDelete).flush();
		// TODO corriger spy flush
		eaeToDelete.setIdEae(987);
		eaeToDelete.setEtat(EaeEtatEnum.C);

		EaeEvaluateur eval = new EaeEvaluateur();
		eval.setIdAgent(9009);
		eaeToDelete.getEaeEvaluateurs().add(eval);

		EaeFichePoste fdp = new EaeFichePoste();
		fdp.setIdAgentShd(10000);
		fdp.setPrimary(true);
		fdp.setFonctionResponsable("fonction responsable");

		eaeToDelete.getEaeFichePostes().add(fdp);

		EntityManager emMock = Mockito.mock(EntityManager.class);
		EaeService service = new EaeService();
		ReflectionTestUtils.setField(service, "eaeEntityManager", emMock);

		// When
		service.resetEaeEvaluateur(eaeToDelete);

		// Then
		Mockito.verify(emMock, Mockito.times(1)).flush();
		assertEquals(EaeEtatEnum.ND, eaeToDelete.getEtat());
		assertEquals(10000, eaeToDelete.getEaeEvaluateurs().iterator().next().getIdAgent());
		assertEquals("fonction responsable", eaeToDelete.getEaeEvaluateurs().iterator().next().getFonction());
	}

	@Test
	public void testResetEaeEvaluateur_eaeIsEC_setEtatToNDAndResetEvaluateurs() throws EaeServiceException {

		// Given
		Eae eaeToDelete = spy(new Eae());
		// org.mockito.Mockito.doNothing().when(eaeToDelete).flush();
		// TODO corriger spy flush
		eaeToDelete.setIdEae(987);
		eaeToDelete.setEtat(EaeEtatEnum.EC);

		EaeEvaluateur eval = new EaeEvaluateur();
		eval.setIdAgent(9009);
		eaeToDelete.getEaeEvaluateurs().add(eval);

		EaeFichePoste fdp = new EaeFichePoste();
		fdp.setIdAgentShd(10000);
		fdp.setPrimary(true);
		fdp.setFonctionResponsable("fonction responsable");

		eaeToDelete.getEaeFichePostes().add(fdp);

		EntityManager emMock = Mockito.mock(EntityManager.class);

		EaeService service = new EaeService();
		ReflectionTestUtils.setField(service, "eaeEntityManager", emMock);

		// When
		service.resetEaeEvaluateur(eaeToDelete);

		// Then
		Mockito.verify(emMock, Mockito.times(1)).flush();
		assertEquals(EaeEtatEnum.ND, eaeToDelete.getEtat());
		assertEquals(10000, eaeToDelete.getEaeEvaluateurs().iterator().next().getIdAgent());
		assertEquals("fonction responsable", eaeToDelete.getEaeEvaluateurs().iterator().next().getFonction());
	}

	@Test
	public void testResetEaeEvaluateur_eaeIsND_resetEvaluateurs() throws EaeServiceException {

		// Given
		Eae eaeToDelete = spy(new Eae());
		// org.mockito.Mockito.doNothing().when(eaeToDelete).flush();
		// TODO corriger spy flush

		eaeToDelete.setIdEae(987);
		eaeToDelete.setEtat(EaeEtatEnum.ND);

		EaeEvaluateur eval = new EaeEvaluateur();
		eval.setIdAgent(9009);
		eaeToDelete.getEaeEvaluateurs().add(eval);

		EaeFichePoste fdp = new EaeFichePoste();
		fdp.setIdAgentShd(10000);
		fdp.setPrimary(true);
		fdp.setFonctionResponsable("fonction responsable");

		eaeToDelete.getEaeFichePostes().add(fdp);

		EntityManager emMock = Mockito.mock(EntityManager.class);
		EaeService service = new EaeService();
		ReflectionTestUtils.setField(service, "eaeEntityManager", emMock);

		// When
		service.resetEaeEvaluateur(eaeToDelete);

		// Then
		Mockito.verify(emMock, Mockito.times(1)).flush();
		assertEquals(EaeEtatEnum.ND, eaeToDelete.getEtat());
		assertEquals(10000, eaeToDelete.getEaeEvaluateurs().iterator().next().getIdAgent());
		assertEquals("fonction responsable", eaeToDelete.getEaeEvaluateurs().iterator().next().getFonction());
	}

	@Test
	public void testResetEaeEvaluateur_eaeIsEC_AgentShdIsNull_setEtatToNDAndResetEvaluateursToNull() throws EaeServiceException {

		// Given
		Eae eaeToDelete = spy(new Eae());
		// org.mockito.Mockito.doNothing().when(eaeToDelete).flush();
		// TODO corriger spy flush
		eaeToDelete.setIdEae(987);
		eaeToDelete.setEtat(EaeEtatEnum.EC);

		EaeEvaluateur eval = new EaeEvaluateur();
		eval.setIdAgent(9009);
		eaeToDelete.getEaeEvaluateurs().add(eval);

		EaeFichePoste fdp = new EaeFichePoste();
		fdp.setIdAgentShd(null);
		fdp.setPrimary(true);

		eaeToDelete.getEaeFichePostes().add(fdp);

		EntityManager emMock = Mockito.mock(EntityManager.class);
		EaeService service = new EaeService();
		ReflectionTestUtils.setField(service, "eaeEntityManager", emMock);

		// When
		service.resetEaeEvaluateur(eaeToDelete);

		// Then
		Mockito.verify(emMock, Mockito.times(1)).flush();
		assertEquals(EaeEtatEnum.ND, eaeToDelete.getEtat());
		assertEquals(0, eaeToDelete.getEaeEvaluateurs().size());
	}

	@Test
	public void testSetDelegataire_DelegataireExists_SetAgentId() throws EaeServiceException {
		// Given
		Eae eae = new Eae();
		eae.setIdAgentDelegataire(123);
		eae.setIdEae(987);
		Integer idAgentDelegataire = 1789;

		// Mock the agent find static method to return our agent
		Agent agentToReturn = new Agent();
		agentToReturn.setIdAgent(idAgentDelegataire);
		agentToReturn.setNomPatronymique("Bilbo");

		EntityManager emMock = Mockito.mock(EntityManager.class);
		when(emMock.find(Eae.class, 987)).thenReturn(eae);

		IAgentService agentService = Mockito.mock(IAgentService.class);
		Mockito.when(agentService.getAgent(idAgentDelegataire)).thenReturn(agentToReturn);

		EaeService service = new EaeService();
		ReflectionTestUtils.setField(service, "eaeEntityManager", emMock);
		ReflectionTestUtils.setField(service, "agentService", agentService);
		// When
		service.setDelegataire(987, idAgentDelegataire);

		// Then
		assertEquals(idAgentDelegataire, eae.getIdAgentDelegataire());
	}

	@Test
	public void testSetDelegataire_DelegataireDoesNotExist_throwException() {
		// Given
		Eae eae = new Eae();
		eae.setIdAgentDelegataire(123);
		eae.setIdEae(987);
		Integer idAgentDelegataire = 1789;

		// Mock the agent find static method to return our null agent
		Agent agentToReturn = null;

		EntityManager emMock = Mockito.mock(EntityManager.class);
		Mockito.when(emMock.find(Agent.class, idAgentDelegataire)).thenReturn(agentToReturn);
		when(emMock.find(Eae.class, 987)).thenReturn(eae);

		IAgentService agentService = mock(IAgentService.class);
		when(agentService.getAgent(idAgentDelegataire)).thenReturn(agentToReturn);

		EaeService service = new EaeService();
		ReflectionTestUtils.setField(service, "eaeEntityManager", emMock);
		ReflectionTestUtils.setField(service, "agentService", agentService);

		try {
			// When
			service.setDelegataire(987, idAgentDelegataire);
		} catch (EaeServiceException ex) {
			// Then
			assertEquals("Impossible d'affecter l'agent '1789' en tant que délégataire: cet Agent n'existe pas.", ex.getMessage());
			assertEquals(new Integer(123), eae.getIdAgentDelegataire());
		}

	}

	@Test
	public void testGetEaesDashboard_NoEaeForAgentId_ReturnEmptyList() throws SirhWSConsumerException {

		// Given
		List<Integer> eaeIds = new ArrayList<Integer>();
		ISirhWsConsumer consumerMock = mock(ISirhWsConsumer.class);
		when(consumerMock.getListOfSubAgentsForAgentId(98)).thenReturn(eaeIds);

		// Mock the query to return a specific result
		TypedQuery<Eae> queryMock = mock(TypedQuery.class);
		when(queryMock.setParameter("eaeIds", eaeIds)).thenReturn(queryMock);
		when(queryMock.getResultList()).thenReturn(new ArrayList<Eae>());

		EntityManager entManagerMock = mock(EntityManager.class);
		when(entManagerMock.createQuery(any(String.class), any(Class.class))).thenReturn(queryMock);

		EaeService service = new EaeService();
		ReflectionTestUtils.setField(service, "sirhWsConsumer", consumerMock);
		ReflectionTestUtils.setField(service, "eaeEntityManager", entManagerMock);
		ReflectionTestUtils.setField(service, "helper", helperMock);

		// When
		List<EaeDashboardItemDto> result = service.getEaesDashboard(98);

		// Then
		assertEquals(0, result.size());
	}

	@Test
	public void testGetEaesDashboard_3EaesForAgentId2Evaluateurs_ReturnDtoListOf2() throws SirhWSConsumerException {

		// Given
		Agent agent19 = new Agent();
		agent19.setNomUsage("toto");

		Agent agent21 = new Agent();
		agent21.setNomUsage("titi");

		EaeEvaluateur toto = new EaeEvaluateur();
		toto.setIdAgent(19);
		EaeEvaluateur titi = new EaeEvaluateur();
		titi.setIdAgent(21);

		Eae eaeToReturn1 = new Eae();
		eaeToReturn1.setEtat(EaeEtatEnum.EC);
		eaeToReturn1.setEaeEvalue(new EaeEvalue());
		eaeToReturn1.getEaeEvalue().setIdAgent(10);
		eaeToReturn1.getEaeEvaluateurs().add(toto);

		Eae eaeToReturn2 = new Eae();
		eaeToReturn2.setEtat(EaeEtatEnum.EC);
		eaeToReturn2.setEaeEvalue(new EaeEvalue());
		eaeToReturn2.getEaeEvalue().setIdAgent(12);
		eaeToReturn2.getEaeEvaluateurs().add(titi);

		Eae eaeToReturn3 = new Eae();
		eaeToReturn3.setEtat(EaeEtatEnum.EC);
		eaeToReturn3.setEaeEvalue(new EaeEvalue());
		eaeToReturn3.getEaeEvalue().setIdAgent(13);
		eaeToReturn3.getEaeEvaluateurs().add(toto);

		List<Integer> agentIds = Arrays.asList(10, 12, 13);
		List<Eae> resultOfQuery = Arrays.asList(eaeToReturn1, eaeToReturn2, eaeToReturn3);

		// Mock the WS to return 3 ids
		ISirhWsConsumer consumerMock = mock(ISirhWsConsumer.class);
		when(consumerMock.getListOfSubAgentsForAgentId(98)).thenReturn(agentIds);

		// Mock the query to return a specific result
		TypedQuery<Eae> queryMock = mock(TypedQuery.class);
		when(queryMock.setParameter("agentIds", agentIds)).thenReturn(queryMock);
		when(queryMock.setParameter("date", helperMock.getCurrentDate())).thenReturn(queryMock);
		when(queryMock.getResultList()).thenReturn(resultOfQuery);

		EntityManager entManagerMock = mock(EntityManager.class);
		when(entManagerMock.createQuery(any(String.class), any(Class.class))).thenReturn(queryMock);

		// Mock the AgentService
		IAgentService agentServiceMock = mock(IAgentService.class);
		when(agentServiceMock.getAgent(19)).thenReturn(agent19);
		when(agentServiceMock.getAgent(21)).thenReturn(agent21);

		EaeService service = new EaeService();
		ReflectionTestUtils.setField(service, "eaeEntityManager", entManagerMock);
		ReflectionTestUtils.setField(service, "agentService", agentServiceMock);
		ReflectionTestUtils.setField(service, "sirhWsConsumer", consumerMock);
		ReflectionTestUtils.setField(service, "helper", helperMock);

		// When
		List<EaeDashboardItemDto> result = service.getEaesDashboard(98);

		// Then
		assertEquals(2, result.size());
		assertEquals("toto", result.get(0).getNom());
		assertEquals(2, result.get(0).getEnCours());
		assertEquals("titi", result.get(1).getNom());
		assertEquals(1, result.get(1).getEnCours());
	}

	@Test
	public void testGetEaesDashboard_1EaeWith2Evaluateurs_ReturnDtoListOf2WithDuplicatedEae() throws SirhWSConsumerException {

		// Given
		Agent agent19 = new Agent();
		agent19.setNomUsage("toto");

		Agent agent21 = new Agent();
		agent21.setNomUsage("titi");

		EaeEvaluateur toto = new EaeEvaluateur();
		toto.setIdAgent(19);
		EaeEvaluateur titi = new EaeEvaluateur();
		titi.setIdAgent(21);

		Eae eaeToReturn1 = new Eae();
		eaeToReturn1.setEtat(EaeEtatEnum.EC);
		eaeToReturn1.getEaeEvaluateurs().add(toto);
		eaeToReturn1.getEaeEvaluateurs().add(titi);

		List<Integer> agentIds = Arrays.asList(1);
		List<Eae> resultOfQuery = Arrays.asList(eaeToReturn1);

		// Mock the WS to return 1 ids
		ISirhWsConsumer consumerMock = mock(ISirhWsConsumer.class);
		when(consumerMock.getListOfSubAgentsForAgentId(98)).thenReturn(agentIds);

		// Mock the query to return a specific result
		TypedQuery<Eae> queryMock = mock(TypedQuery.class);
		when(queryMock.setParameter("agentIds", agentIds)).thenReturn(queryMock);
		when(queryMock.setParameter("date", helperMock.getCurrentDate())).thenReturn(queryMock);
		when(queryMock.getResultList()).thenReturn(resultOfQuery);

		EntityManager entManagerMock = mock(EntityManager.class);
		when(entManagerMock.createQuery(any(String.class), any(Class.class))).thenReturn(queryMock);

		// Mock the AgentService
		IAgentService agentServiceMock = mock(IAgentService.class);
		when(agentServiceMock.getAgent(19)).thenReturn(agent19);
		when(agentServiceMock.getAgent(21)).thenReturn(agent21);

		EaeService service = new EaeService();
		ReflectionTestUtils.setField(service, "eaeEntityManager", entManagerMock);
		ReflectionTestUtils.setField(service, "agentService", agentServiceMock);
		ReflectionTestUtils.setField(service, "sirhWsConsumer", consumerMock);
		ReflectionTestUtils.setField(service, "helper", helperMock);

		// When
		List<EaeDashboardItemDto> result = service.getEaesDashboard(98);

		// Then
		assertEquals(2, result.size());
		assertEquals("toto", result.get(0).getNom());
		assertEquals(1, result.get(0).getEnCours());
		assertEquals("titi", result.get(1).getNom());
		assertEquals(1, result.get(1).getEnCours());
	}

	@Test
	public void testGetEaesDashboard_1EaeWithAgentAndOtherEvaluateur_ReturnDtoListOf1WithoutDuplicatedEae() throws SirhWSConsumerException {

		// Given
		Agent agent98 = new Agent();
		agent98.setNomUsage("toto");

		Agent agent2ext = new Agent();
		agent2ext.setNomUsage("titi");

		EaeEvaluateur toto = new EaeEvaluateur();
		toto.setIdAgent(98);

		EaeEvaluateur titi = new EaeEvaluateur();
		titi.setIdAgent(2);

		Eae eaeToReturn1 = new Eae();
		eaeToReturn1.setEtat(EaeEtatEnum.EC);
		eaeToReturn1.getEaeEvaluateurs().add(toto);
		eaeToReturn1.getEaeEvaluateurs().add(titi);

		List<Integer> eaeIds = Arrays.asList(1);
		List<Eae> resultOfQuery = Arrays.asList(eaeToReturn1);

		// Mock the WS to return 1 ids
		ISirhWsConsumer consumerMock = mock(ISirhWsConsumer.class);
		when(consumerMock.getListOfSubAgentsForAgentId(98)).thenReturn(eaeIds);

		// Mock the query to return a specific result
		TypedQuery<Eae> queryMock = mock(TypedQuery.class);
		when(queryMock.setParameter("eaeIds", eaeIds)).thenReturn(queryMock);
		when(queryMock.getResultList()).thenReturn(resultOfQuery);

		EntityManager entManagerMock = mock(EntityManager.class);
		when(entManagerMock.createQuery(any(String.class), any(Class.class))).thenReturn(queryMock);

		// Mock the AgentService
		IAgentService agentServiceMock = mock(IAgentService.class);
		when(agentServiceMock.getAgent(98)).thenReturn(agent98);

		EaeService service = new EaeService();
		ReflectionTestUtils.setField(service, "eaeEntityManager", entManagerMock);
		ReflectionTestUtils.setField(service, "agentService", agentServiceMock);
		ReflectionTestUtils.setField(service, "sirhWsConsumer", consumerMock);
		ReflectionTestUtils.setField(service, "helper", helperMock);

		// When
		List<EaeDashboardItemDto> result = service.getEaesDashboard(98);

		// Then
		assertEquals(1, result.size());
		assertEquals("toto", result.get(0).getNom());
		assertEquals(1, result.get(0).getEnCours());
	}

	@Test
	public void testGetEaesDashboard_3EaesForAgentId1EvaluateurAnd2WithoutEvaluateur_ReturnDtoListOf2() throws SirhWSConsumerException {

		// Given
		Agent agent19 = new Agent();
		agent19.setNomUsage("toto");
		agent19.setPrenom("tutu");

		EaeEvaluateur toto = new EaeEvaluateur();
		toto.setIdAgent(19);

		Eae eaeToReturn1 = new Eae();
		eaeToReturn1.setEtat(EaeEtatEnum.EC);
		eaeToReturn1.setEaeEvalue(new EaeEvalue());
		eaeToReturn1.getEaeEvalue().setIdAgent(10);
		eaeToReturn1.getEaeEvaluateurs().add(toto);

		Eae eaeToReturn2 = new Eae();
		eaeToReturn2.setEtat(EaeEtatEnum.NA);
		eaeToReturn2.setEaeEvalue(new EaeEvalue());
		eaeToReturn2.getEaeEvalue().setIdAgent(12);

		Eae eaeToReturn3 = new Eae();
		eaeToReturn3.setEtat(EaeEtatEnum.NA);
		eaeToReturn3.setEaeEvalue(new EaeEvalue());
		eaeToReturn3.getEaeEvalue().setIdAgent(13);

		List<Integer> agentIds = Arrays.asList(10, 12, 13);
		List<Eae> resultOfQuery = Arrays.asList(eaeToReturn1, eaeToReturn2, eaeToReturn3);

		// Mock the WS to return 3 ids
		ISirhWsConsumer consumerMock = mock(ISirhWsConsumer.class);
		when(consumerMock.getListOfSubAgentsForAgentId(98)).thenReturn(agentIds);

		// Mock the query to return a specific result
		TypedQuery<Eae> queryMock = mock(TypedQuery.class);
		when(queryMock.setParameter("agentIds", agentIds)).thenReturn(queryMock);
		when(queryMock.setParameter("date", helperMock.getCurrentDate())).thenReturn(queryMock);
		when(queryMock.getResultList()).thenReturn(resultOfQuery);

		EntityManager entManagerMock = mock(EntityManager.class);
		when(entManagerMock.createQuery(any(String.class), any(Class.class))).thenReturn(queryMock);

		// Mock the AgentService
		IAgentService agentServiceMock = mock(IAgentService.class);
		when(agentServiceMock.getAgent(19)).thenReturn(agent19);

		EaeService service = new EaeService();
		ReflectionTestUtils.setField(service, "eaeEntityManager", entManagerMock);
		ReflectionTestUtils.setField(service, "agentService", agentServiceMock);
		ReflectionTestUtils.setField(service, "sirhWsConsumer", consumerMock);
		ReflectionTestUtils.setField(service, "helper", helperMock);

		// When
		List<EaeDashboardItemDto> result = service.getEaesDashboard(98);

		// Then
		assertEquals(2, result.size());
		assertEquals("toto", result.get(0).getNom());
		assertEquals("tutu", result.get(0).getPrenom());
		assertEquals(1, result.get(0).getEnCours());
		assertEquals("?", result.get(1).getNom());
		assertEquals("?", result.get(1).getPrenom());
		assertEquals(2, result.get(1).getNonAffecte());
	}

	@Test
	public void testGetFinalizationInformation_EaeisNull_returnNull() throws SirhWSConsumerException {

		// Given
		Eae eae = null;

		EntityManager entManagerMock = mock(EntityManager.class);
		when(entManagerMock.find(Eae.class, 987)).thenReturn(eae);

		EaeService service = new EaeService();
		ReflectionTestUtils.setField(service, "eaeEntityManager", entManagerMock);

		// When
		FinalizationInformationDto dto = service.getFinalizationInformation(9);

		// Then
		assertNull(dto);
	}

	@Test
	public void testGetFinalizationInformation_ReturnDto() throws SirhWSConsumerException {

		// Given
		EaeCampagne camp = new EaeCampagne();
		camp.setAnnee(2014);
		Eae eae = new Eae();
		eae.setEaeEvalue(new EaeEvalue());
		eae.setIdEae(7896);
		eae.setEaeCampagne(camp);
		EaeEvaluation evaluation = new EaeEvaluation();
		evaluation.setNoteAnnee(13.03f);
		eae.setEaeEvaluation(evaluation);

		IAgentService agentServiceMock = Mockito.mock(IAgentService.class);
		ISirhWsConsumer sirhMock = Mockito.mock(ISirhWsConsumer.class);
		when(sirhMock.getListOfShdAgentsForAgentId(9005138)).thenReturn(new ArrayList<Integer>());

		EntityManager entManagerMock = mock(EntityManager.class);
		when(entManagerMock.find(Eae.class, 7896)).thenReturn(eae);

		EaeService service = new EaeService();
		ReflectionTestUtils.setField(service, "agentService", agentServiceMock);
		ReflectionTestUtils.setField(service, "sirhWsConsumer", sirhMock);
		ReflectionTestUtils.setField(service, "eaeEntityManager", entManagerMock);

		// When
		FinalizationInformationDto dto = service.getFinalizationInformation(7896);

		// Then
		assertNotNull(dto);
		assertEquals(7896, dto.getIdEae());
	}

	@Test
	public void testGetFinalizationInformation_ReturnDto_CallAgentService() throws SirhWSConsumerException {

		// Given
		EaeCampagne camp = new EaeCampagne();
		camp.setAnnee(2014);
		Eae eae = new Eae();
		eae.setEaeEvalue(new EaeEvalue());
		eae.getEaeEvalue().setIdAgent(9005138);
		eae.setIdEae(7896);
		eae.setEaeCampagne(camp);
		EaeEvaluation eval = new EaeEvaluation();
		eval.setNoteAnnee(13.03f);
		eae.setEaeEvaluation(eval);

		Agent shd1 = new Agent();
		shd1.setIdAgent(9008765);
		Agent shd2 = new Agent();
		shd1.setIdAgent(9008654);

		IAgentService agentServiceMock = Mockito.mock(IAgentService.class);
		when(agentServiceMock.getAgent(shd1.getIdAgent())).thenReturn(shd1);
		when(agentServiceMock.getAgent(shd2.getIdAgent())).thenReturn(shd2);

		ISirhWsConsumer sirhMock = Mockito.mock(ISirhWsConsumer.class);
		when(sirhMock.getListOfShdAgentsForAgentId(9005138)).thenReturn(Arrays.asList(shd1.getIdAgent(), shd2.getIdAgent()));

		EntityManager entManagerMock = mock(EntityManager.class);
		when(entManagerMock.find(Eae.class, 7896)).thenReturn(eae);

		EaeService service = new EaeService();
		ReflectionTestUtils.setField(service, "agentService", agentServiceMock);
		ReflectionTestUtils.setField(service, "sirhWsConsumer", sirhMock);
		ReflectionTestUtils.setField(service, "eaeEntityManager", entManagerMock);

		// When
		FinalizationInformationDto dto = service.getFinalizationInformation(7896);

		// Then
		assertNotNull(dto);
		assertEquals(shd1.getIdAgent(), dto.getAgentsShd().get(0).getIdAgent());
		assertEquals(shd2.getIdAgent(), dto.getAgentsShd().get(1).getIdAgent());
		assertEquals(7896, dto.getIdEae());
		assertEquals(eval.getNoteAnnee(), dto.getNoteAnnee());

		verify(agentServiceMock, times(1)).fillEaeWithAgents(eae);
	}

	@Test
	public void testFinalizeEae_EaeIsEC_SetEaeAndCreateEaeFinalisation() throws EaeServiceException {

		// Given
		Eae eae = new Eae();
		eae.setEtat(EaeEtatEnum.EC);
		eae.setIdEae(987);
		Integer idAgent = 90899;
		EaeEvaluation eval = new EaeEvaluation();
		eval.setNoteAnnee(13.03f);
		eae.setEaeEvaluation(eval);

		EaeFinalizationDto dto = new EaeFinalizationDto();
		dto.setCommentaire("le commentaire");
		dto.setIdDocument("çççdikjnvekusvb");
		dto.setVersionDocument("10.1");
		dto.setNoteAnnee(13.03f);

		IAgentMatriculeConverterService converterMock = Mockito.mock(IAgentMatriculeConverterService.class);
		when(converterMock.tryConvertFromADIdAgentToEAEIdAgent(idAgent)).thenReturn(900899);

		EntityManager entManagerMock = mock(EntityManager.class);
		when(entManagerMock.find(Eae.class, 987)).thenReturn(eae);

		MessageSource messageSource = mock(MessageSource.class);
		IAlfrescoCMISService alfrescoCMISService = mock(IAlfrescoCMISService.class);
		when(alfrescoCMISService.uploadDocument(Mockito.anyInt(), Mockito.any(EaeFinalizationDto.class), 
				Mockito.any(EaeFinalisation.class), Mockito.any(Eae.class), Mockito.any(ReturnMessageDto.class)))
			.thenReturn(new ReturnMessageDto());

		EaeService service = new EaeService();
		ReflectionTestUtils.setField(service, "helper", helperMock);
		ReflectionTestUtils.setField(service, "agentMatriculeConverterService", converterMock);
		ReflectionTestUtils.setField(service, "eaeEntityManager", entManagerMock);
		ReflectionTestUtils.setField(service, "messageSource", messageSource);
		ReflectionTestUtils.setField(service, "alfrescoCMISService", alfrescoCMISService);

		// When
		ReturnMessageDto result = service.finalizeEae(987, idAgent, dto);

		// Then
		assertEquals(result.getErrors().size(), 0);

		// Then
		assertEquals(helperMock.getCurrentDate(), eae.getDateFinalisation());
		assertEquals(EaeEtatEnum.F, eae.getEtat());
		assertTrue(eae.isDocAttache());

		EaeFinalisation finalisation = eae.getEaeFinalisations().iterator().next();
		EaeEvaluation evaluation = eae.getEaeEvaluation();
		assertEquals(helperMock.getCurrentDate(), finalisation.getDateFinalisation());
		assertEquals(900899, finalisation.getIdAgent());
		assertEquals("le commentaire", finalisation.getCommentaire());
		assertEquals(dto.getNoteAnnee(), evaluation.getNoteAnnee());
		assertEquals(eae, finalisation.getEae());
	}

	@Test
	public void testFinalizeEae_EaeIsCO_SetEaeAndCreateEaeFinalisation() throws EaeServiceException {

		// Given
		Eae eae = new Eae();
		eae.setEtat(EaeEtatEnum.CO);
		eae.setIdEae(987);
		Integer idAgent = 90899;

		EaeEvaluation evaluation = new EaeEvaluation();
		evaluation.setNoteAnnee(13.03f);
		eae.setEaeEvaluation(evaluation);

		EaeFinalizationDto dto = new EaeFinalizationDto();
		dto.setCommentaire("le commentaire");
		dto.setIdDocument("çççdikjnvekusvb");
		dto.setVersionDocument("10.1");
		dto.setNoteAnnee(13.03f);

		IAgentMatriculeConverterService converterMock = Mockito.mock(IAgentMatriculeConverterService.class);
		when(converterMock.tryConvertFromADIdAgentToEAEIdAgent(idAgent)).thenReturn(900899);

		EntityManager entManagerMock = mock(EntityManager.class);
		when(entManagerMock.find(Eae.class, 987)).thenReturn(eae);

		MessageSource messageSource = mock(MessageSource.class);
		IAlfrescoCMISService alfrescoCMISService = mock(IAlfrescoCMISService.class);
		when(alfrescoCMISService.uploadDocument(Mockito.anyInt(), Mockito.any(EaeFinalizationDto.class), 
				Mockito.any(EaeFinalisation.class), Mockito.any(Eae.class), Mockito.any(ReturnMessageDto.class)))
			.thenReturn(new ReturnMessageDto());
		
		EaeService service = new EaeService();
		ReflectionTestUtils.setField(service, "helper", helperMock);
		ReflectionTestUtils.setField(service, "agentMatriculeConverterService", converterMock);
		ReflectionTestUtils.setField(service, "eaeEntityManager", entManagerMock);
		ReflectionTestUtils.setField(service, "messageSource", messageSource);
		ReflectionTestUtils.setField(service, "alfrescoCMISService", alfrescoCMISService);

		// When
		ReturnMessageDto result = service.finalizeEae(987, idAgent, dto);

		// Then
		assertEquals(result.getErrors().size(), 0);
		assertEquals(helperMock.getCurrentDate(), eae.getDateFinalisation());
		assertEquals(EaeEtatEnum.CO, eae.getEtat());
		assertTrue(eae.isDocAttache());

		EaeFinalisation finalisation = eae.getEaeFinalisations().iterator().next();
		EaeEvaluation eaeEvaluation = eae.getEaeEvaluation();
		assertEquals(helperMock.getCurrentDate(), finalisation.getDateFinalisation());
		assertEquals(900899, finalisation.getIdAgent());
		assertEquals("le commentaire", finalisation.getCommentaire());
		assertEquals(dto.getNoteAnnee(), eaeEvaluation.getNoteAnnee());
		assertEquals(eae, finalisation.getEae());
	}

	@Test
	public void testFinalizeEae_EaeIsNotEC_ThrowException() {

		// Given
		Eae eae = new Eae();
		eae.setEtat(EaeEtatEnum.C);
		eae.setIdEae(987);
		Integer idAgent = 90899;

		EaeFinalizationDto dto = new EaeFinalizationDto();
		dto.setCommentaire("le commentaire");
		dto.setIdDocument("çççdikjnvekusvb");
		dto.setVersionDocument("10.1");

		MessageSource mSource = Mockito.mock(MessageSource.class);
		when(mSource.getMessage(Mockito.eq("EAE_CANNOT_FINALIZE"), Mockito.any(Object[].class), Mockito.any(Locale.class)))
				.thenReturn("Impossible de finaliser l'Eae car son état est 'Créé'");

		EntityManager entManagerMock = mock(EntityManager.class);
		when(entManagerMock.find(Eae.class, 987)).thenReturn(eae);

		EaeService service = new EaeService();
		ReflectionTestUtils.setField(service, "messageSource", mSource);
		ReflectionTestUtils.setField(service, "eaeEntityManager", entManagerMock);

		ReturnMessageDto result = service.finalizeEae(987, idAgent, dto);

		// Then
		assertEquals(result.getErrors().size(), 1);
		assertEquals(result.getErrors().get(0), "Impossible de finaliser l'Eae car son état n'est pas 'En Cours' mais 'Créé'.");
		assertFalse(eae.isDocAttache());
	}

	@Test
	public void canFinalizEae_EaeIsNotEC_ReturnMessageAndFalse() {
		// Given
		Eae eae = new Eae();
		eae.setIdEae(987);
		eae.setEtat(EaeEtatEnum.C);

		MessageSource mSource = Mockito.mock(MessageSource.class);
		when(mSource.getMessage(Mockito.eq("EAE_CANNOT_FINALIZE"), Mockito.any(Object[].class), Mockito.any(Locale.class)))
				.thenReturn("Impossible de finaliser l'Eae car son état est 'Créé'");

		EntityManager entManagerMock = mock(EntityManager.class);
		when(entManagerMock.find(Eae.class, 987)).thenReturn(eae);

		EaeService service = new EaeService();
		ReflectionTestUtils.setField(service, "messageSource", mSource);
		ReflectionTestUtils.setField(service, "eaeEntityManager", entManagerMock);

		// When
		CanFinalizeEaeDto result = service.canFinalizEae(987);

		// Then
		assertFalse(result.isCanFinalize());
		assertEquals("Impossible de finaliser l'Eae car son état est 'Créé'", result.getMessage());
	}

	@Test
	public void canFinalizEae_EaeIsEC_ReturnNoMessageAndTrue() {
		// Given
		Eae eae = new Eae();
		eae.setEtat(EaeEtatEnum.EC);
		eae.setIdEae(987);

		EntityManager entManagerMock = mock(EntityManager.class);
		when(entManagerMock.find(Eae.class, 987)).thenReturn(eae);

		EaeService service = new EaeService();
		ReflectionTestUtils.setField(service, "eaeEntityManager", entManagerMock);

		// When
		CanFinalizeEaeDto result = service.canFinalizEae(987);

		// Then
		assertTrue(result.isCanFinalize());
		assertNull(result.getMessage());
	}

	@Test
	public void getEvalueName_ReturnEvalueFirstAndLastName() {
		// Given
		Eae eae = new Eae();
		EaeEvalue evalue = new EaeEvalue();
		evalue.setAgent(new Agent());
		evalue.getAgent().setPrenom("NICOLAS");
		evalue.getAgent().setNomUsage("RAYNAUD");
		eae.setEaeEvalue(evalue);
		eae.setIdEae(987);

		IAgentService agentServiceMock = mock(IAgentService.class);

		EntityManager entManagerMock = mock(EntityManager.class);
		when(entManagerMock.find(Eae.class, 987)).thenReturn(eae);

		EaeService service = new EaeService();
		ReflectionTestUtils.setField(service, "agentService", agentServiceMock);
		ReflectionTestUtils.setField(service, "eaeEntityManager", entManagerMock);

		// When
		EaeEvalueNameDto dto = service.getEvalueName(987);

		// Then
		assertEquals("RAYNAUD", dto.getNom());
		assertEquals("NICOLAS", dto.getPrenom());

		verify(agentServiceMock, times(1)).fillEaeEvalueWithAgent(evalue);
	}

	@Test
	public void findEaeByAgentAndYear_Existing() {
		// Given
		Eae eae1 = new Eae();
		eae1.setEtat(EaeEtatEnum.EC);
		Eae eae2 = new Eae();
		eae2.setEtat(EaeEtatEnum.EC);
		List<Eae> listEae = Arrays.asList(eae1, eae2);

		EaeService service = new EaeService();
		Agent ag = new Agent();
		ag.setIdAgent(9005138);
		String annee = "2012";

		// Mock the query to return a specific result
		Query queryMock = mock(Query.class);
		when(queryMock.setParameter("idAgent", ag.getIdAgent())).thenReturn(queryMock);
		when(queryMock.setParameter("annee", annee)).thenReturn(queryMock);
		when(queryMock.getResultList()).thenReturn(listEae);

		EntityManager entManagerMock = mock(EntityManager.class);
		when(entManagerMock.createNativeQuery(any(String.class), any(Class.class))).thenReturn(queryMock);
		ReflectionTestUtils.setField(service, "eaeEntityManager", entManagerMock);

		// When
		Eae result = service.findEaeByAgentAndYear(ag.getIdAgent(), Integer.valueOf(annee));

		// Then
		assertEquals(eae1.getEtat(), result.getEtat());
	}
	
	@Test 
	public void findEaesByIdAgentOnly_noResult(){
		
		Integer agentId = 9005138;

		IEaeRepository eaeRepository = mock(IEaeRepository.class);
		when(eaeRepository.findEaeEvalueWithEaesByIdAgentOnly(agentId)).thenReturn(null);
		
		EaeService service = new EaeService();
		ReflectionTestUtils.setField(service, "eaeRepository", eaeRepository);
		
		List<EaeDto> result = service.findEaesByIdAgentOnly(agentId);
		
		assertEquals(0, result.size());
	}
	
	@Test 
	public void findEaesByIdAgentOnly_1Result(){
		
		Integer agentId = 9005138;
		
		EaeEvaluateur evaluateur = new EaeEvaluateur();
		
		Eae eae = new Eae();
		eae.setEtat(EaeEtatEnum.C);
		eae.getEaeEvaluateurs().add(evaluateur);
		eae.setIdEae(12);
		
		EaeEvalue evalue = new EaeEvalue();
			evalue.setEae(eae);
			
		List<EaeEvalue> listEvalue = new ArrayList<EaeEvalue>();
		listEvalue.add(evalue);

		IEaeRepository eaeRepository = mock(IEaeRepository.class);
		when(eaeRepository.findEaeEvalueWithEaesByIdAgentOnly(agentId)).thenReturn(listEvalue);
		
		IAgentService agentService = mock(IAgentService.class);
		
		EaeService service = new EaeService();
		ReflectionTestUtils.setField(service, "eaeRepository", eaeRepository);
		ReflectionTestUtils.setField(service, "agentService", agentService);
		
		List<EaeDto> result = service.findEaesByIdAgentOnly(agentId);
		
		assertEquals(1, result.size());
	}
	
	@Test 
	public void findEaeDto_noResult(){
		
		Integer idEae = 1;

		IEaeRepository eaeRepository = mock(IEaeRepository.class);
		when(eaeRepository.findEae(idEae)).thenReturn(null);
		
		EaeService service = new EaeService();
		ReflectionTestUtils.setField(service, "eaeRepository", eaeRepository);
		
		EaeDto result = service.findEaeDto(idEae);
		
		assertNull(result);
	}
	
	@Test 
	public void findEaeDto_1Result(){
		
		Integer idEae = 1;
		
		EaeEvaluateur evaluateur = new EaeEvaluateur();
		
		Eae eae = new Eae();
			eae.setIdEae(idEae);
			eae.setEtat(EaeEtatEnum.C);
			eae.getEaeEvaluateurs().add(evaluateur);

		IEaeRepository eaeRepository = mock(IEaeRepository.class);
		when(eaeRepository.findEae(idEae)).thenReturn(eae);
		
		IAgentService agentService = mock(IAgentService.class);
		
		EaeService service = new EaeService();
		ReflectionTestUtils.setField(service, "eaeRepository", eaeRepository);
		ReflectionTestUtils.setField(service, "agentService", agentService);
		
		EaeDto result = service.findEaeDto(idEae);
		
		assertNotNull(result);
	}
	
	@Test 
	public void getListeTypeDeveloppement_noResult() {
		
		IEaeRepository eaeRepository = mock(IEaeRepository.class);
		when(eaeRepository.getListeTypeDeveloppement()).thenReturn(null);

		EaeService service = new EaeService();
		ReflectionTestUtils.setField(service, "eaeRepository", eaeRepository);
		
		List<ValeurListeDto> result = service.getListeTypeDeveloppement();
		
		assertEquals(0, result.size());
	}
	
	@Test 
	public void getListeTypeDeveloppement_1Result() {
		
		EaeTypeDeveloppement dev = new EaeTypeDeveloppement();
		dev.setIdEaeTypeDeveloppement(1);
		
		List<EaeTypeDeveloppement> listTypeDvlpt = new ArrayList<EaeTypeDeveloppement>();
		listTypeDvlpt.add(dev);
		
		IEaeRepository eaeRepository = mock(IEaeRepository.class);
		when(eaeRepository.getListeTypeDeveloppement()).thenReturn(listTypeDvlpt);

		EaeService service = new EaeService();
		ReflectionTestUtils.setField(service, "eaeRepository", eaeRepository);
		
		List<ValeurListeDto> result = service.getListeTypeDeveloppement();
		
		assertEquals(1, result.size());
	}

	@Test
	public void countListEaesByAgentId() throws SirhWSConsumerException {

		Integer idAgent = 9005138;

		ISirhWsConsumer sirhWsConsumer = Mockito.mock(ISirhWsConsumer.class);
		Mockito.when(sirhWsConsumer.getListOfSubAgentsForAgentId(idAgent)).thenReturn(Arrays.asList(9005131));

		Eae eae = new Eae();
		Eae eae2 = new Eae();
		List<Eae> listEae = new ArrayList<Eae>();
		listEae.add(eae);
		listEae.add(eae2);

		IEaeRepository eaeRepository = Mockito.mock(IEaeRepository.class);
		Mockito.when(eaeRepository.findEaesNonDebuteOuCreeOuEnCoursForEaeListByAgentIds(Arrays.asList(9005131), idAgent)).thenReturn(listEae);

		EaeService service = new EaeService();
		ReflectionTestUtils.setField(service, "sirhWsConsumer", sirhWsConsumer);
		ReflectionTestUtils.setField(service, "eaeRepository", eaeRepository);

		assertEquals(2, service.countListEaesByAgentId(idAgent).intValue());
	}

	@Test
	public void countListEaesByAgentId_return0() throws SirhWSConsumerException {

		Integer idAgent = 9005138;

		ISirhWsConsumer sirhWsConsumer = Mockito.mock(ISirhWsConsumer.class);
		Mockito.when(sirhWsConsumer.getListOfSubAgentsForAgentId(idAgent)).thenReturn(Arrays.asList(9005131));

		IEaeRepository eaeRepository = Mockito.mock(IEaeRepository.class);
		Mockito.when(eaeRepository.findEaesNonDebuteOuCreeOuEnCoursForEaeListByAgentIds(Arrays.asList(9005131), idAgent)).thenReturn(null);

		EaeService service = new EaeService();
		ReflectionTestUtils.setField(service, "sirhWsConsumer", sirhWsConsumer);
		ReflectionTestUtils.setField(service, "eaeRepository", eaeRepository);

		assertEquals(0, service.countListEaesByAgentId(idAgent).intValue());
	}
	
	@Test(expected = NoContentException.class)
	public void getEaesDashboardForSIRH_NoContentException() throws SirhWSConsumerException {
		
		Integer anneeCampagne = 2015;
		
		IEaeRepository eaeRepository = Mockito.mock(IEaeRepository.class);
		Mockito.when(eaeRepository.findEaeCampagneByAnnee(anneeCampagne)).thenReturn(null);
		
		EaeService service = new EaeService();
		ReflectionTestUtils.setField(service, "eaeRepository", eaeRepository);
		
		service.getEaesDashboardForSIRH(anneeCampagne);
	}
	
	@Test
	public void getEaesDashboardForSIRH_noResult() throws SirhWSConsumerException {
		
		Integer anneeCampagne = 2015;
		
		EaeCampagne eaeCampagne = new EaeCampagne();
		eaeCampagne.setIdCampagneEae(1);
		
		IEaeRepository eaeRepository = Mockito.mock(IEaeRepository.class);
		Mockito.when(eaeRepository.findEaeCampagneByAnnee(anneeCampagne)).thenReturn(eaeCampagne);
		

		Map<String, List<String>> mapDirectionSection = new HashMap<String, List<String>>();
		
		
		Mockito.when(eaeRepository.getListEaeFichePosteParDirectionEtSection(eaeCampagne.getIdCampagneEae())).thenReturn(mapDirectionSection);
		
		EaeService service = new EaeService();
		ReflectionTestUtils.setField(service, "eaeRepository", eaeRepository);
		
		List<EaeDashboardItemDto> result = service.getEaesDashboardForSIRH(anneeCampagne);
		
		assertEquals(0, result.size());
	}
	
	@Test
	public void getEaesDashboardForSIRH_3Results() throws SirhWSConsumerException {
		
		Integer anneeCampagne = 2015;
		
		EaeCampagne eaeCampagne = new EaeCampagne();
		eaeCampagne.setIdCampagneEae(1);
		
		IEaeRepository eaeRepository = Mockito.mock(IEaeRepository.class);
		Mockito.when(eaeRepository.findEaeCampagneByAnnee(anneeCampagne)).thenReturn(eaeCampagne);
		
		List<String> listSectionDsi = new ArrayList<>();
		listSectionDsi.add("SED");
		listSectionDsi.add("SIE");

		List<String> listSectionDrh = new ArrayList<>();
		listSectionDrh.add("formation");
		listSectionDrh.add("recrutement");
		
		Map<String, List<String>> mapDirectionSection = new HashMap<String, List<String>>();
		mapDirectionSection.put("direction DSI", listSectionDsi);
		mapDirectionSection.put("direction DRH", listSectionDrh);
		
		Mockito.when(eaeRepository.getListEaeFichePosteParDirectionEtSection(eaeCampagne.getIdCampagneEae())).thenReturn(mapDirectionSection);
		
		///////////////// SED ///////////////////
		Mockito.when(eaeRepository.countEaeByCampagneAndDirectionAndSectionAndStatut(
				eaeCampagne.getIdCampagneEae(), "direction DSI", "SED", EaeEtatEnum.NA.name(), false)).thenReturn(1);
		Mockito.when(eaeRepository.countEaeByCampagneAndDirectionAndSectionAndStatut(
				eaeCampagne.getIdCampagneEae(), "direction DSI", "SED", EaeEtatEnum.ND.name(), false)).thenReturn(2);
		Mockito.when(eaeRepository.countEaeByCampagneAndDirectionAndSectionAndStatut(
				eaeCampagne.getIdCampagneEae(), "direction DSI", "SED", EaeEtatEnum.C.name(), false)).thenReturn(3);
		Mockito.when(eaeRepository.countEaeByCampagneAndDirectionAndSectionAndStatut(
				eaeCampagne.getIdCampagneEae(), "direction DSI", "SED", EaeEtatEnum.EC.name(), false)).thenReturn(4);
		Mockito.when(eaeRepository.countEaeByCampagneAndDirectionAndSectionAndStatut(
				eaeCampagne.getIdCampagneEae(), "direction DSI", "SED", EaeEtatEnum.F.name(), false)).thenReturn(5);
		Mockito.when(eaeRepository.countEaeByCampagneAndDirectionAndSectionAndStatut(
				eaeCampagne.getIdCampagneEae(), "direction DSI", "SED", EaeEtatEnum.CO.name(), false)).thenReturn(6);
		Mockito.when(eaeRepository.countEaeByCampagneAndDirectionAndSectionAndStatut(
				eaeCampagne.getIdCampagneEae(), "direction DSI", "SED", null, true)).thenReturn(7);
		Mockito.when(eaeRepository.countAvisSHD(
				eaeCampagne.getIdCampagneEae(), "direction DSI", "SED", false, null, false)).thenReturn(8);
		Mockito.when(eaeRepository.countAvisSHD(
				eaeCampagne.getIdCampagneEae(), "direction DSI", "SED", false, "MINI", false)).thenReturn(9);
		Mockito.when(eaeRepository.countAvisSHD(
				eaeCampagne.getIdCampagneEae(), "direction DSI", "SED", false, "MOY", false)).thenReturn(10);
		Mockito.when(eaeRepository.countAvisSHD(
				eaeCampagne.getIdCampagneEae(), "direction DSI", "SED", false, "MAXI", false)).thenReturn(11);
		Mockito.when(eaeRepository.countAvisSHD(
				eaeCampagne.getIdCampagneEae(), "direction DSI", "SED", true, null, true)).thenReturn(12);
		
		///////////////// SIE ///////////////////
		Mockito.when(eaeRepository.countEaeByCampagneAndDirectionAndSectionAndStatut(
				eaeCampagne.getIdCampagneEae(), "direction DSI", "SIE", EaeEtatEnum.NA.name(), false)).thenReturn(13);
		Mockito.when(eaeRepository.countEaeByCampagneAndDirectionAndSectionAndStatut(
				eaeCampagne.getIdCampagneEae(), "direction DSI", "SIE", EaeEtatEnum.ND.name(), false)).thenReturn(14);
		Mockito.when(eaeRepository.countEaeByCampagneAndDirectionAndSectionAndStatut(
				eaeCampagne.getIdCampagneEae(), "direction DSI", "SIE", EaeEtatEnum.C.name(), false)).thenReturn(15);
		Mockito.when(eaeRepository.countEaeByCampagneAndDirectionAndSectionAndStatut(
				eaeCampagne.getIdCampagneEae(), "direction DSI", "SIE", EaeEtatEnum.EC.name(), false)).thenReturn(16);
		Mockito.when(eaeRepository.countEaeByCampagneAndDirectionAndSectionAndStatut(
				eaeCampagne.getIdCampagneEae(), "direction DSI", "SIE", EaeEtatEnum.F.name(), false)).thenReturn(17);
		Mockito.when(eaeRepository.countEaeByCampagneAndDirectionAndSectionAndStatut(
				eaeCampagne.getIdCampagneEae(), "direction DSI", "SIE", EaeEtatEnum.CO.name(), false)).thenReturn(18);
		Mockito.when(eaeRepository.countEaeByCampagneAndDirectionAndSectionAndStatut(
				eaeCampagne.getIdCampagneEae(), "direction DSI", "SIE", null, true)).thenReturn(19);
		Mockito.when(eaeRepository.countAvisSHD(
				eaeCampagne.getIdCampagneEae(), "direction DSI", "SIE", false, null, false)).thenReturn(20);
		Mockito.when(eaeRepository.countAvisSHD(
				eaeCampagne.getIdCampagneEae(), "direction DSI", "SIE", false, "MINI", false)).thenReturn(21);
		Mockito.when(eaeRepository.countAvisSHD(
				eaeCampagne.getIdCampagneEae(), "direction DSI", "SIE", false, "MOY", false)).thenReturn(22);
		Mockito.when(eaeRepository.countAvisSHD(
				eaeCampagne.getIdCampagneEae(), "direction DSI", "SIE", false, "MAXI", false)).thenReturn(23);
		Mockito.when(eaeRepository.countAvisSHD(
				eaeCampagne.getIdCampagneEae(), "direction DSI", "SIE", true, null, true)).thenReturn(24);
		
		///////////////// formation ///////////////////
		Mockito.when(eaeRepository.countEaeByCampagneAndDirectionAndSectionAndStatut(
				eaeCampagne.getIdCampagneEae(), "direction DRH", "formation", EaeEtatEnum.NA.name(), false)).thenReturn(25);
		Mockito.when(eaeRepository.countEaeByCampagneAndDirectionAndSectionAndStatut(
				eaeCampagne.getIdCampagneEae(), "direction DRH", "formation", EaeEtatEnum.ND.name(), false)).thenReturn(26);
		Mockito.when(eaeRepository.countEaeByCampagneAndDirectionAndSectionAndStatut(
				eaeCampagne.getIdCampagneEae(), "direction DRH", "formation", EaeEtatEnum.C.name(), false)).thenReturn(27);
		Mockito.when(eaeRepository.countEaeByCampagneAndDirectionAndSectionAndStatut(
				eaeCampagne.getIdCampagneEae(), "direction DRH", "formation", EaeEtatEnum.EC.name(), false)).thenReturn(28);
		Mockito.when(eaeRepository.countEaeByCampagneAndDirectionAndSectionAndStatut(
				eaeCampagne.getIdCampagneEae(), "direction DRH", "formation", EaeEtatEnum.F.name(), false)).thenReturn(29);
		Mockito.when(eaeRepository.countEaeByCampagneAndDirectionAndSectionAndStatut(
				eaeCampagne.getIdCampagneEae(), "direction DRH", "formation", EaeEtatEnum.CO.name(), false)).thenReturn(30);
		Mockito.when(eaeRepository.countEaeByCampagneAndDirectionAndSectionAndStatut(
				eaeCampagne.getIdCampagneEae(), "direction DRH", "formation", null, true)).thenReturn(31);
		Mockito.when(eaeRepository.countAvisSHD(
				eaeCampagne.getIdCampagneEae(), "direction DRH", "formation", false, null, false)).thenReturn(32);
		Mockito.when(eaeRepository.countAvisSHD(
				eaeCampagne.getIdCampagneEae(), "direction DRH", "formation", false, "MINI", false)).thenReturn(33);
		Mockito.when(eaeRepository.countAvisSHD(
				eaeCampagne.getIdCampagneEae(), "direction DRH", "formation", false, "MOY", false)).thenReturn(34);
		Mockito.when(eaeRepository.countAvisSHD(
				eaeCampagne.getIdCampagneEae(), "direction DRH", "formation", false, "MAXI", false)).thenReturn(35);
		Mockito.when(eaeRepository.countAvisSHD(
				eaeCampagne.getIdCampagneEae(), "direction DRH", "formation", true, null, true)).thenReturn(36);
		
		///////////////// recrutement ///////////////////
		Mockito.when(eaeRepository.countEaeByCampagneAndDirectionAndSectionAndStatut(
				eaeCampagne.getIdCampagneEae(), "direction DRH", "recrutement", EaeEtatEnum.NA.name(), false)).thenReturn(37);
		Mockito.when(eaeRepository.countEaeByCampagneAndDirectionAndSectionAndStatut(
				eaeCampagne.getIdCampagneEae(), "direction DRH", "recrutement", EaeEtatEnum.ND.name(), false)).thenReturn(38);
		Mockito.when(eaeRepository.countEaeByCampagneAndDirectionAndSectionAndStatut(
				eaeCampagne.getIdCampagneEae(), "direction DRH", "recrutement", EaeEtatEnum.C.name(), false)).thenReturn(39);
		Mockito.when(eaeRepository.countEaeByCampagneAndDirectionAndSectionAndStatut(
				eaeCampagne.getIdCampagneEae(), "direction DRH", "recrutement", EaeEtatEnum.EC.name(), false)).thenReturn(40);
		Mockito.when(eaeRepository.countEaeByCampagneAndDirectionAndSectionAndStatut(
				eaeCampagne.getIdCampagneEae(), "direction DRH", "recrutement", EaeEtatEnum.F.name(), false)).thenReturn(41);
		Mockito.when(eaeRepository.countEaeByCampagneAndDirectionAndSectionAndStatut(
				eaeCampagne.getIdCampagneEae(), "direction DRH", "recrutement", EaeEtatEnum.CO.name(), false)).thenReturn(42);
		Mockito.when(eaeRepository.countEaeByCampagneAndDirectionAndSectionAndStatut(
				eaeCampagne.getIdCampagneEae(), "direction DRH", "recrutement", null, true)).thenReturn(43);
		Mockito.when(eaeRepository.countAvisSHD(
				eaeCampagne.getIdCampagneEae(), "direction DRH", "recrutement", false, null, false)).thenReturn(44);
		Mockito.when(eaeRepository.countAvisSHD(
				eaeCampagne.getIdCampagneEae(), "direction DRH", "recrutement", false, "MINI", false)).thenReturn(45);
		Mockito.when(eaeRepository.countAvisSHD(
				eaeCampagne.getIdCampagneEae(), "direction DRH", "recrutement", false, "MOY", false)).thenReturn(46);
		Mockito.when(eaeRepository.countAvisSHD(
				eaeCampagne.getIdCampagneEae(), "direction DRH", "recrutement", false, "MAXI", false)).thenReturn(47);
		Mockito.when(eaeRepository.countAvisSHD(
				eaeCampagne.getIdCampagneEae(), "direction DRH", "recrutement", true, null, true)).thenReturn(48);
		
		
		EaeService service = new EaeService();
		ReflectionTestUtils.setField(service, "eaeRepository", eaeRepository);
		
		List<EaeDashboardItemDto> result = service.getEaesDashboardForSIRH(anneeCampagne);
		
		assertEquals(4, result.size());
		
		// formation
		assertEquals("direction DRH", result.get(0).getDirection());
		assertEquals("formation", result.get(0).getSection());
		assertEquals(25, result.get(0).getNonAffecte());
		assertEquals(26, result.get(0).getNonDebute());
		assertEquals(27, result.get(0).getCree());
		assertEquals(28, result.get(0).getEnCours());
		assertEquals(29, result.get(0).getFinalise());
		assertEquals(30, result.get(0).getNbEaeControle());
		assertEquals(31, result.get(0).getNbEaeCAP());
		assertEquals(32, result.get(0).getNonDefini());
		assertEquals(33, result.get(0).getMini());
		assertEquals(34, result.get(0).getMoy());
		assertEquals(35, result.get(0).getMaxi());
		assertEquals(36, result.get(0).getChangClasse());
		
		// recrutement
		assertEquals("direction DRH", result.get(1).getDirection());
		assertEquals("recrutement", result.get(1).getSection());
		assertEquals(37, result.get(1).getNonAffecte());
		assertEquals(38, result.get(1).getNonDebute());
		assertEquals(39, result.get(1).getCree());
		assertEquals(40, result.get(1).getEnCours());
		assertEquals(41, result.get(1).getFinalise());
		assertEquals(42, result.get(1).getNbEaeControle());
		assertEquals(43, result.get(1).getNbEaeCAP());
		assertEquals(44, result.get(1).getNonDefini());
		assertEquals(45, result.get(1).getMini());
		assertEquals(46, result.get(1).getMoy());
		assertEquals(47, result.get(1).getMaxi());
		assertEquals(48, result.get(1).getChangClasse());
		
		// SED
		assertEquals("direction DSI", result.get(2).getDirection());
		assertEquals("SED", result.get(2).getSection());
		assertEquals(1, result.get(2).getNonAffecte());
		assertEquals(2, result.get(2).getNonDebute());
		assertEquals(3, result.get(2).getCree());
		assertEquals(4, result.get(2).getEnCours());
		assertEquals(5, result.get(2).getFinalise());
		assertEquals(6, result.get(2).getNbEaeControle());
		assertEquals(7, result.get(2).getNbEaeCAP());
		assertEquals(8, result.get(2).getNonDefini());
		assertEquals(9, result.get(2).getMini());
		assertEquals(10, result.get(2).getMoy());
		assertEquals(11, result.get(2).getMaxi());
		assertEquals(12, result.get(2).getChangClasse());
		
		// SIE
		assertEquals("direction DSI", result.get(3).getDirection());
		assertEquals("SIE", result.get(3).getSection());
		assertEquals(13, result.get(3).getNonAffecte());
		assertEquals(14, result.get(3).getNonDebute());
		assertEquals(15, result.get(3).getCree());
		assertEquals(16, result.get(3).getEnCours());
		assertEquals(17, result.get(3).getFinalise());
		assertEquals(18, result.get(3).getNbEaeControle());
		assertEquals(19, result.get(3).getNbEaeCAP());
		assertEquals(20, result.get(3).getNonDefini());
		assertEquals(21, result.get(3).getMini());
		assertEquals(22, result.get(3).getMoy());
		assertEquals(23, result.get(3).getMaxi());
		assertEquals(24, result.get(3).getChangClasse());
	}
}
