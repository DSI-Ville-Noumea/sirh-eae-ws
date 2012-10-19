package nc.noumea.mairie.sirh.tools.transformer;

import static org.junit.Assert.assertEquals;
import nc.noumea.mairie.sirh.domain.Agent;
import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeEvalue;

import org.joda.time.DateTime;
import org.junit.Test;

import flexjson.JSONSerializer;

public class EaeEvalueToAgentTransformerTest {

	@Test
	public void testTransform_agentIsNull() {
		
		// Given
		EaeEvalue eval = new EaeEvalue();
		String expectedJson = "{null}";
		
		// When
		String json = new JSONSerializer().transform(new EaeEvalueToAgentTransformer(), EaeEvalue.class).serialize(eval);
		
		// Then
		assertEquals(expectedJson, json);
	}
	
	@Test
	public void testTransform_agentIsNullAndInlineTrue() {
		
		// Given
		EaeEvalue eval = new EaeEvalue();
		String expectedJson = "{null}";
		
		// When
		String json = new JSONSerializer().transform(new EaeEvalueToAgentTransformer(true), EaeEvalue.class).serialize(eval);
		
		// Then
		assertEquals(expectedJson, json);
	}
	
	@Test
	public void testTransform_agentIsNotNull() {
		
		// Given
		EaeEvalue eval = new EaeEvalue();
		Agent agent = new Agent();
		agent.setIdAgent(1234);
		agent.setDateNaissance(new DateTime(2000, 01, 17, 0, 0, 0, 0).toDate());
		agent.setNomMarital("Marital");
		agent.setPrenom("Prenom");
		agent.setNomPatronymique("POO");
		eval.setAgent(agent);
		
		String expectedJson = "{\"idAgent\":1234,\"nom\":\"Marital\",\"nomJeuneFille\":\"POO\",\"prenom\":\"Prenom\",\"dateNaissance\":948027600000}";
		
		// When
		String json = new JSONSerializer().transform(new EaeEvalueToAgentTransformer(), EaeEvalue.class).serialize(eval);
		
		// Then
		assertEquals(expectedJson, json);
	}
	
	@Test
	public void testTransform_agentIsNotNullInlineTrue() {
		
		// Given
		Eae eae = new Eae();
		EaeEvalue eval = new EaeEvalue();
		Agent agent = new Agent();
		agent.setIdAgent(1234);
		agent.setDateNaissance(new DateTime(2000, 01, 17, 0, 0, 0, 0).toDate());
		agent.setNomMarital("Marital");
		agent.setPrenom("Prenom");
		agent.setNomPatronymique("POO");
		eval.setAgent(agent);
		eae.setEaeEvalue(eval);
		
		String expectedJson = "{\"idAgent\":1234,\"nom\":\"Marital\",\"nomJeuneFille\":\"POO\",\"prenom\":\"Prenom\",\"dateNaissance\":948027600000}";
		
		// When
		String json = new JSONSerializer()
		.include("eaeEvalue")
		.transform(new EaeEvalueToAgentTransformer(true), EaeEvalue.class)
		.exclude("*")
		.serialize(eae);
		
		// Then
		assertEquals(expectedJson, json);
	}
	
	@Test
	public void testTransform_agentIsNotNullInlineFalse() {
		
		// Given
		Eae eae = new Eae();
		EaeEvalue eval = new EaeEvalue();
		Agent agent = new Agent();
		agent.setIdAgent(1234);
		agent.setDateNaissance(new DateTime(2000, 01, 17, 0, 0, 0, 0).toDate());
		agent.setNomMarital("Marital");
		agent.setPrenom("Prenom");
		agent.setNomPatronymique("POO");
		eval.setAgent(agent);
		eae.setEaeEvalue(eval);
		
		String expectedJson = "{\"eaeEvalue\":{\"idAgent\":1234,\"nom\":\"Marital\",\"nomJeuneFille\":\"POO\",\"prenom\":\"Prenom\",\"dateNaissance\":948027600000}}";
		
		// When
		String json = new JSONSerializer()
		.include("eaeEvalue")
		.transform(new EaeEvalueToAgentTransformer(false), EaeEvalue.class)
		.exclude("*")
		.serialize(eae);
		
		// Then
		assertEquals(expectedJson, json);
	}
	
}
