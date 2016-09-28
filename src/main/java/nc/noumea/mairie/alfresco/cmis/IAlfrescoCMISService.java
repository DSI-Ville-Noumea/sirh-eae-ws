package nc.noumea.mairie.alfresco.cmis;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeFinalisation;
import nc.noumea.mairie.sirh.eae.dto.EaeFinalizationDto;
import nc.noumea.mairie.sirh.eae.dto.ReturnMessageDto;

public interface IAlfrescoCMISService {

	ReturnMessageDto uploadDocument(Integer idAgent, EaeFinalizationDto eaeFinalizationDto,
			EaeFinalisation eaeFinalisation, Eae eae, ReturnMessageDto returnDto);

}
