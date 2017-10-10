package nc.noumea.mairie.sirh.ws;

import java.util.List;

import nc.noumea.mairie.sirh.eae.dto.LightUser;
import nc.noumea.mairie.sirh.exception.DaoException;

public interface IRadiWSConsumer {

	List<LightUser> getListeAgentMairie();

	LightUser retrieveAgentFromLdapFromMatricule(String employeeNumber) throws DaoException;
}
