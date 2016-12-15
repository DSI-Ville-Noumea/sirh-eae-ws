package nc.noumea.mairie.sirh.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import nc.noumea.mairie.sirh.eae.domain.EaeRefDelai;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeDelaiEnum;

public class EaeRefDelaiTest {

	@Test
	public void testConstructorFromEnum() {
		EaeRefDelai delai = new EaeRefDelai(EaeDelaiEnum.MOINS1AN);
		EaeRefDelai delai1 = new EaeRefDelai(EaeDelaiEnum.ENTRE1ET2ANS);
		EaeRefDelai delai2 = new EaeRefDelai(EaeDelaiEnum.ENTRE2ET4ANS);
		
		assertEquals(delai.getId(), EaeDelaiEnum.MOINS1AN.getId());
		assertEquals(delai.getCode(), EaeDelaiEnum.MOINS1AN.name());
		assertEquals(delai.getLibelle(), EaeDelaiEnum.MOINS1AN.getLibelle());
		assertEquals("1", delai.getId().toString());
		assertEquals("MOINS1AN", delai.getCode());
		assertEquals("inférieur à 1 an", delai.getLibelle());
		
		assertEquals(delai1.getId(), EaeDelaiEnum.ENTRE1ET2ANS.getId());
		assertEquals(delai1.getCode(), EaeDelaiEnum.ENTRE1ET2ANS.name());
		assertEquals(delai1.getLibelle(), EaeDelaiEnum.ENTRE1ET2ANS.getLibelle());
		assertEquals("2", delai1.getId().toString());
		assertEquals("ENTRE1ET2ANS", delai1.getCode());
		assertEquals("entre 1 et 2 ans", delai1.getLibelle());
		
		assertEquals(delai2.getId(), EaeDelaiEnum.ENTRE2ET4ANS.getId());
		assertEquals(delai2.getCode(), EaeDelaiEnum.ENTRE2ET4ANS.name());
		assertEquals(delai2.getLibelle(), EaeDelaiEnum.ENTRE2ET4ANS.getLibelle());
		assertEquals("3", delai2.getId().toString());
		assertEquals("ENTRE2ET4ANS", delai2.getCode());
		assertEquals("entre 2 et 4 ans", delai2.getLibelle());
	}
}
