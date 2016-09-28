package nc.noumea.mairie.sirh.eae.dto;

import static org.junit.Assert.assertEquals;
import nc.noumea.mairie.sirh.eae.domain.EaeResultat;
import nc.noumea.mairie.sirh.eae.domain.EaeTypeObjectif;

import org.junit.Test;

public class EaeResultatDtoTest {

	@Test
	public void testConstructor() {
		
		EaeTypeObjectif typeObjectif = new EaeTypeObjectif();
		typeObjectif.setLibelle("libelle");
		
		EaeResultat resultat = new EaeResultat();
		resultat.setIdEaeResultat(1);
		resultat.setObjectif("objectif");
		resultat.setResultat("resultat");
		resultat.setTypeObjectif(typeObjectif);
		
		EaeResultatDto result = new EaeResultatDto(resultat);
		
		assertEquals(result.getIdEaeResultat(), resultat.getIdEaeResultat());
		assertEquals(result.getObjectif(), resultat.getObjectif());
		assertEquals(result.getResultat(), resultat.getResultat());
		assertEquals(result.getTypeObjectif().getLibelle(), resultat.getTypeObjectif().getLibelle());
	}
}
