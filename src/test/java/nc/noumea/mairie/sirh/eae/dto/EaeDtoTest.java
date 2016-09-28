package nc.noumea.mairie.sirh.eae.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeCampagne;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluateur;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluation;
import nc.noumea.mairie.sirh.eae.domain.EaeEvalue;
import nc.noumea.mairie.sirh.eae.domain.EaeFichePoste;
import nc.noumea.mairie.sirh.eae.domain.EaeFinalisation;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeEtatEnum;

import org.junit.Test;

public class EaeDtoTest {

	@Test
	public void testConstructor_modeIncomplet(){
		
		Date dateEntretien = new Date();
		EaeCampagne eaeCampagne = new EaeCampagne();
		EaeEvaluateur evaluateur = new EaeEvaluateur();
		
		Eae eae = new Eae();
		eae.setIdEae(1);
		eae.setEtat(EaeEtatEnum.CO);
		eae.setDateEntretien(dateEntretien);
		eae.getEaeEvaluateurs().add(evaluateur);
		eae.setEaeCampagne(eaeCampagne);
		
		EaeDto dto = new EaeDto(eae, false);
		
		assertEquals(dto.getIdEae(), eae.getIdEae());
		assertEquals(dto.getEtat(), EaeEtatEnum.CO.name());
		assertEquals(dto.getDateEntretien(), dateEntretien);
		assertEquals(dto.getEvaluateurs().size(), 1);
		assertNotNull(dto.getCampagne());
	}
	
	@Test
	public void testConstructor_modeComplet(){
		
		Eae eae = new Eae();
		
		Date dateEntretien = new Date();
		EaeCampagne eaeCampagne = new EaeCampagne();
		EaeEvaluateur evaluateur = new EaeEvaluateur();
		EaeEvalue eaeEvalue = new EaeEvalue();
		EaeFichePoste eaeFichePoste = new EaeFichePoste();
			eaeFichePoste.setPrimary(true);
			eaeFichePoste.setEae(eae);
		EaeEvaluation eaeEvaluation = new EaeEvaluation();
			eaeEvaluation.setEae(eae);
		
		EaeFinalisation eaeFinalisation = new EaeFinalisation();
		
		eae.setIdEae(1);
		eae.setEtat(EaeEtatEnum.CO);
		eae.setDateEntretien(dateEntretien);
		eae.getEaeEvaluateurs().add(evaluateur);
		eae.setEaeCampagne(eaeCampagne);
		eae.setEaeEvalue(eaeEvalue);
		eae.getEaeFichePostes().add(eaeFichePoste);
		eae.setEaeEvaluation(eaeEvaluation);
		eae.getEaeFinalisations().add(eaeFinalisation);
		
		EaeDto dto = new EaeDto(eae, true);
		
		assertEquals(dto.getIdEae(), eae.getIdEae());
		assertEquals(dto.getEtat(), EaeEtatEnum.CO.name());
		assertEquals(dto.getDateEntretien(), dateEntretien);
		assertEquals(dto.getEvaluateurs().size(), 1);
		assertNotNull(dto.getCampagne());
		assertNotNull(dto.getEvalue());
		assertNotNull(dto.getFichePoste());
		assertNotNull(dto.getEvaluation());
		assertNotNull(dto.getPlanAction());
		assertNotNull(dto.getEvolution());
		assertNotNull(dto.getFinalisation());
	}
}
