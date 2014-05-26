package nc.noumea.mairie.sirh.eae.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nc.noumea.mairie.sirh.domain.Agent;
import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeCampagne;
import nc.noumea.mairie.sirh.eae.domain.EaeDiplome;
import nc.noumea.mairie.sirh.eae.domain.EaeEvalue;
import nc.noumea.mairie.sirh.eae.domain.EaeFdpActivite;
import nc.noumea.mairie.sirh.eae.domain.EaeFdpCompetence;
import nc.noumea.mairie.sirh.eae.domain.EaeFichePoste;
import nc.noumea.mairie.sirh.eae.domain.EaeFormation;
import nc.noumea.mairie.sirh.eae.domain.EaeParcoursPro;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeAgentPositionAdministrativeEnum;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeTypeCompetenceEnum;
import nc.noumea.mairie.sirh.eae.domain.enums.EtatAvancementEnum;
import nc.noumea.mairie.sirh.eae.dto.AvancementEaeDto;
import nc.noumea.mairie.sirh.eae.dto.CalculEaeInfosDto;
import nc.noumea.mairie.sirh.eae.dto.agent.AutreAdministrationAgentDto;
import nc.noumea.mairie.sirh.eae.dto.agent.CarriereDto;
import nc.noumea.mairie.sirh.eae.dto.agent.DiplomeDto;
import nc.noumea.mairie.sirh.eae.dto.agent.FormationDto;
import nc.noumea.mairie.sirh.eae.dto.agent.GradeDto;
import nc.noumea.mairie.sirh.eae.dto.agent.ParcoursProDto;
import nc.noumea.mairie.sirh.eae.dto.agent.PositionAdmAgentDto;
import nc.noumea.mairie.sirh.eae.dto.poste.FichePosteDto;
import nc.noumea.mairie.sirh.eae.dto.poste.TitrePosteDto;
import nc.noumea.mairie.sirh.eae.repository.IEaeRepository;
import nc.noumea.mairie.sirh.service.AgentService;
import nc.noumea.mairie.sirh.service.IAgentService;
import nc.noumea.mairie.sirh.ws.ISirhWsConsumer;
import nc.noumea.mairie.sirh.ws.SirhWSConsumerException;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

public class CalculEaeServiceTest {

	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	@Test
	public void testcreerEaeAffecte() throws Exception {

		// Given
		Date dateDerniereEmbauche = new Date();

		Agent agent = new Agent();
		agent.setIdAgent(9005138);
		agent.setDateDerniereEmbauche(dateDerniereEmbauche);

		Date dateDebut = new Date();
		CarriereDto carriereFonctionnaireAncienne = new CarriereDto();
		carriereFonctionnaireAncienne.setDateDebut(dateDebut);

		PositionAdmAgentDto positionAdmAgentAncienne = new PositionAdmAgentDto();
		positionAdmAgentAncienne.setDatdeb(20101213);

		PositionAdmAgentDto positionAdmAgentEnCours = new PositionAdmAgentDto();
		positionAdmAgentEnCours.setCdpadm("54");

		CalculEaeInfosDto eaeInfosDto = new CalculEaeInfosDto();
		eaeInfosDto.setFichePostePrincipale(null);
		eaeInfosDto.setCarriereFonctionnaireAncienne(carriereFonctionnaireAncienne);
		eaeInfosDto.setPositionAdmAgentAncienne(positionAdmAgentAncienne);
		eaeInfosDto.setPositionAdmAgentEnCours(positionAdmAgentEnCours);

		Date dateEntreeAAA = new Date();
		AutreAdministrationAgentDto autreAdminAncienne = new AutreAdministrationAgentDto();
		autreAdminAncienne.setDateEntree(dateEntreeAAA);

		Date dateEntree = new Date();
		AutreAdministrationAgentDto autreAdmin = new AutreAdministrationAgentDto();
		autreAdmin.setDateEntree(dateEntree);

		AvancementEaeDto avancement = new AvancementEaeDto();
		avancement.setEtat(AvancementEaeDto.SGC);

		ISirhWsConsumer sirhWsConsumer = Mockito.mock(ISirhWsConsumer.class);
		Mockito.when(sirhWsConsumer.getAvancement(9005138, 2014, true)).thenReturn(avancement);
		Mockito.when(sirhWsConsumer.chercherAutreAdministrationAgentAncienne(9005138, true)).thenReturn(autreAdmin);
		Mockito.when(sirhWsConsumer.chercherAutreAdministrationAgentAncienne(9005138, false)).thenReturn(
				autreAdminAncienne);
		Mockito.when(sirhWsConsumer.getAvancementDetache(Mockito.anyInt(), Mockito.anyInt())).thenReturn(null);
		Mockito.when(sirhWsConsumer.getDetailAffectationActiveByAgent(9005138, 2013)).thenReturn(eaeInfosDto);

		EaeCampagne eaeCampagne = new EaeCampagne();
		eaeCampagne.setIdCampagneEae(1);
		eaeCampagne.setAnnee(2014);

		EaeCampagne campagnePrec = new EaeCampagne();
		campagnePrec.setIdCampagneEae(1);

		Date dateEntreeFonctionnaire = new Date();
		Date dateEntreeAdministration = new Date();
		EaeEvalue eaeEvalue = new EaeEvalue();
		eaeEvalue.setDateEntreeFonctionnaire(dateEntreeFonctionnaire);
		eaeEvalue.setDateEntreeAdministration(dateEntreeAdministration);

		IEaeRepository eaeRepository = Mockito.mock(IEaeRepository.class);
		Mockito.when(eaeRepository.findEaeCampagne(Mockito.anyInt())).thenReturn(eaeCampagne);
		Mockito.when(eaeRepository.findEaeCampagneByAnnee(Mockito.anyInt())).thenReturn(campagnePrec);
		Mockito.when(eaeRepository.findEaeAgent(Mockito.anyInt(), Mockito.anyInt())).thenReturn(null);

		Agent ag = new Agent();
		ag.setIdAgent(9005138);

		AgentService agentService = Mockito.mock(AgentService.class);
		Mockito.when(agentService.getAgent(9005138)).thenReturn(ag);

		CalculEaeService service = new CalculEaeService();
		ReflectionTestUtils.setField(service, "eaeRepository", eaeRepository);
		ReflectionTestUtils.setField(service, "sirhWsConsumer", sirhWsConsumer);
		ReflectionTestUtils.setField(service, "agentService", agentService);

		// When
		service.creerEaeAffecte(1, 9005138);

		// Then
		Mockito.verify(eaeRepository, Mockito.times(1)).persistEntity(Mockito.isA(Eae.class));
	}

