package nc.noumea.mairie.sirh.eae.dto.identification;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import nc.noumea.mairie.sirh.eae.domain.EaeRefDelai;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeDelaiEnum;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeNiveauEnum;

public class EaeListeDtoTest {

	@Test
	public void testConstructor() {
		
		EaeListeDto dto = new EaeListeDto(EaeNiveauEnum.SATISFAISANT, EaeNiveauEnum.class);
		
		assertEquals(dto.getCourant(), EaeNiveauEnum.SATISFAISANT.name());
		assertEquals(dto.getListe().size(), EaeNiveauEnum.class.getEnumConstants().length);
	}
	
	@Test
	public void testConstructorFromDelai() {
		EaeListeDto dto = new EaeListeDto(new EaeRefDelai(EaeDelaiEnum.MOINS1AN));
		EaeListeDto dto1 = new EaeListeDto(new EaeRefDelai(EaeDelaiEnum.ENTRE1ET2ANS));
		EaeListeDto dto2 = new EaeListeDto(new EaeRefDelai(EaeDelaiEnum.ENTRE2ET4ANS));
		
		assertEquals(dto.getCourant(), EaeDelaiEnum.MOINS1AN.name());
		assertEquals(dto.getCourant(), "MOINS1AN");
		
		assertEquals(dto1.getCourant(), EaeDelaiEnum.ENTRE1ET2ANS.name());
		assertEquals(dto1.getCourant(), "ENTRE1ET2ANS");
		
		assertEquals(dto2.getCourant(), EaeDelaiEnum.ENTRE2ET4ANS.name());
		assertEquals(dto2.getCourant(), "ENTRE2ET4ANS");
		
		assertFalse(dto.getListe().isEmpty());
		assertEquals(dto.getListe().get(0).getValeur(), "inférieur à 1 an");
		assertEquals(dto.getListe().get(1).getValeur(), "entre 1 et 2 ans");
		assertEquals(dto.getListe().get(2).getValeur(), "entre 2 et 4 ans");
		
		assertEquals(dto.getListe().size(), dto1.getListe().size());
		assertEquals(dto1.getListe().size(), dto2.getListe().size());
	}
}
