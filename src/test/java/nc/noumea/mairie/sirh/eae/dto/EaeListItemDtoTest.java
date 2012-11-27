package nc.noumea.mairie.sirh.eae.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import nc.noumea.mairie.sirh.domain.Agent;
import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluateur;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluation;
import nc.noumea.mairie.sirh.eae.domain.EaeEvalue;
import nc.noumea.mairie.sirh.eae.domain.EaeFichePoste;
import nc.noumea.mairie.sirh.eae.domain.EaeFinalisation;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeEtatEnum;

import org.junit.Test;

import flexjson.PathExpression;

public class EaeListItemDtoTest {

	@Test
	public void testBuildEaeListItemDto_FromEae() {
		
		// Given
		Eae eaeItem = new Eae();
		eaeItem.setIdEae(6789);
		
		Agent agentEvalue = new Agent();
		agentEvalue.setIdAgent(12);
		agentEvalue.setNomMarital("toto");
		agentEvalue.setPrenom("titi");
		EaeEvalue evalue = new EaeEvalue();
		evalue.setAgent(agentEvalue);
		eaeItem.setEaeEvalue(evalue);
		
		Agent agentDelegataire = new Agent();
		agentDelegataire.setIdAgent(45);
		agentDelegataire.setNomMarital("toto");
		agentDelegataire.setPrenom("titi");
		eaeItem.setAgentDelegataire(agentDelegataire);
		
		EaeEvaluateur ev1 = new EaeEvaluateur();
		ev1.setIdAgent(34);
		eaeItem.getEaeEvaluateurs().add(ev1);
		
		Agent agentShd = new Agent();
		agentShd.setIdAgent(78);
		agentShd.setNomMarital("tutu");
		agentShd.setPrenom("ttcc");
		EaeFichePoste fdp = new EaeFichePoste();
		fdp.setAgentShd(agentShd);
		fdp.setService("service");
		fdp.setDirectionService("direction service");
		fdp.setSectionService("section service");
		fdp.setPrimary(true);
		eaeItem.getEaeFichePostes().add(fdp);
		
		eaeItem.setEtat(EaeEtatEnum.ND);
		eaeItem.setCap(true);
		eaeItem.setDocAttache(true);
		
		Calendar c = new GregorianCalendar();
		c.set(2009, 04, 12);
		
		eaeItem.setDateCreation(c.getTime());
		eaeItem.setDateFinalisation(c.getTime());
		eaeItem.setDateControle(c.getTime());
		
		EaeEvaluation eval = new EaeEvaluation();
		eval.setAvisShd("Minimale");
		
		eaeItem.setEaeEvaluation(eval);
		
		// When
		EaeListItemDto dto = new EaeListItemDto(eaeItem);
		
		// Then
		assertEquals(dto.getIdEae(), eaeItem.getIdEae());
		assertEquals(dto.getAgentEvalue(), eaeItem.getEaeEvalue().getAgent());
		assertEquals(dto.getAgentDelegataire(), eaeItem.getAgentDelegataire());
		assertEquals(dto.getEaeEvaluateurs().size(), eaeItem.getEaeEvaluateurs().size());
		assertEquals(dto.getEaeEvaluateurs().get(0), eaeItem.getEaeEvaluateurs().iterator().next());
		assertEquals(dto.getEtat(), eaeItem.getEtat());
		assertEquals(dto.isCap(), eaeItem.isCap());
		assertEquals(dto.isDocAttache(), eaeItem.isDocAttache());
		assertEquals(dto.getDateCreation(), eaeItem.getDateCreation());
		assertEquals(dto.getDateFinalisation(), eaeItem.getDateFinalisation());
		assertEquals(dto.getDateControle(), eaeItem.getDateControle());
		assertEquals(dto.getAvisShd(), eaeItem.getEaeEvaluation().getAvisShd());
	}
	
	@Test
	public void testBuildEaeListItemDto_EaeNotF_droitImprimerFalse() {
		
		// Given
		Eae eaeItem = new Eae();
		eaeItem.setIdEae(6789);
		eaeItem.setEtat(EaeEtatEnum.C);
		EaeFinalisation fi = new EaeFinalisation();
		fi.setIdGedDocument("theId");
		eaeItem.getEaeFinalisations().add(fi);
		
		// When
		EaeListItemDto dto = new EaeListItemDto(eaeItem);
		
		// Then
		assertFalse(dto.isDroitImprimer());
		assertNull(dto.getIdDocumentGed());
	}
	
