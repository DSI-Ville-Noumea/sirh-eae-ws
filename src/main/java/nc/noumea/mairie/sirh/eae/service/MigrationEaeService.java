package nc.noumea.mairie.sirh.eae.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import nc.noumea.mairie.sirh.domain.Agent;
import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeAppreciation;
import nc.noumea.mairie.sirh.eae.domain.EaeDeveloppement;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluateur;
import nc.noumea.mairie.sirh.eae.domain.EaeEvolution;
import nc.noumea.mairie.sirh.eae.domain.EaeEvolutionSouhait;
import nc.noumea.mairie.sirh.eae.domain.EaePlanAction;
import nc.noumea.mairie.sirh.eae.domain.EaeResultat;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeAvancementEnum;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeNiveauEnum;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeTypeAppreciationEnum;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeTypeDeveloppementEnum;
import nc.noumea.mairie.sirh.eae.dto.poste.FichePosteDto;
import nc.noumea.mairie.sirh.eae.dto.poste.SpbhorDto;
import nc.noumea.mairie.sirh.eae.repository.IEaeRepository;
import nc.noumea.mairie.sirh.ws.SirhWSConsumer;
import nc.noumea.mairie.sirh.ws.SirhWSConsumerException;

@Service
public class MigrationEaeService implements IMigrationEaeService {

	private Logger logger = LoggerFactory.getLogger(MigrationEaeService.class);

	@Autowired
	private IEaeRepository eaeRepository;
	
	@Autowired
	private SirhWSConsumer sirhWsConsumer;
	
	private Integer cellNum = 0;
	
	private List<EaeDeveloppement> formateurs = Lists.newArrayList();
	
