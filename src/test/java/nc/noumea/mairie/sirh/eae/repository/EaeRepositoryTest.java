package nc.noumea.mairie.sirh.eae.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeCampagne;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluateur;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluation;
import nc.noumea.mairie.sirh.eae.domain.EaeEvalue;
import nc.noumea.mairie.sirh.eae.domain.EaeFichePoste;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeAvancementEnum;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeEtatEnum;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/META-INF/spring/applicationContext-test.xml" })
public class EaeRepositoryTest {

	@Autowired
	IEaeRepository repository;

	@PersistenceContext(unitName = "eaePersistenceUnit")
	private EntityManager eaePersistenceUnit;

	@Test
	@Transactional("eaeTransactionManager")
	public void findEaeCampagneByAnnee_1result() {

		EaeCampagne eaeCampagne = new EaeCampagne();
		eaeCampagne.setAnnee(2010);
		eaeCampagne.setDateDebut(new Date());
		eaePersistenceUnit.persist(eaeCampagne);

		EaeCampagne result = repository.findEaeCampagneByAnnee(2010);

		assertNotNull(result);
		assertEquals(2010, result.getAnnee());
	}

	@Test
	@Transactional("eaeTransactionManager")
	public void findEaeCampagneByAnnee_noResult() {

		EaeCampagne eaeCampagne = new EaeCampagne();
		eaeCampagne.setAnnee(2010);
		eaeCampagne.setDateDebut(new Date());
		eaePersistenceUnit.persist(eaeCampagne);

		EaeCampagne result = repository.findEaeCampagneByAnnee(2011);

		assertNull(result);
	}

	@Test
	@Transactional("eaeTransactionManager")
	public void findEaeAgent_1result() {

		EaeCampagne eaeCampagne = new EaeCampagne();
		eaeCampagne.setAnnee(2010);
		eaeCampagne.setDateDebut(new Date());
		eaePersistenceUnit.persist(eaeCampagne);

		EaeEvalue eaeEvalue = new EaeEvalue();
		eaeEvalue.setIdAgent(9005138);

		Eae eae = new Eae();
		eae.setCap(true);
		eae.setEtat(EaeEtatEnum.C);
		eae.setEaeEvalue(eaeEvalue);
		eae.setEaeCampagne(eaeCampagne);
		eaePersistenceUnit.persist(eae);

		eaeEvalue.setEae(eae);
		eaePersistenceUnit.persist(eaeEvalue);

		Eae result = repository.findEaeAgent(9005138, eaeCampagne.getIdCampagneEae());

		assertNotNull(result);
		assertEquals(EaeEtatEnum.C, result.getEtat());
		assertTrue(result.isCap());
	}

	@Test
	@Transactional("eaeTransactionManager")
	public void findEaeAgent_badAgent() {

		EaeCampagne eaeCampagne = new EaeCampagne();
		eaeCampagne.setAnnee(2010);
		eaeCampagne.setDateDebut(new Date());
		eaePersistenceUnit.persist(eaeCampagne);

		EaeEvalue eaeEvalue = new EaeEvalue();
		eaeEvalue.setIdAgent(9005138);

		Eae eae = new Eae();
		eae.setCap(true);
		eae.setEtat(EaeEtatEnum.C);
		eae.setEaeEvalue(eaeEvalue);
		eae.setEaeCampagne(eaeCampagne);
		eaePersistenceUnit.persist(eae);

		eaeEvalue.setEae(eae);
		eaePersistenceUnit.persist(eaeEvalue);

		Eae result = repository.findEaeAgent(9005137, eaeCampagne.getIdCampagneEae());

		assertNull(result);
	}

