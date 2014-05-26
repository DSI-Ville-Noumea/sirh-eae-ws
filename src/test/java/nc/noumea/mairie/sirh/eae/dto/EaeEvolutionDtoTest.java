package nc.noumea.mairie.sirh.eae.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeCommentaire;
import nc.noumea.mairie.sirh.eae.domain.EaeDeveloppement;
import nc.noumea.mairie.sirh.eae.domain.EaeEvolution;
import nc.noumea.mairie.sirh.eae.domain.EaeEvolutionSouhait;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeDelaiEnum;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeTypeDeveloppementEnum;
import nc.noumea.mairie.sirh.eae.dto.poste.SpbhorDto;
import nc.noumea.mairie.sirh.eae.dto.util.ListItemDto;
import nc.noumea.mairie.sirh.eae.dto.util.ValueWithListDto;

import org.joda.time.DateTime;
import org.junit.Test;

import flexjson.PathExpression;

public class EaeEvolutionDtoTest {

	@Test
	public void testConstructor() {
		// Given
		Eae eae = new Eae();
		eae.setIdEae(189);

		EaeEvolution evolution = new EaeEvolution();
		evolution.setEae(eae);
		evolution.setMobiliteGeo(true);
		evolution.setMobiliteFonctionnelle(true);
		evolution.setChangementMetier(true);
		evolution.setDelaiEnvisage(EaeDelaiEnum.ENTRE1ET2ANS);
		evolution.setMobiliteService(true);
		evolution.setMobiliteDirection(true);
		evolution.setMobiliteCollectivite(true);
		evolution.setNomCollectivite("nom collectivité");
		evolution.setMobiliteAutre(true);
		evolution.setConcours(true);
		evolution.setNomConcours("nom concours");
		evolution.setVae(true);
		evolution.setNomVae("nom diplome");
		evolution.setTempsPartiel(true);
		evolution.setTempsPartielIdSpbhor(1);
		evolution.setRetraite(true);
		evolution.setDateRetraite(new DateTime(2014, 4, 19, 0, 0, 0, 0).toDate());
		evolution.setAutrePerspective(true);
		evolution.setLibelleAutrePerspective("autre perspective");
		evolution.setCommentaireEvolution(new EaeCommentaire());
		evolution.setCommentaireEvaluateur(new EaeCommentaire());
		evolution.setCommentaireEvalue(new EaeCommentaire());
		
		eae.setEaeEvolution(evolution);
		SpbhorDto t1 = new SpbhorDto();
		t1.setCdThor(1);
		t1.setLabel("label");
		t1.setTaux(0.9);
		List<SpbhorDto> tempsPartiels = Arrays.asList(t1);
		
		// When
		EaeEvolutionDto dto = new EaeEvolutionDto(eae, tempsPartiels);
		
		// Then
		assertEquals(189, dto.getIdEae());
		assertEquals(evolution.isMobiliteGeo(), dto.isMobiliteGeo());
		assertEquals(evolution.isMobiliteFonctionnelle(), dto.isMobiliteFonctionnelle());
		assertEquals(evolution.isChangementMetier(), dto.isChangementMetier());
		assertEquals(evolution.getDelaiEnvisage().name(), dto.getDelaiEnvisage().getCourant());
		assertEquals(evolution.isMobiliteService(), dto.isMobiliteService());
		assertEquals(evolution.isMobiliteDirection(), dto.isMobiliteDirection());
		assertEquals(evolution.isMobiliteCollectivite(), dto.isMobiliteCollectivite());
		assertEquals(evolution.getNomCollectivite(), dto.getNomCollectivite());
		assertEquals(evolution.isMobiliteAutre(), dto.isMobiliteAutre());
		assertEquals(evolution.isConcours(), dto.isConcours());
		assertEquals(evolution.getNomConcours(), dto.getNomConcours());
		assertEquals(evolution.isVae(), dto.isVae());
		assertEquals(evolution.getNomVae(), dto.getNomVae());
		assertEquals(evolution.isTempsPartiel(), dto.isTempsPartiel());
		assertEquals(evolution.getTempsPartielIdSpbhor(), new Integer(Integer.parseInt(dto.getPourcentageTempsPartiel().getCourant())));
		assertEquals(evolution.isRetraite(), dto.isRetraite());
		assertEquals(evolution.getDateRetraite(), dto.getDateRetraite());
		assertEquals(evolution.isAutrePerspective(), dto.isAutrePerspective());
		assertEquals(evolution.getLibelleAutrePerspective(), dto.getLibelleAutrePerspective());
		assertEquals(evolution.getCommentaireEvolution(), dto.getCommentaireEvolution());
		assertEquals(evolution.getCommentaireEvaluateur(), dto.getCommentaireEvaluateur());
		assertEquals(evolution.getCommentaireEvalue(), dto.getCommentaireEvalue());
	}
	
