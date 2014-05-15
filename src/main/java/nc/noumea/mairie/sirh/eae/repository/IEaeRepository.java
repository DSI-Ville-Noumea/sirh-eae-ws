package nc.noumea.mairie.sirh.eae.repository;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeCampagne;
import nc.noumea.mairie.sirh.eae.domain.EaeCampagneTask;

public interface IEaeRepository {

	EaeCampagne findEaeCampagne(Integer idEaeCampagne);
	
	void persistEntity(Object entity);
	
	void removeEntity(Object entity);
	
	EaeCampagne findEaeCampagneByAnnee(Integer annee);
	
	Eae findEaeAgent(Integer idAgent, Integer idEaeCampagne);
	
	EaeCampagneTask findEaeCampagneTask(Integer idEaeCampagneTask);
}