package nc.noumea.mairie.sirh.eae.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nc.noumea.mairie.sirh.domain.Agent;
import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeAutoEvaluation;
import nc.noumea.mairie.sirh.eae.domain.EaeCampagne;
import nc.noumea.mairie.sirh.eae.domain.EaeCommentaire;
import nc.noumea.mairie.sirh.eae.domain.EaeDeveloppement;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluateur;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluation;
import nc.noumea.mairie.sirh.eae.domain.EaeEvalue;
import nc.noumea.mairie.sirh.eae.domain.EaeEvolution;
import nc.noumea.mairie.sirh.eae.domain.EaeEvolutionSouhait;
import nc.noumea.mairie.sirh.eae.domain.EaeFichePoste;
import nc.noumea.mairie.sirh.eae.domain.EaePlanAction;
import nc.noumea.mairie.sirh.eae.domain.EaeResultat;
import nc.noumea.mairie.sirh.eae.domain.EaeTypeObjectif;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeAgentStatutEnum;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeAvancementEnum;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeDelaiEnum;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeNiveauEnum;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeTypeAvctEnum;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeTypeDeveloppementEnum;
import nc.noumea.mairie.sirh.eae.dto.EaeAppreciationsDto;
import nc.noumea.mairie.sirh.eae.dto.EaeAutoEvaluationDto;
import nc.noumea.mairie.sirh.eae.dto.EaeEvaluationDto;
import nc.noumea.mairie.sirh.eae.dto.EaeEvolutionDto;
import nc.noumea.mairie.sirh.eae.dto.EaeResultatsDto;
import nc.noumea.mairie.sirh.eae.dto.identification.EaeIdentificationDto;
import nc.noumea.mairie.sirh.eae.dto.identification.EaeIdentificationSituationDto;
import nc.noumea.mairie.sirh.eae.dto.planAction.EaePlanActionDto;
import nc.noumea.mairie.sirh.eae.dto.planAction.PlanActionItemDto;
import nc.noumea.mairie.sirh.eae.dto.poste.EaeFichePosteDto;
import nc.noumea.mairie.sirh.eae.dto.poste.SpbhorDto;
import nc.noumea.mairie.sirh.eae.dto.util.ValueWithListDto;
import nc.noumea.mairie.sirh.eae.service.dataConsistency.EaeDataConsistencyServiceException;
import nc.noumea.mairie.sirh.eae.service.dataConsistency.IEaeDataConsistencyService;
import nc.noumea.mairie.sirh.service.IAgentService;
import nc.noumea.mairie.sirh.ws.ISirhWsConsumer;
import nc.noumea.mairie.sirh.ws.SirhWSConsumerException;

import org.joda.time.DateTime;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.mock.staticmock.MockStaticEntityMethods;
import org.springframework.test.util.ReflectionTestUtils;

@MockStaticEntityMethods
public class EvaluationServiceTest {

	private static ITypeObjectifService objService;

	@BeforeClass
	public static void SetUp() {
		objService = mock(ITypeObjectifService.class);
		EaeTypeObjectif t1 = new EaeTypeObjectif();
		t1.setLibelle("PROFESSIONNEL");
		when(objService.getTypeObjectifForLibelle("PROFESSIONNEL")).thenReturn(t1);
		EaeTypeObjectif t2 = new EaeTypeObjectif();
		t2.setLibelle("INDIVIDUEL");
		when(objService.getTypeObjectifForLibelle("INDIVIDUEL")).thenReturn(t2);
		EaeTypeObjectif t3 = new EaeTypeObjectif();
		t3.setLibelle("FINANCIERS");
		when(objService.getTypeObjectifForLibelle("FINANCIERS")).thenReturn(t3);
		EaeTypeObjectif t4 = new EaeTypeObjectif();
		t4.setLibelle("MATERIELS");
		when(objService.getTypeObjectifForLibelle("MATERIELS")).thenReturn(t4);
		EaeTypeObjectif t5 = new EaeTypeObjectif();
		t5.setLibelle("AUTRES");
		when(objService.getTypeObjectifForLibelle("AUTRES")).thenReturn(t5);
	}

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

		IEaeService eaeService = mock(IEaeService.class);
		when(eaeService.findEae(123)).thenReturn(eae);
		
		EvaluationService service = new EvaluationService();
		ReflectionTestUtils.setField(service, "agentService", agentServiceMock);
		ReflectionTestUtils.setField(service, "eaeService", eaeService);

		// When
		EaeIdentificationDto result = service.getEaeIdentification(123);

		// Then
		assertEquals(123, result.getIdEae());

