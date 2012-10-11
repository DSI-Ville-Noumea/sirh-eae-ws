package nc.noumea.mairie.sirh.eae.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import nc.noumea.mairie.sirh.domain.Agent;
import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluateur;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluation;
import nc.noumea.mairie.sirh.eae.domain.EaeFichePoste;
import nc.noumea.mairie.sirh.eae.domain.EaePlanAction;
import nc.noumea.mairie.sirh.eae.domain.EaeResultat;
import nc.noumea.mairie.sirh.eae.domain.EaeTypeObjectif;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeEtatEnum;
import nc.noumea.mairie.sirh.eae.dto.EaeListItemDto;
import nc.noumea.mairie.sirh.service.IAgentService;
import nc.noumea.mairie.sirh.tools.IHelper;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.mock.staticmock.AnnotationDrivenStaticEntityMockingControl;
import org.springframework.mock.staticmock.MockStaticEntityMethods;
import org.springframework.test.util.ReflectionTestUtils;

@MockStaticEntityMethods
public class EaeServiceTest {

	private static IHelper helperMock;
	private static EaeTypeObjectif t1, t2;
	
	@BeforeClass 
	public static void SetUp() {
		Calendar c = new GregorianCalendar();
		c.set(2007, 04, 19);
		
		helperMock = mock(IHelper.class);
		when(helperMock.getCurrentDate()).thenReturn(c.getTime());
		
		t1 = new EaeTypeObjectif();
		t1.setLibelleTypeObjectif("INDIVIDUEL");
		t2 = new EaeTypeObjectif();
		t2.setLibelleTypeObjectif("EQUIPE");
	}
	
	@Test
	public void testlistEaesByAgentId_WhenNoEaeForAgent_returnNull() {

		// Given
		List<Integer> eaeIds = new ArrayList<Integer>();
		ISirhWsConsumer consumerMock = mock(ISirhWsConsumer.class);
		when(consumerMock.getListOfEaesForAgentId(9)).thenReturn(eaeIds);
		
		EaeService service = new EaeService();
		ReflectionTestUtils.setField(service, "sirhWsConsumer", consumerMock);

		// When
		List<EaeListItemDto> result = service.listEaesByAgentId(9);

		// Then
		assertNotNull(result);
		assertEquals(0, result.size());

		verify(consumerMock, times(1)).getListOfEaesForAgentId(9);
	}

	@Test
	public void testlistEaesByAgentId_When1EaeForAgent_returnListOf1EaeWithAgentsFilledIn() {

		// Given
		List<Integer> eaeIds = new ArrayList<Integer>(Arrays.asList(98));
		ISirhWsConsumer consumerMock = mock(ISirhWsConsumer.class);
		when(consumerMock.getListOfEaesForAgentId(9)).thenReturn(eaeIds);
		
		Eae eaeToReturn = new Eae();
		eaeToReturn.setIdAgent(9);
		eaeToReturn.setEtat(EaeEtatEnum.EC);
		List<Eae> resultOfQuery = new ArrayList<Eae>(Arrays.asList(eaeToReturn));

		// Mock the query to return a specific result
		TypedQuery<Eae> queryMock = mock(TypedQuery.class);
		when(queryMock.setParameter("eaeIds", eaeIds)).thenReturn(queryMock);
		when(queryMock.getResultList()).thenReturn(resultOfQuery);

		EntityManager entManagerMock = mock(EntityManager.class);
		when(
				entManagerMock.createQuery(
						"select e from Eae e where e.idEae in (:eaeIds)",
						Eae.class)).thenReturn(queryMock);

		// Mock the AgentService
		IAgentService agentServiceMock = mock(IAgentService.class);
				
		// Set the mock as the entityManager of the service class
		EaeService service = new EaeService();
		ReflectionTestUtils.setField(service, "eaeEntityManager", entManagerMock);
		ReflectionTestUtils.setField(service, "agentService", agentServiceMock);
		ReflectionTestUtils.setField(service, "sirhWsConsumer", consumerMock);
		
		// When
		List<EaeListItemDto> result = service.listEaesByAgentId(9);

		// Then
		assertNotNull(result);
		assertEquals(1, result.size());
		
		verify(consumerMock, times(1)).getListOfEaesForAgentId(9);
		verify(queryMock, times(1)).getResultList();
		verify(agentServiceMock, times(1)).fillEaeWithAgents(eaeToReturn);
	}
	
