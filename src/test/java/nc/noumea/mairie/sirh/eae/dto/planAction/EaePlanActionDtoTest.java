package nc.noumea.mairie.sirh.eae.dto.planAction;

import static org.junit.Assert.assertEquals;

import java.util.List;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaePlanAction;
import nc.noumea.mairie.sirh.eae.domain.EaeTypeObjectif;

import org.junit.BeforeClass;
import org.junit.Test;

import flexjson.PathExpression;

public class EaePlanActionDtoTest {

	private static EaeTypeObjectif t1, t2, t3, t4, t5;
	
	@BeforeClass
	public static void setUp() {
		t1 = new EaeTypeObjectif();
		t1.setLibelle("PROFESSIONNEL");
		
		t2 = new EaeTypeObjectif();
		t2.setLibelle("INDIVIDUEL");
		
		t3 = new EaeTypeObjectif();
		t3.setLibelle("MATERIELS");
		
		t4 = new EaeTypeObjectif();
		t4.setLibelle("FINANCIERS");
		
		t5 = new EaeTypeObjectif();
		t5.setLibelle("AUTRES");
	}
	
	@Test
	public void testConstructorWithEaeNoPlanAction() {
		// Given
		Eae eae = new Eae();
		eae.setIdEae(12);
		
		// When
		EaePlanActionDto dto = new EaePlanActionDto(eae);
		
		// Then
		assertEquals(12, dto.getIdEae());
	}
	
	@Test
	public void testConstructorWithEaePlanAction() {
		// Given
		Eae eae = new Eae();
		eae.setIdEae(12);
		
		EaePlanAction pa1 = new EaePlanAction();
		pa1.setObjectif("obj1");
		pa1.setMesure("mes1");
		pa1.setTypeObjectif(t1);
		
		EaePlanAction pa2 = new EaePlanAction();
		pa2.setObjectif("obj2");
		pa2.setMesure("mes2");
		pa2.setTypeObjectif(t2);
		
		EaePlanAction pa3 = new EaePlanAction();
		pa3.setObjectif("bes1");
		pa3.setTypeObjectif(t3);
		
		EaePlanAction pa4 = new EaePlanAction();
		pa4.setObjectif("bes2");
		pa4.setTypeObjectif(t4);
		
		EaePlanAction pa5 = new EaePlanAction();
		pa5.setObjectif("bes3");
		pa5.setTypeObjectif(t5);
		
		eae.getEaePlanActions().add(pa1);
		eae.getEaePlanActions().add(pa2);
		eae.getEaePlanActions().add(pa3);
		eae.getEaePlanActions().add(pa4);
		eae.getEaePlanActions().add(pa5);
		
		// When
		EaePlanActionDto dto = new EaePlanActionDto(eae);
		
		// Then
		assertEquals(12, dto.getIdEae());
		assertEquals(1, dto.getObjectifsProfessionnels().size());
		assertEquals("obj1", dto.getObjectifsProfessionnels().get(0).getObjectif());
		assertEquals("mes1", dto.getObjectifsProfessionnels().get(0).getIndicateur());
		assertEquals(1, dto.getObjectifsIndividuels().size());
		assertEquals("obj2", dto.getObjectifsIndividuels().get(0));
		assertEquals(1, dto.getMoyensMateriels().size());
		assertEquals("bes1", dto.getMoyensMateriels().get(0));
		assertEquals(1, dto.getMoyensFinanciers().size());
		assertEquals("bes2", dto.getMoyensFinanciers().get(0));
		assertEquals(1, dto.getMoyensAutres().size());
		assertEquals("bes3", dto.getMoyensAutres().get(0));
	}
	
	@Test
	public void testGetJSONSerializerForEaePlanActionDto_inlude_excludes() {
		// When
		List<PathExpression> includes = EaePlanActionDto.getJSONSerializerForEaePlanActionDto().getIncludes();
		List<PathExpression> excludes = EaePlanActionDto.getJSONSerializerForEaePlanActionDto().getExcludes();
		
		// Then
		assertEquals(1, includes.size());
		assertEquals("[*]", includes.get(0).toString());
		
		assertEquals(1, excludes.size());
		assertEquals("[*,class]", excludes.get(0).toString());
	}
	
