package nc.noumea.mairie.sirh.eae.dto;

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
		ap1.setNoteEvalue("NA");
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
		assertEquals("NA", dto.getTechniqueEvalue()[0]);
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
}
