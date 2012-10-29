package nc.noumea.mairie.sirh.eae.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import nc.noumea.mairie.sirh.domain.Agent;
import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeCommentaire;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluateur;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluation;
import nc.noumea.mairie.sirh.eae.domain.EaeEvalue;
import nc.noumea.mairie.sirh.eae.domain.EaeFichePoste;
import nc.noumea.mairie.sirh.eae.domain.EaeNiveau;
import nc.noumea.mairie.sirh.eae.domain.EaeResultat;
import nc.noumea.mairie.sirh.eae.domain.EaeTypeObjectif;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeAvancementEnum;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeTypeAvctEnum;
import nc.noumea.mairie.sirh.eae.dto.EaeAppreciationsDto;
import nc.noumea.mairie.sirh.eae.dto.EaeEvaluationDto;
import nc.noumea.mairie.sirh.eae.dto.EaeFichePosteDto;
import nc.noumea.mairie.sirh.eae.dto.EaeResultatsDto;
import nc.noumea.mairie.sirh.eae.dto.identification.EaeIdentificationDto;
import nc.noumea.mairie.sirh.eae.dto.util.ValueWithListDto;
import nc.noumea.mairie.sirh.eae.service.dataConsistency.EaeDataConsistencyServiceException;
import nc.noumea.mairie.sirh.eae.service.dataConsistency.IEaeDataConsistencyService;
import nc.noumea.mairie.sirh.service.IAgentService;

import org.junit.Test;
import org.springframework.mock.staticmock.AnnotationDrivenStaticEntityMockingControl;
import org.springframework.mock.staticmock.MockStaticEntityMethods;
import org.springframework.test.util.ReflectionTestUtils;

@MockStaticEntityMethods
public class EvaluationServiceTest {

	@Test
	public void testGetEaeIdentification_WithEae_FillAgentsAndReturn() {

		// Given
		Eae eae = new Eae();
		eae.setIdEae(123);
		EaeEvaluateur eval1 = new EaeEvaluateur();
		eae.getEaeEvaluateurs().add(eval1);
		EaeEvalue evalue = new EaeEvalue();
		eae.setEaeEvalue(evalue);
		IAgentService agentServiceMock = mock(IAgentService.class);
		EaeFichePoste fdp = new EaeFichePoste();
		fdp.setPrimary(true);
		eae.getEaeFichePostes().add(fdp);
		EvaluationService service = new EvaluationService();
		ReflectionTestUtils.setField(service, "agentService", agentServiceMock);

		// When
		EaeIdentificationDto result = service.getEaeIdentification(eae);
		
		// Then
		assertEquals(123, result.getIdEae());
		
		verify(agentServiceMock, times(1)).fillEaeEvaluateurWithAgent(eval1);
		verify(agentServiceMock, times(1)).fillEaeEvalueWithAgent(evalue);
	}

	@Test
	public void testGetEaeFichePoste_WithEae_FillAgentsAndReturn() {
		// Given
		Eae eae = new Eae();
		eae.setIdEae(123);
		EaeFichePoste fdp = new EaeFichePoste();
		fdp.setIdAgentShd(12345);
		fdp.setPrimary(true);
		fdp.setAgentShd(new Agent());
		eae.getEaeFichePostes().add(fdp);
		fdp.setEae(eae);

		IAgentService agentServiceMock = mock(IAgentService.class);

		EvaluationService service = new EvaluationService();
		ReflectionTestUtils.setField(service, "agentService", agentServiceMock);

		// When
		List<EaeFichePosteDto> result = service.getEaeFichePoste(eae);
		
		// Then
		assertEquals(1, result.size());
		assertEquals(123, result.get(0).getIdEae());

		verify(agentServiceMock, times(1)).fillEaeFichePosteWithAgent(fdp);
	}
	
