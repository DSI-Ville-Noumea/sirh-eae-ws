package nc.noumea.mairie.sirh.eae.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Iterator;
import java.util.List;

import nc.noumea.mairie.sirh.domain.Agent;
import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeCommentaire;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluateur;
import nc.noumea.mairie.sirh.eae.domain.EaeEvalue;
import nc.noumea.mairie.sirh.eae.domain.EaeFichePoste;
import nc.noumea.mairie.sirh.eae.domain.EaeResultat;
import nc.noumea.mairie.sirh.eae.domain.EaeTypeObjectif;
import nc.noumea.mairie.sirh.eae.dto.EaeFichePosteDto;
import nc.noumea.mairie.sirh.eae.dto.EaeResultatsDto;
import nc.noumea.mairie.sirh.eae.dto.identification.EaeIdentificationDto;
import nc.noumea.mairie.sirh.service.IAgentService;

import org.junit.Test;
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
}
