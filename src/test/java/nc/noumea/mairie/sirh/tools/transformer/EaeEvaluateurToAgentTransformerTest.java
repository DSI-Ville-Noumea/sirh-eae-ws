package nc.noumea.mairie.sirh.tools.transformer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import nc.noumea.mairie.sirh.domain.Agent;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluateur;

import org.junit.Test;

import flexjson.JSONSerializer;

public class EaeEvaluateurToAgentTransformerTest {

	@Test
	public void testTransformNullEaeEvaluateur() {

		// Given
		JSONSerializer serializer = new JSONSerializer();
		EaeEvaluateurToAgentTransformer tr = new EaeEvaluateurToAgentTransformer();
		EaeEvaluateur eval = null;

		// When
		String json = serializer.transform(tr, EaeEvaluateur.class).serialize(
				eval);

		// Then
		assertEquals("null", json);
	}

	@Test
	public void testTransformValidEaeEvaluateur() {

		// Given
		JSONSerializer serializer = new JSONSerializer();
		EaeEvaluateurToAgentTransformer tr = new EaeEvaluateurToAgentTransformer();
		Agent agent = new Agent();
		agent.setIdAgent(899);
		EaeEvaluateur eval = new EaeEvaluateur();
		eval.setAgent(agent);

		// When
		String json = serializer.transform(tr, EaeEvaluateur.class).serialize(eval);

		// Then
		assertTrue(json.startsWith("{\"class\":\"nc.noumea.mairie.sirh.domain.Agent\""));
	}
}
