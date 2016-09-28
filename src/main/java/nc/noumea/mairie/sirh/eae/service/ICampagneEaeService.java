package nc.noumea.mairie.sirh.eae.service;

import java.util.List;

import nc.noumea.mairie.sirh.eae.dto.CampagneEaeDto;
import nc.noumea.mairie.sirh.eae.dto.EaeCampagneTaskDto;
import nc.noumea.mairie.sirh.eae.dto.EaeDocumentDto;
import nc.noumea.mairie.sirh.eae.dto.ReturnMessageDto;

public interface ICampagneEaeService {

	List<CampagneEaeDto> getListeCampagneEae();
	
	ReturnMessageDto createOrModifyCampagneEae(CampagneEaeDto campagneEaeDto);
	
	CampagneEaeDto getCampagneEaeAnnePrecedente(Integer anneePrecedente);
	
	EaeDocumentDto getEaeDocumentByIdDocument(Integer idDocument);
	
	ReturnMessageDto deleteEaeDocument(Integer idEaeDocument);
	
	EaeCampagneTaskDto findEaeCampagneTaskByIdCampagne(Integer idEaeCampagne);
	
	ReturnMessageDto createOrModifyEaeCampagneTask(EaeCampagneTaskDto campagneTaskDto);
}