	private DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	@Override
	@Transactional(readOnly = true)
	public void exportEAEForMigration() throws IOException, SirhWSConsumerException {

		Workbook workbook = new XSSFWorkbook();
	    Sheet sheet = workbook.createSheet("EAE");

	    // Create a Row
	    Row headerRow = sheet.createRow(0);
	    int i = 0;
	    for (String h : getHeaders()) {
	      Cell cell = headerRow.createCell(i++);
	      cell.setCellValue(h);
	    }
	    
	    List<String> listIdsActiveAgent = eaeRepository.getActiveAgentFromTiarhe();

	    int rowNum = 1;
	    for (Eae eae : eaeRepository.findAllForMigration(listIdsActiveAgent)) {
	    	cellNum = 0;
			Row row = sheet.createRow(rowNum++);
			FichePosteDto fp = sirhWsConsumer.getFichePoste(eae.getEaeEvalue().getIdAgent());
			Agent agent = sirhWsConsumer.getAgent(eae.getEaeEvalue().getIdAgent());
			Agent manager = getManager(eae);
			
			logger.info("Write row number {}, for eae {}.", rowNum, eae.getIdEae());

			row.createCell(cellNum++).setCellValue(eae.getIdEae()); // TODO : Supprimer cette ligne après les tests
			row.createCell(cellNum++).setCellValue(notNull(agent.getIdTiarhe()));
			row.createCell(cellNum++).setCellValue(eae.getEaeCampagne().getAnnee() - 1);
			row.createCell(cellNum++).setCellValue(eae.getDateEntretien() == null ? "" : sdf.format(eae.getDateEntretien()));
			row.createCell(cellNum++).setCellValue("18");
			row.createCell(cellNum++).setCellValue(manager == null ? "" : notNull(manager.getIdTiarhe()));
			
			row.createCell(cellNum++).setCellValue(notNull(eae.getPrimaryFichePoste().getEmploi()));
			row.createCell(cellNum++).setCellValue(notNull(eae.getPrimaryFichePoste().getFonction()));
			row.createCell(cellNum++).setCellValue(notNull(eae.getPrimaryFichePoste().getGradePoste()));
			row.createCell(cellNum++).setCellValue("VILLE DE NOUMEA");
			row.createCell(cellNum++).setCellValue(notNull(eae.getPrimaryFichePoste().getDirectionService()));
			row.createCell(cellNum++).setCellValue(notNull(eae.getPrimaryFichePoste().getLocalisation()));
			row.createCell(cellNum++).setCellValue(notNull(eae.getPrimaryFichePoste().getMissions()));
			row.createCell(cellNum++).setCellValue(eae.getPrimaryFichePoste().getAgentShd() == null ? "" : eae.getPrimaryFichePoste().getAgentShd().getDisplayNom());
			row.createCell(cellNum++).setCellValue(notNull(eae.getPrimaryFichePoste().getFonctionResponsable()));
			
			row.createCell(cellNum++).setCellValue(""); // TODO : #54232
			row.createCell(cellNum++).setCellValue(""); // TODO : #54232
			row.createCell(cellNum++).setCellValue(""); // TODO : #54232
			
			// AUTOEVAL_NOUVELEM
			row.createCell(cellNum++).setCellValue(eae.getEaeAutoEvaluation() == null ? "" : notNull(eae.getEaeAutoEvaluation().getParticularites()));
			row.createCell(cellNum++).setCellValue(eae.getEaeAutoEvaluation() == null ? "" : notNull(eae.getEaeAutoEvaluation().getAcquis()));
			row.createCell(cellNum++).setCellValue(eae.getEaeAutoEvaluation() == null ? "" : notNull(eae.getEaeAutoEvaluation().getSuccesDifficultes()));
			row.createCell(cellNum++).setCellValue(eae.getCommentaire() != null ? eae.getCommentaire().getText() : "");

			setObjectifs(eae.getEaeResultats(), row);
			
			setAppreciations(eae.getEaeAppreciations(), row);
			
			// OBJ_ANNEE_SUIV1
			setPlansAction(eae.getEaePlanActions(), row);
			
			// ZYX4SOUHAI
			setSouhaits(eae.getEaeEvolution(), row);
			// ZYX4MOBGEO
			if (eae.getEaeEvolution() != null) {
				row.createCell(cellNum++).setCellValue(eae.getEaeEvolution().isMobiliteGeo() ? "1" : "0");
				row.createCell(cellNum++).setCellValue(eae.getEaeEvolution().isMobiliteFonctionnelle() ? "1" : "0");
				row.createCell(cellNum++).setCellValue(eae.getEaeEvolution().isChangementMetier() ? "1" : "0");
				if (eae.getEaeEvolution().getDelaiEnvisage() != null) {
					String mapping = eae.getEaeEvolution().getDelaiEnvisage().name().equals("MOINS1AN") ? "0" :
						eae.getEaeEvolution().getDelaiEnvisage().name().equals("ENTRE1ET2ANS") ? "1" : "2";
					row.createCell(cellNum++).setCellValue(mapping);
				} else {
					addEmptyCell(row);
				}
				row.createCell(cellNum++).setCellValue(eae.getEaeEvolution().isMobiliteService() ? "1" : "0");
				row.createCell(cellNum++).setCellValue(eae.getEaeEvolution().isMobiliteDirection() ? "1" : "0");
				row.createCell(cellNum++).setCellValue(eae.getEaeEvolution().isMobiliteCollectivite() ? "1" : "0");
				row.createCell(cellNum++).setCellValue(eae.getEaeEvolution().isMobiliteAutre() ? "1" : "0");
				row.createCell(cellNum++).setCellValue(eae.getEaeEvolution().isConcours() ? "1" : "0");
				row.createCell(cellNum++).setCellValue(eae.getEaeEvolution().isVae() ? "1" : "0");
				
				// Base horaire des temps partiels
				if (eae.getEaeEvolution().getTempsPartielIdSpbhor() != null) {
					SpbhorDto bh = sirhWsConsumer.getSpbhorById(eae.getEaeEvolution().getTempsPartielIdSpbhor());
					String value = String.valueOf(bh.getTaux() * 100).substring(0, 2);
					row.createCell(cellNum++).setCellValue(value);
				} else 
					addEmptyCell(row);
				
				row.createCell(cellNum++).setCellValue(notNull(eae.getEaeEvolution().getDateRetraite()));
				row.createCell(cellNum++).setCellValue(notNull(eae.getEaeEvolution().getLibelleAutrePerspective()));
				row.createCell(cellNum++).setCellValue(notNull(eae.getEaeEvolution().getNomConcours()));
				row.createCell(cellNum++).setCellValue(notNull(eae.getEaeEvolution().getNomVae()));
				row.createCell(cellNum++).setCellValue(notNull(eae.getEaeEvolution().getCommentaireEvolution().getText()));
			} else {
				addEmptyCell(row, 16);
			}
			// ZYX4PROAC1 : Développement
			setDeveloppements(eae.getEaeEvolution(), row);
			
			// ZYX4RESULT
			if (eae.getEaeEvaluation().getNiveauEae() != null) {
				String mapping = eae.getEaeEvaluation().getNiveauEae().equals(EaeNiveauEnum.EXCELLENT) ? "0" :
					eae.getEaeEvaluation().getNiveauEae().equals(EaeNiveauEnum.SATISFAISANT) ? "1" :
						eae.getEaeEvaluation().getNiveauEae().equals(EaeNiveauEnum.NECESSITANT_DES_PROGRES) ? "2" : "3";
				row.createCell(cellNum++).setCellValue(mapping);
			} else {
				addEmptyCell(row);
			}
			row.createCell(cellNum++).setCellValue(notNull(eae.getEaeEvaluation().getAvisRevalorisation()));
			row.createCell(cellNum++).setCellValue(notNull(eae.getEaeEvaluation().getAvisChangementClasse()));
			// FORMATEUR_DOMAINE
			row.createCell(cellNum++).setCellValue(getLibelleFormateurs());
			row.createCell(cellNum++).setCellValue(eae.getEaeEvolution() == null ? "" : eae.getEaeEvolution().getCommentaireEvaluateur() != null ? eae.getEaeEvolution().getCommentaireEvaluateur().getText() : "");
			row.createCell(cellNum++).setCellValue(eae.getEaeEvolution() == null ? "" : eae.getEaeEvolution().getCommentaireEvalue() != null ? eae.getEaeEvolution().getCommentaireEvalue().getText() : "");
			row.createCell(cellNum++).setCellValue(notNull(eae.getDureeEntretienMinutes()));
			row.createCell(cellNum++).setCellValue(eae.getEaeEvaluation().getCommentaireEvaluateur() != null ? eae.getEaeEvaluation().getCommentaireEvaluateur().getText() : "");
			// ZYX4AVCECH
			if (eae.getEaeEvaluation().getPropositionAvancement() != null) {
				String mapping = eae.getEaeEvaluation().getPropositionAvancement().equals(EaeAvancementEnum.MINI) ? "N" :
					eae.getEaeEvaluation().getPropositionAvancement().equals(EaeAvancementEnum.MOY) ? "A" : 
						eae.getEaeEvaluation().getPropositionAvancement().equals(EaeAvancementEnum.MAXI) ? "M" : "";
				row.createCell(cellNum++).setCellValue(mapping);
			} else {
				addEmptyCell(row);
			}
			row.createCell(cellNum++).setCellValue(eae.getEaeEvaluation().getCommentaireEvalue() != null ? eae.getEaeEvaluation().getCommentaireEvalue().getText() : "");
			row.createCell(cellNum++).setCellValue(eae.getEaeEvaluation().getCommentaireAvctEvaluateur() != null ? eae.getEaeEvaluation().getCommentaireAvctEvaluateur().getText() : "");
			row.createCell(cellNum++).setCellValue(eae.getEaeEvaluation().getCommentaireAvctEvalue() != null ? eae.getEaeEvaluation().getCommentaireAvctEvalue().getText() : "");
			// FORMATEUR_DOMAINE_ECH
			row.createCell(cellNum++).setCellValue(getEcheanceFormateurs());
			row.createCell(cellNum++).setCellValue(getPriorisationFormateurs());
			// ZYX4TEMPAR
			row.createCell(cellNum++).setCellValue(eae.getEaeEvolution() == null ? "" : notNull(eae.getEaeEvolution().isTempsPartiel()));
			row.createCell(cellNum++).setCellValue(eae.getEaeEvolution() == null ? "" : notNull(eae.getEaeEvolution().isRetraite()));
			row.createCell(cellNum++).setCellValue(eae.getEaeEvolution() == null ? "" : notNull(eae.getEaeEvolution().isAutrePerspective()));
			row.createCell(cellNum++).setCellValue(notNull(eae.getEaeEvaluation().getNoteAnnee()));
			row.createCell(cellNum++).setCellValue(notNull(agent.getNomUsage()));
			row.createCell(cellNum++).setCellValue(notNull(agent.getPrenomUsage()));
			row.createCell(cellNum++).setCellValue(notNull(eae.getEaeEvalue().getNouvGrade()));
			row.createCell(cellNum++).setCellValue(notNull(eae.getEaeEvalue().getNouvEchelon()));
			row.createCell(cellNum++).setCellValue(notNull(eae.getEaeEvalue().getDateEffetAvancement()));
			addEmptyCell(row, 2);
			
			logger.info("Row number {} wrote, for eae {}.", rowNum, eae.getIdEae());
	    }

	    // Write the output to a file
	    FileOutputStream fileOut = new FileOutputStream("/home/teo/Desktop/migration_EAE.xlsx");
	    workbook.write(fileOut);
	    fileOut.close();
	    workbook.close();
	    logger.info("============= Export terminé =============");
	}

