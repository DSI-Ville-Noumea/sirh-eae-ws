package nc.noumea.mairie.sirh.eae.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeEtatEnum;
import nc.noumea.mairie.sirh.service.IAgentService;
import nc.noumea.mairie.sirh.tools.IHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EaeService implements IEaeService {

	@PersistenceContext(unitName = "eaePersistenceUnit")
	private EntityManager eaeEntityManager;
	
	@Autowired
	private IAgentService agentService;
	
	@Autowired
	private IHelper helper;
	
	@Autowired
	private ISirhWsConsumer sirhWsConsumer;
	
	/*
	 * Interface implementation
	 */
	
	@Override
	public List<Eae> listEaesByAgentId(int agentId) {

		List<Eae> result = new ArrayList<Eae>();
		
		// Get the list of EAEs to return
		List<Integer> eaeIds = sirhWsConsumer.getListOfEaesForAgentId(agentId);
		
		if (eaeIds.isEmpty())
			return result;
		
		// Retrieve the EAEs
		TypedQuery<Eae> eaeQuery = eaeEntityManager.createQuery("select e from Eae e where e.idEae in (:eaeIds)", Eae.class);
		eaeQuery.setParameter("eaeIds", eaeIds);
		result = eaeQuery.getResultList();
		
		// For each EAE result, retrieve extra information from SIRH
		for(Eae eae : result) {
			agentService.fillEaeWithAgents(eae);
		}
		
		return result;
	}

	@Override
	public void initializeEae(Eae eaeToInitialize, List<Eae> previousEaes) throws EaeServiceException {
		
		if (eaeToInitialize.getEtat() != EaeEtatEnum.ND)
			throw new EaeServiceException(String.format("Impossible de créer l'EAE id '%d': le statut de cet Eae est '%s'.", eaeToInitialize.getIdEae(), eaeToInitialize.getEtat().toString()));
				
		eaeToInitialize.setDateCreation(helper.getCurrentDate());
		eaeToInitialize.setEtat(EaeEtatEnum.C);
	}

	@Override
	public void startEae(Eae eaeToStart) throws EaeServiceException {

		if (eaeToStart.getEtat() != EaeEtatEnum.C)
			throw new EaeServiceException(String.format("Impossible de créer l'EAE id '%d': le statut de cet Eae est '%s'.", eaeToStart.getIdEae(), eaeToStart.getEtat().toString()));
				
		eaeToStart.setEtat(EaeEtatEnum.EC);
	}

	
	/*
	 * Finders methods
	 */
	
	private List<Eae> findLatestEaesByAgentId(int agentId, int maxResults) {
		
		TypedQuery<Eae> eaeQuery = eaeEntityManager.createQuery("select e from Eae e where e.idAgent = :idAgent order by e.dateCreation desc", Eae.class);
		eaeQuery.setParameter("idAgent", agentId);
		eaeQuery.setMaxResults(maxResults);
		List<Eae> result = eaeQuery.getResultList();
		
		return result;
	}

	@Override
	public Eae findLastEaeByAgentId(int agentId) {
		
		List<Eae> result = findLatestEaesByAgentId(agentId, 1);
		
		if (result.isEmpty())
			return null;
		else
			return result.get(0);
	}
	
	@Override
	public List<Eae> findFourPreviousEaesByAgentId(int agentId) {
		
		List<Eae> result = findLatestEaesByAgentId(agentId, 4);
		
		return result;
	}
	
}
