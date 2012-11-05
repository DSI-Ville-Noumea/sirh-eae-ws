package nc.noumea.mairie.sirh.eae.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeCommentaire;
import nc.noumea.mairie.sirh.eae.domain.EaeDeveloppement;
import nc.noumea.mairie.sirh.eae.domain.EaeEvolution;
import nc.noumea.mairie.sirh.eae.domain.EaeEvolutionSouhait;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeDelaiEnum;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeTypeDeveloppementEnum;
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
		evolution.setPourcentageTempsParciel(50);
		evolution.setRetraite(true);
		evolution.setDateRetraite(new DateTime(2014, 4, 19, 0, 0, 0, 0).toDate());
		evolution.setAutrePerspective(true);
		evolution.setLibelleAutrePerspective("autre perspective");
		evolution.setCommentaireEvolution(new EaeCommentaire());
		evolution.setCommentaireEvaluateur(new EaeCommentaire());
		evolution.setCommentaireEvalue(new EaeCommentaire());
		
		eae.setEaeEvolution(evolution);
		
		// When
		EaeEvolutionDto dto = new EaeEvolutionDto(eae);
		
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
		assertEquals(evolution.getNomVae(), dto.getNomDiplome());
		assertEquals(evolution.isTempsPartiel(), dto.isTempsPartiel());
		assertEquals(evolution.getPourcentageTempsParciel(), dto.getPourcentageTempsPartiel());
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
		EaeEvolutionDto dto = new EaeEvolutionDto(eae);
		
		// Then
		assertEquals(1, dto.getSouhaitsSuggestions().size());
		assertEquals(evolution.getEaeEvolutionSouhaits().iterator().next(), dto.getSouhaitsSuggestions().get(0));
		
		assertEquals(1, dto.getConnaissances().size());
		assertEquals(dev1, dto.getConnaissances().get(0));
		
		assertEquals(1, dto.getCompetences().size());
		assertEquals(dev2, dto.getCompetences().get(0));
		
		assertEquals(1, dto.getExamensConcours().size());
		assertEquals(dev3, dto.getExamensConcours().get(0));
		
		assertEquals(1, dto.getPersonnel().size());
		assertEquals(dev4, dto.getPersonnel().get(0));
		
		assertEquals(1, dto.getComportement().size());
		assertEquals(dev5, dto.getComportement().get(0));
		
		assertEquals(1, dto.getFormateur().size());
		assertEquals(dev6, dto.getFormateur().get(0));
	}
	
	@Test
	public void testSerializeInJSON_emptyDto() {
		// Given
		EaeEvolutionDto dto = new EaeEvolutionDto();
		String expectedJson = "{\"autrePerspective\":false,\"changementMetier\":false,\"commentaireEvaluateur\":null,\"commentaireEvalue\":null,\"commentaireEvolution\":null,\"competences\":[],\"comportement\":[],\"concours\":false,\"connaissances\":[],\"dateRetraite\":null,\"delaiEnvisage\":{\"courant\":null,\"liste\":[{\"code\":\"MOINS1AN\",\"valeur\":\"inférieur à 1 an\"},{\"code\":\"ENTRE1ET2ANS\",\"valeur\":\"entre 1 et 2 ans\"},{\"code\":\"ENTRE2ET4ANS\",\"valeur\":\"entre 2 et 4 ans\"}]},\"examensConcours\":[],\"formateur\":[],\"idEae\":0,\"libelleAutrePerspective\":null,\"mobiliteAutre\":false,\"mobiliteCollectivite\":false,\"mobiliteDirection\":false,\"mobiliteFonctionnelle\":false,\"mobiliteGeo\":false,\"mobiliteService\":false,\"nomCollectivite\":null,\"nomConcours\":null,\"nomDiplome\":null,\"personnel\":[],\"pourcentageTempsPartiel\":0,\"retraite\":false,\"souhaitsSuggestions\":[],\"tempsPartiel\":false,\"vae\":false}";
		
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
		dto.setNomDiplome("nom diplome");
		dto.setTempsPartiel(true);
		dto.setPourcentageTempsPartiel(50);
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
		dto.getConnaissances().add(dev1);

		EaeDeveloppement dev2 = new EaeDeveloppement();
		dev2.setIdEaeDeveloppement(20);
		dev2.setLibelle("libelle COMPETENCE");
		dev2.setTypeDeveloppement(EaeTypeDeveloppementEnum.COMPETENCE);
		dto.getCompetences().add(dev2);

		EaeDeveloppement dev3 = new EaeDeveloppement();
		dev3.setIdEaeDeveloppement(30);
		dev3.setLibelle("libelle CONCOURS");
		dev3.setTypeDeveloppement(EaeTypeDeveloppementEnum.CONCOURS);
		dto.getExamensConcours().add(dev3);

		EaeDeveloppement dev4 = new EaeDeveloppement();
		dev4.setIdEaeDeveloppement(40);
		dev4.setLibelle("libelle PERSONNEL");
		dev4.setTypeDeveloppement(EaeTypeDeveloppementEnum.PERSONNEL);
		dto.getPersonnel().add(dev4);

		EaeDeveloppement dev5 = new EaeDeveloppement();
		dev5.setIdEaeDeveloppement(50);
		dev5.setLibelle("libelle COMPORTEMENT");
		dev5.setTypeDeveloppement(EaeTypeDeveloppementEnum.COMPORTEMENT);
		dto.getComportement().add(dev5);

		EaeDeveloppement dev6 = new EaeDeveloppement();
		dev6.setIdEaeDeveloppement(60);
		dev6.setLibelle("libelle FORMATEUR");
		dev6.setTypeDeveloppement(EaeTypeDeveloppementEnum.FORMATEUR);
		dto.getFormateur().add(dev6);
		
		String expectedJson = "{\"autrePerspective\":true,\"changementMetier\":true,\"commentaireEvaluateur\":\"commentaire evaluateur\",\"commentaireEvalue\":\"commentaire evalue\",\"commentaireEvolution\":\"commentaire evolution\",\"competences\":[{\"echeance\":null,\"idEaeDeveloppement\":20,\"libelle\":\"libelle COMPETENCE\",\"priorisation\":0}],\"comportement\":[{\"echeance\":null,\"idEaeDeveloppement\":50,\"libelle\":\"libelle COMPORTEMENT\",\"priorisation\":0}],\"concours\":true,\"connaissances\":[{\"echeance\":null,\"idEaeDeveloppement\":10,\"libelle\":\"libelle CONNAISSANCE\",\"priorisation\":0}],\"dateRetraite\":\"/Date(1397826000000+1100)/\",\"delaiEnvisage\":{\"courant\":\"ENTRE1ET2ANS\",\"liste\":[{\"code\":\"MOINS1AN\",\"valeur\":\"inférieur à 1 an\"},{\"code\":\"ENTRE1ET2ANS\",\"valeur\":\"entre 1 et 2 ans\"},{\"code\":\"ENTRE2ET4ANS\",\"valeur\":\"entre 2 et 4 ans\"}]},\"examensConcours\":[{\"echeance\":null,\"idEaeDeveloppement\":30,\"libelle\":\"libelle CONCOURS\",\"priorisation\":0}],\"formateur\":[{\"echeance\":null,\"idEaeDeveloppement\":60,\"libelle\":\"libelle FORMATEUR\",\"priorisation\":0}],\"idEae\":19,\"libelleAutrePerspective\":\"autre perspective\",\"mobiliteAutre\":true,\"mobiliteCollectivite\":true,\"mobiliteDirection\":true,\"mobiliteFonctionnelle\":true,\"mobiliteGeo\":true,\"mobiliteService\":true,\"nomCollectivite\":\"nom collectivité\",\"nomConcours\":\"nom concours\",\"nomDiplome\":\"nom diplome\",\"personnel\":[{\"echeance\":null,\"idEaeDeveloppement\":40,\"libelle\":\"libelle PERSONNEL\",\"priorisation\":0}],\"pourcentageTempsPartiel\":50,\"retraite\":true,\"souhaitsSuggestions\":[{\"idEaeEvolutionSouhait\":9,\"souhait\":\"le souhait\",\"suggestion\":\"la suggestion\"}],\"tempsPartiel\":true,\"vae\":true}";
		
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
		assertEquals("[nomDiplome]", includes.get(13).toString());
		assertEquals("[tempsPartiel]", includes.get(14).toString());
		assertEquals("[pourcentageTempsPartiel]", includes.get(15).toString());
		assertEquals("[retraite]", includes.get(16).toString());
		assertEquals("[dateRetraite]", includes.get(17).toString());
		assertEquals("[autrePerspective]", includes.get(18).toString());
		assertEquals("[libelleAutrePerspective]", includes.get(19).toString());
		assertEquals("[souhaitsSuggestions,idEaeEvolutionSouhait]", includes.get(20).toString());
		assertEquals("[souhaitsSuggestions,souhait]", includes.get(21).toString());
		assertEquals("[souhaitsSuggestions,suggestion]", includes.get(22).toString());
		assertEquals("[connaissances,idEaeDeveloppement]", includes.get(23).toString());
		assertEquals("[connaissances,libelle]", includes.get(24).toString());
		assertEquals("[connaissances,echeance]", includes.get(25).toString());
		assertEquals("[connaissances,priorisation]", includes.get(26).toString());
		assertEquals("[competences,idEaeDeveloppement]", includes.get(27).toString());
		assertEquals("[competences,libelle]", includes.get(28).toString());
		assertEquals("[competences,echeance]", includes.get(29).toString());
		assertEquals("[competences,priorisation]", includes.get(30).toString());
		assertEquals("[examensConcours,idEaeDeveloppement]", includes.get(31).toString());
		assertEquals("[examensConcours,libelle]", includes.get(32).toString());
		assertEquals("[examensConcours,echeance]", includes.get(33).toString());
		assertEquals("[examensConcours,priorisation]", includes.get(34).toString());
		assertEquals("[personnel,idEaeDeveloppement]", includes.get(35).toString());
		assertEquals("[personnel,libelle]", includes.get(36).toString());
		assertEquals("[personnel,echeance]", includes.get(37).toString());
		assertEquals("[personnel,priorisation]", includes.get(38).toString());
		assertEquals("[comportement,idEaeDeveloppement]", includes.get(39).toString());
		assertEquals("[comportement,libelle]", includes.get(40).toString());
		assertEquals("[comportement,echeance]", includes.get(41).toString());
		assertEquals("[comportement,priorisation]", includes.get(42).toString());
		assertEquals("[formateur,idEaeDeveloppement]", includes.get(43).toString());
		assertEquals("[formateur,libelle]", includes.get(44).toString());
		assertEquals("[formateur,echeance]", includes.get(45).toString());
		assertEquals("[formateur,priorisation]", includes.get(46).toString());
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
		String json = "{\"autrePerspective\":true,\"changementMetier\":true,\"commentaireEvaluateur\":\"commentaire evaluateur\",\"commentaireEvalue\":\"commentaire evalue\",\"commentaireEvolution\":\"commentaire evolution\",\"competences\":[{\"echeance\":\"/Date(1397826000000+1100)/\",\"idEaeDeveloppement\":20,\"libelle\":\"libelle COMPETENCE\",\"priorisation\":1}],\"comportement\":[{\"echeance\":\"/Date(1397826000000+1100)/\",\"idEaeDeveloppement\":50,\"libelle\":\"libelle COMPORTEMENT\",\"priorisation\":2}],\"concours\":true,\"connaissances\":[{\"echeance\":\"/Date(1397826000000+1100)/\",\"idEaeDeveloppement\":10,\"libelle\":\"libelle CONNAISSANCE\",\"priorisation\":3}],\"dateRetraite\":\"/Date(1397826000000+1100)/\",\"delaiEnvisage\":{\"courant\":\"ENTRE1ET2ANS\",\"liste\":[{\"code\":\"MOINS1AN\",\"valeur\":\"inférieur à 1 an\"},{\"code\":\"ENTRE1ET2ANS\",\"valeur\":\"entre 1 et 2 ans\"},{\"code\":\"ENTRE2ET4ANS\",\"valeur\":\"entre 2 et 4 ans\",\"priorisation\":4}]},\"examensConcours\":[{\"echeance\":\"/Date(1397826000000+1100)/\",\"idEaeDeveloppement\":30,\"libelle\":\"libelle CONCOURS\",\"priorisation\":5}],\"formateur\":[{\"echeance\":\"/Date(1397826000000+1100)/\",\"idEaeDeveloppement\":60,\"libelle\":\"libelle FORMATEUR\",\"priorisation\":6}],\"idEae\":19,\"libelleAutrePerspective\":\"autre perspective\",\"mobiliteAutre\":true,\"mobiliteCollectivite\":true,\"mobiliteDirection\":true,\"mobiliteFonctionnelle\":true,\"mobiliteGeo\":true,\"mobiliteService\":true,\"nomCollectivite\":\"nom collectivité\",\"nomConcours\":\"nom concours\",\"nomDiplome\":\"nom diplome\",\"personnel\":[{\"echeance\":\"/Date(1397826000000+1100)/\",\"idEaeDeveloppement\":40,\"libelle\":\"libelle PERSONNEL\",\"priorisation\":7}],\"pourcentageTempsPartiel\":50,\"retraite\":true,\"souhaitsSuggestions\":[{\"idEaeEvolutionSouhait\":9,\"souhait\":\"le souhait\",\"suggestion\":\"la suggestion\"}],\"tempsPartiel\":true,\"vae\":true}";
		
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
		assertEquals("nom diplome", dto.getNomDiplome());
		assertTrue(dto.isTempsPartiel());
		assertEquals(50, dto.getPourcentageTempsPartiel());
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
		
		assertEquals(1, dto.getConnaissances().size());
		assertEquals(new Integer(10), dto.getConnaissances().get(0).getIdEaeDeveloppement());
		assertEquals("libelle CONNAISSANCE", dto.getConnaissances().get(0).getLibelle());
		assertEquals(new DateTime(2014, 4, 19, 0, 0, 0, 0).toDate(), dto.getConnaissances().get(0).getEcheance());
		assertEquals(3, dto.getConnaissances().get(0).getPriorisation());
		
		assertEquals(1, dto.getCompetences().size());
		assertEquals(new Integer(20), dto.getCompetences().get(0).getIdEaeDeveloppement());
		assertEquals("libelle COMPETENCE", dto.getCompetences().get(0).getLibelle());
		assertEquals(new DateTime(2014, 4, 19, 0, 0, 0, 0).toDate(), dto.getCompetences().get(0).getEcheance());
		assertEquals(1, dto.getCompetences().get(0).getPriorisation());
		
		assertEquals(1, dto.getExamensConcours().size());
		assertEquals(new Integer(30), dto.getExamensConcours().get(0).getIdEaeDeveloppement());
		assertEquals("libelle CONCOURS", dto.getExamensConcours().get(0).getLibelle());
		assertEquals(new DateTime(2014, 4, 19, 0, 0, 0, 0).toDate(), dto.getExamensConcours().get(0).getEcheance());
		assertEquals(5, dto.getExamensConcours().get(0).getPriorisation());
		
		assertEquals(1, dto.getPersonnel().size());
		assertEquals(new Integer(40), dto.getPersonnel().get(0).getIdEaeDeveloppement());
		assertEquals("libelle PERSONNEL", dto.getPersonnel().get(0).getLibelle());
		assertEquals(new DateTime(2014, 4, 19, 0, 0, 0, 0).toDate(), dto.getPersonnel().get(0).getEcheance());
		assertEquals(7, dto.getPersonnel().get(0).getPriorisation());
		
		assertEquals(1, dto.getComportement().size());
		assertEquals(new Integer(50), dto.getComportement().get(0).getIdEaeDeveloppement());
		assertEquals("libelle COMPORTEMENT", dto.getComportement().get(0).getLibelle());
		assertEquals(new DateTime(2014, 4, 19, 0, 0, 0, 0).toDate(), dto.getComportement().get(0).getEcheance());
		assertEquals(2, dto.getComportement().get(0).getPriorisation());
		
		assertEquals(1, dto.getFormateur().size());
		assertEquals(new Integer(60), dto.getFormateur().get(0).getIdEaeDeveloppement());
		assertEquals("libelle FORMATEUR", dto.getFormateur().get(0).getLibelle());
		assertEquals(new DateTime(2014, 4, 19, 0, 0, 0, 0).toDate(), dto.getFormateur().get(0).getEcheance());
		assertEquals(6, dto.getFormateur().get(0).getPriorisation());
	}
}
