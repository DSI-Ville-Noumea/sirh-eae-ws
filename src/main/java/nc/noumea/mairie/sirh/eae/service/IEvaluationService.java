package nc.noumea.mairie.sirh.eae.service;

import java.util.List;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.dto.EaeFichePosteDto;
import nc.noumea.mairie.sirh.eae.dto.EaeResultatsDto;
import nc.noumea.mairie.sirh.eae.dto.identification.EaeIdentificationDto;

public interface IEvaluationService {

	public EaeIdentificationDto getEaeIdentification(Eae eae);
	
	public void setEaeIdentification(Eae eae, EaeIdentificationDto dto) throws EvaluationServiceException;
	
	public List<EaeFichePosteDto> getEaeFichePoste(Eae eae);
	
	public EaeResultatsDto getEaeResultats(Eae eae);
}
