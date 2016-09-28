package nc.noumea.mairie.sirh.eae.repository;

import java.util.List;
import java.util.Map;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeCampagne;
import nc.noumea.mairie.sirh.eae.domain.EaeCampagneTask;
import nc.noumea.mairie.sirh.eae.domain.EaeDocument;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluateur;
import nc.noumea.mairie.sirh.eae.domain.EaeEvalue;
import nc.noumea.mairie.sirh.eae.domain.EaeTypeDeveloppement;
import nc.noumea.mairie.sirh.eae.dto.FormRehercheGestionEae;

public interface IEaeRepository {

	EaeCampagne findEaeCampagne(Integer idEaeCampagne);

	<T> T getEntity(Class<T> Tclass, Object Id);

	void persistEntity(Object entity);

	void removeEntity(Object entity);

	EaeCampagne findEaeCampagneByAnnee(Integer annee);

	Eae findEaeAgent(Integer idAgent, Integer idEaeCampagne);

	EaeCampagneTask findEaeCampagneTask(Integer idEaeCampagneTask);

	List<EaeEvalue> findEaeEvalueWithEaesByIdAgentOnly(Integer agentId);

	Integer chercherEaeNumIncrement();

	List<EaeTypeDeveloppement> getListeTypeDeveloppement();

	Eae findEae(Integer idEae);

	List<EaeCampagne> getListeCampagneEae();

	EaeDocument getEaeDocumentByIdDocument(Integer idDocument);

	List<Eae> getListeEae(FormRehercheGestionEae form);

	EaeCampagneTask findEaeCampagneTaskByIdCampagne(Integer idEaeCampagne);

	String getLastDocumentEaeFinalise(Integer idEae);

	List<Eae> findEaesNonDebuteOuCreeOuEnCoursForEaeListByAgentIds(List<Integer> agentIds, Integer agentId);

	Map<String, List<String>> getListEaeFichePosteParDirectionEtSection(Integer idCampagneEAE);

	Integer countEaeByCampagneAndDirectionAndSectionAndStatut(Integer idCampagneEae, String direction, String section, String etat, boolean cap);

	Integer countAvisSHD(Integer idCampagneEae, String direction, String section, boolean avisRevalorisation, String dureeAvct,
			boolean avisChangementClasse);

	EaeEvaluateur findEvaluateurByIdEaeEvaluateur(Integer idEaeEvaluateur);
}
