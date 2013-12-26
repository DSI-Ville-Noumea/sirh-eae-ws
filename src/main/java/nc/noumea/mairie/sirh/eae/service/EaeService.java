package nc.noumea.mairie.sirh.eae.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import nc.noumea.mairie.sirh.domain.Agent;
import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluateur;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluation;
import nc.noumea.mairie.sirh.eae.domain.EaeFichePoste;
import nc.noumea.mairie.sirh.eae.domain.EaeFinalisation;
import nc.noumea.mairie.sirh.eae.domain.EaePlanAction;
import nc.noumea.mairie.sirh.eae.domain.EaeResultat;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeEtatEnum;
import nc.noumea.mairie.sirh.eae.dto.CanFinalizeEaeDto;
import nc.noumea.mairie.sirh.eae.dto.EaeDashboardItemDto;
import nc.noumea.mairie.sirh.eae.dto.EaeEvalueNameDto;
import nc.noumea.mairie.sirh.eae.dto.EaeFinalizationDto;
import nc.noumea.mairie.sirh.eae.dto.EaeListItemDto;
import nc.noumea.mairie.sirh.eae.dto.FinalizationInformationDto;
import nc.noumea.mairie.sirh.service.IAgentService;
import nc.noumea.mairie.sirh.tools.IHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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

	@Autowired
	private IAgentMatriculeConverterService agentMatriculeConverterService;

	@Autowired
	private MessageSource messageSource;

	/*
	 * Interface implementation
	 */

	@Override
	public List<EaeListItemDto> listEaesByAgentId(int agentId)
			throws SirhWSConsumerException {

		List<EaeListItemDto> result = new ArrayList<EaeListItemDto>();

		// Get the list of agents whose responsible is the given agent
		List<Integer> agentIds = sirhWsConsumer
				.getListOfSubAgentsForAgentId(agentId);

		// Retrieve the EAEs
		List<Eae> queryResult = findEaesForEaeListByAgentIds(agentIds, agentId);

		// For each EAE result, retrieve extra information from SIRH
		for (Eae eae : queryResult) {
			agentService.fillEaeWithAgents(eae);
			EaeListItemDto dtoItem = new EaeListItemDto(eae);
			dtoItem.setAccessRightsForAgentId(eae, agentId);
			result.add(dtoItem);
		}

		return result;
	}

	@Override
	public void initializeEae(Eae eaeToInitialize, Eae previousEae)
			throws EaeServiceException {

		if (eaeToInitialize.getEtat() != EaeEtatEnum.ND)
			throw new EaeServiceException(
					String.format(
							"Impossible d'initialiser l'EAE id '%d': le statut de cet Eae est '%s'.",
							eaeToInitialize.getIdEae(), eaeToInitialize
									.getEtat().toString()));

		eaeToInitialize.setDateCreation(helper.getCurrentDate());
		eaeToInitialize.setEtat(EaeEtatEnum.C);

		if (eaeToInitialize.getEaeEvaluation() == null) {
			EaeEvaluation eva = new EaeEvaluation();
			eva.setEae(eaeToInitialize);
			eaeToInitialize.setEaeEvaluation(eva);
		}

		// If no previous EAE, return
		if (previousEae == null || previousEae.getEaeEvaluation() == null)
			return;

		// Copy the previous notes to current EAE
		eaeToInitialize.getEaeEvaluation().setNoteAnneeN1(
				previousEae.getEaeEvaluation().getNoteAnnee());
		eaeToInitialize.getEaeEvaluation().setNoteAnneeN2(
				previousEae.getEaeEvaluation().getNoteAnneeN1());
		eaeToInitialize.getEaeEvaluation().setNoteAnneeN3(
				previousEae.getEaeEvaluation().getNoteAnneeN2());

		// If this eae already has some eaeResultats, do nothing
		if (eaeToInitialize.getEaeResultats().size() != 0)
			return;

		// otherwise, copy the Plan Actions items of N-1 EAE into EaeResultats
		// of this year's EAE
		for (EaePlanAction pa : previousEae.getEaePlanActions()) {
			EaeResultat res = new EaeResultat();
			res.setEae(eaeToInitialize);
			res.setObjectif(pa.getObjectif());
			res.setTypeObjectif(pa.getTypeObjectif());
			eaeToInitialize.getEaeResultats().add(res);
		}
	}

	@Override
	public void startEae(Eae eaeToStart) throws EaeServiceException {

		if (eaeToStart.getEtat() != EaeEtatEnum.C
				&& eaeToStart.getEtat() != EaeEtatEnum.EC)
			throw new EaeServiceException(
					String.format(
							"Impossible de démarrer l'EAE id '%d': le statut de cet Eae est '%s'.",
							eaeToStart.getIdEae(), eaeToStart.getEtat()
									.toString()));

		if (eaeToStart.getEtat() != EaeEtatEnum.EC)
			eaeToStart.setEtat(EaeEtatEnum.EC);
	}

	@Override
	public void resetEaeEvaluateur(Eae eaeToReset) throws EaeServiceException {

		if (eaeToReset.getEtat() != EaeEtatEnum.C
				&& eaeToReset.getEtat() != EaeEtatEnum.EC
				&& eaeToReset.getEtat() != EaeEtatEnum.ND)
			throw new EaeServiceException(
					String.format(
							"Impossible de réinitialiser l'EAE id '%d': le statut de cet Eae est '%s'.",
							eaeToReset.getIdEae(), eaeToReset.getEtat()
									.toString()));

		if (eaeToReset.getEtat() != EaeEtatEnum.ND)
			eaeToReset.setEtat(EaeEtatEnum.ND);

		eaeToReset.getEaeEvaluateurs().clear();
		eaeToReset.flush();

		EaeFichePoste primaryFichePoste = eaeToReset.getPrimaryFichePoste();

		if (primaryFichePoste.getIdAgentShd() == null)
			return;

		EaeEvaluateur evaluateur = new EaeEvaluateur();
		evaluateur.setEae(eaeToReset);
		evaluateur.setIdAgent(primaryFichePoste.getIdAgentShd());
		evaluateur.setFonction(primaryFichePoste.getFonctionResponsable());
		eaeToReset.getEaeEvaluateurs().add(evaluateur);
	}

	@Override
	public void setDelegataire(Eae eae, int idAgentDelegataire)
			throws EaeServiceException {

		Agent agentDelegataire = eaeEntityManager.find(Agent.class, idAgentDelegataire);		

		if (agentDelegataire == null)
			throw new EaeServiceException(
					String.format(
							"Impossible d'affecter l'agent '%d' en tant que délégataire: cet Agent n'existe pas.",
							idAgentDelegataire));

		eae.setIdAgentDelegataire(idAgentDelegataire);
	}

	@Override
	public List<EaeDashboardItemDto> getEaesDashboard(int idAgent)
			throws SirhWSConsumerException {

		List<EaeDashboardItemDto> result = new ArrayList<EaeDashboardItemDto>();

		// Get the list of EAEs to return
		List<Integer> agentIds = sirhWsConsumer
				.getListOfSubAgentsForAgentId(idAgent);

		// Retrieve the EAEs
		List<Eae> queryResult = findEaesForDashboardByAgentIds(agentIds,
				idAgent);

		Map<Integer, List<Eae>> groupedResult = new HashMap<Integer, List<Eae>>();
		List<Eae> eaesWithoutEvaluateurs = new ArrayList<Eae>();

		for (Eae e : queryResult) {

			if (e.getEaeEvaluateurs().isEmpty())
				eaesWithoutEvaluateurs.add(e);

			// If the agent requesting is evaluator, do not add other evaluators
			// to the list
			if (e.isEvaluateurOrDelegataire(idAgent)) {
				if (!groupedResult.containsKey(idAgent))
					groupedResult.put(idAgent, new ArrayList<Eae>());
				groupedResult.get(idAgent).add(e);
				continue;
			}

			// If the agent requesting is not evaluator, add all the evaluators
			// to the list (even if other team...)
			for (EaeEvaluateur eval : e.getEaeEvaluateurs()) {
				if (!groupedResult.containsKey(eval.getIdAgent()))
					groupedResult.put(eval.getIdAgent(), new ArrayList<Eae>());
				groupedResult.get(eval.getIdAgent()).add(e);
			}
		}

		for (Entry<Integer, List<Eae>> evaluateurAndEaes : groupedResult
				.entrySet()) {
			EaeDashboardItemDto item = new EaeDashboardItemDto(
					evaluateurAndEaes.getValue());
			Agent agent = agentService.getAgent(evaluateurAndEaes.getKey());
			item.setNom(agent.getDisplayNom());
			item.setPrenom(agent.getDisplayPrenom());

			result.add(item);
		}

		if (!eaesWithoutEvaluateurs.isEmpty()) {
			EaeDashboardItemDto itemWithoutEvaluateur = new EaeDashboardItemDto(
					eaesWithoutEvaluateurs);
			itemWithoutEvaluateur.setNom("?");
			itemWithoutEvaluateur.setPrenom("?");
			result.add(itemWithoutEvaluateur);
		}

		return result;
	}

	@Override
	public CanFinalizeEaeDto canFinalizEae(Eae eae) {

		if (eae == null)
			return null;

		CanFinalizeEaeDto dto = new CanFinalizeEaeDto();

		if (eae.getEtat() != EaeEtatEnum.EC)
			dto.setMessage(messageSource.getMessage("EAE_CANNOT_FINALIZE",
					new Object[] { eae.getEtat() }, null));
		else
			dto.setCanFinalize(true);

		return dto;
	}

	@Override
	public FinalizationInformationDto getFinalizationInformation(Eae eae)
			throws SirhWSConsumerException {

		if (eae == null)
			return null;

		agentService.fillEaeWithAgents(eae);
		FinalizationInformationDto result = new FinalizationInformationDto(eae);

		List<Integer> agentsShdIds = sirhWsConsumer
				.getListOfShdAgentsForAgentId(eae.getEaeEvalue().getIdAgent());

		for (Integer shdId : agentsShdIds) {
			result.getAgentsShd().add(agentService.getAgent(shdId));
		}

		return result;
	}

	@Override
	public void finalizEae(Eae eae, int idAgent, EaeFinalizationDto dto)
			throws EaeServiceException {

		if (eae == null)
			return;

		if (eae.getEtat() != EaeEtatEnum.EC)
			if (eae.getEtat() != EaeEtatEnum.CO)
				throw new EaeServiceException(messageSource.getMessage(
						"EAE_CANNOT_FINALIZE", new Object[] { eae.getEtat() },
						null));

		Date finalisationDate = helper.getCurrentDate();

		if (eae.getEtat() != EaeEtatEnum.CO) {
			eae.setEtat(EaeEtatEnum.F);
		}
		eae.setDateFinalisation(finalisationDate);
		eae.setDocAttache(true);

		EaeFinalisation finalisation = new EaeFinalisation();
		finalisation.setEae(eae);
		eae.getEaeFinalisations().add(finalisation);
		finalisation.setIdAgent(agentMatriculeConverterService
				.tryConvertFromADIdAgentToEAEIdAgent(idAgent));
		finalisation.setDateFinalisation(finalisationDate);
		finalisation.setIdGedDocument(dto.getIdDocument());
		finalisation.setVersionGedDocument(dto.getVersionDocument());
		finalisation.setCommentaire(dto.getCommentaire());

		EaeEvaluation eaeEvaluation = eae.getEaeEvaluation();
		eaeEvaluation.setNoteAnnee(dto.getNoteAnnee());

	}

	/*
	 * Finders methods
	 */

	private List<Eae> findLatestEaesByAgentId(int agentId, int maxResults) {

		TypedQuery<Eae> eaeQuery = eaeEntityManager
				.createQuery(
						"select e from Eae e where e.eaeEvalue.idAgent = :idAgent order by e.dateCreation desc",
						Eae.class);
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

	@Override
	public List<Eae> findEaesForDashboardByAgentIds(List<Integer> agentIds,
			Integer agentId) {
		// Query
		StringBuilder sb = new StringBuilder();
		sb.append("select e from Eae e ");
		sb.append("JOIN FETCH e.eaeEvalue LEFT JOIN FETCH e.eaeEvaluateurs LEFT JOIN FETCH e.eaeEvaluation LEFT JOIN FETCH e.eaeAutoEvaluation ");
		sb.append("where (e.eaeEvalue.idAgent in :agentIds ");
		sb.append("OR e.idAgentDelegataire = :agentId ");
		sb.append("OR e.idEae in (select eva.eae.idEae from EaeEvaluateur eva where eva.idAgent = :agentId) ) ");
		sb.append("and e.eaeCampagne.dateOuvertureKiosque is not null and e.eaeCampagne.dateFermetureKiosque is null and  e.eaeCampagne.dateOuvertureKiosque < :date");

		TypedQuery<Eae> eaeQuery = eaeEntityManager.createQuery(sb.toString(),
				Eae.class);
		eaeQuery.setParameter("agentIds", agentIds.size() == 0 ? null
				: agentIds);
		eaeQuery.setParameter("agentId", agentId);
		eaeQuery.setParameter("date", helper.getCurrentDate());

		List<Eae> queryResult = eaeQuery.getResultList();
		return queryResult;
	}

	@Override
	public List<Eae> findEaesForEaeListByAgentIds(List<Integer> agentIds,
			Integer agentId) {

		// Query
		StringBuilder sb = new StringBuilder();
		sb.append("select e from Eae e ");
		sb.append("LEFT JOIN FETCH e.eaeFichePostes LEFT JOIN FETCH e.eaeEvaluateurs JOIN FETCH e.eaeEvalue LEFT JOIN FETCH e.eaeEvaluation LEFT JOIN FETCH e.eaeAutoEvaluation LEFT JOIN FETCH e.eaeEvolution ");
		sb.append("where (e.eaeEvalue.idAgent in :agentIds ");
		sb.append("OR e.idAgentDelegataire = :agentId ");
		sb.append("OR e.idEae in (select eva.eae.idEae from EaeEvaluateur eva where eva.idAgent = :agentId) ) ");
		sb.append("and e.eaeCampagne.dateOuvertureKiosque is not null and e.eaeCampagne.dateFermetureKiosque is null and  e.eaeCampagne.dateOuvertureKiosque < :date");

		TypedQuery<Eae> eaeQuery = eaeEntityManager.createQuery(sb.toString(),
				Eae.class);
		eaeQuery.setParameter("agentIds", agentIds.size() == 0 ? null
				: agentIds);
		eaeQuery.setParameter("agentId", agentId);
		eaeQuery.setParameter("date", helper.getCurrentDate());

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

	@Override
	public Eae getEae(int idEae) {
		return Eae.findEae(idEae);
	}

	@Override
	public EaeEvalueNameDto getEvalueName(Eae eae) {

		agentService.fillEaeEvalueWithAgent(eae.getEaeEvalue());

		EaeEvalueNameDto dto = new EaeEvalueNameDto();
		dto.setPrenom(eae.getEaeEvalue().getAgent().getDisplayPrenom());
		dto.setNom(eae.getEaeEvalue().getAgent().getDisplayNom());

		return dto;
	}

	@Override
	public Eae findEaeByAgentAndYear(int idAgent, String annee) {

		// Query
		StringBuilder sb = new StringBuilder();
		sb.append("select e.* from eae e ");
		sb.append("inner join eae_campagne_eae c on e.id_campagne_eae=c.id_campagne_eae ");
		sb.append("inner join eae_evalue ev on e.id_eae=ev.id_eae ");
		sb.append("where c.annee=:annee and ev.id_agent=:idAgent");

		Query q = eaeEntityManager.createNativeQuery(sb.toString(), Eae.class);
		q.setParameter("idAgent", idAgent);
		q.setParameter("annee", annee);

		@SuppressWarnings("unchecked")
		List<Eae> result = q.getResultList();

		if (result.isEmpty())
			return null;
		else
			return result.get(0);

	}
}
