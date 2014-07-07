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
		assertEquals("\"/Date(1357909200000+1100)/\"", json);
	}

	@Test
	public void testinstantiateValidDateWithoutTimezone() {

		// Given
		String json = "\"/DATE(1355270400000)/\"";
		MSDateTransformer tr = new MSDateTransformer();
		JSONDeserializer<Date> deserializer = new JSONDeserializer<Date>();

		// When
		Date d = deserializer.use(Date.class, tr).deserialize(json, Date.class);

		// Then
		Calendar c = new GregorianCalendar();
		c.clear();
		c.setTime(d);
		assertEquals(1355270400000l, d.getTime());
	}

	@Test
	public void testinstantiateValidDateWithTimezone() {

		// Given
		String json = "\"/DATE(1355270400000+0500)/\"";
		MSDateTransformer tr = new MSDateTransformer();
		JSONDeserializer<Date> deserializer = new JSONDeserializer<Date>();

		// When
		Date d = deserializer.use(Date.class, tr).deserialize(json, Date.class);

		// Then
		Calendar c = new GregorianCalendar();
		c.clear();
		c.setTime(d);
		assertEquals(1355270400000l, d.getTime());
	}

	@Test
	public void testinstantiateValidDateWithDifferentNaming() {

		// Given
		String json = "\"/dAtE(1355270400000)/\"";
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
			deserializer.use(Date.class, tr).deserialize(json, Date.class);
		} catch (JSONException ex) {
			// Then
			assertEquals(
					"Unable to parse '/DAT(1355270400000)/' as a valid date time. Expected format is '/[Dd][Aa][Tt][Ee]\\((\\-?[0-9]+)([\\+\\-]{1}[0-9]{4})*\\)/'",
					ex.getMessage());
		}
	}
}
