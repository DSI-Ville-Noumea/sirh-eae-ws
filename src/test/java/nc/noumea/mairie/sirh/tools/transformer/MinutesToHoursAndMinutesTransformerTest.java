package nc.noumea.mairie.sirh.tools.transformer;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

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
	
}