	@Test
	public void testConstructorWithLists() {
		// Given
		Eae eae = new Eae();
		eae.setIdEae(189);

		EaeEvolution evolution = new EaeEvolution();
		eae.setEaeEvolution(evolution);

		// 1 souhait
		EaeEvolutionSouhait souhait = new EaeEvolutionSouhait();
		souhait.setEaeEvolution(evolution);
		souhait.setIdEaeEvolutionSouhait(9);
		souhait.setSouhait("le souhait");
		souhait.setSuggestion("la suggestion");
		evolution.getEaeEvolutionSouhaits().add(souhait);

		// 1 development per type
		EaeDeveloppement dev1 = new EaeDeveloppement();
		dev1.setIdEaeDeveloppement(10);
		dev1.setLibelle("libelle CONNAISSANCE");
		dev1.setTypeDeveloppement(EaeTypeDeveloppementEnum.CONNAISSANCE);
		evolution.getEaeDeveloppements().add(dev1);

		EaeDeveloppement dev2 = new EaeDeveloppement();
		dev2.setIdEaeDeveloppement(20);
		dev2.setLibelle("libelle COMPETENCE");
		dev2.setTypeDeveloppement(EaeTypeDeveloppementEnum.COMPETENCE);
		evolution.getEaeDeveloppements().add(dev2);

		EaeDeveloppement dev3 = new EaeDeveloppement();
		dev3.setIdEaeDeveloppement(30);
		dev3.setLibelle("libelle CONCOURS");
		dev3.setTypeDeveloppement(EaeTypeDeveloppementEnum.CONCOURS);
		evolution.getEaeDeveloppements().add(dev3);

		EaeDeveloppement dev4 = new EaeDeveloppement();
		dev4.setIdEaeDeveloppement(40);
		dev4.setLibelle("libelle PERSONNEL");
		dev4.setTypeDeveloppement(EaeTypeDeveloppementEnum.PERSONNEL);
		evolution.getEaeDeveloppements().add(dev4);

		EaeDeveloppement dev5 = new EaeDeveloppement();
		dev5.setIdEaeDeveloppement(50);
		dev5.setLibelle("libelle COMPORTEMENT");
		dev5.setTypeDeveloppement(EaeTypeDeveloppementEnum.COMPORTEMENT);
		evolution.getEaeDeveloppements().add(dev5);

		EaeDeveloppement dev6 = new EaeDeveloppement();
		dev6.setIdEaeDeveloppement(60);
		dev6.setLibelle("libelle FORMATEUR");
		dev6.setTypeDeveloppement(EaeTypeDeveloppementEnum.FORMATEUR);
		evolution.getEaeDeveloppements().add(dev6);
		
		// When
		EaeEvolutionDto dto = new EaeEvolutionDto(eae, new ArrayList<SpbhorDto>());
		
		// Then
		assertEquals(1, dto.getSouhaitsSuggestions().size());
		assertEquals(evolution.getEaeEvolutionSouhaits().iterator().next(), dto.getSouhaitsSuggestions().get(0));
		
		assertEquals(1, dto.getDeveloppementConnaissances().size());
		assertEquals(dev1, dto.getDeveloppementConnaissances().get(0));
		
		assertEquals(1, dto.getDeveloppementCompetences().size());
		assertEquals(dev2, dto.getDeveloppementCompetences().get(0));
		
		assertEquals(1, dto.getDeveloppementExamensConcours().size());
		assertEquals(dev3, dto.getDeveloppementExamensConcours().get(0));
		
		assertEquals(1, dto.getDeveloppementPersonnel().size());
		assertEquals(dev4, dto.getDeveloppementPersonnel().get(0));
		
		assertEquals(1, dto.getDeveloppementComportement().size());
		assertEquals(dev5, dto.getDeveloppementComportement().get(0));
		
		assertEquals(1, dto.getDeveloppementFormateur().size());
		assertEquals(dev6, dto.getDeveloppementFormateur().get(0));
	}
	