	@Test
	public void testlistEaesByAgentId_When2EaesForAgent_returnListOf2EaeWithAgentsFilledIn() {

		// Given
		List<Integer> eaeIds = new ArrayList<Integer>(Arrays.asList(98, 67));
		ISirhWsConsumer consumerMock = mock(ISirhWsConsumer.class);
		when(consumerMock.getListOfEaesForAgentId(2)).thenReturn(eaeIds);
		
		Eae eaeToReturn = new Eae();
		eaeToReturn.setIdAgent(9);
		eaeToReturn.setEtat(EaeEtatEnum.EC);
		Eae eaeToReturn2 = new Eae();
		eaeToReturn2.setIdAgent(92);
		eaeToReturn2.setEtat(EaeEtatEnum.EC);
		List<Eae> resultOfQuery = new ArrayList<Eae>(Arrays.asList(eaeToReturn, eaeToReturn2));

		// Mock the query to return a specific result
		TypedQuery<Eae> queryMock = mock(TypedQuery.class);
		when(queryMock.setParameter("eaeIds", eaeIds)).thenReturn(queryMock);
		when(queryMock.getResultList()).thenReturn(resultOfQuery);

		EntityManager entManagerMock = mock(EntityManager.class);
		when(
				entManagerMock.createQuery(
						"select e from Eae e where e.idEae in (:eaeIds)",
						Eae.class)).thenReturn(queryMock);

		// Mock the AgentService
		IAgentService agentServiceMock = mock(IAgentService.class);
				
		// Set the mock as the entityManager of the service class
		EaeService service = new EaeService();
		ReflectionTestUtils.setField(service, "eaeEntityManager", entManagerMock);
		ReflectionTestUtils.setField(service, "agentService", agentServiceMock);
		ReflectionTestUtils.setField(service, "sirhWsConsumer", consumerMock);
		
		// When
		List<EaeListItemDto> result = service.listEaesByAgentId(2);

		// Then
		assertNotNull(result);
		assertEquals(2, result.size());
		
		verify(consumerMock, times(1)).getListOfEaesForAgentId(2);
		verify(queryMock, times(1)).getResultList();
		verify(agentServiceMock, times(1)).fillEaeWithAgents(eaeToReturn);
		verify(agentServiceMock, times(1)).fillEaeWithAgents(eaeToReturn2);
	}
	
	@Test
	public void testInitilizeEae_setCreationDateAndStatus() throws EaeServiceException {
		// Given
		EaeService service = new EaeService();
		ReflectionTestUtils.setField(service, "helper", helperMock);
				
		// Mock the EAE
		Eae eaeToInit = new Eae();
		eaeToInit.setIdEae(987);
		eaeToInit.setEtat(EaeEtatEnum.ND);
				
		// When
		service.initializeEae(eaeToInit, null);
		
		// Then
		assertEquals(helperMock.getCurrentDate(), eaeToInit.getDateCreation());
	}
	
	@Test
	public void testInitilizeEae_noPreviousEaes_createNoEaeResultat() throws EaeServiceException {
		// Given
		EaeService service = new EaeService();
		ReflectionTestUtils.setField(service, "helper", helperMock);
				
		// Mock the EAE 
		Eae eaeToInit = new Eae();
		eaeToInit.setIdEae(987);
		eaeToInit.setEtat(EaeEtatEnum.ND);
		
		// When
		service.initializeEae(eaeToInit, null);
		
		// Then
		assertTrue(eaeToInit.getEaeResultats().isEmpty());
	}
	
