package nc.noumea.mairie.sirh.eae.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import nc.noumea.mairie.sirh.domain.Agent;
import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluateur;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluation;
import nc.noumea.mairie.sirh.eae.domain.EaePlanAction;
import nc.noumea.mairie.sirh.eae.domain.EaeResultat;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeEtatEnum;
import nc.noumea.mairie.sirh.eae.dto.EaeListItemDto;
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
	public List<EaeListItemDto> listEaesByAgentId(int agentId) {

		List<EaeListItemDto> result = new ArrayList<EaeListItemDto>();
		
		// Get the list of EAEs to return
		List<Integer> eaeIds = sirhWsConsumer.getListOfEaesForAgentId(agentId);
		
		if (eaeIds.isEmpty())
			return result;
		
		// Retrieve the EAEs
		TypedQuery<Eae> eaeQuery = eaeEntityManager.createQuery("select e from Eae e where e.idEae in (:eaeIds)", Eae.class);
		eaeQuery.setParameter("eaeIds", eaeIds);
		List<Eae> queryResult = eaeQuery.getResultList();
		
		// For each EAE result, retrieve extra information from SIRH
		for(Eae eae : queryResult) {
			agentService.fillEaeWithAgents(eae);
			EaeListItemDto dtoItem = new EaeListItemDto(eae);
			dtoItem.setAccessRightsForAgentId(eae, agentId);
			result.add(dtoItem);
		}
		
		return result;
	}

	@Override
	public void initializeEae(Eae eaeToInitialize, Eae previousEae) throws EaeServiceException {
		
		if (eaeToInitialize.getEtat() != EaeEtatEnum.ND)
			throw new EaeServiceException(String.format("Impossible d'initialiser l'EAE id '%d': le statut de cet Eae est '%s'.", eaeToInitialize.getIdEae(), eaeToInitialize.getEtat().toString()));
				
		eaeToInitialize.setDateCreation(helper.getCurrentDate());
		eaeToInitialize.setEtat(EaeEtatEnum.C);
		
		if (eaeToInitialize.getEaeEvaluation() == null) {
			EaeEvaluation eva = new EaeEvaluation();
			eva.setEae(eaeToInitialize);
			eaeToInitialize.setEaeEvaluation(eva);
		}
		// If no previous EAE, return
		if (previousEae == null)
			return;
		
		// Copy the previous notes to current EAE
		eaeToInitialize.getEaeEvaluation().setNoteAnneeN1(previousEae.getEaeEvaluation().getNoteAnnee());
		eaeToInitialize.getEaeEvaluation().setNoteAnneeN2(previousEae.getEaeEvaluation().getNoteAnneeN1());
		eaeToInitialize.getEaeEvaluation().setNoteAnneeN3(previousEae.getEaeEvaluation().getNoteAnneeN2());
		
		// Copy the Plan Actions items of N-1 EAE into EaeResultats of this year's EAE
		for(EaePlanAction pa : previousEae.getEaePlanActions()) {
			EaeResultat res = new EaeResultat();
			res.setObjectif(pa.getObjectif());
			res.setTypeObjectif(pa.getTypeObjectif());
			eaeToInitialize.getEaeResultats().add(res);
		}
	}

	@Override
	public void startEae(Eae eaeToStart) throws EaeServiceException {

		if (eaeToStart.getEtat() != EaeEtatEnum.C && eaeToStart.getEtat() != EaeEtatEnum.EC)
			throw new EaeServiceException(String.format("Impossible de démarrer l'EAE id '%d': le statut de cet Eae est '%s'.", eaeToStart.getIdEae(), eaeToStart.getEtat().toString()));
				
		if (eaeToStart.getEtat() != EaeEtatEnum.EC)
			eaeToStart.setEtat(EaeEtatEnum.EC);
	}

	@Override
	public void resetEaeEvaluateur(Eae eaeToReset) throws EaeServiceException {

		if (eaeToReset.getEtat() != EaeEtatEnum.C && eaeToReset.getEtat() != EaeEtatEnum.EC && eaeToReset.getEtat() != EaeEtatEnum.ND)
			throw new EaeServiceException(String.format("Impossible de réinitialiser l'EAE id '%d': le statut de cet Eae est '%s'.", eaeToReset.getIdEae(), eaeToReset.getEtat().toString()));
				
		if (eaeToReset.getEtat() != EaeEtatEnum.ND)
			eaeToReset.setEtat(EaeEtatEnum.ND);
		
		eaeToReset.getEaeEvaluateurs().clear();
		eaeToReset.flush();
		
		EaeEvaluateur evaluateur = new EaeEvaluateur();
		evaluateur.setEae(eaeToReset);
		evaluateur.setIdAgent(eaeToReset.getEaeFichePoste().getIdAgentShd());
		evaluateur.setFonction(eaeToReset.getEaeFichePoste().getFonctionResponsable());
		eaeToReset.getEaeEvaluateurs().add(evaluateur);
	}
	
	@Override
	public void setDelegataire(Eae eae, int idAgentDelegataire) throws EaeServiceException {
		
		Agent agentDelegataire = Agent.findAgent(idAgentDelegataire);
		
		if (agentDelegataire == null)
			throw new EaeServiceException(String.format("Impossible d'affecter l'agent '%d' en tant que délégataire: cet Agent n'existe pas.", idAgentDelegataire));
		
		eae.setIdAgentDelegataire(idAgentDelegataire);
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
	public List<Eae> findCurrentAndPreviousEaesByAgentId(int agentId) {
		
		List<Eae> result = findLatestEaesByAgentId(agentId, 2);
		
		return result;
	}
}