	@Test
	public void testSerializeInJSON_emptyDto() {
		// Given
		EaeEvolutionDto dto = new EaeEvolutionDto();
		String expectedJson = "{\"autrePerspective\":false,\"changementMetier\":false,\"commentaireEvaluateur\":null,\"commentaireEvalue\":null,\"commentaireEvolution\":null,\"concours\":false,\"dateRetraite\":null,\"delaiEnvisage\":{\"courant\":null,\"liste\":[{\"code\":\"MOINS1AN\",\"valeur\":\"inférieur à 1 an\"},{\"code\":\"ENTRE1ET2ANS\",\"valeur\":\"entre 1 et 2 ans\"},{\"code\":\"ENTRE2ET4ANS\",\"valeur\":\"entre 2 et 4 ans\"}]},\"developpementCompetences\":[],\"developpementComportement\":[],\"developpementConnaissances\":[],\"developpementExamensConcours\":[],\"developpementFormateur\":[],\"developpementPersonnel\":[],\"idEae\":0,\"libelleAutrePerspective\":null,\"mobiliteAutre\":false,\"mobiliteCollectivite\":false,\"mobiliteDirection\":false,\"mobiliteFonctionnelle\":false,\"mobiliteGeo\":false,\"mobiliteService\":false,\"nomCollectivite\":null,\"nomConcours\":null,\"nomVae\":null,\"pourcentageTempsPartiel\":null,\"retraite\":false,\"souhaitsSuggestions\":[],\"tempsPartiel\":false,\"vae\":false}";
		
		// When
		String json = dto.serializeInJSON();
		
		// Then
		assertEquals(expectedJson, json);
	}
	