	private Agent getManager(Eae eae) throws SirhWSConsumerException {
		Agent manager = null;
		
		if (!eae.getEaeEvaluateurs().isEmpty()) {
			for (EaeEvaluateur eval : eae.getEaeEvaluateurs()) {
				manager = sirhWsConsumer.getAgent(eval.getIdAgent());
				if (manager.getIdTiarhe() != null)
					return manager;
			}
		}
		// A ce stade, soit il n'y a pas de manager, soit les managers n'ont pas de matricule TIARHE.
		// On va donc rechercher le délégataire.
		if (eae.getIdAgentDelegataire() != null) {
			manager = sirhWsConsumer.getAgent(eae.getIdAgentDelegataire());
			if (manager.getIdTiarhe() == null)
				logger.warn("Le délégataire ne possède pas de matricule TIARHE pour l'EAE {}", eae.getIdEae());
		} else {
			logger.warn("Aucun délégataire pour l'EAE {}", eae.getIdEae());
		}
		
		return manager;
	}
	
	private List<String> getHeaders() {
		List<String> headers = Lists.newArrayList();

		headers.add("ID EAE");
		headers.add("NUDOSS / Matricule");
		headers.add("ANNEE_EAE");
		headers.add("DATE_EAE");
		headers.add("BS");
		headers.add("NUDOSS_VALIDEUR / Matricule");
		headers.add("EMPLOI_REF");
		headers.add("INTITULE_POSTE");
		headers.add("GRADE");
		headers.add("COLLECTIVITE");
		headers.add("DIRECTION_SERVICE");
		headers.add("LOCALISATION");
		headers.add("MISSION");
		headers.add("RESPONSABLE");
		headers.add("FONCTION_RESPONSABLE");
		headers.add("ACTIVITE_PRINCIPALE");
		headers.add("ACTIVITE_SECONDAIRE");
		headers.add("COMPETENCE_REQUISE");
		headers.add("AUTOEVAL_NOUVELEM");
		headers.add("AUTOEVAL_OBJ");
		headers.add("AUTOEVAL_DIFF");
		headers.add("ECART_OBS");
		headers.add("OBJ_ANNEE_PREC1");
		headers.add("RESULT_ANNEE_PREC1");
		headers.add("COMMENT_ANNEE_PREC1");
		headers.add("OBJ_ANNEE_PREC2");
		headers.add("RESULT_ANNEE_PREC2");
		headers.add("COMMENT_ANNEE_PREC2");
		headers.add("OBJ_ANNEE_PREC3");
		headers.add("RESULT_ANNEE_PREC3");
		headers.add("COMMENT_ANNEE_PREC3");
		headers.add("OBJ_ANNEE_PREC4");
		headers.add("RESULT_ANNEE_PREC4");
		headers.add("COMMENT_ANNEE_PREC4");
		headers.add("OBJ_ANNEE_PREC5");
		headers.add("RESULT_ANNEE_PREC5");
		headers.add("COMMENT_ANNEE_PREC5");
		headers.add("OBJ_PROG_IND_ANNEE_PREC");
		headers.add("RESULT_OBJ_ANNEE_PREC");
		headers.add("COM_OBJ_ANNEE_PREC");
		headers.add("QA11");
		headers.add("QA12");
		headers.add("QA13");
		headers.add("QA14");
		headers.add("QA21");
		headers.add("QA22");
		headers.add("QA23");
		headers.add("QA24");
		headers.add("QA31");
		headers.add("QA32");
		headers.add("QA33");
		headers.add("QA34");
		headers.add("QA41");
		headers.add("QA42");
		headers.add("QA43");
		headers.add("QA44");
		headers.add("QR11");
		headers.add("QR12");
		headers.add("QR13");
		headers.add("QR14");
		headers.add("QR21");
		headers.add("QR22");
		headers.add("QR23");
		headers.add("QR24");
		headers.add("QR31");
		headers.add("QR32");
		headers.add("QR33");
		headers.add("QR34");
		headers.add("QR41");
		headers.add("QR42");
		headers.add("QR43");
		headers.add("QR44");
		headers.add("OBJ_ANNEE_SUIV1");
		headers.add("INDIC_MESURE_ANNEE_SUIV1");
		headers.add("DELAI_ANNEE_SUIV1");
		headers.add("OBJ_ANNEE_SUIV2");
		headers.add("INDIC_MESURE_ANNEE_SUIV2");
		headers.add("DELAI_ANNEE_SUIV2");
		headers.add("OBJ_ANNEE_SUIV3");
		headers.add("INDIC_MESURE_ANNEE_SUIV3");
		headers.add("DELAI_ANNEE_SUIV3");
		headers.add("OBJ_ANNEE_SUIV4");
		headers.add("INDIC_MESURE_ANNEE_SUIV4");
		headers.add("DELAI_ANNEE_SUIV4");
		headers.add("OBJ_ANNEE_SUIV5");
		headers.add("INDIC_MESURE_ANNEE_SUIV5");
		headers.add("DELAI_ANNEE_SUIV5");
		headers.add("OBJ_PROG_IND_ANNEE_SUIV");
		headers.add("BESOIN_MAT");
		headers.add("BESOIN_FIN");
		headers.add("BESOIN_AUT");
		headers.add("ZYX4SOUHAI");
		headers.add("ZYX4SUGGES");
		headers.add("ZYX4MOBGEO");
		headers.add("ZYX4MOBFON");
		headers.add("ZYX4CHTMET");
		headers.add("ZYX4DELMOB");
		headers.add("ZYX4MOBSER");
		headers.add("ZYX4MOBDIR");
		headers.add("ZYX4MOBCOL");
		headers.add("ZYX4MOBAUT");
		headers.add("ZYX4CONCOU");
		headers.add("ZYX4SOUVAE");
		headers.add("ZYX4QUOPAR");
		headers.add("ZYX4DATRET");
		headers.add("ZYX4PERAUT");
		headers.add("ZYX4LIBCON");
		headers.add("ZYX4LIBDIP");
		headers.add("ZYX4EVOLUT");
		headers.add("ZYX4PROAC1");
		headers.add("ZYX4PROAC2");
		headers.add("ZYX4PROAC3");
		headers.add("ZYX4ECHPA1");
		headers.add("ZYX4ECHPA2");
		headers.add("ZYX4ECHPA3");
		headers.add("ZYX4PRIOA1");
		headers.add("ZYX4PRIOA2");
		headers.add("ZYX4PRIOA3");
		headers.add("ZYX4PRODE1");
		headers.add("ZYX4PRODE2");
		headers.add("ZYX4PRODE3");
		headers.add("ZYX4ECHPD1");
		headers.add("ZYX4ECHPD2");
		headers.add("ZYX4ECHPD3");
		headers.add("ZYX4PRIOD1");
		headers.add("ZYX4PRIOD2");
		headers.add("ZYX4PRIOD3");
		headers.add("ZYX4PRECON");
		headers.add("ZYX4ECHCON");
		headers.add("ZYX4PRICON");
		headers.add("ZYX4DEVPER");
		headers.add("ZYX4ECHDPE");
		headers.add("ZYX4PRIDPE");
		headers.add("ZYX4COMPOR");
		headers.add("ZYX4ECHCOM");
		headers.add("ZYX4PRICOM");
		headers.add("ZYX4RESULT");
		headers.add("ZYX4AUGMEN");
		headers.add("ZYX4AVCCLA");
		headers.add("FORMATEUR_DOMAINE");
		headers.add("OBSERVATION_EVALUATEUR");
		headers.add("OBSERVATION_EVALUE");
		headers.add("DUREE_ENTRETIEN");
		headers.add("APPRECIATION_EVALUATEUR");
		headers.add("ZYX4AVCECH");
		headers.add("COMMENTAIRE_EVALUE");
		headers.add("DETAIL_DEMANDE_AVANCEMENT");
		headers.add("COMMENTAIRE_EVALUE_PROPOSITION");
		headers.add("FORMATEUR_DOMAINE_ECH");
		headers.add("FORMATEUR_DOMAINE_PRI");
		headers.add("ZYX4TEMPAR");
		headers.add("ZYX4RETRAI");
		headers.add("ZYX4TEMPER");
		headers.add("ZYEQTHEORI");
		headers.add("NOM_AGENT");
		headers.add("PRENOM_AGENT");
		headers.add("AV_CLASSE_AGENT");
		headers.add("AV_ECHELON_AGENT");
		headers.add("AV_DATE_AGENT");
		headers.add("ZYX4MSCCOR");
		headers.add("ZYX4MSCGRA");
		
		return headers;
	}
	
