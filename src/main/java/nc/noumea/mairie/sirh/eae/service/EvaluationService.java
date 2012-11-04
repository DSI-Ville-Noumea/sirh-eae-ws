package nc.noumea.mairie.sirh.eae.service;

import java.util.ArrayList;
import java.util.List;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeAppreciation;
import nc.noumea.mairie.sirh.eae.domain.EaeAutoEvaluation;
import nc.noumea.mairie.sirh.eae.domain.EaeCommentaire;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluateur;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluation;
import nc.noumea.mairie.sirh.eae.domain.EaeFichePoste;
import nc.noumea.mairie.sirh.eae.domain.EaeNiveau;
import nc.noumea.mairie.sirh.eae.domain.EaePlanAction;
import nc.noumea.mairie.sirh.eae.domain.EaeResultat;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeAvancementEnum;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeAvisEnum;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeTypeAppreciationEnum;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeTypeObjectifEnum;
import nc.noumea.mairie.sirh.eae.dto.EaeAppreciationsDto;
import nc.noumea.mairie.sirh.eae.dto.EaeAutoEvaluationDto;
import nc.noumea.mairie.sirh.eae.dto.EaeEvaluationDto;
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
	
	@Override
	public EaeIdentificationDto getEaeIdentification(Eae eae) {
		
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
		
		List<EaeFichePosteDto> result = new ArrayList<EaeFichePosteDto>();
		
		for (EaeFichePoste fdp : eae.getEaeFichePostes()) {
			agentService.fillEaeFichePosteWithAgent(fdp);
			result.add(new EaeFichePosteDto(fdp));
		}
		
		return result;
	}

	@Override
	public EaeResultatsDto getEaeResultats(Eae eae) {
		return new EaeResultatsDto(eae);
	}

	@Override
	public void setEaeResultats(Eae eae, EaeResultatsDto dto) throws EvaluationServiceException {

		if (eae.getCommentaire() == null)
			eae.setCommentaire(new EaeCommentaire());
		
		eae.getCommentaire().setText(dto.getCommentaireGeneral());
		
		for (EaeResultat resPro : dto.getObjectifsProfessionnels()) {
			if (resPro.getIdEaeResultat() == null || resPro.getIdEaeResultat() == 0)
				createAndAddNewEaeResultat(eae, resPro, EaeTypeObjectifEnum.PROFESSIONNEL);
			else
				fillExistingEaeResultat(eae, resPro);
		}
		
		for (EaeResultat resInd : dto.getObjectifsIndividuels()) {
			if (resInd.getIdEaeResultat() == null || resInd.getIdEaeResultat() == 0)
				createAndAddNewEaeResultat(eae, resInd, EaeTypeObjectifEnum.INDIVIDUEL);
			else
				fillExistingEaeResultat(eae, resInd);
		}
		
		eae.flush();
	}

	private void createAndAddNewEaeResultat(Eae eae, EaeResultat resPro, EaeTypeObjectifEnum type) {
		resPro.setTypeObjectif(typeObjectifService.getTypeObjectifForLibelle(type.name()));
		eae.getEaeResultats().add(resPro);
		resPro.setEae(eae);
	}

	private void fillExistingEaeResultat(Eae eae, EaeResultat resultat) {
		for(EaeResultat existingResultat : eae.getEaeResultats()) {
			
			if (existingResultat.getIdEaeResultat().equals(resultat.getIdEaeResultat())) {
			
				existingResultat.setObjectif(resultat.getObjectif());
				existingResultat.setResultat(resultat.getResultat());
				
				if (resultat.getCommentaire() != null) {
					if (existingResultat.getCommentaire() != null)
						existingResultat.getCommentaire().setText(resultat.getCommentaire().getText());
					else
						existingResultat.setCommentaire(resultat.getCommentaire());
				}
				else if (existingResultat.getCommentaire() != null)
					existingResultat.setCommentaire(null);
			}
		}
	}

	@Override
	public EaeAppreciationsDto getEaeAppreciations(Eae eae) {
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
		return new EaeEvaluationDto(eae.getEaeEvaluation());
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
		
		EaeNiveau selectedNiveau = null;
		
		try {
			selectedNiveau = EaeNiveau.findEaeNiveau(Integer.parseInt(dto.getNiveau().getCourant()));
		} catch(NumberFormatException ex) {
			selectedNiveau = null;
		}
		
		if (selectedNiveau == null)
			throw new EvaluationServiceException("La propriété 'niveau' de l'évaluation est incorrecte.");
		
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
		if (evaluation.getCommentaireEvaluateur() == null)
			evaluation.setCommentaireEvaluateur(dto.getCommentaireEvaluateur());
		else if (dto.getCommentaireEvaluateur() != null)
			evaluation.getCommentaireEvaluateur().setText(dto.getCommentaireEvaluateur().getText());
		
		if (evaluation.getCommentaireEvalue() == null)
			evaluation.setCommentaireEvalue(dto.getCommentaireEvalue());
		else if (dto.getCommentaireEvalue() != null)
			evaluation.getCommentaireEvalue().setText(dto.getCommentaireEvalue().getText());
		
		if (evaluation.getCommentaireAvctEvaluateur() == null)
			evaluation.setCommentaireAvctEvaluateur(dto.getCommentaireAvctEvaluateur());
		else if (dto.getCommentaireAvctEvaluateur() != null)
			evaluation.getCommentaireAvctEvaluateur().setText(dto.getCommentaireAvctEvaluateur().getText());
		
		if (evaluation.getCommentaireAvctEvalue() == null)
			evaluation.setCommentaireAvctEvalue(dto.getCommentaireAvctEvalue());
		else if (dto.getCommentaireAvctEvalue() != null)
			evaluation.getCommentaireAvctEvalue().setText(dto.getCommentaireAvctEvalue().getText());
		
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
			case AVCT:
				if (eae.getEaeEvaluation().getPropositionAvancement() != null)
					eae.getEaeEvaluation().setAvisShd(eae.getEaeEvaluation().getPropositionAvancement().toString());
				break;
			default:
				break;
		}
	}

	@Override
	public EaeAutoEvaluationDto getEaeAutoEvaluation(Eae eae) {
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

}