	@Test
	public void testCreerEaeSansAffecte() throws Exception {

		// Given
		Date dateDerniereEmbauche = new Date();

		Agent agent = new Agent();
		agent.setIdAgent(9005138);
		agent.setDateDerniereEmbauche(dateDerniereEmbauche);

		Date dateDebut = new Date();
		CarriereDto carriereFonctionnaireAncienne = new CarriereDto();
		carriereFonctionnaireAncienne.setDateDebut(dateDebut);

		PositionAdmAgentDto positionAdmAgentAncienne = new PositionAdmAgentDto();
		positionAdmAgentAncienne.setDatdeb(20101213);

		PositionAdmAgentDto positionAdmAgentEnCours = new PositionAdmAgentDto();
		positionAdmAgentEnCours.setCdpadm("54");

		CalculEaeInfosDto eaeInfosDto = new CalculEaeInfosDto();
		eaeInfosDto.setFichePostePrincipale(null);
		eaeInfosDto.setCarriereFonctionnaireAncienne(carriereFonctionnaireAncienne);
		eaeInfosDto.setPositionAdmAgentAncienne(positionAdmAgentAncienne);
		eaeInfosDto.setPositionAdmAgentEnCours(positionAdmAgentEnCours);

		Date dateEntreeAAA = new Date();
		AutreAdministrationAgentDto autreAdminAncienne = new AutreAdministrationAgentDto();
		autreAdminAncienne.setDateEntree(dateEntreeAAA);

		Date dateEntree = new Date();
		AutreAdministrationAgentDto autreAdmin = new AutreAdministrationAgentDto();
		autreAdmin.setDateEntree(dateEntree);

		AvancementEaeDto avancement = new AvancementEaeDto();
		avancement.setEtat(AvancementEaeDto.SGC);

		ISirhWsConsumer sirhWsConsumer = Mockito.mock(ISirhWsConsumer.class);
		Mockito.when(sirhWsConsumer.getAvancement(9005138, 2014, true)).thenReturn(avancement);
		Mockito.when(sirhWsConsumer.chercherAutreAdministrationAgentAncienne(9005138, true)).thenReturn(autreAdmin);
		Mockito.when(sirhWsConsumer.chercherAutreAdministrationAgentAncienne(9005138, false)).thenReturn(
				autreAdminAncienne);
		Mockito.when(sirhWsConsumer.getAvancementDetache(Mockito.anyInt(), Mockito.anyInt())).thenReturn(null);
		Mockito.when(sirhWsConsumer.getDetailAffectationActiveByAgent(9005138, 2013)).thenReturn(eaeInfosDto);

		EaeCampagne eaeCampagne = new EaeCampagne();
		eaeCampagne.setIdCampagneEae(1);
		eaeCampagne.setAnnee(2014);

		EaeCampagne campagnePrec = new EaeCampagne();
		campagnePrec.setIdCampagneEae(1);

		Date dateEntreeFonctionnaire = new Date();
		Date dateEntreeAdministration = new Date();
		EaeEvalue eaeEvalue = new EaeEvalue();
		eaeEvalue.setDateEntreeFonctionnaire(dateEntreeFonctionnaire);
		eaeEvalue.setDateEntreeAdministration(dateEntreeAdministration);

		IEaeRepository eaeRepository = Mockito.mock(IEaeRepository.class);
		Mockito.when(eaeRepository.findEaeCampagne(Mockito.anyInt())).thenReturn(eaeCampagne);
		Mockito.when(eaeRepository.findEaeCampagneByAnnee(Mockito.anyInt())).thenReturn(campagnePrec);
		Mockito.when(eaeRepository.findEaeAgent(Mockito.anyInt(), Mockito.anyInt())).thenReturn(null);

		Agent ag = new Agent();
		ag.setIdAgent(9005138);

		AgentService agentService = Mockito.mock(AgentService.class);
		Mockito.when(agentService.getAgent(9005138)).thenReturn(ag);

		CalculEaeService service = new CalculEaeService();
		ReflectionTestUtils.setField(service, "eaeRepository", eaeRepository);
		ReflectionTestUtils.setField(service, "sirhWsConsumer", sirhWsConsumer);
		ReflectionTestUtils.setField(service, "agentService", agentService);

		// When
		service.creerEaeSansAffecte(1, 9005138);

		// Then
		Mockito.verify(eaeRepository, Mockito.times(1)).persistEntity(Mockito.isA(Eae.class));
	}

	@Test
	public void creerEvalue() throws SirhWSConsumerException, ParseException {

		Date dateDerniereEmbauche = new Date();

		Agent agent = new Agent();
		agent.setIdAgent(9005138);
		agent.setDateDerniereEmbauche(dateDerniereEmbauche);

		Date dateDebut = new Date();
		CarriereDto carriereFonctionnaireAncienne = new CarriereDto();
		carriereFonctionnaireAncienne.setDateDebut(dateDebut);

		PositionAdmAgentDto positionAdmAgentAncienne = new PositionAdmAgentDto();
		positionAdmAgentAncienne.setDatdeb(20101213);

		PositionAdmAgentDto positionAdmAgentEnCours = new PositionAdmAgentDto();
		positionAdmAgentEnCours.setCdpadm("54");

		CalculEaeInfosDto eaeInfosDto = new CalculEaeInfosDto();
		eaeInfosDto.setFichePostePrincipale(null);
		eaeInfosDto.setCarriereFonctionnaireAncienne(carriereFonctionnaireAncienne);
		eaeInfosDto.setPositionAdmAgentAncienne(positionAdmAgentAncienne);
		eaeInfosDto.setPositionAdmAgentEnCours(positionAdmAgentEnCours);

		Date dateEntreeAAA = new Date();
		AutreAdministrationAgentDto autreAdminAncienne = new AutreAdministrationAgentDto();
		autreAdminAncienne.setDateEntree(dateEntreeAAA);

		Date dateEntree = new Date();
		AutreAdministrationAgentDto autreAdmin = new AutreAdministrationAgentDto();
		autreAdmin.setDateEntree(dateEntree);

		ISirhWsConsumer sirhWsConsumer = Mockito.mock(ISirhWsConsumer.class);
		Mockito.when(sirhWsConsumer.chercherAutreAdministrationAgentAncienne(9005138, true)).thenReturn(autreAdmin);
		Mockito.when(sirhWsConsumer.chercherAutreAdministrationAgentAncienne(9005138, false)).thenReturn(
				autreAdminAncienne);
		Mockito.when(sirhWsConsumer.getAvancement(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyBoolean()))
				.thenReturn(null);
		Mockito.when(sirhWsConsumer.getAvancementDetache(Mockito.anyInt(), Mockito.anyInt())).thenReturn(null);

		EaeCampagne campagnePrec = new EaeCampagne();
		campagnePrec.setIdCampagneEae(1);

		Date dateEntreeFonctionnaire = new Date();
		Date dateEntreeAdministration = new Date();
		EaeEvalue eaeEvalue = new EaeEvalue();
		eaeEvalue.setDateEntreeFonctionnaire(dateEntreeFonctionnaire);
		eaeEvalue.setDateEntreeAdministration(dateEntreeAdministration);
		Eae eaeAnneePrec = new Eae();
		eaeAnneePrec.setEaeEvalue(eaeEvalue);

		IEaeRepository eaeRepository = Mockito.mock(IEaeRepository.class);
		Mockito.when(eaeRepository.findEaeCampagneByAnnee(Mockito.anyInt())).thenReturn(campagnePrec);
		Mockito.when(eaeRepository.findEaeAgent(Mockito.anyInt(), Mockito.anyInt())).thenReturn(eaeAnneePrec);

		CalculEaeService service = new CalculEaeService();
		ReflectionTestUtils.setField(service, "sirhWsConsumer", sirhWsConsumer);
		ReflectionTestUtils.setField(service, "eaeRepository", eaeRepository);

		Eae eae = new Eae();
		eae.setEaeCampagne(new EaeCampagne());

		service.creerEvalue(agent, eae, eaeInfosDto, true, true);

		assertEquals(eae.getEaeEvalue().getIdAgent(), agent.getIdAgent().intValue());
		assertNull(eae.getEaeEvalue().getDateEntreeService());
		assertEquals(eae.getEaeEvalue().getDateEntreeCollectivite(), agent.getDateDerniereEmbauche());
		assertEquals(eae.getEaeEvalue().getDateEntreeAdministration(), dateEntreeAdministration);
		assertEquals(eae.getEaeEvalue().getDateEntreeFonctionnaire(), dateEntreeFonctionnaire);
		assertEquals(eae.getEaeEvalue().getPosition(), EaeAgentPositionAdministrativeEnum.D);
		assertTrue(eae.getEaeEvalue().isEstDetache());
	}

	@Test
	public void setCarriereActive_AgentStatut_A() throws ParseException {

		GradeDto grade = new GradeDto();

		CarriereDto carriereActive = new CarriereDto();
		carriereActive.setCodeCategorie(5);
		carriereActive.setLibelleCategorie("libelleCategorie");
		carriereActive.setGrade(grade);
		carriereActive.setDateDebut(sdf.parse("01/04/2011"));

		CalculEaeInfosDto eaeInfosDto = new CalculEaeInfosDto();
		eaeInfosDto.setCarriereActive(carriereActive);

		EaeEvalue evalAModif = new EaeEvalue();

		EaeCampagne eaeCampagne = new EaeCampagne();
		eaeCampagne.setAnnee(2012);

		Eae eae = new Eae();
		eae.setEaeCampagne(eaeCampagne);

		CalculEaeService service = new CalculEaeService();

		service.setCarriereActive(eaeInfosDto, evalAModif, eae);

		assertEquals(evalAModif.getStatutPrecision(), carriereActive.getLibelleCategorie());
		assertNull(evalAModif.getCadre());
		assertNull(evalAModif.getAvctDureeMax());
		assertNull(evalAModif.getAvctDureeMoy());
		assertNull(evalAModif.getAvctDureeMin());
		assertNull(evalAModif.getGrade());
		assertNull(evalAModif.getCategorie());
		assertNull(evalAModif.getEchelon());
		assertNull(evalAModif.getClassification());
		assertEquals(evalAModif.getAncienneteEchelonJours().intValue(), 273);
	}