	@Test
	@Transactional("eaeTransactionManager")
	public void findEaeAgent_badCampagne() {

		EaeCampagne eaeCampagne = new EaeCampagne();
		eaeCampagne.setAnnee(2010);
		eaeCampagne.setDateDebut(new Date());
		eaePersistenceUnit.persist(eaeCampagne);

		EaeEvalue eaeEvalue = new EaeEvalue();
		eaeEvalue.setIdAgent(9005138);

		Eae eae = new Eae();
		eae.setCap(true);
		eae.setEtat(EaeEtatEnum.C);
		eae.setEaeEvalue(eaeEvalue);
		eae.setEaeCampagne(eaeCampagne);
		eaePersistenceUnit.persist(eae);

		eaeEvalue.setEae(eae);
		eaePersistenceUnit.persist(eaeEvalue);

		Eae result = repository.findEaeAgent(9005138, eaeCampagne.getIdCampagneEae() + 1);

		assertNull(result);
	}
	
	@Test
	@Transactional("eaeTransactionManager")
	public void findEaeEvalueWithEaesByIdAgentOnly_badAgent() {
		
		EaeCampagne eaeCampagne = new EaeCampagne();
		eaeCampagne.setAnnee(2014);
		eaeCampagne.setDateDebut(new Date());
		eaePersistenceUnit.persist(eaeCampagne);
		
		EaeEvalue eaeEvalue = new EaeEvalue();
		eaeEvalue.setIdAgent(9005138);

		Eae eae = new Eae();
		eae.setCap(true);
		eae.setEtat(EaeEtatEnum.C);
		eae.setEaeEvalue(eaeEvalue);
		eae.setEaeCampagne(eaeCampagne);
		eaePersistenceUnit.persist(eae);

		eaeEvalue.setEae(eae);
		eaePersistenceUnit.persist(eaeEvalue);
		
		List<EaeEvalue> result = repository.findEaeEvalueWithEaesByIdAgentOnly(9000000);
		
		assertEquals(0, result.size());
	}
	
	@Test
	@Transactional("eaeTransactionManager")
	public void findEaeEvalueWithEaesByIdAgentOnly_badAnnee() {
		
		EaeCampagne eaeCampagne = new EaeCampagne();
		eaeCampagne.setAnnee(2012);
		eaeCampagne.setDateDebut(new Date());
		eaePersistenceUnit.persist(eaeCampagne);
		
		EaeEvalue eaeEvalue = new EaeEvalue();
		eaeEvalue.setIdAgent(9005138);

		Eae eae = new Eae();
		eae.setCap(true);
		eae.setEtat(EaeEtatEnum.C);
		eae.setEaeEvalue(eaeEvalue);
		eae.setEaeCampagne(eaeCampagne);
		eaePersistenceUnit.persist(eae);

		eaeEvalue.setEae(eae);
		eaePersistenceUnit.persist(eaeEvalue);
		
		List<EaeEvalue> result = repository.findEaeEvalueWithEaesByIdAgentOnly(9005138);
		
		assertEquals(0, result.size());
	}
	
	@Test
	@Transactional("eaeTransactionManager")
	public void findEaeEvalueWithEaesByIdAgentOnly_ok() {
		
		EaeCampagne eaeCampagne = new EaeCampagne();
		eaeCampagne.setAnnee(2014);
		eaeCampagne.setDateDebut(new Date());
		eaePersistenceUnit.persist(eaeCampagne);
		
		EaeEvalue eaeEvalue = new EaeEvalue();
		eaeEvalue.setIdAgent(9005138);

		Eae eae = new Eae();
		eae.setCap(true);
		eae.setEtat(EaeEtatEnum.C);
		eae.setEaeEvalue(eaeEvalue);
		eae.setEaeCampagne(eaeCampagne);
		eaePersistenceUnit.persist(eae);

		eaeEvalue.setEae(eae);
		eaePersistenceUnit.persist(eaeEvalue);
		
		List<EaeEvalue> result = repository.findEaeEvalueWithEaesByIdAgentOnly(9005138);
		
		assertEquals(1, result.size());
	}
	
