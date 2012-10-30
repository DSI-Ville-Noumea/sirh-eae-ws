package nc.noumea.mairie.sirh.eae.service;

import java.util.List;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.dto.EaeAppreciationsDto;
import nc.noumea.mairie.sirh.eae.dto.EaeAutoEvaluationDto;
import nc.noumea.mairie.sirh.eae.dto.EaeEvaluationDto;
import nc.noumea.mairie.sirh.eae.dto.EaeFichePosteDto;
import nc.noumea.mairie.sirh.eae.dto.EaeResultatsDto;
import nc.noumea.mairie.sirh.eae.dto.identification.EaeIdentificationDto;

public interface IEvaluationService {

	/**
	 * Returns the information for displaying the EAE identification data
	 * @param eae
	 * @return
	 */
	public EaeIdentificationDto getEaeIdentification(Eae eae);
	
	/**
	 * Saves the information for EAE identification data
	 * @param eae
	 * @param dto
	 * @throws EvaluationServiceException
	 */
	public void setEaeIdentification(Eae eae, EaeIdentificationDto dto) throws EvaluationServiceException;
	
	/**
	 * Returns the data related to the FichePoste objects linked to the EAE
	 * @param eae
	 * @return
	 */
	public List<EaeFichePosteDto> getEaeFichePoste(Eae eae);
	
	/**
	 * Returns the data for viewing/filling in the results of last year's EAE objectives
	 * @param eae
	 * @return
	 */
	public EaeResultatsDto getEaeResultats(Eae eae);
	
	/**
	 * Saves the filled in results for last year's EAE objectives
	 * @param eae
	 * @param dto
	 * @throws EvaluationServiceException
	 */
	public void setEaeResultats(Eae eae, EaeResultatsDto dto) throws EvaluationServiceException;
	
	/**
	 * Returns the data for viewing/filling in appreciations in someone's EAE
	 * @param eae
	 * @return
	 */
	public EaeAppreciationsDto getEaeAppreciations(Eae eae);
	
	/**
	 * Saves the filled in appreciations for someone's EAE
	 * @param eae
	 * @param dto
	 */
	public void setEaeAppreciations(Eae eae, EaeAppreciationsDto dto);
	
	/**
	 * Returns the data for viewing/filling in the evaluation part of an EAE
	 * @param eae
	 * @return
	 */
	public EaeEvaluationDto getEaeEvaluation(Eae eae);
	
	/**
	 * Saves the filled in evaluation for someone's EAE
	 * @param eae
	 * @param dto
	 * @throws EvaluationServiceException
	 */
	public void setEaeEvaluation(Eae eae, EaeEvaluationDto dto) throws EvaluationServiceException;
	
	/**
	 * Returns the data for viewing/filling in the auto evaluation part of an EAE
	 * @param eae
	 * @return
	 */
	public EaeAutoEvaluationDto getEaeAutoEvaluation(Eae eae);
	
	/**
	 * Saves the filled in auto evaluation for someone's EAE
	 * @param eae
	 * @param dto
	 * @throws EvaluationServiceException
	 */
	public void setEaeAutoEvaluation(Eae eae, EaeAutoEvaluationDto dto);
}
