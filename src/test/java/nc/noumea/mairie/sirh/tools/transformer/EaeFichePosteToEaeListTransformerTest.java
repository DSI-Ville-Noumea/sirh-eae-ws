package nc.noumea.mairie.sirh.tools.transformer;

import static org.junit.Assert.assertTrue;
import nc.noumea.mairie.sirh.domain.Agent;
import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeFichePoste;

import org.junit.Test;

import flexjson.JSONSerializer;

public class EaeFichePosteToEaeListTransformerTest {

	@Test
	public void testWhenAllFieldsComplete_outputInlineProperties() {
		// Given
		JSONSerializer serializer = new JSONSerializer();
		EaeFichePosteToEaeListTransformer tr = new EaeFichePosteToEaeListTransformer();
		Agent agentShd = new Agent();
		agentShd.setPrenom("titit");
		agentShd.setNomUsage("totot");
		agentShd.setIdAgent(987665);
		Eae eae = new Eae();
		EaeFichePoste fdp = new EaeFichePoste();
		fdp.setDirectionService("direction");
		fdp.setService("svc");
		fdp.setSectionService("section");
		fdp.setAgentShd(agentShd);
		eae.setEaeFichePoste(fdp);

		// When
		String json = serializer.transform(tr, EaeFichePoste.class).serialize(eae);

		// Then
		assertTrue(json.contains(",\"directionService\":\"direction\",\"sectionService\":\"section\",\"service\":\"svc\",\"agentShd\":{\"class\":\"nc.noumea.mairie.sirh.domain.Agent\",\"dateNaissance\":null,\"displayNom\":\"totot\",\"displayPrenom\":\"titit\",\"idAgent\":987665,\"nomMarital\":null,\"nomPatronymique\":null,\"nomUsage\":\"totot\",\"nomatr\":null,\"prenom\":\"titit\",\"prenomUsage\":null},"));
	}
}
