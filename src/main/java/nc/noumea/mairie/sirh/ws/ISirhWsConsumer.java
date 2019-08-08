package nc.noumea.mairie.sirh.ws;

import java.util.List;

import nc.noumea.mairie.sirh.domain.Agent;
import nc.noumea.mairie.sirh.eae.dto.AvancementEaeDto;
import nc.noumea.mairie.sirh.eae.dto.CalculEaeInfosDto;
import nc.noumea.mairie.sirh.eae.dto.agent.AutreAdministrationAgentDto;
import nc.noumea.mairie.sirh.eae.dto.agent.DateAvctDto;
import nc.noumea.mairie.sirh.eae.dto.poste.FichePosteDto;
import nc.noumea.mairie.sirh.eae.dto.poste.SpbhorDto;

public interface ISirhWsConsumer {
	
	FichePosteDto getFichePoste(Integer idAgent) throws SirhWSConsumerException;

	public List<Integer> getListOfSubAgentsForAgentId(int agentId) throws SirhWSConsumerException;

	public List<Integer> getListOfShdAgentsForAgentId(int agentId) throws SirhWSConsumerException;

	AvancementEaeDto getAvancement(Integer idAgent, Integer anneeAvancement, boolean isFonctionnaire)
			throws SirhWSConsumerException;

	AvancementEaeDto getAvancementDetache(Integer idAgent, Integer anneeAvancement) throws SirhWSConsumerException;

	CalculEaeInfosDto getDetailAffectationActiveByAgent(Integer idAgent, Integer anneeFormation)
			throws SirhWSConsumerException;

	List<CalculEaeInfosDto> getListeAffectationsAgentAvecService(Integer idAgent, Integer idService)
			throws SirhWSConsumerException;

	List<CalculEaeInfosDto> getListeAffectationsAgentAvecFP(Integer idAgent, Integer idFichePoste)
			throws SirhWSConsumerException;

	AutreAdministrationAgentDto chercherAutreAdministrationAgentAncienne(Integer idAgent, boolean isFonctionnaire)
			throws SirhWSConsumerException;

	List<AutreAdministrationAgentDto> getListeAutreAdministrationAgent(Integer idAgent) throws SirhWSConsumerException;

	List<SpbhorDto> getListSpbhor() throws SirhWSConsumerException;

	SpbhorDto getSpbhorById(Integer idSpbhor) throws SirhWSConsumerException;

	Agent getAgent(Integer idAgent) throws SirhWSConsumerException;

	DateAvctDto getCalculDateAvct(Integer idAgent) throws SirhWSConsumerException;

	boolean isUtilisateurSirh(Integer idAgent) throws SirhWSConsumerException;
	
	Integer getModeAccesForAgent(Integer idAgent) throws SirhWSConsumerException;
}
