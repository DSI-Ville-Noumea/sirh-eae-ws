package nc.noumea.mairie.sirh.eae.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeCampagne;
import nc.noumea.mairie.sirh.eae.domain.EaeCampagneTask;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class EaeRepository implements IEaeRepository {

	@PersistenceContext(unitName = "eaePersistenceUnit")
    private EntityManager eaeEntityManager;
	
	@Override
	public EaeCampagne findEaeCampagne(Integer idEaeCampagne) {
		return eaeEntityManager.find(EaeCampagne.class, idEaeCampagne);
	}
	
	@Override
	public void persistEntity(Object entity) {
		eaeEntityManager.persist(entity);
	}
	
	@Override
	public void removeEntity(Object entity) {
		eaeEntityManager.remove(entity);
	}

	@Override
	public EaeCampagne findEaeCampagneByAnnee(Integer annee) {
		
		EaeCampagne result = null;
		
		TypedQuery<EaeCampagne> eaeQuery = eaeEntityManager.createQuery("select e from EaeCampagne e where annee = :annee ", EaeCampagne.class);
		eaeQuery.setParameter("annee", annee);

		if(0 < eaeQuery.getResultList().size())
			result = eaeQuery.getSingleResult();
		
		return result;
	}
	
	@Override
	public Eae findEaeAgent(Integer idAgent, Integer idEaeCampagne) {
		
		Eae result = null;
		
		TypedQuery<Eae> eaeQuery = eaeEntityManager.createQuery(
				"select e from Eae e inner join e.eaeEvalue ev where ev.idAgent = :idAgent and e.eaeCampagne.idCampagneEae = :idEaeCampagne order by e.idEae desc ",
				Eae.class);
		eaeQuery.setParameter("idAgent", idAgent);
		eaeQuery.setParameter("idEaeCampagne", idEaeCampagne);
		
		if(0 < eaeQuery.getResultList().size())
			result = eaeQuery.getResultList().get(0);
		
		return result;
	}
	
	@Override
	@Transactional(value = "eaeTransactionManager")
	public EaeCampagneTask findEaeCampagneTask(Integer idEaeCampagneTask) {
		
		EaeCampagneTask result = null;
		
		TypedQuery<EaeCampagneTask> eaeQuery = eaeEntityManager.createNamedQuery("EaeCampagneTask.getEaeCampagneTask", EaeCampagneTask.class);
			eaeQuery.setParameter("idEaeCampagneTask", idEaeCampagneTask);
		
		if(0 < eaeQuery.getResultList().size())
			result = eaeQuery.getSingleResult();
		
		return result;
	}
}