	@Test
	public void testGetEaeFichePoste_WithEae2FichePoste_FillAgentsAndReturn() {
		// Given
		Eae eae = new Eae();
		eae.setIdEae(123);
		EaeFichePoste fdp = new EaeFichePoste();
		fdp.setIdAgentShd(12345);
		fdp.setPrimary(true);
		fdp.setAgentShd(new Agent());
		eae.getEaeFichePostes().add(fdp);
		fdp.setEae(eae);
		
		EaeFichePoste fdp2 = new EaeFichePoste();
		fdp2.setIdAgentShd(12345);
		fdp2.setPrimary(true);
		fdp2.setAgentShd(new Agent());
		eae.getEaeFichePostes().add(fdp2);
		fdp2.setEae(eae);

		IAgentService agentServiceMock = mock(IAgentService.class);

		EvaluationService service = new EvaluationService();
		ReflectionTestUtils.setField(service, "agentService", agentServiceMock);

		// When
		List<EaeFichePosteDto> result = service.getEaeFichePoste(eae);
		
		// Then
		assertEquals(2, result.size());
		assertEquals(123, result.get(0).getIdEae());
		assertEquals(123, result.get(1).getIdEae());

		verify(agentServiceMock, times(1)).fillEaeFichePosteWithAgent(fdp);
		verify(agentServiceMock, times(1)).fillEaeFichePosteWithAgent(fdp2);
	}
	
	@Test
	public void testGetEaeResultats_WithEae_FillResultatsDtoAndReturn() {

		// Given
		Eae eae = new Eae();
		eae.setIdEae(789);
		EaeCommentaire com = new EaeCommentaire();
		com.setText("the comment text");
		eae.setCommentaire(com);
		
		EaeTypeObjectif t1 = new EaeTypeObjectif();
		t1.setLibelle("PROFESSIONNEL");
		
		EaeTypeObjectif t2 = new EaeTypeObjectif();
		t2.setLibelle("INDIVIDUEL");
		
		EaeTypeObjectif t3 = new EaeTypeObjectif();
		t3.setLibelle("AUTRE");
		
		EaeResultat res1 = new EaeResultat();
		res1.setTypeObjectif(t1);
		res1.setObjectif("obj1");
		eae.getEaeResultats().add(res1);
		
		EaeResultat res2 = new EaeResultat();
		res2.setTypeObjectif(t2);
		res2.setObjectif("obj2");
		eae.getEaeResultats().add(res2);
		
		EvaluationService service = new EvaluationService();

		// When
		EaeResultatsDto result = service.getEaeResultats(eae);
		
		// Then
		assertEquals(789, result.getIdEae());
	}
	
	@Test
	public void testSetEaeResultats_1CommentAndNoResultats_createEaeCommentaire() throws EvaluationServiceException {
		
		// Given
		EaeResultatsDto dto = new EaeResultatsDto();
		dto.setCommentaireGeneral("The Comment!");
		dto.setIdEae(789);
		
		Eae eae = spy(new Eae());
		org.mockito.Mockito.doNothing().when(eae).flush();
		
		EvaluationService service = new EvaluationService();
		
		// When
		service.setEaeResultats(eae, dto);
		
		// Then
		assertEquals(dto.getCommentaireGeneral(), eae.getCommentaire().getText());
	}
	
	@Test
	public void testSetEaeResultats_1CommentAndNoResultats_updateEaeCommentaire() throws EvaluationServiceException {
		
		// Given
		EaeResultatsDto dto = new EaeResultatsDto();
		dto.setCommentaireGeneral("The Comment!");
		dto.setIdEae(789);
		
		Eae eae = spy(new Eae());
		org.mockito.Mockito.doNothing().when(eae).flush();
		EaeCommentaire c = new EaeCommentaire();
		c.setText("text before");
		eae.setCommentaire(c);
		
		EvaluationService service = new EvaluationService();
		
		// When
		service.setEaeResultats(eae, dto);
		
		// Then
		assertEquals(dto.getCommentaireGeneral(), eae.getCommentaire().getText());
	}
	
