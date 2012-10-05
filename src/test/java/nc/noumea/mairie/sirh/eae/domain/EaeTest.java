package nc.noumea.mairie.sirh.eae.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import nc.noumea.mairie.sirh.domain.Agent;

import org.junit.Test;

import flexjson.PathExpression;

public class EaeTest {

	@Test
	public void testGetSerializerForEaeList_ListAllIncludesExcludes() {
		
		// Given
		Eae eae = new Eae();
		eae.setIdEae(18);
		eae.setIdAgent(999);
		eae.setDirectionService("direction service bidule");
		eae.setSectionService("section service bbudile");
		eae.setEtat("etat de l'eae");
		eae.setCap(true);
		eae.setAvisCap("avis du cap");
		eae.setDocAttache(true);
		eae.setDateCreation(new Date(2012, 02, 02));
		eae.setDateFinalisation(new Date(2012, 02, 02));
		eae.setDateControle(new Date(2012, 02, 02));
		eae.setDureeEntretien(56);
		
		String expectedResult = "{\"agentEvalue\":null,\"avisCap\":\"avis du cap\",\"cap\":true,\"dateControle\":\"/DATE(61288750800000)/\",\"dateCreation\":\"/DATE(61288750800000)/\",\"dateFinalisation\":\"/DATE(61288750800000)/\",\"directionService\":\"direction service bidule\",\"docAttache\":true,\"dureeEntretien\":56,\"etat\":\"etat de l'eae\",\"sectionService\":\"section service bbudile\"}";
		
		// When
		String result = Eae.getSerializerForEaeList().serialize(eae);
		List<PathExpression> includes = Eae.getSerializerForEaeList().getIncludes();
		List<PathExpression> excludes = Eae.getSerializerForEaeList().getExcludes();
		
		// Then
		assertEquals(expectedResult, result);
		
		assertEquals(11, includes.size());
		assertEquals("[agentEvalue]", includes.get(0).toString());
		assertEquals("[directionService]", includes.get(1).toString());
		assertEquals("[sectionService]", includes.get(2).toString());
		assertEquals("[etat]", includes.get(3).toString());
		assertEquals("[cap]", includes.get(4).toString());
		assertEquals("[avisCap]", includes.get(5).toString());
		assertEquals("[docAttache]", includes.get(6).toString());
		assertEquals("[dateCreation]", includes.get(7).toString());
		assertEquals("[dateFinalisation]", includes.get(8).toString());
		assertEquals("[dateControle]", includes.get(9).toString());
		assertEquals("[dureeEntretien]", includes.get(10).toString());
		
		assertEquals(1, excludes.size());
		assertEquals("[*]", excludes.get(0).toString());
	}
	
	@Test
	public void testGetSerializerForEaeListWithNullInteger() {
		
		// Given
		Eae eae = new Eae();
		eae.setIdEae(18);
		eae.setIdAgent(999);
		eae.setDirectionService("direction service bidule");
		eae.setSectionService("section service bbudile");
		eae.setEtat("etat de l'eae");
		eae.setCap(true);
		eae.setAvisCap("avis du cap");
		eae.setDocAttache(true);
		eae.setDateCreation(null);
		eae.setDateFinalisation(null);
		eae.setDateControle(null);
		eae.setDureeEntretien(null);
		
		String expectedResult = "{\"agentEvalue\":null,\"avisCap\":\"avis du cap\",\"cap\":true,\"dateControle\":null,\"dateCreation\":null,\"dateFinalisation\":null,\"directionService\":\"direction service bidule\",\"docAttache\":true,\"dureeEntretien\":null,\"etat\":\"etat de l'eae\",\"sectionService\":\"section service bbudile\"}";
		
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

}
