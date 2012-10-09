package nc.noumea.mairie.sirh.eae.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
import org.springframework.mock.staticmock.AnnotationDrivenStaticEntityMockingControl;
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
		TypedQuery<Eae> queryMock = mock(TypedQuery.class);
		when(queryMock.setParameter("idAgent", 9)).thenReturn(queryMock);
		when(queryMock.getResultList()).thenReturn(new ArrayList<Eae>());

		EntityManager entManagerMock = mock(EntityManager.class);
		when(
				entManagerMock.createQuery(
						"select e from Eae e where e.idAgent = :idAgent",
						Eae.class)).thenReturn(queryMock);

		EaeService service = new EaeService();
		ReflectionTestUtils.setField(service, "eaeEntityManager",
				entManagerMock);

		// When
		List<Eae> result = service.listEaesByAgentId(9);

		// Then
		assertNotNull(result);
		assertEquals(0, result.size());

		verify(queryMock, times(1)).getResultList();
	}

	@Test
	public void testlistEaesByAgentId_When1EaeForAgent_returnListOf1EaeWithAgentsFilledIn() {

		// Given
		Eae eaeToReturn = new Eae();
		eaeToReturn.setIdAgent(9);
		List<Eae> resultOfQuery = new ArrayList<Eae>(Arrays.asList(eaeToReturn));

		// Mock the query to return a specific result
		TypedQuery<Eae> queryMock = mock(TypedQuery.class);
		when(queryMock.setParameter("idAgent", 9)).thenReturn(queryMock);
		when(queryMock.getResultList()).thenReturn(resultOfQuery);

		// Mock the entity manager to return the mock query
		EntityManager entManagerMock = mock(EntityManager.class);
		when(
				entManagerMock.createQuery(
						"select e from Eae e where e.idAgent = :idAgent",
						Eae.class)).thenReturn(queryMock);

		// Mock the AgentService
		IAgentService agentServiceMock = mock(IAgentService.class);
				
		// Set the mock as the entityManager of the service class
		EaeService service = new EaeService();
		ReflectionTestUtils.setField(service, "eaeEntityManager", entManagerMock);
		ReflectionTestUtils.setField(service, "agentService", agentServiceMock);
		
		// When
		List<Eae> result = service.listEaesByAgentId(9);

		// Then
		assertNotNull(result);
		assertEquals(1, result.size());
		
		verify(queryMock, times(1)).getResultList();
		verify(agentServiceMock, times(1)).fillEaeWithAgents(eaeToReturn);
	}
	
	@Test
	public void testlistEaesByAgentId_When2EaesForAgent_returnListOf2EaeWithAgentsFilledIn() {

		// Given
		Eae eaeToReturn = new Eae();
		eaeToReturn.setIdAgent(9);
		Eae eaeToReturn2 = new Eae();
		eaeToReturn.setIdAgent(92);
		List<Eae> resultOfQuery = new ArrayList<Eae>(Arrays.asList(eaeToReturn, eaeToReturn2));

		// Mock the query to return a specific result
		TypedQuery<Eae> queryMock = mock(TypedQuery.class);
		when(queryMock.setParameter("idAgent", 9)).thenReturn(queryMock);
		when(queryMock.getResultList()).thenReturn(resultOfQuery);

		// Mock the entity manager to return the mock query
		EntityManager entManagerMock = mock(EntityManager.class);
		when(
				entManagerMock.createQuery(
						"select e from Eae e where e.idAgent = :idAgent",
						Eae.class)).thenReturn(queryMock);

		// Mock the AgentService
		IAgentService agentServiceMock = mock(IAgentService.class);
				
		// Set the mock as the entityManager of the service class
		EaeService service = new EaeService();
		ReflectionTestUtils.setField(service, "eaeEntityManager", entManagerMock);
		ReflectionTestUtils.setField(service, "agentService", agentServiceMock);
		
		// When
		List<Eae> result = service.listEaesByAgentId(9);

		// Then
		assertNotNull(result);
		assertEquals(2, result.size());
		
		verify(queryMock, times(1)).getResultList();
		verify(agentServiceMock, times(1)).fillEaeWithAgents(eaeToReturn);
		verify(agentServiceMock, times(1)).fillEaeWithAgents(eaeToReturn2);
	}
	
	@Test
	public void testInitilizeEae_eaeDoesNotExist_throwException() {
		// Given
		EaeService service = new EaeService();
		
		// Mock the agent find static method to return our agent
		Eae eaeToReturn = null;

		Eae.findEae(987);
		AnnotationDrivenStaticEntityMockingControl.expectReturn(eaeToReturn);
		AnnotationDrivenStaticEntityMockingControl.playback();
				
		// When
		try {
			service.initializeEae(987);
		}
		catch(EaeServiceException ex) {
			// Then
			assertEquals("Impossible de créer l'EAE id '987'", ex.getMessage());
		}
	}
	
	@Test
	public void testInitilizeEae_eaeExists_setCreationDateAndStatus() throws EaeServiceException{
		// Given
		EaeService service = new EaeService();
		ReflectionTestUtils.setField(service, "helper", helperMock);
				
		// Mock the agent find static method to return our agent
		Eae eaeToReturn = new Eae();
		eaeToReturn.setIdEae(987);
		
		Eae.findEae(eaeToReturn.getIdEae());
		AnnotationDrivenStaticEntityMockingControl.expectReturn(eaeToReturn);
		AnnotationDrivenStaticEntityMockingControl.playback();
				
		// When
		service.initializeEae(987);
		assertEquals(EaeEtatEnum.C, eaeToReturn.getEtat());
		assertEquals(helperMock.getCurrentDate(), eaeToReturn.getDateCreation());
	}
}