	@Test
	public void testSetEaeResultats_1NewResultatPro_createResultatWithRightObjectifType() throws EvaluationServiceException {
		
		// Given
		EaeResultatsDto dto = new EaeResultatsDto();
		dto.setIdEae(789);
		EaeResultat r = new EaeResultat();
		r.setObjectif("new obj");
		r.setResultat("new res");
		EaeCommentaire c = new EaeCommentaire();
		c.setText("new obj comment");
		r.setCommentaire(c);
		dto.getObjectifsProfessionnels().add(r);
		
		Eae eae = spy(new Eae());
		org.mockito.Mockito.doNothing().when(eae).flush();
		
		ITypeObjectifService objService = mock(ITypeObjectifService.class);
		EaeTypeObjectif t1 = new EaeTypeObjectif();
		t1.setLibelle("PROFESSIONNEL");
		when(objService.getTypeObjectifForLibelle("PROFESSIONNEL")).thenReturn(t1);
		
		EvaluationService service = new EvaluationService();
		ReflectionTestUtils.setField(service, "typeObjectifService", objService);

		// When
		service.setEaeResultats(eae, dto);
		
		// Then
		assertEquals(1, eae.getEaeResultats().size());
		
		Iterator<EaeResultat> it = eae.getEaeResultats().iterator();
		EaeResultat resultat = it.next();
		assertEquals(eae, resultat.getEae());
		assertEquals("PROFESSIONNEL", resultat.getTypeObjectif().getLibelle());
		assertEquals("new obj", resultat.getObjectif());
		assertEquals("new res", resultat.getResultat());
		assertEquals("new obj comment", resultat.getCommentaire().getText());
	}
	
	@Test
	public void testSetEaeResultats_1NewResultatIndividuel_createResultatWithRightObjectifType() throws EvaluationServiceException {
		
		// Given
		EaeResultatsDto dto = new EaeResultatsDto();
		dto.setIdEae(789);
			
		EaeResultat r2 = new EaeResultat();
		r2.setObjectif("new obj2");
		r2.setResultat("new res2");
		EaeCommentaire c2 = new EaeCommentaire();
		c2.setText("new obj comment2");
		r2.setCommentaire(c2);
		dto.getObjectifsIndividuels().add(r2);
		
		Eae eae = spy(new Eae());
		org.mockito.Mockito.doNothing().when(eae).flush();
		
		ITypeObjectifService objService = mock(ITypeObjectifService.class);
		EaeTypeObjectif t2 = new EaeTypeObjectif();
		t2.setLibelle("INDIVIDUEL");
		when(objService.getTypeObjectifForLibelle("INDIVIDUEL")).thenReturn(t2);
		
		EvaluationService service = new EvaluationService();
		ReflectionTestUtils.setField(service, "typeObjectifService", objService);

		// When
		service.setEaeResultats(eae, dto);
		
		// Then
		assertEquals(1, eae.getEaeResultats().size());
		
		Iterator<EaeResultat> it = eae.getEaeResultats().iterator();
		EaeResultat resultat = it.next();
		assertEquals(eae, resultat.getEae());
		assertEquals("INDIVIDUEL", resultat.getTypeObjectif().getLibelle());
		assertEquals("new obj2", resultat.getObjectif());
		assertEquals("new res2", resultat.getResultat());
		assertEquals("new obj comment2", resultat.getCommentaire().getText());
	}
	
