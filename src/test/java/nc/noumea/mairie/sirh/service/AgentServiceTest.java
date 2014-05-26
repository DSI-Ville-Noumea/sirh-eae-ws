package nc.noumea.mairie.sirh.service;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import nc.noumea.mairie.sirh.domain.Agent;
import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluateur;
import nc.noumea.mairie.sirh.eae.domain.EaeEvalue;
import nc.noumea.mairie.sirh.eae.domain.EaeFichePoste;
import nc.noumea.mairie.sirh.ws.ISirhWsConsumer;
import nc.noumea.mairie.sirh.ws.SirhWSConsumerException;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.mock.staticmock.MockStaticEntityMethods;
import org.springframework.test.util.ReflectionTestUtils;

@MockStaticEntityMethods
public class AgentServiceTest {

	@Test
	public void testFillAgentForEaeEvaluateurWhenEaeEvaluateurIsValid() throws SirhWSConsumerException {

		// Given
		EaeEvaluateur eval = new EaeEvaluateur();
		eval.setIdAgent(998);

		// Mock the agent find static method to return our agent
		Agent agentToReturn = new Agent();
		agentToReturn.setIdAgent(998);
		agentToReturn.setNomPatronymique("Bilbo");

		ISirhWsConsumer sirhWsConsumer = Mockito.mock(ISirhWsConsumer.class);
		Mockito.when(sirhWsConsumer.getAgent(998)).thenReturn(agentToReturn);

		AgentService service = new AgentService();
		ReflectionTestUtils.setField(service, "sirhWsConsumer", sirhWsConsumer);

		// When
		EaeEvaluateur eva = service.fillEaeEvaluateurWithAgent(eval);

		// Then
		assertEquals(agentToReturn, eval.getAgent());
		assertEquals(eval.getAgent(), eva.getAgent());
	}

	@Test
	public void testFillAgentForEaeFichePoste() throws SirhWSConsumerException {

		// Given
		EaeFichePoste fdp = new EaeFichePoste();
		fdp.setIdAgentShd(998);

		// Mock the agent find static method to return our agent
		Agent agentToReturn = new Agent();
		agentToReturn.setIdAgent(998);
		agentToReturn.setNomPatronymique("Bilbo");

		ISirhWsConsumer sirhWsConsumer = Mockito.mock(ISirhWsConsumer.class);
		Mockito.when(sirhWsConsumer.getAgent(998)).thenReturn(agentToReturn);
		
		AgentService service = new AgentService();
		ReflectionTestUtils.setField(service, "sirhWsConsumer", sirhWsConsumer);

		// When
		service.fillEaeFichePosteWithAgent(fdp);

		// Then
		assertEquals(agentToReturn, fdp.getAgentShd());
	}

	@Test
	public void testFillAgentForEaeEvalue() throws SirhWSConsumerException {

		// Given
		EaeEvalue evalue = new EaeEvalue();
		evalue.setIdAgent(995);

		// Mock the agent find static method to return our agent
		Agent agentToReturn = new Agent();
		agentToReturn.setIdAgent(995);
		agentToReturn.setNomPatronymique("Billy");

		ISirhWsConsumer sirhWsConsumer = Mockito.mock(ISirhWsConsumer.class);
		Mockito.when(sirhWsConsumer.getAgent(995)).thenReturn(agentToReturn);
		

		AgentService service = new AgentService();
		ReflectionTestUtils.setField(service, "sirhWsConsumer", sirhWsConsumer);

		// When
		service.fillEaeEvalueWithAgent(evalue);

		// Then
		assertEquals(agentToReturn, evalue.getAgent());
	}

