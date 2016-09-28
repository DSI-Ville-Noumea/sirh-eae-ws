package nc.noumea.mairie.sirh.eae.dto.agent;

import static org.junit.Assert.assertEquals;
import nc.noumea.mairie.sirh.domain.Agent;

import org.junit.Test;

public class AgentDtoTest {

	@Test
	public void ctor_withagent() {
		
		// Given
		Agent ag = new Agent();
		ag.setNomUsage("RAYNAUD");
		ag.setPrenomUsage("Nicolas");
		ag.setIdAgent(9006765);
		
		// When
		AgentDto result = new AgentDto(ag);
		
		// Then
		assertEquals("RAYNAUD", result.getNom());
		assertEquals("Nicolas", result.getPrenom());
		assertEquals(9006765, (int)result.getIdAgent());
	}
}