	@Test
	public void testSetEaeResultats_1ExistingResultat_updateResultat() throws EvaluationServiceException {
		
		// Given
		EaeResultatsDto dto = new EaeResultatsDto();
		dto.setIdEae(789);
		EaeResultat r = new EaeResultat();
		r.setIdEaeResultat(678);
		r.setObjectif("new obj");
		r.setResultat("new res");
		EaeCommentaire c = new EaeCommentaire();
		c.setText("new obj comment");
		r.setCommentaire(c);
		dto.getObjectifsProfessionnels().add(r);
		
		Eae eae = spy(new Eae());
		org.mockito.Mockito.doNothing().when(eae).flush();
		EaeResultat existingResultat = new EaeResultat();
		existingResultat.setEae(eae);
		existingResultat.setIdEaeResultat(678);
		existingResultat.setObjectif("old obj");
		existingResultat.setResultat("old res");
		EaeCommentaire cExisting = new EaeCommentaire();
		cExisting.setText("old obj comment");
		existingResultat.setCommentaire(c);
		eae.getEaeResultats().add(existingResultat);
		
		EvaluationService service = new EvaluationService();

		// When
		service.setEaeResultats(eae, dto);
		
		// Then
		assertEquals(1, eae.getEaeResultats().size());
		
		Iterator<EaeResultat> it = eae.getEaeResultats().iterator();
		EaeResultat resultat = it.next();
		assertEquals(eae, resultat.getEae());
		assertEquals("new obj", resultat.getObjectif());
		assertEquals("new res", resultat.getResultat());
		assertEquals("new obj comment", resultat.getCommentaire().getText());
	}
	
	@Test
	public void testGetEaeAppreciations_WithEae_FillResultatsDtoAndReturn() {

		// Given
		Eae eae = new Eae();
		eae.setIdEae(789);
		
		EvaluationService service = new EvaluationService();

		// When
		EaeAppreciationsDto result = service.getEaeAppreciations(eae);
		
		// Then
		assertEquals(789, result.getIdEae());
	}
	
	@Test
	public void testSetEaeAppreciations_NoAppreciations_CreateEaeAppreciations() {

		// Given
		Eae eae = spy(new Eae());
		org.mockito.Mockito.doNothing().when(eae).flush();
		
		eae.setIdEae(789);
		EaeAppreciationsDto dto = new EaeAppreciationsDto();
		dto.setIdEae(789);
		dto.setTechniqueEvalue(new String[]{"A", "B", "C", "D"});
		dto.setTechniqueEvaluateur(new String[]{"A", "B", "C", "D"});
		dto.setSavoirEtreEvalue(new String[]{"A", "B", "C", "D"});
		dto.setSavoirEtreEvaluateur(new String[]{"A", "B", "C", "D"});
		dto.setManagerialEvalue(new String[]{"A", "B", "C", "D"});
		dto.setManagerialEvaluateur(new String[]{"A", "B", "C", "D"});
		dto.setResultatsEvaluateur(new String[]{"A", "B", "C", "D"});
		dto.setResultatsEvalue(new String[]{"A", "B", "C", "D"});
		
		EvaluationService service = new EvaluationService();

		// When
		service.setEaeAppreciations(eae, dto);
		
		// Then
		assertEquals(16, eae.getEaeAppreciations().size());
	}
	
	@Test
	public void testGetEaeEvaluation_WithEae_FillResultatsDtoAndReturn() {

		// Given
		Eae eae = new Eae();
		eae.setIdEae(789);
		EaeEvaluation eval = new EaeEvaluation();
		eval.setEae(eae);
		eae.setEaeEvaluation(eval);
		
		List<EaeNiveau> niveauList = new ArrayList<EaeNiveau>();
		EaeNiveau.findAllEaeNiveaus();
		AnnotationDrivenStaticEntityMockingControl.expectReturn(niveauList);
		AnnotationDrivenStaticEntityMockingControl.playback();
		
		EvaluationService service = new EvaluationService();

		// When
		EaeEvaluationDto result = service.getEaeEvaluation(eae);
		
		// Then
		assertEquals(789, result.getIdEae());
	}
	