	@Test
	public void testSerializeInJSON_filledInDto() {
		// Given
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
		
		ValueWithListDto tpsPartiel = new ValueWithListDto();
		tpsPartiel.setCourant("code1");
		tpsPartiel.getListe().add(new ListItemDto("code1", "valeur1"));
		tpsPartiel.getListe().add(new ListItemDto("code2", "valeur2"));
		dto.setPourcentageTempsPartiel(tpsPartiel);
		
		dto.setCommentaireEvolution(new EaeCommentaire());
		dto.getCommentaireEvolution().setText("commentaire evolution");
		
		dto.setCommentaireEvaluateur(new EaeCommentaire());
		dto.getCommentaireEvaluateur().setText("commentaire evaluateur");
		
		dto.setCommentaireEvalue(new EaeCommentaire());
		dto.getCommentaireEvalue().setText("commentaire evalue");
		
		// 1 souhait
		EaeEvolutionSouhait souhait = new EaeEvolutionSouhait();
		souhait.setIdEaeEvolutionSouhait(9);
		souhait.setSouhait("le souhait");
		souhait.setSuggestion("la suggestion");
		dto.getSouhaitsSuggestions().add(souhait);

		// 1 development per type
		EaeDeveloppement dev1 = new EaeDeveloppement();
		dev1.setIdEaeDeveloppement(10);
		dev1.setLibelle("libelle CONNAISSANCE");
		dev1.setTypeDeveloppement(EaeTypeDeveloppementEnum.CONNAISSANCE);
		dto.getDeveloppementConnaissances().add(dev1);

		EaeDeveloppement dev2 = new EaeDeveloppement();
		dev2.setIdEaeDeveloppement(20);
		dev2.setLibelle("libelle COMPETENCE");
		dev2.setTypeDeveloppement(EaeTypeDeveloppementEnum.COMPETENCE);
		dto.getDeveloppementCompetences().add(dev2);

		EaeDeveloppement dev3 = new EaeDeveloppement();
		dev3.setIdEaeDeveloppement(30);
		dev3.setLibelle("libelle CONCOURS");
		dev3.setTypeDeveloppement(EaeTypeDeveloppementEnum.CONCOURS);
		dto.getDeveloppementExamensConcours().add(dev3);

		EaeDeveloppement dev4 = new EaeDeveloppement();
		dev4.setIdEaeDeveloppement(40);
		dev4.setLibelle("libelle PERSONNEL");
		dev4.setTypeDeveloppement(EaeTypeDeveloppementEnum.PERSONNEL);
		dto.getDeveloppementPersonnel().add(dev4);

		EaeDeveloppement dev5 = new EaeDeveloppement();
		dev5.setIdEaeDeveloppement(50);
		dev5.setLibelle("libelle COMPORTEMENT");
		dev5.setTypeDeveloppement(EaeTypeDeveloppementEnum.COMPORTEMENT);
		dto.getDeveloppementComportement().add(dev5);

		EaeDeveloppement dev6 = new EaeDeveloppement();
		dev6.setIdEaeDeveloppement(60);
		dev6.setLibelle("libelle FORMATEUR");
		dev6.setTypeDeveloppement(EaeTypeDeveloppementEnum.FORMATEUR);
		dto.getDeveloppementFormateur().add(dev6);
		
		String expectedJson = "{\"autrePerspective\":true,\"changementMetier\":true,\"commentaireEvaluateur\":\"commentaire evaluateur\",\"commentaireEvalue\":\"commentaire evalue\",\"commentaireEvolution\":\"commentaire evolution\",\"concours\":true,\"dateRetraite\":\"/Date(1397826000000+1100)/\",\"delaiEnvisage\":{\"courant\":\"ENTRE1ET2ANS\",\"liste\":[{\"code\":\"MOINS1AN\",\"valeur\":\"inférieur à 1 an\"},{\"code\":\"ENTRE1ET2ANS\",\"valeur\":\"entre 1 et 2 ans\"},{\"code\":\"ENTRE2ET4ANS\",\"valeur\":\"entre 2 et 4 ans\"}]},\"developpementCompetences\":[{\"echeance\":null,\"idEaeDeveloppement\":20,\"libelle\":\"libelle COMPETENCE\",\"priorisation\":0}],\"developpementComportement\":[{\"echeance\":null,\"idEaeDeveloppement\":50,\"libelle\":\"libelle COMPORTEMENT\",\"priorisation\":0}],\"developpementConnaissances\":[{\"echeance\":null,\"idEaeDeveloppement\":10,\"libelle\":\"libelle CONNAISSANCE\",\"priorisation\":0}],\"developpementExamensConcours\":[{\"echeance\":null,\"idEaeDeveloppement\":30,\"libelle\":\"libelle CONCOURS\",\"priorisation\":0}],\"developpementFormateur\":[{\"echeance\":null,\"idEaeDeveloppement\":60,\"libelle\":\"libelle FORMATEUR\",\"priorisation\":0}],\"developpementPersonnel\":[{\"echeance\":null,\"idEaeDeveloppement\":40,\"libelle\":\"libelle PERSONNEL\",\"priorisation\":0}],\"idEae\":19,\"libelleAutrePerspective\":\"autre perspective\",\"mobiliteAutre\":true,\"mobiliteCollectivite\":true,\"mobiliteDirection\":true,\"mobiliteFonctionnelle\":true,\"mobiliteGeo\":true,\"mobiliteService\":true,\"nomCollectivite\":\"nom collectivité\",\"nomConcours\":\"nom concours\",\"nomVae\":\"nom diplome\",\"pourcentageTempsPartiel\":{\"courant\":\"code1\",\"liste\":[{\"code\":\"code1\",\"valeur\":\"valeur1\"},{\"code\":\"code2\",\"valeur\":\"valeur2\"}]},\"retraite\":true,\"souhaitsSuggestions\":[{\"idEaeEvolutionSouhait\":9,\"souhait\":\"le souhait\",\"suggestion\":\"la suggestion\"}],\"tempsPartiel\":true,\"vae\":true}";
		
		// When
		String json = dto.serializeInJSON();
		
		// Then
		assertEquals(expectedJson, json);
	}
	