	@Test
	public void setCarriereActive_AgentStatut_F() throws ParseException {

		GradeDto grade = new GradeDto();
		grade.setDureeMaximum(24);
		grade.setDureeMoyenne(18);
		grade.setDureeMinimum(12);
		grade.setLibelleClasse("libelleClasse");
		grade.setGradeInitial("gradeInitial");
		grade.setCodeGradeGenerique("codeGradeGenerique");
		grade.setLibelleEchelon("libelleEchelon");

		CarriereDto carriereActive = new CarriereDto();
		carriereActive.setCodeCategorie(1);
		carriereActive.setLibelleCategorie("libelleCategorie");
		carriereActive.setGrade(grade);
		carriereActive.setDateDebut(sdf.parse("01/04/2011"));

		CalculEaeInfosDto eaeInfosDto = new CalculEaeInfosDto();
		eaeInfosDto.setCarriereActive(carriereActive);

		EaeEvalue evalAModif = new EaeEvalue();

		EaeCampagne eaeCampagne = new EaeCampagne();
		eaeCampagne.setAnnee(2012);

		Eae eae = new Eae();
		eae.setEaeCampagne(eaeCampagne);

		CalculEaeService service = new CalculEaeService();

		service.setCarriereActive(eaeInfosDto, evalAModif, eae);

		assertNull(evalAModif.getStatutPrecision());
		assertEquals(evalAModif.getCadre(), "libelleCategorie");
		assertEquals(evalAModif.getAvctDureeMax().intValue(), 24);
		assertEquals(evalAModif.getAvctDureeMoy().intValue(), 18);
		assertEquals(evalAModif.getAvctDureeMin().intValue(), 12);
		assertEquals(evalAModif.getGrade(), grade.getGradeInitial() + " " + grade.getLibelleClasse());
		assertEquals(evalAModif.getCategorie(), grade.getCodeGradeGenerique());
		assertEquals(evalAModif.getEchelon(), grade.getLibelleEchelon());
		assertNull(evalAModif.getClassification());
		assertEquals(evalAModif.getAncienneteEchelonJours().intValue(), 273);
	}

	@Test
	public void setCarriereActive_AgentStatut_CC() throws ParseException {

		GradeDto grade = new GradeDto();
		grade.setLibelleGrade("libelleGrade");

		CarriereDto carriereActive = new CarriereDto();
		carriereActive.setCodeCategorie(7);
		carriereActive.setLibelleCategorie("libelleCategorie");
		carriereActive.setGrade(grade);
		carriereActive.setDateDebut(sdf.parse("01/04/2011"));

		CalculEaeInfosDto eaeInfosDto = new CalculEaeInfosDto();
		eaeInfosDto.setCarriereActive(carriereActive);

		EaeEvalue evalAModif = new EaeEvalue();

		EaeCampagne eaeCampagne = new EaeCampagne();
		eaeCampagne.setAnnee(2012);

		Eae eae = new Eae();
		eae.setEaeCampagne(eaeCampagne);

		CalculEaeService service = new CalculEaeService();

		service.setCarriereActive(eaeInfosDto, evalAModif, eae);

		assertNull(evalAModif.getStatutPrecision());
		assertNull(evalAModif.getCadre());
		assertNull(evalAModif.getAvctDureeMax());
		assertNull(evalAModif.getAvctDureeMoy());
		assertNull(evalAModif.getAvctDureeMin());
		assertNull(evalAModif.getGrade());
		assertNull(evalAModif.getCategorie());
		assertNull(evalAModif.getEchelon());
		assertEquals(evalAModif.getClassification(), grade.getLibelleGrade());
		assertEquals(evalAModif.getAncienneteEchelonJours().intValue(), 273);
	}

	@Test
	public void setAvancementEvalue_EtatAvctTravail() {

		EaeEvalue evalAModif = new EaeEvalue();

		GradeDto gradeAvct = new GradeDto();
		gradeAvct.setLibelleClasse("libelleClasse");
		gradeAvct.setGradeInitial("gradeInitial");
		gradeAvct.setCodeMotifAvancement("REVA");
		gradeAvct.setLibelleEchelon("libelleEchelon");

		Date dateAvctMoy = new Date();
		AvancementEaeDto avct = new AvancementEaeDto();
		avct.setDateAvctMoy(dateAvctMoy);
		avct.setEtat(EtatAvancementEnum.TRAVAIL.getValue());
		avct.setGrade(gradeAvct);

		CalculEaeService service = new CalculEaeService();

		service.setAvancementEvalue(evalAModif, avct);

		assertNull(evalAModif.getDateEffetAvancement());
		assertEquals(evalAModif.getTypeAvancement().getTypeAvctCode(), "REVA");
		assertEquals(evalAModif.getNouvGrade(), "gradeInitial libelleClasse");
		assertEquals(evalAModif.getNouvEchelon(), "libelleEchelon");
	}

	@Test
	public void setAvancementEvalue_EtatAvctAutre() {

		EaeEvalue evalAModif = new EaeEvalue();

		GradeDto gradeAvct = new GradeDto();
		gradeAvct.setLibelleClasse("libelleClasse");
		gradeAvct.setGradeInitial("gradeInitial");
		gradeAvct.setCodeMotifAvancement("REVA");
		gradeAvct.setLibelleEchelon("libelleEchelon");

		Date dateAvctMoy = new Date();
		AvancementEaeDto avct = new AvancementEaeDto();
		avct.setDateAvctMoy(dateAvctMoy);
		avct.setEtat(EtatAvancementEnum.SEF.getValue());
		avct.setGrade(gradeAvct);

		CalculEaeService service = new CalculEaeService();

		service.setAvancementEvalue(evalAModif, avct);

		assertEquals(evalAModif.getDateEffetAvancement(), dateAvctMoy);
		assertEquals(evalAModif.getTypeAvancement().getTypeAvctCode(), "REVA");
		assertEquals(evalAModif.getNouvGrade(), "gradeInitial libelleClasse");
		assertEquals(evalAModif.getNouvEchelon(), "libelleEchelon");
	}

	@Test
	public void getDateEntreeService_returnDateServiceAnterieur_MultipleAffectation() throws ParseException,
			SirhWSConsumerException {

		CalculEaeInfosDto dto = new CalculEaeInfosDto();
		dto.setDateDebut(sdf.parse("01/01/2010"));
		dto.setDateFin(sdf.parse("01/02/2010"));

		CalculEaeInfosDto dto2 = new CalculEaeInfosDto();
		dto2.setDateDebut(sdf.parse("02/02/2010"));
		dto2.setDateFin(sdf.parse("01/03/2010"));

		CalculEaeInfosDto dto3 = new CalculEaeInfosDto();
		dto3.setDateDebut(sdf.parse("02/03/2010"));
		dto3.setDateFin(sdf.parse("01/04/2010"));

		// le WS de SIRH-WS retourne les affectations du + recent au + ancien
		List<CalculEaeInfosDto> listeAffectationService = new ArrayList<CalculEaeInfosDto>();
		listeAffectationService.add(dto3);
		listeAffectationService.add(dto2);
		listeAffectationService.add(dto);

		ISirhWsConsumer sirhWsConsumer = Mockito.mock(ISirhWsConsumer.class);
		Mockito.when(sirhWsConsumer.getListeAffectationsAgentAvecService(Mockito.anyInt(), Mockito.anyString()))
				.thenReturn(listeAffectationService);

		CalculEaeService service = new CalculEaeService();
		ReflectionTestUtils.setField(service, "sirhWsConsumer", sirhWsConsumer);

		Date result = service.getDateEntreeService(9005138, "codeService");

		assertEquals(result, sdf.parse("01/01/2010"));
	}

