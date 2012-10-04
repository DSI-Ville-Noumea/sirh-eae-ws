package nc.noumea.mairie.sirh.eae.domain;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;

public class EaeTest {

	@Test
	public void testGetSerializerForEaeList() {
		
		// Given
		Eae eae = new Eae();
		eae.setIdEae(18);
		eae.setIdAgent(999);
		eae.setDirectionService("direction service bidule");
		eae.setSectionService("section service bbudile");
		eae.setEtat("etat de l'eae");
		eae.setCap(true);
		eae.setAvisCap("avis du cap");
		eae.setDocAttache(true);
		eae.setDateCreation(new Date(2012, 02, 02));
		eae.setDateFinalisation(new Date(2012, 02, 02));
		eae.setDateControle(new Date(2012, 02, 02));
		eae.setDureeEntretien(56);
		
		String expectedResult = "{\"avisCap\":\"avis du cap\",\"cap\":true,\"dateControle\":\"/DATE(61288750800000)/\",\"dateCreation\":\"/DATE(61288750800000)/\",\"dateFinalisation\":\"/DATE(61288750800000)/\",\"directionService\":\"direction service bidule\",\"docAttache\":true,\"dureeEntretien\":56,\"etat\":\"etat de l'eae\",\"idAgent\":999,\"sectionService\":\"section service bbudile\"}";
		
		// When
		String result = Eae.getSerializerForEaeList().serialize(eae);
		
		// Then
		assertEquals(expectedResult, result);
	}
	
	@Test
	public void testGetSerializerForEaeListWithNullInteger() {
		
		// Given
		Eae eae = new Eae();
		eae.setIdEae(18);
		eae.setIdAgent(999);
		eae.setDirectionService("direction service bidule");
		eae.setSectionService("section service bbudile");
		eae.setEtat("etat de l'eae");
		eae.setCap(true);
		eae.setAvisCap("avis du cap");
		eae.setDocAttache(true);
		eae.setDateCreation(null);
		eae.setDateFinalisation(null);
		eae.setDateControle(null);
		eae.setDureeEntretien(null);
		
		String expectedResult = "{\"avisCap\":\"avis du cap\",\"cap\":true,\"dateControle\":null,\"dateCreation\":null,\"dateFinalisation\":null,\"directionService\":\"direction service bidule\",\"docAttache\":true,\"dureeEntretien\":null,\"etat\":\"etat de l'eae\",\"idAgent\":999,\"sectionService\":\"section service bbudile\"}";
		
		// When
		String result = Eae.getSerializerForEaeList().serialize(eae);
		
		// Then
		assertEquals(expectedResult, result);
	}

}
