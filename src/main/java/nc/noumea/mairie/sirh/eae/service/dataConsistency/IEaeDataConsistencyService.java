package nc.noumea.mairie.sirh.eae.service.dataConsistency;

import nc.noumea.mairie.sirh.eae.domain.Eae;

public interface IEaeDataConsistencyService {

	public void checkDataConsistencyForEaeIdentification(Eae eae) throws EaeDataConsistencyServiceException;
	public void checkDataConsistencyForEaeEvaluation(Eae eae) throws EaeDataConsistencyServiceException;
	public void checkDataConsistencyForEaeEvolution(Eae eae) throws EaeDataConsistencyServiceException;
	
}
