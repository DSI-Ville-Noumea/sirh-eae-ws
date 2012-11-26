package nc.noumea.mairie.sirh.eae.web.controller.xml;

import org.springframework.stereotype.Service;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Service
public class DtoSessionRemoverService {

	/**
	 * Hack
	 * 
	 * Issue:
	 * This method is used in order to free the DTO object from any hibernate proxy reference. 
	 * This code is necessary because Detach/Clear methods of the entity manager does not get rid 
	 * of all the proxies created by hibernate when fetching the record from the database (it only detaches the
	 * object from the session, preventing future modifications from being flushed). 
	 * We need to free the DTO from them because the proxies are being triggered by the SOAP XML marshaller 
	 * when creating the response object. This marshaller is executed outside the scope of the transaction 
	 * and therefore no session is attached anymore to the context : an exception is thrown.
	 * 
	 * Solution:
	 * By serializing/deserializing the object, we automatically get rid of the hibernate proxies, making POJOs out 
	 * of our entities.
	 * 
	 * @param dto
	 * @return the freed dto
	 */
	public <T> T removeSessionOf(T dto) {
		if (dto == null)
			return null;
		
		// The exclude members of the serializations are here to ensure that we do not loop inside the Dtos to 
		// serialize several times 
		// 		- Eaes (which is rerefenced in a lot of Dto inner objects)
		//		- EaeEvolution (wich is referenced inside the EaeEvolutionDto.evolutions and Souhaits objetcs
		String json = new JSONSerializer().exclude("*.eae").exclude("*.eaeEvolution").deepSerialize(dto);
		return new JSONDeserializer<T>().deserialize(json);
	}
	
}
