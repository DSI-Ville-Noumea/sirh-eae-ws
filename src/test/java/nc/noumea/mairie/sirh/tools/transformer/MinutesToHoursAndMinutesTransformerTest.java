package nc.noumea.mairie.sirh.tools.transformer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class MinutesToHoursAndMinutesTransformerTest {

	@Test
	public void testTransform() {
		// Given
		Integer h = 157;
		MinutesToHoursAndMinutesTransformer tr = new MinutesToHoursAndMinutesTransformer();
		
		// When
		String json = new JSONSerializer().transform(tr, Integer.class).serialize(h);
		
		// Then
		assertEquals("{\"heures\":2,\"minutes\":37}", json);
	}
	
	@Test
	public void testTransformWithOnlyMinutes() {
		// Given
		Integer h = 67;
		MinutesToHoursAndMinutesTransformer tr = new MinutesToHoursAndMinutesTransformer();
	
		// When
		String json = new JSONSerializer().transform(tr, Integer.class).serialize(h);
		
		// Then
		assertEquals("{\"heures\":1,\"minutes\":7}", json);
	}
	
	@Test
	public void testInstantiateWithNullValue() {
		// Given
		String json = "null";
		
		// When
		HoldingClass c = new HoldingClass();
		new JSONDeserializer<HoldingClass>()
				.use("dureeEntretien", new MinutesToHoursAndMinutesTransformer())
				.deserializeInto(json, c);
		
		// Then
		assertNull(c.getDureeEntretien());
	}
	
	@Test
	public void testInstantiateWithActualValue() {
		// Given
		String json = "{\"dureeEntretien\":{\"heures\":1,\"minutes\":7}}";
		
		// When
		HoldingClass c = new HoldingClass();
		new JSONDeserializer<HoldingClass>()
				.use("dureeEntretien", new MinutesToHoursAndMinutesTransformer())
				.deserializeInto(json, c);
		
		// Then
		assertEquals(new Integer(67), c.getDureeEntretien());
	}
	
	
	private class HoldingClass {
		Integer dureeEntretien;

		public Integer getDureeEntretien() {
			return dureeEntretien;
		}

		public void setDureeEntretien(Integer dureeEntretien) {
			this.dureeEntretien = dureeEntretien;
		}
	}
}
