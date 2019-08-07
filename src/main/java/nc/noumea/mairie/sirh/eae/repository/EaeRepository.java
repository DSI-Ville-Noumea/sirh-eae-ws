package nc.noumea.mairie.sirh.eae.repository;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeCampagne;
import nc.noumea.mairie.sirh.eae.domain.EaeCampagneTask;
import nc.noumea.mairie.sirh.eae.domain.EaeDocument;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluateur;
import nc.noumea.mairie.sirh.eae.domain.EaeEvalue;
import nc.noumea.mairie.sirh.eae.domain.EaeFinalisation;
import nc.noumea.mairie.sirh.eae.domain.EaeTypeDeveloppement;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeAgentStatutEnum;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeEtatEnum;
import nc.noumea.mairie.sirh.eae.dto.EaeListItemDto;
import nc.noumea.mairie.sirh.eae.dto.FormRehercheGestionEae;
import nc.noumea.mairie.sirh.tools.IHelper;

@Repository
public class EaeRepository implements IEaeRepository {

	@Autowired
	private IHelper			helper;

	@PersistenceContext(unitName = "eaePersistenceUnit")
	private EntityManager	eaeEntityManager;

	@PersistenceContext(unitName = "synchrosiPersistenceUnit")
	private EntityManager	synchrosiPersistenceUnit;

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getActiveAgentFromTiarhe() {
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct(identifianttechnique) FROM vm_tiarhe_agent ");
		sb.append("where statut != 'Supprimé' and identifianttechnique is not null");

		Query q = synchrosiPersistenceUnit.createNativeQuery(sb.toString());

		return q.getResultList();
	}

	@Override
	public EaeCampagne findEaeCampagne(Integer idEaeCampagne) {
		return eaeEntityManager.find(EaeCampagne.class, idEaeCampagne);
	}

