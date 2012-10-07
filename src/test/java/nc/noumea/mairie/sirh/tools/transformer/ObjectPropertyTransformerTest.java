package nc.noumea.mairie.sirh.tools.transformer;

import static org.junit.Assert.assertEquals;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluation;

import org.junit.Test;

import flexjson.JSONSerializer;

public class ObjectPropertyTransformerTest {

	@Test
	public void testTransformNullObject() {
		
		// Given
		JSONSerializer serializer = new JSONSerializer();
		ObjectPropertyTransformer tr = new ObjectPropertyTransformer("avisShd");
		EaeEvaluation eval = null;

		// When
		String json = serializer.include("eaeEvaluation.avisShd").transform(tr, EaeEvaluation.class).serialize(eval);

		// Then
		assertEquals("null", json);
	}

	@Test
	public void testTransformEaeEvaluationPropertyAvisShd() {

		// Given
		JSONSerializer serializer = new JSONSerializer();
		ObjectPropertyTransformer tr = new ObjectPropertyTransformer("avisShd");
		EaeEvaluation eval = new EaeEvaluation();
		eval.setAvisShd("avis shd");

		// When
		String json = serializer.transform(tr, EaeEvaluation.class).serialize(
				eval);

		// Then
		assertEquals("\"avisShd\":\"avis shd\"", json);
	}
}
