package nc.noumea.mairie.sirh.eae.dto.agent;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;

import nc.noumea.mairie.sirh.domain.Agent;

public class AgentEaeDtoTest {

	@Test
	public void testConstructorwithAgent() {
		Date dateNaissance = new Date();

		Agent agent = new Agent();
		agent.setIdAgent(1);
		agent.setNomUsage("nomUsage");
		agent.setNomPatronymique("nom jeune fille");
		agent.setPrenom("prenom");
		agent.setDateNaissance(dateNaissance);

		AgentEaeDto dto = new AgentEaeDto(agent);

		assertEquals(dto.getIdAgent(), agent.getIdAgent());
		assertEquals(dto.getDateNaissance(), dateNaissance);
		assertEquals(dto.getNomUsage(), "nomUsage");
		assertEquals(dto.getNomPatronymique(), "nom jeune fille");
		assertEquals(dto.getPrenom(), "prenom");
	}
}
