package nc.noumea.mairie.sirh.eae.service;

import java.util.List;

import nc.noumea.mairie.sirh.eae.dto.EaeAppreciationsDto;
import nc.noumea.mairie.sirh.eae.dto.EaeAutoEvaluationDto;
import nc.noumea.mairie.sirh.eae.dto.EaeEvaluationDto;
import nc.noumea.mairie.sirh.eae.dto.EaeEvolutionDto;
import nc.noumea.mairie.sirh.eae.dto.EaeResultatsDto;
import nc.noumea.mairie.sirh.eae.dto.identification.EaeIdentificationDto;
import nc.noumea.mairie.sirh.eae.dto.planAction.EaePlanActionDto;
import nc.noumea.mairie.sirh.eae.dto.poste.EaeFichePosteDto;

public interface IEvaluationService {

	/**
	 * Returns the information for displaying the EAE identification data
	 * @param eae
	 * @return EaeIdentificationDto
	 */
	EaeIdentificationDto getEaeIdentification(Integer idEae);
	
	/**
	 * Saves the information for EAE identification data
	 * @param eae
	 * @param dto
	 * @throws EvaluationServiceException
	 */
	public void setEaeIdentification(Integer idEae, EaeIdentificationDto dto) throws EvaluationServiceException, EaeServiceException;
	
	/**
	 * Returns the data related to the FichePoste objects linked to the EAE
	 * @param eae
	 * @return List<EaeFichePosteDto>
	 */
	public List<EaeFichePosteDto> getEaeFichePoste(Integer  idEae);
	
	/**
	 * Returns the data for viewing/filling in the results of last year's EAE objectives
	 * @param eae
	 * @return EaeResultatsDto
	 */
	public EaeResultatsDto getEaeResultats(Integer idEae);
	
	/**
	 * Saves the filled in results for last year's EAE objectives
	 * @param eae
	 * @param dto
	 * @throws EvaluationServiceException
	 */
	public void setEaeResultats(Integer idEae, EaeResultatsDto dto) throws EvaluationServiceException, EaeServiceException;
	
	/**
	 * Returns the data for viewing/filling in appreciations in someone's EAE
	 * @param eae
	 * @return EaeAppreciationsDto
	 */
	public EaeAppreciationsDto getEaeAppreciations(Integer idEae);
	
	/**
	 * Saves the filled in appreciations for someone's EAE
	 * @param eae
	 * @param dto
	 */
	public void setEaeAppreciations(Integer idEae, EaeAppreciationsDto dto) throws EaeServiceException;
	
	/**
	 * Returns the data for viewing/filling in the evaluation part of an EAE
	 * @param eae
	 * @return EaeEvaluationDto
	 */
	public EaeEvaluationDto getEaeEvaluation(Integer idEae);
	
	/**
	 * Saves the filled in evaluation for someone's EAE
	 * @param eae
	 * @param dto
	 * @throws EvaluationServiceException
	 */
	public void setEaeEvaluation(Integer idEae, EaeEvaluationDto dto) throws EvaluationServiceException, EaeServiceException;
	
	/**
	 * Returns the data for viewing/filling in the auto evaluation part of an EAE
	 * @param eae
	 * @return EaeAutoEvaluationDto
	 */
	public EaeAutoEvaluationDto getEaeAutoEvaluation(Integer idEae);
	
	/**
	 * Saves the filled in auto evaluation for someone's EAE
	 * @param eae
	 * @param dto
	 * @throws EvaluationServiceException
	 */
	public void setEaeAutoEvaluation(Integer idEae, EaeAutoEvaluationDto dto) throws EaeServiceException;
	
	/**
	 * Returns the data for viewing/filling in the auto evaluation part of an EAE
	 * @param eae
	 * @return EaePlanActionDto
	 */
	public EaePlanActionDto getEaePlanAction(Integer idEae);
	
	/**
	 * Saves the filled in auto evaluation for someone's EAE
	 * @param eae
	 * @param dto
	 * @throws EvaluationServiceException
	 */
	public void setEaePlanAction(Integer idEae, EaePlanActionDto dto) throws EaeServiceException;
	

	/**
	 * Returns the data for viewing/filling in the evolution part of an EAE
	 * @param eae
	 * @return EaeEvolutionDto
	 */
	public EaeEvolutionDto getEaeEvolution(Integer idEae);
	
	/**
	 * Saves the filled in evolution for someone's EAE
	 * @param eae
	 * @param dto
	 * @throws EvaluationServiceException
	 */
	public void setEaeEvolution(Integer idEae, EaeEvolutionDto dto) throws EvaluationServiceException, EaeServiceException;

	EaeAppreciationsDto getEaeAppreciations(Integer idEae, String annee);

}