	@Test
	public void testfillEaeWithAgents_When1DelegataireAnd1ShdNoEvaluateurs_returnFilledInObject() throws SirhWSConsumerException {

		// Given
		int idAgent = 9;
		Integer idAgentDelegataire = 29;
		Integer idAgentShd = 35;
		Eae eaeToReturn = new Eae();
		EaeEvalue evalue = new EaeEvalue();
		evalue.setIdAgent(idAgent);
		eaeToReturn.setEaeEvalue(evalue);
		eaeToReturn.setIdAgentDelegataire(idAgentDelegataire);
		EaeFichePoste fdp = new EaeFichePoste();
		fdp.setIdAgentShd(idAgentShd);
		eaeToReturn.getEaeFichePostes().add(fdp);

		// Mock the Agent find static method to return our agent
		Agent agentToReturn = new Agent();
		agentToReturn.setIdAgent(idAgent);
		agentToReturn.setNomPatronymique("Bilbo");

		// Mock the Agent static method to return our agentShd
		Agent agentShdToReturn = new Agent();
		agentShdToReturn.setIdAgent(idAgentShd);
		agentShdToReturn.setNomPatronymique("yet another person shd");

		// Mock the Agent static method to return our agentDelegataire
		Agent agentDelegataireToReturn = new Agent();
		agentDelegataireToReturn.setIdAgent(idAgentDelegataire);
		agentDelegataireToReturn.setNomPatronymique("yet another person");

		// Set the mock as the entityManager of the service class
		ISirhWsConsumer sirhWsConsumer = Mockito.mock(ISirhWsConsumer.class);
		Mockito.when(sirhWsConsumer.getAgent(fdp.getIdAgentShd())).thenReturn(agentShdToReturn);
		Mockito.when(sirhWsConsumer.getAgent(idAgent)).thenReturn(agentToReturn);
		Mockito.when(sirhWsConsumer.getAgent(eaeToReturn.getIdAgentDelegataire())).thenReturn(agentDelegataireToReturn);
		
		AgentService service = new AgentService();
		ReflectionTestUtils.setField(service, "sirhWsConsumer", sirhWsConsumer);

		// When
		Eae result = service.fillEaeWithAgents(eaeToReturn);

		// Then
		assertEquals(agentToReturn, result.getEaeEvalue().getAgent());
		assertEquals(agentToReturn, result.getEaeEvalue().getAgent());
		assertEquals(agentDelegataireToReturn, result.getAgentDelegataire());
	}

	@Test
	public void testfillEaeWithAgents_When2Evaluateurs_returnFilledInObject() throws SirhWSConsumerException {

		// Given
		int idAgent = 9;
		Eae eaeToReturn = new Eae();
		EaeEvalue evalue = new EaeEvalue();
		evalue.setIdAgent(idAgent);
		eaeToReturn.setEaeEvalue(evalue);

		Set<EaeEvaluateur> evals = new HashSet<EaeEvaluateur>();
		EaeEvaluateur eval1 = new EaeEvaluateur();
		eval1.setIdAgent(56);
		evals.add(eval1);
		eaeToReturn.setEaeEvaluateurs(evals);

		// Mock the Agent find static method to return our agent
		Agent agentToReturn = new Agent();
		agentToReturn.setIdAgent(evalue.getIdAgent());
		agentToReturn.setNomPatronymique("Bilbo");

		Agent agentToReturn2 = new Agent();
		agentToReturn2.setIdAgent(eval1.getIdAgent());
		agentToReturn2.setNomPatronymique("Bilbo2");

		ISirhWsConsumer sirhWsConsumer = Mockito.mock(ISirhWsConsumer.class);
		Mockito.when(sirhWsConsumer.getAgent(evalue.getIdAgent())).thenReturn(agentToReturn);
		Mockito.when(sirhWsConsumer.getAgent(eval1.getIdAgent())).thenReturn(agentToReturn2);

		AgentService service = new AgentService();
		ReflectionTestUtils.setField(service, "sirhWsConsumer", sirhWsConsumer);

		// When
		Eae result = service.fillEaeWithAgents(eaeToReturn);

		// Then
		assertEquals(agentToReturn, result.getEaeEvalue().getAgent());
		assertEquals(1, result.getEaeEvaluateurs().size());
		assertEquals(agentToReturn2, result.getEaeEvaluateurs().iterator().next().getAgent());

	}

	@Test
	public void testGetAgent_callAgentStaticFinder() throws SirhWSConsumerException {

		// Given
		Agent agentToReturn = new Agent();
		agentToReturn.setIdAgent(789);
		
		ISirhWsConsumer sirhWsConsumer = Mockito.mock(ISirhWsConsumer.class);
		Mockito.when(sirhWsConsumer.getAgent(789)).thenReturn(agentToReturn);

		AgentService service = new AgentService();
		ReflectionTestUtils.setField(service, "sirhWsConsumer", sirhWsConsumer);

		// When
		Agent result = service.getAgent(789);

		// Then
		assertEquals(agentToReturn, result);
	}
}
