package nc.noumea.mairie.sirh.eae.service;

public interface IAgentMatriculeConverterService {

	/***
	 * This helpers takes 6 digits a agent IDs and converts it into a new 7 digits agent ID
	 * @param adIdAgent on 6 digits
	 * @return IdAgent on 7 digits
	 */
	int fromADIdAgentToEAEIdAgent(Integer adIdAgent) throws AgentMatriculeConverterServiceException;
	
	/***
	 * This helpers takes 6 digits a agent IDs and tries to convert it into a new 7 digits agent ID
	 * @param adIdAgent on 6 digits
	 * @return IdAgent on 7 digits
	 */
	int tryConvertFromADIdAgentToEAEIdAgent(Integer adIdAgent);
}
