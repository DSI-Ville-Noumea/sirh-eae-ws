package nc.noumea.mairie.sirh.eae.security;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.ws.SirhWSConsumerException;

public interface IEaeSecurityProvider {

	boolean isAgentAuthorizedToViewEae(int idAgent, Eae eae) throws SirhWSConsumerException;
	boolean isAgentAuthorizedToEditEae(int idAgent, Eae eae);
		
	void checkEaeAndReadRight(int idEae, int idAgent);
	void checkEaeAndWriteRight(int idEae, int idEvaluateur);
}
