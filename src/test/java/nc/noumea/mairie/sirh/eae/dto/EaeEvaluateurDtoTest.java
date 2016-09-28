package nc.noumea.mairie.sirh.eae.dto;

import static org.junit.Assert.*;

import java.util.Date;

import nc.noumea.mairie.sirh.domain.Agent;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluateur;

import org.junit.Test;

public class EaeEvaluateurDtoTest {
	
	@Test
	public void testConstructor() {

		Agent agent = new Agent();
			agent.setNomUsage("nomUsage");
			agent.setPrenomUsage("prenomUsage");
		
		EaeEvaluateur eaeEval = new EaeEvaluateur();
			eaeEval.setDateEntreeCollectivite(new Date());
			eaeEval.setDateEntreeFonction(new Date());
			eaeEval.setDateEntreeService(new Date());
			eaeEval.setFonction("fonction");
			eaeEval.setIdEaeEvaluateur(1);
			eaeEval.setIdAgent(9005138);
			eaeEval.setAgent(agent);
		
		EaeEvaluateurDto result = new EaeEvaluateurDto(eaeEval);
		
		assertEquals(result.getAgent().getNom(), agent.getNomUsage());
		assertEquals(result.getAgent().getPrenom(), agent.getPrenomUsage());
		assertEquals(result.getDateEntreeCollectivite(), eaeEval.getDateEntreeCollectivite());
		assertEquals(result.getDateEntreeFonction(), eaeEval.getDateEntreeFonction());
		assertEquals(result.getDateEntreeService(), eaeEval.getDateEntreeService());
		assertEquals(result.getFonction(), eaeEval.getFonction());
		assertEquals(result.getIdEaeEvaluateur(), eaeEval.getIdEaeEvaluateur());
		assertEquals(result.getIdAgent(), eaeEval.getIdAgent());
	}
	
}
