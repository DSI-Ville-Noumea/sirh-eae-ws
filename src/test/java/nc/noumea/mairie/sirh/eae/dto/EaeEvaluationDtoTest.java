package nc.noumea.mairie.sirh.eae.dto;

import static org.junit.Assert.assertEquals;

import java.util.List;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeCommentaire;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluation;
import nc.noumea.mairie.sirh.eae.domain.EaeNiveau;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeAvancementEnum;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeAvisEnum;

import org.junit.Test;

import flexjson.PathExpression;

public class EaeEvaluationDtoTest {

	@Test
	public void testConstructor() {
		// Given 
		Eae eae = new Eae();
		eae.setIdEae(123);
		eae.setDureeEntretienMinutes(127);
		EaeEvaluation eval = new EaeEvaluation();
		eval.setEae(eae);
		eval.setNoteAnnee(12);
		eval.setNoteAnneeN1(13);
		eval.setNoteAnneeN2(14);
		eval.setNoteAnneeN3(15);
		eval.setAvisRevalorisation(EaeAvisEnum.FAVORABLE);
		eval.setAvisChangementClasse(EaeAvisEnum.DEFAVORABLE);
		eval.setPropositionAvancement(EaeAvancementEnum.MAXI);
		eval.setNiveauEae(new EaeNiveau());
		eval.setCommentaireAvctEvaluateur(new EaeCommentaire());
		eval.setCommentaireAvctEvalue(new EaeCommentaire());
		eval.setCommentaireEvaluateur(new EaeCommentaire());
		eval.setCommentaireEvalue(new EaeCommentaire());
		
		// When
		EaeEvaluationDto dto = new EaeEvaluationDto(eval);
			
		// Then
		assertEquals(123, dto.getIdEae());
		assertEquals(new Integer(127), dto.getDureeEntretien());
		assertEquals(new Integer(12), dto.getNoteAnnee());
		assertEquals(new Integer(13), dto.getNoteAnneeN1());
		assertEquals(new Integer(14), dto.getNoteAnneeN2());
		assertEquals(new Integer(15), dto.getNoteAnneeN3());
		assertEquals(EaeAvisEnum.FAVORABLE, dto.getAvisRevalorisation());
		assertEquals(EaeAvisEnum.DEFAVORABLE, dto.getAvisChangementClasse());
		assertEquals(EaeAvancementEnum.MAXI, dto.getPropositionAvancement());
		assertEquals(eval.getNiveauEae(), dto.getNiveau());
		assertEquals(eval.getCommentaireAvctEvaluateur(), dto.getCommentaireAvctEvaluateur());
		assertEquals(eval.getCommentaireAvctEvalue(), dto.getCommentaireAvctEvalue());
		assertEquals(eval.getCommentaireEvaluateur(), dto.getCommentaireEvaluateur());
		assertEquals(eval.getCommentaireEvalue(), dto.getCommentaireEvalue());
		
	}
	
	@Test
	public void testGetSerializerForEaeEvaluationDto_includes_excludes() {
		
		// When
		List<PathExpression> includes = EaeEvaluationDto.getSerializerForEaeEvaluationDto().getIncludes();
		List<PathExpression> excludes = EaeEvaluationDto.getSerializerForEaeEvaluationDto().getExcludes();
		
		// Then
		assertEquals(14, includes.size());
		assertEquals("[idEae]", includes.get(0).toString());
		assertEquals("[noteAnnee]", includes.get(1).toString());
		assertEquals("[noteAnneeN1]", includes.get(2).toString());
		assertEquals("[noteAnneeN2]", includes.get(3).toString());
		assertEquals("[noteAnneeN3]", includes.get(4).toString());
		assertEquals("[avisRevalorisation]", includes.get(5).toString());
		assertEquals("[propositionAvancement]", includes.get(6).toString());
		assertEquals("[avisChangementClasse]", includes.get(7).toString());
		assertEquals("[niveau]", includes.get(8).toString());
		assertEquals("[commentaireEvaluateur]", includes.get(9).toString());
		assertEquals("[commentaireEvalue]", includes.get(10).toString());
		assertEquals("[commentaireAvctEvaluateur]", includes.get(11).toString());
		assertEquals("[commentaireAvctEvalue]", includes.get(12).toString());
		assertEquals("[dureeEntretien]", includes.get(13).toString());
		
		assertEquals(1, excludes.size());
		assertEquals("[*]", excludes.get(0).toString());
		
	}
	
	//@Test
	public void testSerializeInJSON_emptyObject() {
		// Given
		EaeEvaluationDto dto = new EaeEvaluationDto();
		String expectedJson = "{\"avisChangementClasse\":null,\"avisRevalorisation\":null,\"commentaireAvctEvaluateur\":null,\"commentaireAvctEvalue\":null,\"commentaireEvaluateur\":null,\"commentaireEvalue\":null,\"dureeEntretien\":null,\"idEae\":0,\"niveau\":null,\"noteAnnee\":null,\"noteAnneeN1\":null,\"noteAnneeN2\":null,\"noteAnneeN3\":null,\"propositionAvancement\":null}";
		
		// When
		String json = dto.serializeInJSON();
		
		// Then
		assertEquals(expectedJson, json);
	}
	
	//@Test
	public void testSerializeInJSON_FilledObject() {
		// Given
		EaeEvaluationDto dto = new EaeEvaluationDto();
		dto.setIdEae(13);
		dto.setDureeEntretien(127);
		dto.setNoteAnnee(12);
		dto.setNoteAnneeN1(13);
		dto.setNoteAnneeN2(14);
		dto.setNoteAnneeN3(15);
		dto.setAvisRevalorisation(EaeAvisEnum.FAVORABLE);
		dto.setAvisChangementClasse(EaeAvisEnum.DEFAVORABLE);
		dto.setPropositionAvancement(EaeAvancementEnum.MAXI);
		EaeNiveau niv = new EaeNiveau();
		niv.setIdEaeNiveau(2);
		niv.setLibelleNiveauEae("Satisfaisant");
		dto.setNiveau(niv);
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
		
		
		String expectedJson = "{\"avisChangementClasse\":null,\"avisRevalorisation\":null,\"commentaireAvctEvaluateur\":null,\"commentaireAvctEvalue\":null,\"commentaireEvaluateur\":null,\"commentaireEvalue\":null,\"dureeEntretien\":null,\"idEae\":0,\"niveau\":null,\"noteAnnee\":null,\"noteAnneeN1\":null,\"noteAnneeN2\":null,\"noteAnneeN3\":null,\"propositionAvancement\":null}";
		
		// When
		String json = dto.serializeInJSON();
		
		// Then
		assertEquals(expectedJson, json);
	}
}
