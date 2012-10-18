package nc.noumea.mairie.sirh.eae.dto;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nc.noumea.mairie.sirh.domain.Agent;
import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeDiplome;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluateur;
import nc.noumea.mairie.sirh.eae.domain.EaeEvalue;
import nc.noumea.mairie.sirh.eae.domain.EaeFormation;
import nc.noumea.mairie.sirh.eae.domain.EaeParcoursPro;

import org.junit.BeforeClass;
import org.junit.Test;

import flexjson.PathExpression;

public class EaeIdentificationDtoTest {

	private static Calendar c;
	
	@BeforeClass
	public static void SetUp() {
		c = new GregorianCalendar();
		c.clear();
		c.set(2012, 04, 17, 14, 05, 59);
	}
	
	@Test
	public void testEaeIdentificationDto_FromEae() {
		// Given 
		Eae eae = new Eae();
		eae.setIdEae(89);
		eae.setDateEntretien(c.getTime());
		
		Set<EaeEvaluateur> evals = new HashSet<EaeEvaluateur>();
		evals.add(new EaeEvaluateur());
		eae.setEaeEvaluateurs(evals);
		
		EaeEvalue evalue = new EaeEvalue();
		eae.setEaeEvalue(evalue);
		
		Set<EaeDiplome> diplomes = new HashSet<EaeDiplome>();
		diplomes.add(new EaeDiplome());
		eae.setEaeDiplomes(diplomes);
		
		Set<EaeParcoursPro> parcours = new HashSet<EaeParcoursPro>();
		parcours.add(new EaeParcoursPro());
		eae.setEaeParcoursPros(parcours);
		
		Set<EaeFormation> formations = new HashSet<EaeFormation>();
		formations.add(new EaeFormation());
		eae.setEaeFormations(formations);
		
		// When
		EaeIdentificationDto dto = new EaeIdentificationDto(eae);
		
		// Then
		assertEquals(89, dto.getIdEae());
		assertEquals(c.getTime(), dto.getDateEntretien());
		assertEquals(1, dto.getEvaluateurs().size());
		assertEquals(evals.iterator().next(), dto.getEvaluateurs().get(0));
		assertEquals(evalue, dto.getAgent());
		assertEquals(1, dto.getDiplomes().size());
		assertEquals(diplomes.iterator().next(), dto.getDiplomes().get(0));
		assertEquals(1, dto.getParcoursPros().size());
		assertEquals(parcours.iterator().next(), dto.getParcoursPros().get(0));
		assertEquals(1, dto.getFormations().size());
		assertEquals(formations.iterator().next(), dto.getFormations().get(0));
	}
	
	@Test
	public void testGetSerializerForEaeIdentificationDto_ListAllIncludesExcludes() {
		
		// When
		List<PathExpression> includes = EaeIdentificationDto.getSerializerForEaeIdentificationDto().getIncludes();
		List<PathExpression> excludes = EaeIdentificationDto.getSerializerForEaeIdentificationDto().getExcludes();
		
		// Then
		assertEquals(11, includes.size());
		assertEquals("[idEae]", includes.get(0).toString());
		assertEquals("[dateEntretien]", includes.get(1).toString());
		assertEquals("[evaluateurs,agent]", includes.get(2).toString());
		assertEquals("[evaluateurs,fonction]", includes.get(3).toString());
		assertEquals("[evaluateurs,dateEntreeService]", includes.get(4).toString());
		assertEquals("[evaluateurs,dateEntreeCollectivite]", includes.get(5).toString());
		assertEquals("[evaluateurs,dateEntreeFonction]", includes.get(6).toString());
		assertEquals("[agent]", includes.get(7).toString());
		assertEquals("[diplomes]", includes.get(8).toString());
		assertEquals("[parcoursPros]", includes.get(9).toString());
		assertEquals("[formations]", includes.get(10).toString());
		
		assertEquals(1, excludes.size());
		assertEquals("[*]", excludes.get(0).toString());
	}
	
	@Test
	public void testSerializeInJSON_SerializeEmptyObject() {
		
		// Given
		EaeIdentificationDto dto = new EaeIdentificationDto();
		
		String expectedResult = "{\"agent\":null,\"dateEntretien\":null,\"diplomes\":[],\"evaluateurs\":[],\"formations\":[],\"idEae\":0,\"parcoursPros\":[]}";
		
		// When
		String result = dto.serializeInJSON();
		
		// Then
		assertEquals(expectedResult, result);
	}
	
	@Test
	public void testSerializeInJSON_SerializeFilledInObject() {
		
		// Given
		EaeIdentificationDto dto = new EaeIdentificationDto();
		dto.setIdEae(789);
		dto.setDateEntretien(c.getTime());
		dto.setAgent(new EaeEvalue());
		dto.getEvaluateurs().add(new EaeEvaluateur());
		dto.getDiplomes().add(new EaeDiplome());
		dto.getFormations().add(new EaeFormation());
		dto.getParcoursPros().add(new EaeParcoursPro());
		
		String expectedResult = "{\"agent\":{},\"dateEntretien\":\"/DATE(1337223959000)/\",\"diplomes\":[{}],\"evaluateurs\":[{\"agent\":null,\"dateEntreeCollectivite\":null,\"dateEntreeFonction\":null,\"dateEntreeService\":null,\"fonction\":null}],\"formations\":[{}],\"idEae\":789,\"parcoursPros\":[{}]}";
		
		// When
		String result = dto.serializeInJSON();
		
		// Then
		assertEquals(expectedResult, result);
	}
	
	@Test
	public void testSerializeInJSON_SerializeEvaluateursWithInlineAgentFormat() {
		
		// Given
		EaeIdentificationDto dto = new EaeIdentificationDto();
		EaeEvaluateur eval1 = new EaeEvaluateur();
		eval1.setAgent(new Agent());
		dto.getEvaluateurs().add(eval1);
		
		String expectedResult = "{\"agent\":null,\"dateEntretien\":null,\"diplomes\":[],\"evaluateurs\":[{\"idAgent\":null,\"nom\":null,\"prenom\":null,\"dateEntreeCollectivite\":null,\"dateEntreeFonction\":null,\"dateEntreeService\":null,\"fonction\":null}],\"formations\":[],\"idEae\":0,\"parcoursPros\":[]}";
		
		// When
		String result = dto.serializeInJSON();
		
		// Then
		assertEquals(expectedResult, result);
	}
	
	@Test
	public void testDeserializeFromJSON_dateEntretien() {
		
		// Given
		Calendar c = new GregorianCalendar();
		c.clear();
		c.set(2012,  01, 18);
		String json = "{\"dateEntretien\":\"/DATE(" + c.getTimeInMillis() + ")/\"}";

		// When
		EaeIdentificationDto dto = new EaeIdentificationDto().deserializeFromJSON(json);
		
		// Then
		assertEquals(c.getTime(), dto.getDateEntretien());
	}
}
