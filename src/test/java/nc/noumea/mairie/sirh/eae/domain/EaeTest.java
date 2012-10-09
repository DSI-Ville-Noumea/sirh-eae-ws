package nc.noumea.mairie.sirh.eae.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import nc.noumea.mairie.sirh.domain.Agent;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeEtatEnum;

import org.junit.Test;

import flexjson.PathExpression;

public class EaeTest {

	@Test
	public void testGetSerializerForEaeList_ListAllIncludesExcludes() {
		
		// Given
		Eae eae = new Eae();
		eae.setIdEae(18);
		eae.setIdAgent(999);
		eae.setEtat(EaeEtatEnum.C);
		eae.setCap(true);
		eae.setDocAttache(true);
		eae.setDateCreation(new Date(2012, 02, 02));
		eae.setDateFinalisation(new Date(2012, 02, 02));
		eae.setDateControle(new Date(2012, 02, 02));
		eae.setDureeEntretien(56);
		
		EaeFichePoste fdp = new EaeFichePoste();
		fdp.setSectionService("section service bbudile");
		fdp.setService("the service");
		fdp.setDirectionService("direction service bidule");
		
		eae.setEaeFichePoste(fdp);
		
		// When
		String result = Eae.getSerializerForEaeList().serialize(eae);
		List<PathExpression> includes = Eae.getSerializerForEaeList().getIncludes();
		List<PathExpression> excludes = Eae.getSerializerForEaeList().getExcludes();
		
		// Then
		assertEquals(13, includes.size());
		assertEquals("[agentEvalue]", includes.get(0).toString());
		assertEquals("[etat]", includes.get(1).toString());
		assertEquals("[cap]", includes.get(2).toString());
		assertEquals("[docAttache]", includes.get(3).toString());
		assertEquals("[dateCreation]", includes.get(4).toString());
		assertEquals("[dateFinalisation]", includes.get(5).toString());
		assertEquals("[dateControle]", includes.get(6).toString());
		assertEquals("[dureeEntretien]", includes.get(7).toString());
		assertEquals("[agentDelegataire]", includes.get(8).toString());
		assertEquals("[eaeEvaluation,avisShd]", includes.get(9).toString());
		assertEquals("[idEae]", includes.get(10).toString());
		assertEquals("[eaeEvaluateurs]", includes.get(11).toString());
		assertEquals("[eaeFichePoste]", includes.get(12).toString());
		
		assertEquals(1, excludes.size());
		assertEquals("[*]", excludes.get(0).toString());
	}
	
	@Test
	public void testGetSerializerForEaeListWithNullInteger() {
		
		// Given
		Eae eae = new Eae();
		eae.setIdEae(18);
		eae.setIdAgent(999);
		eae.setEtat(EaeEtatEnum.C);
		eae.setCap(true);
		eae.setDocAttache(true);
		eae.setDateCreation(null);
		eae.setDateFinalisation(null);
		eae.setDateControle(null);
		eae.setDureeEntretien(null);
		
		EaeFichePoste fdp = new EaeFichePoste();
		fdp.setSectionService("section service bbudile");
		fdp.setService("the service");
		fdp.setDirectionService("direction service bidule");
		
		eae.setEaeFichePoste(fdp);
		
		String expectedResult = "{\"agentDelegataire\":null,\"agentEvalue\":null,\"cap\":true,\"dateControle\":null,\"dateCreation\":null,\"dateFinalisation\":null,\"docAttache\":true,\"dureeEntretien\":null,\"eaeEvaluateurs\":null,\"eaeEvaluation\":null,\"directionService\":\"direction service bidule\",\"sectionService\":\"section service bbudile\",\"service\":\"the service\",\"agentShd\":null,\"etat\":\"Créé\",\"idEae\":18}";
		
		// When
		String result = Eae.getSerializerForEaeList().serialize(eae);
		
		// Then
		assertEquals(expectedResult, result);
	}
	
	@Test
	public void testGetSerializerForEaeList_AgentEvalue() {
		
		// Given
		Eae eae = new Eae();
		eae.setIdEae(18);
		eae.setIdAgent(999);
		
		Agent agent = new Agent();
		agent.setIdAgent(999);
		agent.setNomPatronymique("Duck");
		agent.setPrenom("Donald");
		eae.setAgentEvalue(agent);
		
		String expectedResult = "\"agentEvalue\":{\"idAgent\":999,\"nom\":\"Duck\",\"prenom\":\"Donald\"}";
		
		// When
		String result = Eae.getSerializerForEaeList().serialize(eae);
		
		// Then
		assertTrue(result.contains(expectedResult));
	}
	
	@Test
	public void testGetSerializerForEaeList_AgentShd() {
		
		// Given
		Eae eae = new Eae();
		eae.setIdEae(18);
		eae.setIdAgent(999);
		
		EaeFichePoste fdp = new EaeFichePoste();
		eae.setEaeFichePoste(fdp);
		Agent agent = new Agent();
		agent.setIdAgent(999);
		agent.setNomPatronymique("Duck");
		agent.setPrenom("Donald");
		fdp.setAgentShd(agent);
		
		String expectedResult = "\"agentShd\":{\"idAgent\":999,\"nom\":\"Duck\",\"prenom\":\"Donald\"}";
		
		// When
		String result = Eae.getSerializerForEaeList().serialize(eae);
		
		// Then
		assertTrue(result.contains(expectedResult));
	}
	
	@Test
	public void testGetSerializerForEaeList_AgentDelegataire() {
		
		// Given
		Eae eae = new Eae();
		eae.setIdEae(18);
		eae.setIdAgent(999);
		
		Agent agent = new Agent();
		agent.setIdAgent(999);
		agent.setNomPatronymique("Duck");
		agent.setPrenom("Donald");
		eae.setAgentDelegataire(agent);
		
		String expectedResult = "\"agentDelegataire\":{\"idAgent\":999,\"nom\":\"Duck\",\"prenom\":\"Donald\"}";
		
		// When
		String result = Eae.getSerializerForEaeList().serialize(eae);
		
		// Then
		assertTrue(result.contains(expectedResult));
	}
	
	@Test
	public void testGetSerializerForEaeList_EaeEvaluationAviShd() {
		
		// Given
		Eae eae = new Eae();
		eae.setIdEae(18);
		eae.setIdAgent(999);
		
		EaeEvaluation eval = new EaeEvaluation();
		eval.setAvisShd("avis SHD");
		eae.setEaeEvaluation(eval);
		
		String expectedResult = "\"eaeEvaluation\":{\"avisShd\":\"avis SHD\"}";
		
		// When
		String result = Eae.getSerializerForEaeList().serialize(eae);
		
		// Then
		assertTrue(result.contains(expectedResult));
	}

}
