package nc.noumea.mairie.sirh.eae.service;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluateur;
import nc.noumea.mairie.sirh.eae.dto.EaeIdentificationDto;
import nc.noumea.mairie.sirh.service.IAgentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EvaluationService implements IEvaluationService {

	@Autowired
	private IAgentService agentService;
	
	@Override
	public EaeIdentificationDto getEaeIdentification(Eae eae) {
		
		for (EaeEvaluateur eval : eae.getEaeEvaluateurs()) {
			agentService.fillEaeEvaluateurWithAgent(eval);
		}
		
		agentService.fillEaeEvalueWithAgent(eae.getEaeEvalue());
		
		EaeIdentificationDto result = new EaeIdentificationDto(eae);
		
		return result;
	}

	@Override
	public void setEaeIdentification(Eae eae, EaeIdentificationDto dto) throws EvaluationServiceException {

		eae.setDateEntretien(dto.getDateEntretien());

	}

}
