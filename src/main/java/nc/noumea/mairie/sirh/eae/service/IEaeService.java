package nc.noumea.mairie.sirh.eae.service;

import java.util.List;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.dto.EaeDashboardItemDto;
import nc.noumea.mairie.sirh.eae.dto.EaeListItemDto;

public interface IEaeService {

	/**
	 * List the ongoing EAEs a user can see
	 * @param the agentId of the user
	 * @return the list of EAEs
	 */
	List<EaeListItemDto> listEaesByAgentId(int agentId);
	
	/**
	 * Initializes an EAE based on the previous year's EAE (if existing)
	 * @param eaeToInitialize
	 * @param previousEae
	 * @throws EaeServiceException
	 */
	void initializeEae(Eae eaeToInitialize, Eae previousEae) throws EaeServiceException;
	
	/**
	 * Starts the EAE by changing its Etat
	 * @param eaeToStart
	 * @throws EaeServiceException
	 */
	void startEae(Eae eaeToStart) throws EaeServiceException;
	
	/**
	 * Resets the EAE by changing its Etat and reinitializing the list of Evaluateurs
	 * @param eaeToReset
	 * @throws EaeServiceException
	 */
	void resetEaeEvaluateur(Eae eaeToReset) throws EaeServiceException;
	
	/**
	 * Sets the Delegataire of an Eae if existing in SIRH Agents
	 * @param eae
	 * @throws EaeServiceException
	 */
	void setDelegataire(Eae eae, int idAgentDelegataire) throws EaeServiceException;
	
	/**
	 * Retrieves the dashboard of current ongoing EAEs for a SHD, an evaluateur or a delegataire
	 * @param idAgent viewing the dashboard
	 * @return a list of Evaluateurs with their respective list of EAEs statuses
	 */
	List<EaeDashboardItemDto> getEaesDashboard(int idAgent);
	
	/**
	 * Find the last EAE of a given Agent
	 * @param agentId
	 * @return
	 */
	Eae findLastEaeByAgentId(int agentId);
	
	/**
	 * Find the two last EAEs of a given Agent
	 * @param agentId
	 * @return
	 */
	List<Eae> findCurrentAndPreviousEaesByAgentId(int agentId);
	
	/**
	 * Find a list of EAEs by their Ids
	 * @param eaeIds : the list of Ids
	 * @return the list of EAEs corresponding to the Ids
	 */
	List<Eae> findEaesByIds(List<Integer> eaeIds);
}
