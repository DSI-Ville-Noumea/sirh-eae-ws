package nc.noumea.mairie.sirh.tools.transformer;

import static org.junit.Assert.assertEquals;

import java.util.Date;


import org.junit.Test;

import flexjson.JSONSerializer;

public class MSDateTransformerTest {

	@Test
	public void testTransformNullDate() {

		// Given
		JSONSerializer serializer = new JSONSerializer();
		MSDateTransformer tr = new MSDateTransformer();
		Date d = null;

		// When
		String json = serializer.transform(tr, Date.class).serialize(d);

		// Then
		assertEquals("null", json);
	}

	@Test
	public void testTransformValidDate() {

		// Given
		JSONSerializer serializer = new JSONSerializer();
		Date d = new Date(2012, 02, 02);
		MSDateTransformer tr = new MSDateTransformer();

		// When
		String json = serializer.transform(tr, Date.class).serialize(d);

		// Then
		assertEquals("\"/DATE(61288750800000)/\"", json);
	}
}