	@Test
	public void testSerializeInJson_EmptyObject() {
		// Given
		EaePlanActionDto dto = new EaePlanActionDto();
		
		String expectedJson = "{\"idEae\":0,\"moyensAutres\":[],\"moyensFinanciers\":[],\"moyensMateriels\":[],\"objectifsIndividuels\":[],\"objectifsProfessionnels\":[]}";
		
		// When
		String json = dto.serializeInJSON();
		
		// Then
		assertEquals(expectedJson, json);
	}
	
	@Test
	public void testSerializeInJson_FilledInDto() {
		// Given
		EaePlanActionDto dto = new EaePlanActionDto();
		dto.setIdEae(113);
		
		PlanActionItemDto item = new PlanActionItemDto();
		item.setObjectif("obj1");
		item.setIndicateur("mes1");
		dto.getObjectifsProfessionnels().add(item);
		
		dto.getObjectifsIndividuels().add("obj2");
		dto.getMoyensMateriels().add("moy1");
		dto.getMoyensFinanciers().add("moy2");
		dto.getMoyensAutres().add("moy3");
		
		String expectedJson = "{\"idEae\":113,\"moyensAutres\":[\"moy3\"],\"moyensFinanciers\":[\"moy2\"],\"moyensMateriels\":[\"moy1\"],\"objectifsIndividuels\":[\"obj2\"],\"objectifsProfessionnels\":[{\"indicateur\":\"mes1\",\"objectif\":\"obj1\"}]}";
		
		// When
		String json = dto.serializeInJSON();
		
		// Then
		assertEquals(expectedJson, json);
	}
	
	@Test
	public void testDeserializeFromJson_emptyObject() {
		// Given
		String json = "{\"idEae\":113,\"moyensAutres\":[],\"moyensFinanciers\":[],\"moyensMateriels\":[],\"objectifsIndividuels\":[],\"objectifsProfessionnels\":[]}";
		
		// When
		EaePlanActionDto dto = new EaePlanActionDto().deserializeFromJSON(json);
		
		// Then
		assertEquals(113, dto.getIdEae());
		assertEquals(0, dto.getObjectifsProfessionnels().size());
		assertEquals(0, dto.getObjectifsIndividuels().size());
		assertEquals(0, dto.getMoyensFinanciers().size());
		assertEquals(0, dto.getMoyensMateriels().size());
		assertEquals(0, dto.getMoyensAutres().size());
	}
	
	@Test
	public void testDeserializeFromJson_filledInObject() {
		// Given
		String json = "{\"idEae\":113,\"moyensAutres\":[\"moy3\"],\"moyensFinanciers\":[\"moy2\"],\"moyensMateriels\":[\"moy1\"],\"objectifsIndividuels\":[\"obj2\"],\"objectifsProfessionnels\":[{\"indicateur\":\"mes1\",\"objectif\":\"obj1\"}]}";
		
		// When
		EaePlanActionDto dto = new EaePlanActionDto().deserializeFromJSON(json);
		
		// Then
		assertEquals(113, dto.getIdEae());
		assertEquals(1, dto.getObjectifsProfessionnels().size());
		assertEquals("obj1", dto.getObjectifsProfessionnels().get(0).getObjectif());
		assertEquals("mes1", dto.getObjectifsProfessionnels().get(0).getIndicateur());
		assertEquals(1, dto.getObjectifsIndividuels().size());
		assertEquals("obj2", dto.getObjectifsIndividuels().get(0));
		assertEquals(1, dto.getMoyensFinanciers().size());
		assertEquals("moy2", dto.getMoyensFinanciers().get(0));
		assertEquals(1, dto.getMoyensMateriels().size());
		assertEquals("moy1", dto.getMoyensMateriels().get(0));
		assertEquals(1, dto.getMoyensAutres().size());
		assertEquals("moy3", dto.getMoyensAutres().get(0));
	}
}
