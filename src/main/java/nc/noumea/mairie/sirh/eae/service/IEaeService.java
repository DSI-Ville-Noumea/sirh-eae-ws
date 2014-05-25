package nc.noumea.mairie.sirh.eae.service;

import java.util.List;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.dto.CampagneEaeDto;
import nc.noumea.mairie.sirh.eae.dto.CanFinalizeEaeDto;
import nc.noumea.mairie.sirh.eae.dto.EaeDashboardItemDto;
import nc.noumea.mairie.sirh.eae.dto.EaeEvalueNameDto;
import nc.noumea.mairie.sirh.eae.dto.EaeFinalizationDto;
import nc.noumea.mairie.sirh.eae.dto.EaeListItemDto;
import nc.noumea.mairie.sirh.eae.dto.FinalizationInformationDto;

public interface IEaeService {

	/**
	 * List the ongoing EAEs a user can see
	 * 
	 * @param agentId
	 *            the agentId of the user
	 * @return the list of EAEs
	 */
	List<EaeListItemDto> listEaesByAgentId(int agentId) throws SirhWSConsumerException;

	/**
	 * Initializes an EAE based on the previous year's EAE (if existing)
	 * 
	 * @param eaeToInitialize
	 * @param previousEae
	 * @throws EaeServiceException
	 */
	void initializeEae(Eae eaeToInitialize, Eae previousEae) throws EaeServiceException;

	/**
	 * Starts the EAE by changing its Etat
	 * 
	 * @param eaeToStart
	 * @throws EaeServiceException
	 */
	void startEae(Eae eaeToStart) throws EaeServiceException;

	/**
	 * Resets the EAE by changing its Etat and reinitializing the list of
	 * Evaluateurs
	 * 
	 * @param eaeToReset
	 * @throws EaeServiceException
	 */
	@Deprecated
	void resetEaeEvaluateur(Eae eaeToReset) throws EaeServiceException;

	/**
	 * Returns the information saying whether or not the EAE is ready for
	 * finalization
	 * 
	 * @param eae
	 * @return CanFinalizeEaeDto
	 */
	CanFinalizeEaeDto canFinalizEae(Eae eae);

	/**
	 * Retrieves the necessary information for the final EAE document to be
	 * uploaded in the document repository (today sharepoint)
	 * 
	 * @param eae
	 * @return FinalizationInformationDto
	 */
	FinalizationInformationDto getFinalizationInformation(Eae eae) throws SirhWSConsumerException;

	/**
	 * Proceeds to the finalization of the given Eae (setting its Etat to F) as
	 * well as logging for audit trail purposes
	 * 
	 * @param eae
	 *            to finalize
	 * @param idAgent
	 *            finalizing the Eae
	 * @param dto
	 * @throws EaeServiceException
	 */
	void finalizEae(Eae eae, int idAgent, EaeFinalizationDto dto) throws EaeServiceException;

	/**
	 * Sets the Delegataire of an Eae if existing in SIRH Agents
	 * 
	 * @param eae
	 * @throws EaeServiceException
	 */
	void setDelegataire(Eae eae, int idAgentDelegataire) throws EaeServiceException;

	/**
	 * Retrieves the dashboard of current ongoing EAEs for a SHD, an evaluateur
	 * or a delegataire
	 * 
	 * @param idAgent
	 *            viewing the dashboard
	 * @return a list of Evaluateurs with their respective list of EAEs statuses
	 */
	List<EaeDashboardItemDto> getEaesDashboard(int idAgent) throws SirhWSConsumerException;

	/**
	 * Find the last EAE of a given Agent
	 * 
	 * @param agentId
	 * @return Eae
	 */
	Eae findLastEaeByAgentId(int agentId);

	/**
	 * Find the two last EAEs of a given Agent
	 * 
	 * @param agentId
	 * @return List<Eae>
	 */
	List<Eae> findCurrentAndPreviousEaesByAgentId(int agentId);

	/**
	 * Find a list of EAEs by their Ids
	 * 
	 * @param agentIds
	 *            : the list of Ids
	 * @return the list of EAEs corresponding to the Ids
	 */
	List<Eae> findEaesForDashboardByAgentIds(List<Integer> agentIds, Integer agentId);

	/**
	 * Find a list of Eaes by their evaluated agent ids
	 * 
	 * @param agentIds
	 *            : the list of Ids
	 * @return the list of EAEs corresponding to the Ids
	 */
	List<Eae> findEaesForEaeListByAgentIds(List<Integer> agentIds, Integer agentId);

	/**
	 * Returns an EAE by its technical Id
	 * 
	 * @param idEae
	 * @return the EAE corresponding to the given Id
	 */
	Eae getEae(int idEae);

	/**
	 * Returns an EAE evalue's first and lastnames
	 * 
	 * @param eae
	 * @return EaeEvalueNameDto
	 */
	EaeEvalueNameDto getEvalueName(Eae eae);

	Eae findEaeByAgentAndYear(int idAgent, Integer annee);

	Eae findEae(int idEae);

	void flush();

	void remove(Object obj);

	void clear();

	/**
	 * FOR SIRH-WS
	 * 
	 * @return CampagneEaeDto
	 */
	CampagneEaeDto getEaeCampagneOuverte();

	/**
	 * FOR SIRH-WS
	 * 
	 * @return String avis SHD pour CAP
	 */
	String getAvisSHD(int idEae);

	/*
	 * FOR SIRH-WS
	 */
	Integer compterlistIdEaeByCampagneAndAgent(int idCampagneEae, List<Integer> idAgents, int idAgent);

	/*
	 * FOR SIRH-WS
	 */
	List<String> getEaesGedIdsForAgents(List<Integer> list, int annee);
}
