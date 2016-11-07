package nc.noumea.mairie.sirh.eae.service;

import java.text.ParseException;
import java.util.List;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.dto.agent.BirtDto;
import nc.noumea.mairie.sirh.ws.SirhWSConsumerException;

public interface ICalculEaeService {

	void creerEaeSansAffecte(Integer idCampagneEae, Integer idAgent) throws SirhWSConsumerException, ParseException;

	void creerEaeAffecte(Integer idCampagneEae, Integer idAgent) throws SirhWSConsumerException, ParseException;

	void updateEae(Integer idEae) throws SirhWSConsumerException, ParseException;

	void resetEvaluateurFromSIRH(Eae eae, List<BirtDto> evaluateurs) throws SirhWSConsumerException;

}
