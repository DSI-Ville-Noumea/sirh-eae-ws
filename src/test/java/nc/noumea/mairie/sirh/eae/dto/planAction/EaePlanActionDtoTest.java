package nc.noumea.mairie.sirh.eae.dto.planAction;

import static org.junit.Assert.assertEquals;
import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaePlanAction;
import nc.noumea.mairie.sirh.eae.domain.EaeTypeObjectif;

import org.junit.BeforeClass;
import org.junit.Test;

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
		assertEquals(1, dto.getListeObjectifsIndividuels().size());
		assertEquals("obj2", dto.getListeObjectifsIndividuels().get(0).getLibelle());
		assertEquals(1, dto.getListeMoyensMateriels().size());
		assertEquals("bes1", dto.getListeMoyensMateriels().get(0).getLibelle());
		assertEquals(1, dto.getListeMoyensFinanciers().size());
		assertEquals("bes2", dto.getListeMoyensFinanciers().get(0).getLibelle());
		assertEquals(1, dto.getListeMoyensAutres().size());
		assertEquals("bes3", dto.getListeMoyensAutres().get(0).getLibelle());
	}
}
