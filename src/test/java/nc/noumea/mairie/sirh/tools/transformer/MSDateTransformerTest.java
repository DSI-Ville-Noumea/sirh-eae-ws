package nc.noumea.mairie.sirh.tools.transformer;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Test;

import flexjson.JSONDeserializer;
import flexjson.JSONException;
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
		Calendar c = new GregorianCalendar();
		c.clear();
		c.set(2012, 12, 12, 0, 0, 0);
		MSDateTransformer tr = new MSDateTransformer();

		// When
		String json = serializer.transform(tr, Date.class).serialize(c.getTime());

		// Then
		assertEquals("\"/DATE(1357909200000)/\"", json);
	}
	
	@Test
	public void testinstantiateValidDate() {

		// Given
		String json = "\"/DATE(1355270400000)/\"";
		MSDateTransformer tr = new MSDateTransformer();
		JSONDeserializer<Date> deserializer = new JSONDeserializer<Date>();

		// When
		Date d = deserializer.use(Date.class, tr).deserialize(json, Date.class);

		// Then
		assertEquals(1355270400000l, d.getTime());
	}
	
	@Test
	public void testinstantiateInvalidDate() {

		// Given
		String json = "\"/DAT(1355270400000)/\"";
		MSDateTransformer tr = new MSDateTransformer();
		JSONDeserializer<Date> deserializer = new JSONDeserializer<Date>();
		
		try {
			// When
			Date d = deserializer.use(Date.class, tr).deserialize(json, Date.class);
		}
		catch (JSONException ex) {
			// Then
			assertEquals("Unable to parse '/DAT(1355270400000)/' as a valid date time. Expected format is '/DATE\\(([0-9]+)\\)/'", ex.getMessage());
		}		
	}
}
