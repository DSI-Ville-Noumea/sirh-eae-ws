package nc.noumea.mairie.sirh.eae.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeEvolution;
import nc.noumea.mairie.sirh.eae.domain.EaeEvolutionSouhait;
import nc.noumea.mairie.sirh.eae.domain.EaeResultat;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeAvancementEnum;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeNiveauEnum;
import nc.noumea.mairie.sirh.eae.repository.IEaeRepository;

@Service
public class MigrationEaeService implements IMigrationEaeService {

	private Logger logger = LoggerFactory.getLogger(MigrationEaeService.class);

	@Autowired
	private IEaeRepository					eaeRepository;
	
	private Integer cellNum = 0;

	@Override
	@Transactional(readOnly = true)
	public void exportEAEForMigration() throws IOException {

		Workbook workbook = new XSSFWorkbook();
	    Sheet sheet = workbook.createSheet("EAE");

	    // Create a Row
	    Row headerRow = sheet.createRow(0);
	    int i = 0;
	    for (String h : getHeaders()) {
	      Cell cell = headerRow.createCell(i++);
	      cell.setCellValue(h);
	    }

	    int rowNum = 1;
	    for (Eae eae : eaeRepository.findAllForMigration()) {
	    	cellNum = 0;
			Row row = sheet.createRow(rowNum++);
			
			logger.info("Starting wirte row number {}, for eae {}.", rowNum, eae.getIdEae());

			row.createCell(cellNum++).setCellValue(eae.getIdEae()); // TODO : 
			row.createCell(cellNum++).setCellValue(eae.getEaeEvalue().getIdAgent()); //TODO : matr Tiarhé
			row.createCell(cellNum++).setCellValue(eae.getEaeCampagne().getAnnee());
			row.createCell(cellNum++).setCellValue(eae.getDateEntretien());
			row.createCell(cellNum++).setCellValue("18");
			row.createCell(cellNum++).setCellValue(eae.getEaeEvaluateurs().isEmpty() ? "" : String.valueOf(eae.getEaeEvaluateurs().iterator().next().getIdAgent())); // TODO : Matr Tiarhé
			row.createCell(cellNum++).setCellValue(notNull(eae.getPrimaryFichePoste().getEmploi()));
			row.createCell(cellNum++).setCellValue(notNull(eae.getPrimaryFichePoste().getFonction()));
			row.createCell(cellNum++).setCellValue(notNull(eae.getPrimaryFichePoste().getGradePoste()));
			row.createCell(cellNum++).setCellValue(""); // TODO
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

			// TODO : Trier en fonction du type. Faire une fonction, ça sera plus propre.
			setObjectifs(eae.getEaeResultats(), row);
			// Objectif 1
//			EaeResultat result = eae.getEaeResultats().isEmpty() ? null : eae.getEaeResultats().iterator().next();
//			if (result != null) {
//				row.createCell(cellNum++).setCellValue(result.getCommentaire() == null ? "" : result.getCommentaire().getText());
//				row.createCell(cellNum++).setCellValue(notNull(result.getObjectif()));
//				row.createCell(cellNum++).setCellValue(notNull(result.getResultat()));
//				result = eae.getEaeResultats().iterator().next();
//			} else {
//				row.createCell(cellNum++).setCellValue("");
//				row.createCell(cellNum++).setCellValue("");
//				row.createCell(cellNum++).setCellValue("");
//			}
//			// Objectif 2
//			if (result != null) {
//				row.createCell(cellNum++).setCellValue(result.getCommentaire() == null ? "" : result.getCommentaire().getText());
//				row.createCell(cellNum++).setCellValue(notNull(result.getObjectif()));
//				row.createCell(cellNum++).setCellValue(notNull(result.getResultat()));
//				result = eae.getEaeResultats().iterator().next();
//			} else {
//				row.createCell(cellNum++).setCellValue("");
//				row.createCell(cellNum++).setCellValue("");
//				row.createCell(cellNum++).setCellValue("");
//			}
//			// Objectif 3
//			if (result != null) {
//				row.createCell(cellNum++).setCellValue(result.getCommentaire() == null ? "" : result.getCommentaire().getText());
//				row.createCell(cellNum++).setCellValue(notNull(result.getObjectif()));
//				row.createCell(cellNum++).setCellValue(notNull(result.getResultat()));
//				result = eae.getEaeResultats().iterator().next();
//			} else {
//				row.createCell(cellNum++).setCellValue("");
//				row.createCell(cellNum++).setCellValue("");
//				row.createCell(cellNum++).setCellValue("");
//			}
//			// Objectif 4
//			if (result != null) {
//				row.createCell(cellNum++).setCellValue(result.getCommentaire() == null ? "" : result.getCommentaire().getText());
//				row.createCell(cellNum++).setCellValue(notNull(result.getObjectif()));
//				row.createCell(cellNum++).setCellValue(notNull(result.getResultat()));
//				result = eae.getEaeResultats().iterator().next();
//			} else {
//				row.createCell(cellNum++).setCellValue("");
//				row.createCell(cellNum++).setCellValue("");
//				row.createCell(cellNum++).setCellValue("");
//			}
//			// Objectif 5
//			if (result != null) {
//				row.createCell(cellNum++).setCellValue(result.getCommentaire() == null ? "" : result.getCommentaire().getText());
//				row.createCell(cellNum++).setCellValue(notNull(result.getObjectif()));
//				row.createCell(cellNum++).setCellValue(notNull(result.getResultat()));
//			} else {
//				row.createCell(cellNum++).setCellValue("");
//				row.createCell(cellNum++).setCellValue("");
//				row.createCell(cellNum++).setCellValue("");
//			}
			
			//OBJ_PROG_IND_ANNEE_PREC;	RESULT_OBJ_ANNEE_PREC;	COM_OBJ_ANNEE_PREC
			row.createCell(cellNum++).setCellValue("1");
			row.createCell(cellNum++).setCellValue("");
			row.createCell(cellNum++).setCellValue("");
			
			// QA11
//			row.createCell(cellNum++).setCellValue(eae.getEaeAppreciations().iterator().next().getNoteEvalue());
//			row.createCell(cellNum++).setCellValue(eae.getEaeAppreciations().iterator().next().getNoteEvalue());
//			row.createCell(cellNum++).setCellValue(eae.getEaeAppreciations().iterator().next().getNoteEvalue());
//			row.createCell(cellNum++).setCellValue(eae.getEaeAppreciations().iterator().next().getNoteEvalue());
//			row.createCell(cellNum++).setCellValue(eae.getEaeAppreciations().iterator().next().getNoteEvalue());
//			row.createCell(cellNum++).setCellValue(eae.getEaeAppreciations().iterator().next().getNoteEvalue());
//			row.createCell(cellNum++).setCellValue(eae.getEaeAppreciations().iterator().next().getNoteEvalue());
//			row.createCell(cellNum++).setCellValue(eae.getEaeAppreciations().iterator().next().getNoteEvalue());
//			row.createCell(cellNum++).setCellValue(eae.getEaeAppreciations().iterator().next().getNoteEvalue());
//			row.createCell(cellNum++).setCellValue(eae.getEaeAppreciations().iterator().next().getNoteEvalue());
//			row.createCell(cellNum++).setCellValue(eae.getEaeAppreciations().iterator().next().getNoteEvalue());
//			row.createCell(cellNum++).setCellValue(eae.getEaeAppreciations().iterator().next().getNoteEvalue());
//			row.createCell(cellNum++).setCellValue(eae.getEaeAppreciations().iterator().next().getNoteEvalue());
//			row.createCell(cellNum++).setCellValue(eae.getEaeAppreciations().iterator().next().getNoteEvalue());
//			row.createCell(cellNum++).setCellValue(eae.getEaeAppreciations().iterator().next().getNoteEvalue());
//			row.createCell(cellNum++).setCellValue(eae.getEaeAppreciations().iterator().next().getNoteEvalue());
			//QR11
//			row.createCell(cellNum++).setCellValue(eae.getEaeAppreciations().iterator().next().getNoteEvaluateur());
//			row.createCell(cellNum++).setCellValue(eae.getEaeAppreciations().iterator().next().getNoteEvaluateur());
//			row.createCell(cellNum++).setCellValue(eae.getEaeAppreciations().iterator().next().getNoteEvaluateur());
//			row.createCell(cellNum++).setCellValue(eae.getEaeAppreciations().iterator().next().getNoteEvaluateur());
//			row.createCell(cellNum++).setCellValue(eae.getEaeAppreciations().iterator().next().getNoteEvaluateur());
//			row.createCell(cellNum++).setCellValue(eae.getEaeAppreciations().iterator().next().getNoteEvaluateur());
//			row.createCell(cellNum++).setCellValue(eae.getEaeAppreciations().iterator().next().getNoteEvaluateur());
//			row.createCell(cellNum++).setCellValue(eae.getEaeAppreciations().iterator().next().getNoteEvaluateur());
//			row.createCell(cellNum++).setCellValue(eae.getEaeAppreciations().iterator().next().getNoteEvaluateur());
//			row.createCell(cellNum++).setCellValue(eae.getEaeAppreciations().iterator().next().getNoteEvaluateur());
//			row.createCell(cellNum++).setCellValue(eae.getEaeAppreciations().iterator().next().getNoteEvaluateur());
//			row.createCell(cellNum++).setCellValue(eae.getEaeAppreciations().iterator().next().getNoteEvaluateur());
//			row.createCell(cellNum++).setCellValue(eae.getEaeAppreciations().iterator().next().getNoteEvaluateur());
//			row.createCell(cellNum++).setCellValue(eae.getEaeAppreciations().iterator().next().getNoteEvaluateur());
//			row.createCell(cellNum++).setCellValue(eae.getEaeAppreciations().iterator().next().getNoteEvaluateur());
//			row.createCell(cellNum++).setCellValue(eae.getEaeAppreciations().iterator().next().getNoteEvaluateur());
			// QA11
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
			//QR11
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
			
			// OBJ_ANNEE_SUIV1
			row.createCell(cellNum++).setCellValue("TODO : plan action - objectif");
			row.createCell(cellNum++).setCellValue("TODO : plan action - mesure");
			row.createCell(cellNum++).setCellValue("");
			row.createCell(cellNum++).setCellValue("TODO : plan action - objectif");
			row.createCell(cellNum++).setCellValue("TODO : plan action - mesure");
			row.createCell(cellNum++).setCellValue("");
			row.createCell(cellNum++).setCellValue("TODO : plan action - objectif");
			row.createCell(cellNum++).setCellValue("TODO : plan action - mesure");
			row.createCell(cellNum++).setCellValue("");
			row.createCell(cellNum++).setCellValue("TODO : plan action - objectif");
			row.createCell(cellNum++).setCellValue("TODO : plan action - mesure");
			row.createCell(cellNum++).setCellValue("");
			row.createCell(cellNum++).setCellValue("TODO : plan action - objectif");
			row.createCell(cellNum++).setCellValue("TODO : plan action - mesure");
			row.createCell(cellNum++).setCellValue("");
			row.createCell(cellNum++).setCellValue("typeObj = 2");
			row.createCell(cellNum++).setCellValue("typeObj = 3");
			row.createCell(cellNum++).setCellValue("typeObj = 4");
			row.createCell(cellNum++).setCellValue("typeObj = 5");
			
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
					row.createCell(cellNum++).setCellValue("");
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
			// ZYX4RESULT
			if (eae.getEaeEvaluation().getNiveauEae() != null) {
				String mapping = eae.getEaeEvaluation().getNiveauEae().equals(EaeNiveauEnum.EXCELLENT) ? "0" :
					eae.getEaeEvaluation().getNiveauEae().equals(EaeNiveauEnum.SATISFAISANT) ? "1" :
						eae.getEaeEvaluation().getNiveauEae().equals(EaeNiveauEnum.NECESSITANT_DES_PROGRES) ? "2" : "3";
				row.createCell(cellNum++).setCellValue(mapping);
			} else {
				row.createCell(cellNum++).setCellValue("");
			}
			row.createCell(cellNum++).setCellValue(notNull(eae.getEaeEvaluation().getAvisRevalorisation()));
			row.createCell(cellNum++).setCellValue(notNull(eae.getEaeEvaluation().getAvisChangementClasse()));
			// FORMATEUR_DOMAINE
			row.createCell(cellNum++).setCellValue("");
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
				row.createCell(cellNum++).setCellValue("");
			}
			row.createCell(cellNum++).setCellValue(eae.getEaeEvaluation().getCommentaireEvalue() != null ? eae.getEaeEvaluation().getCommentaireEvalue().getText() : "");
			row.createCell(cellNum++).setCellValue(eae.getEaeEvaluation().getCommentaireAvctEvaluateur() != null ? eae.getEaeEvaluation().getCommentaireAvctEvaluateur().getText() : "");
			row.createCell(cellNum++).setCellValue(eae.getEaeEvaluation().getCommentaireAvctEvalue() != null ? eae.getEaeEvaluation().getCommentaireAvctEvalue().getText() : "");
			// FORMATEUR_DOMAINE_ECH
			row.createCell(cellNum++).setCellValue("");
			row.createCell(cellNum++).setCellValue("");
			// ZYX4TEMPAR
			row.createCell(cellNum++).setCellValue(eae.getEaeEvolution() == null ? "" : notNull(eae.getEaeEvolution().isTempsPartiel()));
			row.createCell(cellNum++).setCellValue(eae.getEaeEvolution() == null ? "" : notNull(eae.getEaeEvolution().isRetraite()));
			row.createCell(cellNum++).setCellValue(eae.getEaeEvolution() == null ? "" : notNull(eae.getEaeEvolution().isAutrePerspective()));
			row.createCell(cellNum++).setCellValue(notNull(eae.getEaeEvaluation().getNoteAnnee()));
			if (eae.getEaeEvalue().getAgent() != null) {
				row.createCell(cellNum++).setCellValue(notNull(eae.getEaeEvalue().getAgent().getNomUsage()));
				row.createCell(cellNum++).setCellValue(notNull(eae.getEaeEvalue().getAgent().getPrenomUsage()));
			} else {
				row.createCell(cellNum++).setCellValue("");
				row.createCell(cellNum++).setCellValue("");
			}
			row.createCell(cellNum++).setCellValue(notNull(eae.getEaeEvalue().getNouvGrade()));
			row.createCell(cellNum++).setCellValue(notNull(eae.getEaeEvalue().getNouvEchelon()));
			row.createCell(cellNum++).setCellValue(notNull(eae.getEaeEvalue().getDateEffetAvancement()));
			row.createCell(cellNum++).setCellValue("");
			row.createCell(cellNum++).setCellValue("");
			
			logger.info("Row number {} wrote, for eae {}.", rowNum, eae.getIdEae());
	    }

	    // Write the output to a file
	    FileOutputStream fileOut = new FileOutputStream("/home/teo/Desktop/testEAE.xlsx");
	    workbook.write(fileOut);
	    fileOut.close();
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
		logger.info("Enter setSouhaits");
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
		logger.info("Exit setSouhaits");
	}
	
	private void setObjectifs(Set<EaeResultat> eaeResultat, Row row) {
		logger.info("Enter setObjectifs");
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
					row.createCell(cellNum++).setCellValue("");
					row.createCell(cellNum++).setCellValue("");
					row.createCell(cellNum++).setCellValue("");
				}
			}
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
			row.createCell(cellNum++).setCellValue("");
			row.createCell(cellNum++).setCellValue("");
		}
		logger.info("Exit setObjectifs");
	}
	
	private String notNull(Object s) {
		return s != null ? s.toString() : "";
	}
}