	private void setSouhaits(EaeEvolution eaeEvolution, Row row) {
		String souhaits = "";
		String suggestions = "";
		if (eaeEvolution != null) {
			for (EaeEvolutionSouhait souhait : eaeEvolution.getEaeEvolutionSouhaits()) {
				souhaits = souhaits == "" ? souhait.getSouhait() : souhaits + ", " + souhait.getSouhait();
				suggestions = suggestions == "" ? souhait.getSuggestion() : suggestions + ", " + souhait.getSuggestion();
			}
		}
		row.createCell(cellNum++).setCellValue(souhaits);
		row.createCell(cellNum++).setCellValue(suggestions);
	}
	
	private void setDeveloppements(EaeEvolution eaeEvolution, Row row) {
		List<EaeDeveloppement> connaissances = Lists.newArrayList();
		List<EaeDeveloppement> competences = Lists.newArrayList();
		List<EaeDeveloppement> concours = Lists.newArrayList();
		List<EaeDeveloppement> personels = Lists.newArrayList();
		List<EaeDeveloppement> comportements = Lists.newArrayList();
		formateurs = Lists.newArrayList();

		// Les formateurs sont déclarés en variable de classe, car les données doivent être affichées plus tard.
		// Préparation des données
		if (eaeEvolution != null) {
			for (EaeDeveloppement dev : eaeEvolution.getEaeDeveloppements()) {
				switch (dev.getTypeDeveloppement()) {
					case CONNAISSANCE: 
						connaissances.add(dev);
						break;
					case COMPETENCE: 
						competences.add(dev);
						break;
					case CONCOURS: 
						concours.add(dev);
						break;
					case PERSONNEL: 
						personels.add(dev);
						break;
					case COMPORTEMENT: 
						comportements.add(dev);
						break;
					case FORMATEUR: 
						formateurs.add(dev);
						break;
					default : break;
				}
			}
			
			if (connaissances.size() > 3)
				logger.warn("Il y a {} connaissances pour l'eaeEvolution id {}", connaissances.size(), eaeEvolution.getIdEaeEvolution());
			if (competences.size() > 3)
				logger.warn("Il y a {} compétences pour l'eaeEvolution id {}", competences.size(), eaeEvolution.getIdEaeEvolution());

			// Ecriture des données
			writeConnaissancesAndCompetences(connaissances, row);
			writeConnaissancesAndCompetences(competences, row);
			writeDeveloppements(concours, row);
			writeDeveloppements(personels, row);
			writeDeveloppements(comportements, row);
		} else {
			addEmptyCell(row, 27);
		}
	}
	
