package nc.noumea.mairie.sirh.eae.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nc.noumea.mairie.alfresco.cmis.IAlfrescoCMISService;
import nc.noumea.mairie.sirh.domain.Agent;
import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeCampagne;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluateur;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluation;
import nc.noumea.mairie.sirh.eae.domain.EaeEvalue;
import nc.noumea.mairie.sirh.eae.domain.EaeFichePoste;
import nc.noumea.mairie.sirh.eae.domain.EaeFinalisation;
import nc.noumea.mairie.sirh.eae.domain.EaePlanAction;
import nc.noumea.mairie.sirh.eae.domain.EaeResultat;
import nc.noumea.mairie.sirh.eae.domain.EaeTypeDeveloppement;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeEtatEnum;
import nc.noumea.mairie.sirh.eae.dto.CampagneEaeDto;
import nc.noumea.mairie.sirh.eae.dto.CanFinalizeEaeDto;
import nc.noumea.mairie.sirh.eae.dto.EaeDashboardItemDto;
import nc.noumea.mairie.sirh.eae.dto.EaeDto;
import nc.noumea.mairie.sirh.eae.dto.EaeEvalueNameDto;
import nc.noumea.mairie.sirh.eae.dto.EaeFinalizationDto;
import nc.noumea.mairie.sirh.eae.dto.EaeListItemDto;
import nc.noumea.mairie.sirh.eae.dto.FinalizationInformationDto;
import nc.noumea.mairie.sirh.eae.dto.FormRehercheGestionEae;
import nc.noumea.mairie.sirh.eae.dto.ReturnMessageDto;
import nc.noumea.mairie.sirh.eae.dto.agent.AgentDto;
import nc.noumea.mairie.sirh.eae.dto.identification.ValeurListeDto;
import nc.noumea.mairie.sirh.eae.repository.IEaeRepository;
import nc.noumea.mairie.sirh.eae.web.controller.NoContentException;
import nc.noumea.mairie.sirh.service.IAgentService;
import nc.noumea.mairie.sirh.tools.IHelper;
import nc.noumea.mairie.sirh.ws.ISirhWsConsumer;
import nc.noumea.mairie.sirh.ws.SirhWSConsumerException;

@Service
public class EaeService implements IEaeService {

	private Logger							logger	= LoggerFactory.getLogger(EaeService.class);

	@PersistenceContext(unitName = "eaePersistenceUnit")
	private EntityManager					eaeEntityManager;

	@Autowired
	private IAgentService					agentService;

	@Autowired
	private IHelper							helper;

	@Autowired
	private ISirhWsConsumer					sirhWsConsumer;

	@Autowired
	private IAgentMatriculeConverterService	agentMatriculeConverterService;

	@Autowired
	private MessageSource					messageSource;

	@Autowired
	private IEaeRepository					eaeRepository;

	@Autowired
	private IAlfrescoCMISService			alfrescoCMISService;

	/*
	 * Interface implementation
	 */

	@Override
	@Transactional(readOnly = true)
	public List<EaeListItemDto> listEaesByAgentId(int agentId) throws SirhWSConsumerException {

		List<EaeListItemDto> result = new ArrayList<EaeListItemDto>();

		// Get the list of agents whose responsible is the given agent
		List<Integer> agentIds = sirhWsConsumer.getListOfSubAgentsForAgentId(agentId);

		// Retrieve the EAEs
		List<Eae> queryResult = findEaesForEaeListByAgentIds(agentIds, agentId);

		// For each EAE result, retrieve extra information from SIRH
		for (Eae eae : queryResult) {
			logger.debug("Filling info for EAE id [{}]", eae.getIdEae());
			agentService.fillEaeWithAgents(eae);
			EaeListItemDto dtoItem = new EaeListItemDto(eae);
			dtoItem.setAccessRightsForAgentId(eae, agentId);
			result.add(dtoItem);
		}

		return result;
	}