	@Test
	public void testBuildEaeListItemDto_EaeIsF_droitImprimerTrueAndIdDocumentGedSet() {
		
		// Given
		Eae eaeItem = new Eae();
		eaeItem.setIdEae(6789);
		eaeItem.setEtat(EaeEtatEnum.F);
		EaeFinalisation fi = new EaeFinalisation();
		fi.setIdGedDocument("theId");
		eaeItem.getEaeFinalisations().add(fi);
		
		// When
		EaeListItemDto dto = new EaeListItemDto(eaeItem);
		
		// Then
		assertTrue(dto.isDroitImprimer());
		assertEquals("theId", dto.getIdDocumentGed());
	}
	
	@Test
	public void testSetAccessRight_AgentIsNotDelegataireNorEvaluateurAndEtatIsND() {
		// Given
		Eae eae = new Eae();
		eae.setEtat(EaeEtatEnum.ND);
		
		// When
		EaeListItemDto dto = new EaeListItemDto(eae);
		dto.setAccessRightsForAgentId(eae, 789);
		
		// Then
		assertFalse(dto.isDroitInitialiser());
		assertFalse(dto.isDroitDemarrer());
		assertFalse(dto.isDroitAcceder());
		
	}
	
	@Test
	public void testSetAccessRight_AgentIsDelegataireAndEtatIsND() {
		// Given
		Eae eae = new Eae();
		eae.setEtat(EaeEtatEnum.ND);
		eae.setIdAgentDelegataire(789);
		
		// When
		EaeListItemDto dto = new EaeListItemDto(eae);
		dto.setAccessRightsForAgentId(eae, 789);
		
		// Then
		assertTrue(dto.isDroitInitialiser());
		assertFalse(dto.isDroitDemarrer());
		assertFalse(dto.isDroitAcceder());
	}
	
	@Test
	public void testSetAccessRight_AgentIsEvaluateurAndEtatIsND() {
		// Given
		Eae eae = new Eae();
		eae.setEtat(EaeEtatEnum.ND);
		EaeEvaluateur eval = new EaeEvaluateur();
		eval.setIdAgent(789);
		eae.getEaeEvaluateurs().add(eval);
		
		// When
		EaeListItemDto dto = new EaeListItemDto(eae);
		dto.setAccessRightsForAgentId(eae, 789);
		
		// Then
		assertTrue(dto.isDroitInitialiser());
		assertFalse(dto.isDroitDemarrer());
		assertFalse(dto.isDroitAcceder());
	}
	
	@Test
	public void testSetAccessRight_AgentIsNotDelegataireNorEvaluateurAndEtatIsC() {
		// Given
		Eae eae = new Eae();
		eae.setEtat(EaeEtatEnum.C);
		
		// When
		EaeListItemDto dto = new EaeListItemDto(eae);
		dto.setAccessRightsForAgentId(eae, 789);
		
		// Then
		assertFalse(dto.isDroitInitialiser());
		assertFalse(dto.isDroitDemarrer());
		assertTrue(dto.isDroitAcceder());
		
	}
	
	@Test
	public void testSetAccessRight_AgentIsDelegataireAndEtatIsC() {
		// Given
		Eae eae = new Eae();
		eae.setEtat(EaeEtatEnum.C);
		eae.setIdAgentDelegataire(789);
		
		// When
		EaeListItemDto dto = new EaeListItemDto(eae);
		dto.setAccessRightsForAgentId(eae, 789);
		
		// Then
		assertFalse(dto.isDroitInitialiser());
		assertTrue(dto.isDroitDemarrer());
		assertTrue(dto.isDroitAcceder());
	}
	
