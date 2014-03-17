package nc.noumea.mairie.sirh.eae.service;

import java.util.ArrayList;

import nc.noumea.mairie.sirh.domain.Agent;
import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeCampagne;
import nc.noumea.mairie.sirh.eae.domain.EaeFichePoste;
import nc.noumea.mairie.sirh.eae.dto.AvancementEaeDto;
import nc.noumea.mairie.sirh.eae.dto.CalculEaeInfosDto;
import nc.noumea.mairie.sirh.eae.dto.agent.AutreAdministrationAgentDto;
import nc.noumea.mairie.sirh.eae.dto.agent.DiplomeDto;
import nc.noumea.mairie.sirh.eae.dto.agent.FormationDto;
import nc.noumea.mairie.sirh.eae.dto.poste.FichePosteDto;
import nc.noumea.mairie.sirh.eae.repository.IEaeRepository;
import nc.noumea.mairie.sirh.service.AgentService;
import nc.noumea.mairie.sirh.tools.CalculEaeHelper;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

public class CalculEaeServiceTest {

	@Test
	public void testcreerEaeAffecte() throws Exception {

		// Given
		EaeCampagne campPrec = new EaeCampagne();
		campPrec.setIdCampagneEae(2);
		campPrec.setAnnee(2013);

		EaeCampagne camp = new EaeCampagne();
		camp.setIdCampagneEae(1);
		camp.setAnnee(2014);

		Agent ag = new Agent();
		ag.setIdAgent(9005138);

		AvancementEaeDto av = new AvancementEaeDto();
		av.setEtat("T");

		CalculEaeInfosDto affAgent = new CalculEaeInfosDto();
		affAgent.setListDiplome(new ArrayList<DiplomeDto>());
		affAgent.setListFormation(new ArrayList<FormationDto>());

		AutreAdministrationAgentDto autreAdmin = new AutreAdministrationAgentDto();

		IEaeRepository eR = Mockito.mock(IEaeRepository.class);
		Mockito.when(eR.findEaeCampagne(1)).thenReturn(camp);
		Mockito.when(eR.findEaeCampagneByAnnee(2013)).thenReturn(campPrec);
		Mockito.when(eR.findEaeAgent(9005138, campPrec.getIdCampagneEae())).thenReturn(null);

		ISirhWsConsumer wsMock = Mockito.mock(ISirhWsConsumer.class);
		Mockito.when(wsMock.getAvancement(9005138, 2014, true)).thenReturn(av);
		Mockito.when(wsMock.getDetailAffectationActiveByAgent(9005138, 2013)).thenReturn(affAgent);
		Mockito.when(wsMock.chercherAutreAdministrationAgentAncienne(9005138, true)).thenReturn(autreAdmin);

		AgentService agS = Mockito.mock(AgentService.class);
		Mockito.when(agS.getAgent(9005138)).thenReturn(ag);

		CalculEaeHelper eaeH = Mockito.mock(CalculEaeHelper.class);
		Mockito.when(eaeH.getDateAnterieure(null, null)).thenReturn(null);

		CalculEaeService service = new CalculEaeService();
		ReflectionTestUtils.setField(service, "eaeRepository", eR);
		ReflectionTestUtils.setField(service, "sirhWsConsumer", wsMock);
		ReflectionTestUtils.setField(service, "agentService", agS);
		ReflectionTestUtils.setField(service, "calculEaeHelper", eaeH);

		// When
		service.creerEaeAffecte(1, 9005138, 2014);

		// Then
		Mockito.verify(eR, Mockito.times(1)).persistEntity(Mockito.any(Eae.class));
		Mockito.verify(eR, Mockito.times(1)).persistEntity(Mockito.any(EaeFichePoste.class));
	}

	@Test
	public void testcreerEaeSAnsAffecte() throws Exception {

		// Given
		EaeCampagne campPrec = new EaeCampagne();
		campPrec.setIdCampagneEae(2);
		campPrec.setAnnee(2013);

		EaeCampagne camp = new EaeCampagne();
		camp.setIdCampagneEae(1);
		camp.setAnnee(2014);

		Agent ag = new Agent();
		ag.setIdAgent(9005138);

		AvancementEaeDto av = new AvancementEaeDto();
		av.setEtat("T");
		
		FichePosteDto fichePostePrincipale = new FichePosteDto();

		CalculEaeInfosDto affAgent = new CalculEaeInfosDto();
		affAgent.setListDiplome(new ArrayList<DiplomeDto>());
		affAgent.setListFormation(new ArrayList<FormationDto>());
		affAgent.setFichePostePrincipale(fichePostePrincipale);

		AutreAdministrationAgentDto autreAdmin = new AutreAdministrationAgentDto();

		IEaeRepository eR = Mockito.mock(IEaeRepository.class);
		Mockito.when(eR.findEaeCampagne(1)).thenReturn(camp);
		Mockito.when(eR.findEaeCampagneByAnnee(2013)).thenReturn(campPrec);
		Mockito.when(eR.findEaeAgent(9005138, campPrec.getIdCampagneEae())).thenReturn(null);

		ISirhWsConsumer wsMock = Mockito.mock(ISirhWsConsumer.class);
		Mockito.when(wsMock.getAvancement(9005138, 2014, true)).thenReturn(av);
		Mockito.when(wsMock.getDetailAffectationActiveByAgent(9005138, 2013)).thenReturn(affAgent);
		Mockito.when(wsMock.chercherAutreAdministrationAgentAncienne(9005138, true)).thenReturn(autreAdmin);

		AgentService agS = Mockito.mock(AgentService.class);
		Mockito.when(agS.getAgent(9005138)).thenReturn(ag);

		CalculEaeHelper eaeH = Mockito.mock(CalculEaeHelper.class);
		Mockito.when(eaeH.getDateAnterieure(null, null)).thenReturn(null);

		CalculEaeService service = new CalculEaeService();
		ReflectionTestUtils.setField(service, "eaeRepository", eR);
		ReflectionTestUtils.setField(service, "sirhWsConsumer", wsMock);
		ReflectionTestUtils.setField(service, "agentService", agS);
		ReflectionTestUtils.setField(service, "calculEaeHelper", eaeH);

		// When
		service.creerEaeSansAffecte(1, 9005138);

		// Then
		//Mockito.verify(eR, Mockito.times(1)).persistEntity(Mockito.any(Eae.class));
		//Mockito.verify(eR, Mockito.times(1)).persistEntity(Mockito.any(EaeFichePoste.class));
	}
}
