package nc.noumea.mairie.sirh.eae.security;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.service.SirhWSConsumerException;

import org.springframework.http.ResponseEntity;

public interface IEaeSecurityProvider {

	boolean isAgentAuthorizedToViewEae(int idAgent, Eae eae) throws SirhWSConsumerException;
	boolean isAgentAuthorizedToEditEae(int idAgent, Eae eae);
	
	ResponseEntity<String> checkEaeReadRight(int idEae, int idAgent);
	ResponseEntity<String> checkEaeWriteRight(int idEae, int idEvaluateur);
}