	@Test
	public void testSetAccessRight_AgentIsEvaluateurAndEtatIsC() {
		// Given
		Eae eae = new Eae();
		eae.setEtat(EaeEtatEnum.C);
		EaeEvaluateur eval = new EaeEvaluateur();
		eval.setIdAgent(789);
		eae.getEaeEvaluateurs().add(eval);
		
		// When
		EaeListItemDto dto = new EaeListItemDto(eae);
		dto.setAccessRightsForAgentId(eae, 789);
		
		// Then
		assertFalse(dto.isDroitInitialiser());
		assertTrue(dto.isDroitDemarrer());
		assertTrue(dto.isDroitAcceder());
	}
	
	@Test
	public void testSetAccessRight_AgentIsNotDelegataireNorEvaluateurAndEtatIsEC() {
		// Given
		Eae eae = new Eae();
		eae.setEtat(EaeEtatEnum.EC);
		
		// When
		EaeListItemDto dto = new EaeListItemDto(eae);
		dto.setAccessRightsForAgentId(eae, 789);
		
		// Then
		assertFalse(dto.isDroitInitialiser());
		assertFalse(dto.isDroitDemarrer());
		assertTrue(dto.isDroitAcceder());
		
	}
	
	@Test
	public void testSetAccessRight_AgentIsDelegataireAndEtatIsEC() {
		// Given
		Eae eae = new Eae();
		eae.setEtat(EaeEtatEnum.EC);
		eae.setIdAgentDelegataire(789);
		
		// When
		EaeListItemDto dto = new EaeListItemDto(eae);
		dto.setAccessRightsForAgentId(eae, 789);
		
		// Then
		assertFalse(dto.isDroitInitialiser());
		assertTrue(dto.isDroitDemarrer());
		assertTrue(dto.isDroitAcceder());
	}
	
	@Test
	public void testSetAccessRight_AgentIsEvaluateurAndEtatIsEC() {
		// Given
		Eae eae = new Eae();
		eae.setEtat(EaeEtatEnum.EC);
		EaeEvaluateur eval = new EaeEvaluateur();
		eval.setIdAgent(789);
		eae.getEaeEvaluateurs().add(eval);
		
		// When
		EaeListItemDto dto = new EaeListItemDto(eae);
		dto.setAccessRightsForAgentId(eae, 789);
		
		// Then
		assertFalse(dto.isDroitInitialiser());
		assertTrue(dto.isDroitDemarrer());
		assertTrue(dto.isDroitAcceder());
	}
	
	@Test
	public void testSetAccessRight_AgentIsNotDelegataireNorEvaluateurAndEtatIsF() {
		// Given
		Eae eae = new Eae();
		eae.setEtat(EaeEtatEnum.F);
		eae.getEaeFinalisations().add(new EaeFinalisation());
		
		// When
		EaeListItemDto dto = new EaeListItemDto(eae);
		dto.setAccessRightsForAgentId(eae, 789);
		
		// Then
		assertFalse(dto.isDroitInitialiser());
		assertFalse(dto.isDroitDemarrer());
		assertTrue(dto.isDroitAcceder());
		
	}
	
	@Test
	public void testSetAccessRight_AgentIsDelegataireAndEtatIsF() {
		// Given
		Eae eae = new Eae();
		eae.setEtat(EaeEtatEnum.F);
		eae.setIdAgentDelegataire(789);
		eae.getEaeFinalisations().add(new EaeFinalisation());
		
		// When
		EaeListItemDto dto = new EaeListItemDto(eae);
		dto.setAccessRightsForAgentId(eae, 789);
		
		// Then
		assertFalse(dto.isDroitInitialiser());
		assertFalse(dto.isDroitDemarrer());
		assertTrue(dto.isDroitAcceder());
	}
	
	@Test
	public void testSetAccessRight_AgentIsEvaluateurAndEtatIsF() {
		// Given
		Eae eae = new Eae();
		eae.setEtat(EaeEtatEnum.F);
		EaeEvaluateur eval = new EaeEvaluateur();
		eval.setIdAgent(789);
		eae.getEaeEvaluateurs().add(eval);
		eae.getEaeFinalisations().add(new EaeFinalisation());
		
		// When
		EaeListItemDto dto = new EaeListItemDto(eae);
		dto.setAccessRightsForAgentId(eae, 789);
		
		// Then
		assertFalse(dto.isDroitInitialiser());
		assertFalse(dto.isDroitDemarrer());
		assertTrue(dto.isDroitAcceder());
	}
	
