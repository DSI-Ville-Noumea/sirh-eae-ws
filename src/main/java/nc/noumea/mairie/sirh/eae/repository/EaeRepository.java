package nc.noumea.mairie.sirh.eae.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeCampagne;
import nc.noumea.mairie.sirh.eae.domain.EaeCampagneTask;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeEtatEnum;
import nc.noumea.mairie.sirh.tools.IHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class EaeRepository implements IEaeRepository {

	@Autowired
	private IHelper helper;
	
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

	@Override
	public List<Eae> findEaesNonDebuteOuCreeOuEnCoursForEaeListByAgentIds(List<Integer> agentIds, Integer agentId) {

		// Query
		StringBuilder sb = new StringBuilder();
		sb.append("select DISTINCT(e) from Eae e ");
		sb.append("JOIN FETCH e.eaeEvalue ");
		sb.append("where e.etat in ( :ND, :C, :EC )");
		sb.append("AND (e.eaeEvalue.idAgent in :agentIds ");
		sb.append("OR e.idAgentDelegataire = :agentId ");
		sb.append("OR e.idEae in (select eva.eae.idEae from EaeEvaluateur eva where eva.idAgent = :agentId) ) ");
		sb.append("and e.eaeCampagne.dateOuvertureKiosque is not null and e.eaeCampagne.dateFermetureKiosque is null and  e.eaeCampagne.dateOuvertureKiosque < :date");

		TypedQuery<Eae> eaeQuery = eaeEntityManager.createQuery(sb.toString(), Eae.class);
		eaeQuery.setParameter("agentIds", agentIds.size() == 0 ? null : agentIds);
		eaeQuery.setParameter("agentId", agentId);
		eaeQuery.setParameter("date", helper.getCurrentDate());
		eaeQuery.setParameter("ND", EaeEtatEnum.ND);
		eaeQuery.setParameter("C", EaeEtatEnum.C);
		eaeQuery.setParameter("EC", EaeEtatEnum.EC);

		List<Eae> queryResult = eaeQuery.getResultList();

		// Because there might be several times the same EAE from the previous
		// query
		// (several Evaluateurs or several FichePostes) but the query is more
		// performant with doubles
		// followed with this loop than having to fetch the records after
		// (select N+1 issue)
		// we remove the extra ones with this loop
		List<Eae> disctinctEaes = new ArrayList<Eae>();
		for (Eae eae : queryResult)
			if (!disctinctEaes.contains(eae))
				disctinctEaes.add(eae);

		return disctinctEaes;
	}
}