	private void writeConnaissancesAndCompetences(List<EaeDeveloppement> developpements, Row row) {
		EaeDeveloppement dev1 = null;
		EaeDeveloppement dev2 = null;
		EaeDeveloppement dev3 = null;
		
		for (EaeDeveloppement dev : developpements) {
			if (dev1 == null)
				dev1 = dev;
           else if (dev2 == null)
        	   dev2 = dev;
           else if (dev3 == null)
        	   dev3 = dev;
		}
		row.createCell(cellNum++).setCellValue(dev1 == null ? "" : dev1.getLibelle());
		row.createCell(cellNum++).setCellValue(dev2 == null ? "" : dev2.getLibelle());
		row.createCell(cellNum++).setCellValue(dev3 == null ? "" : dev3.getLibelle());
		row.createCell(cellNum++).setCellValue(dev1 == null ? "" : sdf.format(dev1.getEcheance()));
		row.createCell(cellNum++).setCellValue(dev2 == null ? "" : sdf.format(dev2.getEcheance()));
		row.createCell(cellNum++).setCellValue(dev3 == null ? "" : sdf.format(dev3.getEcheance()));
		row.createCell(cellNum++).setCellValue(dev1 == null ? "" : String.valueOf(dev1.getPriorisation()));
		row.createCell(cellNum++).setCellValue(dev2 == null ? "" : String.valueOf(dev2.getPriorisation()));
		row.createCell(cellNum++).setCellValue(dev3 == null ? "" : String.valueOf(dev3.getPriorisation()));
	}
	
