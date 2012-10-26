package nc.noumea.mairie.sirh.eae.dto.util;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import nc.noumea.mairie.sirh.eae.domain.EaeNiveau;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeAvancementEnum;

import org.junit.Test;

public class ValueWithListDtoTest {

	@Test
	public void testConstructorWithEnum() {
		// Given
		EaeAvancementEnum value = EaeAvancementEnum.MAXI;
		
		// When
		ValueWithListDto dto = new ValueWithListDto(value, EaeAvancementEnum.class);
		
		// Then
		assertEquals(EaeAvancementEnum.MAXI.name(), dto.getCourant());
		assertEquals(3, dto.getListe().size());
		assertEquals(EaeAvancementEnum.MINI.name(), dto.getListe().get(0).getCode());
		assertEquals(EaeAvancementEnum.MINI.toString(), dto.getListe().get(0).getValeur());
		assertEquals(EaeAvancementEnum.MOY.name(), dto.getListe().get(1).getCode());
		assertEquals(EaeAvancementEnum.MOY.toString(), dto.getListe().get(1).getValeur());
		assertEquals(EaeAvancementEnum.MAXI.name(), dto.getListe().get(2).getCode());
		assertEquals(EaeAvancementEnum.MAXI.toString(), dto.getListe().get(2).getValeur());
	}
	
	@Test
	public void testConstructorWithEnumAndNullValue() {
		// Given
		EaeAvancementEnum value = null;
		
		// When
		ValueWithListDto dto = new ValueWithListDto(value, EaeAvancementEnum.class);
		
		// Then
		assertEquals(null, dto.getCourant());
		assertEquals(3, dto.getListe().size());
		assertEquals(EaeAvancementEnum.MINI.name(), dto.getListe().get(0).getCode());
		assertEquals(EaeAvancementEnum.MINI.toString(), dto.getListe().get(0).getValeur());
		assertEquals(EaeAvancementEnum.MOY.name(), dto.getListe().get(1).getCode());
		assertEquals(EaeAvancementEnum.MOY.toString(), dto.getListe().get(1).getValeur());
		assertEquals(EaeAvancementEnum.MAXI.name(), dto.getListe().get(2).getCode());
		assertEquals(EaeAvancementEnum.MAXI.toString(), dto.getListe().get(2).getValeur());
	}
	
	@Test
	public void testConstructorWithEaeNiveau() {
		// Given
		EaeNiveau niv1 = new EaeNiveau();
		niv1.setIdEaeNiveau(1);
		niv1.setLibelleNiveauEae("uno");
		EaeNiveau niv2 = new EaeNiveau();
		niv2.setIdEaeNiveau(2);
		niv2.setLibelleNiveauEae("dos");
		
		// When
		ValueWithListDto dto = new ValueWithListDto(niv2, Arrays.asList(niv1, niv2));
		
		// Then
		assertEquals("2", dto.getCourant());
		assertEquals(2, dto.getListe().size());
		assertEquals("1", dto.getListe().get(0).getCode());
		assertEquals("uno", dto.getListe().get(0).getValeur());
		assertEquals("2", dto.getListe().get(1).getCode());
		assertEquals("dos", dto.getListe().get(1).getValeur());
	}
}
