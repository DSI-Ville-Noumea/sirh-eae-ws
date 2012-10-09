package nc.noumea.mairie.sirh.eae.service;

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
	
	@Override
	public List<Eae> listEaesByAgentId(int agentId) {

		//TODO: this will change into a WS call to SIRH-WS in order to retrieve the list of EAEs to display
		TypedQuery<Eae> eaeQuery = eaeEntityManager.createQuery("select e from Eae e where e.idAgent = :idAgent", Eae.class);
		eaeQuery.setParameter("idAgent", agentId);
		List<Eae> result = eaeQuery.getResultList();
		
		// For each EAE result, retrieve the Agent, SHD and Delegataire informations from the Agent (other persistenceUnit)
		// retrieve also the Evaluateurs of the current EAE
		for(Eae eae : result) {
			agentService.fillEaeWithAgents(eae);
		}
		
		return result;
	}

	@Override
	public void initializeEae(int idEae) throws EaeServiceException {
		
		Eae eaeToInitialize = Eae.findEae(idEae);
		
		if (eaeToInitialize == null) 
			throw new EaeServiceException(String.format("Impossible de créer l'EAE id '%d'", idEae));
		
		eaeToInitialize.setDateCreation(helper.getCurrentDate());
		eaeToInitialize.setEtat(EaeEtatEnum.C);
	}
	
}
