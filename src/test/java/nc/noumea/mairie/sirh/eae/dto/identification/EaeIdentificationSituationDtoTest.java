package nc.noumea.mairie.sirh.eae.dto.identification;

import static org.junit.Assert.assertEquals;
import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeEvalue;
import nc.noumea.mairie.sirh.eae.domain.EaeFichePoste;

import org.joda.time.DateTime;
import org.junit.Test;

public class EaeIdentificationSituationDtoTest {

	@Test
	public void testEaeIdentificationSituationDto() {
		// Given
		Eae eae = new Eae();
		
		EaeFichePoste fp = new EaeFichePoste();
		fp.setDateEntreeFonction(new DateTime(2007, 8, 6, 0, 0, 0, 0).toDate());
		fp.setFonction("fonction");
		fp.setEmploi("emploi");
		fp.setDirectionService("direction service");
		eae.setEaeFichePoste(fp);
		
		EaeEvalue eaeEvalue = new EaeEvalue();
		eaeEvalue.setDateEntreeAdministration(new DateTime(2006, 5, 3, 0, 0, 0, 0).toDate());
		eaeEvalue.setDateEntreeFonctionnaire(new DateTime(2006, 5, 3, 0, 0, 0, 0).toDate());
		eae.setEaeEvalue(eaeEvalue);
		
		// When
		EaeIdentificationSituationDto dto = new EaeIdentificationSituationDto(eae);
		
		// Then
		assertEquals(fp.getDateEntreeFonction(), dto.getDateEntreeFonction());
		assertEquals(fp.getFonction(), dto.getFonction());
		assertEquals(fp.getEmploi(), dto.getEmploi());
		assertEquals(fp.getDirectionService(), dto.getDirectionService());
		assertEquals(eaeEvalue.getDateEntreeAdministration(), dto.getDateEntreeAdministration());
		assertEquals(eaeEvalue.getDateEntreeFonctionnaire(), dto.getDateEntreeFonctionnaire());
	}
}