	@Test
	public void getDateEntreeService_returnDateServiceAnterieur() throws ParseException, SirhWSConsumerException {

		CalculEaeInfosDto dto = new CalculEaeInfosDto();
		dto.setDateDebut(sdf.parse("01/01/2010"));
		dto.setDateFin(sdf.parse("01/02/2010"));

		CalculEaeInfosDto dto2 = new CalculEaeInfosDto();
		dto2.setDateDebut(sdf.parse("02/02/2010"));
		dto2.setDateFin(sdf.parse("01/03/2010"));
		// le WS de SIRH-WS retourne les affectations du + recent au + ancien
		List<CalculEaeInfosDto> listeAffectationService = new ArrayList<CalculEaeInfosDto>();
		listeAffectationService.add(dto2);
		listeAffectationService.add(dto);

		ISirhWsConsumer sirhWsConsumer = Mockito.mock(ISirhWsConsumer.class);
		Mockito.when(sirhWsConsumer.getListeAffectationsAgentAvecService(Mockito.anyInt(), Mockito.anyString()))
				.thenReturn(listeAffectationService);

		CalculEaeService service = new CalculEaeService();
		ReflectionTestUtils.setField(service, "sirhWsConsumer", sirhWsConsumer);

		Date result = service.getDateEntreeService(9005138, "codeService");

		assertEquals(result, sdf.parse("01/01/2010"));
	}

	@Test
	public void getDateEntreeService_returnDateServiceActuel_ecartEntreService() throws ParseException,
			SirhWSConsumerException {

		CalculEaeInfosDto dto = new CalculEaeInfosDto();
		dto.setDateDebut(sdf.parse("01/01/2010"));
		dto.setDateFin(sdf.parse("01/02/2010"));

		CalculEaeInfosDto dto2 = new CalculEaeInfosDto();
		dto2.setDateDebut(sdf.parse("03/02/2010"));
		dto2.setDateFin(sdf.parse("01/03/2010"));
		// le WS de SIRH-WS retourne les affectations du + recent au + ancien
		List<CalculEaeInfosDto> listeAffectationService = new ArrayList<CalculEaeInfosDto>();
		listeAffectationService.add(dto2);
		listeAffectationService.add(dto);

		ISirhWsConsumer sirhWsConsumer = Mockito.mock(ISirhWsConsumer.class);
		Mockito.when(sirhWsConsumer.getListeAffectationsAgentAvecService(Mockito.anyInt(), Mockito.anyString()))
				.thenReturn(listeAffectationService);

		CalculEaeService service = new CalculEaeService();
		ReflectionTestUtils.setField(service, "sirhWsConsumer", sirhWsConsumer);

		Date result = service.getDateEntreeService(9005138, "codeService");

		assertEquals(result, sdf.parse("03/02/2010"));
	}

	@Test
	public void getDateEntreeService_returnDateServiceActuel() throws ParseException, SirhWSConsumerException {

		CalculEaeInfosDto dto = new CalculEaeInfosDto();
		dto.setDateDebut(sdf.parse("01/01/2010"));
		dto.setDateFin(sdf.parse("01/02/2010"));

		CalculEaeInfosDto dto2 = new CalculEaeInfosDto();
		dto2.setDateDebut(sdf.parse("02/02/2010"));
		dto2.setDateFin(sdf.parse("01/03/2010"));
		// le WS de SIRH-WS retourne les affectations du + recent au + ancien
		List<CalculEaeInfosDto> listeAffectationService = new ArrayList<CalculEaeInfosDto>();
		listeAffectationService.add(dto);
		listeAffectationService.add(dto2);

		ISirhWsConsumer sirhWsConsumer = Mockito.mock(ISirhWsConsumer.class);
		Mockito.when(sirhWsConsumer.getListeAffectationsAgentAvecService(Mockito.anyInt(), Mockito.anyString()))
				.thenReturn(listeAffectationService);

		CalculEaeService service = new CalculEaeService();
		ReflectionTestUtils.setField(service, "sirhWsConsumer", sirhWsConsumer);

		Date result = service.getDateEntreeService(9005138, "codeService");

		assertEquals(result, sdf.parse("01/01/2010"));
	}

	@Test
	public void getDateEntreeService_returnNull() throws SirhWSConsumerException {

		ISirhWsConsumer sirhWsConsumer = Mockito.mock(ISirhWsConsumer.class);
		Mockito.when(sirhWsConsumer.getListeAffectationsAgentAvecService(Mockito.anyInt(), Mockito.anyString()))
				.thenReturn(null);

		CalculEaeService service = new CalculEaeService();
		ReflectionTestUtils.setField(service, "sirhWsConsumer", sirhWsConsumer);

		Date result = service.getDateEntreeService(9005138, "codeService");

		assertNull(result);
	}

	@Test
	public void creerEvaluateur_1persist() throws SirhWSConsumerException, ParseException {

		EaeFichePoste eaeFDP = new EaeFichePoste();
		eaeFDP.setIdAgentShd(9005138);

		TitrePosteDto tpResp = new TitrePosteDto();
		tpResp.setLibTitrePoste("libTitrePoste");

		Agent agentResp = new Agent();
		agentResp.setIdAgent(9005138);
		agentResp.setDateDerniereEmbauche(sdf.parse("01/01/2010"));

		IEaeRepository eaeRepository = Mockito.mock(IEaeRepository.class);

		IAgentService agentService = Mockito.mock(IAgentService.class);
		Mockito.when(agentService.getAgent(9005138)).thenReturn(agentResp);

		CalculEaeService service = new CalculEaeService();
		ReflectionTestUtils.setField(service, "eaeRepository", eaeRepository);
		ReflectionTestUtils.setField(service, "agentService", agentService);

		Eae eae = new Eae();

		service.creerEvaluateur(eaeFDP, eae, tpResp, null);

		assertEquals(1, eae.getEaeEvaluateurs().size());
	}

	@Test
	public void creerEvaluateur_noPersist() throws SirhWSConsumerException, ParseException {

		EaeFichePoste eaeFDP = new EaeFichePoste();
		eaeFDP.setIdAgentShd(null);

		TitrePosteDto tpResp = new TitrePosteDto();
		tpResp.setLibTitrePoste("libTitrePoste");

		Agent agentResp = new Agent();
		agentResp.setIdAgent(9005138);
		agentResp.setDateDerniereEmbauche(sdf.parse("01/01/2010"));

		IEaeRepository eaeRepository = Mockito.mock(IEaeRepository.class);

		CalculEaeService service = new CalculEaeService();
		ReflectionTestUtils.setField(service, "eaeRepository", eaeRepository);

		Eae eae = new Eae();

		service.creerEvaluateur(eaeFDP, eae, tpResp, null);

		assertEquals(0, eae.getEaeEvaluateurs().size());
	}

	@Test
	public void getDateEntreeAffectation_result() throws SirhWSConsumerException {

		Date dateDebut = new Date();
		CalculEaeInfosDto dto = new CalculEaeInfosDto();
		dto.setDateDebut(dateDebut);

		List<CalculEaeInfosDto> listeAffectationSurMemeFDP = new ArrayList<CalculEaeInfosDto>();
		listeAffectationSurMemeFDP.add(dto);

		ISirhWsConsumer sirhWsConsumer = Mockito.mock(ISirhWsConsumer.class);
		Mockito.when(sirhWsConsumer.getListeAffectationsAgentAvecFP(Mockito.anyInt(), Mockito.anyInt())).thenReturn(
				listeAffectationSurMemeFDP);

		CalculEaeService service = new CalculEaeService();
		ReflectionTestUtils.setField(service, "sirhWsConsumer", sirhWsConsumer);

		Date result = service.getDateEntreeAffectation(1, 9005138);

		assertEquals(result, dateDebut);
	}

