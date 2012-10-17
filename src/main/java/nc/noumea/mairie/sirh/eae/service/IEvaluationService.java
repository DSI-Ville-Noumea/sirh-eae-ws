package nc.noumea.mairie.sirh.eae.service;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.dto.EaeIdentificationDto;

public interface IEvaluationService {

	public EaeIdentificationDto getEaeIdentification(Eae eae);
}
