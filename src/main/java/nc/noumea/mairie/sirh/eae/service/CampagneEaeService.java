package nc.noumea.mairie.sirh.eae.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nc.noumea.mairie.sirh.eae.domain.EaeCampagne;
import nc.noumea.mairie.sirh.eae.domain.EaeCampagneActeurs;
import nc.noumea.mairie.sirh.eae.domain.EaeCampagneAction;
import nc.noumea.mairie.sirh.eae.domain.EaeCampagneTask;
import nc.noumea.mairie.sirh.eae.domain.EaeDocument;
import nc.noumea.mairie.sirh.eae.dto.CampagneEaeDto;
import nc.noumea.mairie.sirh.eae.dto.EaeCampagneActeursDto;
import nc.noumea.mairie.sirh.eae.dto.EaeCampagneActionDto;
import nc.noumea.mairie.sirh.eae.dto.EaeCampagneTaskDto;
import nc.noumea.mairie.sirh.eae.dto.EaeDocumentDto;
import nc.noumea.mairie.sirh.eae.dto.ReturnMessageDto;
import nc.noumea.mairie.sirh.eae.repository.IEaeRepository;

@Service
public class CampagneEaeService implements ICampagneEaeService {

	@Autowired
	IEaeRepository eaeRepository;

	@Override
	@Transactional(value = "eaeTransactionManager")
	public List<CampagneEaeDto> getListeCampagneEae() {

		List<CampagneEaeDto> result = new ArrayList<CampagneEaeDto>();

		List<EaeCampagne> listCampagneEAe = eaeRepository.getListeCampagneEae();

		if (null != listCampagneEAe && !listCampagneEAe.isEmpty()) {
			for (EaeCampagne campagne : listCampagneEAe) {
				result.add(new CampagneEaeDto(campagne));
			}
		}

		return result;
	}