	@Test
	public void getDateEntreeAffectation_resultNull() throws SirhWSConsumerException {

		ISirhWsConsumer sirhWsConsumer = Mockito.mock(ISirhWsConsumer.class);
		Mockito.when(sirhWsConsumer.getListeAffectationsAgentAvecFP(Mockito.anyInt(), Mockito.anyInt())).thenReturn(
				null);

		CalculEaeService service = new CalculEaeService();
		ReflectionTestUtils.setField(service, "sirhWsConsumer", sirhWsConsumer);

		Date result = service.getDateEntreeAffectation(1, 9005138);

		assertNull(result);
	}

	@Test
	public void creerFichePoste() throws SirhWSConsumerException, ParseException {

		TitrePosteDto titrePoste = new TitrePosteDto();
		titrePoste.setLibTitrePoste("libTitrePoste");

		FichePosteDto fichePosteDto = new FichePosteDto();
		fichePosteDto.setIdFichePoste(1);
		fichePosteDto.setCodeService("codeService");
		fichePosteDto.setDirection("direction");
		fichePosteDto.setService("service");
		fichePosteDto.setSection("section");
		fichePosteDto.setEmploiPrimaire("emploiPrimaire");
		fichePosteDto.setEmploiSecondaire("emploiSecondaire");
		fichePosteDto.setTitrePoste(titrePoste);
		fichePosteDto.setGradePoste("gradePoste");
		fichePosteDto.setLieu("lieu");
		fichePosteDto.setMissions("missions");

		FichePosteDto fpResp = new FichePosteDto();
		fpResp.setIdAgent(9005138);
		fpResp.setIdFichePoste(1);
		fpResp.setCodeService("codeService");
		TitrePosteDto tpResp = new TitrePosteDto();
		tpResp.setLibTitrePoste("libTitrePosteResponsable");

		ISirhWsConsumer sirhWsConsumer = Mockito.mock(ISirhWsConsumer.class);
		Mockito.when(sirhWsConsumer.getListeAffectationsAgentAvecFP(Mockito.anyInt(), Mockito.anyInt())).thenReturn(
				null);
		Mockito.when(sirhWsConsumer.getListeAffectationsAgentAvecService(Mockito.anyInt(), Mockito.anyString()))
				.thenReturn(null);

		IEaeRepository eaeRepository = Mockito.mock(IEaeRepository.class);

		Agent agentResp = new Agent();
		agentResp.setIdAgent(9005138);
		agentResp.setDateDerniereEmbauche(sdf.parse("01/01/2010"));

		IAgentService agentService = Mockito.mock(IAgentService.class);
		Mockito.when(agentService.getAgent(9005138)).thenReturn(agentResp);

		Eae eae = new Eae();
		CalculEaeService service = new CalculEaeService();
		ReflectionTestUtils.setField(service, "eaeRepository", eaeRepository);
		ReflectionTestUtils.setField(service, "agentService", agentService);
		ReflectionTestUtils.setField(service, "sirhWsConsumer", sirhWsConsumer);

		service.creerFichePoste(fichePosteDto, eae, fpResp, tpResp, true, true);

		assertEquals(1, eae.getEaeFichePostes().size());

		EaeFichePoste fichePoste = null;
		for (EaeFichePoste e : eae.getEaeFichePostes()) {
			fichePoste = e;
			break;
		}

		assertEquals(fichePoste.getIdSirhFichePoste(), fichePosteDto.getIdFichePoste());
		assertEquals(fichePoste.getCodeService(), fichePosteDto.getCodeService());
		assertTrue(fichePoste.isPrimary());
		assertEquals(fichePoste.getDirectionService(), fichePosteDto.getDirection());
		assertEquals(fichePoste.getService(), fichePosteDto.getService());
		assertEquals(fichePoste.getSectionService(), fichePosteDto.getSection());
		assertEquals(fichePoste.getEmploi(), fichePosteDto.getEmploiPrimaire());
		assertEquals(fichePoste.getFonction(), fichePosteDto.getTitrePoste().getLibTitrePoste());
		assertNull(fichePoste.getDateEntreeFonction());
		assertEquals(fichePoste.getGradePoste(), fichePosteDto.getGradePoste());
		assertEquals(fichePoste.getLocalisation(), fichePosteDto.getLieu());
		assertEquals(fichePoste.getMissions(), fichePosteDto.getMissions());
		assertEquals(fichePoste.getAgentShd().getIdAgent().intValue(), 9005138);
		assertEquals(fichePoste.getFonctionResponsable(), tpResp.getLibTitrePoste());
		assertEquals(fichePoste.getDateEntreeCollectiviteResponsable(), agentResp.getDateDerniereEmbauche());
		assertNull(fichePoste.getDateEntreeFonctionResponsable());
		assertNull(fichePoste.getDateEntreeServiceResponsable());
	}

	@Test
	public void creerActivitesFichePoste_persist() {

		List<String> activites = new ArrayList<String>();
		activites.add("activites");

		FichePosteDto fichePoste = new FichePosteDto();
		fichePoste.setActivites(activites);

		IEaeRepository eaeRepository = Mockito.mock(IEaeRepository.class);

		CalculEaeService service = new CalculEaeService();
		ReflectionTestUtils.setField(service, "eaeRepository", eaeRepository);

		EaeFichePoste eaeFichePoste = new EaeFichePoste();
		service.creerActivitesFichePoste(fichePoste, eaeFichePoste);
		;

		assertEquals(1, eaeFichePoste.getEaeFdpActivites().size());

		for (EaeFdpActivite e : eaeFichePoste.getEaeFdpActivites()) {
			assertEquals(e.getLibelle(), "activites");
		}
	}

	@Test
	public void creerActivitesFichePoste_noPersist() {

		FichePosteDto fichePoste = new FichePosteDto();
		fichePoste.setActivites(null);

		IEaeRepository eaeRepository = Mockito.mock(IEaeRepository.class);

		CalculEaeService service = new CalculEaeService();
		ReflectionTestUtils.setField(service, "eaeRepository", eaeRepository);

		EaeFichePoste eaeFichePoste = new EaeFichePoste();
		service.creerActivitesFichePoste(fichePoste, eaeFichePoste);
		;

		assertEquals(0, eaeFichePoste.getEaeFdpActivites().size());
		Mockito.verify(eaeRepository, Mockito.times(0)).persistEntity(Mockito.isA(EaeFdpActivite.class));
	}

	@Test
	public void creerCompetencesFichePoste() {

		List<String> comportementsProfessionnels = new ArrayList<String>();
		comportementsProfessionnels.add("comportementsProfessionnels");
		List<String> savoirs = new ArrayList<String>();
		savoirs.add("savoirs");
		List<String> savoirsFaire = new ArrayList<String>();
		savoirsFaire.add("savoirsFaire");

		FichePosteDto fichePoste = new FichePosteDto();
		fichePoste.setComportementsProfessionnels(comportementsProfessionnels);
		fichePoste.setSavoirs(savoirs);
		fichePoste.setSavoirsFaire(savoirsFaire);

		IEaeRepository eaeRepository = Mockito.mock(IEaeRepository.class);

		CalculEaeService service = new CalculEaeService();
		ReflectionTestUtils.setField(service, "eaeRepository", eaeRepository);

		EaeFichePoste eaeFichePoste = new EaeFichePoste();
		service.creerCompetencesFichePoste(fichePoste, eaeFichePoste);
		;

		assertEquals(3, eaeFichePoste.getEaeFdpCompetences().size());

		for (EaeFdpCompetence e : eaeFichePoste.getEaeFdpCompetences()) {
			if (e.getLibelle().equals("comportementsProfessionnels")) {
				assertEquals(e.getType(), EaeTypeCompetenceEnum.CP);
			}
			if (e.getLibelle().equals("savoirs")) {
				assertEquals(e.getType(), EaeTypeCompetenceEnum.SA);
			}
			if (e.getLibelle().equals("savoirsFaire")) {
				assertEquals(e.getType(), EaeTypeCompetenceEnum.SF);
			}
		}
	}

