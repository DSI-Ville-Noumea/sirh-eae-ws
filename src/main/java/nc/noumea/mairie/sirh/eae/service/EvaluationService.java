package nc.noumea.mairie.sirh.eae.service;

import java.util.ArrayList;
import java.util.List;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluateur;
import nc.noumea.mairie.sirh.eae.domain.EaeFichePoste;
import nc.noumea.mairie.sirh.eae.dto.EaeFichePosteDto;
import nc.noumea.mairie.sirh.eae.dto.EaeResultatsDto;
import nc.noumea.mairie.sirh.eae.dto.identification.EaeIdentificationDto;
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

	@Override
	public List<EaeFichePosteDto> getEaeFichePoste(Eae eae) {
		
		List<EaeFichePosteDto> result = new ArrayList<EaeFichePosteDto>();
		
		for (EaeFichePoste fdp : eae.getEaeFichePostes()) {
			agentService.fillEaeFichePosteWithAgent(fdp);
			result.add(new EaeFichePosteDto(fdp));
		}
		
		return result;
	}

	@Override
	public EaeResultatsDto getEaeResultats(Eae eae) {
		return new EaeResultatsDto(eae);
	}

}
