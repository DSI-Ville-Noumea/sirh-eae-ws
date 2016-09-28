package nc.noumea.mairie.sirh.eae.dto;

import static org.junit.Assert.assertEquals;
import nc.noumea.mairie.sirh.domain.Agent;
import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeFdpActivite;
import nc.noumea.mairie.sirh.eae.domain.EaeFdpCompetence;
import nc.noumea.mairie.sirh.eae.domain.EaeFichePoste;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeTypeCompetenceEnum;
import nc.noumea.mairie.sirh.eae.dto.poste.EaeFichePosteDto;

import org.junit.Test;

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
		f.setService("service");
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
		assertEquals(f.getService(), result.getService());
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
	public void testConstructorWithEaeFichePoste_WhenAgentShdIsNull() {
		
		// Given
		Eae eae = new Eae();
		eae.setIdEae(11);
		EaeFichePoste f = new EaeFichePoste();
		f.setEae(eae);
		f.setFonction("fonction");
		f.setGradePoste("grade poste");
		f.setEmploi("emploi");
		f.setService("service");
		f.setDirectionService("directionService");
		f.setLocalisation("localisation");
		f.setMissions("missions");
		f.setAgentShd(null);
		
		// When
		EaeFichePosteDto result = new EaeFichePosteDto(f);
		
		// Then
		assertEquals(eae.getIdEae(), new Integer(result.getIdEae()));
		assertEquals(f.getFonction(), result.getIntitule());
		assertEquals(f.getGradePoste(), result.getGrade());
		assertEquals(f.getEmploi(), result.getEmploi());
		assertEquals(f.getDirectionService(), result.getDirectionService());
		assertEquals(f.getService(), result.getService());
		assertEquals(f.getLocalisation(), result.getLocalisation());
		assertEquals(f.getMissions(), result.getMissions());
		assertEquals(null, result.getResponsableNom());
		assertEquals(null, result.getResponsablePrenom());
		assertEquals(f.getFonctionResponsable(), result.getResponsableFonction());
	}
}