	@Test
	public void testGetSerializerForEaeEvolutionDto_includes_exlcudes() {
		// When
		List<PathExpression> includes = EaeEvolutionDto.getSerializerForEaeEvolutionDto().getIncludes();
		List<PathExpression> excludes = EaeEvolutionDto.getSerializerForEaeEvolutionDto().getExcludes();
		
		// Then
		assertEquals(50, includes.size());
		assertEquals("[idEae]", includes.get(0).toString());
		assertEquals("[mobiliteGeo]", includes.get(1).toString());
		assertEquals("[mobiliteFonctionnelle]", includes.get(2).toString());
		assertEquals("[changementMetier]", includes.get(3).toString());
		assertEquals("[delaiEnvisage,*]", includes.get(4).toString());
		assertEquals("[mobiliteService]", includes.get(5).toString());
		assertEquals("[mobiliteDirection]", includes.get(6).toString());
		assertEquals("[mobiliteCollectivite]", includes.get(7).toString());
		assertEquals("[nomCollectivite]", includes.get(8).toString());
		assertEquals("[mobiliteAutre]", includes.get(9).toString());
		assertEquals("[concours]", includes.get(10).toString());
		assertEquals("[nomConcours]", includes.get(11).toString());
		assertEquals("[vae]", includes.get(12).toString());
		assertEquals("[nomVae]", includes.get(13).toString());
		assertEquals("[tempsPartiel]", includes.get(14).toString());
		assertEquals("[pourcentageTempsPartiel,*]", includes.get(15).toString());
		assertEquals("[retraite]", includes.get(16).toString());
		assertEquals("[dateRetraite]", includes.get(17).toString());
		assertEquals("[autrePerspective]", includes.get(18).toString());
		assertEquals("[libelleAutrePerspective]", includes.get(19).toString());
		assertEquals("[souhaitsSuggestions,idEaeEvolutionSouhait]", includes.get(20).toString());
		assertEquals("[souhaitsSuggestions,souhait]", includes.get(21).toString());
		assertEquals("[souhaitsSuggestions,suggestion]", includes.get(22).toString());
		assertEquals("[developpementConnaissances,idEaeDeveloppement]", includes.get(23).toString());
		assertEquals("[developpementConnaissances,libelle]", includes.get(24).toString());
		assertEquals("[developpementConnaissances,echeance]", includes.get(25).toString());
		assertEquals("[developpementConnaissances,priorisation]", includes.get(26).toString());
		assertEquals("[developpementCompetences,idEaeDeveloppement]", includes.get(27).toString());
		assertEquals("[developpementCompetences,libelle]", includes.get(28).toString());
		assertEquals("[developpementCompetences,echeance]", includes.get(29).toString());
		assertEquals("[developpementCompetences,priorisation]", includes.get(30).toString());
		assertEquals("[developpementExamensConcours,idEaeDeveloppement]", includes.get(31).toString());
		assertEquals("[developpementExamensConcours,libelle]", includes.get(32).toString());
		assertEquals("[developpementExamensConcours,echeance]", includes.get(33).toString());
		assertEquals("[developpementExamensConcours,priorisation]", includes.get(34).toString());
		assertEquals("[developpementPersonnel,idEaeDeveloppement]", includes.get(35).toString());
		assertEquals("[developpementPersonnel,libelle]", includes.get(36).toString());
		assertEquals("[developpementPersonnel,echeance]", includes.get(37).toString());
		assertEquals("[developpementPersonnel,priorisation]", includes.get(38).toString());
		assertEquals("[developpementComportement,idEaeDeveloppement]", includes.get(39).toString());
		assertEquals("[developpementComportement,libelle]", includes.get(40).toString());
		assertEquals("[developpementComportement,echeance]", includes.get(41).toString());
		assertEquals("[developpementComportement,priorisation]", includes.get(42).toString());
		assertEquals("[developpementFormateur,idEaeDeveloppement]", includes.get(43).toString());
		assertEquals("[developpementFormateur,libelle]", includes.get(44).toString());
		assertEquals("[developpementFormateur,echeance]", includes.get(45).toString());
		assertEquals("[developpementFormateur,priorisation]", includes.get(46).toString());
		assertEquals("[commentaireEvolution]", includes.get(47).toString());
		assertEquals("[commentaireEvaluateur]", includes.get(48).toString());
		assertEquals("[commentaireEvalue]", includes.get(49).toString());
		
		assertEquals(2, excludes.size());
		assertEquals("[*,class]", excludes.get(0).toString());
		assertEquals("[*]", excludes.get(1).toString());
	}
	