	@Test
	public void testSetEaeEvaluation_valuesInDto_setEaeEvaluationValues() throws EvaluationServiceException, EaeDataConsistencyServiceException {
		
		// Given
		EaeEvaluationDto dto = new EaeEvaluationDto();
		dto.setIdEae(13);
		dto.setDureeEntretien(127);
		dto.setNoteAnnee(12);
		dto.setNoteAnneeN1(13);
		dto.setNoteAnneeN2(14);
		dto.setNoteAnneeN3(15);
		dto.setAvisRevalorisation(true);
		dto.setAvisChangementClasse(false);
		ValueWithListDto subDto = new ValueWithListDto(EaeAvancementEnum.MAXI, EaeAvancementEnum.class);
		dto.setPropositionAvancement(subDto);
		
		EaeNiveau niv = new EaeNiveau();
		niv.setIdEaeNiveau(2);
		niv.setLibelleNiveauEae("Satisfaisant");
		EaeNiveau niv2 = new EaeNiveau();
		niv2.setIdEaeNiveau(4);
		niv2.setLibelleNiveauEae("Cool");
		
		ValueWithListDto subDto2 = new ValueWithListDto(niv, Arrays.asList(niv, niv2));
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
		
		Eae eae = new Eae();
		EaeEvaluation eval = new EaeEvaluation();
		eae.setEaeEvaluation(eval);
		eval.setEae(eae);
		eae.setEaeEvalue(new EaeEvalue());
		
		EaeNiveau niveau = new EaeNiveau();
		niveau.setIdEaeNiveau(2);
		EaeNiveau.findEaeNiveau(2);
		AnnotationDrivenStaticEntityMockingControl.expectReturn(niveau);
		AnnotationDrivenStaticEntityMockingControl.playback();
		
		IEaeDataConsistencyService dcService = mock(IEaeDataConsistencyService.class);
		
		EvaluationService service = new EvaluationService();
		ReflectionTestUtils.setField(service, "eaeDataConsistencyService", dcService);
		
		// When
		service.setEaeEvaluation(eae, dto);
		
		// Then
		assertEquals("com3", eval.getCommentaireEvaluateur().getText());
		assertEquals("com4", eval.getCommentaireEvalue().getText());
		assertEquals("com1", eval.getCommentaireAvctEvaluateur().getText());
		assertEquals("com2", eval.getCommentaireAvctEvalue().getText());
		assertEquals(new Integer(12), eval.getNoteAnnee());
		assertFalse(eval.getAvisChangementClasse());
		assertTrue(eval.getAvisRevalorisation());
		assertEquals(new Integer(127), eval.getEae().getDureeEntretienMinutes());
		assertEquals(niv.getIdEaeNiveau(), eval.getNiveauEae().getIdEaeNiveau());
		assertEquals(EaeAvancementEnum.MAXI, eval.getPropositionAvancement());
	}
	
