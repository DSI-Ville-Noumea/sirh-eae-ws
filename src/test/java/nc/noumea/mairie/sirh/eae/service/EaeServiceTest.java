package nc.noumea.mairie.sirh.eae.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.service.EaeService;

import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

public class EaeServiceTest {

	@Test
	public void testlistEaesByAgentId_WhenNoEaeForAgent_returnNull() {
		
		// Given
		TypedQuery<Eae> queryMock = mock(TypedQuery.class);
		when(queryMock.setParameter("idAgent", 9)).thenReturn(queryMock);
		when(queryMock.getResultList()).thenReturn(new ArrayList<Eae>());
		
		EntityManager entManagerMock = mock(EntityManager.class);
		when(entManagerMock.createQuery("select e from Eae e where e.idAgent = :idAgent", Eae.class)).thenReturn(queryMock);
		
		EaeService service = new EaeService();
		ReflectionTestUtils.setField(service, "eaeEntityManager", entManagerMock);
		
		// When
		List<Eae> result = service.listEaesByAgentId(9);
		
		// Then
		assertNotNull(result);
		assertEquals(0, result.size());

		verify(queryMock, times(1)).getResultList();
	}
	
	@Test
	public void testlistEaesByAgentId_When1EaeForAgent_returnListOf1Eae() {
		
		// Given
		List<Eae> resultOfQuery = new ArrayList<Eae>(Arrays.asList(new Eae()));
		
		TypedQuery<Eae> queryMock = mock(TypedQuery.class);
		when(queryMock.setParameter("idAgent", 9)).thenReturn(queryMock);
		when(queryMock.getResultList()).thenReturn(resultOfQuery);
		
		EntityManager entManagerMock = mock(EntityManager.class);
		when(entManagerMock.createQuery("select e from Eae e where e.idAgent = :idAgent", Eae.class)).thenReturn(queryMock);
		
		EaeService service = new EaeService();
		ReflectionTestUtils.setField(service, "eaeEntityManager", entManagerMock);
		
		// When
		List<Eae> result = service.listEaesByAgentId(9);
		
		// Then
		assertNotNull(result);
		assertEquals(1, result.size());
		
		verify(queryMock, times(1)).getResultList();
	}
	
}
