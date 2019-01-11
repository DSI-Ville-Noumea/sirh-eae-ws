package nc.noumea.mairie.sirh.ws;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.sun.jersey.api.client.ClientResponse;

import nc.noumea.mairie.sirh.eae.dto.LightUser;
import nc.noumea.mairie.sirh.exception.DaoException;

@Service
public class RadiWSConsumer extends BaseWsConsumer implements IRadiWSConsumer {

	@Autowired
	@Qualifier("RADI_WS_Base_URL")
	private String				RADI_WS_Base_URL;

	private static final String	searchUserUrl	= "users";

	private Logger				logger			= LoggerFactory.getLogger(RadiWSConsumer.class);

	@Override
	public List<LightUser> getListeAgentMairie() {

		String url = String.format(RADI_WS_Base_URL + searchUserUrl);
		logger.info("SIIDMAJob : entrée getListeAgentMairie avec URL : " + url);

		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("employeeID", "90*");

		ClientResponse res = createAndFireGetRequest(parameters, url);

		return readResponseAsList(LightUser.class, res, url);
	}

	@Override
	public LightUser retrieveAgentFromLdapFromMatricule(String employeeNumber) throws DaoException {

		String url = String.format(RADI_WS_Base_URL + searchUserUrl);

		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("employeeID", employeeNumber);

		ClientResponse res = createAndFireGetRequest(parameters, url);

		List<LightUser> list = readResponseAsList(LightUser.class, res, url);
		if (list == null || list.size() == 0) {
			throw new DaoException(String.format("Expected 1 user corresponding to this employeeNumber '%s' but found null.", employeeNumber));
		}
		logger.info("Agent found: employeeNumber={}, mail={}, login={}.",
				new Object[] { list.get(0).getEmployeeNumber(), list.get(0).getMail(), list.get(0).getsAMAccountName() });

		return list.size() == 0 ? null : list.get(0);
	}
}
