package nc.noumea.mairie.sirh.eae.security;

import java.util.List;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluateur;
import nc.noumea.mairie.sirh.eae.service.IAgentMatriculeConverterService;
import nc.noumea.mairie.sirh.eae.service.IEaeService;
import nc.noumea.mairie.sirh.eae.web.controller.ForbiddenException;
import nc.noumea.mairie.sirh.eae.web.controller.NotFoundException;
import nc.noumea.mairie.sirh.eae.web.controller.UnavailableException;
import nc.noumea.mairie.sirh.ws.ISirhWsConsumer;
import nc.noumea.mairie.sirh.ws.SirhWSConsumerException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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
	public void checkEaeAndReadRight(int idEae, int idAgent) {
		
		Eae eae = eaeService.findEae(idEae);
		
		if (eae == null)
			throw new NotFoundException();
		
		try {
			if (!isAgentAuthorizedToViewEae(idAgent, eae))
				throw new ForbiddenException(messageSource.getMessage("EAE_CANNOT_READ", new Object[] { idAgent }, null));
		} catch (SirhWSConsumerException e) {
			throw new UnavailableException(e.getMessage());
		}
	}
	
	@Override
	@Transactional(readOnly = true)
	public void checkEaeAndWriteRight(int idEae, int idEvaluateur) {
		
		Eae eae = eaeService.findEae(idEae);
		
		if (eae == null)
			throw new NotFoundException();
		
		if (!isAgentAuthorizedToEditEae(idEvaluateur, eae))
			throw new ForbiddenException(messageSource.getMessage("EAE_CANNOT_WRITE", new Object[] { idEvaluateur }, null));
	}
}
