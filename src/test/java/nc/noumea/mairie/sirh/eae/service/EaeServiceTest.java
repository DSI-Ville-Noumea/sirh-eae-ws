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

import nc.noumea.mairie.sirh.domain.Agent;
import nc.noumea.mairie.sirh.eae.domain.Eae;

import org.junit.Test;
import org.springframework.mock.staticmock.AnnotationDrivenStaticEntityMockingControl;
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
	public void testlistEaesByAgentId_When1EaeForAgent_returnListOf1EaeWithAgentEvalueFilledIn() {

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

		// Mock the agent find static method to return our agent
		Agent agentToReturn = new Agent();
		agentToReturn.setIdAgent(9);
		agentToReturn.setNomPatronymique("Bilbo");

		Agent.findAgent(9);
		AnnotationDrivenStaticEntityMockingControl.expectReturn(agentToReturn);
		AnnotationDrivenStaticEntityMockingControl.playback();

		// Set the mock as the entityManager of the service class
		EaeService service = new EaeService();
		ReflectionTestUtils.setField(service, "eaeEntityManager", entManagerMock);

		// When
		List<Eae> result = service.listEaesByAgentId(9);

		// Then
		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(agentToReturn, result.get(0).getAgentEvalue());

		verify(queryMock, times(1)).getResultList();
	}
	
	@Test
	public void testlistEaesByAgentId_When1EaeForAgent_returnListOf1EaeWithAgentShdFilledIn() {

		// Given
		int idAgent = 9;
		Integer idAgentShd = 17;
		Eae eaeToReturn = new Eae();
		eaeToReturn.setIdAgent(idAgent);
		eaeToReturn.setIdAgentShd(idAgentShd);
		List<Eae> resultOfQuery = new ArrayList<Eae>(Arrays.asList(eaeToReturn));

		// Mock the query to return a specific result
		TypedQuery<Eae> queryMock = mock(TypedQuery.class);
		when(queryMock.setParameter("idAgent", idAgent)).thenReturn(queryMock);
		when(queryMock.getResultList()).thenReturn(resultOfQuery);

		// Mock the entity manager to return the mock query
		EntityManager entManagerMock = mock(EntityManager.class);
		when(
				entManagerMock.createQuery(
						"select e from Eae e where e.idAgent = :idAgent",
						Eae.class)).thenReturn(queryMock);

		// Mock the Agent find static method to return our agent
		Agent agentToReturn = new Agent();
		agentToReturn.setIdAgent(idAgent);
		agentToReturn.setNomPatronymique("Bilbo");

		Agent.findAgent(idAgent);
		AnnotationDrivenStaticEntityMockingControl.expectReturn(agentToReturn);
		
		// Mock the Agent static method to return our agentSHD
		Agent agentShdToReturn = new Agent();
		agentShdToReturn.setIdAgent(idAgentShd);
		agentShdToReturn.setNomPatronymique("somone else");

		Agent.findAgent(idAgentShd);
		AnnotationDrivenStaticEntityMockingControl.expectReturn(agentShdToReturn);
		AnnotationDrivenStaticEntityMockingControl.playback();

		// Set the mock as the entityManager of the service class
		EaeService service = new EaeService();
		ReflectionTestUtils.setField(service, "eaeEntityManager", entManagerMock);

		// When
		List<Eae> result = service.listEaesByAgentId(9);

		// Then
		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(agentShdToReturn, result.get(0).getAgentShd());

		verify(queryMock, times(1)).getResultList();
	}

	@Test
	public void testlistEaesByAgentId_When1EaeForAgent_returnListOf1EaeWithAgentDelegataireFilledIn() {

		// Given
		int idAgent = 9;
		Integer idAgentDelegataire = 29;
		Eae eaeToReturn = new Eae();
		eaeToReturn.setIdAgent(idAgent);
		eaeToReturn.setIdAgentDelegataire(idAgentDelegataire);
		List<Eae> resultOfQuery = new ArrayList<Eae>(Arrays.asList(eaeToReturn));

		// Mock the query to return a specific result
		TypedQuery<Eae> queryMock = mock(TypedQuery.class);
		when(queryMock.setParameter("idAgent", idAgent)).thenReturn(queryMock);
		when(queryMock.getResultList()).thenReturn(resultOfQuery);

		// Mock the entity manager to return the mock query
		EntityManager entManagerMock = mock(EntityManager.class);
		when(
				entManagerMock.createQuery(
						"select e from Eae e where e.idAgent = :idAgent",
						Eae.class)).thenReturn(queryMock);

		// Mock the Agent find static method to return our agent
		Agent agentToReturn = new Agent();
		agentToReturn.setIdAgent(idAgent);
		agentToReturn.setNomPatronymique("Bilbo");

		Agent.findAgent(idAgent);
		AnnotationDrivenStaticEntityMockingControl.expectReturn(agentToReturn);
		
		// Mock the Agent static method to return our agentSHD
		Agent agentDelegataireToReturn = new Agent();
		agentDelegataireToReturn.setIdAgent(idAgentDelegataire);
		agentDelegataireToReturn.setNomPatronymique("yet another person");

		Agent.findAgent(idAgentDelegataire);
		AnnotationDrivenStaticEntityMockingControl.expectReturn(agentDelegataireToReturn);
		
		AnnotationDrivenStaticEntityMockingControl.playback();

		// Set the mock as the entityManager of the service class
		EaeService service = new EaeService();
		ReflectionTestUtils.setField(service, "eaeEntityManager", entManagerMock);

		// When
		List<Eae> result = service.listEaesByAgentId(9);

		// Then
		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(agentDelegataireToReturn, result.get(0).getAgentDelegataire());

		verify(queryMock, times(1)).getResultList();
	}
}
