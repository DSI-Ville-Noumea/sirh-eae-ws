package nc.noumea.mairie.sirh.eae.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeCampagne;
import nc.noumea.mairie.sirh.eae.domain.EaeCommentaire;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluation;
import nc.noumea.mairie.sirh.eae.domain.EaeEvalue;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeAgentStatutEnum;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeAvancementEnum;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeNiveauEnum;
import nc.noumea.mairie.sirh.eae.dto.util.ValueWithListDto;

import org.junit.Test;
import org.springframework.mock.staticmock.MockStaticEntityMethods;

import flexjson.PathExpression;

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
		eval.setCommentaireAvctEvaluateur(new EaeCommentaire());
		eval.setCommentaireAvctEvalue(new EaeCommentaire());
		eval.setCommentaireEvaluateur(new EaeCommentaire());
		eval.setCommentaireEvalue(new EaeCommentaire());

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
		assertEquals(eval.getCommentaireAvctEvaluateur(), dto.getCommentaireAvctEvaluateur());
		assertEquals(eval.getCommentaireAvctEvalue(), dto.getCommentaireAvctEvalue());
		assertEquals(eval.getCommentaireEvaluateur(), dto.getCommentaireEvaluateur());
		assertEquals(eval.getCommentaireEvalue(), dto.getCommentaireEvalue());
	}

	@Test
	public void testConstructor_withDefaultValuesWhenNull() {
		// Given
		Eae eae = new Eae();
		eae.setIdEae(123);
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
		assertTrue(dto.getAvisRevalorisation());
		assertTrue(dto.getAvisChangementClasse());
		assertEquals(EaeAvancementEnum.MOY.name(), dto.getPropositionAvancement().getCourant());
		assertEquals(camp.getAnnee(), dto.getAnneeAvancement());
	}

	@Test
	public void testGetSerializerForEaeEvaluationDto_includes_excludes() {

		// When
		List<PathExpression> includes = EaeEvaluationDto.getSerializerForEaeEvaluationDto().getIncludes();
		List<PathExpression> excludes = EaeEvaluationDto.getSerializerForEaeEvaluationDto().getExcludes();

		// Then
		assertEquals(16, includes.size());
		assertEquals("[idEae]", includes.get(0).toString());
		assertEquals("[noteAnneeN1]", includes.get(1).toString());
		assertEquals("[noteAnneeN2]", includes.get(2).toString());
		assertEquals("[noteAnneeN3]", includes.get(3).toString());
		assertEquals("[avisRevalorisation]", includes.get(4).toString());
		assertEquals("[propositionAvancement,*]", includes.get(5).toString());
		assertEquals("[avisChangementClasse]", includes.get(6).toString());
		assertEquals("[niveau,*]", includes.get(7).toString());
		assertEquals("[commentaireEvaluateur]", includes.get(8).toString());
		assertEquals("[commentaireEvalue]", includes.get(9).toString());
		assertEquals("[commentaireAvctEvaluateur]", includes.get(10).toString());
		assertEquals("[commentaireAvctEvalue]", includes.get(11).toString());
		assertEquals("[dureeEntretien]", includes.get(12).toString());
		assertEquals("[anneeAvancement]", includes.get(13).toString());
		assertEquals("[statut]", includes.get(14).toString());
		assertEquals("[typeAvct]", includes.get(15).toString());

		assertEquals(2, excludes.size());
		assertEquals("[*,class]", excludes.get(0).toString());
		assertEquals("[*]", excludes.get(1).toString());
	}

	@Test
	public void testSerializeInJSON_emptyObject() {
		// Given
		EaeEvaluationDto dto = new EaeEvaluationDto();
		String expectedJson = "{\"anneeAvancement\":0,\"avisChangementClasse\":null,\"avisRevalorisation\":null,\"commentaireAvctEvaluateur\":null,\"commentaireAvctEvalue\":null,\"commentaireEvaluateur\":null,\"commentaireEvalue\":null,\"dureeEntretien\":null,\"idEae\":0,\"niveau\":null,\"noteAnneeN1\":null,\"noteAnneeN2\":null,\"noteAnneeN3\":null,\"propositionAvancement\":null,\"statut\":null,\"typeAvct\":null}";

		// When
		String json = dto.serializeInJSON();

		// Then
		assertEquals(expectedJson, json);
	}

	@Test
	public void testSerializeInJSON_FilledObject() {
		// Given
		EaeEvaluationDto dto = new EaeEvaluationDto();
		dto.setIdEae(13);
		dto.setDureeEntretien(127);
		dto.setNoteAnneeN1(13.03f);
		dto.setNoteAnneeN2(14.04f);
		dto.setNoteAnneeN3(15.05f);
		dto.setAvisRevalorisation(true);
		dto.setAvisChangementClasse(false);
		ValueWithListDto subDto = new ValueWithListDto(EaeAvancementEnum.MAXI, EaeAvancementEnum.class);
		dto.setPropositionAvancement(subDto);
		dto.setAnneeAvancement(2014);
		dto.setStatut("F");
		dto.setTypeAvct("AD");

		ValueWithListDto subDto2 = new ValueWithListDto(EaeNiveauEnum.EXCELLENT, EaeNiveauEnum.class);
		dto.setNiveau(subDto2);
		EaeCommentaire com1 = new EaeCommentaire();
		com1.setText("com1");
		dto.setCommentaireAvctEvaluateur(com1);
		EaeCommentaire com2 = new EaeCommentaire();
		com2.setText("com2");
		dto.setCommentaireAvctEvalue(com2);
		EaeCommentaire com3 = new EaeCommentaire();
		com3.setText("com3");
		dto.setCommentaireEvaluateur(com3);
		EaeCommentaire com4 = new EaeCommentaire();
		com4.setText("com4");
		dto.setCommentaireEvalue(com4);

		String expectedJson = "{\"anneeAvancement\":2014,\"avisChangementClasse\":false,\"avisRevalorisation\":true,\"commentaireAvctEvaluateur\":\"com1\",\"commentaireAvctEvalue\":\"com2\",\"commentaireEvaluateur\":\"com3\",\"commentaireEvalue\":\"com4\",\"dureeEntretien\":{\"heures\":2,\"minutes\":7},\"idEae\":13,\"niveau\":{\"courant\":\"EXCELLENT\",\"liste\":[{\"code\":\"EXCELLENT\",\"valeur\":\"EXCELLENT\"},{\"code\":\"SATISFAISANT\",\"valeur\":\"SATISFAISANT\"},{\"code\":\"NECESSITANT_DES_PROGRES\",\"valeur\":\"NECESSITANT DES PROGRES\"},{\"code\":\"INSUFFISANT\",\"valeur\":\"INSUFFISANT\"}]},\"noteAnneeN1\":13.03,\"noteAnneeN2\":14.04,\"noteAnneeN3\":15.05,\"propositionAvancement\":{\"courant\":\"MAXI\",\"liste\":[{\"code\":\"MINI\",\"valeur\":\"Durée minimale\"},{\"code\":\"MOY\",\"valeur\":\"Durée moyenne\"},{\"code\":\"MAXI\",\"valeur\":\"Durée maximale\"}]},\"statut\":\"F\",\"typeAvct\":\"AD\"}";

		// When
		String json = dto.serializeInJSON();

		// Then
		assertEquals(expectedJson, json);
	}

	@Test
	public void testDeserializeFromNullObjectJSON() {
		// Given
		String json = "{\"anneeAvancement\":2014,\"avisChangementClasse\":null,\"avisRevalorisation\":null,\"commentaireAvctEvaluateur\":null,\"commentaireAvctEvalue\":null,\"commentaireEvaluateur\":null,\"commentaireEvalue\":null,\"dureeEntretien\":null,\"idEae\":194,\"niveau\":{\"courant\":null,\"liste\":[{\"code\":\"1\",\"valeur\":\"EXCELLENT\"},{\"code\":\"2\",\"valeur\":\"SATISFAISANT\"},{\"code\":\"3\",\"valeur\":\"NECESSITANT DES PROGRES\"},{\"code\":\"4\",\"valeur\":\"INSUFFISANT\"}]},\"noteAnnee\":null,\"noteAnneeN1\":null,\"noteAnneeN2\":null,\"noteAnneeN3\":null,\"propositionAvancement\":{\"courant\":null,\"liste\":[{\"code\":\"MINI\",\"valeur\":\"Durée minimale\"},{\"code\":\"MOY\",\"valeur\":\"Durée moyenne\"},{\"code\":\"MAXI\",\"valeur\":\"Durée maximale\"}]}}";

		// When
		EaeEvaluationDto dto = new EaeEvaluationDto().deserializeFromJSON(json);

		// Then
		assertNull(dto.getAvisChangementClasse());
		assertNull(dto.getAvisRevalorisation());
		assertNull(dto.getCommentaireEvaluateur());
		assertNull(dto.getCommentaireEvalue());
		assertNull(dto.getCommentaireAvctEvaluateur());
		assertNull(dto.getCommentaireAvctEvalue());
		assertNull(dto.getDureeEntretien());
		assertNull(dto.getNiveau().getCourant());
		assertNull(dto.getPropositionAvancement().getCourant());
	}

	@Test
	public void testDeserializeFromFilledObjectJSON() {
		// Given
		String json = "{\"avisChangementClasse\":true,\"avisRevalorisation\":true,\"commentaireAvctEvaluateur\":\"com3\",\"commentaireAvctEvalue\":\"com4\",\"commentaireEvaluateur\":\"com1\",\"commentaireEvalue\":\"com2\",\"dureeEntretien\":{\"heures\":1,\"minutes\":43},\"idEae\":194,\"niveau\":{\"courant\":\"SATISFAISANT\",\"liste\":[{\"code\":\"EXCELLENT\",\"valeur\":\"EXCELLENT\"},{\"code\":\"SATISFAISANT\",\"valeur\":\"SATISFAISANT\"},{\"code\":\"NECESSITANT_DES_PROGRES\",\"valeur\":\"NECESSITANT DES PROGRES\"},{\"code\":\"INSUFFISANT\",\"valeur\":\"INSUFFISANT\"}]},\"noteAnnee\":13.07,\"noteAnneeN1\":1,\"noteAnneeN2\":2,\"noteAnneeN3\":3,\"propositionAvancement\":{\"courant\":\"MAXI\",\"liste\":[{\"code\":\"MINI\",\"valeur\":\"Minimale\"},{\"code\":\"MOY\",\"valeur\":\"Moyenne\"},{\"code\":\"MAXI\",\"valeur\":\"Maximale\"}]}}";

		// When
		EaeEvaluationDto dto = new EaeEvaluationDto().deserializeFromJSON(json);

		// Then
		assertTrue(dto.getAvisChangementClasse());
		assertTrue(dto.getAvisRevalorisation());
		assertEquals("com1", dto.getCommentaireEvaluateur().getText());
		assertEquals("com2", dto.getCommentaireEvalue().getText());
		assertEquals("com3", dto.getCommentaireAvctEvaluateur().getText());
		assertEquals("com4", dto.getCommentaireAvctEvalue().getText());
		assertEquals(new Integer(103), dto.getDureeEntretien());
		assertEquals("SATISFAISANT", dto.getNiveau().getCourant());
		assertEquals("MAXI", dto.getPropositionAvancement().getCourant());
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
		ValueWithListDto dto = new EaeEvaluationDto().getDureesAvancement(evaluation, evalue);

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
		ValueWithListDto dto = new EaeEvaluationDto().getDureesAvancement(evaluation, evalue);

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