	@Override
	@Transactional(value = "eaeTransactionManager")
	public ReturnMessageDto createOrModifyCampagneEae(CampagneEaeDto campagneEaeDto) {

		ReturnMessageDto result = new ReturnMessageDto();

		if (null == campagneEaeDto) {
			result.getErrors().add("Merci de renseigner les informations de la campagne.");
			return result;
		}

		EaeCampagne eaeCampagne = null;

		if (null != campagneEaeDto.getIdCampagneEae()) {
			eaeCampagne = eaeRepository.findEaeCampagne(campagneEaeDto.getIdCampagneEae());

			if (null == eaeCampagne) {
				result.getErrors().add("La campagne EAE n'existe pas.");
				return result;
			}
		}

		if (null == eaeCampagne) {
			eaeCampagne = new EaeCampagne();
			eaeCampagne.setAnnee(campagneEaeDto.getAnnee());
		}

		eaeCampagne.setCommentaire(campagneEaeDto.getCommentaire());
		eaeCampagne.setDateDebut(campagneEaeDto.getDateDebut());
		eaeCampagne.setDateFin(campagneEaeDto.getDateFin());
		eaeCampagne.setDateOuvertureKiosque(campagneEaeDto.getDateOuvertureKiosque());
		eaeCampagne.setDateFermetureKiosque(campagneEaeDto.getDateFermetureKiosque());

		if (null == eaeCampagne.getListeCampagneAction()) {
			eaeCampagne.setListeCampagneAction(new HashSet<EaeCampagneAction>());
		}
		if (null == eaeCampagne.getListeDocument()) {
			eaeCampagne.setListeDocument(new HashSet<EaeDocument>());
		}

		if (null != campagneEaeDto.getListeCampagneAction() && !campagneEaeDto.getListeCampagneAction().isEmpty()) {
			for (EaeCampagneActionDto actionDto : campagneEaeDto.getListeCampagneAction()) {
				EaeCampagneAction action = getEaeCampagneActionByIdAction((HashSet<EaeCampagneAction>) eaeCampagne.getListeCampagneAction(),
						actionDto.getIdCampagneAction());

				if (null == action) {
					action = new EaeCampagneAction();
				}

				action.setIdCampagneAction(actionDto.getIdCampagneAction());
				action.setNomAction(actionDto.getNomAction());
				action.setCommentaire(actionDto.getCommentaire());
				action.setDateAFaireLe(actionDto.getDateAFaireLe());
				action.setDateFaitLe(actionDto.getDateFaitLe());
				action.setDateMailEnvoye(actionDto.getDateMailEnvoye());
				action.setDateTransmission(actionDto.getDateTransmission());
				action.setEaeCampagne(eaeCampagne);
				action.setIdAgentRealisation(actionDto.getIdAgentRealisation());
				action.setMessage(actionDto.getMessage());

				if (!eaeCampagne.getListeCampagneAction().contains(action)) {
					eaeCampagne.getListeCampagneAction().add(action);
				}

				///////////// gestion des acteurs de l action ///////////
				if (null != action.getListActeurs()) {
					action.setListActeurs(new ArrayList<EaeCampagneActeurs>());
				}
				if (null != actionDto.getListeCampagneActeurs() && !actionDto.getListeCampagneActeurs().isEmpty()) {
					for (EaeCampagneActeursDto acteursDto : actionDto.getListeCampagneActeurs()) {

						EaeCampagneActeurs acteur = getEaeCampagneActeursByIdActeur(action.getListActeurs(), acteursDto.getIdCampagneActeurs());

						if (null == acteur) {
							acteur = new EaeCampagneActeurs();
							acteur.setCampagneAction(action);
						}

						acteur.setIdAgent(acteursDto.getIdAgent());

						if (!action.getListActeurs().contains(acteur)) {
							action.getListActeurs().add(acteur);
						}
					}
				}
			}
		}

		if (null != campagneEaeDto.getListeEaeDocument() && !campagneEaeDto.getListeEaeDocument().isEmpty()) {
			for (EaeDocumentDto documentDto : campagneEaeDto.getListeEaeDocument()) {
				EaeDocument document = getEaeDocumentByIdDocument(eaeCampagne.getListeDocument(), documentDto.getIdDocument());

				if (null == document) {
					document = new EaeDocument();
					document.setEaeCampagne(eaeCampagne);
				}

				document.setIdCampagneAction(documentDto.getIdCampagneAction());
				document.setIdDocument(documentDto.getIdDocument());
				document.setTypeDocument(documentDto.getTypeDocument());

				if (!eaeCampagne.getListeDocument().contains(document)) {
					eaeCampagne.getListeDocument().add(document);
				}
			}
		}

		eaeRepository.persistEntity(eaeCampagne);

		result.getInfos().add("Campagne EAE mise à jour.");
		return result;
	}

	private EaeCampagneAction getEaeCampagneActionByIdAction(HashSet<EaeCampagneAction> listEaeCampagneAction, Integer idAction) {

		if (null == listEaeCampagneAction || listEaeCampagneAction.isEmpty() || null == idAction) {
			return null;
		}

		for (EaeCampagneAction action : listEaeCampagneAction) {
			if (action.getIdCampagneAction().equals(idAction)) {
				return action;
			}
		}
		return null;
	}

	private EaeDocument getEaeDocumentByIdDocument(Set<EaeDocument> listEaeDocument, Integer idDocument) {

		if (null == listEaeDocument || listEaeDocument.isEmpty() || null == idDocument) {
			return null;
		}

		for (EaeDocument doc : listEaeDocument) {
			if (doc.getIdDocument().equals(idDocument)) {
				return doc;
			}
		}
		return null;
	}

