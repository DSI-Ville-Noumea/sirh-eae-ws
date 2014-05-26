package nc.noumea.mairie.sirh.eae.security;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.ws.SirhWSConsumerException;

import org.springframework.http.ResponseEntity;

public interface IEaeSecurityProvider {

	boolean isAgentAuthorizedToViewEae(int idAgent, Eae eae) throws SirhWSConsumerException;
	boolean isAgentAuthorizedToEditEae(int idAgent, Eae eae);
		
	ResponseEntity<String> checkEaeAndReadRight(int idEae, int idAgent);
	ResponseEntity<String> checkEaeAndWriteRight(int idEae, int idEvaluateur);
}
