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
import nc.noumea.mairie.sirh.eae.domain.EaeEvolution;
import nc.noumea.mairie.sirh.eae.domain.EaeEvolutionSouhait;
import nc.noumea.mairie.sirh.eae.domain.EaePlanAction;
import nc.noumea.mairie.sirh.eae.domain.EaeResultat;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeAvancementEnum;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeNiveauEnum;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeTypeAppreciationEnum;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeTypeDeveloppementEnum;
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
	
	private EaeDeveloppement formateur = new EaeDeveloppement();
	
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
			Agent agent = sirhWsConsumer.getAgent(eae.getEaeEvalue().getIdAgent());
			Agent manager = null;
			if (!eae.getEaeEvaluateurs().isEmpty())
				manager = sirhWsConsumer.getAgent(eae.getEaeEvaluateurs().iterator().next().getIdAgent());
			
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
			row.createCell(cellNum++).setCellValue(eae.getPrimaryFichePoste().getAgentShd() == null ? "" : eae.getPrimaryFichePoste().getAgentShd().getDisplayNom()); // TODO : NPE
			row.createCell(cellNum++).setCellValue(notNull(eae.getPrimaryFichePoste().getFonctionResponsable()));
			row.createCell(cellNum++).setCellValue(""); // TODO
			row.createCell(cellNum++).setCellValue(""); // TODO
			row.createCell(cellNum++).setCellValue(""); // TODO
			
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
					// TODO : Vérifier que le mapping se fait bien (String, ou objet enum ?)
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
				row.createCell(cellNum++).setCellValue(notNull(eae.getEaeEvolution().getTempsPartielIdSpbhor())); // TODO : Mapping
				row.createCell(cellNum++).setCellValue(notNull(eae.getEaeEvolution().getDateRetraite()));
				row.createCell(cellNum++).setCellValue(notNull(eae.getEaeEvolution().getLibelleAutrePerspective()));
				row.createCell(cellNum++).setCellValue(notNull(eae.getEaeEvolution().getNomConcours()));
				row.createCell(cellNum++).setCellValue(notNull(eae.getEaeEvolution().getNomVae()));
				row.createCell(cellNum++).setCellValue(notNull(eae.getEaeEvolution().getCommentaireEvolution().getText()));
			} else {
				row.createCell(cellNum++).setCellValue("");
				row.createCell(cellNum++).setCellValue("");
				row.createCell(cellNum++).setCellValue("");
				row.createCell(cellNum++).setCellValue("");
				row.createCell(cellNum++).setCellValue("");
				row.createCell(cellNum++).setCellValue("");
				row.createCell(cellNum++).setCellValue("");
				row.createCell(cellNum++).setCellValue("");
				row.createCell(cellNum++).setCellValue("");
				row.createCell(cellNum++).setCellValue("");
				row.createCell(cellNum++).setCellValue("");
				row.createCell(cellNum++).setCellValue("");
				row.createCell(cellNum++).setCellValue("");
				row.createCell(cellNum++).setCellValue("");
				row.createCell(cellNum++).setCellValue("");
				row.createCell(cellNum++).setCellValue("");
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
			row.createCell(cellNum++).setCellValue(formateur == null ? "" : formateur.getLibelle());
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
			row.createCell(cellNum++).setCellValue(formateur == null || formateur.getEcheance() == null ? "" : sdf.format(formateur.getEcheance()));
			row.createCell(cellNum++).setCellValue(formateur == null ? "" : String.valueOf(formateur.getPriorisation()));
			// ZYX4TEMPAR
			row.createCell(cellNum++).setCellValue(eae.getEaeEvolution() == null ? "" : notNull(eae.getEaeEvolution().isTempsPartiel()));
			row.createCell(cellNum++).setCellValue(eae.getEaeEvolution() == null ? "" : notNull(eae.getEaeEvolution().isRetraite()));
			row.createCell(cellNum++).setCellValue(eae.getEaeEvolution() == null ? "" : notNull(eae.getEaeEvolution().isAutrePerspective()));
			row.createCell(cellNum++).setCellValue(notNull(eae.getEaeEvaluation().getNoteAnnee()));
			if (eae.getEaeEvalue().getAgent() != null) {
				row.createCell(cellNum++).setCellValue(notNull(eae.getEaeEvalue().getAgent().getNomUsage()));
				row.createCell(cellNum++).setCellValue(notNull(eae.getEaeEvalue().getAgent().getPrenomUsage()));
			} else {
				addEmptyCell(row, 2);
			}
			row.createCell(cellNum++).setCellValue(notNull(eae.getEaeEvalue().getNouvGrade()));
			row.createCell(cellNum++).setCellValue(notNull(eae.getEaeEvalue().getNouvEchelon()));
			row.createCell(cellNum++).setCellValue(notNull(eae.getEaeEvalue().getDateEffetAvancement()));
			addEmptyCell(row, 2);
			
			logger.info("Row number {} wrote, for eae {}.", rowNum, eae.getIdEae());
	    }

	    // Write the output to a file
	    FileOutputStream fileOut = new FileOutputStream("/home/teo/Desktop/testEAE.xlsx");
	    workbook.write(fileOut);
	    fileOut.close();
	    workbook.close();
	    logger.info("============= Export terminé =============");
	
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
		// Le formateur est déclaré en variable de classe, car les données doivent être affichées plus tard.
		formateur = null;
		EaeDeveloppement connaissance1 = null;
		EaeDeveloppement connaissance2 = null;
		EaeDeveloppement connaissance3 = null;
		EaeDeveloppement competence1 = null;
		EaeDeveloppement competence2 = null;
		EaeDeveloppement competence3 = null;
		EaeDeveloppement concours = null;
		EaeDeveloppement personnel = null;
		EaeDeveloppement comportement = null;

		// Préparation des données
		if (eaeEvolution != null) {
			for (EaeDeveloppement dev : eaeEvolution.getEaeDeveloppements()) {
				if (dev.getTypeDeveloppement().equals(EaeTypeDeveloppementEnum.FORMATEUR))
					formateur = dev;
				if (dev.getTypeDeveloppement().equals(EaeTypeDeveloppementEnum.CONCOURS))
					concours = dev;
				else if (dev.getTypeDeveloppement().equals(EaeTypeDeveloppementEnum.PERSONNEL))
					personnel = dev;
				else if (dev.getTypeDeveloppement().equals(EaeTypeDeveloppementEnum.COMPORTEMENT))
					comportement = dev;
				else if (dev.getTypeDeveloppement().equals(EaeTypeDeveloppementEnum.CONNAISSANCE)) {
					if (connaissance1 == null)
						connaissance1 = dev;
					else if (connaissance2 == null)
						connaissance2 = dev;
					else if (connaissance3 == null)
						connaissance3 = dev;
				}
				else if (dev.getTypeDeveloppement().equals(EaeTypeDeveloppementEnum.COMPETENCE)) {
					if (competence1 == null)
						competence1 = dev;
					else if (competence2 == null)
						competence2 = dev;
					else if (competence3 == null)
						competence3 = dev;
				}
			}

			// Ecriture des données
			row.createCell(cellNum++).setCellValue(connaissance1 == null ? "" : connaissance1.getLibelle());
			row.createCell(cellNum++).setCellValue(connaissance2 == null ? "" : connaissance2.getLibelle());
			row.createCell(cellNum++).setCellValue(connaissance3 == null ? "" : connaissance3.getLibelle());
			row.createCell(cellNum++).setCellValue(connaissance1 == null ? "" : sdf.format(connaissance1.getEcheance()));
			row.createCell(cellNum++).setCellValue(connaissance2 == null ? "" : sdf.format(connaissance2.getEcheance()));
			row.createCell(cellNum++).setCellValue(connaissance3 == null ? "" : sdf.format(connaissance3.getEcheance()));
			row.createCell(cellNum++).setCellValue(connaissance1 == null ? "" : String.valueOf(connaissance1.getPriorisation()));
			row.createCell(cellNum++).setCellValue(connaissance2 == null ? "" : String.valueOf(connaissance2.getPriorisation()));
			row.createCell(cellNum++).setCellValue(connaissance3 == null ? "" : String.valueOf(connaissance3.getPriorisation()));
			
			row.createCell(cellNum++).setCellValue(competence1 == null ? "" : competence1.getLibelle());
			row.createCell(cellNum++).setCellValue(competence2 == null ? "" : competence2.getLibelle());
			row.createCell(cellNum++).setCellValue(competence3 == null ? "" : competence3.getLibelle());
			row.createCell(cellNum++).setCellValue(competence1 == null ? "" : sdf.format(competence1.getEcheance()));
			row.createCell(cellNum++).setCellValue(competence2 == null ? "" : sdf.format(competence2.getEcheance()));
			row.createCell(cellNum++).setCellValue(competence3 == null ? "" : sdf.format(competence3.getEcheance()));
			row.createCell(cellNum++).setCellValue(competence1 == null ? "" : String.valueOf(competence1.getPriorisation()));
			row.createCell(cellNum++).setCellValue(competence2 == null ? "" : String.valueOf(competence2.getPriorisation()));
			row.createCell(cellNum++).setCellValue(competence3 == null ? "" : String.valueOf(competence3.getPriorisation()));

			row.createCell(cellNum++).setCellValue(concours == null ? "" : concours.getLibelle());
			row.createCell(cellNum++).setCellValue(concours == null ? "" : sdf.format(concours.getEcheance()));
			row.createCell(cellNum++).setCellValue(concours == null ? "" : String.valueOf(concours.getPriorisation()));

			row.createCell(cellNum++).setCellValue(personnel == null ? "" : personnel.getLibelle());
			row.createCell(cellNum++).setCellValue(personnel == null ? "" : sdf.format(personnel.getEcheance()));
			row.createCell(cellNum++).setCellValue(personnel == null ? "" : String.valueOf(personnel.getPriorisation()));

			row.createCell(cellNum++).setCellValue(comportement == null ? "" : comportement.getLibelle());
			row.createCell(cellNum++).setCellValue(comportement == null ? "" : sdf.format(comportement.getEcheance()));
			row.createCell(cellNum++).setCellValue(comportement == null ? "" : String.valueOf(comportement.getPriorisation()));
		} else {
			addEmptyCell(row, 27);
		}
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