	private EaeCampagneActeurs getEaeCampagneActeursByIdActeur(List<EaeCampagneActeurs> listEaeCampagneActeurs, Integer idActeur) {

		if (null == listEaeCampagneActeurs || listEaeCampagneActeurs.isEmpty() || null == idActeur) {
			return null;
		}

		for (EaeCampagneActeurs acteur : listEaeCampagneActeurs) {
			if (acteur.getIdCampagneActeurs().equals(idActeur)) {
				return acteur;
			}
		}
		return null;
	}

	@Override
	@Transactional(value = "eaeTransactionManager")
	public CampagneEaeDto getCampagneEaeAnnePrecedente(Integer anneePrecedente) {

		EaeCampagne eaeCampagne = eaeRepository.findEaeCampagneByAnnee(anneePrecedente);

		if (null == eaeCampagne) {
			return null;
		}
		CampagneEaeDto res = new CampagneEaeDto();
		res.setIdCampagneEae(eaeCampagne.getIdCampagneEae());
		return res;
	}

	@Override
	@Transactional(value = "eaeTransactionManager")
	public EaeDocumentDto getEaeDocumentByIdDocument(Integer idDocument) {

		EaeDocument eaeDoc = eaeRepository.getEaeDocumentByIdDocument(idDocument);

		if (null == eaeDoc) {
			return null;
		}

		return new EaeDocumentDto(eaeDoc);
	}

	@Override
	@Transactional(value = "eaeTransactionManager")
	public ReturnMessageDto deleteEaeDocument(Integer idEaeDocument) {

		ReturnMessageDto result = new ReturnMessageDto();

		EaeDocument eaeDoc = eaeRepository.getEntity(EaeDocument.class, idEaeDocument);

		if (null == eaeDoc) {
			result.getErrors().add("Document inexistant.");
			return result;
		}

		eaeRepository.removeEntity(eaeDoc);

		result.getInfos().add("Document supprimé.");

		return result;
	}

	@Override
	@Transactional(value = "eaeTransactionManager")
	public EaeCampagneTaskDto findEaeCampagneTaskByIdCampagne(Integer idEaeCampagne) {

		EaeCampagneTask eaeCampagneTask = eaeRepository.findEaeCampagneTaskByIdCampagne(idEaeCampagne);

		if (null == eaeCampagneTask || eaeCampagneTask.getDateCalculEae() == null) {
			return new EaeCampagneTaskDto();
		}
		return new EaeCampagneTaskDto(eaeCampagneTask);
	}

	@Override
	@Transactional(value = "eaeTransactionManager")
	public ReturnMessageDto createOrModifyEaeCampagneTask(EaeCampagneTaskDto campagneTaskDto) {

		ReturnMessageDto result = new ReturnMessageDto();

		if (null == campagneTaskDto) {
			result.getErrors().add("Merci de renseigner les informations.");
			return result;
		}

		EaeCampagneTask eaeCampagneTask = null;

		if (null != campagneTaskDto.getIdEaeCampagneTask()) {
			eaeCampagneTask = eaeRepository.findEaeCampagneTask(campagneTaskDto.getIdEaeCampagneTask());

			if (null == eaeCampagneTask) {
				result.getErrors().add("Eae Campagne Task non trouvé.");
				return result;
			}
		}

		if (null == eaeCampagneTask) {
			eaeCampagneTask = new EaeCampagneTask();
			// je ne vois pas pk cette ligne, je l'enleve
			// eaeCampagneTask.setIdEaeCampagneTask(campagneTaskDto.getIdEaeCampagneTask());
		}

		eaeCampagneTask.setAnnee(campagneTaskDto.getAnnee());
		eaeCampagneTask.setDateCalculEae(campagneTaskDto.getDateCalculEae());
		eaeCampagneTask.setEaeCampagne(eaeRepository.findEaeCampagne(campagneTaskDto.getIdEaeCampagne()));
		eaeCampagneTask.setIdAgent(campagneTaskDto.getIdAgent());

		eaeRepository.persistEntity(eaeCampagneTask);

		result.getInfos().add("Campagne EAE Task mis à jour.");
		return result;
	}

}
