package nc.noumea.mairie.sirh.eae.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
import org.springframework.test.util.ReflectionTestUtils;

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
		t1.setLibelleTypeObjectif("PROFESSIONNEL");
		
		EaeTypeObjectif t2 = new EaeTypeObjectif();
		t2.setLibelleTypeObjectif("INDIVIDUEL");
		
		EaeTypeObjectif t3 = new EaeTypeObjectif();
		t3.setLibelleTypeObjectif("AUTRE");
		
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
}
