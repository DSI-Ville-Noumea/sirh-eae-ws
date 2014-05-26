package nc.noumea.mairie.sirh.eae.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import nc.noumea.mairie.sirh.domain.Agent;
import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeCampagne;
import nc.noumea.mairie.sirh.eae.domain.EaeDiplome;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluateur;
import nc.noumea.mairie.sirh.eae.domain.EaeEvalue;
import nc.noumea.mairie.sirh.eae.domain.EaeFdpActivite;
import nc.noumea.mairie.sirh.eae.domain.EaeFdpCompetence;
import nc.noumea.mairie.sirh.eae.domain.EaeFichePoste;
import nc.noumea.mairie.sirh.eae.domain.EaeFormation;
import nc.noumea.mairie.sirh.eae.domain.EaeParcoursPro;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeAgentPositionAdministrativeEnum;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeAgentStatutEnum;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeEtatEnum;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeTypeAvctEnum;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeTypeCompetenceEnum;
import nc.noumea.mairie.sirh.eae.domain.enums.EtatAvancementEnum;
import nc.noumea.mairie.sirh.eae.dto.AvancementEaeDto;
import nc.noumea.mairie.sirh.eae.dto.CalculEaeInfosDto;
import nc.noumea.mairie.sirh.eae.dto.agent.AutreAdministrationAgentDto;
import nc.noumea.mairie.sirh.eae.dto.agent.DiplomeDto;
import nc.noumea.mairie.sirh.eae.dto.agent.FormationDto;
import nc.noumea.mairie.sirh.eae.dto.agent.GradeDto;
import nc.noumea.mairie.sirh.eae.dto.agent.ParcoursProDto;
import nc.noumea.mairie.sirh.eae.dto.poste.FichePosteDto;
import nc.noumea.mairie.sirh.eae.dto.poste.TitrePosteDto;
import nc.noumea.mairie.sirh.eae.repository.IEaeRepository;
import nc.noumea.mairie.sirh.service.IAgentService;
import nc.noumea.mairie.sirh.tools.CalculEaeHelper;
import nc.noumea.mairie.sirh.ws.ISirhWsConsumer;
import nc.noumea.mairie.sirh.ws.SirhWSConsumerException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CalculEaeService implements ICalculEaeService {

	private Logger logger = LoggerFactory.getLogger(EaeService.class);
	
	@Autowired
	IEaeRepository eaeRepository;
	
	@Autowired
	ISirhWsConsumer sirhWsConsumer;
	
	@Autowired
	IAgentService agentService;
	

	public static final String CHAINE_VIDE = "";
	public static final String ZERO = "0";
	public static final String DATE_NULL = "01/01/0001";
	
	SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
	
	@Override
	public  void creerEaeAffecte(Integer idCampagneEae, Integer idAgent) throws SirhWSConsumerException, ParseException {
		
		// si on trouve un EAE dejà existant alors on ne fait rien
		Eae eae = eaeRepository.findEaeAgent(idAgent, idCampagneEae);
		if(null == eae) {
			logger.info("Création de l'EAE pour l'agent : " + idAgent);
			
			EaeCampagne eaeCampagne = eaeRepository.findEaeCampagne(idCampagneEae);
			
			// Création de l'EAE
			eae = new Eae();
			eae.setEaeCampagne(eaeCampagne);
			eae.setDocAttache(false);
			eae.setDateCreation(null);
	
			// pour le CAP
			// on cherche si il y a une ligne dans les avancements
			// logger.info("Req AS400 : chercherAvancementAvecAnneeEtAgent");
			AvancementEaeDto avct = sirhWsConsumer.getAvancement(idAgent, eaeCampagne.getAnnee(), true);
			if (avct != null && null != avct.getEtat() && avct.getEtat().equals(AvancementEaeDto.SGC)) {
				// on a trouvé une ligne dans avancement, on regarde l'etat de la ligne
				// si 'valid DRH' alors on met CAP à true;
				eae.setCap(true);
			} else {
				eae.setCap(false);
			}
	
			eae.setEtat(EaeEtatEnum.NA);
			
			Agent agent = agentService.getAgent(idAgent);
			CalculEaeInfosDto affAgent = sirhWsConsumer.getDetailAffectationActiveByAgent(idAgent, eaeCampagne.getAnnee()-1);
			// on met les données dans EAE-evalue
			creerEvalue(agent, eae, affAgent, true, true);
			// on met les données dans EAE-Diplome
			creerDiplome(eae, affAgent.getListDiplome());
			// on met les données dans EAE-Parcours-Pro
			creerParcoursPro(agent, eae, affAgent.getListParcoursPro());
			// on met les données dans EAE-Formation
			creerFormation(eae, affAgent.getListFormation());
			
			eaeRepository.persistEntity(eae);
		}else{
			logger.info("EAE deja existant pour l'agent : " + idAgent);
		}
	}
	
	@Override
	public void creerEaeSansAffecte(Integer idCampagneEae, Integer idAgent) throws SirhWSConsumerException, ParseException {
		
		// si on trouve un EAE dejà existant alors on ne fait rien
		Eae eae = eaeRepository.findEaeAgent(idAgent, idCampagneEae);
		if(null != eae) {
			logger.info("EAE deja existant pour l'agent : " + idAgent);
			return;
		}
		
		logger.info("Création de l'EAE pour l'agent : " + idAgent);
		
		EaeCampagne eaeCampagne = eaeRepository.findEaeCampagne(idCampagneEae);
		
		// Création de l'EAE
		eae = new Eae();
			eae.setEaeCampagne(eaeCampagne);
			eae.setDocAttache(false);
			eae.setDateCreation(null);

		// pour le CAP
		// on cherche si il y a une ligne dans les avancements
		AvancementEaeDto avct = sirhWsConsumer.getAvancement(idAgent, eaeCampagne.getAnnee(), true);
		if (avct != null && null != avct.getEtat() && avct.getEtat().equals(AvancementEaeDto.SGC)) {
			// on a trouvé une ligne dans avancement
			// on regarde l'etat de la ligne
			// si 'valid DRH' alors on met CAP à true;
				eae.setCap(true);
		} else {
			eae.setCap(false);
		}

		eae.setEtat(EaeEtatEnum.ND);

		// on recupere le poste
		CalculEaeInfosDto affAgent = sirhWsConsumer.getDetailAffectationActiveByAgent(idAgent, eaeCampagne.getAnnee()-1);
		FichePosteDto fpPrincipale = null;
		FichePosteDto fpSecondaire = null;
		FichePosteDto fpResponsable = null;
		TitrePosteDto tpResp = null;
		fpPrincipale = affAgent.getFichePostePrincipale();
		
		// on recupere le superieur hierarchique
		if (affAgent.getFichePosteResponsable() != null) {
			fpResponsable = affAgent.getFichePosteResponsable();
			tpResp = affAgent.getFichePosteResponsable().getTitrePoste();
		}
		
		if (affAgent.getFichePosteSecondaire() != null) {
			fpSecondaire = affAgent.getFichePosteSecondaire();
		}
		
		Agent agent = agentService.getAgent(idAgent);
		// on met les données dans EAE-evalué
		creerEvalue(agent, eae, affAgent, true, false);
		// on met les données dans EAE-FichePoste, EAE-FDP-Activites et EAE_FDP_COMPETENCE
		// on cree la fiche de poste primaire
		creerFichePoste(fpPrincipale, eae, fpResponsable, tpResp, true, true);
		// on cree la fiche de poste secondaire
		creerFichePoste(fpSecondaire, eae, fpResponsable, tpResp, true, false);
		// on met les données dans EAE-Diplome
		creerDiplome(eae, affAgent.getListDiplome());
		// on met les données dans EAE-Parcours-Pro
		creerParcoursPro(agent, eae, affAgent.getListParcoursPro());
		// on met les données dans EAE-Formation
		creerFormation(eae, affAgent.getListFormation());

		// on met à jour l'etat de l'EAE
		EaeFichePoste eaeFDP = eae.getPrimaryFichePoste();
		if (null != eaeFDP && (eaeFDP.getIdAgentShd() == null || eaeFDP.getIdAgentShd() == 0)) {
			eae.setEtat(EaeEtatEnum.NA);
		}

		creerEvaluateur(eaeFDP, eae, tpResp, fpResponsable);

		eaeRepository.persistEntity(eae);
	}
	
	
	public void creerEvalue(Agent agent, Eae eae, CalculEaeInfosDto eaeInfosDto, boolean miseAjourDateAdministration, boolean agentAffecte) 
			throws SirhWSConsumerException, ParseException {
		
		// cas de la modif
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		EaeEvalue evalAModif = new EaeEvalue();

		evalAModif.setEae(eae);
		evalAModif.setIdAgent(agent.getIdAgent());

		if (eaeInfosDto != null && eaeInfosDto.getFichePostePrincipale() != null) {
			evalAModif.setDateEntreeService(
					getDateEntreeService(agent.getIdAgent(), eaeInfosDto.getFichePostePrincipale().getCodeService()));
		}

		evalAModif.setDateEntreeCollectivite(agent.getDateDerniereEmbauche());
		// on cherche la date la plus ancienne dans les carrieres pour le statut fonctionnaire
		Date dateCarriere = null;
		if (null != eaeInfosDto.getCarriereFonctionnaireAncienne()) {
			dateCarriere = eaeInfosDto.getCarriereFonctionnaireAncienne().getDateDebut();
		}
		// idem dans la liste des autres administration
		AutreAdministrationAgentDto autreAdmin = sirhWsConsumer.chercherAutreAdministrationAgentAncienne(agent.getIdAgent(), true);
		Date dateAutreAdmin = null;
		if (autreAdmin != null) {
			dateAutreAdmin = autreAdmin.getDateEntree();
		}

		evalAModif.setDateEntreeFonctionnaire(CalculEaeHelper.getDateAnterieure(dateAutreAdmin, dateCarriere));
		
		// on regarde si la date de l'EAE precedent est différente alors on
		// prend la date de l'EAE de l'année passée
		EaeCampagne campagnePrec = eaeRepository.findEaeCampagneByAnnee(eae.getEaeCampagne().getAnnee() - 1);
		Eae eaeAnneePrec = eaeRepository.findEaeAgent(agent.getIdAgent(), campagnePrec.getIdCampagneEae());
		// si on ne trouve pas d'EAE pour l'année precedente
		EaeEvalue ancienneValeur = null;
		if (eaeAnneePrec != null) {
			ancienneValeur =  eaeAnneePrec.getEaeEvalue();
			if (evalAModif.getDateEntreeFonctionnaire() != null) {
				if (ancienneValeur.getDateEntreeFonctionnaire() != null
						&& (evalAModif.getDateEntreeFonctionnaire().compareTo(
								ancienneValeur.getDateEntreeFonctionnaire()) != 0)) {
					evalAModif.setDateEntreeFonctionnaire(ancienneValeur.getDateEntreeFonctionnaire());
				}
			} else {
				if (ancienneValeur.getDateEntreeFonctionnaire() != null) {
					evalAModif.setDateEntreeFonctionnaire(ancienneValeur.getDateEntreeFonctionnaire());
				}
			}
		}

		if (miseAjourDateAdministration) {
			// on cherche la date la plus ancienne dans les PA de
			// mairie.SPADMN
			Date dateSpadmnAncienne = null;
			if (eaeInfosDto.getPositionAdmAgentAncienne() != null && eaeInfosDto.getPositionAdmAgentAncienne().getDatdeb() != null) {
				dateSpadmnAncienne = sdf.parse(eaeInfosDto.getPositionAdmAgentAncienne().getDatdeb().toString());
			}
			// idem dans la liste des autres administration
			AutreAdministrationAgentDto autreAdminAncienne = sirhWsConsumer.chercherAutreAdministrationAgentAncienne(agent.getIdAgent(), false);
			Date dateAutreAdminAncienne = null;
			if (autreAdminAncienne != null) {
				dateAutreAdminAncienne = autreAdminAncienne.getDateEntree();
			}

			evalAModif.setDateEntreeAdministration(CalculEaeHelper.getDateAnterieure(dateAutreAdminAncienne, dateSpadmnAncienne));
		}

		// on regarde si la date de l'EAE precedent est différente alors on
		// prend la date de l'EAE de l'année passée
		if (ancienneValeur != null) {
			if (evalAModif.getDateEntreeAdministration() != null) {
				if (ancienneValeur.getDateEntreeAdministration() != null
						&& (evalAModif.getDateEntreeAdministration().compareTo(
								ancienneValeur.getDateEntreeAdministration()) != 0)) {
					evalAModif.setDateEntreeAdministration(ancienneValeur.getDateEntreeAdministration());
				}
			} else {
				if (ancienneValeur.getDateEntreeAdministration() != null) {
					evalAModif.setDateEntreeAdministration(ancienneValeur.getDateEntreeAdministration());
				}
			}
		}

		setCarriereActive(eaeInfosDto, evalAModif, eae);

		// on regarde dans l'avancement pour le nouveau grade, le nouvel echelon et la date d'avancement
		AvancementEaeDto avctFonct = sirhWsConsumer.getAvancement(agent.getIdAgent(), eae.getEaeCampagne().getAnnee(), false);
		if (null == avctFonct) {
			// sinon, on cherche dans les détachés
			AvancementEaeDto avctDetache = sirhWsConsumer.getAvancementDetache(agent.getIdAgent(), eae.getEaeCampagne().getAnnee());
			if (null != avctDetache){
				setAvancementEvalue(evalAModif, avctDetache);
			}
		} else {
			setAvancementEvalue(evalAModif, avctFonct);
		}

		// pour la PA
		if (null != eaeInfosDto.getPositionAdmAgentEnCours()) {
			evalAModif.setPosition(EaeAgentPositionAdministrativeEnum.valueOf(CalculEaeHelper.getPositionAdmEAE(eaeInfosDto.getPositionAdmAgentEnCours().getCdpadm())));
		}

		evalAModif.setEstDetache(agentAffecte);
		
		eae.setEaeEvalue(evalAModif);
//		eaeRepository.persistEntity(evalAModif);
	}
	
	public void setCarriereActive(CalculEaeInfosDto eaeInfosDto, EaeEvalue evalAModif, Eae eae) {
		
 		if (null != eaeInfosDto.getCarriereActive()) {
			evalAModif.setStatut(EaeAgentStatutEnum.valueOf(CalculEaeHelper.getStatutCarriereEAE(eaeInfosDto.getCarriereActive().getCodeCategorie().toString())));
			if (EaeAgentStatutEnum.A.equals(evalAModif.getStatut())) {
				evalAModif.setStatutPrecision(eaeInfosDto.getCarriereActive().getLibelleCategorie());
			}
			if (EaeAgentStatutEnum.F.equals(evalAModif.getStatut())) {
				// pour le cadre
				evalAModif.setCadre(eaeInfosDto.getCarriereActive().getLibelleCategorie());
				
				// pour la categorie
				GradeDto grade = eaeInfosDto.getCarriereActive().getGrade();
				if (null != grade){
					
					evalAModif.setAvctDureeMin(grade.getDureeMinimum().equals(ZERO) ? null : grade.getDureeMinimum());
					evalAModif.setAvctDureeMoy(grade.getDureeMoyenne().equals(ZERO) ? null : grade.getDureeMoyenne());
					evalAModif.setAvctDureeMax(grade.getDureeMaximum().equals(ZERO) ? null : grade.getDureeMaximum());

					// pour le grade on cherche la classe si elle existe
					String classeString = CHAINE_VIDE;
					if(null != grade.getGradeInitial()) {
						classeString = grade.getGradeInitial().trim();
					}
					if (null != grade.getLibelleClasse()) {
						classeString += " " + grade.getLibelleClasse().trim();
					}
					
					evalAModif.setGrade(classeString);
					evalAModif.setCategorie(grade.getCodeGradeGenerique());
					
					// pour l'echelon
					evalAModif.setEchelon(grade.getLibelleEchelon());
				}
			} 
			if (EaeAgentStatutEnum.CC.equals(evalAModif.getStatut())) {
				// pour la classification
				if(null != eaeInfosDto.getCarriereActive().getGrade()) {
					evalAModif.setClassification(eaeInfosDto.getCarriereActive().getGrade().getLibelleGrade());
				}
			}
			
			// pour l'anciennete on met le resultat en nb de jours
			if (null != eaeInfosDto.getCarriereActive().getDateDebut()) {
				Calendar cal = Calendar.getInstance();
					cal.set(eae.getEaeCampagne().getAnnee()-1, 11, 31);
				
				int nbJours = CalculEaeHelper.compteJoursEntreDates(eaeInfosDto.getCarriereActive().getDateDebut(), cal.getTime());
				evalAModif.setAncienneteEchelonJours(nbJours > 0 ? nbJours - 1 : 0);
			}
		}
	}
	
	public void setAvancementEvalue(EaeEvalue evalAModif, AvancementEaeDto avct) {
		
		if (!EtatAvancementEnum.TRAVAIL.getValue().equals(avct.getEtat())) {
			// attention dans le cas des categorie 4 on a pas de date moyenne avct
			evalAModif.setDateEffetAvancement(avct.getDateAvctMoy());
		}
		GradeDto gradeAvct = avct.getGrade();
		if (null != gradeAvct){
			// on cherche la classe si elle existe
			String classeString = CHAINE_VIDE;
			if (null != gradeAvct.getLibelleClasse()) {
				classeString = gradeAvct.getLibelleClasse();
			}
			evalAModif.setNouvGrade(gradeAvct.getGradeInitial() + " " + classeString);
			if (null != gradeAvct.getCodeMotifAvancement()) {
				evalAModif.setTypeAvancement(EaeTypeAvctEnum.valueOf(gradeAvct.getCodeMotifAvancement()));
			}
			if(null != gradeAvct.getLibelleEchelon()) {
				evalAModif.setNouvEchelon(gradeAvct.getLibelleEchelon());
			}
		}
	}
	
	public Date getDateEntreeService(Integer idAgent, String codeService) throws SirhWSConsumerException {
		// on cherche toutes les affectations sur le meme service et on prend la date la plus ancienne
		// NB : pour les affectations successives
		List<CalculEaeInfosDto> listeAffectationService = sirhWsConsumer.getListeAffectationsAgentAvecService(idAgent, codeService);
		
		Date dateDebutService = null;
		if(null != listeAffectationService) {
			for (int i = 0; i < listeAffectationService.size(); i++) {
				CalculEaeInfosDto affCours = listeAffectationService.get(i);
				
				if (listeAffectationService.size() > i + 1
					&& listeAffectationService.get(i + 1) != null) {
					CalculEaeInfosDto affPrecedente = listeAffectationService.get(i + 1);
					
					Calendar cal = Calendar.getInstance();
						cal.setTime(affPrecedente.getDateFin());
						cal.add(Calendar.DAY_OF_YEAR, 1);
					
					if (affCours.getDateDebut().equals(cal.getTime())) {
						dateDebutService = affPrecedente.getDateDebut();
					} else {
						dateDebutService = affCours.getDateDebut();
						break;
					}
				}else{
					dateDebutService = affCours.getDateDebut();
					break;
				}
			}
		}
		
		return dateDebutService;
	}
	
	public void creerEvaluateur(EaeFichePoste eaeFDP, Eae eae, TitrePosteDto tpResp, FichePosteDto fpResponsable) throws SirhWSConsumerException {
		
		eae.getEaeEvaluateurs().clear();
		// on créer les evaluateurs
		if(null != eaeFDP && eaeFDP.getIdAgentShd() != null && eaeFDP.getIdAgentShd() != 0 && tpResp != null) {
			// logger.info("Req AS400 : chercherAgent (evaluateur)");
			Agent agentResp = agentService.getAgent(eaeFDP.getIdAgentShd());
			EaeEvaluateur eval = new EaeEvaluateur();
				eval.setEae(eae);
				eval.setIdAgent(Integer.valueOf(agentResp.getIdAgent()));
				eval.setFonction(tpResp.getLibTitrePoste());
				eval.setDateEntreeCollectivite(agentResp.getDateDerniereEmbauche());
			// on cherche toutes les affectations sur la FDP du responsable
			// on prend la date la plus ancienne
			if (fpResponsable != null && fpResponsable.getCodeService() != null) {
				eval.setDateEntreeFonction(getDateEntreeAffectation(fpResponsable.getIdFichePoste(), agentResp.getIdAgent()));
				eval.setDateEntreeService(getDateEntreeService(agentResp.getIdAgent(), fpResponsable.getCodeService()));
			}
			eae.getEaeEvaluateurs().add(eval);
//			eaeRepository.persistEntity(eval);
		}
	}
	
	public Date getDateEntreeAffectation(Integer idFichePoste, Integer idAgent) throws SirhWSConsumerException {
		
		List<CalculEaeInfosDto> listeAffectationSurMemeFDP = sirhWsConsumer.getListeAffectationsAgentAvecFP(idAgent, idFichePoste );
		
		if(null != listeAffectationSurMemeFDP && !listeAffectationSurMemeFDP.isEmpty()) {
			return listeAffectationSurMemeFDP.get(0).getDateDebut();
		}
		return null;
	}
	
	public void creerFichePoste(FichePosteDto fichePoste, Eae eae, FichePosteDto fpResp, TitrePosteDto tpResp, boolean modifDateFonction, boolean isFPPrimaire) throws SirhWSConsumerException {

		// on traite la fiche de poste
		if (fichePoste != null) {
			
			EaeFichePoste eaeFichePoste = null;
			if(isFPPrimaire) {
				eaeFichePoste = eae.getPrimaryFichePoste();
			}else{
				eaeFichePoste = eae.getSecondaryFichePoste();
			}
			
			if(null == eaeFichePoste) {
				eaeFichePoste = new EaeFichePoste();
			}
			
			eaeFichePoste.setEae(eae);
			eaeFichePoste.setIdSirhFichePoste(fichePoste.getIdFichePoste());
			eaeFichePoste.setPrimary(isFPPrimaire);
			eaeFichePoste.setCodeService(fichePoste.getCodeService());
			eaeFichePoste.setDirectionService(fichePoste.getDirection());
			eaeFichePoste.setService(fichePoste.getService());
			eaeFichePoste.setSectionService(fichePoste.getSection());
			// pour l'emploi
			if(isFPPrimaire) {
				eaeFichePoste.setEmploi(fichePoste.getEmploiPrimaire());
			}else{
				eaeFichePoste.setEmploi(fichePoste.getEmploiSecondaire());
			}
			
			// pour la fonction
			if(null != fichePoste.getTitrePoste()) {
				eaeFichePoste.setFonction(fichePoste.getTitrePoste().getLibTitrePoste());
			}
			if (modifDateFonction && null != eae.getEaeEvalue()) {
				eaeFichePoste.setDateEntreeFonction(getDateEntreeAffectation(fichePoste.getIdFichePoste(), eae.getEaeEvalue().getIdAgent()));
			}
			// grade du poste
			eaeFichePoste.setGradePoste(fichePoste.getGradePoste());
			eaeFichePoste.setLocalisation(fichePoste.getLieu());
			eaeFichePoste.setMissions(fichePoste.getMissions());
			
			if (null != fpResp) {
				Agent agentResp = agentService.getAgent(fpResp.getIdAgent());
				
				eaeFichePoste.setFonctionResponsable(tpResp.getLibTitrePoste());
				
				if (null != agentResp) {
					eaeFichePoste.setAgentShd(agentResp);
					eaeFichePoste.setIdAgentShd(agentResp.getIdAgent());
					eaeFichePoste.setDateEntreeCollectiviteResponsable(agentResp.getDateDerniereEmbauche());
				}

				// on cherche toutes les affectations sur la FDP du responsable
				// on prend la date la plus ancienne
				if (null != fpResp && null != agentResp) {
					eaeFichePoste.setDateEntreeFonctionResponsable(getDateEntreeAffectation(fpResp.getIdFichePoste(), agentResp.getIdAgent()));
					eaeFichePoste.setDateEntreeServiceResponsable(getDateEntreeService(agentResp.getIdAgent(), fpResp.getCodeService()));
				}
			}

			creerActivitesFichePoste(fichePoste, eaeFichePoste);
			creerCompetencesFichePoste(fichePoste, eaeFichePoste);
			
//			eaeRepository.persistEntity(eaeFichePoste);
			eae.addEaeFichePoste(eaeFichePoste);
		}
	}
	
	public void creerActivitesFichePoste(FichePosteDto fichePoste, EaeFichePoste eaeFichePoste) {
		
		eaeFichePoste.getEaeFdpActivites().clear();
		// gere les activites
		if(null != fichePoste.getActivites()){
			for (String activite : fichePoste.getActivites()) {
				EaeFdpActivite acti = new EaeFdpActivite();
					acti.setEaeFichePoste(eaeFichePoste);
					acti.setLibelle(activite);
//				eaeRepository.persistEntity(acti);
				eaeFichePoste.getEaeFdpActivites().add(acti);
			}
		}
	}
	
	public void creerCompetencesFichePoste(FichePosteDto fichePoste, EaeFichePoste eaeFichePoste) {
		
		eaeFichePoste.getEaeFdpCompetences().clear();
		
		creerCompetencesSpecifiquesFichePoste(fichePoste.getComportementsProfessionnels(), eaeFichePoste, EaeTypeCompetenceEnum.CP);
		creerCompetencesSpecifiquesFichePoste(fichePoste.getSavoirs(), eaeFichePoste, EaeTypeCompetenceEnum.SA);
		creerCompetencesSpecifiquesFichePoste(fichePoste.getSavoirsFaire(), eaeFichePoste, EaeTypeCompetenceEnum.SF);
	}
	
	public void creerCompetencesSpecifiquesFichePoste(List<String> listCompetence, EaeFichePoste eaeFichePoste, EaeTypeCompetenceEnum type) {
		
		if(null != listCompetence) {
			for (String libelleCompetence : listCompetence) {
				EaeFdpCompetence compEAE = new EaeFdpCompetence();
					compEAE.setEaeFichePoste(eaeFichePoste);
					compEAE.setType(type);
					compEAE.setLibelle(libelleCompetence);
//				eaeRepository.persistEntity(compEAE);
				eaeFichePoste.getEaeFdpCompetences().add(compEAE);
			}
		}
	}
	
	public void creerDiplome(Eae eae, List<DiplomeDto> listDiplomesAgent) {
		
		eae.getEaeDiplomes().clear();
		if(null != listDiplomesAgent) {
			for (DiplomeDto diplomeDto : listDiplomesAgent) {
				EaeDiplome eaeDiplome = new EaeDiplome();
					eaeDiplome.setEae(eae);
				String anneeObtention = CHAINE_VIDE;
				if (null != diplomeDto.getDateObtention()) {
					anneeObtention = sdfYear.format(diplomeDto.getDateObtention()).toString();
				}
				eaeDiplome.setLibelleDiplome((anneeObtention.equals(CHAINE_VIDE) ? CHAINE_VIDE : anneeObtention
						+ " : ")
						+ diplomeDto.getLibTitreDiplome() + " " + diplomeDto.getLibSpeDiplome());
				
//				eaeRepository.persistEntity(eaeDiplome);
				eae.getEaeDiplomes().add(eaeDiplome);
			}
		}
	}
	
	public void creerParcoursPro(Agent agent, Eae eae, List<ParcoursProDto> listParcoursPro) throws SirhWSConsumerException {
		
		// on nettoie la liste des parcours pro
		eae.getEaeParcoursPros().clear();
		
		if(null != listParcoursPro) {
			for (int i=0; i < listParcoursPro.size(); i++) {
	
				ParcoursProDto ppDto = listParcoursPro.get(i);
				EaeParcoursPro parcours = new EaeParcoursPro();
					parcours.setEae(eae);
					parcours.setDateDebut(ppDto.getDateDebut());
					parcours.setLibelleParcoursPro(ppDto.getDirection() + " " + ppDto.getService());
				
				if (null != ppDto.getDateFin()) {
					for(int j=i; j < listParcoursPro.size(); j++) {
					// la liste de parcours pro est trie par date de debut croissante du cote de SIRH-WS 
						if(j+1 < listParcoursPro.size()
							&& null != listParcoursPro.get(j+1).getDateDebut()
							&& null != listParcoursPro.get(j).getDateFin()
							&& ppDto.getService().equals(listParcoursPro.get(j+1).getService())) {

							Calendar cal = Calendar.getInstance();
								cal.setTime(listParcoursPro.get(j).getDateFin());
								cal.add(Calendar.DAY_OF_YEAR, 1);
							
							if(listParcoursPro.get(j+1).getDateDebut().equals(cal.getTime())) {
								parcours.setDateFin(listParcoursPro.get(j+1).getDateFin());
								i=j+1;
							} else {
								parcours.setDateFin(listParcoursPro.get(j).getDateFin());
								break;
							}
						} else {
							parcours.setDateFin(listParcoursPro.get(j).getDateFin());
							break;
						}
					}
				}
				
				eae.getEaeParcoursPros().add(parcours);
			}
		}
		
		creerParcoursProAvecAutreAdministration(agent, eae);
	}
	
	public void creerParcoursProAvecAutreAdministration(Agent agent, Eae eae) throws SirhWSConsumerException {
		// sur autre administration
		List<AutreAdministrationAgentDto> listAutreAdmin = sirhWsConsumer.getListeAutreAdministrationAgent(agent.getIdAgent());
		
		if(null != listAutreAdmin) {
			for (int i = 0; i < listAutreAdmin.size(); i++) {
				AutreAdministrationAgentDto admAgent = listAutreAdmin.get(i);
	
				// mise en place de cette verification, car probleme d index unique rencontre
				boolean isParcoursExistant = false;
				for(EaeParcoursPro parcoursDejaCree : eae.getEaeParcoursPros()) {
					if(parcoursDejaCree.getDateDebut().equals(admAgent.getDateEntree())) {
						isParcoursExistant = true;
						break;
					}
				}
				
				if(!isParcoursExistant) {
					EaeParcoursPro parcours = new EaeParcoursPro();
						parcours.setEae(eae);
						parcours.setDateDebut(admAgent.getDateEntree());
						parcours.setLibelleParcoursPro(admAgent.getLibelleAdministration());
					
					if (null != admAgent.getDateSortie()) {
						for(int j=i; j < listAutreAdmin.size(); j++) {
						// la liste de parcours pro est trie par date de debut croissante du cote de SIRH-WS 
							if(j+1 < listAutreAdmin.size()
								&& null != listAutreAdmin.get(j+1).getDateEntree()
								&& null != listAutreAdmin.get(j).getDateSortie()
								&& admAgent.getLibelleAdministration().equals(listAutreAdmin.get(j+1).getLibelleAdministration())) {
		
								Calendar cal = Calendar.getInstance();
									cal.setTime(listAutreAdmin.get(j).getDateSortie());
									cal.add(Calendar.DAY_OF_YEAR, 1);
								
								if(listAutreAdmin.get(j+1).getDateEntree().equals(cal.getTime())) {
									parcours.setDateFin(listAutreAdmin.get(j+1).getDateSortie());
									i=j+1;
								} else {
									break;
								}
							} else {
								break;
							}
						}
					} else {
						parcours.setDateFin(admAgent.getDateSortie());
					}
					
	//				eaeRepository.persistEntity(parcours);
					eae.getEaeParcoursPros().add(parcours);
				}
			}
		}
	}
	
	public void creerFormation(Eae eae, List<FormationDto> listFormation) {
		
		eae.getEaeFormations().clear();
		if(null != listFormation) {
			for (int i = 0; i < listFormation.size(); i++) {
				FormationDto formation = listFormation.get(i);
				EaeFormation form = new EaeFormation();
					form.setEae(eae);
					form.setAnneeFormation(formation.getAnneeFormation());
					form.setDureeFormation(formation.getDureeFormation().toString() + " " + formation.getUniteDuree());
					form.setLibelleFormation(formation.getTitreFormation() + " - " + formation.getCentreFormation());
//				eaeRepository.persistEntity(form);
				eae.getEaeFormations().add(form);
			}
		}
	}
}