	@Test
	public void creerCompetencesFichePoste_noPersist() {

		List<String> savoirsFaire = new ArrayList<String>();

		FichePosteDto fichePoste = new FichePosteDto();
		fichePoste.setComportementsProfessionnels(null);
		fichePoste.setSavoirs(null);
		fichePoste.setSavoirsFaire(savoirsFaire);

		IEaeRepository eaeRepository = Mockito.mock(IEaeRepository.class);

		CalculEaeService service = new CalculEaeService();
		ReflectionTestUtils.setField(service, "eaeRepository", eaeRepository);

		EaeFichePoste eaeFichePoste = new EaeFichePoste();
		service.creerCompetencesFichePoste(fichePoste, eaeFichePoste);
		;

		assertEquals(0, eaeFichePoste.getEaeFdpCompetences().size());
		Mockito.verify(eaeRepository, Mockito.times(0)).persistEntity(Mockito.isA(EaeFdpCompetence.class));
	}

	@Test
	public void creerDiplome() throws ParseException {

		DiplomeDto diplomeDto = new DiplomeDto();
		diplomeDto.setDateObtention(sdf.parse("10/10/2014"));
		diplomeDto.setLibSpeDiplome("libSpeDiplome");
		diplomeDto.setLibTitreDiplome("libTitreDiplome");

		List<DiplomeDto> listDiplomesAgent = new ArrayList<DiplomeDto>();
		listDiplomesAgent.add(diplomeDto);

		IEaeRepository eaeRepository = Mockito.mock(IEaeRepository.class);

		CalculEaeService service = new CalculEaeService();
		ReflectionTestUtils.setField(service, "eaeRepository", eaeRepository);

		Eae eae = new Eae();
		service.creerDiplome(eae, listDiplomesAgent);

		assertEquals(1, eae.getEaeDiplomes().size());

		for (EaeDiplome diplome : eae.getEaeDiplomes()) {
			assertEquals("2014 : libTitreDiplome libSpeDiplome", diplome.getLibelleDiplome());
		}
	}

	@Test
	public void creerDiplome_listeVide() {

		List<DiplomeDto> listDiplomesAgent = new ArrayList<DiplomeDto>();

		IEaeRepository eaeRepository = Mockito.mock(IEaeRepository.class);

		CalculEaeService service = new CalculEaeService();
		ReflectionTestUtils.setField(service, "eaeRepository", eaeRepository);

		Eae eae = new Eae();
		service.creerDiplome(eae, listDiplomesAgent);

		assertEquals(0, eae.getEaeDiplomes().size());
		Mockito.verify(eaeRepository, Mockito.times(0)).persistEntity(Mockito.isA(EaeDiplome.class));
	}

	@Test
	public void creerParcoursPro_memeService4foisQuiSuient() throws SirhWSConsumerException, ParseException {

		ParcoursProDto parcoursProDto = new ParcoursProDto();
		parcoursProDto.setDateDebut(sdf.parse("10/10/2010"));
		parcoursProDto.setDirection("direction");
		parcoursProDto.setService("memeService");
		parcoursProDto.setDateFin(sdf.parse("31/12/2010"));

		ParcoursProDto parcoursProDto2 = new ParcoursProDto();
		parcoursProDto2.setDateDebut(sdf.parse("01/01/2011"));
		parcoursProDto2.setDirection("direction");
		parcoursProDto2.setService("memeService");
		parcoursProDto2.setDateFin(sdf.parse("28/02/2011"));

		ParcoursProDto parcoursProDto3 = new ParcoursProDto();
		parcoursProDto3.setDateDebut(sdf.parse("01/03/2011"));
		parcoursProDto3.setDirection("direction");
		parcoursProDto3.setService("memeService");
		parcoursProDto3.setDateFin(sdf.parse("01/10/2011"));

		ParcoursProDto parcoursProDto4 = new ParcoursProDto();
		parcoursProDto4.setDateDebut(sdf.parse("02/10/2011"));
		parcoursProDto4.setDirection("direction");
		parcoursProDto4.setService("memeService");
		parcoursProDto4.setDateFin(null);

		List<ParcoursProDto> listParcoursPro = new ArrayList<ParcoursProDto>();
		listParcoursPro.addAll(Arrays.asList(parcoursProDto, parcoursProDto2, parcoursProDto3, parcoursProDto4));

		IEaeRepository eaeRepository = Mockito.mock(IEaeRepository.class);

		ISirhWsConsumer sirhWsConsumer = Mockito.mock(ISirhWsConsumer.class);
		Mockito.when(sirhWsConsumer.getListeAutreAdministrationAgent(Mockito.anyInt())).thenReturn(null);

		CalculEaeService service = new CalculEaeService();
		ReflectionTestUtils.setField(service, "sirhWsConsumer", sirhWsConsumer);
		ReflectionTestUtils.setField(service, "eaeRepository", eaeRepository);

		Eae eae = new Eae();
		Agent agent = new Agent();
		agent.setIdAgent(9005138);

		service.creerParcoursPro(agent, eae, listParcoursPro);

		assertEquals(1, eae.getEaeParcoursPros().size());

		for (EaeParcoursPro parcoursPro : eae.getEaeParcoursPros()) {
			assertEquals(sdf.parse("10/10/2010"), parcoursPro.getDateDebut());
			assertNull(parcoursPro.getDateFin());
			assertEquals("direction memeService", parcoursPro.getLibelleParcoursPro());
		}
	}

	@Test
	public void creerParcoursPro_2ServicesQuiSuivent() throws SirhWSConsumerException, ParseException {

		ParcoursProDto parcoursProDto = new ParcoursProDto();
		parcoursProDto.setDateDebut(sdf.parse("10/10/2010"));
		parcoursProDto.setDirection("direction");
		parcoursProDto.setService("memeService");
		parcoursProDto.setDateFin(sdf.parse("31/12/2010"));

		ParcoursProDto parcoursProDto2 = new ParcoursProDto();
		parcoursProDto2.setDateDebut(sdf.parse("01/01/2011"));
		parcoursProDto2.setDirection("direction");
		parcoursProDto2.setService("memeService");
		parcoursProDto2.setDateFin(sdf.parse("28/02/2011"));

		ParcoursProDto parcoursProDto3 = new ParcoursProDto();
		parcoursProDto3.setDateDebut(sdf.parse("02/03/2011"));
		parcoursProDto3.setDirection("direction");
		parcoursProDto3.setService("memeService");
		parcoursProDto3.setDateFin(sdf.parse("01/10/2011"));

		ParcoursProDto parcoursProDto4 = new ParcoursProDto();
		parcoursProDto4.setDateDebut(sdf.parse("02/10/2011"));
		parcoursProDto4.setDirection("direction");
		parcoursProDto4.setService("memeService");
		parcoursProDto4.setDateFin(null);

		List<ParcoursProDto> listParcoursPro = new ArrayList<ParcoursProDto>();
		listParcoursPro.addAll(Arrays.asList(parcoursProDto, parcoursProDto2, parcoursProDto3, parcoursProDto4));

		IEaeRepository eaeRepository = Mockito.mock(IEaeRepository.class);

		ISirhWsConsumer sirhWsConsumer = Mockito.mock(ISirhWsConsumer.class);
		Mockito.when(sirhWsConsumer.getListeAutreAdministrationAgent(Mockito.anyInt())).thenReturn(null);

		CalculEaeService service = new CalculEaeService();
		ReflectionTestUtils.setField(service, "sirhWsConsumer", sirhWsConsumer);
		ReflectionTestUtils.setField(service, "eaeRepository", eaeRepository);

		Eae eae = new Eae();
		Agent agent = new Agent();
		agent.setIdAgent(9005138);

		service.creerParcoursPro(agent, eae, listParcoursPro);

		assertEquals(2, eae.getEaeParcoursPros().size());
	}