	@Test
	public void testInitilizeEae_1PreviousEae_createEaeResultatFromPreviousPlanActionAndCopyNotes() throws EaeServiceException {
		// Given
		EaeService service = new EaeService();
		ReflectionTestUtils.setField(service, "helper", helperMock);
				
		// Mock EAEs list (current, previous)
		Eae eaeToInit = new Eae();
		eaeToInit.setIdEae(987);
		eaeToInit.setEtat(EaeEtatEnum.ND);
		
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
		eval.setNoteAnnee(13);
		eval.setNoteAnneeN1(14);
		eval.setNoteAnneeN2(15);
		eval.setNoteAnneeN3(16);
		
		previousEae.setEaeEvaluation(eval);
		
		// When
		service.initializeEae(eaeToInit, previousEae);
		
		// Then
		assertFalse(eaeToInit.getEaeResultats().isEmpty());
		assertNull(eaeToInit.getEaeEvaluation().getNoteAnnee());
		assertEquals(new Integer(13), eaeToInit.getEaeEvaluation().getNoteAnneeN1());
		assertEquals(new Integer(14), eaeToInit.getEaeEvaluation().getNoteAnneeN2());
		assertEquals(new Integer(15), eaeToInit.getEaeEvaluation().getNoteAnneeN3());
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
		EaeService service = new EaeService();
		ReflectionTestUtils.setField(service, "helper", helperMock);
				
		// Mock the agent find static method to return our agent
		Eae eaeToInit = new Eae();
		eaeToInit.setIdEae(987);
		eaeToInit.setEtat(EaeEtatEnum.EC);
		
		// When
		String exMessage = "";
		
		try {
			service.initializeEae(eaeToInit, null);
		}
		catch(EaeServiceException ex) {
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
		when(
				entManagerMock.createQuery(
						"select e from Eae e where e.idAgent = :idAgent order by e.dateCreation desc",
						Eae.class)).thenReturn(queryMock);
		
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
		when(
				entManagerMock.createQuery(
						"select e from Eae e where e.idAgent = :idAgent order by e.dateCreation desc",
						Eae.class)).thenReturn(queryMock);
		
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
		
		EaeService service = new EaeService();
		
		try {
			// When
			service.startEae(eaeToStart);
		}
		catch (EaeServiceException ex) {
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
		
		EaeService service = new EaeService();

		// When
		service.startEae(eaeToStart);
		
		// Then
		assertEquals(EaeEtatEnum.EC, eaeToStart.getEtat());
	}
	
	@Test
	public void testStartEae_eaeIsEC_doNothing() throws EaeServiceException {
		
		// Given
		Eae eaeToStart = new Eae();
		eaeToStart.setIdEae(987);
		eaeToStart.setEtat(EaeEtatEnum.EC);
		
		EaeService service = new EaeService();

		// When
		service.startEae(eaeToStart);
		
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
		}
		catch (EaeServiceException ex) {
			// Then
			assertEquals("Impossible de réinitialiser l'EAE id '987': le statut de cet Eae est 'Finalisé'.", ex.getMessage());
		}
		
		assertEquals(eval, eaeToDelete.getEaeEvaluateurs().iterator().next());
	}
	
	@Test
	public void testResetEaeEvaluateur_eaeIsCree_setEtatToNDAndResetEvaluateurs() throws EaeServiceException {
		
		// Given
		Eae eaeToDelete = spy(new Eae());
		org.mockito.Mockito.doNothing().when(eaeToDelete).flush();
		eaeToDelete.setIdEae(987);
		eaeToDelete.setEtat(EaeEtatEnum.C);
		
		EaeEvaluateur eval = new EaeEvaluateur();
		eval.setIdAgent(9009);
		eaeToDelete.getEaeEvaluateurs().add(eval);
		
		EaeFichePoste fdp = new EaeFichePoste();
		fdp.setIdAgentShd(10000);
		fdp.setFonctionResponsable("fonction responsable");
		
		eaeToDelete.setEaeFichePoste(fdp);
		
		EaeService service = new EaeService();

		// When
		service.resetEaeEvaluateur(eaeToDelete);
		
		// Then
		assertEquals(EaeEtatEnum.ND, eaeToDelete.getEtat());
		assertEquals(10000, eaeToDelete.getEaeEvaluateurs().iterator().next().getIdAgent());
		assertEquals("fonction responsable", eaeToDelete.getEaeEvaluateurs().iterator().next().getFonction());
	}
	
	@Test
	public void testResetEaeEvaluateur_eaeIsEC_setEtatToNDAndResetEvaluateurs() throws EaeServiceException {
		
		// Given
		Eae eaeToDelete = spy(new Eae());
		org.mockito.Mockito.doNothing().when(eaeToDelete).flush();
		eaeToDelete.setIdEae(987);
		eaeToDelete.setEtat(EaeEtatEnum.EC);
		
		EaeEvaluateur eval = new EaeEvaluateur();
		eval.setIdAgent(9009);
		eaeToDelete.getEaeEvaluateurs().add(eval);
		
		EaeFichePoste fdp = new EaeFichePoste();
		fdp.setIdAgentShd(10000);
		fdp.setFonctionResponsable("fonction responsable");
		
		eaeToDelete.setEaeFichePoste(fdp);
		
		EaeService service = new EaeService();

		// When
		service.resetEaeEvaluateur(eaeToDelete);
		
		// Then
		assertEquals(EaeEtatEnum.ND, eaeToDelete.getEtat());
		assertEquals(10000, eaeToDelete.getEaeEvaluateurs().iterator().next().getIdAgent());
		assertEquals("fonction responsable", eaeToDelete.getEaeEvaluateurs().iterator().next().getFonction());
	}
	
	@Test
	public void testResetEaeEvaluateur_eaeIsND_resetEvaluateurs() throws EaeServiceException {
		
		// Given
		Eae eaeToDelete = spy(new Eae());
		org.mockito.Mockito.doNothing().when(eaeToDelete).flush();
		 
		eaeToDelete.setIdEae(987);
		eaeToDelete.setEtat(EaeEtatEnum.ND);
		
		EaeEvaluateur eval = new EaeEvaluateur();
		eval.setIdAgent(9009);
		eaeToDelete.getEaeEvaluateurs().add(eval);

		EaeFichePoste fdp = new EaeFichePoste();
		fdp.setIdAgentShd(10000);
		fdp.setFonctionResponsable("fonction responsable");
		
		eaeToDelete.setEaeFichePoste(fdp);
		
		EaeService service = new EaeService();

		// When
		service.resetEaeEvaluateur(eaeToDelete);
		
		// Then
		assertEquals(EaeEtatEnum.ND, eaeToDelete.getEtat());
		assertEquals(10000, eaeToDelete.getEaeEvaluateurs().iterator().next().getIdAgent());
		assertEquals("fonction responsable", eaeToDelete.getEaeEvaluateurs().iterator().next().getFonction());
	}
	
	@Test
	public void testSetDelegataire_DelegataireExists_SetAgentId() throws EaeServiceException {
		// Given
		Eae eae = new Eae();
		eae.setIdAgentDelegataire(123);
		Integer idAgentDelegataire = 1789;
		
		// Mock the agent find static method to return our agent
		Agent agentToReturn = new Agent();
		agentToReturn.setIdAgent(idAgentDelegataire);
		agentToReturn.setNomPatronymique("Bilbo");

		Agent.findAgent(idAgentDelegataire);
		AnnotationDrivenStaticEntityMockingControl.expectReturn(agentToReturn);
		AnnotationDrivenStaticEntityMockingControl.playback();
		
		EaeService service = new EaeService();
		
		// When
		service.setDelegataire(eae, idAgentDelegataire);
		
		// Then
		assertEquals(idAgentDelegataire, eae.getIdAgentDelegataire());
	}
	
	@Test
	public void testSetDelegataire_DelegataireDoesNotExist_throwException() {
		// Given
		Eae eae = new Eae();
		eae.setIdAgentDelegataire(123);
		Integer idAgentDelegataire = 1789;
		
		// Mock the agent find static method to return our null agent
		Agent agentToReturn = null;
	
		Agent.findAgent(idAgentDelegataire);
		AnnotationDrivenStaticEntityMockingControl.expectReturn(agentToReturn);
		AnnotationDrivenStaticEntityMockingControl.playback();
		
		EaeService service = new EaeService();
		
		try {
			// When
			service.setDelegataire(eae, idAgentDelegataire);
		}
		catch (EaeServiceException ex) {
			// Then
			assertEquals("Impossible d'affecter l'agent '1789' en tant que délégataire: cet Agent n'existe pas.", ex.getMessage());
			assertEquals(new Integer(123), eae.getIdAgentDelegataire());
		}
		
	}
}
