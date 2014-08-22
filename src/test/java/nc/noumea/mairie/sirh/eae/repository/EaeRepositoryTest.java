package nc.noumea.mairie.sirh.eae.repository;

import static org.junit.Assert.*;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeCampagne;
import nc.noumea.mairie.sirh.eae.domain.EaeEvalue;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeEtatEnum;

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
	
	
	//@Test
	//@Transactional("eaeTransactionManager")
//	public void findEaeAgent_1result() {
//		
//		EaeCampagne eaeCampagne = new EaeCampagne();
//			eaeCampagne.setAnnee(2010);
//			eaeCampagne.setDateDebut(new Date());
//		eaePersistenceUnit.persist(eaeCampagne);
//	
//		EaeEvalue eaeEvalue = new EaeEvalue();
//			eaeEvalue.setIdAgent(9005138);
//		
//		Eae eae = new Eae();
//			eae.setCap(true);
//			eae.setEtat(EaeEtatEnum.C);
//			eae.setEaeEvalue(eaeEvalue);
//			eae.setEaeCampagne(eaeCampagne);
//		eaePersistenceUnit.persist(eae);
//		
//		eaeEvalue.setEae(eae);
//		eaePersistenceUnit.persist(eaeEvalue);
//		
//		Eae result = repository.findEaeAgent(9005138, eaeCampagne.getIdCampagneEae());
//		
//		assertNotNull(result);
//		assertEquals(EaeEtatEnum.C, result.getEtat());
//		assertTrue(result.isCap());
//	}
	
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
		
		Eae result = repository.findEaeAgent(9005138, eaeCampagne.getIdCampagneEae()+1);
		
		assertNull(result);
	}
}
