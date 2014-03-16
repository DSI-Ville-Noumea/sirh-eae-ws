package nc.noumea.mairie.sirh.eae.service;

import java.util.List;

import nc.noumea.mairie.sirh.eae.dto.AvancementEaeDto;
import nc.noumea.mairie.sirh.eae.dto.CalculEaeInfosDto;
import nc.noumea.mairie.sirh.eae.dto.CampagneEaeDto;
import nc.noumea.mairie.sirh.eae.dto.agent.AutreAdministrationAgentDto;

public interface ISirhWsConsumer {

	public List<Integer> getListOfSubAgentsForAgentId(int agentId)
			throws SirhWSConsumerException;

	public List<Integer> getListOfShdAgentsForAgentId(int agentId)
			throws SirhWSConsumerException;

	public CampagneEaeDto getCampagneEnCours() throws SirhWSConsumerException;
	
	AvancementEaeDto getAvancement(Integer idAgent, Integer anneeAvancement, boolean isFonctionnaire) throws SirhWSConsumerException;
	
	AvancementEaeDto getAvancementDetache(Integer idAgent, Integer anneeAvancement) throws SirhWSConsumerException;
	
	CalculEaeInfosDto getDetailAffectationActiveByAgent(Integer idAgent, Integer anneeFormation) throws SirhWSConsumerException;
	
	List<CalculEaeInfosDto> getListeAffectationsAgentAvecService(Integer idAgent, String idService) throws SirhWSConsumerException;
	
	List<CalculEaeInfosDto> getListeAffectationsAgentAvecFP(Integer idAgent, Integer idFichePoste) throws SirhWSConsumerException;
	
	AutreAdministrationAgentDto chercherAutreAdministrationAgentAncienne(Integer idAgent, boolean isFonctionnaire) throws SirhWSConsumerException;
	
	List<AutreAdministrationAgentDto> getListeAutreAdministrationAgent(Integer idAgent) throws SirhWSConsumerException;
}
