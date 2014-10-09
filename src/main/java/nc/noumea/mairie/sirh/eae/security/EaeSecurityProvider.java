package nc.noumea.mairie.sirh.eae.security;

import java.util.List;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluateur;
import nc.noumea.mairie.sirh.eae.service.IAgentMatriculeConverterService;
import nc.noumea.mairie.sirh.eae.service.IEaeService;
import nc.noumea.mairie.sirh.ws.ISirhWsConsumer;
import nc.noumea.mairie.sirh.ws.SirhWSConsumerException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EaeSecurityProvider implements IEaeSecurityProvider {

	private Logger logger = LoggerFactory.getLogger(EaeSecurityProvider.class);
	
	@Autowired
	private IAgentMatriculeConverterService agentMatriculeConverterService;
	
	@Autowired
	private ISirhWsConsumer sirhWsConsumer;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private IEaeService eaeService;
	
	@Override
	public boolean isAgentAuthorizedToViewEae(int idAgent, Eae eae) throws SirhWSConsumerException {
		
		Integer convertedIdAgent = agentMatriculeConverterService.tryConvertFromADIdAgentToEAEIdAgent(idAgent);
		
		List<Integer> agentIds = sirhWsConsumer.getListOfSubAgentsForAgentId(convertedIdAgent);
		
		Boolean isAuthorized = agentIds.contains(eae.getEaeEvalue().getIdAgent()) || isEvaluateurOrDelegataire(convertedIdAgent, eae);
		
		if (!isAuthorized)
			logger.warn("Agent '{}' tried to view EAE '{}': he was blocked because he doesn't have sufficient rights.", idAgent, eae.getIdEae());
		
		return isAuthorized;
	}

	@Override
	public boolean isAgentAuthorizedToEditEae(int idAgent, Eae eae) {
		
		
		Integer convertedIdAgent = agentMatriculeConverterService.tryConvertFromADIdAgentToEAEIdAgent(idAgent);
		
		Boolean isAuthorized = isEvaluateurOrDelegataire(convertedIdAgent, eae);
		
		if (!isAuthorized)
			logger.warn("Agent '{}' tried to edit EAE '{}':  he was blocked because he doesn't have sufficient rights.", idAgent, eae.getIdEae());
		
		return isAuthorized;
	}

	protected boolean isEvaluateurOrDelegataire(int idAgent, Eae eae) {
		
		if (eae.getIdAgentDelegataire() != null && eae.getIdAgentDelegataire().equals(idAgent))
			return true;
		
		for (EaeEvaluateur eval : eae.getEaeEvaluateurs())
			if (eval.getIdAgent() == idAgent)
				return true;
		
		return false;
	}
	
	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<String> checkEaeAndReadRight(int idEae, int idAgent) {
		
		Eae eae = eaeService.findEae(idEae);
		
		if (eae == null)
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		
		try {
			if (!isAgentAuthorizedToViewEae(idAgent, eae))
				return new ResponseEntity<String>(messageSource.getMessage("EAE_CANNOT_READ", new Object[] { idAgent }, null), HttpStatus.FORBIDDEN);
		} catch (SirhWSConsumerException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
		}
		
		return null;
	}
	
	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<String> checkEaeAndWriteRight(int idEae, int idEvaluateur) {
		
		Eae eae = eaeService.findEae(idEae);
		
		if (eae == null)
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		
		if (!isAgentAuthorizedToEditEae(idEvaluateur, eae))
			return new ResponseEntity<String>(messageSource.getMessage("EAE_CANNOT_WRITE", new Object[] { idEvaluateur }, null), HttpStatus.FORBIDDEN);
		
		return null;
	}
}
