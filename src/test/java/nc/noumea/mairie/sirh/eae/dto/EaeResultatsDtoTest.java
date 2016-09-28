package nc.noumea.mairie.sirh.eae.dto;

import static org.junit.Assert.assertEquals;
import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeCommentaire;
import nc.noumea.mairie.sirh.eae.domain.EaeResultat;
import nc.noumea.mairie.sirh.eae.domain.EaeTypeObjectif;

import org.junit.Test;

public class EaeResultatsDtoTest {

	@Test
	public void testConstructor() {
		
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
		
		EaeResultat res3 = new EaeResultat();
		res3.setTypeObjectif(t3);
		res3.setObjectif("obj3");
		eae.getEaeResultats().add(res3);
		
		// When
		EaeResultatsDto dto = new EaeResultatsDto(eae);
		
		// Then
		assertEquals(789, dto.getIdEae());
		assertEquals("the comment text", dto.getCommentaireGeneral());
		assertEquals(1, dto.getObjectifsProfessionnels().size());
		assertEquals("obj1", dto.getObjectifsProfessionnels().get(0).getObjectif());
		assertEquals(1, dto.getObjectifsIndividuels().size());
		assertEquals("obj2", dto.getObjectifsIndividuels().get(0).getObjectif());
	}
	
//	@Test
//	public void testGetSerializerForEaeResultatDto_includes_excludes() {
//		
//		// When
//		List<PathExpression> includes = EaeResultatsDto.getSerializerForEaeResultatDto().getIncludes();
//		List<PathExpression> excludes = EaeResultatsDto.getSerializerForEaeResultatDto().getExcludes();
//		
//		// Then
//		assertEquals(10, includes.size());
//		assertEquals("[idEae]", includes.get(0).toString());
//		assertEquals("[commentaireGeneral]", includes.get(1).toString());
//		assertEquals("[objectifsIndividuels,idEaeResultat]", includes.get(2).toString());
//		assertEquals("[objectifsIndividuels,objectif]", includes.get(3).toString());
//		assertEquals("[objectifsIndividuels,resultat]", includes.get(4).toString());
//		assertEquals("[objectifsIndividuels,commentaire]", includes.get(5).toString());
//		assertEquals("[objectifsProfessionnels,idEaeResultat]", includes.get(6).toString());
//		assertEquals("[objectifsProfessionnels,objectif]", includes.get(7).toString());
//		assertEquals("[objectifsProfessionnels,resultat]", includes.get(8).toString());
//		assertEquals("[objectifsProfessionnels,commentaire]", includes.get(9).toString());
//		
//		assertEquals(2, excludes.size());
//		assertEquals("[*,class]", excludes.get(0).toString());
//		assertEquals("[*]", excludes.get(1).toString());
//	}
//	
//	@Test
//	public void testSerializeInJSON_EmptyObject() {
//		
//		//Given
//		EaeResultatsDto dto = new EaeResultatsDto();
//		String expectedResult = "{\"commentaireGeneral\":null,\"idEae\":0,\"objectifsIndividuels\":[],\"objectifsProfessionnels\":[]}";
//		
//		// When
//		String result = dto.serializeInJSON();
//		
//		// Then
//		assertEquals(expectedResult, result);
//	}
//	
//	@Test
//	public void testSerializeInJSON_FilledObject() {
//		
//		//Given
//		EaeResultatsDto dto = new EaeResultatsDto();
//		dto.setIdEae(56789);
//		dto.setCommentaireGeneral("very short comment");
//		
//		EaeTypeObjectif t1 = new EaeTypeObjectif();
//		t1.setLibelle("PROFESSIONNEL");
//		
//		EaeTypeObjectif t2 = new EaeTypeObjectif();
//		t2.setLibelle("INDIVIDUEL");
//		
//		EaeResultat res1 = new EaeResultat();
//		res1.setIdEaeResultat(79);
//		res1.setTypeObjectif(t1);
//		res1.setObjectif("obj1");
//		res1.setResultat("res obj1");
//		EaeCommentaire com1 = new EaeCommentaire();
//		com1.setText("text obj1");
//		res1.setCommentaire(com1);
//		dto.getObjectifsProfessionnels().add(res1);
//		
//		EaeResultat res2 = new EaeResultat();
//		res2.setIdEaeResultat(81);
//		res2.setTypeObjectif(t2);
//		res2.setObjectif("obj2");
//		res2.setResultat("res obj2");
//		dto.getObjectifsIndividuels().add(res2);
//		
//		String expectedResult = "{\"commentaireGeneral\":\"very short comment\",\"idEae\":56789,\"objectifsIndividuels\":[{\"commentaire\":null,\"idEaeResultat\":81,\"objectif\":\"obj2\",\"resultat\":\"res obj2\"}],\"objectifsProfessionnels\":[{\"commentaire\":\"text obj1\",\"idEaeResultat\":79,\"objectif\":\"obj1\",\"resultat\":\"res obj1\"}]}";
//		
//		// When
//		String result = dto.serializeInJSON();
//		
//		// Then
//		assertEquals(expectedResult, result);
//	}
//	
//	@Test
//	public void testDeserializeFromJSON_emptyJson() {
//		
//		// Given
//		String json = "{\"commentaireGeneral\":null,\"idEae\":0,\"objectifsIndividuels\":[],\"objectifsProfessionnels\":[]}";
//		
//		// When
//		EaeResultatsDto dto = new EaeResultatsDto().deserializeFromJSON(json);
//		
//		// Then
//		assertEquals(0, dto.getIdEae());
//		assertNull(dto.getCommentaireGeneral());
//		assertEquals(0, dto.getObjectifsIndividuels().size());
//		assertEquals(0, dto.getObjectifsProfessionnels().size());
//	}
//	
//	@Test
//	public void testDeserializeFromJSON_FilledInJson() {
//		
//		// Given
//		String json = "{\"commentaireGeneral\":\"the comment that has changed\",\"idEae\":9876540,\"objectifsIndividuels\":[{\"commentaire\":\"commentaire2\",\"idEaeResultat\":79,\"objectif\":\"obj2\",\"resultat\":\"filled in resultat2\"}],\"objectifsProfessionnels\":[{\"commentaire\":\"commentaire1\",\"idEaeResultat\":81,\"objectif\":\"obj1\",\"resultat\":\"filled in resultat1\"}]}";
//		
//		// When
//		EaeResultatsDto dto = new EaeResultatsDto().deserializeFromJSON(json);
//		
//		// Then
//		assertEquals(9876540, dto.getIdEae());
//		assertEquals("the comment that has changed", dto.getCommentaireGeneral());
//		assertEquals(1, dto.getObjectifsIndividuels().size());
//		assertEquals(new Integer(79), dto.getObjectifsIndividuels().get(0).getIdEaeResultat());
//		assertEquals("filled in resultat2", dto.getObjectifsIndividuels().get(0).getResultat());
//		assertEquals("commentaire2", dto.getObjectifsIndividuels().get(0).getCommentaire().getText());
//		assertEquals(1, dto.getObjectifsProfessionnels().size());
//		assertEquals(new Integer(81), dto.getObjectifsProfessionnels().get(0).getIdEaeResultat());
//		assertEquals("filled in resultat1", dto.getObjectifsProfessionnels().get(0).getResultat());
//		assertEquals("commentaire1", dto.getObjectifsProfessionnels().get(0).getCommentaire().getText());
//	}
}