	@Test
	public void testSetEaeEvaluation_valuesInDtoAndExistingValuesInEvaluation_overwriteValues() throws EvaluationServiceException, EaeDataConsistencyServiceException {
		
		// Given
		EaeEvaluationDto dto = new EaeEvaluationDto();
		dto.setIdEae(13);
		dto.setDureeEntretien(127);
		dto.setNoteAnnee(12);
		dto.setNoteAnneeN1(13);
		dto.setNoteAnneeN2(14);
		dto.setNoteAnneeN3(15);
		dto.setAvisRevalorisation(true);
		dto.setAvisChangementClasse(false);
		ValueWithListDto subDto = new ValueWithListDto(EaeAvancementEnum.MAXI, EaeAvancementEnum.class);
		dto.setPropositionAvancement(subDto);
		
		EaeNiveau niv = new EaeNiveau();
		niv.setIdEaeNiveau(2);
		niv.setLibelleNiveauEae("Satisfaisant");
		EaeNiveau niv2 = new EaeNiveau();
		niv2.setIdEaeNiveau(4);
		niv2.setLibelleNiveauEae("Cool");
		
		ValueWithListDto subDto2 = new ValueWithListDto(niv, Arrays.asList(niv, niv2));
		dto.setNiveau(subDto2);
		EaeCommentaire com1 = new EaeCommentaire();
		com1.setText("com3");
		dto.setCommentaireAvctEvaluateur(com1);
		EaeCommentaire com2 = new EaeCommentaire();
		com2.setText("com4");
		dto.setCommentaireAvctEvalue(com2);
		EaeCommentaire com3 = new EaeCommentaire();
		com3.setText("com1");
		dto.setCommentaireEvaluateur(com3);
		EaeCommentaire com4 = new EaeCommentaire();
		com4.setText("com2");
		dto.setCommentaireEvalue(com4);
		
		Eae eae = new Eae();
		eae.setEaeEvalue(new EaeEvalue());
		EaeEvaluation eval = new EaeEvaluation();
		eae.setEaeEvaluation(eval);
		eval.setEae(eae);
		EaeCommentaire comBefore1 = new EaeCommentaire();
		comBefore1.setIdEaeCommentaire(1);
		eval.setCommentaireEvaluateur(comBefore1);
		eval.getCommentaireEvaluateur().setText("text before");
		EaeCommentaire comBefore2 = new EaeCommentaire();
		comBefore2.setIdEaeCommentaire(2);
		eval.setCommentaireEvalue(comBefore2);
		eval.getCommentaireEvalue().setText("text before");
		EaeCommentaire comBefore3 = new EaeCommentaire();
		comBefore3.setIdEaeCommentaire(3);
		eval.setCommentaireAvctEvaluateur(comBefore3);
		eval.getCommentaireAvctEvaluateur().setText("text before");
		EaeCommentaire comBefore4 = new EaeCommentaire();
		comBefore4.setIdEaeCommentaire(4);
		eval.setCommentaireAvctEvalue(comBefore4);
		eval.getCommentaireAvctEvalue().setText("text before");
		
		EaeNiveau niveau = new EaeNiveau();
		niveau.setIdEaeNiveau(2);
		EaeNiveau.findEaeNiveau(2);
		AnnotationDrivenStaticEntityMockingControl.expectReturn(niveau);
		AnnotationDrivenStaticEntityMockingControl.playback();
		
		IEaeDataConsistencyService dcService = mock(IEaeDataConsistencyService.class);
		
		EvaluationService service = new EvaluationService();
		ReflectionTestUtils.setField(service, "eaeDataConsistencyService", dcService);
		
		// When
		service.setEaeEvaluation(eae, dto);
		
		// Then
		assertEquals(comBefore1.getIdEaeCommentaire(), eval.getCommentaireEvaluateur().getIdEaeCommentaire());
		assertEquals("com1", eval.getCommentaireEvaluateur().getText());
		assertEquals(comBefore2.getIdEaeCommentaire(), eval.getCommentaireEvalue().getIdEaeCommentaire());
		assertEquals("com2", eval.getCommentaireEvalue().getText());
		assertEquals(comBefore3.getIdEaeCommentaire(), eval.getCommentaireAvctEvaluateur().getIdEaeCommentaire());
		assertEquals("com3", eval.getCommentaireAvctEvaluateur().getText());
		assertEquals(comBefore4.getIdEaeCommentaire(), eval.getCommentaireAvctEvalue().getIdEaeCommentaire());
		assertEquals("com4", eval.getCommentaireAvctEvalue().getText());
		assertEquals(new Integer(12), eval.getNoteAnnee());
		assertFalse(eval.getAvisChangementClasse());
		assertTrue(eval.getAvisRevalorisation());
		assertEquals(new Integer(127), eval.getEae().getDureeEntretienMinutes());
		assertEquals(niv.getIdEaeNiveau(), eval.getNiveauEae().getIdEaeNiveau());
		assertEquals(EaeAvancementEnum.MAXI, eval.getPropositionAvancement());
	}
	
	@Test
	public void testSetEaeEvaluation_NiveauIsNull_throwException() {
		
		// Given
		EaeEvaluationDto dto = new EaeEvaluationDto();
		dto.setPropositionAvancement(new ValueWithListDto());
		
		dto.setNiveau(new ValueWithListDto());
		
		Eae eae = new Eae();
		EaeEvaluation eval = new EaeEvaluation();
		eae.setEaeEvaluation(eval);
		eval.setEae(eae);
		
		EvaluationService service = new EvaluationService();
		
		try {
			// When
			service.setEaeEvaluation(eae, dto);
		} catch (EvaluationServiceException e) {
			// Then
			assertEquals("La propriété 'niveau' de l'évaluation est manquante.", e.getMessage());
			return;
		}
		
		fail("Should have thrown an exception here!");
	}
	