	@Test
	@Transactional("eaeTransactionManager")
	public void findEae_badAgent() {

		EaeCampagne eaeCampagne = new EaeCampagne();
		eaeCampagne.setAnnee(2010);
		eaeCampagne.setDateDebut(new Date());
		eaePersistenceUnit.persist(eaeCampagne);

		EaeEvalue eaeEvalue = new EaeEvalue();
		eaeEvalue.setIdAgent(9005138);

		Eae eae = new Eae();
		eae.setCap(true);
		eae.setEtat(EaeEtatEnum.C);
		eae.setEaeEvalue(eaeEvalue);
		eae.setEaeCampagne(eaeCampagne);
		eaePersistenceUnit.persist(eae);

		eaeEvalue.setEae(eae);
		eaePersistenceUnit.persist(eaeEvalue);

		Eae result = repository.findEae(eae.getIdEae()+1);

		assertNull(result);
	}
	

	
	@Test
	@Transactional("eaeTransactionManager")
	public void findEae_ok() {

		EaeCampagne eaeCampagne = new EaeCampagne();
		eaeCampagne.setAnnee(2010);
		eaeCampagne.setDateDebut(new Date());
		eaePersistenceUnit.persist(eaeCampagne);

		EaeEvalue eaeEvalue = new EaeEvalue();
		eaeEvalue.setIdAgent(9005138);

		Eae eae = new Eae();
		eae.setCap(true);
		eae.setEtat(EaeEtatEnum.C);
		eae.setEaeEvalue(eaeEvalue);
		eae.setEaeCampagne(eaeCampagne);
		eaePersistenceUnit.persist(eae);

		eaeEvalue.setEae(eae);
		eaePersistenceUnit.persist(eaeEvalue);

		Eae result = repository.findEae(eae.getIdEae());

		assertNotNull(result);
	}

	@Test
	@Transactional("eaeTransactionManager")
	public void findEaesNonDebuteOuCreeOuEnCoursForEaeListByAgentIds() {
		
		List<Integer> agentIds = new ArrayList<Integer>(); 
		agentIds.add(9005131);
		Integer agentId = 9005138;
		
		EaeCampagne eaeCampagne = new EaeCampagne();
		eaeCampagne.setAnnee(2010);
		eaeCampagne.setDateDebut(new Date());
		eaeCampagne.setDateOuvertureKiosque(new DateTime(2015,1,1,0,0,0).toDate());
		eaePersistenceUnit.persist(eaeCampagne);

		// 1er EAE retourne par la requete par son EVALUE
		EaeEvalue eaeEvalue = new EaeEvalue();
		eaeEvalue.setIdAgent(9005131);

		Eae eae = new Eae();
		eae.setCap(true);
		eae.setEaeEvalue(eaeEvalue);
		eae.setEaeCampagne(eaeCampagne);
		eae.setEtat(EaeEtatEnum.ND);
		eaePersistenceUnit.persist(eae);

		eaeEvalue.setEae(eae);
		eaePersistenceUnit.persist(eaeEvalue);

		// 2e EAE retourne par la requete par son EVALUATEUR
		EaeEvalue eaeEvalue2 = new EaeEvalue();
		eaeEvalue2.setIdAgent(9005141);

		EaeEvaluateur evaluateur = new EaeEvaluateur();
		evaluateur.setIdAgent(agentId);
		
		Eae eae2 = new Eae();
		eae2.setCap(true);
		eae2.setEaeEvalue(eaeEvalue);
		eae2.setEaeCampagne(eaeCampagne);
		eae2.setEtat(EaeEtatEnum.EC);
		eae2.getEaeEvaluateurs().add(evaluateur);
		eaePersistenceUnit.persist(eae2);

		eaeEvalue2.setEae(eae2);
		eaePersistenceUnit.persist(eaeEvalue2);
		
		evaluateur.setEae(eae2);
		eaePersistenceUnit.persist(evaluateur);

		// 3e EAE non retourne
		EaeEvalue eaeEvalue3 = new EaeEvalue();
		eaeEvalue3.setIdAgent(9005142);

		EaeEvaluateur evaluateur3 = new EaeEvaluateur();
		evaluateur3.setIdAgent(agentId+1);
		
		Eae eae3 = new Eae();
		eae3.setCap(true);
		eae3.setEaeEvalue(eaeEvalue3);
		eae3.setEaeCampagne(eaeCampagne);
		eae3.setEtat(EaeEtatEnum.EC);
		eae3.getEaeEvaluateurs().add(evaluateur3);
		eaePersistenceUnit.persist(eae3);

		eaeEvalue3.setEae(eae3);
		eaePersistenceUnit.persist(eaeEvalue3);
		
		evaluateur3.setEae(eae3);
		eaePersistenceUnit.persist(evaluateur3);
		
		List<Eae> result = repository.findEaesNonDebuteOuCreeOuEnCoursForEaeListByAgentIds(agentIds, agentId);
		
		assertEquals(2, result.size());
	}
	