	@Test
	public void creerParcoursPro_memeServiceSansSeSuivret() throws SirhWSConsumerException, ParseException {

		ParcoursProDto parcoursProDto = new ParcoursProDto();
		parcoursProDto.setDateDebut(sdf.parse("10/10/2010"));
		parcoursProDto.setDirection("direction");
		parcoursProDto.setService("memeService");
		parcoursProDto.setDateFin(sdf.parse("31/12/2010"));

		ParcoursProDto parcoursProDto2 = new ParcoursProDto();
		parcoursProDto2.setDateDebut(sdf.parse("02/01/2011"));
		parcoursProDto2.setDirection("direction");
		parcoursProDto2.setService("memeService");
		parcoursProDto2.setDateFin(sdf.parse("28/02/2011"));

		ParcoursProDto parcoursProDto3 = new ParcoursProDto();
		parcoursProDto3.setDateDebut(sdf.parse("02/03/2011"));
		parcoursProDto3.setDirection("direction");
		parcoursProDto3.setService("memeService");
		parcoursProDto3.setDateFin(sdf.parse("01/10/2011"));

		ParcoursProDto parcoursProDto4 = new ParcoursProDto();
		parcoursProDto4.setDateDebut(sdf.parse("03/10/2011"));
		parcoursProDto4.setDirection("direction");
		parcoursProDto4.setService("memeService");
		parcoursProDto4.setDateFin(null);

		List<ParcoursProDto> listParcoursPro = new ArrayList<ParcoursProDto>();
		listParcoursPro.addAll(Arrays.asList(parcoursProDto, parcoursProDto2, parcoursProDto3, parcoursProDto4));

		IEaeRepository eaeRepository = Mockito.mock(IEaeRepository.class);

		ISirhWsConsumer sirhWsConsumer = Mockito.mock(ISirhWsConsumer.class);
		Mockito.when(sirhWsConsumer.getListeAutreAdministrationAgent(Mockito.anyInt())).thenReturn(null);

		CalculEaeService service = new CalculEaeService();
		ReflectionTestUtils.setField(service, "sirhWsConsumer", sirhWsConsumer);
		ReflectionTestUtils.setField(service, "eaeRepository", eaeRepository);

		Eae eae = new Eae();
		Agent agent = new Agent();
		agent.setIdAgent(9005138);

		service.creerParcoursPro(agent, eae, listParcoursPro);

		assertEquals(4, eae.getEaeParcoursPros().size());
	}

	@Test
	public void creerParcoursPro_4ParcoursQuiSuient_servicesDifferents() throws SirhWSConsumerException, ParseException {

		ParcoursProDto parcoursProDto = new ParcoursProDto();
		parcoursProDto.setDateDebut(sdf.parse("10/10/2010"));
		parcoursProDto.setDirection("direction");
		parcoursProDto.setService("service 1");
		parcoursProDto.setDateFin(sdf.parse("31/12/2010"));

		ParcoursProDto parcoursProDto2 = new ParcoursProDto();
		parcoursProDto2.setDateDebut(sdf.parse("01/01/2011"));
		parcoursProDto2.setDirection("direction");
		parcoursProDto2.setService("service 2");
		parcoursProDto2.setDateFin(sdf.parse("28/02/2011"));

		ParcoursProDto parcoursProDto3 = new ParcoursProDto();
		parcoursProDto3.setDateDebut(sdf.parse("01/03/2011"));
		parcoursProDto3.setDirection("direction");
		parcoursProDto3.setService("service 1");
		parcoursProDto3.setDateFin(sdf.parse("01/10/2011"));

		ParcoursProDto parcoursProDto4 = new ParcoursProDto();
		parcoursProDto4.setDateDebut(sdf.parse("02/10/2011"));
		parcoursProDto4.setDirection("direction");
		parcoursProDto4.setService("service 2");
		parcoursProDto4.setDateFin(null);

		List<ParcoursProDto> listParcoursPro = new ArrayList<ParcoursProDto>();
		listParcoursPro.addAll(Arrays.asList(parcoursProDto, parcoursProDto2, parcoursProDto3, parcoursProDto4));

		IEaeRepository eaeRepository = Mockito.mock(IEaeRepository.class);

		ISirhWsConsumer sirhWsConsumer = Mockito.mock(ISirhWsConsumer.class);
		Mockito.when(sirhWsConsumer.getListeAutreAdministrationAgent(Mockito.anyInt())).thenReturn(null);

		CalculEaeService service = new CalculEaeService();
		ReflectionTestUtils.setField(service, "sirhWsConsumer", sirhWsConsumer);
		ReflectionTestUtils.setField(service, "eaeRepository", eaeRepository);

		Eae eae = new Eae();
		Agent agent = new Agent();
		agent.setIdAgent(9005138);

		service.creerParcoursPro(agent, eae, listParcoursPro);

		assertEquals(4, eae.getEaeParcoursPros().size());
	}

	@Test
	public void creerParcoursProAvecAutreAdministration_memeAdmin4foisQuiSuient() throws SirhWSConsumerException,
			ParseException {

		AutreAdministrationAgentDto aaa1 = new AutreAdministrationAgentDto();
		aaa1.setDateEntree(sdf.parse("10/10/2010"));
		aaa1.setDateSortie(sdf.parse("10/10/2011"));
		aaa1.setLibelleAdministration("libelleAdministration");

		AutreAdministrationAgentDto aaa2 = new AutreAdministrationAgentDto();
		aaa2.setDateEntree(sdf.parse("11/10/2011"));
		aaa2.setDateSortie(sdf.parse("31/12/2012"));
		aaa2.setLibelleAdministration("libelleAdministration");

		AutreAdministrationAgentDto aaa3 = new AutreAdministrationAgentDto();
		aaa3.setDateEntree(sdf.parse("01/01/2013"));
		aaa3.setDateSortie(sdf.parse("28/02/2013"));
		aaa3.setLibelleAdministration("libelleAdministration");

		AutreAdministrationAgentDto aaa4 = new AutreAdministrationAgentDto();
		aaa4.setDateEntree(sdf.parse("01/03/2013"));
		aaa4.setDateSortie(null);
		aaa4.setLibelleAdministration("libelleAdministration");

		List<AutreAdministrationAgentDto> listAutreAdmin = new ArrayList<AutreAdministrationAgentDto>();
		listAutreAdmin.addAll(Arrays.asList(aaa1, aaa2, aaa3, aaa4));

		IEaeRepository eaeRepository = Mockito.mock(IEaeRepository.class);

		ISirhWsConsumer sirhWsConsumer = Mockito.mock(ISirhWsConsumer.class);
		Mockito.when(sirhWsConsumer.getListeAutreAdministrationAgent(Mockito.anyInt())).thenReturn(listAutreAdmin);

		CalculEaeService service = new CalculEaeService();
		ReflectionTestUtils.setField(service, "sirhWsConsumer", sirhWsConsumer);
		ReflectionTestUtils.setField(service, "eaeRepository", eaeRepository);

		Eae eae = new Eae();
		Agent agent = new Agent();
		agent.setIdAgent(9005138);

		service.creerParcoursProAvecAutreAdministration(agent, eae);

		assertEquals(1, eae.getEaeParcoursPros().size());
	}