	@Test
	public void testSetEaeEvaluation_NiveauIsIncorrectNumber_throwException() {
		
		// Given
		EaeEvaluationDto dto = new EaeEvaluationDto();
		dto.setPropositionAvancement(new ValueWithListDto());
		
		ValueWithListDto subDto2 = new ValueWithListDto();
		subDto2.setCourant("89");
		dto.setNiveau(subDto2);
		
		Eae eae = new Eae();
		EaeEvaluation eval = new EaeEvaluation();
		eae.setEaeEvaluation(eval);
		eval.setEae(eae);
		
		EaeNiveau.findEaeNiveau(89);
		AnnotationDrivenStaticEntityMockingControl.expectReturn(null);
		AnnotationDrivenStaticEntityMockingControl.playback();
		
		EvaluationService service = new EvaluationService();
		
		try {
			// When
			service.setEaeEvaluation(eae, dto);
		} catch (EvaluationServiceException e) {
			// Then
			assertEquals("La propriété 'niveau' de l'évaluation est incorrecte.", e.getMessage());
			return;
		}
		
		fail("Should have thrown an exception here!");
	}
	
	@Test
	public void testSetEaeEvaluation_NiveauIsIncorrectString_throwException() {
		
		// Given
		EaeEvaluationDto dto = new EaeEvaluationDto();
		dto.setPropositionAvancement(new ValueWithListDto());
		
		ValueWithListDto subDto2 = new ValueWithListDto();
		subDto2.setCourant("INVALID_CODE");
		dto.setNiveau(subDto2);
		
		Eae eae = new Eae();
		EaeEvaluation eval = new EaeEvaluation();
		eae.setEaeEvaluation(eval);
		eval.setEae(eae);
		
		EvaluationService service = new EvaluationService();
		
		try {
			// When
			service.setEaeEvaluation(eae, dto);
		} catch (EvaluationServiceException e) {
			// Then
			assertEquals("La propriété 'niveau' de l'évaluation est incorrecte.", e.getMessage());
			return;
		}
		
		fail("Should have thrown an exception here!");
	}
	
	@Test
	public void testSetEaeEvaluation_AvancementIsIncorrect_throwException() {
		
		// Given
		EaeEvaluationDto dto = new EaeEvaluationDto();
		ValueWithListDto subDto = new ValueWithListDto();
		subDto.setCourant("INVALID");
		dto.setPropositionAvancement(subDto);
		
		EaeNiveau niv = new EaeNiveau();
		niv.setIdEaeNiveau(2);
		niv.setLibelleNiveauEae("Satisfaisant");
		
		ValueWithListDto subDto2 = new ValueWithListDto();
		subDto2.setCourant("2");
		dto.setNiveau(subDto2);
		
		Eae eae = new Eae();
		EaeEvaluation eval = new EaeEvaluation();
		eae.setEaeEvaluation(eval);
		eval.setEae(eae);
		
		EaeNiveau.findEaeNiveau(2);
		AnnotationDrivenStaticEntityMockingControl.expectReturn(niv);
		AnnotationDrivenStaticEntityMockingControl.playback();
		
		EvaluationService service = new EvaluationService();
		
		try {
			// When
			service.setEaeEvaluation(eae, dto);
		} catch (EvaluationServiceException e) {
			// Then
			assertEquals("La propriété 'propositionAvancement' de l'évaluation est incorrecte.", e.getMessage());
			return;
		}
		
		fail("Should have thrown an exception here!");
	}
	