	@Override
	public <T> T getEntity(Class<T> Tclass, Object Id) {
		return eaeEntityManager.find(Tclass, Id);
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
	public List<Eae> findAllForMigration(List<String> listIdsAgent) {
		TypedQuery<Eae> eaeQuery = eaeEntityManager.createNamedQuery("findEaeForMigration", Eae.class);
		List<Integer> integerList = Lists.newArrayList();
		for (String id : listIdsAgent) {
			integerList.add(Integer.valueOf(id));
		}
		eaeQuery.setParameter("listIdsAgent", integerList);
		return eaeQuery.getResultList();
	}

	@Override
	public EaeCampagne findEaeCampagneByAnnee(Integer annee) {

		EaeCampagne result = null;

		TypedQuery<EaeCampagne> eaeQuery = eaeEntityManager.createQuery("select e from EaeCampagne e where annee = :annee ", EaeCampagne.class);
		eaeQuery.setParameter("annee", annee);

		if (0 < eaeQuery.getResultList().size())
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

		if (0 < eaeQuery.getResultList().size())
			result = eaeQuery.getResultList().get(0);

		return result;
	}

	@Override
	@Transactional(value = "eaeTransactionManager")
	public EaeCampagneTask findEaeCampagneTask(Integer idEaeCampagneTask) {

		EaeCampagneTask result = null;

		TypedQuery<EaeCampagneTask> eaeQuery = eaeEntityManager.createNamedQuery("EaeCampagneTask.getEaeCampagneTask", EaeCampagneTask.class);
		eaeQuery.setParameter("idEaeCampagneTask", idEaeCampagneTask);

		if (0 < eaeQuery.getResultList().size())
			result = eaeQuery.getSingleResult();

		return result;
	}

	@Override
	@Transactional(value = "eaeTransactionManager")
	public EaeCampagneTask findEaeCampagneTaskByIdCampagne(Integer idEaeCampagne) {

		EaeCampagneTask result = null;

		TypedQuery<EaeCampagneTask> eaeQuery = eaeEntityManager.createNamedQuery("EaeCampagneTask.getEaeCampagneTaskByIdCampagne",
				EaeCampagneTask.class);
		eaeQuery.setParameter("idEaeCampagne", idEaeCampagne);

		List<EaeCampagneTask> res = eaeQuery.getResultList();

		if (res.size() > 0)
			result = res.get(0);

		return result;
	}

	@Override
	public List<EaeEvalue> findEaeEvalueWithEaesByIdAgentOnly(Integer agentId) {

		// Query
		StringBuilder sb = new StringBuilder();
		sb.append("select ev from EaeEvalue ev ");
		sb.append("INNER JOIN ev.eae AS e ");
		sb.append("INNER JOIN e.eaeCampagne AS ec ");
		sb.append("where  ev.idAgent= :idAgent and ec.annee != 2012");

		TypedQuery<EaeEvalue> eaeQuery = eaeEntityManager.createQuery(sb.toString(), EaeEvalue.class);
		eaeQuery.setParameter("idAgent", agentId);

		return eaeQuery.getResultList();
	}

	@Override
	public Eae findEae(Integer idEae) {

		TypedQuery<Eae> eaeQuery = eaeEntityManager.createNamedQuery("findEaeByIdAgent", Eae.class);
		eaeQuery.setParameter("idEae", idEae);

		if (0 < eaeQuery.getResultList().size())
			return eaeQuery.getResultList().get(0);

		return null;
	}

	@Override
	public Integer chercherEaeNumIncrement() {

		String sqlClePrimaire = "select nextval('EAE_S_NUM_INCREMENT_DOCUMENT')";
		Query q = eaeEntityManager.createNativeQuery(sqlClePrimaire.toString(), Integer.class);

		return q.getFirstResult();
	}

	@Override
	public List<EaeTypeDeveloppement> getListeTypeDeveloppement() {

		TypedQuery<EaeTypeDeveloppement> eaeQuery = eaeEntityManager.createNamedQuery("getListeTypeDeveloppement", EaeTypeDeveloppement.class);

		return eaeQuery.getResultList();
	}

	@Override
	public List<EaeCampagne> getListeCampagneEae() {

		TypedQuery<EaeCampagne> eaeQuery = eaeEntityManager.createNamedQuery("getListeCampagneEae", EaeCampagne.class);

		return eaeQuery.getResultList();
	}

	@Override
	public EaeDocument getEaeDocumentByIdDocument(Integer idDocument) {

		TypedQuery<EaeDocument> eaeQuery = eaeEntityManager.createNamedQuery("getEaeDocumentByIdDocument", EaeDocument.class);
		eaeQuery.setParameter("idDocument", idDocument);

		if (0 < eaeQuery.getResultList().size())
			return eaeQuery.getResultList().get(0);

		return null;
	}

	@Override
	public List<Eae> getListeEae(FormRehercheGestionEae form, Integer pageSize, Integer pageNumber) {

		StringBuilder sbSelect = new StringBuilder();
		sbSelect.append("select e from Eae e ");
		StringBuilder sbInner = new StringBuilder();
		sbInner.append(" inner join e.eaeEvalue ev ");
		StringBuilder sbWhere = new StringBuilder();
		sbWhere.append("where e.eaeCampagne.idCampagneEae = :idCampagneEae ");

		if (null != form.getEtat() && !"".equals(form.getEtat().trim())) {
			sbWhere.append(" and e.etat = :etat ");
		}
		if (null != form.getStatut() && !"".equals(form.getStatut().trim())) {
			sbWhere.append(" and ev.statut = :statut ");
		}
		if (null != form.getListeSousService() && !form.getListeSousService().isEmpty()) {
			sbInner.append(" inner join e.eaeFichePostes fp ");
			sbInner.append("  on fp.idServiceADS in :listeIdServiceAds ");
		}
		if (null != form.getCap()) {
			sbWhere.append(" and e.cap = :cap ");
		}
		if (null != form.getIsEstDetache()) {
			sbWhere.append(" and ev.estDetache = :estDetache ");
		}
		if (null != form.getIdAgentEvaluateur()) {
			sbInner.append(" inner join e.eaeEvaluateurs eval on eval.idAgent = :idAgentEvaluateur  ");
		}
		if (null != form.getIdAgentEvalue()) {
			sbWhere.append(" and ev.idAgent = :idAgentEvalue ");
		}
		
		// On tri automatiquement sur l'id des agents, afin de garder des résultats cohérents pour la pagination lors des changements de page.
		sbWhere.append(" ORDER BY e.eaeEvalue.idAgent");

		TypedQuery<Eae> eaeQuery = eaeEntityManager.createQuery(sbSelect.toString() + sbInner.toString() + sbWhere.toString(), Eae.class);
		eaeQuery.setParameter("idCampagneEae", form.getIdCampagneEae());
		if (null != form.getEtat() && !"".equals(form.getEtat().trim())) {
			eaeQuery.setParameter("etat", EaeEtatEnum.valueOf(form.getEtat()));
		}
		if (null != form.getStatut() && !"".equals(form.getStatut().trim())) {
			eaeQuery.setParameter("statut", EaeAgentStatutEnum.valueOf(form.getStatut()));
		}
		if (null != form.getListeSousService() && !form.getListeSousService().isEmpty()) {
			eaeQuery.setParameter("listeIdServiceAds", form.getListeSousService());
		}
		if (null != form.getCap()) {
			eaeQuery.setParameter("cap", form.getCap());
		}
		if (null != form.getIsEstDetache()) {
			eaeQuery.setParameter("estDetache", form.getIsEstDetache());
		}
		if (null != form.getIdAgentEvaluateur()) {
			eaeQuery.setParameter("idAgentEvaluateur", form.getIdAgentEvaluateur());
		}
		if (null != form.getIdAgentEvalue()) {
			eaeQuery.setParameter("idAgentEvalue", form.getIdAgentEvalue());
		}
		
		if (pageSize != null)
			eaeQuery.setMaxResults(pageSize);
		
		if (pageNumber != null && pageSize != null) {
			eaeQuery.setFirstResult(pageSize * (pageNumber - 1));
		}

		return eaeQuery.getResultList();
	}

	@Override
	public String getLastDocumentEaeFinalise(Integer idEae) {

		TypedQuery<EaeFinalisation> eaeQuery = eaeEntityManager.createNamedQuery("lastdocumentFinalise", EaeFinalisation.class);
		eaeQuery.setParameter("idEae", idEae);

		if (0 < eaeQuery.getResultList().size())
			return eaeQuery.getResultList().get(0).getNodeRefAlfresco();

		return null;
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
		sb.append(
				"and e.eaeCampagne.dateOuvertureKiosque is not null and e.eaeCampagne.dateFermetureKiosque is null and  e.eaeCampagne.dateOuvertureKiosque < :date");

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
		for (Eae eae : queryResult) {
			// #38868 : Le compteur des EAE à réaliser ne se fait que sur les EAE pouvant être édités par l'agent.
			EaeListItemDto dtoItem = new EaeListItemDto(eae);
			dtoItem.setAccessRightsForAgentId(eae, agentId);
			if (!disctinctEaes.contains(eae) && (dtoItem.isDroitDemarrer() || dtoItem.isDroitInitialiser()))
				disctinctEaes.add(eae);
		}

		return disctinctEaes;
	}

	@Override
	public Map<String, Map<String, List<String>>> getListEaeFichePosteParDirectionEtSection(Integer idCampagneEAE) {

		Map<String, Map<String, List<String>>> result = new HashMap<String, Map<String, List<String>>>();

		StringBuilder sb = new StringBuilder();
		sb.append("select fp.DIRECTION_SERVICE, fp.SERVICE, fp.SECTION_SERVICE from EAE_FICHE_POSTE fp ");
		sb.append("inner join EAE e on e.id_eae=fp.ID_EAE ");
		sb.append("where e.ID_CAMPAGNE_EAE = :idCampagneEAE ");
		sb.append("group by fp.DIRECTION_SERVICE, fp.SERVICE, fp.SECTION_SERVICE ");
		sb.append("order by fp.DIRECTION_SERVICE, fp.SERVICE ");

		Query q = eaeEntityManager.createNativeQuery(sb.toString());

		q.setParameter("idCampagneEAE", idCampagneEAE);

		@SuppressWarnings("unchecked")
		List<Object[]> resultQuery = q.getResultList();

		// Construction de la Map
		if (null != resultQuery) {
			for (Object[] l : resultQuery) {

				String direction = (String) l[0];
				String service = (String) l[1];
				String section = (String) l[2];

				Map<String, List<String>> mapServices = null;
				List<String> listSection = null;
				if (result.get(direction) != null) {
					mapServices = result.get(direction);
					if (mapServices.get(service) != null) {
						listSection = mapServices.get(service);
						listSection.add(section);
					} else {
						listSection = new ArrayList<String>();
						listSection.add(section);
						mapServices.put(service, listSection);
				}
				} else {
					mapServices =  new HashMap<String, List<String>>();
					listSection = new ArrayList<String>();
					listSection.add(section);
					mapServices.put(service, listSection);
				}

				result.put(direction, mapServices);
			}
		}

		return result;
	}

	@Override
	public Integer countEaeByCampagneAndDirectionAndSectionAndStatut(Integer idCampagneEae, String direction, String service, String section, String etat,
			boolean cap) {

		StringBuilder sb = new StringBuilder();
		sb.append(" select count(e.id_eae) from EAE e ");
		sb.append(" inner join EAE_FICHE_POSTE fp on e.id_eae=fp.id_eae ");
		sb.append(" where e.ID_CAMPAGNE_EAE = :idCampagneEae ");

		if (null != direction) {
			sb.append(" and fp.DIRECTION_SERVICE = :direction ");
		}
		if (null != service) {
			sb.append(" and fp.SERVICE = :service ");
		}
		if (null != section) {
			sb.append(" and fp.SECTION_SERVICE = :section ");
		}
		if (null != etat) {
			sb.append(" and e.ETAT = :etat ");
		}

		if (cap) {
			sb.append(" and e.CAP is true ");
		}

		Query q = eaeEntityManager.createNativeQuery(sb.toString());

		q.setParameter("idCampagneEae", idCampagneEae);
		if (null != direction) {
			q.setParameter("direction", direction);
		}
		if (null != service) {
			q.setParameter("service", service);
		}
		if (null != section) {
			q.setParameter("section", section);
		}
		if (null != etat) {
			q.setParameter("etat", etat);
		}

		BigInteger result = (BigInteger) q.getSingleResult();

		return result.intValue();
	}

	@Override
	public Integer countAvisSHD(Integer idCampagneEae, String direction, String service, String section, boolean avisRevalorisation, String dureeAvct,
			boolean avisChangementClasse) {

		StringBuilder sb = new StringBuilder();
		sb.append(" select count(ev.id_eae) from EAE_EVALUATION ev ");
		sb.append(" inner join EAE e on e.id_eae=ev.id_eae ");
		sb.append(" inner join EAE_FICHE_POSTE fp on fp.id_eae=ev.id_eae ");
		sb.append(" where e.ID_CAMPAGNE_EAE = :idCampagneEae ");

		if (null != direction) {
			sb.append(" and fp.DIRECTION_SERVICE = :direction ");
		}
		if (null != service) {
			sb.append(" and fp.SERVICE = :service ");
		}
		if (null != section) {
			sb.append(" and fp.SECTION_SERVICE = :section ");
		}

		if (!avisRevalorisation) {
			sb.append(" and (ev.AVIS_REVALORISATION is null or ev.AVIS_REVALORISATION = 0) ");
		}
		if (null != dureeAvct) {
			sb.append(" and ev.PROPOSITION_AVANCEMENT = :dureeAvct ");
		}
		if (avisChangementClasse) {
			sb.append(" and ev.AVIS_CHANGEMENT_CLASSE = 1 ");
		}

		Query q = eaeEntityManager.createNativeQuery(sb.toString());

		q.setParameter("idCampagneEae", idCampagneEae);
		if (null != direction) {
			q.setParameter("direction", direction);
		}
		if (null != service) {
			q.setParameter("service", service);
		}
		if (null != section) {
			q.setParameter("section", section);
		}
		if (null != dureeAvct) {
			q.setParameter("dureeAvct", dureeAvct);
		}

		BigInteger result = (BigInteger) q.getSingleResult();

		return result.intValue();
	}

	@Override
	public EaeEvaluateur findEvaluateurByIdEaeEvaluateur(Integer idEaeEvaluateur) {
		return eaeEntityManager.find(EaeEvaluateur.class, idEaeEvaluateur);
	}
	
	@Override
	public Integer countList(FormRehercheGestionEae form) {
		return getListeEae(form, null, null).size();
	}
}