	@Test
	@Transactional("eaeTransactionManager")
	public void getListEaeFichePosteParDirectionEtSection() {
		
		EaeCampagne eaeCampagne = new EaeCampagne();
		eaeCampagne.setAnnee(2010);
		eaeCampagne.setDateDebut(new Date());
		eaeCampagne.setDateOuvertureKiosque(new DateTime(2015,1,1,0,0,0).toDate());
		eaePersistenceUnit.persist(eaeCampagne);
		
		/////// 1er fiche de poste ///////
		EaeEvalue eaeEvalue = new EaeEvalue();
		eaeEvalue.setIdAgent(9005142);
		
		Eae eae = new Eae();
		eae.setCap(true);
		eae.setEaeEvalue(eaeEvalue);
		eae.setEaeCampagne(eaeCampagne);
		eae.setEtat(EaeEtatEnum.EC);
		eaePersistenceUnit.persist(eae);

		eaeEvalue.setEae(eae);
		eaePersistenceUnit.persist(eaeEvalue);
		
		EaeFichePoste f = new EaeFichePoste();
		f.setEae(eae);
		f.setFonction("fonction");
		f.setGradePoste("grade poste");
		f.setEmploi("emploi");
		f.setDirectionService("directionService");
		f.setService("service");
		f.setSectionService("sectionService");
		f.setLocalisation("localisation");
		f.setMissions("missions");

		eaePersistenceUnit.persist(f);
		
		/////// 2e fiche de poste ///////
		EaeEvalue eaeEvalue2 = new EaeEvalue();
		eaeEvalue2.setIdAgent(9005142);
		
		Eae eae2 = new Eae();
		eae2.setCap(true);
		eae2.setEaeEvalue(eaeEvalue2);
		eae2.setEaeCampagne(eaeCampagne);
		eae2.setEtat(EaeEtatEnum.EC);
		eaePersistenceUnit.persist(eae2);

		eaeEvalue2.setEae(eae2);
		eaePersistenceUnit.persist(eaeEvalue2);
		
		EaeFichePoste f2 = new EaeFichePoste();
		f2.setEae(eae2);
		f2.setFonction("fonction");
		f2.setGradePoste("grade poste");
		f2.setEmploi("emploi");
		f2.setDirectionService("directionService");
		f2.setService("service");
		f2.setSectionService("sectionService 2");
		f2.setLocalisation("localisation");
		f2.setMissions("missions");

		eaePersistenceUnit.persist(f2);
		
		/////// 3e fiche de poste ///////
		EaeEvalue eaeEvalue3 = new EaeEvalue();
		eaeEvalue3.setIdAgent(9005142);
		
		Eae eae3 = new Eae();
		eae3.setCap(true);
		eae3.setEaeEvalue(eaeEvalue3);
		eae3.setEaeCampagne(eaeCampagne);
		eae3.setEtat(EaeEtatEnum.EC);
		eaePersistenceUnit.persist(eae3);

		eaeEvalue3.setEae(eae3);
		eaePersistenceUnit.persist(eaeEvalue3);
		
		EaeFichePoste f3 = new EaeFichePoste();
		f3.setEae(eae3);
		f3.setFonction("fonction");
		f3.setGradePoste("grade poste");
		f3.setEmploi("emploi");
		f3.setDirectionService("directionService 2");
		f3.setService("service");
		f3.setSectionService("sectionService 3");
		f3.setLocalisation("localisation");
		f3.setMissions("missions");

		eaePersistenceUnit.persist(f3);
		
		Map<String, List<String>> result = repository.getListEaeFichePosteParDirectionEtSection(eaeCampagne.getIdCampagneEae());
		
		assertEquals("sectionService", result.get("directionService").get(0));
		assertEquals("sectionService 2", result.get("directionService").get(1));
		assertEquals("sectionService 3", result.get("directionService 2").get(0));
	}
	
