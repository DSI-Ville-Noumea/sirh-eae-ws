package nc.noumea.mairie.sirh.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import nc.noumea.mairie.sirh.eae.domain.EaeTypeDeveloppement;

public class EaeTypeDeveloppementTest {

	@Test
	public void testEaeTypeDeveloppementEquals() {
		EaeTypeDeveloppement typeDev = new EaeTypeDeveloppement(1, "Libellé");
		
		EaeTypeDeveloppement typeDev2 = new EaeTypeDeveloppement();
		typeDev2.setCode("LIBELLE");
		typeDev2.setLibelle("Libellé");
		typeDev2.setIdEaeTypeDeveloppement(1);

		assertEquals(typeDev.getCode(), typeDev2.getCode());
		assertEquals(typeDev, typeDev2);
	}
}
