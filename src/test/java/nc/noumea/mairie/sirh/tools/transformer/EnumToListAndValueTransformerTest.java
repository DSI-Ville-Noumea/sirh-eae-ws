package nc.noumea.mairie.sirh.tools.transformer;

import static org.junit.Assert.assertEquals;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeAgentStatutEnum;

import org.junit.Test;

import flexjson.JSONSerializer;

public class EnumToListAndValueTransformerTest {

	@Test
	public void transform() {
		
		// Given
		EaeAgentStatutEnum e = EaeAgentStatutEnum.CC;
		String expectedJson = "{\"courant\":\"CC\",\"liste\":[{\"code\":\"F\",\"valeur\":\"Fonctionnaire\"},{\"code\":\"C\",\"valeur\":\"Contractuel\"},{\"code\":\"CC\",\"valeur\":\"Convention collective\"},{\"code\":\"AL\",\"valeur\":\"Allocataire\"},{\"code\":\"A\",\"valeur\":\"Autre\"}]}";
		
		// When
		String json = new JSONSerializer()
			.transform(new EnumToListAndValueTransformer(EaeAgentStatutEnum.class), EaeAgentStatutEnum.class)
			.serialize(e);

		// Then
		assertEquals(expectedJson, json);
	}
}