	@Test
	@Transactional("eaeTransactionManager")
	public void countEaeByCampagneAndDirectionAndSectionAndStatut() {

		EaeCampagne eaeCampagne = new EaeCampagne();
		eaeCampagne.setAnnee(2010);
		eaeCampagne.setDateDebut(new Date());
		eaeCampagne.setDateOuvertureKiosque(new DateTime(2015,1,1,0,0,0).toDate());
		eaePersistenceUnit.persist(eaeCampagne);
		
		/////// 1er fiche de poste ///////
		EaeEvalue eaeEvalue = new EaeEvalue();
		eaeEvalue.setIdAgent(9005142);
		
		Eae eaeEnCours = new Eae();
		eaeEnCours.setCap(true);
		eaeEnCours.setEaeEvalue(eaeEvalue);
		eaeEnCours.setEaeCampagne(eaeCampagne);
		eaeEnCours.setEtat(EaeEtatEnum.EC);
		eaePersistenceUnit.persist(eaeEnCours);

		eaeEvalue.setEae(eaeEnCours);
		eaePersistenceUnit.persist(eaeEvalue);
		
		EaeFichePoste f = new EaeFichePoste();
		f.setEae(eaeEnCours);
		f.setFonction("fonction");
		f.setGradePoste("grade poste");
		f.setEmploi("emploi");
		f.setDirectionService("directionService");
		f.setService("service");
		f.setSectionService("sectionService");
		f.setLocalisation("localisation");
		f.setMissions("missions");

		eaePersistenceUnit.persist(f);
		
		// EAE En cours BIS
		EaeEvalue eaeEvalueBis = new EaeEvalue();
		eaeEvalueBis.setIdAgent(9005148);
		
		Eae eaeEnCoursBis = new Eae();
		eaeEnCoursBis.setCap(false);
		eaeEnCoursBis.setEaeEvalue(eaeEvalueBis);
		eaeEnCoursBis.setEaeCampagne(eaeCampagne);
		eaeEnCoursBis.setEtat(EaeEtatEnum.EC);
		eaePersistenceUnit.persist(eaeEnCoursBis);

		eaeEvalueBis.setEae(eaeEnCoursBis);
		eaePersistenceUnit.persist(eaeEvalueBis);
		
		EaeFichePoste fBis = new EaeFichePoste();
		fBis.setEae(eaeEnCoursBis);
		fBis.setFonction("fonction");
		fBis.setGradePoste("grade poste");
		fBis.setEmploi("emploi");
		fBis.setDirectionService("directionService");
		fBis.setService("service");
		fBis.setSectionService("sectionService 2");
		fBis.setLocalisation("localisation");
		fBis.setMissions("missions");

		eaePersistenceUnit.persist(fBis);
		
		/////// 2e fiche de poste ///////
		EaeEvalue eaeEvalue2 = new EaeEvalue();
		eaeEvalue2.setIdAgent(9005142);
		
		Eae eaeNonDebute = new Eae();
		eaeNonDebute.setCap(true);
		eaeNonDebute.setEaeEvalue(eaeEvalue2);
		eaeNonDebute.setEaeCampagne(eaeCampagne);
		eaeNonDebute.setEtat(EaeEtatEnum.ND);
		eaePersistenceUnit.persist(eaeNonDebute);

		eaeEvalue2.setEae(eaeNonDebute);
		eaePersistenceUnit.persist(eaeEvalue2);
		
		EaeFichePoste f2 = new EaeFichePoste();
		f2.setEae(eaeNonDebute);
		f2.setFonction("fonction");
		f2.setGradePoste("grade poste");
		f2.setEmploi("emploi");
		f2.setDirectionService("directionService");
		f2.setService("service");
		f2.setSectionService("sectionService 2");
		f2.setLocalisation("localisation");
		f2.setMissions("missions");

		eaePersistenceUnit.persist(f2);
		
		/////// 3e fiche de poste ///////
		EaeEvalue eaeEvalue3 = new EaeEvalue();
		eaeEvalue3.setIdAgent(9005142);
		
		Eae eaeFinalise = new Eae();
		eaeFinalise.setCap(true);
		eaeFinalise.setEaeEvalue(eaeEvalue3);
		eaeFinalise.setEaeCampagne(eaeCampagne);
		eaeFinalise.setEtat(EaeEtatEnum.F);
		eaePersistenceUnit.persist(eaeFinalise);

		eaeEvalue3.setEae(eaeFinalise);
		eaePersistenceUnit.persist(eaeEvalue3);
		
		EaeFichePoste f3 = new EaeFichePoste();
		f3.setEae(eaeFinalise);
		f3.setFonction("fonction");
		f3.setGradePoste("grade poste");
		f3.setEmploi("emploi");
		f3.setDirectionService("directionService 2");
		f3.setService("service");
		f3.setSectionService("sectionService 3");
		f3.setLocalisation("localisation");
		f3.setMissions("missions");

		eaePersistenceUnit.persist(f3);
		
		// TEST OK
		assertEquals(2, repository.countEaeByCampagneAndDirectionAndSectionAndStatut(
				eaeCampagne.getIdCampagneEae(), "directionService", null, EaeEtatEnum.EC.name(), false).intValue());
		
		// bad campagne
		assertEquals(0, repository.countEaeByCampagneAndDirectionAndSectionAndStatut(
				eaeCampagne.getIdCampagneEae()+1, "directionService", null, EaeEtatEnum.EC.name(), false).intValue());
		
		// bad direction
		assertEquals(0, repository.countEaeByCampagneAndDirectionAndSectionAndStatut(
				eaeCampagne.getIdCampagneEae(), "bad directionService", null, EaeEtatEnum.EC.name(), false).intValue());
		
		// TEST OK section
		assertEquals(1, repository.countEaeByCampagneAndDirectionAndSectionAndStatut(
				eaeCampagne.getIdCampagneEae(), "directionService", "sectionService", EaeEtatEnum.EC.name(), false).intValue());
		
		// TEST OK section
		assertEquals(1, repository.countEaeByCampagneAndDirectionAndSectionAndStatut(
				eaeCampagne.getIdCampagneEae(), "directionService", "sectionService 2", EaeEtatEnum.EC.name(), false).intValue());
		
		// bad section
		assertEquals(0, repository.countEaeByCampagneAndDirectionAndSectionAndStatut(
				eaeCampagne.getIdCampagneEae(), "directionService", "sectionService 3", EaeEtatEnum.EC.name(), false).intValue());
		
		// TEST OK cap true
		assertEquals(1, repository.countEaeByCampagneAndDirectionAndSectionAndStatut(
				eaeCampagne.getIdCampagneEae(), "directionService", null, EaeEtatEnum.EC.name(), true).intValue());
		
		// Test 2e FP
		assertEquals(1, repository.countEaeByCampagneAndDirectionAndSectionAndStatut(
				eaeCampagne.getIdCampagneEae(), "directionService", "sectionService 2", EaeEtatEnum.ND.name(), true).intValue());
		
		// Test 2e FP
		assertEquals(1, repository.countEaeByCampagneAndDirectionAndSectionAndStatut(
				eaeCampagne.getIdCampagneEae(), "directionService 2", "sectionService 3", EaeEtatEnum.F.name(), true).intValue());
	}
	

