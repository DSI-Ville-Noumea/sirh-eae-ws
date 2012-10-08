package nc.noumea.mairie.sirh.service;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import nc.noumea.mairie.sirh.domain.Agent;
import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluateur;
import nc.noumea.mairie.sirh.service.AgentService;

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
	public void testfillEaeWithAgents_When1DelegataireAnd1ShdNoEvaluateurs_returnFilledInObject() {

		// Given
		int idAgent = 9;
		Integer idAgentDelegataire = 29;
		Integer idAgentShd = 35;
		Eae eaeToReturn = new Eae();
		eaeToReturn.setIdAgent(idAgent);
		eaeToReturn.setIdAgentDelegataire(idAgentDelegataire);
		eaeToReturn.setIdAgentShd(idAgentShd);
		
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

		Agent.findAgent(eaeToReturn.getIdAgentShd());
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
		assertEquals(agentToReturn, result.getAgentEvalue());
		assertEquals(agentDelegataireToReturn, result.getAgentDelegataire());
		assertEquals(agentShdToReturn, result.getAgentShd());
	}
	
	@Test
	public void testfillEaeWithAgents_When2Evaluateurs_returnFilledInObject() {

		// Given
		int idAgent = 9;
		Eae eaeToReturn = new Eae();
		eaeToReturn.setIdAgent(idAgent);
		
		Set<EaeEvaluateur> evals = new HashSet<EaeEvaluateur>();
		EaeEvaluateur eval1 = new EaeEvaluateur();
		eval1.setIdAgent(56);
		evals.add(eval1);
		eaeToReturn.setEaeEvaluateurs(evals);
		
		// Mock the Agent find static method to return our agent
		Agent agentToReturn = new Agent();
		agentToReturn.setIdAgent(eaeToReturn.getIdAgent());
		agentToReturn.setNomPatronymique("Bilbo");

		Agent.findAgent(eaeToReturn.getIdAgent());
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
		assertEquals(agentToReturn, result.getAgentEvalue());
		assertEquals(1, result.getEaeEvaluateurs().size());
		assertEquals(agentToReturn2, result.getEaeEvaluateurs().iterator().next().getAgent());
		
	}
}
