package nc.noumea.mairie.sirh.tools;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import flexjson.JSONSerializer;

public class NullableIntegerTransformerTest {

	@Test
	public void testTransformNullInteger() {

		// Given
		JSONSerializer serializer = new JSONSerializer();
		Integer i = null;
		NullableIntegerTransformer tr = new NullableIntegerTransformer();

		// When
		String json = serializer.transform(tr, Integer.class).serialize(i);

		// Then
		assertEquals("null", json);
	}

	@Test
	public void testTransformValidInteger() {

		// Given
		JSONSerializer serializer = new JSONSerializer();
		Integer i = 18765;
		NullableIntegerTransformer tr = new NullableIntegerTransformer();

		// When
		String json = serializer.transform(tr, Integer.class).serialize(i);

		// Then
		assertEquals("18765", json);
	}
}
