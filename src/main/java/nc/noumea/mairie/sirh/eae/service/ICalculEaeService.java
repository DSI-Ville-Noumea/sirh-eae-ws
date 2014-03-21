package nc.noumea.mairie.sirh.eae.service;

import java.text.ParseException;

public interface ICalculEaeService {
	
	void creerEaeSansAffecte(Integer idCampagneEae, Integer idAgent) throws SirhWSConsumerException, ParseException;

	void creerEaeAffecte(Integer idCampagneEae, Integer idAgent) throws SirhWSConsumerException, ParseException;
	
}