	@Override
	@Transactional(readOnly = true)
	public Integer countListEaesByAgentId(int agentId) throws SirhWSConsumerException {

		// Get the list of agents whose responsible is the given agent
		List<Integer> agentIds = sirhWsConsumer.getListOfSubAgentsForAgentId(agentId);

		// Retrieve the EAEs
		List<Eae> result = eaeRepository.findEaesNonDebuteOuCreeOuEnCoursForEaeListByAgentIds(agentIds, agentId);

		return null == result ? 0 : result.size();
	}

	@Override
	@Transactional(value = "eaeTransactionManager")
	public void initializeEae(Eae eaeToInitialize, Eae previousEae) throws EaeServiceException {

		eaeToInitialize = findEae(eaeToInitialize.getIdEae());
		if (null != previousEae) {
			previousEae = findEae(previousEae.getIdEae());
		}

		if (eaeToInitialize.getEtat() != EaeEtatEnum.ND)
			throw new EaeServiceException(String.format("Impossible d'initialiser l'EAE id '%d': le statut de cet Eae est '%s'.",
					eaeToInitialize.getIdEae(), eaeToInitialize.getEtat().toString()));

		// #19139 : ne pas modifier la date de creation initiale
		if (eaeToInitialize.getDateCreation() == null) {
			eaeToInitialize.setDateCreation(helper.getCurrentDate());
		}
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
		eaeToInitialize.getEaeEvaluation().setNoteAnneeN1(previousEae.getEaeEvaluation().getNoteAnnee());
		eaeToInitialize.getEaeEvaluation().setNoteAnneeN2(previousEae.getEaeEvaluation().getNoteAnneeN1());
		eaeToInitialize.getEaeEvaluation().setNoteAnneeN3(previousEae.getEaeEvaluation().getNoteAnneeN2());

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
	public Eae startEae(Integer idEaeToStart, boolean isSirh) throws EaeServiceException {

		Eae eaeToStart = findEae(idEaeToStart);

		if (isSirh)
			return eaeToStart;

		if (eaeToStart.getEtat() != EaeEtatEnum.C && eaeToStart.getEtat() != EaeEtatEnum.EC)
			throw new EaeServiceException(String.format("Impossible de démarrer l'EAE id '%d': le statut de cet Eae est '%s'.", eaeToStart.getIdEae(),
					eaeToStart.getEtat().toString()));

		if (eaeToStart.getEtat() != EaeEtatEnum.EC)
			eaeToStart.setEtat(EaeEtatEnum.EC);

		return eaeToStart;
	}

	@Override
	public void resetEaeEvaluateur(Eae eaeToReset) throws EaeServiceException {

		if (eaeToReset.getEtat() != EaeEtatEnum.C && eaeToReset.getEtat() != EaeEtatEnum.EC && eaeToReset.getEtat() != EaeEtatEnum.ND)
			throw new EaeServiceException(String.format("Impossible de réinitialiser l'EAE id '%d': le statut de cet Eae est '%s'.",
					eaeToReset.getIdEae(), eaeToReset.getEtat().toString()));

		if (eaeToReset.getEtat() != EaeEtatEnum.ND)
			eaeToReset.setEtat(EaeEtatEnum.ND);

		eaeToReset.getEaeEvaluateurs().clear();

		flush();

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
	@Transactional(value = "eaeTransactionManager")
	public void setDelegataire(Integer idEae, int idAgentDelegataire) throws EaeServiceException {

		Eae eae = findEae(idEae);

		Agent agentDelegataire = agentService.getAgent(idAgentDelegataire);

		if (agentDelegataire == null)
			throw new EaeServiceException(
					String.format("Impossible d'affecter l'agent '%d' en tant que délégataire: cet Agent n'existe pas.", idAgentDelegataire));

		eae.setIdAgentDelegataire(idAgentDelegataire);
	}

	@Override
	@Transactional(readOnly = true)
	public List<EaeDashboardItemDto> getEaesDashboard(int idAgent) throws SirhWSConsumerException {

		List<EaeDashboardItemDto> result = new ArrayList<EaeDashboardItemDto>();

		// Get the list of EAEs to return
		List<Integer> agentIds = sirhWsConsumer.getListOfSubAgentsForAgentId(idAgent);

		// Retrieve the EAEs
		List<Eae> queryResult = findEaesForDashboardByAgentIds(agentIds, idAgent);

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

		for (Entry<Integer, List<Eae>> evaluateurAndEaes : groupedResult.entrySet()) {
			EaeDashboardItemDto item = new EaeDashboardItemDto(evaluateurAndEaes.getValue());
			Agent agent = agentService.getAgent(evaluateurAndEaes.getKey());
			item.setNom(agent.getDisplayNom());
			item.setPrenom(agent.getDisplayPrenom());

			result.add(item);
		}

		if (!eaesWithoutEvaluateurs.isEmpty()) {
			EaeDashboardItemDto itemWithoutEvaluateur = new EaeDashboardItemDto(eaesWithoutEvaluateurs);
			itemWithoutEvaluateur.setNom("?");
			itemWithoutEvaluateur.setPrenom("?");
			result.add(itemWithoutEvaluateur);
		}

		return result;
	}

	@Override
	@Transactional(value = "eaeTransactionManager")
	public CanFinalizeEaeDto canFinalizEae(Integer idEae) {

		Eae eae = findEae(idEae);

		if (eae == null)
			return null;

		CanFinalizeEaeDto dto = new CanFinalizeEaeDto();

		if (eae.getEtat() != EaeEtatEnum.EC)
			dto.setMessage(messageSource.getMessage("EAE_CANNOT_FINALIZE", new Object[] { eae.getEtat() }, null));
		else
			dto.setCanFinalize(true);

		return dto;
	}

	@Override
	@Transactional(readOnly = true)
	public FinalizationInformationDto getFinalizationInformation(Integer idEae) throws SirhWSConsumerException {

		Eae eae = findEae(idEae);

		if (eae == null)
			return null;

		agentService.fillEaeWithAgents(eae);
		FinalizationInformationDto result = new FinalizationInformationDto(eae);

		List<Integer> agentsShdIds = sirhWsConsumer.getListOfShdAgentsForAgentId(eae.getEaeEvalue().getIdAgent());

		for (Integer shdId : agentsShdIds) {
			result.getAgentsShd().add(new AgentDto(agentService.getAgent(shdId)));
		}

		return result;
	}

	@Override
	@Transactional(value = "eaeTransactionManager")
	public ReturnMessageDto finalizeEae(Integer idEae, int idAgent, EaeFinalizationDto dto) {

		// # ne fonctionne plus avec sharepoint, mais on garde le meme principe
		// #19138 : on change le mode de fonctionnement de ce WS afin que
		// Sharepoint affiche les messages ici du WS et qu'il arrete d'avoir les
		// messages en dur dans le code sharepoint
		ReturnMessageDto result = new ReturnMessageDto();

		Eae eae = findEae(idEae);

		if (eae == null) {
			result.getErrors().add("L'EAE n'a pu être trouvé, merci de contacter la DRH.");
			return result;
		}

		if (eae.getEtat() != EaeEtatEnum.EC) {
			if (eae.getEtat() != EaeEtatEnum.CO) {
				result.getErrors()
						.add(String.format("Impossible de finaliser l'Eae car son état n'est pas 'En Cours' mais '%s'.", eae.getEtat().toString()));
				return result;
			}
		}

		// on upload vers alfresco avant de modifier les objets en BDD
		// en cas d erreur
		EaeFinalisation finalisation = new EaeFinalisation();
		result = alfrescoCMISService.uploadDocument(idAgent, dto, finalisation, eae, result);

		if (null != result && null != result.getErrors() && !result.getErrors().isEmpty()) {
			return result;
		}

		// on sauvegarde la finalisation en BDD
		Date finalisationDate = helper.getCurrentDate();

		if (eae.getEtat() != EaeEtatEnum.CO) {
			eae.setEtat(EaeEtatEnum.F);
		}
		eae.setDateFinalisation(finalisationDate);
		eae.setDocAttache(true);

		finalisation.setEae(eae);
		eae.getEaeFinalisations().add(finalisation);
		finalisation.setIdAgent(agentMatriculeConverterService.tryConvertFromADIdAgentToEAEIdAgent(idAgent));
		finalisation.setDateFinalisation(finalisationDate);
		finalisation.setCommentaire(dto.getCommentaire());

		EaeEvaluation eaeEvaluation = eae.getEaeEvaluation();
		eaeEvaluation.setNoteAnnee(dto.getNoteAnnee());

		// si ok alors on renvoi un message d'info
		result.getInfos().add(messageSource.getMessage("EAE_FINALISE_OK", null, null));
		return result;
	}

	/*
	 * Finders methods
	 */

	private List<Eae> findLatestEaesByAgentId(int agentId, int maxResults) {

		TypedQuery<Eae> eaeQuery = eaeEntityManager.createQuery(
				"select e from Eae e where e.eaeEvalue.idAgent = :idAgent and e.etat != 'S' order by e.eaeCampagne.annee desc", Eae.class);
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
	@Transactional(readOnly = true)
	public List<Eae> findCurrentAndPreviousEaesByAgentId(int agentId) {

		List<Eae> result = findLatestEaesByAgentId(agentId, 2);

		return result;
	}

	@Override
	public List<Eae> findEaesForDashboardByAgentIds(List<Integer> agentIds, Integer agentId) {
		// Query
		StringBuilder sb = new StringBuilder();
		sb.append("select e from Eae e ");
		sb.append("JOIN FETCH e.eaeEvalue LEFT JOIN FETCH e.eaeEvaluateurs LEFT JOIN FETCH e.eaeEvaluation LEFT JOIN FETCH e.eaeAutoEvaluation ");
		sb.append("where (e.eaeEvalue.idAgent in :agentIds ");
		sb.append("OR e.idAgentDelegataire = :agentId ");
		sb.append("OR e.idEae in (select eva.eae.idEae from EaeEvaluateur eva where eva.idAgent = :agentId) ) ");
		sb.append(
				"and e.eaeCampagne.dateOuvertureKiosque is not null and e.eaeCampagne.dateFermetureKiosque is null and  e.eaeCampagne.dateOuvertureKiosque < :date");

		TypedQuery<Eae> eaeQuery = eaeEntityManager.createQuery(sb.toString(), Eae.class);
		eaeQuery.setParameter("agentIds", agentIds.size() == 0 ? null : agentIds);
		eaeQuery.setParameter("agentId", agentId);
		eaeQuery.setParameter("date", helper.getCurrentDate());

		List<Eae> queryResult = eaeQuery.getResultList();
		return queryResult;
	}

	@Override
	public List<Eae> findEaesForEaeListByAgentIds(List<Integer> agentIds, Integer agentId) {

		// Query
		StringBuilder sb = new StringBuilder();
		sb.append("select e from Eae e ");
		sb.append("LEFT JOIN FETCH e.eaeFichePostes " + "LEFT JOIN FETCH e.eaeEvaluateurs " + "JOIN FETCH e.eaeEvalue "
				+ "LEFT JOIN FETCH e.eaeEvaluation " + "LEFT JOIN FETCH e.eaeAutoEvaluation " + "LEFT JOIN FETCH e.eaeEvolution ");
		sb.append("where (e.eaeEvalue.idAgent in :agentIds ");
		sb.append("OR e.idAgentDelegataire = :agentId ");
		sb.append("OR e.idEae in (select eva.eae.idEae from EaeEvaluateur eva where eva.idAgent = :agentId) ) ");
		sb.append(
				"and e.eaeCampagne.dateOuvertureKiosque is not null and e.eaeCampagne.dateFermetureKiosque is null and  e.eaeCampagne.dateOuvertureKiosque < :date");

		TypedQuery<Eae> eaeQuery = eaeEntityManager.createQuery(sb.toString(), Eae.class);
		eaeQuery.setParameter("agentIds", agentIds.size() == 0 ? null : agentIds);
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
	@Transactional(value = "eaeTransactionManager", readOnly = true)
	public EaeEvalueNameDto getEvalueName(Integer idEae) {

		Eae eae = findEae(idEae);

		agentService.fillEaeEvalueWithAgent(eae.getEaeEvalue());

		EaeEvalueNameDto dto = new EaeEvalueNameDto();
		dto.setPrenom(eae.getEaeEvalue().getAgent().getDisplayPrenom());
		dto.setNom(eae.getEaeEvalue().getAgent().getDisplayNom());

		return dto;
	}

	@Override
	@Transactional(value = "eaeTransactionManager", readOnly = true)
	public Eae findEaeByAgentAndYear(int idAgent, Integer annee) {

		// Query
		StringBuilder sb = new StringBuilder();
		sb.append("select e.* from eae e ");
		sb.append("inner join eae_campagne_eae c on e.id_campagne_eae=c.id_campagne_eae ");
		sb.append("inner join eae_evalue ev on e.id_eae=ev.id_eae ");
		sb.append("where c.annee=:annee and ev.id_agent=:idAgent and e.etat != 'S' ");

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

	@Override
	public Eae findEae(int idEae) {
		return eaeEntityManager.find(Eae.class, idEae);
	}

	@Override
	public void flush() {
		eaeEntityManager.flush();
	}

	@Override
	public void remove(Object obj) {
		eaeEntityManager.remove(obj);
	}

	@Override
	public void clear() {
		eaeEntityManager.clear();
	}

	@Override
	@Transactional(value = "eaeTransactionManager", readOnly = true)
	public CampagneEaeDto getEaeCampagneOuverte() {
		CampagneEaeDto result = new CampagneEaeDto();
		EaeCampagne camp = null;

		Query query = eaeEntityManager.createQuery(
				"select camp from EaeCampagne camp where camp.dateOuvertureKiosque is not null and camp.dateFermetureKiosque is null and  camp.dateOuvertureKiosque<:dateJour",
				EaeCampagne.class);

		query.setParameter("dateJour", new Date());
		try {
			camp = (EaeCampagne) query.getSingleResult();
			result = new CampagneEaeDto(camp);
		} catch (Exception e) {
			// aucune campagne trouvée
		}

		return result;
	}

	@Override
	@Transactional(value = "eaeTransactionManager", readOnly = true)
	public String getAvisSHD(int idEae) {

		Eae eae = findEae(idEae);
		if (eae == null || eae.getEaeEvaluation() == null)
			return "";

		return eae.getEaeEvaluation().getAvisShd();
	}

	@Override
	@Transactional(value = "eaeTransactionManager", readOnly = true)
	public Integer compterlistIdEaeByCampagneAndAgent(int idCampagneEae, List<Integer> idAgents, int idAgent) {

		StringBuilder sb = new StringBuilder();
		sb.append("select count(e) from Eae e ");
		sb.append("JOIN e.eaeEvalue ");
		sb.append("where (e.eaeEvalue.idAgent in :idAgents ");
		sb.append("OR e.idAgentDelegataire = :idAgent ");
		sb.append("OR e.idEae in (select eva.eae.idEae from EaeEvaluateur eva where eva.idAgent = :idAgent) ) ");
		sb.append("and e.eaeCampagne.idCampagneEae = :idCampagne");

		TypedQuery<Long> eaeQuery = eaeEntityManager.createQuery(sb.toString(), Long.class);
		eaeQuery.setParameter("idAgents", idAgents.size() == 0 ? null : idAgents);
		eaeQuery.setParameter("idCampagne", idCampagneEae);
		eaeQuery.setParameter("idAgent", idAgent);

		Long nbRes = eaeQuery.getSingleResult();

		return nbRes.intValue();
	}

	@Override
	@Transactional(value = "eaeTransactionManager", readOnly = true)
	public List<String> getEaesGedIdsForAgents(List<Integer> agentIds, int annee) {

		StringBuilder sb = new StringBuilder();
		sb.append("SELECT DISTINCT fin.nodeRefAlfresco ");
		sb.append("FROM EaeFinalisation fin ");
		sb.append("INNER JOIN fin.eae AS e ");
		sb.append("INNER JOIN e.eaeCampagne AS c ");
		sb.append("INNER JOIN e.eaeEvalue AS ev ");
		sb.append("WHERE c.annee = :annee ");
		sb.append("AND ev.idAgent IN :agentIds");

		TypedQuery<String> qEaesIds = eaeEntityManager.createQuery(sb.toString(), String.class);
		qEaesIds.setParameter("annee", annee);
		qEaesIds.setParameter("agentIds", agentIds.size() == 0 ? null : agentIds);

		List<String> result = qEaesIds.getResultList();

		return result;
	}

	@Override
	@Transactional(value = "eaeTransactionManager", readOnly = true)
	public List<EaeDto> findEaesByIdAgentOnly(Integer agentId) {

		// Query
		List<EaeEvalue> listEvalue = eaeRepository.findEaeEvalueWithEaesByIdAgentOnly(agentId);

		List<EaeDto> listEaeDto = new ArrayList<EaeDto>();

		if (null != listEvalue) {
			for (EaeEvalue evalue : listEvalue) {

				for (EaeEvaluateur evaluateur : evalue.getEae().getEaeEvaluateurs()) {
					agentService.fillEaeEvaluateurWithAgent(evaluateur);
				}

				EaeDto eaeDto = new EaeDto(evalue.getEae(), true);
				listEaeDto.add(eaeDto);
			}
		}

		return listEaeDto;
	}

	@Override
	@Transactional(value = "eaeTransactionManager", readOnly = true)
	public EaeDto findEaeDto(Integer idEae) {

		// Query
		Eae eae = eaeRepository.findEae(idEae);

		EaeDto eaeDto = null;
		if (null != eae) {
			agentService.fillEaeWithAgents(eae);
			eaeDto = new EaeDto(eae, true);
		}

		return eaeDto;
	}

	@Override
	@Transactional(value = "eaeTransactionManager", readOnly = true)
	public Integer chercherEaeNumIncrement() {
		return eaeRepository.chercherEaeNumIncrement();
	}

	@Override
	@Transactional(value = "eaeTransactionManager", readOnly = true)
	public List<ValeurListeDto> getListeTypeDeveloppement() {

		List<EaeTypeDeveloppement> listTypeDvlpt = eaeRepository.getListeTypeDeveloppement();

		List<ValeurListeDto> result = new ArrayList<ValeurListeDto>();

		if (null != listTypeDvlpt) {
			for (EaeTypeDeveloppement type : listTypeDvlpt) {
				result.add(new ValeurListeDto(type.getIdEaeTypeDeveloppement().toString(), type.getLibelle()));
			}
		}

		return result;
	}

	@Override
	@Transactional(value = "eaeTransactionManager")
	public void setEae(EaeDto eaeDto) throws EaeServiceException {

		Eae eae = findEae(eaeDto.getIdEae());

		if (null != eaeDto.getIdAgentDelegataire()) {
			Agent agentDelegataire = agentService.getAgent(eaeDto.getIdAgentDelegataire());

			if (agentDelegataire == null)
				throw new EaeServiceException(String.format("Impossible d'affecter l'agent '%d' en tant que délégataire: cet Agent n'existe pas.",
						eaeDto.getIdAgentDelegataire()));
		}

		eae.setIdAgentDelegataire(eaeDto.getIdAgentDelegataire());
		eae.setEtat(EaeEtatEnum.valueOf(eaeDto.getEtat()));

		if (EaeEtatEnum.CO.equals(eae.getEtat())) {
			logger.debug(String.format("EAE de l annee %d Controle pour l agent %d ", eae.getEaeCampagne().getAnnee(), eae.getEaeEvalue()
					.getIdAgent()));
			String nodeRef = null;
			if (null != eae.getEaeFinalisations() && !eae.getEaeFinalisations().isEmpty()) {
				nodeRef = eae.getEaeFinalisations().iterator().next().getNodeRefAlfresco();
			}
			if (null != nodeRef) {
				alfrescoCMISService.setPermissionsEaeControle(nodeRef);
			}
		}
		if (EaeEtatEnum.F.equals(eae.getEtat())) {
			logger.debug(String.format("EAE de l annee %d Finalise pour l agent %d ", eae.getEaeCampagne().getAnnee(), eae.getEaeEvalue()
					.getIdAgent()));
			String nodeRef = null;
			if (null != eae.getEaeFinalisations() && !eae.getEaeFinalisations().isEmpty()) {
				nodeRef = eae.getEaeFinalisations().iterator().next().getNodeRefAlfresco();
			}
			if (null != nodeRef) {
				Agent agentEvalue = agentService.getAgent(eae.getEaeEvalue().getIdAgent());
				alfrescoCMISService.setPermissionsEaeNonControle(nodeRef, agentEvalue.getIdAgent(), agentEvalue
						.getDisplayNom(), agentEvalue.getDisplayPrenom());
			}
		}
	}

	@Override
	@Transactional(value = "eaeTransactionManager", readOnly = true)
	public List<EaeDto> getListeEaeDto(FormRehercheGestionEae form) throws EaeServiceException {

		if (null == form.getIdCampagneEae() || form.getIdCampagneEae() == 0) {
			throw new EaeServiceException("Le choix de la campagne EAE est obligatoire.");
		}

		List<EaeDto> listEaeDto = new ArrayList<EaeDto>();

		List<Eae> listEae = eaeRepository.getListeEae(form);

		if (null != listEae) {
			for (Eae eae : listEae) {
				agentService.fillEaeWithAgents(eae);
				EaeDto eaeDto = new EaeDto(eae, true);
				listEaeDto.add(eaeDto);
			}
		}

		return listEaeDto;
	}

	@Override
	public String getLastDocumentEaeFinalise(Integer idEae) {

		return eaeRepository.getLastDocumentEaeFinalise(idEae);
	}

	@Override
	@Transactional(value = "eaeTransactionManager", readOnly = true)
	public List<EaeFinalizationDto> listEeaControleByAgent(Integer idAgent) {

		StringBuilder sb = new StringBuilder();
		sb.append("SELECT fin ");
		sb.append("FROM EaeFinalisation fin ");
		sb.append("INNER JOIN fin.eae AS e ");
		sb.append("INNER JOIN e.eaeCampagne AS c ");
		sb.append("INNER JOIN e.eaeEvalue AS ev ");
		sb.append("WHERE ev.idAgent = :idAgent ");
		sb.append("and fin.idEaeFinalisation in(select max(fin2.idEaeFinalisation) from EaeFinalisation fin2 inner join fin2.eae e2 group by fin2.eae ) ");
		sb.append("and e.etat = :etat ");
		sb.append("order by  c.annee desc ");

		TypedQuery<EaeFinalisation> qEaesIds = eaeEntityManager.createQuery(sb.toString(), EaeFinalisation.class);
		qEaesIds.setParameter("idAgent", idAgent);
		qEaesIds.setParameter("etat", EaeEtatEnum.CO);

		List<EaeFinalisation> resultReq = qEaesIds.getResultList();
		List<EaeFinalizationDto> result = new ArrayList<>();
		for (EaeFinalisation finalisation : resultReq) {
			EaeFinalizationDto dto = new EaeFinalizationDto(finalisation);
			result.add(dto);
		}

		return result;
	}

	@Override
	public ReturnMessageDto updateCapEae(Integer idEae, boolean cap) {

		ReturnMessageDto result = new ReturnMessageDto();

		Eae eae = eaeRepository.findEae(idEae);

		if (null == eae) {
			result.getErrors().add("Eae non trouvé");
			return result;
		}

		eae.setCap(cap);

		eaeRepository.persistEntity(eae);

		result.getInfos().add("CAP de l'EAE mis à jour.");
		return result;
	}

	@Override
	@Transactional(readOnly = true)
	public List<EaeDashboardItemDto> getEaesDashboardForSIRH(Integer anneeCampagne) throws SirhWSConsumerException {

		List<EaeDashboardItemDto> result = new ArrayList<EaeDashboardItemDto>();

		// on recupere la campagne EAE
		EaeCampagne eaeCampagne = eaeRepository.findEaeCampagneByAnnee(anneeCampagne);

		if (null == eaeCampagne) {
			throw new NoContentException();
		}

		// listerEaeFichePosteGrouperParDirectionSection
		Map<String, List<String>> mapDirectionSection = eaeRepository.getListEaeFichePosteParDirectionEtSection(eaeCampagne.getIdCampagneEae());

		if (null != mapDirectionSection) {
			for (Map.Entry<String, List<String>> entry : mapDirectionSection.entrySet()) {
				String direction = entry.getKey();
				List<String> listSection = entry.getValue();

				if (null != listSection) {
					for (String section : listSection) {

						// on cherche la liste des EAE pour cette direction et
						// section
						// et par etat
						Integer nbEaeNonAffecte = eaeRepository.countEaeByCampagneAndDirectionAndSectionAndStatut(eaeCampagne.getIdCampagneEae(),
								direction, section, EaeEtatEnum.NA.name(), false);

						Integer nbEaeNonDebute = eaeRepository.countEaeByCampagneAndDirectionAndSectionAndStatut(eaeCampagne.getIdCampagneEae(),
								direction, section, EaeEtatEnum.ND.name(), false);

						Integer nbEaeCree = eaeRepository.countEaeByCampagneAndDirectionAndSectionAndStatut(eaeCampagne.getIdCampagneEae(), direction,
								section, EaeEtatEnum.C.name(), false);

						Integer nbEaeEnCours = eaeRepository.countEaeByCampagneAndDirectionAndSectionAndStatut(eaeCampagne.getIdCampagneEae(),
								direction, section, EaeEtatEnum.EC.name(), false);

						Integer nbEaeFinalise = eaeRepository.countEaeByCampagneAndDirectionAndSectionAndStatut(eaeCampagne.getIdCampagneEae(),
								direction, section, EaeEtatEnum.F.name(), false);

						Integer nbEaeControle = eaeRepository.countEaeByCampagneAndDirectionAndSectionAndStatut(eaeCampagne.getIdCampagneEae(),
								direction, section, EaeEtatEnum.CO.name(), false);

						Integer nbEaeCAP = eaeRepository.countEaeByCampagneAndDirectionAndSectionAndStatut(eaeCampagne.getIdCampagneEae(), direction,
								section, null, true);

						Integer totalEAE = nbEaeNonAffecte + nbEaeNonDebute + nbEaeCree + nbEaeEnCours + nbEaeFinalise + nbEaeControle;

						// on cherche les propositions d'avancement
						Integer nbAvctNonDefini = eaeRepository.countAvisSHD(eaeCampagne.getIdCampagneEae(), direction, section, false, null, false);

						Integer nbAvctMini = eaeRepository.countAvisSHD(eaeCampagne.getIdCampagneEae(), direction, section, false, "MINI", false);

						Integer nbAvctMoy = eaeRepository.countAvisSHD(eaeCampagne.getIdCampagneEae(), direction, section, false, "MOY", false);

						Integer nbAvctMAxi = eaeRepository.countAvisSHD(eaeCampagne.getIdCampagneEae(), direction, section, false, "MAXI", false);

						Integer nbAvctChangementClasse = eaeRepository.countAvisSHD(eaeCampagne.getIdCampagneEae(), direction, section, true, null,
								true);

						EaeDashboardItemDto dto = new EaeDashboardItemDto();

						dto.setDirection(direction);
						dto.setSection(section);

						dto.setNonAffecte(nbEaeNonAffecte);
						dto.setNonDebute(nbEaeNonDebute);
						dto.setCree(nbEaeCree);
						dto.setEnCours(nbEaeEnCours);
						dto.setFinalise(nbEaeFinalise);

						dto.setNbEaeControle(nbEaeControle);
						dto.setNbEaeCAP(nbEaeCAP);
						dto.setTotalEAE(totalEAE);
						dto.setNonDefini(nbAvctNonDefini);
						dto.setMini(nbAvctMini);
						dto.setMoy(nbAvctMoy);
						dto.setMaxi(nbAvctMAxi);
						dto.setChangClasse(nbAvctChangementClasse);

						result.add(dto);
					}
				}
			}
		}

		Collections.sort(result, new Comparator<EaeDashboardItemDto>() {

			@Override
			public int compare(EaeDashboardItemDto o1, EaeDashboardItemDto o2) {

				if (o1 == o2) {
					return 0;
				}
				if (null == o2 || o2.getDirection() == null) {
					return 1;
				}
				if (null == o1 || o1.getDirection() == null) {
					return -1;
				}
				return o1.getDirection().compareTo(o2.getDirection());
			}

		});
		return result;
	}
}