	@Test
	public void testGetSerializerForEaeItemListDto_ListAllIncludesExcludes() {
		
		// When
		List<PathExpression> includes = EaeListItemDto.getSerializerForEaeListItemDto().getIncludes();
		List<PathExpression> excludes = EaeListItemDto.getSerializerForEaeListItemDto().getExcludes();
		
		// Then
		assertEquals(18, includes.size());
		assertEquals("[agentEvalue]", includes.get(0).toString());
		assertEquals("[etat]", includes.get(1).toString());
		assertEquals("[cap]", includes.get(2).toString());
		assertEquals("[docAttache]", includes.get(3).toString());
		assertEquals("[dateCreation]", includes.get(4).toString());
		assertEquals("[dateFinalisation]", includes.get(5).toString());
		assertEquals("[dateControle]", includes.get(6).toString());
		assertEquals("[agentDelegataire]", includes.get(7).toString());
		assertEquals("[avisShd]", includes.get(8).toString());
		assertEquals("[idEae]", includes.get(9).toString());
		assertEquals("[eaeEvaluateurs]", includes.get(10).toString());
		assertEquals("[eaeFichePoste]", includes.get(11).toString());
		assertEquals("[droitInitialiser]", includes.get(12).toString());
		assertEquals("[droitAcceder]", includes.get(13).toString());
		assertEquals("[droitDemarrer]", includes.get(14).toString());
		assertEquals("[droitAffecterDelegataire]", includes.get(15).toString());
		assertEquals("[droitImprimer]", includes.get(16).toString());
		assertEquals("[idDocumentGed]", includes.get(17).toString());
		
		assertEquals(1, excludes.size());
		assertEquals("[*]", excludes.get(0).toString());
	}
	
	@Test
	public void testGetSerializerForEaeItemListDto_AgentEvalue() {
		
		// Given
		EaeListItemDto eae = new EaeListItemDto();
		eae.setIdEae(18);
		
		Agent agent = new Agent();
		agent.setIdAgent(999);
		agent.setNomPatronymique("Duck");
		agent.setPrenom("Donald");
		eae.setAgentEvalue(agent);
		
		String expectedResult = "\"agentEvalue\":{\"idAgent\":999,\"nom\":\"Duck\",\"prenom\":\"Donald\"}";
		
		// When
		String result = EaeListItemDto.getSerializerForEaeListItemDto().serialize(eae);
		
		// Then
		assertTrue(result.contains(expectedResult));
	}
	
	@Test
	public void testGetSerializerForEaeItemListDto_AgentShd() {
		
		// Given
		EaeListItemDto eae = new EaeListItemDto();
		eae.setIdEae(18);
		
		EaeFichePoste fdp = new EaeFichePoste();
		eae.setEaeFichePoste(fdp);
		Agent agent = new Agent();
		agent.setIdAgent(999);
		agent.setNomPatronymique("Duck");
		agent.setPrenom("Donald");
		fdp.setAgentShd(agent);
		
		String expectedResult = "\"agentShd\":{\"idAgent\":999,\"nom\":\"Duck\",\"prenom\":\"Donald\"}";
		
		// When
		String result = EaeListItemDto.getSerializerForEaeListItemDto().serialize(eae);
		
		// Then
		assertTrue(result.contains(expectedResult));
	}
	
	@Test
	public void testGetSerializerForEaeItemListDto_AgentDelegataire() {
		
		// Given
		EaeListItemDto eae = new EaeListItemDto();
		eae.setIdEae(18);
		
		Agent agent = new Agent();
		agent.setIdAgent(999);
		agent.setNomPatronymique("Duck");
		agent.setPrenom("Donald");
		eae.setAgentDelegataire(agent);
		
		String expectedResult = "\"agentDelegataire\":{\"idAgent\":999,\"nom\":\"Duck\",\"prenom\":\"Donald\"}";
		
		// When
		String result = EaeListItemDto.getSerializerForEaeListItemDto().serialize(eae);
		
		// Then
		assertTrue(result.contains(expectedResult));
	}
}
