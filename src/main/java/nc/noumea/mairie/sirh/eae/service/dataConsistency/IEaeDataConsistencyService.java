package nc.noumea.mairie.sirh.eae.service.dataConsistency;

import nc.noumea.mairie.sirh.eae.domain.Eae;

public interface IEaeDataConsistencyService {

	public void checkDataConsistencyForEaeEvaluation(Eae eae) throws EaeDataConsistencyServiceException;
	
}