	private void writeDeveloppements(List<EaeDeveloppement> developpements, Row row) {
		if (developpements.isEmpty()) {
			addEmptyCell(row, 3);
			return;
		}
		
		String libelle = "";
		String echeance = "";
		String priorisation = "";
		EaeDeveloppement dev = null;
		
		if (developpements.size() > 1) {
			for (int i = 0 ; i < developpements.size() ; i++) {
				dev = developpements.get(i);
				libelle += (i+1) + " - " + dev.getLibelle() + "\n";
				echeance += (i+1) + " - " + sdf.format(dev.getEcheance()) + "\n";
				priorisation += (i+1) + " - " + String.valueOf(dev.getPriorisation()) + "\n";
			}
		} else {
			dev = developpements.get(0);
			libelle = dev.getLibelle();
			echeance = dev.getEcheance() == null ? "" : sdf.format(dev.getEcheance());
			priorisation = String.valueOf(dev.getPriorisation());
		}
		
		row.createCell(cellNum++).setCellValue(notNull(libelle));
		row.createCell(cellNum++).setCellValue(notNull(echeance));
		row.createCell(cellNum++).setCellValue(notNull(priorisation));
	}
	
	private String getLibelleFormateurs() {
		if (formateurs.isEmpty())
			return "";
		if (formateurs.size() == 1)
			return formateurs.get(0).getLibelle();
		
		String libelle = "";
		EaeDeveloppement dev = null;
		
		for (int i = 0 ; i < formateurs.size() ; i++) {
			dev = formateurs.get(i);
			libelle += (i+1) + " - " + dev.getLibelle() + "\n";
		}
		
		return libelle;
	}
	
