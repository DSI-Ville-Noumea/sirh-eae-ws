package nc.noumea.mairie.sirh.tools.transformer;

import static org.junit.Assert.assertEquals;
import nc.noumea.mairie.sirh.domain.Agent;

import org.junit.Test;

import flexjson.JSONSerializer;

public class SimpleAgentTransformerTest {

	@Test
	public void testTransformNullAgent() {

		// Given
		JSONSerializer serializer = new JSONSerializer();
		SimpleAgentTransformer tr = new SimpleAgentTransformer();
		Agent a = null;

		// When
		String json = serializer.transform(tr, Agent.class).serialize(a);

		// Then
		assertEquals("null", json);
	}

	@Test
	public void testTransformValidAgent() {

		// Given
		JSONSerializer serializer = new JSONSerializer();
		SimpleAgentTransformer tr = new SimpleAgentTransformer();
		Agent a = new Agent();
		a.setIdAgent(1899);
		a.setNomPatronymique("Duck");
		a.setPrenom("Donald");
		
		// When
		String json = serializer.transform(tr, Agent.class).serialize(a);

		// Then
		assertEquals("{\"idAgent\":1899,\"nom\":\"Duck\",\"prenom\":\"Donald\"}", json);
	}
}
