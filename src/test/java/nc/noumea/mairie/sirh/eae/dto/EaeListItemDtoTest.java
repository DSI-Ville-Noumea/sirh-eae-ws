package nc.noumea.mairie.sirh.eae.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.GregorianCalendar;

import nc.noumea.mairie.sirh.domain.Agent;
import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluateur;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluation;
import nc.noumea.mairie.sirh.eae.domain.EaeEvalue;
import nc.noumea.mairie.sirh.eae.domain.EaeFichePoste;
import nc.noumea.mairie.sirh.eae.domain.EaeFinalisation;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeEtatEnum;

import org.junit.Test;

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
		assertEquals(dto.getAgentEvalue().getIdAgent(), eaeItem.getEaeEvalue().getAgent().getIdAgent());
		assertEquals(dto.getAgentEvalue().getNom(), eaeItem.getEaeEvalue().getAgent().getDisplayNom());
		assertEquals(dto.getAgentEvalue().getPrenom(), eaeItem.getEaeEvalue().getAgent().getDisplayPrenom());
		assertEquals(dto.getAgentDelegataire().getIdAgent(), eaeItem.getAgentDelegataire().getIdAgent());
		assertEquals(dto.getAgentDelegataire().getNom(), eaeItem.getAgentDelegataire().getDisplayNom());
		assertEquals(dto.getAgentDelegataire().getPrenom(), eaeItem.getAgentDelegataire().getDisplayPrenom());
		assertEquals(dto.getEaeEvaluateurs().size(), eaeItem.getEaeEvaluateurs().size());
		assertEquals(dto.getEaeEvaluateurs().get(0).getIdAgent(), eaeItem.getEaeEvaluateurs().iterator().next().getIdAgent());
		assertEquals(dto.getEtat(), eaeItem.getEtat().name());
		assertEquals(dto.isCap(), eaeItem.isCap());
		assertEquals(dto.isDocAttache(), eaeItem.isDocAttache());
		assertEquals(dto.getDateCreation(), eaeItem.getDateCreation());
		assertEquals(dto.getDateFinalisation(), eaeItem.getDateFinalisation());
		assertEquals(dto.getDateControle(), eaeItem.getDateControle());
		assertEquals(dto.getAvisShd(), eaeItem.getEaeEvaluation().getAvisShd());
		assertEquals(dto.isEstDetache(), eaeItem.getEaeEvalue().isEstDetache());
		assertEquals(dto.getDirectionService(), fdp.getDirectionService());
		assertEquals(dto.getSectionService(), fdp.getSectionService());
		assertEquals(dto.getService(), fdp.getService());
		assertEquals(dto.getAgentShd().getIdAgent(), fdp.getAgentShd().getIdAgent());
		assertEquals(dto.getAgentShd().getNom(), fdp.getAgentShd().getDisplayNom());
		assertEquals(dto.getAgentShd().getPrenom(), fdp.getAgentShd().getDisplayPrenom());
	}
	
	@Test
	public void testBuildEaeListItemDto_EaeDoesNotHaveAnyFichePoste_FromEae() {
		
		// Given
		Eae eaeItem = new Eae();
		eaeItem.setIdEae(6789);
		
		Agent agentEvalue = new Agent();
		agentEvalue.setIdAgent(12);
		agentEvalue.setNomMarital("toto");
		agentEvalue.setPrenom("titi");
		EaeEvalue evalue = new EaeEvalue();
		evalue.setAgent(agentEvalue);
		evalue.setEstDetache(true);
		eaeItem.setEaeEvalue(evalue);
		
		Agent agentDelegataire = new Agent();
		agentDelegataire.setIdAgent(45);
		agentDelegataire.setNomMarital("toto");
		agentDelegataire.setPrenom("titi");
		eaeItem.setAgentDelegataire(agentDelegataire);
		
		EaeEvaluateur ev1 = new EaeEvaluateur();
		ev1.setIdAgent(34);
		eaeItem.getEaeEvaluateurs().add(ev1);
		
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
		assertEquals(dto.getAgentEvalue().getNom(), eaeItem.getEaeEvalue().getAgent().getDisplayNom());
		assertEquals(dto.getAgentEvalue().getPrenom(), eaeItem.getEaeEvalue().getAgent().getDisplayPrenom());
		assertEquals(dto.getAgentEvalue().getIdAgent(), eaeItem.getEaeEvalue().getAgent().getIdAgent());
		assertEquals(dto.getAgentDelegataire().getNom(), eaeItem.getAgentDelegataire().getDisplayNom());
		assertEquals(dto.getAgentDelegataire().getPrenom(), eaeItem.getAgentDelegataire().getDisplayPrenom());
		assertEquals(dto.getAgentDelegataire().getIdAgent(), eaeItem.getAgentDelegataire().getIdAgent());
		assertEquals(dto.getEaeEvaluateurs().size(), eaeItem.getEaeEvaluateurs().size());
		assertEquals(dto.getEaeEvaluateurs().get(0).getIdAgent(), eaeItem.getEaeEvaluateurs().iterator().next().getIdAgent());
		assertEquals(dto.getEtat(), eaeItem.getEtat().name());
		assertEquals(dto.isCap(), eaeItem.isCap());
		assertEquals(dto.isDocAttache(), eaeItem.isDocAttache());
		assertEquals(dto.getDateCreation(), eaeItem.getDateCreation());
		assertEquals(dto.getDateFinalisation(), eaeItem.getDateFinalisation());
		assertEquals(dto.getDateControle(), eaeItem.getDateControle());
		assertEquals(dto.getAvisShd(), eaeItem.getEaeEvaluation().getAvisShd());
		assertEquals(dto.isEstDetache(), eaeItem.getEaeEvalue().isEstDetache());
		assertNull(dto.getDirectionService());
		assertNull(dto.getSectionService());
		assertNull(dto.getService());
		assertNull(dto.getAgentShd());
	}
	
	@Test
	public void testBuildEaeListItemDto_EaeIsC_droitImprimerGedFalseAndDroitImprimerBirtTrue() {
		
		// Given
		Eae eaeItem = new Eae();
		eaeItem.setIdEae(6789);
		eaeItem.setEtat(EaeEtatEnum.C);
		EaeFinalisation fi = new EaeFinalisation();
		fi.setNodeRefAlfresco("theId");
		eaeItem.getEaeFinalisations().add(fi);
		
		// When
		EaeListItemDto dto = new EaeListItemDto(eaeItem);
		
		// Then
		assertFalse(dto.isDroitImprimerGed());
		assertNull(dto.getIdDocumentGed());
		assertTrue(dto.isDroitImprimerBirt());
	}
	
	@Test
	public void testBuildEaeListItemDto_EaeIsEC_droitImprimerGedFalseAndDroitImprimerBirtTrue() {
		
		// Given
		Eae eaeItem = new Eae();
		eaeItem.setIdEae(6789);
		eaeItem.setEtat(EaeEtatEnum.EC);
		EaeFinalisation fi = new EaeFinalisation();
		fi.setNodeRefAlfresco("theId");
		eaeItem.getEaeFinalisations().add(fi);
		
		// When
		EaeListItemDto dto = new EaeListItemDto(eaeItem);
		
		// Then
		assertFalse(dto.isDroitImprimerGed());
		assertNull(dto.getIdDocumentGed());
		assertTrue(dto.isDroitImprimerBirt());
	}
	
	@Test
	public void testBuildEaeListItemDto_EaeIsND_droitImprimerGedFalseAndDroitImprimerBirtFalse() {
		
		// Given
		Eae eaeItem = new Eae();
		eaeItem.setIdEae(6789);
		eaeItem.setEtat(EaeEtatEnum.ND);
		EaeFinalisation fi = new EaeFinalisation();
		fi.setNodeRefAlfresco("theId");
		eaeItem.getEaeFinalisations().add(fi);
		
		// When
		EaeListItemDto dto = new EaeListItemDto(eaeItem);
		
		// Then
		assertFalse(dto.isDroitImprimerGed());
		assertNull(dto.getIdDocumentGed());
		assertFalse(dto.isDroitImprimerBirt());
	}
	
	@Test
	public void testBuildEaeListItemDto_EaeIsF_droitImprimerGedTrueAndIdDocumentGedSetAndDroitImprimerBirtFalse() {
		
		// Given
		Eae eaeItem = new Eae();
		eaeItem.setIdEae(6789);
		eaeItem.setEtat(EaeEtatEnum.F);
		EaeFinalisation fi = new EaeFinalisation();
		fi.setNodeRefAlfresco("theId");
		eaeItem.getEaeFinalisations().add(fi);
		
		// When
		EaeListItemDto dto = new EaeListItemDto(eaeItem);
		
		// Then
		assertTrue(dto.isDroitImprimerGed());
		assertEquals("theId", dto.getIdDocumentGed());
		assertFalse(dto.isDroitImprimerBirt());
	}
	
	@Test
	public void testBuildEaeListItemDto_EaeIsCO_droitImprimerGedTrueAndIdDocumentGedSetAndDroitImprimerBirtFalse() {
		
		// Given
		Eae eaeItem = new Eae();
		eaeItem.setIdEae(6789);
		eaeItem.setEtat(EaeEtatEnum.CO);
		EaeFinalisation fi = new EaeFinalisation();
		fi.setNodeRefAlfresco("theId");
		eaeItem.getEaeFinalisations().add(fi);
		
		// When
		EaeListItemDto dto = new EaeListItemDto(eaeItem);
		
		// Then
		assertTrue(dto.isDroitImprimerGed());
		assertEquals("theId", dto.getIdDocumentGed());
		assertFalse(dto.isDroitImprimerBirt());
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
	
//	@Test
//	public void testGetSerializerForEaeItemListDto_ListAllIncludesExcludes() {
//		
//		// When
//		List<PathExpression> includes = EaeListItemDto.getSerializerForEaeListItemDto().getIncludes();
//		List<PathExpression> excludes = EaeListItemDto.getSerializerForEaeListItemDto().getExcludes();
//		
//		// Then
//		assertEquals(23, includes.size());
//		assertEquals("[agentEvalue]", includes.get(0).toString());
//		assertEquals("[etat]", includes.get(1).toString());
//		assertEquals("[cap]", includes.get(2).toString());
//		assertEquals("[docAttache]", includes.get(3).toString());
//		assertEquals("[dateCreation]", includes.get(4).toString());
//		assertEquals("[dateFinalisation]", includes.get(5).toString());
//		assertEquals("[dateControle]", includes.get(6).toString());
//		assertEquals("[agentDelegataire]", includes.get(7).toString());
//		assertEquals("[avisShd]", includes.get(8).toString());
//		assertEquals("[idEae]", includes.get(9).toString());
//		assertEquals("[eaeEvaluateurs]", includes.get(10).toString());
//		assertEquals("[droitInitialiser]", includes.get(11).toString());
//		assertEquals("[droitAcceder]", includes.get(12).toString());
//		assertEquals("[droitDemarrer]", includes.get(13).toString());
//		assertEquals("[droitAffecterDelegataire]", includes.get(14).toString());
//		assertEquals("[droitImprimerBirt]", includes.get(15).toString());
//		assertEquals("[droitImprimerGed]", includes.get(16).toString());
//		assertEquals("[idDocumentGed]", includes.get(17).toString());
//		assertEquals("[estDetache]", includes.get(18).toString());
//		assertEquals("[directionService]", includes.get(19).toString());
//		assertEquals("[sectionService]", includes.get(20).toString());
//		assertEquals("[service]", includes.get(21).toString());
//		assertEquals("[agentShd]", includes.get(22).toString());
//		
//		assertEquals(1, excludes.size());
//		assertEquals("[*]", excludes.get(0).toString());
//	}
//	
//	@Test
//	public void testGetSerializerForEaeItemListDto_AgentEvalue() {
//		
//		// Given
//		EaeListItemDto eae = new EaeListItemDto();
//		eae.setIdEae(18);
//		
//		Agent agent = new Agent();
//		agent.setIdAgent(999);
//		agent.setNomPatronymique("Duck");
//		agent.setPrenom("Donald");
//		eae.setAgentEvalue(agent);
//		
//		String expectedResult = "\"agentEvalue\":{\"idAgent\":999,\"nom\":\"Duck\",\"prenom\":\"Donald\"}";
//		
//		// When
//		String result = EaeListItemDto.getSerializerForEaeListItemDto().serialize(eae);
//		
//		// Then
//		assertTrue(result.contains(expectedResult));
//	}
//	
//	@Test
//	public void testGetSerializerForEaeItemListDto_AgentShd() {
//		
//		// Given
//		EaeListItemDto eae = new EaeListItemDto();
//		eae.setIdEae(18);
//		
//		Agent agent = new Agent();
//		agent.setIdAgent(999);
//		agent.setNomPatronymique("Duck");
//		agent.setPrenom("Donald");
//		eae.setAgentShd(agent);
//		
//		String expectedResult = "\"agentShd\":{\"idAgent\":999,\"nom\":\"Duck\",\"prenom\":\"Donald\"}";
//		
//		// When
//		String result = EaeListItemDto.getSerializerForEaeListItemDto().serialize(eae);
//		
//		// Then
//		assertTrue(result.contains(expectedResult));
//	}
//	
//	@Test
//	public void testGetSerializerForEaeItemListDto_AgentDelegataire() {
//		
//		// Given
//		EaeListItemDto eae = new EaeListItemDto();
//		eae.setIdEae(18);
//		
//		Agent agent = new Agent();
//		agent.setIdAgent(999);
//		agent.setNomPatronymique("Duck");
//		agent.setPrenom("Donald");
//		eae.setAgentDelegataire(agent);
//		
//		String expectedResult = "\"agentDelegataire\":{\"idAgent\":999,\"nom\":\"Duck\",\"prenom\":\"Donald\"}";
//		
//		// When
//		String result = EaeListItemDto.getSerializerForEaeListItemDto().serialize(eae);
//		
//		// Then
//		assertTrue(result.contains(expectedResult));
//	}
}
