package nc.noumea.mairie.sirh.eae.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.service.IAgentService;

import org.junit.Test;
import org.springframework.mock.staticmock.MockStaticEntityMethods;
import org.springframework.test.util.ReflectionTestUtils;

@MockStaticEntityMethods
public class EaeServiceTest {

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
	
}