	private String getEcheanceFormateurs() {
		if (formateurs.isEmpty())
			return "";
		if (formateurs.size() == 1)
			return formateurs.get(0).getEcheance() == null ? "" : sdf.format(formateurs.get(0).getEcheance());
		
		String echeance = "";
		EaeDeveloppement dev = null;
		
		for (int i = 0 ; i < formateurs.size() ; i++) {
			dev = formateurs.get(i);
			echeance += dev.getEcheance() == null ? "" : (i+1) + " - " + sdf.format(dev.getEcheance()) + "\n";
		}
		
		return echeance;
	}
	
	private String getPriorisationFormateurs() {
		if (formateurs.isEmpty())
			return "";
		if (formateurs.size() == 1)
			return String.valueOf(formateurs.get(0).getPriorisation());
		
		String priorisation = "";
		EaeDeveloppement dev = null;
		
		for (int i = 0 ; i < formateurs.size() ; i++) {
			dev = formateurs.get(i);
			priorisation += (i+1) + " - " + String.valueOf(dev.getPriorisation()) + "\n";
		}
		
		return priorisation;
	}
	
	private void setObjectifs(Set<EaeResultat> eaeResultat, Row row) {
		if (!eaeResultat.isEmpty()) {
			Map<Integer, EaeResultat> map = Maps.newHashMap();
			int i = 0;
			for (EaeResultat resultat : eaeResultat) {
				if (resultat.getTypeObjectif().getIdEaeTypeObjectif() == 1 && i < 5) 
					map.put(i++, resultat);
				else if (resultat.getTypeObjectif().getIdEaeTypeObjectif() == 2 && map.get(5) == null) 
					map.put(5, resultat);
			}
			
			for (Integer u = 0 ; u < 6 ; u++) {
				EaeResultat result = map.get(u);
				if (result != null) {
					row.createCell(cellNum++).setCellValue(result.getObjectif());
					row.createCell(cellNum++).setCellValue(result.getResultat());
					row.createCell(cellNum++).setCellValue(result.getCommentaire() == null ? "" : result.getCommentaire().getText());
				} else {
					addEmptyCell(row, 3);
				}
			}
		} else {
			addEmptyCell(row, 18);
		}
	}
	
	private void setPlansAction(Set<EaePlanAction> planAction, Row row) {
		if (!planAction.isEmpty()) {
			Map<Integer, EaePlanAction> map = Maps.newHashMap();
			String objAnneeSuivante = "";
			String besoinMateriel = "";
			String besoinFinancier = "";
			String autreBesoin = "";
			
			int i = 0;
			for (EaePlanAction plan : planAction) {
				if (plan.getTypeObjectif().getIdEaeTypeObjectif() == 1 && i < 5) 
					map.put(i++, plan);
				else if (plan.getTypeObjectif().getIdEaeTypeObjectif() == 2 && StringUtils.isEmpty(objAnneeSuivante)) 
					objAnneeSuivante = plan.getObjectif();
				else if (plan.getTypeObjectif().getIdEaeTypeObjectif() == 3 && StringUtils.isEmpty(besoinMateriel)) 
					besoinMateriel = plan.getObjectif();
				else if (plan.getTypeObjectif().getIdEaeTypeObjectif() == 4 && StringUtils.isEmpty(besoinFinancier)) 
					besoinFinancier = plan.getObjectif();
				else if (plan.getTypeObjectif().getIdEaeTypeObjectif() == 5 && StringUtils.isEmpty(autreBesoin)) 
					autreBesoin = plan.getObjectif();
			}
			
			for (Integer u = 0 ; u < 5 ; u++) {
				EaePlanAction result = map.get(u);
				if (result != null) {
					row.createCell(cellNum++).setCellValue(result.getObjectif());
					row.createCell(cellNum++).setCellValue(result.getMesure());
					addEmptyCell(row);
				} else {
					addEmptyCell(row, 3);
				}
			}
			// OBJ_PROG_IND_ANNEE_SUIV, BESOIN_MAT, BESOIN_FIN, BESOIN_AUT
			row.createCell(cellNum++).setCellValue(objAnneeSuivante);
			row.createCell(cellNum++).setCellValue(besoinMateriel);
			row.createCell(cellNum++).setCellValue(besoinFinancier);
			row.createCell(cellNum++).setCellValue(autreBesoin);
		} else {
			addEmptyCell(row, 19);
		}
	}
	