		verify(agentServiceMock, times(1)).fillEaeEvaluateurWithAgent(eval1);
		verify(agentServiceMock, times(1)).fillEaeEvalueWithAgent(evalue);
	}

	@Test
	public void testGetEaeIdentification_WithEaeWithNoFichePoste_FillAgentsAndReturn() {

		// Given
		Eae eae = new Eae();
		eae.setIdEae(123);
		EaeEvaluateur eval1 = new EaeEvaluateur();
		eae.getEaeEvaluateurs().add(eval1);
		EaeEvalue evalue = new EaeEvalue();
		eae.setEaeEvalue(evalue);
		
		IAgentService agentServiceMock = mock(IAgentService.class);

		IEaeService eaeService = mock(IEaeService.class);
		when(eaeService.findEae(123)).thenReturn(eae);
		
		EvaluationService service = new EvaluationService();
		ReflectionTestUtils.setField(service, "agentService", agentServiceMock);
		ReflectionTestUtils.setField(service, "eaeService", eaeService);

		// When
		EaeIdentificationDto result = service.getEaeIdentification(123);

		// Then
		assertEquals(123, result.getIdEae());

		verify(agentServiceMock, times(1)).fillEaeEvaluateurWithAgent(eval1);
		verify(agentServiceMock, times(1)).fillEaeEvalueWithAgent(evalue);
	}

	@Test
	public void testGetEaeIdentification_WithEaeWithNoEvaluateur_FillAgentsAndReturn() {

		// Given
		Eae eae = new Eae();
		eae.setIdEae(123);
		EaeEvalue evalue = new EaeEvalue();
		eae.setEaeEvalue(evalue);
		
		IAgentService agentServiceMock = mock(IAgentService.class);

		IEaeService eaeService = mock(IEaeService.class);
		when(eaeService.findEae(123)).thenReturn(eae);
		
		EvaluationService service = new EvaluationService();
		ReflectionTestUtils.setField(service, "agentService", agentServiceMock);
		ReflectionTestUtils.setField(service, "eaeService", eaeService);

		// When
		EaeIdentificationDto result = service.getEaeIdentification(123);

		// Then
		assertEquals(123, result.getIdEae());

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

		IEaeService eaeService = mock(IEaeService.class);
		when(eaeService.findEae(123)).thenReturn(eae);

		EvaluationService service = new EvaluationService();
		ReflectionTestUtils.setField(service, "agentService", agentServiceMock);
		ReflectionTestUtils.setField(service, "eaeService", eaeService);

		// When
		List<EaeFichePosteDto> result = service.getEaeFichePoste(123);

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

		IEaeService eaeService = mock(IEaeService.class);
		when(eaeService.findEae(123)).thenReturn(eae);

		EvaluationService service = new EvaluationService();
		ReflectionTestUtils.setField(service, "agentService", agentServiceMock);
		ReflectionTestUtils.setField(service, "eaeService", eaeService);

		// When
		List<EaeFichePosteDto> result = service.getEaeFichePoste(123);

		// Then
		assertEquals(2, result.size());
		assertEquals(123, result.get(0).getIdEae());
		assertEquals(123, result.get(1).getIdEae());

		verify(agentServiceMock, times(1)).fillEaeFichePosteWithAgent(fdp);
		verify(agentServiceMock, times(1)).fillEaeFichePosteWithAgent(fdp2);
	}

	@Test
	public void testGetEaeFichePoste_WithNoEaeFichePoste_ReturnAnEmptyList() {
		// Given
		Eae eae = new Eae();
		eae.setIdEae(123);

		IAgentService agentServiceMock = mock(IAgentService.class);

		IEaeService eaeService = mock(IEaeService.class);
		when(eaeService.findEae(123)).thenReturn(eae);

		EvaluationService service = new EvaluationService();
		ReflectionTestUtils.setField(service, "agentService", agentServiceMock);
		ReflectionTestUtils.setField(service, "eaeService", eaeService);

		// When
		List<EaeFichePosteDto> result = service.getEaeFichePoste(123);

		// Then
		assertEquals(0, result.size());
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

		IEaeService eaeService = mock(IEaeService.class);
		when(eaeService.findEae(789)).thenReturn(eae);

		EvaluationService service = new EvaluationService();
		ReflectionTestUtils.setField(service, "eaeService", eaeService);

		// When
		EaeResultatsDto result = service.getEaeResultats(789);

		// Then
		assertEquals(789, result.getIdEae());
	}

	@Test
	public void testSetEaeResultats_1CommentAndNoResultats_createEaeCommentaire() throws EvaluationServiceException, EaeServiceException {

		// Given
		EaeResultatsDto dto = new EaeResultatsDto();
		dto.setCommentaireGeneral("The Comment!");
		dto.setIdEae(789);

		Eae eae = spy(new Eae());
		// org.mockito.Mockito.doNothing().when(eae).flush();
		// TODO corriger spy flush
		IEaeService eaeServiceMock = Mockito.mock(IEaeService.class);
		when(eaeServiceMock.startEae(789)).thenReturn(eae);

		EvaluationService service = new EvaluationService();
		ReflectionTestUtils.setField(service, "eaeService", eaeServiceMock);

		// When
		try {
			service.setEaeResultats(789, dto);
		} catch (EaeServiceException e) {
			fail("EaeServiceException");
		}

		// Then
		Mockito.verify(eaeServiceMock, Mockito.times(1)).flush();
		assertEquals(dto.getCommentaireGeneral(), eae.getCommentaire().getText());
	}

	@Test
	public void testSetEaeResultats_1CommentAndNoResultats_updateEaeCommentaire() throws EvaluationServiceException, EaeServiceException {

		// Given
		EaeResultatsDto dto = new EaeResultatsDto();
		dto.setCommentaireGeneral("The Comment!");
		dto.setIdEae(789);

		Eae eae = spy(new Eae());
		// org.mockito.Mockito.doNothing().when(eae).flush();
		// TODO corriger spy flush
		EaeCommentaire c = new EaeCommentaire();
		c.setText("text before");
		eae.setCommentaire(c);

		IEaeService eaeServiceMock = Mockito.mock(IEaeService.class);
		when(eaeServiceMock.startEae(789)).thenReturn(eae);

		EvaluationService service = new EvaluationService();
		ReflectionTestUtils.setField(service, "eaeService", eaeServiceMock);

		// When
		try {
			service.setEaeResultats(789, dto);
		} catch (EaeServiceException e) {
			fail("EaeServiceException");
		}

		// Then
		Mockito.verify(eaeServiceMock, Mockito.times(1)).flush();
		assertEquals(dto.getCommentaireGeneral(), eae.getCommentaire().getText());
	}

	@Test
	public void testSetEaeResultats_1NewResultatPro_createResultatWithRightObjectifType()
			throws EvaluationServiceException, EaeServiceException {

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
		// org.mockito.Mockito.doNothing().when(eae).flush();
		// TODO corriger spy flush
		IEaeService eaeServiceMock = Mockito.mock(IEaeService.class);
		when(eaeServiceMock.startEae(789)).thenReturn(eae);

		EvaluationService service = new EvaluationService();
		ReflectionTestUtils.setField(service, "typeObjectifService", objService);
		ReflectionTestUtils.setField(service, "eaeService", eaeServiceMock);

		// When
		service.setEaeResultats(789, dto);

		// Then
		Mockito.verify(eaeServiceMock, Mockito.times(1)).flush();
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
	public void testSetEaeResultats_1NewResultatIndividuel_createResultatWithRightObjectifType()
			throws EvaluationServiceException, EaeServiceException {

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
		// org.mockito.Mockito.doNothing().when(eae).flush();
		// TODO corriger spy flush

		IEaeService eaeServiceMock = Mockito.mock(IEaeService.class);
		when(eaeServiceMock.startEae(789)).thenReturn(eae);
		
		EvaluationService service = new EvaluationService();
		ReflectionTestUtils.setField(service, "typeObjectifService", objService);
		ReflectionTestUtils.setField(service, "eaeService", eaeServiceMock);

		// When
		service.setEaeResultats(789, dto);

		// Then
		Mockito.verify(eaeServiceMock, Mockito.times(1)).flush();
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
	public void testSetEaeResultats_1ExistingResultat_updateResultat() throws EvaluationServiceException, EaeServiceException {

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
		// org.mockito.Mockito.doNothing().when(eae).flush();
		// TODO corriger spy flush
		EaeResultat existingResultat = new EaeResultat();
		existingResultat.setEae(eae);
		existingResultat.setIdEaeResultat(678);
		existingResultat.setObjectif("old obj");
		existingResultat.setResultat("old res");
		EaeCommentaire cExisting = new EaeCommentaire();
		cExisting.setText("old obj comment");
		existingResultat.setCommentaire(c);
		eae.getEaeResultats().add(existingResultat);

		IEaeService eaeServiceMock = Mockito.mock(IEaeService.class);
		when(eaeServiceMock.startEae(789)).thenReturn(eae);

		EvaluationService service = new EvaluationService();
		ReflectionTestUtils.setField(service, "eaeService", eaeServiceMock);

		// When
		service.setEaeResultats(789, dto);

		// Then
		Mockito.verify(eaeServiceMock, Mockito.times(1)).flush();
		assertEquals(1, eae.getEaeResultats().size());

		Iterator<EaeResultat> it = eae.getEaeResultats().iterator();
		EaeResultat resultat = it.next();
		assertEquals(eae, resultat.getEae());
		assertEquals("new obj", resultat.getObjectif());
		assertEquals("new res", resultat.getResultat());
		assertEquals("new obj comment", resultat.getCommentaire().getText());
	}

	@Test
	public void testSetEaeResultats_2ExistingResultats_1MissingInDto_deleteResultatAndComment()
			throws EvaluationServiceException, EaeServiceException {

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
		// org.mockito.Mockito.doNothing().when(eae).flush();
		// TODO corriger spy flush
		EaeResultat existingResultat = new EaeResultat();
		existingResultat.setEae(eae);
		existingResultat.setIdEaeResultat(678);
		existingResultat.setObjectif("old obj");
		existingResultat.setResultat("old res");
		EaeCommentaire cExisting = new EaeCommentaire();
		cExisting.setIdEaeCommentaire(11);
		cExisting.setText("old obj comment");
		existingResultat.setCommentaire(cExisting);
		eae.getEaeResultats().add(existingResultat);

		EaeResultat existingResultat2 = spy(new EaeResultat());
		// org.mockito.Mockito.doNothing().when(existingResultat2).remove();
		// TODO gerer le remove
		existingResultat2.setEae(eae);
		existingResultat2.setIdEaeResultat(679);
		existingResultat2.setObjectif("old obj2");
		existingResultat2.setResultat("old res2");
		EaeCommentaire cExisting2 = new EaeCommentaire();
		cExisting2.setIdEaeCommentaire(12);
		cExisting2.setText("old obj comment2");
		existingResultat2.setCommentaire(cExisting2);
		eae.getEaeResultats().add(existingResultat2);

		IEaeService eaeServiceMock = Mockito.mock(IEaeService.class);
		when(eaeServiceMock.startEae(789)).thenReturn(eae);

		EvaluationService service = new EvaluationService();
		ReflectionTestUtils.setField(service, "eaeService", eaeServiceMock);

		// When
		service.setEaeResultats(789, dto);

		// Then
		Mockito.verify(eaeServiceMock, Mockito.times(1)).remove(existingResultat2);
		Mockito.verify(eaeServiceMock, Mockito.times(1)).flush();
		assertEquals(1, eae.getEaeResultats().size());

		Iterator<EaeResultat> it = eae.getEaeResultats().iterator();
		EaeResultat resultat = it.next();
		assertEquals(eae, resultat.getEae());
		assertEquals("new obj", resultat.getObjectif());
		assertEquals("new res", resultat.getResultat());
		assertEquals("new obj comment", resultat.getCommentaire().getText());

		// org.mockito.Mockito.verify(existingResultat2).remove();
		// TODO gerer le remove
	}

	@Test
	public void testGetEaeAppreciations_WithEae_FillResultatsDtoAndReturn() {

		// Given
		Eae eae = new Eae();
		eae.setIdEae(789);
		eae.setEaeEvalue(new EaeEvalue());

		IEaeService eaeService = mock(IEaeService.class);
		when(eaeService.findEae(789)).thenReturn(eae);

		EvaluationService service = new EvaluationService();
		ReflectionTestUtils.setField(service, "eaeService", eaeService);

		// When
		EaeAppreciationsDto result = service.getEaeAppreciations(789);

		// Then
		assertEquals(789, result.getIdEae());
	}

	@Test
	public void testSetEaeAppreciations_NoAppreciations_CreateEaeAppreciations() throws EaeServiceException {

		// Given
		Eae eae = spy(new Eae());
		eae.setEaeEvalue(new EaeEvalue());
		// org.mockito.Mockito.doNothing().when(eae).flush();
		// TODO corriger spy flush

		eae.setIdEae(789);
		
		EaeAppreciationsDto dto = new EaeAppreciationsDto();
			dto.setIdEae(789);
			dto.setEstEncadrant(true);
			dto.setTechniqueEvalue(new String[] { "A", "B", "C", "D" });
			dto.setTechniqueEvaluateur(new String[] { "A", "B", "C", "D" });
			dto.setSavoirEtreEvalue(new String[] { "A", "B", "C", "D" });
			dto.setSavoirEtreEvaluateur(new String[] { "A", "B", "C", "D" });
			dto.setManagerialEvalue(new String[] { "A", "B", "C", "D" });
			dto.setManagerialEvaluateur(new String[] { "A", "B", "C", "D" });
			dto.setResultatsEvaluateur(new String[] { "A", "B", "C", "D" });
			dto.setResultatsEvalue(new String[] { "A", "B", "C", "D" });

		IEaeService eaeServiceMock = Mockito.mock(IEaeService.class);
		when(eaeServiceMock.startEae(789)).thenReturn(eae);
		
		EvaluationService service = new EvaluationService();
		ReflectionTestUtils.setField(service, "eaeService", eaeServiceMock);

		// When
		service.setEaeAppreciations(789, dto);

		// Then
		Mockito.verify(eaeServiceMock, Mockito.times(1)).flush();
		assertEquals(16, eae.getEaeAppreciations().size());
		assertTrue(eae.getEaeEvalue().isEstEncadrant());
	}

	@Test
	public void testGetEaeEvaluation_WithEae_FillResultatsDtoAndReturn() {

		// Given
		Eae eae = new Eae();
		eae.setIdEae(789);
		EaeEvaluation eval = new EaeEvaluation();
		eval.setEae(eae);
		eae.setEaeEvaluation(eval);
		EaeEvalue evalue = new EaeEvalue();
		evalue.setStatut(EaeAgentStatutEnum.F);
		eae.setEaeEvalue(evalue);

		EaeCampagne camp = new EaeCampagne();
		eae.setEaeCampagne(camp);

		IEaeService eaeServiceMock = Mockito.mock(IEaeService.class);
		when(eaeServiceMock.findEae(789)).thenReturn(eae);

		EvaluationService service = new EvaluationService();
		ReflectionTestUtils.setField(service, "eaeService", eaeServiceMock);

		// When
		EaeEvaluationDto result = service.getEaeEvaluation(789);

		// Then
		assertEquals(789, result.getIdEae());
	}

	@Test
	public void testSetEaeEvaluation_valuesInDto_setEaeEvaluationValues() throws EvaluationServiceException,
			EaeDataConsistencyServiceException, EaeServiceException {

		// Given
		EaeEvaluationDto dto = new EaeEvaluationDto();
		dto.setIdEae(13);
		dto.setDureeEntretien(127);
		dto.setNoteAnneeN1(13f);
		dto.setNoteAnneeN2(14f);
		dto.setNoteAnneeN3(15f);
		dto.setAvisRevalorisation(true);
		dto.setAvisChangementClasse(false);
		ValueWithListDto subDto = new ValueWithListDto(EaeAvancementEnum.MAXI, EaeAvancementEnum.class);
		dto.setPropositionAvancement(subDto);

		ValueWithListDto subDto2 = new ValueWithListDto(EaeNiveauEnum.NECESSITANT_DES_PROGRES, EaeNiveauEnum.class);
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

		IEaeDataConsistencyService dcService = mock(IEaeDataConsistencyService.class);

		IEaeService eaeServiceMock = Mockito.mock(IEaeService.class);
		when(eaeServiceMock.startEae(789)).thenReturn(eae);

		EvaluationService service = new EvaluationService();
		ReflectionTestUtils.setField(service, "eaeDataConsistencyService", dcService);
		ReflectionTestUtils.setField(service, "eaeService", eaeServiceMock);

		// When
		service.setEaeEvaluation(789, dto);

		// Then
		assertEquals("com3", eval.getCommentaireEvaluateur().getText());
		assertEquals("com4", eval.getCommentaireEvalue().getText());
		assertEquals("com1", eval.getCommentaireAvctEvaluateur().getText());
		assertEquals("com2", eval.getCommentaireAvctEvalue().getText());
		assertEquals(0, (int) eval.getAvisChangementClasse());
		assertEquals(1, (int) eval.getAvisRevalorisation());
		assertEquals(new Integer(127), eval.getEae().getDureeEntretienMinutes());
		assertEquals(EaeNiveauEnum.NECESSITANT_DES_PROGRES, eval.getNiveauEae());
		assertEquals(EaeAvancementEnum.MAXI, eval.getPropositionAvancement());
	}

	@Test
	public void testSetEaeEvaluation_valuesInDtoAndExistingValuesInEvaluation_overwriteValues()
			throws EvaluationServiceException, EaeDataConsistencyServiceException, EaeServiceException {

		// Given
		EaeEvaluationDto dto = new EaeEvaluationDto();
		dto.setIdEae(13);
		dto.setDureeEntretien(127);
		dto.setNoteAnneeN1(13f);
		dto.setNoteAnneeN2(14f);
		dto.setNoteAnneeN3(15f);
		dto.setAvisRevalorisation(true);
		dto.setAvisChangementClasse(false);
		ValueWithListDto subDto = new ValueWithListDto(EaeAvancementEnum.MAXI, EaeAvancementEnum.class);
		dto.setPropositionAvancement(subDto);

		ValueWithListDto subDto2 = new ValueWithListDto(EaeNiveauEnum.SATISFAISANT, EaeNiveauEnum.class);
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
		eae.setIdEae(789);
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

		IEaeDataConsistencyService dcService = mock(IEaeDataConsistencyService.class);

		IEaeService eaeService = Mockito.mock(IEaeService.class);
		when(eaeService.startEae(789)).thenReturn(eae);

		EvaluationService service = new EvaluationService();
		ReflectionTestUtils.setField(service, "eaeDataConsistencyService", dcService);
		ReflectionTestUtils.setField(service, "eaeService", eaeService);

		// When
		service.setEaeEvaluation(789, dto);

		// Then
		assertEquals(comBefore1.getIdEaeCommentaire(), eval.getCommentaireEvaluateur().getIdEaeCommentaire());
		assertEquals("com1", eval.getCommentaireEvaluateur().getText());
		assertEquals(comBefore2.getIdEaeCommentaire(), eval.getCommentaireEvalue().getIdEaeCommentaire());
		assertEquals("com2", eval.getCommentaireEvalue().getText());
		assertEquals(comBefore3.getIdEaeCommentaire(), eval.getCommentaireAvctEvaluateur().getIdEaeCommentaire());
		assertEquals("com3", eval.getCommentaireAvctEvaluateur().getText());
		assertEquals(comBefore4.getIdEaeCommentaire(), eval.getCommentaireAvctEvalue().getIdEaeCommentaire());
		assertEquals("com4", eval.getCommentaireAvctEvalue().getText());
		assertEquals(0, (int) eval.getAvisChangementClasse());
		assertEquals(1, (int) eval.getAvisRevalorisation());
		assertEquals(new Integer(127), eval.getEae().getDureeEntretienMinutes());
		assertEquals(EaeNiveauEnum.SATISFAISANT, eval.getNiveauEae());
		assertEquals(EaeAvancementEnum.MAXI, eval.getPropositionAvancement());
	}

	@Test
	public void testSetEaeEvaluation_NiveauIsNull_throwException() throws EaeServiceException {

		// Given
		EaeEvaluationDto dto = new EaeEvaluationDto();
		dto.setPropositionAvancement(new ValueWithListDto());

		dto.setNiveau(new ValueWithListDto());

		Eae eae = new Eae();
		eae.setIdEae(789);
		EaeEvaluation eval = new EaeEvaluation();
		eae.setEaeEvaluation(eval);
		eval.setEae(eae);

		IEaeService eaeService = Mockito.mock(IEaeService.class);
		when(eaeService.startEae(789)).thenReturn(eae);

		EvaluationService service = new EvaluationService();
		ReflectionTestUtils.setField(service, "eaeService", eaeService);

		try {
			// When
			service.setEaeEvaluation(789, dto);
		} catch (EvaluationServiceException e) {
			// Then
			assertEquals("La propriété 'niveau' de l'évaluation est manquante.", e.getMessage());
			return;
		}

		fail("Should have thrown an exception here!");
	}

	@Test
	public void testSetEaeEvaluation_NiveauIsIncorrectNumber_throwException() throws EaeServiceException {

		// Given
		EaeEvaluationDto dto = new EaeEvaluationDto();
		dto.setPropositionAvancement(new ValueWithListDto());

		ValueWithListDto subDto2 = new ValueWithListDto();
		subDto2.setCourant("blabla");
		dto.setNiveau(subDto2);

		Eae eae = new Eae();
		eae.setIdEae(789);
		EaeEvaluation eval = new EaeEvaluation();
		eae.setEaeEvaluation(eval);
		eval.setEae(eae);

		IEaeService eaeService = Mockito.mock(IEaeService.class);
		when(eaeService.startEae(789)).thenReturn(eae);

		EvaluationService service = new EvaluationService();
		ReflectionTestUtils.setField(service, "eaeService", eaeService);

		try {
			// When
			service.setEaeEvaluation(789, dto);
		} catch (EvaluationServiceException e) {
			// Then
			assertEquals("La propriété 'niveau' de l'évaluation est incorrecte.", e.getMessage());
			return;
		}

		fail("Should have thrown an exception here!");
	}

	@Test
	public void testSetEaeEvaluation_NiveauIsIncorrectString_throwException() throws EaeServiceException {

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

		IEaeService eaeService = Mockito.mock(IEaeService.class);
		when(eaeService.startEae(789)).thenReturn(eae);

		EvaluationService service = new EvaluationService();
		ReflectionTestUtils.setField(service, "eaeService", eaeService);

		try {
			// When
			service.setEaeEvaluation(789, dto);
		} catch (EvaluationServiceException e) {
			// Then
			assertEquals("La propriété 'niveau' de l'évaluation est incorrecte.", e.getMessage());
			return;
		}

		fail("Should have thrown an exception here!");
	}

	@Test
	public void testSetEaeEvaluation_AvancementIsIncorrect_throwException() throws EaeServiceException {

		// Given
		EaeEvaluationDto dto = new EaeEvaluationDto();
		ValueWithListDto subDto = new ValueWithListDto();
		subDto.setCourant("INVALID");
		dto.setPropositionAvancement(subDto);

		ValueWithListDto subDto2 = new ValueWithListDto();
		subDto2.setCourant("SATISFAISANT");
		dto.setNiveau(subDto2);

		Eae eae = new Eae();
		EaeEvaluation eval = new EaeEvaluation();
		eae.setEaeEvaluation(eval);
		eval.setEae(eae);

		IEaeService eaeService = Mockito.mock(IEaeService.class);
		when(eaeService.startEae(789)).thenReturn(eae);

		EvaluationService service = new EvaluationService();
		ReflectionTestUtils.setField(service, "eaeService", eaeService);

		try {
			// When
			service.setEaeEvaluation(789, dto);
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
		evaluation.setAvisRevalorisation(0);
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
		evaluation.setAvisRevalorisation(1);
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
		evalue.setTypeAvancement(EaeTypeAvctEnum.PROMO);
		eae.setEaeEvalue(evalue);
		EaeEvaluation evaluation = new EaeEvaluation();
		evaluation.setAvisChangementClasse(0);
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
		evalue.setTypeAvancement(EaeTypeAvctEnum.PROMO);
		eae.setEaeEvalue(evalue);
		EaeEvaluation evaluation = new EaeEvaluation();
		evaluation.setAvisChangementClasse(1);
		eae.setEaeEvaluation(evaluation);

		EvaluationService service = new EvaluationService();

		// When
		service.calculateAvisShd(eae);

		// Then
		assertEquals("Favorable", evaluation.getAvisShd());
	}

	@Test
	public void testCalculateAvisShd_typeAvctIsPROMOAndAvisMINI() {

		// Given
		Eae eae = new Eae();
		EaeEvalue evalue = new EaeEvalue();
		evalue.setTypeAvancement(EaeTypeAvctEnum.AD);
		eae.setEaeEvalue(evalue);
		EaeEvaluation evaluation = new EaeEvaluation();
		evaluation.setPropositionAvancement(EaeAvancementEnum.MINI);
		eae.setEaeEvaluation(evaluation);

		EvaluationService service = new EvaluationService();

		// When
		service.calculateAvisShd(eae);

		// Then
		assertEquals("Durée minimale", evaluation.getAvisShd());
	}

	@Test
	public void testCalculateAvisShd_typeAvctIsPROMOAndAvisMAXI() {

		// Given
		Eae eae = new Eae();
		EaeEvalue evalue = new EaeEvalue();
		evalue.setTypeAvancement(EaeTypeAvctEnum.AD);
		eae.setEaeEvalue(evalue);
		EaeEvaluation evaluation = new EaeEvaluation();
		evaluation.setPropositionAvancement(EaeAvancementEnum.MAXI);
		eae.setEaeEvaluation(evaluation);

		EvaluationService service = new EvaluationService();

		// When
		service.calculateAvisShd(eae);

		// Then
		assertEquals("Durée maximale", evaluation.getAvisShd());
	}

	@Test
	public void testGetEaeAutoEvaluation_WithEae_FillResultatsDtoAndReturn() {

		// Given
		Eae eae = new Eae();
		eae.setIdEae(789);
		EaeAutoEvaluation eval = new EaeAutoEvaluation();
		eval.setEae(eae);
		eae.setEaeAutoEvaluation(eval);

		IEaeService eaeService = Mockito.mock(IEaeService.class);
		when(eaeService.findEae(789)).thenReturn(eae);

		EvaluationService service = new EvaluationService();
		ReflectionTestUtils.setField(service, "eaeService", eaeService);

		// When
		EaeAutoEvaluationDto result = service.getEaeAutoEvaluation(789);

		// Then
		assertEquals(789, result.getIdEae());
	}

	@Test
	public void testSetEaeAutoEvaluation_WithEaeAutoEvalAlreadyExisting_FillResultatsFromDtoAndReturn() throws EaeServiceException {

		// Given
		Eae eae = new Eae();
		eae.setIdEae(789);
		EaeAutoEvaluation eval = new EaeAutoEvaluation();
		eval.setEae(eae);
		eae.setEaeAutoEvaluation(eval);

		EaeAutoEvaluationDto dto = new EaeAutoEvaluationDto();
		dto.setParticularites("particularités");
		dto.setAcquis("acquis");
		dto.setSuccesDifficultes("succès");

		IEaeService eaeService = Mockito.mock(IEaeService.class);
		when(eaeService.startEae(789)).thenReturn(eae);

		EvaluationService service = new EvaluationService();
		ReflectionTestUtils.setField(service, "eaeService", eaeService);

		// When
		service.setEaeAutoEvaluation(789, dto);

		// Then
		assertEquals("particularités", eae.getEaeAutoEvaluation().getParticularites());
		assertEquals("acquis", eae.getEaeAutoEvaluation().getAcquis());
		assertEquals("succès", eae.getEaeAutoEvaluation().getSuccesDifficultes());
	}

	@Test
	public void testSetEaeAutoEvaluation_WithNoEaeAutoEvalxisting_FillResultatsFromDtoAndReturn() throws EaeServiceException {

		// Given
		Eae eae = new Eae();
		eae.setIdEae(789);

		EaeAutoEvaluationDto dto = new EaeAutoEvaluationDto();
		dto.setParticularites("particularités");
		dto.setAcquis("acquis");
		dto.setSuccesDifficultes("succès");

		IEaeService eaeService = Mockito.mock(IEaeService.class);
		when(eaeService.startEae(789)).thenReturn(eae);

		EvaluationService service = new EvaluationService();
		ReflectionTestUtils.setField(service, "eaeService", eaeService);

		// When
		service.setEaeAutoEvaluation(789, dto);

		// Then
		assertEquals(new Integer(789), eae.getEaeAutoEvaluation().getEae().getIdEae());
		assertEquals("particularités", eae.getEaeAutoEvaluation().getParticularites());
		assertEquals("acquis", eae.getEaeAutoEvaluation().getAcquis());
		assertEquals("succès", eae.getEaeAutoEvaluation().getSuccesDifficultes());
	}

	@Test
	public void testGetEaePlanAction_WithEae_FillPlanActionDtoAndReturn() {

		// Given
		Eae eae = new Eae();
		eae.setIdEae(789);

		IEaeService eaeService = Mockito.mock(IEaeService.class);
		when(eaeService.findEae(789)).thenReturn(eae);

		EvaluationService service = new EvaluationService();
		ReflectionTestUtils.setField(service, "eaeService", eaeService);

		// When
		EaePlanActionDto result = service.getEaePlanAction(789);

		// Then
		assertEquals(789, result.getIdEae());
	}

	@Test
	public void testSetEaePlanAction_EmptyEaeEvaluation_FillPlanActionfromDto() throws EaeServiceException {

		// Given
		Eae eae = new Eae();
		eae.setIdEae(789);

		EaePlanActionDto dto = new EaePlanActionDto();
		dto.setIdEae(789);

		PlanActionItemDto item = new PlanActionItemDto();
		item.setObjectif("obj1");
		item.setIndicateur("mes1");
		dto.getObjectifsProfessionnels().add(item);

		dto.getObjectifsIndividuels().add("obj2");
		dto.getMoyensMateriels().add("moy1");
		dto.getMoyensFinanciers().add("moy2");
		dto.getMoyensAutres().add("moy3");

		IEaeService eaeService = Mockito.mock(IEaeService.class);
		when(eaeService.startEae(789)).thenReturn(eae);

		EvaluationService service = new EvaluationService();
		ReflectionTestUtils.setField(service, "typeObjectifService", objService);
		ReflectionTestUtils.setField(service, "eaeService", eaeService);

		// When
		service.setEaePlanAction(789, dto);

		// Then
		assertEquals(5, eae.getEaePlanActions().size());
	}

	@Test
	public void testSetEaePlanAction_8ExistingEaeEvaluations_FillPlanActionfromDto() throws EaeServiceException {

		// Given
		Eae eae = new Eae();
		eae.setIdEae(789);
		eae.getEaePlanActions().add(new EaePlanAction());
		eae.getEaePlanActions().add(new EaePlanAction());
		eae.getEaePlanActions().add(new EaePlanAction());
		eae.getEaePlanActions().add(new EaePlanAction());
		eae.getEaePlanActions().add(new EaePlanAction());
		eae.getEaePlanActions().add(new EaePlanAction());
		eae.getEaePlanActions().add(new EaePlanAction());
		eae.getEaePlanActions().add(new EaePlanAction());

		EaePlanActionDto dto = new EaePlanActionDto();
		dto.setIdEae(789);

		PlanActionItemDto item = new PlanActionItemDto();
		item.setObjectif("obj1");
		item.setIndicateur("mes1");
		dto.getObjectifsProfessionnels().add(item);

		dto.getObjectifsIndividuels().add("obj2");
		dto.getMoyensMateriels().add("moy1");
		dto.getMoyensFinanciers().add("moy2");
		dto.getMoyensAutres().add("moy3");

		IEaeService eaeService = Mockito.mock(IEaeService.class);
		when(eaeService.startEae(789)).thenReturn(eae);

		EvaluationService service = new EvaluationService();
		ReflectionTestUtils.setField(service, "typeObjectifService", objService);
		ReflectionTestUtils.setField(service, "eaeService", eaeService);

		// When
		service.setEaePlanAction(789, dto);

		// Then
		assertEquals(5, eae.getEaePlanActions().size());
	}

	@Test
	public void testGetEaeEvolution_WithEae_FillEvolutionDtoAndReturn() throws SirhWSConsumerException {

		// Given
		Eae eae = new Eae();
		eae.setIdEae(789);

		ISirhWsConsumer sirhWSConsumer = mock(ISirhWsConsumer.class);
		when(sirhWSConsumer.getListSpbhor()).thenReturn(new ArrayList<SpbhorDto>());

		IEaeService eaeService = Mockito.mock(IEaeService.class);
		when(eaeService.findEae(789)).thenReturn(eae);

		EvaluationService service = new EvaluationService();
		ReflectionTestUtils.setField(service, "sirhWSConsumer", sirhWSConsumer);
		ReflectionTestUtils.setField(service, "eaeService", eaeService);

		// When
		EaeEvolutionDto result = service.getEaeEvolution(789);

		// Then
		assertEquals(789, result.getIdEae());
	}

	@Test
	public void testSetEaeEvolution_EmptyEaeEvolutionSetPropertiesAndCommentaire_FillEvolutionfromDto()
			throws EvaluationServiceException, SirhWSConsumerException, EaeServiceException {

		// Given
		Eae eae = new Eae();
		eae.setIdEae(19);

		EaeEvolutionDto dto = new EaeEvolutionDto();
		dto.setIdEae(19);
		dto.setMobiliteGeo(true);
		dto.setMobiliteFonctionnelle(true);
		dto.setChangementMetier(true);
		dto.setDelaiEnvisage(new ValueWithListDto(EaeDelaiEnum.ENTRE1ET2ANS, EaeDelaiEnum.class));
		dto.setMobiliteService(true);
		dto.setMobiliteDirection(true);
		dto.setMobiliteCollectivite(true);
		dto.setNomCollectivite("nom collectivité");
		dto.setMobiliteAutre(true);
		dto.setConcours(true);
		dto.setNomConcours("nom concours");
		dto.setVae(true);
		dto.setNomVae("nom diplome");
		dto.setTempsPartiel(true);
		dto.setRetraite(true);
		dto.setDateRetraite(new DateTime(2014, 4, 19, 0, 0, 0, 0).toDate());
		dto.setAutrePerspective(true);
		dto.setLibelleAutrePerspective("autre perspective");

		dto.setCommentaireEvolution(new EaeCommentaire());
		dto.getCommentaireEvolution().setText("commentaire evolution");

		dto.setCommentaireEvaluateur(new EaeCommentaire());
		dto.getCommentaireEvaluateur().setText("commentaire evaluateur");

		dto.setCommentaireEvalue(new EaeCommentaire());
		dto.getCommentaireEvalue().setText("commentaire evalue");

		ValueWithListDto tempsPartiel = new ValueWithListDto();
		tempsPartiel.setCourant("1");
		dto.setPourcentageTempsPartiel(tempsPartiel);

		SpbhorDto t = new SpbhorDto();
			t.setCdThor(1);

		IEaeDataConsistencyService dataConsistencyService = mock(IEaeDataConsistencyService.class);

		ISirhWsConsumer sirhWSConsumer = mock(ISirhWsConsumer.class);
		when(sirhWSConsumer.getSpbhorById(1)).thenReturn(t);

		IEaeService eaeService = Mockito.mock(IEaeService.class);
		when(eaeService.startEae(19)).thenReturn(eae);

		EvaluationService service = new EvaluationService();
		ReflectionTestUtils.setField(service, "eaeDataConsistencyService", dataConsistencyService);
		ReflectionTestUtils.setField(service, "sirhWSConsumer", sirhWSConsumer);
		ReflectionTestUtils.setField(service, "eaeService", eaeService);

		// When
		service.setEaeEvolution(19, dto);

		// Then
		EaeEvolution evo = eae.getEaeEvolution();
		assertTrue(evo.isMobiliteGeo());
		assertTrue(evo.isMobiliteFonctionnelle());
		assertTrue(evo.isChangementMetier());
		assertEquals("ENTRE1ET2ANS", evo.getDelaiEnvisage().name());
		assertTrue(evo.isMobiliteService());
		assertTrue(evo.isMobiliteDirection());
		assertTrue(evo.isMobiliteCollectivite());
		assertEquals("nom collectivité", evo.getNomCollectivite());
		assertTrue(evo.isMobiliteAutre());
		assertTrue(evo.isConcours());
		assertEquals("nom concours", evo.getNomConcours());
		assertTrue(evo.isVae());
		assertEquals("nom diplome", evo.getNomVae());
		assertTrue(evo.isTempsPartiel());
		assertEquals(new Integer(1), evo.getTempsPartielIdSpbhor());
		assertTrue(evo.isRetraite());
		assertEquals(new DateTime(2014, 4, 19, 0, 0, 0, 0).toDate(), evo.getDateRetraite());
		assertTrue(evo.isAutrePerspective());
		assertEquals("autre perspective", evo.getLibelleAutrePerspective());
		assertEquals("commentaire evolution", evo.getCommentaireEvolution().getText());
		assertEquals("commentaire evaluateur", evo.getCommentaireEvaluateur().getText());
		assertEquals("commentaire evalue", evo.getCommentaireEvalue().getText());
	}

	@Test
	public void testSetEaeEvolution_EmptyEaeEvolutionSetSouhaitsAndDeveloppements_FillEvolutionfromDto()
			throws EvaluationServiceException, EaeServiceException {

		// Given
		Eae eae = new Eae();
		eae.setIdEae(19);

		EaeEvolutionDto dto = new EaeEvolutionDto();
		dto.setIdEae(19);

		// 1 new souhait
		EaeEvolutionSouhait souhait = new EaeEvolutionSouhait();
		souhait.setSouhait("le souhait");
		souhait.setSuggestion("la suggestion");
		dto.getSouhaitsSuggestions().add(souhait);

		// 1 development per type
		EaeDeveloppement dev1 = new EaeDeveloppement();
		dev1.setLibelle("libelle CONNAISSANCE");
		dev1.setTypeDeveloppement(EaeTypeDeveloppementEnum.CONNAISSANCE);
		dto.getDeveloppementConnaissances().add(dev1);

		EaeDeveloppement dev2 = new EaeDeveloppement();
		dev2.setLibelle("libelle COMPETENCE");
		dev2.setTypeDeveloppement(EaeTypeDeveloppementEnum.COMPETENCE);
		dto.getDeveloppementCompetences().add(dev2);

		EaeDeveloppement dev3 = new EaeDeveloppement();
		dev3.setLibelle("libelle CONCOURS");
		dev3.setTypeDeveloppement(EaeTypeDeveloppementEnum.CONCOURS);
		dto.getDeveloppementExamensConcours().add(dev3);

		EaeDeveloppement dev4 = new EaeDeveloppement();
		dev4.setLibelle("libelle PERSONNEL");
		dev4.setTypeDeveloppement(EaeTypeDeveloppementEnum.PERSONNEL);
		dto.getDeveloppementPersonnel().add(dev4);

		EaeDeveloppement dev5 = new EaeDeveloppement();
		dev5.setLibelle("libelle COMPORTEMENT");
		dev5.setTypeDeveloppement(EaeTypeDeveloppementEnum.COMPORTEMENT);
		dto.getDeveloppementComportement().add(dev5);

		EaeDeveloppement dev6 = new EaeDeveloppement();
		dev6.setLibelle("libelle FORMATEUR");
		dev6.setTypeDeveloppement(EaeTypeDeveloppementEnum.FORMATEUR);
		dto.getDeveloppementFormateur().add(dev6);

		IEaeService eaeService = Mockito.mock(IEaeService.class);
		when(eaeService.startEae(19)).thenReturn(eae);

		IEaeDataConsistencyService dataConsistencyService = mock(IEaeDataConsistencyService.class);

		EvaluationService service = new EvaluationService();
		ReflectionTestUtils.setField(service, "eaeDataConsistencyService", dataConsistencyService);
		ReflectionTestUtils.setField(service, "eaeService", eaeService);

		// When
		service.setEaeEvolution(19, dto);

		// Then
		EaeEvolution evo = eae.getEaeEvolution();

		assertEquals(1, evo.getEaeEvolutionSouhaits().size());
		assertEquals(evo, evo.getEaeEvolutionSouhaits().iterator().next().getEaeEvolution());
		assertEquals("le souhait", evo.getEaeEvolutionSouhaits().iterator().next().getSouhait());
		assertEquals("la suggestion", evo.getEaeEvolutionSouhaits().iterator().next().getSuggestion());

		assertEquals(6, evo.getEaeDeveloppements().size());

		for (EaeDeveloppement dev : evo.getEaeDeveloppements()) {
			assertEquals(evo, dev.getEaeEvolution());
			if (dev.getTypeDeveloppement() == EaeTypeDeveloppementEnum.CONNAISSANCE)
				assertEquals("libelle CONNAISSANCE", dev.getLibelle());
			else if (dev.getTypeDeveloppement() == EaeTypeDeveloppementEnum.COMPETENCE)
				assertEquals("libelle COMPETENCE", dev.getLibelle());
			else if (dev.getTypeDeveloppement() == EaeTypeDeveloppementEnum.CONCOURS)
				assertEquals("libelle CONCOURS", dev.getLibelle());
			else if (dev.getTypeDeveloppement() == EaeTypeDeveloppementEnum.PERSONNEL)
				assertEquals("libelle PERSONNEL", dev.getLibelle());
			else if (dev.getTypeDeveloppement() == EaeTypeDeveloppementEnum.COMPORTEMENT)
				assertEquals("libelle COMPORTEMENT", dev.getLibelle());
			else if (dev.getTypeDeveloppement() == EaeTypeDeveloppementEnum.FORMATEUR)
				assertEquals("libelle FORMATEUR", dev.getLibelle());
			else
				fail("test failed");
		}
	}

	@Test
	public void testSetEaeEvolution_ExistingEaeEvolutionSetSouhait_ReplaceEvolutionfromDto()
			throws EvaluationServiceException, EaeServiceException {

		// Given
		Eae eae = new Eae();
		eae.setIdEae(19);
		EaeEvolution evol = new EaeEvolution();
		eae.setEaeEvolution(evol);

		EaeEvolutionSouhait s1 = new EaeEvolutionSouhait();
		s1.setIdEaeEvolutionSouhait(89);
		s1.setSouhait("souhait");
		s1.setSuggestion("suggestion");
		evol.getEaeEvolutionSouhaits().add(s1);

		EaeEvolutionDto dto = new EaeEvolutionDto();
		dto.setIdEae(19);

		// 1 existing souhait
		EaeEvolutionSouhait existingSouhait = new EaeEvolutionSouhait();
		existingSouhait.setIdEaeEvolutionSouhait(89);
		existingSouhait.setSouhait("le souhait existant");
		existingSouhait.setSuggestion("la suggestion existante");
		dto.getSouhaitsSuggestions().add(existingSouhait);

		IEaeService eaeService = Mockito.mock(IEaeService.class);
		when(eaeService.startEae(19)).thenReturn(eae);

		IEaeDataConsistencyService dataConsistencyService = mock(IEaeDataConsistencyService.class);

		EvaluationService service = new EvaluationService();
		ReflectionTestUtils.setField(service, "eaeDataConsistencyService", dataConsistencyService);
		ReflectionTestUtils.setField(service, "eaeService", eaeService);

		// When
		service.setEaeEvolution(19, dto);

		// Then
		EaeEvolution evo = eae.getEaeEvolution();

		assertEquals(1, evo.getEaeEvolutionSouhaits().size());
		assertEquals("le souhait existant", evo.getEaeEvolutionSouhaits().iterator().next().getSouhait());
		assertEquals("la suggestion existante", evo.getEaeEvolutionSouhaits().iterator().next().getSuggestion());
	}

	@Test
	public void testSetEaeEvolution_2ExistingEaeEvolutionSetSouhait_ReplaceOneAndDeleteOther()
			throws EvaluationServiceException, EaeServiceException {

		// Given
		Eae eae = spy(new Eae());
		// org.mockito.Mockito.doNothing().when(eae).flush();
		// TODO corriger spy flush
		eae.setIdEae(19);
		EaeEvolution evol = new EaeEvolution();
		eae.setEaeEvolution(evol);

		EaeEvolutionSouhait s1 = new EaeEvolutionSouhait();
		s1.setIdEaeEvolutionSouhait(89);
		s1.setSouhait("souhait");
		s1.setSuggestion("suggestion");
		evol.getEaeEvolutionSouhaits().add(s1);

		EaeEvolutionSouhait s2 = spy(new EaeEvolutionSouhait());
		// org.mockito.Mockito.doNothing().when(s2).remove();
		// TODO gerer le remove
		s2.setIdEaeEvolutionSouhait(90);
		s2.setSouhait("souhait 2");
		s2.setSuggestion("suggestion 2");
		evol.getEaeEvolutionSouhaits().add(s2);

		EaeEvolutionDto dto = new EaeEvolutionDto();
		dto.setIdEae(19);

		// 1 existing souhait in DTO
		EaeEvolutionSouhait existingSouhait = new EaeEvolutionSouhait();
		existingSouhait.setIdEaeEvolutionSouhait(89);
		existingSouhait.setSouhait("le souhait existant");
		existingSouhait.setSuggestion("la suggestion existante");
		dto.getSouhaitsSuggestions().add(existingSouhait);

		IEaeDataConsistencyService dataConsistencyService = mock(IEaeDataConsistencyService.class);
		
		IEaeService eaeServiceMock = Mockito.mock(IEaeService.class);
		when(eaeServiceMock.startEae(19)).thenReturn(eae);

		EvaluationService service = new EvaluationService();
		ReflectionTestUtils.setField(service, "eaeDataConsistencyService", dataConsistencyService);
		ReflectionTestUtils.setField(service, "eaeService", eaeServiceMock);

		// When
		service.setEaeEvolution(19, dto);

		// Then
		EaeEvolution evo = eae.getEaeEvolution();

		Mockito.verify(eaeServiceMock, Mockito.times(1)).remove(s2);
		Mockito.verify(eaeServiceMock, Mockito.times(0)).remove(s1);
		assertEquals(1, evo.getEaeEvolutionSouhaits().size());
		assertEquals("le souhait existant", evo.getEaeEvolutionSouhaits().iterator().next().getSouhait());
		assertEquals("la suggestion existante", evo.getEaeEvolutionSouhaits().iterator().next().getSuggestion());

		// verify(s2).remove();
		// TODO gerer le remove
	}

	@Test
	public void testSetEaeEvolution_ExistingEaeEvolutionSetDeveloppement_ReplaceEvolutionfromDto()
			throws EvaluationServiceException, EaeServiceException {

		// Given
		Eae eae = new Eae();
		eae.setIdEae(19);
		EaeEvolution evol = new EaeEvolution();
		eae.setEaeEvolution(evol);

		EaeDeveloppement existingDeveloppement = new EaeDeveloppement();
		existingDeveloppement.setIdEaeDeveloppement(89);
		existingDeveloppement.setLibelle("comportement existant");
		existingDeveloppement.setEcheance(new DateTime(2009, 12, 12, 13, 57, 0, 0).toDate());
		existingDeveloppement.setTypeDeveloppement(EaeTypeDeveloppementEnum.COMPORTEMENT);
		evol.getEaeDeveloppements().add(existingDeveloppement);

		EaeEvolutionDto dto = new EaeEvolutionDto();
		dto.setIdEae(19);

		// 1 existing developpement
		EaeDeveloppement dtoExistingDeveloppement = new EaeDeveloppement();
		dtoExistingDeveloppement.setIdEaeDeveloppement(89);
		dtoExistingDeveloppement.setLibelle("comportement existant");
		dtoExistingDeveloppement.setEcheance(new DateTime(2012, 12, 12, 13, 57, 0, 0).toDate());
		dto.getDeveloppementComportement().add(dtoExistingDeveloppement);

		IEaeDataConsistencyService dataConsistencyService = mock(IEaeDataConsistencyService.class);
		
		IEaeService eaeServiceMock = Mockito.mock(IEaeService.class);
		when(eaeServiceMock.startEae(19)).thenReturn(eae);

		EvaluationService service = new EvaluationService();
		ReflectionTestUtils.setField(service, "eaeDataConsistencyService", dataConsistencyService);
		ReflectionTestUtils.setField(service, "eaeService", eaeServiceMock);

		// When
		service.setEaeEvolution(19, dto);

		// Then
		EaeEvolution evo = eae.getEaeEvolution();

		assertEquals(1, evo.getEaeDeveloppements().size());
		assertEquals("comportement existant", evo.getEaeDeveloppements().iterator().next().getLibelle());
		assertEquals(new DateTime(2012, 12, 12, 13, 57, 0, 0).toDate(), evo.getEaeDeveloppements().iterator().next()
				.getEcheance());
	}

	@Test
	public void testSetEaeEvolution_ExistingEaeEvolutionWithDeveloppement_RemoveDeveloppementfromEvolution()
			throws EvaluationServiceException, EaeServiceException {

		// Given
		Eae eae = new Eae();
		eae.setIdEae(19);
		EaeEvolution evol = new EaeEvolution();
		eae.setEaeEvolution(evol);

		EaeDeveloppement existingDeveloppement = spy(new EaeDeveloppement());
		// org.mockito.Mockito.doNothing().when(existingDeveloppement).remove();
		// TODO gerer le remove
		existingDeveloppement.setIdEaeDeveloppement(89);
		existingDeveloppement.setLibelle("comportement existant");
		existingDeveloppement.setEcheance(new DateTime(2009, 12, 12, 13, 57, 0, 0).toDate());
		existingDeveloppement.setTypeDeveloppement(EaeTypeDeveloppementEnum.COMPORTEMENT);
		existingDeveloppement.setEaeEvolution(evol);
		evol.getEaeDeveloppements().add(existingDeveloppement);

		EaeEvolutionDto dto = new EaeEvolutionDto();
		dto.setIdEae(19);

		IEaeDataConsistencyService dataConsistencyService = mock(IEaeDataConsistencyService.class);
		
		IEaeService eaeServiceMock = Mockito.mock(IEaeService.class);
		when(eaeServiceMock.startEae(19)).thenReturn(eae);

		EvaluationService service = new EvaluationService();
		ReflectionTestUtils.setField(service, "eaeDataConsistencyService", dataConsistencyService);
		ReflectionTestUtils.setField(service, "eaeService", eaeServiceMock);
		// When
		service.setEaeEvolution(19, dto);

		// Then
		EaeEvolution evo = eae.getEaeEvolution();

		assertEquals(0, evo.getEaeDeveloppements().size());

		Mockito.verify(eaeServiceMock, Mockito.times(1)).remove(existingDeveloppement);
		// org.mockito.Mockito.verify(existingDeveloppement).remove();
		// TODO gerer le remove
	}

	@Test
	public void testSetEaeEvolution_ExistingEaeEvolutionWithEvolSouhait_RemoveEvolSouhaitfromEvolution()
			throws EvaluationServiceException, EaeServiceException {

		// Given
		Eae eae = new Eae();
		eae.setIdEae(19);
		EaeEvolution evol = new EaeEvolution();
		eae.setEaeEvolution(evol);

		EaeEvolutionSouhait existingSouhait = spy(new EaeEvolutionSouhait());
		// org.mockito.Mockito.doNothing().when(existingSouhait).remove();
		// TODO gerer le remove
		existingSouhait.setIdEaeEvolutionSouhait(89);
		evol.getEaeEvolutionSouhaits().add(existingSouhait);

		EaeEvolutionDto dto = new EaeEvolutionDto();
		dto.setIdEae(19);

		IEaeDataConsistencyService dataConsistencyService = mock(IEaeDataConsistencyService.class);
		
		IEaeService eaeServiceMock = Mockito.mock(IEaeService.class);
		when(eaeServiceMock.startEae(19)).thenReturn(eae);

		EvaluationService service = new EvaluationService();
		ReflectionTestUtils.setField(service, "eaeDataConsistencyService", dataConsistencyService);
		ReflectionTestUtils.setField(service, "eaeService", eaeServiceMock);

		// When
		service.setEaeEvolution(19, dto);

		// Then
		EaeEvolution evo = eae.getEaeEvolution();

		Mockito.verify(eaeServiceMock, Mockito.times(1)).remove(existingSouhait);
		assertEquals(0, evo.getEaeEvolutionSouhaits().size());
	}

	@Test
	public void testSetEaeEvolution_DelaiEnvisageIsNotValid_throwException() throws EaeServiceException {
		// Given
		EaeEvolutionDto dto = new EaeEvolutionDto();
		dto.setDelaiEnvisage(new ValueWithListDto());
		dto.getDelaiEnvisage().setCourant("");
		
		IEaeService eaeServiceMock = Mockito.mock(IEaeService.class);
		when(eaeServiceMock.startEae(19)).thenReturn(new Eae());

		EvaluationService service = new EvaluationService();
		ReflectionTestUtils.setField(service, "eaeService", eaeServiceMock);

		try {
			// When
			service.setEaeEvolution(19, dto);
		} catch (EvaluationServiceException ex) {
			// Then
			assertEquals("La propriété 'delaiEnvisage' de l'évolution est incorrecte.", ex.getMessage());
			return;
		}

		fail("Should have thrown exception");
	}

	@Test
	public void testSetEaeEvolution_ErrorDuringDataConsistency_throwException()
			throws EaeDataConsistencyServiceException, EaeServiceException {
		// Given
		Eae eae = new Eae();
		EaeEvolutionDto dto = new EaeEvolutionDto();

		IEaeDataConsistencyService dataConsistencyService = mock(IEaeDataConsistencyService.class);
		doThrow(new EaeDataConsistencyServiceException("La propriété 'delaiEnvisage' de l'évolution est incorrecte."))
				.when(dataConsistencyService).checkDataConsistencyForEaeEvolution(eae);
		
		IEaeService eaeServiceMock = Mockito.mock(IEaeService.class);
		when(eaeServiceMock.startEae(19)).thenReturn(eae);

		EvaluationService service = new EvaluationService();
		ReflectionTestUtils.setField(service, "eaeDataConsistencyService", dataConsistencyService);
		ReflectionTestUtils.setField(service, "eaeService", eaeServiceMock);

		try {
			// When
			service.setEaeEvolution(19, dto);
		} catch (EvaluationServiceException ex) {
			// Then
			assertEquals("La propriété 'delaiEnvisage' de l'évolution est incorrecte.", ex.getMessage());
			return;
		}

		fail("Should have thrown exception");
	}

	@Test
	public void testSetEaeIdentification_FillInFromDto() throws EvaluationServiceException, EaeServiceException {

		// Given
		Eae eae = new Eae();
		EaeEvalue evalue = new EaeEvalue();
		eae.setEaeEvalue(evalue);
		evalue.setEae(eae);

		EaeFichePoste fdp = new EaeFichePoste();
		fdp.setPrimary(true);
		eae.getEaeFichePostes().add(fdp);
		eae.getEaeFichePostes().add(new EaeFichePoste());

		EaeIdentificationDto dto = new EaeIdentificationDto();
		dto.setDateEntretien(new DateTime(2012, 01, 03, 0, 0, 0, 0).toDate());
		dto.setSituation(new EaeIdentificationSituationDto());
		dto.getSituation().setDateEntreeAdministration(new DateTime(1972, 01, 10, 0, 0, 0, 0).toDate());
		dto.getSituation().setDateEntreeFonction(new DateTime(1984, 06, 15, 0, 0, 0, 0).toDate());
		
		IEaeService eaeServiceMock = Mockito.mock(IEaeService.class);
		when(eaeServiceMock.startEae(19)).thenReturn(eae);

		EvaluationService service = new EvaluationService();
		ReflectionTestUtils.setField(service, "eaeService", eaeServiceMock);

		// When
		service.setEaeIdentification(19, dto);

		// Then
		assertEquals(new DateTime(2012, 01, 03, 0, 0, 0, 0).toDate(), eae.getDateEntretien());
	}

}
