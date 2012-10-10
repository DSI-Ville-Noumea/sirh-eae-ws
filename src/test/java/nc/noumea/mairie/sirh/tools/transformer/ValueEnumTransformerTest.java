package nc.noumea.mairie.sirh.tools.transformer;

import static org.junit.Assert.assertEquals;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeEtatEnum;

import org.junit.Test;

import flexjson.JSONSerializer;

public class ValueEnumTransformerTest {

	@Test
	public void testTransformReturnValueAndNotName() {
		
		// Given
		JSONSerializer serializer = new JSONSerializer();
		ValueEnumTransformer tr = new ValueEnumTransformer();
		EaeEtatEnum etat = EaeEtatEnum.EC;
		
		// When
		String json = serializer.transform(tr, Enum.class).serialize(etat);
		
		// Then
		assertEquals("\"En cours\"", json);
	}
}
