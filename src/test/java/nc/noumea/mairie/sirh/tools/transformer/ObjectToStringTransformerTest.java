package nc.noumea.mairie.sirh.tools.transformer;

import static org.junit.Assert.assertEquals;
import nc.noumea.mairie.sirh.domain.Agent;
import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeDiplome;

import org.junit.Test;

import flexjson.JSONSerializer;

public class ObjectToStringTransformerTest {

	@Test
	public void transformExistingFieldWithValue() {
		
		// Given
		Agent agent = new Agent();
		agent.setNomUsage("poupou");
		
		// When
		String json = new JSONSerializer().transform(new ObjectToStringTransformer("nomUsage", Agent.class), Agent.class)
				.serialize(agent);
		
		// Then
		assertEquals("\"poupou\"", json);
	}
	
	@Test
	public void transformExistingFieldsWithValue() {
		
		// Given
		EaeDiplome diplome1 = new EaeDiplome();
		diplome1.setLibelleDiplome("poupou1");
		
		Eae eae = new Eae();
		eae.getEaeDiplomes().add(diplome1);
		
		// When
		String json = new JSONSerializer()
				.include("eaeDiplomes")
				.transform(new ObjectToStringTransformer("libelleDiplome", EaeDiplome.class), EaeDiplome.class)
				.exclude("*")
				.serialize(eae);
		
		// Then
		assertEquals("{\"eaeDiplomes\":[\"poupou1\"]}", json);
	}
}
