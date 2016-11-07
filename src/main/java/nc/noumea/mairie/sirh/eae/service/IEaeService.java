package nc.noumea.mairie.sirh.eae.service;

import java.util.List;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.dto.CampagneEaeDto;
import nc.noumea.mairie.sirh.eae.dto.CanFinalizeEaeDto;
import nc.noumea.mairie.sirh.eae.dto.EaeDashboardItemDto;
import nc.noumea.mairie.sirh.eae.dto.EaeDto;
import nc.noumea.mairie.sirh.eae.dto.EaeEvalueNameDto;
import nc.noumea.mairie.sirh.eae.dto.EaeFinalizationDto;
import nc.noumea.mairie.sirh.eae.dto.EaeListItemDto;
import nc.noumea.mairie.sirh.eae.dto.FinalizationInformationDto;
import nc.noumea.mairie.sirh.eae.dto.FormRehercheGestionEae;
import nc.noumea.mairie.sirh.eae.dto.ReturnMessageDto;
import nc.noumea.mairie.sirh.eae.dto.identification.ValeurListeDto;
import nc.noumea.mairie.sirh.ws.SirhWSConsumerException;

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
	Eae startEae(Integer idEaeToStart, boolean isSirh) throws EaeServiceException;

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
	CanFinalizeEaeDto canFinalizEae(Integer idEae);

	/**
	 * Retrieves the necessary information for the final EAE document to be
	 * uploaded in the document repository (today sharepoint)
	 * 
	 * @param eae
	 * @return FinalizationInformationDto
	 */
	FinalizationInformationDto getFinalizationInformation(Integer idEae) throws SirhWSConsumerException;

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
	ReturnMessageDto finalizeEae(Integer idEae, int idAgent, EaeFinalizationDto dto);

	/**
	 * Sets the Delegataire of an Eae if existing in SIRH Agents
	 * 
	 * @param eae
	 * @throws EaeServiceException
	 */
	void setDelegataire(Integer idEae, int idAgentDelegataire) throws EaeServiceException;

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
	 * Returns an EAE evalue's first and lastnames
	 * 
	 * @param eae
	 * @return EaeEvalueNameDto
	 */
	EaeEvalueNameDto getEvalueName(Integer idEae);

	Eae findEaeByAgentAndYear(int idAgent, Integer annee);

	/**
	 * Returns an EAE by its technical Id
	 * 
	 * @param idEae
	 * @return the EAE corresponding to the given Id
	 */
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

	/**
	 * for SIRH
	 * 
	 * @param agentId
	 * @return
	 */
	List<EaeDto> findEaesByIdAgentOnly(Integer agentId);

	Integer chercherEaeNumIncrement();

	List<ValeurListeDto> getListeTypeDeveloppement();

	EaeDto findEaeDto(Integer idEae);

	void setEae(EaeDto eaeDto) throws EaeServiceException, SirhWSConsumerException;

	List<EaeDto> getListeEaeDto(FormRehercheGestionEae form) throws EaeServiceException;

	String getLastDocumentEaeFinalise(Integer idEae);

	/**
	 * for KiosqueRH : page accueil nombre EAE a realiser
	 * 
	 * @param agentId
	 *            int
	 * @return Integer
	 * @throws SirhWSConsumerException
	 */
	Integer countListEaesByAgentId(int agentId) throws SirhWSConsumerException;

	List<EaeFinalizationDto> listEeaControleByAgent(Integer convertedId);

	/**
	 * Mise a jour du CAP de l EAE
	 * 
	 * @param idEae
	 *            Integer
	 * @param cap
	 *            boolean
	 * @return ReturnMessageDto
	 */
	ReturnMessageDto updateCapEae(Integer idEae, boolean cap);

	/**
	 * Retrieves the dashboard of current ongoing EAEs for DRH
	 * 
	 * @param anneeCampagne
	 *            Integer Annee de la campagne a retourner
	 * @return a list of Evaluateurs with their respective list of EAEs statuses
	 */
	List<EaeDashboardItemDto> getEaesDashboardForSIRH(Integer anneeCampagne) throws SirhWSConsumerException;
}