	private void setAppreciations(Set<EaeAppreciation> eaeAppreciations, Row row) {
		Map<Integer, EaeAppreciation> technique = Maps.newHashMap();
		Map<Integer, EaeAppreciation> savoirEtre = Maps.newHashMap();
		Map<Integer, EaeAppreciation> managerial = Maps.newHashMap();
		Map<Integer, EaeAppreciation> resultat = Maps.newHashMap();
		
		if (eaeAppreciations != null && !eaeAppreciations.isEmpty()) {
			// Construction des données
			for (EaeAppreciation app : eaeAppreciations) {
				if (app.getTypeAppreciation().equals(EaeTypeAppreciationEnum.TE))
					technique.put(app.getNumero(), app);
				else if (app.getTypeAppreciation().equals(EaeTypeAppreciationEnum.SE))
					savoirEtre.put(app.getNumero(), app);
				else if (app.getTypeAppreciation().equals(EaeTypeAppreciationEnum.MA))
					managerial.put(app.getNumero(), app);
				else if (app.getTypeAppreciation().equals(EaeTypeAppreciationEnum.RE))
					resultat.put(app.getNumero(), app);
			}

				// Ecriture des données (evalue)
			for (int i = 0 ; i < 4 ; i++) {
				EaeAppreciation appreciation = technique.get(i);
				row.createCell(cellNum++).setCellValue((appreciation == null || appreciation.getNoteEvalue().equals("NA")) ? "" : appreciation.getNoteEvalue());
			}
			for (int i = 0 ; i < 4 ; i++) {
				EaeAppreciation appreciation = savoirEtre.get(i);
				row.createCell(cellNum++).setCellValue((appreciation == null || appreciation.getNoteEvalue().equals("NA")) ? "" : appreciation.getNoteEvalue());
			}
			for (int i = 0 ; i < 4 ; i++) {
				EaeAppreciation appreciation = managerial.get(i);
				row.createCell(cellNum++).setCellValue((appreciation == null || appreciation.getNoteEvalue().equals("NA")) ? "" : appreciation.getNoteEvalue());
			}
			for (int i = 0 ; i < 4 ; i++) {
				EaeAppreciation appreciation = resultat.get(i);
				row.createCell(cellNum++).setCellValue((appreciation == null || appreciation.getNoteEvalue().equals("NA")) ? "" : appreciation.getNoteEvalue());
			}
			// Ecriture des données (evaluateur)
			for (int i = 0 ; i < 4 ; i++) {
				EaeAppreciation appreciation = technique.get(i);
				row.createCell(cellNum++).setCellValue((appreciation == null || appreciation.getNoteEvalue().equals("NA")) ? "" : appreciation.getNoteEvaluateur());
			}
			for (int i = 0 ; i < 4 ; i++) {
				EaeAppreciation appreciation = savoirEtre.get(i);
				row.createCell(cellNum++).setCellValue((appreciation == null || appreciation.getNoteEvalue().equals("NA")) ? "" : appreciation.getNoteEvaluateur());
			}
			for (int i = 0 ; i < 4 ; i++) {
				EaeAppreciation appreciation = managerial.get(i);
				row.createCell(cellNum++).setCellValue((appreciation == null || appreciation.getNoteEvalue().equals("NA")) ? "" : appreciation.getNoteEvaluateur());
			}
			for (int i = 0 ; i < 4 ; i++) {
				EaeAppreciation appreciation = resultat.get(i);
				row.createCell(cellNum++).setCellValue((appreciation == null || appreciation.getNoteEvalue().equals("NA")) ? "" : appreciation.getNoteEvaluateur());
			}
		} else {
			addEmptyCell(row, 32);
		}
	}
	
	private String notNull(Object s) {
		return s != null ? s.toString() : "";
	}
	
	private void addEmptyCell(Row row, int number) {
		for (int i = 0 ; i < number ; i++)
			row.createCell(cellNum++).setCellValue("");
	}
	
	private void addEmptyCell(Row row) {
		addEmptyCell(row, 1);
	}
}
