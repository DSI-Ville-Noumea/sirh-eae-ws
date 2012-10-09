package nc.noumea.mairie.sirh.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AgentTest {

	@Test
	public void testGetDisplayPrenom_prenomUsage() {
		Agent agent = new Agent();
		agent.setPrenom("toto");
		agent.setPrenomUsage("titi");
		
		assertEquals("titi", agent.getDisplayPrenom());
	}
	
	@Test
	public void testGetDisplayPrenom_prenom() {
		Agent agent = new Agent();
		agent.setPrenom("toto");
		
		assertEquals("toto", agent.getDisplayPrenom());
	}
	
	@Test
	public void testGetDisplayNom_nomMarital() {
		Agent agent = new Agent();
		agent.setNomMarital("toto");
		agent.setNomUsage("titi");
		agent.setNomPatronymique("titi");
		
		assertEquals("toto", agent.getDisplayNom());
	}
	
	@Test
	public void testGetDisplayNom_nomUsage() {
		Agent agent = new Agent();
		agent.setNomUsage("titi");
		agent.setNomPatronymique("titi");
		
		assertEquals("titi", agent.getDisplayNom());
	}
	
	@Test
	public void testGetDisplayNom_nomPatronymique() {
		Agent agent = new Agent();
		agent.setNomPatronymique("titi");
		
		assertEquals("titi", agent.getDisplayNom());
	}
}
