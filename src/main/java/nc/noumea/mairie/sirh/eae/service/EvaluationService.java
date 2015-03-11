package nc.noumea.mairie.sirh.eae.service;

import java.util.ArrayList;
import java.util.List;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeAppreciation;
import nc.noumea.mairie.sirh.eae.domain.EaeAutoEvaluation;
import nc.noumea.mairie.sirh.eae.domain.EaeCommentaire;
import nc.noumea.mairie.sirh.eae.domain.EaeDeveloppement;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluateur;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluation;
import nc.noumea.mairie.sirh.eae.domain.EaeEvolution;
import nc.noumea.mairie.sirh.eae.domain.EaeEvolutionSouhait;
import nc.noumea.mairie.sirh.eae.domain.EaeFichePoste;
import nc.noumea.mairie.sirh.eae.domain.EaePlanAction;
import nc.noumea.mairie.sirh.eae.domain.EaeResultat;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeAvancementEnum;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeAvisEnum;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeDelaiEnum;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeNiveauEnum;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeTypeAppreciationEnum;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeTypeDeveloppementEnum;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeTypeObjectifEnum;
import nc.noumea.mairie.sirh.eae.dto.EaeAppreciationsDto;
import nc.noumea.mairie.sirh.eae.dto.EaeAutoEvaluationDto;
import nc.noumea.mairie.sirh.eae.dto.EaeEvaluationDto;
import nc.noumea.mairie.sirh.eae.dto.EaeEvolutionDto;
import nc.noumea.mairie.sirh.eae.dto.EaeResultatsDto;
import nc.noumea.mairie.sirh.eae.dto.identification.EaeIdentificationDto;
import nc.noumea.mairie.sirh.eae.dto.planAction.EaePlanActionDto;
import nc.noumea.mairie.sirh.eae.dto.planAction.PlanActionItemDto;
import nc.noumea.mairie.sirh.eae.dto.poste.EaeFichePosteDto;
import nc.noumea.mairie.sirh.eae.dto.poste.SpbhorDto;
import nc.noumea.mairie.sirh.eae.service.dataConsistency.EaeDataConsistencyServiceException;
import nc.noumea.mairie.sirh.eae.service.dataConsistency.IEaeDataConsistencyService;
import nc.noumea.mairie.sirh.service.IAgentService;
import nc.noumea.mairie.sirh.ws.ISirhWsConsumer;
import nc.noumea.mairie.sirh.ws.SirhWSConsumerException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EvaluationService implements IEvaluationService {

	private Logger logger = LoggerFactory.getLogger(EvaluationService.class);

	@Autowired
	private IAgentService agentService;

	@Autowired
	private IEaeService eaeService;

	@Autowired
	private ITypeObjectifService typeObjectifService;

	@Autowired
	private IEaeDataConsistencyService eaeDataConsistencyService;

	@Autowired
	private ISirhWsConsumer sirhWSConsumer;

	@Override
	@Transactional(readOnly = true)
	public EaeIdentificationDto getEaeIdentification(Integer idEae) {

		Eae eae = eaeService.findEae(idEae);
		
		if (eae == null)
			return null;

		for (EaeEvaluateur eval : eae.getEaeEvaluateurs()) {
			agentService.fillEaeEvaluateurWithAgent(eval);
		}

		agentService.fillEaeEvalueWithAgent(eae.getEaeEvalue());

		EaeIdentificationDto result = new EaeIdentificationDto(eae);

		return result;
	}

	@Override
	@Transactional(value = "eaeTransactionManager")
	public void setEaeIdentification(Integer idEae, EaeIdentificationDto dto) throws EvaluationServiceException, EaeServiceException {
		
		Eae eae = eaeService.startEae(idEae);
		
		eae.setDateEntretien(dto.getDateEntretien());
		eae.getEaeEvalue().setDateEntreeAdministration(dto.getSituation().getDateEntreeAdministration());
		eae.getEaeEvalue().setDateEntreeFonctionnaire(dto.getSituation().getDateEntreeFonctionnaire());
		if (eae.getPrimaryFichePoste() != null)
			eae.getPrimaryFichePoste().setDateEntreeFonction(dto.getSituation().getDateEntreeFonction());

		for (EaeEvaluateur evaluateur : eae.getEaeEvaluateurs()) {
			for (EaeEvaluateur evalDto : dto.getEvaluateurs()) {
				if (evaluateur.getIdAgent() == evalDto.getIdAgent()) {
					evaluateur.setDateEntreeFonction(evalDto.getDateEntreeFonction());
					evaluateur.setDateEntreeService(evalDto.getDateEntreeService());
				}
			}
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<EaeFichePosteDto> getEaeFichePoste(Integer idEae) {

		Eae eae = eaeService.findEae(idEae);
		
		if (eae == null)
			return null;

		List<EaeFichePosteDto> result = new ArrayList<EaeFichePosteDto>();

		for (EaeFichePoste fdp : eae.getEaeFichePostes()) {
			agentService.fillEaeFichePosteWithAgent(fdp);
			result.add(new EaeFichePosteDto(fdp));
		}

		return result;
	}

	@Override
	@Transactional(readOnly = true)
	public EaeResultatsDto getEaeResultats(Integer idEae) {

		Eae eae = eaeService.findEae(idEae);
		
		if (eae == null)
			return null;

		return new EaeResultatsDto(eae);
	}

	@Override
	@Transactional(value = "eaeTransactionManager")
	public void setEaeResultats(Integer idEae, EaeResultatsDto dto) throws EvaluationServiceException, EaeServiceException {

		Eae eae = eaeService.startEae(idEae);
		
		if (eae.getCommentaire() == null)
			eae.setCommentaire(new EaeCommentaire());

		eae.getCommentaire().setText(dto.getCommentaireGeneral());

		List<EaeResultat> listOfAllResultats = new ArrayList<EaeResultat>(eae.getEaeResultats());

		for (EaeResultat resPro : dto.getObjectifsProfessionnels()) {
			if (resPro.getIdEaeResultat() == null || resPro.getIdEaeResultat() == 0)
				createAndAddNewEaeResultat(eae, resPro, EaeTypeObjectifEnum.PROFESSIONNEL);
			else
				resPro = fillExistingEaeResultat(eae, resPro);

			listOfAllResultats.remove(resPro);
		}

		for (EaeResultat resInd : dto.getObjectifsIndividuels()) {
			if (resInd.getIdEaeResultat() == null || resInd.getIdEaeResultat() == 0)
				createAndAddNewEaeResultat(eae, resInd, EaeTypeObjectifEnum.INDIVIDUEL);
			else
				resInd = fillExistingEaeResultat(eae, resInd);

			listOfAllResultats.remove(resInd);
		}

		// Removes EaeResultats not present in the DTO (consider them as
		// deleted)
		for (EaeResultat res : listOfAllResultats) {
			eae.getEaeResultats().remove(res);
			eaeService.remove(res);
		}

		eaeService.flush();
	}

	private void createAndAddNewEaeResultat(Eae eae, EaeResultat resPro, EaeTypeObjectifEnum type) {
		resPro.setIdEaeResultat(null);
		resPro.setTypeObjectif(typeObjectifService.getTypeObjectifForLibelle(type.name()));
		eae.getEaeResultats().add(resPro);
		resPro.setEae(eae);
	}

	private EaeResultat fillExistingEaeResultat(Eae eae, EaeResultat resultat) {

		for (EaeResultat existingResultat : eae.getEaeResultats()) {

			if (existingResultat.getIdEaeResultat() != null
					&& existingResultat.getIdEaeResultat().equals(resultat.getIdEaeResultat())) {

				existingResultat.setObjectif(resultat.getObjectif());
				existingResultat.setResultat(resultat.getResultat());

				existingResultat.setCommentaire(updateEaeCommentaire(existingResultat.getCommentaire(),
						(resultat.getCommentaire())));
				return existingResultat;
			}
		}

		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public EaeAppreciationsDto getEaeAppreciations(Integer idEae, String annee) {
		
		Eae eae = null;
		EaeAppreciationsDto dto = null;
		
		// si l'année est renseignée, alors il faut trouver l'EAE de l'annee
				// precedente de la personne
		if (annee != null) {
			// on trouve l'id de l'agent de l'EAE
			Eae eaeAgent = eaeService.findEae(idEae);
			eae = eaeService.findEaeByAgentAndYear(eaeAgent.getEaeEvalue().getIdAgent(), Integer.valueOf(annee));
			if (eae == null) {
				dto = new EaeAppreciationsDto();
			} else {
				dto = new EaeAppreciationsDto(eae);
			}
		} else {
			dto = getEaeAppreciations(idEae);
		}
		
		return dto;
	}
	
	@Override
	@Transactional(readOnly = true)
	public EaeAppreciationsDto getEaeAppreciations(Integer idEae) {

		Eae eae = eaeService.findEae(idEae);
		
		if (eae == null)
			return null;

		return new EaeAppreciationsDto(eae);
	}

	@Override
	@Transactional(value = "eaeTransactionManager")
	public void setEaeAppreciations(Integer idEae, EaeAppreciationsDto dto) throws EaeServiceException {

		Eae eae = eaeService.startEae(idEae);
		
		eae.getEaeAppreciations().clear();
		eaeService.flush();

		eae.getEaeEvalue().setEstEncadrant(dto.isEstEncadrant());
		fillAppreciationsWithArray(eae, dto.getTechniqueEvalue(), dto.getTechniqueEvaluateur(),
				EaeTypeAppreciationEnum.TE);
		fillAppreciationsWithArray(eae, dto.getSavoirEtreEvalue(), dto.getSavoirEtreEvaluateur(),
				EaeTypeAppreciationEnum.SE);
		fillAppreciationsWithArray(eae, dto.getManagerialEvalue(), dto.getManagerialEvaluateur(),
				EaeTypeAppreciationEnum.MA);
		fillAppreciationsWithArray(eae, dto.getResultatsEvalue(), dto.getResultatsEvaluateur(),
				EaeTypeAppreciationEnum.RE);

	}

	private void fillAppreciationsWithArray(Eae eae, String[] arrayOfAppreciationsEvalue,
			String[] arrayOfAppreciationsEvaluateur, EaeTypeAppreciationEnum appreciationType) {
		for (int i = 0; i < arrayOfAppreciationsEvalue.length; i++) {
			EaeAppreciation app = new EaeAppreciation();
			app.setEae(eae);
			app.setNumero(i);
			app.setNoteEvalue(arrayOfAppreciationsEvalue[i]);
			app.setNoteEvaluateur(arrayOfAppreciationsEvaluateur[i]);
			app.setTypeAppreciation(appreciationType);
			eae.getEaeAppreciations().add(app);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public EaeEvaluationDto getEaeEvaluation(Integer idEae) {

		Eae eae = eaeService.findEae(idEae);
		
		if (eae == null)
			return null;

		return new EaeEvaluationDto(eae);
	}

	@Override
	@Transactional(value = "eaeTransactionManager")
	public void setEaeEvaluation(Integer idEae, EaeEvaluationDto dto) throws EvaluationServiceException, EaeServiceException {

		Eae eae = eaeService.startEae(idEae);
		
		eae.setDureeEntretienMinutes(dto.getDureeEntretien());
		EaeEvaluation evaluation = eae.getEaeEvaluation();
		evaluation.setAvisChangementClasse(dto.getAvisChangementClasse() == null ? null
				: dto.getAvisChangementClasse() ? 1 : 0);
		evaluation.setAvisRevalorisation(dto.getAvisRevalorisation() == null ? null : dto.getAvisRevalorisation() ? 1
				: 0);

		// Check the Niveau if it's valid and/or not set
		if (dto.getNiveau() == null || dto.getNiveau().getCourant() == null)
			throw new EvaluationServiceException("La propriété 'niveau' de l'évaluation est manquante.");

		EaeNiveauEnum selectedNiveau = null;

		if (dto.getNiveau() != null && dto.getNiveau().getCourant() != null) {
			try {
				selectedNiveau = EaeNiveauEnum.valueOf(dto.getNiveau().getCourant());
			} catch (IllegalArgumentException ex) {
				throw new EvaluationServiceException("La propriété 'niveau' de l'évaluation est incorrecte.");
			}
		}

		evaluation.setNiveauEae(selectedNiveau);

		// Check the propositionAvancement validity
		EaeAvancementEnum selectedAvancement = null;

		if (dto.getPropositionAvancement() != null && dto.getPropositionAvancement().getCourant() != null) {
			try {
				selectedAvancement = EaeAvancementEnum.valueOf(dto.getPropositionAvancement().getCourant());
			} catch (IllegalArgumentException ex) {
				throw new EvaluationServiceException(
						"La propriété 'propositionAvancement' de l'évaluation est incorrecte.");
			}
		}

		evaluation.setPropositionAvancement(selectedAvancement);

		// Calculate Avis SHD based on what has been modified
		calculateAvisShd(eae);

		// For all the comments, check if a comment already exists and update
		// it, or assign the new one
		evaluation.setCommentaireEvaluateur(updateEaeCommentaire(evaluation.getCommentaireEvaluateur(),
				dto.getCommentaireEvaluateur()));
		evaluation.setCommentaireEvalue(updateEaeCommentaire(evaluation.getCommentaireEvalue(),
				dto.getCommentaireEvalue()));
		evaluation.setCommentaireAvctEvaluateur(updateEaeCommentaire(evaluation.getCommentaireAvctEvaluateur(),
				dto.getCommentaireAvctEvaluateur()));
		evaluation.setCommentaireAvctEvalue(updateEaeCommentaire(evaluation.getCommentaireAvctEvalue(),
				dto.getCommentaireAvctEvalue()));

		try {
			eaeDataConsistencyService.checkDataConsistencyForEaeEvaluation(eae);
		} catch (EaeDataConsistencyServiceException e) {
			throw new EvaluationServiceException(e.getMessage(), e);
		}
	}

	protected void calculateAvisShd(Eae eae) {

		if (eae.getEaeEvalue().getTypeAvancement() == null)
			return;

		switch (eae.getEaeEvalue().getTypeAvancement().getTypeAvctCode()) {
			case "PROMO":
				if (eae.getEaeEvaluation().getAvisChangementClasse() != null)
					eae.getEaeEvaluation().setAvisShd(
							EaeAvisEnum.fromBooleanToAvisEnum(
									eae.getEaeEvaluation().getAvisChangementClasse() == 0 ? false : true).toString());
				break;
			case "REVA":
				if (eae.getEaeEvaluation().getAvisRevalorisation() != null)
					eae.getEaeEvaluation().setAvisShd(
							EaeAvisEnum.fromBooleanToAvisEnum(
									eae.getEaeEvaluation().getAvisRevalorisation() == 0 ? false : true).toString());
				break;
			case "AD":
				if (eae.getEaeEvaluation().getPropositionAvancement() != null)
					eae.getEaeEvaluation().setAvisShd(eae.getEaeEvaluation().getPropositionAvancement().toString());
				break;
			default:
				break;
		}
	}

	@Override
	@Transactional(readOnly = true)
	public EaeAutoEvaluationDto getEaeAutoEvaluation(Integer idEae) {

		Eae eae = eaeService.findEae(idEae);
		
		if (eae == null)
			return null;

		return new EaeAutoEvaluationDto(eae);
	}

	@Override
	@Transactional(value = "eaeTransactionManager")
	public void setEaeAutoEvaluation(Integer idEae, EaeAutoEvaluationDto dto) throws EaeServiceException {

		Eae eae = eaeService.startEae(idEae);
		
		EaeAutoEvaluation autoEval = eae.getEaeAutoEvaluation();

		if (autoEval == null) {
			autoEval = new EaeAutoEvaluation();
			eae.setEaeAutoEvaluation(autoEval);
			autoEval.setEae(eae);
		}

		autoEval.setParticularites(dto.getParticularites());
		autoEval.setAcquis(dto.getAcquis());
		autoEval.setSuccesDifficultes(dto.getSuccesDifficultes());
	}

	@Override
	@Transactional(readOnly = true)
	public EaePlanActionDto getEaePlanAction(Integer idEae) {

		Eae eae = eaeService.findEae(idEae);
		
		if (eae == null)
			return null;

		return new EaePlanActionDto(eae);
	}

	@Override
	@Transactional(value = "eaeTransactionManager")
	public void setEaePlanAction(Integer idEae, EaePlanActionDto dto) throws EaeServiceException {

		Eae eae = eaeService.startEae(idEae);
		
		// Clear previous plan actions items
		eae.getEaePlanActions().clear();

		for (PlanActionItemDto item : dto.getObjectifsProfessionnels()) {
			CreateAndAddPlanAction(eae, item.getObjectif(), item.getIndicateur(), EaeTypeObjectifEnum.PROFESSIONNEL);
		}

		for (String s : dto.getObjectifsIndividuels()) {
			CreateAndAddPlanAction(eae, s, null, EaeTypeObjectifEnum.INDIVIDUEL);
		}

		for (String s : dto.getMoyensFinanciers()) {
			CreateAndAddPlanAction(eae, s, null, EaeTypeObjectifEnum.FINANCIERS);
		}

		for (String s : dto.getMoyensMateriels()) {
			CreateAndAddPlanAction(eae, s, null, EaeTypeObjectifEnum.MATERIELS);
		}

		for (String s : dto.getMoyensAutres()) {
			CreateAndAddPlanAction(eae, s, null, EaeTypeObjectifEnum.AUTRES);
		}
	}

	protected void CreateAndAddPlanAction(Eae eae, String objectif, String mesure, EaeTypeObjectifEnum typeObjectif) {
		EaePlanAction pa = new EaePlanAction();
		pa.setObjectif(objectif);
		pa.setMesure(mesure);
		pa.setTypeObjectif(typeObjectifService.getTypeObjectifForLibelle(typeObjectif.name()));
		pa.setEae(eae);
		eae.getEaePlanActions().add(pa);
	}

	@Override
	@Transactional(readOnly = true)
	public EaeEvolutionDto getEaeEvolution(Integer idEae) {

		Eae eae = eaeService.findEae(idEae);
		
		if (eae == null)
			return null;

		List<SpbhorDto> listOfPartialTimes = null;
		try {
			listOfPartialTimes = sirhWSConsumer.getListSpbhor();
		} catch (SirhWSConsumerException e) {
			logger.debug(e.getMessage());
			return null;
		}

		return new EaeEvolutionDto(eae, listOfPartialTimes);
	}

	@Override
	@Transactional(value = "eaeTransactionManager")
	public void setEaeEvolution(Integer idEae, EaeEvolutionDto dto) throws EvaluationServiceException, EaeServiceException {

		Eae eae = eaeService.startEae(idEae);
		
		EaeEvolution evolution = eae.getEaeEvolution();

		if (evolution == null) {
			evolution = new EaeEvolution();
			evolution.setEae(eae);
			eae.setEaeEvolution(evolution);
		}

		EaeDelaiEnum delai = null;
		if (dto.getDelaiEnvisage() != null && dto.getDelaiEnvisage().getCourant() != null) {
			try {
				delai = EaeDelaiEnum.valueOf(dto.getDelaiEnvisage().getCourant());
			} catch (IllegalArgumentException ex) {
				throw new EvaluationServiceException("La propriété 'delaiEnvisage' de l'évolution est incorrecte.");
			}
		}
		evolution.setDelaiEnvisage(delai);

		Integer selectedId = null;
		if (dto.getPourcentageTempsPartiel() != null && dto.getPourcentageTempsPartiel().getCourant() != null) {
			try {
				selectedId = Integer.parseInt(dto.getPourcentageTempsPartiel().getCourant());

				if (sirhWSConsumer.getSpbhorById(selectedId) == null)
					throw new EvaluationServiceException(
							"La propriété 'pourcentage temps partiel' de l'évolution est incorrecte.");
			} catch (SirhWSConsumerException e) {
				throw new EvaluationServiceException(
						"Erreur lors de l'appel à SIRH-WS.");
			} catch (NumberFormatException ex) {
				throw new EvaluationServiceException(
						"La propriété 'pourcentage temps partiel' de l'évolution est incorrecte.");
			}
		}
		evolution.setTempsPartielIdSpbhor(selectedId);

		// Inline properties
		evolution.setMobiliteGeo(dto.isMobiliteGeo());
		evolution.setMobiliteFonctionnelle(dto.isMobiliteFonctionnelle());
		evolution.setChangementMetier(dto.isChangementMetier());
		evolution.setMobiliteService(dto.isMobiliteService());
		evolution.setMobiliteDirection(dto.isMobiliteDirection());
		evolution.setMobiliteCollectivite(dto.isMobiliteCollectivite());
		evolution.setNomCollectivite(dto.getNomCollectivite());
		evolution.setMobiliteAutre(dto.isMobiliteAutre());
		evolution.setConcours(dto.isConcours());
		evolution.setNomConcours(dto.getNomConcours());
		evolution.setVae(dto.isVae());
		evolution.setNomVae(dto.getNomVae());
		evolution.setTempsPartiel(dto.isTempsPartiel());
		evolution.setRetraite(dto.isRetraite());
		evolution.setDateRetraite(dto.getDateRetraite());
		evolution.setAutrePerspective(dto.isAutrePerspective());
		evolution.setLibelleAutrePerspective(dto.getLibelleAutrePerspective());

		// Commentaires
		evolution.setCommentaireEvolution(updateEaeCommentaire(evolution.getCommentaireEvolution(),
				dto.getCommentaireEvolution()));
		evolution.setCommentaireEvaluateur(updateEaeCommentaire(evolution.getCommentaireEvaluateur(),
				dto.getCommentaireEvaluateur()));
		evolution.setCommentaireEvalue(updateEaeCommentaire(evolution.getCommentaireEvalue(),
				dto.getCommentaireEvalue()));

		// List of EvolutionSouhaits
		List<EaeEvolutionSouhait> listAllEvolutionSouhaits = new ArrayList<EaeEvolutionSouhait>(
				evolution.getEaeEvolutionSouhaits());

		for (EaeEvolutionSouhait evolSouhait : dto.getSouhaitsSuggestions()) {
			if (evolSouhait.getIdEaeEvolutionSouhait() == null || evolSouhait.getIdEaeEvolutionSouhait().equals(0)) {
				evolSouhait.setIdEaeEvolutionSouhait(null);
				evolution.getEaeEvolutionSouhaits().add(evolSouhait);
				evolSouhait.setEaeEvolution(evolution);
			} else {
				for (EaeEvolutionSouhait existingEvolSouhait : evolution.getEaeEvolutionSouhaits()) {
					if (existingEvolSouhait.getIdEaeEvolutionSouhait().equals(evolSouhait.getIdEaeEvolutionSouhait())) {
						existingEvolSouhait.setSouhait(evolSouhait.getSouhait());
						existingEvolSouhait.setSuggestion(evolSouhait.getSuggestion());
						listAllEvolutionSouhaits.remove(existingEvolSouhait);
					}
				}
			}
		}

		for (EaeEvolutionSouhait souhait : listAllEvolutionSouhaits) {
			evolution.getEaeEvolutionSouhaits().remove(souhait);
			eaeService.remove(souhait);
		}

		// List of Developpements
		List<EaeDeveloppement> listAllDeveloppements = new ArrayList<EaeDeveloppement>(evolution.getEaeDeveloppements());

		updateEaeDeveloppements(evolution, dto.getDeveloppementConnaissances(), EaeTypeDeveloppementEnum.CONNAISSANCE,
				listAllDeveloppements);
		updateEaeDeveloppements(evolution, dto.getDeveloppementCompetences(), EaeTypeDeveloppementEnum.COMPETENCE,
				listAllDeveloppements);
		updateEaeDeveloppements(evolution, dto.getDeveloppementExamensConcours(), EaeTypeDeveloppementEnum.CONCOURS,
				listAllDeveloppements);
		updateEaeDeveloppements(evolution, dto.getDeveloppementPersonnel(), EaeTypeDeveloppementEnum.PERSONNEL,
				listAllDeveloppements);
		updateEaeDeveloppements(evolution, dto.getDeveloppementComportement(), EaeTypeDeveloppementEnum.COMPORTEMENT,
				listAllDeveloppements);
		updateEaeDeveloppements(evolution, dto.getDeveloppementFormateur(), EaeTypeDeveloppementEnum.FORMATEUR,
				listAllDeveloppements);

		for (EaeDeveloppement dev : listAllDeveloppements) {
			evolution.getEaeDeveloppements().remove(dev);
			eaeService.remove(dev);
		}

		try {
			eaeDataConsistencyService.checkDataConsistencyForEaeEvolution(eae);
		} catch (EaeDataConsistencyServiceException e) {
			throw new EvaluationServiceException(e.getMessage(), e);
		}
	}

	protected void updateEaeDeveloppements(EaeEvolution evolution, List<EaeDeveloppement> dtoDeveloppements,
			EaeTypeDeveloppementEnum typeDeveloppement, List<EaeDeveloppement> listAllDeveloppements) {

		for (EaeDeveloppement dev : dtoDeveloppements) {
			if (dev.getIdEaeDeveloppement() == null || dev.getIdEaeDeveloppement().equals(0)) {
				dev.setIdEaeDeveloppement(null);
				dev.setEaeEvolution(evolution);
				dev.setTypeDeveloppement(typeDeveloppement);
				evolution.getEaeDeveloppements().add(dev);
			} else {
				for (EaeDeveloppement existingDev : evolution.getEaeDeveloppements()) {
					if (existingDev.getIdEaeDeveloppement()!=null && existingDev.getIdEaeDeveloppement().equals(dev.getIdEaeDeveloppement())) {
						existingDev.setLibelle(dev.getLibelle());
						existingDev.setEcheance(dev.getEcheance());
						existingDev.setPriorisation(dev.getPriorisation());
						listAllDeveloppements.remove(existingDev);
					}
				}
			}
		}
	}

	protected EaeCommentaire updateEaeCommentaire(EaeCommentaire oldCommentaire, EaeCommentaire newCommentaire) {

		if (newCommentaire == null)
			return null;

		if (oldCommentaire == null)
			return newCommentaire;

		oldCommentaire.setText(newCommentaire.getText());

		return oldCommentaire;
	}

}