	@Test
	public void testDeserializeFromJson() {
		// Given
		String json = "{\"autrePerspective\":true,\"changementMetier\":true,\"commentaireEvaluateur\":\"commentaire evaluateur\",\"commentaireEvalue\":\"commentaire evalue\",\"commentaireEvolution\":\"commentaire evolution\",\"developpementCompetences\":[{\"echeance\":\"/Date(1397826000000+1100)/\",\"idEaeDeveloppement\":20,\"libelle\":\"libelle COMPETENCE\",\"priorisation\":1}],\"developpementComportement\":[{\"echeance\":\"/Date(1397826000000+1100)/\",\"idEaeDeveloppement\":50,\"libelle\":\"libelle COMPORTEMENT\",\"priorisation\":2}],\"concours\":true,\"developpementConnaissances\":[{\"echeance\":\"/Date(1397826000000+1100)/\",\"idEaeDeveloppement\":10,\"libelle\":\"libelle CONNAISSANCE\",\"priorisation\":3}],\"dateRetraite\":\"/Date(1397826000000+1100)/\",\"delaiEnvisage\":{\"courant\":\"ENTRE1ET2ANS\",\"liste\":[{\"code\":\"MOINS1AN\",\"valeur\":\"inférieur à 1 an\"},{\"code\":\"ENTRE1ET2ANS\",\"valeur\":\"entre 1 et 2 ans\"},{\"code\":\"ENTRE2ET4ANS\",\"valeur\":\"entre 2 et 4 ans\",\"priorisation\":4}]},\"developpementExamensConcours\":[{\"echeance\":\"/Date(1397826000000+1100)/\",\"idEaeDeveloppement\":30,\"libelle\":\"libelle CONCOURS\",\"priorisation\":5}],\"developpementFormateur\":[{\"echeance\":\"/Date(1397826000000+1100)/\",\"idEaeDeveloppement\":60,\"libelle\":\"libelle FORMATEUR\",\"priorisation\":6}],\"idEae\":19,\"libelleAutrePerspective\":\"autre perspective\",\"mobiliteAutre\":true,\"mobiliteCollectivite\":true,\"mobiliteDirection\":true,\"mobiliteFonctionnelle\":true,\"mobiliteGeo\":true,\"mobiliteService\":true,\"nomCollectivite\":\"nom collectivité\",\"nomConcours\":\"nom concours\",\"nomVae\":\"nom diplome\",\"developpementPersonnel\":[{\"echeance\":\"/Date(1397826000000+1100)/\",\"idEaeDeveloppement\":40,\"libelle\":\"libelle PERSONNEL\",\"priorisation\":7}],\"pourcentageTempsPartiel\":{\"courant\":\"code1\",\"liste\":[{\"code\":\"code1\",\"valeur\":\"valeur1\"},{\"code\":\"code2\",\"valeur\":\"valeur2\"}]},\"retraite\":true,\"souhaitsSuggestions\":[{\"idEaeEvolutionSouhait\":9,\"souhait\":\"le souhait\",\"suggestion\":\"la suggestion\"}],\"tempsPartiel\":true,\"vae\":true}";
		
		// When
		EaeEvolutionDto dto = new EaeEvolutionDto().deserializeFromJSON(json);
		
		// Then
		assertEquals(19, dto.getIdEae());
		assertTrue(dto.isMobiliteGeo());
		assertTrue(dto.isMobiliteFonctionnelle());
		assertTrue(dto.isChangementMetier());
		assertEquals("ENTRE1ET2ANS", dto.getDelaiEnvisage().getCourant());
		assertTrue(dto.isMobiliteService());
		assertTrue(dto.isMobiliteDirection());
		assertTrue(dto.isMobiliteCollectivite());
		assertEquals("nom collectivité", dto.getNomCollectivite());
		assertTrue(dto.isMobiliteAutre());
		assertTrue(dto.isConcours());
		assertEquals("nom concours", dto.getNomConcours());
		assertTrue(dto.isVae());
		assertEquals("nom diplome", dto.getNomVae());
		assertTrue(dto.isTempsPartiel());
		assertEquals("code1", dto.getPourcentageTempsPartiel().getCourant());
		assertTrue(dto.isRetraite());
		assertEquals(new DateTime(2014, 4, 19, 0, 0, 0, 0).toDate(), dto.getDateRetraite());
		assertTrue(dto.isAutrePerspective());
		assertEquals("autre perspective", dto.getLibelleAutrePerspective());
		assertEquals("commentaire evolution", dto.getCommentaireEvolution().getText());
		assertEquals("commentaire evaluateur", dto.getCommentaireEvaluateur().getText());
		assertEquals("commentaire evalue", dto.getCommentaireEvalue().getText());
		
		assertEquals(1, dto.getSouhaitsSuggestions().size());
		assertEquals("le souhait", dto.getSouhaitsSuggestions().get(0).getSouhait());
		assertEquals("la suggestion", dto.getSouhaitsSuggestions().get(0).getSuggestion());
		
		assertEquals(1, dto.getDeveloppementConnaissances().size());
		assertEquals(new Integer(10), dto.getDeveloppementConnaissances().get(0).getIdEaeDeveloppement());
		assertEquals("libelle CONNAISSANCE", dto.getDeveloppementConnaissances().get(0).getLibelle());
		assertEquals(new DateTime(2014, 4, 19, 0, 0, 0, 0).toDate(), dto.getDeveloppementConnaissances().get(0).getEcheance());
		assertEquals(3, dto.getDeveloppementConnaissances().get(0).getPriorisation());
		
		assertEquals(1, dto.getDeveloppementCompetences().size());
		assertEquals(new Integer(20), dto.getDeveloppementCompetences().get(0).getIdEaeDeveloppement());
		assertEquals("libelle COMPETENCE", dto.getDeveloppementCompetences().get(0).getLibelle());
		assertEquals(new DateTime(2014, 4, 19, 0, 0, 0, 0).toDate(), dto.getDeveloppementCompetences().get(0).getEcheance());
		assertEquals(1, dto.getDeveloppementCompetences().get(0).getPriorisation());
		
		assertEquals(1, dto.getDeveloppementExamensConcours().size());
		assertEquals(new Integer(30), dto.getDeveloppementExamensConcours().get(0).getIdEaeDeveloppement());
		assertEquals("libelle CONCOURS", dto.getDeveloppementExamensConcours().get(0).getLibelle());
		assertEquals(new DateTime(2014, 4, 19, 0, 0, 0, 0).toDate(), dto.getDeveloppementExamensConcours().get(0).getEcheance());
		assertEquals(5, dto.getDeveloppementExamensConcours().get(0).getPriorisation());
		
		assertEquals(1, dto.getDeveloppementPersonnel().size());
		assertEquals(new Integer(40), dto.getDeveloppementPersonnel().get(0).getIdEaeDeveloppement());
		assertEquals("libelle PERSONNEL", dto.getDeveloppementPersonnel().get(0).getLibelle());
		assertEquals(new DateTime(2014, 4, 19, 0, 0, 0, 0).toDate(), dto.getDeveloppementPersonnel().get(0).getEcheance());
		assertEquals(7, dto.getDeveloppementPersonnel().get(0).getPriorisation());
		
		assertEquals(1, dto.getDeveloppementComportement().size());
		assertEquals(new Integer(50), dto.getDeveloppementComportement().get(0).getIdEaeDeveloppement());
		assertEquals("libelle COMPORTEMENT", dto.getDeveloppementComportement().get(0).getLibelle());
		assertEquals(new DateTime(2014, 4, 19, 0, 0, 0, 0).toDate(), dto.getDeveloppementComportement().get(0).getEcheance());
		assertEquals(2, dto.getDeveloppementComportement().get(0).getPriorisation());
		
		assertEquals(1, dto.getDeveloppementFormateur().size());
		assertEquals(new Integer(60), dto.getDeveloppementFormateur().get(0).getIdEaeDeveloppement());
		assertEquals("libelle FORMATEUR", dto.getDeveloppementFormateur().get(0).getLibelle());
		assertEquals(new DateTime(2014, 4, 19, 0, 0, 0, 0).toDate(), dto.getDeveloppementFormateur().get(0).getEcheance());
		assertEquals(6, dto.getDeveloppementFormateur().get(0).getPriorisation());
	}
}
