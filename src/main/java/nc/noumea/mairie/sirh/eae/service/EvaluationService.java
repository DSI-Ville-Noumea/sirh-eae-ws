package nc.noumea.mairie.sirh.eae.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import nc.noumea.mairie.mairie.domain.Spbhor;
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
import nc.noumea.mairie.sirh.eae.dto.EaeFichePosteDto;
import nc.noumea.mairie.sirh.eae.dto.EaeResultatsDto;
import nc.noumea.mairie.sirh.eae.dto.identification.EaeIdentificationDto;
import nc.noumea.mairie.sirh.eae.dto.planAction.EaePlanActionDto;
import nc.noumea.mairie.sirh.eae.dto.planAction.PlanActionItemDto;
import nc.noumea.mairie.sirh.eae.service.dataConsistency.EaeDataConsistencyServiceException;
import nc.noumea.mairie.sirh.eae.service.dataConsistency.IEaeDataConsistencyService;
import nc.noumea.mairie.sirh.service.IAgentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EvaluationService implements IEvaluationService {

	@Autowired
	private IAgentService agentService;
	
	@Autowired
	private ITypeObjectifService typeObjectifService;
	
	@Autowired
	private IEaeDataConsistencyService eaeDataConsistencyService;
	
	@PersistenceContext(unitName = "sirhPersistenceUnit")
	private EntityManager sirhEntityManager;
	
	@Override
	public EaeIdentificationDto getEaeIdentification(Eae eae) {
		
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
	public void setEaeIdentification(Eae eae, EaeIdentificationDto dto) throws EvaluationServiceException {
		eae.setDateEntretien(dto.getDateEntretien());
	}

	@Override
	public List<EaeFichePosteDto> getEaeFichePoste(Eae eae) {
		
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
	public EaeResultatsDto getEaeResultats(Eae eae) {
		
		if (eae == null)
			return null;
		
		return new EaeResultatsDto(eae);
	}

	@Override
	public void setEaeResultats(Eae eae, EaeResultatsDto dto) throws EvaluationServiceException {

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
		
		// Removes EaeResultats not present in the DTO (consider them as deleted)
		for(EaeResultat res : listOfAllResultats) {
			eae.getEaeResultats().remove(res);
			res.remove();
		}
		
		eae.flush();
	}

	private void createAndAddNewEaeResultat(Eae eae, EaeResultat resPro, EaeTypeObjectifEnum type) {
		resPro.setTypeObjectif(typeObjectifService.getTypeObjectifForLibelle(type.name()));
		eae.getEaeResultats().add(resPro);
		resPro.setEae(eae);
	}

	private EaeResultat fillExistingEaeResultat(Eae eae, EaeResultat resultat) {
		
		for(EaeResultat existingResultat : eae.getEaeResultats()) {
			
			if (existingResultat.getIdEaeResultat() != null && existingResultat.getIdEaeResultat().equals(resultat.getIdEaeResultat())) {
			
				existingResultat.setObjectif(resultat.getObjectif());
				existingResultat.setResultat(resultat.getResultat());
				
				resultat.setCommentaire(updateEaeCommentaire(existingResultat.getCommentaire(), (resultat.getCommentaire())));
				return existingResultat;
			}
		}
		
		return null;
	}

	@Override
	public EaeAppreciationsDto getEaeAppreciations(Eae eae) {
		
		if (eae == null)
			return null;
		
		return new EaeAppreciationsDto(eae);
	}

	@Override
	public void setEaeAppreciations(Eae eae, EaeAppreciationsDto dto) {

		eae.getEaeAppreciations().clear();
		eae.flush();
		
		fillAppreciationsWithArray(eae, dto.getTechniqueEvalue(), dto.getTechniqueEvaluateur(), EaeTypeAppreciationEnum.TE);
		fillAppreciationsWithArray(eae, dto.getSavoirEtreEvalue(), dto.getSavoirEtreEvaluateur(), EaeTypeAppreciationEnum.SE);
		fillAppreciationsWithArray(eae, dto.getManagerialEvalue(), dto.getManagerialEvaluateur(), EaeTypeAppreciationEnum.MA);
		fillAppreciationsWithArray(eae, dto.getResultatsEvalue(), dto.getResultatsEvaluateur(), EaeTypeAppreciationEnum.RE);

	}

	private void fillAppreciationsWithArray(Eae eae, String[] arrayOfAppreciationsEvalue, String[] arrayOfAppreciationsEvaluateur, EaeTypeAppreciationEnum appreciationType) {
		for (int i = 0; i < arrayOfAppreciationsEvalue.length ; i++) {
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
	public EaeEvaluationDto getEaeEvaluation(Eae eae) {
		
		if (eae == null)
			return null;
		
		return new EaeEvaluationDto(eae);
	}

	@Override
	public void setEaeEvaluation(Eae eae, EaeEvaluationDto dto) throws EvaluationServiceException {
		
		eae.setDureeEntretienMinutes(dto.getDureeEntretien());
		EaeEvaluation evaluation = eae.getEaeEvaluation();
		evaluation.setNoteAnnee(dto.getNoteAnnee());
		evaluation.setAvisChangementClasse(dto.getAvisChangementClasse());
		evaluation.setAvisRevalorisation(dto.getAvisRevalorisation());
		
		// Check the Niveau if it's valid and/or not set
		if (dto.getNiveau() == null || dto.getNiveau().getCourant() == null)
			throw new EvaluationServiceException("La propriété 'niveau' de l'évaluation est manquante.");
		
		EaeNiveauEnum selectedNiveau = null;

		if (dto.getNiveau() != null && dto.getNiveau().getCourant() != null) {
			try {
				selectedNiveau = EaeNiveauEnum.valueOf(dto.getNiveau().getCourant());
			} catch(IllegalArgumentException ex) {
				throw new EvaluationServiceException("La propriété 'niveau' de l'évaluation est incorrecte.");
			}
		}
		
		evaluation.setNiveauEae(selectedNiveau);
		
		
		// Check the propositionAvancement validity
		EaeAvancementEnum selectedAvancement = null;
		
		if (dto.getPropositionAvancement() != null && dto.getPropositionAvancement().getCourant() != null) {
			try {
				selectedAvancement = EaeAvancementEnum.valueOf(dto.getPropositionAvancement().getCourant());
			} catch(IllegalArgumentException ex) {
				throw new EvaluationServiceException("La propriété 'propositionAvancement' de l'évaluation est incorrecte.");
			}
		}
		
		evaluation.setPropositionAvancement(selectedAvancement);
		
		// Calculate Avis SHD based on what has been modified
		calculateAvisShd(eae);
		
		// For all the comments, check if a comment already exists and update it, or assign the new one
		evaluation.setCommentaireEvaluateur(updateEaeCommentaire(evaluation.getCommentaireEvaluateur(), dto.getCommentaireEvaluateur()));
		evaluation.setCommentaireEvalue(updateEaeCommentaire(evaluation.getCommentaireEvalue(), dto.getCommentaireEvalue()));
		evaluation.setCommentaireAvctEvaluateur(updateEaeCommentaire(evaluation.getCommentaireAvctEvaluateur(), dto.getCommentaireAvctEvaluateur()));
		evaluation.setCommentaireAvctEvalue(updateEaeCommentaire(evaluation.getCommentaireAvctEvalue(), dto.getCommentaireAvctEvalue()));
		
		try {
			eaeDataConsistencyService.checkDataConsistencyForEaeEvaluation(eae);
		} catch (EaeDataConsistencyServiceException e) {
			throw new EvaluationServiceException(e.getMessage(), e);
		}
	}
	
	protected void calculateAvisShd(Eae eae) {
		
		if (eae.getEaeEvalue().getTypeAvancement() == null)
			return;
		
		switch (eae.getEaeEvalue().getTypeAvancement()) {
			case AD:
				if (eae.getEaeEvaluation().getAvisChangementClasse() != null)
					eae.getEaeEvaluation().setAvisShd(EaeAvisEnum.fromBooleanToAvisEnum(eae.getEaeEvaluation().getAvisChangementClasse()).toString());
				break;
			case REVA:
				if (eae.getEaeEvaluation().getAvisRevalorisation() != null)
					eae.getEaeEvaluation().setAvisShd(EaeAvisEnum.fromBooleanToAvisEnum(eae.getEaeEvaluation().getAvisRevalorisation()).toString());
				break;
			case PROMO:
				if (eae.getEaeEvaluation().getPropositionAvancement() != null)
					eae.getEaeEvaluation().setAvisShd(eae.getEaeEvaluation().getPropositionAvancement().toString());
				break;
			default:
				break;
		}
	}

	@Override
	public EaeAutoEvaluationDto getEaeAutoEvaluation(Eae eae) {
		
		if (eae == null)
			return null;
		
		return new EaeAutoEvaluationDto(eae);
	}

	@Override
	public void setEaeAutoEvaluation(Eae eae, EaeAutoEvaluationDto dto) {
		
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
	public EaePlanActionDto getEaePlanAction(Eae eae) {
		
		if (eae == null)
			return null;
		
		return new EaePlanActionDto(eae);
	}

	@Override
	public void setEaePlanAction(Eae eae, EaePlanActionDto dto) {
		
		// Clear previous plan actions items
		eae.getEaePlanActions().clear();
		
		for(PlanActionItemDto item : dto.getObjectifsProfessionnels()) {
			CreateAndAddPlanAction(eae, item.getObjectif(), item.getIndicateur(), EaeTypeObjectifEnum.PROFESSIONNEL);
		}
		
		for(String s : dto.getObjectifsIndividuels()) {
			CreateAndAddPlanAction(eae, s, null, EaeTypeObjectifEnum.INDIVIDUEL);
		}
		
		for(String s : dto.getMoyensFinanciers()) {
			CreateAndAddPlanAction(eae, s, null, EaeTypeObjectifEnum.FINANCIERS);
		}
		
		for(String s : dto.getMoyensMateriels()) {
			CreateAndAddPlanAction(eae, s, null, EaeTypeObjectifEnum.MATERIELS);
		}
		
		for(String s : dto.getMoyensAutres()) {
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
	public EaeEvolutionDto getEaeEvolution(Eae eae) {
		
		if (eae == null)
			return null;
		
		List<Spbhor> listOfPartialTimes = sirhEntityManager.createNamedQuery("Spbhor.whereCdTauxNotZero", Spbhor.class).getResultList();

		return new EaeEvolutionDto(eae, listOfPartialTimes);
	}

	@Override
	public void setEaeEvolution(Eae eae, EaeEvolutionDto dto) throws EvaluationServiceException{

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
			} catch(IllegalArgumentException ex) {
				throw new EvaluationServiceException("La propriété 'delaiEnvisage' de l'évolution est incorrecte.");
			}
		}
		evolution.setDelaiEnvisage(delai);
		
		Integer selectedId = null;
		if (dto.getPourcentageTempsPartiel() != null && dto.getPourcentageTempsPartiel().getCourant() != null) {
			try {
				selectedId = Integer.parseInt(dto.getPourcentageTempsPartiel().getCourant());
				if (Spbhor.findSpbhor(selectedId) == null)
					throw new EvaluationServiceException("La propriété 'pourcentage temps partiel' de l'évolution est incorrecte.");
			} catch(NumberFormatException ex) {
				throw new EvaluationServiceException("La propriété 'pourcentage temps partiel' de l'évolution est incorrecte.");
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
		evolution.setCommentaireEvolution(updateEaeCommentaire(evolution.getCommentaireEvolution(), dto.getCommentaireEvolution()));
		evolution.setCommentaireEvaluateur(updateEaeCommentaire(evolution.getCommentaireEvaluateur(), dto.getCommentaireEvaluateur()));
		evolution.setCommentaireEvalue(updateEaeCommentaire(evolution.getCommentaireEvalue(), dto.getCommentaireEvalue()));
		
		// List of EvolutionSouhaits
		List<EaeEvolutionSouhait> listAllEvolutionSouhaits = new ArrayList<EaeEvolutionSouhait>(evolution.getEaeEvolutionSouhaits());
		
		for (EaeEvolutionSouhait evolSouhait : dto.getSouhaitsSuggestions()) {
			if (evolSouhait.getIdEaeEvolutionSouhait() == null || evolSouhait.getIdEaeEvolutionSouhait().equals(0))
			{	
				evolution.getEaeEvolutionSouhaits().add(evolSouhait);
				evolSouhait.setEaeEvolution(evolution);
			}
			else {
				for (EaeEvolutionSouhait existingEvolSouhait : evolution.getEaeEvolutionSouhaits()) {
					if (existingEvolSouhait.getIdEaeEvolutionSouhait() == evolSouhait.getIdEaeEvolutionSouhait()) {
						existingEvolSouhait.setSouhait(evolSouhait.getSouhait());
						existingEvolSouhait.setSuggestion(evolSouhait.getSuggestion());
						listAllEvolutionSouhaits.remove(existingEvolSouhait);
					}
				}
			}
		}
		
		for (EaeEvolutionSouhait souhait : listAllEvolutionSouhaits) {
			evolution.getEaeEvolutionSouhaits().remove(souhait);
			souhait.remove();
		}
		
		// List of Developpements
		List<EaeDeveloppement> listAllDeveloppements = new ArrayList<EaeDeveloppement>(evolution.getEaeDeveloppements());
		
		updateEaeDeveloppements(evolution, dto.getDeveloppementConnaissances(), EaeTypeDeveloppementEnum.CONNAISSANCE, listAllDeveloppements);
		updateEaeDeveloppements(evolution, dto.getDeveloppementCompetences(), EaeTypeDeveloppementEnum.COMPETENCE, listAllDeveloppements);
		updateEaeDeveloppements(evolution, dto.getDeveloppementExamensConcours(), EaeTypeDeveloppementEnum.CONCOURS, listAllDeveloppements);
		updateEaeDeveloppements(evolution, dto.getDeveloppementPersonnel(), EaeTypeDeveloppementEnum.PERSONNEL, listAllDeveloppements);
		updateEaeDeveloppements(evolution, dto.getDeveloppementComportement(), EaeTypeDeveloppementEnum.COMPORTEMENT, listAllDeveloppements);
		updateEaeDeveloppements(evolution, dto.getDeveloppementFormateur(), EaeTypeDeveloppementEnum.FORMATEUR, listAllDeveloppements);
		
		for (EaeDeveloppement dev : listAllDeveloppements) {
			evolution.getEaeDeveloppements().remove(dev);
			dev.remove();
		}
		
		try {
			eaeDataConsistencyService.checkDataConsistencyForEaeEvolution(eae);
		} catch (EaeDataConsistencyServiceException e) {
			throw new EvaluationServiceException(e.getMessage(), e);
		}
	}
	
	protected void updateEaeDeveloppements(EaeEvolution evolution, List<EaeDeveloppement> dtoDeveloppements, EaeTypeDeveloppementEnum typeDeveloppement, List<EaeDeveloppement> listAllDeveloppements) {
		
		for (EaeDeveloppement dev : dtoDeveloppements) {
			if (dev.getIdEaeDeveloppement() == null || dev.getIdEaeDeveloppement().equals(0)) {
				dev.setEaeEvolution(evolution);
				dev.setTypeDeveloppement(typeDeveloppement);
				evolution.getEaeDeveloppements().add(dev);
			}
			else {
				for (EaeDeveloppement existingDev : evolution.getEaeDeveloppements()) {
					if (existingDev.getIdEaeDeveloppement() == dev.getIdEaeDeveloppement()) {
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
