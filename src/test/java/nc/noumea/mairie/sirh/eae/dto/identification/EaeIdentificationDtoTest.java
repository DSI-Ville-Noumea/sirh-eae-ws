package nc.noumea.mairie.sirh.eae.dto.identification;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
import nc.noumea.mairie.sirh.eae.domain.EaeFichePoste;
import nc.noumea.mairie.sirh.eae.domain.EaeFormation;
import nc.noumea.mairie.sirh.eae.domain.EaeParcoursPro;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeAgentPositionAdministrativeEnum;

import org.joda.time.DateTime;
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
		evalue.setPosition(EaeAgentPositionAdministrativeEnum.AC);
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
		
		EaeFichePoste fdp = new EaeFichePoste();
		fdp.setPrimary(true);
		eae.getEaeFichePostes().add(fdp);
		
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
		assertEquals(EaeAgentPositionAdministrativeEnum.AC, dto.getPosition());
		assertNotNull(dto.getSituation());
	}
	
	@Test
	public void testGetSerializerForEaeIdentificationDto_ListAllIncludesExcludes() {
		
		// When
		List<PathExpression> includes = EaeIdentificationDto.getSerializerForEaeIdentificationDto().getIncludes();
		List<PathExpression> excludes = EaeIdentificationDto.getSerializerForEaeIdentificationDto().getExcludes();
		
		// Then
		assertEquals(14, includes.size());
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
		assertEquals("[situation,*]", includes.get(11).toString());
		assertEquals("[statut,*]", includes.get(12).toString());
		assertEquals("[position]", includes.get(13).toString());
		
		assertEquals(2, excludes.size());
		assertEquals("[*,class]", excludes.get(0).toString());
		assertEquals("[*]", excludes.get(1).toString());
	}
	
	@Test
	public void testSerializeInJSON_SerializeEmptyObject() {
		
		// Given
		EaeIdentificationDto dto = new EaeIdentificationDto();
		
		String expectedResult = "{\"agent\":null,\"dateEntretien\":null,\"diplomes\":[],\"evaluateurs\":[],\"formations\":[],\"idEae\":0,\"parcoursPros\":[],\"position\":null,\"situation\":null,\"statut\":null}";
		
		// When
		String result = dto.serializeInJSON();
		
		// Then
		assertEquals(expectedResult, result);
	}
	
	@Test
	public void testSerializeInJSON_SerializeFilledInObject() {
		
		// Given
		Agent agentEvalue = new Agent();
		agentEvalue.setIdAgent(12);
		agentEvalue.setNomMarital("michelle");
		agentEvalue.setPrenom("michmich");
		agentEvalue.setDateNaissance(new DateTime(1957, 02, 03, 0, 0, 0, 0).toDate());
		
		Agent agentEvaluateur = new Agent();
		agentEvaluateur.setIdAgent(177);
		agentEvaluateur.setNomMarital("bonno");
		agentEvaluateur.setPrenom("patrice");
		agentEvaluateur.setDateNaissance(new DateTime(1980, 02, 03, 0, 0, 0, 0).toDate());
		
		EaeIdentificationDto dto = new EaeIdentificationDto();
		dto.setIdEae(789);
		dto.setDateEntretien(c.getTime());
		dto.setAgent(new EaeEvalue());
		dto.getAgent().setAgent(agentEvalue);
		EaeEvaluateur eval = new EaeEvaluateur();
		eval.setAgent(agentEvaluateur);
		dto.getEvaluateurs().add(eval);
		
		EaeDiplome d1 = new EaeDiplome();
		d1.setLibelleDiplome("diplome 1");
		dto.getDiplomes().add(d1);
		EaeDiplome d2 = new EaeDiplome();
		d2.setLibelleDiplome("diplome 2");
		dto.getDiplomes().add(d2);
		
		EaeFormation f1 = new EaeFormation();
		f1.setLibelleFormation("formation 1");
		dto.getFormations().add(f1);
		EaeFormation f2 = new EaeFormation();
		f2.setLibelleFormation("formation 2");
		dto.getFormations().add(f2);

		EaeParcoursPro p1 = new EaeParcoursPro();
		p1.setLibelleParcoursPro("parcours 1");
		dto.getParcoursPros().add(p1);
		
		EaeParcoursPro p2 = new EaeParcoursPro();
		p2.setLibelleParcoursPro("parcours 2");
		dto.getParcoursPros().add(p2);
		
		dto.setSituation(new EaeIdentificationSituationDto());
		dto.setStatut(new EaeIdentificationStatutDto());
		
		String expectedResult = "{\"agent\":{\"idAgent\":12,\"nom\":\"michelle\",\"nomJeuneFille\":null,\"prenom\":\"michmich\",\"dateNaissance\":\"/Date(-407415600000+1100)/\"},\"dateEntretien\":\"/Date(1337223959000+1100)/\",\"diplomes\":[\"diplome 1\",\"diplome 2\"],\"evaluateurs\":[{\"idAgent\":177,\"nom\":\"bonno\",\"prenom\":\"patrice\",\"dateEntreeCollectivite\":null,\"dateEntreeFonction\":null,\"dateEntreeService\":null,\"fonction\":null}],\"formations\":[\"formation 1\",\"formation 2\"],\"idEae\":789,\"parcoursPros\":[\"parcours 1\",\"parcours 2\"],\"position\":null,\"situation\":{\"dateEntreeAdministration\":null,\"dateEntreeFonction\":null,\"dateEntreeFonctionnaire\":null,\"directionService\":null,\"emploi\":null,\"fonction\":null},\"statut\":{\"ancienneteEchelonJours\":null,\"cadre\":null,\"categorie\":null,\"classification\":null,\"dateEffet\":null,\"echelon\":null,\"grade\":null,\"nouvEchelon\":null,\"nouvGrade\":null,\"statut\":null,\"statutPrecision\":null}}";
		
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
		
		String expectedResult = "{\"agent\":null,\"dateEntretien\":null,\"diplomes\":[],\"evaluateurs\":[{\"idAgent\":null,\"nom\":null,\"prenom\":null,\"dateEntreeCollectivite\":null,\"dateEntreeFonction\":null,\"dateEntreeService\":null,\"fonction\":null}],\"formations\":[],\"idEae\":0,\"parcoursPros\":[],\"position\":null,\"situation\":null,\"statut\":null}";
		
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
