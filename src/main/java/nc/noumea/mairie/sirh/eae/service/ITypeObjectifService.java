package nc.noumea.mairie.sirh.eae.service;

import nc.noumea.mairie.sirh.eae.domain.EaeTypeObjectif;

public interface ITypeObjectifService {

	public EaeTypeObjectif getTypeObjectifForLibelle(String libelle);
}
