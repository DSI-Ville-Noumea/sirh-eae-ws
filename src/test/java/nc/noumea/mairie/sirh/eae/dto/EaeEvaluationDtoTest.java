package nc.noumea.mairie.sirh.eae.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.mock.staticmock.MockStaticEntityMethods;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeCampagne;
import nc.noumea.mairie.sirh.eae.domain.EaeCommentaire;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluation;
import nc.noumea.mairie.sirh.eae.domain.EaeEvalue;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeAgentStatutEnum;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeAvancementEnum;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeNiveauEnum;
import nc.noumea.mairie.sirh.eae.dto.identification.EaeListeDto;

@MockStaticEntityMethods
public class EaeEvaluationDtoTest {

	@Test
	public void testConstructor() {
		// Given
		Eae eae = new Eae();
		eae.setIdEae(123);
		eae.setDureeEntretienMinutes(127);
		EaeEvaluation eval = new EaeEvaluation();
		eval.setEae(eae);
		eval.setNoteAnnee(12f);
		eval.setNoteAnneeN1(13f);
		eval.setNoteAnneeN2(14f);
		eval.setNoteAnneeN3(15f);
		eval.setAvisRevalorisation(true);
		eval.setAvisChangementClasse(false);
		eval.setPropositionAvancement(EaeAvancementEnum.MAXI);
		eval.setNiveauEae(EaeNiveauEnum.INSUFFISANT);
		EaeCommentaire commentaireAvctEvaluateur = new EaeCommentaire();
		commentaireAvctEvaluateur.setText("commentaireAvctEvaluateur");
		eval.setCommentaireAvctEvaluateur(commentaireAvctEvaluateur);
		EaeCommentaire commentaireAvctEvalue = new EaeCommentaire();
		commentaireAvctEvalue.setText("commentaireAvctEvalue");
		eval.setCommentaireAvctEvalue(commentaireAvctEvalue);
		EaeCommentaire commentaireEvaluateur = new EaeCommentaire();
		commentaireEvaluateur.setText("commentaireEvaluateur");
		eval.setCommentaireEvaluateur(commentaireEvaluateur);
		EaeCommentaire commentaireEvalue = new EaeCommentaire();
		commentaireEvalue.setText("commentaireEvalue");
		eval.setCommentaireEvalue(commentaireEvalue);

		// When
		EaeEvaluationDto dto = new EaeEvaluationDto(eval);

		// Then
		assertEquals(123, dto.getIdEae());
		assertEquals(new Integer(127), dto.getDureeEntretien());
		assertEquals(new Float(13), dto.getNoteAnneeN1());
		assertEquals(new Float(14), dto.getNoteAnneeN2());
		assertEquals(new Float(15), dto.getNoteAnneeN3());
		assertTrue(dto.getAvisRevalorisation());
		assertFalse(dto.getAvisChangementClasse());
		assertEquals(EaeNiveauEnum.INSUFFISANT.toString(), dto.getNiveau().getCourant());
		assertEquals(eval.getCommentaireAvctEvaluateur().getText(), dto.getCommentaireAvctEvaluateur().getText());
		assertEquals(eval.getCommentaireAvctEvalue().getText(), dto.getCommentaireAvctEvalue().getText());
		assertEquals(eval.getCommentaireEvaluateur().getText(), dto.getCommentaireEvaluateur().getText());
		assertEquals(eval.getCommentaireEvalue().getText(), dto.getCommentaireEvalue().getText());
	}

	@Test
	public void testConstructor_withDefaultValuesWhenNull() {
		// Given
		Eae eae = new Eae();
		eae.setIdEae(123);
		eae.setCap(true);
		EaeEvaluation eval = new EaeEvaluation();
		eae.setEaeEvaluation(eval);
		eval.setEae(eae);
		EaeEvalue evalue = new EaeEvalue();
		evalue.setStatut(EaeAgentStatutEnum.F);
		eae.setEaeEvalue(evalue);

		EaeCampagne camp = new EaeCampagne();
		camp.setAnnee(2014);
		eae.setEaeCampagne(camp);

		// When
		EaeEvaluationDto dto = new EaeEvaluationDto(eae);

		// Then
		assertEquals(123, dto.getIdEae());
		assertTrue(dto.isCap());
		assertTrue(dto.getAvisRevalorisation());
		assertTrue(dto.getAvisChangementClasse());
		assertEquals(EaeAvancementEnum.MOY.name(), dto.getPropositionAvancement().getCourant());
		assertEquals(camp.getAnnee(), dto.getAnneeAvancement());
	}

	@Test
	public void testGetDureesAvancement_defaultSelectedValue_createSubDtoWithEvalueValues() {
		// Given
		Eae eae = new Eae();
		EaeEvalue evalue = new EaeEvalue();
		evalue.setAvctDureeMin(12);
		evalue.setAvctDureeMoy(24);
		evalue.setAvctDureeMax(36);
		eae.setEaeEvalue(evalue);
		EaeEvaluation evaluation = new EaeEvaluation();
		eae.setEaeEvaluation(evaluation);
		evalue.setStatut(EaeAgentStatutEnum.F);

		// When
		EaeListeDto dto = new EaeEvaluationDto().getDureesAvancement(evaluation, evalue);

		// Then
		assertEquals("MINI", dto.getListe().get(0).getCode());
		assertEquals("Durée minimale (12 mois)", dto.getListe().get(0).getValeur());
		assertEquals("MOY", dto.getListe().get(1).getCode());
		assertEquals("Durée moyenne (24 mois)", dto.getListe().get(1).getValeur());
		assertEquals("MAXI", dto.getListe().get(2).getCode());
		assertEquals("Durée maximale (36 mois)", dto.getListe().get(2).getValeur());

		assertEquals("MOY", dto.getCourant());
	}

	@Test
	public void testGetDureesAvancement_MAXISelectedValue_createSubDtoWithEvalueValues() {
		// Given
		Eae eae = new Eae();
		EaeEvalue evalue = new EaeEvalue();
		evalue.setAvctDureeMin(12);
		evalue.setAvctDureeMoy(24);
		evalue.setAvctDureeMax(36);
		eae.setEaeEvalue(evalue);
		EaeEvaluation evaluation = new EaeEvaluation();
		evaluation.setPropositionAvancement(EaeAvancementEnum.MAXI);
		eae.setEaeEvaluation(evaluation);
		evalue.setStatut(EaeAgentStatutEnum.F);

		// When
		EaeListeDto dto = new EaeEvaluationDto().getDureesAvancement(evaluation, evalue);

		// Then
		assertEquals("MINI", dto.getListe().get(0).getCode());
		assertEquals("Durée minimale (12 mois)", dto.getListe().get(0).getValeur());
		assertEquals("MOY", dto.getListe().get(1).getCode());
		assertEquals("Durée moyenne (24 mois)", dto.getListe().get(1).getValeur());
		assertEquals("MAXI", dto.getListe().get(2).getCode());
		assertEquals("Durée maximale (36 mois)", dto.getListe().get(2).getValeur());

		assertEquals("MAXI", dto.getCourant());
	}
}
