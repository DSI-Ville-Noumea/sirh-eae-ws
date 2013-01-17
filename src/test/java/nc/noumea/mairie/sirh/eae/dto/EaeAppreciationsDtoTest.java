package nc.noumea.mairie.sirh.eae.dto;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeAppreciation;
import nc.noumea.mairie.sirh.eae.domain.EaeEvalue;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeTypeAppreciationEnum;

import org.junit.Test;

import flexjson.PathExpression;

public class EaeAppreciationsDtoTest {

	@Test
	public void testConstructorWithEae() {
		// Given
		Eae eae = new Eae();
		eae.setEaeEvalue(new EaeEvalue());
		eae.getEaeEvalue().setEstEncadrant(true);
		eae.setIdEae(79);
		EaeAppreciation ap1 = new EaeAppreciation();
		ap1.setNumero(0);
		ap1.setTypeAppreciation(EaeTypeAppreciationEnum.TE);
		ap1.setNoteEvalue("A");
		ap1.setNoteEvaluateur("D");
		eae.getEaeAppreciations().add(ap1);
		
		EaeAppreciation ap2 = new EaeAppreciation();
		ap2.setNumero(1);
		ap2.setTypeAppreciation(EaeTypeAppreciationEnum.SE);
		ap2.setNoteEvalue("B");
		ap2.setNoteEvaluateur("C");
		eae.getEaeAppreciations().add(ap2);
		
		EaeAppreciation ap3 = new EaeAppreciation();
		ap3.setNumero(2);
		ap3.setTypeAppreciation(EaeTypeAppreciationEnum.MA);
		ap3.setNoteEvalue("C");
		ap3.setNoteEvaluateur("B");
		eae.getEaeAppreciations().add(ap3);
		
		EaeAppreciation ap4 = new EaeAppreciation();
		ap4.setNumero(3);
		ap4.setTypeAppreciation(EaeTypeAppreciationEnum.RE);
		ap4.setNoteEvalue("D");
		ap4.setNoteEvaluateur("A");
		eae.getEaeAppreciations().add(ap4);
		
		// When
		EaeAppreciationsDto dto = new EaeAppreciationsDto(eae);
		
		// Then
		assertEquals(79, dto.getIdEae());
		assertTrue(dto.isEstEncadrant());
		assertEquals("A", dto.getTechniqueEvalue()[0]);
		assertEquals("D", dto.getTechniqueEvaluateur()[0]);
		
		assertEquals("B", dto.getSavoirEtreEvalue()[1]);
		assertEquals("C", dto.getSavoirEtreEvaluateur()[1]);
		
		assertEquals("C", dto.getManagerialEvalue()[2]);
		assertEquals("B", dto.getManagerialEvaluateur()[2]);
		
		assertEquals("D", dto.getResultatsEvalue()[3]);
		assertEquals("A", dto.getResultatsEvaluateur()[3]);
	}
	
	@Test
	public void testGetSerializerForEaeAppreciationsDto_includes_excludes() {
		
		// When
		List<PathExpression> includes = EaeAppreciationsDto.getSerializerForEaeAppreciationsDto().getIncludes();
		List<PathExpression> excludes = EaeAppreciationsDto.getSerializerForEaeAppreciationsDto().getExcludes();
		
		// Then
		assertEquals(1, includes.size());
		assertEquals("[*]", includes.get(0).toString());
		
		assertEquals(1, excludes.size());
		assertEquals("[*,class]", excludes.get(0).toString());
	}
	
	@Test
	public void testSerializeInJSON_emptyObject() {
		
		// Given
		EaeAppreciationsDto dto = new EaeAppreciationsDto();
		String expectedJson = "{\"estEncadrant\":false,\"idEae\":0,\"managerialEvaluateur\":[null,null,null,null],\"managerialEvalue\":[null,null,null,null],\"resultatsEvaluateur\":[null,null,null,null],\"resultatsEvalue\":[null,null,null,null],\"savoirEtreEvaluateur\":[null,null,null,null],\"savoirEtreEvalue\":[null,null,null,null],\"techniqueEvaluateur\":[null,null,null,null],\"techniqueEvalue\":[null,null,null,null]}";
		
		// When
		String json = dto.serializeInJSON();
		
		// Then
		assertEquals(expectedJson, json);
	}
	
