package nc.noumea.mairie.sirh.eae.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import nc.noumea.mairie.sirh.domain.Agent;
import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeCampagne;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluateur;
import nc.noumea.mairie.sirh.eae.domain.EaeEvalue;

import org.junit.Test;

import flexjson.PathExpression;

public class FinalizationInformationDtoTest {

	@Test
	public void testConstructorWithEae() {
		
		// Given
		EaeCampagne camp = new EaeCampagne();
		camp.setAnnee(2014);
		Eae eae = new Eae();
		eae.setIdEae(7896);
		eae.setEaeCampagne(camp);
		
		Agent agentEvalue = new Agent();
		agentEvalue.setIdAgent(9007000);
		agentEvalue.setNomUsage("Evalue");
		agentEvalue.setPrenom("Agent");
		EaeEvalue evalue = new EaeEvalue();
		evalue.setAgent(agentEvalue);
		eae.setEaeEvalue(evalue);
		
		Agent agentDelegataire = new Agent();
		agentDelegataire.setIdAgent(9007001);
		agentDelegataire.setNomUsage("Delegataire");
		agentDelegataire.setPrenom("Agent");
		eae.setAgentDelegataire(agentDelegataire);
		
		Agent agentEvaluateur1 = new Agent();
		agentEvaluateur1.setIdAgent(9007002);
		agentEvaluateur1.setNomUsage("Evaluateur1");
		agentEvaluateur1.setPrenom("Agent");
		EaeEvaluateur eval1 = new EaeEvaluateur();
		eval1.setAgent(agentEvaluateur1);
		eae.getEaeEvaluateurs().add(eval1);
		
		Agent agentEvaluateur2 = new Agent();
		agentEvaluateur2.setIdAgent(9007003);
		agentEvaluateur2.setNomUsage("Evaluateur2");
		agentEvaluateur2.setPrenom("Agent");
		EaeEvaluateur eval2 = new EaeEvaluateur();
		eval2.setAgent(agentEvaluateur2);
		eae.getEaeEvaluateurs().add(eval2);
		
		// When
		FinalizationInformationDto result = new FinalizationInformationDto(eae);
		
		// Then
		assertEquals(2014, result.getAnnee());
		assertEquals(7896, result.getIdEae());
		assertEquals(new Integer(9007000), result.getAgentEvalue().getIdAgent());
		assertEquals(new Integer(9007001), result.getAgentDelegataire().getIdAgent());
		assertEquals(2, result.getAgentsEvaluateurs().size());
		assertTrue(result.getAgentsEvaluateurs().get(0).getIdAgent().equals(new Integer(9007002)) || result.getAgentsEvaluateurs().get(1).getIdAgent().equals(new Integer(9007002)));
		assertTrue(result.getAgentsEvaluateurs().get(0).getIdAgent().equals(new Integer(9007003)) || result.getAgentsEvaluateurs().get(1).getIdAgent().equals(new Integer(9007003)));
	}
	
	@Test
	public void testGetJSONSerializerForFinalizationInformationDto_includes_excludes() {
		
		// When
		List<PathExpression> includes = FinalizationInformationDto.getSerializerForFinalizationInformationDto().getIncludes();
		List<PathExpression> excludes = FinalizationInformationDto.getSerializerForFinalizationInformationDto().getExcludes();
		
		// Then
		assertEquals(1, includes.size());
		assertEquals("[*]", includes.get(0).toString());
		
		assertEquals(1, excludes.size());
		assertEquals("[*,class]", excludes.get(0).toString());
	}
	
	@Test
	public void testSerializeInJSON_emptyObject() {
		// Given
		FinalizationInformationDto dto = new FinalizationInformationDto();
		String expectedJson = "{\"agentDelegataire\":null,\"agentEvalue\":null,\"agentsEvaluateurs\":[],\"annee\":0,\"idEae\":0}";
		
		// When
		String result = dto.serializeInJSON();
		
		//Then
		assertEquals(expectedJson, result);
	}
	
	@Test
	public void testSerializeInJSON_fullObject() {
		// Given
		FinalizationInformationDto dto = new FinalizationInformationDto();
		Agent agentEvalue = new Agent();
		agentEvalue.setIdAgent(9007000);
		agentEvalue.setNomUsage("Evalue");
		agentEvalue.setPrenom("Agent");
		dto.setAgentEvalue(agentEvalue);
		
		Agent agentDelegataire = new Agent();
		agentDelegataire.setIdAgent(9007001);
		agentDelegataire.setNomUsage("Delegataire");
		agentDelegataire.setPrenom("Agent");
		dto.setAgentDelegataire(agentDelegataire);
		
		Agent agentEvaluateur1 = new Agent();
		agentEvaluateur1.setIdAgent(9007002);
		agentEvaluateur1.setNomUsage("Evaluateur1");
		agentEvaluateur1.setPrenom("Agent");
		dto.getAgentsEvaluateurs().add(agentEvaluateur1);
		
		Agent agentEvaluateur2 = new Agent();
		agentEvaluateur2.setIdAgent(9007003);
		agentEvaluateur2.setNomUsage("Evaluateur2");
		agentEvaluateur2.setPrenom("Agent");
		EaeEvaluateur eval2 = new EaeEvaluateur();
		eval2.setAgent(agentEvaluateur2);
		dto.getAgentsEvaluateurs().add(agentEvaluateur2);
		
		dto.setAnnee(2014);
		dto.setIdEae(120);
		
		String expectedJson = "{\"agentDelegataire\":{\"idAgent\":9007001,\"nom\":\"Delegataire\",\"prenom\":\"Agent\"},\"agentEvalue\":{\"idAgent\":9007000,\"nom\":\"Evalue\",\"prenom\":\"Agent\"},\"agentsEvaluateurs\":[{\"idAgent\":9007002,\"nom\":\"Evaluateur1\",\"prenom\":\"Agent\"},{\"idAgent\":9007003,\"nom\":\"Evaluateur2\",\"prenom\":\"Agent\"}],\"annee\":2014,\"idEae\":120}";
		
		// When
		String result = dto.serializeInJSON();
		
		//Then
		assertEquals(expectedJson, result);
	}
}
