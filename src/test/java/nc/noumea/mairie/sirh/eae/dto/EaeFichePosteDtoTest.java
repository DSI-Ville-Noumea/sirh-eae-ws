package nc.noumea.mairie.sirh.eae.dto;

import static org.junit.Assert.assertEquals;

import java.util.List;

import nc.noumea.mairie.sirh.domain.Agent;
import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeFdpActivite;
import nc.noumea.mairie.sirh.eae.domain.EaeFdpCompetence;
import nc.noumea.mairie.sirh.eae.domain.EaeFichePoste;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeTypeCompetenceEnum;

import org.junit.Test;

import flexjson.PathExpression;

public class EaeFichePosteDtoTest {

	@Test
	public void testConstructorWithEaeFichePoste() {
		
		// Given
		Eae eae = new Eae();
		eae.setIdEae(11);
		EaeFichePoste f = new EaeFichePoste();
		f.setEae(eae);
		f.setFonction("fonction");
		f.setGradePoste("grade poste");
		f.setEmploi("emploi");
		f.setDirectionService("directionService");
		f.setLocalisation("localisation");
		f.setMissions("missions");
		Agent shd = new Agent();
		shd.setNomMarital("nom");
		shd.setPrenom("prenom");
		f.setAgentShd(shd);
		
		EaeFdpActivite act = new EaeFdpActivite();
		act.setLibelle("lii");
		f.getEaeFdpActivites().add(act);
		
		EaeFdpCompetence fdp1 = new EaeFdpCompetence();
		fdp1.setType(EaeTypeCompetenceEnum.SA);
		fdp1.setLibelle("savoir");
		f.getEaeFdpCompetences().add(fdp1);
		
		EaeFdpCompetence fdp2 = new EaeFdpCompetence();
		fdp2.setType(EaeTypeCompetenceEnum.SF);
		fdp2.setLibelle("savoir faire");
		f.getEaeFdpCompetences().add(fdp2);
		
		EaeFdpCompetence fdp3 = new EaeFdpCompetence();
		fdp3.setType(EaeTypeCompetenceEnum.CP);
		fdp3.setLibelle("comp pro");
		f.getEaeFdpCompetences().add(fdp3);
		
		// When
		EaeFichePosteDto result = new EaeFichePosteDto(f);
		
		// Then
		assertEquals(eae.getIdEae(), new Integer(result.getIdEae()));
		assertEquals(f.getFonction(), result.getIntitule());
		assertEquals(f.getGradePoste(), result.getGrade());
		assertEquals(f.getEmploi(), result.getEmploi());
		assertEquals(f.getDirectionService(), result.getDirectionService());
		assertEquals(f.getLocalisation(), result.getLocalisation());
		assertEquals(f.getMissions(), result.getMissions());
		assertEquals(f.getAgentShd().getDisplayNom(), result.getResponsableNom());
		assertEquals(f.getAgentShd().getDisplayPrenom(), result.getResponsablePrenom());
		assertEquals(f.getFonctionResponsable(), result.getResponsableFonction());
		assertEquals("lii", result.getActivites().get(0));
		
		assertEquals("savoir", result.getCompetencesSavoir().get(0));
		assertEquals("savoir faire", result.getCompetencesSavoirFaire().get(0));
		assertEquals("comp pro", result.getCompetencesComportementProfessionnel().get(0));
	}
	
	@Test
	public void testGetSerializerForEaeFichePosteDto_includes_excludes() {
		
		// When
		List<PathExpression> includes = EaeFichePosteDto.getSerializerForEaeFichePosteDto().getIncludes();
		List<PathExpression> excludes = EaeFichePosteDto.getSerializerForEaeFichePosteDto().getExcludes();
		
		// Then
		assertEquals(14, includes.size());
		assertEquals("[idEae]", includes.get(0).toString());
		assertEquals("[intitule]", includes.get(1).toString());
		assertEquals("[grade]", includes.get(2).toString());
		assertEquals("[emploi]", includes.get(3).toString());
		assertEquals("[directionService]", includes.get(4).toString());
		assertEquals("[localisation]", includes.get(5).toString());
		assertEquals("[missions]", includes.get(6).toString());
		assertEquals("[responsableNom]", includes.get(7).toString());
		assertEquals("[responsablePrenom]", includes.get(8).toString());
		assertEquals("[responsableFonction]", includes.get(9).toString());
		assertEquals("[activites,*]", includes.get(10).toString());
		assertEquals("[competencesSavoir,*]", includes.get(11).toString());
		assertEquals("[competencesSavoirFaire,*]", includes.get(12).toString());
		assertEquals("[competencesComportementProfessionnel,*]", includes.get(13).toString());
		
		assertEquals(1, excludes.size());
		assertEquals("[*]", excludes.get(0).toString());
	}
	
	@Test
	public void testSerializeInJSON_SerializeEmptyObject() {
		
		// Given
		EaeFichePosteDto dto = new EaeFichePosteDto();
		
		String expectedResult = "{\"activites\":[],\"competencesComportementProfessionnel\":[],\"competencesSavoir\":[],\"competencesSavoirFaire\":[],\"directionService\":null,\"emploi\":null,\"grade\":null,\"idEae\":0,\"intitule\":null,\"localisation\":null,\"missions\":null,\"responsableFonction\":null,\"responsableNom\":null,\"responsablePrenom\":null}";
		
		// When
		String result = dto.serializeInJSON();
		
		// Then
		assertEquals(expectedResult, result);
	}
	
	@Test
	public void testSerializeInJSON_SerializeFullObject() {
		
		// Given
		EaeFichePosteDto dto = new EaeFichePosteDto();
		dto.setIdEae(123);
		dto.setIntitule("intitule");
		dto.setGrade("grade");
		dto.setEmploi("emploi");
		dto.setDirectionService("directionService");
		dto.setMissions("missions");
		dto.setResponsableFonction("responsableFonction");
		dto.setResponsableNom("responsableNom");
		dto.setResponsablePrenom("responsablePrenom");
		dto.setLocalisation("localisation");
		dto.getActivites().add("act1");
		dto.getActivites().add("act2");
		dto.getCompetencesSavoir().add("comp1");
		dto.getCompetencesSavoirFaire().add("comp2");
		dto.getCompetencesComportementProfessionnel().add("comp3");
		
		String expectedResult = "{\"activites\":[\"act1\",\"act2\"],\"competencesComportementProfessionnel\":[\"comp3\"],\"competencesSavoir\":[\"comp1\"],\"competencesSavoirFaire\":[\"comp2\"],\"directionService\":\"directionService\",\"emploi\":\"emploi\",\"grade\":\"grade\",\"idEae\":123,\"intitule\":\"intitule\",\"localisation\":\"localisation\",\"missions\":\"missions\",\"responsableFonction\":\"responsableFonction\",\"responsableNom\":\"responsableNom\",\"responsablePrenom\":\"responsablePrenom\"}";
		
		// When
		String result = dto.serializeInJSON();
		
		// Then
		assertEquals(expectedResult, result);
	}
}
