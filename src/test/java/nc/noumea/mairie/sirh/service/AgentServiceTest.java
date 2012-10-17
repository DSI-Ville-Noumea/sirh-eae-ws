package nc.noumea.mairie.sirh.service;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import nc.noumea.mairie.sirh.domain.Agent;
import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluateur;
import nc.noumea.mairie.sirh.eae.domain.EaeEvalue;
import nc.noumea.mairie.sirh.eae.domain.EaeFichePoste;

import org.junit.Test;
import org.springframework.mock.staticmock.AnnotationDrivenStaticEntityMockingControl;
import org.springframework.mock.staticmock.MockStaticEntityMethods;

@MockStaticEntityMethods
public class AgentServiceTest {

	@Test
	public void testFillAgentForEaeEvaluateurWhenEaeEvaluateurIsValid() {

		// Given
		EaeEvaluateur eval = new EaeEvaluateur();
		eval.setIdAgent(998);

		// Mock the agent find static method to return our agent
		Agent agentToReturn = new Agent();
		agentToReturn.setIdAgent(998);
		agentToReturn.setNomPatronymique("Bilbo");

		Agent.findAgent(998);
		AnnotationDrivenStaticEntityMockingControl.expectReturn(agentToReturn);
		AnnotationDrivenStaticEntityMockingControl.playback();

		// When
		AgentService service = new AgentService();
		service.fillEaeEvaluateurWithAgent(eval);

		// Then
		assertEquals(agentToReturn, eval.getAgent());
	}
	
	@Test
	public void testFillAgentForEaeFichePoste() {

		// Given
		EaeFichePoste fdp = new EaeFichePoste();
		fdp.setIdAgentShd(998);

		// Mock the agent find static method to return our agent
		Agent agentToReturn = new Agent();
		agentToReturn.setIdAgent(998);
		agentToReturn.setNomPatronymique("Bilbo");

		Agent.findAgent(998);
		AnnotationDrivenStaticEntityMockingControl.expectReturn(agentToReturn);
		AnnotationDrivenStaticEntityMockingControl.playback();

		// When
		AgentService service = new AgentService();
		service.fillEaeFichePosteWithAgent(fdp);

		// Then
		assertEquals(agentToReturn, fdp.getAgentShd());
	}
	
	@Test
	public void testFillAgentForEaeEvalue() {

		// Given
		EaeEvalue evalue = new EaeEvalue();
		evalue.setIdAgent(995);

		// Mock the agent find static method to return our agent
		Agent agentToReturn = new Agent();
		agentToReturn.setIdAgent(995);
		agentToReturn.setNomPatronymique("Billy");

		Agent.findAgent(995);
		AnnotationDrivenStaticEntityMockingControl.expectReturn(agentToReturn);
		AnnotationDrivenStaticEntityMockingControl.playback();

		// When
		AgentService service = new AgentService();
		service.fillEaeEvalueWithAgent(evalue);

		// Then
		assertEquals(agentToReturn, evalue.getAgent());
	}

	@Test
	public void testfillEaeWithAgents_When1DelegataireAnd1ShdNoEvaluateurs_returnFilledInObject() {

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
		eaeToReturn.setEaeFichePoste(fdp);
		
		// Mock the Agent find static method to return our agent
		Agent agentToReturn = new Agent();
		agentToReturn.setIdAgent(idAgent);
		agentToReturn.setNomPatronymique("Bilbo");

		Agent.findAgent(idAgent);
		AnnotationDrivenStaticEntityMockingControl.expectReturn(agentToReturn);
		
		// Mock the Agent static method to return our agentShd
		Agent agentShdToReturn = new Agent();
		agentShdToReturn.setIdAgent(idAgentShd);
		agentShdToReturn.setNomPatronymique("yet another person shd");

		Agent.findAgent(fdp.getIdAgentShd());
		AnnotationDrivenStaticEntityMockingControl.expectReturn(agentShdToReturn);
				
		// Mock the Agent static method to return our agentDelegataire
		Agent agentDelegataireToReturn = new Agent();
		agentDelegataireToReturn.setIdAgent(idAgentDelegataire);
		agentDelegataireToReturn.setNomPatronymique("yet another person");

		Agent.findAgent(eaeToReturn.getIdAgentDelegataire());
		AnnotationDrivenStaticEntityMockingControl.expectReturn(agentDelegataireToReturn);
						
		AnnotationDrivenStaticEntityMockingControl.playback();

		// Set the mock as the entityManager of the service class
		AgentService service = new AgentService();
		
		// When
		Eae result = service.fillEaeWithAgents(eaeToReturn);

		// Then
		assertEquals(agentToReturn, result.getEaeEvalue().getAgent());
		assertEquals(agentToReturn, result.getEaeEvalue().getAgent());
		assertEquals(agentDelegataireToReturn, result.getAgentDelegataire());
	}

	@Test
	public void testfillEaeWithAgents_When2Evaluateurs_returnFilledInObject() {

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

		Agent.findAgent(evalue.getIdAgent());
		AnnotationDrivenStaticEntityMockingControl.expectReturn(agentToReturn);
		
		Agent agentToReturn2 = new Agent();
		agentToReturn2.setIdAgent(eval1.getIdAgent());
		agentToReturn2.setNomPatronymique("Bilbo2");

		Agent.findAgent(eval1.getIdAgent());
		AnnotationDrivenStaticEntityMockingControl.expectReturn(agentToReturn2);
				
		AnnotationDrivenStaticEntityMockingControl.playback();

		// Set the mock as the entityManager of the service class
		AgentService service = new AgentService();
		
		// When
		Eae result = service.fillEaeWithAgents(eaeToReturn);

		// Then
		assertEquals(agentToReturn, result.getEaeEvalue().getAgent());
		assertEquals(1, result.getEaeEvaluateurs().size());
		assertEquals(agentToReturn2, result.getEaeEvaluateurs().iterator().next().getAgent());
		
	}
	
	@Test
	public void testGetAgent_callAgentStaticFinder() {
		
		// Given
		Agent agentToReturn = new Agent();
		agentToReturn.setIdAgent(789);
		
		// Setup mock for static Agent finder method
		Agent.findAgent(789);
		AnnotationDrivenStaticEntityMockingControl.expectReturn(agentToReturn);
		AnnotationDrivenStaticEntityMockingControl.playback();
		
		AgentService service = new AgentService();
		
		// When
		Agent result = service.getAgent(789);
		
		// Then
		assertEquals(agentToReturn, result);
	}
}
