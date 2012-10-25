package nc.noumea.mairie.sirh.eae.dto.util;

import static org.junit.Assert.assertEquals;
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
}
