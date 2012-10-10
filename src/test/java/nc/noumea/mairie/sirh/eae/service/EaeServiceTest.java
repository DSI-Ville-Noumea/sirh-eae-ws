package nc.noumea.mairie.sirh.eae.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
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

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeEtatEnum;
import nc.noumea.mairie.sirh.service.IAgentService;
import nc.noumea.mairie.sirh.tools.IHelper;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.mock.staticmock.MockStaticEntityMethods;
import org.springframework.test.util.ReflectionTestUtils;

@MockStaticEntityMethods
public class EaeServiceTest {

	private static IHelper helperMock;
	
	@BeforeClass 
	public static void SetUp() {
		Calendar c = new GregorianCalendar();
		c.set(2007, 04, 19);
		
		helperMock = mock(IHelper.class);
		when(helperMock.getCurrentDate()).thenReturn(c.getTime());
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
		List<Eae> result = service.listEaesByAgentId(9);

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
		List<Eae> result = service.listEaesByAgentId(9);

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
		Eae eaeToReturn2 = new Eae();
		eaeToReturn.setIdAgent(92);
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
		List<Eae> result = service.listEaesByAgentId(2);

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
				
		// Mock the agent find static method to return our agent
		Eae eaeToInit = new Eae();
		eaeToInit.setIdEae(987);
		eaeToInit.setEtat(EaeEtatEnum.ND);
		
		// When
		service.initializeEae(eaeToInit);
		assertEquals(EaeEtatEnum.C, eaeToInit.getEtat());
		assertEquals(helperMock.getCurrentDate(), eaeToInit.getDateCreation());
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
			service.initializeEae(eaeToInit);
		}
		catch(EaeServiceException ex) {
			exMessage = ex.getMessage();
		}
		
		// Then
		assertEquals("Impossible de créer l'EAE id '987': le statut de cet Eae est 'En cours'.", exMessage);
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
						"select e from Eae e where e.idAgent = :idAgent orderby e.DateCreation desc",
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
						"select e from Eae e where e.idAgent = :idAgent orderby e.DateCreation desc",
						Eae.class)).thenReturn(queryMock);
		
		EaeService service = new EaeService();
		ReflectionTestUtils.setField(service, "eaeEntityManager", entManagerMock);
		
		// When
		Eae result = service.findLastEaeByAgentId(agentId);
		
		// Then
		assertEquals(firstEae, result);
		verify(queryMock, times(1)).getResultList();
	}
}