	@Test
	@Transactional("eaeTransactionManager")
	public void countAvisSHD() {

		EaeCampagne eaeCampagne = new EaeCampagne();
		eaeCampagne.setAnnee(2010);
		eaeCampagne.setDateDebut(new Date());
		eaeCampagne.setDateOuvertureKiosque(new DateTime(2015,1,1,0,0,0).toDate());
		eaePersistenceUnit.persist(eaeCampagne);
		
		/////// 1er fiche de poste ///////
		EaeEvaluation evaluation = new EaeEvaluation();
		evaluation.setPropositionAvancement(EaeAvancementEnum.MINI);
		evaluation.setAvisChangementClasse(true);
		evaluation.setAvisRevalorisation(true);
		
		EaeEvalue eaeEvalue = new EaeEvalue();
		eaeEvalue.setIdAgent(9005142);
		
		Eae eaeEnCours = new Eae();
		eaeEnCours.setCap(true);
		eaeEnCours.setEaeEvalue(eaeEvalue);
		eaeEnCours.setEaeCampagne(eaeCampagne);
		eaeEnCours.setEtat(EaeEtatEnum.EC);
		eaeEnCours.setEaeEvaluation(evaluation);
		eaePersistenceUnit.persist(eaeEnCours);

		eaeEvalue.setEae(eaeEnCours);
		eaePersistenceUnit.persist(eaeEvalue);
		
		evaluation.setEae(eaeEnCours);
		eaePersistenceUnit.persist(evaluation);
		
		EaeFichePoste f = new EaeFichePoste();
		f.setEae(eaeEnCours);
		f.setFonction("fonction");
		f.setGradePoste("grade poste");
		f.setEmploi("emploi");
		f.setDirectionService("directionService");
		f.setService("service");
		f.setSectionService("sectionService");
		f.setLocalisation("localisation");
		f.setMissions("missions");

		eaePersistenceUnit.persist(f);
		
		// EAE En cours BIS
		EaeEvalue eaeEvalueBis = new EaeEvalue();
		eaeEvalueBis.setIdAgent(9005148);
		
		EaeEvaluation evaluationBis = new EaeEvaluation();
		evaluationBis.setPropositionAvancement(EaeAvancementEnum.MOY);
		evaluationBis.setAvisChangementClasse(false);
		evaluationBis.setAvisRevalorisation(false);
		
		Eae eaeEnCoursBis = new Eae();
		eaeEnCoursBis.setCap(false);
		eaeEnCoursBis.setEaeEvalue(eaeEvalueBis);
		eaeEnCoursBis.setEaeCampagne(eaeCampagne);
		eaeEnCoursBis.setEtat(EaeEtatEnum.EC);
		eaeEnCoursBis.setEaeEvaluation(evaluationBis);
		eaePersistenceUnit.persist(eaeEnCoursBis);

		eaeEvalueBis.setEae(eaeEnCoursBis);
		eaePersistenceUnit.persist(eaeEvalueBis);

		evaluationBis.setEae(eaeEnCoursBis);
		eaePersistenceUnit.persist(evaluationBis);
		
		EaeFichePoste fBis = new EaeFichePoste();
		fBis.setEae(eaeEnCoursBis);
		fBis.setFonction("fonction");
		fBis.setGradePoste("grade poste");
		fBis.setEmploi("emploi");
		fBis.setDirectionService("directionService");
		fBis.setService("service");
		fBis.setSectionService("sectionService 2");
		fBis.setLocalisation("localisation");
		fBis.setMissions("missions");

		eaePersistenceUnit.persist(fBis);
		
		/////// 2e fiche de poste ///////
		EaeEvalue eaeEvalue2 = new EaeEvalue();
		eaeEvalue2.setIdAgent(9005142);
		
		EaeEvaluation evaluation2 = new EaeEvaluation();
		evaluation2.setPropositionAvancement(EaeAvancementEnum.MAXI);
		evaluation2.setAvisChangementClasse(true);
		evaluation2.setAvisRevalorisation(false);
		
		Eae eaeNonDebute = new Eae();
		eaeNonDebute.setCap(true);
		eaeNonDebute.setEaeEvalue(eaeEvalue2);
		eaeNonDebute.setEaeCampagne(eaeCampagne);
		eaeNonDebute.setEtat(EaeEtatEnum.ND);
		eaeNonDebute.setEaeEvaluation(evaluation2);
		eaePersistenceUnit.persist(eaeNonDebute);

		eaeEvalue2.setEae(eaeNonDebute);
		eaePersistenceUnit.persist(eaeEvalue2);

		evaluation2.setEae(eaeNonDebute);
		eaePersistenceUnit.persist(evaluation2);
		
		EaeFichePoste f2 = new EaeFichePoste();
		f2.setEae(eaeNonDebute);
		f2.setFonction("fonction");
		f2.setGradePoste("grade poste");
		f2.setEmploi("emploi");
		f2.setDirectionService("directionService");
		f2.setService("service");
		f2.setSectionService("sectionService 2");
		f2.setLocalisation("localisation");
		f2.setMissions("missions");

		eaePersistenceUnit.persist(f2);
		
		/////// 3e fiche de poste ///////
		EaeEvalue eaeEvalue3 = new EaeEvalue();
		eaeEvalue3.setIdAgent(9005142);
		
		EaeEvaluation evaluation3 = new EaeEvaluation();
		evaluation3.setPropositionAvancement(EaeAvancementEnum.MAXI);
		evaluation3.setAvisChangementClasse(false);
		evaluation3.setAvisRevalorisation(true);
		
		Eae eaeFinalise = new Eae();
		eaeFinalise.setCap(true);
		eaeFinalise.setEaeEvalue(eaeEvalue3);
		eaeFinalise.setEaeCampagne(eaeCampagne);
		eaeFinalise.setEtat(EaeEtatEnum.F);
		eaeFinalise.setEaeEvaluation(evaluation3);
		eaePersistenceUnit.persist(eaeFinalise);

		eaeEvalue3.setEae(eaeFinalise);
		eaePersistenceUnit.persist(eaeEvalue3);

		evaluation3.setEae(eaeFinalise);
		eaePersistenceUnit.persist(evaluation3);
		
		EaeFichePoste f3 = new EaeFichePoste();
		f3.setEae(eaeFinalise);
		f3.setFonction("fonction");
		f3.setGradePoste("grade poste");
		f3.setEmploi("emploi");
		f3.setDirectionService("directionService 2");
		f3.setService("service");
		f3.setSectionService("sectionService 3");
		f3.setLocalisation("localisation");
		f3.setMissions("missions");

		eaePersistenceUnit.persist(f3);
		
		// test  1er fiche de poste
		assertEquals(1, repository.countAvisSHD(eaeCampagne.getIdCampagneEae(), "directionService", "sectionService",
				true, EaeAvancementEnum.MINI.name(), true).intValue());
		
		// test bis
		assertEquals(1, repository.countAvisSHD(eaeCampagne.getIdCampagneEae(), "directionService", "sectionService 2",
				false, EaeAvancementEnum.MOY.name(), false).intValue());
		
		// test 2e fiche de poste
		assertEquals(1, repository.countAvisSHD(eaeCampagne.getIdCampagneEae(), "directionService", "sectionService 2",
				false, EaeAvancementEnum.MAXI.name(), true).intValue());
		
		// test 2e fiche de poste
		assertEquals(1, repository.countAvisSHD(eaeCampagne.getIdCampagneEae(), "directionService 2", "sectionService 3",
				true, EaeAvancementEnum.MAXI.name(), false).intValue());
		
		// bad direction 
		assertEquals(0, repository.countAvisSHD(eaeCampagne.getIdCampagneEae(), "bad directionService 2", "sectionService 3",
				true, EaeAvancementEnum.MAXI.name(), false).intValue());
		
		// bad section
		assertEquals(0, repository.countAvisSHD(eaeCampagne.getIdCampagneEae(), "directionService 2", "bad sectionService 3",
				true, EaeAvancementEnum.MAXI.name(), false).intValue());
		
		// bad avisRevalorisation
		assertEquals(0, repository.countAvisSHD(eaeCampagne.getIdCampagneEae(), "directionService 2", "sectionService 3",
				false, EaeAvancementEnum.MAXI.name(), false).intValue());
		
		// bad duree avancement
		assertEquals(0, repository.countAvisSHD(eaeCampagne.getIdCampagneEae(), "directionService 2", "sectionService 3",
				true, EaeAvancementEnum.MINI.name(), false).intValue());

		// bad changement classe
		assertEquals(0, repository.countAvisSHD(eaeCampagne.getIdCampagneEae(), "directionService 2", "sectionService 3",
				true, EaeAvancementEnum.MAXI.name(), true).intValue());
	}
}