	@Test
	public void testCalculateAvisShd_typeAvctIsREVAAndAvisFalse() {
		
		// Given
		Eae eae = new Eae();
		EaeEvalue evalue = new EaeEvalue();
		evalue.setTypeAvancement(EaeTypeAvctEnum.REVA);
		eae.setEaeEvalue(evalue);
		EaeEvaluation evaluation = new EaeEvaluation();
		evaluation.setAvisRevalorisation(false);
		eae.setEaeEvaluation(evaluation);
		
		EvaluationService service = new EvaluationService();
		
		// When
		service.calculateAvisShd(eae);
		
		// Then
		assertEquals("Défavorable", evaluation.getAvisShd());
	}
	
	@Test
	public void testCalculateAvisShd_typeAvctIsREVAAndAvisTrue() {
		
		// Given
		Eae eae = new Eae();
		EaeEvalue evalue = new EaeEvalue();
		evalue.setTypeAvancement(EaeTypeAvctEnum.REVA);
		eae.setEaeEvalue(evalue);
		EaeEvaluation evaluation = new EaeEvaluation();
		evaluation.setAvisRevalorisation(true);
		eae.setEaeEvaluation(evaluation);
		
		EvaluationService service = new EvaluationService();
		
		// When
		service.calculateAvisShd(eae);
		
		// Then
		assertEquals("Favorable", evaluation.getAvisShd());
	}
	
	@Test
	public void testCalculateAvisShd_typeAvctIsADAndAvisFalse() {
		
		// Given
		Eae eae = new Eae();
		EaeEvalue evalue = new EaeEvalue();
		evalue.setTypeAvancement(EaeTypeAvctEnum.AD);
		eae.setEaeEvalue(evalue);
		EaeEvaluation evaluation = new EaeEvaluation();
		evaluation.setAvisChangementClasse(false);
		eae.setEaeEvaluation(evaluation);
		
		EvaluationService service = new EvaluationService();
		
		// When
		service.calculateAvisShd(eae);
		
		// Then
		assertEquals("Défavorable", evaluation.getAvisShd());
	}
	
	@Test
	public void testCalculateAvisShd_typeAvctIsADAndAvisTrue() {
		
		// Given
		Eae eae = new Eae();
		EaeEvalue evalue = new EaeEvalue();
		evalue.setTypeAvancement(EaeTypeAvctEnum.AD);
		eae.setEaeEvalue(evalue);
		EaeEvaluation evaluation = new EaeEvaluation();
		evaluation.setAvisChangementClasse(true);
		eae.setEaeEvaluation(evaluation);
		
		EvaluationService service = new EvaluationService();
		
		// When
		service.calculateAvisShd(eae);
		
		// Then
		assertEquals("Favorable", evaluation.getAvisShd());
	}
	
	@Test
	public void testCalculateAvisShd_typeAvctIsAVCTAndAvisMINI() {
		
		// Given
		Eae eae = new Eae();
		EaeEvalue evalue = new EaeEvalue();
		evalue.setTypeAvancement(EaeTypeAvctEnum.AVCT);
		eae.setEaeEvalue(evalue);
		EaeEvaluation evaluation = new EaeEvaluation();
		evaluation.setPropositionAvancement(EaeAvancementEnum.MINI);
		eae.setEaeEvaluation(evaluation);
		
		EvaluationService service = new EvaluationService();
		
		// When
		service.calculateAvisShd(eae);
		
		// Then
		assertEquals("Minimale", evaluation.getAvisShd());
	}
	
	@Test
	public void testCalculateAvisShd_typeAvctIsAVCTAndAvisMAXI() {
		
		// Given
		Eae eae = new Eae();
		EaeEvalue evalue = new EaeEvalue();
		evalue.setTypeAvancement(EaeTypeAvctEnum.AVCT);
		eae.setEaeEvalue(evalue);
		EaeEvaluation evaluation = new EaeEvaluation();
		evaluation.setPropositionAvancement(EaeAvancementEnum.MAXI);
		eae.setEaeEvaluation(evaluation);
		
		EvaluationService service = new EvaluationService();
		
		// When
		service.calculateAvisShd(eae);
		
		// Then
		assertEquals("Maximale", evaluation.getAvisShd());
	}
}