	@Test
	public void testSerializeInJSON_filledObject() {
		
		// Given
		EaeAppreciationsDto dto = new EaeAppreciationsDto();
		dto.setIdEae(123);
		dto.setEstEncadrant(true);
		dto.setTechniqueEvalue(new String[]{"A", "B", "C", "D"});
		dto.setTechniqueEvaluateur(new String[]{"A", "B", "C", "D"});
		dto.setSavoirEtreEvalue(new String[]{"A", "B", "C", "D"});
		dto.setSavoirEtreEvaluateur(new String[]{"A", "B", "C", "D"});
		dto.setManagerialEvalue(new String[]{"A", "B", "C", "D"});
		dto.setManagerialEvaluateur(new String[]{"A", "B", "C", "D"});
		dto.setResultatsEvaluateur(new String[]{"A", "B", "C", "D"});
		dto.setResultatsEvalue(new String[]{"A", "B", "C", "D"});
		
		String expectedJson = "{\"estEncadrant\":true,\"idEae\":123,\"managerialEvaluateur\":[\"A\",\"B\",\"C\",\"D\"],\"managerialEvalue\":[\"A\",\"B\",\"C\",\"D\"],\"resultatsEvaluateur\":[\"A\",\"B\",\"C\",\"D\"],\"resultatsEvalue\":[\"A\",\"B\",\"C\",\"D\"],\"savoirEtreEvaluateur\":[\"A\",\"B\",\"C\",\"D\"],\"savoirEtreEvalue\":[\"A\",\"B\",\"C\",\"D\"],\"techniqueEvaluateur\":[\"A\",\"B\",\"C\",\"D\"],\"techniqueEvalue\":[\"A\",\"B\",\"C\",\"D\"]}";
		
		// When
		String json = dto.serializeInJSON();
		
		// Then
		assertEquals(expectedJson, json);
	}
	
	@Test
	public void deserializeFromJSON_filledInObject() {
		// Given
		String json = "{\"estEncadrant\":true,\"idEae\":123,\"managerialEvaluateur\":[\"A\",\"B\",\"C\",\"D\"],\"managerialEvalue\":[\"A\",\"B\",\"C\",\"D\"],\"resultatsEvaluateur\":[\"A\",\"B\",\"C\",\"D\"],\"resultatsEvalue\":[\"A\",\"B\",\"C\",\"D\"],\"savoirEtreEvaluateur\":[\"A\",\"B\",\"C\",\"D\"],\"savoirEtreEvalue\":[\"A\",\"B\",\"C\",\"D\"],\"techniqueEvaluateur\":[\"A\",\"B\",\"C\",\"D\"],\"techniqueEvalue\":[\"A\",\"B\",\"C\",\"D\"]}";
		
		// When
		EaeAppreciationsDto dto = new EaeAppreciationsDto().deserializeFromJSON(json);
		
		// Then
		assertEquals(123, dto.getIdEae());
		assertTrue(dto.isEstEncadrant());
		assertArrayEquals(new String[]{"A", "B", "C", "D"}, dto.getTechniqueEvalue());
		assertArrayEquals(new String[]{"A", "B", "C", "D"}, dto.getTechniqueEvaluateur());
		assertArrayEquals(new String[]{"A", "B", "C", "D"}, dto.getSavoirEtreEvalue());
		assertArrayEquals(new String[]{"A", "B", "C", "D"}, dto.getSavoirEtreEvaluateur());
		assertArrayEquals(new String[]{"A", "B", "C", "D"}, dto.getManagerialEvalue());
		assertArrayEquals(new String[]{"A", "B", "C", "D"}, dto.getManagerialEvaluateur());
		assertArrayEquals(new String[]{"A", "B", "C", "D"}, dto.getResultatsEvaluateur());
		assertArrayEquals(new String[]{"A", "B", "C", "D"}, dto.getResultatsEvalue());
	}
}