	@Test
	public void creerParcoursProAvecAutreAdministration_memeAdmin3fois_1adminDifferenteQuiSuit()
			throws SirhWSConsumerException, ParseException {

		AutreAdministrationAgentDto aaa1 = new AutreAdministrationAgentDto();
		aaa1.setDateEntree(sdf.parse("10/10/2010"));
		aaa1.setDateSortie(sdf.parse("10/10/2011"));
		aaa1.setLibelleAdministration("libelleAdministration");

		AutreAdministrationAgentDto aaa2 = new AutreAdministrationAgentDto();
		aaa2.setDateEntree(sdf.parse("11/10/2011"));
		aaa2.setDateSortie(sdf.parse("31/12/2012"));
		aaa2.setLibelleAdministration("libelleAdministration");

		AutreAdministrationAgentDto aaa3 = new AutreAdministrationAgentDto();
		aaa3.setDateEntree(sdf.parse("01/01/2013"));
		aaa3.setDateSortie(sdf.parse("10/10/2013"));
		aaa3.setLibelleAdministration("libelleAdministration");

		AutreAdministrationAgentDto aaa4 = new AutreAdministrationAgentDto();
		aaa4.setDateEntree(sdf.parse("11/10/2013"));
		aaa4.setDateSortie(sdf.parse("10/10/2014"));
		aaa4.setLibelleAdministration("libelleAdministration bis");

		List<AutreAdministrationAgentDto> listAutreAdmin = new ArrayList<AutreAdministrationAgentDto>();
		listAutreAdmin.addAll(Arrays.asList(aaa1, aaa2, aaa3, aaa4));

		IEaeRepository eaeRepository = Mockito.mock(IEaeRepository.class);

		ISirhWsConsumer sirhWsConsumer = Mockito.mock(ISirhWsConsumer.class);
		Mockito.when(sirhWsConsumer.getListeAutreAdministrationAgent(Mockito.anyInt())).thenReturn(listAutreAdmin);

		CalculEaeService service = new CalculEaeService();
		ReflectionTestUtils.setField(service, "sirhWsConsumer", sirhWsConsumer);
		ReflectionTestUtils.setField(service, "eaeRepository", eaeRepository);

		Eae eae = new Eae();
		Agent agent = new Agent();
		agent.setIdAgent(9005138);

		service.creerParcoursProAvecAutreAdministration(agent, eae);

		assertEquals(2, eae.getEaeParcoursPros().size());
	}

	@Test
	public void creerParcoursProAvecAutreAdministration_adminsDifferentes() throws SirhWSConsumerException,
			ParseException {

		AutreAdministrationAgentDto aaa1 = new AutreAdministrationAgentDto();
		aaa1.setDateEntree(sdf.parse("10/10/2010"));
		aaa1.setDateSortie(sdf.parse("10/10/2011"));
		aaa1.setLibelleAdministration("libelleAdministration");

		AutreAdministrationAgentDto aaa2 = new AutreAdministrationAgentDto();
		aaa2.setDateEntree(sdf.parse("11/10/2011"));
		aaa2.setDateSortie(sdf.parse("31/12/2012"));
		aaa2.setLibelleAdministration("libelleAdministration bis");

		AutreAdministrationAgentDto aaa3 = new AutreAdministrationAgentDto();
		aaa3.setDateEntree(sdf.parse("01/01/2013"));
		aaa3.setDateSortie(sdf.parse("10/10/2013"));
		aaa3.setLibelleAdministration("libelleAdministration");

		AutreAdministrationAgentDto aaa4 = new AutreAdministrationAgentDto();
		aaa4.setDateEntree(sdf.parse("11/10/2013"));
		aaa4.setDateSortie(sdf.parse("10/10/2014"));
		aaa4.setLibelleAdministration("libelleAdministration bis");

		List<AutreAdministrationAgentDto> listAutreAdmin = new ArrayList<AutreAdministrationAgentDto>();
		listAutreAdmin.addAll(Arrays.asList(aaa1, aaa2, aaa3, aaa4));

		IEaeRepository eaeRepository = Mockito.mock(IEaeRepository.class);

		ISirhWsConsumer sirhWsConsumer = Mockito.mock(ISirhWsConsumer.class);
		Mockito.when(sirhWsConsumer.getListeAutreAdministrationAgent(Mockito.anyInt())).thenReturn(listAutreAdmin);

		CalculEaeService service = new CalculEaeService();
		ReflectionTestUtils.setField(service, "sirhWsConsumer", sirhWsConsumer);
		ReflectionTestUtils.setField(service, "eaeRepository", eaeRepository);

		Eae eae = new Eae();
		Agent agent = new Agent();
		agent.setIdAgent(9005138);

		service.creerParcoursProAvecAutreAdministration(agent, eae);

		assertEquals(4, eae.getEaeParcoursPros().size());
	}

	@Test
	public void creerParcoursProAvecAutreAdministration_memeAdmin3fois_maisSansSeSuivre()
			throws SirhWSConsumerException, ParseException {

		AutreAdministrationAgentDto aaa1 = new AutreAdministrationAgentDto();
		aaa1.setDateEntree(sdf.parse("10/10/2010"));
		aaa1.setDateSortie(sdf.parse("09/10/2011"));
		aaa1.setLibelleAdministration("libelleAdministration");

		AutreAdministrationAgentDto aaa2 = new AutreAdministrationAgentDto();
		aaa2.setDateEntree(sdf.parse("11/10/2011"));
		aaa2.setDateSortie(sdf.parse("30/12/2012"));
		aaa2.setLibelleAdministration("libelleAdministration");

		AutreAdministrationAgentDto aaa3 = new AutreAdministrationAgentDto();
		aaa3.setDateEntree(sdf.parse("01/01/2013"));
		aaa3.setDateSortie(sdf.parse("10/10/2013"));
		aaa3.setLibelleAdministration("libelleAdministration");

		AutreAdministrationAgentDto aaa4 = new AutreAdministrationAgentDto();
		aaa4.setDateEntree(sdf.parse("11/10/2013"));
		aaa4.setDateSortie(sdf.parse("10/10/2014"));
		aaa4.setLibelleAdministration("libelleAdministration bis");

		List<AutreAdministrationAgentDto> listAutreAdmin = new ArrayList<AutreAdministrationAgentDto>();
		listAutreAdmin.addAll(Arrays.asList(aaa1, aaa2, aaa3, aaa4));

		IEaeRepository eaeRepository = Mockito.mock(IEaeRepository.class);

		ISirhWsConsumer sirhWsConsumer = Mockito.mock(ISirhWsConsumer.class);
		Mockito.when(sirhWsConsumer.getListeAutreAdministrationAgent(Mockito.anyInt())).thenReturn(listAutreAdmin);

		CalculEaeService service = new CalculEaeService();
		ReflectionTestUtils.setField(service, "sirhWsConsumer", sirhWsConsumer);
		ReflectionTestUtils.setField(service, "eaeRepository", eaeRepository);

		Eae eae = new Eae();
		Agent agent = new Agent();
		agent.setIdAgent(9005138);

		service.creerParcoursProAvecAutreAdministration(agent, eae);

		assertEquals(4, eae.getEaeParcoursPros().size());
	}

	@Test
	public void creerFormation() {

		Set<EaeFormation> eaeFormations = new HashSet<EaeFormation>();
		eaeFormations.add(new EaeFormation());

		Eae eae = new Eae();
		eae.setEaeFormations(eaeFormations);

		FormationDto formationDto = new FormationDto();
		formationDto.setAnneeFormation(2010);
		formationDto.setDureeFormation(10);
		formationDto.setUniteDuree("Jours");
		formationDto.setTitreFormation("titreFormation");
		formationDto.setCentreFormation("centreFormation");
		FormationDto formationDto2 = new FormationDto();
		formationDto2.setAnneeFormation(2010);
		formationDto2.setDureeFormation(10);
		formationDto2.setUniteDuree("Jours");
		formationDto2.setTitreFormation("titreFormation");
		formationDto2.setCentreFormation("centreFormation");
		List<FormationDto> listFormation = new ArrayList<FormationDto>();
		listFormation.add(formationDto);
		listFormation.add(formationDto2);

		IEaeRepository eaeRepository = Mockito.mock(IEaeRepository.class);

		CalculEaeService service = new CalculEaeService();
		ReflectionTestUtils.setField(service, "eaeRepository", eaeRepository);

		service.creerFormation(eae, listFormation);

		assertEquals(eae.getEaeFormations().size(), 2);
	}

	@Test
	public void creerFormation_noPersist() {

		Eae eae = new Eae();
		List<FormationDto> listFormation = new ArrayList<FormationDto>();

		IEaeRepository eaeRepository = Mockito.mock(IEaeRepository.class);

		CalculEaeService service = new CalculEaeService();
		ReflectionTestUtils.setField(service, "eaeRepository", eaeRepository);

		service.creerFormation(eae, listFormation);

		Mockito.verify(eaeRepository, Mockito.times(0)).persistEntity(Mockito.isA(EaeFormation.class));
		assertEquals(eae.getEaeFormations().size(), 0);
	}
}
