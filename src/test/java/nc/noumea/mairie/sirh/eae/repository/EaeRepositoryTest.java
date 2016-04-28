package nc.noumea.mairie.sirh.eae.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeCampagne;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluateur;
import nc.noumea.mairie.sirh.eae.domain.EaeEvalue;
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
}
